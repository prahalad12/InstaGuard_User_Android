package com.instaguard.user.Helpers;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.facebook.BuildConfig;
import com.instaguard.user.Fragments.EditSettingsFragment;
import com.instaguard.user.Fragments.EmergencyContactsFragment;
import com.instaguard.user.Fragments.ForgotPasswordFragment;
import com.instaguard.user.Fragments.InviteFriendsFragment;
import com.instaguard.user.Fragments.LoginFragment;
import com.instaguard.user.Fragments.MyCalenderBookingFragment;
import com.instaguard.user.Fragments.MyWalletFragment;
import com.instaguard.user.Fragments.OnGoingJobsFragment;
import com.instaguard.user.Fragments.PlacesAutoCompleteFragment;
import com.instaguard.user.Fragments.RegisterFragment;
import com.instaguard.user.Fragments.ScheduleTripFragment;
import com.instaguard.user.Fragments.SettingsFragment;
import com.instaguard.user.Fragments.SocialSignupFragment;
import com.instaguard.user.Fragments.StripePaymentFragment;
import com.instaguard.user.Fragments.SubCategoryFragment;
import com.instaguard.user.Fragments.SubscriptionPlanFragment;
import com.instaguard.user.Fragments.SubscriptionPlanHistoryFragment;
import com.instaguard.user.Fragments.SupportFragment;
import com.instaguard.user.Fragments.SupportSubFragment;
import com.instaguard.user.Fragments.WalletTermsFragment;
import com.instaguard.user.Fragments.YourJobsFragment;
import com.instaguard.user.Fragments.YourAnnocementsOffersFragment;
import com.instaguard.user.Fragments.YourWalletTransAllFragment;
import com.instaguard.user.Fragments.YourWalletTransMoneyInFragment;
import com.instaguard.user.Fragments.YourWalletTransMoneyOutFragment;
import com.instaguard.user.Fragments.YourWalletTransactionFragment;
import com.instaguard.user.R;

public class Utils {

    public static String BASE_NAME = "Insta-Guard";

    /*********************************************************************************************/
    //Demo
    public static String BASE_CODE = "_Demo";
    public static String BASE_URL = "https://backend.insta-guard.co.uk/";

    public static final String REDIRECT_URI = "https://backend.insta-guard.co.uk/";
    public static final String REDIRECT_URI_LIVE = "https://backend.insta-guard.co.uk/";

    public static String RETROFIT_URL_CALL_CHAT_ACCESS = "https://backend.insta-guard.co.uk/api/v3/";

    //public static String RETROFIT_URL = "http://instaguard.uplogictech.com/api/";
    public static String RETROFIT_URL = "https://backend.insta-guard.co.uk/api/v3/";

    public static String CALENDER_WEB_URL = "https://backend.insta-guard.co.uk/calender/customer/";

    /*-********************************************************************************************/
    //Live
    /*public static String BASE_CODE = "_Live";
    public static String BASE_URL = "https://backend.insta-guard.co.uk/";

    public static final String REDIRECT_URI = "https://backend.insta-guard.co.uk/";

    public static final String REDIRECT_URI_LIVE = "https://backend.insta-guard.co.uk/";

    public static String RETROFIT_URL_CALL_CHAT_ACCESS = "https://backend.insta-guard.co.uk/api/v3/";

    //public static String RETROFIT_URL = "https://backend.insta-guard.co.uk/api/";
    public static String RETROFIT_URL = "https://backend.insta-guard.co.uk/api/v3/";

    public static String CALENDER_WEB_URL = "https://www.insta-guard.co.uk/calender/customer/";*/

    /*public static String CALENDER_WEB_URL = "https://backend.insta-guard.co.uk/calender/customer/";*/

    /*-********************************************************************************************/

    public static String com_stripe_publishable_key = "pk_test_WDP0fwVbtyJzXdGucvd901EJ";

    public static String BASE_URL_CATEGORY_IMAGE = BASE_URL + "uploads/";
    public static String BASE_URL_IMAGE_UPLOADS = BASE_URL + "/uploads/";
    public static String BASE_URL_BANNER_IMAGE = BASE_URL + "/uploads/";
    public static String BASE_URL_ADMIN = BASE_URL + "admin";

    /*********************************************************************************************/

    public static final String MY_PREFS_NAME = BASE_NAME + BASE_CODE + "_Customer";

    public static final String SP_USERID = MY_PREFS_NAME + "_userId";

    public static final String MY_PREFS_NAME_NEW = MY_PREFS_NAME + "_face_finger";

    /*********************************************************************************************/
    // Google sign in web Credentials
    public static final String GOOGLE_CLIENT_ID_DEMO = "729845483920-8jdenlumt00thchbac11vfrql2hs2akc.apps.googleusercontent.com";
    public static final String GOOGLE_CLIENT_ID_LIVE = "729845483920-qmqskov3408i7if8k0asjcprmhsdc3je.apps.googleusercontent.com";

    /*-********************************************************************************************/

