# InstaGuard User App — Flutter Implementation Mapping

> How to rebuild the Android-native User app in Flutter using **Provider + ChangeNotifier + Dio + Repository pattern**.
> No Flutter code is implemented here — this is the architecture plan. Pair with the API, booking, screen and model docs.

---

## 1. Android → Flutter primitive mapping

| Android Native | Flutter equivalent |
|----------------|--------------------|
| Activity | Screen/Page (routed widget) |
| Fragment | Screen/Page or nested Widget |
| `Utils.switchContent(TAG)` fragment swap | `Navigator` routes / `go_router` |
| Intent + extras | Route arguments (typed args object) |
| `startActivityForResult` | `Navigator.push(...).then(result)` |
| ViewModel — **none (logic in Activity)** | `ChangeNotifier` (per feature) |
| Repository — **none** | **Repository** class (add this layer) |
| Retrofit `APIInterface` | `Dio` API service + (optionally `retrofit`/`dio` codegen) |
| `@FieldMap HashMap` (form-urlencoded) | `FormData.fromMap({...})` / `application/x-www-form-urlencoded` |
| OkHttp interceptors | Dio `Interceptors` |
| Gson + `NullStringToEmptyAdapterFactory` | `json_serializable` + nullable-safe parsing |
| LiveData / StateFlow / EventBus | `ChangeNotifier` + `Provider`/`Consumer`, or streams |
| RecyclerView + Adapter | `ListView.builder` / `GridView.builder` |
| SharedPreferences | `shared_preferences` (+ `flutter_secure_storage` for tokens) |
| Parcelable / Serializable model | plain Dart model class (`fromJson`/`toJson`) |
| Firebase Realtime DB + GeoFire | `firebase_database` + `geoflutterfire_plus` (or manual geohash) |
| FCM (`MyFirebaseMessagingService`) | `firebase_messaging` |
| Google Maps | `google_maps_flutter` |
| Fused location | `geolocator` / `location` |
| Places autocomplete / Geocode | `google_places_flutter` + `geocoding` or REST |
| Stripe Android + PaymentLauncher + GooglePayLauncher | `flutter_stripe` (`Stripe`, `confirmPayment`, Google Pay) |
| PayPal native checkout | `flutter_paypal_*` / custom WebView with `paypalpay://` deep link |
| Twilio Voice + Conversations | `twilio_voice` + `twilio_conversations` plugins |
| `awesome-validation` / regex | `form_field_validator` / manual validators |
| materialdatetimepicker | `showDatePicker` / `showTimePicker` |
| Lottie | `lottie` |
| libphonenumber | `phone_numbers_parser` / `libphonenumber_plugin` |
| Picasso / Fresco | `cached_network_image` |
| Biometric | `local_auth` |
| Localization (LocalizationActivity) | `flutter_localizations` + `intl` / `easy_localization` |
| EventBus | `event_bus` or Provider notifications |

---

## 2. Recommended project structure

```
lib/
  core/
    network/        dio_client.dart (base url, interceptors, form-urlencoded)
                    api_endpoints.dart (the ~70 paths)
                    api_exception.dart (401/refresh, message parsing)
    storage/        prefs_service.dart (SharedPreferences), secure_storage.dart (tokens)
    firebase/       rtdb_service.dart, geofire_service.dart, fcm_service.dart
    config/         app_config.dart (base urls), app_settings_store.dart (appsetting cache)
    payments/       stripe_service.dart, paypal_service.dart, google_pay_service.dart
    location/       location_service.dart, places_service.dart
  data/
    models/         (one file per model from USER_DATA_MODELS.md)
    repositories/   auth_repo, profile_repo, discovery_repo, booking_repo,
                    job_repo, payment_repo, wallet_repo, subscription_repo,
                    history_repo, support_repo, emergency_repo, document_repo
  features/
    auth/ splash/ home/ discovery/ provider_list/ booking/ job_progress/
    history/ wallet/ subscription/ profile/ settings/ emergency/ support/
    documents/ notifications/ offers/
      <feature>/ presentation/ (screen + widgets)
                 provider/ (ChangeNotifier)
  app.dart  main.dart
```

