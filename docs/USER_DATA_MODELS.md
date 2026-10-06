# InstaGuard User App — Data Models & Local Storage

> All model classes are under `app/src/main/java/com/instaguard/user/Models/`.
> Models use Gson `@SerializedName`. **Null strings deserialize to `""`** (custom adapter).
> Many fields are typed `Object` in the Java source (can arrive as number or string) — marked `Object` below; parse defensively in Flutter.

---

## 1. Auth & account models

### UserData (`Models/UserData.java`) — the logged-in user
`id`, `first_name`, `last_name`, `social_id`, `social_type`, `avatar`, `email`, `country_code`, `phone_number`, `birth_date`, `gender`, `phone_verified`(Int), `email_verified`(Int), `address`, `city`, `state_id`, `country_id`, `postal_code`, `device_type`, `device_token`, `login_token`, `reset_pass_token`, `language`, `currency`, `wallet_balance`(Double), `last_seen`, `login_status`, `status`(Int), `created_at`, `updated_at`, `referral_code`, `invite_code`, `salt`, `currency_symbol`.

### LoginNewModel (`Models/LoginNewModel.java`)
`message` (`@SerializedName("...")`), `result: LoginModel`. **LoginModel:** `result.customer: UserData`, `access_token`, `token_type`, `expires_in`(Int).

### RegisterModel / RegisterData
`RegisterModel { message, result: RegisterData }`. `RegisterData` ≈ `UserData` shape.

### EmailMobileCheckingModel
`message`, `email_available`(Boolean), `phone_number_available`(Boolean).

### ForgetPassModel / ForgetPassData
`ForgetPassModel { message, result: ForgetPassData }`.

### VerifyOtpModel / VerifyOtpData
`VerifyOtpModel { message, result: VerifyOtpData }`; `VerifyOtpData` has `id` (used as reset target).

### VerifyMobileModel · ResetPasswordModel · ChangePasswordModel · ForgotPasswordModel
`VerifyMobileModel { id, message }` · `ResetPasswordModel { id, message }` · `ChangePasswordModel { message }` · `ForgotPasswordModel { message }` (dead path).

### LogoutModel · DeleteModel / DeleteData · RefreshTokenModel
`LogoutModel { message }`. `DeleteModel { message, result_message, data: DeleteData }`; `DeleteData { driver_id, user_type, date_time, mobile_type, unique_code(Int), status(Int), email }`. `RefreshTokenModel { access_token, token_type, expires_in(Int) }`.

### Social models
`SocialLoginModel { msg→message, result: SocialLoginDataModel, access_token, token_type, expires_in(Int) }`. `SocialSignupModel { id(Int), result: SocialSignupData, msg }`. `LinkedInUserDetailsModel` (sub, name, given_name, email).

### App settings
`AppSettingWebModel { data:[AppSupportDataModel], app_setting: AppSetting, sms_templates:[AppSettingSmsTemplate], currency:[AppSettingCurrencyModel], language:[AppSettingLanguageModel], message }`.
**AppSetting** (`Models/AppSetting.java`) key fields: `id, site_name, phone_verification/email_verification(+driver)(Int), country_code, distance_unit, wallet_minimum(Double), referral_amount(Double), approximate_time, timedelay_accept, payment_publisher_key, payment_secret_key, payment_mode, enable_commission, fb/twitter/google/linkedin keys + *_login_user flags, driver_search_radius, pass_phrase, distance_limit(Int), android_app_link, iphone_app_link, police_number, tawk_url, property_url, property_id, document_norms, paypal_*(client_id/secret/type/urls/status), vat_percentage(String), guard_subscription(Int), customer_manage_document(Int), timeout_delay(Int)`.
`AppSettingCurrencyModel` (code, symbol, ratio), `AppSettingLanguageModel` (name, short_name, country), `AppSettingSmsTemplate` (template content for SOS).

---

## 2. Profile models

