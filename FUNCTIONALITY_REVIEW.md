# InstaGuard User App — Full Functionality Review

## Overview

**InstaGuard User** is an Android application that lets customers book on-demand or scheduled security guard services. Users can search for guards near them, book instantly or in advance, track the guard in real time on a map, pay through multiple payment methods, and manage their account and emergency contacts.

| Property | Value |
|---|---|
| Package | `com.instaguard.user` |
| Version | 1.2 (versionCode 3) |
| Min SDK | 24 (Android 7.0) |
| Target SDK | 34 (Android 14) |
| Language | Java |
| Backend | `https://backend.insta-guard.co.uk/api/v3/` |
| Supported Languages | English, Arabic, Finnish, Sinhala |

---

## Application Architecture

| Layer | Technology |
|---|---|
| Networking | Retrofit 2 + OkHttp 3 |
| Real-time data | Firebase Realtime Database |
| Auth | Firebase Auth |
| Push notifications | Firebase Cloud Messaging (FCM) |
| Maps | Google Maps SDK, Directions API, Distance Matrix API |
| Location | Fused Location Provider, Geocoder, GeoFire |
| Image loading | Fresco, Picasso |
| Payments | Stripe 17.1.2, PayPal Native SDK, Google Pay |
| Communication | Twilio Voice + Conversations |
| Biometrics | AndroidX Biometric |
| Social auth | Facebook SDK 13, Google Sign-In, Twitter4J, LinkedIn OAuth |
| Crash reporting | Firebase Crashlytics |
| Analytics | Firebase Analytics |
| Background work | WorkManager |
| Animations | Lottie |
| Event bus | GreenRobot EventBus |
| Data binding | Android Data Binding |

---

## Screens / Activities

| Activity | Role |
|---|---|
| `LaunchActivity` | Splash screen — entry point of the app |
| `HomeActivity` | Hosts auth fragments (login, register, social login) |
| `HomeMainActivity` | Auth support activity |
| `MainActivity` | Main dashboard — map, booking, navigation drawer |
| `MainJobProgressActivity` | Live job/trip progress with real-time map tracking |
| `ProviderListActivity` | Browse and select from a list of available guards |
| `DocumentActivity` | Upload and manage identity/verification documents |
| `SettingsFaceFingerscreen` | Biometric (face / fingerprint) authentication setup |

---

## Feature-by-Feature Breakdown

### 1. Authentication

**Screens**: `LoginFragment`, `RegisterFragment`, `ForgotPasswordFragment`, `SocialSignupFragment`

- Email/password login with optional "Remember Me"
- New user registration (name, email, mobile with country-code picker, DOB, password)
- Email and mobile uniqueness check before registration (`emailAvailable`)
- Forgot password → OTP verification → reset password flow
- Social login:
  - Google Sign-In
  - Facebook Login (with graph API profile fetch)
  - LinkedIn OAuth (OpenID Connect — openid, profile, email)
  - Twitter (Twitter4J)
- Social sign-up completion screen for first-time social users (fill missing fields)
- Token refresh handled automatically on 401 responses

---

### 2. Home / Dashboard (`MainActivity`)

- Google Map as the main interface
- Detects user's current location via Fused Location Provider
- Navigation drawer with links to all sections
- Displays app-wide banner images (promotional sliders)
- Language and currency switcher
- Shows wallet balance from Firebase Realtime Database in real time
- Service category selection (guard type) → subcategory → booking flow
- Unique guard ID entry (`GuardUniqueIdFragment`) to request a specific guard

---

### 3. Service Booking

**Screens**: `SubCategoryFragment`, `ScheduleTripFragment`, `PlacesAutoCompleteFragment`

#### Instant Booking
- Select service category and subcategory
- Enter pickup location (manual or GPS) via Google Places autocomplete
- Enter drop/destination location (if applicable)
- Check guard availability (`availabilityCheckService`)
- View fare estimate (`FareEstimate`)
- Apply promo code (`checkPromoCode`)
- Choose payment method (Wallet / Card / PayPal / Google Pay)
- Send service request (`requestService`)

#### Scheduled Booking
- Select date with Material DatePicker
- Select time from available time slots (grid)
- Select start/end date and time for extended bookings
- Validates time in future and slot availability
- Sends scheduled booking (`scheduledBooking`)