**Layering:** `Screen` → `ChangeNotifier (Provider)` → `Repository` → `Dio service` / `Firebase service` / `prefs`. Keep server key names (`customer_id`, `driver_id`, etc.) inside the Dio layer; expose clean Dart domain models upward.

---

## 3. Feature-by-feature structuring

| Feature | Provider (ChangeNotifier) | Repository | Key APIs / sources |
|---------|---------------------------|------------|--------------------|
| Splash/session | `SessionProvider` | `AuthRepository` | read `SP_USERID`, Firebase `users/<id>` |
| Auth | `AuthProvider` | `AuthRepository` | login/register/emailAvailable/social/forgot/refresh/logout |
| App settings | `AppSettingsProvider` (load once, cache) | `ConfigRepository` | `appsetting` → Stripe/PayPal keys, distance, VAT, currencies, languages, doc flag |
| Home/map | `HomeProvider`, `LocationProvider` | `DiscoveryRepository` | Categories, Service, getbanners, customerongoing, location |
| Location pick | `PlacesProvider` | — | Google Places + Geocode |
| Guard discovery | `ProviderListProvider` | `DiscoveryRepository` + `GeoFireService` | GeoFire `drivers_location`, `providers/<id>` |
| Booking | `BookingProvider` (holds booking-input state that Android kept in prefs) | `BookingRepository` | FareEstimate, checkPromoCode, driverReferenceIdValidate, requestService, customerTimeout |
| Payment | `PaymentProvider` | `PaymentRepository` | Stripe/PayPal/GooglePay + Paycard/Paypaypal/customerTripPayment/payment_success |
| Active job | `JobProgressProvider` (listens Firebase) | `JobRepository` | Firebase `booking_trips/<id>`, `drivers_location`, `direction`; ExtraCalFare, UpdateJob, cancelJobs, CustomerFeedbackStore, Twilio tokens |
| History | `HistoryProvider` (paginated) | `HistoryRepository` | customerongoing, CustomerUpcomingJob (offset 8), pastjobs |
| Wallet | `WalletProvider` (Firebase balance) | `WalletRepository` | Firebase `users/<id>/wallet`, Transaction |
| Subscription | `SubscriptionProvider` | `SubscriptionRepository` | subscriptionList, createCheckoutSession (WebView), checkSubscription, updateAutoRenew, subscription_history; Firebase `subs_buy_status` |
| Profile/settings | `ProfileProvider` | `ProfileRepository` | profileView, updateprofile, picupload, auth/passwordChange, delete_account_* |
| Emergency/SOS | `EmergencyProvider` | `EmergencyRepository` | emergencyContact*, savepanic + SMS intent |
| Support | `SupportProvider` | `SupportRepository` | pages, Support |
| Documents | `DocumentProvider` | `DocumentRepository` | manageCustomerDocument, customerViewDocument, customerImageUpload, customerDocumentUpdate, customerProofReupload |
| Notifications | `NotificationProvider` | — | FCM; Firebase Offers/Announcements |

---

## 4. Dio client requirements

1. **Base URL** `https://backend.insta-guard.co.uk/api/v3/`; separate Dio instances or `baseUrl` overrides for Google Places (`maps.googleapis.com`) and LinkedIn.
2. **Content-Type:** `application/x-www-form-urlencoded` for all app POSTs; send `FormData`/`Map` not JSON.
3. **Per-endpoint auth:** do NOT blanket-inject `Bearer`. Implement an `authMode` per call: `bearer | none | empty | literalToken | raw`. Most endpoints are `none` (user id in body) or `bearer`.
4. **401 interceptor:** on 401 call `auth/refresh {id, token}`, store new token, retry once; 2nd failure → clear session → login.
5. **Response envelope:** parse `{ message, ... }`; success = HTTP 200/201 AND message matches the documented string. Centralize the success-string checks (consider asking backend for a boolean/status-code contract).
6. **Null-safety:** treat empty string ≈ null (Android converts null→""). Many fields are numeric-or-string — use tolerant parsers (`num.tryParse(value.toString())`).
7. **Timeouts:** Android used 1000s; use sane Flutter timeouts (e.g. 30–60s) unless long polling is required.