### ProfileModel / ProfileData
`ProfileModel { data: ProfileData, rating(Double), message }`.
**ProfileData:** `id`(Int), `first_name`, `last_name`, `social_id`(Obj), `social_type`(Obj), `avatar`(Obj), `email`, `country_code`, `phone_number`, `birth_date`(Obj), `gender`(Obj), `phone_verified`(Int), `email_verified`(Int), `address`(Obj), `city`(Obj), `state_id`(Obj), `country_id`(Obj), `postal_code`(Obj), `device_type`, `device_token`(Obj), `login_token`(Obj), `reset_pass_token`(Obj), `language`, `currency`, `wallet_balance`(Double), `last_seen`, `status`(Int), `created_at`, `updated_at`, `referral_code`(Obj), `invite_code`, `salt`(Obj), **+subscription block:** `subscription_id, subscription_status, subscription_name, subscription_amount, subscription_purchase, subscription_expiry, plan_type, free_km(Int), free_job(Int), subscription_description, period_type(Int), period(Int), remeaning_job(Int), subscription_code, maximum_discount_amount, tow_service_id, appointment_service_id, subscription_history_id(Int), auto_renew(Int), discount_percentage(Int), free_appointment_services`.

### ProfileUpdateModel / ProfileUpdateData
`ProfileUpdateModel { data: ProfileUpdateData, message }`; `ProfileUpdateData` ≈ ProfileData + `login_status`(Obj), `currency_symbol`.

### ProfilePicModel / ProfilePicData
`ProfilePicModel { data: ProfilePicData, message }`; `ProfilePicData { avatar }`.

---

## 3. Discovery models

### VehicleCategoryModel / VehicleCategoryData
`{ message, result:[VehicleCategoryData] }`. **VehicleCategoryData:** `id, category_name, category_name_spanish, category_image, category_type, level_type, level_amount`.

### VehicleSubCategoryModel / VehicleSubCategoryData
`{ message, result:[VehicleSubCategoryData] }`. **VehicleSubCategoryData:** `id, category_id, service_name(+_french/_spanish/_swati), service_image, fixed_fare, base_fare, visit_fare, price_per_hour, price_per_km, price_per_agent, price_per_day, price_per_min, user_cancellation_charge, user_cancellation_time, admin_commission, amount_type, status, has_level, Level_entry, book_guard_min_time`.

### CategoryModel / CategorySubModel
Plain UI POJOs for the category/service pickers.

### FareEstimateModel / FareEstimateData (`Models/FareEstimate/`)
`{ message, result: FareEstimateData }`. **FareEstimateData (all `Object`):** `promo_discount, promo_status, promo_code, base_fare, total_hrs, price_per_hr, price_per_guard, total_guard_amount, before_vat_amount, overall_total, wallet_status, wallet_amount, subscription_status, subscription_percentage, subscription_amount, sub_total`.

### CheckAvailablityModel · BannerModel / BannerData · CheckPromoModel / CheckPromoData
`CheckAvailablityModel { message, result (CSV driver ids) }`. `BannerData { id, banner_id, images, status, title, description, created_at, updated_at }`. `CheckPromoData { id, coupon_code, description(+french/spanish/swati), discount(String %), discount_type, validity, activation_date, expiry_date, usage_limit, status, created_at, updated_at }`.

### UniqeIdResModel
`{ message, result: List<String> }` (guard reference ids).

### ProviderListData (`Models/ProviderListData.java`) — Firebase POJO (no @SerializedName)
`id, fname, lname, propic, mobile, email, status, serviceDesc, miles, rating, currentDate, currentTime, companyName, providerType`.

### FirebaseGuardListModel · FirebaseModel · TripsFirebaseGuardListModel · TripsFirebaseGuardRatingListModel · GroupDriverDatum
Firebase POJOs for guard-location entries and group-booking guard lists/ratings.

---

## 4. Booking request/response models

### SendRequestModel / ServiceRequest (`Models/Request/`)
`SendRequestModel { success(Boolean), message, service_request: ServiceRequest }`.
**ServiceRequest:** `booking_id`(**Integer**), `job_number`, `request_status`, `service_status`(Int), `category`, `category_id`, `service_id`, `customer_id`, `provider_ids`, `mode`, `payment_type`, `promocode`, `currency`, `customer_first_name`, `customer_last_name`, `customer_email`, `customer_phone_number`, `strip_id`, `created_at`, `updated_at`, + echoed location/drop fields.

