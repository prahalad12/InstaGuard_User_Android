package com.instaguard.user.Activities;

import static com.instaguard.user.Activities.BaseActivity.EMOJI_FILTER;
import static com.instaguard.user.Activities.BaseActivity.callView_;
import static com.instaguard.user.Activities.BaseActivity.checkStringEmpty;
import static com.instaguard.user.Activities.BaseActivity.checkStringEmptyZero;
import static com.instaguard.user.Activities.BaseActivity.convertCurrencyPrice;
import static com.instaguard.user.Activities.BaseActivity.convertHoursToDaysHoursMinutes;
import static com.instaguard.user.Activities.BaseActivity.hideKeyBoard;
import static com.instaguard.user.Activities.BaseActivity.hideProgressDialog;
import static com.instaguard.user.Activities.BaseActivity.saveHashMap;
import static com.instaguard.user.Activities.BaseActivity.saveHashMap_only;
import static com.instaguard.user.Activities.BaseActivity.setHashMapPaymentId;
import static com.instaguard.user.Activities.BaseActivity.setHashMapSaveCardStatus;
import static com.instaguard.user.Activities.BaseActivity.setSetUpIntentOneSave_;
import static com.instaguard.user.Activities.BaseActivity.setVisibility;
import static com.instaguard.user.Activities.BaseActivity.showProgressDialog;
import static com.instaguard.user.Activities.MainActivity.uniqueIdList;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_200;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_201;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_401;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_500;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_501;
import static com.instaguard.user.Helpers.Utils.RETROFIT_URL_CALL_CHAT_ACCESS;
import static com.instaguard.user.Helpers.Utils.mIdentity;
import static com.instaguard.user.Helpers.Utils.toIdentity;
import static java.lang.System.out;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Picture;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.icu.lang.UCharacter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.model.Step;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.facebook.BuildConfig;
import com.facebook.ads.AdView;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.instaguard.user.Adapter.PaymentAdapter;
import com.instaguard.user.Adapter.TripFirebaseGuardListAdapter;
import com.instaguard.user.Adapter.TripFirebaseGuardListRatingAdapter;
import com.instaguard.user.CustomViews.CustomButton;
import com.instaguard.user.CustomViews.CustomEditText;
import com.instaguard.user.CustomViews.CustomSwitch.BaseToggleSwitch;
import com.instaguard.user.CustomViews.CustomSwitch.ToggleSwitch;
import com.instaguard.user.CustomViews.CustomTextView;
import com.instaguard.user.Helpers.CustomDialog.DialogUtils;
import com.instaguard.user.Helpers.DecimalUtils;
import com.instaguard.user.Helpers.FusedLocation.FusedLocationSingleton;
import com.instaguard.user.Helpers.Geocoder.FetchAddressIntentService;
import com.instaguard.user.Helpers.Localizationactivity.ui.LocalizationActivity;
import com.instaguard.user.Helpers.MapUtils;
import com.instaguard.user.Helpers.PermissionUtils;
import com.instaguard.user.Helpers.RatingBar.SimpleRatingBar;
import com.instaguard.user.Helpers.RecyclerViewsTools.RecyclerTouchListener;
import com.instaguard.user.Helpers.Retrofit.APIClient;
import com.instaguard.user.Helpers.Retrofit.APIInterface;
import com.instaguard.user.Helpers.SoundPoolManager;
import com.instaguard.user.Helpers.Utils;
import com.instaguard.user.Models.AppSettingSmsTemplate;
import com.instaguard.user.Models.AppSettingWebModel;
import com.instaguard.user.Models.BalanceWalletAddMoneyModel;
import com.instaguard.user.Models.Bank.BankModel;
import com.instaguard.user.Models.Banner.BannerData;
import com.instaguard.user.Models.Banner.BannerModel;
import com.instaguard.user.Models.CancelJobModel;
import com.instaguard.user.Models.ChatAccessModel;
import com.instaguard.user.Models.EmergencyViewDataModel;
import com.instaguard.user.Models.EmergencyViewModel;
import com.instaguard.user.Models.FareEstimate.FareEstimateModel;
import com.instaguard.user.Models.FirebaseGuardListModel;
import com.instaguard.user.Models.InternalPaymentMethod_;
import com.instaguard.user.Models.PanicAddAdminModel;
import com.instaguard.user.Models.PayPalCreateModel;
import com.instaguard.user.Models.Payment.PaymentIntentModel;
import com.instaguard.user.Models.Payment.SaveWalletPaymentIntentModel;
import com.instaguard.user.Models.RatingDriverModel;
import com.instaguard.user.Models.RatingModel;
import com.instaguard.user.Models.Request.SendRequestModel;
import com.instaguard.user.Models.TripsFirebaseGuardListModel;
import com.instaguard.user.Models.TripsFirebaseGuardRatingListModel;

import com.instaguard.user.R;
import com.logicbeanzs.uberpolylineanimation.MapAnimator;
import com.paypal.android.corepayments.CoreConfig;
import com.paypal.android.corepayments.Environment;
import com.paypal.android.corepayments.PayPalSDKError;
import com.paypal.android.fraudprotection.PayPalDataCollector;
import com.paypal.android.paypalnativepayments.PayPalNativeCheckoutClient;
import com.paypal.android.paypalnativepayments.PayPalNativeCheckoutListener;
import com.paypal.android.paypalnativepayments.PayPalNativeCheckoutRequest;
import com.paypal.android.paypalnativepayments.PayPalNativeCheckoutResult;
import com.paypal.android.paypalnativepayments.PayPalNativePaysheetActions;
import com.paypal.android.paypalnativepayments.PayPalNativeShippingAddress;
import com.paypal.android.paypalnativepayments.PayPalNativeShippingListener;
import com.paypal.android.paypalnativepayments.PayPalNativeShippingMethod;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentAuthConfig;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.Stripe;
import com.stripe.android.googlepaylauncher.GooglePayEnvironment;
import com.stripe.android.googlepaylauncher.GooglePayLauncher;
import com.stripe.android.googlepaylauncher.GooglePayPaymentMethodLauncher;
import com.stripe.android.model.CardParams;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.model.StripeIntent;
import com.stripe.android.model.Token;
import com.stripe.android.payments.paymentlauncher.PaymentLauncher;
import com.stripe.android.payments.paymentlauncher.PaymentResult;
import com.stripe.android.view.CardInputWidget;
import com.twilio.conversations.CallbackListener;
import com.twilio.conversations.Conversation;
import com.twilio.conversations.ConversationListener;
import com.twilio.conversations.ConversationsClient;
import com.twilio.conversations.ConversationsClientListener;
import com.twilio.conversations.Message;
import com.twilio.conversations.Participant;
import com.twilio.conversations.StatusListener;
import com.twilio.conversations.User;
import com.twilio.util.ErrorInfo;
import com.twilio.voice.CallException;
import com.twilio.voice.CallInvite;
import com.twilio.voice.ConnectOptions;
import com.twilio.voice.RegistrationException;
import com.twilio.voice.RegistrationListener;
import com.twilio.voice.Voice;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainJobProgressActivity extends LocalizationActivity implements
        OnMapReadyCallback, GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnCameraIdleListener,
        GoogleApiClient.ConnectionCallbacks, GeoQueryEventListener,
        GoogleApiClient.OnConnectionFailedListener, ValueEventListener, DirectionCallback, GoogleMap.OnMarkerClickListener, TripFirebaseGuardListAdapter.SuccessResponse, TripFirebaseGuardListRatingAdapter.SuccessResponseRating {

    DecimalFormat format = new DecimalFormat("0.#");
    /*Geocoder and Fusedlocation Start*/
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static GoogleApiClient mGoogleApiClient;
    private static final int ACCESS_FINE_LOCATION_INTENT_ID = 3;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS_N = 0x26;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public String mDriverId;

    private static final String BROADCAST_ACTION = "android.location.PROVIDERS_CHANGED";
    Dialog myDialog;

    private AddressResultReceiver mResultReceiver;
    private String mAddressOutput, gettingType;

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final String ADDRESS_REQUESTED_KEY = "address-request-pending";
    private static final String LOCATION_ADDRESS_KEY = "location-address";
    private boolean mAddressRequested;
    public static Location locFusedLocation;
    public Marker markerCurrentLocationMan;
    public boolean beginMoveCamera = true;
    /*Geocoder and Fusedlocation End*/

    /*Googlemap Start*/
    RelativeLayout rlGoogleWatermark;
    private GoogleMap mMap;
    public SupportMapFragment mapFragment;
    /*Googlemap End*/

    /*Trip Status*/
    View lytJobInprogress;
    View lytSMSalert;
    View lytTripSummary;
    View lytCardPayment;
    /*Layout Details*/

    CustomButton btnSmsAlert;
    CustomTextView txtSmsAlertContent;
    /*Trip Status*/

    String strMainCategoryidAr, strMainCategoryid, strSubCategoryid;

    String struserIdFB, strPickupLatFB, strPickupLngFB, strPromoCodeValueFB, strPromoCodeFB,
            strDropLocationFB, strPickupLocationFB;

    String strBookingId, strAcceptTimeFB, strArriveTimeFB, strBeginJobTimeFB, strStartTimeFB, strCancelServiceStatusFB,
            strCreatedIdFB, strUpdatedFB, struserEmailFB, struserFnameFB, struserLnameFB,
            struserMobileFB, strdriverEmailFB, strdriverFnameFB, strdriverIdFB, strdriverLnameFB, strdriverMobileFB, strJobDateFB,
            strJobNumberFB, strModeFB, strPaymentTypeFB, strSiteCommissionFB, strStripIdFB,
            strTotalAmountFB, strUserProfPicFB, strDriverProfPicFB, strUnitPriceFB, strMaterialChargeFB,
            strMiscChargeFB, strBalanceAmountFB, strDriverDiscountFB, strCashStatusFB, strCount, strDays, strPayPalOrderIdFB, strCompanyName;

    String strTotalDistanceFB;
    String strPricePerKMFB;
    String strVisitPriceFB;
    String strMainUnitPriceFB;
    String strLevelTypeFB;
    String strAmountTypeFB;
    String strSubLevelAmountFB;
    String strPriceperAgentFB;
    String strPriceperDayFB;
    String strTotalHrsFB;
    String stPricePerHrsFB;
    String strVatAmountsFB;
    String strExtendedHrsFB;
    String strExtendedAmountFB;
    String strExtendedVatFB;
    String strExtendedStatusFB;
    String strSubscriptionStatusFB;
    String strTripTypeFB = "1";

    DatabaseReference DriverLocDatabaseRef;
    ValueEventListener DriverLocValueEventListener;

    DatabaseReference tripStatusDatabaseRef;
    ValueEventListener tripStatusValueEventListener;

    LatLng AcceptDropLatLng;

    String strEstDuration, strEstDistance;

    /*Trip Status*/

    /*Job In progress start*/
    PopupMenu popup;
    ImageView ivMenuJobinprogress, ivBackJobinprogress, ivAlertMain;
    SimpleDraweeView ivuserProfpicJobinprogress;
    SimpleRatingBar ratingbarJobinprogress;
    CustomTextView txtTitleJobinprogres, txtUsernameJobinprogress,
            txtDaysJobinprogress, txtCountJobinprogress, txtCompanyJobinprogress,
            txtPickupJobinprogress, txtEstDistDurationJobinprogress, txtJobInprogressHeader;

    LinearLayout llTripviewone, llTripviewtwo, llTripviewthree, llTripviewfour;
    CustomTextView txtAcceptone, txtAccepttwo, txtAcceptthree, txtAcceptfour;
    CustomTextView txtAccepttimeone, txtAccepttimetwo, txtAccepttimethree, txtAccepttimefour;
    CustomTextView txtMinagoone, txtMinagotwo, txtMinagothree, txtMinagofour;


    ToggleSwitch switchLiveTrackJobinProgress;

    Animation slideEndtoStart, slideStarttoEnd;

    DatabaseReference driverWalletDatabaseBRef;
    ValueEventListener driverWalletValueEventListener;

    Dialog dialogCustomerinfo;

    AlertDialog.Builder builder, buildera, builderb, builderc;
    /*Job In progress end*/

    boolean doubleBackToExitPressedOnce = false;

    String TAG = "MainActivity";
    CustomTextView txtPickupLocation;
    String userId, userAccessToken, tokenExpTime, strCurrency, strUserProPic, strUserfname, strWalletAmt, strUserlname, strCustomerEmailFB;

    SharedPreferences spCommon, spCurrency;
    SharedPreferences.Editor getState, getRequestState;

    String strWhereFrom, customerLocation, strLanguageShortName, strLanguage, strCurrencySymbol;

    List<String> mobileNumbers = new ArrayList<>();
    String smsTemplateSubject, smsTemplateContent;

    /*Update Driver Rating*/
    Dialog dialogUpdateDriverRatingAfterTrip;
    SimpleRatingBar rbUpdateDriverRating, rbUpdateDriverDialog;
    CustomButton btnCancelUpdateDriverDialog, btnRatingUpdateDriverDialog, btnRatingUpdateDriverRating;
    CustomEditText edtCommentUpdateDriverRating, edtCommentUpdateDriverDialog;
    /*Update Driver Rating*/

    Marker markerPickupDropAccept;
    Polyline directionPolyline;
    Float DriverCurrentBearing;
    LatLng curPos, prevPos = new LatLng(0, 0);
    public Marker driverAcceptCarMarker = null;

    private Map<String, Marker> markers;
    BitmapDescriptor bitmapDescriptor;
    BitmapDescriptor bitmapDescriptorUser;
    DatabaseReference bearingDatabaseRef;
    ValueEventListener bearingValueEventListener;
    float driverBearing = 0.0f;

    /*Image Slider Start*/
    private View parent_view;
    private ViewPager viewPager;
    private LinearLayout layout_dots;
    private AdapterImageSlider adapterImageSlider;
    private Runnable runnable = null;
    private Handler handler = new Handler();
    View progresView, viewImageSlider;
    RecyclerView.LayoutManager layoutManager;
    List<BannerData> bannerData = new ArrayList<>();
    RelativeLayout rlEmptyviewAnnouncement, rlEmptyView;
    /*Image Slider End*/

    SharedPreferences spTripState;
    SharedPreferences.Editor spEditTripState;
    List<Step> step;

    /*Trip Summary*/
    CustomButton btnCollectPaymentTripSummary;
    CustomButton btnMarkAsCompletedJobTripSummary;
    LinearLayout llCurrentChargelyt, llMaterialFeelyt, llMiscFeelyt, llDiscountlyt, llPickupDropLyt, llSurgeFeeLytTripSummary, llDistanceLytTripSummary, llFinalTotallyt;
    CustomTextView txtFinalTotalHeaderTripSummary, txtJobDateTripSummary, txtDiscountTripSummary, txt, txtPaymentTypeTripSummary, txtDistanceTripSummaryLabel, txtDropLocTripSummaryLabel, txtDropLocTripSummary, txtPickupLocTripSummary, txPromoCodeTripSummary, txtCurrentChargeLabelTripSummary, txtCategoryTripSummary, txtRating;
    CustomEditText txtFinalTotalTripSummary, txtCurrentChargeTripSummary, txtMaterialFeeTripSummary, txtSurgeFeeLytTripSummary, txtMiscFeeTripSummary, txtDistanceTripSummary, txtDiscountDriverTripSummary;
    CardView cvRating;
    RecyclerView rcvGuardDetailsListRatingTripSummary;
    TripFirebaseGuardListRatingAdapter tripFirebaseGuardListRatingAdapter;

    Boolean isRatingUpdated;
    CustomTextView txtNoofHrsTripSummary;
    CustomTextView txtPricePerHrsLabelTripSummary;
    CustomTextView txtPricePerHrsTripSummary;
    CustomTextView txtVatAmountTripSummary, txtTotalAmtSummary;
    CustomTextView txtExtendNoofHrsTripSummary;
    CustomTextView txtSubscribtionStatusTripSummary;
    TableRow trExtendNoofHrsTripSummary;

    /*Trip Summary*/

    /*Card Payment*/
    CustomTextView txtCardPaymentTitleLabel;
    CustomButton btnPayStripeCardPayment;
    CardInputWidget creditCardModuleStripeCardPayment;
    String strCardStatus;
    String strPayPalStatus;

    private LinearLayout llCardPaymentDetailsListAreaCardPayment;
    private LinearLayout llCurrentChargeLytCardPayment;
    private CustomTextView txtCurrentChargeLabelCardPayment;
    private CustomEditText txtCurrentChargeCardPayment;
    private LinearLayout llMaterialFeeLytCardPayment;
    private CustomTextView txtMaterialFeeLabelCardPayment;
    private CustomEditText edtMaterialFeeCardPayment;
    private LinearLayout llMiscFeeLytCardPayment;
    private CustomTextView txtMiscFeeLabelCardPayment;
    private CustomEditText edtMiscFeeCardPayment;
    private LinearLayout llDiscountLytCardPayment;
    private CustomTextView txtDiscountDriverLabelCardPayment;
    private CustomEditText edtDiscountDriverCardPayment;
    private CustomEditText edtAgentCardPayment;
    private CustomEditText edtAgentDayCardPayment;
    private LinearLayout llDistanceLytCardPayment;
    private CustomTextView txtDistanceDriverLabelCardPayment;
    private CustomEditText edtDistanceDriverCardPayment;
    private LinearLayout llFinalTotalLytCardPayment;
    private CustomTextView txPromoCodeCardPayment;
    private CustomTextView txtFinalTotalLabelCardPayment;
    private CustomEditText txtFinalTotalCardPayment;
    private LinearLayout llStripeContentCardPayment;
    private CustomTextView txtWaitaMomentCardPayment;
    private View btnPayPalPayment;
    private View btnConfirmExtendPayment;
    private ImageView ivBackCardPayment;
    public int selectedId = -1;

    /*Card Payment*/


    /*Balance Amount */
    Dialog dialogBalanceAmtStripePayment;
    CustomTextView txtBalanceAmount;
    CustomButton btnCancelJobsInProgress;
    CustomButton btnPayStripe;
    CardInputWidget creditCardModuleStripe;
    CardParams cardToSave;
    String stripeTokenID, STRIPE_SECRETKEY, STRIPE_PUBLISHKEY;

    CustomEditText txtBalanceAgentCharge, txtBalanceDayCharge;

    private LinearLayout llCurrentChargeLytBalanceAmtStripePay;
    private CustomTextView txtCurrentChargeLabelBalanceAmtStripePay;
    private CustomEditText txtCurrentChargeBalanceAmtStripePay;
    private LinearLayout llMaterialFeeLytBalanceAmtStripePay;
    private CustomTextView txtMaterialFeeLabelBalanceAmtStripePay;
    private CustomEditText edtMaterialFeeBalanceAmtStripePay;
    private LinearLayout llMiscFeeLytBalanceAmtStripePay;
    private CustomTextView txtMiscFeeLabelBalanceAmtStripePay;
    private CustomEditText edtMiscFeeBalanceAmtStripePay;
    private LinearLayout llDiscountLytBalanceAmtStripePay;
    private CustomTextView txtDiscountDriverLabelBalanceAmtStripePay;
    private CustomEditText edtDiscountDriverBalanceAmtStripePay;
    private LinearLayout llDistanceLytBalanceAmtStripePay;
    private CustomTextView txtDistanceDriverLabelBalanceAmtStripePay;
    private CustomEditText edtDistanceDriverBalanceAmtStripePay;
    private LinearLayout llFinalTotalLytBalanceAmtStripePay;
    private CustomTextView txPromoCodeBalanceAmtStripePay;
    private CustomTextView txtFinalTotalLabelBalanceAmtStripePay, txtAgentFeeLabel, txtDayFeeLabel;

    private CustomEditText txtFinalTotalBalanceAmtStripePay;
    private CustomEditText txtFinalBalanceAmtStripePay;
    private CustomEditText txtMyWalletBalanceAmtStripePay, txtAgentCharge, txtDayCharge, txtBaseCharge;

    private LinearLayout llParentLytAmountDetailsBalanceAmtStripePay;
    /*Balance Amount */

    RoundingParams roundingParams;

    CustomTextView txtCashStatusTripSummary;
    CustomTextView txtBookingIdCommon;

    /*Ads*/
    AdView adViewFacebook;
    com.google.android.gms.ads.AdView adViewGoogle;

    LinearLayout adContainer;
    /*Ads*/

    /*Multi Guards*/
    RecyclerView rcvGuardDetailsList;
    CustomButton btnExtendJobsInProgress;
    View llExtendJobsInProgressDetailsLyt;
    CustomTextView txtExtendHrsJobsInProgress;
    CustomTextView txtExtendHrsAmountJobsInProgress;
    CustomTextView txtExtendHrsVATAmountJobsInProgress;
    CustomTextView txtExtendTotalAmountJobsInProgress;

    CustomTextView txtExtendHrsCardPayment;
    CustomTextView txtExtendHrsAmountCardPayment;
    CustomTextView txtExtendHrsVATAmountCardPayment;
    CustomTextView txtTotalAmountCardPayment;

    Context activity;
    BaseActivity baseActivity;

    APIInterface apiInterface;

    String getBaseFare = "0";

    String getTotalHrs = "0";
    String getPricePerHr = "0";

    String getPricePerGuard = "0";

    String getOverallTotal = "0";
    String getBeforeVatAmount = "0";
    String getTotalGuardAmount = "0";

    String getPromoCode = "";
    String getPromoDiscount = "0";
    String getPromoStatus = "0";

    Switch switchmTraffic;
    RelativeLayout contentView;


    @SuppressLint({"CommitPrefEdits", "SetTextI18n", "LongLogTag"})
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainjobprogress);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        callSharedPreferences();

        this.markers = new HashMap<String, Marker>();
        bitmapDescriptorUser = BitmapDescriptorFactory.fromResource(R.drawable.ic_center_blue_marker);
        bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_guard_marker);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        activity = MainJobProgressActivity.this;

        baseActivity = new BaseActivity();

        try {
            Intent l = getIntent();
            strWhereFrom = l.getStringExtra("from");
            strBookingId = l.getStringExtra("booking_id");
            customerLocation = l.getStringExtra("customer_location");

            if (strBookingId == null) {
                strBookingId = spCommon.getString(Utils.SP_USER_CURRENT_BOOKING_ID, null);
            }

            System.out.println("userid  ~~ Main ~~" + userId);
            System.out.println("booking ~~ MainJobProgressActivity ~~" + strBookingId);

            int color = getResources().getColor(R.color.white);
            roundingParams = RoundingParams.fromCornersRadius(10f);
            roundingParams.setBorder(color, 1.5f);
            roundingParams.setRoundAsCircle(true);

        } catch (Exception e) {
            e.printStackTrace();
        }

        /*Geocoder and Fusedlocation Start*/
        mResultReceiver = new AddressResultReceiver(new Handler());
        updateValuesFromBundle(savedInstanceState);
        showGpsDailog();
        initGoogleAPIClient();//Init Google API Client
        checkPermissions();//Check Permission
        registerReceiver(mGpsSwitchStateReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));   //Register location button receiver
        /*Geocoder and Fusedlocation End*/

        if (isGpsEnable()) {
            callLocationListener();
            fusedLocationCalled();
        } else {
            Toast.makeText(activity, R.string.gps_disable, Toast.LENGTH_SHORT).show();
        }


        initViews();

        callAppSettings();

        callSwitchLiveTrackJobinprogress();

        getTripStatus_Listener();

        callAddCardsView();

        callPayPalIntegration();

        callTwilioSetup();

        callTwilioChatSetup();

        BaseActivity baseActivity = new BaseActivity();
        adContainer = findViewById(R.id.banner_container);
        baseActivity.callFacebookAdsCommon(activity, adViewFacebook, adContainer);

        adViewGoogle = findViewById(R.id.adView);
        baseActivity.callGoogleAdsCommon(activity, adViewGoogle);

        TripFirebaseGuardListAdapter.successResponse = this;
        TripFirebaseGuardListRatingAdapter.successResponse = this;

        onClick();

        isRatingUpdated = false;
    }

    private void callSharedPreferences() {

        getState = getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE).edit();

        getRequestState = getSharedPreferences(Utils.MY_PREFS_REQUEST_STATE, MODE_PRIVATE).edit();

        spCommon = getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE);
        spCurrency = getSharedPreferences(Utils.PREFS_NAME_CURRENCY, MODE_PRIVATE);

        strLanguage = spCurrency.getString(Utils.SP_LANGUAGE, null);
        strLanguageShortName = spCurrency.getString(Utils.SP_LANGUAGE_SHORT_NAME, null);
        if (strLanguageShortName == null) {
            strLanguageShortName = "en";
        }

        strCurrencySymbol = spCurrency.getString(Utils.SP_CURRENCY_SYMBOL, null);
        strCurrency = spCurrency.getString(Utils.SP_CURRENCY, null);

        userId = spCommon.getString(Utils.SP_USERID, null);
        tokenExpTime = spCommon.getString(Utils.SP_USER_TOKENEXPTIME, null);
        userAccessToken = spCommon.getString(Utils.SP_USER_ACCESSTOKEN, null);
        strUserProPic = spCommon.getString(Utils.SP_USER_PRO_PIC, null);
        strUserfname = spCommon.getString(Utils.SP_USER_FNAME, null);
        strUserlname = spCommon.getString(Utils.SP_USER_LNAME, null);
        strWalletAmt = spCommon.getString(Utils.SP_USER_WALLET_AMT, null);

        STRIPE_PUBLISHKEY = spCommon.getString(Utils.SP_COMMON_STRIPE_PUBLISH_KEY, null);
        STRIPE_SECRETKEY = spCommon.getString(Utils.SP_COMMON_STRIPE_SECRET_KEY, null);


        spEditTripState = getSharedPreferences(Utils.SP_MAP_TRIPSTATE, MODE_PRIVATE).edit();
        spTripState = getSharedPreferences(Utils.SP_MAP_TRIPSTATE, MODE_PRIVATE);

        spEditTripState.putString(Utils.SP_MAP_TRIPSTATE, "Normalstate");
        spEditTripState.apply();
    }

    private void initViews() {

        txtPickupLocation = (CustomTextView) findViewById(R.id.txt_pickup_location);
        //  addMarqueeTextfield(txtPickupLocation);

        contentView = findViewById(R.id.frame_mainjob);

        lytSMSalert = (View) findViewById(R.id.lyt_sms_alert);
        lytTripSummary = (View) findViewById(R.id.lyt_trip_summary);
        lytCardPayment = (View) findViewById(R.id.lyt_card_payment);

        lytJobInprogress = findViewById(R.id.lyt_job_inprogress_view);

        txtSmsAlertContent = (CustomTextView) findViewById(R.id.txt_sms);
        btnSmsAlert = (CustomButton) findViewById(R.id.btn_ok_sms);
        switchmTraffic = findViewById(R.id.switchm_traffic);

        callJobInProgressinitViews();

        builder = new AlertDialog.Builder(activity, R.style.AppCompatAlertDialogStyle);
        buildera = new AlertDialog.Builder(activity, R.style.AppCompatAlertDialogStyle);
        builderb = new AlertDialog.Builder(activity, R.style.AppCompatAlertDialogStyle);
        builderc = new AlertDialog.Builder(activity, R.style.AppCompatAlertDialogStyle);

        callTripRatinginitViews();

        callTripSummaryinitViews();

        callCardPaymentinitViews();

        callBalanceCardPaymentinitViews();

        rcvGuardDetailsList = findViewById(R.id.rcv_guard_details_list);
        btnCancelJobsInProgress = findViewById(R.id.btn_call_jobs_job_inprogress);
        btnExtendJobsInProgress = findViewById(R.id.btn_extra_hours_job_inprogress);

        llExtendJobsInProgressDetailsLyt = findViewById(R.id.ll_extra_hours_job_inprogress_lyt);
        txtExtendHrsJobsInProgress = findViewById(R.id.txt_extra_no_of_hrs);
        txtExtendHrsAmountJobsInProgress = findViewById(R.id.txt_extend_no_of_hrs_amount);
        txtExtendHrsVATAmountJobsInProgress = findViewById(R.id.txt_extend_vat_amount);
        txtExtendTotalAmountJobsInProgress = findViewById(R.id.txt_total_fare);

        txtCashStatusTripSummary = findViewById(R.id.generalCommentTxt);
        txtBookingIdCommon = findViewById(R.id.txt_bookingid);

        layout_dots = (LinearLayout) findViewById(R.id.layout_dots);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        rlEmptyviewAnnouncement = (RelativeLayout) findViewById(R.id.rl_emptyview_announcement);
        viewImageSlider = findViewById(R.id.view_image_slider);

        //viewPager.setLayoutMode(View.LAYOUT_DIRECTION_LTR);
        //viewPager.setRotationY(180);
        // viewPager.setRtlOriented(false);
        Log.i(TAG, "onCreateView: viewpager rotation--->" + viewPager.getRotationY());
        adapterImageSlider = new AdapterImageSlider(MainJobProgressActivity.this, new ArrayList<Image>());

        layoutManager = new LinearLayoutManager(activity);
        // rvMain.setLayoutManager(layoutManager);
    }

    private void callTripRatinginitViews() {

        /*Dialog Rating charge*/
        dialogUpdateDriverRatingAfterTrip = new Dialog(activity, R.style.AppTheme);
        dialogUpdateDriverRatingAfterTrip.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialogUpdateDriverRatingAfterTrip.setContentView(R.layout.dialog_update_driver_rating);
        Objects.requireNonNull(dialogUpdateDriverRatingAfterTrip.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogUpdateDriverRatingAfterTrip.setCancelable(false);
        dialogUpdateDriverRatingAfterTrip.getWindow().setWindowAnimations(R.style.DialogAnimationLR);

        rbUpdateDriverDialog = (SimpleRatingBar) dialogUpdateDriverRatingAfterTrip.findViewById(R.id.rb_updatedriverrating);
        edtCommentUpdateDriverDialog = (CustomEditText) dialogUpdateDriverRatingAfterTrip.findViewById(R.id.edt_comment_updatedriverrating);
        btnCancelUpdateDriverDialog = (CustomButton) dialogUpdateDriverRatingAfterTrip.findViewById(R.id.btn_cancel_updatedriverrating);
        btnRatingUpdateDriverDialog = (CustomButton) dialogUpdateDriverRatingAfterTrip.findViewById(R.id.btn_rating_updatedriverrating);
    }

    private void callJobInProgressinitViews() {

        /*Job In progress start */
        switchLiveTrackJobinProgress = findViewById(R.id.switch_container_livetrack_jobinprogress);
        // slide-up animation
        slideEndtoStart = AnimationUtils.loadAnimation(this, R.anim.fade_out_speed);
        slideStarttoEnd = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);

        llTripviewone = (LinearLayout) findViewById(R.id.ll_trip_one);
        llTripviewtwo = (LinearLayout) findViewById(R.id.ll_trip_two);
        llTripviewthree = (LinearLayout) findViewById(R.id.ll_trip_three);
        llTripviewfour = (LinearLayout) findViewById(R.id.ll_trip_four);

        ivBackJobinprogress = (ImageView) findViewById(R.id.iv_back_job_inprogress);
        ivMenuJobinprogress = (ImageView) findViewById(R.id.iv_menu_job_inprogress);
        ivAlertMain = (ImageView) findViewById(R.id.iv_alert_main);

        ivuserProfpicJobinprogress = (SimpleDraweeView) findViewById(R.id.iv_user_prof_pic_job_inprogress);
        ratingbarJobinprogress = (SimpleRatingBar) findViewById(R.id.ratingbar_job_inprogress);

        txtEstDistDurationJobinprogress = (CustomTextView) findViewById(R.id.txt_est_dist_duration_job_inprogress);

        txtJobInprogressHeader = (CustomTextView) findViewById(R.id.txt_job_inprogress_header);
        txtTitleJobinprogres = (CustomTextView) findViewById(R.id.txt_job_inprogres_title);
        txtUsernameJobinprogress = (CustomTextView) findViewById(R.id.txt_username_job_inprogress);
        txtPickupJobinprogress = (CustomTextView) findViewById(R.id.txt_pickup_job_inprogress);
        txtCompanyJobinprogress = (CustomTextView) findViewById(R.id.txt_company_name1);
        txtCountJobinprogress = (CustomTextView) findViewById(R.id.txt_job_count);
        txtDaysJobinprogress = (CustomTextView) findViewById(R.id.txt_job_days);
        txtAcceptone = (CustomTextView) findViewById(R.id.txt_accept_one);
        txtAccepttwo = (CustomTextView) findViewById(R.id.txt_accept_two);
        txtAcceptthree = (CustomTextView) findViewById(R.id.txt_accept_three);
        txtAcceptfour = (CustomTextView) findViewById(R.id.txt_accept_four);

        txtAccepttimeone = (CustomTextView) findViewById(R.id.txt_accept_time_one);
        txtAccepttimetwo = (CustomTextView) findViewById(R.id.txt_accept_time_two);
        txtAccepttimethree = (CustomTextView) findViewById(R.id.txt_accept_time_three);
        txtAccepttimefour = (CustomTextView) findViewById(R.id.txt_accept_time_four);

        txtMinagoone = (CustomTextView) findViewById(R.id.txt_min_ago_one);
        txtMinagotwo = (CustomTextView) findViewById(R.id.txt_min_ago_two);
        txtMinagothree = (CustomTextView) findViewById(R.id.txt_min_ago_three);
        txtMinagofour = (CustomTextView) findViewById(R.id.txt_min_ago_four);

        ivuserProfpicJobinprogress.getHierarchy().setRoundingParams(roundingParams);
        ivuserProfpicJobinprogress.setImageURI("http://demo.uplogictech.com/uberx/admin/images/driver/61128183_w640_h640_rabota_voditelem_spb1.png");
    }

    private void callTripSummaryinitViews() {

        /*Fare Summary Dialog view */
        /*dialogTripSummary = new Dialog(MainActivity.this, R.style.AppTheme);
        dialogTripSummary.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialogTripSummary.setContentView(R.layout.dialog_trip_summary);
        setCancelable(false);*/


        btnCollectPaymentTripSummary = (CustomButton) findViewById(R.id.btn_Collect_payments);
        rcvGuardDetailsListRatingTripSummary = findViewById(R.id.rcv_guard_details_list_rating_);
        btnMarkAsCompletedJobTripSummary = findViewById(R.id.btn_mark_as_completed);
        txtRating = findViewById(R.id.ufxratingDriverHTxt__);

        rbUpdateDriverRating = (SimpleRatingBar) findViewById(R.id.ratingbar_trip_summary);
        edtCommentUpdateDriverRating = (CustomEditText) findViewById(R.id.edt_comment_trip_summary);
        btnRatingUpdateDriverRating = (CustomButton) findViewById(R.id.btn_rating_payment);
        cvRating = (CardView) findViewById(R.id.cv_rating);


        llCurrentChargelyt = (LinearLayout) findViewById(R.id.ll_current_charge_lyt);
        llMaterialFeelyt = (LinearLayout) findViewById(R.id.ll_material_fee_lyt);
        llMiscFeelyt = (LinearLayout) findViewById(R.id.ll_misc_fee_lyt);
        llDiscountlyt = (LinearLayout) findViewById(R.id.ll_discount_lyt);
        llFinalTotallyt = (LinearLayout) findViewById(R.id.ll_final_total_lyt);

        txtFinalTotalHeaderTripSummary = (CustomTextView) findViewById(R.id.txt_total_fare_trip_summary);
        txtJobDateTripSummary = (CustomTextView) findViewById(R.id.txt_date_trip_summary);
        txtDiscountTripSummary = (CustomTextView) findViewById(R.id.txt_discount_trip_summary);
        txtPaymentTypeTripSummary = (CustomTextView) findViewById(R.id.txt_payment_type_trip_summary);
        txtCategoryTripSummary = (CustomTextView) findViewById(R.id.category_trip_summary);
        txtCurrentChargeLabelTripSummary = (CustomTextView) findViewById(R.id.txt_current_charge_label);
        txPromoCodeTripSummary = (CustomTextView) findViewById(R.id.tx_promo_code);

        txtFinalTotalTripSummary = (CustomEditText) findViewById(R.id.txt_final_total);
        txtCurrentChargeTripSummary = (CustomEditText) findViewById(R.id.txt_current_charge);
        txtMaterialFeeTripSummary = (CustomEditText) findViewById(R.id.edt_material_fee);
        txtMiscFeeTripSummary = (CustomEditText) findViewById(R.id.edt_misc_fee);
        txtDiscountDriverTripSummary = (CustomEditText) findViewById(R.id.edt_discount_driver);

        llDistanceLytTripSummary = (LinearLayout) findViewById(R.id.ll_distance_lyt);

        llPickupDropLyt = (LinearLayout) findViewById(R.id.ll_pickup_drop_lyt);
        txtPickupLocTripSummary = (CustomTextView) findViewById(R.id.tx_pickup_loc);
        txtDropLocTripSummary = (CustomTextView) findViewById(R.id.tx_drop_loc);
        txtDropLocTripSummaryLabel = (CustomTextView) findViewById(R.id.txt_drop);

        txtDistanceTripSummaryLabel = (CustomTextView) findViewById(R.id.txt_distance_driver_label);
        txtDistanceTripSummary = (CustomEditText) findViewById(R.id.edt_distance_driver);

        txtAgentFeeLabel = findViewById(R.id.txt_agent_fee_label);
        txtDayFeeLabel = findViewById(R.id.txt_day_fee_label);
        txtAgentCharge = findViewById(R.id.txt_agent_charge);
        txtDayCharge = findViewById(R.id.txt_day_charge);
        txtBaseCharge = findViewById(R.id.txt_base_charge);

        txtNoofHrsTripSummary = findViewById(R.id.txt_no_of_hrs_trip_summary);
        txtPricePerHrsLabelTripSummary = findViewById(R.id.txt_price_per_hrs_trip_summary_label);
        txtPricePerHrsTripSummary = findViewById(R.id.txt_price_per_hrs_trip_summary);
        txtVatAmountTripSummary = findViewById(R.id.txt_vat_amount_trip_summary);
        txtTotalAmtSummary = findViewById(R.id.txt_total_amount_trip_summary);
        trExtendNoofHrsTripSummary = findViewById(R.id.tr_extra_no_of_hrs_trip_summary);
        txtExtendNoofHrsTripSummary = findViewById(R.id.txt_extra_no_of_hrs_trip_summary);

        txtSubscribtionStatusTripSummary = findViewById(R.id.txt_subscribed_user_trip_summary);

        /*Fare Summary Dialog view */
    }

    private void callBalanceCardPaymentinitViews() {
        /*Dialog Balance Stripe Payment*/
        dialogBalanceAmtStripePayment = new Dialog(activity, R.style.AppTheme);
        dialogBalanceAmtStripePayment.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialogBalanceAmtStripePayment.setContentView(R.layout.dialog_balance_stripe_payment);
        Objects.requireNonNull(dialogBalanceAmtStripePayment.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogBalanceAmtStripePayment.setCancelable(false);
        dialogBalanceAmtStripePayment.getWindow().setWindowAnimations(R.style.DialogAnimationLR);

        txtBalanceAgentCharge = (CustomEditText) dialogBalanceAmtStripePayment.findViewById(R.id.txt_card_balance_agent_charge);
        txtBalanceDayCharge = (CustomEditText) dialogBalanceAmtStripePayment.findViewById(R.id.txt_balance_card_day_charge);

        txtBalanceAmount = (CustomTextView) dialogBalanceAmtStripePayment.findViewById(R.id.txt_balance_amount);
        creditCardModuleStripe = (CardInputWidget) dialogBalanceAmtStripePayment.findViewById(R.id.credit_card_module_stripe);
        creditCardModuleStripe.setPostalCodeEnabled(false);

        btnPayStripe = (CustomButton) dialogBalanceAmtStripePayment.findViewById(R.id.pay_button);

        /*Dialog Balance Stripe Payment*/

        llCurrentChargeLytBalanceAmtStripePay = (LinearLayout) dialogBalanceAmtStripePayment.findViewById(R.id.ll_current_charge_lyt_card);
        txtCurrentChargeLabelBalanceAmtStripePay = (CustomTextView) dialogBalanceAmtStripePayment.findViewById(R.id.txt_current_charge_label_card);
        txtCurrentChargeBalanceAmtStripePay = (CustomEditText) dialogBalanceAmtStripePayment.findViewById(R.id.txt_current_charge_card);

        llMaterialFeeLytBalanceAmtStripePay = (LinearLayout) dialogBalanceAmtStripePayment.findViewById(R.id.ll_material_fee_lyt_card);
        txtMaterialFeeLabelBalanceAmtStripePay = (CustomTextView) dialogBalanceAmtStripePayment.findViewById(R.id.txt_material_fee_label_card);
        edtMaterialFeeBalanceAmtStripePay = (CustomEditText) dialogBalanceAmtStripePayment.findViewById(R.id.edt_material_fee_card);

        llMiscFeeLytBalanceAmtStripePay = (LinearLayout) dialogBalanceAmtStripePayment.findViewById(R.id.ll_misc_fee_lyt_card);
        txtMiscFeeLabelBalanceAmtStripePay = (CustomTextView) dialogBalanceAmtStripePayment.findViewById(R.id.txt_misc_fee_label_card);
        edtMiscFeeBalanceAmtStripePay = (CustomEditText) dialogBalanceAmtStripePayment.findViewById(R.id.edt_misc_fee_card);

        llDiscountLytBalanceAmtStripePay = (LinearLayout) dialogBalanceAmtStripePayment.findViewById(R.id.ll_discount_lyt_card);
        txtDiscountDriverLabelBalanceAmtStripePay = (CustomTextView) dialogBalanceAmtStripePayment.findViewById(R.id.txt_discount_driver_label_card);
        edtDiscountDriverBalanceAmtStripePay = (CustomEditText) dialogBalanceAmtStripePayment.findViewById(R.id.edt_discount_driver_card);

        llDistanceLytBalanceAmtStripePay = (LinearLayout) dialogBalanceAmtStripePayment.findViewById(R.id.ll_distance_lyt_card);
        txtDistanceDriverLabelBalanceAmtStripePay = (CustomTextView) dialogBalanceAmtStripePayment.findViewById(R.id.txt_distance_driver_label_card);
        edtDistanceDriverBalanceAmtStripePay = (CustomEditText) dialogBalanceAmtStripePayment.findViewById(R.id.edt_distance_driver_card);

        llFinalTotalLytBalanceAmtStripePay = (LinearLayout) dialogBalanceAmtStripePayment.findViewById(R.id.ll_final_total_lyt_card);
        txPromoCodeBalanceAmtStripePay = (CustomTextView) dialogBalanceAmtStripePayment.findViewById(R.id.tx_promo_code_card);
        txtFinalTotalLabelBalanceAmtStripePay = (CustomTextView) dialogBalanceAmtStripePayment.findViewById(R.id.txt_final_total_label_card);

        txtFinalTotalBalanceAmtStripePay = (CustomEditText) dialogBalanceAmtStripePayment.findViewById(R.id.txt_final_total_card);
        txtMyWalletBalanceAmtStripePay = (CustomEditText) dialogBalanceAmtStripePayment.findViewById(R.id.txt_final_wallet_card);
        txtFinalBalanceAmtStripePay = (CustomEditText) dialogBalanceAmtStripePayment.findViewById(R.id.txt_final_bal_card);

        llParentLytAmountDetailsBalanceAmtStripePay = (LinearLayout) dialogBalanceAmtStripePayment.findViewById(R.id.ll_card_payment_details_list_area_card);

        /*Dialog Balance Stripe Payment*/
    }

    private void callCardPaymentinitViews() {

        txtExtendHrsCardPayment = findViewById(R.id.txt_extra_no_of_hrs_card);
        txtExtendHrsAmountCardPayment = findViewById(R.id.txt_extend_no_of_hrs_amount_card);
        txtExtendHrsVATAmountCardPayment = findViewById(R.id.txt_extend_vat_amount_card);
        txtTotalAmountCardPayment = findViewById(R.id.txt_total_amount_card);


        txtCardPaymentTitleLabel = (CustomTextView) findViewById(R.id.txt_balance_amount_card);
        creditCardModuleStripeCardPayment = (CardInputWidget) findViewById(R.id.credit_card_module_stripe_card);
        creditCardModuleStripeCardPayment.setPostalCodeEnabled(false);


        llCardPaymentDetailsListAreaCardPayment = (LinearLayout) findViewById(R.id.ll_card_payment_details_list_area_card);

        llCurrentChargeLytCardPayment = (LinearLayout) findViewById(R.id.ll_current_charge_lyt_card);
        txtCurrentChargeLabelCardPayment = (CustomTextView) findViewById(R.id.txt_current_charge_label_card);
        txtCurrentChargeCardPayment = (CustomEditText) findViewById(R.id.txt_current_charge_card);

        llMaterialFeeLytCardPayment = (LinearLayout) findViewById(R.id.ll_material_fee_lyt_card);
        txtMaterialFeeLabelCardPayment = (CustomTextView) findViewById(R.id.txt_material_fee_label_card);
        edtMaterialFeeCardPayment = (CustomEditText) findViewById(R.id.edt_material_fee_card);

        llMiscFeeLytCardPayment = (LinearLayout) findViewById(R.id.ll_misc_fee_lyt_card);
        txtMiscFeeLabelCardPayment = (CustomTextView) findViewById(R.id.txt_misc_fee_label_card);
        edtMiscFeeCardPayment = (CustomEditText) findViewById(R.id.edt_misc_fee_card);

        llDiscountLytCardPayment = (LinearLayout) findViewById(R.id.ll_discount_lyt_card);
        txtDiscountDriverLabelCardPayment = (CustomTextView) findViewById(R.id.txt_discount_driver_label_card);
        edtDiscountDriverCardPayment = (CustomEditText) findViewById(R.id.edt_discount_driver_card);


        edtAgentCardPayment = (CustomEditText) findViewById(R.id.txt_card_agent_charge);
        edtAgentDayCardPayment = (CustomEditText) findViewById(R.id.txt_card_day_charge);


        llDistanceLytCardPayment = (LinearLayout) findViewById(R.id.ll_distance_lyt_card);
        txtDistanceDriverLabelCardPayment = (CustomTextView) findViewById(R.id.txt_distance_driver_label_card);
        edtDistanceDriverCardPayment = (CustomEditText) findViewById(R.id.edt_distance_driver_card);

        llFinalTotalLytCardPayment = (LinearLayout) findViewById(R.id.ll_final_total_lyt_card);
        txPromoCodeCardPayment = (CustomTextView) findViewById(R.id.tx_promo_code_card);
        txtFinalTotalLabelCardPayment = (CustomTextView) findViewById(R.id.txt_final_total_label_card);
        txtFinalTotalCardPayment = (CustomEditText) findViewById(R.id.txt_final_total_card);

        llStripeContentCardPayment = (LinearLayout) findViewById(R.id.ll_stripe_content_card);

        btnPayStripeCardPayment = findViewById(R.id.pay_button_card);
        btnPayPalPayment = findViewById(R.id.pay_button_paypal);
        btnConfirmExtendPayment = findViewById(R.id.pay_button_);
        rcvSelectPayment = findViewById(R.id.rv_selectpayment);

        ivBackCardPayment = findViewById(R.id.imgbtn_back_add_card_also__card);

        txtWaitaMomentCardPayment = findViewById(R.id.txt_wait_a_moment_card_payment);
    }


    private void callSwitchLiveTrackJobinprogress() {

        switchLiveTrackJobinProgress.setCheckedTogglePosition(0);
        Log.e("Inside ", "infirststate" + switchLiveTrackJobinProgress.getCheckedTogglePosition());

        switchLiveTrackJobinProgress.setOnToggleSwitchChangeListener(new BaseToggleSwitch.OnToggleSwitchChangeListener() {
            @Override
            public void onToggleSwitchChangeListener(int position, boolean isChecked) {
                Log.e(TAG, "onToggleSwitchChangeListener: " + position + " ~~~" + isChecked);
                if (position == 0) {
                    Log.e(TAG, "onToggleSwitchChangeListener: Login ~ 000");
                    if (lytJobInprogress.getVisibility() == View.VISIBLE) {
                        lytJobInprogress.setVisibility(View.GONE);
                        currentLocationClickDriver();
                    }
                } else {
                    Log.e(TAG, "onToggleSwitchChangeListener: Signup ~ 111");
                    if (lytJobInprogress.getVisibility() == View.GONE) {
                        lytJobInprogress.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void initComponent(Activity requireActivity) {
        final List<Image> items = new ArrayList<>();

        Log.i(TAG, "initComponent: " + bannerData.size());
        for (int i = 0; i < bannerData.size(); i++) {
            Image obj = new Image();
            obj.image = bannerData.get(i).getImages();
            obj.name = bannerData.get(i).getTitle();
            obj.caption = bannerData.get(i).getDescription();
            items.add(obj);
        }
        adapterImageSlider.setItems(items);
        viewPager.setAdapter(adapterImageSlider);
        addBottomDots(layout_dots, adapterImageSlider.getCount(), 0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int pos, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int pos) {
                addBottomDots(layout_dots, adapterImageSlider.getCount(), pos);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        startAutoSlider(adapterImageSlider.getCount());
        rlEmptyviewAnnouncement.setVisibility(View.GONE);
        viewImageSlider.setVisibility(View.VISIBLE);

    }


    private void addBottomDots(LinearLayout layout_dots, int size, int current) {
        try {
            ImageView[] dots = new ImageView[size];

            layout_dots.removeAllViews();
            for (int i = 0; i < dots.length; i++) {
                dots[i] = new ImageView(activity);
                int width_height = 15;
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
                params.setMargins(10, 10, 10, 10);
                params.setLayoutDirection(0);
                dots[i].setLayoutParams(params);
                dots[i].setImageResource(R.drawable.shape_circle_outline_slider_image);
                layout_dots.addView(dots[i]);
            }

            if (dots.length > 0) {
                dots[current].setImageResource(R.drawable.shape_circle_slider_image);
            }
        } catch (Exception e) {
            Log.i(TAG, "addBottomDots: crash in addbottom -- >" + e.toString());
        }
    }

    private void startAutoSlider(final int count) {
        Log.i(TAG, "startAutoSlider: runnable--->" + runnable);
        runnable = new Runnable() {
            @Override
            public void run() {
                int pos = viewPager.getCurrentItem();
                pos = pos + 1;
                if (pos >= count) pos = 0;
                viewPager.setCurrentItem(pos);
                handler.postDelayed(runnable, 5000);
            }
        };
        handler.postDelayed(runnable, 5000);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(getApplicationContext(),
                        "Marker Clicked: " + marker.getTitle(), Toast.LENGTH_LONG)
                .show();
        return false;
    }

    @Override
    public void onKeyEntered(String key, GeoLocation location) {

        String tripState = spTripState.getString(Utils.SP_MAP_TRIPSTATE, null);
        Log.i(TAG, "onKeyEntered: " + key + " ");
        if (Objects.equals(tripState, "Starttrip") || Objects.equals(tripState, "Accepttrip")) {
            FirebaseDatabase.getInstance().getReference().child("drivers_location").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Object bearingObj = dataSnapshot.child("bearing").getValue();
                    String markerPosition;
                    if (bearingObj != null)
                        markerPosition = dataSnapshot.child("bearing").getValue().toString();
                    else
                        markerPosition = "0.0";

                    Log.i(TAG, "onDataChange in onkeyentered-->: " + markerPosition);
                    Marker marker1 = markers.get(key);
                    marker1 = mMap.addMarker(new MarkerOptions().position(new LatLng(location.latitude, location.longitude)).icon(bitmapDescriptor).flat(true));
                    marker1.setRotation(Float.parseFloat(markerPosition));
                    marker1.setAnchor(0.5f, 0.5f);
                    Animator animator = ObjectAnimator.ofFloat(marker1, "alpha", 0f, 1f);
                    animator.setInterpolator(new DecelerateInterpolator()); //add this
                    Marker finalMarker = marker1;
                    animator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationEnd(Animator animator) {
                            markers.put(key, finalMarker);
                        }

                        @Override
                        public void onAnimationStart(Animator animator) {
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {
                        }
                    });
                    animator.setDuration(1000).start();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } else {

            Log.i(TAG, "onKeyEntered: " + spTripState.getString(Utils.SP_MAP_TRIPSTATE, null));

        }
    }

    @Override
    public void onKeyExited(String key) {

        Log.i(TAG, "onKeyExited: " + key);
        Marker marker_remove = this.markers.get(key);

        Animator animator = ObjectAnimator.ofFloat(marker_remove, "alpha", 1f, 0f);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                if (marker_remove != null) {
                    marker_remove.remove();
                } else {
                    Log.i(TAG, "onKeyExited: marker did not remove");
                }
            }

            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        animator.setDuration(500).start();

    }

    @Override
    public void onKeyMoved(String key, GeoLocation location) {

        Log.i(TAG, "onKeyMoved: ");
        Marker marker = this.markers.get(key);

        if (marker != null) {
            LatLng curPos, prevPos;

            prevPos = new LatLng(marker.getPosition().latitude, marker.getPosition().latitude);
            curPos = new LatLng(location.latitude, location.latitude);

            if (!prevPos.equals(curPos)) {
                double[] startValues = new double[]{marker.getPosition().latitude, marker.getPosition().longitude};
                double[] endValues = new double[]{location.latitude, location.longitude};
                System.out.println("Driver ID call listion key moved===>" + key);
                baseActivity.rotateMarker(marker, getDriverBearing(key), startValues, endValues);
            }
        }
    }

    @Override
    public void onGeoQueryReady() {

    }

    @Override
    public void onGeoQueryError(DatabaseError error) {

    }


    private static class AdapterImageSlider extends PagerAdapter {

        private Activity act;
        private List<Image> items;

        private OnItemClickListener onItemClickListener;

        private interface OnItemClickListener {
            void onItemClick(View view, Image obj);
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        // constructor
        private AdapterImageSlider(Activity requireActivity, List<Image> items) {
            this.act = requireActivity;
            this.items = items;
        }

        @Override
        public int getCount() {
            return this.items.size();
        }

        public Image getItem(int pos) {
            return items.get(pos);
        }

        public void setItems(List<Image> items) {
            this.items = items;
            notifyDataSetChanged();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            final Image o = items.get(position);
            LayoutInflater inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.item_slider_image_main, container, false);

            SimpleDraweeView image = (SimpleDraweeView) v.findViewById(R.id.image);
            CustomTextView title = (CustomTextView) v.findViewById(R.id.title);
            CustomTextView brief = (CustomTextView) v.findViewById(R.id.brief);

            title.setText(o.name);
            brief.setText(o.caption);
            Uri imageUri = Uri.parse(Utils.BASE_URL_BANNER_IMAGE + o.image);
            Log.i("ddd", "instantiateItem: int the list--->" + o.image);
            //  Log.i("image", "instantiateItem: "+(Utils.BASE_IMAGE_URL+o.image.toString()));
            image.setImageURI(imageUri);

            ((ViewPager) container).addView(v);

            return v;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            ((ViewPager) container).removeView((RelativeLayout) object);

        }

    }

    public class Image {
        public String name;
        public String caption;
        public String image;
        public Integer counter = null;

    }
    /*Image Slider End*/


    @SuppressLint("ClickableViewAccessibility")
    private void onClick() {

        switchmTraffic.setOnCheckedChangeListener((compoundButton, b) -> updateTraffic());
        ivBackJobinprogress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tripStatusDatabaseRef != null) {
                    tripStatusDatabaseRef.removeEventListener(tripStatusValueEventListener);
                }

                getState.putString(Utils.SP_USER_CURRENT_BOOKING_ID, null).apply();
                if (strWhereFrom.equals("main")) {
                    Intent mainActivityIntent = new Intent(activity, MainActivity.class);
                    mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainActivityIntent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                } else {
                    finish();
                }
            }
        });
        btnSmsAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lytSMSalert.setVisibility(View.GONE);
            }
        });

        ivAlertMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callViewEmergencyDetails();
            }
        });

        ratingbarJobinprogress.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        btnRatingUpdateDriverRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtCommentUpdateDriverRating.getText().toString().length() == 0) {
                    edtCommentUpdateDriverRating.setError(getString(R.string.error_required));
                } else {
                    String ratings = String.valueOf(rbUpdateDriverRating.getRating());
                    String edtcomments = edtCommentUpdateDriverRating.getText().toString();
                    callUpdateDriverRating("normal", userId, strBookingId, strdriverIdFB, ratings, edtcomments);
                }
            }
        });

        btnRatingUpdateDriverDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtCommentUpdateDriverDialog.getText().toString().length() == 0) {
                    edtCommentUpdateDriverDialog.setError(getString(R.string.error_required));
                } else {
                    callUpdateDriverDialog();
                }
            }
        });

        btnCollectPaymentTripSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  lytTripSummary.setVisibility(View.GONE);

                if (strBookingId != null) {
                    removeBookingTripsFirebase(strBookingId);
                }
                getState.putString(Utils.SP_USER_CURRENT_BOOKING_ID, null).apply();

                Intent mainActivityIntent = new Intent(activity, MainActivity.class);
                mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainActivityIntent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });

        btnCancelUpdateDriverDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dialogUpdateDriverRatingAfterTrip.isShowing()) {
                    dialogUpdateDriverRatingAfterTrip.dismiss();
                }
               /* paymentCompleteDialog();
                if(strBookingId!=null)
                    removeBookingTripsFirebase(strBookingId);*/
            }
        });

        ratingbarJobinprogress.setFocusable(false);

        ivMenuJobinprogress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popup = new PopupMenu(Objects.requireNonNull(activity), ivMenuJobinprogress);

                switch (reqStatus) {
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                    case "0":
                    default:
                        popup.getMenuInflater().inflate(R.menu.jobinprogress_menu, popup.getMenu());
                        break;
                    case "5":
                    case "5.1":
                    case "6":
                    case "7":
                    case "8":
                    case "9":
                        popup.getMenuInflater().inflate(R.menu.jobinprogress_menu_trip, popup.getMenu());
                        break;
                }

                /*if (scrollviewJobInprogress.getVisibility() == View.VISIBLE) {
                    popup.getMenuInflater().inflate(R.menu.jobinprogress_menu, popup.getMenu());
                } else {
                    popup.getMenuInflater().inflate(R.menu.jobinprogress_menu1, popup.getMenu());
                }*/

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @SuppressLint({"LongLogTag", "NonConstantResourceId"})
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        // if(R.id.call_job == )

                        /*switch (id) {
                            case R.id.call_job:
                                callDriverTelephonyManager(strdriverMobileFB);

                                break;
                            case R.id.msg_job:
                                // Call permissions is already available, show the camera preview.
                                try {
                                    //  startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", drivermobile, null)));
                                    Intent smsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("smsto:" + strdriverMobileFB));
                                    smsIntent.putExtra("sms_body", "Hi " + strdriverFnameFB + strdriverLnameFB);
                                    startActivity(smsIntent);
                                } catch (ActivityNotFoundException e) {
                                    // Display some sort of error message here.
                                }
                                break;
                            case R.id.livetrack_job:
                                if (lytJobInprogress.getVisibility() == View.VISIBLE) {
                                    lytJobInprogress.setVisibility(View.GONE);
                                    txtJobInprogressHeader.setText("Live Track");
                                } else {
                                    lytJobInprogress.setVisibility(View.VISIBLE);
                                    txtJobInprogressHeader.setText("Job Progress");
                                }
                                break;
                            case R.id.sos_job:
                                //showCustomerInfodialog();
                                callViewEmergencyDetails();
                                break;

                            case R.id.cancel_job:

                                callCancelJobAleryDialog();

                                break;

                        }*/
                        if (id == R.id.call_job) {
                            callDriverTelephonyManager(strdriverMobileFB);

                        } else if (id == R.id.msg_job) {
                            // Call permissions is already available, show the camera preview.
                            try {
                                //  startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", drivermobile, null)));
                                Intent smsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("smsto:" + strdriverMobileFB));
                                smsIntent.putExtra("sms_body", "Hi " + strdriverFnameFB + strdriverLnameFB);
                                startActivity(smsIntent);
                            } catch (ActivityNotFoundException e) {
                                // Display some sort of error message here.
                            }

                        } else if (id == R.id.livetrack_job) {
                            if (lytJobInprogress.getVisibility() == View.VISIBLE) {
                                lytJobInprogress.setVisibility(View.GONE);
                                txtJobInprogressHeader.setText("Live Track");
                            } else {
                                lytJobInprogress.setVisibility(View.VISIBLE);
                                txtJobInprogressHeader.setText("Job Progress");
                            }

                        } else if (id == R.id.sos_job) {
                            callViewEmergencyDetails();

                        } else if (id == R.id.cancel_job) {
                            callCancelJobAleryDialog();
                        }

                        return false;
                    }
                });
                popup.show();

            }
        });

        btnCancelJobsInProgress.setOnClickListener(v -> {
            callCancelJobAleryDialog();
        });

        btnPayStripe.setOnClickListener(v -> {
            callClickAction(btnPayStripe, false);
            //stripePayment("BalancePayment");
        });

        /*btnPayStripeCardPayment.setOnClickListener(v -> {
            callClickAction(btnPayStripeCardPayment, false);
            //stripePayment("CardPayment");
            if ("save_card".equals("save_card")) {
                if (getOverallTotal != null && !getOverallTotal.equals("0")) {
                    callSaveUpdateDetails(getOverallTotal, "stripe");
                } else {
                    Toast.makeText(activity, getResources().getString(R.string.total_amount_empty_try_again), Toast.LENGTH_SHORT).show();
                }
            } else {
                if (getOverallTotal != null && !getOverallTotal.equals("0")) {
                    callUpdateStripAlsoSaveCard(getOverallTotal);
                } else {
                    Toast.makeText(activity, getResources().getString(R.string.total_amount_empty_try_again), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnPayPalPayment.setOnClickListener(view -> {
            try {
                if (getOverallTotal != null && !getOverallTotal.equals("0")) {
                    BaseActivity.callClickAction(btnPayPalPayment, false);
                    callCreatePayPalPayment(getOverallTotal);
                } else {
                    callClickAction(btnPayPalPayment, true);
                    Toast.makeText(activity, getResources().getString(R.string.total_amount_empty_try_again), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                hideProgressDialog();
                callClickAction(btnPayPalPayment, true);
                System.out.println("~~~Exception~~~btnPayPalPayment~~~" + e.toString());
            }
        });*/

        rcvSelectPayment.addOnItemTouchListener(
                new RecyclerTouchListener(activity, rcvSelectPayment,
                        new RecyclerTouchListener.OnTouchActionListener() {
                            @SuppressLint({"LongLogTag", "ObsoleteSdkInt"})
                            @Override
                            public void onClick(View view, int position) {
                                Log.i("onClick", ": rcvSelectPayment-> ");
                                if (position != -1) {
                                    String getId = String.valueOf(paymentMethodList.get(position).getId());
                                    String getName = String.valueOf(paymentMethodList.get(position).getName());
                                    String getKey = String.valueOf(paymentMethodList.get(position).getKey());
                                    String getStatus = String.valueOf(paymentMethodList.get(position).getStatus());
                                    Log.e("rcvSelectPayment~~Onclick~~", getId + "~~" + getName + "~~" + getKey + "~~" + getStatus);

                                    if (getStatus.equals("1")) {

                                        if (paymentAdapter != null) {
                                            paymentAdapter.selected(position);
                                        }

                                        switch (getKey) {
                                            case "card":
                                                selectedId = 2;
                                                break;
                                            case "gpay":
                                                selectedId = 8;
                                                break;
                                            case "paypal":
                                                selectedId = 6;
                                                break;
                                            case "wallet":
                                                selectedId = 0;
                                                break;
                                            default:
                                                selectedId = -1;
                                                break;
                                        }
                                    } else {
                                        Toast.makeText(activity, getResources().getString(R.string.other_select_payment_option), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onRightSwipe(View view, int position) {
                            }

                            @Override
                            public void onLeftSwipe(View view, int position) {
                            }
                        }));

        btnConfirmExtendPayment.setOnClickListener(v -> {

            out.println("rcv SelectPayment->> " + selectedId);
            //Log.e(TAG, "rGSelectPayment: " + rGSelectPayment.getCheckedRadioButtonId());
            //if (rGSelectPayment.getCheckedRadioButtonId() == -1) {
            if (selectedId == -1) {
                //empty selection
                Toast.makeText(MainJobProgressActivity.this, getResources().getString(R.string.toast_pls_select_payment_option), Toast.LENGTH_SHORT).show();
            } else {

                //0-rb_mywallet 1-view
                //2-rb_payonline 3-view
                //4-rb_cashonhand  5-view
                //6-rb_paypal 7-view
                //8-rb_google_pay 9-view
                //10-google_pay_button

                if (selectedId == 0) { //Wallet
                    BaseActivity.callClickAction(btnConfirmExtendPayment, true);
                    callRequestPaymode("wallet");
                } else if (selectedId == 2) {  //Card payment
                    BaseActivity.callClickAction(btnConfirmExtendPayment, true);
                    callRequestPaymode("card");
                } else if (selectedId == 4) { //cash
                    BaseActivity.callClickAction(btnConfirmExtendPayment, true);
                    callRequestPaymode("cash");
                } else if (selectedId == 6) { //Paypal
                    BaseActivity.callClickAction(btnConfirmExtendPayment, true);
                    callRequestPaymode("paypal");
                } else if (selectedId == 8) { //Google Pay
                    BaseActivity.callClickAction(btnConfirmExtendPayment, true);
                    callRequestPaymode("google_pay");
                }
            }
        });

       /* btnPayPalPayment.setOnClickListener(view -> {
            try {
                callClickAction(btnPayPalPayment, false);
                *//*
                System.out.println("paypal amount ==>>>" + strTotalAmountFB);
                payPalPayment = new PayPalPayment(new BigDecimal(strTotalAmountFB), "USD",
                        getResources().getString(R.string.paypal_totalpayment_txt),
                        PayPalPayment.PAYMENT_INTENT_SALE);
                Intent intent = new Intent(requireActivity,
                        com.paypal.android.sdk.payments.PaymentActivity.class);
                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
                startActivityForResult(intent, Utils.REQUEST_PAYPAL_PAYMENT);*//*

                if (strPayPalOrderIdFB != null && !strPayPalOrderIdFB.equals("") && !strPayPalOrderIdFB.equals("null")) {

                    // showProgressDialog(requireActivity);
                    String id = strPayPalOrderIdFB;
                    //if (com.instaguard.user.BuildConfig.DEBUG){
                    System.out.println("~~~~btnPayPalPayment~~~id~~~" + id);
                    System.out.println("~~~~btnPayPalPayment~~~strCustomerEmailFB~~~" + strCustomerEmailFB);
                    //}
                    PayPalNativeCheckoutRequest request = new PayPalNativeCheckoutRequest(id, strCustomerEmailFB);
                    paypalNativeClient.startCheckout(request);

                } else {
                    callCreatePayPalPayment();
                }

            } catch (Exception e) {
                e.printStackTrace();
                hideProgressDialog();
                callClickAction(btnPayPalPayment, true);
                System.out.println("~~~Exception~~~btnPayPalPayment~~~" + e.toString());
            }
        });*/

        //txtCashStatusTripSummary.setOnClickListener(v -> callPaymentFinish_());
        btnMarkAsCompletedJobTripSummary.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setCancelable(false);
            Typeface tf = Typeface.createFromAsset(activity.getAssets(), getString(R.string.App_Font));
            BaseActivity.CustomTFSpan tfSpan = new BaseActivity.CustomTFSpan(tf);
            SpannableString spannableString = new SpannableString(getString(R.string.completed_job_title));
            spannableString.setSpan(tfSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            SpannableString spannableStrings = new SpannableString(getString(R.string.map_completed_job_label));
            spannableStrings.setSpan(tfSpan, 0, spannableStrings.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            SpannableString spannableStringss = new SpannableString(getString(R.string.ok));
            spannableStringss.setSpan(tfSpan, 0, spannableStringss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            SpannableString spannableStringsss = new SpannableString(getString(R.string.cancel));
            spannableStringsss.setSpan(tfSpan, 0, spannableStringsss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setTitle(spannableString);
            builder.setMessage(spannableStrings);
            builder.setPositiveButton(spannableStringss,
                    (dialog, which) -> {
                        dialog.dismiss();
                        callPaymentFinish_();
                    });
            builder.setNegativeButton(spannableStringsss,
                    (dialog, which) -> dialog.dismiss());
            builder.show();

        });

        btnExtendJobsInProgress.setOnClickListener(v -> {
            BaseActivity.callClickAction(btnExtendJobsInProgress, false);
            callExtendHoursAlertDialog();
        });

        ivBackCardPayment.setOnClickListener(view -> {
            BaseActivity.callClickAction(btnExtendJobsInProgress, true);
            setVisibility(lytCardPayment, false);
        });

    }

    private void callCancelJobAleryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);
        Typeface tf = Typeface.createFromAsset(activity.getAssets(), getString(R.string.App_Font));
        BaseActivity.CustomTFSpan tfSpan = new BaseActivity.CustomTFSpan(tf);
        SpannableString spannableString = new SpannableString(getString(R.string.map_cancel_job_title));
        spannableString.setSpan(tfSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableString spannableStrings = new SpannableString(getString(R.string.map_cancel_job_label));
        spannableStrings.setSpan(tfSpan, 0, spannableStrings.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableString spannableStringss = new SpannableString(getString(R.string.no));
        spannableStringss.setSpan(tfSpan, 0, spannableStringss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableString spannableStringsss = new SpannableString(getString(R.string.yes));
        spannableStringsss.setSpan(tfSpan, 0, spannableStringsss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setTitle(spannableString);
        builder.setMessage(spannableStrings);
        builder.setPositiveButton(spannableStringss,
                (dialog, which) -> dialog.dismiss());
        builder.setNegativeButton(spannableStringsss,
                (dialog, which) -> {
                    dialog.dismiss();
                    callCancelJobUserUpdate();
                });
        builder.show();
    }

    private void callClickAction(View view, Boolean b) {
        view.setEnabled(b);
        view.setFocusableInTouchMode(b);
        view.setFocusable(b);
        view.setClickable(b);
    }


    private void stripePayment_(String key) {

        showProgressDialog(activity);
        // Log.e("Stripe Key inside payment~~~ ",STRIPE_PUBLISHKEY);

        STRIPE_PUBLISHKEY = spCommon.getString(Utils.SP_COMMON_STRIPE_PUBLISH_KEY, null);
        STRIPE_SECRETKEY = spCommon.getString(Utils.SP_COMMON_STRIPE_SECRET_KEY, null);

        if (STRIPE_PUBLISHKEY == null) {
            if (Utils.BASE_CODE.equals("_Demo")) {
                //STRIPE_PUBLISHKEY = Utils.com_stripe_publishable_key_DEMO;
                STRIPE_PUBLISHKEY = Utils.com_stripe_publishable_key;
            } else if (Utils.BASE_CODE.equals("_Live")) {
                if (BuildConfig.DEBUG) {
                    //STRIPE_PUBLISHKEY = Utils.com_stripe_publishable_key_DEMO;
                    STRIPE_PUBLISHKEY = Utils.com_stripe_publishable_key;
                } else {
                    //STRIPE_PUBLISHKEY = Utils.com_stripe_publishable_key_LIVE;
                    STRIPE_PUBLISHKEY = Utils.com_stripe_publishable_key;
                }
            } else {
                //STRIPE_PUBLISHKEY = Utils.com_stripe_publishable_key_DEMO;
                STRIPE_PUBLISHKEY = Utils.com_stripe_publishable_key;
            }
            callAppSettings();
        }

        if (key.equals("CardPayment")) {
            cardToSave = creditCardModuleStripeCardPayment.getCardParams();
        } else {
            cardToSave = creditCardModuleStripe.getCardParams();
        }

        if (cardToSave == null) {
            hideProgressDialog();

            Toast.makeText(activity, R.string.invalid_card_details, Toast.LENGTH_SHORT).show();

            callClickAction(btnPayStripe, true);
            callClickAction(btnPayStripeCardPayment, true);

            return;
        } else {

            try {

                final Stripe stripe = new Stripe(activity, STRIPE_PUBLISHKEY);
                stripe.createCardToken(cardToSave,
                        null,
                        null,
                        new ApiResultCallback<Token>() {
                            @Override
                            public void onSuccess(@NonNull Token token) {
                                // TODO: Send Token information to your backend to initiate a charge

                                stripeTokenID = token.getId();

                                Log.e("TokenID==>", token.getId());

                                hideProgressDialog();

                                if (key.equals("CardPayment")) {

                                    try {
                                        callCardPaymentAmount(stripeTokenID, cardToSave.getLast4());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        callCardPaymentAmount(stripeTokenID, cardToSave.getName());
                                    }

                                    // callCardPaymentAmount(stripeTokenID, cardToSave.getNumber());
                                } else {

                                    try {
                                        callPaymentBalanceAmount(stripeTokenID, cardToSave.getLast4());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        callPaymentBalanceAmount(stripeTokenID, cardToSave.getName());
                                    }

                                    // callPaymentBalanceAmount(stripeTokenID, cardToSave.getNumber());
                                }
                            }

                            @Override
                            public void onError(@NonNull Exception error) {

                                if (BuildConfig.DEBUG)
                                    Log.d("Stripe error", Objects.requireNonNull(error.getLocalizedMessage()));

                                hideProgressDialog();

                                callClickAction(btnPayStripe, true);
                                callClickAction(btnPayStripeCardPayment, true);

                                Toast.makeText(activity, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                // Toast.makeText(requireActivity, R.string.error_stripe_try_again_later, Toast.LENGTH_LONG).show();

                            }
                        });

            } catch (Exception e) {
                e.printStackTrace();

                callClickAction(btnPayStripe, true);
                callClickAction(btnPayStripeCardPayment, true);

                Toast.makeText(activity, R.string.try_again, Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void callCardPaymentAmount(String stripeTokenID, String cardToSave) {

        Log.i(TAG, "callCardPaymentAmount: stripeTokenID-->" + stripeTokenID + "~~~" + cardToSave);

        userAccessToken = spCommon.getString(Utils.SP_USER_ACCESSTOKEN, null);
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("booking_id", strBookingId);
        map1.put("amount", strTotalAmountFB);
        map1.put("card_number", cardToSave);
        map1.put("strip_token", stripeTokenID);

        Log.i("callCardPaymentAmount", "~~~~" + map1);
        showProgressDialog(activity);

        Call<BalanceWalletAddMoneyModel> call2 = apiInterface.doUpdateCardPayment("Bearer " + userAccessToken, map1);
        call2.enqueue(new Callback<BalanceWalletAddMoneyModel>() {
            @Override
            public void onResponse(@NonNull Call<BalanceWalletAddMoneyModel> call, @NonNull Response<BalanceWalletAddMoneyModel> response) {
                Log.i(TAG, "onResponse: resonse code--> " + response.code());
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    hideProgressDialog();
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Money debited successfully.")) {

                        callClickAction(btnPayStripeCardPayment, true);

                        DatabaseReference refs = FirebaseDatabase.getInstance().getReference();
                        refs.child(Utils.FIREBASE_PROVIDERS_HEADER).child(strdriverIdFB).child("trips").child("card_status").setValue("1");
                        refs.child(Utils.FIREBASE_BOOKING_TRIPS_HEADER).child(strBookingId).child("card_status").setValue("1");
                        lytCardPayment.setVisibility(View.GONE);
                        txtWaitaMomentCardPayment.setVisibility(View.VISIBLE);

                    } else if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Credit Amount should be grater than 50Cents")) {

                        callClickAction(btnPayStripe, true);
                        Toast.makeText(activity, getResources().getString(R.string.please_recharge_valid_amount), Toast.LENGTH_SHORT).show();

                    } else if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Amount must be at least $0.50 usd")) {

                        callClickAction(btnPayStripe, true);
                        Toast.makeText(activity, getResources().getString(R.string.please_recharge_valid_amount), Toast.LENGTH_SHORT).show();

                    } else if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Amount must be at least $0.50 USD")) {

                        callClickAction(btnPayStripe, true);
                        Toast.makeText(activity, getResources().getString(R.string.please_recharge_valid_amount), Toast.LENGTH_SHORT).show();

                    } else {

                        callClickAction(btnPayStripeCardPayment, true);
                        Toast.makeText(activity, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onResponse: success");
                    }

                } else if (response.code() == RESPONSECODE_401) {
                    //  callRefreshTokenBalanceAmount(stripeTokenID, cardToSave);h
                    callClickAction(btnPayStripeCardPayment, true);
                    Toast.makeText(activity, "Response Error 401", Toast.LENGTH_SHORT).show();

                } else if (response.code() == RESPONSECODE_500) {
                    callClickAction(btnPayStripeCardPayment, true);
                    Toast.makeText(activity, "Response Error 500", Toast.LENGTH_SHORT).show();
                    hideProgressDialog();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BalanceWalletAddMoneyModel> call, @NonNull Throwable t) {
                call.cancel();
                callClickAction(btnPayStripeCardPayment, true);
                hideProgressDialog();
            }
        });
    }


    @SuppressLint("LongLogTag")
    private void callPaymentBalanceAmount(String stripeTokenID, String cardToSave) {

        Log.i(TAG, "callPaymentBalanceAmount: stripeTokenID-->" + stripeTokenID + "~~~" + cardToSave);
        userAccessToken = spCommon.getString(Utils.SP_USER_ACCESSTOKEN, null);
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("driver_id", strdriverIdFB);
        map1.put("customer_id", userId);
        map1.put("transaction_token", stripeTokenID);
        map1.put("booking_id", strBookingId);
        map1.put("balance_amount", strBalanceAmountFB);
        map1.put("total_amount", strTotalAmountFB);
        map1.put("card_number", cardToSave);
        Log.i("callPaymentBalanceAmount", "~~~~" + map1);
        showProgressDialog(activity);

        Call<BalanceWalletAddMoneyModel> call2 = apiInterface.doUpdateBalanceWalletAmount("Bearer " + userAccessToken, map1);
        call2.enqueue(new Callback<BalanceWalletAddMoneyModel>() {
            @Override
            public void onResponse(@NonNull Call<BalanceWalletAddMoneyModel> call, @NonNull Response<BalanceWalletAddMoneyModel> response) {
                Log.i(TAG, "onResponse: resonse code--> " + response.code());
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {

                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Request status updated successfully.")) {

                        callClickAction(btnPayStripe, true);

                        double roundedCustomerWalletBalance = DecimalUtils.round(Objects.requireNonNull(response.body()).getBalanceWalletAddMonetDataModel().getCustomerWalletBalance(), 2);
                        double roundedDriverWalletBalance = DecimalUtils.round(Objects.requireNonNull(response.body()).getBalanceWalletAddMonetDataModel().getDriverWalletBalance(), 2);
                        double roundedBalanceAmount = DecimalUtils.round(Double.parseDouble(Objects.requireNonNull(response.body()).getBalanceWalletAddMonetDataModel().getBalanceAmount()), 2);

                        Log.e(TAG, "onResponse: ~~~" + roundedCustomerWalletBalance + "~~" + roundedDriverWalletBalance + "~~" + roundedBalanceAmount);

                        hideProgressDialog();

                        if (dialogBalanceAmtStripePayment != null) {
                            if (dialogBalanceAmtStripePayment.isShowing()) {
                                dialogBalanceAmtStripePayment.dismiss();
                            }
                        }

                        DatabaseReference refs = FirebaseDatabase.getInstance().getReference();
                        refs.child(Utils.FIREBASE_USER_HEADER).child(userId).child("balance_amount").setValue(roundedBalanceAmount);

                        //paymentCompleteDialog();
                        if (dialogUpdateDriverRatingAfterTrip != null) {
                            if (!dialogUpdateDriverRatingAfterTrip.isShowing()) {
                                dialogUpdateDriverRatingAfterTrip.show();
                            }
                        }

                    } else if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Payment Not Done Check your Card Credentials")) {

                        callClickAction(btnPayStripe, true);
                        Toast.makeText(activity, R.string.please_enter_valid_details, Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onResponse: success");
                    } else if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Credit Amount should be grater than 50Cents")) {

                        callClickAction(btnPayStripe, true);
                        Toast.makeText(activity, getResources().getString(R.string.please_recharge_valid_amount), Toast.LENGTH_SHORT).show();

                    } else if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Amount must be at least $0.50 usd")) {

                        callClickAction(btnPayStripe, true);
                        Toast.makeText(activity, getResources().getString(R.string.please_recharge_valid_amount), Toast.LENGTH_SHORT).show();

                    } else if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Amount must be at least $0.50 USD")) {

                        callClickAction(btnPayStripe, true);
                        Toast.makeText(activity, getResources().getString(R.string.please_recharge_valid_amount), Toast.LENGTH_SHORT).show();

                    } else {

                        callClickAction(btnPayStripe, true);
                        Toast.makeText(activity, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();

                    }

                } else if (response.code() == RESPONSECODE_401) {

                    callClickAction(btnPayStripe, true);

                    Toast.makeText(activity, "Response Error 401", Toast.LENGTH_SHORT).show();

                } else if (response.code() == RESPONSECODE_500) {

                    callClickAction(btnPayStripe, true);

                } else if (response.code() == RESPONSECODE_500) {
                    Toast.makeText(activity, "Responde Error 500", Toast.LENGTH_SHORT).show();

                    callClickAction(btnPayStripe, true);

                    hideProgressDialog();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BalanceWalletAddMoneyModel> call, @NonNull Throwable t) {
                call.cancel();
                hideProgressDialog();
                callClickAction(btnPayStripe, true);
            }
        });
    }

    @SuppressLint("LongLogTag")
    private void callUpdateDriverRating(String from, String userId, String strBookingId,
                                        String strdriverIdFB, String ratings, String edtComments) {

        userAccessToken = spCommon.getString(Utils.SP_USER_ACCESSTOKEN, null);

        HashMap<String, Object> map = new HashMap<>();
        map.put("from_id", userId);   //userid
        map.put("job_id", strBookingId);
        map.put("to_id", strdriverIdFB);  //driverid
        map.put("rating", ratings);
        map.put("status", "0");
        map.put("feedback", edtComments);
        Log.e("callUpdateRating adapter", "~~~~" + map);

        Call<RatingDriverModel> call2 = apiInterface.doUpdateRatingUser("Bearer " + userAccessToken, Utils.POST_ACCEPT_CONTENT_TYPE, map);
        showProgressDialog(activity);
        call2.enqueue(new Callback<RatingDriverModel>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<RatingDriverModel> call, @NonNull Response<RatingDriverModel> response) {
                hideProgressDialog();
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    hideProgressDialog();
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Feedback received")) {
                        isRatingUpdated = true;
                        txtRating.setVisibility(View.GONE);

                        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
                        dbRef.child(Utils.FIREBASE_BOOKING_TRIPS_HEADER).child(strBookingId).child("drivers").child(strdriverIdFB).child("rating_status").setValue("1");

                        out.println("rating given feedback");
                        Toast.makeText(activity, R.string.updated, Toast.LENGTH_SHORT).show();

                        if (from.equals("normal")) {
                            cvRating.setVisibility(View.GONE);
                            if (strBookingId != null) {
                                DatabaseReference refs = FirebaseDatabase.getInstance().getReference();
                                refs.child(Utils.FIREBASE_BOOKING_TRIPS_HEADER).child(strBookingId).child("rating_status").setValue("1");
                            }
                        } else {

                            /*for (TripsFirebaseGuardListModel item : tripsFirebaseGuardListModels) {
                                if (item.getDriver_id().equals(strdriverIdFB)) {
                                    // Update the rating_status to '1'
                                    item.setRating_status("1");*/
                            out.println("rating given");

                            tripFirebaseGuardListRatingAdapter = new TripFirebaseGuardListRatingAdapter(
                                    activity, tripsFirebaseGuardListModels, strdriverIdFB);
                            LinearLayoutManager linearLayoutManager_ = new LinearLayoutManager(activity,
                                    LinearLayoutManager.VERTICAL, false);
                            rcvGuardDetailsListRatingTripSummary.setLayoutManager(linearLayoutManager_);
                            rcvGuardDetailsListRatingTripSummary.setAdapter(tripFirebaseGuardListRatingAdapter);
                            tripFirebaseGuardListRatingAdapter.notifyDataSetChanged();

                                   /* break; // Exit the loop once the item is found and updated
                                }
                            }*/


                        }

                    } else {
                        Toast.makeText(activity, R.string.try_again, Toast.LENGTH_SHORT).show();
                    }
                } else if (response.code() == RESPONSECODE_401) {

                }
            }

            @Override
            public void onFailure(@NonNull Call<RatingDriverModel> call, @NonNull Throwable t) {
                call.cancel();
            }
        });
    }

    @SuppressLint("LongLogTag")
    private void callUpdateDriverDialog() {

        userAccessToken = spCommon.getString(Utils.SP_USER_ACCESSTOKEN, null);
        String ratings = String.valueOf(rbUpdateDriverDialog.getRating());

        HashMap<String, Object> map = new HashMap<>();
        map.put("from_id", userId);   //userid
        map.put("job_id", strBookingId);
        map.put("to_id", strdriverIdFB);  //driverid
        map.put("rating", ratings);
        map.put("status", "0");
        map.put("feedback", edtCommentUpdateDriverDialog.getText().toString());
        Log.e("callUpdateRating adapter", "~~~~" + map);

        Call<RatingDriverModel> call2 = apiInterface.doUpdateRatingUser("Bearer " + userAccessToken, Utils.POST_ACCEPT_CONTENT_TYPE, map);
        showProgressDialog(activity);
        call2.enqueue(new Callback<RatingDriverModel>() {
            @Override
            public void onResponse(@NonNull Call<RatingDriverModel> call, @NonNull Response<RatingDriverModel> response) {
                hideProgressDialog();
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    hideProgressDialog();
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Feedback received")) {
                        Toast.makeText(activity, R.string.updated, Toast.LENGTH_SHORT).show();

                        hideProgressDialog();

                        isRatingUpdated = true;
                        txtRating.setVisibility(View.GONE);

                        // cvRating.setVisibility(View.GONE);

                        if (dialogUpdateDriverRatingAfterTrip.isShowing()) {
                            dialogUpdateDriverRatingAfterTrip.dismiss();
                        }

                       /* if(strBookingId!=null) {
                            DatabaseReference refs = FirebaseDatabase.getInstance().getReference();
                            refs.child(Utils.FIREBASE_BOOKING_TRIPS_HEADER).child(strBookingId).child("rating_status").setValue("1");
                        }*/

                       /* dialogUpdateDriverRatingAfterTrip.dismiss();
                        if(strBookingId!=null)
                            removeBookingTripsFirebase(strBookingId);
                        paymentCompleteDialog();*/

                    } else {
                        Toast.makeText(activity, R.string.try_again, Toast.LENGTH_SHORT).show();
                    }
                } else if (response.code() == RESPONSECODE_401) {

                }
            }

            @Override
            public void onFailure(@NonNull Call<RatingDriverModel> call, @NonNull Throwable t) {
                call.cancel();
            }
        });
    }

    @Override
    public void onCameraIdle() {
        LatLng center_point_Idle = mMap.getCameraPosition().target;
        Log.i(TAG, "onCameraIdle: " + center_point_Idle);
        Location locMapCenterLocation = new Location(LocationManager.GPS_PROVIDER);
        locMapCenterLocation.setLatitude(center_point_Idle.latitude);
        locMapCenterLocation.setLongitude(center_point_Idle.longitude);
        //startIntentService(locMapCenterLocation, "normal");
    }

    @Override
    public void onCameraMoveCanceled() {

    }

    @Override
    public void onCameraMove() {

    }

    @Override
    public void onCameraMoveStarted(int i) {

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mMap = googleMap;

        /*try {
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.map_style));
            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }*/

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        updateTraffic();

//        mMap.setMyLocationEnabled(true);
        mMap.setOnCameraIdleListener(this);
        mMap.setOnCameraMoveStartedListener(this);
        mMap.setOnCameraMoveListener(this);
        mMap.setOnCameraMoveCanceledListener(this);

        float minZoomLevel = 15.0f; // Example minimum zoom level
        float maxZoomLevel = 17.0f; // Example maximum zoom level
        mMap.setMinZoomPreference(minZoomLevel);
        mMap.setMaxZoomPreference(maxZoomLevel);

//        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);

    }

    private void updateTraffic() {
        if (!checkReady()) {
            return;
        }
        //  mMap.setTrafficEnabled(switchmTraffic.isChecked());
        if (switchmTraffic.isChecked()) {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }

    private boolean checkReady() {
        if (mMap == null) {
            Toast.makeText(this, R.string.map_not_ready, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /*Geocoder and Fusedlocation Start*/
    private BroadcastReceiver mLocationUpdated = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Location location = (Location) intent.getParcelableExtra(Utils.LBM_EVENT_LOCATION_UPDATE);

            callLocationOnCommon(location);

        }
    };

    public void fusedLocationCalled() {

        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Log.i(TAG, "fusedLocationCalled: permission called");
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    // Log.e(TAG, "onSuccess: mFusedLocationClient-->" + location);
                    if (location != null) {
                        callLocationOnCommon(location);
                    } else {
                        Log.i(TAG, "onSuccess: else oft the fused location else");
                        // Toast.makeText(MainActivity.this, "Fused", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void callLocationListener() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("MissingPermission~~~", "MissingPermission");
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //    int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //  return;
        } else {
            // Permission has already been granted
            Log.e("MissingPermission~~~", "Success");
            fusedLocationCalled();
            LocationManager locationMangaer = (LocationManager)
                    getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new MyLocationListener();
            Objects.requireNonNull(locationMangaer).requestLocationUpdates(LocationManager
                    .GPS_PROVIDER, 5000, 10, locationListener);
        }
    }

    private class MyLocationListener implements LocationListener {

        public void onLocationChanged(Location location) {
            callLocationOnCommon(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    private void callLocationOnCommon(Location location) {

        try {
            //mPositionContainer.setText("Lat: " + location.getLatitude() + " Lon: " + location.getLongitude());
            Log.i(TAG, "onReceive: Lat:" + location.getLatitude() + "  Lon:  " + location.getLongitude());
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            locFusedLocation = new Location(LocationManager.GPS_PROVIDER);
            locFusedLocation.setLatitude(location.getLatitude());
            locFusedLocation.setLongitude(location.getLongitude());

            if (beginMoveCamera) {
                Log.i(TAG, "onReceive: Lat: " + strPickupLatFB);
                Log.i(TAG, "onReceive: lng: " + strPickupLngFB);
                Log.i(TAG, "onReceive: current lat: " + location.getLatitude());
                Log.i(TAG, "onReceive: current lng: " + location.getLongitude());
                if (strPickupLatFB != null) {
                    moveCamera(Double.parseDouble(strPickupLatFB), Double.parseDouble(strPickupLngFB));
                } else {
                    moveCamera(location.getLatitude(), location.getLongitude());
                }
                beginMoveCamera = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: ----------------------->>>>>>>>>>>>");

        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED)
                requestLocationPermission();
            else {
                // start location updates
                FusedLocationSingleton.getInstance().startLocationUpdates();
                // register observer for location updates
                LocalBroadcastManager.getInstance(activity).registerReceiver(mLocationUpdated,
                        new IntentFilter(Utils.INTENT_FILTER_LOCATION_UPDATE));
            }
        } else {
            // start location updates
            FusedLocationSingleton.getInstance().startLocationUpdates();
            // register observer for location updates
            LocalBroadcastManager.getInstance(activity).registerReceiver(mLocationUpdated,
                    new IntentFilter(Utils.INTENT_FILTER_LOCATION_UPDATE));
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause: ------------------>>>>>>>>>>>>>>>>>>>>>>>");
        // stop location updates
        FusedLocationSingleton.getInstance().stopLocationUpdates();
        // unregister observer
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(mLocationUpdated);
    }

    /* Initiate Google API Client  */
    private void initGoogleAPIClient() {
        //Without Google API Client Auto Location Dialog will not work
        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .enableAutoManage(this, 0, this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();

    }

    /* Check Location Permission for Marshmallow Devices */
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED)
                requestLocationPermission();
            else {
                //  showSettingDialog();
                locationEnableDialogMessage();
            }
        } else {
            //   showSettingDialog();
            locationEnableDialogMessage();
        }

    }

    /*  Show Popup to access User Permission  */
    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainJobProgressActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(MainJobProgressActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_INTENT_ID);

        } else {
            ActivityCompat.requestPermissions(MainJobProgressActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_INTENT_ID);
        }
    }


    public void showGpsDailog() {
        String title = getString(R.string.mainactivity_gps_enable_dialog_title);
        String message = getString(R.string.mainactivity_gps_enable_dialog_message);
        String negativeButton = getString(R.string.mainactivity_gps_enable_dialog_negativebutton);
        String positiveButton = getString(R.string.mainactivity_gps_enable_dialog_positivebutton);

        myDialog = DialogUtils.createDialogMessage(this, title, message,
                negativeButton, null, false, new DialogUtils.DialogListener() {
                    @Override
                    public void onPositiveButton() {
                        myDialog.dismiss();
                        finish();
                        System.exit(0);
                    }

                    @Override
                    public void onNegativeButton() {
                        myDialog.dismiss();
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                });
        myDialog.setCancelable(false);
    }

    protected void locationEnableDialogMessage() {

        if (myDialog != null && !myDialog.isShowing()) {
            myDialog.show();
        }
    }

    private BroadcastReceiver mGpsSwitchStateReceiver = new BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
                // Make an action or refresh an already managed state.
                Log.i(TAG, "onReceive: mGpsSwitchStateReceiver");
                if (isGpsEnable()) {
                    // Toast.makeText(context, "GPS is Enable!", Toast.LENGTH_LONG).show();
                    if (myDialog != null && myDialog.isShowing()) {
                        myDialog.dismiss();
                    }
                } else {
                    if (myDialog != null && myDialog.isShowing()) {
                        myDialog.dismiss();
                    }
                    if (myDialog != null && !myDialog.isShowing()) {
                        myDialog.show();
                    }
                    //  Toast.makeText(context, "GPS is disable!", Toast.LENGTH_LONG).show();
                }

            }
        }
    };

    public Boolean isGpsEnable() {
        final LocationManager manager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return false;
        } else {
            return true;
        }
    }

    public void gpsState() {
        if (isGpsEnable()) {
            //  Toast.makeText(getApplicationContext(), "GPS is Enable!", Toast.LENGTH_LONG).show();
            if (myDialog != null && myDialog.isShowing()) {
                myDialog.dismiss();
            }
        } else {
            if (myDialog != null && myDialog.isShowing()) {
                myDialog.dismiss();
            }
            if (myDialog != null && !myDialog.isShowing()) {
                myDialog.show();
            }
            //  Toast.makeText(getApplicationContext(), "GPS is disable!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy: ------------------->>>>>>>>>>>>>>>>>>");
        super.onDestroy();
        try {

            SharedPreferences.Editor getState_ = getApplicationContext().getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE).edit();
            getState_.putInt("chat_click", 0);
            getState_.apply();

            if (runnable != null) handler.removeCallbacks(runnable);
            if (adViewFacebook != null) {
                adViewFacebook.destroy();
            }
            if (adViewGoogle != null) {
                adViewGoogle.destroy();
            }

            Utils.CURRENT_TAG = null;

            if (tripStatusDatabaseRef != null) {
                tripStatusDatabaseRef.removeEventListener(tripStatusValueEventListener);
            }

            unregisterReceiver(mGpsSwitchStateReceiver);

            /*if (firebaseRecyclerAdapter != null) {
                firebaseRecyclerAdapter.stopListening();
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "onConnected: called");
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED)
                requestLocationPermission();
            else {
                gpsState();
            }
        } else {
            gpsState();
        }
        // gpsState();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    String reqStatus = "0";

    @SuppressLint({"LongLogTag", "SetTextI18n"})
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        Object reqStatusObj = dataSnapshot.child(Utils.FIREBASE_SERVICE_STATUS).getValue();
        Log.i("Requeststatusresponse===>", String.valueOf(reqStatusObj));

        if (reqStatusObj != null) {
            reqStatus = String.valueOf(reqStatusObj);

            switch (reqStatus) {

                case "0":
                    //callSlideBannerImage("0");
                    break;
                case "1":
                    //callSlideBannerImage("1");
                    break;
                case "2":

                    spEditTripState.putString(Utils.SP_MAP_TRIPSTATE, "Accepttrip");
                    spEditTripState.commit();

                    //callSlideBannerImage("2");

                    callCommonDataSnapShot(dataSnapshot);

                    strAcceptTimeFB = Objects.requireNonNull(dataSnapshot.child("accept_time").getValue()).toString();

                    showGeneralDetails("2");

                    callGoogleDirection("2");

                    getDriverEstDistanceDuration(strdriverIdFB);

                    getDriverWalletAmount();

                    hideProgressDialog();

                    if (markers != null) {
                        Log.i(TAG, "onDataChange: in case 2 marker");
                        markers.clear();
                    }

                    break;
                case "3":

                    spEditTripState.putString(Utils.SP_MAP_TRIPSTATE, "ArriveTrips");
                    spEditTripState.commit();

                    //callSlideBannerImage("3");

                    callCommonDataSnapShot(dataSnapshot);

                    strAcceptTimeFB = Objects.requireNonNull(dataSnapshot.child("accept_time").getValue()).toString();
                    strStartTimeFB = Objects.requireNonNull(dataSnapshot.child("start_time").getValue()).toString();

                    showGeneralDetails("3");

                    if (markers != null) {
                        Log.i(TAG, "onDataChange: in case 3 marker");
                        markers.clear();
                    }

                    lytTripSummary.setVisibility(View.GONE);
                    //lytCardPayment.setVisibility(View.GONE);

                    callGoogleDirection("3");

                    //   dialogCaseAlert("3");

                    getDriverEstDistanceDuration(strdriverIdFB);

                    getDriverWalletAmount();

                    hideProgressDialog();

                    break;
                case "4":

                    spEditTripState.putString(Utils.SP_MAP_TRIPSTATE, "ArriveTrip");
                    spEditTripState.commit();

                    //callSlideBannerImage("4");

                    callCommonDataSnapShot(dataSnapshot);

                    strAcceptTimeFB = Objects.requireNonNull(dataSnapshot.child("accept_time").getValue()).toString();
                    strStartTimeFB = Objects.requireNonNull(dataSnapshot.child("start_time").getValue()).toString();
                    strArriveTimeFB = Objects.requireNonNull(dataSnapshot.child("reach_time").getValue()).toString();

                    showGeneralDetails("4");

                   /* final Dialog builder = new Dialog(requireActivity);
                    builder.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                    builder.setContentView(R.layout.dialog_case_alert);
                    Objects.requireNonNull(builder.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    builder.setCancelable(false);
                    builder.getWindow().setWindowAnimations(R.style.DialogAnimationLR);

                    Window window = builder.getWindow();
                    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    window.setGravity(Gravity.CENTER);

                    CustomTextView txtHeader = builder.findViewById(R.id.txt_header);
                    CustomTextView txtDescription = builder.findViewById(R.id.txt_description);
                    CustomTextView txtOk = builder.findViewById(R.id.txt_ok);
                    CustomTextView txtCancel = builder.findViewById(R.id.txt_cancel);

                    txtHeader.setText(getResources().getString(R.string.map_dialog_confirm));
                    txtDescription.setText(getResources().getString(R.string.map_dialog_driver_arrived_loc));

                    txtCancel.setVisibility(View.VISIBLE);
                    txtOk.setOnClickListener(v -> {
                        // here I want to dismiss it after SetBackground() method
                        DatabaseReference requestAcceptrDatabaseBRef;
                        requestAcceptrDatabaseBRef = FirebaseDatabase.getInstance().getReference().child("providers").child(strdriverIdFB).child(Utils.FIREBASE_TRIP_HEADER);
                        requestAcceptrDatabaseBRef.child("request_status").setValue("1");

                        DatabaseReference requestAcceptrDatabaseBRefs;
                        requestAcceptrDatabaseBRefs = FirebaseDatabase.getInstance().getReference().child("booking_trips").child(strBookingId);
                        requestAcceptrDatabaseBRefs.child("request_status").setValue("1");
                        builder.dismiss();
                    });

                    txtCancel.setOnClickListener(v -> {
                        // here I want to dismiss it after SetBackground() method
                        DatabaseReference requestAcceptrDatabaseBRef;
                        requestAcceptrDatabaseBRef = FirebaseDatabase.getInstance().getReference().child("providers").child(strdriverIdFB).child(Utils.FIREBASE_TRIP_HEADER);
                        requestAcceptrDatabaseBRef.child("request_status").setValue("2");

                        DatabaseReference requestAcceptrDatabaseBRefs;
                        requestAcceptrDatabaseBRefs = FirebaseDatabase.getInstance().getReference().child("booking_trips").child(strBookingId);
                        requestAcceptrDatabaseBRefs.child("request_status").setValue("2");
                        builder.dismiss();
                    });

                    try {
                        if (dataSnapshot.child("request_status").getValue() != null) {
                            String request_status = Objects.requireNonNull(dataSnapshot.child("request_status").getValue()).toString();
                            if (request_status.equals("0") || request_status.equals("3")) {
                                if (!builder.isShowing()) {
                                    builder.show();
                                } else {
                                    builder.dismiss();
                                }
                            }
                        } else {
                       *//* DatabaseReference requestAcceptrDatabaseBRefs;
                        requestAcceptrDatabaseBRefs = FirebaseDatabase.getInstance().getReference().child("booking_trips").child(strBookingId);
                        requestAcceptrDatabaseBRefs.child("request_status").setValue("0");*//*
                            Log.e(TAG, "onDataChange: Error ---->>> Request status --->>>");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/

                    if (markers != null) {
                        Log.i(TAG, "onDataChange: in case 4 marker");
                        markers.clear();
                    }

                    lytTripSummary.setVisibility(View.GONE);
                    //lytCardPayment.setVisibility(View.GONE);

                    callGoogleDirection("4");

                    getDriverEstDistanceDuration(strdriverIdFB);

                    getDriverWalletAmount();

                    hideProgressDialog();

                    break;
                case "5":
                    strPriceperAgentFB = Objects.requireNonNull(dataSnapshot.child("price_per_agent").getValue()).toString();
                    strPriceperDayFB = Objects.requireNonNull(dataSnapshot.child("price_per_day").getValue()).toString();
                    spEditTripState.putString(Utils.SP_MAP_TRIPSTATE, "Starttrip");
                    spEditTripState.commit();
                    if (dataSnapshot.child("total_agent").getValue() != null && !dataSnapshot.child("total_agent").getValue().toString().equals("")) {
                        strCount = Objects.requireNonNull(dataSnapshot.child("total_agent").getValue()).toString();
                        //  txtCount.setText(strCount);
                    } else {
                        strCount = "";

                    }
                    if (dataSnapshot.child("total_days").getValue() != null && !dataSnapshot.child("total_days").getValue().toString().equals("")) {
                        strDays = Objects.requireNonNull(dataSnapshot.child("total_days").getValue()).toString();
                        //   txtDays.setText(strDays);
                    } else {
                        strDays = "";
                    }
                    Double totalAgentAmount = Double.parseDouble(strCount) * Double.parseDouble(strPriceperAgentFB);
                    edtAgentCardPayment.setText(strCurrencySymbol + " " + convertCurrencyPrice(activity, totalAgentAmount.toString()));
                    Double totalDayAmount = Double.parseDouble(strCount) * Double.parseDouble(strDays) * Double.parseDouble(strPriceperDayFB);
                    edtAgentDayCardPayment.setText(strCurrencySymbol + " " + convertCurrencyPrice(activity, totalDayAmount.toString()));
                    //callSlideBannerImage("5");

                    callCommonDataSnapShot(dataSnapshot);

                    showGeneralDetails("5");

                    if (markers != null) {
                        Log.i(TAG, "onDataChange: in case 5 marker");
                        markers.clear();
                    }

                    //  dialogCaseAlert("5");

                    lytTripSummary.setVisibility(View.GONE);
                    //lytCardPayment.setVisibility(View.GONE);

                    callGoogleDirection("5");

                    getDriverWalletAmount();

                    getDriverEstDistanceDuration(strdriverIdFB);

                    hideProgressDialog();

                    break;

                case "5.1": //Card

                    spEditTripState.putString(Utils.SP_MAP_TRIPSTATE, "Starttrip");
                    spEditTripState.commit();

                    //callSlideBannerImage("5");

                    callCommonDataSnapShot(dataSnapshot);
                    strPriceperAgentFB = Objects.requireNonNull(dataSnapshot.child("price_per_agent").getValue()).toString();
                    strPriceperDayFB = Objects.requireNonNull(dataSnapshot.child("price_per_day").getValue()).toString();
                    spEditTripState.putString(Utils.SP_MAP_TRIPSTATE, "Starttrip");
                    spEditTripState.commit();

                    if (dataSnapshot.child("total_agent").getValue() != null && !dataSnapshot.child("total_agent").getValue().toString().equals("")) {
                        strCount = Objects.requireNonNull(dataSnapshot.child("total_agent").getValue()).toString();
                    } else {
                        strCount = "";

                    }
                    if (dataSnapshot.child("total_days").getValue() != null && !dataSnapshot.child("total_days").getValue().toString().equals("")) {
                        strDays = Objects.requireNonNull(dataSnapshot.child("total_days").getValue()).toString();
                    } else {
                        strDays = "";
                    }

                    Double totalAgentAmount2 = Double.parseDouble(strCount) * Double.parseDouble(strPriceperAgentFB);
                    edtAgentCardPayment.setText(strCurrencySymbol + " " + convertCurrencyPrice(activity, totalAgentAmount2.toString()));
                    Double totalDayAmount2 = Double.parseDouble(strCount) * Double.parseDouble(strDays) * Double.parseDouble(strPriceperDayFB);
                    edtAgentDayCardPayment.setText(strCurrencySymbol + " " + convertCurrencyPrice(activity, totalDayAmount2.toString()));

                    strAcceptTimeFB = Objects.requireNonNull(dataSnapshot.child("accept_time").getValue()).toString();
                    strStartTimeFB = Objects.requireNonNull(dataSnapshot.child("start_time").getValue()).toString();
                    strArriveTimeFB = Objects.requireNonNull(dataSnapshot.child("reach_time").getValue()).toString();
                    strBeginJobTimeFB = Objects.requireNonNull(dataSnapshot.child("service_start_time").getValue()).toString();

                    if (dataSnapshot.child("extra_charge").getValue() != null)
                        strMaterialChargeFB = Objects.requireNonNull(dataSnapshot.child("extra_charge").getValue()).toString();
                    else
                        strMaterialChargeFB = "0.0";

                    if (dataSnapshot.child("misc_charge").getValue() != null)
                        strMiscChargeFB = Objects.requireNonNull(dataSnapshot.child("misc_charge").getValue()).toString();
                    else
                        strMiscChargeFB = "0.0";

                    if (dataSnapshot.child("driver_discount").getValue() != null)
                        strDriverDiscountFB = Objects.requireNonNull(dataSnapshot.child("driver_discount").getValue()).toString();
                    else
                        strDriverDiscountFB = "0.0";

                    strTotalAmountFB = Objects.requireNonNull(dataSnapshot.child("total_amount").getValue()).toString();
                    strUnitPriceFB = Objects.requireNonNull(dataSnapshot.child("unit_price").getValue()).toString();

                    strTotalDistanceFB = Objects.requireNonNull(dataSnapshot.child("total_distance").getValue()).toString();
                    strPricePerKMFB = Objects.requireNonNull(dataSnapshot.child("price_per_km").getValue()).toString();
                    strVisitPriceFB = Objects.requireNonNull(dataSnapshot.child("visit_fare").getValue()).toString();
                    strMainUnitPriceFB = Objects.requireNonNull(dataSnapshot.child("level_amount").getValue()).toString();
                    strLevelTypeFB = Objects.requireNonNull(dataSnapshot.child("level_type").getValue()).toString();
                    strAmountTypeFB = Objects.requireNonNull(dataSnapshot.child("amount_type").getValue()).toString();
                    strSubLevelAmountFB = Objects.requireNonNull(dataSnapshot.child("sub_level_amount").getValue()).toString();

                    if (dataSnapshot.child("card_status").getValue() != null) {
                        strCardStatus = dataSnapshot.child("card_status").getValue().toString();
                    } else {
                        strCardStatus = "4";
                    }

                    if (dataSnapshot.child("promocode").getValue() != null) {
                        strPromoCodeFB = Objects.requireNonNull(dataSnapshot.child("promocode").getValue()).toString();
                        strPromoCodeValueFB = Objects.requireNonNull(dataSnapshot.child("promo_value").getValue()).toString();
                    } else {
                        strPromoCodeFB = "";
                    }

                    showGeneralDetails("5.1");

                    if (markers != null) {
                        Log.i(TAG, "onDataChange: in case 5 marker");
                        markers.clear();
                    }

                    showCardPayment("card");

                    // callGoogleDirection("5.1");

                    getDriverWalletAmount();

                    getDriverEstDistanceDuration(strdriverIdFB);

                    hideProgressDialog();

                    break;
                case "5.3": //PayPal

                    spEditTripState.putString(Utils.SP_MAP_TRIPSTATE, "Starttrip");
                    spEditTripState.commit();

                    //callSlideBannerImage("5");

                    callCommonDataSnapShot(dataSnapshot);

                    strPriceperAgentFB = Objects.requireNonNull(dataSnapshot.child("price_per_agent").getValue()).toString();
                    strPriceperDayFB = Objects.requireNonNull(dataSnapshot.child("price_per_day").getValue()).toString();
                    spEditTripState.putString(Utils.SP_MAP_TRIPSTATE, "Starttrip");
                    spEditTripState.commit();

                    if (dataSnapshot.child("total_agent").getValue() != null && !dataSnapshot.child("total_agent").getValue().toString().equals("")) {
                        strCount = Objects.requireNonNull(dataSnapshot.child("total_agent").getValue()).toString();
                    } else {
                        strCount = "";

                    }
                    if (dataSnapshot.child("total_days").getValue() != null && !dataSnapshot.child("total_days").getValue().toString().equals("")) {
                        strDays = Objects.requireNonNull(dataSnapshot.child("total_days").getValue()).toString();
                    } else {
                        strDays = "";
                    }

                    Double totalAgentAmount3 = Double.parseDouble(strCount) * Double.parseDouble(strPriceperAgentFB);
                    edtAgentCardPayment.setText(strCurrencySymbol + " " + convertCurrencyPrice(activity, totalAgentAmount3.toString()));
                    Double totalDayAmount3 = Double.parseDouble(strCount) * Double.parseDouble(strDays) * Double.parseDouble(strPriceperDayFB);
                    edtAgentDayCardPayment.setText(strCurrencySymbol + " " + convertCurrencyPrice(activity, totalDayAmount3.toString()));

                    strAcceptTimeFB = Objects.requireNonNull(dataSnapshot.child("accept_time").getValue()).toString();
                    strStartTimeFB = Objects.requireNonNull(dataSnapshot.child("start_time").getValue()).toString();
                    strArriveTimeFB = Objects.requireNonNull(dataSnapshot.child("reach_time").getValue()).toString();
                    strBeginJobTimeFB = Objects.requireNonNull(dataSnapshot.child("service_start_time").getValue()).toString();

                    if (dataSnapshot.child("extra_charge").getValue() != null)
                        strMaterialChargeFB = Objects.requireNonNull(dataSnapshot.child("extra_charge").getValue()).toString();
                    else
                        strMaterialChargeFB = "0.0";

                    if (dataSnapshot.child("misc_charge").getValue() != null)
                        strMiscChargeFB = Objects.requireNonNull(dataSnapshot.child("misc_charge").getValue()).toString();
                    else
                        strMiscChargeFB = "0.0";

                    if (dataSnapshot.child("driver_discount").getValue() != null)
                        strDriverDiscountFB = Objects.requireNonNull(dataSnapshot.child("driver_discount").getValue()).toString();
                    else
                        strDriverDiscountFB = "0.0";

                    strTotalAmountFB = Objects.requireNonNull(dataSnapshot.child("total_amount").getValue()).toString();
                    strUnitPriceFB = Objects.requireNonNull(dataSnapshot.child("unit_price").getValue()).toString();

                    strTotalDistanceFB = Objects.requireNonNull(dataSnapshot.child("total_distance").getValue()).toString();
                    strPricePerKMFB = Objects.requireNonNull(dataSnapshot.child("price_per_km").getValue()).toString();
                    strVisitPriceFB = Objects.requireNonNull(dataSnapshot.child("visit_fare").getValue()).toString();
                    strMainUnitPriceFB = Objects.requireNonNull(dataSnapshot.child("level_amount").getValue()).toString();
                    strLevelTypeFB = Objects.requireNonNull(dataSnapshot.child("level_type").getValue()).toString();
                    strAmountTypeFB = Objects.requireNonNull(dataSnapshot.child("amount_type").getValue()).toString();
                    strSubLevelAmountFB = Objects.requireNonNull(dataSnapshot.child("sub_level_amount").getValue()).toString();

                    if (dataSnapshot.child("paypal_status").getValue() != null) {
                        strPayPalStatus = dataSnapshot.child("paypal_status").getValue().toString();
                    } else {
                        strPayPalStatus = "4";
                    }

                    if (dataSnapshot.child("promocode").getValue() != null) {
                        strPromoCodeFB = Objects.requireNonNull(dataSnapshot.child("promocode").getValue()).toString();
                        strPromoCodeValueFB = Objects.requireNonNull(dataSnapshot.child("promo_value").getValue()).toString();
                    } else {
                        strPromoCodeFB = "";
                        strPromoCodeValueFB = "";
                    }

                    showGeneralDetails("5.3");

                    if (markers != null) {
                        Log.i(TAG, "onDataChange: in case 5 marker");
                        markers.clear();
                    }

                    showCardPayment("paypal");

                    // callGoogleDirection("5.3");

                    getDriverWalletAmount();

                    getDriverEstDistanceDuration(strdriverIdFB);

                    hideProgressDialog();

                    break;
                case "5.2": //CashPayment
                case "6":

                    if (lytCardPayment.getVisibility() == View.VISIBLE) {
                        lytCardPayment.setVisibility(View.GONE);
                    }

                    spEditTripState.putString(Utils.SP_MAP_TRIPSTATE, "Endtrip");
                    spEditTripState.commit();

                    //callSlideBannerImage("6");

                    callCommonDataSnapShot(dataSnapshot);

                    showGeneralDetails("6");

                    if (mMap != null) {
                        mMap.clear();
                    }

                    getDriverWalletAmount();

                    showTripSummary(Double.valueOf(strPricePerKMFB), Double.valueOf(strTotalDistanceFB));

                    /*if (dataSnapshot.child("rating_status").getValue() != null) {
                        String strRatingStatusFB = dataSnapshot.child("rating_status").getValue().toString();
                        Log.e(TAG, "strRatingStatusFB~~: " + strRatingStatusFB);
                        if (strRatingStatusFB != null) {
                            if (strRatingStatusFB.equalsIgnoreCase("0")) {
                                cvRating.setVisibility(View.VISIBLE);
                                rbUpdateDriverRating.setRating(0);
                                edtCommentUpdateDriverRating.setText("");
                            } else if (strRatingStatusFB.equalsIgnoreCase("1")) {
                                cvRating.setVisibility(View.GONE);
                                rbUpdateDriverRating.setRating(0);
                                edtCommentUpdateDriverRating.setText("");
                            } else {
                                cvRating.setVisibility(View.VISIBLE);
                                rbUpdateDriverRating.setRating(0);
                                edtCommentUpdateDriverRating.setText("");
                            }
                        } else
                            cvRating.setVisibility(View.GONE);
                    } else {
                        DatabaseReference refs = FirebaseDatabase.getInstance().getReference();
                        refs.child("booking_trips").child(strBookingId).child("rating_status").setValue("0");
                    }*/

                    if (dataSnapshot.child("cash_status").getValue() != null) {
                        strCashStatusFB = Objects.requireNonNull(dataSnapshot.child("cash_status").getValue()).toString();
                    } else {
                        strCashStatusFB = "0";
                    }

                    btnMarkAsCompletedJobTripSummary.setVisibility(View.VISIBLE);
                    BaseActivity.callClickAction(btnMarkAsCompletedJobTripSummary, true);

                    txtCashStatusTripSummary.setVisibility(View.VISIBLE);
                    txtCashStatusTripSummary.setBackgroundColor(getResources().getColor(R.color.holo_red_dark));
                    txtCashStatusTripSummary.setText(getResources().getString(R.string.please_wait_customer_accept_your_completion));
                    callGoToHomeButtonAction(false);


                    /*if (strPaymentTypeFB != null) {
                        if (!strPaymentTypeFB.equals("null")) {
                            if (!strPaymentTypeFB.equals("")) {

                                if (strPaymentTypeFB.equals("cash")) {
                                    switch (strCashStatusFB) {
                                        case "0":
                                            txtCashStatusTripSummary.setVisibility(View.VISIBLE);
                                            txtCashStatusTripSummary.setBackgroundColor(getResources().getColor(R.color.black));
                                            txtCashStatusTripSummary.setText(getResources().getString(R.string.trip_summary_please_wait_provider_confirm_to_cash_payment));
                                            // lytCashPayment.setVisibility(View.VISIBLE);

                                            callClickAction(txtCashStatusTripSummary, false);

                                            callGoToHomeButtonAction(false);

                                            break;
                                        case "1":
                                            txtCashStatusTripSummary.setVisibility(View.VISIBLE);
                                            txtCashStatusTripSummary.setBackgroundColor(getResources().getColor(R.color.holo_green_dark));
                                            txtCashStatusTripSummary.setText(getResources().getString(R.string.trip_summary_cash_payment_received));
                                            //  lytCashPayment.setVisibility(View.GONE);

                                            callClickAction(txtCashStatusTripSummary, false);

                                            callGoToHomeButtonAction(true);

                                            break;
                                        case "2":
                                            txtCashStatusTripSummary.setVisibility(View.VISIBLE);
                                            txtCashStatusTripSummary.setBackgroundColor(getResources().getColor(R.color.holo_red_dark));
                                            txtCashStatusTripSummary.setText(getResources().getString(R.string.trip_summary_please_collect_cash_payment_from_user_side));
                                            //  lytCashPayment.setVisibility(View.GONE);

                                            callClickAction(txtCashStatusTripSummary, true);

                                            callGoToHomeButtonAction(false);

                                            txtCashStatusTripSummary.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    DatabaseReference refs = FirebaseDatabase.getInstance().getReference();
                                                    refs.child("booking_trips").child(strBookingId).child("cash_status").setValue("0");
                                                    refs.child("providers").child(strdriverIdFB).child("trips").child("cash_status").setValue("0");
                                                }
                                            });

                                            break;
                                    }
                                } else {

                                    callGoToHomeButtonAction(true);
                                    //lytCashPayment.setVisibility(View.GONE);
                                    txtCashStatusTripSummary.setVisibility(View.GONE);
                                }


                            } else {
                                callGoToHomeButtonAction(true);
                                //lytCashPayment.setVisibility(View.GONE);
                                txtCashStatusTripSummary.setVisibility(View.GONE);
                            }
                        } else {
                            callGoToHomeButtonAction(true);
                            // lytCashPayment.setVisibility(View.GONE);
                            txtCashStatusTripSummary.setVisibility(View.GONE);
                        }
                    } else {
                        callGoToHomeButtonAction(true);
                        // lytCashPayment.setVisibility(View.GONE);
                        txtCashStatusTripSummary.setVisibility(View.GONE);
                    }*/

                    Double totalAgentAmount1 = Double.parseDouble(strCount) * Double.parseDouble(strPriceperAgentFB);
                    txtAgentCharge.setText(convertCurrencyPrice(activity, totalAgentAmount1.toString()));

                           /* strBasePriceFB = Objects.requireNonNull(dataSnapshot.child("base_fare").getValue()).toString();
                            txtBaseCharge.setText(convertCurrencyPrice(requireActivity,strBasePriceFB));*/
                    Double totalDayAmount1 = Double.parseDouble(strCount) * Double.parseDouble(strDays) * Double.parseDouble(strPriceperDayFB);

                    txtDayCharge.setText(convertCurrencyPrice(activity, totalDayAmount1.toString()));
                    txtAgentFeeLabel.setText("(" + strCount + " X" + strCurrencySymbol + " " + convertCurrencyPrice(activity, strPriceperAgentFB) + ") Guard Count X Price per Guard");
                    txtDayFeeLabel.setText("(" + strCount + " * " + strDays + "*" + strCurrencySymbol + " " + convertCurrencyPrice(activity, strPriceperDayFB) + ") Guard Count X Number of Days X Price per Day");

                    /*End Trip*/
                    hideProgressDialog();
                    break;
                case "7":
                    spEditTripState.putString(Utils.SP_MAP_TRIPSTATE, "Normalstate");
                    spEditTripState.commit();

                    if (lytCardPayment.getVisibility() == View.VISIBLE) {
                        lytCardPayment.setVisibility(View.GONE);
                    }

                    spEditTripState.putString(Utils.SP_MAP_TRIPSTATE, "Endtrip");
                    spEditTripState.commit();

                    //callSlideBannerImage("6");

                    out.println("status7Listened");

                    callCommonDataSnapShot(dataSnapshot);

                    showGeneralDetails("6");

                    if (mMap != null) {
                        mMap.clear();
                    }

                    getDriverWalletAmount();

                    showTripSummary(Double.valueOf(strPricePerKMFB), Double.valueOf(strTotalDistanceFB));

                    /*if (dataSnapshot.child("rating_status").getValue() != null) {
                        String strRatingStatusFB = dataSnapshot.child("rating_status").getValue().toString();
                        Log.e(TAG, "strRatingStatusFB~~: " + strRatingStatusFB);
                        if (strRatingStatusFB != null) {
                            if (strRatingStatusFB.equalsIgnoreCase("0")) {
                                cvRating.setVisibility(View.VISIBLE);
                                rbUpdateDriverRating.setRating(0);
                                edtCommentUpdateDriverRating.setText("");
                            } else if (strRatingStatusFB.equalsIgnoreCase("1")) {
                                cvRating.setVisibility(View.GONE);
                                rbUpdateDriverRating.setRating(0);
                                edtCommentUpdateDriverRating.setText("");
                            } else {
                                cvRating.setVisibility(View.VISIBLE);
                                rbUpdateDriverRating.setRating(0);
                                edtCommentUpdateDriverRating.setText("");
                            }
                        } else
                            cvRating.setVisibility(View.GONE);
                    } else {
                        DatabaseReference refs = FirebaseDatabase.getInstance().getReference();
                        refs.child("booking_trips").child(strBookingId).child("rating_status").setValue("0");
                    }*/

                    if (dataSnapshot.child("cash_status").getValue() != null) {
                        strCashStatusFB = Objects.requireNonNull(dataSnapshot.child("cash_status").getValue()).toString();
                    } else {
                        strCashStatusFB = "0";
                    }

                    callGoToHomeButtonAction(true);
                    txtCashStatusTripSummary.setVisibility(View.GONE);

                    btnMarkAsCompletedJobTripSummary.setVisibility(View.GONE);
                    BaseActivity.callClickAction(btnMarkAsCompletedJobTripSummary, false);

                    /*End Trip*/
                    hideProgressDialog();
                    break;
                case "8":
                    getState.putString(Utils.SP_USER_CURRENT_BOOKING_ID, null).apply();
                    spEditTripState.putString(Utils.SP_MAP_TRIPSTATE, "Normalstate");
                    spEditTripState.commit();
                    /*User Cancel*/
                    Toast.makeText(this, R.string.cancel_job, Toast.LENGTH_SHORT).show();
                    hideProgressDialog();
                    removeRequestDetailsFirebase();
                    hideProgressDialog();

                    strBookingId = checkStringEmpty(dataSnapshot, "booking_id");
                    if (!strBookingId.equals("")) {
                        removeBookingTripsFirebase(strBookingId);
                    }

                    Intent mainActivityIntent = new Intent(activity, MainActivity.class);
                    mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainActivityIntent);
                    finish();
                    break;
                case "9":

                    getState.putString(Utils.SP_USER_CURRENT_BOOKING_ID, null).apply();
                    spEditTripState.putString(Utils.SP_MAP_TRIPSTATE, "Normalstate");
                    spEditTripState.commit();
                    /*Driver Cancel*/

                    Toast.makeText(this, R.string.map_cancel_provider_job, Toast.LENGTH_SHORT).show();

                    removeRequestDetailsFirebase();
                    hideProgressDialog();

                    strBookingId = checkStringEmpty(dataSnapshot, "booking_id");
                    if (!strBookingId.equals("")) {
                        removeBookingTripsFirebase(strBookingId);
                    }

                    Intent mainActivityIntents = new Intent(activity, MainActivity.class);
                    mainActivityIntents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mainActivityIntents.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainActivityIntents);
                    finish();

                    break;
                case "10":

                    if (lytCardPayment.getVisibility() == View.VISIBLE) {
                        lytCardPayment.setVisibility(View.GONE);
                    }

                    spEditTripState.putString(Utils.SP_MAP_TRIPSTATE, "Normalstate");
                    spEditTripState.commit();

                    //callSlideBannerImage("7");
                    /*User Balanace Amount state*/
                    callCommonDataSnapShot(dataSnapshot);
                    strPriceperAgentFB = Objects.requireNonNull(dataSnapshot.child("price_per_agent").getValue()).toString();
                    strPriceperDayFB = Objects.requireNonNull(dataSnapshot.child("price_per_day").getValue()).toString();
                    strAcceptTimeFB = Objects.requireNonNull(dataSnapshot.child("accept_time").getValue()).toString();
                    strStartTimeFB = Objects.requireNonNull(dataSnapshot.child("start_time").getValue()).toString();
                    strArriveTimeFB = Objects.requireNonNull(dataSnapshot.child("reach_time").getValue()).toString();
                    strBeginJobTimeFB = Objects.requireNonNull(dataSnapshot.child("service_start_time").getValue()).toString();

                    strMaterialChargeFB = Objects.requireNonNull(dataSnapshot.child("extra_charge").getValue()).toString();
                    strMiscChargeFB = Objects.requireNonNull(dataSnapshot.child("misc_charge").getValue()).toString();
                    strDriverDiscountFB = Objects.requireNonNull(dataSnapshot.child("driver_discount").getValue()).toString();

                    strTotalDistanceFB = Objects.requireNonNull(dataSnapshot.child("total_distance").getValue()).toString();
                    strPricePerKMFB = Objects.requireNonNull(dataSnapshot.child("price_per_km").getValue()).toString();
                    strVisitPriceFB = Objects.requireNonNull(dataSnapshot.child("visit_fare").getValue()).toString();
                    strMainUnitPriceFB = Objects.requireNonNull(dataSnapshot.child("level_amount").getValue()).toString();
                    strLevelTypeFB = Objects.requireNonNull(dataSnapshot.child("level_type").getValue()).toString();
                    strAmountTypeFB = Objects.requireNonNull(dataSnapshot.child("amount_type").getValue()).toString();
                    strSubLevelAmountFB = Objects.requireNonNull(dataSnapshot.child("sub_level_amount").getValue()).toString();

                    if (dataSnapshot.child("promocode").getValue() != null) {
                        strPromoCodeFB = Objects.requireNonNull(dataSnapshot.child("promocode").getValue()).toString();
                        strPromoCodeValueFB = Objects.requireNonNull(dataSnapshot.child("promo_value").getValue()).toString();
                    } else {
                        strPromoCodeFB = "";
                    }

                    //strBalanceAmountFB = Objects.requireNonNull(dataSnapshot.child("balance_amount").getValue()).toString();

                    if (dataSnapshot.child("balance_amount").getValue() != null) {
                        strBalanceAmountFB = Objects.requireNonNull(dataSnapshot.child("balance_amount").getValue()).toString();
                    } else {
                        strBalanceAmountFB = "0";
                    }

                    showGeneralDetails("10");

                    showTripSummary(Double.parseDouble(strPricePerKMFB), Double.parseDouble(strTotalDistanceFB));

                    showBalanceStripePayment();

                    dialogBalanceAmtStripePayment.show();

                    Double totalAgentAmountx = Double.parseDouble(strCount) * Double.parseDouble(strPriceperAgentFB);
                    txtBalanceAgentCharge.setText(strCurrencySymbol + " " + convertCurrencyPrice(activity, totalAgentAmountx.toString()));
                    Double totalDayAmountx = Double.parseDouble(strCount) * Double.parseDouble(strDays) * Double.parseDouble(strPriceperDayFB);
                    txtBalanceDayCharge.setText(strCurrencySymbol + " " + convertCurrencyPrice(activity, totalDayAmountx.toString()));
                    hideProgressDialog();

                    break;
                default:
                    spEditTripState.putString(Utils.SP_MAP_TRIPSTATE, "Normalstate");
                    spEditTripState.commit();
                    getState.putString(Utils.SP_USER_CURRENT_BOOKING_ID, null).apply();
                    hideProgressDialog();
                    break;
            }

        } else {
            hideProgressDialog();
            getState.putString(Utils.SP_USER_CURRENT_BOOKING_ID, null).apply();
        }

    }

    @SuppressLint("SetTextI18n")
    private void callCommonDataSnapShot(DataSnapshot dataSnapshot) {

        strBookingId = Objects.requireNonNull(dataSnapshot.child("booking_id").getValue()).toString();

        getState.putString(Utils.SP_USER_CURRENT_BOOKING_ID, strBookingId).apply();

        strTotalAmountFB = checkStringEmptyZero(dataSnapshot, "total_amount");

        if (dataSnapshot.child("customer_email").getValue() != null) {
            strCustomerEmailFB = Objects.requireNonNull(dataSnapshot.child("customer_email").getValue()).toString();
        } else {
            strCustomerEmailFB = "";
        }

        if (dataSnapshot.child("cancel_service_status").getValue() != null) {
            strCancelServiceStatusFB = Objects.requireNonNull(dataSnapshot.child("cancel_service_status").getValue()).toString();
        } else {
            strCancelServiceStatusFB = "";
        }

        if (dataSnapshot.child("category").getValue() != null) {
            strMainCategoryid = Objects.requireNonNull(dataSnapshot.child("category").getValue()).toString();
        } else {
            strMainCategoryid = "";
        }

        if (dataSnapshot.child("category_ar").getValue() != null) {
            strMainCategoryidAr = Objects.requireNonNull(dataSnapshot.child("category_ar").getValue()).toString();
        } else {
            strMainCategoryidAr = strMainCategoryid;
        }

        if (dataSnapshot.child("service_id").getValue() != null) {
            strSubCategoryid = Objects.requireNonNull(dataSnapshot.child("service_id").getValue()).toString();
        } else {
            strSubCategoryid = "";
        }

        if (dataSnapshot.child("created_at").getValue() != null) {
            strCreatedIdFB = Objects.requireNonNull(dataSnapshot.child("created_at").getValue()).toString();
        } else {
            strCreatedIdFB = "";
        }

        if (dataSnapshot.child("updated_at").getValue() != null) {
            strUpdatedFB = Objects.requireNonNull(dataSnapshot.child("updated_at").getValue()).toString();
        } else {
            strUpdatedFB = "";
        }

        struserIdFB = Objects.requireNonNull(dataSnapshot.child("customer_id").getValue()).toString();
        struserFnameFB = Objects.requireNonNull(dataSnapshot.child("customer_first_name").getValue()).toString();
        struserLnameFB = Objects.requireNonNull(dataSnapshot.child("customer_last_name").getValue()).toString();
        struserMobileFB = Objects.requireNonNull(dataSnapshot.child("customer_phone_number").getValue()).toString();
        struserEmailFB = Objects.requireNonNull(dataSnapshot.child("customer_email").getValue()).toString();
        strPickupLocationFB = Objects.requireNonNull(dataSnapshot.child("customer_location").getValue()).toString();
        strPickupLatFB = Objects.requireNonNull(dataSnapshot.child("customer_lat").getValue()).toString();
        strPickupLngFB = Objects.requireNonNull(dataSnapshot.child("customer_lon").getValue()).toString();

        strdriverIdFB = Objects.requireNonNull(dataSnapshot.child("driver_id").getValue()).toString();
        strdriverEmailFB = Objects.requireNonNull(dataSnapshot.child("driver_email").getValue()).toString();
        strdriverFnameFB = Objects.requireNonNull(dataSnapshot.child("driver_first_name").getValue()).toString();
        strdriverLnameFB = Objects.requireNonNull(dataSnapshot.child("driver_last_name").getValue()).toString();
        strdriverMobileFB = Objects.requireNonNull(dataSnapshot.child("driver_phone_number").getValue()).toString();

        if (dataSnapshot.child("job_number").getValue() != null) {
            strJobNumberFB = Objects.requireNonNull(dataSnapshot.child("job_number").getValue()).toString();
        } else {
            strJobNumberFB = "";
        }

        if (dataSnapshot.child("job_date").getValue() != null) {
            strJobDateFB = Objects.requireNonNull(dataSnapshot.child("job_date").getValue()).toString();
        } else {
            strJobDateFB = "";
        }

        if (dataSnapshot.child("mode").getValue() != null) {
            strModeFB = Objects.requireNonNull(dataSnapshot.child("mode").getValue()).toString();
        } else {
            strModeFB = "";
        }

        if (dataSnapshot.child("payment_type").getValue() != null) {
            strPaymentTypeFB = Objects.requireNonNull(dataSnapshot.child("payment_type").getValue()).toString();
        } else {
            strPaymentTypeFB = "";
        }

        if (dataSnapshot.child("site_commission").getValue() != null) {
            strSiteCommissionFB = Objects.requireNonNull(dataSnapshot.child("site_commission").getValue()).toString();
        } else {
            strSiteCommissionFB = "";
        }

        if (dataSnapshot.child("strip_id").getValue() != null) {
            strStripIdFB = Objects.requireNonNull(dataSnapshot.child("strip_id").getValue()).toString();
        } else {
            strStripIdFB = "";
        }

        if (dataSnapshot.child("driver_avatar").getValue() != null) {
            strDriverProfPicFB = Objects.requireNonNull(dataSnapshot.child("driver_avatar").getValue()).toString();
        } else {
            strDriverProfPicFB = "";
        }

        if (dataSnapshot.child("customer_avatar").getValue() != null) {
            strUserProfPicFB = Objects.requireNonNull(dataSnapshot.child("customer_avatar").getValue()).toString();
        } else {
            strUserProfPicFB = "";
        }

        if (dataSnapshot.child("drop_location").getValue() != null) {
            strDropLocationFB = Objects.requireNonNull(dataSnapshot.child("drop_location").getValue()).toString();
        } else {
            strDropLocationFB = "";
        }


        AcceptDropLatLng = new LatLng(Double.parseDouble(Objects.requireNonNull(dataSnapshot.child("drop_lat").getValue()).toString()),
                Double.parseDouble(Objects.requireNonNull(dataSnapshot.child("drop_lon").getValue()).toString()));

        strUnitPriceFB = checkStringEmptyZero(dataSnapshot, "unit_price");
        strBalanceAmountFB = checkStringEmptyZero(dataSnapshot, "balance_amount");

        strDays = checkStringEmpty(dataSnapshot, "total_days");
        strCount = checkStringEmpty(dataSnapshot, "total_agent");

        if (dataSnapshot.child("company_name").getValue() != null) {
            strCompanyName = Objects.requireNonNull(dataSnapshot.child("company_name").getValue()).toString();
        } else {
            strCompanyName = "Individual";
        }

        strPayPalOrderIdFB = checkStringEmpty(dataSnapshot, "paypal_order_id");

        strAcceptTimeFB = checkStringEmpty(dataSnapshot, "accept_time");
        strStartTimeFB = checkStringEmpty(dataSnapshot, "start_time");
        strArriveTimeFB = checkStringEmpty(dataSnapshot, "reach_time");
        strBeginJobTimeFB = checkStringEmpty(dataSnapshot, "service_start_time");


        strMaterialChargeFB = checkStringEmptyZero(dataSnapshot, "extra_charge");
        strMiscChargeFB = checkStringEmptyZero(dataSnapshot, "misc_charge");
        strDriverDiscountFB = checkStringEmptyZero(dataSnapshot, "driver_discount");

        strTotalDistanceFB = checkStringEmptyZero(dataSnapshot, "total_distance");
        strPricePerKMFB = checkStringEmptyZero(dataSnapshot, "price_per_km");
        strVisitPriceFB = checkStringEmptyZero(dataSnapshot, "visit_fare");
        strMainUnitPriceFB = checkStringEmptyZero(dataSnapshot, "level_amount");
        strLevelTypeFB = checkStringEmptyZero(dataSnapshot, "level_type");
        strAmountTypeFB = checkStringEmptyZero(dataSnapshot, "amount_type");
        strSubLevelAmountFB = checkStringEmptyZero(dataSnapshot, "sub_level_amount");
        strPriceperAgentFB = checkStringEmptyZero(dataSnapshot, "price_per_agent");
        strPriceperDayFB = checkStringEmptyZero(dataSnapshot, "price_per_day");

        strTotalHrsFB = checkStringEmptyZero(dataSnapshot, "total_hrs");
        stPricePerHrsFB = checkStringEmptyZero(dataSnapshot, "price_per_hr");
        strVatAmountsFB = checkStringEmptyZero(dataSnapshot, "vat_amount");

        strExtendedHrsFB = checkStringEmptyZero(dataSnapshot, "extended_hrs");
        strExtendedAmountFB = checkStringEmptyZero(dataSnapshot, "extended_amount");
        strExtendedVatFB = checkStringEmptyZero(dataSnapshot, "extended_vat");
        strExtendedStatusFB = checkStringEmptyZero(dataSnapshot, "extended_status");

        strGroupDriverIdFB = checkStringEmpty(dataSnapshot, "group_driver_id");

        strSubscriptionStatusFB = checkStringEmptyZero(dataSnapshot, "subscription_status");


        if (dataSnapshot.child("trip_type").getValue() != null) {
            strTripTypeFB = Objects.requireNonNull(dataSnapshot.child("trip_type").getValue()).toString();
        } else {
            strTripTypeFB = "1";
        }

        if (dataSnapshot.child("promocode").getValue() != null) {
            strPromoCodeFB = Objects.requireNonNull(dataSnapshot.child("promocode").getValue()).toString();
            strPromoCodeValueFB = Objects.requireNonNull(dataSnapshot.child("promo_value").getValue()).toString();
        } else {
            strPromoCodeFB = "";
        }


        if (dataSnapshot.child("drivers").getValue() != null) {
            try {
                /*Query query = dataSnapshot.child("drivers").getRef();
                if (BuildConfig.DEBUG) {
                    out.println("onDataChange: query-------drivers-------------->>>" + query);
                }
                query.addChildEventListener(new ChildEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                        Log.e(TAG, "onChildAdded: --->>>" + dataSnapshot.getKey() + ">--->" + dataSnapshot.getChildrenCount());
                        Log.e(TAG, "onChildAdded: --->>>" + dataSnapshot.getChildrenCount());

                        if (Double.parseDouble(String.valueOf(dataSnapshot.getChildrenCount())) > 0.0) {
                            if (BuildConfig.DEBUG) {
                                out.println("~~~~*****************addChildEventListener********~~~~~~~");
                            }
                            if (rcvGuardDetailsList.getVisibility() == View.GONE) {
                                rcvGuardDetailsList.setVisibility(View.VISIBLE);
                            }
                        } else {
                            if (rcvGuardDetailsList.getVisibility() == View.VISIBLE) {
                                rcvGuardDetailsList.setVisibility(View.GONE);
                            }
                        }
                        firebaseRecyclerAdapter.notifyDataSetChanged();
                    }

                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                        Log.e(TAG, "onChildChanged: --->>>" + dataSnapshot.getChildrenCount() + " " + s + " --- " + dataSnapshot.getKey());
                        firebaseRecyclerAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        Log.e(TAG, "onChildRemoved: --->>>" + dataSnapshot.getChildrenCount() + " ");
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {
                        //Log.e(TAG, "onChildMoved: --->>>"+dataSnapshot.getChildrenCount()+" "+s);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e(TAG, "onCancelled: -child(request).-->>>" + databaseError);
                    }
                });*/

                //viewTimelineFBStatus(query);
                viewTimelineFBStatus(dataSnapshot);

            } catch (Exception e) {
                e.printStackTrace();
                out.println("~~~~drivers~~~" + e);
            }
        } else {
            setVisibility(rcvGuardDetailsList, false);
        }


    }


    @SuppressLint("SetTextI18n")
    private void showGeneralDetails(String key) {

        if (!strDropLocationFB.equals("NoDrop")) {
            txtEstDistDurationJobinprogress.setVisibility(View.VISIBLE);
            txtPickupJobinprogress.setText(getString(R.string.map_job_inprogress_drop_location) + " " + strDropLocationFB);
        } else {
            txtEstDistDurationJobinprogress.setVisibility(View.GONE);
            txtPickupJobinprogress.setText(strPickupLocationFB);
        }

        switch (key) {
            case "2":

                btnCancelJobsInProgress.setVisibility(View.VISIBLE);

                llTripviewone.setVisibility(View.VISIBLE);
                llTripviewtwo.setVisibility(View.GONE);
                llTripviewthree.setVisibility(View.GONE);
                llTripviewfour.setVisibility(View.GONE);

                lytTripSummary.setVisibility(View.GONE);
                lytCardPayment.setVisibility(View.GONE);

                if (strLanguageShortName.equals("en")) {
                    txtAcceptone.setText(strMainCategoryid + " " + getString(R.string.map_status_count_driver_accepted_the_request));
                } else {
                    txtAcceptone.setText(strMainCategoryidAr + " " + getString(R.string.map_status_count_driver_accepted_the_request));
                }

                txtAccepttimeone.setText(strAcceptTimeFB);
                txtAccepttimetwo.setText("");
                txtAccepttimethree.setText("");
                txtAccepttimefour.setText("");

                break;
            case "3":

                btnCancelJobsInProgress.setVisibility(View.VISIBLE);

                llTripviewone.setVisibility(View.VISIBLE);
                llTripviewtwo.setVisibility(View.VISIBLE);
                llTripviewthree.setVisibility(View.GONE);
                llTripviewfour.setVisibility(View.GONE);

                if (strLanguageShortName.equals("en")) {
                    txtAcceptone.setText(strMainCategoryid + " " + getString(R.string.map_status_count_driver_accepted_the_request));
                    txtAccepttwo.setText(strMainCategoryid + " " + getString(R.string.map_status_count_Driver_has_started_the_trip_to_your_location));

                } else {
                    txtAcceptone.setText(strMainCategoryidAr + " " + getString(R.string.map_status_count_driver_accepted_the_request));
                    txtAccepttwo.setText(strMainCategoryidAr + " " + getString(R.string.map_status_count_Driver_has_started_the_trip_to_your_location));
                }

                txtAccepttimeone.setText(strAcceptTimeFB);
                txtAccepttimetwo.setText(strStartTimeFB);
                txtAccepttimethree.setText("");
                txtAccepttimefour.setText("");

                break;
            case "4":

                btnCancelJobsInProgress.setVisibility(View.VISIBLE);

                llTripviewone.setVisibility(View.VISIBLE);
                llTripviewtwo.setVisibility(View.VISIBLE);
                llTripviewthree.setVisibility(View.VISIBLE);
                llTripviewfour.setVisibility(View.GONE);

                if (strLanguageShortName.equals("en")) {
                    txtAcceptone.setText(strMainCategoryid + " " + getString(R.string.map_status_count_driver_accepted_the_request));
                    txtAccepttwo.setText(strMainCategoryid + " " + getString(R.string.map_status_count_Driver_has_started_the_trip_to_your_location));
                    txtAcceptthree.setText(strMainCategoryid + " " + getString(R.string.map_status_count_Driver_has_arrived_to_job_location));
                } else {
                    txtAcceptone.setText(strMainCategoryidAr + " " + getString(R.string.map_status_count_driver_accepted_the_request));
                    txtAccepttwo.setText(strMainCategoryidAr + " " + getString(R.string.map_status_count_Driver_has_started_the_trip_to_your_location));
                    txtAcceptthree.setText(strMainCategoryidAr + " " + getString(R.string.map_status_count_Driver_has_arrived_to_job_location));
                }


                txtAccepttimeone.setText(strAcceptTimeFB);
                txtAccepttimetwo.setText(strStartTimeFB);
                txtAccepttimethree.setText(strArriveTimeFB);
                txtAccepttimefour.setText("");

                break;
            case "5":
            case "5.1":
            case "5.3":
            case "6":
            case "10":

                btnCancelJobsInProgress.setVisibility(View.GONE);

                llTripviewone.setVisibility(View.VISIBLE);
                llTripviewtwo.setVisibility(View.VISIBLE);
                llTripviewthree.setVisibility(View.VISIBLE);
                llTripviewfour.setVisibility(View.VISIBLE);

                if (strLanguageShortName.equals("en")) {

                    txtAcceptone.setText(strMainCategoryid + " " + getString(R.string.map_status_count_driver_accepted_the_request));
                    txtAccepttwo.setText(strMainCategoryid + " " + getString(R.string.map_status_count_Driver_has_started_the_trip_to_your_location));
                    txtAcceptthree.setText(strMainCategoryid + " " + getString(R.string.map_status_count_Driver_has_arrived_to_job_location));
                    txtAcceptfour.setText(strMainCategoryid + " " + getString(R.string.map_status_count_Driver_has_started_job));
                } else {

                    txtAcceptone.setText(strMainCategoryidAr + " " + getString(R.string.map_status_count_driver_accepted_the_request));
                    txtAccepttwo.setText(strMainCategoryidAr + " " + getString(R.string.map_status_count_Driver_has_started_the_trip_to_your_location));
                    txtAcceptthree.setText(strMainCategoryidAr + " " + getString(R.string.map_status_count_Driver_has_arrived_to_job_location));
                    txtAcceptfour.setText(strMainCategoryidAr + " " + getString(R.string.map_status_count_Driver_has_started_job));
                }
                txtAccepttimeone.setText(strAcceptTimeFB);
                txtAccepttimetwo.setText(strStartTimeFB);
                txtAccepttimethree.setText(strArriveTimeFB);
                txtAccepttimefour.setText(strBeginJobTimeFB);

                break;

        }

        try {
            if (strDriverProfPicFB != null) {
                ivuserProfpicJobinprogress.setImageURI(Utils.BASE_URL_IMAGE_UPLOADS + strDriverProfPicFB);
                ivuserProfpicJobinprogress.getHierarchy().setRoundingParams(roundingParams);

            } else {
                int color = getResources().getColor(R.color.colorPrimary);
                ivuserProfpicJobinprogress.getHierarchy().setRoundingParams(roundingParams);
                ivuserProfpicJobinprogress.setImageURI(Utils.SAMPLE_PROFILE_PIC);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        txtUsernameJobinprogress.setText(strdriverFnameFB + " " + strdriverLnameFB);
        txtPickupJobinprogress.setText(strPickupLocationFB);

        txtBookingIdCommon.setText(strBookingId);
        txtTitleJobinprogres.setText(getResources().getString(R.string.booking_id) + "# " + strBookingId);

        txtDaysJobinprogress.setText(getResources().getString(R.string.no_of_days) + " :- " + strDays);
        txtCountJobinprogress.setText(getResources().getString(R.string.no_of_agents) + " :- " + strCount);

        txtUsernameJobinprogress.setText(struserFnameFB + " " + struserLnameFB);
        txtPickupJobinprogress.setText(strPickupLocationFB);
        txtCompanyJobinprogress.setText(strCompanyName);
        if (strPriceperAgentFB != null) {
            txtAgentFeeLabel.setText("(" + strCount + " X" + strCurrencySymbol + " "
                    + convertCurrencyPrice(activity, strPriceperAgentFB) + ") " +
                    getResources().getString(R.string.guard_count_x_price_per_guard));
            txtDayFeeLabel.setText("(" + strCount + " * " + strDays + "*"
                    + strCurrencySymbol + " " + convertCurrencyPrice(activity, strPriceperDayFB)
                    + ") " + getResources().getString(R.string.guard_count_x_number_of_days_x_price_per_day));
        }

        if (strTripTypeFB.equals("1")) {
            setVisibility(btnExtendJobsInProgress, true);
        } else {
            setVisibility(btnExtendJobsInProgress, false);
        }

        if (strExtendedStatusFB.equals("1")) {
            setVisibility(lytCardPayment, false);
            setVisibility(btnExtendJobsInProgress, false);
            setVisibility(llExtendJobsInProgressDetailsLyt, true);
            if (!strExtendedHrsFB.equals("0")) {
                txtExtendHrsJobsInProgress.setText(convertHoursToDaysHoursMinutes(strExtendedHrsFB));
            } else {
                txtExtendHrsJobsInProgress.setText("-");
            }
            if (!strExtendedAmountFB.equals("0")) {
                txtExtendHrsAmountJobsInProgress.setText(strCurrencySymbol + " " +
                        convertCurrencyPrice(activity, strExtendedAmountFB));
            } else {
                txtExtendHrsAmountJobsInProgress.setText("-");
            }
            if (!strExtendedVatFB.equals("0")) {
                txtExtendHrsVATAmountJobsInProgress.setText(strCurrencySymbol + " " +
                        convertCurrencyPrice(activity, strExtendedVatFB));
            } else {
                txtExtendHrsVATAmountJobsInProgress.setText("-");
            }
            if (!strTotalAmountFB.equals("0")) {
                txtExtendTotalAmountJobsInProgress.setText(strCurrencySymbol + " " +
                        convertCurrencyPrice(activity, strTotalAmountFB));
            } else {
                txtExtendTotalAmountJobsInProgress.setText("-");
            }
        } else {
            //setVisibility(lytCardPayment, false);
            setVisibility(btnExtendJobsInProgress, true);
            setVisibility(llExtendJobsInProgressDetailsLyt, false);
        }

    }

    @SuppressLint("SetTextI18n")
    private void showBalanceStripePayment() {

        cvRating.setVisibility(View.GONE);

        if (strPromoCodeFB.equals("")) {
            txPromoCodeBalanceAmtStripePay.setVisibility(View.GONE);
        } else {
            txPromoCodeBalanceAmtStripePay.setVisibility(View.VISIBLE);
            txPromoCodeBalanceAmtStripePay.setText(getString(R.string.promo_code_apply) + " : " + strPromoCodeFB + " " + strPromoCodeValueFB + "%");
        }

        if (strMaterialChargeFB.equals("0.0")) {
            llMaterialFeeLytBalanceAmtStripePay.setVisibility(View.GONE);
        } else {
            llMaterialFeeLytBalanceAmtStripePay.setVisibility(View.VISIBLE);
            edtMaterialFeeBalanceAmtStripePay.setText(strCurrencySymbol + " " + convertCurrencyPrice(activity, strMaterialChargeFB));
        }

        if (strMiscChargeFB.equals("0.0")) {
            llMiscFeeLytBalanceAmtStripePay.setVisibility(View.GONE);
        } else {
            llMiscFeeLytBalanceAmtStripePay.setVisibility(View.VISIBLE);
            edtMiscFeeBalanceAmtStripePay.setText(strCurrencySymbol + " " + convertCurrencyPrice(activity, strMiscChargeFB));
        }
        if (strDriverDiscountFB.equals("0.0")) {
            llDiscountLytBalanceAmtStripePay.setVisibility(View.GONE);
        } else {
            llDiscountLytBalanceAmtStripePay.setVisibility(View.VISIBLE);
            edtDiscountDriverBalanceAmtStripePay.setText("-" + strCurrencySymbol + " " + convertCurrencyPrice(activity, strDriverDiscountFB));
        }

        if (!strDropLocationFB.equals("NoDrop")) {
            if (strTotalDistanceFB != null) {
                if (!strTotalDistanceFB.equals("0")) {
                    DecimalFormat f = new DecimalFormat("##.##");
                    double fare = Double.parseDouble(strPricePerKMFB) * Double.parseDouble(strTotalDistanceFB);
                    if (strAmountTypeFB.equals("3")) {
                        if (convertDistanceUnit().equals("mile")) {
                            edtDistanceDriverBalanceAmtStripePay.setText("( " + getResources().getString(R.string.additional_charge_price_per_mile) + " " +
                                    convertCurrencyPrice(activity, strPricePerKMFB) + " * " + strTotalDistanceFB + " Mile ) =" + strCurrencySymbol + " " +
                                    convertCurrencyPrice(activity, String.valueOf(fare)));
                        } else if (convertDistanceUnit().equals("km")) {
                            edtDistanceDriverBalanceAmtStripePay.setText("( " + getResources().getString(R.string.additional_charge_price_per_km) + " " +
                                    convertCurrencyPrice(activity, strPricePerKMFB) + " * " + strTotalDistanceFB + " KM ) =" + strCurrencySymbol + " " + convertCurrencyPrice(activity, String.valueOf(fare)));
                        }

                       /* edtDistanceDriverBalanceAmtStripePay.setText("( " + getResources().getString(R.string.additional_charge_price_per_km) + " " +
                                convertCurrencyPrice(requireActivity,strPricePerKMFB) + " * " + strTotalDistanceFB + " KM ) =" + strCurrencySymbol +
                                " " + convertCurrencyPrice(requireActivity,String.valueOf(fare)));*/

                        llDistanceLytBalanceAmtStripePay.setVisibility(View.VISIBLE);
                        edtDistanceDriverBalanceAmtStripePay.setTextSize(10.0f);
                    } else {
                        llDistanceLytBalanceAmtStripePay.setVisibility(View.GONE);
                    }
                }
            }
        } else {
            llDistanceLytBalanceAmtStripePay.setVisibility(View.GONE);
        }

        if (strLevelTypeFB.equals("0")) {

            if (strAmountTypeFB.equals("3")) {

                if (!strVisitPriceFB.equals("0")) {
                    txtCurrentChargeLabelBalanceAmtStripePay.setText(getResources().getString(R.string.additional_charge_current_charger) + "(" + convertCurrencyPrice(activity, strSubLevelAmountFB) + ")"
                            + "+" + getResources().getString(R.string.additional_charge_visit_price) + "(" + convertCurrencyPrice(activity, strVisitPriceFB) + ")");

                    double fares = (Double.parseDouble(strSubLevelAmountFB) + Double.parseDouble(strVisitPriceFB));

                    Log.e(TAG, "callEndJobServices:fares~~ ~~~" + fares);

                    txtCurrentChargeBalanceAmtStripePay.setText(strCurrencySymbol + " " + convertCurrencyPrice(activity, String.valueOf(fares)));
                } else {
                    txtCurrentChargeBalanceAmtStripePay.setText(strCurrencySymbol + " " + convertCurrencyPrice(activity, strUnitPriceFB));
                }

            } else {
                if (!strVisitPriceFB.equals("0")) {
                    txtCurrentChargeLabelBalanceAmtStripePay.setText(getResources().getString(R.string.additional_charge_current_charger) + "(" + convertCurrencyPrice(activity, strSubLevelAmountFB) + ")"
                            + "+" + getResources().getString(R.string.additional_charge_visit_price) + "(" + convertCurrencyPrice(activity, strVisitPriceFB) + ")");

                    double fares = (Double.parseDouble(strSubLevelAmountFB) + Double.parseDouble(strVisitPriceFB));

                    Log.e(TAG, "callEndJobServices:fares~~ ~~~" + fares);

                    txtCurrentChargeBalanceAmtStripePay.setText(strCurrencySymbol + " " + convertCurrencyPrice(activity, String.valueOf(fares)));
                } else {
                    txtCurrentChargeBalanceAmtStripePay.setText(strCurrencySymbol + " " + convertCurrencyPrice(activity, strUnitPriceFB));
                }
            }

        } else {

            if (strVisitPriceFB.equals("0")) {
                txtCurrentChargeLabelBalanceAmtStripePay.setText(getResources().getString(R.string.additional_charge_current_charger) + "(" + strUnitPriceFB + ")" + "*" +
                        strMainCategoryid + " charge" + "(" + strMainUnitPriceFB + ")");
                txtCurrentChargeLabelBalanceAmtStripePay.setTextSize(14.0f);

            } else {
                txtCurrentChargeLabelBalanceAmtStripePay.setText(getResources().getString(R.string.additional_charge_current_charger) + "(" + strUnitPriceFB + ")" + "*" +
                        strMainCategoryid + " charge" + "(" + strMainUnitPriceFB + ") + " + getResources().getString(R.string.additional_charge_visit_price) + "(" + strVisitPriceFB + ")");
                txtCurrentChargeLabelBalanceAmtStripePay.setTextSize(14.0f);

            }

            DecimalFormat f1 = new DecimalFormat("##.##");
            double fares = ((Double.parseDouble(strUnitPriceFB) * Double.parseDouble(strMainUnitPriceFB))
                    + Double.parseDouble(strVisitPriceFB));
            Log.e(TAG, "callEndJobServices:fares~~ ~~~" + fares);
            txtCurrentChargeBalanceAmtStripePay.setText(convertCurrencyPrice(activity, String.valueOf(fares)));

        }


        if (strTotalAmountFB != null && !strTotalAmountFB.equals("null")) {

            llParentLytAmountDetailsBalanceAmtStripePay.setVisibility(View.VISIBLE);
            txtFinalTotalBalanceAmtStripePay.setText(strCurrencySymbol + " " + convertCurrencyPrice(activity, strTotalAmountFB));

            if (strBalanceAmountFB != null && !strBalanceAmountFB.equals("null")) {

                txtBalanceAmount.setText(getString(R.string.dialog_balance_amount_your_current_job_balance_amount) + " " + strCurrencySymbol + " " + convertCurrencyPrice(activity, strBalanceAmountFB));
                txtFinalBalanceAmtStripePay.setText(strCurrencySymbol + " " + convertCurrencyPrice(activity, strBalanceAmountFB));

                double balfares = (Double.parseDouble(strTotalAmountFB) - Double.parseDouble(strBalanceAmountFB));
                if (balfares > 0.0) {
                    txtMyWalletBalanceAmtStripePay.setText("-" + strCurrencySymbol + " " + convertCurrencyPrice(activity, String.valueOf(balfares)));
                } else {
                    txtMyWalletBalanceAmtStripePay.setText("-" + strCurrencySymbol + "-");
                }

            } else {
                txtFinalBalanceAmtStripePay.setText(strCurrencySymbol + " " + convertCurrencyPrice(activity, strBalanceAmountFB));
            }

        } else {
            llParentLytAmountDetailsBalanceAmtStripePay.setVisibility(View.GONE);
        }

        double totalAgentAmount = Double.parseDouble(strCount) * Double.parseDouble(strPriceperAgentFB);
        txtAgentCharge.setText(strCurrencySymbol + " " + convertCurrencyPrice(activity, Double.toString(totalAgentAmount)));
        txtAgentFeeLabel.setText("(" + strCount + " *" + strCurrencySymbol + " " + strPriceperAgentFB + ")");
                           /* strBasePriceFB = Objects.requireNonNull(dataSnapshot.child("base_fare").getValue()).toString();
                            txtBaseCharge.setText(convertCurrencyPrice(requireActivity,strBasePriceFB));*/
        double totalDayAmount = Double.parseDouble(strCount) * Double.parseDouble(strDays) * Double.parseDouble(strPriceperDayFB);

        txtDayCharge.setText(strCurrencySymbol + " " + convertCurrencyPrice(activity, Double.toString(totalDayAmount)));
        txtDayFeeLabel.setText("(" + strCount + " * " + strDays + "*" + strCurrencySymbol + " " + strPriceperDayFB + ")");

    }

    @SuppressLint("SetTextI18n")
    private void showCardPayment(String from) {

        lytTripSummary.setVisibility(View.GONE);

        llCardPaymentDetailsListAreaCardPayment.setVisibility(View.VISIBLE);

        if (from.equals("card")) {
            llStripeContentCardPayment.setVisibility(View.VISIBLE);
            btnPayPalPayment.setVisibility(View.GONE);
            txtCardPaymentTitleLabel.setText(getResources().getString(R.string.you_choose) + " " + getResources().getString(R.string.select_payment_pay_online));
            switch (strCardStatus) {
                case "1":
                    lytCardPayment.setVisibility(View.GONE);
                    txtWaitaMomentCardPayment.setVisibility(View.VISIBLE);
                    break;
                case "0":
                case "2":
                    lytCardPayment.setVisibility(View.VISIBLE);
                    txtWaitaMomentCardPayment.setVisibility(View.GONE);
                    break;
                case "4":
                default:
                    lytCardPayment.setVisibility(View.GONE);
                    txtWaitaMomentCardPayment.setVisibility(View.GONE);
                    break;
            }

        } else {
            txtCardPaymentTitleLabel.setText(getResources().getString(R.string.you_choose) + " " + getResources().getString(R.string.select_payment_paypal));
            llStripeContentCardPayment.setVisibility(View.GONE);
            btnPayPalPayment.setVisibility(View.VISIBLE);
            switch (strPayPalStatus) {
                case "1":
                    lytCardPayment.setVisibility(View.GONE);
                    txtWaitaMomentCardPayment.setVisibility(View.VISIBLE);
                    break;
                case "0":
                case "2":
                    lytCardPayment.setVisibility(View.VISIBLE);
                    txtWaitaMomentCardPayment.setVisibility(View.GONE);
                    break;
                case "4":
                default:
                    lytCardPayment.setVisibility(View.GONE);
                    txtWaitaMomentCardPayment.setVisibility(View.GONE);
                    break;
            }

        }

        if (strPromoCodeFB.equals("")) {
            txPromoCodeCardPayment.setVisibility(View.GONE);
        } else {
            txPromoCodeCardPayment.setVisibility(View.VISIBLE);
            txPromoCodeCardPayment.setText(getString(R.string.promo_code_apply) + " : " + strPromoCodeFB + " " + strPromoCodeValueFB + "%");
        }

        if (strMaterialChargeFB.equals("0.0")) {
            llMaterialFeeLytCardPayment.setVisibility(View.GONE);
        } else {
            llMaterialFeeLytCardPayment.setVisibility(View.VISIBLE);
            edtMaterialFeeCardPayment.setText(strCurrencySymbol + " " + convertCurrencyPrice(activity, strMaterialChargeFB));
        }

        if (strMiscChargeFB.equals("0.0")) {
            llMiscFeeLytCardPayment.setVisibility(View.GONE);
        } else {
            llMiscFeeLytCardPayment.setVisibility(View.VISIBLE);
            edtMiscFeeCardPayment.setText(strCurrencySymbol + " " + convertCurrencyPrice(activity, strMiscChargeFB));
        }
        if (strDriverDiscountFB.equals("0.0")) {
            llDiscountLytCardPayment.setVisibility(View.GONE);
        } else {
            llDiscountLytCardPayment.setVisibility(View.VISIBLE);
            edtDiscountDriverCardPayment.setText("-" + strCurrencySymbol + " " + convertCurrencyPrice(activity, strDriverDiscountFB));
        }

        if (!strDropLocationFB.equals("NoDrop")) {
            if (strTotalDistanceFB != null) {
                if (!strTotalDistanceFB.equals("0")) {
                    if (strAmountTypeFB.equals("3")) {

                        double fare = Double.parseDouble(strPricePerKMFB) * Double.parseDouble(strTotalDistanceFB);


                        if (convertDistanceUnit().equals("mile")) {
                            edtDistanceDriverCardPayment.setText("( " + getResources().getString(R.string.additional_charge_price_per_mile) + " " +
                                    convertCurrencyPrice(activity, strPricePerKMFB) + " * " + strTotalDistanceFB + " Mile ) =" + strCurrencySymbol + " " +
                                    convertCurrencyPrice(activity, String.valueOf(fare)));

                        } else if (convertDistanceUnit().equals("km")) {
                            edtDistanceDriverCardPayment.setText("( " + getResources().getString(R.string.additional_charge_price_per_km) + " " +
                                    convertCurrencyPrice(activity, strPricePerKMFB) + " * " + strTotalDistanceFB + " KM ) =" + strCurrencySymbol + " " + convertCurrencyPrice(activity, String.valueOf(fare)));
                        }

                        llDistanceLytCardPayment.setVisibility(View.VISIBLE);
                        edtDistanceDriverCardPayment.setTextSize(10.0f);

                    } else {
                        llDistanceLytCardPayment.setVisibility(View.GONE);
                    }
                } else {
                    llDistanceLytCardPayment.setVisibility(View.GONE);
                }
            } else {
                llDistanceLytCardPayment.setVisibility(View.GONE);
            }
        } else {
            llDistanceLytCardPayment.setVisibility(View.GONE);
        }

        if (strLevelTypeFB.equals("0")) {

            if (strAmountTypeFB.equals("3")) {

                if (!strVisitPriceFB.equals("0")) {
                    txtCurrentChargeLabelCardPayment.setText(getResources().getString(R.string.additional_charge_current_charger) + "(" + convertCurrencyPrice(activity, strSubLevelAmountFB) + ")"
                            + "+" + getResources().getString(R.string.additional_charge_visit_price) + "(" + convertCurrencyPrice(activity, strVisitPriceFB) + ")");

                    double fares = (Double.parseDouble(strSubLevelAmountFB) + Double.parseDouble(strVisitPriceFB));

                    Log.e(TAG, "callEndJobServices:fares~~ ~~~" + fares);

                    txtCurrentChargeCardPayment.setText(strCurrencySymbol + " " + convertCurrencyPrice(activity, String.valueOf(fares)));
                } else {
                    txtCurrentChargeCardPayment.setText(strCurrencySymbol + " " + convertCurrencyPrice(activity, strUnitPriceFB));
                }

            } else {
                if (!strVisitPriceFB.equals("0")) {
                    txtCurrentChargeLabelCardPayment.setText(getResources().getString(R.string.additional_charge_current_charger) + "(" + convertCurrencyPrice(activity, strSubLevelAmountFB) + ")"
                            + "+" + getResources().getString(R.string.additional_charge_visit_price) + "(" + convertCurrencyPrice(activity, strVisitPriceFB) + ")");

                    double fares = (Double.parseDouble(strSubLevelAmountFB) + Double.parseDouble(strVisitPriceFB));

                    Log.e(TAG, "callEndJobServices:fares~~ ~~~" + fares);

                    txtCurrentChargeCardPayment.setText(strCurrencySymbol + " " + convertCurrencyPrice(activity, String.valueOf(fares)));
                } else {
                    txtCurrentChargeCardPayment.setText(strCurrencySymbol + " " + convertCurrencyPrice(activity, strUnitPriceFB));
                }
            }

            if (strTotalAmountFB != null && !strTotalAmountFB.equals("null")) {
                txtFinalTotalCardPayment.setText(strCurrencySymbol + " " + convertCurrencyPrice(activity, strTotalAmountFB));
            }

        } else {

            if (strVisitPriceFB.equals("0")) {
                txtCurrentChargeLabelCardPayment.setText(getResources().getString(R.string.additional_charge_current_charger) + "(" + strUnitPriceFB + ")" + "*" +
                        strMainCategoryid + " charge" + "(" + strMainUnitPriceFB + ")");
                txtCurrentChargeLabelCardPayment.setTextSize(14.0f);

            } else {
                txtCurrentChargeLabelCardPayment.setText(getResources().getString(R.string.additional_charge_current_charger) + "(" + strUnitPriceFB + ")" + "*" +
                        strMainCategoryid + " charge" + "(" + strMainUnitPriceFB + ") + " + getResources().getString(R.string.additional_charge_visit_price) + "(" + strVisitPriceFB + ")");
                txtCurrentChargeLabelCardPayment.setTextSize(14.0f);

            }

            DecimalFormat f1 = new DecimalFormat("##.##");
            double fares = ((Double.parseDouble(strUnitPriceFB) * Double.parseDouble(strMainUnitPriceFB))
                    + Double.parseDouble(strVisitPriceFB));
            Log.e(TAG, "callEndJobServices:fares~~ ~~~" + fares);
            txtCurrentChargeCardPayment.setText(convertCurrencyPrice(activity, String.valueOf(fares)));

            if (strTotalAmountFB != null && !strTotalAmountFB.equals("null")) {
                txtFinalTotalCardPayment.setText(strCurrencySymbol + " " + convertCurrencyPrice(activity, strTotalAmountFB));
            }

        }

    }


    @SuppressLint("SetTextI18n")
    private void showTripSummary(Double aDouble, Double aDouble2) {


        if (strPromoCodeFB.equals("")) {
            txPromoCodeTripSummary.setVisibility(View.GONE);
        } else {
            txPromoCodeTripSummary.setVisibility(View.VISIBLE);
            txPromoCodeTripSummary.setText(getResources().getString(R.string.promo_code_apply) + " : " + strPromoCodeFB + " " + strPromoCodeValueFB + "%");
        }

        if (strMaterialChargeFB.equals("0.0")) {
            llMaterialFeelyt.setVisibility(View.GONE);
        } else {
            llMaterialFeelyt.setVisibility(View.VISIBLE);
            txtMaterialFeeTripSummary.setText(strCurrencySymbol + " " + convertCurrencyPrice(activity, strMaterialChargeFB));
        }
        if (strMiscChargeFB.equals("0.0")) {
            llMiscFeelyt.setVisibility(View.GONE);
        } else {
            llMiscFeelyt.setVisibility(View.VISIBLE);
            txtMiscFeeTripSummary.setText(strCurrencySymbol + " " + convertCurrencyPrice(activity, strMiscChargeFB));
        }
        if (strDriverDiscountFB.equals("0.0")) {
            llDiscountlyt.setVisibility(View.GONE);
        } else {
            llDiscountlyt.setVisibility(View.VISIBLE);
            txtDiscountDriverTripSummary.setText("-" + strCurrencySymbol + " " + convertCurrencyPrice(activity, strDriverDiscountFB));
            txtDiscountTripSummary.setText(strCurrencySymbol + " " + convertCurrencyPrice(activity, strDriverDiscountFB));
        }


        if (strPickupLocationFB != null) {
            txtPickupLocTripSummary.setText(strPickupLocationFB);
        }

        if (!strDropLocationFB.equals("NoDrop")) {
            txtDropLocTripSummary.setText(strDropLocationFB);
            if (strTotalDistanceFB != null) {
                if (!strTotalDistanceFB.equals("0")) {

                    if (strAmountTypeFB.equals("3")) {

                        double fare = Double.parseDouble(strPricePerKMFB) * Double.parseDouble(strTotalDistanceFB);

                        if (fare > 0.0) {
                            llDistanceLytTripSummary.setVisibility(View.VISIBLE);
                        } else {
                            llDistanceLytTripSummary.setVisibility(View.GONE);
                        }

                        if (convertDistanceUnit().equals("mile")) {
                            txtDistanceTripSummaryLabel.setText("( " + getResources().getString(R.string.additional_charge_price_per_mile) + " " + convertCurrencyPrice(activity, strPricePerKMFB) +
                                    " * " + strTotalDistanceFB + " Mile )");

                            txtDistanceTripSummary.setText(strCurrencySymbol + " " + convertCurrencyPrice(activity, String.valueOf(fare)));

                        } else if (convertDistanceUnit().equals("km")) {
                            txtDistanceTripSummaryLabel.setText("( " + getResources().getString(R.string.additional_charge_price_per_km) + " " + convertCurrencyPrice(activity, strPricePerKMFB) +
                                    " * " + strTotalDistanceFB + " KM )");
                            txtDistanceTripSummary.setText(strCurrencySymbol + " " + convertCurrencyPrice(activity, String.valueOf(fare)));
                        }

                    } else {
                        llDistanceLytTripSummary.setVisibility(View.GONE);
                    }
                } else {
                    llDistanceLytTripSummary.setVisibility(View.GONE);
                }
            } else {
                llDistanceLytTripSummary.setVisibility(View.GONE);
            }
        } else {
            llDistanceLytTripSummary.setVisibility(View.GONE);
            txtDropLocTripSummary.setVisibility(View.GONE);
            txtDropLocTripSummaryLabel.setVisibility(View.GONE);
        }

        txtJobDateTripSummary.setText(strJobDateFB);
        txtCategoryTripSummary.setText(strMainCategoryid);
        if(strPaymentTypeFB.equals("stripe_invoice")) {
            txtPaymentTypeTripSummary.setText("Stripe");
        } else {
            txtPaymentTypeTripSummary.setText(strPaymentTypeFB);
        }

        if (strTotalAmountFB != null && !strTotalAmountFB.equals("null")) {
            txtFinalTotalHeaderTripSummary.setText(strCurrencySymbol + " " + convertCurrencyPrice(activity, strTotalAmountFB));
            txtFinalTotalTripSummary.setText(strCurrencySymbol + " " + convertCurrencyPrice(activity, strTotalAmountFB));
        }

        if (!strVatAmountsFB.equals("0")) {
            txtVatAmountTripSummary.setText(strCurrencySymbol + ""
                    + convertCurrencyPrice(activity, strVatAmountsFB));
        } else {
            txtVatAmountTripSummary.setText("");
        }

        if (!strTotalAmountFB.equals("0")) {
            txtTotalAmtSummary.setText(strCurrencySymbol + "" + convertCurrencyPrice( this, strTotalAmountFB));
        } else {
            txtTotalAmtSummary.setText("");
        }

        if (!strTotalHrsFB.equals("0")) {
            //txtNoofHrsTripSummary.setText(strTotalHrsFB + " " + getResources().getString(R.string.hrs));
            txtNoofHrsTripSummary.setText(convertHoursToDaysHoursMinutes(strTotalHrsFB));
        } else {
            txtNoofHrsTripSummary.setText("");
        }

        if (strExtendedStatusFB.equals("1")) {
            setVisibility(trExtendNoofHrsTripSummary, true);
            if (!strExtendedHrsFB.equals("0")) {
                txtExtendNoofHrsTripSummary.setText(convertHoursToDaysHoursMinutes(strExtendedHrsFB));
            } else {
                txtExtendNoofHrsTripSummary.setText("-");
            }

           /* if (!strExtendedAmountFB.equals("0")) {
                txtExtendHrsAmountJobsInProgress.setText(strCurrencySymbol + " " +
                        convertCurrencyPrice(requireActivity, strExtendedAmountFB));
            } else {
                txtExtendHrsAmountJobsInProgress.setText("-");
            }
            if (!strExtendedVatFB.equals("0")) {
                txtExtendHrsVATAmountJobsInProgress.setText(strCurrencySymbol + " " +
                        convertCurrencyPrice(requireActivity, strExtendedVatFB));
            } else {
                txtExtendHrsVATAmountJobsInProgress.setText("-");
            }*/
        } else {
            setVisibility(trExtendNoofHrsTripSummary, false);
        }

        if (!strTotalHrsFB.equals("0") && !stPricePerHrsFB.equals("0")) {

            String hrs_;
            if (strExtendedStatusFB.equals("1")) {
                hrs_ = String.valueOf(Double.parseDouble(strTotalHrsFB) + Double.parseDouble(strExtendedHrsFB));
            } else {
                hrs_ = strTotalHrsFB;
            }

            String label = "( " + getResources().getString(R.string.price_per_hrs) + " = " + strCurrencySymbol + ""
                    + convertCurrencyPrice(activity, stPricePerHrsFB) + " * " + hrs_
                    + " " + getResources().getString(R.string.hrs) + " )";
            txtPricePerHrsLabelTripSummary.setText(label);

            double fare = Double.parseDouble(stPricePerHrsFB) * Double.parseDouble(hrs_);
            if (fare <= 0.0) {
                fare = 0.0;
            }
            txtPricePerHrsTripSummary.setText(strCurrencySymbol + ""
                    + convertCurrencyPrice(activity, String.valueOf(fare)));
        } else {
            txtPricePerHrsLabelTripSummary.setText(getResources().getString(R.string.price_per_hrs));
            txtPricePerHrsTripSummary.setText("");
        }

        if (strSubscriptionStatusFB.equals("1")) {
            setVisibility(txtSubscribtionStatusTripSummary, true);
        } else {
            setVisibility(txtSubscribtionStatusTripSummary, false);
        }

        if (lytTripSummary.getVisibility() == View.GONE) {
            lytTripSummary.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        Log.e(TAG, "onCancelled: ~~~~~" + databaseError.toString());
    }

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "onDirectionSuccess: ~~~~~~");
        }
    }

    @Override
    public void onDirectionFailure(Throwable t) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "onDirectionSuccess: ~~~~~~" + t.toString());
        }
    }


    public float getDriverBearing(String key) {

        Log.i("***getkey*****", key);
        if (bearingDatabaseRef != null) {
            bearingDatabaseRef.removeEventListener(bearingValueEventListener);
        }
        bearingDatabaseRef = FirebaseDatabase.getInstance().getReference().child("drivers_location").child(key).child("bearing");
        bearingValueEventListener = bearingDatabaseRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    String status = dataSnapshot.getValue().toString();
                    Log.i("***********getDriverBearing***************", status);

                    if (isFloat(status)) {
                        driverBearing = Float.parseFloat(status);
                    } else {
                        driverBearing = (float) Integer.parseInt(status);
                    }

                    // getDriverEstDistanceDuration(key);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: ~~~~~" + databaseError.toString());
            }
        });
        return driverBearing;
    }

    private void getDriverEstDistanceDuration(String key) {

      /*  if (driverEstDistanceDurationDatabaseBRef != null) {
            driverEstDistanceDurationDatabaseBRef.removeEventListener(driverEstDistanceDurationValueEventListener);
        }
        driverEstDistanceDurationDatabaseBRef = FirebaseDatabase.getInstance().getReference().child(Utils.FIREBASE_PROVIDERS_HEADER).child(key);
        driverEstDistanceDurationValueEventListener = driverEstDistanceDurationDatabaseBRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    Object est_distance = dataSnapshot.child("est_distance").getValue();
                    Object est_duration = dataSnapshot.child("est_duration").getValue();

                    if (est_distance != null) {
                        strEstDistance = String.valueOf(est_distance);
//                        Log.e(TAG, "onDataChange: strEstDistance~~~>>"+strEstDistance);
                    } else {
                        strEstDistance = "Loading";
                    }

                    if (est_duration != null) {
                        strEstDuration = String.valueOf(est_duration);
//                        Log.e(TAG, "onDataChange: strEstDuration~~~>>"+strEstDuration);
                    } else {
                        strEstDuration = "Loading";
                    }

                    if (driverAcceptCarMarker != null) {
                        driverAcceptCarMarker.setTitle(strEstDistance + " , " + strEstDuration);
                    }

                    txtEstDistDurationJobinprogress.setText(getResources().getString(R.string.est_distance) + "-" + strEstDistance + " & " + getResources().getString(R.string.est_duration) + "-" + strEstDuration);

                    if (!strDropLocationFB.equals("NoDrop")) {
                        if (driverAcceptCarMarker != null *//*&& !driverAcceptCarMarker.isCluster()*//*) {
                            // driverAcceptCarMarker.showInfoWindow();
                            if (driverAcceptCarMarker.isInfoWindowShown()) {
                                driverAcceptCarMarker.hideInfoWindow();
                                driverAcceptCarMarker.showInfoWindow();
                            }
                        }


                    } else {
                        Log.e(TAG, "onDirectionSuccessPlaceMarker: Distance NO``");
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: ~~~~" + databaseError.toString());
            }
        });*/
    }

    /**
     * Receiver for data sent from FetchAddressIntentService.
     */
    private class AddressResultReceiver extends ResultReceiver {
        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        /**
         * Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string or an error message sent from the intent service.
            mAddressOutput = resultData.getString(Utils.RESULT_DATA_KEY);
            gettingType = resultData.getString(Utils.RESULT_DATA);
            Log.i(TAG, "onReceiveResult: gettingType-->" + gettingType);
            //displayAddressOutput();
            displayAddressOutput();

            // Show a toast message if an address was found.
            if (resultCode == Utils.SUCCESS_RESULT) {
                //    showToast(getString(R.string.address_found));
            }

            // Reset. Enable the Fetch Address button and stop showing the progress bar.
            //mAddressRequested = false;
            // updateUIWidgets();
        }
    }

    public void displayAddressOutput() {
        Log.i(TAG, "onReceiveResult: geocoder-->" + mAddressOutput);
        // txtPickupLocation.setText(mAddressOutput);
    }

    private void startIntentService(Location location, String type) {
        // Create an intent for passing to the intent service responsible for fetching the address.
        Intent intent = new Intent(this, FetchAddressIntentService.class);

        // Pass the result receiver as an extra to the service.
        intent.putExtra(Utils.RECEIVER, mResultReceiver);

        // Pass the location data as an extra to the service.
        intent.putExtra(Utils.LOCATION_DATA_EXTRA, location);
        intent.putExtra(Utils.GEOCODER_TYPE, type);
        Log.i(TAG, "startIntentService: type---->" + type);

        // Start the service. If the service isn't already running, it is instantiated and started
        // (creating a process for it if needed); if it is running then it remains running. The
        // service kills itself automatically once all intents are processed.
        startService(intent);
    }

    /**
     * Updates fields based on data stored in the bundle.
     */
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Check savedInstanceState to see if the address was previously requested.
            if (savedInstanceState.keySet().contains(ADDRESS_REQUESTED_KEY)) {
                mAddressRequested = savedInstanceState.getBoolean(ADDRESS_REQUESTED_KEY);
                Log.i(TAG, "updateValuesFromBundle:mAddressRequested -->" + mAddressRequested);
            }
            // Check savedInstanceState to see if the location address string was previously found
            // and stored in the Bundle. If it was found, display the address string in the UI.
            if (savedInstanceState.keySet().contains(LOCATION_ADDRESS_KEY)) {
                mAddressOutput = savedInstanceState.getString(LOCATION_ADDRESS_KEY);
                Log.i(TAG, "updateValuesFromBundle:mAddressOutput -->" + mAddressOutput);
                displayAddressOutput();

            }
        }
    }

    public void currentLocationClick(View view) {
        Log.i(TAG, "currentLocationClick: " + locFusedLocation);
        if (locFusedLocation != null) {
            Log.i(TAG, "currentLocationClick: " + locFusedLocation.getLatitude() + " " + locFusedLocation.getLongitude());
            LatLng latLng = new LatLng(locFusedLocation.getLatitude(), locFusedLocation.getLongitude());
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)                              // Sets the center of the map to current location
                    .zoom(Utils.MAP_DEFAULT_ZOOM)
                    .tilt(0)                                     // Sets the tilt of the camera to 0 degrees
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        } else {
            Toast.makeText(this, getString(R.string.mainacativity_getting_location), Toast.LENGTH_SHORT).show();
        }

    }

    /*Geocoder and Fusedlocation End*/
    public void moveCamera(Double lat, Double lng) {
        LatLng coordinates = new LatLng(lat, lng);
        CameraUpdate current = CameraUpdateFactory.newLatLngZoom(coordinates, Utils.MAP_DEFAULT_ZOOM);
        mMap.moveCamera(current);
    }

  /*  public void addMarqueeTextfield(TextView textview){
        textview.setHorizontallyScrolling(true);
        textview.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textview.setSingleLine();
        textview.setSelected(true);
    }*/

    private void getTripStatus_Listener() {
        if (strBookingId != null && !strBookingId.equals("null") && !strBookingId.equals("") && !strBookingId.equals(" ")) {
            Log.e(TAG, "getTripStatus_Listener: ~~~~~~~~~~~~~~~>>>>>>>>>>>>>>>>>>>>>");
            if (tripStatusDatabaseRef != null) {
                tripStatusDatabaseRef.removeEventListener(tripStatusValueEventListener);
            }
            tripStatusDatabaseRef = /*new GeoFire(*/FirebaseDatabase.getInstance().getReference().
                    child(Utils.FIREBASE_BOOKING_TRIPS_HEADER).child(strBookingId).limitToLast(100).getRef()/*).getDatabaseReference()*/;
            tripStatusValueEventListener = tripStatusDatabaseRef.addValueEventListener(this);
        } else {
            getState.putString(Utils.SP_USER_CURRENT_BOOKING_ID, null).apply();
            finish();
        }
    }


    /*-------------------
     *
     * *Call related function using driver request accepting
     *
     *--------------------- */
    private void callDriverTelephonyManager(String phoneNumber) {
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            try {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(intent);
            } catch (ActivityNotFoundException ex) {

                Toast.makeText(activity, R.string.no_is_not_found, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(activity, R.string.no_mobile_num, Toast.LENGTH_SHORT).show();
        }
    }

    private void callGoToHomeButtonAction(boolean b) {
        //9B9B9B
        if (b) {
            btnCollectPaymentTripSummary.setBackgroundTintList(getResources().getColorStateList(R.color.black));
        } else {
            btnCollectPaymentTripSummary.setBackgroundTintList(getResources().getColorStateList(R.color.gray_black));
        }

        btnCollectPaymentTripSummary.setEnabled(b);
        btnCollectPaymentTripSummary.setClickable(b);
        btnCollectPaymentTripSummary.setFocusable(b);
    }


    public void removeRequestDetailsFirebase() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().
                child(Utils.FIREBASE_USER_HEADER).child(userId).child(Utils.FIREBASE_TRIP_HEADER);

        databaseReference.child(Utils.FIREBASE_SERVICE_STATUS).setValue("0");
        databaseReference.child(Utils.FIREBASE_ACCEPT_STATUS).setValue("0");

        Map<String, Object> taskMap = new HashMap<>();
        taskMap.put(Utils.FIREBASE_TRIP_CUS_ID, null);
        taskMap.put(Utils.FIREBASE_TRIP_CUS_NAME, null);
        taskMap.put("user_rating", null);
        taskMap.put("user_pickup", null);
        taskMap.put("user_drop", null);
        taskMap.put("main_service", null);
        taskMap.put("sub_service", null);
        taskMap.put("pickup_lat", null);
        taskMap.put("pickup_lng", null);
        taskMap.put("paymode", null);
        taskMap.put("special_ins", null);
        taskMap.put("promocode", null);
        taskMap.put("base_fare", null);
        databaseReference.updateChildren(taskMap, null);
    }


    public void removeBookingTripsFirebase(String bookingId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child(Utils.FIREBASE_BOOKING_TRIPS_HEADER).child(bookingId);
        databaseReference.removeValue();
    }

    /*-------------------
     *
     * * Dialog customer info
     *
     *--------------------- */
    @SuppressLint("SetTextI18n")
    private void showCustomerInfodialog() {

        dialogCustomerinfo = new Dialog(activity);
        dialogCustomerinfo.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialogCustomerinfo.setContentView(R.layout.dialog_driverinfo);
        Objects.requireNonNull(dialogCustomerinfo.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogCustomerinfo.setCancelable(true);
        dialogCustomerinfo.getWindow().setWindowAnimations(R.style.DialogAnimationLR);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogCustomerinfo.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        CustomTextView txtCustomerNameinfo = dialogCustomerinfo.findViewById(R.id.txt_customer_name_info);
        CustomTextView txtBookingidinfo = dialogCustomerinfo.findViewById(R.id.txt_bookingid_info);
        CustomTextView txtCustomerMobileinfo = dialogCustomerinfo.findViewById(R.id.txt_mobile_info);
        // LinearLayout calldriver = (LinearLayout) dialogCustomerinfo.findViewById(R.id.ll_call);
        // LinearLayout messagedriver = (LinearLayout) dialogCustomerinfo.findViewById(R.id.ll_message);
        ImageView close = dialogCustomerinfo.findViewById(R.id.imgclose);
        SimpleDraweeView imgCustomerInfoProPic = dialogCustomerinfo.findViewById(R.id.img_customer_info_pro_pic);

        //Customer info screen profile pic
        try {
            imgCustomerInfoProPic.getHierarchy().setRoundingParams(roundingParams);
            imgCustomerInfoProPic.setImageURI(Utils.SAMPLE_PROFILE_PIC);
        } catch (Exception e) {
            e.printStackTrace();
        }

        txtCustomerNameinfo.setText(strdriverFnameFB + " " + strdriverLnameFB);
        txtBookingidinfo.setText(strBookingId);
        txtCustomerMobileinfo.setText(strdriverMobileFB);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // here I want to dismiss it after SetBackground() method
                dialogCustomerinfo.dismiss();
            }
        });

        dialogCustomerinfo.show();
        dialogCustomerinfo.getWindow().setAttributes(lp);
    }

    private void callCancelJobUserUpdate() {

        SharedPreferences getspCurrency = getSharedPreferences(Utils.PREFS_NAME_CURRENCY, MODE_PRIVATE);
        String strLanguageShortName = getspCurrency.getString(Utils.SP_LANGUAGE_SHORT_NAME, null);
        if (strLanguageShortName == null) {
            strLanguageShortName = "en";
        }

        userAccessToken = spCommon.getString(Utils.SP_USER_ACCESSTOKEN, null);
        HashMap<String, Object> map = new HashMap<>();
        map.put("booking_id", strBookingId);
        map.put("reason", "");
        map.put("mode", "customer");
        map.put("language", strLanguageShortName);
        Log.i("CallUpdateDetails", "~~~~" + map);

        Call<CancelJobModel> call2 = apiInterface.doCancelJobDetails("Bearer " + userAccessToken, map);
        showProgressDialog(this);
        call2.enqueue(new Callback<CancelJobModel>() {
            @Override
            public void onResponse(@NonNull Call<CancelJobModel> call, @NonNull Response<CancelJobModel> response) {

                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {

                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Cancelled successfully.")) {
                        // removeFragment(Utils.FRAGMENT_ONGOINGJOBS);
                        Intent mainActivityIntent = new Intent(activity, MainActivity.class);
                        mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainActivityIntent);
                        finish();
                    } else {
                        Toast.makeText(activity, R.string.try_again, Toast.LENGTH_SHORT).show();

                    }
                } else if (response.code() == RESPONSECODE_401) {

                }
            }

            @Override
            public void onFailure(@NonNull Call<CancelJobModel> call, @NonNull Throwable t) {
                hideProgressDialog();
                call.cancel();
            }
        });
    }


    public void removeFragment(String Tag) {
        Utils.CURRENT_TAG = null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(Objects.requireNonNull(fragmentManager.findFragmentByTag(Tag))).commit();
    }


    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        finish();
    }


    @SuppressLint("LongLogTag")
    private void callViewEmergencyDetails() {

        showProgressDialog(activity);
        userAccessToken = spCommon.getString(Utils.SP_USER_ACCESSTOKEN, null);
        HashMap<String, Object> map = new HashMap<>();
        map.put("customer_id", userId);
        Log.i("callViewEmergencyDetails", "~~~~" + map);

        Call<EmergencyViewModel> call2 = apiInterface.doGetViewEmergencyDetails("Bearer " + userAccessToken, map);
        call2.enqueue(new Callback<EmergencyViewModel>() {
            @SuppressLint({"SetTextI18n", "LongLogTag"})
            @Override
            public void onResponse(@NonNull Call<EmergencyViewModel> call, @NonNull Response<EmergencyViewModel> response) {
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("customer emergency Contact listed successfully.")) {

                        hideProgressDialog();
                        List<EmergencyViewDataModel> getve = Objects.requireNonNull(response.body()).getEmergencyViewDataModel();
                        Log.e(TAG, "onResponse: " + getve.size());

                        for (int i = 0; i < getve.size(); i++) {
                            mobileNumbers.add(getve.get(i).getPhoneNumber().replaceAll(" ", ""));
                        }

                        String locmessage;
                        if (locFusedLocation == null) {
                            locmessage = " HELP!";
                        } else {
                            locmessage = "https://maps.google.com/maps?q=loc:" + locFusedLocation.getLatitude() + "," + locFusedLocation.getLongitude();
                        }

                        String message = smsTemplateContent + " " + locmessage;

                        callPanicAddAdmin(message);

                        Log.i(TAG, "callViewEmergencyDetails: ~~~~~~~~1~~~~" + mobileNumbers);
                        Log.i(TAG, "callViewEmergencyDetails: ~~~~~~~~2~~~~" + String.valueOf(mobileNumbers).replaceAll("\\[", "").replaceAll("\\]", ""));

                        Intent smsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("smsto:" + String.valueOf(mobileNumbers).replaceAll("\\[", "").replaceAll("\\]", "")));
                        smsIntent.putExtra("sms_body", message);
                        startActivity(smsIntent);

                    } else if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("No records Found.")) {
                        hideProgressDialog();
                        Utils.switchContent(R.id.frame_mainactivity_jobinprogress,
                                Utils.FRAGMENT_EMERGENCY_CONTACTS,
                                MainJobProgressActivity.this, null,
                                Utils.AnimationType.SLIDE_IN_SLIDE_OUT);
                    } else {

                    }
                    hideProgressDialog();
                } else if (response.code() == RESPONSECODE_401) {

                }
            }

            @Override
            public void onFailure(@NonNull Call<EmergencyViewModel> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: ~~~~~~~EmergencyViewModel~~~~~~~~~~" + t.toString());
                hideProgressDialog();
                call.cancel();
            }
        });

    }


    private void callAppSettings() {


        Call<AppSettingWebModel> call2 = apiInterface.doGetAppSettings();
        call2.enqueue(new Callback<AppSettingWebModel>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(@NonNull Call<AppSettingWebModel> call, @NonNull Response<AppSettingWebModel> response) {
                hideProgressDialog();
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {

                    String strDistanceUnit = null; //miles
                    if (response.body() != null) {
                        strDistanceUnit = response.body().getAppSetting().getDistanceUnit();
                    }

                    PAYPAL_TYPE = Objects.requireNonNull(response.body()).getAppSetting().getPaypalType().toString();
                    CONFIG_CLIENT_ID = Objects.requireNonNull(response.body()).getAppSetting().getPaypalClientId().toString();
                    CONFIG_SECRET_ID = Objects.requireNonNull(response.body()).getAppSetting().getPaypalClientSecret().toString();
                    PAYPAL_PRIVACY_URL = Objects.requireNonNull(response.body()).getAppSetting().getPaypalPrivacyUrl().toString();
                    PAYPAL_LEGAL_URL = Objects.requireNonNull(response.body()).getAppSetting().getPaypalLegalUrl().toString();

                    String STRIPE_PUB_KEY = Objects.requireNonNull(response.body()).getAppSetting().getPaymentPublisherKey();
                    String STRIPE_SECRET_KEY = Objects.requireNonNull(response.body()).getAppSetting().getPaymentSecretKey();
                    //String cashOnHandStatus = String.valueOf(Objects.requireNonNull(response.body()).getAppSetting().getCashIn());
                    String adminPaypalStatus = String.valueOf(Objects.requireNonNull(response.body()).getAppSetting().getPaypalStatus());

                    if (BuildConfig.DEBUG) {
                        Log.e(TAG, "onResponse: StripeKeys Admin" + STRIPE_PUB_KEY + "~~" + STRIPE_SECRET_KEY);
                        Log.e(TAG, "onResponse: Distance Limit" + response.body().getAppSetting().getDistanceLimit());
                    }

                    getState.putString(Utils.SP_COMMON_STRIPE_PUBLISH_KEY, STRIPE_PUB_KEY);
                    getState.putString(Utils.SP_COMMON_STRIPE_SECRET_KEY, STRIPE_SECRET_KEY);
                    getState.putString(Utils.SP_COMMON_DISTANCE_UNIT, strDistanceUnit);
                    //getState.putString(Utils.SP_COMMON_CASH_ON_HAND_STATUS, cashOnHandStatus);
                    getState.putString(Utils.SP_COMMON_ADMIN_PAYPAL_STATUS, adminPaypalStatus);
                    getState.apply();

                    List<AppSettingSmsTemplate> smsTemplates = Objects.requireNonNull(response.body()).getSmsTemplates();

                    for (int i = 0; i < smsTemplates.size(); i++) {
                        smsTemplateSubject = smsTemplates.get(i).getSmsSubject();
                        smsTemplateContent = smsTemplates.get(i).getSmsContent();
                        if (BuildConfig.DEBUG) {
                            Log.e("smsTemplates.get(i).getSmsSubject()~~", smsTemplates.get(i).getSmsSubject());
                            Log.e("smsTemplates.get(i).getSmsContent()~~", smsTemplates.get(i).getSmsContent());
                        }
                    }

                    // callPayPalIntegration();

                }
            }

            @Override
            public void onFailure(@NonNull Call<AppSettingWebModel> call, @NonNull Throwable t) {
                call.cancel();
            }
        });
    }

    /*Paypal Code Details*/
    String CONFIG_ENVIRONMENT_PAYPAL, PAYPAL_TYPE, CONFIG_CLIENT_ID, PAYPAL_LEGAL_URL, PAYPAL_PRIVACY_URL, CONFIG_SECRET_ID;
    CoreConfig coreConfig;
    Environment environment;
    PayPalNativeShippingListener payPalNativeShippingListener;
    PayPalNativeCheckoutListener payPalNativeCheckoutListener;
    PayPalNativeCheckoutClient paypalNativeClient;
    PayPalDataCollector payPalDataCollector;

//    private void callPayPalIntegration() {
//
//        System.out.println("~~~~callPayPalIntegration~~Inside~~~");
//        /*if (PAYPAL_TYPE != null && !PAYPAL_TYPE.equals("null") && !PAYPAL_TYPE.equals("")) {
//            if (PAYPAL_TYPE.equals("1") && PAYPAL_TYPE.equals("1.0")) {
//                CONFIG_ENVIRONMENT_PAYPAL = PayPalConfiguration.ENVIRONMENT_PRODUCTION;
//            }
//        } else {
//            CONFIG_ENVIRONMENT_PAYPAL = PayPalConfiguration.ENVIRONMENT_SANDBOX;
//        }*/
//
//        if (CONFIG_CLIENT_ID == null || CONFIG_CLIENT_ID.equals("null") || CONFIG_CLIENT_ID.equals("")) {
//            CONFIG_CLIENT_ID = Utils.PAYPAL_CLIENTID;
//        }
//        if (PAYPAL_PRIVACY_URL == null || PAYPAL_PRIVACY_URL.equals("null") || PAYPAL_PRIVACY_URL.equals("")) {
//            PAYPAL_PRIVACY_URL = getResources().getString(R.string.paypal_privacy);
//        }
//        if (PAYPAL_LEGAL_URL == null || PAYPAL_LEGAL_URL.equals("null") || PAYPAL_LEGAL_URL.equals("")) {
//            PAYPAL_LEGAL_URL = getResources().getString(R.string.paypal_legal);
//        }
//
//        System.out.println("value of client id====>>" + CONFIG_CLIENT_ID);
//        //  System.out.println("value of CONFIG_ENVIRONMENT_PAYPAL====>>" + CONFIG_ENVIRONMENT_PAYPAL);
//
//        /* PayPalConfiguration config = new PayPalConfiguration()
//                .environment(CONFIG_ENVIRONMENT_PAYPAL)
//                .clientId(CONFIG_CLIENT_ID)
//                .acceptCreditCards(true)
//                .merchantName(getString(R.string.app_name))
//                .merchantPrivacyPolicyUri(Uri.parse(PAYPAL_PRIVACY_URL))
//                .merchantUserAgreementUri(Uri.parse(PAYPAL_LEGAL_URL));
//
//        Intent intent = new Intent(requireActivity, PayPalService.class);
//        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
//        startService(intent);*/
//
//
//        if (PAYPAL_TYPE != null && !PAYPAL_TYPE.equals("null") && !PAYPAL_TYPE.equals("")) {
//            if (PAYPAL_TYPE.equals("1") || PAYPAL_TYPE.equals("1.0")) {
//                environment = Environment.LIVE;
//            } else {
//                environment = Environment.SANDBOX;
//            }
//        } else {
//            environment = Environment.SANDBOX;
//        }
//
//        CoreConfig coreConfig = new CoreConfig(CONFIG_CLIENT_ID, environment);
//
//        paypalNativeClient = new PayPalNativeCheckoutClient(
//                (Application) requireActivity.getApplicationContext(), coreConfig, Utils.PAYPAL_RETURN_URL);
//        payPalDataCollector = new PayPalDataCollector(coreConfig);
//
//        payPalNativeShippingListener = new PayPalNativeShippingListener() {
//            @Override
//            public void onPayPalNativeShippingAddressChange(@NotNull PayPalNativePaysheetActions actions,
//                                                            @NotNull PayPalNativeShippingAddress payPalNativeShippingAddress) {
//                System.out.println("~~~~onPayPalNativeShippingAddressChange``~~");
//                actions.approve();
//                hideProgressDialog();
//            }
//
//            @Override
//            public void onPayPalNativeShippingMethodChange(@NotNull PayPalNativePaysheetActions actions, @NotNull PayPalNativeShippingMethod payPalNativeShippingMethod) {
//                // called when the user updates their chosen shipping method
//                // patch your order server-side with the updated shipping amount.
//                // Once complete, call `actions.approve()` or `actions.reject()`
//                System.out.println("~~~~onPayPalNativeShippingMethodChange``~~");
//                try {
//                    // TODO: patch order on server side, notify SDK of success by calling actions.approve()
//                    actions.approve();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    // catch any errors from patching the order e.g. network unavailable
//                    // and notify SDK that the update was unsuccessful
//                    actions.reject();
//                }
//
//                hideProgressDialog();
//            }
//        };
//
//        payPalNativeCheckoutListener = new PayPalNativeCheckoutListener() {
//            @Override
//            public void onPayPalCheckoutStart() {
//                // the PayPal paysheet is about to show up
//                System.out.println("~~~~onPayPalCheckoutStart~~~~~`");
//                callClickAction(btnPayPalPayment, true);
//            }
//
//            @Override
//            public void onPayPalCheckoutSuccess(@NotNull PayPalNativeCheckoutResult payPalNativeCheckoutResult) {
//                // order was approved and is ready to be captured/authorized
//                System.out.println("~~~~payPalNativeCheckoutResult~~~~~`" + payPalNativeCheckoutResult.getOrderId());
//                System.out.println("~~~~payPalNativeCheckoutResult~~~~~`" + payPalNativeCheckoutResult.getPayerId());
//                callPayPalPaymentAmount(payPalNativeCheckoutResult.getOrderId());
//            }
//
//            @Override
//            public void onPayPalCheckoutFailure(@NotNull PayPalSDKError payPalSDKError) {
//                // handle the error
//                System.out.println("~~~~payPalSDKError~~~~~`" + payPalSDKError);
//                hideProgressDialog();
//                callClickAction(btnPayPalPayment, true);
//                Toast.makeText(requireActivity, payPalSDKError.toString(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onPayPalCheckoutCanceled() {
//                // the user canceled the flow
//                System.out.println("~~~~onPayPalCheckoutCanceled~~~~~`");
//                hideProgressDialog();
//                callClickAction(btnPayPalPayment, true);
//                Toast.makeText(requireActivity, getResources().getString(R.string.paypal_cancelled),
//                        Toast.LENGTH_SHORT).show();
//            }
//        };
//
//        paypalNativeClient.setShippingListener(payPalNativeShippingListener);
//        paypalNativeClient.setListener(payPalNativeCheckoutListener);
//
//    }


    private void callPanicAddAdmin(String message) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("customer_id", userId);
        map.put("mode", "customer");
        map.put("location_link", message);
        showProgressDialog(activity);
        Log.i("callPanicAddAdmin", "callPanicAddAdmin~~~" + map);

        Call<PanicAddAdminModel> call2 = apiInterface.doUpdateAddPanic(map);
        call2.enqueue(new Callback<PanicAddAdminModel>() {
            @Override
            public void onResponse(@NonNull Call<PanicAddAdminModel> call, @NonNull Response<PanicAddAdminModel> response) {
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Panic Call details updated successfully.")) {
                        hideProgressDialog();
                    } else {
                        hideProgressDialog();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<PanicAddAdminModel> call, @NonNull Throwable t) {
                call.cancel();
                hideProgressDialog();
            }
        });
    }


    //get wallet amount & rating in firebase
    private void getDriverWalletAmount() {
        if (driverWalletDatabaseBRef != null) {
            driverWalletDatabaseBRef.removeEventListener(driverWalletValueEventListener);
        }
        driverWalletDatabaseBRef = FirebaseDatabase.getInstance().getReference().child(Utils.FIREBASE_PROVIDERS_HEADER).child(strdriverIdFB);
        driverWalletValueEventListener = driverWalletDatabaseBRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    Object rating = dataSnapshot.child("rating").getValue();
                    if (rating != null) {
                        ratingbarJobinprogress.setRating(Float.parseFloat(rating.toString()));
                    } else {
                        ratingbarJobinprogress.setRating(0);
                    }
                    if (driverWalletDatabaseBRef != null) {
                        driverWalletDatabaseBRef.removeEventListener(driverWalletValueEventListener);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: ~~~" + databaseError.toString());
            }
        });
    }

    DatabaseReference driverDirectoinDbRef;
    ValueEventListener driverDirectionValEvent;
    String drivBearing = "0";
    float getBearing = 0.0f;
    LatLng DriverCurrentLatLng;
    String strGroupDriverIdFB = "";
    private final Map<String, List<MarkerInfo>> markerInfoMap = new HashMap<>();
    private final Map<Marker, Float> markerRotationMap = new HashMap<>(); // To keep track of marker rotations

    public void callGoogleDirection(String key) {

        try {

            if (markerCurrentLocationMan == null) {
                if (strPickupLatFB != null) {
                    markerCurrentLocationMan = mMap.addMarker(new MarkerOptions().position(new
                            LatLng(Double.parseDouble(strPickupLatFB), Double.parseDouble(strPickupLngFB))).icon(bitmapDescriptorUser));
                }
            }

            //onDirectionSuccessPlaceMarker(null, false, key, getBearing);

            if (directionPolyline != null) {
                Log.i(TAG, "ondirection: polyline change");
                directionPolyline.remove();
            }

            for (Marker marker : this.markers.values()) {
                marker.remove();
            }
            this.markers.clear();

            if (!strGroupDriverIdFB.equals("") && !strGroupDriverIdFB.equals("0")) {
                // Check if the string contains a comma
                if (strGroupDriverIdFB.contains(",")) {
                    // String contains a comma
                    // Do something if it contains a comma
                    System.out.println("String contains a comma");

                    // Split the string by comma to separate numbers
                    String[] numbers = strGroupDriverIdFB.split(",");

                    for (int i = 0; i < numbers.length; i++) {
                        // Perform some operation on each character
                        String strdriverIdFB = numbers[i];

                        // Example: Convert the string to an integer
                        int intValue = Integer.parseInt(strdriverIdFB);

                        // Print the result or perform other operations
                        System.out.println("Number at index -->" + i + ": " + intValue);
                        System.out.println("Number at index ==>" + i + ": " + strdriverIdFB);

                        callDirectionPolyline(strdriverIdFB, key);
                    }

                } else {
                    // String does not contain a comma
                    // Do something if it does not contain a comma
                    System.out.println("String does not contain a comma");

                    callDirectionPolyline(strGroupDriverIdFB, key);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        /*if (driverDirectoinDbRef != null) {
            driverDirectoinDbRef.removeEventListener(driverDirectionValEvent);
        }
        driverDirectoinDbRef = FirebaseDatabase.getInstance().getReference().child("direction").child(strdriverIdFB);
        driverDirectionValEvent = driverDirectoinDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("direction").getValue() != null) {
                    if (!dataSnapshot.child("direction").getValue().toString().equals("")) {
                        String json = dataSnapshot.child("direction").getValue().toString();
                        Gson gson = new Gson();
                        Direction direction = gson.fromJson(json, Direction.class);

                        callGoogleDirectionNew(direction, key);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: ~~~~~" + databaseError.toString());
            }
        });*/
    }

    private void callDirectionPolyline(String strGroupDriverIdFB, String key) {


        DriverLocDatabaseRef = FirebaseDatabase.getInstance().getReference().child("drivers_location")
                .child(strGroupDriverIdFB);
        DriverLocValueEventListener = DriverLocDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                List<MarkerInfo> markerInfoList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    if (dataSnapshot.child("bearing").getValue() != null)
                        drivBearing = Objects.requireNonNull(dataSnapshot.child("bearing").getValue()).toString(); //get bearing child
                    else
                        drivBearing = "0";

                    String status = Objects.requireNonNull(dataSnapshot.child("l").getValue()).toString(); //get location child
                    System.out.println("Status==>" + status);

                    if (isFloat(drivBearing)) {
                        getBearing = Float.parseFloat(drivBearing);
                    } else {
                        getBearing = (float) Integer.parseInt(drivBearing);
                    }

                    String[] lat1ong = status.split(",");

                    System.out.println("length of the latlong==>" + lat1ong.length);

                    String latitude = lat1ong[0];
                    String longitude = lat1ong[1];

                    String latreplace = latitude.replaceAll("\\[", "");
                    String longreplace = longitude.replaceAll("\\]", "");

                    double templat = Double.parseDouble(latreplace);
                    double templng = Double.parseDouble(longreplace);

                    DriverCurrentLatLng = new LatLng(templat, templng);
                    DriverCurrentBearing = getBearing;


                    if (BuildConfig.DEBUG) {
                        Log.i(TAG, "onDataChange: drivercurrent latlng--->" + DriverCurrentLatLng + "~~~Status~~~");
                        Log.i(TAG, "onDataChange in onkeyentered-->: " + DriverCurrentBearing);
                    }

                    // Retrieve latitude and longitude from Firebase snapshot
                    //double latitude = snapshot.child("latitude").getValue(Double.class);
                    //double longitude = snapshot.child("longitude").getValue(Double.class);
                    // float rotation = snapshot.child("bearing").getValue(Float.class); // Added rotation value
                    // LatLng position = new LatLng(latitude, longitude);

                    MarkerInfo markerInfo = new MarkerInfo(DriverCurrentLatLng, DriverCurrentBearing);
                    markerInfoList.add(markerInfo);

                    //callCalculateETA(DriverCurrentLatLng);

                }

                // Update marker info map
                markerInfoMap.put(strGroupDriverIdFB, markerInfoList);

                // Update markers on the map
                updateMarkersOnMap(key);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                out.println("~~onCancelled~~~");
            }
        });


    }

    private void callPolylineUpdate(String strGroupDriverIdFB, String key) {
        if (driverDirectoinDbRef != null) {
            driverDirectoinDbRef.removeEventListener(driverDirectionValEvent);
        }
        driverDirectoinDbRef = FirebaseDatabase.getInstance().getReference().child("direction")
                .child(strGroupDriverIdFB);
        driverDirectionValEvent = driverDirectoinDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("direction").getValue() != null) {

                    if (!dataSnapshot.child("direction").getValue().toString().equals("")) {

                        String json = dataSnapshot.child("direction").getValue().toString();
                        Gson gson = new Gson();
                        Direction direction = gson.fromJson(json, Direction.class);

                        switch (key) {
                            case "2":
                            case "3":
                            case "4":
                            case "5":
                                if (strPickupLatFB != null || strPickupLngFB != null ||
                                        !strPickupLatFB.equals("null") || !strPickupLngFB.equals("null")) {
                                    try {
                                        if (direction != null) {
                                            if (direction.isOK()) {
                                                Log.i(TAG, "onDirectionSuccess: isOK");
                                                Route route = direction.getRouteList().get(0);
                                                // onDirectionSuccessPlaceMarker(direction, true, key, getBearing);
                                                ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
                                                switch (key) {
                                                    case "2":
                                                    case "3":
                                                    case "4":
                                                        if (directionPolyline == null) {
                                                            // Create PolylineOptions to set color, width, etc.
                                                            PolylineOptions polylineOptions =
                                                                    DirectionConverter.createPolyline(activity,
                                                                            directionPositionList, 3,
                                                                            Utils.POLYLINE_COLOR_ACCEPTTRIP);
                                                            // Add polyline to the map
                                                            directionPolyline = mMap.addPolyline(polylineOptions);
                                                        } else {
                                                            // Update existing polyline with new points
                                                            directionPolyline.setPoints(directionPositionList);
                                                        }

                                                       /* directionPolyline = mMap.addPolyline(DirectionConverter.createPolyline(activity,
                                                                directionPositionList, 3,
                                                                Utils.POLYLINE_COLOR_ACCEPTTRIP));*/
                                                        break;
                                                }
                                            } else {
                                                Log.i(TAG, "onDirectionSuccess: notOK");
                                                //  onDirectionSuccessPlaceMarker(direction, false, key, getBearing);
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: ~~~~~" + databaseError.toString());
            }
        });
    }

    private void updateMarkersOnMap(String key) {

        mMap.clear(); // Clear previous markers

        // Add new markers to the map for each child node
        for (List<MarkerInfo> markerInfoList : markerInfoMap.values()) {
            for (MarkerInfo markerInfo : markerInfoList) {
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(markerInfo.getPosition())
                        .rotation(markerInfo.getRotation()); // Set rotation for the marker
                Marker marker = mMap.addMarker(markerOptions.icon(bitmapDescriptor).flat(true));
                markerRotationMap.put(marker, markerInfo.getRotation()); // Store marker rotation
                //callPolylineUpdate_(markerInfo.getPosition());

                if (com.instaguard.user.BuildConfig.DEBUG) {
                    if (!callCalculateETA(markerInfo.getPosition()).isEmpty()) {
                        out.println("~~~~~getPosition~~~~?>>" + markerInfo.getPosition() + "~~~callCalculateETA~>>>" + callCalculateETA(markerInfo.getPosition()));
                    }
                }

                if (marker != null /*&& marker.isInfoWindowShown()*/) {
                    marker.hideInfoWindow();
                    marker.showInfoWindow();
                    if (!callCalculateETA(markerInfo.getPosition()).isEmpty()) {
                        marker.setTitle(callCalculateETA(markerInfo.getPosition()));
                    }
                }

            }
        }

        // Move camera to the position of the first marker to ensure all markers are visible
        /*for (List<MarkerInfo> markerInfoList : markerInfoMap.values()) {
                for (MarkerInfo markerInfo : markerInfoList) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerInfo.getPosition(), Utils.MAP_ONTRIP_ZOOM));
                    return; // Exit loop after moving camera to the first marker position
                }
            }*/

        // Animate marker rotation
        animateMarkerRotation();

        // callPolylineUpdate(strGroupDriverIdFB, key);
    }

    private String callCalculateETA(LatLng guardPosition) {
        if (strPickupLatFB != null) {
            LatLng strPickupLatLng = new LatLng(Double.parseDouble(strPickupLatFB), Double.parseDouble(strPickupLngFB));

            double distanceKm = CalculationByDistance(guardPosition, strPickupLatLng);

            out.println("~~~~~~callCalculateETA~~~distanceKm~~~~>>>" + distanceKm);

            String eta = callETA(distanceKm);

            out.println("~~~~~~callCalculateETA~~~eta~~~~>>>" + eta);

            if (!eta.equals("0")) {
                return eta;
            } else {
                return "Arrived";
            }
        } else {
            return "Arrived";
        }
    }

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {

        System.out.println("Lat--->" + StartP + "Long--->" + EndP);
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;

        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.parseInt(newFormat.format(valueResult));

        double meter = valueResult % 1000;
        int meterInDec = Integer.parseInt(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return DecimalUtils.round(valueResult, 2);
    }

    private String callETA(double distanceKm) {

        //int distanceKm = 10; // Distance in kilometers
        int speedKmh = 30;   // Speed in kilometers per hour

        // Calculate the arrival time in hours
        double arrivalTimeHours = (double) distanceKm / speedKmh;

        // Convert the arrival time to hours and minutes
        int arrivalHours = (int) arrivalTimeHours;  // Get the whole number part as hours
        int arrivalMinutes = (int) ((arrivalTimeHours - arrivalHours) * 60); // Convert the decimal part to minutes

        // Output the arrival time
        System.out.println("Arrival Time:-->> " + arrivalHours + " hours and " + arrivalMinutes + " minutes");

        String value_;
        String hrvalue_;
        String minvalue_;

        /*if (arrivalHours > 0) {
            if (Double.parseDouble(String.valueOf(arrivalHours)) > 0.0) {
                if (Double.parseDouble(String.valueOf(arrivalHours)) == 1.0) {
                    if (arrivalMinutes > 0) {
                        if (Double.parseDouble(String.valueOf(arrivalMinutes)) > 0.0) {
                            if (Double.parseDouble(String.valueOf(arrivalMinutes)) == 1.0) {
                                minvalue_ = arrivalMinutes + " min";
                            } else {
                                minvalue_ = arrivalMinutes + " mins";
                            }
                            hrvalue_ = arrivalHours + " hr" + minvalue_;
                            value_ = hrvalue_;
                        } else {
                            minvalue_ = arrivalMinutes + " mins";
                            hrvalue_ = arrivalHours + " hr";
                            value_ = hrvalue_;
                        }
                    } else {
                        hrvalue_ = arrivalHours + " hr";
                        value_ = hrvalue_;
                    }
                } else {
                    if (arrivalMinutes > 0) {
                        if (Double.parseDouble(String.valueOf(arrivalMinutes)) > 0.0) {
                            if (Double.parseDouble(String.valueOf(arrivalMinutes)) == 1.0) {
                                minvalue_ = arrivalMinutes + " min";
                            } else {
                                minvalue_ = arrivalMinutes + " mins";
                            }
                            hrvalue_ = arrivalHours + " hr" + minvalue_;
                            value_ = hrvalue_;
                        } else {
                            minvalue_ = arrivalMinutes + " mins";
                            hrvalue_ = arrivalHours + " hr";
                            value_ = hrvalue_;
                        }
                    } else {
                        hrvalue_ = arrivalHours + " hr";
                        value_ = hrvalue_;
                    }
                }
            } else {
                if (arrivalMinutes > 0) {
                    if (Double.parseDouble(String.valueOf(arrivalMinutes)) > 0.0) {
                        if (Double.parseDouble(String.valueOf(arrivalMinutes)) == 1.0) {
                            minvalue_ = arrivalMinutes + " min";
                        } else {
                            minvalue_ = arrivalMinutes + " mins";
                        }
                    } else {
                        minvalue_ = arrivalMinutes + " mins";
                    }
                    value_ = minvalue_;
                } else {
                    minvalue_ = "0 min";
                    value_ = minvalue_;
                }
            }
        } else {
            hrvalue_ = "0 hr";
            minvalue_ = "0 min";
            value_ = "0";
        }*/

        //return String.valueOf(arrivalMinutes);
        if (arrivalHours > 0) {
            if (arrivalMinutes > 0) {
                value_ = arrivalHours + " hrs " + arrivalMinutes + " mins";
            } else {
                value_ = arrivalHours + " hrs";
            }
        } else {
            if (arrivalMinutes > 0) {
                value_ = arrivalMinutes + " mins";
            } else {
                value_ = "Arrived";
            }
        }
        return value_;
    }

    private void animateMarkerRotation() {
        for (final Marker marker : markerRotationMap.keySet()) {
            final float currentRotation = marker.getRotation();
            final float targetRotation = markerRotationMap.get(marker);
            ValueAnimator animator = ValueAnimator.ofFloat(currentRotation, targetRotation);
            animator.setDuration(1000); // Animation duration in milliseconds
            animator.addUpdateListener(animation -> {
                float animatedValue = (float) animation.getAnimatedValue();
                marker.setRotation(animatedValue);
            });
            animator.start();
        }
    }

    // Custom class to hold marker information
    private static class MarkerInfo {
        private final LatLng position;
        private final float rotation;

        public MarkerInfo(LatLng latLng, float rotation) {
            this.position = latLng;
            this.rotation = rotation;
        }

        public LatLng getPosition() {
            return position;
        }

        public float getRotation() {
            return rotation;
        }
    }

    List<LatLng> alLatLng = new ArrayList<>();

    private void callPolylineUpdate_(LatLng position) {
        try {
            if (!checkReady()) {
                return;
            }

            LatLng start = new LatLng(Double.parseDouble(strPickupLatFB), Double.parseDouble(strPickupLngFB));
            LatLng end = new LatLng(9.919033517164335, 78.10867445411951);

            LatLng start1 = new LatLng(9.916781557264631, 78.09853573563835);
            LatLng end1 = new LatLng(9.906019382081157, 78.11066918071194);
            // LatLng end = position;

            /*double cLat = ((start.latitude + end.latitude) / 2);
            double cLon = ((start.longitude + end.longitude) / 2);

            //add skew and arcHeight to move the midPoint
            if(Math.abs(start.longitude - end.longitude) < 0.0001){
                cLon -= 0.0195;
            } else {
                cLat += 0.0195;
            }

            double tDelta = 1.0/50;
            for (double t = 0; t <= 1.0; t+=tDelta) {
                double oneMinusT = (1.0-t);
                double t2 = Math.pow(t, 2);
                double lon = oneMinusT * oneMinusT * start.longitude
                        + 2 * oneMinusT * t * cLon
                        + t2 * end.longitude;
                double lat = oneMinusT * oneMinusT * start.latitude
                        + 2 * oneMinusT * t * cLat
                        + t2 * end.latitude;

                alLatLng.add(new LatLng(lat, lon));
            }*/

            /*out.println("~~~~~~~alLatLng~~~~~~~~"+alLatLng.size());
            if (alLatLng != null) {
                // draw polyline
                PolylineOptions line = new PolylineOptions();
                line.width(5f);
                line.color(Color.RED);
                line.addAll(alLatLng);
                mMap.addPolyline(line);
            }*/

            List<LatLng> mergedList = new ArrayList<>();
            mergedList.add(start);
            mergedList.add(end);
            mergedList.add(start1);
            mergedList.add(end1);

            MapAnimator.getInstance().animateRoute(mMap, mergedList);

            //MapUtils.showCurvedPolyline(mMap, new LatLng(Double.parseDouble(strPickupLatFB), Double.parseDouble(strPickupLngFB)),position, 0.5, Utils.POLYLINE_COLOR_ACCEPTTRIP);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDirectionSuccessPlaceMarker(Direction direction, Boolean isOK, String key, Float getBearing) {

        Log.e(TAG, "onDirectionSuccess: -----strEstDistance------------strEstDuration--------------->>>>>>"
                + strEstDistance + "----" + strEstDuration);

        /*curPos = new LatLng(DriverCurrentLatLng.latitude, DriverCurrentLatLng.longitude);
        if (driverAcceptCarMarker != null) {
            Log.i(TAG, "onDirectionSuccessPlaceMarker: driverAcceptCarMarker not null-->" + driverAcceptCarMarker);
            driverAcceptCarMarker.remove();
            driverAcceptCarMarker = mMap.addMarker(new MarkerOptions().position(curPos).
                    title(strEstDistance + " , " + strEstDuration).icon(bitmapDescriptor).flat(true));
            driverAcceptCarMarker.setAnchor(0.5f, 0.5f);
            driverAcceptCarMarker.setRotation(DriverCurrentBearing);
        }

        if (driverAcceptCarMarker == null) {
            Log.i(TAG, "onDirectionSuccessPlaceMarker: marker placed if");
            driverAcceptCarMarker = mMap.addMarker(new MarkerOptions().position(DriverCurrentLatLng).
                    title(strEstDistance + " , " + strEstDuration).icon(bitmapDescriptor).flat(true));
            driverAcceptCarMarker.setAnchor(0.5f, 0.5f);
            driverAcceptCarMarker.setRotation(DriverCurrentBearing);

        } else {
            Log.i(TAG, "onDirectionSuccessPlaceMarker: marker placed else->" + prevPos);
            Log.i(TAG, "onDirectionSuccessPlaceMarker: marker placed else-->" + curPos);
            if (!prevPos.equals(new LatLng(0, 0))) {
                if (!prevPos.equals(curPos)) {

                    double[] startValues = new double[]{prevPos.latitude, prevPos.longitude};
                    double[] endValues = new double[]{curPos.latitude, curPos.longitude};
                    baseActivity.animateMarkerWithRotation(driverAcceptCarMarker, getBearing, startValues, endValues);

                } else {
                    driverAcceptCarMarker.setRotation(DriverCurrentBearing);
                }
            } else {
                Log.i(TAG, "onDirectionSuccessPlaceMarker: marker placed else else");
                driverAcceptCarMarker.setPosition(curPos);
                driverAcceptCarMarker.setRotation(DriverCurrentBearing);
            }
            prevPos = new LatLng(curPos.latitude, curPos.longitude);
        }*/

        if (markerCurrentLocationMan != null)
            markerCurrentLocationMan.remove();

        switch (key) {
            case "2":
            case "4":
            case "3":
            case "5":
                if (!strDropLocationFB.equals("NoDrop")) {
                    if (driverAcceptCarMarker != null) {
                        driverAcceptCarMarker.showInfoWindow();
                    }
                } else {
                    Log.e(TAG, "onDirectionSuccessPlaceMarker: Distance NO``");
                }

                if (markerPickupDropAccept != null)
                    markerPickupDropAccept.remove();
                markerCurrentLocationMan = mMap.addMarker(new MarkerOptions().position(new
                        LatLng(Double.parseDouble(strPickupLatFB), Double.parseDouble(strPickupLngFB))).icon(bitmapDescriptorUser));

                break;
            case "5.1":
            case "5.3":

                if (!strDropLocationFB.equals("NoDrop")) {
                    if (driverAcceptCarMarker != null) {
                        driverAcceptCarMarker.showInfoWindow();
                    }
                } else {
                    Log.e(TAG, "onDirectionSuccessPlaceMarker: Distance NO``");
                }

                if (AcceptDropLatLng != null) {
                    if (markerPickupDropAccept != null)
                        markerPickupDropAccept.remove();

                    markerCurrentLocationMan = mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(strPickupLatFB), Double.parseDouble(strPickupLngFB))).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_green_pin)));

                    markerPickupDropAccept = mMap.addMarker(new MarkerOptions().position(AcceptDropLatLng).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_red_pin)));
                } else {
                    Toast.makeText(this, R.string.map_drop_location_not_mentioned, Toast.LENGTH_SHORT).show();
                }

                break;
        }

        if (isOK) {
            List<Leg> leg = direction.getRouteList().get(0).getLegList();
            List<Step> step = leg.get(0).getStepList();
            List<Leg> legDistance = direction.getRouteList().get(0).getLegList();

            System.out.println("distance==>>" + legDistance.get(0).getDistance().getText());
            System.out.println("duration==>>" + legDistance.get(0).getDuration().getText());
            System.out.println("distance==1>>" + step.get(0).getDistance().getText());
            System.out.println("duration==1>>" + step.get(0).getDuration().getText());

            String label = "";
            if (legDistance.get(0).getDistance().getText() != null
                    && !legDistance.get(0).getDistance().getText().equals("null")
                    && !legDistance.get(0).getDistance().getText().equals("")) {
                label = getResources().getString(R.string.est_distance) + "-"
                        + legDistance.get(0).getDistance().getText()
                        + " & " + getResources().getString(R.string.est_duration) + "-"
                        + legDistance.get(0).getDuration().getText();
            }

            txtEstDistDurationJobinprogress.setText(label);

            if (!strDropLocationFB.equals("NoDrop")) {
                if (driverAcceptCarMarker != null /*&& !driverAcceptCarMarker.isCluster()*/) {
                    // driverAcceptCarMarker.showInfoWindow();
                    if (driverAcceptCarMarker.isInfoWindowShown()) {
                        driverAcceptCarMarker.hideInfoWindow();
                        driverAcceptCarMarker.showInfoWindow();
                    }
                }
            } else {
                Log.e(TAG, "onDirectionSuccessPlaceMarker: Distance NO``");
            }
        }

    }

    public void callGoogleDirectionNew(Direction direction, String key) {

        if (strdriverIdFB != null) {
            if (DriverLocDatabaseRef != null) {
                DriverLocDatabaseRef.removeEventListener(DriverLocValueEventListener);
            }
            DriverLocDatabaseRef = FirebaseDatabase.getInstance().getReference().child("drivers_location").child(strdriverIdFB);
            DriverLocValueEventListener = DriverLocDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {

                        String drivBearing;
                        if (dataSnapshot.child("bearing").getValue() != null)
                            drivBearing = dataSnapshot.child("bearing").getValue().toString(); //get bearing child
                        else
                            drivBearing = "0";

                        String status = dataSnapshot.child("l").getValue().toString(); //get location child
                        System.out.println("Status==>" + status);
                        float getBearing = 0.0f;
                        if (drivBearing != null) {
                            if (isFloat(drivBearing)) {
                                getBearing = Float.parseFloat(drivBearing);
                            } else {
                                getBearing = (float) Integer.parseInt(drivBearing);
                            }
                        }
                        String[] lat1ong = status.split(",");

                        System.out.println("length of the latlong==>" + lat1ong.length);

                        String latitude = lat1ong[0];
                        String longitude = lat1ong[1];

                        String latreplace = latitude.replaceAll("\\[", "");
                        String longreplace = longitude.replaceAll("\\]", "");

                        double templat = Double.parseDouble(latreplace);
                        double templng = Double.parseDouble(longreplace);

                        DriverCurrentLatLng = new LatLng(templat, templng);
                        DriverCurrentBearing = getBearing;
                        if (BuildConfig.DEBUG)
                            Log.i(TAG, "onDataChange: drivercurrent latlng--->" + DriverCurrentLatLng + "~~~Status~~~" + key);

                        switch (key) {
                            case "2":
                            case "3":
                            case "4":
                                if (strPickupLatFB != null || strPickupLngFB != null ||
                                        !strPickupLatFB.equals("null") || !strPickupLngFB.equals("null")) {
                                    try {
                                        if (directionPolyline != null) {
                                            Log.i(TAG, "onDataChange: Accepttrip directionPolyline");
                                            directionPolyline.remove();
                                        }
                                        if (direction != null) {
                                            if (direction.isOK()) {
                                                Log.i(TAG, "onDirectionSuccess: isOK");
                                                Route route = direction.getRouteList().get(0);
                                                onDirectionSuccessPlaceMarker(direction, true, key, getBearing);

                                                if (directionPolyline != null) {
                                                    Log.i(TAG, "ondirection: polyline change");
                                                    directionPolyline.remove();
                                                }

                                                ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
                                                switch (key) {
                                                    case "2":
                                                    case "3":
                                                    case "4":
                                                        directionPolyline = mMap.addPolyline(DirectionConverter.createPolyline(activity, directionPositionList, 3, Utils.POLYLINE_COLOR_ACCEPTTRIP));
                                                        break;
                                                }

                                            } else {
                                                Log.i(TAG, "onDirectionSuccess: notOK");
                                                onDirectionSuccessPlaceMarker(direction, false, key, getBearing);
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                break;
                            case "5":
                            case "5.1":
                            case "5.3":
                                if (AcceptDropLatLng.latitude != 0.0 || AcceptDropLatLng.longitude != 0.0) {
                                    try {
                                        if (directionPolyline != null) {
                                            Log.i(TAG, "onDataChange: Starttrip directionPolyline if ");
                                            directionPolyline.remove();
                                        }
                                        if (direction != null) {
                                            if (direction.isOK()) {
                                                Log.i(TAG, "onDirectionSuccess: isOK");
                                                Route route = direction.getRouteList().get(0);
                                                onDirectionSuccessPlaceMarker(direction, true, key, getBearing);

                                                if (directionPolyline != null) {
                                                    Log.i(TAG, "ondirection: polyline change");
                                                    directionPolyline.remove();
                                                }

                                                ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
                                                directionPolyline = mMap.addPolyline(DirectionConverter.createPolyline(activity, directionPositionList, 3, Utils.POLYLINE_COLOR_STARTTRIP));

                                            } else {
                                                Log.i(TAG, "onDirectionSuccess: notOK");
                                                onDirectionSuccessPlaceMarker(direction, false, key, getBearing);
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Log.i(TAG, "onDataChange: drop location not set");
                                }

                                break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e(TAG, "onCancelled: " + databaseError.toString());
                }
            });
        }
    }

    boolean isFloat(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    private void callSlideBannerImage(String key) {

        userAccessToken = spCommon.getString(Utils.SP_USER_ACCESSTOKEN, null);
        HashMap<String, Object> map = new HashMap<>();
        map.put("mode", "all");
        Log.i("callSlideBannerImage", "~~~~" + map);

        Call<BannerModel> call = apiInterface.doSlideBannerImages("Bearer " + userAccessToken, map);
        call.enqueue(new Callback<BannerModel>() {

            @Override
            public void onResponse(@NonNull Call<BannerModel> call, @NonNull Response<BannerModel> response) {

                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {

                    if (response.body().getMessage().equals("Success")) {
                        Log.e("sdfs", response.code() + "");
                        BannerModel resource = response.body();
                        bannerData = response.body().getData();
                        Log.i("sfsdf", "onResponse: callBannerList-->" + bannerData.size());
                        if (bannerData != null) {
                            if (bannerData.size() != 0) {
                                try {
                                    initComponent(MainJobProgressActivity.this);
                                } catch (Exception e) {
                                    Log.i("sdff", "onResponse: exception--->" + e);
                                }
                            } else {
                                viewImageSlider.setVisibility(View.GONE);
                                rlEmptyviewAnnouncement.setVisibility(View.VISIBLE);
                            }
                        } else {
                            viewImageSlider.setVisibility(View.GONE);
                            rlEmptyviewAnnouncement.setVisibility(View.VISIBLE);
                        }
                    } else {
                        viewImageSlider.setVisibility(View.GONE);
                        rlEmptyviewAnnouncement.setVisibility(View.VISIBLE);
                    }
                } else {
                    viewImageSlider.setVisibility(View.GONE);
                    rlEmptyviewAnnouncement.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BannerModel> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.try_again, Toast.LENGTH_SHORT).show();
                viewImageSlider.setVisibility(View.GONE);
                rlEmptyviewAnnouncement.setVisibility(View.VISIBLE);
            }
        });
    }

    public String convertDistanceUnit() {

        String strDistanceUnit = spCommon.getString(Utils.SP_COMMON_DISTANCE_UNIT, null);
        String miles;
        if (strDistanceUnit != null) {
            if (!strDistanceUnit.equals("null")) {
                if (!strDistanceUnit.equals("")) {
                    if (!strDistanceUnit.equals(" ")) {
                        switch (strDistanceUnit) {
                            case "miles":
                            case "Miles":
                            case "mile":
                            case "Mile":
                                miles = "mile";
                                break;
                            case "KM":
                            case "km":
                            case "kilometers":
                            case "kilometer":
                            case "Kilometer":
                            case "Kilometers":
                            default:
                                miles = "km";
                                break;
                        }
                    } else {
                        miles = "km";
                    }
                } else {
                    miles = "km";
                }
            } else {
                miles = "km";
            }
        } else {
            miles = "km";
        }
        return miles;
    }


    public void currentLocationClickDriver() {

        Log.i(TAG, "currentLocationClick: " + locFusedLocation);
        if (DriverCurrentLatLng != null) {
            if (!DriverCurrentLatLng.equals(new LatLng(0, 0)) || !DriverCurrentLatLng.equals(new LatLng(0.0, 0.0))) {

                Log.i(TAG, "currentLocationClick: " + DriverCurrentLatLng.latitude + " " + DriverCurrentLatLng.longitude);

                LatLng latLng = new LatLng(DriverCurrentLatLng.latitude, DriverCurrentLatLng.longitude);
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng)                              // Sets the center of the map to current location
                        .zoom(Utils.MAP_DEFAULT_ZOOM)
                        .tilt(0)                                     // Sets the tilt of the camera to 0 degrees
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            } else {
                if (locFusedLocation != null) {
                    Log.i(TAG, "currentLocationClick: " + locFusedLocation.getLatitude() + " " + locFusedLocation.getLongitude());
                    LatLng latLng = new LatLng(locFusedLocation.getLatitude(), locFusedLocation.getLongitude());
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(latLng)                              // Sets the center of the map to current location
                            .zoom(Utils.MAP_DEFAULT_ZOOM)
                            .tilt(0)                                     // Sets the tilt of the camera to 0 degrees
                            .build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                } else {
                    Toast.makeText(this, getString(R.string.mainacativity_getting_location), Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            if (locFusedLocation != null) {
                Log.i(TAG, "currentLocationClick: " + locFusedLocation.getLatitude() + " " + locFusedLocation.getLongitude());
                LatLng latLng = new LatLng(locFusedLocation.getLatitude(), locFusedLocation.getLongitude());
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng)                              // Sets the center of the map to current location
                        .zoom(Utils.MAP_DEFAULT_ZOOM)
                        .tilt(0)                                     // Sets the tilt of the camera to 0 degrees
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            } else {
                Toast.makeText(this, getString(R.string.mainacativity_getting_location), Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        System.out.println("requestCode" + requestCode + "resultCode" + resultCode + "navigationDrawerData" + data);
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

/*
            case Utils.REQUEST_PAYPAL_PAYMENT: {
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        PaymentConfirmation confirm = null;
                        if (data != null) {
                            confirm = data
                                    .getParcelableExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                        }
                        Log.i("before Response", "before Response" + confirm);
                        if (confirm != null) {
                            try {
                                Log.i("Response", "Response" + confirm);
                                Log.i("paymentExample", confirm.toJSONObject().toString());
                                JSONObject jsonObj = new JSONObject(confirm.toJSONObject().toString());

                                String paymentId = jsonObj.getJSONObject("response").optString("id");
                                Log.i("payment id:-", "paymentId" + paymentId);

                                //callPayPalPaymentAmount(paymentId);

                            } catch (JSONException e) {
                                Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                            }
                        }
                        callClickAction(btnPayPalPayment, true);
                        break;

                    case Activity.RESULT_CANCELED:
                        Log.i("paymentExample", "The user canceled.");
                        callClickAction(btnPayPalPayment, true);
                        break;
                }
            }
            break;
*/

            default: {
                callClickAction(btnPayPalPayment, true);
                // ..
                System.out.println("else state");
                break;
            }
        }
    }

    private void callCreatePayPalPayment() {

        userAccessToken = spCommon.getString(Utils.SP_USER_ACCESSTOKEN, null);
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("booking_id", strBookingId);
        map1.put("type", "app_end"); // need only booking id
        map1.put("amount", strTotalAmountFB);

        Log.i("", "~callPayPalPaymentAmount~~~" + map1);

        showProgressDialog(activity);

        Call<PayPalCreateModel> call2 = apiInterface.doCreatePayPalPayment("Bearer " + userAccessToken, map1);
        call2.enqueue(new Callback<PayPalCreateModel>() {
            @Override
            public void onResponse(@NonNull Call<PayPalCreateModel> call, @NonNull Response<PayPalCreateModel> response) {
                Log.i(TAG, "onResponse: resonse code--> " + response.code());
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    hideProgressDialog();
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Order Created")) {

                        callClickAction(btnPayPalPayment, true);

                        try {

                            String id = response.body().getId();
                            //if (com.instaguard.user.BuildConfig.DEBUG){
                            System.out.println("~~~~PayPalNativeCheckoutRequest~~~id~~~" + id);
                            System.out.println("~~~~PayPalNativeCheckoutRequest~~~strCustomerEmailFB~~~" + strCustomerEmailFB);
                            //}
                            PayPalNativeCheckoutRequest request = new PayPalNativeCheckoutRequest(id, strCustomerEmailFB);
                            paypalNativeClient.startCheckout(request);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {

                        callClickAction(btnPayPalPayment, true);
                        Toast.makeText(activity, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onResponse: success");
                    }

                } else if (response.code() == RESPONSECODE_401) {
                    //  callRefreshTokenBalanceAmount(stripeTokenID, cardToSave);h
                    callClickAction(btnPayPalPayment, true);
                    Toast.makeText(activity, "Response Error 401", Toast.LENGTH_SHORT).show();

                } else if (response.code() == RESPONSECODE_500) {
                    callClickAction(btnPayPalPayment, true);
                    Toast.makeText(activity, "Response Error 500", Toast.LENGTH_SHORT).show();
                    hideProgressDialog();
                }
            }

            @Override
            public void onFailure(@NonNull Call<PayPalCreateModel> call, @NonNull Throwable t) {
                call.cancel();
                callClickAction(btnPayPalPayment, true);
                hideProgressDialog();
            }
        });
    }

    private void callPayPalPaymentAmount(String paypalId) {

        userAccessToken = spCommon.getString(Utils.SP_USER_ACCESSTOKEN, null);
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("booking_id", strBookingId);
        map1.put("amount", strTotalAmountFB);
        map1.put("payment_id", paypalId);

        Log.i("", "~callPayPalPaymentAmount~~~" + map1);

        // showProgressDialog(requireActivity);

        Call<BalanceWalletAddMoneyModel> call2 = apiInterface.doUpdatePayPalPayment("Bearer " + userAccessToken, map1);
        call2.enqueue(new Callback<BalanceWalletAddMoneyModel>() {
            @Override
            public void onResponse(@NonNull Call<BalanceWalletAddMoneyModel> call, @NonNull Response<BalanceWalletAddMoneyModel> response) {
                Log.i(TAG, "onResponse: resonse code--> " + response.code());
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    hideProgressDialog();
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Money debited successfully.")) {

                        callClickAction(btnPayPalPayment, true);

                        DatabaseReference refs = FirebaseDatabase.getInstance().getReference();
                        refs.child(Utils.FIREBASE_PROVIDERS_HEADER).child(strdriverIdFB).child("trips").child("paypal_status").setValue("1");
                        refs.child(Utils.FIREBASE_BOOKING_TRIPS_HEADER).child(strBookingId).child("paypal_status").setValue("1");

                        lytCardPayment.setVisibility(View.GONE);
                        txtWaitaMomentCardPayment.setVisibility(View.VISIBLE);

                    } else {

                        callClickAction(btnPayPalPayment, true);
                        Toast.makeText(activity, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onResponse: success");
                    }

                } else if (response.code() == RESPONSECODE_401) {
                    //  callRefreshTokenBalanceAmount(stripeTokenID, cardToSave);h
                    callClickAction(btnPayPalPayment, true);
                    Toast.makeText(activity, "Response Error 401", Toast.LENGTH_SHORT).show();

                } else if (response.code() == RESPONSECODE_500) {
                    callClickAction(btnPayPalPayment, true);
                    Toast.makeText(activity, "Response Error 500", Toast.LENGTH_SHORT).show();
                    hideProgressDialog();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BalanceWalletAddMoneyModel> call, @NonNull Throwable t) {
                call.cancel();
                callClickAction(btnPayPalPayment, true);
                hideProgressDialog();
            }
        });
    }

    private void callPaymentFinish_() {

        Log.i(TAG, "callPaymentFinish_: ->");

        userAccessToken = spCommon.getString(Utils.SP_USER_ACCESSTOKEN, null);
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("mode", "payment_success");
        map1.put("booking_id", strBookingId);
        map1.put("driver_id", strdriverIdFB);

        Log.i("callPaymentFinish_", "~~~~" + map1);

        showProgressDialog(activity);
        out.println("main job progress activity api call "+ uniqueIdList);

        Call<BalanceWalletAddMoneyModel> call2 = apiInterface.doPaymentFinish_("Bearer " + userAccessToken, map1);
        call2.enqueue(new Callback<BalanceWalletAddMoneyModel>() {
            @Override
            public void onResponse(@NonNull Call<BalanceWalletAddMoneyModel> call, @NonNull Response<BalanceWalletAddMoneyModel> response) {
                Log.i(TAG, "onResponse: resonse code--> " + response.code());
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    hideProgressDialog();
                    out.println("CompleteApiCall");
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Money debited successfully.")) {

                    } else {

                        Toast.makeText(activity, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onResponse: success");
                    }

                } else if (response.code() == RESPONSECODE_401) {
                    Toast.makeText(activity, "Response Error 401", Toast.LENGTH_SHORT).show();
                } else if (response.code() == RESPONSECODE_500) {
                    Toast.makeText(activity, "Response Error 500", Toast.LENGTH_SHORT).show();
                    hideProgressDialog();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BalanceWalletAddMoneyModel> call, @NonNull Throwable t) {
                call.cancel();
                hideProgressDialog();
            }
        });
    }


    //public FirebaseRecyclerAdapter<FirebaseGuardListModel, MainCategoryViewHolder> firebaseRecyclerAdapter;
    public static class MainCategoryViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView ivGuardProfPic_multi;
        CustomTextView txtCount_multi, txtGuardName_multi, txtOneMessage_multi, txtOneTime_multi,
                txtTwoMessage_multi, txtTwoTime_multi,
                txtThreeMessage_multi, txtThreeTime_multi,
                txtFourMessage_multi, txtFourTime_multi;
        SimpleRatingBar rbarGuardRating_multi;

        View llOneTripStatus_multi;
        View llTwoTripStatus_multi;
        View llThreeTripStatus_multi;
        View llFourTripStatus_multi;

        MainCategoryViewHolder(View view) {
            super(view);

            ivGuardProfPic_multi = view.findViewById(R.id.iv_user_prof_pic_job_inprogress);
            txtCount_multi = view.findViewById(R.id.iv_count);
            txtGuardName_multi = view.findViewById(R.id.txt_username_job_inprogress);
            rbarGuardRating_multi = view.findViewById(R.id.ratingbar_job_inprogress);

            llOneTripStatus_multi = view.findViewById(R.id.ll_trip_one);
            txtOneMessage_multi = view.findViewById(R.id.txt_accept_one);
            txtOneTime_multi = view.findViewById(R.id.txt_accept_time_one);

            llTwoTripStatus_multi = view.findViewById(R.id.ll_trip_two);
            txtTwoMessage_multi = view.findViewById(R.id.txt_accept_two);
            txtTwoTime_multi = view.findViewById(R.id.txt_accept_time_two);

            llThreeTripStatus_multi = view.findViewById(R.id.ll_trip_three);
            txtThreeMessage_multi = view.findViewById(R.id.txt_accept_three);
            txtThreeTime_multi = view.findViewById(R.id.txt_accept_time_three);

            llFourTripStatus_multi = view.findViewById(R.id.ll_trip_four);
            txtFourMessage_multi = view.findViewById(R.id.txt_accept_four);
            txtFourTime_multi = view.findViewById(R.id.txt_accept_time_four);
        }

        @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
        void setBlogPost(int position, FirebaseGuardListModel firebaseGuardListModel) {

            // String status = String.valueOf(categoryModel.getStatus());
            //String strBookingId = String.valueOf(categoryModel.getBookingId());

            String getDriver_first = String.valueOf(firebaseGuardListModel.getDriver_first());
            String getDriver_last = String.valueOf(firebaseGuardListModel.getDriver_last());
            String getTrip_status = String.valueOf(firebaseGuardListModel.getTrip_status());

            String getAccept_time = String.valueOf(firebaseGuardListModel.getAccept_time());
            String getStart_time = String.valueOf(firebaseGuardListModel.getStart_time());
            String getReach_time = String.valueOf(firebaseGuardListModel.getReach_time());
            String getService_start_time = String.valueOf(firebaseGuardListModel.getService_start_time());

            out.println("setBlogPost: ~~~~~getDriver_first~~~~" + getDriver_first);
            out.println("setBlogPost: ~~~~~getDriver_last~~~~" + getDriver_last);

            txtGuardName_multi.setText(getDriver_first + " " + getDriver_last);
            txtCount_multi.setText("" + (position + 1));

            switch (getTrip_status) {
                case "2": //accept
                    setVisibility(llOneTripStatus_multi, true);
                    setVisibility(llTwoTripStatus_multi, false);
                    setVisibility(llThreeTripStatus_multi, false);
                    setVisibility(llFourTripStatus_multi, false);

                    txtOneTime_multi.setText(getAccept_time);

                    break;
                case "3": //start
                    setVisibility(llOneTripStatus_multi, true);
                    setVisibility(llTwoTripStatus_multi, true);
                    setVisibility(llThreeTripStatus_multi, false);
                    setVisibility(llFourTripStatus_multi, false);

                    txtOneTime_multi.setText(getAccept_time);
                    txtTwoTime_multi.setText(getStart_time);

                    break;
                case "4": //arrivied
                    setVisibility(llOneTripStatus_multi, true);
                    setVisibility(llTwoTripStatus_multi, true);
                    setVisibility(llThreeTripStatus_multi, true);
                    setVisibility(llFourTripStatus_multi, false);

                    txtOneTime_multi.setText(getAccept_time);
                    txtTwoTime_multi.setText(getStart_time);
                    txtThreeTime_multi.setText(getReach_time);

                    break;
                case "5": //begin
                case "6": //end
                    setVisibility(llOneTripStatus_multi, true);
                    setVisibility(llTwoTripStatus_multi, true);
                    setVisibility(llThreeTripStatus_multi, true);
                    setVisibility(llFourTripStatus_multi, true);

                    txtOneTime_multi.setText(getAccept_time);
                    txtTwoTime_multi.setText(getStart_time);
                    txtThreeTime_multi.setText(getReach_time);
                    txtFourTime_multi.setText(getService_start_time);

                    break;
            }

        }

    }

    List<TripsFirebaseGuardListModel> tripsFirebaseGuardListModels = new ArrayList<>();
    List<TripsFirebaseGuardRatingListModel> tripsFirebaseGuardListModelsRating = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    // private void viewTimelineFBStatus(@NonNull Query dataSnapshot) {
    private void viewTimelineFBStatus(@NonNull DataSnapshot dataSnapshot) {

        Log.e(TAG, "viewTimelineFBStatus: query-------drivers--------dataSnapshot------>>>");
        Log.e(TAG, "onBindViewHolder: Inside firebase -------->>>>");

        if (tripsFirebaseGuardListModels.size() > 0) {
                out.println("finalState");
                tripsFirebaseGuardListModels.clear();
        }

      /*  if (tripsFirebaseGuardListModelsRating.size() > 0) {
                tripsFirebaseGuardListModelsRating.clear();
                out.println("ratingDataClearAndAdd");
        }*/

       /* for (int i = 0; i <= tripsFirebaseGuardListModelsRating.size()-1; i++) {
            out.println("tripsStatus1::"+tripsFirebaseGuardListModels.get(i).getTrip_status());
            if(!tripsFirebaseGuardListModelsRating.get(i).getTrip_status().equals("7")) {
                if (tripsFirebaseGuardListModelsRating.size() > 0) {
                    tripsFirebaseGuardListModelsRating.clear();
                    out.println("ratingDataClearAndAdd");
                }
            }
        }*/

        DataSnapshot listDataSnapshot = dataSnapshot.child("drivers");

        for (DataSnapshot ds : listDataSnapshot.getChildren()) {

            TripsFirebaseGuardListModel d = new TripsFirebaseGuardListModel(activity);
            //strExtendedStatusFB = checkStringEmptyZero(dataSnapshot, "booking_id");
            d.setBooking_id(checkStringEmptyZero(ds, "booking_id"));
            d.setDriver_id(checkStringEmptyZero(ds, "driver_id"));
            d.setDriver_first(checkStringEmpty(ds, "driver_first"));
            d.setDriver_last(checkStringEmpty(ds, "driver_last"));
            d.setDriver_mobile(checkStringEmpty(ds, "driver_mobile"));
            d.setDriver_avatar(checkStringEmpty(ds, "driver_avatar"));

            d.setStatus(checkStringEmptyZero(ds, "status"));
            d.setTrip_status(checkStringEmptyZero(ds, "trip_status"));

            d.setAccept_time(checkStringEmpty(ds, "accept_time"));
            d.setStart_time(checkStringEmpty(ds, "start_time"));
            d.setReach_time(checkStringEmpty(ds, "reach_time"));
            d.setService_start_time(checkStringEmpty(ds, "service_start_time"));
            d.setRating_status(checkStringEmptyZero(ds,"rating_status"));

            if ( Integer.parseInt(checkStringEmptyZero(ds, "trip_status")) == 8
                    || Integer.parseInt(checkStringEmptyZero(ds, "trip_status")) == 9
                    || Integer.parseInt(checkStringEmptyZero(ds, "trip_status")) == 13) {
                setVisibility(rcvGuardDetailsList, false);
            } else {
                out.println("DataAddedInTripListModel");
                tripsFirebaseGuardListModels.add(d);
            }

            System.out.println("getFirebaseTripsList() tripListFirebaseModel.size==>" + tripsFirebaseGuardListModels.size());

        }

/*
        for (DataSnapshot ds : listDataSnapshot.getChildren()) {
            TripsFirebaseGuardRatingListModel d = new TripsFirebaseGuardRatingListModel(activity);
            d.setBooking_id(checkStringEmptyZero(ds, "booking_id"));
            d.setDriver_first(checkStringEmpty(ds, "driver_first"));
            d.setDriver_last(checkStringEmpty(ds, "driver_last"));
            d.setDriver_avatar(checkStringEmpty(ds, "driver_avatar"));
            d.setTrip_status(checkStringEmptyZero(ds, "trip_status"));
            d.setDriver_id(checkStringEmptyZero(ds, "driver_id"));
            d.setRating_status(checkStringEmptyZero(ds, "rating_status"));

            out.println("dataAddedList::"+checkStringEmptyZero(ds, "driver_id"));
            out.println("dataAddedTripStatus::"+checkStringEmptyZero(ds, "trip_status"));
            if (Integer.parseInt(checkStringEmptyZero(ds, "trip_status")) == 6 ){
                tripsFirebaseGuardListModelsRating.add(d);
                out.println("dataAdded::"+checkStringEmptyZero(ds, "driver_id") );
                out.println("RatingListSize::"+tripsFirebaseGuardListModelsRating.size());
            }
        }
*/

        out.println("tripsFirebaseGuardListModels-->"+tripsFirebaseGuardListModels);

        if (tripsFirebaseGuardListModels != null && tripsFirebaseGuardListModels.size() > 0) {
            out.println("tripsFirebaseGuardListModels1-->"+tripsFirebaseGuardListModels);

            setVisibility(rcvGuardDetailsList, true);

            out.println("rating data updated");

            TripFirebaseGuardListAdapter tripFirebaseGuardListAdapter = new TripFirebaseGuardListAdapter(
                    activity, tripsFirebaseGuardListModels);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
            rcvGuardDetailsList.setLayoutManager(linearLayoutManager);
            rcvGuardDetailsList.setAdapter(tripFirebaseGuardListAdapter);
            tripFirebaseGuardListAdapter.notifyDataSetChanged();

            for (int i = 0; i <= tripsFirebaseGuardListModels.size()-1; i++) {
                isRatingUpdated = tripsFirebaseGuardListModels.get(i).getRating_status().equals("1");
            }

                if (isRatingUpdated && isRatingUpdated != null) {
                    txtRating.setVisibility(View.GONE);
                    out.println("isRatingUpdatedT"+isRatingUpdated);
                } else {
                    out.println("isRatingUpdatedF"+isRatingUpdated);
                    txtRating.setVisibility(View.VISIBLE);
                }


            tripFirebaseGuardListRatingAdapter = new TripFirebaseGuardListRatingAdapter(
                    activity, tripsFirebaseGuardListModels, "0");
            LinearLayoutManager linearLayoutManager_ = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
            rcvGuardDetailsListRatingTripSummary.setLayoutManager(linearLayoutManager_);
            rcvGuardDetailsListRatingTripSummary.setAdapter(tripFirebaseGuardListRatingAdapter);
            tripFirebaseGuardListRatingAdapter.notifyDataSetChanged();

        } else {
            setVisibility(rcvGuardDetailsList, false);
        }

        /*dataSnapshot.keepSynced(true);
        FirebaseRecyclerOptions<FirebaseGuardListModel> options =
                new FirebaseRecyclerOptions.Builder<FirebaseGuardListModel>()
                        .setQuery(dataSnapshot, FirebaseGuardListModel.class)
                        .build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FirebaseGuardListModel, MainCategoryViewHolder>(options) {
            @NonNull
            @Override
            public MainCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_guard_details_list_main, parent, false);
                return new MainCategoryViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MainCategoryViewHolder holder, int position, @NonNull FirebaseGuardListModel model) {
                Log.e(TAG, "onBindViewHolder: ~~~~position~~~" + position);
                try {
                    holder.setBlogPost(position, model);
                } catch (Exception e) {
                    //throw new RuntimeException(e);
                    out.println("~~~setBlogPost~`~`~Exception~~~" + e);
                }
            }
        };

        firebaseRecyclerAdapter.startListening();

        rcvGuardDetailsList.setAdapter(firebaseRecyclerAdapter);

        firebaseRecyclerAdapter.notifyDataSetChanged();*/

    }


    String strAcceptBookingId = "";
    String strAcceptDriverId = "";
    String strAcceptDriverName = "";
    String strAcceptDriverAvatar = "";

    @Override
    public void onSuccessTripFirebaseGuardList(HashMap<String, Object> map) {

        String from = String.valueOf(map.get("from"));
        TripsFirebaseGuardListModel data = (TripsFirebaseGuardListModel) map.get("data");

        if (data != null) {

            strAcceptBookingId = data.getBooking_id();
            strAcceptDriverId = data.getDriver_id();
            strAcceptDriverName = data.getDriver_first() + " " + data.getDriver_last();
            strAcceptDriverAvatar = data.getDriver_avatar();

            if (strAcceptDriverAvatar != null) {
                ivCallImage.setImageURI(Utils.BASE_URL + strAcceptDriverAvatar);
                int color = getResources().getColor(R.color.colorPrimary);
                RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
                roundingParams.setBorder(color, 1.0f);
                roundingParams.setRoundAsCircle(true);
                ivCallImage.getHierarchy().setRoundingParams(roundingParams);
            }

            txtCallName.setText(strAcceptDriverName);

            if (from.equals("phone")) {
                if (!checkPermissionForMicrophone()) {
                    requestPermissionForMicrophone();
                } else {
                    toIdentity = "InstaGuard_driver" + strAcceptDriverId;
                    if (accessToken != null) {
                        params.put("to", toIdentity);
                        ConnectOptions connectOptions = new ConnectOptions.Builder(accessToken)
                                .params(params)
                                .build();
                        activeCall = Voice.connect(activity, connectOptions, callListener);
                        setCallUI();
                    } else {
                        retrieveAccessToken();
                    }
                }
            } else if (from.equals("chat")) {
                retrieveAccessTokenfromServer(strAcceptDriverId);
            }
        }
    }

    private void callExtendHoursAlertDialog() {

        AlertDialog b;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.alertdialog_extended_hrs, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setCancelable(false);

        Typeface tfc = Typeface.createFromAsset(this.getAssets(), getString(R.string.App_Font));

        BaseActivity.CustomTFSpan tfSpanc = new BaseActivity.CustomTFSpan(tfc);

        SpannableString spannableStringc = new SpannableString(getString(R.string.extended_hours_details));
        spannableStringc.setSpan(tfSpanc, 0, spannableStringc.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString spannableStringsc = new SpannableString(getString(R.string.ok));
        spannableStringsc.setSpan(tfSpanc, 0, spannableStringsc.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString spannableStringssc = new SpannableString(getString(R.string.cancel));
        spannableStringssc.setSpan(tfSpanc, 0, spannableStringssc.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        final CustomEditText edt = dialogView.findViewById(R.id.edt_comment);
        final Spinner sp_no_of_hrs_ = dialogView.findViewById(R.id.sp_no_of_hrs_);
        String[] number = {"0"};
        String[] unit = {""};
        try {
            /*BaseActivity.MySpinnerAdapter adapter = new BaseActivity.MySpinnerAdapter(getApplicationContext(), R.layout.layout_spinner,
                    getResources().getStringArray(R.array.gender_type));
            sp_no_of_hrs_.setAdapter(adapter);*/

            // Define hours with "hrs" suffix
            String[] hours = new String[]{"1 hr", "2 hrs", "3 hrs", "4 hrs", "5 hrs", "6 hrs",
                    "7 hrs", "8 hrs", "9 hrs", "10 hrs", "11 hrs", "12 hrs"};

            // Create ArrayAdapter using the hours array
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.layout_spinner, hours);
            // Set dropdown layout style
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Set the adapter to the spinner
            sp_no_of_hrs_.setAdapter(adapter);
            // Set up item selected listener
            sp_no_of_hrs_.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // Get selected item
                    String selectedItem = parent.getItemAtPosition(position).toString();
                    // Split the selected item to get "0" and "hrs"
                    String[] parts = selectedItem.split(" ");
                    number[0] = parts[0]; // "0"
                    unit[0] = parts[1]; // "hrs"
                    // Display "0" and "hrs"
                    //Toast.makeText(requireActivity, "Number: " + number + ", Unit: " + unit, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Do nothing
                }
            });

            edt.setHint(getResources().getString(R.string.enter_your_hours_details));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                edt.setImeOptions(UCharacter.NumericType.NUMERIC);
            }
            edt.setFilters(new InputFilter[]{EMOJI_FILTER});

        } catch (Exception e) {
            e.printStackTrace();
        }

        dialogBuilder.setMessage(spannableStringc);

        dialogBuilder.setPositiveButton(spannableStringsc, (dialog, whichButton) -> {
            //do something with edt.getText().toString();
            /*if (edt.getText().toString().length() == 0) {
                edt.setError(getResources().getString(R.string.enter_your_hours_details));
            } else*/
            if (number[0].length() == 0 || number[0].equals("0")) {
                Toast.makeText(activity, getResources().getString(R.string.select_no_hours), Toast.LENGTH_SHORT).show();
            } else {
                BaseActivity.callClickAction(btnExtendJobsInProgress, true);
                dialog.dismiss();
                // callExtendedPaymentView(edt.getText().toString());
                callExtendedPaymentView(number[0]);
            }
        });

        dialogBuilder.setNegativeButton(spannableStringssc, (dialog, whichButton) -> {
            dialog.dismiss();
            BaseActivity.callClickAction(btnExtendJobsInProgress, true);
        });

        b = dialogBuilder.create();
        b.show();
    }

    private void callExtendedPaymentView(String edtExtendedHrs) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("customer_id", userId);
        map.put("booking_id", strBookingId);
        map.put("hrs", edtExtendedHrs);
        showProgressDialog(activity);

        Log.i("callFareEstimate~~", "~~~~" + map);
        Call<FareEstimateModel> call2 = apiInterface.doGetExtendFareEstimate("", map);
        call2.enqueue(new Callback<FareEstimateModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<FareEstimateModel> call, @NonNull Response<FareEstimateModel> response) {

                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {

                    hideProgressDialog();

                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("success")) {

                        getBaseFare = callView_(response.body().getResult().getBaseFare());

                        getTotalHrs = callView_(response.body().getResult().getTotalHrs());
                        getPricePerHr = callView_(response.body().getResult().getPricePerHr());

                        getPricePerGuard = callView_(response.body().getResult().getPricePerGuard());

                        getOverallTotal = callView_(response.body().getResult().getOverallTotal());
                        getBeforeVatAmount = callView_(response.body().getResult().getBeforeVatAmount());
                        getTotalGuardAmount = callView_(response.body().getResult().getTotalGuardAmount());

                        getPromoCode = callView_(response.body().getResult().getPromoCode());
                        getPromoDiscount = callView_(response.body().getResult().getPromoDiscount());
                        getPromoStatus = callView_(response.body().getResult().getPromoStatus());


                        if (!getTotalHrs.equals("0")) {
                            txtExtendHrsCardPayment.setText(convertHoursToDaysHoursMinutes(getTotalHrs));
                        } else {
                            txtExtendHrsCardPayment.setText("-");
                        }

                        if (!getTotalGuardAmount.equals("0")) {
                            txtExtendHrsAmountCardPayment.setText(strCurrencySymbol + " " +
                                    convertCurrencyPrice(activity, getTotalGuardAmount));
                        } else {
                            txtExtendHrsAmountCardPayment.setText("-");
                        }

                        if (!getBeforeVatAmount.equals("0")) {
                            txtExtendHrsVATAmountCardPayment.setText(strCurrencySymbol + " " +
                                    convertCurrencyPrice(activity, getBeforeVatAmount));
                        } else {
                            txtExtendHrsVATAmountCardPayment.setText("-");
                        }

                        if (!getOverallTotal.equals("0")) {
                            txtTotalAmountCardPayment.setText(strCurrencySymbol + " " +
                                    convertCurrencyPrice(activity, getOverallTotal));
                        } else {
                            txtTotalAmountCardPayment.setText("-");
                        }

                        String getWallet = callView_(response.body().getResult().getWalletAmount());
                        out.println("~~~~~~walletstatus--->>" + response.body().getResult().getWalletStatus());
                        if (response.body().getResult().getWalletStatus() ||
                                response.body().getResult().getWalletStatus().toString().equals("true")) {
                            getState.putBoolean(Utils.SP_COMMON_WALLET_STATUS, true);
                        } else {
                            getState.putBoolean(Utils.SP_COMMON_WALLET_STATUS, false);
                        }
                        getState.putString(Utils.SP_USER_WALLET_AMT, getWallet);
                        getState.apply();

                        callCreatePaymentMethod_();

                    } else {
                        hideProgressDialog();
                        Toast.makeText(activity,
                                Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<FareEstimateModel> call, @NonNull Throwable t) {
                hideProgressDialog();
                call.cancel();
                out.println("~~~~onFailure~~FareEst~~~" + t);
            }
        });

    }


    @Override
    public void onSuccessTripFirebaseGuardListRating(HashMap<String, Object> map) {

        String from = String.valueOf(map.get("from"));
        String edtComments = String.valueOf(map.get("edtComments"));
        String ratings = String.valueOf(map.get("ratings"));
        TripsFirebaseGuardListModel data = (TripsFirebaseGuardListModel) map.get("data");

        String strBookingId;
        String strdriverIdFB;
        if (data != null) {
            strBookingId = String.valueOf(data.getBooking_id());
            strdriverIdFB = String.valueOf(data.getDriver_id());

            out.println("bookingId::"+data.getBooking_id());

            if (from.equals("rate")) {
                callUpdateDriverRating("multi", userId, strBookingId, strdriverIdFB, ratings, edtComments);
            }
        }
    }


    /*-********************** CARD **************************************/

    View lytGotPaynow;
    View lytPaymentPaynowWebView;
    ImageView ivTitleLogoPaynowWebView;
    RelativeLayout rlParentLytPaymentPaynow;
    ImageView imgClosePaymentPaynowWebView;
    WebView webView;
    RelativeLayout progressBarPaynow;
    View lytAddCardPaymentAlsoSaveView;
    RelativeLayout rlParentLytAddCardView;
    ImageView ivBackAddCardView;
    CheckBox cbAlsoSaveCard;
    CustomButton btnSaveCardView;

    String generateTokenAPI = "";
    String intentURL = "";
    WebSettings settings;

    Stripe stripe;
    PaymentLauncher paymentLauncher;
    String paymentId;
    private ActivityResultLauncher<Intent> paymentLauncherRegister;

    public static int STRIPE_PAYMENT_INTENT_CALL_INTERVAL = 6 * 1000; //6 seconds
    String strClient_Secret;
    private final Handler mHandler = new Handler();
    GooglePayLauncher googlePayLauncher__;
    /*Paypal Code Details*/

    /*-********************** Handler **************************************/
    Runnable mHandlerTask = new Runnable() {
        @Override
        public void run() {
            //doSomething();
            callPaymentIntentGetWebview();
            mHandler.postDelayed(mHandlerTask, STRIPE_PAYMENT_INTENT_CALL_INTERVAL);
        }
    };

    private void callPaymentIntentGetWebview() {

        PaymentConfiguration paymentConfiguration
                = PaymentConfiguration.getInstance(activity);

        Stripe stripe = new Stripe(activity, STRIPE_PUBLISHKEY);
        stripe.retrievePaymentIntent(strClient_Secret, paymentConfiguration.getStripeAccountId(),
                new ApiResultCallback<PaymentIntent>() {
                    @Override
                    public void onSuccess(@NonNull PaymentIntent paymentIntent) {
                        out.println("~~~~stripe~retrievePaymentIntent~~~~" + paymentIntent);

                        if (paymentIntent.getStatus() == StripeIntent.Status.Succeeded) {
                            stopRepeatingTask();
                        } else if (paymentIntent.getStatus() == StripeIntent.Status.RequiresAction) { // Waiting payment
                            //stopRepeatingTask();
                            out.println("~~~RequiresAction~~~");
                        } else if (paymentIntent.getStatus() == StripeIntent.Status.RequiresCapture) { // Confirm payment
                            stopRepeatingTask();
                            out.println("~~~RequiresAction~~~");

                            if (lytAddCardPaymentAlsoSaveView.getVisibility() == View.VISIBLE) {
                                lytAddCardPaymentAlsoSaveView.setVisibility(View.GONE);
                            }
                            if (lytPaymentPaynowWebView.getVisibility() == View.VISIBLE) {
                                lytPaymentPaynowWebView.setVisibility(View.GONE);
                            }
                            CallRequestBooking("save_card", paymentIntent.getId());

                        } else if (paymentIntent.getStatus() == StripeIntent.Status.RequiresPaymentMethod) {
                            if (lytAddCardPaymentAlsoSaveView.getVisibility() == View.VISIBLE) {
                                lytAddCardPaymentAlsoSaveView.setVisibility(View.GONE);
                            }
                            if (lytPaymentPaynowWebView.getVisibility() == View.VISIBLE) {
                                lytPaymentPaynowWebView.setVisibility(View.GONE);
                            }
                            stopRepeatingTask();
                            Toast.makeText(activity, "Stripe Payment Failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(@NonNull Exception e) {
                        out.println("~~~~stripe~onError~~~~" + e);
                        stopRepeatingTask();
                        if (lytAddCardPaymentAlsoSaveView.getVisibility() == View.VISIBLE) {
                            lytAddCardPaymentAlsoSaveView.setVisibility(View.GONE);
                        }
                        if (lytPaymentPaynowWebView.getVisibility() == View.VISIBLE) {
                            lytPaymentPaynowWebView.setVisibility(View.GONE);
                        }
                        Toast.makeText(activity, getResources().getString(R.string.try_again), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    void startRepeatingTask() {
        Log.i("TAG", "startRepeatingTask: ");
        if (mHandlerTask != null)
            mHandlerTask.run();
    }

    void stopRepeatingTask() {
        Log.i("TAG", "stopRepeatingTask: ");
        if (mHandlerTask != null)
            mHandler.removeCallbacks(mHandlerTask);
    }
    /*-********************** Handler **************************************/

    private void callAddCardsView() {

        lytGotPaynow = findViewById(R.id.got_paynow);
        lytPaymentPaynowWebView = findViewById(R.id.lyt_payment_paynow_view);
        ivTitleLogoPaynowWebView = findViewById(R.id.got_paynow_image);
        rlParentLytPaymentPaynow = findViewById(R.id.rl_parent_lyt_payment_paynow);
        imgClosePaymentPaynowWebView = findViewById(R.id.img_close_paynow);
        webView = findViewById(R.id.webview);
        progressBarPaynow = findViewById(R.id.progressBar_paynow);
        lytAddCardPaymentAlsoSaveView = findViewById(R.id.lyt_payment_add_card_view);
        rlParentLytAddCardView = findViewById(R.id.lyt_parent_add_card_view);
        ivBackAddCardView = findViewById(R.id.imgbtn_back_add_card_also);
        creditCardModuleStripe = findViewById(R.id.credit_card_module_stripe);
        cbAlsoSaveCard = findViewById(R.id.cb_save_card_confirm);
        btnSaveCardView = findViewById(R.id.save_button_add_card_view);

        STRIPE_PUBLISHKEY = activity.getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE)
                .getString(Utils.SP_COMMON_STRIPE_PUBLISH_KEY, null);
        STRIPE_SECRETKEY = activity.getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE)
                .getString(Utils.SP_COMMON_STRIPE_SECRET_KEY, null);

        final PaymentAuthConfig.Stripe3ds2UiCustomization uiCustomization =
                new PaymentAuthConfig.Stripe3ds2UiCustomization.Builder()
                        .setLabelCustomization(
                                new PaymentAuthConfig.Stripe3ds2LabelCustomization.Builder()
                                        .setTextFontSize(12)
                                        .build())
                        .build();
        PaymentAuthConfig.init(new PaymentAuthConfig.Builder()
                .set3ds2Config(new PaymentAuthConfig.Stripe3ds2Config.Builder()
                        .setTimeout(10)
                        .setUiCustomization(uiCustomization)
                        .build())
                .build());

        PaymentConfiguration.init(activity, STRIPE_PUBLISHKEY);

        PaymentConfiguration paymentConfiguration
                = PaymentConfiguration.getInstance(activity);

        //   mGetContent.launch("paymentLauncher");

        paymentLauncher = PaymentLauncher.Companion.create(
                MainJobProgressActivity.this,
                paymentConfiguration.getPublishableKey(),
                paymentConfiguration.getStripeAccountId(),
                MainJobProgressActivity.this::onPaymentResult
        );


        paymentLauncherRegister = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    out.println("~~~~paymentLauncherLauncher~~~~~~" + result.getResultCode());
                    // Handle the result of the PaymentLauncher requireActivity here
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Handle success
                    } else {
                        // Handle failure or cancellation
                    }
                }
        );

        rlParentLytAddCardView.setOnClickListener(view -> {
        });

        ivBackAddCardView.setOnClickListener(view -> {
            if (lytAddCardPaymentAlsoSaveView.getVisibility() == View.VISIBLE) {
                lytAddCardPaymentAlsoSaveView.setVisibility(View.GONE);
            }
        });

         /* final GooglePayPaymentMethodLauncher googlePayLauncher = new GooglePayPaymentMethodLauncher(
                MainJobProgressActivity.this,
                new GooglePayPaymentMethodLauncher.Config(
                        GooglePayEnvironment.Test,
                        "US",
                        "Widget Store"
                ),
                this::onGooglePayReady,
                this::onGooglePayResult
        );

        // launch `GooglePayPaymentMethodLauncher` to create a PaymentMethod
        googlePayButton.setOnClickListener(
                v -> googlePayLauncher.present("USD", 2500)
        );*/

        googlePayLauncher__ = new GooglePayLauncher(
                MainJobProgressActivity.this,
                new GooglePayLauncher.Config(
                        GooglePayEnvironment.Production,
                        "GB",
                        "Insta-Guard",
                        true,
                        new GooglePayLauncher.BillingAddressConfig(false),
                        false
                ),
                this::onGooglePayReady_,
                this::onGooglePayResult_
        );

        // launch `GooglePayLauncher` to confirm a Payment Intent
        /*googlePayButton.setOnClickListener(
                v -> callPaymentIntent_API("250", "0", "gpay")
        );*/
    }


    private void callPayPalIntegration() {

        System.out.println("~~~~callPayPalIntegration~~Inside~~~");

        if (CONFIG_CLIENT_ID == null || CONFIG_CLIENT_ID.equals("null") || CONFIG_CLIENT_ID.equals("")) {
            CONFIG_CLIENT_ID = Utils.PAYPAL_CLIENTID;
        }
        if (PAYPAL_PRIVACY_URL == null || PAYPAL_PRIVACY_URL.equals("null") || PAYPAL_PRIVACY_URL.equals("")) {
            PAYPAL_PRIVACY_URL = getResources().getString(R.string.paypal_privacy);
        }
        if (PAYPAL_LEGAL_URL == null || PAYPAL_LEGAL_URL.equals("null") || PAYPAL_LEGAL_URL.equals("")) {
            PAYPAL_LEGAL_URL = getResources().getString(R.string.paypal_legal);
        }

        System.out.println("value of client id====>>" + CONFIG_CLIENT_ID);
        //  System.out.println("value of CONFIG_ENVIRONMENT_PAYPAL====>>" + CONFIG_ENVIRONMENT_PAYPAL);

        if (PAYPAL_TYPE != null && !PAYPAL_TYPE.equals("null") && !PAYPAL_TYPE.equals("")) {
            if (PAYPAL_TYPE.equals("1") || PAYPAL_TYPE.equals("1.0")) {
                environment = Environment.LIVE;
            } else {
                environment = Environment.SANDBOX;
            }
        } else {
            environment = Environment.SANDBOX;
        }

        CoreConfig coreConfig = new CoreConfig(CONFIG_CLIENT_ID, environment);

        paypalNativeClient = new PayPalNativeCheckoutClient(
                (Application) activity.getApplicationContext(), coreConfig, Utils.PAYPAL_RETURN_URL);
        payPalDataCollector = new PayPalDataCollector(coreConfig);

        payPalNativeShippingListener = new PayPalNativeShippingListener() {
            @Override
            public void onPayPalNativeShippingAddressChange(@NotNull PayPalNativePaysheetActions actions,
                                                            @NotNull PayPalNativeShippingAddress payPalNativeShippingAddress) {
                System.out.println("~~~~onPayPalNativeShippingAddressChange``~~");
                actions.approve();
                hideProgressDialog();
            }

            @Override
            public void onPayPalNativeShippingMethodChange(@NotNull PayPalNativePaysheetActions actions, @NotNull PayPalNativeShippingMethod payPalNativeShippingMethod) {
                // called when the user updates their chosen shipping method
                // patch your order server-side with the updated shipping amount.
                // Once complete, call `actions.approve()` or `actions.reject()`
                System.out.println("~~~~onPayPalNativeShippingMethodChange``~~");
                try {
                    // TODO: patch order on server side, notify SDK of success by calling actions.approve()
                    actions.approve();
                } catch (Exception e) {
                    e.printStackTrace();
                    // catch any errors from patching the order e.g. network unavailable
                    // and notify SDK that the update was unsuccessful
                    actions.reject();
                }
                hideProgressDialog();
            }
        };

        payPalNativeCheckoutListener = new PayPalNativeCheckoutListener() {
            @Override
            public void onPayPalCheckoutStart() {
                // the PayPal paysheet is about to show up
                System.out.println("~~~~onPayPalCheckoutStart~~~~~`");
                BaseActivity.callClickAction(btnPayPalPayment, true);
                BaseActivity.callClickAction(btnConfirmExtendPayment, true);
            }

            @Override
            public void onPayPalCheckoutSuccess(@NotNull PayPalNativeCheckoutResult payPalNativeCheckoutResult) {
                // order was approved and is ready to be captured/authorized
                if (com.instaguard.user.BuildConfig.DEBUG) {
                    System.out.println("~~~~payPalNativeCheckoutResult~~~~~`" + payPalNativeCheckoutResult.getOrderId());
                    System.out.println("~~~~payPalNativeCheckoutResult~~~~~`" + payPalNativeCheckoutResult.getPayerId());
                }
                CallRequestBooking("paypal", payPalNativeCheckoutResult.getOrderId());
            }

            @Override
            public void onPayPalCheckoutFailure(@NotNull PayPalSDKError payPalSDKError) {
                // handle the error
                System.out.println("~~~~payPalSDKError~~~~~`" + payPalSDKError);
                hideProgressDialog();
                BaseActivity.callClickAction(btnPayPalPayment, true);
                BaseActivity.callClickAction(btnConfirmExtendPayment, true);
                Toast.makeText(activity, payPalSDKError.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPayPalCheckoutCanceled() {
                // the user canceled the flow
                System.out.println("~~~~onPayPalCheckoutCanceled~~~~~`");
                hideProgressDialog();
                BaseActivity.callClickAction(btnPayPalPayment, true);
                BaseActivity.callClickAction(btnConfirmExtendPayment, true);
                Toast.makeText(activity, getResources().getString(R.string.paypal_cancelled),
                        Toast.LENGTH_SHORT).show();
            }
        };

        paypalNativeClient.setShippingListener(payPalNativeShippingListener);
        paypalNativeClient.setListener(payPalNativeCheckoutListener);

    }


    private void callUpdateStripAlsoSaveCard(String total_amount) {

        if (lytAddCardPaymentAlsoSaveView.getVisibility() == View.GONE) {
            lytAddCardPaymentAlsoSaveView.setVisibility(View.VISIBLE);
        }

        btnSaveCardView.setOnClickListener(view -> stripePayment(total_amount));
    }

    @SuppressLint("LongLogTag")
    private void stripePayment(String total_amount) {
        showProgressDialog(activity);
        cardToSave = creditCardModuleStripe.getCardParams();
        if (cardToSave == null) {
            hideProgressDialog();
            Toast.makeText(activity, R.string.invalid_card_details, Toast.LENGTH_SHORT).show();
        } else {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "stripePayment: " + cardToSave);
                Log.e(TAG, "STRIPE_PUBLISHKEY: " + STRIPE_PUBLISHKEY);
            }
            if (cbAlsoSaveCard.getVisibility() == View.VISIBLE) {
                if (cbAlsoSaveCard.isChecked()) { // Save Card
                    callPaymentIntent_API(total_amount, "1", "stripe");
                } else { // Option only token get
                    callPaymentIntent_API(total_amount, "0", "stripe");
                }
            } else { // Default State Token get
                callPaymentIntent_API(total_amount, "0", "stripe");
            }
        }
    }

    /*-*************************** ONE TIME PAYMENT *************************/
    private void onPaymentResult(PaymentResult paymentResult) {

        System.out.println("~~~~~~paymentResult~~~~~" + paymentResult);

        hideKeyBoard(MainJobProgressActivity.this);

        String message = "";
        if (paymentResult instanceof PaymentResult.Completed) {
            message = "Completed!";

            if (lytAddCardPaymentAlsoSaveView.getVisibility() == View.VISIBLE) {
                lytAddCardPaymentAlsoSaveView.setVisibility(View.GONE);
            }
            paymentId = BaseActivity.getHashMapPaymentId(activity, Utils.SP_PAYMENT_HASHMAP_KEYS_ + strBookingId);

            if (BaseActivity.getSetUpIntentOneSave_(activity, Utils.SP_PAYMENT_HASHMAP_KEYS_ + strBookingId).equals("save_card")) {
                CallRequestBooking("save_card", paymentId);
            } else {
                CallRequestBooking("one_time", paymentId);
            }

        } else if (paymentResult instanceof PaymentResult.Canceled) {
            if (lytAddCardPaymentAlsoSaveView.getVisibility() == View.VISIBLE) {
                lytAddCardPaymentAlsoSaveView.setVisibility(View.GONE);
            }
            message = "Canceled!";
        } else if (paymentResult instanceof PaymentResult.Failed) {
            // This string comes from the PaymentIntent's error message.
            // See here: https://stripe.com/docs/api/payment_intents/object#payment_intent_object-last_payment_error-message
            if (lytAddCardPaymentAlsoSaveView.getVisibility() == View.VISIBLE) {
                lytAddCardPaymentAlsoSaveView.setVisibility(View.GONE);
            }
            message = "Failed: "
                    + ((PaymentResult.Failed) paymentResult).getThrowable().getMessage();
        }

        Toast.makeText(activity, "PaymentResult~~" + message, Toast.LENGTH_SHORT).show();
    }

    private void callPaymentIntent_API(String total_amount, String save_card_status, String paymode) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("customer_id", userId);
        map.put("amount", total_amount);
        map.put("save_card", save_card_status);

        Call<PaymentIntentModel> call2 = apiInterface.doCreatePaymentIntentModel("token", map);

        showProgressDialog(activity);

        call2.enqueue(new Callback<PaymentIntentModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<PaymentIntentModel> call, @NonNull Response<PaymentIntentModel> response) {
                hideProgressDialog();

                if (response.body() != null) {

                    String client_secret = response.body().getClient_secret();
                    paymentId = response.body().getResult().getId();

                    setHashMapSaveCardStatus(activity, Utils.SP_PAYMENT_HASHMAP_KEYS_ + strBookingId, save_card_status);
                    setHashMapPaymentId(activity, Utils.SP_PAYMENT_HASHMAP_KEYS_ + strBookingId, paymentId);
                    setSetUpIntentOneSave_(activity, Utils.SP_PAYMENT_HASHMAP_KEYS_ + strBookingId, "one_time");

                    if (paymode.equals("google_pay")) {

                        out.println("~~~~~client_secret~~~~~~" + client_secret);
                        if (googlePayLauncher__ != null) {
                            googlePayLauncher__.presentForPaymentIntent(client_secret);
                        }

                    } else {
                        final PaymentMethodCreateParams params = creditCardModuleStripe.getPaymentMethodCreateParams();
                        if (params != null) {
                            final ConfirmPaymentIntentParams confirmParams =
                                    ConfirmPaymentIntentParams.createWithPaymentMethodCreateParams(
                                            params,
                                            client_secret
                                    );
                            try {
                                paymentLauncher.confirm(confirmParams);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<PaymentIntentModel> call, @NonNull Throwable t) {
                call.cancel();
            }
        });
    }

    /*-*************************** ONE TIME PAYMENT *************************/

    /*-*************************** SAVE CARD PAYMENT *************************/
    private void callSaveUpdateDetails(String price_, String paymode) {

        try {

            HashMap<String, Object> map = new HashMap<>();

            map.put("booking_id", strBookingId);
            map.put("extended_hrs", getTotalHrs);
            map.put("extended_vat", getBeforeVatAmount);
            map.put("extended_amount", getOverallTotal);
            map.put("extended_status", "1");

            saveHashMap_only(activity, Utils.SP_PAYMENT_HASHMAP_KEYS_ + strBookingId, map);

        } catch (Exception e) {
            e.printStackTrace();
        }

        HashMap<String, Object> map_ = new HashMap<>();
        map_.put("customer_id", userId);
        map_.put("amount", price_);
        map_.put("type", "trip");

        Log.i("callUpdateDetails", "~~~~" + map_);
        Call<SaveWalletPaymentIntentModel> call2 = apiInterface.doSaveWalletPaymentIntentModel("token", map_);
        showProgressDialog(activity);
        call2.enqueue(new Callback<SaveWalletPaymentIntentModel>() {
            @SuppressLint("SetJavaScriptEnabled")
            @Override
            public void onResponse(@NonNull Call<SaveWalletPaymentIntentModel> call, @NonNull Response<SaveWalletPaymentIntentModel> response) {
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {

                    if (Objects.requireNonNull(response.body()).getMessage().equals("success")) {

                        hideProgressDialog();

                        try {

                            String wallet_status = response.body().getWallet_status();

                            if (wallet_status.equals("0") || wallet_status.equals("2")) {

                                strClient_Secret = response.body().getClient_secret();
                                paymentId = response.body().getPayment_intent_id();

                                if (paymode.equals("google_pay")) {

                                    out.println("~~~~~client_secret~~~~~~" + strClient_Secret);

                                    setHashMapPaymentId(activity, "InstaGuard" + userId, paymentId);
                                    setSetUpIntentOneSave_(activity, "InstaGuard" + userId, "save_card");

                                    if (googlePayLauncher__ != null) {
                                        googlePayLauncher__.presentForSetupIntent(strClient_Secret, "GBP");
                                    }

                                } else {

                                    progressBarPaynow.setVisibility(View.VISIBLE);

                                    if (response.body().getAction_url_status() != null) {
                                        if (response.body().getAction_url_status().equals("1")) {

                                            String urlPaynow = response.body().getAction_url();

                                            String afterDecode = URLDecoder.decode(urlPaynow, "UTF-8");

                                            webView.clearCache(true);

                                            webView.clearFormData();

                                            webView.clearHistory();

                                            webView.clearSslPreferences();

                                            webView.loadUrl(afterDecode);

                                            settings = webView.getSettings();
                                            settings.setDomStorageEnabled(true);
                                            settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
                                            settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
                                            settings.setJavaScriptEnabled(true);
                                            settings.setJavaScriptCanOpenWindowsAutomatically(true);
                                            settings.setUserAgentString("Android Webview");

                                            webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

                                            webView.setPictureListener(new WebView.PictureListener() {
                                                @Override
                                                public void onNewPicture(WebView webView, @Nullable Picture picture) {
                                                    if (picture != null) {
                                                        out.println("~~~~PictureListener~~" + picture);
                                                    }
                                                }
                                            });


                                            webView.setWebViewClient(new WebViewClient() {

                                                @Override
                                                public void onLoadResource(WebView view, String url) {
                                                    super.onLoadResource(view, url);

                                                    if (BuildConfig.DEBUG) {
                                                        Log.e("TAG", "onLoadResource: " + url);
                                                    }

                                                    Boolean aBoolean = true;
                                                    if (aBoolean) {
                                                        //aBoolean = false;
                                                        if (url.contains("https://qr.stripe.com/")) {
                                                            out.println("~~~~//qr.stripe.com/~~~~~~" + url);
                                                            intentURL = url;
                                                            if (url.endsWith(".svg?download=true&border=2")) {
                                                                out.println("~~~~//svg?download/~~~~~~" + url);
                                                            }
                                                            //https://qr.stripe.com/test_YWNjdF8xTHgxeEZBZjZJSlZMd3czLF9NanNKdjlLNHlPOUVlaVZlSW9haHQySFA5cDl0RURh01002xmQw61w.svg?download=true&border=2
                                                            Log.v("TAG", "download() Method completed ");
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onPageFinished(WebView view, String url) {
                                                    Log.e("TAG", "onPageFinished: " + url);
                                                    super.onPageFinished(view, url);
                                                    progressBarPaynow.setVisibility(View.GONE);

                                                }

                                                @Override
                                                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                                    Log.e("TAG", "onPageStarted: " + url);
                                                    progressBarPaynow.setVisibility(View.VISIBLE);
                                                }
                                            });

                                /*~~~~stripe~onError~~~~com.stripe.android.core.exception.PermissionException:
                                The provided key 'pk_test_***********************************************************************************************jlF3'
                                does not have access to account 'pi_3NVyfwAf6IJVLww31nZGbdwS' (or that account does not exist).
                                 Application access may have been revoked.
                                * */

                                            startRepeatingTask();


                                            if (lytPaymentPaynowWebView.getVisibility() == View.GONE) {
                                                lytPaymentPaynowWebView.setVisibility(View.VISIBLE);
                                            }

                                            // baseActivity.setimageView(requireActivity, ivTitleLogoPaynowWebView, "", R.drawable.logo, false);

                                        } else if (response.body().getAction_url_status().equals("0")) {
                                            if (response.body().getAction_url() != null && !String.valueOf(response.body().getAction_url()).equals("null")
                                                    && !String.valueOf(response.body().getAction_url()).equals("")) {
                                                if (response.body().getAction_url().equals("require_3d2")) {
                                                    try {

                                                        setHashMapPaymentId(activity, Utils.SP_PAYMENT_HASHMAP_KEYS_ + strBookingId, paymentId);
                                                        setSetUpIntentOneSave_(activity, Utils.SP_PAYMENT_HASHMAP_KEYS_ + strBookingId, "save_card");

                                                        if (response.body().getResult() != null && response.body().getResult().getpayment_method_id() != null &&
                                                                !String.valueOf(response.body().getResult().getpayment_method_id()).equals("null")
                                                                && !String.valueOf(response.body().getResult().getpayment_method_id()).equals("")
                                                        ) {
                                                            String getpayment_method_id = response.body().getResult().getpayment_method_id();
                                                            // Create a PaymentIntentParams
                                                            ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams.createWithPaymentMethodId(
                                                                    getpayment_method_id,
                                                                    strClient_Secret
                                                            );
                                                            paymentLauncher.confirm(confirmParams);
                                                        } else {
                                                            // Create a PaymentIntentParams
                                                            ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams.create(
                                                                    strClient_Secret
                                                            );
                                                            paymentLauncher.confirm(confirmParams);
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        Toast.makeText(activity, getResources().getString(R.string.try_again), Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    CallRequestBooking("save_card", paymentId);
                                                }
                                            } else {
                                                CallRequestBooking("save_card", paymentId);
                                            }
                                        }
                                    } else {
                                        CallRequestBooking("save_card", paymentId);
                                    }

                                }
                            } else { //Using wallet
                                CallRequestBooking("save_card", "use_wallet");
                            }

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            progressBarPaynow.setVisibility(View.GONE);
                            Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show();
                        }

                        if (lytAddCardPaymentAlsoSaveView.getVisibility() == View.VISIBLE) {
                            lytAddCardPaymentAlsoSaveView.setVisibility(View.GONE);
                        }

                    } else { //Card not available

                        progressBarPaynow.setVisibility(View.GONE);

                        if (lytAddCardPaymentAlsoSaveView.getVisibility() == View.VISIBLE) {
                            lytAddCardPaymentAlsoSaveView.setVisibility(View.GONE);
                        }

                        hideProgressDialog();

                        if (paymode.equals("google_pay")) {

                            callPaymentIntent_API(getOverallTotal, "0", "google_pay");

                        } else {
                            //Toast.makeText(requireActivity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            if (getOverallTotal != null && !getOverallTotal.equals("0")) {
                                callUpdateStripAlsoSaveCard(getOverallTotal);
                            } else {
                                //callFareEstimate();
                                Toast.makeText(activity, getResources().getString(R.string.total_amount_empty_try_again), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                if (response.code() == RESPONSECODE_500 || response.code() == RESPONSECODE_501 || response.code() == RESPONSECODE_401) {
                    hideProgressDialog();
                    progressBarPaynow.setVisibility(View.GONE);

                }

            }

            @Override
            public void onFailure(@NonNull Call<SaveWalletPaymentIntentModel> call, @NonNull Throwable t) {
                progressBarPaynow.setVisibility(View.GONE);
                hideProgressDialog();
                call.cancel();
            }
        });
    }

    private void callCreatePayPalPayment(String getOverallTotal) {

        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("amount", getOverallTotal);

        Log.i("", "~callCreatePayPalPayment~~~" + map1);

        showProgressDialog(activity);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<PayPalCreateModel> call2 = apiInterface.doCreatePayPalPayment("Bearer " + userAccessToken, map1);
        call2.enqueue(new Callback<PayPalCreateModel>() {
            @Override
            public void onResponse(@NonNull Call<PayPalCreateModel> call, @NonNull Response<PayPalCreateModel> response) {
                Log.i(TAG, "onResponse: resonse code--> " + response.code());
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    hideProgressDialog();
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Order Created")) {
                        try {
                            String id = response.body().getId();
                            //if (com.instaguard.user.BuildConfig.DEBUG){
                            System.out.println("~~~~PayPalNativeCheckoutRequest~~~id~~~" + id);
                            System.out.println("~~~~PayPalNativeCheckoutRequest~~~strCustomerEmailFB~~~" + strCustomerEmailFB);
                            //}
                            PayPalNativeCheckoutRequest request = new PayPalNativeCheckoutRequest(id, strCustomerEmailFB);
                            paypalNativeClient.startCheckout(request);

                        } catch (Exception e) {
                            e.printStackTrace();
                            BaseActivity.callClickAction(btnConfirmExtendPayment, true);
                        }
                    } else {
                        BaseActivity.callClickAction(btnConfirmExtendPayment, true);
                        Toast.makeText(activity, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onResponse: success");
                    }
                } else {
                    BaseActivity.callClickAction(btnConfirmExtendPayment, true);
                    Toast.makeText(activity, "Response Error 500", Toast.LENGTH_SHORT).show();
                    hideProgressDialog();
                }
            }

            @Override
            public void onFailure(@NonNull Call<PayPalCreateModel> call, @NonNull Throwable t) {
                call.cancel();
                BaseActivity.callClickAction(btnConfirmExtendPayment, true);
                hideProgressDialog();
            }
        });
    }

    private void CallRequestBooking(String key_one_save_card, String strTripType_PaymentId) {

        HashMap<String, Object> map = new HashMap<>();
        try {

            String save_card_status = "0";
            /*if (cbAlsoSaveCard.isChecked()) {
            save_card_status = "1";
            } else {
            save_card_status = "0";
            }*/

            /*booking_id:120
            payment_type:stripe/paypal
            stripe_token:1254859536
            extended_hrs:5
            extended_amount:120
            extended_vat:0
            extended_status:1*/

            map.put("booking_id", strBookingId);
            map.put("extended_hrs", getTotalHrs);
            map.put("extended_vat", getBeforeVatAmount);
            map.put("extended_amount", getOverallTotal);
            map.put("extended_status", "1");

            if (selectedId == 0 || key_one_save_card.equals("wallet")) {  //Wallet payment
                map.put("payment_type", "wallet");
            } else if (selectedId == 2 /*|| rbStripePayment.isChecked()*/) {  //Card payment
                map.put("payment_type", "stripe");
                if (!strTripType_PaymentId.equals("use_wallet")) {
                    map.put("stripe_token", strTripType_PaymentId);
                }
            } else if (selectedId == 6 || key_one_save_card.equals("paypal")) { //Paypal
                map.put("payment_type", "paypal");
                map.put("stripe_token", strTripType_PaymentId);
            } else if (selectedId == 8 || key_one_save_card.equals("save_card_gpay")
                    || key_one_save_card.equals("one_time_gpay")) {  //Google Pay
                map.put("payment_type", "stripe_gpay");
                if (!strTripType_PaymentId.equals("use_wallet")) {
                    map.put("stripe_token", strTripType_PaymentId);
                }
            }


            /*if (key_one_save_card.equals("wallet")) {  //wallet
                map.put("payment_type", "wallet");
            } else if (key_one_save_card.equals("paypal")) {  //Paypal
                map.put("payment_type", "paypal");
                map.put("stripe_token", strTripType_PaymentId);
            } else { //Card payment
                map.put("payment_type", "stripe");
                if (!strTripType_PaymentId.equals("use_wallet")) {
                    map.put("stripe_token", strTripType_PaymentId);
                }
            }*/

            saveHashMap(activity, Utils.SP_PAYMENT_HASHMAP_KEYS_ + strBookingId, map, save_card_status);
            setSetUpIntentOneSave_(activity, Utils.SP_PAYMENT_HASHMAP_KEYS_ + strBookingId, key_one_save_card);

        } catch (Exception e) {
            e.printStackTrace();

            if (BaseActivity.getHashMap(activity, Utils.SP_PAYMENT_HASHMAP_KEYS_ + strBookingId).get("booking_id") != null) {
                map.put("booking_id", BaseActivity.getHashMap(activity, Utils.SP_PAYMENT_HASHMAP_KEYS_ + strBookingId).get("booking_id"));
            }
            if (BaseActivity.getHashMap(activity, Utils.SP_PAYMENT_HASHMAP_KEYS_ + strBookingId).get("extended_hrs") != null) {
                map.put("extended_hrs", BaseActivity.getHashMap(activity, Utils.SP_PAYMENT_HASHMAP_KEYS_ + strBookingId).get("extended_hrs"));
            }
            if (BaseActivity.getHashMap(activity, Utils.SP_PAYMENT_HASHMAP_KEYS_ + strBookingId).get("extended_vat") != null) {
                map.put("extended_vat", BaseActivity.getHashMap(activity, Utils.SP_PAYMENT_HASHMAP_KEYS_ + strBookingId).get("extended_vat"));
            }
            if (BaseActivity.getHashMap(activity, Utils.SP_PAYMENT_HASHMAP_KEYS_ + strBookingId).get("extended_amount") != null) {
                map.put("extended_amount", BaseActivity.getHashMap(activity, Utils.SP_PAYMENT_HASHMAP_KEYS_ + strBookingId).get("extended_amount"));
            }
            if (BaseActivity.getHashMap(activity, Utils.SP_PAYMENT_HASHMAP_KEYS_ + strBookingId).get("extended_status") != null) {
                map.put("extended_status", BaseActivity.getHashMap(activity, Utils.SP_PAYMENT_HASHMAP_KEYS_ + strBookingId).get("extended_status"));
            }

            if (BaseActivity.getHashMap(activity, Utils.SP_PAYMENT_HASHMAP_KEYS_ + strBookingId).get("payment_type") != null) {

                map.put("payment_type", BaseActivity.getHashMap(activity, Utils.SP_PAYMENT_HASHMAP_KEYS_ + strBookingId).get("payment_type"));

                if (Objects.requireNonNull(BaseActivity.getHashMap(activity,
                        Utils.SP_PAYMENT_HASHMAP_KEYS_ + strBookingId).get("payment_type")).toString().equals("stripe")) {
                    if (!strTripType_PaymentId.equals("use_wallet")) {
                        map.put("stripe_token", strTripType_PaymentId);
                    }
                } else if (Objects.requireNonNull(BaseActivity.getHashMap(activity,
                        Utils.SP_PAYMENT_HASHMAP_KEYS_ + strBookingId).get("payment_type")).toString().equals("paypal")) {
                    map.put("stripe_token", strTripType_PaymentId);
                }

            }
        }

        showProgressDialog(activity);

        Call<SendRequestModel> call2 = apiInterface.doExtendedHoursUpdateDetails(map);
        call2.enqueue(new Callback<SendRequestModel>() {
            @Override
            public void onResponse(@NonNull Call<SendRequestModel> call, @NonNull Response<SendRequestModel> response) {
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    hideProgressDialog();
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Request saved successfully.")) {

                    } else {
                        Toast.makeText(activity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        hideProgressDialog();
                    }
                } else if (response.code() == RESPONSECODE_401) {
                    hideProgressDialog();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SendRequestModel> call, @NonNull Throwable t) {
                call.cancel();
                hideProgressDialog();
                System.out.println("~~~onFailure~~" + t);
            }
        });
    }


    private static final int MIC_PERMISSION_REQUEST_CODE = 1;
    private static final int SNACKBAR_DURATION = 4000;

    private String accessToken;
    private AudioManager audioManager;
    private int savedAudioMode = AudioManager.MODE_INVALID;

    private boolean isReceiverRegistered = false;
    private VoiceBroadcastReceiver voiceBroadcastReceiver;

    // Empty HashMap, never populated for the Quickstart
    HashMap<String, String> params = new HashMap<>();

    private View coordinatorLayout;
    private FloatingActionButton callActionFab;
    private FloatingActionButton hangupActionFab;
    private FloatingActionButton loudspeakerActionFab;
    private FloatingActionButton holdActionFab;
    private FloatingActionButton muteActionFab;
    private Chronometer chronometer;
    private SoundPoolManager soundPoolManager;
    private View lytCall;
    private SimpleDraweeView ivCallImage;
    private CustomTextView txtCallName;

    public static final String INCOMING_CALL_INVITE = "INCOMING_CALL_INVITE";
    public static final String CANCELLED_CALL_INVITE = "CANCELLED_CALL_INVITE";
    public static final String INCOMING_CALL_NOTIFICATION_ID = "INCOMING_CALL_NOTIFICATION_ID";
    public static final String ACTION_INCOMING_CALL = "ACTION_INCOMING_CALL";
    public static final String ACTION_CANCEL_CALL = "ACTION_CANCEL_CALL";
    public static final String ACTION_FCM_TOKEN = "ACTION_FCM_TOKEN";

    private NotificationManager notificationManager;
    private AlertDialog alertDialog;
    private CallInvite activeCallInvite;
    private com.twilio.voice.Call activeCall;
    private int activeCallNotificationId;

    RegistrationListener registrationListener = registrationListener();
    com.twilio.voice.Call.Listener callListener = callListener();

    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;
    private int field = 0x00000020;
    public static boolean CurrentlyRunningChat = false;
    /*Twillio Start End*/


    private void callTwilioSetup() {

        /*Twillio Start*/
        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(field, getLocalClassName());

        Utils.identity = "InstaGuard_user" + userId;

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        lytCall = findViewById(R.id.lyt_call);

        coordinatorLayout = findViewById(R.id.coordinator_layout);
        callActionFab = findViewById(R.id.call_action_fab);
        hangupActionFab = findViewById(R.id.hangup_action_fab);
        loudspeakerActionFab = findViewById(R.id.loudspeaker_action_fab);
        holdActionFab = findViewById(R.id.hold_action_fab);
        muteActionFab = findViewById(R.id.mute_action_fab);
        chronometer = findViewById(R.id.chronometer);

        ivCallImage = findViewById(R.id.iv_call_image);
        txtCallName = findViewById(R.id.txt_call_name);

        // callActionFab.setOnClickListener(callActionFabClickListener());
        hangupActionFab.setOnClickListener(hangupActionFabClickListener());
        loudspeakerActionFab.setOnClickListener(loudspeakerActionFabClickListener());
        holdActionFab.setOnClickListener(holdActionFabClickListener());
        muteActionFab.setOnClickListener(muteActionFabClickListener());

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        soundPoolManager = SoundPoolManager.getInstance(this);

        /*
         * Setup the broadcast receiver to be notified of FCM Token updates
         * or incoming call invite in this Activity.
         */
        voiceBroadcastReceiver = new VoiceBroadcastReceiver();
        registerReceiver();

        /*
         * Needed for setting/abandoning audio focus during a call
         */
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setSpeakerphoneOn(true);

        /*
         * Enable changing the volume using the up/down keys during a conversation
         */
        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);

        /*
         * Setup the UI
         */
        resetUI();

        /*
         * Displays a call dialog if the intent contains a call invite
         */
        handleIncomingCallIntent(getIntent());

        /*
         * Ensure the microphone permission is enabled
         */
        if (!checkPermissionForMicrophone()) {
            requestPermissionForMicrophone();
        } /*else {
            retrieveAccessToken();
        }*/

        /*Twillio End*/

        /*coordinatorLayout.setOnClickListener(v -> { });*/

        coordinatorLayout.setOnClickListener(view -> {

        });


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIncomingCallIntent(intent);
    }

    private RegistrationListener registrationListener() {
        return new RegistrationListener() {
            @Override
            public void onRegistered(@NonNull String accessToken, @NonNull String fcmToken) {
            }

            @Override
            public void onError(@NonNull RegistrationException error, @NonNull String accessToken, @NonNull String fcmToken) {
                @SuppressLint("DefaultLocale") String message = String.format("Registration Error: %d, %s", error.getErrorCode(), error.getMessage());
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        };
    }

    private com.twilio.voice.Call.Listener callListener() {
        return new com.twilio.voice.Call.Listener() {

            @Override
            public void onRinging(@NonNull com.twilio.voice.Call call) {
                Log.d(TAG, "Ringing");
            }

            @Override
            public void onConnectFailure(@NonNull com.twilio.voice.Call call, @NonNull CallException error) {
                setAudioFocus(false);
                @SuppressLint("DefaultLocale") String message = String.format("Call Error: %d, %s", error.getErrorCode(), error.getMessage());
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                //Snackbar.make(coordinatorLayout, message, SNACKBAR_DURATION).show();
                resetUI();
            }

            @Override
            public void onConnected(@NonNull com.twilio.voice.Call call) {
                setAudioFocus(true);
                activeCall = call;
            }

            @Override
            public void onReconnecting(@NonNull com.twilio.voice.Call call, @NonNull CallException callException) {
            }

            @Override
            public void onReconnected(@NonNull com.twilio.voice.Call call) {
            }

            @Override
            public void onDisconnected(@NonNull com.twilio.voice.Call call, CallException error) {
                setAudioFocus(false);
                if (error != null) {
                    @SuppressLint("DefaultLocale")
                    String message = String.format("Call Error: %d, %s", error.getErrorCode(), error.getMessage());
                    //Snackbar.make(coordinatorLayout, message, SNACKBAR_DURATION).show();
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "Call ended", Toast.LENGTH_LONG).show();
                }
                resetUI();
            }
        };
    }

    /*
     * The UI state when there is an active call
     */
    private void setCallUI() {
        callActionFab.hide();
        hangupActionFab.show();
        // holdActionFab.show();
        muteActionFab.show();
        chronometer.setVisibility(View.VISIBLE);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        lytCall.setVisibility(View.VISIBLE);

        chronometer.setOnChronometerTickListener(chronometer -> {
            // TODO Auto-generated method stub
            String currentTime = chronometer.getText().toString();
            // System.out.println("time-->"+currentTime);
            if (currentTime.equals("02:00")) //put time according to you
            {
                soundPoolManager.playDisconnect();
                resetUI();
                disconnect();
            }
        });

        if (!wakeLock.isHeld()) {
            wakeLock.acquire();
            //wakeLock.acquire(10*60*1000L /*10 minutes*/); // New suspend
        }
    }

    /*
     * Reset UI elements
     */
    @SuppressLint({"ObsoleteSdkInt", "UseCompatLoadingForDrawables"})
    private void resetUI() {
        //callActionFab.show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            loudspeakerActionFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_volume_up_white_24dp, getApplicationContext().getTheme()));
            muteActionFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_mic_white_24dp, getApplicationContext().getTheme()));
            holdActionFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_white_24dp, getApplicationContext().getTheme()));

        } else {
            loudspeakerActionFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_volume_up_white_24dp));
            muteActionFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_mic_white_24dp));
            holdActionFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_white_24dp));
        }

        // muteActionFab.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_mic_white_24dp));
        holdActionFab.hide();
        holdActionFab.setBackgroundTintList(ColorStateList
                .valueOf(ContextCompat.getColor(this, R.color.colorAccent)));
        muteActionFab.hide();
        hangupActionFab.hide();
        chronometer.setVisibility(View.INVISIBLE);
        chronometer.stop();
        //  holdActionFab.setBackgroundResource(R.drawable.ic_volume_up_white_24dp);
        lytCall.setVisibility(View.GONE);

        if (wakeLock != null) {
            if (wakeLock.isHeld()) {
                wakeLock.release();
            }
        }
    }

    /*
     * Accept an incoming Call
     */
    private void answer() {
        activeCallInvite.accept(this, callListener);
        notificationManager.cancel(activeCallNotificationId);
    }

    /*
     * Disconnect from Call
     */
    private void disconnect() {
        if (activeCall != null) {
            activeCall.disconnect();
            activeCall = null;
        }
    }

    @SuppressLint({"ObsoleteSdkInt", "UseCompatLoadingForDrawables"})
    private void audioOption() {
        if (activeCall != null) {
            if (audioManager.isSpeakerphoneOn()) {
                audioManager.setSpeakerphoneOn(false);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    loudspeakerActionFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_phonelink_ring_white_24dp, getApplicationContext().getTheme()));
                } else {
                    loudspeakerActionFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_phonelink_ring_white_24dp));
                }

            } else {
                audioManager.setSpeakerphoneOn(true);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    loudspeakerActionFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_volume_up_white_24dp, getApplicationContext().getTheme()));
                } else {
                    loudspeakerActionFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_volume_up_white_24dp));
                }

            }
        }
    }

    private void hold() {
        if (activeCall != null) {
            boolean hold = !activeCall.isOnHold();
            activeCall.hold(hold);

            // Set fab as pressed when call is on hold
            ColorStateList holdActionBarCsl = hold ?
                    ColorStateList.valueOf(ContextCompat.getColor(this,
                            R.color.colorPrimaryDark)) :
                    ColorStateList.valueOf(ContextCompat.getColor(this,
                            R.color.colorAccent));
            holdActionFab.setBackgroundTintList(holdActionBarCsl);
        }
    }

    private void mute() {
        if (activeCall != null) {

            boolean mute = !activeCall.isMuted();
            activeCall.mute(mute);
            if (mute) {
                muteActionFab.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_mic_white_off_24dp));
            } else {
                muteActionFab.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_mic_white_24dp));
            }
        }
    }

    @SuppressLint("WrongConstant")
    private void setAudioFocus(boolean setFocus) {
        if (audioManager != null) {
            if (setFocus) {
                savedAudioMode = audioManager.getMode();
                // Request audio focus before making any device switch.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    AudioAttributes playbackAttributes = new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                            .build();
                    AudioFocusRequest focusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                            .setAudioAttributes(playbackAttributes)
                            .setAcceptsDelayedFocusGain(true)
                            .setOnAudioFocusChangeListener(i -> {
                            })
                            .build();
                    audioManager.requestAudioFocus(focusRequest);
                } else {
                    int focusRequestResult = audioManager.requestAudioFocus(focusChange -> {
                            }, AudioManager.STREAM_VOICE_CALL,
                            AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                }

                audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            } else {
                audioManager.setMode(savedAudioMode);
                audioManager.abandonAudioFocus(null);
            }
        }
    }

    private boolean checkPermissionForMicrophone() {
        int resultMic = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return resultMic == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissionForMicrophone() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
            Toast.makeText(this, "Microphone permissions needed. Please allow in your application settings.", Toast.LENGTH_SHORT).show();

        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    MIC_PERMISSION_REQUEST_CODE);
        }
    }


    private void retrieveAccessToken() {

        if (com.instaguard.user.BuildConfig.DEBUG) {
            String tokenURL = RETROFIT_URL_CALL_CHAT_ACCESS + "?identity=" + Utils.identity + "&device=" + "android_rider";
            System.out.println("~~tokenURL~~~" + tokenURL);
        }
        APIInterface apiInterface = APIClient.getClient_chat().create(APIInterface.class);
        Call<ChatAccessModel> call2 = apiInterface.doCallAccessToken("android_rider", Utils.identity);
        showProgressDialog(activity);
        call2.enqueue(new Callback<ChatAccessModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<ChatAccessModel> call, @NonNull Response<ChatAccessModel> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    String accessToken = response.body().getToken();
                    MainJobProgressActivity.this.accessToken = accessToken;
                    registerForCallInvites();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ChatAccessModel> call, @NonNull Throwable t) {
                call.cancel();
                hideProgressDialog();
                Toast.makeText(activity, t.toString(), Toast.LENGTH_SHORT).show();
                new MyAsyncTask1(MainJobProgressActivity.this).execute();
            }
        });


        /*String url = TWILIO_ACCESS_TOKEN_SERVER_URL + "?identity=" + Utils.identity + "&device=" + "android_rider";
        System.out.println("url==>" + TWILIO_ACCESS_TOKEN_SERVER_URL + "?identity=" + Utils.identity + "&device=" + "android_rider");

        Ion.with(this).load(TWILIO_ACCESS_TOKEN_SERVER_URL + "?identity=" + Utils.
                identity + "&device=" + "android_rider").asString().setCallback((e, accessToken) -> {
            if (e == null) {
                try {
                    JSONObject obj = new JSONObject(accessToken);
                    System.out.println("accessToken=>obj" + obj.get("token"));
                    accessToken = obj.get("token").toString();
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
                requireActivity.accessToken = accessToken;
                registerForCallInvites();
            } else {
                System.out.println("Error==>" + e.toString());
                new MyAsyncTask1(requireActivity).execute();
            }
        });*/

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i("permission", "Request Code: " + requestCode);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        /*if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {

        }*/

        switch (requestCode) {

            case ACCESS_FINE_LOCATION_INTENT_ID:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(getApplicationContext(), R.string.permission_granted, Toast.LENGTH_SHORT).show();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getApplicationContext(), R.string.permission_denied, Toast.LENGTH_SHORT).show();
                }
                //return;
                break;

            case REQUEST_ID_MULTIPLE_PERMISSIONS:

                if (PermissionUtils.checkPermission_Notification(activity).equals("1")) {
                    // success
                    if (NotificationManagerCompat.from(activity).areNotificationsEnabled()) {
                        // Toast.makeText(this, "Notification is on for this Application", Toast.LENGTH_SHORT).show();
                    } else {
                        // Toast.makeText(this, "Notification is off for this Application", Toast.LENGTH_SHORT).show();
                        displayNeverAskAgainDialog();
                    }
                } else if (PermissionUtils.checkPermission_Notification(activity).equals("2")) {
                    displayNeverAskAgainDialog();
                } else {
                    checkAndRequestPermissions(activity);
                }

                break;

            case REQUEST_ID_MULTIPLE_PERMISSIONS_N: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    perms.put(Manifest.permission.POST_NOTIFICATIONS, PackageManager.PERMISSION_GRANTED);
                }

                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        if (perms.get(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "Notification Permission Granted.", Toast.LENGTH_LONG).show();
                            //else any one or both the permissions are not granted
                        } else {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.POST_NOTIFICATIONS)) {

                                showDialogOK(
                                        (dialog, which) -> {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions(activity);
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    //Toast.makeText(requireActivity, "Necessary Permissions required for this app", Toast.LENGTH_LONG).show();
                                                    displayNeverAskAgainDialog();
                                                    break;
                                            }
                                        });
                            }
                            //permission is denied (and never ask again is  checked)
                            //shouldShowRequestPermissionRationale will return false
                            else {
                                displayNeverAskAgainDialog();
                            }
                        }
                    } else {
                        //
                    }
                }
            }
            break;
        }

    }


    public static AlertDialog createIncomingCallDialog(
            Context context,
            CallInvite callInvite,
            DialogInterface.OnClickListener answerCallClickListener,
            DialogInterface.OnClickListener cancelClickListener) {

        Typeface tf = Typeface.createFromAsset(context.getAssets(), context.getResources().getString(R.string.App_Font));

        BaseActivity.CustomTFSpan tfSpan = new BaseActivity.CustomTFSpan(tf);

        SpannableString spannableString = new SpannableString(context.getResources().getString(R.string.twilio_incoming_call));
        spannableString.setSpan(tfSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString accept = new SpannableString(context.getResources().getString(R.string.twilio_accept));
        accept.setSpan(tfSpan, 0, accept.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString reject = new SpannableString(context.getResources().getString(R.string.twilio_reject));
        reject.setSpan(tfSpan, 0, reject.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString message = new SpannableString(context.getResources().getString(R.string.twilio_user_is_calling));
        message.setSpan(tfSpan, 0, message.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setIcon(R.drawable.ic_call_black_24dp);

        alertDialogBuilder.setTitle(spannableString);

        alertDialogBuilder.setPositiveButton(accept, answerCallClickListener);
        alertDialogBuilder.setNegativeButton(reject, cancelClickListener);
        //   alertDialogBuilder.setMessage(callInvite.getFrom() + " is calling.");
        alertDialogBuilder.setMessage(message);

        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setOnCancelListener(DialogInterface::dismiss);

        return alertDialogBuilder.create();
    }

    private void handleIncomingCallIntent(Intent intent) {
        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals(ACTION_INCOMING_CALL)) {
                activeCallInvite = intent.getParcelableExtra(INCOMING_CALL_INVITE);
                if (activeCallInvite != null) {
                    soundPoolManager.playRinging();
                    alertDialog = createIncomingCallDialog(activity,
                            activeCallInvite,
                            answerCallClickListener(),
                            cancelCallClickListener());
                    alertDialog.show();
                    activeCallNotificationId = intent.getIntExtra(INCOMING_CALL_NOTIFICATION_ID, 0);
                } else {

                }
            } else if (intent.getAction().equals(ACTION_CANCEL_CALL)) {
                try {
                    if (!MainJobProgressActivity.this.isFinishing()) {
                        if (alertDialog != null && alertDialog.isShowing()) {
                            soundPoolManager.stopRinging();
                            alertDialog.cancel();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else if (intent.getAction().equals(ACTION_FCM_TOKEN)) {
                retrieveAccessToken();
            }
        }
    }

    private DialogInterface.OnClickListener answerCallClickListener() {
        return (dialog, which) -> {
            soundPoolManager.stopRinging();
            answer();
            setCallUI();
            alertDialog.dismiss();
        };
    }

    private DialogInterface.OnClickListener cancelCallClickListener() {
        return (dialogInterface, i) -> {
            soundPoolManager.stopRinging();
            if (activeCallInvite != null) {
                activeCallInvite.reject(activity);
                notificationManager.cancel(activeCallNotificationId);
            }
            alertDialog.dismiss();
        };
    }

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ACTION_INCOMING_CALL);
            intentFilter.addAction(ACTION_CANCEL_CALL);
            intentFilter.addAction(ACTION_FCM_TOKEN);
            LocalBroadcastManager.getInstance(this).registerReceiver(
                    voiceBroadcastReceiver, intentFilter);
            isReceiverRegistered = true;
        }
    }

    private void unregisterReceiver() {
        if (isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(voiceBroadcastReceiver);
            isReceiverRegistered = false;
        }
    }

    private class VoiceBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_INCOMING_CALL) || action.equals(ACTION_CANCEL_CALL)) {
                /*
                 * Handle the incoming or cancelled call invite
                 */
                handleIncomingCallIntent(intent);
            }
        }
    }


    private void registerForCallInvites() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    // Get new FCM registration token
                    String token = task.getResult();
                    System.out.println("refreshedToken =token=>" + token);
                    final String fcmToken = token;
                    if (fcmToken != null) {
                        System.out.println("accessToken=>" + accessToken);
                        Voice.register(accessToken, Voice.RegistrationChannel.FCM, fcmToken, registrationListener);
                    }
                });


    }

    /*private View.OnClickListener callActionFabClickListener() {
        return v -> {
            toIdentity = "InstaGuard_driver" + strAcceptDriverId;
            params.put("to", toIdentity);
            ConnectOptions connectOptions = new ConnectOptions.Builder(accessToken)
                    .params(params)
                    .build();
            activeCall = Voice.connect(requireActivity, connectOptions, callListener);
            setCallUI();
        };
    }*/

    private View.OnClickListener hangupActionFabClickListener() {
        return v -> {
            soundPoolManager.playDisconnect();
            resetUI();
            disconnect();
        };
    }

    private View.OnClickListener loudspeakerActionFabClickListener() {
        return v -> audioOption();
    }

    private View.OnClickListener holdActionFabClickListener() {
        return v -> hold();
    }

    private View.OnClickListener muteActionFabClickListener() {
        return v -> mute();
    }

    @SuppressLint("StaticFieldLeak")
    private class MyAsyncTask1 extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog dialog;
        private final MainJobProgressActivity requireActivity;

        MyAsyncTask1(MainJobProgressActivity requireActivity) {
            this.requireActivity = requireActivity;
            context = requireActivity;
            dialog = new ProgressDialog(context);
        }

        private Context context;

        protected void onPreExecute() {
            dialog = new ProgressDialog(context);
            dialog.setMessage("loading...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            // dialog.show();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (dialog != null && dialog.isShowing()) {
                if (!requireActivity.isFinishing() && !requireActivity.isDestroyed()) {
                    try {
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (success) {
                System.out.print("success");
            } else {
                System.out.print("failure");
            }
        }

        @Override
        protected Boolean doInBackground(final String... args) {
            try {
                // ... processing ...
                String url = RETROFIT_URL_CALL_CHAT_ACCESS + "?identity=" + Utils.identity + "&device=" + "android_rider";
                URL yahoo = null;
                try {
                    yahoo = new URL(url);
                    BufferedReader in = null;
                    try {
                        in = new BufferedReader(
                                new InputStreamReader(
                                        yahoo.openStream()));

                        String inputLine;

                        while ((inputLine = in.readLine()) != null) {
                            System.out.println("TokenSuccess" + inputLine);
                            requireActivity.accessToken = inputLine;
                            registerForCallInvites();
                        }


                        in.close();

                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                } catch (MalformedURLException ex) {
                    ex.printStackTrace();
                }
                // getOnlineCategoryInfo();
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    private void checkAndRequestPermissions(Context context) {

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            int notificationPermission = ContextCompat.checkSelfPermission(context,
                    Manifest.permission.POST_NOTIFICATIONS);
            if (notificationPermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.POST_NOTIFICATIONS);
            }
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) context, listPermissionsNeeded.toArray(new String[0]), REQUEST_ID_MULTIPLE_PERMISSIONS_N);
        }
    }

    private void displayNeverAskAgainDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Typeface tf = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.App_Font));

        BaseActivity.CustomTFSpan tfSpan = new BaseActivity.CustomTFSpan(tf);
        SpannableString spannableStrings = new SpannableString(getResources().getString(R.string.permit_manually));
        spannableStrings.setSpan(tfSpan, 0, spannableStrings.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString spannableStringss = new SpannableString(getResources().getString(R.string.cancel));
        spannableStringss.setSpan(tfSpan, 0, spannableStringss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString spannableStringssd = new SpannableString(getResources().getString(R.string.ok));
        spannableStringssd.setSpan(tfSpan, 0, spannableStringssd.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString spannableString = new SpannableString(getString(R.string.permit_manually_msg));
        spannableString.setSpan(tfSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        builder.setTitle(spannableStrings);

        builder.setMessage(spannableString);

        builder.setCancelable(false);

        builder.setPositiveButton(spannableStringssd, (dialog, which) -> {
            dialog.dismiss();
            Intent intent = new Intent();
            intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        });
        builder.setNegativeButton(spannableStringss, null);
        builder.show();
    }

    private void showDialogOK(DialogInterface.OnClickListener okListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Typeface tf = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.App_Font));

        BaseActivity.CustomTFSpan tfSpan = new BaseActivity.CustomTFSpan(tf);
        SpannableString spannableStrings = new SpannableString(getResources().getString(R.string.permit_manually));
        spannableStrings.setSpan(tfSpan, 0, spannableStrings.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString spannableStringssd = new SpannableString(getResources().getString(R.string.ok));
        spannableStringssd.setSpan(tfSpan, 0, spannableStringssd.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString spannableStringss = new SpannableString(getResources().getString(R.string.cancel));
        spannableStringss.setSpan(tfSpan, 0, spannableStringss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // SpannableString spannableString = new SpannableString(getString(R.string.permit_manually_msg));
        SpannableString spannableString = new SpannableString(getString(R.string.permit_manually_msg_nec));
        spannableString.setSpan(tfSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        new AlertDialog.Builder(this)
                .setTitle(spannableStrings)
                .setMessage(spannableString)
                .setPositiveButton(spannableStringssd, okListener)
                .setNegativeButton(spannableStringss, okListener)
                .create()
                .show();
    }

    /*Twillio call End*/

    private RecyclerView mMessagesRecyclerView;
    private MessagesAdapter mMessagesAdapter;
    private final ArrayList<Message> mMessages = new ArrayList<>();

    private CustomEditText mWriteMessageEditText;
    //private Button mSendChatMessageButton;
    private ImageView mSendChatMessageButton;
    private CustomTextView empty;
    boolean aBoolean = false;

    private ConversationsClient mConversationsClient;
    private Conversation mGeneralChannel;

    ImageView imgBack;
    String sendDateTime;
    String datetime = "";

    RelativeLayout loading;
    View parentView;
    View lytChat;
    //final static String SERVER_TOKEN_URL = Utils.RETROFIT_URL + "chatAccessToken";
    String accessTokenChat;
    String DEFAULT_CHANNEL_NAME = "";

    private void callTwilioChatSetup() {

        lytChat = findViewById(R.id.lyt_chat);

        imgBack = findViewById(R.id.img_back);
        parentView = findViewById(R.id.rl_coordinate_lyt);
        loading = findViewById(R.id.rl_progress_bar_lyt);

        loading.setVisibility(View.VISIBLE);

        // DEFAULT_CHANNEL_NAME = "Channel:" + customerId + "," + driverId + "," + bookingId;
        Log.e(TAG, "onCreate: DEFAULT_CHANNEL_NAME" + DEFAULT_CHANNEL_NAME);

        mMessagesRecyclerView = (RecyclerView) findViewById(R.id.messagesRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        mMessagesRecyclerView.setLayoutManager(layoutManager);

        mMessagesAdapter = new MessagesAdapter();
        mMessagesRecyclerView.setAdapter(mMessagesAdapter);

        mWriteMessageEditText = findViewById(R.id.writeMessageEditText);
        mSendChatMessageButton = findViewById(R.id.sendChatMessageButton);
        View rl_parent_lyt_chat = findViewById(R.id.rl_parent_lyt_chat);
        empty = findViewById(R.id.empty);

        rl_parent_lyt_chat.setOnClickListener(v -> BaseActivity.hideKeyBoard(MainJobProgressActivity.this));

        SharedPreferences.Editor getState_ = getApplicationContext().getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE).edit();
        getState_.putInt("chat_click", 0);
        getState_.apply();

        imgBack.setOnClickListener(v -> {

            getState_.putInt("chat_click", 1);
            getState_.apply();

            if (lytChat.getVisibility() == View.VISIBLE) {

                // baseActivity.hideKeyBoard(MapsActivity_annotation.this);

                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                lytChat.setVisibility(View.GONE);
            }

        });

        mSendChatMessageButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onClick(View view) {
                mSendChatMessageButton.setEnabled(false);
                if (mGeneralChannel != null) {
                    String messageBody = mWriteMessageEditText.getText().toString();
                    Calendar c = Calendar.getInstance();
                    //   SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd hh:mm a");
                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat df = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        df = new SimpleDateFormat("dd/MM/YYYY hh:mm a");
                    }

                    sendDateTime = df.format(c.getTime());
                    if (mWriteMessageEditText.getText().toString().trim().length() <= 0) {
                        Toast.makeText(activity, getString(R.string.chat_field_empty), Toast.LENGTH_LONG).show();
                        mSendChatMessageButton.setEnabled(true);
                    } else if (mWriteMessageEditText.getText().toString().equals("") || mWriteMessageEditText.length() < 1 || mWriteMessageEditText.getText().toString().equals(" ")) {
                        Toast.makeText(activity, getString(R.string.chat_field_empty), Toast.LENGTH_SHORT).show();
                        mSendChatMessageButton.setEnabled(true);
                    } else {

                        mGeneralChannel.prepareMessage().setBody(messageBody).buildAndSend(new CallbackListener() {
                            @Override
                            public void onSuccess(Object result) {
                                runOnUiThread(() -> {
                                    // need to modify user interface elements on the UI thread
                                    mWriteMessageEditText.setText("");
                                    mSendChatMessageButton.setEnabled(true);
                                    callPushDetails(strAcceptDriverId, messageBody);
                                });
                            }

                            @Override
                            public void onError(ErrorInfo errorInfo) {
                                CallbackListener.super.onError(errorInfo);
                                Log.e(TAG, "Error sending message: " + errorInfo.getMessage());
                            }
                        });

                        Log.d(TAG, "Message created");
                    }
                } else {
                    Log.d(TAG, "onClick: else mGeneralChannel ");
                }
            }
        });


        System.out.println("~~PermissionUtils~~~" + PermissionUtils.checkPermission_Notification(activity));


        if (PermissionUtils.checkPermission_Notification(activity).equals("1")) {
            // success
            if (NotificationManagerCompat.from(activity).areNotificationsEnabled()) {
                // Toast.makeText(this, "Notification is on for this Application", Toast.LENGTH_SHORT).show();
            } else {
                // Toast.makeText(this, "Notification is off for this Application", Toast.LENGTH_SHORT).show();
                displayNeverAskAgainDialog();
            }
        } else if (PermissionUtils.checkPermission_Notification(activity).equals("2")) {
            if (!aBoolean) {
                displayNeverAskAgainDialog();
            }
        } else {
            checkAndRequestPermissions(activity);
        }

    }

    class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView mMessageTime, mMessageDate, rightUser;
            TextView mMessageTextView, mMessageTextView1;
            TextView mMessageTime1, mMessageDate1, leftUser;
            View right, left;

            ViewHolder(View view) {
                super(view);
                right = view.findViewById(R.id.right);
                mMessageTextView = (TextView) view.findViewById(R.id.btn_message_boddy);
                mMessageTime = (TextView) view.findViewById(R.id.txt_message_time);
                rightUser = (TextView) view.findViewById(R.id.txt_message_time_you);
                // mMessageDate = (TextView) view.findViewById(R.id.txt_message_date);

                left = view.findViewById(R.id.left);
                leftUser = view.findViewById(R.id.txt_message_time_from_customer);
                mMessageTextView1 = (TextView) view.findViewById(R.id.btn_message_boddy1);
                mMessageTime1 = (TextView) view.findViewById(R.id.txt_message_time1);
                // mMessageDate1 = (TextView) view.findViewById(R.id.txt_message_date1);

            }
        }

        MessagesAdapter() {
        }

        @NonNull
        @Override
        public MessagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
            View messageTextView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_text_view, parent, false);

            /*TextView messageTextView = (TextView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_text_view, parent, false);*/
            return new MessagesAdapter.ViewHolder(messageTextView);
        }

        @SuppressLint("SimpleDateFormat")
        @Override
        public void onBindViewHolder(@NonNull MessagesAdapter.ViewHolder holder, int position) {
            Message message = mMessages.get(position);

            String messageTexts = String.format(message.getBody());
            //    String messageTime = String.format(String.valueOf(message.getTimeStampAsDate()));
            Log.d(TAG, "Buddy id : " + strAcceptDriverId);
            Log.d(TAG, "User id : " + userId);
            Log.d(TAG, "Author Name : " + message.getAuthor());
            Log.d(TAG, "date created : " + message.getDateCreated());
            Log.d(TAG, "date created : " + message.getDateCreatedAsDate());

            Log.d(TAG, "date created : messageTexts--> " + messageTexts);

            message.getAuthor();
            message.getDateCreatedAsDate();

            @SuppressLint("SimpleDateFormat") SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            //    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            SimpleDateFormat outputFormat = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                outputFormat = new SimpleDateFormat("MM/dd/YYYY hh:mm a");
            }
            Date date = null;

            try {
                date = inputFormat.parse(message.getDateCreatedAsDate().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String formattedDate = outputFormat.format(date);
            Log.d(TAG, "date format=====>>>>>>>>>" + formattedDate);

            if (message.getAuthor().contains(mIdentity)) {

                holder.right.setVisibility(View.VISIBLE);
                holder.left.setVisibility(View.GONE);

                holder.rightUser.setVisibility(View.GONE);
                holder.leftUser.setVisibility(View.GONE);

                holder.mMessageTextView.setText(messageTexts);
                holder.mMessageTime.setText(formattedDate);
                //holder.mMessageDate.setText(messageDate);
            } else {
                holder.right.setVisibility(View.GONE);
                holder.left.setVisibility(View.VISIBLE);

                holder.rightUser.setVisibility(View.GONE);
                holder.leftUser.setVisibility(View.GONE);

                holder.mMessageTextView1.setText(messageTexts);
                holder.mMessageTime1.setText(formattedDate);

                //holder.mMessageDate1.setText(messageDate);
            }
        }

        @Override
        public int getItemCount() {
            return mMessages.size();
        }
    }


    private void retrieveAccessTokenfromServer(/*String from*/String strAcceptDriverId) {

        out.println("~~retrieveAccessTokenfromServer~~count~~>> " + getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE).getInt("chat_click", 0) + " <<<<");

        if (getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE).getInt("chat_click", 0) == 1) {

            /*if (!from.equals("home")) {
                if (lytChat.getVisibility() == View.GONE) {
                    lytChat.setVisibility(View.VISIBLE);
                }
            }else {
                if (lytChat.getVisibility() == View.VISIBLE) {
                    lytChat.setVisibility(View.GONE);
                }
            }*/

            if (lytChat.getVisibility() == View.GONE) {
                lytChat.setVisibility(View.VISIBLE);
            }
        } else {

           /* if (!from.equals("home")) {
                if (lytChat.getVisibility() == View.GONE) {
                    lytChat.setVisibility(View.VISIBLE);
                }
            } else {*/

            DEFAULT_CHANNEL_NAME = "Channel:" + userId + "," + strAcceptDriverId + "," + strBookingId;
            mIdentity = "customer" + strAcceptDriverId + "InstaGuard";
            mDriverId = "driver" + strAcceptDriverId + "InstaGuard";

            APIInterface apiInterface = APIClient.getClient_chat().create(APIInterface.class);
            Call<ChatAccessModel> call2 = apiInterface.doChatAccessToken(userId, mIdentity);
            showProgressDialog(activity);
            call2.enqueue(new Callback<ChatAccessModel>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NonNull Call<ChatAccessModel> call, @NonNull Response<ChatAccessModel> response) {
                    hideProgressDialog();
                    if (response.body() != null) {
                        String accessToken = response.body().getToken();
                        out.println("accessToken::"+accessToken);
                        setTitle("Chat");
                        ConversationsClient.Properties props = ConversationsClient.Properties.newBuilder().createProperties();
                        ConversationsClient.create(activity, accessToken, props, mConversationsClientCallback);

                        Log.e(TAG, "onCreate: DEFAULT_CHANNEL_NAME" + DEFAULT_CHANNEL_NAME);

                        //if (!from.equals("home")) {
                        if (lytChat.getVisibility() == View.GONE) {
                            lytChat.setVisibility(View.VISIBLE);
                        }
                        //}
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ChatAccessModel> call, @NonNull Throwable t) {
                    call.cancel();
                    hideProgressDialog();
                    loading.setVisibility(View.GONE);
                    Toast.makeText(activity, R.string.error_retrieving_access_token, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // 3rd
    private final CallbackListener<ConversationsClient> mConversationsClientCallback = new CallbackListener<ConversationsClient>() {
        @Override
        public void onSuccess(ConversationsClient conversationsClient) {
            mConversationsClient = conversationsClient;
            conversationsClient.addListener(mConversationsClientListener);
        }

        @Override
        public void onError(ErrorInfo errorInfo) {
            Log.e(TAG, "Error creating Twilio Chat Client: " + errorInfo.getMessage());
        }
    };

    //4th
    private final ConversationsClientListener mConversationsClientListener = new ConversationsClientListener(){

        @Override
        public void onConversationAdded(Conversation conversation) {

        }

        @Override
        public void onConversationUpdated(Conversation conversation, Conversation.UpdateReason reason) {

        }

        @Override
        public void onConversationDeleted(Conversation conversation) {

        }

        @Override
        public void onConversationSynchronizationChange(Conversation conversation) {

        }

        @Override
        public void onError(ErrorInfo errorInfo) {

        }

        @Override
        public void onUserUpdated(User user, User.UpdateReason reason) {

        }

        @Override
        public void onUserSubscribed(User user) {

        }

        @Override
        public void onUserUnsubscribed(User user) {

        }

        @Override
        public void onClientSynchronization(ConversationsClient.SynchronizationStatus synchronizationStatus) {
            if (synchronizationStatus == ConversationsClient.SynchronizationStatus.COMPLETED) {
                loadChannels();
            }
        }

        @Override
        public void onNewMessageNotification(String conversationSid, String messageSid, long messageIndex) {

        }

        @Override
        public void onAddedToConversationNotification(String conversationSid) {

        }

        @Override
        public void onRemovedFromConversationNotification(String conversationSid) {

        }

        @Override
        public void onNotificationSubscribed() {

        }

        @Override
        public void onNotificationFailed(ErrorInfo errorInfo) {

        }

        @Override
        public void onConnectionStateChange(ConversationsClient.ConnectionState state) {

        }

        @Override
        public void onTokenExpired() {

        }

        @Override
        public void onTokenAboutToExpire() {

        }
    };

    //5th
    private void loadChannels() {

        Log.d(TAG, "loadChannels-->" + DEFAULT_CHANNEL_NAME);

        mConversationsClient.getConversation(DEFAULT_CHANNEL_NAME, new CallbackListener<Conversation>() {
            @Override
            public void onSuccess(Conversation conversation) {
                Log.i(TAG, "onSuccess: load channel---->" + conversation.getStatus() + "~~~" + conversation.getFriendlyName() + "~~~" + conversation.getUniqueName());

                if (conversation != null) {
                    if (conversation.getStatus() == Conversation.ConversationStatus.JOINED
                        /*|| conversation.getStatus() == Conversation.ConversationStatus.NOT_PARTICIPATING*/) {
                        Log.d(TAG, "Already Exists in Conversation: " + DEFAULT_CHANNEL_NAME);
                        mGeneralChannel = conversation;
                        out.println("mGeneral Channel "+ mGeneralChannel);

                        conversation.addListener(mDefaultChannelListener);

                        if (conversation.getSynchronizationStatus() == Conversation.SynchronizationStatus.ALL) {
                            loadPreviousMessages(conversation);
                        }

                        out.println("Join Conversation Listened "+ conversation.getStatus() + " "+ conversation.getStatus().JOINED );
                    } else {
                        Log.d(TAG, "Joining Conversation: " + DEFAULT_CHANNEL_NAME);
                        joinChannel(conversation);
                    }
                } else {
                    Log.i(TAG, "onSuccess: create channel");
                    createchannel();
                }
            }

            @Override
            public void onError(ErrorInfo errorInfo) {
                Log.e(TAG, "Error retrieving channel: " + errorInfo.getMessage());
                createchannel();
            }

        });

    }

    //6th
    private void loadPreviousMessages(final Conversation conversation) {
        conversation.getLastMessages(100, new CallbackListener<List<Message>>() {
            @Override
            public void onSuccess(List<Message> messages) {
                Log.d(TAG, "onSuccess: message list " + messages);
                mMessages.addAll(messages);
                mMessagesAdapter.notifyDataSetChanged();
                mMessagesRecyclerView.scrollToPosition(mMessagesAdapter.getItemCount() - 1);
                loading.setVisibility(View.GONE);
                if (mMessages.size() > 0) {
                    empty.setVisibility(View.GONE);
                } else {
                    empty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(ErrorInfo errorInfo) {
                CallbackListener.super.onError(errorInfo);
                out.println("load Msg data error " + errorInfo.toString());
            }
        });
    }

    //7th
    private void joinChannel(Conversation conversation) {

        loading.setVisibility(View.GONE);

        Log.d(TAG, "Joining Channel getUniqueName: " + "~~~~" + conversation.getStatus() + "~~" + conversation.getUniqueName() + "~~~~~~" + DEFAULT_CHANNEL_NAME);
        Log.d(TAG, "Joining Channel getUniqueName: " + "~~~" + conversation);
        Log.d(TAG, "channel Id from join Conversation " + "~~~" + conversation.getSid().toString());

        if (conversation.getStatus() == Conversation.ConversationStatus.JOINED) {
            // already in the channel, load the messages
            mGeneralChannel.addListener(mDefaultChannelListener);
            out.println("Join Conversation Listened "+ conversation.getStatus() + " "+ Conversation.ConversationStatus.JOINED );
        } else {

            //  Log.i(TAG, "joinChannel: get memeners-->"+channel.getMembers().toString());
            // join the channel
            conversation.join(new StatusListener() {
                @Override
                public void onSuccess() {
                    mGeneralChannel = conversation;
                    out.println("mGeneralChannel "+ mGeneralChannel);
                    mGeneralChannel.setUniqueName(DEFAULT_CHANNEL_NAME, new StatusListener() {
                        @Override
                        public void onSuccess() {
                            Log.d(TAG, "Joined default channel" + conversation);
                            mGeneralChannel.addListener(mDefaultChannelListener);
                        }
                    });
                }

                @Override
                public void onError(ErrorInfo errorInfo) {
                    Log.e(TAG, "Error joining channel: " + errorInfo.getMessage());

                    mGeneralChannel = conversation;
                    out.println("mGeneralChannelError "+ mGeneralChannel);

                    mGeneralChannel.setUniqueName(DEFAULT_CHANNEL_NAME, () -> {
                        Log.d(TAG, "Joined default channel" + conversation);
                        mGeneralChannel.addListener(mDefaultChannelListener);

                        conversation.getLastMessages(100, new CallbackListener<List<Message>>() {
                            @SuppressLint("NotifyDataSetChanged")
                            @Override
                            public void onSuccess(List<Message> messages) {
                                Log.d(TAG, "onSuccess: message list " + messages.toString());
                                mMessages.addAll(messages);
                                mMessagesAdapter.notifyDataSetChanged();
                                mMessagesRecyclerView.scrollToPosition(mMessagesAdapter.getItemCount() - 1);
                                //hideProgressDialog();
                                loading.setVisibility(View.GONE);
                                if (mMessages.size() > 0) {
                                    empty.setVisibility(View.GONE);
                                } else {
                                    empty.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    });
                }
            });
        }

    }

    //8th
    private void createchannel() {
        Log.d(TAG, "Creating Conversation: " + DEFAULT_CHANNEL_NAME);

        mConversationsClient.createConversation(DEFAULT_CHANNEL_NAME, new CallbackListener<Conversation>() {
            @Override
            public void onSuccess(Conversation conversation) {
                if (conversation != null) {
                    Log.d(TAG, "Joining Conversation in Create channel : " + DEFAULT_CHANNEL_NAME);
                    Log.d(TAG, "Accepted driver id : " + strAcceptDriverId);

                    conversation.setUniqueName(DEFAULT_CHANNEL_NAME, new StatusListener() {
                        @Override
                        public void onSuccess() {
                            out.println("twilio driver unique name was set");
                        }
                    });

                    conversation.addParticipantByIdentity( mDriverId, null, new StatusListener() {
                        @Override
                        public void onSuccess() {
                            // Participant added successfully

                            Log.d("Twilio", "Participant added: " +  mDriverId);
                        }

                        @Override
                        public void onError(ErrorInfo errorInfo) {
                            StatusListener.super.onError(errorInfo);
                            out.println("create channel error "+ errorInfo.toString());
                        }
                    });

                    joinNewConv(conversation);

                }
            }

            @Override
            public void onError(ErrorInfo errorInfo) {
                Log.e(TAG, "Error creating conversation: " + errorInfo.getMessage());
            }
        });
    }

    //9th
    private void joinNewConv(final Conversation conversation) {
        Log.d(TAG, "Joining Conversation: " + conversation.getUniqueName());
        out.println("new conversion satus "+ conversation.getStatus());

        if (conversation.getStatus() == Conversation.ConversationStatus.JOINED) {
            DEFAULT_CHANNEL_NAME = "Channel:" + userId + "," + strAcceptDriverId + "," + strBookingId;
            mIdentity = "customer" + strAcceptDriverId + "InstaGuard";
            mDriverId = "driver" + strAcceptDriverId + "InstaGuard";

            mGeneralChannel = conversation;
            Log.d(TAG, "Already joined default conversation");
            conversation.addListener(mDefaultChannelListener);
            return;
        }


        conversation.join(new StatusListener() {
            @Override
            public void onSuccess() {
                mGeneralChannel = conversation;
                Log.d(TAG, "Joined default conversation");
                conversation.addListener(mDefaultChannelListener);
                loadPreviousMessages(conversation);
            }

            @Override
            public void onError(ErrorInfo errorInfo) {
                Log.e(TAG, "Error joining conversation: " + errorInfo.getMessage());
            }
        });
    }

    //10th
    private final ConversationListener mDefaultChannelListener = new ConversationListener() {

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onMessageAdded(final Message message) {
            Log.d(TAG, "Message added" + message);
            runOnUiThread(() -> {
                // need to modify user interface elements on the UI thread
                mSendChatMessageButton.setEnabled(true);
                mMessages.add(message);
                out.println("Added Msg "+ message.toString());
                mMessagesAdapter.notifyDataSetChanged();
                mMessagesRecyclerView.scrollToPosition(mMessagesAdapter.getItemCount() - 1);
                if (mMessages.size() > 0) {
                    empty.setVisibility(View.GONE);
                } else {
                    empty.setVisibility(View.VISIBLE);
                }
            });
        }

        @Override
        public void onMessageUpdated(Message message, Message.UpdateReason updateReason) {
            Log.i(TAG, "onMessageUpdated: ---" + message.toString());
        }


        @Override
        public void onMessageDeleted(Message message) {
            Log.d(TAG, "Message deleted");
        }

        @Override
        public void onParticipantAdded(Participant participant) {

        }

        @Override
        public void onParticipantUpdated(Participant participant, Participant.UpdateReason reason) {

        }

        @Override
        public void onParticipantDeleted(Participant participant) {

        }

        @Override
        public void onTypingStarted(Conversation conversation, Participant participant) {

        }

        @Override
        public void onTypingEnded(Conversation conversation, Participant participant) {

        }

        @Override
        public void onSynchronizationChanged(Conversation conversation) {
            Log.d(TAG, "synchronation list channel " + mGeneralChannel.getStatus());
            String status = String.valueOf(mGeneralChannel.getStatus());
            String msg = String.valueOf(mGeneralChannel);
            out.println("channel sid "+ mGeneralChannel.getSid());
            out.println("channel fName "+ mGeneralChannel.getFriendlyName());
            Log.d(TAG, "synchronation status channel " + status);
            Log.d(TAG, "synchronation msg channel " + msg);

            if (status.equals("JOINED")) {
                out.println("entered in joined sync "+ msg.length());
            } else {
                loading.setVisibility(View.GONE);
            }
        }
    };

    @SuppressLint("LongLogTag")
    private void callPushDetails(String userId, String message) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("id", userId);
        map.put("type", "driver");
        map.put("message", message);
        if (!strAcceptBookingId.equals("")) {
            // mode - call/chat
            map.put("booking_id", strAcceptBookingId);
            map.put("mode", "chat");
        }
        Call<BankModel> call2 = apiInterface.addSendMessageNotify(userAccessToken, map);
        call2.enqueue(new Callback<BankModel>() {
            @Override
            public void onResponse(@NonNull Call<BankModel> call, @NonNull Response<BankModel> response) {
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    if (response.body() != null && response.body().getMessage().equals("success")) {
                        System.out.println("~~~");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<BankModel> call, @NonNull Throwable t) {
                hideProgressDialog();
                loading.setVisibility(View.GONE);
                call.cancel();
            }
        });
    }


    private void onGooglePayReady(boolean isReady) {
        out.println("~~~~~onGooglePayReady~~" + isReady);
        //googlePayButton.setEnabled(isReady);
    }

    private void onGooglePayResult(@NotNull GooglePayPaymentMethodLauncher.Result result) {
        out.println("~~~~~result~~" + result);
        if (result instanceof GooglePayPaymentMethodLauncher.Result.Completed) {
            // Payment details successfully captured.
            // Send the paymentMethodId to your server to finalize payment.
            final String paymentMethodId =
                    ((GooglePayPaymentMethodLauncher.Result.Completed) result).getPaymentMethod().id;
            out.println("~~~~~result~~paymentMethodId-->>>" + paymentMethodId);

        } else if (result instanceof GooglePayPaymentMethodLauncher.Result.Canceled) {
            // User canceled the operation
            out.println("~~~~~Canceled~~");
        } else if (result instanceof GooglePayPaymentMethodLauncher.Result.Failed) {
            // Operation failed; inspect `result.getError()` for the exception
            out.println("~~~~~Failed~~");
        }
    }

    boolean isReadyGPay = false;

    @SuppressLint("SetTextI18n")
    public void onGooglePayReady_(boolean isReady) {
        isReadyGPay = isReady;
      /*  out.println("~~~~~onGooglePayReady~~isReady" + isReady);
        googlePayButton.setEnabled(isReady);
        if (!isReady) {
            BaseActivity.callClickAction(rbGooglePayPayment, false);
            rbGooglePayPayment.setText("Google Pay" + " (Not in device)");
        }*/
    }

    public void onGooglePayResult_(@NotNull GooglePayLauncher.Result result) {
        out.println("~~~~~result~isReady~" + result);
        if (result instanceof GooglePayLauncher.Result.Completed) {
            // Payment details successfully captured.
            // Send the paymentMethodId to your server to finalize payment.
            out.println("~~~~~Completed~~");

            baseActivity.showSnackbar(this,contentView,"Payment authorized successfully.","other");

            new Handler().postDelayed(() -> {
                if (BaseActivity.getSetUpIntentOneSave_(activity, "InstaGuard" + userId).equals("save_card")) {
                    CallRequestBooking("save_card_gpay", paymentId);
                } else {
                CallRequestBooking("one_time_gpay", paymentId);
                }
            }, 2000);

        } else if (result instanceof GooglePayLauncher.Result.Canceled) {
            // User canceled the operation

            baseActivity.showSnackbar(this,contentView,"Canceled","warning");

            out.println("~~~~~Canceled~~");
        } else if (result instanceof GooglePayLauncher.Result.Failed) {
            // Operation failed; inspect `result.getError()` for the exception

            baseActivity.showSnackbar(this,contentView,((GooglePayLauncher.Result.Failed) result).getError().getMessage(),"warning");
            out.println("~~~~~Failed~~");
        }
    }


    private void callRequestPaymode(String paymode) {

        switch (paymode) {
            case "card":
                if ("save_card".equals("save_card")) {
                    if (getOverallTotal != null && !getOverallTotal.equals("0")) {
                        callSaveUpdateDetails(getOverallTotal, "stripe");
                    } else {
                        Toast.makeText(activity, getResources().getString(R.string.total_amount_empty_try_again), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (getOverallTotal != null && !getOverallTotal.equals("0")) {
                        callUpdateStripAlsoSaveCard(getOverallTotal);
                    } else {
                        Toast.makeText(activity, getResources().getString(R.string.total_amount_empty_try_again), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case "paypal":
                try {
                    if (getOverallTotal != null && !getOverallTotal.equals("0")) {
                        BaseActivity.callClickAction(btnConfirmExtendPayment, false);
                        callCreatePayPalPayment(getOverallTotal);
                    } else {
                        callClickAction(btnConfirmExtendPayment, true);
                        Toast.makeText(activity, getResources().getString(R.string.total_amount_empty_try_again), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    hideProgressDialog();
                    callClickAction(btnConfirmExtendPayment, true);
                    System.out.println("~~~Exception~~~btnConfirmExtendPayment~~~" + e.toString());
                }
                break;
            case "google_pay":
                if ("save_card".equals("save_card")) { //***************Not workinng***********************
                    if (getOverallTotal != null && !getOverallTotal.equals("0")) {
                        callSaveUpdateDetails(getOverallTotal, "google_pay");
                    } else {
                        Toast.makeText(activity, getResources().getString(R.string.total_amount_empty_try_again), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (getOverallTotal != null && !getOverallTotal.equals("0")) {
                        BaseActivity.callClickAction(btnConfirmExtendPayment, false);
                        callPaymentIntent_API(getOverallTotal, "0", "google_pay");
                    } else {
                        Toast.makeText(activity, getResources().getString(R.string.total_amount_empty_try_again), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case "wallet":
                CallRequestBooking("wallet", "use_wallet");
                break;
        }
    }

    RecyclerView rcvSelectPayment;
    PaymentAdapter paymentAdapter;
    List<InternalPaymentMethod_> paymentMethodList;

    private void callCreatePaymentMethod_() {

        if (paymentMethodList != null) {
            paymentMethodList.clear();
        }

        paymentMethodList = new ArrayList<>();

        InternalPaymentMethod_ cardPayment, payPalPayment, gPayPayment, walletPayment;

        if (isReadyGPay) {
            gPayPayment = new InternalPaymentMethod_("3", getResources().getString(R.string.pay_via_google_pay), "gpay", 1);
        } else {
            gPayPayment = new InternalPaymentMethod_("3", getResources().getString(R.string.pay_via_google_pay), "gpay", 0);
        }
        paymentMethodList.add(gPayPayment);

        cardPayment = new InternalPaymentMethod_("1", getResources().getString(R.string.select_payment_pay_online), "card", 1);
        paymentMethodList.add(cardPayment);

        if (activity.getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE)
                .getString(Utils.SP_COMMON_ADMIN_PAYPAL_STATUS, "0").equals("0")) { // Paypal Disable
            payPalPayment = new InternalPaymentMethod_("2", getResources().getString(R.string.select_payment_paypal), "paypal", 0);
        } else { // Paypal Enable
            payPalPayment = new InternalPaymentMethod_("2", getResources().getString(R.string.select_payment_paypal), "paypal", 1);
        }
        paymentMethodList.add(payPalPayment);

        if (!activity.getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE)
                .getBoolean(Utils.SP_COMMON_WALLET_STATUS, false)) { // Wallet Disable
            walletPayment = new InternalPaymentMethod_("4", getResources().getString(R.string.select_payment_my_wallet), "wallet", 0);
        } else { // Wallet Enable
            walletPayment = new InternalPaymentMethod_("4", getResources().getString(R.string.select_payment_my_wallet), "wallet", 1);
        }
        paymentMethodList.add(walletPayment);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        rcvSelectPayment.setLayoutManager(linearLayoutManager);

        paymentAdapter = new PaymentAdapter(activity, paymentMethodList);
        rcvSelectPayment.setAdapter(paymentAdapter);
        paymentAdapter.selected(-1);

        setVisibility(lytCardPayment, true);

    }

}