    public static String LINKED_IN_URL = "https://api.linkedin.com/v3/";
    public static final String AUTHURL = "https://www.linkedin.com/oauth/v3/authorization";
    public static final String TOKENURL = "https://www.linkedin.com/oauth/v3/accessToken";
    public static final String SCOPE = "openid%20profile%20email";
    //public static final String SCOPE = "r_liteprofile%20r_emailaddress";
    //    const scope = ['email', 'profile', "openid"]
    /*-********************************************************************************************/

    //Paypal
    public static final int REQUEST_PAYPAL_PAYMENT = 0x2;
    public static final String PAYPAL_CLIENTID = "AYdK2krWwvxK1bPHieQfntCTznmxrDC4-vR5plHTluzS9azWpiNQYvQhcuJAK32SXCwdtPCnZXItSh6y";

    public static final String PAYPAL_RETURN_URL = "com.instaguard.user://paypalpay";

    /****************************** TWILIO *************************************/

    public static String identity = MY_PREFS_NAME + "_identity";

    public static String toIdentity = MY_PREFS_NAME + "_toIdentity";

    public static String mIdentity = MY_PREFS_NAME + "_mIdentity";

    //Direct URL access Reason - Twilio connect callback as *****
    /*-********************************************************************************************/

    public static int SPLASH_DISPLAY_LENGTH = 2000;
    public static int RESPONSECODE_200 = 200;
    public static int RESPONSECODE_201 = 201;
    public static int RESPONSECODE_401 = 401;
    public static int RESPONSECODE_404 = 404;
    public static int RESPONSECODE_500 = 500;
    public static int RESPONSECODE_501 = 501;


    // Retrofit post method header pass
    public static String POST_ACCEPT_CONTENT_TYPE = "application/json";

    public static int DELETE_ACCOUNT_CALL_INTERVAL = 10 * 1000; //10 seconds

    public static final String SP_USER_DELETE_ACCOUNT = MY_PREFS_NAME + "_delete";
    public static final String SP_PAYMENT_HASHMAP_KEYS_ = MY_PREFS_NAME + "_hashmap_";

    //Face & Finger
    public static final String spFingerUserId = MY_PREFS_NAME + "_fingeruserid";
    public static final String spFingerStatus = MY_PREFS_NAME + "_fingerstatus";
    public static final String spFingerNoStatus = MY_PREFS_NAME + "_fingerno";

    public static final String spFaceUserId = MY_PREFS_NAME + "_faceuserid";
    public static final String spFaceStatus = MY_PREFS_NAME + "_facestatus";
    public static final String spFaceNoStatus = MY_PREFS_NAME + "_faceno";

    /*********************************************************************************************/

    //Firebase User Header
    public static String FIREBASE_USER_HEADER = "users";
    public static String FIREBASE_PROVIDERS_HEADER = "providers";
    public static String FIREBASE_TRIP_HEADER = "trips";
    public static String FIREBASE_BOOKING_TRIPS_HEADER = "booking_trips";
    public static String FIREBASE_SERVICE_STATUS = "service_status";
    public static String FIREBASE_ACCEPT_STATUS = "accept_status";
    public static String FIREBASE_BOOKING_ID = "booking_id";

    public static String FIREBASE_USER_FNAME = "fname";
    public static String FIREBASE_USER_LNAME = "lname";
    public static String FIREBASE_USER_EMAIL = "email";
    public static String FIREBASE_USER_CC = "cc";
    public static String FIREBASE_USER_MOBILE = "mobile";
    public static String FIREBASE_USER_PRO_PIC = "pro_pic";
    public static String FIREBASE_USER_LANGUAGE = "language";

    public static String FIREBASE_TRIP_CUS_NAME = "customer_name";
    public static String FIREBASE_TRIP_CUS_ID = "customer_id";

    public static String FIREBASE_NOTIFICATIONS_HEADER = "Notifications";
    public static String FIREBASE_OFFERS_HEADER = "Offers";
    public static String FIREBASE_ANNOUNCEMENTS_HEADER = "Announcements";
    public static String FIREBASE_OFFER_USER_HEADER = "user";
    public static String FIREBASE_OFFER_PROVIDER_HEADER = "provider";

    /*********************************************************************************************/

    //Sharedpreferences

    public static final String SP_USER_ACCESSTOKEN = MY_PREFS_NAME + "_accessToken";
    public static final String SP_USER_TOKENTYPE = MY_PREFS_NAME + "_tokenType";
    public static final String SP_USER_TOKENEXPTIME = MY_PREFS_NAME + "_tokenExpTime";

    public static final String SP_USER_CURRENT_BOOKING_ID = MY_PREFS_NAME + "_current_booking_id";


    public static final String SP_CURRENCY = MY_PREFS_NAME + "_currency";
    public static final String SP_CURRENCY_SYMBOL = MY_PREFS_NAME + "_currency_symbol";
    public static final String SP_LANGUAGE = MY_PREFS_NAME + "_language";
    public static final String SP_LANGUAGE_SHORT_NAME = MY_PREFS_NAME + "_language_short_name";
    public static final String SP_LANGUAGE_COUNTRY = MY_PREFS_NAME + "_language_country_name";