### ScheduleRequestModel / ScheduleRequestData (dead path)
`ScheduleRequestModel { success, message, service_request: ScheduleRequestData }`; `ScheduleRequestData { booking_id(Int), driver_id, job_date, request_time, request_date, service_status(Int), cancel_service_status(Int), unit_price(Double), site_commission(Double), total_amount(Double), driver+customer name/email/phone, strip_id, created_at, updated_at }`.

### CancelJobModel / CancelJobDataModel · UpcomingCancelJobsModel
`CancelJobModel { data: CancelJobDataModel (has service_status Int), message }`. `UpcomingCancelJobsModel { booking_id, message }`.

### RatingDriverModel / RatingModel
`RatingDriverModel { message }`. `RatingModel` UI POJO.

---

## 5. Jobs history models

### OnGoingJobsModel / OnGoingJobsData
`{ success, service_request:[OnGoingJobsData], message }`.
**OnGoingJobsData** (all String except `service_status:Integer`): `booking_id, job_number, job_date, driver_name, customer_name, customer_location, driver_avatar, service_name, category_name, payment_type, total_amount, unit_price, site_commission, extra_charge, misc_charge, driver_discount, service_status(Int), accept_time, rating, driver_id, category_ar, service_ar, company_name`.

### RideHistoryModel / RideHistoryData (paginated upcoming)
`RideHistoryModel { result:[RideHistoryData], message, page, next_page(Integer) }`.
**RideHistoryData** (Serializable): `id`(Int), `service_status`(Int), `start_date_time`, `end_date_time`; **all other ~60 fields `Object`** (job_number, job_date, customer_*, service_id, customer_location, category_id, driver_*, provider_type, company_name, total_distance, total_amount, unit_price, total_agent, total_days, price_per_*, vat_amount, promocode, payment_type, …).

### UpcomingJobsModel / UpcomingJobsData (legacy/dead)
`UpcomingJobsData { booking_id(Int), total_amount/unit_price/site_commission/extra_charge/misc_charge/driver_discount/rating(Double), service_status(Int), job_date, driver_name, customer_location, service_name, category_name, payment_type, driver_phone_number, company_name, total_agent, total_days, start_date_time, end_date_time, category_ar, service_ar }`.

### PastJobsModel / PastJobsData
`{ success, service_request:[PastJobsData]→getPastJobsData(), message }`.
**PastJobsData:** `rating_status, rating(Float), booking_id(Int), job_number, job_date, job_type, driver_name, customer_name, customer_location, service_name, category_name, payment_type, total_amount(Double), unit_price(Int), site_commission(Int), extra_charge/misc_charge/driver_discount(Double), price_per_km(Int), total_distance(Double), level_type(Int), main_level_amount/sub_level_amount/visit_fare(Double), service_status(Int), accept_time, customer_avatar, driver_avatar, customer_id(Int), driver_id(Int), promocode, promo_discount(Double), category_ar, service_ar, company_name, total_agent/total_days/price_per_day(String)`, + `Object`: `emergency_msg, start_date_time, end_date_time, extended_amount, extended_vat, vat_amount, group_driver_id, group_driver_data:[GroupDriverDatum], total_hrs, price_per_hr, extended_status, extended_hrs, trip_type, price_per_agent, subscription_status, discount_amount, discount_percentage`.

---

## 6. Wallet models

### WalletAllModel / WalletAllData · TransactionModel
`WalletAllModel { data:[WalletAllData], message }`.
**WalletAllData:** `id(Int), transaction_token(Int), customer_id(Int), driver_id(Obj), transaction_type (credit/debit), transaction_date, transaction_amount(Double), transaction_mode (card/stripe/wallet/stripe_gpay/paypal), card_number(Obj), transaction_notes, transaction_notes_ar, transaction_status(Int), created_at, updated_at`.
`TransactionModel` (UI POJO): TransactionType/Amount/Date/Notes/NotesAr/Mode/is_selected.

### WalletAddMoneyModel / WalletAddMonetDataModel (disabled path)
`{ success, service_request: WalletAddMonetDataModel, wallet_balance(Double), balance_amount(Double), message }`.

