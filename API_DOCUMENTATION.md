# InstaGuard User App — API Documentation

**Base URL:** `https://backend.insta-guard.co.uk/api/v3/`  
**Protocol:** HTTPS  
**Request Format:** `application/x-www-form-urlencoded` (FormUrlEncoded)  
**Response Format:** JSON  

---

## Authentication

Auth-required endpoints need the following header:

```
Authorization: Bearer <access_token>
```

The `access_token` is received from Login / Register / Social Login response.

---

## Table of Contents

1. [Authentication APIs](#1-authentication-apis)
2. [Profile APIs](#2-profile-apis)
3. [Categories & Services](#3-categories--services)
4. [Job / Booking APIs](#4-job--booking-apis)
5. [Payment APIs](#5-payment-apis)
6. [Wallet APIs](#6-wallet-apis)
7. [Stripe Payment APIs](#7-stripe-payment-apis)
8. [PayPal Payment APIs](#8-paypal-payment-apis)
9. [Emergency Contact APIs](#9-emergency-contact-apis)
10. [Document Upload APIs](#10-document-upload-apis)
11. [Subscription APIs](#11-subscription-apis)
12. [Support & Misc APIs](#12-support--misc-apis)
13. [Chat / Call APIs](#13-chat--call-apis)
14. [Third-Party APIs](#14-third-party-apis)

---

## 1. Authentication APIs

---

### 1.1 Login

| | |
|---|---|
| **Endpoint** | `POST auth/login` |
| **Auth Required** | No |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `email` | String | Yes | User's email (lowercased) |
| `password` | String | Yes | User's password |
| `device_token` | String | Yes | FCM push notification token |
| `type` | String | Yes | Always `"app"` |

**Response:**
```json
{
  "message": "Login Successful",
  "result": {
    "access_token": "eyJ...",
    "token_type": "bearer",
    "expires_in": 3600,
    "user_data": {
      "id": 1,
      "first_name": "John",
      "last_name": "Doe",
      "email": "john@example.com",
      "avatar": "profile.jpg",
      "birth_date": "1990-01-01",
      "state_id": 1,
      "country_id": 1,
      "address": "...",
      "phone_number": "1234567890",
      "country_code": "+44",
      "wallet_balance": "50.00",
      "referral_code": "ABC123"
    }
  }
}
```

---

### 1.2 Register

| | |
|---|---|
| **Endpoint** | `POST auth/register` |
| **Auth Required** | No |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `first_name` | String | Yes | First name |
| `last_name` | String | Yes | Last name |
| `email` | String | Yes | Email address |
| `country_code` | String | Yes | e.g. `"+44"` |
| `phone_number` | String | Yes | Mobile number |
| `password` | String | Yes | Password |
| `device_token` | String | Yes | FCM device token |
| `referral_code` | String | No | Referral code (optional) |

**Response:**
```json
{
  "message": "Registered Successfully",
  "result": {
    "id": 10,
    "first_name": "John",
    "last_name": "Doe",
    "email": "john@example.com",
    "avatar": "",
    "country_code": "+44",
    "phone_number": "1234567890",
    "birth_date": "",
    "gender": "",
    "phone_verified": 0,
    "email_verified": 0,
    "address": "",
    "city": "",
    "state_id": 0,
    "country_id": 0,
    "postal_code": "",
    "device_type": "android",
    "device_token": "...",
    "language": "en",
    "currency": "GBP",
    "status": 1,
    "referral_code": "XYZ789",
    "invite_code": "",
    "created_at": "2024-01-01",
    "updated_at": "2024-01-01"
  }
}
```

---

### 1.3 Check Email / Mobile Availability

| | |
|---|---|
| **Endpoint** | `POST emailAvailable` |
| **Auth Required** | No |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `email` | String | No | Email to check |
| `phone_number` | String | No | Phone number to check |
| `mode` | String | Yes | Always `"customer"` |

**Response:**
```json
{
  "message": "Available" 
}
```

---

### 1.4 Logout

| | |
|---|---|
| **Endpoint** | `POST auth/logout` |
| **Auth Required** | No (but send token in body) |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `id` | Integer | Yes | User ID |
| `token` | String | Yes | Access token |

**Response:**
```json
{
  "message": "Logged Out Successfully"
}
```

---

### 1.5 Refresh Token

| | |
|---|---|
| **Endpoint** | `POST auth/refresh` |
| **Auth Required** | No |
| **Headers** | `Accept: application/json`, `Content-Type: application/x-www-form-urlencoded` |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `id` | Integer | Yes | User ID |
| `token` | String | Yes | Current access token |

**Response:**
```json
{
  "access_token": "eyJ...",
  "token_type": "bearer",
  "expires_in": 3600
}
```

---

### 1.6 Forgot Password (Send OTP)

| | |
|---|---|
| **Endpoint** | `POST forgetpass` |
| **Auth Required** | No |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `email` | String | Yes | Registered email |
| `mode` | String | Yes | Always `"customer"` |

**Response:**
```json
{
  "message": "OTP sent to your email"
}
```

---

### 1.7 Verify OTP

| | |
|---|---|
| **Endpoint** | `POST verifyotp` |
| **Auth Required** | No |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `email` | String | Yes | Registered email |
| `otp` | String | Yes | OTP received |
| `mode` | String | Yes | Always `"customer"` |

**Response:**
```json
{
  "message": "OTP Verified",
  "id": 10
}
```

---

### 1.8 Verify Mobile (for Password Reset)

| | |
|---|---|
| **Endpoint** | `POST verifyMobile` |
| **Auth Required** | No |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `phone_number` | String | Yes | Mobile number |
| `mode` | String | Yes | Always `"customer"` |

**Response:**
```json
{
  "message": "Mobile verified",
  "id": 10
}
```

---

### 1.9 Reset Password

| | |
|---|---|
| **Endpoint** | `POST updatePassword` |
| **Auth Required** | No |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `id` | Integer | Yes | User ID (from OTP verify response) |
| `password` | String | Yes | New password |
| `mode` | String | Yes | Always `"customer"` |

**Response:**
```json
{
  "message": "Password Updated Successfully"
}
```

---

### 1.10 Social Login — Check Account

| | |
|---|---|
| **Endpoint** | `POST customer/socialAdd` |
| **Auth Required** | No |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `social_id` | String | Yes | Google/Facebook UID |
| `social_type` | String | Yes | `"google"` or `"facebook"` |
| `device_id` | String | Yes | FCM device token |

**Response:**
```json
{
  "message": "Success",
  "result": {
    "id": 5,
    "email": "user@gmail.com",
    "first_name": "John",
    ...
  }
}
```

---

### 1.11 Social Login — Complete Registration

| | |
|---|---|
| **Endpoint** | `POST customer/socialUpdate` |
| **Auth Required** | No |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `customer_id` | Integer | Yes | Customer ID from socialAdd |
| `email` | String | Yes | Email |
| `phone_number` | String | Yes | Phone number |
| `country_code` | String | Yes | e.g. `"+44"` |
| `device_id` | String | Yes | FCM device token |
| `name` | String | Yes | First name |
| `last_name` | String | Yes | Last name |
| `referral_code` | String | No | Referral code |

**Response:**
```json
{
  "msg": "Profile Updated"
}
```

---

### 1.12 Delete Account Request

| | |
|---|---|
| **Endpoint** | `POST delete_account_request` |
| **Auth Required** | No |

**Request Parameters:** *(sent via map — check with backend for exact keys)*

**Response:**
```json
{
  "message": "Account deletion request submitted"
}
```

---

### 1.13 Delete Account Confirm

| | |
|---|---|
| **Endpoint** | `POST delete_account_confirm` |
| **Auth Required** | No |

**Request Parameters:** *(sent via map — check with backend for exact keys)*

**Response:**
```json
{
  "message": "Account deleted"
}
```

---

## 2. Profile APIs

---

### 2.1 View Profile

| | |
|---|---|
| **Endpoint** | `POST profileView` |
| **Auth Required** | Yes |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `id` | Integer | Yes | User ID |
| `token` | String | Yes | Access token |

**Response:**
```json
{
  "result": {
    "id": 1,
    "first_name": "John",
    "last_name": "Doe",
    "social_id": "",
    "social_type": "",
    "avatar": "profile.jpg",
    "email": "john@example.com",
    "country_code": "+44",
    "phone_number": "1234567890",
    "birth_date": "1990-01-01",
    "gender": "male",
    "phone_verified": 1,
    "email_verified": 1,
    "address": "123 Street",
    "city": "London",
    "state_id": 1,
    "country_id": 1,
    "postal_code": "SW1A 1AA",
    "device_type": "android",
    "device_token": "fcm_token",
    "language": "en",
    "currency": "GBP",
    "wallet_balance": "100.00",
    "last_seen": "2024-01-01 10:00:00",
    "status": 1,
    "referral_code": "ABC123",
    "subscription_id": 2,
    "subscription_status": 1,
    "subscription_name": "Premium",
    "created_at": "2024-01-01",
    "updated_at": "2024-01-01"
  }
}
```

---

### 2.2 Update Profile

| | |
|---|---|
| **Endpoint** | `POST updateprofile` |
| **Auth Required** | Yes |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `id` | Integer | Yes | User ID |
| `first_name` | String | Yes | First name |
| `last_name` | String | Yes | Last name |
| `email` | String | Yes | Email |
| `phone_number` | String | Yes | Phone number |
| `country_code` | String | Yes | Country code |
| `currency` | String | No | Currency code e.g. `"GBP"` |
| `language` | String | No | Language code e.g. `"en"` |

**Response:**
```json
{
  "message": "Profile Updated Successfully"
}
```

---

### 2.3 Update Profile Picture

| | |
|---|---|
| **Endpoint** | `POST picupload` |
| **Auth Required** | Yes |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `id` | Integer | Yes | User ID |
| `file_binary_data` | String | Yes | Base64 encoded image |
| `avatar` | String | Yes | Image filename/path |

**Response:**
```json
{
  "message": "Profile picture updated"
}
```

---

### 2.4 Change Password

| | |
|---|---|
| **Endpoint** | `POST auth/passwordChange` |
| **Auth Required** | Yes |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `id` | Integer | Yes | User ID |
| `old_password` | String | Yes | Current password |
| `new_password` | String | Yes | New password |

**Response:**
```json
{
  "message": "Password changed successfully"
}
```

---

## 3. Categories & Services

---

### 3.1 Get Vehicle / Service Categories

| | |
|---|---|
| **Endpoint** | `POST Categories` |
| **Auth Required** | Yes |
| **Headers** | `Authorization`, `Accept`, `Content-Type` |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `language` | String | Yes | Language code e.g. `"en"`, `"ar"` |

**Response:**
```json
{
  "result": [
    {
      "id": 1,
      "category_name": "Security Guard",
      "category_name_spanish": "حارس أمن",
      "category_image": "category.png",
      "category_type": "guard",
      "level_type": "1",
      "level_amount": "10.00"
    }
  ]
}
```

---

### 3.2 Get Sub-Categories / Services

| | |
|---|---|
| **Endpoint** | `POST Service` |
| **Auth Required** | Yes |
| **Headers** | `Authorization`, `Accept` |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `id` | Integer | Yes | Category ID |
| `language` | String | Yes | Language code |

**Response:**
```json
{
  "result": [
    {
      "id": 5,
      "service_name": "Event Security",
      "service_name_spanish": "أمن الفعاليات",
      "service_image": "service.png",
      "base_fare": "50.00",
      "admin_commission": "10",
      "category_id": 1,
      "price_per_km": "2.00",
      "visit_fare": "20.00",
      "price_per_agent": "15.00",
      "price_per_day": "120.00",
      "book_guard_min_time": "2"
    }
  ]
}
```

---

### 3.3 Get App Settings

| | |
|---|---|
| **Endpoint** | `POST appsetting` |
| **Auth Required** | No |

**Request Parameters:** None

**Response:**
```json
{
  "result": {
    "payment_publisher_key": "pk_live_...",
    "payment_secret_key": "sk_live_...",
    "distance_limit": "50",
    "time_out_delay": "60",
    "tawk_url": "https://...",
    "property_url": "https://...",
    "property_id": "...",
    "paypal_status": 1,
    "customer_manage_document": 1,
    "distance_unit": "km",
    "wallet_minimum": "10.00",
    "language": [...],
    "currency": [...],
    "sms_templates": [...]
  }
}
```

---

## 4. Job / Booking APIs

---

### 4.1 Get Ongoing Jobs

| | |
|---|---|
| **Endpoint** | `POST customerongoing` |
| **Auth Required** | Yes |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `customer_id` | Integer | Yes | Customer ID |
| `language` | String | Yes | Language code |

**Response:**
```json
{
  "result": [
    {
      "booking_id": 100,
      "job_number": "JOB001",
      "job_date": "2024-01-15",
      "driver_name": "Guard Name",
      "customer_name": "John Doe",
      "customer_location": "123 Street, London",
      "driver_avatar": "guard.jpg",
      "service_name": "Event Security",
      "category_name": "Security Guard",
      "payment_type": "wallet",
      "total_amount": "150.00",
      "unit_price": "75.00",
      "site_commission": "15.00",
      "extra_charge": "0.00",
      "misc_charge": "0.00",
      "driver_discount": "0.00",
      "service_status": "ongoing",
      "accept_time": "10:00:00",
      "rating": "0",
      "driver_id": 20,
      "category_ar": "حارس أمن",
      "service_ar": "أمن الفعاليات",
      "company_name": "InstaGuard Ltd"
    }
  ]
}
```

---

### 4.2 Get Upcoming Jobs

| | |
|---|---|
| **Endpoint** | `POST upcomingJobs` |
| **Auth Required** | Yes |
| **Headers** | `Authorization`, `Accept` |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `user_id` | Integer | Yes | Customer ID |
| `mode` | String | Yes | Always `"customer"` |
| `language` | String | Yes | Language code |

**Response:**
```json
{
  "result": [
    {
      "booking_id": 101,
      "job_number": "JOB002",
      "job_date": "2024-02-01",
      "driver_name": "Guard Name",
      "customer_name": "John Doe",
      "customer_location": "123 Street",
      "service_name": "Event Security",
      "category_name": "Security Guard",
      "payment_type": "stripe",
      "total_amount": "300.00",
      "unit_price": "150.00",
      "site_commission": "30.00",
      "extra_charge": "0.00",
      "misc_charge": "0.00",
      "driver_discount": "0.00",
      "service_status": "scheduled",
      "accept_time": "",
      "rating": "0",
      "category_ar": "",
      "service_ar": "",
      "driver_phone_number": "+44123456",
      "company_name": "InstaGuard Ltd",
      "total_agent": 2,
      "total_days": 1,
      "start_date_time": "2024-02-01 09:00:00",
      "end_date_time": "2024-02-01 17:00:00"
    }
  ]
}
```

---

### 4.3 Get Upcoming Jobs (Paginated)

| | |
|---|---|
| **Endpoint** | `POST CustomerUpcomingJob` |
| **Auth Required** | Yes |
| **Headers** | `Authorization`, `Accept` |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `customer_id` | Integer | Yes | Customer ID |
| `offset` | Integer | Yes | Page offset (0, 10, 20...) |

**Response:** Same structure as upcoming jobs

---

### 4.4 Get Past Jobs

| | |
|---|---|
| **Endpoint** | `POST pastjobs` |
| **Auth Required** | Yes |
| **Headers** | `Authorization`, `Accept` |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `user_id` | Integer | Yes | Customer ID |
| `mode` | String | Yes | Always `"customer"` |
| `language` | String | Yes | Language code |

**Response:**
```json
{
  "result": [
    {
      "booking_id": 90,
      "job_number": "JOB090",
      "job_date": "2024-01-01",
      "job_type": "ridenow",
      "driver_name": "Guard Name",
      "customer_name": "John Doe",
      "customer_location": "123 Street",
      "service_name": "Event Security",
      "category_name": "Security Guard",
      "payment_type": "wallet",
      "total_amount": "150.00",
      "unit_price": "75.00",
      "site_commission": "15.00",
      "extra_charge": "0.00",
      "misc_charge": "0.00",
      "driver_discount": "0.00",
      "price_per_km": "2.00",
      "total_distance": "10.00",
      "level_type": "1",
      "main_level_amount": "10.00",
      "sub_level_amount": "5.00",
      "visit_fare": "20.00",
      "service_status": "completed",
      "accept_time": "10:00:00",
      "customer_avatar": "customer.jpg",
      "driver_avatar": "guard.jpg",
      "customer_id": 1,
      "driver_id": 20,
      "rating": "5",
      "rating_status": "1"
    }
  ]
}
```

---

### 4.5 Send Service Request (Book Now / Book Later)

| | |
|---|---|
| **Endpoint** | `POST requestService` |
| **Auth Required** | No |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `mode` | String | Yes | `"ridenow"` or `"ridelater"` |
| `category` | Integer | Yes | Category ID |
| `customer_id` | Integer | Yes | Customer ID |
| `service_id` | Integer | Yes | Sub-category / Service ID |
| `special_instruction` | String | No | Special instructions |
| `customer_lat` | String | Yes | Customer latitude |
| `customer_lon` | String | Yes | Customer longitude |
| `customer_location` | String | Yes | Customer address text |
| `drop_location` | String | No | Drop address (use `"NoDrop"` if none) |
| `drop_lat` | String | No | Drop latitude |
| `drop_lon` | String | No | Drop longitude |
| `language` | String | Yes | Language code |
| `total_hrs` | String | Yes | Total hours |
| `price_per_hr` | String | Yes | Price per hour |
| `vat_amount` | String | Yes | VAT amount |
| `total_amount` | String | Yes | Total payable amount |
| `emergency_msg` | String | No | Emergency message |
| `total_agent` | Integer | Yes | Number of guards required |
| `subscription_status` | String | Yes | `"0"` or `"1"` |
| `discount_amount` | String | No | Discount amount |
| `discount_percentage` | String | No | Discount percentage |
| `promocode` | String | No | Promo code |
| `payment_type` | String | Yes | `"wallet"`, `"stripe"`, `"paypal"`, `"stripe_gpay"` |
| `stripe_token` | String | Conditional | Stripe payment token (if payment_type is stripe/gpay) |
| `start_date` | String | Conditional | Start date (ridelater only) |
| `start_time` | String | Conditional | Start time (ridelater only) |
| `end_date` | String | Conditional | End date (ridelater only) |
| `end_time` | String | Conditional | End time (ridelater only) |
| `start_date_time` | String | Conditional | Full start datetime (ridelater only) |
| `end_date_time` | String | Conditional | Full end datetime (ridelater only) |
| `trip_type` | String | Conditional | Trip type (ridelater only) |

**Response:**
```json
{
  "result": {
    "job_number": "JOB010",
    "customer_id": 1,
    "service_id": 5,
    "customer_location": "123 Street",
    "customer_lat": "51.5074",
    "customer_lon": "-0.1278",
    "payment_type": "wallet",
    "service_status": "searching",
    "request_status": "pending",
    "mode": "ridenow"
  }
}
```

---

### 4.6 Cancel Ongoing Job

| | |
|---|---|
| **Endpoint** | `POST cancelJobs` |
| **Auth Required** | Yes |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `booking_id` | Integer | Yes | Booking ID |
| `reason` | String | No | Cancellation reason |
| `mode` | String | Yes | Always `"customer"` |
| `language` | String | Yes | Language code |

**Response:**
```json
{
  "result": {
    "id": 100,
    "job_number": "JOB001",
    "service_status": "cancelled",
    "cancel_service_status": "customer"
  }
}
```

---

### 4.7 Cancel Scheduled / Upcoming Job

| | |
|---|---|
| **Endpoint** | `POST scheduledcancelJobs` |
| **Auth Required** | Yes |
| **Headers** | `Authorization`, `Accept` |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `booking_id` | Integer | Yes | Booking ID |
| `reason` | String | No | Cancellation reason |
| `mode` | String | Yes | Always `"customer"` |
| `language` | String | Yes | Language code |

**Response:**
```json
{
  "message": "Job cancelled successfully"
}
```

---

### 4.8 Cancel Pending Request

| | |
|---|---|
| **Endpoint** | `POST requestService` (with mode=`customer_cancel`) |
| **Auth Required** | Yes |
| **Headers** | `Authorization`, `Accept` |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `customer_booking_id` | Integer | Yes | Booking ID |
| `mode` | String | Yes | Always `"customer_cancel"` |

**Response:**
```json
{
  "message": "Request cancelled"
}
```

---

### 4.9 Schedule Booking (Confirm Scheduled Job)

| | |
|---|---|
| **Endpoint** | `POST scheduledBooking` |
| **Auth Required** | Yes |
| **Headers** | `Authorization`, `Accept` |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `mode` | String | Yes | Always `"accept"` |
| `customer_id` | Integer | Yes | Customer ID |
| `driver_id` | Integer | Yes | Driver/Guard ID |
| `request_time` | String | Yes | Requested time |
| `job_date` | String | Yes | Job date |
| `service_id` | Integer | Yes | Service ID |
| `category` | Integer | Yes | Category ID |
| `customer_location` | String | Yes | Customer location |
| `customer_lat` | String | Yes | Customer latitude |
| `customer_lon` | String | Yes | Customer longitude |
| `drop_lat` | String | No | Drop latitude |
| `drop_lon` | String | No | Drop longitude |
| `distance` | String | No | Distance |
| `drop_location` | String | No | Drop address |
| `special_instruction` | String | No | Special instructions |
| `language` | String | Yes | Language code |
| `payment_type` | String | Yes | Payment method |
| `stripe_token` | String | Conditional | Stripe token (if stripe payment) |
| `promocode` | String | No | Promo code |

**Response:**
```json
{
  "result": {
    "booking_id": 101,
    "job_number": "JOB002",
    "service_status": "scheduled",
    "driver_first_name": "Guard",
    "driver_last_name": "Name",
    "unit_price": "150.00",
    "site_commission": "15.00",
    "total_amount": "165.00"
  }
}
```

---

### 4.10 Get Fare Estimate

| | |
|---|---|
| **Endpoint** | `POST FareEstimate` |
| **Auth Required** | Yes (token passed in Authorization header) |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `service_id` | Integer | Yes | Sub-category ID |
| `customer_id` | Integer | Yes | Customer ID |
| `total_agent` | Integer | Yes | Number of guards |
| `type` | String | Conditional | `"ridelater"` for scheduled booking |
| `start_date_time` | String | Conditional | Start datetime (for ridelater) |
| `end_date_time` | String | Conditional | End datetime (for ridelater) |
| `promocode` | String | No | Promo code |

**Response:**
```json
{
  "result": {
    "promo_discount": "0.00",
    "promo_status": "0",
    "promo_code": "",
    "base_fare": "50.00",
    "total_hrs": "8",
    "price_per_hr": "15.00",
    "price_per_guard": "15.00",
    "total_guard_amount": "30.00",
    "before_vat_amount": "240.00",
    "overall_total": "264.00",
    "wallet_status": "1",
    "wallet_amount": "50.00",
    "subscription_status": "1",
    "subscription_percentage": "10",
    "subscription_amount": "24.00",
    "sub_total": "240.00"
  }
}
```

---

### 4.11 Get Extended Hours Fare Estimate

| | |
|---|---|
| **Endpoint** | `POST ExtraCalFare` |
| **Auth Required** | Yes |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `customer_id` | Integer | Yes | Customer ID |
| `booking_id` | Integer | Yes | Booking ID |
| `hrs` | Integer | Yes | Number of extended hours |

**Response:** Same as Fare Estimate response

---

### 4.12 Update Job (Extend Hours Payment)

| | |
|---|---|
| **Endpoint** | `POST UpdateJob` |
| **Auth Required** | No |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `booking_id` | Integer | Yes | Booking ID |
| `extended_hrs` | String | Yes | Extended hours count |
| `extended_vat` | String | Yes | VAT for extended hours |
| `extended_amount` | String | Yes | Total amount for extended hours |
| `extended_status` | String | Yes | Always `"1"` |
| `payment_type` | String | Yes | `"wallet"`, `"stripe"`, `"paypal"`, `"stripe_gpay"` |
| `stripe_token` | String | Conditional | Stripe token (if applicable) |

**Response:**
```json
{
  "message": "Job updated successfully"
}
```

---

### 4.13 Check Promo Code

| | |
|---|---|
| **Endpoint** | `POST checkPromoCode` |
| **Auth Required** | Yes |
| **Headers** | `Authorization`, `Accept` |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `promocode` | String | Yes | Promo/coupon code |
| `customer_id` | Integer | Yes | Customer ID |

**Response:**
```json
{
  "result": {
    "id": 1,
    "coupon_code": "SAVE10",
    "description": "10% off",
    "discount": "10",
    "discount_type": "percentage",
    "validity": "2024-12-31",
    "activation_date": "2024-01-01",
    "expiry_date": "2024-12-31",
    "usage_limit": 100,
    "status": 1
  }
}
```

---

### 4.14 Check Guard Availability

| | |
|---|---|
| **Endpoint** | `POST availabilityCheckService` |
| **Auth Required** | Yes |
| **Headers** | `Authorization`, `Accept` |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `day` | String | Yes | Day selected |
| `hour` | String | Yes | Hour string |
| `service` | Integer | Yes | Sub-category ID |
| `pick_lat` | String | Yes | Pickup latitude |
| `pick_lon` | String | Yes | Pickup longitude |

**Response:**
```json
{
  "message": "Guard available"
}
```

---

### 4.15 Submit Rating / Feedback

| | |
|---|---|
| **Endpoint** | `POST CustomerFeedbackStore` |
| **Auth Required** | Yes |
| **Headers** | `Authorization`, `Accept` |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `from_id` | Integer | Yes | Customer (user) ID |
| `job_id` | Integer | Yes | Booking ID |
| `to_id` | Integer | Yes | Driver/Guard ID |
| `rating` | Integer | Yes | Rating (1-5) |
| `status` | String | Yes | Always `"0"` |
| `feedback` | String | No | Text feedback |

**Response:**
```json
{
  "message": "Rating submitted successfully"
}
```

---

### 4.16 Customer Booking Timeout

| | |
|---|---|
| **Endpoint** | `POST customerTimeout` |
| **Auth Required** | No |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `booking_id` | Integer | Yes | Booking ID |
| `type` | String | Yes | Always `"app"` |

**Response:**
```json
{
  "message": "Timeout handled"
}
```

---

### 4.17 Validate Guard Unique ID

| | |
|---|---|
| **Endpoint** | `POST driverReferenceIdValidate` |
| **Auth Required** | No |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `reference_id` | String | Yes | Guard's unique reference ID |

**Response:**
```json
{
  "message": "Valid",
  "result": [
    {
      "id": 20,
      "name": "Guard Name"
    }
  ]
}
```

---

## 5. Payment APIs

---

### 5.1 Trip Payment (Wallet)

| | |
|---|---|
| **Endpoint** | `POST customerTripPayment` |
| **Auth Required** | Yes |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `driver_id` | Integer | Yes | Driver/Guard ID |
| `customer_id` | Integer | Yes | Customer ID |
| `transaction_token` | String | Yes | Stripe token |
| `booking_id` | Integer | Yes | Booking ID |
| `balance_amount` | String | Yes | Amount deducted from wallet |
| `total_amount` | String | Yes | Total amount |
| `card_number` | String | No | Last 4 digits of card |

**Response:**
```json
{
  "result": {
    "customer_id": 1,
    "balance_amount": "50.00",
    "booking_id": 100,
    "driver_id": 20,
    "card_number": "****4242",
    "transaction_token": "tok_...",
    "driver_wallet_balance": "200.00",
    "customer_wallet_balance": "50.00"
  }
}
```

---

### 5.2 Card Payment (End of Job)

| | |
|---|---|
| **Endpoint** | `POST Paycard` |
| **Auth Required** | Yes |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `booking_id` | Integer | Yes | Booking ID |
| `amount` | String | Yes | Amount to charge |
| `card_number` | String | Yes | Last 4 digits of card |
| `strip_token` | String | Yes | Stripe token |

**Response:** Same as customerTripPayment response

---

### 5.3 Mark Payment Complete

| | |
|---|---|
| **Endpoint** | `POST requestService` (with `mode=payment_success`) |
| **Auth Required** | Yes |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `mode` | String | Yes | Always `"payment_success"` |
| `booking_id` | Integer | Yes | Booking ID |
| `driver_id` | Integer | Yes | Driver/Guard ID |

**Response:**
```json
{
  "message": "Payment successful"
}
```

---

## 6. Wallet APIs

---

### 6.1 Add Money to Wallet

| | |
|---|---|
| **Endpoint** | `POST customerAddMoney` |
| **Auth Required** | Yes |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `customer_id` | Integer | Yes | Customer ID |
| `transaction_amount` | String | Yes | Amount to add |
| `transaction_token` | String | Yes | Stripe token |
| `transaction_mode` | String | Yes | Always `"card"` |
| `card_number` | String | Yes | Last 4 digits of card |
| `language` | String | Yes | Language code |

**Response:**
```json
{
  "result": {
    "customer_id": 1,
    "transaction_mode": "card",
    "card_number": "****4242",
    "transaction_amount": "50.00",
    "transaction_token": "tok_...",
    "transaction_date": "2024-01-01",
    "transaction_type": "credit",
    "transaction_notes": "Wallet recharge",
    "transaction_status": "success",
    "id": 200,
    "created_at": "2024-01-01",
    "updated_at": "2024-01-01"
  }
}
```

---

### 6.2 Get Wallet Transactions

| | |
|---|---|
| **Endpoint** | `POST Transaction` |
| **Auth Required** | Yes |
| **Headers** | `Authorization`, `Accept` |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `customer_id` | Integer | Yes | Customer ID |
| `language` | String | Yes | Language code |

**Response:**
```json
{
  "result": [
    {
      "id": 200,
      "transaction_amount": "50.00",
      "transaction_type": "credit",
      "transaction_date": "2024-01-01",
      "transaction_notes": "Wallet recharge",
      "transaction_status": "success"
    }
  ]
}
```

---

## 7. Stripe Payment APIs

---

### 7.1 Create Payment Intent (3D Secure)

| | |
|---|---|
| **Endpoint** | `POST StripePayment3DApp` |
| **Auth Required** | Yes |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `customer_id` | Integer | Yes | Customer ID |
| `amount` | String | Yes | Amount to charge (in pence/cents) |
| `save_card` | String | Yes | `"1"` to save card, `"0"` otherwise |

**Response:**
```json
{
  "result": {
    "id": "pi_xxx",
    "client_secret": "pi_xxx_secret_xxx"
  }
}
```

---

### 7.2 Create Setup Intent (Save Card)

| | |
|---|---|
| **Endpoint** | `POST CreateSaveCardSetupIntent` |
| **Auth Required** | Yes |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `customer_id` | Integer | Yes | Customer ID |
| `amount` | String | Yes | Amount |
| `save_card` | String | Yes | Always `"1"` |

**Response:** Same as Payment Intent response

---

### 7.3 Capture Wallet Recharge (Stripe)

| | |
|---|---|
| **Endpoint** | `POST CaptureWalletRecharge` |
| **Auth Required** | Yes |

**Request Parameters:** *(Passed via map — confirm exact keys with backend)*

**Response:**
```json
{
  "result": {
    "message": "Wallet recharged",
    "client_secret": "..."
  }
}
```

---

### 7.4 Payment with Saved Card (3D Secure)

| | |
|---|---|
| **Endpoint** | `POST StripeSaveCardPayment3DApp` |
| **Auth Required** | Yes |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `customer_id` | Integer | Yes | Customer ID |
| `amount` | String | Yes | Amount to charge |
| `type` | String | Yes | Always `"trip"` |

**Response:**
```json
{
  "message": "Payment initiated",
  "client_secret": "pi_xxx_secret_xxx",
  "payment_intent_id": "pi_xxx",
  "wallet_status": "0",
  "action_url_status": "1",
  "action_url": "https://..."
}
```

---

## 8. PayPal Payment APIs

---

### 8.1 Create PayPal Order

| | |
|---|---|
| **Endpoint** | `POST createOrder` |
| **Auth Required** | Yes |

**Request Parameters (Variant 1 — Pre-booking):**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `amount` | String | Yes | Order amount |

**Request Parameters (Variant 2 — End of Job):**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `booking_id` | Integer | Yes | Booking ID |
| `type` | String | Yes | Always `"app_end"` |
| `amount` | String | Yes | Order amount |

**Response:**
```json
{
  "message": "Order created",
  "id": "PAYPAL_ORDER_ID_xxx"
}
```

---

### 8.2 Capture PayPal Payment

| | |
|---|---|
| **Endpoint** | `POST Paypaypal` |
| **Auth Required** | Yes |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `booking_id` | Integer | Yes | Booking ID |
| `amount` | String | Yes | Amount |
| `payment_id` | String | Yes | PayPal payment/order ID |

**Response:**
```json
{
  "message": "Payment captured",
  "result": {
    "customer_wallet_balance": "150.00"
  }
}
```

---

## 9. Emergency Contact APIs

---

### 9.1 Add Emergency Contact

| | |
|---|---|
| **Endpoint** | `POST emergencyContactCustomer` |
| **Auth Required** | Yes |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `customer_id` | Integer | Yes | Customer ID |
| `phone_number` | String | Yes | Emergency contact number |
| `contact_name` | String | Yes | Contact name |
| `status` | String | Yes | Always `"1"` |

**Response:**
```json
{
  "result": {
    "customer_id": 1,
    "phone_number": "+441234567890",
    "status": "1",
    "contact_name": "Jane Doe"
  }
}
```

---

### 9.2 Get Emergency Contacts

| | |
|---|---|
| **Endpoint** | `POST emergencyContactGET` |
| **Auth Required** | Yes |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `customer_id` | Integer | Yes | Customer ID |

**Response:**
```json
{
  "result": [
    {
      "id": 1,
      "customer_id": 1,
      "contact_name": "Jane Doe",
      "phone_number": "+441234567890",
      "status": "1"
    }
  ]
}
```

---

### 9.3 Delete Emergency Contact

| | |
|---|---|
| **Endpoint** | `POST emergencyContactDelete` |
| **Auth Required** | Yes |
| **Headers** | `Authorization`, `Accept` |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `contact_id` | Integer | Yes | Contact ID to delete |

**Response:**
```json
{
  "message": "Contact deleted successfully"
}
```

---

### 9.4 Save Panic / SOS Alert

| | |
|---|---|
| **Endpoint** | `POST savepanic` |
| **Auth Required** | No |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `customer_id` | Integer | Yes | Customer ID |
| `mode` | String | Yes | Always `"customer"` |
| `location_link` | String | Yes | Google Maps URL or HELP message |

**Response:**
```json
{
  "result": {
    "id": 50,
    "panic_date": "2024-01-01 10:00:00"
  }
}
```

---

## 10. Document Upload APIs

---

### 10.1 Get Required Document List

| | |
|---|---|
| **Endpoint** | `POST manageCustomerDocument` |
| **Auth Required** | No |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `id` | Integer | Yes | User ID |
| `type` | String | Yes | Always `"driver"` (for document template list) |
| `mode` | String | Yes | Always `"api"` |

**Response:**
```json
{
  "result": [
    {
      "id": 1,
      "document_name": "Passport",
      "document_name_ar": "جواز سفر",
      "is_required": 1,
      "document_type": "identity"
    }
  ]
}
```

---

### 10.2 Upload Document Image

| | |
|---|---|
| **Endpoint** | `POST customerImageUpload` |
| **Auth Required** | No |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `image` | String | Yes | Base64 encoded image |
| `type` | String | Yes | Document type |

**Response:**
```json
{
  "result": {
    "image_path": "uploads/documents/doc123.jpg"
  }
}
```

---

### 10.3 Submit Document

| | |
|---|---|
| **Endpoint** | `POST customerDocumentUpdate` |
| **Auth Required** | No |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `customer_id` | Integer | Yes | Customer ID |
| `document_id` | Integer | Yes | Document type ID |
| `document` | String | Yes | Base64 image data |
| `document_type` | String | Yes | Document type name |
| `document_type_ar` | String | No | Document type name (Arabic) |
| `expiry_date` | String | No | Document expiry date |
| `type` | String | Yes | Document type |

**Response:**
```json
{
  "message": "Document submitted successfully"
}
```

---

### 10.4 Re-upload Rejected Document

| | |
|---|---|
| **Endpoint** | `POST customerProofReupload` |
| **Auth Required** | No |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `customer_id` | Integer | Yes | Customer ID |
| `document_id` | Integer | Yes | Document type ID |
| `id` | Integer | Yes | Existing document record ID |
| `document` | String | Yes | Base64 image data |
| `expiry_date` | String | No | Document expiry date |
| `type` | String | Yes | Document type |

**Response:**
```json
{
  "message": "Document re-uploaded successfully"
}
```

---

### 10.5 View Uploaded Documents

| | |
|---|---|
| **Endpoint** | `POST customerViewDocument` |
| **Auth Required** | No |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `customer_id` | Integer | Yes | Customer ID |

**Response:**
```json
{
  "result": [
    {
      "id": 1,
      "document_id": 1,
      "customer_id": 1,
      "document_type": "Passport",
      "document_path": "uploads/doc.jpg",
      "expiry_date": "2025-12-31",
      "status": "approved"
    }
  ]
}
```

---

## 11. Subscription APIs

---

### 11.1 Get All Subscription Plans

| | |
|---|---|
| **Endpoint** | `POST subscriptionList` |
| **Auth Required** | No |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `user_type` | String | Yes | Always `"1"` (customer) |

**Response:**
```json
{
  "result": [
    {
      "id": 1,
      "name": "Basic",
      "description": "Basic plan",
      "amount": "9.99",
      "period": 1,
      "period_type": "month",
      "free_km": "10",
      "free_job_request": "5",
      "status": 1,
      "plan_type": "basic",
      "created_at": "2024-01-01",
      "updated_at": "2024-01-01"
    }
  ]
}
```

---

### 11.2 View Specific Subscription Plan

| | |
|---|---|
| **Endpoint** | `POST subscriptionGetList` |
| **Auth Required** | Yes |
| **Headers** | `Authorization`, `Accept` |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `plan_id` | Integer | Yes | Subscription plan ID |

**Response:** Same as subscriptionList single item

---

### 11.3 Buy Subscription

| | |
|---|---|
| **Endpoint** | `POST buySubscription` |
| **Auth Required** | No |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `customer_id` | Integer | Yes | Customer ID |
| `plan_id` | Integer | Yes | Plan ID to purchase |

**Response:**
```json
{
  "message": "Subscription purchased"
}
```

---

### 11.4 Create Stripe Checkout Session (Subscription)

| | |
|---|---|
| **Endpoint** | `POST createCheckoutSession` |
| **Auth Required** | No |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `user_id` | Integer | Yes | Customer ID |
| `plan_id` | Integer | Yes | Plan ID |
| `auto_renew` | String | Yes | Always `"1"` |
| `type` | String | Yes | Always `"1"` |

**Response:**
```json
{
  "result": {
    "checkout_url": "https://checkout.stripe.com/..."
  }
}
```

---

### 11.5 Check Active Subscription

| | |
|---|---|
| **Endpoint** | `POST checkSubscription` |
| **Auth Required** | No |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `customer_id` | Integer | Yes | Customer ID |

**Response:**
```json
{
  "result": {
    "id": 10,
    "order_id": "SUB001",
    "cus_id": 1,
    "plan_id": 1,
    "plan_name": "Basic",
    "subscription_description": "Basic plan",
    "period_type": "month",
    "period": 1,
    "purchase_date": "2024-01-01",
    "subscription_code": "CODE123",
    "expiry_date": "2024-02-01",
    "amount": "9.99",
    "status": 1
  }
}
```

---

### 11.6 Enable/Disable Auto-Renewal

| | |
|---|---|
| **Endpoint** | `POST updateAutoRenew` |
| **Auth Required** | No |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `customer_id` | Integer | Yes | Customer ID |
| `subscription_id` | Integer | Yes | Subscription ID |
| `user_type` | String | Yes | Always `"1"` |
| `action` | String | Yes | `"enable"` or `"disable"` |
| `auto_renew` | String | Yes | `"1"` (enable) or `"0"` (disable) |

**Response:**
```json
{
  "message": "Auto-renew updated"
}
```

---

### 11.7 Get Subscription History

| | |
|---|---|
| **Endpoint** | `POST subscription_history` |
| **Auth Required** | No |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `cus_id` | Integer | Yes | Customer ID |
| `offset` | Integer | Yes | Pagination offset (start with `0`) |
| `user_type` | String | Yes | Always `"1"` |

**Response:**
```json
{
  "result": [
    {
      "id": 10,
      "plan_name": "Basic",
      "amount": "9.99",
      "purchase_date": "2024-01-01",
      "expiry_date": "2024-02-01",
      "status": 1
    }
  ]
}
```

---

### 11.8 Check Service Included in Plan

| | |
|---|---|
| **Endpoint** | `POST checkService` |
| **Auth Required** | No |

**Request Parameters:** *(confirm exact keys with backend)*

**Response:**
```json
{
  "result": {
    "included": true,
    "message": "Service included in your plan"
  }
}
```

---

## 12. Support & Misc APIs

---

### 12.1 Get Support Info

| | |
|---|---|
| **Endpoint** | `POST Support` |
| **Auth Required** | Yes |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `id` | Integer | Yes | User ID or page ID |

**Response:**
```json
{
  "result": {
    "email": "support@insta-guard.co.uk",
    "phone": "+44...",
    "address": "..."
  }
}
```

---

### 12.2 Get App Pages (Privacy Policy, Terms, etc.)

| | |
|---|---|
| **Endpoint** | `POST pages` |
| **Auth Required** | Yes |

**Request Parameters:** None (empty map)

**Response:**
```json
{
  "result": [
    {
      "id": 1,
      "title": "Privacy Policy",
      "content": "..."
    }
  ]
}
```

---

### 12.3 Get Banner Images

| | |
|---|---|
| **Endpoint** | `POST getbanners` |
| **Auth Required** | Yes |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `mode` | String | Yes | `"all"`, `"announcement"`, `"offer"` |

**Response:**
```json
{
  "result": [
    {
      "id": 1,
      "image": "banner1.jpg",
      "title": "Special Offer",
      "type": "offer"
    }
  ]
}
```

---

### 12.4 Send Push Notification to Guard

| | |
|---|---|
| **Endpoint** | `POST notifysend` |
| **Auth Required** | Yes |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `customer_id` | Integer | Yes | Customer ID |
| `driver_id` | Integer | Yes | Guard/Driver ID |
| `mode` | String | Yes | Always `"accept"` |
| `language` | String | Yes | Language code |

**Response:**
```json
{
  "message": "Notification sent"
}
```

---

### 12.5 Send Chat Message Notification

| | |
|---|---|
| **Endpoint** | `POST message_notify` |
| **Auth Required** | Yes |

**Request Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `id` | Integer | Yes | User ID |
| `type` | String | Yes | Always `"driver"` |
| `message` | String | Yes | Chat message text |
| `booking_id` | Integer | Yes | Booking ID |
| `mode` | String | Yes | Always `"chat"` |

**Response:**
```json
{
  "message": "Notification sent"
}
```

---

## 13. Chat / Call APIs

> **Note:** These endpoints use a separate Retrofit client with base URL: `https://backend.insta-guard.co.uk/api/v3/`

---

### 13.1 Get Chat Access Token (Twilio)

| | |
|---|---|
| **Endpoint** | `GET chatAccessToken` |
| **Auth Required** | No |

**Query Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `device` | String | Yes | User ID |
| `identity` | String | Yes | Identity string |

**Response:**
```json
{
  "token": "twilio_chat_token_xxx"
}
```

---

### 13.2 Get Voice Call Access Token (Twilio)

| | |
|---|---|
| **Endpoint** | `GET accesstoken` |
| **Auth Required** | No |

**Query Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `device` | String | Yes | Always `"android_rider"` |
| `identity` | String | Yes | User identity string |

**Response:**
```json
{
  "token": "twilio_voice_token_xxx"
}
```

---

## 14. Third-Party APIs

---

### 14.1 Google Places Autocomplete

| | |
|---|---|
| **Base URL** | Google Maps Places API |
| **Endpoint** | `GET json` |
| **Auth Required** | API Key |

**Query Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `input` | String | Yes | Search text |
| `location` | String | Yes | `"lat,lon"` for location bias |
| `key` | String | Yes | Google Maps API Key |
| `radius` | String | Yes | Search radius in meters |

---

### 14.2 Google Directions / Distance

| | |
|---|---|
| **Base URL** | `https://maps.googleapis.com/` |
| **Endpoint** | `GET /maps/api/directions/json` |
| **Auth Required** | API Key |

**Query Parameters:**

| Parameter | Type | Required | Description |
|---|---|---|---|
| `origin` | String | Yes | Origin `"lat,lon"` |
| `destination` | String | Yes | Destination `"lat,lon"` |
| `key` | String | Yes | Google Maps API Key |

---

### 14.3 LinkedIn User Info

| | |
|---|---|
| **Base URL** | `https://api.linkedin.com/v3/` |
| **Endpoint** | `GET userinfo` |
| **Auth Required** | Yes (LinkedIn OAuth Bearer token) |

**Request:** No body — token in Authorization header

**Response:**
```json
{
  "sub": "linkedin_user_id",
  "email": "user@example.com",
  "name": "John Doe",
  "picture": "https://..."
}
```

---

## Error Response Format

All API error responses follow this format:

```json
{
  "message": "Error message here",
  "status": false
}
```

Common HTTP status codes:
- `200` — Success
- `401` — Unauthorized (invalid/expired token)
- `422` — Validation error
- `500` — Server error

---

## Notes for Backend Developer

1. **Token Storage:** Access token received from login/register must be stored and sent in `Authorization: Bearer <token>` header for all protected endpoints.

2. **Token Refresh:** App calls `auth/refresh` whenever a 401 is received to get a new token.

3. **Image Upload:** All document/profile images are sent as **Base64 encoded strings** in the request body.

4. **Firebase Realtime Database:** The app also uses Firebase for:
   - Real-time driver/guard location tracking
   - Booking status updates
   - Wallet balance sync
   - Driver rating list
   These are **not REST API calls** — handled via Firebase SDK directly.

5. **Payment Flow (Stripe 3D Secure):**
   - App calls `StripePayment3DApp` to get `client_secret`
   - Stripe SDK handles 3D authentication with `client_secret`
   - On success, app calls appropriate endpoint to confirm

6. **Language Support:** Most APIs accept a `language` parameter (`"en"` for English, `"ar"` for Arabic).

7. **Pagination:** Paginated endpoints use `offset` parameter (e.g. 0, 10, 20...).

8. **Chat Base URL:** Chat/Call token endpoints use a separate base URL: `https://backend.insta-guard.co.uk/api/v3/`