    public static final String PREFS_NAME_CURRENCY = MY_PREFS_NAME + "_Currecny_All";

    public static final String MY_PREFS_NAME_REMEMBER = MY_PREFS_NAME + "_rememberme";

    public static final String MY_PREFS_REQUEST_STATE = MY_PREFS_NAME + "_Request_State";

    public static final String MY_PREFS_FB_CATEGORY_TRIPSTATE = MY_PREFS_NAME + "_category_tripstate";

    public static final String GLOBAL_STORED_ID = MY_PREFS_NAME + "_global_stored";

    public static final String SAMPLE_PROFILE_PIC = "https://www.cars.ro/images/photo_galleries/large/2954/driver-4.jpg";

    public static final String SP_DEVICE_NOTIFY_TYPE = MY_PREFS_NAME + "_regid_notify";

    public static final String SP_COMMON_ADMIN_PAYPAL_STATUS = MY_PREFS_NAME + "_REQUEST_PAYPAL_STATUS";
    public static final String SP_COMMON_WALLET_STATUS = MY_PREFS_NAME + "_REQUEST_WALLET_STATUS_STATUS";
    public static final String SP_COMMON_SUBSCRIPTION_STATUS = MY_PREFS_NAME + "_REQUEST_SUBSCRIPTION_STATUS_STATUS";

    /*********************************************************************************************/

    //Shared preference
    public static final String SP_USER_FNAME = MY_PREFS_NAME + "_userfname";
    public static final String SP_USER_LNAME = MY_PREFS_NAME + "_userlname";
    public static final String SP_USER_EMAIL = MY_PREFS_NAME + "_useremail";
    public static final String SP_USER_PASSWORD = MY_PREFS_NAME + "_userpassword";
    public static final String SP_USER_DOB = MY_PREFS_NAME + "_userdob";
    public static final String SP_USER_PRO_PIC = MY_PREFS_NAME + "_userProPic";
    public static final String SP_USER_STATE = MY_PREFS_NAME + "_userstate";
    public static final String SP_USER_COUNTRY = MY_PREFS_NAME + "_usercountry";
    public static final String SP_USER_ADDRESS = MY_PREFS_NAME + "_useraddress";
    public static final String SP_MAP_BOOKID = MY_PREFS_NAME + "_bookid";
    public static final String SP_USER_MOBILE = MY_PREFS_NAME + "_usermobile";
    public static final String SP_USER_COUNTRYCODE = MY_PREFS_NAME + "_usermobileCC";
    public static final String SP_USER_WALLET_AMT = MY_PREFS_NAME + "_userwalletAmt";
    public static final String SP_USER_WALLET_AMT_MIN = MY_PREFS_NAME + "_userwalletAmtMin";
    public static final String SP_USER_SOCIAL_LOGIN_TYPE = MY_PREFS_NAME + "_sociallogintype";

    public static final String SP_CURRENCY_COMMON_VALUES = MY_PREFS_NAME + "_currency_common_ratio";

    public static final String SP_USER_REFERAL_CODE = MY_PREFS_NAME + "_userRegReferalCode";
    public static final String SP_USER_INVITE_CODE = MY_PREFS_NAME + "_userSendInviteCode";

    public static final String SP_USER_EMAIL_REMEMBERME = MY_PREFS_NAME + "_email_remember";
    public static final String SP_USER_PASS_REMEMBERME = MY_PREFS_NAME + "_pass_remember";
    public static final String SP_CHECKED_REMEMBERME = MY_PREFS_NAME + "_checked_remember";

    public static final String SP_MAP_TRIPSTATE = MY_PREFS_NAME + "_tripstate";

    public static final String SP_FB_CATEGORY_PAYMODE_TRIPSTATE = MY_PREFS_NAME + "_paymode_request";

    public static final String SP_MAP_TRIPSTATE_DISTANCE = MY_PREFS_NAME + "_distance";
    public static final String SP_MAP_TRIPSTATE_DURATION = MY_PREFS_NAME + "_duration";

    public static final String SP_COMMON_CUSTOMER_MANAGE_DOCUMENT = MY_PREFS_NAME + "_CustomerManageDocument";

    public static final String SP_COMMON_STRIPE_PUBLISH_KEY = MY_PREFS_NAME + "_STRIPE_PUBLISH_KEY";
    public static final String SP_COMMON_STRIPE_SECRET_KEY = MY_PREFS_NAME + "_STRIPE_SECRET_KEY";
    public static final String SP_COMMON_DISTANCE_LIMIT = MY_PREFS_NAME + "_DISTANCE_LIMIT";
    public static final String SP_COMMON_DISTANCE_UNIT = MY_PREFS_NAME + "_DISTANCE_UNIT";

    public static final String SP_COMMON_REQUEST_TIME_DELAY_ACCEPT = MY_PREFS_NAME + "_REQUEST_TIME_DELAY_ACCEPT";

    public static final String SP_COMMON_TOWK_CHAT_URL = MY_PREFS_NAME + "_TOWK_CHAT_URL";
    public static final String SP_COMMON_TOWK_PROPERTY_URL = MY_PREFS_NAME + "_TOWK_PROPERTY_URL";
    public static final String SP_COMMON_TOWK_PROPERTY_ID = MY_PREFS_NAME + "_TOWK_PROPERTY_ID";


