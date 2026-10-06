
# InstaGuard User App — Complete Documentation

> **Source of truth for rebuilding the User application in Flutter.**
> Reverse-engineered directly from the Android native (Java) source at
> `app/src/main/java/com/instaguard/user/`. Nothing here is guessed — every API,
> field key, status value, storage key and navigation edge was read from the actual code.
>
> **Companion files (read together):**
> - [`USER_API_DOCUMENTATION.md`](./USER_API_DOCUMENTATION.md) — every endpoint, request/response, master list, diagrams
> - [`USER_BOOKING_FLOW.md`](./USER_BOOKING_FLOW.md) — the full booking + payment + status lifecycle
> - [`USER_SCREEN_FLOW.md`](./USER_SCREEN_FLOW.md) — navigation + screen-by-screen inventory
> - [`USER_DATA_MODELS.md`](./USER_DATA_MODELS.md) — all request/response models
> - [`FLUTTER_IMPLEMENTATION_MAPPING.md`](./FLUTTER_IMPLEMENTATION_MAPPING.md) — Android→Flutter mapping & architecture plan

---

## 1. Project Overview

**InstaGuard** is an on-demand **security-guard booking** platform (an Uber-style marketplace where the "driver/provider" is a **security guard**). This repository (`com.instaguard.user`) is the **Customer/User app only** — the entire Java package tree is the user side. Guards run a **separate app**; they appear in this app purely as bookable providers sourced from **Firebase Realtime Database + GeoFire**.

### What the User app does
- Lets a user register/login (email/phone + password, or social login).
- Shows **service categories** (e.g. guard types) and **sub-categories/services**.
- Finds **nearby available guards** on a map using GeoFire proximity search.
- Books a guard **now** or **scheduled (book later)**, choosing number of guards, location, hours, and a payment method.
- Gets a server-computed **fare estimate** (hours × price-per-hour × guards + VAT − promo/subscription discount).
- Tracks the **active job** live on a map (guard location + ETA from Firebase), chat & voice call the guard (Twilio), extend hours, cancel, and settle payment (Stripe / PayPal / Wallet / Cash / Google Pay).
- Manages **booking history** (ongoing / upcoming / past), **wallet**, **subscription plans**, **profile**, **emergency contacts / SOS panic**, **documents**, **support pages**, and **offers/announcements**.

### Roles
| Role | In this app |
|------|-------------|
| **User / Customer** | Everything in this codebase. Identified by `customer_id` (= stored `SP_USERID`). |
| **Guard / Provider / Driver** | The bookable party. Internally referred to as **driver / provider**. Lives in Firebase nodes `providers/<id>` and `drivers_location/<id>`. Booked by the user; runs a separate guard app. |

> ⚠️ **Naming trap for the Flutter rewrite:** the backend and models use **driver/provider** terminology throughout (`driver_id`, `providers`, `drivers_location`, `doGetVehiclecategoryUpdateDetails`, `VehicleCategoryModel`, `RideHistoryModel`, etc.). These are **security-guard** concepts, not vehicles/rides. Keep the server key names verbatim in your Dio payloads; only rename in your UI/domain layer.

### How the User interacts with Guards
1. User picks category → service → location → guard count.
2. App queries **GeoFire** node `drivers_location` for guards within a radius, filtered by `providers/<id>.status=="1"`, availability `service_status ∈ {0,8,9}`, and whether the guard's `category` CSV contains the selected service id.
3. User selects a guard (or enters a guard **reference/unique id**), confirms fare & payment, and sends a `requestService` booking request.
4. Acceptance is signalled through **Firebase** (`users/<userId>/trips/accept_status`), not a REST response.
5. Live job tracking, chat/call, payment settlement and rating all happen in `MainJobProgressActivity`, driven by the Firebase `booking_trips/<bookingId>` node.

---

## 2. Architecture (actual)

```
UI (Activity / Fragment + Android DataBinding / findViewById)
        │  (direct calls, no ViewModel, no Repository layer)
        ▼
Retrofit  APIInterface   (Helpers/Retrofit/APIInterface.java)
        │   @FormUrlEncoded @POST, body = @FieldMap HashMap<String,Object>
        ▼
APIClient.getClient()    (Helpers/Retrofit/APIClient.java)  → OkHttp (logging in DEBUG)
        ▼
Backend REST  https://backend.insta-guard.co.uk/api/v3/

  ── in parallel ──
Firebase Realtime Database + GeoFire  (live guard location, job state, wallet, offers)
Firebase Cloud Messaging (push)   |   Twilio (chat + voice)   |   Stripe / PayPal / Google Pay (payments)
```

Key architectural facts:
- **No MVVM / no Repository / no LiveData.** Each Activity/Fragment calls `APIClient.getClient().create(APIInterface.class).<method>(...)` and handles the response inline via `Call.enqueue`. (For Flutter you should **add** a Repository + Provider/ChangeNotifier layer — see the mapping doc.)
- All POST bodies are **form-urlencoded** (`@FieldMap HashMap<String,Object>`), values are strings. There is **no JSON request body and no multipart** anywhere — images/documents are **Base64 strings inside form fields**.
- **Auth header is built inline at each call site**, usually `"Bearer " + accessToken`. Some endpoints send `""` (empty), a literal `"token"` placeholder, or a raw token — documented per-endpoint in the API file. Many endpoints require **no auth** and identify the user via a `customer_id`/`id` field in the body.
- Success is determined by **HTTP 200/201 AND an exact English message-string match** (e.g. `"loggedin successfully"`). These strings are load-bearing.
- **401 → refresh:** most screens catch HTTP 401, call `auth/refresh` with `{id, token}`, store the new token, and retry; a second 401 force-logs-out (clears prefs → `HomeActivity`).
- Gson is configured with a `NullStringToEmptyAdapterFactory` — **null strings deserialize to `""`**. Many response fields are typed `Object` (can arrive as number or string) — parse defensively.

