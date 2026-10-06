package com.instaguard.user.Activities;

import static com.instaguard.user.Activities.BaseActivity.callView_;
import static com.instaguard.user.Activities.BaseActivity.convertHoursToDaysHoursMinutes;
import static com.instaguard.user.Activities.BaseActivity.hideProgressDialog;
import static com.instaguard.user.Activities.BaseActivity.saveHashMap;
import static com.instaguard.user.Activities.BaseActivity.saveHashMap_only;
import static com.instaguard.user.Activities.BaseActivity.setHashMapPaymentId;
import static com.instaguard.user.Activities.BaseActivity.setHashMapSaveCardStatus;
import static com.instaguard.user.Activities.BaseActivity.setSetUpIntentOneSave_;
import static com.instaguard.user.Activities.BaseActivity.setVisibility;
import static com.instaguard.user.Activities.BaseActivity.showProgressDialog;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_200;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_201;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_401;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_500;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_501;
import static java.lang.System.out;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Picture;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.facebook.BuildConfig;
import com.facebook.ads.AdView;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.instaguard.user.Adapter.PaymentAdapter;
import com.instaguard.user.Adapter.ProviderListAdapter;
import com.instaguard.user.CustomViews.CustomButton;
import com.instaguard.user.CustomViews.CustomEditText;
import com.instaguard.user.CustomViews.CustomTextView;
import com.instaguard.user.Helpers.Creditcard.fields.CreditCardModule;
import com.instaguard.user.Helpers.DecimalUtils;
import com.instaguard.user.Helpers.DistanceCalculator;
import com.instaguard.user.Helpers.Localizationactivity.ui.LocalizationActivity;
import com.instaguard.user.Helpers.RatingBar.SimpleRatingBar;
import com.instaguard.user.Helpers.RecyclerViewsTools.RecyclerTouchListener;
import com.instaguard.user.Helpers.Retrofit.APIClient;
import com.instaguard.user.Helpers.Retrofit.APIInterface;
import com.instaguard.user.Helpers.RippleBackground.RippleBackground;
import com.instaguard.user.Helpers.Utils;
import com.instaguard.user.Models.AppSettingWebModel;
import com.instaguard.user.Models.Bank.BankModel;
import com.instaguard.user.Models.Banner.BannerData;
import com.instaguard.user.Models.Banner.BannerModel;
import com.instaguard.user.Models.CheckPromoModel;
import com.instaguard.user.Models.FareEstimate.FareEstimateModel;
import com.instaguard.user.Models.InternalPaymentMethod_;
import com.instaguard.user.Models.PayPalCreateModel;
import com.instaguard.user.Models.Payment.PaymentIntentModel;
import com.instaguard.user.Models.Payment.SaveWalletPaymentIntentModel;
import com.instaguard.user.Models.ProviderListData;
import com.instaguard.user.Models.PushModel;
import com.instaguard.user.Models.Request.SendRequestModel;
import com.instaguard.user.Models.ScheduleRequestModel;
import com.instaguard.user.R;
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
import com.stripe.android.payments.paymentlauncher.PaymentLauncher;
import com.stripe.android.payments.paymentlauncher.PaymentResult;
import com.stripe.android.view.CardInputWidget;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProviderListActivity extends LocalizationActivity implements GeoQueryEventListener
        , ProviderListAdapter.SuccessResponse, SwipeRefreshLayout.OnRefreshListener {


    Context requireActivity;

    AdView adViewFacebook;
    com.google.android.gms.ads.AdView adViewGoogle;

    LinearLayout adContainer;

    ImageButton imgbtnRefresh, imgbtnBack;
    CustomTextView txtProviderListHeader;
    RecyclerView rvProviderList;
    LinearLayout llNoProvider;
    SwipeRefreshLayout swipeRefresh;
    View view;
    RelativeLayout progressBar;

    int counts = 30;

    //Geo fire
    GeoFire geoFire;
    GeoQuery geoQuery;
    public DataSnapshot dataSnapshotAllDriver;
    private ArrayList<ProviderListData> providerListData = new ArrayList<>();
    ProviderListAdapter providerListAdapter;

    private LatLng currentLatLng = new LatLng(0.0, 0.0);

    // Dialog dailogBookingDetails, dialogRequesting,dailogDriverDetails,dailogAcceptDriver,dialogAcceptSchedule,dialogSelectPayment,dialogPayOnline;
    private View dailogBookingDetails, dialogRequesting, dailogDriverDetails, dialogAcceptSchedule, dialogSelectPayment, dialogPayOnline;
    private RelativeLayout rlParentLytBookingDetails, rlParentLytRequest, rlParentLytRequesting, rlParentLytDriverDetails, rlParentLytAcceptSchedule, rlParentLytSelectPayment;


    //Request Booking
    CustomButton btnBookingDetailsContinue;
    CustomTextView txtServiceItemLabel;
    CustomTextView txtServiceItem;
    CustomTextView txtProvidernNameLabel;
    CustomTextView txtProvidernName;
    CustomTextView txtBookingLocatoin;
    CustomTextView txtApplyPromo;
    CustomTextView txtPromoCodeExpDate;
    CustomTextView txtPromoCodeDiscount;
    CustomTextView txtPromoCodeDescr;
    CustomTextView txtPromoCodeStatus;
    CustomTextView txtPromoCodeCancel;
    CustomTextView txtPromoCode;
    CustomTextView txtAgentCost;
    CustomTextView txtDaysCost;
    CustomEditText edPromo, txtComment;
    ImageButton imgbtnBackBookingDetails;
    LinearLayout llPromocodeViewDetails;


    LinearLayout llBookingEndDate;
    LinearLayout llBookingEndTime;

    CustomTextView txtBookingTimeLabel;
    CustomTextView txtBookingDateLabel;
    CustomTextView txtBookingDate;
    CustomTextView txtBookingTime;

    CustomTextView txtBookingEndDateLabel;
    CustomTextView txtBookingEndTimeLabel;
    CustomTextView txtBookingEndDate;
    CustomTextView txtBookingEndTime;
    CustomTextView txtEmergencyInstruction;
    CustomTextView txtNoOfGuardsCount;

    Timer mTimerCountPluse;

    //Requesting Dialog
    RippleBackground rippleBackground;
    public ImageView ivCancelRequest;
    private CustomTextView btnCancelRequest;
    private CustomTextView txtCountTimer;

    //Requesting Accept Dialog
    public View dailogAcceptDriver;
    public CustomTextView txtViewOnGoingJobs, txtCancel;
    public CustomTextView txtViewOnGoingJobs1, txtCancel1;
    public RelativeLayout rlSuccess, rlSuccess1, rlParentLytAcceptDriver;
    public static Boolean booleanCheckStatusAccepted = false;

    //Schedule Requesting Accept Dialog
    CustomTextView txtBookingSchedule;

    //Select Payment
    RecyclerView rcvSelectPayment;
    PaymentAdapter paymentAdapter;
    List<InternalPaymentMethod_> paymentMethodList;

    RadioGroup rGSelectPayment;
    RadioButton rbPaypalPayment;
    RadioButton rbGooglePayPayment;
    RadioButton rbStripePayment;
    ImageButton imgbtnBackSelectPayment;
    CustomButton btnBookNow;
    CustomTextView txtTotalFarePayment;
    CustomTextView txtSubscribeFarePayment;
    CustomTextView txtBaseFarePayment;
    CustomTextView txtGuardFarePayment;
    CustomTextView txtTotalHoursPayment;
    CustomTextView txtVATFareLabelPayment;
    CustomTextView txtVATFarePayment;

    //Payonline
    LinearLayout llStripeContent;
    CustomButton btnAddcardPayOnline;
    //CardInputWidget creditCardModuleStripe;
    // Card cardToSave;

    ImageButton imgbtnBackPayOnline;
    CreditCardModule creditCardModule;

    String creditCardNumber, creditCardMonth, creditCardYear, creditCardCvv, stripeTokenID = "", status, message, stripeKey;

    RadioButton rbSelectedPaymentId;
    public int selectedId = -1;

    //Driver Details Dailog
    SimpleDraweeView imgProviderProfile;
    CustomButton btnNextDriverView;
    CustomTextView txtMiles, txtDescription, txtProviderName, txtProviderPrice;
    SimpleRatingBar rbProviderRating;
    ImageView imgClose;

    String strDriverId, strFname, strLname, strDate, strTime, strMiles, strProPic, strServiceDes, strRating;

    public String TAG = "ProviderListFragment";
    private boolean add = false;
    DecimalFormat format = new DecimalFormat("0.#");

    public ProviderListActivity() {
        // Required empty public constructor
    }

    String todayDate, todayTime;
    SharedPreferences sprefs, getspCurrency, spAcceptDetails;
    SharedPreferences.Editor putSpState, getState;
    String categoryId, subCategoryId, categoryNameAr, categoryName, subCategoryNameAr, subCategoryName, subCategoryPrice, tripType, availableScheduleTripDriver;
    String strSpecialIns = "-";

    String strLanguage, strCurrencySymbol, strCurrency, strLanguageShortName;
    String strPickupAddress;
    String strPickupLat;
    String strPickupLng;
    String strDropAddress;
    String strDropLat;
    String strDropLng;
    String strTotalEstAmt;
    String strDistance;

    String strPickupDate;
    String strPickupTime;
    String strPickupDateEnd;
    String strPickupTimeEnd;
    String strPickupDate_LABEL;
    String strPickupTime_LABEL;
    String strPickupDateEnd_LABEL;
    String strPickupTimeEnd_LABEL;
    String strEmergencyMsg;
    String strNoOfGuardCount;
    String userId, userAccessToken, strFNameCustomer, strLNameCustomer, strCustomerEmail;
    ArrayList<String> uniqueIdList;

    public static CountDownTimer countDownTimer;

    DatabaseReference requestAcceptrDatabaseBRef;
    ValueEventListener requestAccepValueEventListener;
    Boolean boolStatusAccepted = true;
    public String bookingId;

    DatabaseReference customerWalletDatabaseBRef;
    ValueEventListener customerWalletValueEventListener;

    public String strRatingCustomer, walletAmount;
    String strWalletAmtMin;

    boolean boolPromoAccepted = false;
    private String ValidPromoCode = "";

    DatabaseReference providerListDatabaseRef;
    ValueEventListener providerListValueEventListener;

    DatabaseReference providerLocationListDatabaseRef;
    ValueEventListener providerLocationListValueEventListener;

    DatabaseReference userWalletDatabaseBRef;
    ValueEventListener userWalletValueEventListener;

    LatLng driverCurrentLatLng;

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

    String DISTANCE_LIMIT;
    Integer Distance;

    String categoryCount, categoryDays, categoryAgentPrice, categoryDayPrice;
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
    String getSubscriptionAmount = "0";
    String getSubscriptionPercentage = "0";
    BaseActivity baseActivity;
    FrameLayout contentView;
    ArrayList<String> getUniqueIdList = new ArrayList<>();
    String sendUniqueIdList;
    public static StringBuilder result_ = new StringBuilder(); // for clear value after trip accept or timeout we empty in home page

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_providerlist);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        requireActivity = ProviderListActivity.this;

        apiInterface = APIClient.getClient().create(APIInterface.class);

        baseActivity = new BaseActivity();

       /* if (getArguments() != null) {

            categoryCount = getArguments().getString(Utils.B_CATEGORY_COUNT);
            categoryDays = getArguments().getString(Utils.B_CATEGORY_DAYS);
            categoryAgentPrice = getArguments().getString(Utils.B_CATEGORY_AGENT_PRICE);
            categoryDayPrice = getArguments().getString(Utils.B_CATEGORY_DAYS_PRICE);

        }
        Log.e(TAG, "onCreate: categoryAgentPrice " + categoryAgentPrice);
        Log.e(TAG, "onCreate: categoryDays " + categoryDays);
    }

    @SuppressLint({"CommitPrefEdits", "SetTextI18n"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {*/
        // Inflate the layout for this fragment
        //view = inflater.inflate(R.layout.fragment_providerlist, container, false);

        callSharedPreferences();

        initViews();

        txtServiceItemLabel.setText(categoryName);
        txtServiceItem.setText(subCategoryName);
        txtBookingLocatoin.setText(strPickupAddress);
        txtEmergencyInstruction.setText(strEmergencyMsg);
        out.println("guardsCount-->"+strNoOfGuardCount);

        if (strNoOfGuardCount.equals("1")) {
            txtNoOfGuardsCount.setText(strNoOfGuardCount + " " + getString(R.string.guard));
        } else {
            txtNoOfGuardsCount.setText(strNoOfGuardCount + " " + getString(R.string.guards));
        }

        if (tripType.equals("scheduletrip")) {

            llBookingEndDate.setVisibility(View.VISIBLE);
            llBookingEndTime.setVisibility(View.VISIBLE);
            txtBookingDateLabel.setText(getResources().getString(R.string.start_date));
            txtBookingTimeLabel.setText(getResources().getString(R.string.start_time));


            txtBookingDate.setText(strPickupDate_LABEL);
            txtBookingTime.setText(strPickupTime_LABEL);
            txtBookingEndDate.setText(strPickupDateEnd_LABEL);
            txtBookingEndTime.setText(strPickupTimeEnd_LABEL);

            btnBookNow.setText(getResources().getString(R.string.confirm) + " " + getResources().getString(R.string.book_a_security_guard1));

        } else {

            btnBookNow.setText(getResources().getString(R.string.confirm) + " " + getResources().getString(R.string.sos_emergency_1));

            llBookingEndDate.setVisibility(View.GONE);
            llBookingEndTime.setVisibility(View.GONE);

            txtBookingDateLabel.setText(getResources().getString(R.string.booking_details_booking_date));
            txtBookingTimeLabel.setText(getResources().getString(R.string.booking_details_booking_time));
            todayDate = getCurrentDate();
            todayTime = getCurrentTime();
            txtBookingDate.setText(todayDate);
            txtBookingTime.setText(todayTime);

            /*mTimerCountPluse = new Timer();
            mTimerCountPluse.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(() -> {
                        Date calendar = Calendar.getInstance().getTime();
                        String time = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar);
                        out.println("Prnt Tme -- >"+time);
                        txtBookingTime.setText(time);
                    });
                }
            }, 20, 1000);*/

        }

        //CountDown Entailment
        countdownIntailze();

        //getAllDriversFirebase();

        callAddCardsView();