    public static final String SP_USER_SUBSCRIPTION_CODE = MY_PREFS_NAME + "_USER_SUBSCRIPTION_CODE";
    public static final String SP_USER_SUBSCRIPTION_CODE_ID = MY_PREFS_NAME + "_USER_SUBSCRIPTION_CODE_ID";
    public static final String SP_USER_SUBSCRIPTION_CODE_DISCOUNT_PERCENTAGE = MY_PREFS_NAME + "_USER_SUBSCRIPTION_CODE_PeRCENTAge";
    public static final String SP_USER_SUBSCRIPTION_REMAINING_COUNT = MY_PREFS_NAME + "_USER_SUBSCRIPTION_CODE_REMAINING_COUNT";
    public static final String SP_USER_SUBSCRIPTION_SERVICE_IDS = MY_PREFS_NAME + "_USER_SUBSCRIPTION_CODE_SERVICE_IDS";
    public static final String SP_USER_SUBSCRIPTION_APPOINTMENT_SERVICE_IDS = MY_PREFS_NAME + "_USER_SUBSCRIPTION_CODE_APPOINTMENT_SERVICE_IDS";
    public static final String SP_USER_SUBSCRIPTION_FREE_APPOINTMENT_SERVICE = MY_PREFS_NAME + "_USER_SUBSCRIPTION_FREE_APPOINTMENT_SERVICE";

    /*********************************************************************************************/

    //Fused Location
    public static final String LBM_EVENT_LOCATION_UPDATE = "lbmLocationUpdate";
    public static final String INTENT_FILTER_LOCATION_UPDATE = "intentFilterLocationUpdate";

    public static int countDownTimeLimit = 30;

    //Geocoder
    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static final String PACKAGE_NAME = "com.instaguard.user";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
    public static final String RESULT_DATA = PACKAGE_NAME + ".RESULT_DATA";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";
    public static final String LOCATION_DATA_EXTRA_TYPE = PACKAGE_NAME + ".LOCATION_DATA_EXTRA_TYPE";
    public static final String GEOCODER_TYPE = "GEOCODER";

    public static final int POLYLINE_COLOR_ACCEPTTRIP = 0xFF000000;
    public static final int POLYLINE_COLOR_STARTTRIP = 0xFF3B5998;
    //Map utils
    public static final int MAP_DEFAULT_ZOOM = 15;
    public static final int MAP_ONTRIP_ZOOM = 21;
    public static final long SET_INTERVAL = 5000; //5 Seconds
    public static final long SET_FASTESTINTERVAL = 3000; //3 Seconds
    public static final int REQUEST_CHECK_SETTINGS = 0x1;
    public static final int GEOQUERY_RADIUS = 5;

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 1;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    //   public static final String SHARED_PREF = "ah_firebase";
    public static final String SP_DEVICE_ID = "regid";

    public static final String AUTO_COMPLETE_URL = "https://maps.googleapis.com/maps/api/place/autocomplete/";

    //Places auto complete call
    public static final String AUTOCOMPLETE_MAINACTIVITY = "MainActivity";

    /*********************************************************************************************/

    //Bundle
    public static final String B_USERID = MY_PREFS_NAME + "_userId";
    public static final String B_AUTOCOMPLETELAYOUT = MY_PREFS_NAME + "_autocompletelayout";
    public static final String B_PICKUPAUTOCOMPLETELAYOUT = MY_PREFS_NAME + "_pickuplayout";
    public static final String B_DROPAUTOCOMPLETELAYOUT = MY_PREFS_NAME + "_droplayout";
    public static final String B_NORMALSCHEDULETRIP = MY_PREFS_NAME + "_normalscheduletrip";
    public static final String B_EDITSCHEDULETRIP = MY_PREFS_NAME + "_editscheduletrip";

    public static final String B_EDIT_BOOKINGID_SCHEDULETRIP = MY_PREFS_NAME + "_bookingid";
    public static final String B_EDIT_PICKUP_DATE_SCHEDULETRIP = MY_PREFS_NAME + "_pickupdate";
    public static final String B_EDIT_PICKUP_TIME_SCHEDULETRIP = MY_PREFS_NAME + "_pickuptime";
    public static final String B_EDIT_PICKUP_LAT_SCHEDULETRIP = MY_PREFS_NAME + "_pickuplat";
    public static final String B_EDIT_PICKUP_LNG_SCHEDULETRIP = MY_PREFS_NAME + "_pickuplng";
    public static final String B_EDIT_DROP_LAT_SCHEDULETRIP = MY_PREFS_NAME + "_droplat";
    public static final String B_EDIT_DROP_LNG_SCHEDULETRIP = MY_PREFS_NAME + "_droplng";
    public static final String B_EDIT_PICKUP_LOCATION_SCHEDULETRIP = MY_PREFS_NAME + "_pickuploc";
    public static final String B_EDIT_DROP_LOCATION_SCHEDULETRIP = MY_PREFS_NAME + "_droploc";
    public static final String B_EDIT_CATEGORY_SCHEDULETRIP = MY_PREFS_NAME + "_category";