### Base URLs & environments (`Helpers/Utils.java`)
| Purpose | URL |
|---------|-----|
| REST API base (`RETROFIT_URL`) | `https://backend.insta-guard.co.uk/api/v3/` |
| Chat/Call API base | `https://backend.insta-guard.co.uk/api/v3/` |
| Image/asset base (`BASE_URL`) | `https://backend.insta-guard.co.uk/` (+ `uploads/`) |
| Calendar WebView | `https://backend.insta-guard.co.uk/calender/customer/` |
| Google Places autocomplete | `https://maps.googleapis.com/maps/api/place/autocomplete/` |
| LinkedIn | `https://api.linkedin.com/v3/` |

- There is effectively **one environment (production)**. `APIClient` switches only the OkHttp logging interceptor on `BuildConfig.DEBUG` — the base URL is the same in debug and release. No staging/dev URL is active (an old `instaguard.uplogictech.com` URL is commented out).
- Build identity: `applicationId = com.instaguard.user`, `versionName 1.2`, `versionCode 3`, `minSdk 24`, `targetSdk 34`. A build flag `Utils.BASE_CODE = "_Demo"` makes the SharedPreferences store name `Insta-Guard_Demo_Customer`.
- **Secrets:** `local.properties` holds the Google Maps `API_KEY`; Stripe/PayPal keys are delivered at runtime by the `appsetting` API. Signing keystore (`insta-guard-user.jks`), `google-services.json`, `private_key.pepk`, and OAuth client json are present in the repo. **All of these are `[SECRET/REDACTED]` — do not reproduce them in documentation or commits.**

---

## 3. Complete User Feature List