#### Guard Unique ID Booking
- Enter a specific guard's reference ID
- Validated against server (`driverReferenceIdValidate`)

---

### 4. Real-Time Job Tracking (`MainJobProgressActivity`)

- Google Maps showing guard's real-time GPS location (Firebase Realtime DB)
- Animated polyline between user and guard (dual-color animation)
- Job status flow: **Pending → Accepted → In Progress → Completed**
- Ripple animation on map during guard search
- Chronometer showing elapsed job time
- Option to extend job hours during active job
- Extended fare calculation (`ExtraCalFare`)
- Job completion → rating and feedback screen
- Cancel job option with confirmation
- Request timeout handling (`customerTimeout`)
- Twilio in-app voice call button to contact guard
- Twilio in-app chat button to message guard
- PayPal deep-link callback support for payment completion

---

### 5. Provider / Guard List (`ProviderListActivity`, `ProviderListAdapter`)

- Displays a list of available guards for the requested service
- Guard details: name, photo, rating, badge/rank
- Select a specific guard to send the request to

---

### 6. Job History (`YourJobsFragment`)

Three tabs managed via fragment:

| Tab | Fragment | Description |
|---|---|---|
| Ongoing | `OnGoingJobsFragment` | Active jobs in progress |
| Upcoming | `YourJobsUpcomingFragment` | Scheduled/future jobs with cancel option |
| Past | `YourJobsPastFragment` | Completed job history with rating |

- Offset/pagination supported for upcoming jobs
- Cancel upcoming jobs (`scheduledcancelJobs`, `requestService` cancel)
- Rate past jobs with star rating

---

### 7. Calendar Booking View (`MyCalenderBookingFragment`)

- Web-view embedding of the backend calendar (`/calender/customer/`)
- Shows all bookings on an interactive calendar interface

---

### 8. Wallet (`MyWalletFragment`)

- Displays current wallet balance (synced from Firebase Realtime DB in real time)
- Quick top-up amount buttons (preset amounts)
- Custom amount entry with validation
- Add money via Stripe card payment
- View wallet terms & conditions (`WalletTermsFragment`)
- Navigate to transaction history

---

### 9. Payments

Multiple payment methods are supported throughout the app:

| Method | Integration |
|---|---|
| Stripe | 3D Secure flow, save card, wallet recharge |
| PayPal | Native SDK + web payments, deep-link callback |
| Google Pay | Google Wallet API |
| In-app Wallet | Balance deducted, real-time Firebase sync |

**Stripe flows** (`StripePaymentFragment`):
- Create Payment Intent (`StripePayment3DApp`)
- 3DS authentication handled in-app
- Save card for future payments (`CreateSaveCardSetupIntent`)
- Use saved card for payment (`StripeSaveCardPayment3DApp`)
- Wallet recharge via Stripe (`CaptureWalletRecharge`)

**PayPal flows**:
- Create order (`createOrder`)
- Complete payment (`Paypaypal`)
- Deep-link return URL: `com.instaguard.user://paypalpay`

---

### 10. Transaction History

Three tabs in `YourWalletTransactionFragment`:

| Tab | Fragment | Description |
|---|---|---|
| All | `YourWalletTransAllFragment` | All wallet transactions |
| Money In | `YourWalletTransMoneyInFragment` | Top-ups and credits |
| Money Out | `YourWalletTransMoneyOutFragment` | Payments and debits |

Powered by `Transaction` API endpoint with adapter `TransactionAllAdapter` / `TransactionMoneyInOutAdapter`.

---

### 11. Subscription Plans (`SubscriptionPlanFragment`)

- List all available subscription plans (`subscriptionList`)
- View active user subscription (`subscriptionGetList`)
- Subscribe via Stripe Checkout webhook session (`createCheckoutSession`)
- Buy plan directly (`buySubscription`)
- Cancel auto-renewal (`updateAutoRenew`)
- Check if a service is covered by subscription (`checkService`)
- Subscription history (`SubscriptionPlanHistoryFragment`)
- Discounts applied automatically when booking covered services

---

### 12. Emergency Contacts (`EmergencyContactsFragment`)

