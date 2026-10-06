# InstaGuard User App — Complete Guard Booking, Payment & Status Flow

> The single most important document for the Flutter rewrite. Everything here is reverse-engineered
> from `ProviderListActivity.java` (~4,494 lines), `MainJobProgressActivity.java` (~9,525 lines),
> `ScheduleTripFragment.java`, `GuardUniqueIdFragment.java` and the booking/payment models.
> For raw endpoint shapes see [`USER_API_DOCUMENTATION.md`](./USER_API_DOCUMENTATION.md).

---

## 0. Mental model

- A "booking" = a **service request** for one or more **guards** at a location for a number of **hours**.
- Two kinds: **Book Now** (`normaltrip`, `mode=ridenow`) and **Book Later / scheduled** (`scheduletrip`, same `ridenow` call + `trip_type=2` + start/end date-time).
- **The guard is NOT chosen by id in the create call.** The app sends a `reference_id` list (guard unique IDs, possibly empty) + `customer_id`; the **server matches** guards and the **guard app accepts**. The user app learns the result from **Firebase** (`users/<userId>/trips/accept_status`), not from the REST response.
- **Pricing is entirely server-side** (`FareEstimate`); the client only echoes returned numbers into the booking.
- **State after acceptance lives in Firebase** `booking_trips/<bookingId>` — `MainJobProgressActivity` is a live view over that node.

---

## 1. Pre-booking state (set on the map screen)

All inputs are collected in `MainActivity` (map) / `SubCategoryFragment` / `ScheduleTripFragment` and **persisted to SharedPreferences store `MY_PREFS_REQUEST_STATE`** (keys prefixed `B_`). There are **no Intent extras** carrying booking data — `ProviderListActivity` reads everything from prefs in `callSharedPreferences()`.

| Pref key | Meaning | Source |
|----------|---------|--------|
| `B_CATEGORY_ID` | selected category (guard type) | Categories API |
| `B_SUB_CATEGORY_ID` | selected service | Service API |
| `B_SUB_CATEGORY_PRICE` | service price | Service API |
| `B_PICKUP`, `B_PICKUP_LAT`, `B_PICKUP_LNG` | pickup address/coords | map center or Places autocomplete |
| `B_DROP`, `B_DROP_LAT`, `B_DROP_LNG` | `"NoDrop"`, `"0.0"`, `"0.0"` (guards have no drop) | hardcoded |
| `B_TRIP_TYPE` | `"normaltrip"` or `"scheduletrip"` | Book Now / Book Later |
| `B_CATEGORY_GUARDS_COUNT` | guard count (**≥2 required for Book Now**) | input (`edtNoOfGuards`) |
| `B_CATEGORY_TELL_US_EMERGENCY` | emergency/instruction note | input |
| `B_PICKUP_DATE(_END)`, `B_PICKUP_TIME(_END)` (+`_LABEL`) | scheduled date/time | date/time picker |
| `B_SCH_TRIP_DRIVERID` | CSV of available guard ids | `availabilityCheckService` result |

---

## 2. Guard discovery inside ProviderListActivity

1. Read prefs (category, service, pickup, trip type, guard count, radius).
2. **Radius:** `Distance` = `SP_COMMON_DISTANCE_LIMIT` (km, from `appsetting.distance_limit`); falls back to **5 km** (`Utils.GEOQUERY_RADIUS`) when limit is null/`"0"`.
3. **Book Now (`normaltrip`):** GeoFire query
   ```
   GeoFire(FirebaseDatabase.ref("drivers_location"))
     .queryAtLocation(GeoLocation(pickupLat, pickupLng), Distance)
   ```
   For each matched key (= guard/driver id), read `providers/<id>` and **include the guard only if**:
   - `status == "1"` (guard online), AND
   - `trips/service_status ∈ {"0","8","9"}` (free/available), AND
   - `category` CSV contains the selected `subCategoryId`.
   Distance label = great-circle km (`DistanceCalculator`), list sorted ascending by distance.