| # | Feature | Screen / Class | Purpose | APIs Used | Status |
|---|---------|----------------|---------|-----------|--------|
| 1 | Splash / session routing | `LaunchActivity` | Route logged-in vs logged-out; optional biometric unlock; fetch FCM token | (reads Firebase `users/<id>`) | ✅ Active |
| 2 | Login (email/phone + password) | `LoginFragment` (host `HomeActivity`) | Authenticate | `auth/login` | ✅ Active |
| 3 | Register | `RegisterFragment` | Create account (with email/phone availability pre-check) | `emailAvailable`, `auth/register` | ✅ Active |
| 4 | Social login (FB/Google/Twitter/LinkedIn) | `HomeActivity`, `SocialSignupFragment` | OAuth login + social signup completion | `customer/socialAdd`, `customer/socialUpdate`, LinkedIn `userinfo` | ✅ Active (token persistence buggy — see API doc) |
| 5 | Forgot / reset password | `ForgotPasswordFragment` | Email-OTP password reset | `forgetpass`, `verifyotp`, `updatePassword` | ✅ Active |
| 6 | Biometric (fingerprint) app-lock | `SettingsFaceFingerscreen`, `LaunchActivity` | Local unlock for a persisted session | — (local) | ✅ Fingerprint only (Face = "Under Construct") |
| 7 | App settings bootstrap | `HomeActivity`/`MainActivity`/others | Load Stripe/PayPal keys, distance limit, currencies, languages, VAT, Tawk, doc flag | `appsetting` | ✅ Active |
| 8 | Home / map dashboard | `MainActivity` | Map, category/service pick, location pick, guard count, banners, drawer | `Categories`, `Service`, `getbanners`, `customerongoing`, `appsetting` | ✅ Active |
| 9 | Category & sub-category (service) selection | `MainActivity`, `SubCategoryFragment` | Choose guard type & service | `Categories`, `Service` | ✅ Active |
| 10 | Location pick (current + Places autocomplete) | `PlacesAutoCompleteFragment` | Pickup location | Google Places `json`, Geocode | ✅ Active |
| 11 | Guard discovery (nearby, GeoFire) | `ProviderListActivity` | List available guards by proximity | Firebase GeoFire `drivers_location` + `providers` | ✅ Active |
| 12 | Fare estimate | `ProviderListActivity` | Server-computed price | `FareEstimate` | ✅ Active |
| 13 | Availability check (scheduled) | `ScheduleTripFragment` | Check guards free for a date/time | `availabilityCheckService` | ✅ Active |
| 14 | Book a specific guard by reference id | `GuardUniqueIdFragment` | Add guard(s) by unique id | `driverReferenceIdValidate` | ✅ Active |
| 15 | Promo code | `ProviderListActivity` | Apply discount code | `checkPromoCode` | ✅ Active |
| 16 | Create booking (now / scheduled) | `ProviderListActivity` | Send booking request | `requestService` (mode `ridenow`, `trip_type=2`), `customerTimeout` | ✅ Active |
| 17 | Active / live job tracking | `MainJobProgressActivity` | Map tracking, timeline, ETA | Firebase `booking_trips`, `drivers_location`, `direction` | ✅ Active |
| 18 | Extend hours | `MainJobProgressActivity` | Add hours mid-job | `ExtraCalFare`, `UpdateJob` | ✅ Active |
| 19 | In-app chat with guard | `MainJobProgressActivity` (Twilio) | Messaging | `chatAccessToken`, `message_notify` | ✅ Active |
| 20 | Voice call guard | `MainJobProgressActivity` (Twilio Voice) | Calling | `accesstoken` | ✅ Active |
| 21 | Payment settlement | `MainJobProgressActivity` | Pay at/after job | `requestService` (mode `payment_success`), `Paycard`, `Paypaypal`, `customerTripPayment`, Stripe `StripePayment3DApp`/`StripeSaveCardPayment3DApp` | ✅ Active |
| 22 | Cancel booking | `MainJobProgressActivity`, history tabs | Cancel active/upcoming | `cancelJobs`, `scheduledcancelJobs`, `requestService` (mode `customer_cancel`) | ✅ Active |
| 23 | Rating & review | `MainJobProgressActivity`, `YourJobsPastFragment` | Rate guard after job | `CustomerFeedbackStore` | ✅ Active |
| 24 | Ongoing jobs | `OnGoingJobsFragment` | List in-progress jobs | `customerongoing` | ✅ Active |
| 25 | Upcoming/request jobs (paginated) | `YourJobsUpcomingFragment` | List requests/upcoming | `CustomerUpcomingJob` (offset paging) | ✅ Active |
| 26 | Past jobs | `YourJobsPastFragment` | History + rating | `pastjobs` | ✅ Active |
| 27 | Calendar bookings | `MyCalenderBookingFragment` | WebView calendar | (WebView only) | ✅ Active |
| 28 | Wallet balance | `MyWalletFragment` | Show balance, add-money entry | Firebase `users/<id>/wallet`; Stripe top-up **disabled** | ⚠️ Balance ✅ / top-up commented out |
| 29 | Wallet transactions | `YourWalletTransaction*Fragment` | All / money-in / money-out | `Transaction` (client-side filter) | ✅ Active |
| 30 | Subscription plans | `SubscriptionPlanFragment` | List & buy plans (Stripe Checkout WebView) | `subscriptionList`, `createCheckoutSession`, `checkSubscription`, `updateAutoRenew` | ✅ Active |
| 31 | Subscription history | `SubscriptionPlanHistoryFragment` | Past plans | `subscription_history` | ✅ Active |
| 32 | Profile (view) | `SettingsFragment` | Show profile | `profileView` | ✅ Active |
| 33 | Edit profile | `EditSettingsFragment` | Update name/email/phone/currency + avatar + change password | `updateprofile`, `picupload`, `auth/passwordChange` | ✅ Active |
| 34 | Delete account | `SettingsFragment` | OTP-confirmed deletion | `delete_account_request`, `delete_account_confirm` | ✅ Active |
| 35 | Emergency contacts | `EmergencyContactsFragment` | Add/view/delete (max 5) | `emergencyContactCustomer`, `emergencyContactGET`, `emergencyContactDelete` | ✅ Active |
| 36 | SOS / panic | `MainJobProgressActivity`, `MainActivity` | SMS emergency contacts + notify admin | `savepanic` (+ `emergencyContactGET`) | ✅ Active |
| 37 | Invite friends | `InviteFriendsFragment` | Share invite code | — (local share) | ✅ Active |
| 38 | Support pages | `SupportFragment`, `SupportSubFragment` | Dynamic/static pages (About/Privacy/Terms/Contact/FAQ) | `pages`, `Support` | ✅ Active |
| 39 | Tawk.to live chat | `SupportFragment` | Web chat | (WebView; config from `appsetting`) | ⚠️ Wired but FAB hidden (GONE) |
| 40 | Offers & announcements | `YourOffersFragment`, `YourAnnocementFragment` | Promo / news feed | Firebase `Notifications/Offers|Announcements/user` | ✅ Active (Firebase, not REST) |
| 41 | Documents | `DocumentActivity` | Upload/view/re-upload docs (Base64) | `manageCustomerDocument`, `customerViewDocument`, `customerImageUpload`, `customerDocumentUpdate`, `customerProofReupload` | ✅ Active (gated by `customer_manage_document`) |
| 42 | Language & currency | `HomeActivity`/`MainActivity`/`EditSettingsFragment` | Localization + FX | `appsetting`; Firebase `currency/<code>` | ✅ Active |
| 43 | Push notifications | `MyFirebaseMessagingService` | Incoming push + call invites | FCM; token sent via `device_token` in auth | ✅ Active |
| 44 | Google Ads | `MainActivity` etc. | AdMob / Facebook Audience | (ad SDKs) | ✅ Present |

**Dead / commented / unused in the current build** (present in code but not reachable — documented for completeness): `scheduledBooking`, `checkService`, `verifyMobile`, `forgot_password`, `auth/SocialLog`, `buySubscription` (superseded), `subscriptionGetList` (stub), wallet top-up (`customerAddMoney`), `CaptureWalletRecharge`, `CreateSaveCardSetupIntent`, Google Directions `getResponseDistance`, `notifysend` (only `ProviderListActivity` uses it). See the API doc's "Declared but dead/unused" section.

---

## 4. Authentication (summary)

Full request/response detail is in [`USER_API_DOCUMENTATION.md`](./USER_API_DOCUMENTATION.md). Key points:

- **Login gate is the presence of `SP_USERID`**, not token validity. `LaunchActivity` treats a non-null stored userId as "logged in", then verifies the Firebase `users/<id>` node exists before entering `MainActivity`; otherwise it routes to `HomeActivity`.
- **Login** (`auth/login`): single field accepts **email OR phone**, sent as `email` with `type="app"`, plus `password` and `device_token` (FCM). Returns `access_token`, `token_type`, `expires_in` and a full `UserData`. Success string: `"loggedin successfully"`.
- **Register** (`auth/register`): pre-checked with `emailAvailable`. Does **not** auto-login (navigates to Login after success). Fields: first/last name, email, country_code, phone_number, password, referral_code, device_token.
- **Social login**: all providers funnel into `customer/socialAdd`; new users complete `customer/socialUpdate`. ⚠️ Known bug: the Home path persists **placeholder tokens** (`"1256"/"32556"`, `"123"/"token"`) rather than the real social access token.
- **Forgot password**: `forgetpass` (email OTP) → `verifyotp` → `updatePassword`.
- **Logout** (`auth/logout` `{id, token}`): clears `MY_PREFS_NAME` and returns to `HomeActivity`.
- **Token storage keys:** `SP_USER_ACCESSTOKEN`, `SP_USER_TOKENTYPE`, `SP_USER_TOKENEXPTIME`, `SP_USERID`. Refresh via `auth/refresh {id, token}`.