- Add emergency contacts manually (name + phone number)
- Import contacts directly from the phone's address book (contacts permission)
- View list of saved emergency contacts
- Delete individual emergency contacts
- Panic / SOS button → sends alert to all emergency contacts (`savepanic`)

---

### 13. Document Management (`DocumentActivity`)

- List required documents fetched from backend
- Upload identity documents (image from camera or gallery)
- Image compression before upload (`ImageZipper`)
- Re-upload rejected/expired documents (`customerProofReupload`)
- View document status (pending / approved / rejected)
- Manage documents list (`manageCustomerDocument`)

---

### 14. Profile Management (`EditSettingsFragment`, `SettingsFragment`)

- View profile details (name, email, mobile, DOB, address, country, profile picture)
- Edit profile fields and save changes (`updateprofile`)
- Upload/change profile photo (`picupload`)
- Change password (`auth/passwordChange`)
- Country code picker for mobile number
- Delete account: two-step — request (`delete_account_request`) → confirm (`delete_account_confirm`)

---

### 15. Biometric Authentication (`SettingsFaceFingerscreen`)

- Fingerprint unlock — toggle on/off via `SwitchButton`
- Face recognition unlock — toggle on/off
- Uses AndroidX Biometric API (`BiometricPrompt`)
- Falls back to device credential (PIN/pattern/password)
- Status persisted in SharedPreferences

---

### 16. In-App Communication (Twilio)

- **Voice calling**: Twilio Voice SDK — token fetched via `accesstoken` endpoint
- **Chat/Messaging**: Twilio Conversations SDK — token via `chatAccessToken`
- Identity management for user-to-guard communication
- Message notification triggered server-side (`message_notify`)
- Push notification sent on new message (`notifysend`)

---

### 17. Announcements & Offers

| Fragment | Data Source | Description |
|---|---|---|
| `YourAnnocementFragment` | Firebase Realtime DB (`Announcements`) | App-wide announcements |
| `YourAnnocementsOffersFragment` | Firebase Realtime DB (`Offers`) | Promotional offers |
| `YourOffersFragment` | Firebase Realtime DB | Offers listing |

Real-time listener — updates appear instantly without polling.

---

### 18. Support (`SupportFragment`, `SupportSubFragment`)

- Browse support topics/categories (`Support` endpoint)
- View support sub-items / articles (`pages` endpoint)
- Expandable panel layout for FAQ-style content

---

### 19. Invite Friends (`InviteFriendsFragment`)

- View user's referral/invite code
- Share invite code via Android share sheet
- Referral code stored in profile and used during registration

---

### 20. Multi-Language Support

- Supported locales: `en`, `ar`, `fi`, `si`
- RTL layout support for Arabic
- In-app language switcher (stored in SharedPreferences)
- Locale change handled without full app restart (`LocalizationActivity`)
- Language config: `res/xml/locales_config.xml`

---

### 21. Multi-Currency Support

- Currency selection from list (`AppSettingCurrencyModel`)
- Currency symbol shown throughout the app
- Conversion ratio stored locally for display
- Currency setting persisted in SharedPreferences

---

### 22. Push Notifications (`MyFirebaseMessagingService`)

- FCM token registered on login and refreshed automatically
- Notification types: new booking, job accepted, job started, job completed, payment, offers
- Custom notification channel (`default_notification_channel_id`)
- Deep-link navigation from notification tap

---

### 23. App Settings (`appsetting` endpoint)

On launch, the app fetches global config:
- Stripe publish/secret keys
- Distance limit and unit (km/miles)
- Payment method toggles (wallet, PayPal, subscription)
- Request acceptance time delay
- Twilio chat/property URLs
- Banner images
- Currency and language options

---

### 24. Ads

- **Google AdMob** — banner and interstitial ads
- **Facebook Audience Network** — additional ad inventory
- **Google UMP** — consent management for GDPR/CCPA

---

## API Endpoint Reference

### Authentication
| Method | Endpoint | Description |
|---|---|---|
| POST | `auth/login` | Login with email/password |
| POST | `auth/register` | Register new user |
| POST | `auth/logout` | Logout |
| POST | `auth/refresh` | Refresh access token |
| POST | `auth/SocialLog` | Social login |
| POST | `auth/passwordChange` | Change password |
| POST | `customer/socialAdd` | Add social user |
| POST | `customer/socialUpdate` | Complete social signup |
| POST | `emailAvailable` | Check email/mobile uniqueness |
| POST | `forgetpass` | Initiate forgot password |
| POST | `verifyotp` | Verify OTP |
| POST | `updatePassword` | Reset password |
| POST | `verifyMobile` | Mobile verification |
| POST | `forgot_password` | Forgot password (alt) |