### BalanceWalletAddMoneyModel / BalanceWalletAddMonetDataModel
`{ service_request: BalanceWalletAddMonetDataModel (customerWalletBalance, driverWalletBalance, balanceAmount), message }` — used by Paycard/Paypaypal/customerTripPayment/payment_success.

---

## 7. Payment models (`Models/Payment/`)

- **PaymentIntentModel** `{ client_secret, result: PaymentIntentModelResult { id } }`
- **WalletPaymentIntentModel** `{ client_secret, result: WalletPaymentIntentResult { id } }` (dead)
- **SaveWalletPaymentIntentModel** `{ client_secret, payment_intent_id, action_url, action_url_status, wallet_status, result: SaveWalletPaymentIntentResult { id, payment_method } }`
- **PayPalCreateModel** `{ id, message, status }`
- **InternalPaymentMethod_** (UI POJO): payment-method chooser items (id 1=card, 2=paypal, 3=gpay, 4=wallet).

---

## 8. Subscription models (`Models/Subscription/`)

- **ListViewSubsModel** `{ result:[ListViewSubsData], message }`; **ListViewSubsData** `{ id(Int), name, description(Obj), amount(Obj), period(Int), period_type(Obj), free_km(Int), free_job_request(Int), status(Obj), plan_type(Obj), created_at, updated_at }`.
- **ViewSubsModel / ViewSubsData** (stub) ≈ ListViewSubsData.
- **BuyPlanWebHookModel** `{ result: BuyPlanWebHookData { id, url }, message }`.
- **BuyPlanModel** `{ result(String), message }`.
- **UpdateSubsModel / UpdateSubsData** `{ order_id, cus_id, plan_id, purchase_date, expiry_date, status, subscription_name, subscription_amount, … }` (superseded).
- **SubsHistoryModel / SubsHistoryData** `{ data:[SubsHistoryData], message }`; **SubsHistoryData** `{ id(Int), order_id, cus_id(Int), plan_id(Int), purchase_date, expiry_date, plan_period, amount(Double), status(Int), created_at, updated_at, name, auto_renew(Int) }`.
- **CancelAutoRenewPlanModel** `{ message }`.
- **CheckPlanModel / CheckPlanData** (dead) `{ plan_id, plan_name, subscription_description, period, period_type, subscription_code, expiry_date, amount(Double), maximum_discount_amount(Int), tow_service_id, appointment_service_id, status }`.

**plan_type:** 0/1 = roadside/assistance, 2 = home appointment, `"1,2"` = both. **period_type:** 0 days, 1 month(ly), 2 year(ly). **status:** 1 active, 2 expired. **auto_renew:** 0 off, 1 on.

---

## 9. Emergency / panic / support / document models

- **EmergencyAddModel** `{ result: EmergencyAddDataModel {customer_id, phone_number, status, contact_name}, message }`
- **EmergencyViewModel** `{ data:[EmergencyViewDataModel {id(Int), contact_name, customer_id(Int), phone_number}], message }`
- **EmergencyDeleteModel** `{ message }` · **EmergencyModel** (UI POJO)
- **PanicAddAdminModel** `{ response: PanicAddAdminDataModel {customer_id, panic_date, updated_at, created_at, id(Int)}, message }`
- **AppSupportModel** `{ data:[AppSupportDataModel {id(Int), description (HTML)}], message }`
- **AppSupportPagesModel** `{ data:[AppSupportPagesData {id(Int), description, meta_title, meta_description, page_name, status(Int)}], message }`
- **Documents (`Models/DocumentsUpload/`):**
  - `DocumentListData { data:[DocumentList], message }`
  - `DocumentList { id(Int), document_name, status(Int), country_id(Int), expire_on(Obj), document_name_ar, created_at, updated_at, car_id(Int), driver_id(Int), document, document_proof, approved_status(Int), expiry_date, expiry_status(Int), reason, document_priority(Obj) }`
  - `DocumentViewModel { result:[DocumentList], reject_status(Int), message }`
  - `UploadModel { result (path String), message }`
  - `UploadDocModel { result: UploadDocData {driver_id, id, document_id, document}, message }`
  - `ReUploadModel { result: ReUploadData {driver_id, id, document_id, document}, message }`

- **ChatAccessModel** `{ identity, token }` · **PushModel** `{ message }` · **BankModel** `{ message }` (generic).