See also the **Authentication Mermaid diagram** in [`USER_API_DOCUMENTATION.md`](./USER_API_DOCUMENTATION.md#auth-diagram).

---

## 5. Guard Discovery (summary)

Full flow in [`USER_BOOKING_FLOW.md`](./USER_BOOKING_FLOW.md). Key points:

1. `MainActivity` obtains current location (FusedLocation + fallback LocationManager), shows a satellite map; the **map-center** is the pickup point (reverse-geocoded to an address).
2. `Categories` API loads guard categories (first auto-selected). Selecting one calls `Service` to load sub-categories/services (price fields: `price_per_hour`, `price_per_agent`, `price_per_day`, `book_guard_min_time`, etc.).
3. User sets pickup (map center or Places autocomplete), guard count (**Book Now requires ≥ 2 guards**), optional emergency note, and taps **Book Now** (`normaltrip`) or **Book Later** (`scheduletrip`).
4. All selections are written to SharedPreferences store `MY_PREFS_REQUEST_STATE` (keys `B_*`) — **not Intent extras** — then `ProviderListActivity` opens.
5. `ProviderListActivity` runs a **GeoFire** query on `drivers_location` within a radius (`SP_COMMON_DISTANCE_LIMIT` km, default **5 km**). For each matched guard it reads `providers/<id>`, keeping only those with `status=="1"`, `trips/service_status ∈ {"0","8","9"}`, and whose `category` CSV contains the selected service id. Distance is computed client-side (great-circle).
6. For **scheduled** bookings, GeoFire is skipped; `availabilityCheckService` returns a CSV of available driver ids, and only those are loaded from Firebase.

---

## 6. Guard Details (summary)

There is **no dedicated "guard profile" REST endpoint.** The guard card/detail is assembled from the **Firebase `providers/<driverId>`** node and the GeoFire location node:

| Field | Source |
|-------|--------|
| id | GeoFire key = provider/driver id |
| First/last name | `providers/<id>/fname`, `/lname` |
| Profile image | `providers/<id>/pro_pic` → `BASE_URL + uploads/...` |
| Rating | `providers/<id>/rating` (default `"0"`) |
| Company name / provider type | `providers/<id>/company_name`, `/provider_type` (default `"Individual"`) |
| Email / mobile | `providers/<id>/email`, `/mobile` |
| Service description | `providers/<id>/service_desc` |
| Distance (miles/KM) | client-side great-circle from pickup to `drivers_location/<id>` |
| Online/available | `status=="1"` AND `trips/service_status ∈ {0,8,9}` |
| Services offered | `category` CSV of service ids |

Tapping a guard (More Info / photo / Book Now) surfaces `{id, fname, lname, pro_pic, rating, service_desc, categoryid, subcategoryid, date, time, miles}` to the booking flow. The `ProviderListData` model fields are in [`USER_DATA_MODELS.md`](./USER_DATA_MODELS.md).

---

## 7–16. Booking, Payment, Status, History, Cancel, Rating

These are the most important areas and are documented in full in **[`USER_BOOKING_FLOW.md`](./USER_BOOKING_FLOW.md)**, including:
- Step-by-step booking-request construction (immediate + scheduled).
- Complete `requestService` request body (every field key and source).
- The **booking API sequence** and dependency diagram.
- The **service_status lifecycle** (every integer and what the user sees/can do).
- Payment flows for Wallet / Card (Stripe + 3DS) / Google Pay / PayPal / Cash, and balance top-ups.
- Cancellation (`cancelJobs`, `scheduledcancelJobs`, request cancel) and charge logic.
- Rating/review (`CustomerFeedbackStore`).
- History tabs (ongoing / upcoming-paginated / past) and calendar.

---

## 17. Notifications (FCM)

- **Provider:** Firebase Cloud Messaging (`MyFirebaseMessagingService`). Token fetched in `LaunchActivity` / `onNewToken`, stored in SharedPreferences store `GLOBAL_STORED_ID` under key **`"regid"`** (`SP_DEVICE_ID`).
- **Token registration:** there is **no dedicated register-token endpoint**. The token is sent as `device_token` (login/register) or `device_id` (social) in the auth payloads. Default `"123"` when FCM token is missing.
- **Incoming push parsing** (`onMessageReceived`, reads `remoteMessage.getData()`):
  - `a_data == "pn_city"` → data notification with `image`, `title`, `body` (BigPicture via Picasso if image present).
  - `twi_account_sid` (+ `twi_*`) → **Twilio Voice** incoming-call invite → launches `MainJobProgressActivity` with `ACTION_INCOMING_CALL`, extra `booking_id` (= `SP_USER_CURRENT_BOOKING_ID`), `from="main"`.
  - `status == "1"` → marks unread (UI update commented out).
  - else → standard `getNotification()` title/body.
- **Channels:** `"101"` ("Notification", IMPORTANCE_MAX) for general pushes; `"default"` ("Primary Voice Channel", IMPORTANCE_HIGH) for calls; `NotificationUtils` also uses `"default_channel_id"`. General notification id = 1, big-image = 101.
- **Click behaviour:** general push → `LaunchActivity` (which then routes); call push → `MainJobProgressActivity`.
- **Outgoing push:** `notifysend` (guard-accept notification, from `ProviderListActivity`) and `message_notify` (chat notification to guard, from `MainJobProgressActivity`).
- **Offers & Announcements lists** are **Firebase RTDB** streams (`Notifications/Offers/user`, `Notifications/Announcements/user`, filtered `status==1`), **not** REST.

---

## 18. User Profile

- **View:** `SettingsFragment` → `profileView {id, token}` → `ProfileModel` (read-only display of name, email, country_code + phone, language, currency, avatar, plus a subscription block).
- **Edit:** `EditSettingsFragment` → `updateprofile`. **Editable fields actually sent:** `first_name`, `last_name`, `email`, `phone_number`, `country_code`, `currency`. **Not sent:** dob, gender, address, language (present in model but no UI / commented out).
- **Avatar upload:** `picupload {id, file_binary_data (Base64 JPEG), avatar}`. **Base64, not multipart.**
- **Change password:** `auth/passwordChange {id, old_password, new_password}` (Bearer). Client regex `(?=.*[a-z])(?=.*[0-9]).{8,}`.
- **Delete account:** `SettingsFragment` two-step OTP — `delete_account_request` → `delete_account_confirm`.
- Profile mirrors name/email/phone/currency to Firebase `users/<userId>` on update.

---

## 19. Location & Maps

- **Google Maps** (satellite, zoom locked 15–17) in `MainActivity` (pick pickup) and `MainJobProgressActivity` (track guard).
- **Location:** `FusedLocationProviderClient` (singleton + `LocalBroadcastManager`), with a legacy `LocationManager` GPS fallback. Reverse geocoding via `FetchAddressIntentService`.
- **Places autocomplete:** Google `place/autocomplete/json` (`getPlaces`), then Geocode by `place_id` for lat/lng.
- **Live guard tracking:** `drivers_location/<driverId>` (child `l = "lat,lng"`, `bearing`); route polyline from Firebase node `direction/<driverId>`; **ETA computed locally (Haversine)**. Note: the Google Directions endpoint is declared but not called.
- **Distance** in the guard list is client-side great-circle (`DistanceCalculator`).
- **Permissions (Maps/Location):** `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION`. If denied → toast, map shows no blue dot, location stays null; GPS-off shows a dialog to Location Settings. See §25.

---

## 20. Local Storage

Full inventory in [`USER_DATA_MODELS.md`](./USER_DATA_MODELS.md#local-storage). SharedPreferences only (no Room/SQLite/DataStore). Stores:

| Store | Holds |
|-------|-------|
| `Insta-Guard_Demo_Customer` (`MY_PREFS_NAME`) | tokens, userId, profile, wallet, common/appsetting flags (Stripe keys, distance limit, Tawk, doc flag, paypal/wallet/subscription status), subscription code |
| `..._face_finger` (`MY_PREFS_NAME_NEW`) | biometric flags |
| `..._Currecny_All` (`PREFS_NAME_CURRENCY`) | currency + language selection, FX ratio |
| `..._rememberme` (`MY_PREFS_NAME_REMEMBER`) | remember-me email/password |
| `..._Request_State` (`MY_PREFS_REQUEST_STATE`) | the booking-input bundle (`B_*` keys) |
| `..._category_tripstate` (`MY_PREFS_FB_CATEGORY_TRIPSTATE`) | pay-mode trip state |
| `..._global_stored` (`GLOBAL_STORED_ID`) | FCM token (`regid`) |
| `..._hashmap_<bookingId>` (`SP_PAYMENT_HASHMAP_KEYS_`) | persisted payment request map (retry) |

Critical keys: `SP_USERID`, `SP_USER_ACCESSTOKEN`, `SP_USER_CURRENT_BOOKING_ID`, `SP_COMMON_STRIPE_PUBLISH_KEY`, `SP_COMMON_DISTANCE_LIMIT`.

---

## 21. Error Handling (what the user sees)

| Condition | Handling |
|-----------|----------|
| HTTP 200/201 + expected message | success path |
| HTTP 200/201 + other message | snackbar/toast with the server message |
| **401 Unauthorized** | call `auth/refresh {id, token}` → retry original; 2nd 401 → clear prefs, logout to `HomeActivity` (`showTimeOut()`) |
| 404 / 500 / 501 | snackbar like `"500 Error ..."` |
| `onFailure` (network/timeout) | `call.cancel()` + hide progress + snackbar `"onFailure<throwable>"`; OkHttp timeouts are **1000s** |
| No internet | `BaseActivity.isOnline` / `Tovuti` network monitor gate many calls; shows no-internet UI |
| Empty list | server message `"No records Found."` → empty-state view |
| Payment error | Stripe/PayPal SDK callback errors → toast; booking not finalized |
| Validation error | inline field errors via `awesome-validation` / regex before the call |

Status code constants: `RESPONSECODE_200=200, 201, 401, 404, 500, 501` (`Utils.java`).

---

## 22. Permissions

| Permission | Why | Requested where | If denied |
|------------|-----|-----------------|-----------|
| `ACCESS_FINE_LOCATION` / `ACCESS_COARSE_LOCATION` | Map, pickup location, nearby guards, live tracking, SOS location link | `HomeActivity`, `MainActivity` (code 3) | Toast; map has no location; GPS-off dialog |
| `POST_NOTIFICATIONS` | FCM push (Android 13+) | runtime | No push shown |
| `CAMERA` | Avatar & document capture | `EditSettingsFragment`, `DocumentActivity` | Can't take photo |
| `READ_MEDIA_IMAGES` / `READ/WRITE_EXTERNAL_STORAGE` (≤32) | Gallery pick for avatar/docs | same | Can't pick image |
| `READ_CONTACTS` / `WRITE_CONTACTS` / `GET_ACCOUNTS` | Pick emergency contacts | `EmergencyContactsFragment` | Can't import contact |
| `CALL_PHONE` | Call/dial guard | `MainJobProgressActivity` | Dial fails |
| `READ_PHONE_STATE` | Device/telephony state | app | — |
| `INTERNET`, `ACCESS_NETWORK_STATE`, `ACCESS_WIFI_STATE` | Networking | app | — |
| `AD_ID` / `ACCESS_ADSERVICES_AD_ID` | Ads | app | — |
| `RECORD_AUDIO` (via Twilio libs) | Voice calls | at call time | Call mic fails |

Declared in `AndroidManifest.xml` (see §25 list there). Features are declared `required=false` for camera/telephony/location hardware.

---

## 23. Third-Party SDKs

| SDK / Library | Version | Purpose | Used in |
|---------------|---------|---------|---------|
| Retrofit2 + OkHttp + Gson/Jackson converters | 2.9.0 / 4.11.0 | REST networking | all API calls |
| Firebase Realtime Database | 20.3.1 | Live guard location, job state, wallet, offers, currency | discovery, job progress, wallet, offers |
| GeoFire | 3.2.0 | Proximity query for nearby guards | `ProviderListActivity` |
| Firebase Auth | 22.3.1 | Anonymous auth for RTDB reads | `LaunchActivity` |
| Firebase Cloud Messaging | 23.4.1 | Push | `MyFirebaseMessagingService` |
| Firebase Crashlytics + Analytics | BOM 32.2.0 | Crash/analytics | app |
| Google Play Services Maps | 19.0.0 | Maps | map screens |
| Google Play Services Location | 21.3.0 | Fused location | location |
| Google Maps Utils | 2.3.0 | Map clustering/utils | maps |
| Google Maps Services (`google-maps-services`) | 0.15.0 | Directions/distance (server-side lib) | helpers |
| Google Directions lib (`googledirectionlibrary`) | 1.1.1 | Polyline | maps |
| Stripe Android | 17.1.2 | Card payments, 3DS, Google Pay, PaymentLauncher | `MainJobProgressActivity`, subscription |
| Google Pay (`play-services-wallet`) | 19.1.0 | Google Pay via Stripe | payments |
| PayPal (core/card/web/native payments + buttons + fraud) | 1.1.0 | PayPal checkout | payments |
| Twilio Voice | 4.4.0 | Calls to guard | `MainJobProgressActivity` |
| Twilio Conversations | 6.0.4 | In-app chat | `MainJobProgressActivity` |
| Facebook SDK | 13.0.0 | FB login + share | `HomeActivity`, invite |
| Facebook Audience Network Ads | 6.16.0 | Ads | app |
| Google Play Services Auth | 21.2.0 | Google sign-in | `HomeActivity` |
| Twitter4J | 4.0.7 | Twitter login | `HomeActivity` |
| Picasso | 2.71828 | Image loading (notifications) | FCM |
| Fresco | 3.1.3 | Image loading | lists |
| Lottie | 6.0.0 | Animations (splash/finger/face) | `LaunchActivity` |
| libphonenumber | 8.12.46 | Phone validation | register |
| AndroidX Biometric | 1.1.0 | Fingerprint | biometric |
| WorkManager | 2.7.1 | Background work | app |
| EventBus | 3.3.1 | In-app events | app |
| Volley | 1.2.1 | Geocode request | Places |
| materialdatetimepicker | 4.2.1 | Date/time pick (scheduled booking) | `ScheduleTripFragment` |
| pinview | 1.4.4 | OTP entry | OTP dialogs |
| Google Ads (`play-services-ads`) | 22.2.0 | AdMob | app |
| RxJava2 + reactive-location | 2.2.21 | Reactive location | helpers |

---

## 24. UNKNOWN / REQUIRES BACKEND CONFIRMATION

These could not be fully determined from client code alone and **must be confirmed with the backend team** before the Flutter rewrite relies on them:

1. **Full `requestService` response schema & server-side matching.** The client sends `reference_id` (guard unique-id list) + `customer_id`; the server decides which guard(s) get the request. The exact matching rules, what `request_status`/`service_status` integers the create-response carries, and how group bookings assign multiple guards are server-side. Client only reacts to Firebase `accept_status`.
2. **Exact fare formula.** All price math is server-side in `FareEstimate`/`ExtraCalFare`. Client only echoes returned values (`overall_total`, `before_vat_amount`, `total_hrs`, `price_per_hr`, `subscription_amount`, `subscription_percentage`). Confirm inputs→outputs with backend.
3. **Cancellation charge / refund logic.** `cancelJobs` sends `reason=""` and no amount; any cancellation fee/refund is computed server-side (sub-category has `user_cancellation_charge` / `user_cancellation_time`, but the applied rule is unconfirmed).
4. **Firebase database schema & security rules.** Full node shapes for `booking_trips/<id>` (all child fields and their write-owners — which are written by the guard app vs server) need confirmation. Many status transitions are driven by the **guard app**, not this one.
5. **Payment settlement ordering.** Whether `Paycard`/`Paypaypal`/`customerTripPayment` must precede or follow `requestService mode=payment_success`, and which is authoritative, needs backend confirmation. The client calls differ per payment type and per status (5.1/5.3/10/6).
6. **Stripe key/secret usage.** `StripePayment3DApp` et al. are called with a literal `"token"` header and a `save_card` flag; confirm how the server authenticates these and whether a Stripe Customer/ephemeral key is returned.
7. **Social-login token contract.** Client persists placeholder tokens after social login (bug). Confirm what bearer the backend expects for subsequent authenticated calls by social users.
8. **Dead endpoints.** Confirm whether `scheduledBooking`, `checkService`, `buySubscription`, `CaptureWalletRecharge`, `CreateSaveCardSetupIntent`, `verifyMobile`, `forgot_password`, `auth/SocialLog` are deprecated server-side or should be wired up in the new app.
9. **Wallet top-up.** Currently disabled client-side; confirm the intended recharge flow (`customerAddMoney` vs `CaptureWalletRecharge`).
10. **`appsetting` full schema & which fields gate features** (`payment_mode`, `guard_subscription`, `customer_manage_document`, `wallet_minimum`, `vat_percentage`).

See each companion doc for per-area "gotchas".

---

## 25. Android Permissions (Manifest) & Deep Links

Declared in `AndroidManifest.xml`:
`ACCESS_NETWORK_STATE, CALL_PHONE, GET_ACCOUNTS, INTERNET, ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION, READ_CONTACTS, WRITE_CONTACTS, READ_PHONE_STATE, ACCESS_WIFI_STATE, WRITE_EXTERNAL_STORAGE (≤32), READ_EXTERNAL_STORAGE (≤32), ACCESS_GPS, ACCESS_LOCATION, POST_NOTIFICATIONS, READ_MEDIA_IMAGES, CAMERA, AD_ID, ACCESS_ADSERVICES_AD_ID`.

**Deep link / intent filters:**
- Launcher: `LaunchActivity` (`MAIN` / `LAUNCHER`).
- **PayPal return**: scheme `paypalpay`, host `com.instaguard.user` (`PAYPAL_RETURN_URL = "com.instaguard.user://paypalpay"`) on `ProviderListActivity` and `MainJobProgressActivity`.
- Facebook `CustomTabActivity` (fb login protocol scheme).
- Exported activities: `LaunchActivity`, `HomeActivity`, `MainActivity`, `ProviderListActivity`, `MainJobProgressActivity`, `DocumentActivity`, `SettingsFaceFingerscreen`.
- Services: `MyFirebaseMessagingService` (FCM), `FetchAddressIntentService` (geocode), `AppLocalesMetadataHolderService`.

Full screen inventory is in [`USER_SCREEN_FLOW.md`](./USER_SCREEN_FLOW.md).

---

## 26. Final User Journey (actual)

```
Install app
  ↓
LaunchActivity (splash)
  ├─ SP_USERID == null ───────────────→ HomeActivity (login/register landing)
  └─ SP_USERID != null
        ├─ (optional) fingerprint unlock if spFingerStatus=="1"
        ├─ Firebase users/<id> exists? ── no ──→ HomeActivity
        └─ yes ─────────────────────────────────→ MainActivity
  ↓
HomeActivity → Login (auth/login)  |  Register (emailAvailable → auth/register → back to Login)  |  Social login
  ↓ (on success)
MainActivity (map home)
  → appsetting (Stripe/PayPal keys, distance, currencies) + getbanners + customerongoing
  → Categories (pick guard type) → Service (pick service)
  → pick pickup location (map center / Places autocomplete), guard count (≥2 for Book Now), emergency note
  → Book Now (normaltrip)  OR  Book Later (scheduletrip → ScheduleTripFragment: date/time → availabilityCheckService)
  ↓  (state saved to MY_PREFS_REQUEST_STATE)
ProviderListActivity
  → GeoFire drivers_location within radius → filter providers → guard list
  → (optional) GuardUniqueIdFragment: add guard by reference id (driverReferenceIdValidate)
  → select guard → FareEstimate → appsetting → booking-details + payment dialog
  → (optional) checkPromoCode → re-run FareEstimate
  → choose payment: Wallet / Card(Stripe+3DS) / Google Pay / PayPal / Cash
  → requestService (mode=ridenow [+ trip_type=2 if scheduled]) → customerTimeout
  → listen Firebase users/<id>/trips/accept_status
        "1"=requesting … "yes"=accepted … "2"=rejected … "3"=reset
  ↓ (accepted → View Ongoing Jobs)
OnGoingJobsFragment → tap job → MainJobProgressActivity (booking_id)
  → booking_trips/<id>/service_status state machine:
     2 accepted → 3 on the way → 4 arrived → 5 service begin
        (5.1 card pay / 5.3 paypal pay) → 6 job ended (Mark Completed → requestService mode=payment_success)
        → 7 completed → rating (CustomerFeedbackStore)
     8 user-cancelled / 9 guard-cancelled / 10 balance due (customerTripPayment)
  → during job: extend hours (ExtraCalFare → UpdateJob), chat (Twilio + message_notify), call (Twilio), SOS (savepanic)
  ↓
Booking History: OnGoing (customerongoing) | Upcoming/Request (CustomerUpcomingJob, paged) | Past (pastjobs) | Calendar (WebView)
  ↓
Profile / Wallet / Subscription / Emergency contacts / Documents / Support / Offers
  ↓
Logout (auth/logout) → clear prefs → HomeActivity
```

---

## 27. Summary (counts)

| Metric | Count |
|--------|-------|
| **User screens found** | **8 Activities + ~55 Fragments** (active user-facing screens ≈ 45; see screen doc). Core activities: Launch, Home (auth), Main (map), ProviderList, MainJobProgress, Document, SettingsFaceFinger, HomeMain(unused stub). |
| **Total distinct backend endpoints** | **~70** (`api/v3/`), plus 2 Google + 1 LinkedIn endpoints (75 Retrofit method signatures, some sharing the `requestService` path). |
| **Authentication APIs** | 10 — `auth/login`, `auth/register`, `emailAvailable`, `auth/logout`, `forgetpass`, `verifyotp`, `updatePassword`, `auth/refresh`, `auth/passwordChange`, `customer/socialAdd`, `customer/socialUpdate` (+ dead: `verifyMobile`, `forgot_password`, `auth/SocialLog`), `delete_account_request/confirm`. |
| **Guard-related APIs** | `Categories`, `Service`, `availabilityCheckService`, `driverReferenceIdValidate`, `FareEstimate`, `getbanners` + Firebase GeoFire discovery (no REST guard-profile). |
| **Booking APIs** | `requestService` (create / settle / cancel-request), `scheduledBooking` (dead), `customerTimeout`, `checkPromoCode`, `UpdateJob`, `ExtraCalFare`, `cancelJobs`, `scheduledcancelJobs`, `customerongoing`, `upcomingJobs` (dead), `CustomerUpcomingJob`, `pastjobs`. |
| **Payment APIs** | `StripePayment3DApp`, `StripeSaveCardPayment3DApp`, `CreateSaveCardSetupIntent` (dead), `CaptureWalletRecharge` (dead), `Paycard`, `Paypaypal`, `createOrder`, `customerTripPayment`, `customerAddMoney` (disabled) + Stripe/PayPal/GooglePay SDKs. |
| **Notification APIs** | `notifysend`, `message_notify`, `chatAccessToken`, `accesstoken` + FCM; Offers/Announcements via Firebase. |
| **Profile APIs** | `profileView`, `updateprofile`, `picupload`, `auth/passwordChange`. |
| **Wallet / Subscription APIs** | `Transaction`, (`customerAddMoney`/`CaptureWalletRecharge` disabled); `subscriptionList`, `subscriptionGetList` (stub), `buySubscription` (superseded), `createCheckoutSession`, `checkSubscription`, `subscription_history`, `updateAutoRenew`, `checkService` (dead). |
| **Support / Emergency / Document APIs** | `pages`, `Support`; `emergencyContactCustomer/GET/Delete`, `savepanic`; `manageCustomerDocument`, `customerViewDocument`, `customerImageUpload`, `customerDocumentUpdate`, `customerProofReupload`. |

### Backend dependencies the Flutter implementation MUST know
1. **Firebase Realtime Database is mandatory** and co-equal with REST. Booking acceptance, live guard location, ETA, wallet balance, offers/announcements, subscription-purchase result, and currency FX ratios all come from Firebase — not REST. You need the same Firebase project/config and node schema.
2. **GeoFire** on `drivers_location` is how nearby guards are found. Replicate the GeoFire query (radius from `appsetting.distance_limit`, default 5 km) + the `providers/<id>` filtering rules.
3. **`appsetting` must be called early** — it supplies Stripe publishable key, PayPal config, distance limit, VAT %, Tawk config, `customer_manage_document` gate, currencies and languages.
4. **Form-urlencoded + Base64** everywhere (no JSON body, no multipart). Keep `customer_id`/`driver_id` terminology in payloads.
5. **Message-string-based success detection** — preserve the exact server strings or coordinate a proper status-code/flag contract with backend.
6. **Payment is multi-rail** (Stripe + 3DS, Google Pay via Stripe, PayPal native, Wallet, Cash) and interleaved with Firebase status and multiple settle endpoints — see booking doc.
7. **Twilio** (Conversations + Voice) powers guard chat/call; needs access tokens from `chatAccessToken`/`accesstoken`.
8. Several flows are **partially disabled/dead** in the Android build (wallet top-up, scheduledBooking endpoint, subscription stub) — decide per backend confirmation whether to implement or drop (see §24).