### Profile
| Method | Endpoint | Description |
|---|---|---|
| POST | `profileView` | Get profile data |
| POST | `updateprofile` | Update profile |
| POST | `picupload` | Upload profile picture |
| POST | `delete_account_request` | Request account deletion |
| POST | `delete_account_confirm` | Confirm account deletion |

### Booking & Jobs
| Method | Endpoint | Description |
|---|---|---|
| POST | `requestService` | Send booking request |
| POST | `scheduledBooking` | Create scheduled booking |
| POST | `cancelJobs` | Cancel an active job |
| POST | `scheduledcancelJobs` | Cancel a scheduled job |
| POST | `customerongoing` | Get ongoing jobs |
| POST | `upcomingJobs` | Get upcoming jobs |
| POST | `CustomerUpcomingJob` | Upcoming jobs (paginated) |
| POST | `pastjobs` | Get past jobs |
| POST | `UpdateJob` | Extend job hours |
| POST | `FareEstimate` | Get fare estimate |
| POST | `ExtraCalFare` | Fare for extended hours |
| POST | `checkPromoCode` | Validate promo code |
| POST | `availabilityCheckService` | Check guard availability |
| POST | `CustomerFeedbackStore` | Submit rating/feedback |
| POST | `customerTimeout` | Handle request timeout |
| POST | `driverReferenceIdValidate` | Validate guard unique ID |

### Categories
| Method | Endpoint | Description |
|---|---|---|
| POST | `Categories` | Get service categories |
| POST | `Service` | Get service subcategories |

### Wallet & Payments
| Method | Endpoint | Description |
|---|---|---|
| POST | `customerAddMoney` | Add money to wallet |
| POST | `customerTripPayment` | Pay for completed trip |
| POST | `Transaction` | Get transaction history |
| POST | `Paycard` | Card payment |
| POST | `createOrder` | Create PayPal order |
| POST | `Paypaypal` | Complete PayPal payment |
| POST | `StripePayment3DApp` | Stripe 3DS payment intent |
| POST | `CreateSaveCardSetupIntent` | Stripe save card setup |
| POST | `CaptureWalletRecharge` | Stripe wallet recharge |
| POST | `StripeSaveCardPayment3DApp` | Saved card payment |

### Emergency & Safety
| Method | Endpoint | Description |
|---|---|---|
| POST | `emergencyContactCustomer` | Add emergency contact |
| POST | `emergencyContactGET` | Get emergency contacts |
| POST | `emergencyContactDelete` | Delete emergency contact |
| POST | `savepanic` | Trigger panic/SOS alert |

### Documents
| Method | Endpoint | Description |
|---|---|---|
| POST | `customerImageUpload` | Upload document image |
| POST | `customerDocumentUpdate` | Update document record |
| POST | `customerProofReupload` | Re-upload rejected document |
| POST | `customerViewDocument` | View document details |
| POST | `manageCustomerDocument` | Manage documents list |

### Subscription
| Method | Endpoint | Description |
|---|---|---|
| POST | `subscriptionList` | List subscription plans |
| POST | `subscriptionGetList` | Get user's active subscription |
| POST | `buySubscription` | Purchase a plan |
| POST | `subscription_history` | Subscription purchase history |
| POST | `updateAutoRenew` | Enable/disable auto-renewal |
| POST | `checkService` | Check service subscription coverage |
| POST | `createCheckoutSession` | Create Stripe checkout session |
| POST | `checkSubscription` | Verify subscription status |

### Support & App Config
| Method | Endpoint | Description |
|---|---|---|
| POST | `Support` | Get support topics |
| POST | `pages` | Get support page content |
| POST | `appsetting` | Get global app settings |
| POST | `getbanners` | Get promotional banner images |

### Communication
| Method | Endpoint | Description |
|---|---|---|
| GET | `chatAccessToken` | Get Twilio chat token |
| GET | `accesstoken` | Get Twilio voice call token |
| POST | `notifysend` | Send push notification |
| POST | `message_notify` | Trigger message notification |