4. **Book Later (`scheduletrip`):** GeoFire is NOT used. `B_SCH_TRIP_DRIVERID` (CSV from `availabilityCheckService`) is split and each guard loaded from Firebase (`drivers_location/<id>/l` for position).
5. Guard card fields come from `providers/<id>` → `ProviderListData` (`id, fname, lname, propic, email, mobile, status, rating, serviceDesc, companyName, providerType, miles`).

**Availability check (scheduled only), `ScheduleTripFragment`:**
```
POST availabilityCheckService  (Bearer + Accept)
  day=<selectedDay> hour=<hourOfDay+1> service=<subCategoryId>
  pick_lat=<lat> pick_lon=<lng>
→ message "available Drivers", result = CSV of driver ids  → saved to B_SCH_TRIP_DRIVERID
```

**Book a specific guard by reference id, `GuardUniqueIdFragment`:**
```
POST driverReferenceIdValidate  (no auth, HashMap<String,String>)
  reference_id=<entered id>
→ message "success", result = [ids]  → appended to MainActivity.uniqueIdList
```
Validation: non-empty, total ≤ guard count, no duplicates. On submit the list is JSON-serialized to prefs (`"shared preferences"`/`"uniqueIdList"`) → becomes `sendUniqueIdList` → sent as `reference_id` in the create call.

---

## 3. Step-by-step booking (as implemented)

> This replaces the generic "assumed" steps with the actual code path.

1. **Select guard** (tap card / photo / Book Now) → `strDriverId = map.get("id")` captured. (Used for scheduled/notify/Firebase; **not** in the immediate create body.)
2. **Fare estimate** (`callFareEstimate`):
   ```
   POST FareEstimate  (empty auth)
     service_id=<subCategoryId> customer_id=<userId> total_agent=<guardCount>
     [type=ridelater start_date_time=<..> end_date_time=<..>]   // scheduled only
     promocode=<code | "">
   → FareEstimateData { sub_total, total_hrs, price_per_hr, price_per_guard,
       total_guard_amount, before_vat_amount, overall_total, promo_*,
       wallet_status, wallet_amount, subscription_status, subscription_percentage, subscription_amount }
   ```
   Stores `wallet_status`→`SP_COMMON_WALLET_STATUS`, `wallet_amount`→`SP_USER_WALLET_AMT`, `subscription_status`→`SP_COMMON_SUBSCRIPTION_STATUS`.
3. **App settings** (`callAppSettings`) reloaded → Stripe publishable key, PayPal config (`SP_COMMON_ADMIN_PAYPAL_STATUS`), VAT %, wallet status. Booking-details + payment dialog shown.
4. **(Optional) promo code** (`checkPromoRequest`) — on `"Valid Promocode"`, re-run fare estimate with the code so the discount is applied server-side.
5. **Select payment method** via `PaymentAdapter` (radio). Availability: card always on; Google Pay if `isReadyGPay`; PayPal if `SP_COMMON_ADMIN_PAYPAL_STATUS != "0"`; wallet if `SP_COMMON_WALLET_STATUS`.
6. **Validate:** a payment option must be selected; for card/paypal/gpay `overall_total` must be non-null and `!= "0"`.
7. **Run payment pre-step** (card→Stripe PaymentIntent/3DS; paypal→create order; gpay→GooglePayLauncher; wallet/cash→none) to obtain a token/order id.
8. **Create booking** (`CallRequestBooking`) → `requestService` (see §4). Request map also saved to prefs `"InstaGuard"+userId` for retry.
9. **On success** (`"Request saved successfully."`):
   - read `booking_id` from `service_request.booking_id`;
   - write Firebase accept_status `"1"`; show `dialogRequesting` + countdown + ripple;
   - call `customerTimeout {booking_id, type:"app"}`.