    public static final String B_WALLET_RECHARGE_AMOUNT = MY_PREFS_NAME + "_RechargeAmount";
    public static final String B_WALLET_PAGE = MY_PREFS_NAME + "_page";

    //Bundle
    public static final String B_DRIVERID = "_userId";
    public static final String B_SUPPORT = "_support";
    public static final String B_SUPPORT_STATIC_DYNAMIC = "_support_static_dynamic";
    public static final String B_SUPPORT_STATIC_DYNAMIC_PAGE = "_support_static_dynamic_page";
    public static final String B_SUPPORT_FROM = "_support_static_dynamic_page_from";
    public static final String B_ABOUTUS = "_aboutus";
    public static final String B_TERMS = "_terms";
    public static final String B_PRIVACY = "_privacy";
    public static final String B_CONTACTUS = "_contactus";
    public static final String B_FAQ = "_faq";

    public static final String Currency = MY_PREFS_NAME + "_$";
    //Category Bundle
    public static final String B_CATEGORY_ID = MY_PREFS_NAME + "_category_id";
    public static final String B_CATEGORY_NAME = MY_PREFS_NAME + "_category_name";
    public static final String B_CATEGORY_NAME_AR = MY_PREFS_NAME + "_category_name_ar";
    public static final String B_CATEGORY_TYPE = MY_PREFS_NAME + "_category_type";
    public static final String B_REQUEST_TYPE = MY_PREFS_NAME + "_request_type";

    public static final String B_CATEGORY_GUARDS_COUNT = MY_PREFS_NAME + "_catgory_count";
    public static final String B_CATEGORY_DAYS = MY_PREFS_NAME + "_catgory_days";
    public static final String B_CATEGORY_TELL_US_EMERGENCY = MY_PREFS_NAME + "_catgory_tell_us_emergency";


    public static final String B_CATEGORY_AGENT_PRICE = MY_PREFS_NAME + "_catgory_agent_price";
    public static final String B_CATEGORY_DAYS_PRICE = MY_PREFS_NAME + "_catgory_days_price";

    public static final String B_CATEGORY_LEVEL_TYPE = MY_PREFS_NAME + "_category_level_type";
    public static final String B_CATEGORY_LEVEL_AMOUNT = MY_PREFS_NAME + "_category_level_amount";


    public static final String B_PICKUP = MY_PREFS_NAME + "_pickup";
    public static final String B_PICKUP_LAT = MY_PREFS_NAME + "_pickup_lat";
    public static final String B_PICKUP_LNG = MY_PREFS_NAME + "_pickup_lng";

    public static final String B_DROP = MY_PREFS_NAME + "_drop";
    public static final String B_DROP_LAT = MY_PREFS_NAME + "_drop_lat";
    public static final String B_DROP_LNG = MY_PREFS_NAME + "_drop_lng";

    public static final String B_DISTANCE = MY_PREFS_NAME + "_distance";

    public static final String B_DISTANCE_LABEL = MY_PREFS_NAME + "_distance_label";
    public static final String B_DURATION = MY_PREFS_NAME + "_duration";
    public static final String B_DURATION_LABEL = MY_PREFS_NAME + "_duration_label";

    public static final String B_TOTAL_EST_AMT = MY_PREFS_NAME + "_total_est_amt";

    public static final String B_CURRENT_LAT = MY_PREFS_NAME + "_current_lat";
    public static final String B_CURRENT_LNG = MY_PREFS_NAME + "_current_lng";

    public static final String B_SUB_CATEGORY_NAME = MY_PREFS_NAME + "_sub_category_name";
    public static final String B_SUB_CATEGORY_NAME_AR = MY_PREFS_NAME + "_sub_category_name_ar";
    public static final String B_SUB_CATEGORY_ID = MY_PREFS_NAME + "_sub_category_id";
    public static final String B_SUB_CATEGORY_PRICE = MY_PREFS_NAME + "_sub_category_price";

    public static final String B_TRIP_TYPE = MY_PREFS_NAME + "_trip_type";
    public static final String B_PICKUP_DATE = MY_PREFS_NAME + "_pickup_date";
    public static final String B_PICKUP_TIME = MY_PREFS_NAME + "_pickup_time";

    public static final String B_PICKUP_DATE_END = MY_PREFS_NAME + "_pickup_date_end";
    public static final String B_PICKUP_TIME_END = MY_PREFS_NAME + "_pickup_time_end";

    public static final String B_PICKUP_DATE_LABEL = MY_PREFS_NAME + "_pickup_date_LABEL";
    public static final String B_PICKUP_TIME_LABEL = MY_PREFS_NAME + "_pickup_time_LABEL";

    public static final String B_PICKUP_DATE_END_LABEL = MY_PREFS_NAME + "_pickup_date_end_LABEL";
    public static final String B_PICKUP_TIME_END_LABEL = MY_PREFS_NAME + "_pickup_time_end_LABEL";
    public static final String B_SCH_TRIP_DRIVERID = MY_PREFS_NAME + "_schedule_trip_driver_id";