---

## 5. Things to deliberately FIX or DECIDE during the rewrite

(See the "UNKNOWN / REQUIRES BACKEND CONFIRMATION" section of `USER_APP_DOCUMENTATION.md`.)

1. **Social-login token bug** — Android persists placeholder tokens (`"1256"/"32556"`, `"123"/"token"`). Persist the real `access_token` and confirm the backend contract.
2. **Past-job rating header** — Android sends a hardcoded `"Bearer 123"`. Send the real token.
3. **Wallet top-up** — disabled in Android (`customerAddMoney`/`CaptureWalletRecharge` commented/dead). Decide whether to implement.
4. **Scheduled booking** — `scheduledBooking` endpoint is dead; scheduled = `requestService` + `trip_type=2`. Keep that, or wire `scheduledBooking` per backend.
5. **Dead endpoints** — `checkService`, `buySubscription`, `subscriptionGetList`, `verifyMobile`, `forgot_password`, `auth/SocialLog`, Google Directions. Confirm before implementing.
6. **Success-by-message-string** — replace with a proper status contract if the backend team agrees.
7. **SharedPreferences-as-navigation** — Android passes booking input via `MY_PREFS_REQUEST_STATE`. In Flutter, pass it via a `BookingProvider` / typed route args instead (cleaner, testable).
8. **Secure token storage** — move tokens from plain SharedPreferences to `flutter_secure_storage`.
9. **Firebase parity** — you MUST reuse the same Firebase project and node schema (`users`, `providers`, `drivers_location`, `booking_trips`, `direction`, `Notifications`, `currency`). Confirm security rules allow the Flutter app (anonymous auth is used).
10. **GeoFire** — pick a maintained Flutter geo-query lib compatible with the existing `drivers_location` geohash format, or replicate the query server-side.

---

## 6. Build / config parity checklist

- `applicationId`/bundle id: `com.instaguard.user` (PayPal return `com.instaguard.user://paypalpay`, FCM, OAuth all depend on it).
- Google Maps API key (was in `local.properties`) — **`[SECRET/REDACTED]`**, provide via `--dart-define` / platform config.
- `google-services.json` / `GoogleService-Info.plist` — reuse the existing Firebase project — **`[SECRET/REDACTED]`**.
- Stripe publishable key comes from `appsetting` at runtime (DEBUG fallback `pk_test_...`). Do the same; never hardcode the secret key client-side.
- PayPal client id + environment from `appsetting` (`paypal_*`, `PAYPAL_TYPE`).
- Twilio tokens fetched at runtime (`chatAccessToken`/`accesstoken`).
- Supported locales: `fi, en, ar, si` (+ language list from `appsetting`). RTL support for Arabic.
- Min SDK 24 / target 34 equivalent; iOS parity required (the native app is Android-only).
- Permissions to declare: location (fine/coarse), notifications, camera, photos/storage, contacts, phone/call, microphone (Twilio).

---

## 7. Suggested build order

1. Core: Dio client + auth modes + 401 refresh; prefs/secure storage; Firebase init; `appsetting` bootstrap; models.
2. Auth + splash/session routing.
3. Home/map + location + Categories/Service + banners.
4. Guard discovery (GeoFire) + provider list.
5. Fare estimate + promo + payment services (Stripe first) + **create booking**.
6. Active job (Firebase state machine + map tracking) + settle/cancel/extend + rating.
7. History (ongoing/upcoming-paged/past) + Twilio chat/call.
8. Wallet + subscription + profile/settings + emergency/SOS + documents + support + offers + FCM.

Booking + active-job (steps 5–6) are the highest-risk, highest-value areas — prioritize and validate against the backend early.