10. **Listen** Firebase `users/<userId>/trips/accept_status`:
    - `"1"` → still requesting;
    - `"yes"` → accepted → success dialog → **View Ongoing Jobs** (`FRAGMENT_ONGOINGJOBS`);
    - `"2"` → rejected/failed;
    - `"3"` → reset/dismiss.
11. From Ongoing jobs, tapping the job opens **`MainJobProgressActivity`** (`booking_id`, `from="ongoing"`, `customer_location`). No direct activity launch from the create call.

---

## 4. `requestService` create body — exact fields

```
// Book Now (immediate)
mode=ridenow
reference_id=<CSV of guard unique ids | "">
category=<B_CATEGORY_ID>
customer_id=<SP_USERID>
service_id=<B_SUB_CATEGORY_ID>
special_instruction=<comment>
customer_lat=<pickup lat>   customer_lon=<pickup lng>   customer_location=<address>
drop_location=NoDrop  drop_lat=0  drop_lon=0
payment_type=<wallet | stripe | stripe_gpay | paypal>
stripe_token=<token/orderId>        // omitted when wallet
language=en
total_hrs=<FareEstimate.total_hrs>
price_per_hr=<FareEstimate.price_per_hr>
vat_amount=<FareEstimate.before_vat_amount>
total_amount=<FareEstimate.overall_total>
emergency_msg=<B_CATEGORY_TELL_US_EMERGENCY>
total_agent=<guard count>
subscription_status=<1|0>  discount_amount=<subscription_amount|0>  discount_percentage=<subscription_percentage|0>
promocode=<code | "">

// Book Later adds:
start_date, start_time, end_date, end_time, start_date_time, end_date_time, trip_type=2
```
**Auth:** none (identity via `customer_id`). **Response:** `SendRequestModel` (`success`, `message`, `service_request.booking_id`, `request_status`, `service_status`).

### Payment-mode string values
| UI radio `selectedId` | Method | `payment_type` sent | `stripe_token` |
|---|---|---|---|
| 0 | Wallet | `wallet` | `"use_wallet"` (omitted) |
| 2 | Card (Stripe) | `stripe` | Stripe paymentId |
| 4 | Cash | (unhandled in current switch) | — |
| 6 | PayPal | `paypal` | PayPal order id |
| 8 | Google Pay | `stripe_gpay` | Stripe paymentId |

> The scheduled `scheduledBooking`/`doScheduleRequesting` method uses `payment_type="card"` and a `stripe_token`, but **that method is commented out** — the active scheduled flow is `requestService` with `trip_type=2`.

---

## 5. Booking API sequence

```text
(discovery)                 (price)            (pre-pay)             (create)           (arm)
GeoFire drivers_location → FareEstimate  → [Stripe/PayPal/GPay] → requestService  → customerTimeout
                           (+ checkPromoCode, re-FareEstimate)     (mode=ridenow)         │
                                                                                          ▼
                                                               Firebase users/<id>/trips/accept_status
                                                                  "yes" → customerongoing → MainJobProgressActivity
                                                                                          │
                   (live)                          (extend)                 (settle)       ▼
   Firebase booking_trips/<id>  ── drivers_location/<id> + direction/<id> (map/ETA)
     service_status state machine:
        5.1 → Paycard        5.3 → createOrder+Paypaypal     10 → customerTripPayment
        6   → requestService(mode=payment_success)  → 7 completed → CustomerFeedbackStore
        extend: ExtraCalFare → UpdateJob
```

---

## 6. Booking Data Model (key fields)

Full models in [`USER_DATA_MODELS.md`](./USER_DATA_MODELS.md). The booking-critical identifiers:

| Concept | Field | Type | Where |
|---------|-------|------|-------|
| Booking id | `booking_id` | Integer | `ServiceRequest`, `OnGoingJobsData`, Firebase `booking_trips/<id>` |
| Job number | `job_number` | String | `ServiceRequest`, history |
| User id | `customer_id` | String/Int | request body (`SP_USERID`) |
| Guard id | `driver_id` | String/Int | settle/cancel/rating, Firebase `providers/<id>` |
| Service | `service_id` | String | request (= `B_SUB_CATEGORY_ID`) |
| Category | `category` / `category_id` | String | request (= `B_CATEGORY_ID`) |
| Pickup | `customer_lat/customer_lon/customer_location` | String | request |
| Guard count | `total_agent` | String | request |
| Hours | `total_hrs` | String/Object | FareEstimate → request |
| Price/hr | `price_per_hr` | String/Object | FareEstimate → request |
| VAT | `vat_amount` (= before_vat_amount) | String/Object | FareEstimate → request |
| Total charged | `total_amount` (= overall_total) | String/Object | FareEstimate → request |
| Payment mode | `payment_type` | String | request (`wallet/stripe/stripe_gpay/paypal`) |
| Payment token | `stripe_token` | String | request |
| Promo | `promocode` | String | request |
| Booking status | `service_status` | Integer | Firebase + history models |
| Payment status (card/paypal/cash) | `card_status`/`paypal_status`/`cash_status` | Firebase | `booking_trips/<id>` |
| Balance due | `balance_amount` | Firebase | status 10 |
| Currency | `currency` / `currency_symbol` | String | `UserData`, prefs |

---

## 7. Booking Status Lifecycle  ★

There are TWO status spaces: the **Firebase `service_status`** that drives the live job screen, and the **history API `service_status`** integers shown in lists. Both are documented below (they overlap but are consumed in different screens).

### 7a. Live job — Firebase `booking_trips/<bookingId>/service_status` (`MainJobProgressActivity`)

| Value | Meaning | SP_MAP_TRIPSTATE | User sees / can do |
|-------|---------|------------------|--------------------|
| `"0"` | Requested / searching | — | (banner, no action) |
| `"1"` | Pending | — | (no action) |
| `"2"` | **Guard accepted** | `Accepttrip` | Timeline step 1; live track + polyline; **Cancel** visible |
| `"3"` | **Guard on the way** | `ArriveTrips` | Timeline step 2; reads `start_time`; **Cancel** visible |
| `"4"` | **Guard arrived** | `ArriveTrip` | Timeline step 3; reads `reach_time`; **Cancel** visible |
| `"5"` | **Service begin (job started)** | `Starttrip` | reads `price_per_agent/price_per_day/total_agent/total_days`; **Extend Hours** available; cancel hidden |
| `"5.1"` | Begin + **card payment requested** | `Starttrip` | `showCardPayment("card")`; reads fare + `card_status` (default "4") |
| `"5.2"` | Cash payment (→ falls to 6) | `Endtrip` | — |
| `"5.3"` | Begin + **PayPal requested** | `Starttrip` | `showCardPayment("paypal")`; reads `paypal_status` |
| `"6"` | **Job ended / trip summary** | `Endtrip` | `showTripSummary`; **Mark as Completed** → `requestService mode=payment_success` |
| `"7"` | **Completed** | `Normalstate`→`Endtrip` | summary; **Go to Home**; **rating** shown |
| `"8"` | **User cancelled** | `Normalstate` | toast; clear `SP_USER_CURRENT_BOOKING_ID`; → MainActivity |
| `"9"` | **Guard cancelled** | `Normalstate` | toast provider-cancel; → MainActivity |
| `"10"` | **Balance amount due (extra)** | `Normalstate` | reads `balance_amount`; `showBalanceStripePayment` → `customerTripPayment` |
| default | — | `Normalstate` | clears current booking id |

Supporting Firebase fields: `request_status` (0/3 pending, 1 confirm, 2 reject — dialog code commented out); `card_status`/`paypal_status`/`cash_status` (0 none, 1 paid, 2 rejected, default "4"); `rating_status` (0 pending, 1 done); per-guard `trip_status` in `booking_trips/<id>/drivers/*` (2 accept, 3 start, 4 arrived, 5 begin, 6 end).

**Who changes it:** the **guard app / backend** drives 2→3→4→5→6; the **user app** writes 8 (cancel) and triggers 6→7 via Mark-Completed + `payment_success`.