    /*********************************************************************************************/

    //Availablility
    public static String[] dayArray = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saterday"};
    public static String[] timeSlotArray = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24"};
    public static String[] timeArray = {"12-01 AM", "01-02 AM", "02-03 AM", "03-04 AM", "04-05 AM", "05-06 AM", "06-07 AM", "07-08 AM", "08-09 AM", "09-10 AM", "10-11 AM", "11-12 AM", "12-01 PM", "01-02 PM", "02-03 PM", "03-04 PM", "04-05 PM", "05-06 PM", "06-07 PM", "07-08 PM", "08-09 PM", "09-10 PM", "10-11 PM", "11-12 PM"};

    /*********************************************************************************************/

    //Fragment transitions
    public static String CURRENT_TAG = null;

    public static final String FRAGMENT_SIGNIN = "LoginFragment";
    public static final String FRAGMENT_SIGNUP = "RegisterFragment";
    public static final String FRAGMENT_FORGOTPASSWORD = "ForgotPasswordFragment";
    public static final String FRAGMENT_RESETPASSWORD = "ResetPasswordFragment";
    public static final String FRAGMENT_PLACESAUTOCOMPLETE = "PlacesAutoCompleteFragment";
    public static final String FRAGMENT_RIDEHISTORY = "RideHistoryFragment";
    public static final String FRAGMENT_UPCOMINGTRIPS = "UpcomingTripsFragment";
    public static final String FRAGMENT_SETTINGS = "SettingsFragment";
    public static final String FRAGMENT_EDITSETTINGS = "EditSettingsFragment";
    public static final String FRAGMENT_MYWALLET = "MyWalletFragment";
    public static final String FRAGMENT_SCHEDULETRIP = "ScheduleTripFragment";
    public static final String FRAGMENT_SCHEDULETRIP_PLACESAUTOCOMPLETE = "ScheduleTripPlacesAutoCompleteFragment";

    public static final String FRAGMENT_YOURJOBS = "YourJobsFragment";
    public static final String FRAGMENT_ONGOINGJOBS = "OnGoingJobsFragment";

    public static final String FRAGMENT_INVITEFRIENDS = "InviteFriendsFragment";
    public static final String FRAGMENT_SUPPORT = "SupportFragment";
    public static final String FRAGMENT_SUPPORT_SUBPAGES = "SupportSubFragment";
    public static final String FRAGMENT_WALLET_TERMS = "WalletTermsFragment";

    public static final String FRAGMENT_EMERGENCY_CONTACTS = "EmergencyContactsFragment";
    public static final String FRAGMENT_SUBCATEGORY = "SubCategoryFragment";
    public static final String FRAGMENT_PROVIDERLIST = "ProviderListFragment";

    public static final String FRAGMENT_STRIPE_PAYMENT = "StripePaymentFragment";

    public static final String FRAGMENT_YOUR_WALLET_TRANSACTION = "YourWalletTransactionFragment";

    private static final String FRAGMENT_YOUR_WALLET_TRANSACTION_ALL = "YourWalletTransAllFragment";
    private static final String FRAGMENT_YOUR_WALLET_TRANSACTION_MONEY_IN = "YourWalletTransMoneyInFragment";
    private static final String FRAGMENT_YOUR_WALLET_TRANSACTION_MONEY_OUT = "YourWalletTransMoneyOutFragment";

    public static final String FRAGMENT_YOUR_OFFERS_ANNOCEMENTS = "YourAnnocementsOffersFragment";
    private static final String FRAGMENT_YOUR_OFFERS = "YourOffersFragment";
    private static final String FRAGMENT_YOUR_ANNOCEMENTS = "YourAnnocementFragment";

    public static final String FRAGMENT_SOCIALSIGNUP = "SocialSignupFragment";

    public static final String FRAGMENT_MY_CALENDER_BOOKING = "MyCalenderBookingFragment";

    public static final String FRAGMENT_SUBSCRIPTION_PLAN = "SubscriptionPlanFragment";
    public static final String FRAGMENT_SUBSCRIPTION_PLAN_HISTORY = "SubscriptionPlanHistoryFragment";