---

## Firebase Realtime Database Structure

| Node | Purpose |
|---|---|
| `users/{userId}` | User profile data (name, email, mobile, profile pic, language) |
| `providers/{providerId}` | Guard/provider real-time location |
| `trips/{tripId}` | Active trip data |
| `booking_trips/{bookingId}` | Booking trip details (status, guard location) |
| `Notifications/{userId}` | Per-user notifications |
| `Offers/user/{userId}` | User-targeted offers |
| `Announcements` | App-wide announcements |

Key fields tracked in real-time:
- `service_status` — current job status
- `accept_status` — whether guard accepted
- `booking_id` — links Firebase data to backend booking

---

## Permissions

| Permission | Purpose |
|---|---|
| `ACCESS_FINE_LOCATION` | GPS for real-time tracking |
| `ACCESS_COARSE_LOCATION` | Network-based location |
| `CAMERA` | Document and profile photo capture |
| `READ_CONTACTS` / `WRITE_CONTACTS` | Import emergency contacts from phone |
| `CALL_PHONE` | Direct call to guard |
| `INTERNET` | All network operations |
| `POST_NOTIFICATIONS` | FCM push notifications (Android 13+) |
| `READ_MEDIA_IMAGES` | Gallery image picker (Android 13+) |
| `READ_EXTERNAL_STORAGE` | File access (Android ≤ 12) |
| `WRITE_EXTERNAL_STORAGE` | File save (Android ≤ 12) |
| `READ_PHONE_STATE` | Device identification |
| `GET_ACCOUNTS` | Google account access |
| `ACCESS_WIFI_STATE` | Network state detection |

---

## Third-Party SDKs

| Library | Version | Use |
|---|---|---|
| Retrofit 2 | 2.9.0 | REST API client |
| OkHttp 3 | 4.11.0 | HTTP client + logging |
| Firebase BOM | 32.2.0 | Firebase suite |
| Firebase Database | 20.3.1 | Real-time data |
| Firebase Messaging | 23.4.1 | Push notifications |
| Firebase Crashlytics | 18.2.13 | Crash reporting |
| Firebase Analytics | 21.3.0 | Usage analytics |
| Firebase Auth | 22.3.1 | Authentication |
| GeoFire | 3.2.0 | Location queries |
| Google Maps | 19.0.0 | Maps display |
| Google Location | 21.3.0 | Fused location |
| Google Sign-In | 21.2.0 | Google auth |
| Facebook SDK | 13.0.0 | Facebook auth + ads |
| Twitter4J | 4.0.7 | Twitter auth |
| Stripe Android | 17.1.2 | Card payments |
| PayPal SDK | 1.1.0 | PayPal payments |
| Google Wallet | 19.1.0 | Google Pay |
| Twilio Voice | 4.4.0 | In-app calling |
| Twilio Conversations | 6.0.4 | In-app chat |
| Lottie | 6.0.0 | JSON animations |
| Fresco | 3.1.3 | Image loading/caching |
| Picasso | 2.71828 | Image loading |
| Google AdMob | 22.2.0 | Advertising |
| Google UMP | 2.1.0 | Consent management |
| Facebook Audience Network | 6.16.0 | Advertising |
| WorkManager | 2.7.1 | Background jobs |
| GreenRobot EventBus | 3.3.1 | Event communication |
| AndroidX Biometric | 1.1.0 | Fingerprint/face auth |
| AwesomeValidation | 4.1 | Form validation |
| Material DateTimePicker | 4.2.1 | Date/time selection |
| Libphonenumber | 8.12.46 | Phone number parsing |
| Volley | 1.2.1 | HTTP requests (legacy) |
| SwitchButton | 2.1.0 | Toggle UI component |

---

## Build Flavors / Environments

The app has two environments toggled via comments in `Utils.java`:

| Env | Base URL | Notes |
|---|---|---|
| Demo | `https://backend.insta-guard.co.uk/` | Active by default |
| Live | `https://backend.insta-guard.co.uk/` | Same URL, different build code suffix |

Stripe key: `pk_test_WDP0fwVbtyJzXdGucvd901EJ` (test key — should be replaced for production).

---

*Generated: 2026-07-31*