### 7b. History lists — `service_status` integers (ongoing/upcoming/past adapters)

| Value | Meaning | Where / action |
|-------|---------|----------------|
| `1` | Requesting | Upcoming tab — Cancel visible |
| `2` | Accepted by guard | Upcoming/ongoing |
| `3` | No guard available | Upcoming |
| `6` | Completed — awaiting customer acceptance | Past (toast, no detail) |
| `7` | Finished | Past — opens detail dialog; rating |
| `8` | Cancelled by user | Past |
| `9` | Cancelled by guard | Past |
| `13` | Upcoming/scheduled | Ongoing tab — **Cancel** (→ `scheduledcancelJobs`) |
| `14` | Scheduled cancel by user | Past |
| `15` | Scheduled cancel by guard | Past |
| `21` | Timeout | Ongoing tab — timeout dialog → cancel (`cancelJobs`) |

---

## 8. Payment (detailed)

### Providers & SDKs
- **Stripe** (`stripe-android:17.1.2`) — PaymentLauncher, 3DS2, PaymentConfiguration. Publishable key = `SP_COMMON_STRIPE_PUBLISH_KEY` (from `appsetting`), DEBUG fallback `pk_test_...`.
- **Google Pay** via `GooglePayLauncher` (Stripe) — `Config(Production, "GB", "Insta-Guard", ...)`, currency `GBP`.
- **PayPal** native checkout (`com.paypal.android:*:1.1.0`) — `CoreConfig(clientId, env)`, env LIVE if `PAYPAL_TYPE ∈ {"1","1.0"}`, return URL `com.instaguard.user://paypalpay`.
- **Wallet** — Firebase balance; no card step.
- **Cash** — no card API; guard confirms via Firebase `cash_status`.

<a name="payment"></a>
### Which call runs, by payment type
| Payment | At booking (create) | At job (status) | Settle |
|---------|--------------------|-----------------|--------|
| **Wallet** | `payment_type=wallet`, no token | — | `requestService mode=payment_success` (+ `customerTripPayment` if balance due, status 10) |
| **Card (Stripe)** | `StripePayment3DApp` → confirm 3DS → `payment_type=stripe`, `stripe_token=paymentId` | status 5.1 → `Paycard` | `requestService mode=payment_success` |
| **Google Pay** | GooglePayLauncher → `payment_type=stripe_gpay` | as card | `requestService mode=payment_success` |
| **PayPal** | `createOrder` → native checkout → `payment_type=paypal`, `stripe_token=orderId` | status 5.3 → `createOrder` + `Paypaypal` | `requestService mode=payment_success` |
| **Cash** | (no token) | status 5.2 → Firebase `cash_status` | `requestService mode=payment_success` |

### Stripe one-time intent (status 5.1 / booking)
```
POST StripePayment3DApp  (auth "token")
  customer_id=<userId> amount=<total_amount> save_card=<0|1>
→ { client_secret, result.id }
→ SDK confirm(ConfirmPaymentIntentParams.createWithPaymentMethodCreateParams(params, client_secret))
→ onPaymentResult → Paycard {booking_id, amount, card_number, strip_token} → Firebase card_status=1
```

### Stripe save-card / 3DS URL
```
POST StripeSaveCardPayment3DApp  (auth "token")
  customer_id=<userId> amount=<price> type="trip"
→ { client_secret, payment_intent_id, action_url, action_url_status, wallet_status, result.id/payment_method }
  if action_url_status=="1" → load action_url (decoded) in WebView for 3DS
  else → GooglePay presentForSetupIntent(client_secret, "GBP")
```

### PayPal
```
POST createOrder {booking_id, type="app_end", amount} → {id}  ("Order Created")
→ PayPalNativeCheckoutRequest(id, email) → startCheckout
→ POST Paypaypal {booking_id, amount, payment_id} → "Money debited successfully." → Firebase paypal_status=1
```