//        callPayPalIntegration();

        OnClick();

        getSharPref();

        getCustomerWalletAmount();

        getrequestAcceptLisetner();

        if (providerListData.size() == 0) {
            rvProviderList.setVisibility(View.GONE);
            llNoProvider.setVisibility(View.VISIBLE);
        }

        callSlideBannerImage("8");

        BaseActivity baseActivity = new BaseActivity();
        adContainer = findViewById(R.id.banner_container);
        baseActivity.callFacebookAdsCommon(requireActivity, adViewFacebook, adContainer);

        adViewGoogle = findViewById(R.id.adView);
        baseActivity.callGoogleAdsCommon(requireActivity, adViewGoogle);

        //onBackButton();
        //return view;

    }

    public void getSharPref() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);

        String jsonString = sharedPreferences.getString("uniqueIdList", null);

        if (jsonString != null) {
            try {

                JSONArray jsonArray = new JSONArray(jsonString);

                ArrayList<String> list = new ArrayList<>();
                for (int i = 0; i< jsonArray.length(); i++) {
                    list.add(jsonArray.get(i).toString());
                }

                /*StringBuilder builder = new StringBuilder("\"");

                for (int i = 0; i < list.size(); i++) {
                    builder.append(list.get(i));
                    // Add a comma if it's not the last element
                    if (i < list.size() - 1) {
                        builder.append(",");
                    } else {
                        builder.append("\"");
                    }
                }

                sendUniqueIdList = builder.toString();*/

                sendUniqueIdList = String.join(",",list);

                out.println("get unique id list "+ uniqueIdList + " ~~~ " + sendUniqueIdList.getClass().getName());

                /*result_.append("[");
                for (int i = 0; i < jsonArray.length(); i++) {
                    result_.append("\"").append(jsonArray.get(i)).append("\"");
                    if (i < jsonArray.length() - 1) {
                        result_.append(", ");
                    } else {
                        result_.append("]");
                    }
                }*/


            } catch (JSONException e) {
                e.printStackTrace(); // Handle exception
            }
        } else {
            out.println("list is empty ");
        }
    }

    private void callSharedPreferences() {

        sprefs = getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE);
        getspCurrency = getSharedPreferences(Utils.PREFS_NAME_CURRENCY, MODE_PRIVATE);

        userId = sprefs.getString(Utils.SP_USERID, null);
        strCustomerEmail = sprefs.getString(Utils.SP_USER_EMAIL, null);
        userAccessToken = sprefs.getString(Utils.SP_USER_ACCESSTOKEN, null);
        strWalletAmtMin = sprefs.getString(Utils.SP_USER_WALLET_AMT_MIN, null);
        strWalletAmtMin = sprefs.getString(Utils.SP_USER_WALLET_AMT_MIN, null);

        /*if (getArguments() != null) {

            categoryCount = getArguments().getString(Utils.B_CATEGORY_COUNT);
            categoryDays = getArguments().getString(Utils.B_CATEGORY_DAYS);
            categoryAgentPrice = getArguments().getString(Utils.B_CATEGORY_AGENT_PRICE);
            categoryDayPrice = getArguments().getString(Utils.B_CATEGORY_DAYS_PRICE);

        }
        Log.e(TAG, "NewonCreate: categoryAgentPrice " + categoryAgentPrice);
        Log.e(TAG, "NewonCreate: categoryAgentPrice " + categoryDayPrice);
        Log.e(TAG, "NewonCreate: categoryDays " + categoryDays);*/

        strCurrency = getspCurrency.getString(Utils.SP_CURRENCY, null);
        strCurrencySymbol = getspCurrency.getString(Utils.SP_CURRENCY_SYMBOL, null);

        strLanguage = getspCurrency.getString(Utils.SP_LANGUAGE, null);
        strLanguageShortName = getspCurrency.getString(Utils.SP_LANGUAGE_SHORT_NAME, null);
        if (strLanguageShortName == null) {
            strLanguageShortName = "en";
        }

        DISTANCE_LIMIT = sprefs.getString(Utils.SP_COMMON_DISTANCE_LIMIT, null);
        System.out.println("Distance limit===>>>" + DISTANCE_LIMIT);

        if (DISTANCE_LIMIT != null) {
            if (DISTANCE_LIMIT.equals("0")) {
                Distance = Utils.GEOQUERY_RADIUS;
            } else {
                Distance = Integer.valueOf(DISTANCE_LIMIT);
            }
        } else {
            Distance = Utils.GEOQUERY_RADIUS;
        }

        spAcceptDetails = getSharedPreferences(Utils.MY_PREFS_REQUEST_STATE, Context.MODE_PRIVATE);
        categoryId = spAcceptDetails.getString(Utils.B_CATEGORY_ID, null);
        categoryName = spAcceptDetails.getString(Utils.B_CATEGORY_NAME, null);
        categoryNameAr = spAcceptDetails.getString(Utils.B_CATEGORY_NAME_AR, null);
        subCategoryId = spAcceptDetails.getString(Utils.B_SUB_CATEGORY_ID, null);
        subCategoryName = spAcceptDetails.getString(Utils.B_SUB_CATEGORY_NAME, null);
        subCategoryNameAr = spAcceptDetails.getString(Utils.B_SUB_CATEGORY_NAME_AR, null);
        subCategoryPrice = spAcceptDetails.getString(Utils.B_SUB_CATEGORY_PRICE, null);
        categoryAgentPrice = spAcceptDetails.getString(Utils.B_CATEGORY_AGENT_PRICE, null);
        categoryDayPrice = spAcceptDetails.getString(Utils.B_CATEGORY_DAYS_PRICE, null);
        System.out.println("agentPrice--dayprice===>>" + categoryAgentPrice + " " + categoryDayPrice);

        strPickupAddress = spAcceptDetails.getString(Utils.B_PICKUP, null);
        strPickupLat = spAcceptDetails.getString(Utils.B_PICKUP_LAT, null);
        strPickupLng = spAcceptDetails.getString(Utils.B_PICKUP_LNG, null);

        strDropAddress = spAcceptDetails.getString(Utils.B_DROP, null);
        strDropLat = spAcceptDetails.getString(Utils.B_DROP_LAT, null);
        strDropLng = spAcceptDetails.getString(Utils.B_DROP_LNG, null);

        strDistance = spAcceptDetails.getString(Utils.B_DISTANCE, null);
        strTotalEstAmt = spAcceptDetails.getString(Utils.B_TOTAL_EST_AMT, null);

        strPickupDate = spAcceptDetails.getString(Utils.B_PICKUP_DATE, "");
        strPickupTime = spAcceptDetails.getString(Utils.B_PICKUP_TIME, "");
        strPickupDateEnd = spAcceptDetails.getString(Utils.B_PICKUP_DATE_END, "");
        strPickupTimeEnd = spAcceptDetails.getString(Utils.B_PICKUP_TIME_END, "");

        strPickupDate_LABEL = spAcceptDetails.getString(Utils.B_PICKUP_DATE_LABEL, "");
        strPickupTime_LABEL = spAcceptDetails.getString(Utils.B_PICKUP_TIME_LABEL, "");
        strPickupDateEnd_LABEL = spAcceptDetails.getString(Utils.B_PICKUP_DATE_END_LABEL, "");
        strPickupTimeEnd_LABEL = spAcceptDetails.getString(Utils.B_PICKUP_TIME_END_LABEL, "");

        tripType = spAcceptDetails.getString(Utils.B_TRIP_TYPE, null);
        strEmergencyMsg = spAcceptDetails.getString(Utils.B_CATEGORY_TELL_US_EMERGENCY, "");
        strNoOfGuardCount = spAcceptDetails.getString(Utils.B_CATEGORY_GUARDS_COUNT, "2");

        Log.i(TAG, "onCreateView: trip type-->" + tripType);
        Log.i(TAG, "onCreateView:--------->>>>>>>>>>" + subCategoryId + "~~~~~~" + categoryId);

        putSpState = getSharedPreferences(Utils.MY_PREFS_FB_CATEGORY_TRIPSTATE, MODE_PRIVATE).edit();
        getState = getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE).edit();

        ProviderListAdapter.successResponse = this;

        if (strPickupLat != null) {
            currentLatLng = new LatLng(Double.parseDouble(strPickupLat), Double.parseDouble(strPickupLng));
            Log.i(TAG, "onCreateView: providerlistfragment-->" + currentLatLng);
            //currentLatLng = new LatLng(MainActivity.locFusedLocation.getLatitude(),MainActivity.locFusedLocation.getLongitude());
        }
    }

    private void initViews() {

        getUniqueIdList.add("");

        imgbtnBack = (ImageButton) findViewById(R.id.imgbtn_back);
        imgbtnRefresh = (ImageButton) findViewById(R.id.imgbtn_refresh);
        progressBar = findViewById(R.id.rl_progress_bar);

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefresh.setOnRefreshListener(this);

        txtProviderListHeader = (CustomTextView) findViewById(R.id.txt_providerlist_header);
        rvProviderList = (RecyclerView) findViewById(R.id.rv_providerlist);
        llNoProvider = (LinearLayout) findViewById(R.id.ll_noprovider);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity, LinearLayoutManager.VERTICAL, false);
        rvProviderList.setLayoutManager(linearLayoutManager);

        providerListAdapter = new ProviderListAdapter(requireActivity, providerListData);
        rvProviderList.setAdapter(providerListAdapter);

        if (categoryName != null) {
            txtProviderListHeader.setText(categoryName + " - " + subCategoryName);
        }

        contentView = findViewById(R.id.frame_provider_list);

        dailogBookingDetails = findViewById(R.id.lyt_dialog_booking_details);
        rlParentLytBookingDetails = findViewById(R.id.rl_parent_lyt_booking_details);

        dialogRequesting = findViewById(R.id.lyt_dialog_requesting);
        rlParentLytRequesting = findViewById(R.id.reqRelView_request);
        rlParentLytRequest = findViewById(R.id.rl_parent_lyt_request);

        txtCountTimer = findViewById(R.id.txt_count_timer);
        btnCancelRequest = findViewById(R.id.btn_cancel_request);


        dailogDriverDetails = findViewById(R.id.lyt_dialog_driver_view_details);
        rlParentLytDriverDetails = findViewById(R.id.rl_parent_lyt_dialog_driver_view_details);

        //Schedule Booking Accepted
        dialogAcceptSchedule = findViewById(R.id.lyt_dialog_acceptschedule);
        rlParentLytAcceptSchedule = findViewById(R.id.reqRelView_dialog_acceptschedule);

        //Select payment dialog
        dialogSelectPayment = findViewById(R.id.lyt_dialog_select_payment_type);
        rlParentLytSelectPayment = findViewById(R.id.rl_parent_lyt_dialog_select_payment_type);

        //Booking dialog
        btnBookingDetailsContinue = (CustomButton) findViewById(R.id.btn_booking_details_continue);
        txtServiceItemLabel = (CustomTextView) findViewById(R.id.txt_serviceitem_label);
        txtServiceItem = (CustomTextView) findViewById(R.id.txt_serviceitem);
        txtProvidernNameLabel = (CustomTextView) findViewById(R.id.txt_providername_label);
        txtProvidernName = (CustomTextView) findViewById(R.id.txt_providername);

        txtBookingDateLabel = (CustomTextView) findViewById(R.id.txt_bookingdate_label);
        txtBookingTimeLabel = (CustomTextView) findViewById(R.id.txt_bookingtime_label);
        txtBookingDate = (CustomTextView) findViewById(R.id.txt_bookingdate);
        txtBookingTime = (CustomTextView) findViewById(R.id.txt_bookingtime);

        llBookingEndDate = findViewById(R.id.ll_bookingdate_end);
        llBookingEndTime = findViewById(R.id.ll_bookingtime_end);

        txtBookingEndDateLabel = (CustomTextView) findViewById(R.id.txt_bookingdate_label_end);
        txtBookingEndTimeLabel = (CustomTextView) findViewById(R.id.txt_bookingtime_label_end);
        txtBookingEndDate = (CustomTextView) findViewById(R.id.txt_bookingdate_end);
        txtBookingEndTime = (CustomTextView) findViewById(R.id.txt_bookingtime_end);
        txtEmergencyInstruction = findViewById(R.id.txt_ll_emergency_msg);
        txtNoOfGuardsCount = findViewById(R.id.txt_bookingtime_guard_count);

        txtBookingLocatoin = (CustomTextView) findViewById(R.id.txt_bookinglocation);

        edPromo = (CustomEditText) findViewById(R.id.ed_promo);
        txtComment = (CustomEditText) findViewById(R.id.txt_comment);
        txtApplyPromo = (CustomTextView) findViewById(R.id.txt_apply_promo);
        imgbtnBackBookingDetails = (ImageButton) findViewById(R.id.imgbtn_back_booking_details);

        txtPromoCode = (CustomTextView) findViewById(R.id.txt_promo_code);
        txtPromoCodeDescr = (CustomTextView) findViewById(R.id.txt_promo_code_descr);
        txtPromoCodeDiscount = (CustomTextView) findViewById(R.id.txt_promo_code_discount_offer);
        txtPromoCodeExpDate = (CustomTextView) findViewById(R.id.txt_promo_code_exp_date);
        txtPromoCodeStatus = (CustomTextView) findViewById(R.id.txt_promo_status);
        txtPromoCodeCancel = (CustomTextView) findViewById(R.id.txt_apply_promo_cancel);
        llPromocodeViewDetails = (LinearLayout) findViewById(R.id.ll_view_promocode_details);

        txtAgentCost = (CustomTextView) findViewById(R.id.txt_total_agent);
        txtDaysCost = (CustomTextView) findViewById(R.id.txt_total_days);

        //Driver Details Dialog
        imgProviderProfile = (SimpleDraweeView) findViewById(R.id.img_provider_profile);
        btnNextDriverView = (CustomButton) findViewById(R.id.btn_next_driverview);
        imgClose = (ImageView) findViewById(R.id.img_close);
        txtMiles = (CustomTextView) findViewById(R.id.txt_miles);
        //txtDescription = (CustomTextView) findViewById(R.id.txt_description);
        txtProviderName = (CustomTextView) findViewById(R.id.txt_provider_name);
        txtProviderPrice = (CustomTextView) findViewById(R.id.txt_provider_price);
        rbProviderRating = (SimpleRatingBar) findViewById(R.id.rb_provider_rating);

        //Requesting Dialog
        rippleBackground = (RippleBackground) findViewById(R.id.rippleBackground);
        ivCancelRequest = (ImageView) findViewById(R.id.iv_cancelrequest);

        //Schedule Requesting Accept Dialog
        txtBookingSchedule = (CustomTextView) findViewById(R.id.txt_viewbooking);

        //Payment Select Dialog
        imgbtnBackSelectPayment = (ImageButton) findViewById(R.id.imgbtn_back_selectpayment);

        rcvSelectPayment = findViewById(R.id.rv_selectpayment);

        rGSelectPayment = (RadioGroup) findViewById(R.id.rg_selectpayment);
        rbStripePayment = findViewById(R.id.rb_payonline);
        rbPaypalPayment = findViewById(R.id.rb_paypal);
        rbGooglePayPayment = findViewById(R.id.rb_google_pay);

        txtBaseFarePayment = findViewById(R.id.txt_base_fare_payment);
        txtGuardFarePayment = findViewById(R.id.txt_guard_fare_payment);
        txtTotalHoursPayment = findViewById(R.id.txt_total_hrs_payment);
        txtVATFareLabelPayment = findViewById(R.id.txt_total_vat_label);
        txtVATFarePayment = findViewById(R.id.txt_total_vat_payment);
        txtTotalFarePayment = findViewById(R.id.txt_fare_payment);

        txtSubscribeFarePayment = findViewById(R.id.txt_subscribed_user_payment);

        btnBookNow = (CustomButton) findViewById(R.id.btn_booknow);

        //Stripe Payment Details
        creditCardModule = (CreditCardModule) findViewById(R.id.credit_card_module);
        //creditCardModuleStripe = (CardInputWidget) findViewById(R.id.credit_card_module_stripe);
        btnAddcardPayOnline = (CustomButton) findViewById(R.id.btn_addcard_payonline);
        llStripeContent = (LinearLayout) findViewById(R.id.ll_stripe_content);
        //  stripeKey = Utils.com_stripe_publishable_key;


        //Requesting Accept Dialog
        dailogAcceptDriver = findViewById(R.id.lyt_dialog_acceptprovider);

        txtViewOnGoingJobs = (CustomTextView) findViewById(R.id.txt_viewongoingjobs);
        rlParentLytAcceptDriver = (RelativeLayout) findViewById(R.id.reqRelView);
        txtCancel = (CustomTextView) findViewById(R.id.txt_cancel);
        txtViewOnGoingJobs1 = (CustomTextView) findViewById(R.id.txt_viewongoingjobs1);

        rlSuccess = (RelativeLayout) findViewById(R.id.success);

        rlSuccess1 = (RelativeLayout) findViewById(R.id.success1);
        txtCancel1 = (CustomTextView) findViewById(R.id.txt_cancel1);


        layout_dots = (LinearLayout) findViewById(R.id.layout_dots);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        rlEmptyviewAnnouncement = (RelativeLayout) findViewById(R.id.rl_emptyview_announcement);
        viewImageSlider = findViewById(R.id.view_image_slider);

        adapterImageSlider = new AdapterImageSlider(requireActivity, new ArrayList<>());
        layoutManager = new LinearLayoutManager(requireActivity);
    }

    private void OnClick() {

        imgbtnBack.setOnClickListener(v -> {
            hideKeyBoard();
            backButtonFunction();
        });

        imgbtnRefresh.setOnClickListener(v -> {

            progressBar.setVisibility(View.VISIBLE);

            if (providerListData != null) {
                providerListData.clear();
            }
            // getAllDriversFirebase();

        });
        imgbtnBackBookingDetails.setOnClickListener(v -> {

            if (dailogBookingDetails.getVisibility() == View.VISIBLE) {
                dailogBookingDetails.setVisibility(View.GONE);
            }

            boolPromoAccepted = false;
            ValidPromoCode = "";
            edPromo.setText("");
            edPromo.setEnabled(true);
            txtApplyPromo.setEnabled(true);
            if (txtPromoCodeStatus.getVisibility() == View.VISIBLE) {
                txtPromoCodeStatus.setVisibility(View.GONE);
            }
            if (llPromocodeViewDetails.getVisibility() == View.VISIBLE) {
                llPromocodeViewDetails.setVisibility(View.GONE);
            }
            if (txtPromoCodeCancel.getVisibility() == View.VISIBLE) {
                txtPromoCodeCancel.setVisibility(View.GONE);
            }
        });

        txtPromoCodeCancel.setOnClickListener(view -> {
            boolPromoAccepted = false;
            ValidPromoCode = "";
            edPromo.setText("");
            edPromo.setEnabled(true);
            txtApplyPromo.setEnabled(true);
            if (txtPromoCodeStatus.getVisibility() == View.VISIBLE) {
                txtPromoCodeStatus.setVisibility(View.GONE);
            }
            if (llPromocodeViewDetails.getVisibility() == View.VISIBLE) {
                llPromocodeViewDetails.setVisibility(View.GONE);
            }
            if (txtPromoCodeCancel.getVisibility() == View.VISIBLE) {
                txtPromoCodeCancel.setVisibility(View.GONE);
            }
        });

        btnBookingDetailsContinue.setOnClickListener(v -> {

            callFareEstimate();

        });

        rlParentLytRequest.setOnClickListener(v -> {
        });

        ivCancelRequest.setOnClickListener(v -> {
            afterAcceptRequest();
            removeRequestDetailsFirebase();

        });

        btnCancelRequest.setOnClickListener(view1 -> callCancelBooking());

        imgClose.setOnClickListener(v -> {

            if (dailogDriverDetails.getVisibility() == View.VISIBLE) {
                dailogDriverDetails.setVisibility(View.GONE);
            }
        });

        txtApplyPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.requireNonNull(edPromo.getText()).toString().equals("")) {
                    Toast.makeText(requireActivity, R.string.pleas_check_promo_code, Toast.LENGTH_SHORT).show();
                } else {
                    checkPromoRequest(edPromo.getText().toString());
                }
            }
        });

        txtBookingSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogAcceptSchedule.getVisibility() == View.VISIBLE) {
                    dialogAcceptSchedule.setVisibility(View.GONE);
                }
                Bundle args = new Bundle();
                args.putString("viascheduletrip", "scheduletrip");
                args.putString("from", "trip");
                Utils.switchContent(R.id.frame_provider_list, //frame_mainactivity
                        Utils.FRAGMENT_YOURJOBS,
                        (FragmentActivity) requireActivity, args,
                        Utils.AnimationType.SLIDE_IN_SLIDE_OUT);
            }
        });

        imgbtnBackSelectPayment.setOnClickListener(v -> {
            if (dialogSelectPayment.getVisibility() == View.VISIBLE) {
                dialogSelectPayment.setVisibility(View.GONE);
            }
            if (dailogBookingDetails.getVisibility() == View.GONE) {
                dailogBookingDetails.setVisibility(View.VISIBLE);
            }
        });


        btnAddcardPayOnline.setOnClickListener(v -> {
            // getStripToken();
        });

        rlParentLytBookingDetails.setOnClickListener(view -> {

        });
        rlParentLytRequesting.setOnClickListener(view -> {

        });
        rlParentLytDriverDetails.setOnClickListener(view -> {

        });
        rlParentLytAcceptSchedule.setOnClickListener(view -> {

        });
        rlParentLytSelectPayment.setOnClickListener(view -> {

        });

        rlParentLytAcceptDriver.setOnClickListener(view -> {

        });

        rGSelectPayment.setOnCheckedChangeListener((group, checkedId) -> {
            rbSelectedPaymentId = (RadioButton) dialogSelectPayment.findViewById(checkedId);
            RadioButton checkedRadioButton = (RadioButton) rGSelectPayment.findViewById(checkedId);
            selectedId = rGSelectPayment.indexOfChild(checkedRadioButton);
            // Log.e(TAG, "onCheckedChanged: " + rGSelectPayment.getCheckedRadioButtonId());
            Log.e(TAG, "onCheckedChanged:checkedId --->>>" + selectedId);

            //0-rb_mywallet 1-view
            //2-rb_payonline 3-view
            //4-rb_cashonhand  5-view
            //6-rb_paypal 7-view
            //8-rb_google_pay 9-view
            //10-google_pay_button

            if (selectedId == 0) {
                llStripeContent.setVisibility(View.GONE);
            } else if (selectedId == 1) {
                if (stripeTokenID == null) {
                    llStripeContent.setVisibility(View.GONE);
                } else {
                    llStripeContent.setVisibility(View.GONE);
                }
            } else if (selectedId == 2) {
                llStripeContent.setVisibility(View.GONE);
            }
        });

        rcvSelectPayment.addOnItemTouchListener(
                new RecyclerTouchListener(ProviderListActivity.this, rcvSelectPayment,
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
                                        Toast.makeText(requireActivity, getResources().getString(R.string.other_select_payment_option), Toast.LENGTH_SHORT).show();
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


        btnBookNow.setOnClickListener(v -> {
            out.println("rcv SelectPayment->> " + selectedId);
            //Log.e(TAG, "rGSelectPayment: " + rGSelectPayment.getCheckedRadioButtonId());
            //if (rGSelectPayment.getCheckedRadioButtonId() == -1) {
            if (selectedId == -1) {
                //empty selection
                Toast.makeText(requireActivity, getResources().getString(R.string.toast_pls_select_payment_option), Toast.LENGTH_SHORT).show();
            } else {

                //0-rb_mywallet 1-view
                //2-rb_payonline 3-view
                //4-rb_cashonhand  5-view
                //6-rb_paypal 7-view
                //8-rb_google_pay 9-view
                //10-google_pay_button

                if (selectedId == 0) { //Wallet
                    //llStripeContent.setVisibility(View.GONE);
                    //Log.i(TAG, "onClick: minwallet-->" + strWalletAmtMin);
                    //Log.i(TAG, "onClick: walletAmount-->" + walletAmount);
                    //Log.i(TAG, "onClick: strPickupLat ---strPickupLng-->" + strPickupLat + "  " + strPickupLng);
                    BaseActivity.callClickAction(btnBookNow, true);
                    /*if (Double.parseDouble(walletAmount) >= Double.parseDouble(strWalletAmtMin)) {*/
                    callRequestPaymode("wallet");
                    /*} else {
                    showProfileUpdateAlertDialog(requireActivity);
                    }*/
                } else if (selectedId == 2) {  //Card payment
                    /*if (stripeTokenID != null) {*/
                    BaseActivity.callClickAction(btnBookNow, true);
                    callRequestPaymode("card");
                    /*} else {
                        Toast.makeText(requireActivity, "Please add valid card details or select mywallet option", Toast.LENGTH_SHORT).show();
                    }*/
                } else if (selectedId == 4) { //cash
                    BaseActivity.callClickAction(btnBookNow, true);
                    callRequestPaymode("cash");
                } else if (selectedId == 6) { //Paypal
                    BaseActivity.callClickAction(btnBookNow, true);
                    callRequestPaymode("paypal");
                } else if (selectedId == 8) { //Google Pay
                    BaseActivity.callClickAction(btnBookNow, true);
                    callRequestPaymode("google_pay");
                }
            }
        });
    }

    private void callRequestPaymode(String paymode) {

        strSpecialIns = Objects.requireNonNull(txtComment.getText()).toString();

        putSpState.putString(Utils.SP_FB_CATEGORY_PAYMODE_TRIPSTATE, paymode);
        putSpState.apply();

        switch (paymode) {
            case "card":
                if ("save_card".equals("save_card")) {
                    if (getOverallTotal != null && !getOverallTotal.equals("0")) {
                        callSaveUpdateDetails(getOverallTotal, tripType, "stripe");
                    } else {
                        //callFareEstimate();
                        Toast.makeText(requireActivity, getResources().getString(R.string.total_amount_empty_try_again), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (getOverallTotal != null && !getOverallTotal.equals("0")) {
                        callUpdateStripAlsoSaveCard(getOverallTotal, tripType);
                    } else {
                        Toast.makeText(requireActivity, getResources().getString(R.string.total_amount_empty_try_again), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case "paypal":
                if (getOverallTotal != null && !getOverallTotal.equals("0")) {
                    BaseActivity.callClickAction(btnBookNow, false);
                    callCreatePayPalPayment(getOverallTotal);
                } else {
                    Toast.makeText(requireActivity, getResources().getString(R.string.total_amount_empty_try_again), Toast.LENGTH_SHORT).show();
                }
                break;
            case "google_pay":
                if ("save_card".equals("save_card")) { //***************Not workinng***********************
                    if (getOverallTotal != null && !getOverallTotal.equals("0")) {
                        callSaveUpdateDetails(getOverallTotal, tripType, "google_pay");
                    } else {
                        Toast.makeText(requireActivity, getResources().getString(R.string.total_amount_empty_try_again), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (getOverallTotal != null && !getOverallTotal.equals("0")) {
                        BaseActivity.callClickAction(btnBookNow, false);
                        callPaymentIntent_API(getOverallTotal, "0", tripType, "google_pay");
                    } else {
                        Toast.makeText(requireActivity, getResources().getString(R.string.total_amount_empty_try_again), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case "wallet":
                CallRequestBooking("save_card", "use_wallet");
                break;
        }

    }

    public void removeCustomerAcceptStatus(String key) {
        Log.e(TAG, "removeCustomerAcceptStatus: " + key);
        FirebaseDatabase.getInstance().getReference().child(Utils.FIREBASE_USER_HEADER).child(userId).
                child(Utils.FIREBASE_TRIP_HEADER).child(Utils.FIREBASE_ACCEPT_STATUS).setValue(key);
        if (key.equals("0")) {
            FirebaseDatabase.getInstance().getReference().child(Utils.FIREBASE_USER_HEADER).child(userId).
                    child(Utils.FIREBASE_TRIP_HEADER).child(Utils.FIREBASE_BOOKING_ID).setValue(key);
        }
    }

    /*
        public void callPayOnline() {

            if (tripType.equals("scheduletrip")) {

                strSpecialIns = txtComment.getText().toString();

                callScheduleRequest("card");

            } else {

                callNotifySend(userId, strDriverId);

                if (dialogRequesting.getVisibility() == View.GONE) {
                    dialogRequesting.setVisibility(View.VISIBLE);
                }

                countDownTimer.start();
                rippleBackground.startRippleAnimation();

                strSpecialIns = txtComment.getText().toString();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(Utils.FIREBASE_PROVIDERS_HEADER).
                child(strDriverId).child(Utils.FIREBASE_TRIP_HEADER);
                Map<String, Object> taskMap = new HashMap<String, Object>();
                taskMap.put(Utils.FIREBASE_SERVICE_STATUS, "1");
                taskMap.put(Utils.FIREBASE_TRIP_CUS_ID, userId);
                taskMap.put(Utils.FIREBASE_TRIP_CUS_NAME, strFNameCustomer + ":" + strLNameCustomer);
                taskMap.put("user_rating", strRatingCustomer);
                taskMap.put("user_pickup", strPickupAddress);

                taskMap.put("user_drop", strDropAddress);

                taskMap.put("main_service_ar", categoryNameAr);
                taskMap.put("sub_service_ar", subCategoryNameAr);

                taskMap.put("main_service", categoryId + ":" + categoryName);
                taskMap.put("sub_service", subCategoryId + ":" + subCategoryName);
                taskMap.put("pickup_lat", strPickupLat);
                taskMap.put("pickup_lng", strPickupLng);
                taskMap.put("drop_lat", strDropLat);
                taskMap.put("drop_lon", strDropLng);
                taskMap.put("distance", strDistance);


                taskMap.put("paymode", "card");
                taskMap.put("special_ins", strSpecialIns);
                taskMap.put("stripe_token", stripeTokenID);
    //            taskMap.put("card_number",cardToSave.getNumber());
                taskMap.put("card_number", "1234");
                if (boolPromoAccepted) {
                    taskMap.put("promocode", ValidPromoCode);
                } else {
                    taskMap.put("promocode", "");
                }
                taskMap.put("base_fare", subCategoryPrice);
                databaseReference.updateChildren(taskMap);

                putSpState.putString(Utils.SP_FB_CATEGORY_PAYMODE_TRIPSTATE, "card");
                putSpState.apply();

                getrequestAcceptLisetner();
            }
        }
    */

    private void showProfileUpdateAlertDialog(Context self) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(self);
        builder.setCancelable(false);
        Typeface tf = Typeface.createFromAsset(self.getAssets(),
                getString(R.string.App_Font));

        Resources res = getResources();
        // String lowWalletToast = String.format(getResources().getString(R.string.toast_recharge_wallet_amount),strWalletAmtMin);
        String lowWalletToast = (getResources().getString(R.string.toast_recharge_wallet_amount) + " " + strWalletAmtMin);

        BaseActivity.CustomTFSpan tfSpan = new BaseActivity.CustomTFSpan(tf);
        SpannableString spannableString = new SpannableString(lowWalletToast);
        spannableString.setSpan(tfSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableString spannableStrings = new SpannableString(getString(R.string.ok));
        spannableStrings.setSpan(tfSpan, 0, spannableStrings.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setMessage(spannableString).setPositiveButton(spannableStrings,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        backButtonFunction();


                        if (dailogBookingDetails.getVisibility() == View.VISIBLE) {
                            dailogBookingDetails.setVisibility(View.GONE);
                        }

                        if (dialogSelectPayment.getVisibility() == View.VISIBLE) {
                            dialogSelectPayment.setVisibility(View.GONE);
                        }

                        //  MainActivity.frameClickable(true);
                        Bundle args = new Bundle();
                        args.putString(Utils.B_WALLET_RECHARGE_AMOUNT, walletAmount);
                        args.putString(Utils.B_WALLET_PAGE, "wallet");
                        Utils.switchContent(R.id.frame_mainactivity, //frame_mainactivity
                                Utils.FRAGMENT_MYWALLET,
                                (FragmentActivity) requireActivity, args,
                                Utils.AnimationType.SLIDE_IN_SLIDE_OUT);

                       /* Intent resultIntent = new Intent(requireActivity, MainActivity.class);
                       // resultIntent.putExtra("fragment", "walletfragment_call");
                        startActivity(resultIntent);
                        finish();*/

                    }
                }).show();
    }

    public void onBackButton(View view) {
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP
                        && keyCode == KeyEvent.KEYCODE_BACK) {
                    backButtonFunction();
                }
                return true;
            }
        });
    }


    private void callScheduleRequest(String paymentType) {

        Log.i(TAG, "callScheduleRequest : customer_lat ---customer_lon-->" + strPickupLat + "  " + strPickupLng);
        userAccessToken = sprefs.getString(Utils.SP_USER_ACCESSTOKEN, null);

        HashMap<String, Object> map = new HashMap<>();
        map.put("mode", "accept");
        map.put("customer_id", userId);
        map.put("driver_id", strDriverId);
        map.put("request_time", strPickupTime);
        map.put("job_date", strPickupDate);

        map.put("service_id", subCategoryId);
        map.put("category", categoryId);

        map.put("customer_location", strPickupAddress);
        map.put("customer_lat", strPickupLat);
        map.put("customer_lon", strPickupLng);

        map.put("drop_lat", strDropLat);
        map.put("drop_lon", strDropLng);
        map.put("distance", strDistance);
        map.put("drop_location", strDropAddress);

        map.put("special_instruction", strSpecialIns);
        map.put("language", strLanguageShortName);

        map.put("payment_type", paymentType);

        if (paymentType.equals("card")) {
            map.put("stripe_token", stripeTokenID);
        }

        if (boolPromoAccepted) {
            map.put("promocode", ValidPromoCode);
        } else {
            map.put("promocode", "");
        }


       /* map.put("total_agent", categoryCount);
        map.put("total_days", categoryDays);*/


        Log.e("callScheduleRequest", "~~~~" + map);

        Call<ScheduleRequestModel> call2 = apiInterface.doScheduleRequesting("Bearer " + userAccessToken, Utils.POST_ACCEPT_CONTENT_TYPE, map);

        showProgressDialog(requireActivity);

        call2.enqueue(new Callback<ScheduleRequestModel>() {
            @Override
            public void onResponse(@NonNull Call<ScheduleRequestModel> call, @NonNull Response<ScheduleRequestModel> response) {
                hideProgressDialog();
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    hideProgressDialog();
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Request saved successfully.")) {
                        // dialogAcceptSchedule.show();

                        if (dialogAcceptSchedule.getVisibility() == View.GONE) {
                            dialogAcceptSchedule.setVisibility(View.VISIBLE);
                        }

                        boolPromoAccepted = false;
                        removeCustomerAcceptStatus("1");


                    } else if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Failure")) {
                        hideProgressDialog();
                        Toast.makeText(requireActivity, R.string.try_again, Toast.LENGTH_SHORT).show();
                    }

                } else if (response.code() == RESPONSECODE_401) {
                    //callRefreshToken(paymentType);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ScheduleRequestModel> call, @NonNull Throwable t) {
                call.cancel();
            }
        });
    }

    private void checkPromoRequest(String promo_code) {

        userAccessToken = sprefs.getString(Utils.SP_USER_ACCESSTOKEN, null);
        HashMap<String, Object> map = new HashMap<>();
        map.put("promocode", promo_code);
        map.put("customer_id", userId);
        Log.e("callScheduleRequest", "~~~~" + map);
        Call<CheckPromoModel> call2 = apiInterface.doCheckPromoCode("Bearer " + userAccessToken, Utils.POST_ACCEPT_CONTENT_TYPE, map);
        showProgressDialog(requireActivity);
        call2.enqueue(new Callback<CheckPromoModel>() {
            @Override
            public void onResponse(@NonNull Call<CheckPromoModel> call, @NonNull Response<CheckPromoModel> response) {
                hideProgressDialog();
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    hideProgressDialog();
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Valid Promocode")) {

                        boolPromoAccepted = true;
                        ValidPromoCode = promo_code;
                        edPromo.setEnabled(false);
                        txtApplyPromo.setEnabled(false);
                        Toast.makeText(requireActivity, getResources().getString(R.string.promocode_your_promo_accepted), Toast.LENGTH_SHORT).show();
                        txtPromoCodeStatus.setVisibility(View.VISIBLE);
                        txtPromoCodeStatus.setText(getResources().getString(R.string.promocode_your_promo_accepted));

                        if (response.body().getResult() != null) {
                            if (!response.body().getResult().equals("null")) {
                                llPromocodeViewDetails.setVisibility(View.VISIBLE);
                                txtPromoCodeCancel.setVisibility(View.VISIBLE);
                                callTextviews(txtPromoCode, response.body().getResult().getCouponCode().toString(), "--");
                                callTextviews(txtPromoCodeDescr, response.body().getResult().getDescription().toString(), "--");
                                callTextviews(txtPromoCodeDiscount, response.body().getResult().getDiscount().toString() + " %", "--");
                                callTextviews(txtPromoCodeExpDate, response.body().getResult().getExpiryDate().toString(), "--");
                            } else {
                                llPromocodeViewDetails.setVisibility(View.GONE);
                                txtPromoCodeCancel.setVisibility(View.GONE);
                            }
                        } else {
                            llPromocodeViewDetails.setVisibility(View.GONE);
                            txtPromoCodeCancel.setVisibility(View.GONE);
                        }

                    } else if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Invalid Promocode.")) {

                        llPromocodeViewDetails.setVisibility(View.GONE);
                        txtPromoCodeCancel.setVisibility(View.GONE);
                        boolPromoAccepted = false;
                        Toast.makeText(requireActivity, getResources().getString(R.string.promocode_your_promo_invalid_try_again), Toast.LENGTH_SHORT).show();
                        edPromo.setEnabled(true);
                        txtPromoCodeStatus.setVisibility(View.VISIBLE);
                        txtPromoCodeStatus.setText(getResources().getString(R.string.promocode_your_promo_invalid_try_again));

                    } else if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Expired Promocode.")) {

                        llPromocodeViewDetails.setVisibility(View.GONE);
                        txtPromoCodeCancel.setVisibility(View.GONE);
                        boolPromoAccepted = false;
                        Toast.makeText(requireActivity, getResources().getString(R.string.promocode_your_promo_expired), Toast.LENGTH_SHORT).show();
                        edPromo.setEnabled(true);
                        txtPromoCodeStatus.setVisibility(View.VISIBLE);
                        txtPromoCodeStatus.setText(getResources().getString(R.string.promocode_your_promo_expired));

                    } else if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Usage limit exceeded for this Promocode.")) {

                        llPromocodeViewDetails.setVisibility(View.GONE);
                        txtPromoCodeCancel.setVisibility(View.GONE);
                        boolPromoAccepted = false;
                        Toast.makeText(requireActivity, getResources().getString(R.string.promocode_your_promo_usage_limit_exceed), Toast.LENGTH_SHORT).show();
                        txtPromoCodeStatus.setVisibility(View.VISIBLE);
                        txtPromoCodeStatus.setText(getResources().getString(R.string.promocode_your_promo_usage_limit_exceed));
                        edPromo.setEnabled(true);

                    } else {

                        llPromocodeViewDetails.setVisibility(View.GONE);
                        txtPromoCodeCancel.setVisibility(View.GONE);
                        boolPromoAccepted = false;
                        Toast.makeText(requireActivity, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                        txtPromoCodeStatus.setVisibility(View.VISIBLE);
                        txtPromoCodeStatus.setText(getResources().getString(R.string.promocode_your_promo_usage_limit_exceed));
                        edPromo.setEnabled(true);

                    }

                } else if (response.code() == RESPONSECODE_401) {
                    //callRefreshToken1(promo_code);
                    llPromocodeViewDetails.setVisibility(View.GONE);
                    txtPromoCodeStatus.setVisibility(View.GONE);
                    txtPromoCodeCancel.setVisibility(View.GONE);
                }
            }

            private void callTextviews(CustomTextView customTextView, String value, String empty) {
                if (value != null) {
                    if (!value.equals("null")) {
                        if (!value.equals("")) {
                            customTextView.setText(value);
                        } else {
                            customTextView.setText(empty);
                        }
                    } else {
                        customTextView.setText(empty);
                    }
                } else {
                    customTextView.setText(empty);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CheckPromoModel> call, @NonNull Throwable t) {
                call.cancel();
                llPromocodeViewDetails.setVisibility(View.GONE);
                txtPromoCodeStatus.setVisibility(View.GONE);
                txtPromoCodeCancel.setVisibility(View.GONE);
            }
        });
    }


    public void backButtonFunction() {
        /*if (providerListDatabaseRef != null) {
            providerListDatabaseRef.removeEventListener(providerListValueEventListener);
        }
        removeFragment(Utils.FRAGMENT_PROVIDERLIST);*/

        if (mTimerCountPluse != null) {
            mTimerCountPluse.cancel();
            mTimerCountPluse = null;
        }

        onBackPressed();
    }

    public void removeFragment(String Tag) {
        Utils.CURRENT_TAG = null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(Objects.requireNonNull(fragmentManager.findFragmentByTag(Tag))).commit();
    }

    private void getrequestAcceptLisetner() {

        if (requestAcceptrDatabaseBRef != null) {
            requestAcceptrDatabaseBRef.removeEventListener(requestAccepValueEventListener);
        }
        requestAcceptrDatabaseBRef = FirebaseDatabase.getInstance().getReference().child(Utils.FIREBASE_USER_HEADER).child(userId).child(Utils.FIREBASE_TRIP_HEADER);

        requestAccepValueEventListener = requestAcceptrDatabaseBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    String status = Objects.requireNonNull(dataSnapshot.child(Utils.FIREBASE_ACCEPT_STATUS).getValue()).toString();
                    Log.i(TAG, "onDataChange in getrequestAcceptLisetner: -->" + status);

                    switch (status) {
                        case "yes":

                            if (countDownTimer != null) {
                                countDownTimer.cancel();
                            }

                            boolStatusAccepted = true;
                            bookingId = Objects.requireNonNull(dataSnapshot.child(Utils.FIREBASE_ACCEPT_STATUS).getValue()).toString();

                            if (dailogAcceptDriver.getVisibility() == View.GONE) {
                                dailogAcceptDriver.setVisibility(View.VISIBLE);
                            }

                            BaseActivity.setVisibility(rlSuccess, true);
                            BaseActivity.setVisibility(rlSuccess1, false);

                            txtViewOnGoingJobs.setOnClickListener(v -> {

                                if (dailogAcceptDriver.getVisibility() == View.VISIBLE) {
                                    dailogAcceptDriver.setVisibility(View.GONE);
                                }

                                removeCustomerAcceptStatus("0");

                                Utils.switchContent(R.id.frame_provider_list, //frame_mainactivity
                                        Utils.FRAGMENT_ONGOINGJOBS,
                                        ProviderListActivity.this, null,
                                        Utils.AnimationType.SLIDE_IN_SLIDE_OUT);
                            });


                            break;
                        case "2":

                            if (countDownTimer != null) {
                                countDownTimer.cancel();
                            }

                            boolStatusAccepted = false;

                            if (dailogAcceptDriver.getVisibility() == View.GONE) {
                                dailogAcceptDriver.setVisibility(View.VISIBLE);
                            }

                            BaseActivity.setVisibility(rlSuccess, false);
                            BaseActivity.setVisibility(rlSuccess1, true);

                            txtCancel1.setOnClickListener(view -> {

                                if (dailogAcceptDriver.getVisibility() == View.VISIBLE) {
                                    dailogAcceptDriver.setVisibility(View.GONE);
                                }

                                removeCustomerAcceptStatus("0");
                            });

                            break;
                        case "1":
                            System.out.println("requesting--->");
                            break;
                        case "3":
                            if (dailogAcceptDriver.getVisibility() == View.VISIBLE) {
                                dailogAcceptDriver.setVisibility(View.GONE);
                            }
                            removeCustomerAcceptStatus("0");
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: ~~~~" + databaseError.toString());
            }
        });

    }

    private void getAllDriversFirebase() {

        if (tripType.equals("scheduletrip")) {

            availableScheduleTripDriver = spAcceptDetails.getString(Utils.B_SCH_TRIP_DRIVERID, null);
            if (availableScheduleTripDriver != null) {
                String[] parts = availableScheduleTripDriver.split(",");
                int[] ints = new int[parts.length];
                for (int i = 0; i < parts.length; i++) {
                    ints[i] = Integer.parseInt(parts[i]);
                    collectDriversScheduleTrip(String.valueOf(ints[i]));
                }
            }
        } else {
            CallGeofireAndGeoQuery(new LatLng(currentLatLng.latitude, currentLatLng.longitude));
        }

        new Handler().postDelayed(() -> progressBar.setVisibility(View.GONE), 300);
    }

    private void collectDrivers(String key, GeoLocation location) {
        Log.i(TAG, "collectDrivers: values-->" + "~~~" + key);
        Log.i(TAG, "collectDrivers: sub Categoryid-->" + subCategoryId);

        /*if (providerListDatabaseRef != null) {
            providerListDatabaseRef.removeEventListener(providerListValueEventListener);
        }*/
        providerListDatabaseRef = FirebaseDatabase.getInstance().getReference().child(Utils.FIREBASE_PROVIDERS_HEADER).child(key);
        providerListValueEventListener = providerListDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    if (dataSnapshot.child("status").getValue().toString().equals("1")) {
                        Object serviceStatusStr = dataSnapshot.child("trips").child("service_status").getValue();
                        Log.i(TAG, "onDataChange: service_status" + serviceStatusStr.toString()

                        );
                        if (serviceStatusStr.toString().equals("0") || serviceStatusStr.toString().equals("8") || serviceStatusStr.toString().equals("9")) {
                            checkAndDeleteMatchingIds(key);
                            Object categoryObj = dataSnapshot.child("category").getValue();
                            Log.i(TAG, "onDataChange: categoryObj" + categoryObj.toString());
                            String[] parts = String.valueOf(categoryObj.toString()).split(",");
                            if (!parts.equals("")) {
                                int[] ints = new int[parts.length];
                                for (int i = 0; i < parts.length; i++) {
                                    if (!parts[i].equals("")) {
                                        ints[i] = Integer.parseInt(parts[i]);
                                    } else {
                                        Log.d(TAG, "onDataChange: ~~~~Error occur~");
                                    }
                                }
                                if (contains(ints, Integer.parseInt(subCategoryId))) {
                                    ProviderListData d = new ProviderListData();
                                    System.out.println("Entry of getvalue" + dataSnapshot.getKey() + "````" + dataSnapshot.child("trips"));
                                    System.out.println("Entry of getvalue" + dataSnapshot.child("trips"));
                                    d.setId(String.valueOf(dataSnapshot.getKey()));
                                    d.setFname(String.valueOf(dataSnapshot.child("fname").getValue()));
                                    d.setLname(String.valueOf(dataSnapshot.child("lname").getValue()));
                                    d.setPropic(String.valueOf(dataSnapshot.child("pro_pic").getValue()));
                                    d.setEmail(String.valueOf(dataSnapshot.child("email").getValue()));
                                    d.setMobile(String.valueOf(dataSnapshot.child("mobile").getValue()));
                                    d.setStatus(String.valueOf(dataSnapshot.child("status").getValue()));

                                    //d.setRating(String.valueOf(dataSnapshot.child("rating").getValue()));
                                    //d.setServiceDes(String.valueOf(dataSnapshot.child("service_desc").getValue()));

                                    d.setCurrentDate(todayDate);
                                    d.setCurrentTime(todayTime);
                                    if (dataSnapshot.child("company_name").getValue() != null) {
                                        d.setCompanyName(String.valueOf(dataSnapshot.child("company_name").getValue()));
                                    } else {
                                        d.setCompanyName("Individual");
                                    }
                                    if (dataSnapshot.child("provider_type").getValue() != null) {
                                        d.setProviderType(String.valueOf(dataSnapshot.child("provider_type").getValue()));
                                    } else {
                                        d.setProviderType("Individual");
                                    }
                                    Object ratingObj = dataSnapshot.child("rating").getValue();
                                    if (ratingObj != null) {
                                        d.setRating(String.valueOf(dataSnapshot.child("rating").getValue()));
                                    } else {
                                        d.setRating("0");
                                    }

                                    Object serviceDes = dataSnapshot.child("service_desc").getValue();
                                    if (serviceDes != null) {
                                        d.setServiceDes(String.valueOf(dataSnapshot.child("service_desc").getValue()));
                                    } else {
                                        d.setServiceDes("");
                                    }

                                    if (currentLatLng.latitude != 0.0) {
                                        d.setMiles(Math.round(DistanceCalculator.greatCircleInKilometers(currentLatLng.latitude, currentLatLng.longitude, location.latitude, location.longitude)) + " KM");
                                    } else {
                                        d.setMiles("did not calculate miles");
                                    }

                                    providerListData.add(d);

                                    Log.i(TAG, "onDataChange: in the providerListData-->" + providerListData.size());

                                    if (providerListData.size() == 0) {
                                        rvProviderList.setVisibility(View.GONE);
                                        llNoProvider.setVisibility(View.VISIBLE);
                                    }

                                    Log.i(TAG, "collectDrivers: providerListData.size()-->" + providerListData.size());

                                    if (providerListData.size() > 0) {

                                        //https://stackoverflow.com/questions/32160742/how-to-sort-arraylistobject-in-ascending-order-android
                                        Collections.sort(providerListData, new Comparator<ProviderListData>() {
                                            @Override
                                            public int compare(ProviderListData lhs, ProviderListData rhs) {
                                                return lhs.getMiles().compareTo(rhs.getMiles());
                                            }
                                        });

                                        rvProviderList.setVisibility(View.VISIBLE);
                                        llNoProvider.setVisibility(View.GONE);
                                        providerListAdapter.notifyDataSetChanged();

                                    } else {
                                        Log.i(TAG, "collectDrivers: no data");
                                        //Toast.makeText(requireActivity, "No data", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                Toast.makeText(requireActivity, "Checking near provider available..", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            checkAndDeleteMatchingIds(key);
                        }
                    }
                }
                if (providerListData.size() == 0) {
                    rvProviderList.setVisibility(View.GONE);
                    llNoProvider.setVisibility(View.VISIBLE);
                }
                Log.i(TAG, "collectDrivers: providerListData.size()-->" + providerListData.size());
                if (providerListData.size() > 0) {
                    rvProviderList.setVisibility(View.VISIBLE);
                    llNoProvider.setVisibility(View.GONE);
                    providerListAdapter.notifyDataSetChanged();
                } else {
                    Log.i(TAG, "collectDrivers: no data");
                    //Toast.makeText(requireActivity, "No data", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i(TAG, "onCancelled databaseError: -->" + databaseError);
            }
        });
    }

    private void collectDriversScheduleTrip(String ints) {

        if (providerListData != null) {
            providerListData.clear();
        }

        /*if (providerListDatabaseRef != null) {
            providerListDatabaseRef.removeEventListener(providerListValueEventListener);
        }*/

        Log.i(TAG, "collectDrivers: sub Categoryid-->" + subCategoryId);
        Log.i(TAG, "collectDrivers: sub avialableDriver-->" + ints);

        providerListDatabaseRef = FirebaseDatabase.getInstance().getReference().child(Utils.FIREBASE_PROVIDERS_HEADER).child(ints);
        providerListValueEventListener = providerListDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    if (dataSnapshot.child("trips").child("service_status").getValue() != null) {

                        Object serviceStatusStr = dataSnapshot.child("trips").child("service_status").getValue();

                        Log.i(TAG, "onDataChange: service_status" + serviceStatusStr.toString());

                        checkAndDeleteMatchingIds(ints);

                        Object categoryObj = dataSnapshot.child("category").getValue();
                        Log.i(TAG, "onDataChange: categoryObj" + categoryObj.toString());

                        callDriverLocationScheduleTrip(dataSnapshot, ints);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i(TAG, "onCancelled databaseError: -->" + databaseError);
            }
        });
    }

    private void callDriverLocationScheduleTrip(DataSnapshot dataSnapshot_, String ints) {

        Log.i(TAG, "collectDrivers: callDriverLocationScheduleTrip-->" + ints);

        providerLocationListDatabaseRef = FirebaseDatabase.getInstance().getReference().child("drivers_location").child(ints);
        providerLocationListValueEventListener = providerLocationListDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("l").getValue() != null) {

                    String status = Objects.requireNonNull(dataSnapshot.child("l").getValue()).toString(); //get location child

                    System.out.println("Status==>" + status);

                    String[] lat1ong = status.split(",");

                    System.out.println("length of the latlong==>" + lat1ong.length);

                    String latitude = lat1ong[0];
                    String longitude = lat1ong[1];

                    String latreplace = latitude.replaceAll("\\[", "");
                    String longreplace = longitude.replaceAll("\\]", "");

                    double templat = Double.parseDouble(latreplace);
                    double templng = Double.parseDouble(longreplace);

                    driverCurrentLatLng = new LatLng(templat, templng);

                    //driverCurrentLatLng = getBearing;

                    if (BuildConfig.DEBUG)
                        Log.i(TAG, "onDataChange: drivercurrent latlng--->" + driverCurrentLatLng + "~~~Status~~~");

                    callAddDetailsProviderList(dataSnapshot_, driverCurrentLatLng);

                } else {

                    driverCurrentLatLng = new LatLng(0.0, 0.0);

                    callAddDetailsProviderList(dataSnapshot_, driverCurrentLatLng);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i(TAG, "onCancelled databaseError: -->" + databaseError);
                driverCurrentLatLng = new LatLng(0.0, 0.0);

                callAddDetailsProviderList(dataSnapshot_, driverCurrentLatLng);
            }
        });
    }


    @SuppressLint("NotifyDataSetChanged")
    private void callAddDetailsProviderList(DataSnapshot dataSnapshot_, LatLng driverCurrentLatLng) {

        ProviderListData d = new ProviderListData();
        System.out.println("Entry of getvalue" + dataSnapshot_.getKey() + "````" + dataSnapshot_.child("trips"));
        System.out.println("Entry of getvalue" + dataSnapshot_.child("trips"));
        d.setId(String.valueOf(dataSnapshot_.getKey()));
        d.setFname(String.valueOf(dataSnapshot_.child("fname").getValue()));
        d.setLname(String.valueOf(dataSnapshot_.child("lname").getValue()));
        d.setPropic(String.valueOf(dataSnapshot_.child("pro_pic").getValue()));
        d.setEmail(String.valueOf(dataSnapshot_.child("email").getValue()));
        d.setMobile(String.valueOf(dataSnapshot_.child("mobile").getValue()));
        d.setStatus(String.valueOf(dataSnapshot_.child("status").getValue()));

        // d.setRating(String.valueOf(dataSnapshot_.child("rating").getValue()));
        // d.setServiceDes(String.valueOf(dataSnapshot_.child("service_desc").getValue()));
        // d.setCurrentDate(todayDate);
        // d.setCurrentTime(todayTime);

        Object ratingObj = dataSnapshot_.child("rating").getValue();
        if (ratingObj != null) {
            d.setRating(String.valueOf(dataSnapshot_.child("rating").getValue()));
        } else {
            d.setRating("0");
        }

        Object serviceDes = dataSnapshot_.child("service_desc").getValue();
        if (serviceDes != null) {
            d.setServiceDes(String.valueOf(dataSnapshot_.child("service_desc").getValue()));
        } else {
            d.setServiceDes("");
        }

        d.setCurrentDate(strPickupDate);
        d.setCurrentTime(strPickupTime);

        if (driverCurrentLatLng.latitude != 0.0) {

            if (currentLatLng.latitude != 0.0)
                d.setMiles(Math.round(DistanceCalculator.greatCircleInKilometers(currentLatLng.latitude, currentLatLng.longitude,
                        driverCurrentLatLng.latitude, driverCurrentLatLng.longitude)) + " KM");
            else
                d.setMiles("did not calculate miles");

        } else {

            if (currentLatLng.latitude != 0.0)
                d.setMiles("null");
            else
                d.setMiles("null");

        }

        if (providerListData != null) {
            if (providerListData.size() == 0) {
                Log.e(TAG, "callAddDetailsProviderList: ~~~~>>>>>000>>>");
                providerListData.add(d);
            }
            for (int i = 0; i < providerListData.size(); i++) {
                Log.e(TAG, "callAddDetailsProviderList: ~~~~>>>>>FOR>>>");
                if (!providerListData.get(i).getEmail().equals(String.valueOf(dataSnapshot_.child("email").getValue()))) {

                    Log.e(TAG, "callAddDetailsProviderList: ~~~~>>>>>EMAIL>>>" + providerListData.get(i).getEmail());
                    Log.e(TAG, "callAddDetailsProviderList: ~~~~>>>>>EMAIL>>>" + String.valueOf(dataSnapshot_.child("email").getValue()));

                    add = true;
                    // providerListAdapter.notifyDataSetChanged();
                } else {
                    add = false;
                }
            }
            if (add) {
                providerListData.add(d);
            }
        }

        Log.i(TAG, "onDataChange: in the providerListData-->" + providerListData.size());
        if (providerListData.size() == 0) {
            rvProviderList.setVisibility(View.GONE);
            llNoProvider.setVisibility(View.VISIBLE);
        }

        Log.i(TAG, "collectDrivers: providerListData.size()-->" + providerListData.size());
        if (providerListData.size() > 0) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Collections.sort(providerListData, Comparator.comparing(ProviderListData::getMiles));
            }

            rvProviderList.setVisibility(View.VISIBLE);
            llNoProvider.setVisibility(View.GONE);
            providerListAdapter.notifyDataSetChanged();

        } else {
            Log.i(TAG, "collectDrivers: no data");
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


    @SuppressLint("NotifyDataSetChanged")
    private void collectDriversScheduleTrip1(Map<String, Object> value, String ints) {
        //Log.i(TAG, "collectDrivers: values-->"+value+"~~~"+key);
        Log.i(TAG, "collectDrivers: sub Categoryid-->" + subCategoryId);
        Log.i(TAG, "collectDrivers: sub avialableDriver-->" + ints);
        //Get user map
        if (value != null) {
            for (Map.Entry<String, Object> entry : value.entrySet()) {
                //  Log.i(TAG, "collectDrivers: inside---- "+value.entrySet()+"````"+entry);
                Map singleUser = (Map) entry.getValue();

                //  Log.i(TAG, "collectDrivers: entry.getKey()"+entry.getKey());
                //Log.i(TAG, "collectDrivers: key"+key);
                //  Log.i(TAG, "collectDrivers: trips details-->"+String.valueOf(singleUser.get("trips")));

                //  if(String.valueOf(singleUser.get("trips")))
                // Log.i(TAG, "collectDrivers: key values"+entry.getKey());

                //    if(key.equals(entry.getKey())){
                // for(int i = 0;i<=ints.length;i++){
                if (entry.getKey().equals(ints)) {
                    // int a = ints[i];
                    Log.i(TAG, "inside the loop: key values-->" + ints);
                    // if(contains(ints, ints[i])){
                    ProviderListData d = new ProviderListData();
                    System.out.println("Entry of getvalue" + entry.getKey() + "````" + singleUser.get("strFname"));
                    d.setId(String.valueOf(entry.getKey()));
                    d.setFname(String.valueOf(singleUser.get("fname")));
                    d.setLname(String.valueOf(singleUser.get("lname")));
                    d.setPropic(String.valueOf(singleUser.get("pro_pic")));
                    d.setEmail(String.valueOf(singleUser.get("email")));
                    d.setMobile(String.valueOf(singleUser.get("mobile")));
                    d.setStatus(String.valueOf(singleUser.get("status")));
                    d.setRating(String.valueOf(singleUser.get("rating")));
                    d.setServiceDes(String.valueOf(singleUser.get("service_desc")));
                    d.setCurrentDate(todayDate);
                    d.setCurrentTime(todayTime);
                    if (currentLatLng.latitude != 0.0)
                        d.setMiles("null");
                    else
                        d.setMiles("null");

                    providerListData.add(d);
                    //  }
                }
                //  }
            }
        }

        if (providerListData.size() == 0) {
            rvProviderList.setVisibility(View.GONE);
            llNoProvider.setVisibility(View.VISIBLE);
        }
        Log.i(TAG, "collectDrivers: providerListData.size()-->" + providerListData.size());
        if (providerListData.size() > 0) {
            rvProviderList.setVisibility(View.VISIBLE);
            llNoProvider.setVisibility(View.GONE);
            providerListAdapter.notifyDataSetChanged();
        } else {
            Log.i(TAG, "collectDrivers: no data");
            //Toast.makeText(requireActivity, "No data", Toast.LENGTH_SHORT).show();
        }

    }

    public String getCurrentDate() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date todayDate = new Date();
        return currentDate.format(todayDate);
    }

    public String getCurrentTime() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat currentDate = new SimpleDateFormat("HH:mm", Locale.US);
        Date todayDate = new Date();
        return currentDate.format(todayDate);
    }

    public static boolean contains(int[] arr, int item) {
        for (int n : arr) {
            if (item == n) {
                return true;
            }
        }
        return false;
    }

    public void hideKeyBoard() {
        // Check if no view has focus:
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void CallGeofireAndGeoQuery(LatLng latLng) {
        //Geo Fire
        geoFire = new GeoFire(FirebaseDatabase.getInstance().getReference().child("drivers_location"));
        setGeofire(geoFire);
        geoQuery = getGeofire().queryAtLocation(new GeoLocation(latLng.latitude, latLng.longitude), Distance);
        geoQuery.addGeoQueryEventListener(this);
    }

    public GeoFire getGeofire() {
        return geoFire;
    }

    public void setGeofire(GeoFire geofire) {
        this.geoFire = geofire;
    }

    public void onKeyEntered(String key, GeoLocation location) {
        Log.i(TAG, "onKeyEntered: " + key);
        rvProviderList.setVisibility(View.VISIBLE);
        llNoProvider.setVisibility(View.GONE);
        try {
            collectDrivers(key, location);
        } catch (Exception e) {
            Log.i(TAG, "onKeyEntered: exception-->" + e);
        }
    }

    @Override
    public void onKeyExited(String key) {
        Log.i(TAG, "onKeyExited: " + key);
        checkAndDeleteMatchingIds(key);
        if (providerListData.size() == 0) {
            rvProviderList.setVisibility(View.GONE);
            llNoProvider.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onKeyMoved(String key, GeoLocation location) {
        Log.i(TAG, "onKeyMoved: " + key);
    }

    @Override
    public void onGeoQueryReady() {
        Log.i(TAG, "onGeoQueryReady: ");
    }

    @Override
    public void onGeoQueryError(DatabaseError error) {
        Log.i(TAG, "onGeoQueryError: ");
    }

    public void checkAndDeleteMatchingIds(String idToMatch) {
        Log.i(TAG, "checkAndDeleteMatchingIds: " + idToMatch + "~~~" + providerListData.size());
        for (int i = 0; i < providerListData.size(); i++) {
            Log.i(TAG, "checkAndDeleteMatchingIds: for id-->" + providerListData.get(i).getId() + "~~~~" + idToMatch);
            if (providerListData.get(i).getId().equals(idToMatch)) {
                Log.i(TAG, "checkAndDeleteMatchingIds: checkids-> " + providerListData.get(i).getId() + "~~~~" + i);
                removePart(i);
            }
        }
    }

    public void removePart(int position) {
        this.providerListData.remove(position);
        providerListAdapter.notifyDataSetChanged();
        // notifyItemRemoved(position);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSuccess(HashMap<String, Object> map) {

        Log.i(TAG, "onSuccess: success details-->" + map.get("strFname"));
        Log.i(TAG, "onSuccess: pro_pic details-->" + map.get("pro_pic"));
        strDriverId = String.valueOf(map.get("id"));
        strFname = String.valueOf(map.get("fname"));
        strLname = String.valueOf(map.get("lname"));
        strDate = String.valueOf(map.get("date"));
        strTime = String.valueOf(map.get("time"));
        strMiles = String.valueOf(map.get("miles"));
        strProPic = String.valueOf(map.get("pro_pic"));
        strServiceDes = String.valueOf(map.get("service_desc"));
        strRating = String.valueOf(map.get("rating"));
        String strClick = String.valueOf(map.get("click"));

        txtProviderName.setText(strFname + " " + strLname);
        rbProviderRating.setRating(Float.parseFloat(strRating));

        if (strMiles != null) {
            if (!strMiles.equals("null")) {
                txtMiles.setText(strMiles);
            } else {
                txtMiles.setText("0.0 KM");
            }
        } else {
            txtMiles.setText("0.0 KM");
        }

        /*if (strServiceDes != null) {
            if (strServiceDes.equals("null")) {
                txtDescription.setText("");
            } else {
                txtDescription.setText(strServiceDes);
            }
        }*/

        System.out.println("strProPic==>>" + Utils.BASE_URL + strProPic);

        if (strProPic.isEmpty()) {
            //  imgProviderProfile.setImageURI("http://demo.uplogictech.com/uberx/admin/images/driver/61128183_w640_h640_rabota_voditelem_spb1.png");
            int color = getResources().getColor(R.color.colorPrimary);
            RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
            roundingParams.setBorder(color, 1.0f);
            roundingParams.setRoundAsCircle(true);
            imgProviderProfile.getHierarchy().setRoundingParams(roundingParams);
        } else {
            if (strProPic != null) {
                imgProviderProfile.setImageURI(Utils.BASE_URL + strProPic);
                int color = getResources().getColor(R.color.colorPrimary);
                RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
                roundingParams.setBorder(color, 1.0f);
                roundingParams.setRoundAsCircle(true);
                imgProviderProfile.getHierarchy().setRoundingParams(roundingParams);
            } else {
                // imgProviderProfile.setImageURI("http://demo.uplogictech.com/uberx/admin/images/driver/61128183_w640_h640_rabota_voditelem_spb1.png");
                int color = getResources().getColor(R.color.colorPrimary);
                RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
                roundingParams.setBorder(color, 1.0f);
                roundingParams.setRoundAsCircle(true);
                imgProviderProfile.getHierarchy().setRoundingParams(roundingParams);
            }
        }

        if (strClick.equals("profileimage")) {

            txtProvidernName.setText(strFname + " " + strLname);
            txtBookingDate.setText(strDate);
            txtBookingTime.setText(strTime);
            txtServiceItemLabel.setText(subCategoryName);
            //  txtServiceItem.setText(Utils.Currency+subCategoryPrice); vig27
            txtServiceItem.setText(strCurrencySymbol + " " + format.format(DecimalUtils.round(Double.parseDouble(convertCurrencyPrice(subCategoryPrice)), 0)));
            txtBookingLocatoin.setText(strPickupAddress);
            // dailogBookingDetails.show();
            Double totalAgentAmount1 = Double.parseDouble(categoryCount) * Double.parseDouble(categoryAgentPrice);
            Log.e(TAG, "onClick:totalAgentAmount1 " + totalAgentAmount1);
            Log.e(TAG, "onClick: convertCurrencyPrice totalAgentAmount1 " + convertCurrencyPrice(String.valueOf(totalAgentAmount1)));
            txtAgentCost.setText(strCurrencySymbol + " " + convertCurrencyPrice(totalAgentAmount1.toString()));

                           /* strBasePriceFB = Objects.requireNonNull(dataSnapshot.child("base_fare").getValue()).toString();
                            txtBaseCharge.setText(convertCurrencyPrice(strBasePriceFB));*/
            Double totalDayAmount1 = Double.parseDouble(categoryDays) * Double.parseDouble(categoryCount) * Double.parseDouble(categoryDayPrice);
            txtDaysCost.setText(strCurrencySymbol + " " + convertCurrencyPrice(totalDayAmount1.toString()));


            if (dailogBookingDetails.getVisibility() == View.GONE) {
                dailogBookingDetails.setVisibility(View.VISIBLE);
            }

        } else if (strClick.equals("moreinfo")) {
            //dailogDriverDetails.show();

            if (dailogDriverDetails.getVisibility() == View.GONE) {
                dailogDriverDetails.setVisibility(View.VISIBLE);
            }
        }

    }

    public void removeRequestDetailsFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("providers").child(strDriverId).child("trips");

       /* Map<String, Object> taskMap1 = new HashMap<String, Object>();
        taskMap1.put(Utils.FIREBASE_SERVICE_STATUS, "0");       //update 0 status in service status
        databaseReference.updateChildren(taskMap1);*/

        databaseReference.child(Utils.FIREBASE_SERVICE_STATUS).setValue("0");

        Map<String, Object> taskMap = new HashMap<>();
        taskMap.put(Utils.FIREBASE_TRIP_CUS_ID, null);
        taskMap.put(Utils.FIREBASE_TRIP_CUS_NAME, null);
        taskMap.put("user_rating", null);
        taskMap.put("user_pickup", null);
        taskMap.put("user_drop", null);
        taskMap.put("main_service_ar", null);
        taskMap.put("sub_service_ar", null);
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

    public void countdownIntailze() {

        String strRequestCount = sprefs.getString(Utils.SP_COMMON_REQUEST_TIME_DELAY_ACCEPT, null);
        Log.e(TAG, "callRequestTimePeriod: ~~~~~~~~~~~~~~~~~~~~" + strRequestCount);
        if (strRequestCount == null) {
            strRequestCount = String.valueOf(Utils.countDownTimeLimit);
            //strRequestCount = String.valueOf(counts);
        }

        //count down timer
        countDownTimer = new CountDownTimer(Long.parseLong(strRequestCount) * 1000, 1000) {

            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                int minutes = (int) (millisUntilFinished / 1000) / 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;

                String timeRemaining = getString(R.string.countdown_timer, minutes, seconds);
                txtCountTimer.setText(timeRemaining);

//                txtCountTimer.setText("00:" + millisUntilFinished / 1000);
                // timer = Integer.valueOf(String.valueOf(millisUntilFinished));
            }

            public void onFinish() {
                new CountDownTimer(300, 100) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        try {
                            Log.i("Inside timer count", "finish");

                            if (dialogRequesting.getVisibility() == View.VISIBLE) {
                                dialogRequesting.setVisibility(View.GONE);
                            }

                            if (countDownTimer != null) {
                                countDownTimer.cancel();
                            }

                            if (rippleBackground != null) {
                                rippleBackground.stopRippleAnimation();
                            }

                            removeCustomerAcceptStatus("3");

                            showAlertDialogTimeOut();

                            //Toast.makeText(requireActivity, R.string.request_time_out, Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            e.printStackTrace();
                            //TODO: Fill in exception
                        }


//                        try {
//                            Log.i("Inside timer count", "finish");
//                            if (!boolStatusAccepted) {
//                                removeRequestDetailsFirebase();
//                            }
//                            // afterAcceptRequest(); Feb15-2019
//                            /*if (dialogRequesting!=null) {
//                                if (dialogRequesting.isShowing()) {
//                                    dialogRequesting.dismiss();
//                                }
//                            }*/
//
//
//                            if (dialogRequesting.getVisibility() == View.VISIBLE) {
//                                dialogRequesting.setVisibility(View.GONE);
//                            }
//
//                            if (countDownTimer != null) {
//                                countDownTimer.cancel();
//                            }
//                            if (providerListDatabaseRef != null) {
//                                providerListDatabaseRef.removeEventListener(providerListValueEventListener);
//                            }
//
//                            Toast.makeText(requireActivity, R.string.request_time_out, Toast.LENGTH_SHORT).show();
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            //TODO: Fill in exception
//                        }
                    }
                }.start();
            }
        };
    }

    private void afterAcceptRequest() {
        new CountDownTimer(300, 100) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                try {
                    Log.i("Inside timer count", "finish");

                    if (requestAcceptrDatabaseBRef != null) {
                        requestAcceptrDatabaseBRef.removeEventListener(requestAccepValueEventListener);
                    }

                    /*if (dialogRequesting!=null) {
                        if (dialogRequesting.isShowing()) {
                            dialogRequesting.dismiss();
                        }
                    }*/

                    if (dialogRequesting.getVisibility() == View.VISIBLE) {
                        dialogRequesting.setVisibility(View.GONE);
                    }

                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                    }
                    if (providerListDatabaseRef != null) {
                        providerListDatabaseRef.removeEventListener(providerListValueEventListener);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    //TODO: Fill in exception
                }
            }
        }.start();

    }

    //get wallet amount in firebase
    private void getCustomerWalletAmount() {

        if (customerWalletDatabaseBRef != null) {
            customerWalletDatabaseBRef.removeEventListener(customerWalletValueEventListener);
        }
        customerWalletDatabaseBRef = FirebaseDatabase.getInstance().getReference().child(Utils.FIREBASE_USER_HEADER).child(userId);
        customerWalletValueEventListener = customerWalletDatabaseBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {

                    Object walletAmountObj = dataSnapshot.child("wallet").getValue();
                    if (walletAmountObj != null) {
                        walletAmount = walletAmountObj.toString();
                    } else {
                        walletAmount = "0";
                    }
                    Object ratingObj = dataSnapshot.child("rating").getValue();
                    if (ratingObj != null) {
                        strRatingCustomer = ratingObj.toString();
                    } else {
                        strRatingCustomer = "0";
                    }

                    Object fname = dataSnapshot.child("fname").getValue();
                    if (fname != null) {
                        strFNameCustomer = fname.toString();
                    } else {
                        strFNameCustomer = sprefs.getString(Utils.SP_USER_FNAME, null);
                        if (strFNameCustomer == null || strFNameCustomer.equals("null")) {
                            strFNameCustomer = "SpotnTow";
                        }
                    }

                    Object lname = dataSnapshot.child("lname").getValue();
                    if (lname != null) {
                        strLNameCustomer = lname.toString();
                    } else {
                        strLNameCustomer = sprefs.getString(Utils.SP_USER_LNAME, null);
                        if (strLNameCustomer == null || strLNameCustomer.equals("null")) {
                            strLNameCustomer = "User";
                        }
                    }

                    Object email = dataSnapshot.child("email").getValue();
                    if (email != null) {
                        strCustomerEmail = email.toString();
                    } else {
                        strCustomerEmail = sprefs.getString(Utils.SP_USER_EMAIL, null);
                        if (strCustomerEmail == null || strCustomerEmail.equals("null")) {
                            strCustomerEmail = "User";
                        }
                    }

                    Log.i(TAG, "onDataChange in walletAmount: -->" + walletAmount);

                    getState.putString(Utils.SP_USER_WALLET_AMT, walletAmount);
                    getState.apply();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: ~~~~~~" + databaseError.toString());
            }
        });

    }

    /*private void getStripToken() {
        showProgressDialog(requireActivity);
        System.out.println("Stripe Key inside payment" + stripeKey);
        cardToSave = creditCardModuleStripe.getCard();

        if (cardToSave == null) {
            hideProgressDialog();
            Toast.makeText(requireActivity, "Invalid Card Details", Toast.LENGTH_SHORT).show();
            //mErrorDialogHandler.showError("Invalid Card Data");
            return;
        } else {

            final Stripe stripe = new Stripe(requireActivity);
            stripe.createToken(cardToSave, stripeKey, new TokenCallback() {
                public void onSuccess(@NonNull Token token) {
                    // TODO: Send Token information to your backend to initiate a charge
                    stripeTokenID = token.getId();
                    Log.e("TokenID==>", token.getId());
                    Toast.makeText(requireActivity, "Your card has accepted", Toast.LENGTH_SHORT).show();

                    hideProgressDialog();

                *//*if (dialogPayOnline.isShowing()) {
                    dialogPayOnline.dismiss();
                }*//*
                }

                public void onError(@NonNull Exception error) {
                    Log.d("Stripe error", error.getLocalizedMessage());
                    Toast.makeText(requireActivity, R.string.error_stripe_try_again_later, Toast.LENGTH_LONG).show();
                }
            });
        }
    }*/

    public String convertCurrencyPrice(String price) {
        String currencyCommonPrice = getspCurrency.getString(Utils.SP_CURRENCY_COMMON_VALUES, "1");
        double multiplePrice = Double.parseDouble(currencyCommonPrice) * Double.parseDouble(price);
        double roundedNumber = DecimalUtils.round(multiplePrice, 2);
        return String.valueOf(roundedNumber);
    }


    private void callSlideBannerImage(String key) {

        userAccessToken = sprefs.getString(Utils.SP_USER_ACCESSTOKEN, null);
        HashMap<String, Object> map = new HashMap<>();
        map.put("mode", key);
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
                                    initComponent();
                                } catch (Exception e) {
                                    Log.i("sdff", "onResponse: exception--->" + e);
                                }
                            } else {
                                callFailureBanner(key);
                            }
                        } else {
                            callFailureBanner(key);
                        }
                    } else {
                        callFailureBanner(key);
                    }
                } else {
                    callFailureBanner(key);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BannerModel> call, @NonNull Throwable t) {
                Toast.makeText(requireActivity, R.string.try_again, Toast.LENGTH_SHORT).show();
                viewImageSlider.setVisibility(View.GONE);
                rlEmptyviewAnnouncement.setVisibility(View.VISIBLE);
            }
        });
    }

    private void callFailureBanner(String key) {
        if (!key.equals("all")) {
            callSlideBannerImage("all");
        } else {
            viewImageSlider.setVisibility(View.GONE);
            rlEmptyviewAnnouncement.setVisibility(View.VISIBLE);
        }
    }

    private void initComponent() {
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
                dots[i] = new ImageView(requireActivity);
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
    public void onRefresh() {

        swipeRefresh.setRefreshing(false);
        if (providerListData != null) {
            providerListData.clear();
        }
        //getAllDriversFirebase();
        callSlideBannerImage("8");
    }


    private static class AdapterImageSlider extends PagerAdapter {

        private Context act;
        private List<Image> items;

        private AdapterImageSlider.OnItemClickListener onItemClickListener;

        private interface OnItemClickListener {
            void onItemClick(View view, Image obj);
        }

        public void setOnItemClickListener(AdapterImageSlider.OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        // constructor
        private AdapterImageSlider(Context activity, List<Image> items) {
            this.act = activity;
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

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final Image o = items.get(position);
            LayoutInflater inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.item_slider_image_main, container, false);

            SimpleDraweeView image = (SimpleDraweeView) v.findViewById(R.id.image);
            CustomTextView title = (CustomTextView) v.findViewById(R.id.title);
            CustomTextView brief = (CustomTextView) v.findViewById(R.id.brief);

            title.setText(o.name);
            brief.setText(o.caption);

            //  Log.i("image", "instantiateItem: "+o.image);
            Uri imageUri = Uri.parse(Utils.BASE_URL_BANNER_IMAGE + o.image);
            Log.i("ddd", "instantiateItem: int the list--->" + o.image);
            //  Log.i("image", "instantiateItem: "+(Utils.BASE_IMAGE_URL+o.image.toString()));
            image.setImageURI(imageUri);

            ((ViewPager) container).addView(v);

            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
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

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: ~~~~~~~~~~~~~~~~");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "onPause: ~~~~~~~~~~~~~~");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "onStop: ~~~~~~~~~~~~~~~~");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: ~~~~~~~~~~~~~~~~~~~~");
    }

    /*@Override
    public void onDestroyView() {
        if (runnable != null) handler.removeCallbacks(runnable);
        if (adViewFacebook != null) {
            adViewFacebook.destroy();
        }
        if (adViewGoogle != null) {
            adViewGoogle.destroy();
        }
        super.onDestroyView();
        Log.e(TAG, "onDestroyView: ~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }*/

    @SuppressLint("LongLogTag")
    private void callNotifySend(String userId, String driverId) {

        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("customer_id", userId);
        map1.put("driver_id", driverId);
        map1.put("mode", "accept");
        map1.put("language", strLanguageShortName);
        Log.i("callForgotPasswordDetail~~", "~~~~" + map1);
        Call<PushModel> call2 = apiInterface.doNotifySend("", map1);
        call2.enqueue(new Callback<PushModel>() {
            @Override
            public void onResponse(@NonNull Call<PushModel> call, @NonNull Response<PushModel> response) {
                hideProgressDialog();
                Log.i(TAG, "onResponse: resonse code--> " + response.code());
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    hideProgressDialog();
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("notification send successfully")) {
                        // Toast.makeText(requireActivity,Objects.requireNonNull(response.body()).getMessage(),Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireActivity, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<PushModel> call, Throwable t) {
                hideProgressDialog();
                call.cancel();
            }
        });
    }


    private void callFareEstimate() {

        showProgressDialog(requireActivity);
        /*RideNow
        service_id:1

        RIdeLater
        service_id:1
        type:ridelater
        start_date_time:2023-01-09 11:30
        end_date_time:2023-01-12 20:30*/

        hideKeyBoard();

        HashMap<String, Object> map = getStringObjectHashMap();

        Log.i("callFareEstimate~~", "~~~~" + map);
        Call<FareEstimateModel> call2 = apiInterface.doGetFareEstimate("", map);
        call2.enqueue(new Callback<FareEstimateModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<FareEstimateModel> call, @NonNull Response<FareEstimateModel> response) {

                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {

                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("success")) {

                        // Toast.makeText(requireActivity,Objects.requireNonNull(response.body()).getMessage(),Toast.LENGTH_SHORT).show();

                        // getBaseFare = callView_(response.body().getResult().getBaseFare());
                        getBaseFare = callView_(response.body().getResult().getSubTotal());

                        getTotalHrs = callView_(response.body().getResult().getTotalHrs());
                        getPricePerHr = callView_(response.body().getResult().getPricePerHr());

                        getPricePerGuard = callView_(response.body().getResult().getPricePerGuard());

                        getOverallTotal = callView_(response.body().getResult().getOverallTotal());
                        getBeforeVatAmount = callView_(response.body().getResult().getBeforeVatAmount());
                        getTotalGuardAmount = callView_(response.body().getResult().getTotalGuardAmount());

                        getPromoCode = callView_(response.body().getResult().getPromoCode());
                        getPromoDiscount = callView_(response.body().getResult().getPromoDiscount());
                        getPromoStatus = callView_(response.body().getResult().getPromoStatus());


                        txtBaseFarePayment.setText(strCurrencySymbol + " " +
                                BaseActivity.convertCurrencyPrice(requireActivity, getBaseFare));

                        txtGuardFarePayment.setText(strCurrencySymbol + " " +
                                BaseActivity.convertCurrencyPrice(requireActivity, getTotalGuardAmount));

                        txtVATFarePayment.setText(strCurrencySymbol + " " +
                                BaseActivity.convertCurrencyPrice(requireActivity, getBeforeVatAmount));

                        txtTotalFarePayment.setText(strCurrencySymbol + " " +
                                BaseActivity.convertCurrencyPrice(requireActivity, getOverallTotal));

                        if (!getTotalHrs.equals("0")) {
                            txtTotalHoursPayment.setText(convertHoursToDaysHoursMinutes(getTotalHrs));
                        } else {
                            txtTotalHoursPayment.setText("-");
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

                        getSubscriptionAmount = callView_(response.body().getResult().getSubscriptionAmount());
                        getSubscriptionPercentage = callView_(response.body().getResult().getSubscriptionPercentage());
                        out.println("~~~~~~getSubscriptionStatus--->>" + response.body().getResult().getSubscriptionStatus());

                        if (response.body().getResult().getSubscriptionStatus() ||
                                response.body().getResult().getSubscriptionStatus().toString().equals("true")) {
                            getState.putBoolean(Utils.SP_COMMON_SUBSCRIPTION_STATUS, true);
                            setVisibility(txtSubscribeFarePayment, true);
                        } else {
                            getState.putBoolean(Utils.SP_COMMON_SUBSCRIPTION_STATUS, false);
                            setVisibility(txtSubscribeFarePayment, false);
                        }
                        getState.apply();

                        callAppSettings();

                    } else {
                        hideProgressDialog();
                        Toast.makeText(requireActivity, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
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

    @NonNull
    private HashMap<String, Object> getStringObjectHashMap() {

        HashMap<String, Object> map = new HashMap<>();
        map.put("service_id", subCategoryId);
        map.put("customer_id", userId);
        map.put("total_agent", strNoOfGuardCount);

        if (tripType.equals("scheduletrip")) {
            map.put("type", "ridelater");
            map.put("start_date_time", strPickupDate + " " + strPickupTime);
            map.put("end_date_time", strPickupDateEnd + " " + strPickupTimeEnd);
        }
        if (boolPromoAccepted) {
            map.put("promocode", ValidPromoCode);
        } else {
            map.put("promocode", "");
        }
        return map;
    }


    private void callAppSettings() {

        hideKeyBoard();

        Call<AppSettingWebModel> call2 = apiInterface.doGetAppSettings();
        call2.enqueue(new Callback<AppSettingWebModel>() {
            @SuppressLint({"LongLogTag", "SetTextI18n"})
            @Override
            public void onResponse(@NonNull Call<AppSettingWebModel> call, @NonNull Response<AppSettingWebModel> response) {

                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {

                    if (response.body() != null) {

                        if (response.body().getMessage().equals("App settings listed successfully.")) {

                            hideProgressDialog();

                            PAYPAL_TYPE = Objects.requireNonNull(response.body()).getAppSetting().getPaypalType().toString();
                            CONFIG_CLIENT_ID = Objects.requireNonNull(response.body()).getAppSetting().getPaypalClientId().toString();
                            CONFIG_SECRET_ID = Objects.requireNonNull(response.body()).getAppSetting().getPaypalClientSecret().toString();
                            PAYPAL_PRIVACY_URL = Objects.requireNonNull(response.body()).getAppSetting().getPaypalPrivacyUrl().toString();
                            PAYPAL_LEGAL_URL = Objects.requireNonNull(response.body()).getAppSetting().getPaypalLegalUrl().toString();

                            STRIPE_PUBLISHKEY = Objects.requireNonNull(response.body()).getAppSetting().getPaymentPublisherKey();
                            STRIPE_SECRETKEY = Objects.requireNonNull(response.body()).getAppSetting().getPaymentSecretKey();
                            getVatPercentage = Objects.requireNonNull(response.body()).getAppSetting().getVatPercentage();

                            if (BuildConfig.DEBUG) {
                                Log.e(TAG, "onResponse: StripeKeys Admin" + STRIPE_PUBLISHKEY + "~~" + STRIPE_SECRETKEY);
                            }

                            getState.putString(Utils.SP_COMMON_STRIPE_PUBLISH_KEY, STRIPE_PUBLISHKEY);
                            getState.putString(Utils.SP_COMMON_STRIPE_SECRET_KEY, STRIPE_SECRETKEY);
                            getState.apply();

                            String adminPaypalStatus = String.valueOf(Objects.requireNonNull(response.body()).getAppSetting().getPaypalStatus());

                            if (com.instaguard.user.BuildConfig.DEBUG) {
                                Log.e(TAG, "onResponse: adminPaypalStatus --->" + adminPaypalStatus);
                            }

                            callPaypalStatusDefault_(adminPaypalStatus,
                                    getResources().getString(R.string.total_vat) + " (" + getVatPercentage + "%)");

                            callPayPalIntegration();
                        } else {
                            hideProgressDialog();
                            callPaypalStatusDefault_("0", getResources().getString(R.string.total_vat));
                        }
                    }
                } else {
                    hideProgressDialog();
                    callPaypalStatusDefault_("0", getResources().getString(R.string.total_vat));
                }
            }

            @Override
            public void onFailure(@NonNull Call<AppSettingWebModel> call, @NonNull Throwable t) {
                call.cancel();
                hideProgressDialog();
                callPaypalStatusDefault_("0", getResources().getString(R.string.total_vat));
            }
        });
    }

    private void callPaypalStatusDefault_(String number, String total_vat) {

        getState.putString(Utils.SP_COMMON_ADMIN_PAYPAL_STATUS, number);
        getState.apply();

        if (dailogBookingDetails.getVisibility() == View.GONE) {
            dailogBookingDetails.setVisibility(View.VISIBLE);
        }

        if (dialogSelectPayment.getVisibility() == View.GONE) {
            dialogSelectPayment.setVisibility(View.VISIBLE);
        }

        if (sprefs.getString(Utils.SP_COMMON_ADMIN_PAYPAL_STATUS, "0").equals("0")) { // Paypal Disable
            rbPaypalPayment.setVisibility(View.GONE);
        } else { // Paypal Enable
            rbPaypalPayment.setVisibility(View.VISIBLE);
        }

        txtVATFareLabelPayment.setText(total_vat);

        callCreatePaymentMethod_();

    }

    private void callCancelBooking() {

        if (dialogRequesting.getVisibility() == View.VISIBLE) {
            dialogRequesting.setVisibility(View.GONE);
        }

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (rippleBackground != null) {
            rippleBackground.stopRippleAnimation();
        }

        removeCustomerAcceptStatus("2");
    }

    public void showAlertDialogTimeOut() {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity, R.style.AppCompatAlertDialogStyle);
        builder.setCancelable(false);

        Typeface tf = Typeface.createFromAsset(requireActivity.getAssets(),
                getString(R.string.App_Font));

        BaseActivity.CustomTFSpan tfSpan = new BaseActivity.CustomTFSpan(tf);
        //SpannableString spannableString = new SpannableString(getString(R.string.my_wallet_add_money_btn_text));
        //spannableString.setSpan(tfSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString spannableStrings = new SpannableString(getResources().getString(R.string.request_time_out));
        spannableStrings.setSpan(tfSpan, 0, spannableStrings.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString spannableStringss = new SpannableString(getString(R.string.ok));
        spannableStringss.setSpan(tfSpan, 0, spannableStringss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        builder.setMessage(spannableStrings);

        builder.setPositiveButton(spannableStringss,
                (dialog, which) -> dialog.dismiss());

        builder.show();
    }


    private void calltimeout(String strbookingId) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("booking_id", strbookingId);
        map.put("type", "app");
        Log.e("strbookingId", "~~~~" + map);
        Call<BankModel> call2 = apiInterface.docallTimeout(map);
        call2.enqueue(new Callback<BankModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<BankModel> call, @NonNull Response<BankModel> response) {
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("success")) {
                        Log.e(TAG, "onResponse:Time out success ");
                    } else {
                        Log.e(TAG, "onResponse:Time out failure ");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<BankModel> call, @NonNull Throwable t) {
                call.cancel();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
    CardInputWidget creditCardModuleStripe;
    CheckBox cbAlsoSaveCard;
    CustomButton btnSaveCardView;

    String generateTokenAPI = "";
    String intentURL = "";
    WebSettings settings;

    CardParams cardToSave;
    String getVatPercentage;
    String STRIPE_SECRETKEY, STRIPE_PUBLISHKEY;
    Stripe stripe;
    PaymentLauncher paymentLauncher;
    String paymentId;
    private ActivityResultLauncher<Intent> paymentLauncherRegister;

    public static int STRIPE_PAYMENT_INTENT_CALL_INTERVAL = 6 * 1000; //6 seconds
    String strClient_Secret;
    private final Handler mHandler = new Handler();

    /*Paypal Code Details*/
    String CONFIG_ENVIRONMENT_PAYPAL, PAYPAL_TYPE, CONFIG_CLIENT_ID, PAYPAL_LEGAL_URL, PAYPAL_PRIVACY_URL, CONFIG_SECRET_ID;
    CoreConfig coreConfig;
    Environment environment;
    PayPalNativeShippingListener payPalNativeShippingListener;
    PayPalNativeCheckoutListener payPalNativeCheckoutListener;
    PayPalNativeCheckoutClient paypalNativeClient;
    PayPalDataCollector payPalDataCollector;


    // fetch client_secret from backend
    private String clientSecret = "";
    private CustomButton googlePayButton;
    GooglePayLauncher googlePayLauncher__;

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
                = PaymentConfiguration.getInstance(requireActivity);

        Stripe stripe = new Stripe(requireActivity, STRIPE_PUBLISHKEY);
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
                            Toast.makeText(requireActivity, "Stripe Payment Failed", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(requireActivity, getResources().getString(R.string.try_again), Toast.LENGTH_SHORT).show();
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

        STRIPE_PUBLISHKEY = requireActivity.getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE)
                .getString(Utils.SP_COMMON_STRIPE_PUBLISH_KEY, null);
        STRIPE_SECRETKEY = requireActivity.getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE)
                .getString(Utils.SP_COMMON_STRIPE_SECRET_KEY, null);

        out.println("~~~STRIPE_PUBLISHKEY~~~~~>>>" + STRIPE_PUBLISHKEY);
        out.println("~~~STRIPE_SECRETKEY~~~~~>>>" + STRIPE_SECRETKEY);

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

        PaymentConfiguration.init(requireActivity, STRIPE_PUBLISHKEY);

        PaymentConfiguration paymentConfiguration
                = PaymentConfiguration.getInstance(requireActivity);

        //   mGetContent.launch("paymentLauncher");

        paymentLauncher = PaymentLauncher.Companion.create(
                ProviderListActivity.this,
                paymentConfiguration.getPublishableKey(),
                paymentConfiguration.getStripeAccountId(),
                ProviderListActivity.this::onPaymentResult
        );


        paymentLauncherRegister = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    out.println("~~~~paymentLauncherLauncher~~~~~~" + result.getResultCode());
                    // Handle the result of the PaymentLauncher activity here
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
            hideKeyBoard();
            if (lytAddCardPaymentAlsoSaveView.getVisibility() == View.VISIBLE) {
                lytAddCardPaymentAlsoSaveView.setVisibility(View.GONE);
            }
        });

        googlePayButton = findViewById(R.id.google_pay_button);

       /* final GooglePayPaymentMethodLauncher googlePayLauncher = new GooglePayPaymentMethodLauncher(
                ProviderListActivity.this,
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

        /*PaymentsClient paymentsClient;
        int walletId;
        //STRIPE_STATUS.equals("sandbox")
        if (com.instaguard.user.BuildConfig.DEBUG) {
            walletId = WalletConstants.ENVIRONMENT_TEST;
        } else {
            walletId = WalletConstants.ENVIRONMENT_PRODUCTION;
        }
        paymentsClient = Wallet.getPaymentsClient(this,
                new Wallet.WalletOptions.Builder()
                        .setEnvironment(walletId)
                        .build());
        paymentsClient.isReadyToPay();*/

        GooglePayEnvironment test;

        // change to debug when your work with demo
        if (com.instaguard.user.BuildConfig.DEBUG) {
            test = GooglePayEnvironment.Test;
            out.println("gPayFromDebug");
        } else {
            test = GooglePayEnvironment.Production;
            out.println("gPayFromProduction");
        }

        googlePayLauncher__ = new GooglePayLauncher(
                ProviderListActivity.this,
                new GooglePayLauncher.Config(
                        test,
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
                (Application) requireActivity.getApplicationContext(), coreConfig, Utils.PAYPAL_RETURN_URL);
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
                BaseActivity.callClickAction(btnBookNow, true);
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
                BaseActivity.callClickAction(btnBookNow, true);
                Toast.makeText(requireActivity, payPalSDKError.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPayPalCheckoutCanceled() {
                // the user canceled the flow
                System.out.println("~~~~onPayPalCheckoutCanceled~~~~~`");
                hideProgressDialog();
                BaseActivity.callClickAction(btnBookNow, true);
                Toast.makeText(requireActivity, getResources().getString(R.string.paypal_cancelled),
                        Toast.LENGTH_SHORT).show();
            }
        };

        paypalNativeClient.setShippingListener(payPalNativeShippingListener);
        paypalNativeClient.setListener(payPalNativeCheckoutListener);

    }


    private void callUpdateStripAlsoSaveCard(String total_amount, String tripType) {

        if (lytAddCardPaymentAlsoSaveView.getVisibility() == View.GONE) {
            lytAddCardPaymentAlsoSaveView.setVisibility(View.VISIBLE);
        }

        btnSaveCardView.setOnClickListener(view -> stripePayment(total_amount, tripType));
    }

    @SuppressLint("LongLogTag")
    private void stripePayment(String total_amount, String tripType) {
        showProgressDialog(requireActivity);
        cardToSave = creditCardModuleStripe.getCardParams();
        if (cardToSave == null) {
            hideProgressDialog();
            Toast.makeText(requireActivity, R.string.invalid_card_details, Toast.LENGTH_SHORT).show();
        } else {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "stripePayment: " + cardToSave);
                Log.e(TAG, "STRIPE_PUBLISHKEY: " + STRIPE_PUBLISHKEY);
            }
            if (cbAlsoSaveCard.getVisibility() == View.VISIBLE) {
                if (cbAlsoSaveCard.isChecked()) { // Save Card
                    callPaymentIntent_API(total_amount, "1", tripType, "stripe");
                } else { // Option only token get
                    callPaymentIntent_API(total_amount, "0", tripType, "stripe");
                }
            } else { // Default State Token get
                callPaymentIntent_API(total_amount, "0", tripType, "stripe");
            }
        }
    }

    /*-*************************** ONE TIME PAYMENT *************************/
    private void onPaymentResult(PaymentResult paymentResult) {

        System.out.println("~~~~~~paymentResult~~~~~" + paymentResult);

        hideKeyBoard();

        String message = "";
        if (paymentResult instanceof PaymentResult.Completed) {
            message = "Completed!";

            if (lytAddCardPaymentAlsoSaveView.getVisibility() == View.VISIBLE) {
                lytAddCardPaymentAlsoSaveView.setVisibility(View.GONE);
            }
            paymentId = BaseActivity.getHashMapPaymentId(requireActivity, "InstaGuard" + userId);

            if (BaseActivity.getSetUpIntentOneSave_(requireActivity, "InstaGuard" + userId).equals("save_card")) {
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

        Toast.makeText(requireActivity, "PaymentResult~~" + message, Toast.LENGTH_SHORT).show();
    }

    private void callPaymentIntent_API(String total_amount, String save_card_status, String tripType, String paymode) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("customer_id", userId);
        map.put("amount", total_amount);
        map.put("save_card", save_card_status);

        Call<PaymentIntentModel> call2 = apiInterface.doCreatePaymentIntentModel("token", map);

        showProgressDialog(requireActivity);

        call2.enqueue(new Callback<PaymentIntentModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<PaymentIntentModel> call, @NonNull Response<PaymentIntentModel> response) {
                hideProgressDialog();

                if (response.body() != null) {

                    String client_secret = response.body().getClient_secret();
                    paymentId = response.body().getResult().getId();

                    setHashMapSaveCardStatus(requireActivity, "InstaGuard" + userId, save_card_status);
                    setHashMapPaymentId(requireActivity, "InstaGuard" + userId, paymentId);
                    setSetUpIntentOneSave_(requireActivity, "InstaGuard" + userId, "one_time");

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
    private void callSaveUpdateDetails(String price_, String strTripType, String paymode) {

        try {

            HashMap<String, Object> map = new HashMap<>();

            if (strTripType.equals("scheduletrip")) {
                map.put("mode", "ridelater");
                map.put("start_date", strPickupDate);
                map.put("start_time", strPickupTime);
                map.put("end_date", strPickupDateEnd);
                map.put("end_time", strPickupTimeEnd);
                map.put("start_date_time", strPickupDate + " " + strPickupTime);
                map.put("end_date_time", strPickupDateEnd + " " + strPickupTimeEnd);
            } else {
                map.put("mode", "ridenow");
            }

            map.put("category", categoryId);
            map.put("customer_id", userId);
            map.put("service_id", subCategoryId);

            map.put("special_instruction", strSpecialIns);

            map.put("customer_lat", strPickupLat);
            map.put("customer_lon", strPickupLng);
            map.put("customer_location", strPickupAddress);

            map.put("drop_location", "NoDrop");
            map.put("drop_lat", "0");
            map.put("drop_lon", "0");

            //0-rb_mywallet 1-view
            //2-rb_payonline 3-view
            //4-rb_cashonhand  5-view
            //6-rb_paypal 7-view
            //8-rb_google_pay 9-view
            //10-google_pay_button

            // map.put("payment_type", "stripe");
            if (selectedId == 0 /*|| rbStripePayment.isChecked()*/) {  //Card payment
                map.put("payment_type", "wallet");
            } else if (selectedId == 2 /*|| rbStripePayment.isChecked()*/) {  //Card payment
                map.put("payment_type", "stripe");
            } else if (selectedId == 6 /*|| rbPaypalPayment.isChecked()*/) { //Paypal
                map.put("payment_type", "paypal");
            } else if (selectedId == 8 /*|| rbGooglePayPayment.isChecked()*/) {  //Google Pay
                map.put("payment_type", "stripe_gpay");
            }

            map.put("language", strLanguageShortName);

            map.put("total_hrs", getTotalHrs);
            map.put("price_per_hr", getPricePerHr);
            map.put("vat_amount", getBeforeVatAmount);
            map.put("total_amount", getOverallTotal);

            map.put("emergency_msg", strEmergencyMsg);
            map.put("total_agent", strNoOfGuardCount);

            if (sprefs.getBoolean(Utils.SP_COMMON_SUBSCRIPTION_STATUS, false)) { //SUBSCRIPTION_ Enable
                map.put("subscription_status", "1");
                map.put("discount_amount", getSubscriptionAmount);
                map.put("discount_percentage", getSubscriptionPercentage);
            } else {
                map.put("subscription_status", "0");
                map.put("discount_amount", "0");
                map.put("discount_percentage", "0");
            }

            if (boolPromoAccepted) {
                map.put("promocode", ValidPromoCode);
            } else {
                map.put("promocode", "");
            }

            saveHashMap_only(requireActivity, "InstaGuard" + userId, map);

        } catch (Exception e) {
            e.printStackTrace();
        }

        HashMap<String, Object> map_ = new HashMap<>();
        map_.put("customer_id", userId);
        map_.put("amount", price_);
        map_.put("type", "trip");

        Log.i("callUpdateDetails", "~~~~" + map_);
        Call<SaveWalletPaymentIntentModel> call2 = apiInterface.doSaveWalletPaymentIntentModel("token", map_);
        showProgressDialog(requireActivity);
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

                                    setHashMapPaymentId(requireActivity, "InstaGuard" + userId, paymentId);
                                    setSetUpIntentOneSave_(requireActivity, "InstaGuard" + userId, "save_card");

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

                                                        setHashMapPaymentId(requireActivity, "InstaGuard" + userId, paymentId);
                                                        setSetUpIntentOneSave_(requireActivity, "InstaGuard" + userId, "save_card");

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
                                                        Toast.makeText(requireActivity, getResources().getString(R.string.try_again), Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(requireActivity, e.toString(), Toast.LENGTH_SHORT).show();
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

                            callPaymentIntent_API(getOverallTotal, "0", tripType, "google_pay");

                        } else {

                            //Toast.makeText(requireActivity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            if (getOverallTotal != null && !getOverallTotal.equals("0")) {
                                callUpdateStripAlsoSaveCard(getOverallTotal, tripType);
                            } else {
                                //callFareEstimate();
                                Toast.makeText(requireActivity, getResources().getString(R.string.total_amount_empty_try_again), Toast.LENGTH_SHORT).show();
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

    private void CallRequestBooking(String key_one_save_card, String strTripType_PaymentId) {

        HashMap<String, Object> map = new HashMap<>();
        try {

            String save_card_status = "0";
            /*if (cbAlsoSaveCard.isChecked()) {
                save_card_status = "1";
            } else {
                save_card_status = "0";
            }*/

            if (tripType.equals("scheduletrip")) {
                map.put("mode", "ridenow");
                map.put("start_date", strPickupDate);
                map.put("start_time", strPickupTime);
                map.put("end_date", strPickupDateEnd);
                map.put("end_time", strPickupTimeEnd);
                map.put("start_date_time", strPickupDate + " " + strPickupTime);
                map.put("end_date_time", strPickupDateEnd + " " + strPickupTimeEnd);
                map.put("trip_type", "2");
                /*if(result_.length() > 1){
                    map.put("reference_id", result_ );
                }*/
                if (sendUniqueIdList != null && !sendUniqueIdList.isEmpty()){
                    map.put("reference_id", sendUniqueIdList);
                }else {
                    map.put("reference_id", getUniqueIdList );
                }

            } else {
                out.println("result of size "+ result_.length() + " ~~~ "+ result_.toString().length() + " ~~~ " + sendUniqueIdList);

                map.put("mode", "ridenow");
                /*if(result_.length() > 1){
                    map.put("reference_id", result_ );
                }*/
                if (sendUniqueIdList != null && !sendUniqueIdList.isEmpty()){
                    map.put("reference_id", sendUniqueIdList);
                }else {
                    map.put("reference_id", getUniqueIdList );
                }
            }

            map.put("category", categoryId);
            map.put("customer_id", userId);
            map.put("service_id", subCategoryId);

            map.put("special_instruction", strSpecialIns);

            map.put("customer_lat", strPickupLat);
            map.put("customer_lon", strPickupLng);
            map.put("customer_location", strPickupAddress);

            map.put("drop_location", "NoDrop");
            map.put("drop_lat", "0");
            map.put("drop_lon", "0");

            //0-rb_mywallet 1-view
            //2-rb_payonline 3-view
            //4-rb_cashonhand  5-view
            //6-rb_paypal 7-view
            //8-rb_google_pay 9-view
            //10-google_pay_button

            // map.put("payment_type", "stripe");
            if (selectedId == 0) {  //Wallet payment
                map.put("payment_type", "wallet");
            } else if (selectedId == 2 /*|| rbStripePayment.isChecked()*/) {  //Card payment
                map.put("payment_type", "stripe");
                if (!strTripType_PaymentId.equals("use_wallet")) {
                    map.put("stripe_token", strTripType_PaymentId);
                }
            } else if (selectedId == 6 /*|| rbPaypalPayment.isChecked()*/) { //Paypal
                map.put("payment_type", "paypal");
                map.put("stripe_token", strTripType_PaymentId);
            } else if (selectedId == 8 /*|| rbGooglePayPayment.isChecked()*/) {  //Google Pay
                map.put("payment_type", "stripe_gpay");
                if (!strTripType_PaymentId.equals("use_wallet")) {
                    map.put("stripe_token", strTripType_PaymentId);
                }
            }

            map.put("language", strLanguageShortName);

            map.put("total_hrs", getTotalHrs);
            map.put("price_per_hr", getPricePerHr);
            map.put("vat_amount", getBeforeVatAmount);
            map.put("total_amount", getOverallTotal);

            map.put("emergency_msg", strEmergencyMsg);
            map.put("total_agent", strNoOfGuardCount);

            if (sprefs.getBoolean(Utils.SP_COMMON_SUBSCRIPTION_STATUS, false)) { //SUBSCRIPTION_ Enable
                map.put("subscription_status", "1");
                map.put("discount_amount", getSubscriptionAmount);
                map.put("discount_percentage", getSubscriptionPercentage);
            } else {
                map.put("subscription_status", "0");
                map.put("discount_amount", "0");
                map.put("discount_percentage", "0");
            }

            if (boolPromoAccepted) {
                map.put("promocode", ValidPromoCode);
            } else {
                map.put("promocode", "");
            }

            saveHashMap(requireActivity, "InstaGuard" + userId, map, save_card_status);
            setSetUpIntentOneSave_(requireActivity, "InstaGuard" + userId, key_one_save_card);

        } catch (Exception e) {
            e.printStackTrace();

            map.put("customer_id", userId);
            map.put("drop_location", "NoDrop");
            map.put("drop_lat", "0");
            map.put("drop_lon", "0");

            if (BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("mode") != null) {

                map.put("mode", BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("mode"));

                if (Objects.requireNonNull(BaseActivity.getHashMap(requireActivity,
                        "InstaGuard" + userId).get("trip_type")).toString().equals("2")) {

                    if (BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("start_date") != null) {
                        map.put("start_date", BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("start_date"));
                    }
                    if (BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("start_time") != null) {
                        map.put("start_time", BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("start_time"));
                    }
                    if (BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("end_date") != null) {
                        map.put("end_date", BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("end_date"));
                    }
                    if (BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("end_time") != null) {
                        map.put("end_time", BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("end_time"));
                    }

                    if (BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("start_date_time") != null) {
                        map.put("start_date_time", BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("start_date_time"));
                    }
                    if (BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("end_date_time") != null) {
                        map.put("end_date_time", BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("end_date_time"));
                    }
                    if (BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("trip_type") != null) {
                        map.put("trip_type", "2");
                    }

                }
            }

            if (BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("category") != null) {
                map.put("category", BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("category"));
            }
            if (BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("service_id") != null) {
                map.put("service_id", BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("service_id"));
            }
            if (BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("special_instruction") != null) {
                map.put("special_instruction", BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("special_instruction"));
            }
            if (BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("customer_lat") != null) {
                map.put("customer_lat", BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("customer_lat"));
            }
            if (BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("customer_lon") != null) {
                map.put("customer_lon", BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("customer_lon"));
            }
            if (BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("customer_location") != null) {
                map.put("customer_location", BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("customer_location"));
            }
            if (BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("language") != null) {
                map.put("language", BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("language"));
            }
            if (BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("total_hrs") != null) {
                map.put("total_hrs", BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("total_hrs"));
            }
            if (BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("price_per_hr") != null) {
                map.put("price_per_hr", BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("price_per_hr"));
            }
            if (BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("vat_amount") != null) {
                map.put("vat_amount", BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("vat_amount"));
            }
            if (BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("total_amount") != null) {
                map.put("total_amount", BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("total_amount"));
            }
            if (BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("promocode") != null) {
                map.put("promocode", BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("promocode"));
            }

            if (BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("emergency_msg") != null) {
                map.put("emergency_msg", BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("emergency_msg"));
            }


            if (BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("subscription_status") != null) {
                map.put("subscription_status", BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("subscription_status"));
            }

            if (BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("discount_amount") != null) {
                map.put("discount_amount", BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("discount_amount"));
            }

            if (BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("discount_percentage") != null) {
                map.put("discount_percentage", BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("discount_percentage"));
            }


            if (BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("payment_type") != null) {

                map.put("payment_type", BaseActivity.getHashMap(requireActivity, "InstaGuard" + userId).get("payment_type"));

                if (Objects.requireNonNull(BaseActivity.getHashMap(requireActivity,
                        "InstaGuard" + userId).get("payment_type")).toString().equals("stripe")) {
                    if (!strTripType_PaymentId.equals("use_wallet")) {
                        map.put("stripe_token", strTripType_PaymentId);
                    }
                } else if (Objects.requireNonNull(BaseActivity.getHashMap(requireActivity,
                        "InstaGuard" + userId).get("payment_type")).toString().equals("stripe_gpay")) {
                    if (!strTripType_PaymentId.equals("use_wallet")) {
                        map.put("stripe_token", strTripType_PaymentId);
                    }
                } else if (Objects.requireNonNull(BaseActivity.getHashMap(requireActivity,
                        "InstaGuard" + userId).get("payment_type")).toString().equals("paypal")) {
                    map.put("stripe_token", strTripType_PaymentId);
                }

            }
        }

        out.println("provider list activity api call ");

        showProgressDialog(requireActivity);
        out.println("request params "+ map);

        Call<SendRequestModel> call2 = apiInterface.doRequestSendDetails(map);
        call2.enqueue(new Callback<SendRequestModel>() {
            @Override
            public void onResponse(@NonNull Call<SendRequestModel> call, @NonNull Response<SendRequestModel> response) {
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    hideProgressDialog();
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Request saved successfully.")) {

                        if (tripType.equals("scheduletrip")) {

                            if (dialogAcceptSchedule.getVisibility() == View.GONE) {
                                dialogAcceptSchedule.setVisibility(View.VISIBLE);
                            }

                            boolPromoAccepted = false;

                        } else {
                            removeCustomerAcceptStatus("1");

                            if (dialogRequesting.getVisibility() == View.GONE) {
                                dialogRequesting.setVisibility(View.VISIBLE);
                                if (countDownTimer != null) {
                                    countDownTimer.start();
                                }
                            }

                            if (rippleBackground != null) {
                                rippleBackground.startRippleAnimation();
                            }

                            // rlProgressBarAddNotes.setVisibility(View.VISIBLE);

                            if (dialogRequesting.getVisibility() == View.GONE) {
                                dialogRequesting.setVisibility(View.VISIBLE);
                                if (countDownTimer != null) {
                                    countDownTimer.start();
                                }
                            }

                            if (rippleBackground != null) {
                                rippleBackground.startRippleAnimation();
                            }

                            String strbookingId = response.body().getServiceRequest().getBookingId().toString();
                            calltimeout(strbookingId);

                            if (!strbookingId.equals("null") && !strbookingId.equals("")) {
                                // btnCancelRequest.setVisibility(View.VISIBLE);
                                btnCancelRequest.setVisibility(View.INVISIBLE);
                            } else {
                                btnCancelRequest.setVisibility(View.INVISIBLE);
                            }
                        }

                    } else if(response.body().getMessage().equals("Driver is not available")) {
                        Toast.makeText(requireActivity, R.string.no_guard_available, Toast.LENGTH_SHORT).show();
                    } else {

                        btnCancelRequest.setVisibility(View.INVISIBLE);

                        Toast.makeText(requireActivity, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        hideProgressDialog();

                        if (dialogRequesting.getVisibility() == View.VISIBLE) {
                            dialogRequesting.setVisibility(View.GONE);
                        }

                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                        }

                        if (rippleBackground != null) {
                            rippleBackground.stopRippleAnimation();
                        }

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

    /*-*************************** SAVE CARD PAYMENT *************************/

    private void callCreatePayPalPayment(String getOverallTotal) {

        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("amount", getOverallTotal);

        Log.i("", "~callCreatePayPalPayment~~~" + map1);

        showProgressDialog(requireActivity);
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
                            System.out.println("~~~~PayPalNativeCheckoutRequest~~~strCustomerEmailFB~~~" + strCustomerEmail);
                            //}
                            PayPalNativeCheckoutRequest request = new PayPalNativeCheckoutRequest(id, strCustomerEmail);
                            paypalNativeClient.startCheckout(request);

                        } catch (Exception e) {
                            e.printStackTrace();
                            BaseActivity.callClickAction(btnBookNow, true);
                        }

                    } else {

                        BaseActivity.callClickAction(btnBookNow, true);
                        Toast.makeText(requireActivity, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onResponse: success");
                    }

                } else if (response.code() == RESPONSECODE_401) {
                    //  callRefreshTokenBalanceAmount(stripeTokenID, cardToSave);h
                    BaseActivity.callClickAction(btnBookNow, true);
                    Toast.makeText(requireActivity, "Response Error 401", Toast.LENGTH_SHORT).show();

                } else if (response.code() == RESPONSECODE_500) {
                    BaseActivity.callClickAction(btnBookNow, true);
                    Toast.makeText(requireActivity, "Response Error 500", Toast.LENGTH_SHORT).show();
                    hideProgressDialog();
                }
            }

            @Override
            public void onFailure(@NonNull Call<PayPalCreateModel> call, @NonNull Throwable t) {
                call.cancel();
                BaseActivity.callClickAction(btnBookNow, true);
                hideProgressDialog();
            }
        });
    }


    private void onGooglePayReady(boolean isReady) {
        out.println("~~~~~onGooglePayReady~~" + isReady);
        googlePayButton.setEnabled(isReady);
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
        out.println("~~~~~onGooglePayReady~~isReady" + isReady);
        googlePayButton.setEnabled(isReady);
        if (!isReady) {
            BaseActivity.callClickAction(rbGooglePayPayment, false);
            rbGooglePayPayment.setText("Google Pay");
        }
    }

    public void onGooglePayResult_(@NotNull GooglePayLauncher.Result result) {
        out.println("~~~~~result~isReady~" + result);
        if (result instanceof GooglePayLauncher.Result.Completed) {
            // Payment details successfully captured.
            // Send the paymentMethodId to your server to finalize payment.
            out.println("~~~~~Completed~~");

            baseActivity.showSnackbar(this,contentView,"Payment authorized successfully.","other");

            new Handler().postDelayed(() -> {
                        if (BaseActivity.getSetUpIntentOneSave_(requireActivity, "InstaGuard" + userId).equals("save_card")) {
                            CallRequestBooking("save_card", paymentId);
                        } else {
                            CallRequestBooking("one_time", paymentId);
                        }
                    }, 2000);

        } else if (result instanceof GooglePayLauncher.Result.Canceled) {
            // User canceled the operation
            baseActivity.showSnackbar(this,contentView,"Canceled","warning");

            out.println("~~~~~Canceled~~");
        } else if (result instanceof GooglePayLauncher.Result.Failed) {
            // Operation failed; inspect `result.getError()` for the exception
            baseActivity.showSnackbar(this,contentView,((GooglePayLauncher.Result.Failed) result).getError().getMessage(),"warning");
        }
    }

    @Override
    public void onDestroy() {
        if (runnable != null) handler.removeCallbacks(runnable);
        if (adViewFacebook != null) {
            adViewFacebook.destroy();
        }
        if (adViewGoogle != null) {
            adViewGoogle.destroy();
        }
        super.onDestroy();

        Utils.CURRENT_TAG = null;
        if (webView != null) {
            webView.clearCache(true);
            webView.clearFormData();
            webView.clearHistory();
            webView.clearSslPreferences();
        }

        if (paymentLauncherRegister != null) {
            paymentLauncherRegister.unregister();
        }

        stopRepeatingTask();

        Log.e(TAG, "onDestroy: ~~~~~~~~~~~~~~~~~~~");
    }


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

        if (sprefs.getString(Utils.SP_COMMON_ADMIN_PAYPAL_STATUS, "0").equals("0")) { // Paypal Disable
            payPalPayment = new InternalPaymentMethod_("2", getResources().getString(R.string.select_payment_paypal), "paypal", 0);
        } else { // Paypal Enable
            payPalPayment = new InternalPaymentMethod_("2", getResources().getString(R.string.select_payment_paypal), "paypal", 1);
        }
        paymentMethodList.add(payPalPayment);

        if (!sprefs.getBoolean(Utils.SP_COMMON_WALLET_STATUS, false)) { // Wallet Disable
            walletPayment = new InternalPaymentMethod_("4", getResources().getString(R.string.select_payment_my_wallet), "wallet", 0);
        } else { // Wallet Enable
            walletPayment = new InternalPaymentMethod_("4", getResources().getString(R.string.select_payment_my_wallet), "wallet", 1);
        }
        paymentMethodList.add(walletPayment);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity, LinearLayoutManager.VERTICAL, false);
        rcvSelectPayment.setLayoutManager(linearLayoutManager);

        paymentAdapter = new PaymentAdapter(ProviderListActivity.this, paymentMethodList);
        rcvSelectPayment.setAdapter(paymentAdapter);
        paymentAdapter.selected(-1);

    }

}