---

<a name="local-storage"></a>
## 10. Local Storage (SharedPreferences) — complete inventory

No Room/SQLite/DataStore. All persistence is SharedPreferences, keys defined in `Helpers/Utils.java`.

### Store (file) names
| Store constant | File name | Purpose |
|----------------|-----------|---------|
| `MY_PREFS_NAME` | `Insta-Guard_Demo_Customer` | tokens, userId, profile, wallet, common/appsetting flags, subscription code |
| `MY_PREFS_NAME_NEW` | `..._Customer_face_finger` | biometric flags |
| `PREFS_NAME_CURRENCY` | `..._Customer_Currecny_All` | currency + language selection, FX ratio |
| `MY_PREFS_NAME_REMEMBER` | `..._Customer_rememberme` | remember-me |
| `MY_PREFS_REQUEST_STATE` | `..._Customer_Request_State` | booking input bundle (`B_*`) |
| `MY_PREFS_FB_CATEGORY_TRIPSTATE` | `..._Customer_category_tripstate` | pay-mode trip state |
| `GLOBAL_STORED_ID` | `..._Customer_global_stored` | FCM token (`regid`) |
| `SP_PAYMENT_HASHMAP_KEYS_` | `..._Customer_hashmap_` (+bookingId) | persisted payment request map (retry) |

### Session / token keys (store = `MY_PREFS_NAME`)
| Key | String | Holds |
|-----|--------|-------|
| `SP_USERID` (=`B_USERID`) | `..._userId` | **User ID** (customer_id/id/user_id) |
| `SP_USER_ACCESSTOKEN` | `..._accessToken` | **Bearer access token** |
| `SP_USER_TOKENTYPE` | `..._tokenType` | token type |
| `SP_USER_TOKENEXPTIME` | `..._tokenExpTime` | expiry |
| `SP_USER_CURRENT_BOOKING_ID` | `..._current_booking_id` | active booking id |
| `SP_MAP_BOOKID` | `..._bookid` | map booking id |
| `SP_USER_DELETE_ACCOUNT` | `..._delete` | delete-in-progress flag |
| `SP_USER_SOCIAL_LOGIN_TYPE` | `..._sociallogintype` | normal/facebook/google/twitter/linkedin |

### FCM
| Key | String | Holds |
|-----|--------|-------|
| `SP_DEVICE_ID` | `regid` (in `GLOBAL_STORED_ID`) | FCM device token |
| `SP_DEVICE_NOTIFY_TYPE` | `..._regid_notify` | notify type |

### Profile
`SP_USER_FNAME, SP_USER_LNAME, SP_USER_EMAIL, SP_USER_PASSWORD, SP_USER_DOB, SP_USER_PRO_PIC, SP_USER_STATE, SP_USER_COUNTRY, SP_USER_ADDRESS, SP_USER_MOBILE, SP_USER_COUNTRYCODE, SP_USER_WALLET_AMT, SP_USER_WALLET_AMT_MIN, SP_USER_REFERAL_CODE, SP_USER_INVITE_CODE`.

### Currency / language (store = `PREFS_NAME_CURRENCY`)
`SP_CURRENCY, SP_CURRENCY_SYMBOL, SP_CURRENCY_COMMON_VALUES (FX ratio), SP_LANGUAGE, SP_LANGUAGE_SHORT_NAME, SP_LANGUAGE_COUNTRY, PREFS_NAME_CURRENCY`.

### Remember-me (store = `MY_PREFS_NAME_REMEMBER`)
`SP_USER_EMAIL_REMEMBERME, SP_USER_PASS_REMEMBERME, SP_CHECKED_REMEMBERME`.

### Common / appsetting-derived (store = `MY_PREFS_NAME`)
`SP_COMMON_STRIPE_PUBLISH_KEY, SP_COMMON_STRIPE_SECRET_KEY, SP_COMMON_DISTANCE_LIMIT, SP_COMMON_DISTANCE_UNIT, SP_COMMON_REQUEST_TIME_DELAY_ACCEPT, SP_COMMON_TOWK_CHAT_URL, SP_COMMON_TOWK_PROPERTY_URL, SP_COMMON_TOWK_PROPERTY_ID, SP_COMMON_CUSTOMER_MANAGE_DOCUMENT, SP_COMMON_ADMIN_PAYPAL_STATUS, SP_COMMON_WALLET_STATUS, SP_COMMON_SUBSCRIPTION_STATUS`.