### Balance due (status 10)
```
POST customerTripPayment {driver_id, customer_id, transaction_token, booking_id,
     balance_amount, total_amount, card_number}
→ "Request status updated successfully." → update Firebase users/<id>/balance_amount → rating dialog
```

---

## 9. Extend hours

```
1) POST ExtraCalFare (empty auth) {customer_id, booking_id, hrs} → new fare fields
2) user picks payment → (Stripe/PayPal/GPay pre-step as above)
3) POST UpdateJob (no auth) {booking_id, extended_hrs, extended_vat, extended_amount,
      extended_status="1", payment_type, stripe_token}
   → "Request saved successfully."
```

---

## 10. Cancellation

| Scenario | Endpoint | Body | Success |
|----------|----------|------|---------|
| Active job (status 2/3/4) | `cancelJobs` | `booking_id, reason="", mode="customer", language` | `"Cancelled successfully."` → MainActivity |
| Scheduled/upcoming (status 13) | `scheduledcancelJobs` | `booking_id, reason, mode="customer", language` | `"Cancelled successfully."` |
| Pending request (Upcoming tab) | `requestService` | `customer_booking_id, mode="customer_cancel"` | `"Cancelled successfully."` / `"Customer Cancel successfully."` |
| Timeout (status 21) | `cancelJobs` | as active | — |

- Cancel is only offered in statuses 2/3/4 on the live screen (button hidden from 5+).
- No reason UI on the active path (`reason=""`); scheduled path may pass `"Job Cancel"`.
- **Refund/charge is server-side.** Sub-category carries `user_cancellation_charge` / `user_cancellation_time`, but the applied rule is not computed client-side — **confirm with backend**.

---

## 11. Rating & review

```
POST CustomerFeedbackStore  (Bearer + Accept)
  from_id=<userId> job_id=<booking_id> to_id=<driver_id> rating=<stars> status="0" feedback=<comment>
→ "Feedback received" → Firebase rating_status=1
```
- Shown after completion (status 7) and in a post-payment dialog; also from Past jobs detail.
- Group bookings: per-guard rating via `TripFirebaseGuardListRatingAdapter`.
- ⚠️ The Past-jobs rating path sends a hardcoded `"Bearer 123"` header (bug).

---

## 12. Chat & call (Twilio)

- **Chat:** `chatAccessToken?device=<userId>&identity=customer<driverId>InstaGuard` → Twilio `ConversationsClient`; channel `Channel:<userId>,<driverId>,<bookingId>`. Sending a message also calls `message_notify {id=driverId, type="driver", message, booking_id, mode="chat"}` (raw token header) to push-notify the guard.
- **Call:** `accesstoken?device=android_rider&identity=<identity>` → Twilio Voice `registerForCallInvites()`. Incoming calls arrive via FCM `twi_*` data → `MainJobProgressActivity` with `ACTION_INCOMING_CALL`.

---

## 13. Gotchas for the Flutter rewrite (booking)

1. **Acceptance is Firebase-driven**, not REST. Implement a Firebase listener on `users/<id>/trips/accept_status`.
2. **Create call has no `driver_id`** and **no auth header**; targeting via `reference_id` + server match.
3. **Two status spaces** (live Firebase `service_status` with `5.1/5.3/10` sub-states vs history integers with `13/14/15/21`). Model both.
4. **Echo fare values**; never recompute price client-side.
5. **Payment is interleaved** with status and multiple settle endpoints — follow the per-type table in §8 exactly.
6. **`scheduledBooking` is dead** — scheduled = `requestService` + `trip_type=2`.
7. **Live polyline is from Firebase `direction/<driverId>`**, ETA is local Haversine — the Google Directions endpoint is not called.
8. **All booking input flows via SharedPreferences** in the Android app — in Flutter pass it explicitly via navigation arguments / a booking-state Provider instead.
9. Confirm **cancellation charge, settlement ordering, and full Firebase `booking_trips` schema** with backend (see UNKNOWN section of the main doc).