    /*-**************************************************************************************/
    //Fragment Trasitions
    public static void switchContent(int id, String TAG,
                                     FragmentActivity baseActivity, Bundle args, AnimationType transitionStyle) {

        Fragment fragmentToReplace = null;
        FragmentManager fragmentManager = baseActivity
                .getSupportFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // If our current fragment is null, or the new fragment is different, we
        // need to change our current fragment
        Log.i(TAG, "switchContent:" + CURRENT_TAG + "--------" + TAG);
        if (!TAG.equals(CURRENT_TAG)) {

            if (transitionStyle != null) {
                switch (transitionStyle) {
                    case SLIDE_DOWN:
                        // Exit from down
                        transaction.setCustomAnimations(R.anim.slide_up,
                                R.anim.slide_down);
                        break;
                    case SLIDE_UP:
                        // Enter from Up
                        transaction.setCustomAnimations(R.anim.slide_in_up,
                                R.anim.slide_out_up);
                        break;
                    case SLIDE_LEFT:
                    case SLIDE_IN_SLIDE_OUT:
                        // Enter from left
                        transaction.setCustomAnimations(R.anim.slide_left,
                                R.anim.slide_out_left);
                        break;
                    // Enter from right
                    case SLIDE_RIGHT:
                    case SLIDE_IN_SLIDE_OUT_RIGHT:
                        transaction.setCustomAnimations(R.anim.slide_right,
                                R.anim.slide_out_right);
                        break;
                    case FADE_IN:
                        transaction.setCustomAnimations(R.anim.fade_in,
                                R.anim.fade_out);
                    case FADE_OUT:
                        transaction.setCustomAnimations(R.anim.fade_in,
                                R.anim.donot_move);
                        break;
                    default:
                        break;
                }
            }

            // Try to find the fragment we are switching to
            Fragment fragment = fragmentManager.findFragmentByTag(TAG);

            // If the new fragment can't be found in the manager, create a new
            // one
            if (fragment == null) {
                System.out.println("if utils");
                switch (TAG) {
                    case FRAGMENT_SIGNIN:
                        fragmentToReplace = new LoginFragment();
                        break;
                    case FRAGMENT_SIGNUP:
                        fragmentToReplace = new RegisterFragment();
                        break;
                    case FRAGMENT_FORGOTPASSWORD:
                        fragmentToReplace = new ForgotPasswordFragment();
                        break;
                    case FRAGMENT_PLACESAUTOCOMPLETE:
                        fragmentToReplace = new PlacesAutoCompleteFragment();
                        break;
                    case FRAGMENT_SETTINGS:
                        fragmentToReplace = new SettingsFragment();
                        break;
                    case FRAGMENT_EDITSETTINGS:
                        fragmentToReplace = new EditSettingsFragment();
                        break;
                    case FRAGMENT_MYWALLET:
                        fragmentToReplace = new MyWalletFragment();
                        break;
                    case FRAGMENT_YOURJOBS:
                        fragmentToReplace = new YourJobsFragment();
                        break;
                    case FRAGMENT_ONGOINGJOBS:
                        fragmentToReplace = new OnGoingJobsFragment();
                        break;
                    case FRAGMENT_INVITEFRIENDS:
                        fragmentToReplace = new InviteFriendsFragment();
                        break;
                    case FRAGMENT_SUPPORT:
                        fragmentToReplace = new SupportFragment();
                        break;
                    case FRAGMENT_SUPPORT_SUBPAGES:
                        fragmentToReplace = new SupportSubFragment();
                        break;
                    case FRAGMENT_EMERGENCY_CONTACTS:
                        fragmentToReplace = new EmergencyContactsFragment();
                        break;
                    case FRAGMENT_SUBCATEGORY:
                        fragmentToReplace = new SubCategoryFragment();
                        break;
                    case FRAGMENT_STRIPE_PAYMENT:
                        fragmentToReplace = new StripePaymentFragment();
                        break;
                    case FRAGMENT_WALLET_TERMS:
                        fragmentToReplace = new WalletTermsFragment();
                        break;
                    case FRAGMENT_PROVIDERLIST:
                        //fragmentToReplace = new ProviderListFragment();
                        break;
                    case FRAGMENT_SCHEDULETRIP:
                        fragmentToReplace = new ScheduleTripFragment();
                        break;
                    case FRAGMENT_YOUR_WALLET_TRANSACTION:
                        fragmentToReplace = new YourWalletTransactionFragment();
                        break;
                    case FRAGMENT_YOUR_WALLET_TRANSACTION_ALL:
                        fragmentToReplace = new YourWalletTransAllFragment();
                        break;
                    case FRAGMENT_YOUR_WALLET_TRANSACTION_MONEY_IN:
                        fragmentToReplace = new YourWalletTransMoneyInFragment();
                        break;
                    case FRAGMENT_YOUR_WALLET_TRANSACTION_MONEY_OUT:
                        fragmentToReplace = new YourWalletTransMoneyOutFragment();
                        break;
                    case FRAGMENT_YOUR_OFFERS_ANNOCEMENTS:
                        fragmentToReplace = new YourAnnocementsOffersFragment();
                        break;
                    case FRAGMENT_SOCIALSIGNUP:
                        fragmentToReplace = new SocialSignupFragment();
                        break;
                    case FRAGMENT_MY_CALENDER_BOOKING:
                        fragmentToReplace = new MyCalenderBookingFragment();
                        break;
                    case FRAGMENT_SUBSCRIPTION_PLAN:
                        fragmentToReplace = new SubscriptionPlanFragment();
                        break;
                    case FRAGMENT_SUBSCRIPTION_PLAN_HISTORY:
                        fragmentToReplace = new SubscriptionPlanHistoryFragment();
                        break;
                }
            } else {
                System.out.println("else utils");
                switch (TAG) {
                    case FRAGMENT_SIGNIN:
                        fragmentToReplace = (LoginFragment) fragment;
                        break;
                    case FRAGMENT_SIGNUP:
                        fragmentToReplace = (RegisterFragment) fragment;
                        break;
                    case FRAGMENT_FORGOTPASSWORD:
                        fragmentToReplace = (ForgotPasswordFragment) fragment;
                        break;
                    case FRAGMENT_PLACESAUTOCOMPLETE:
                        fragmentToReplace = (PlacesAutoCompleteFragment) fragment;
                        break;
                    case FRAGMENT_SETTINGS:
                        fragmentToReplace = (SettingsFragment) fragment;
                        break;
                    case FRAGMENT_EDITSETTINGS:
                        fragmentToReplace = (EditSettingsFragment) fragment;
                        break;
                    case FRAGMENT_MYWALLET:
                        fragmentToReplace = (MyWalletFragment) fragment;
                        break;
                    case FRAGMENT_YOURJOBS:
                        fragmentToReplace = (YourJobsFragment) fragment;
                        break;
                    case FRAGMENT_ONGOINGJOBS:
                        fragmentToReplace = (OnGoingJobsFragment) fragment;
                        break;
                    case FRAGMENT_INVITEFRIENDS:
                        fragmentToReplace = (InviteFriendsFragment) fragment;
                        break;
                    case FRAGMENT_SUPPORT:
                        fragmentToReplace = (SupportFragment) fragment;
                        break;
                    case FRAGMENT_SUPPORT_SUBPAGES:
                        fragmentToReplace = (SupportSubFragment) fragment;
                        break;
                    case FRAGMENT_EMERGENCY_CONTACTS:
                        fragmentToReplace = (EmergencyContactsFragment) fragment;
                        break;
                    case FRAGMENT_SUBCATEGORY:
                        fragmentToReplace = (SubCategoryFragment) fragment;
                        break;
                    case FRAGMENT_STRIPE_PAYMENT:
                        fragmentToReplace = (StripePaymentFragment) fragment;
                        break;
                    case FRAGMENT_WALLET_TERMS:
                        fragmentToReplace = (WalletTermsFragment) fragment;
                        break;
                    case FRAGMENT_PROVIDERLIST:
                        //fragmentToReplace = (ProviderListFragment) fragment;
                        break;
                    case FRAGMENT_SCHEDULETRIP:
                        fragmentToReplace = (ScheduleTripFragment) fragment;
                        break;
                    case FRAGMENT_YOUR_WALLET_TRANSACTION:
                        fragmentToReplace = (YourWalletTransactionFragment) fragment;
                        break;
                    case FRAGMENT_YOUR_WALLET_TRANSACTION_ALL:
                        fragmentToReplace = (YourWalletTransAllFragment) fragment;
                        break;
                    case FRAGMENT_YOUR_WALLET_TRANSACTION_MONEY_IN:
                        fragmentToReplace = (YourWalletTransMoneyInFragment) fragment;
                        break;
                    case FRAGMENT_YOUR_WALLET_TRANSACTION_MONEY_OUT:
                        fragmentToReplace = (YourWalletTransMoneyOutFragment) fragment;
                        break;
                    case FRAGMENT_YOUR_OFFERS_ANNOCEMENTS:
                        fragmentToReplace = (YourAnnocementsOffersFragment) fragment;
                        break;
                    case FRAGMENT_SOCIALSIGNUP:
                        fragmentToReplace = (SocialSignupFragment) fragment;
                        break;
                    case FRAGMENT_MY_CALENDER_BOOKING:
                        fragmentToReplace = (MyCalenderBookingFragment) fragment;
                        break;
                    case FRAGMENT_SUBSCRIPTION_PLAN:
                        fragmentToReplace = (SubscriptionPlanFragment) fragment;
                        break;
                    case FRAGMENT_SUBSCRIPTION_PLAN_HISTORY:
                        fragmentToReplace = (SubscriptionPlanHistoryFragment) fragment;
                        break;
                }
            }

            CURRENT_TAG = TAG;
            if (args != null)
                fragmentToReplace.setArguments(args);
            // Replace our current fragment with the one we are changing to
            transaction.replace(id, fragmentToReplace, TAG);
            transaction.commit();
        } else {
            if (BuildConfig.DEBUG) Log.e(TAG, "switchContent: Else ~~~~~");
            // Do nothing since we are already on the fragment being changed to

        }
    }

    public static void rlLayoutAnimation(Context context, RelativeLayout layout, AnimationType transitionStyle) {
        Animation startAnimation;
        switch (transitionStyle) {
            case FADE_IN:
                layout.setVisibility(View.VISIBLE);
                startAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
                layout.startAnimation(startAnimation);
                break;
            case FADE_OUT:
                startAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_out);
                layout.startAnimation(startAnimation);
                layout.setVisibility(View.GONE);
                break;
        }
    }

    public enum AnimationType {
        SLIDE_LEFT, SLIDE_RIGHT, SLIDE_UP, SLIDE_DOWN, FADE_IN, SLIDE_IN_SLIDE_OUT, SLIDE_IN_SLIDE_OUT_RIGHT, FADE_OUT
    }
}