### Subscription
`SP_USER_SUBSCRIPTION_CODE, SP_USER_SUBSCRIPTION_CODE_ID` (active), + declared-but-writes-commented: `_DISCOUNT_PERCENTAGE, _REMAINING_COUNT, _SERVICE_IDS, _APPOINTMENT_SERVICE_IDS, _FREE_APPOINTMENT_SERVICE`.

### Trip / map state
`SP_MAP_TRIPSTATE, SP_MAP_TRIPSTATE_DISTANCE, SP_MAP_TRIPSTATE_DURATION, SP_FB_CATEGORY_PAYMODE_TRIPSTATE, MY_PREFS_REQUEST_STATE, MY_PREFS_FB_CATEGORY_TRIPSTATE`.

### Biometric (store = `MY_PREFS_NAME_NEW`)
`spFingerUserId, spFingerStatus, spFingerNoStatus, spFaceUserId, spFaceStatus, spFaceNoStatus`.

### Twilio
`identity, toIdentity, mIdentity`.

### Booking input bundle (`B_*`, store = `MY_PREFS_REQUEST_STATE` / Bundles)
`B_USERID, B_DRIVERID, B_CATEGORY_ID, B_CATEGORY_NAME(_AR), B_CATEGORY_TYPE, B_REQUEST_TYPE, B_SUB_CATEGORY_ID, B_SUB_CATEGORY_NAME(_AR), B_SUB_CATEGORY_PRICE, B_CATEGORY_GUARDS_COUNT, B_CATEGORY_DAYS, B_CATEGORY_TELL_US_EMERGENCY, B_CATEGORY_AGENT_PRICE, B_CATEGORY_DAYS_PRICE, B_CATEGORY_LEVEL_TYPE, B_CATEGORY_LEVEL_AMOUNT, B_PICKUP(_LAT/_LNG), B_DROP(_LAT/_LNG), B_CURRENT_LAT/LNG, B_DISTANCE(_LABEL), B_DURATION(_LABEL), B_TOTAL_EST_AMT, B_TRIP_TYPE, B_PICKUP_DATE/TIME(_END)(_LABEL), B_SCH_TRIP_DRIVERID, B_AUTOCOMPLETELAYOUT, B_PICKUP/DROPAUTOCOMPLETELAYOUT, B_NORMALSCHEDULETRIP, B_EDITSCHEDULETRIP, B_EDIT_* (schedule edit set), B_WALLET_RECHARGE_AMOUNT, B_WALLET_PAGE, B_SUPPORT*, B_ABOUTUS/B_TERMS/B_PRIVACY/B_CONTACTUS/B_FAQ`.

---

## 11. Firebase Realtime Database schema (observed)

| Node | Children used |
|------|---------------|
| `users/<userId>` | `wallet`, `balance_amount`, `rating`, `currency` (`"CUR:symbol"`), `language`, `delete_status`, `fname/lname/email/cc/mobile/pro_pic`, `subs_code`, `remaining_job`, `subs_buy_status`, child `trips/{accept_status, booking_id}` |
| `providers/<driverId>` | `status`, `category` (CSV service ids), `fname/lname/pro_pic/email/mobile`, `rating`, `service_desc`, `company_name`, `provider_type`, child `trips/{service_status, customer_id, customer_name, card_status, paypal_status, cash_status}` |
| `drivers_location/<driverId>` | GeoFire node (`g`, `l="[lat,lng]"`, `bearing`) |
| `booking_trips/<bookingId>` | `service_status`, `start_time`, `reach_time`, fare fields, `card_status`, `paypal_status`, `cash_status`, `balance_amount`, `request_status`, `rating_status`, child `drivers/<driverId>/{trip_status, rating_status}` |
| `direction/<driverId>` | `direction` (JSON polyline) |
| `currency/<code>` | FX ratio |
| `Notifications/Offers/user`, `Notifications/Announcements/user` | offer/announcement items (filter `status==1`) |
