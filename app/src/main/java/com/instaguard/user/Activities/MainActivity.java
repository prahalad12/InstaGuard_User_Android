package com.instaguard.user.Activities;

import static com.instaguard.user.Activities.BaseActivity.EMOJI_FILTER;
import static com.instaguard.user.Activities.BaseActivity.hideProgressDialog;
import static com.instaguard.user.Activities.BaseActivity.setVisibility;
import static com.instaguard.user.Activities.BaseActivity.showProgressDialog;
import static com.instaguard.user.Activities.ProviderListActivity.result_;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_200;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_201;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_401;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_500;

import static java.lang.System.out;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.os.LocaleListCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.constant.TransitMode;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.model.Step;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.crashlytics.CustomKeysAndValues;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.model.Unit;
import com.instaguard.user.Adapter.CurrencyAdapter;
import com.instaguard.user.Adapter.LanguageAdapter;
import com.instaguard.user.Adapter.MapCategoryAdapter;
import com.instaguard.user.Adapter.MapCategorySubAdapter;
import com.instaguard.user.BuildConfig;
import com.instaguard.user.CustomViews.CustomButton;
import com.instaguard.user.CustomViews.CustomEditText;
import com.instaguard.user.CustomViews.CustomTextView;
import com.instaguard.user.CustomViews.CustomTypefaceSpan;
import com.instaguard.user.FingerPrint.Activity.SettingsFaceFingerscreen;
import com.instaguard.user.Fragments.EditSettingsFragment;
import com.instaguard.user.Fragments.GuardUniqueIdFragment;
import com.instaguard.user.Helpers.CustomDialog.DialogUtils;
import com.instaguard.user.Helpers.DecimalUtils;
import com.instaguard.user.Helpers.ExpansionPanelLayout.ExpansionLayout;
import com.instaguard.user.Helpers.FusedLocation.FusedLocationSingleton;
import com.instaguard.user.Helpers.Geocoder.FetchAddressIntentService;
import com.instaguard.user.Helpers.Localizationactivity.ui.LocalizationActivity;
import com.instaguard.user.Helpers.PlaceAutoComplete.Prediction;
import com.instaguard.user.Helpers.RatingBar.SimpleRatingBar;
import com.instaguard.user.Helpers.RecyclerViewsTools.ItemOffsetDecoration;
import com.instaguard.user.Helpers.RecyclerViewsTools.RecyclerTouchListener;
import com.instaguard.user.Helpers.RecyclerViewsTools.StartSnapHelper;
import com.instaguard.user.Helpers.Retrofit.APIClient;
import com.instaguard.user.Helpers.Retrofit.APIInterface;
import com.instaguard.user.Helpers.Utils;
import com.instaguard.user.Models.AppSettingCurrencyModel;
import com.instaguard.user.Models.AppSettingLanguageModel;
import com.instaguard.user.Models.AppSettingSmsTemplate;
import com.instaguard.user.Models.AppSettingWebModel;
import com.instaguard.user.Models.Banner.BannerData;
import com.instaguard.user.Models.Banner.BannerModel;
import com.instaguard.user.Models.CategoryModel;
import com.instaguard.user.Models.CategorySubModel;
import com.instaguard.user.Models.EmergencyViewDataModel;
import com.instaguard.user.Models.EmergencyViewModel;
import com.instaguard.user.Models.LogoutModel;
import com.instaguard.user.Models.OnGoingJobsData;
import com.instaguard.user.Models.OnGoingJobsModel;
import com.instaguard.user.Models.PanicAddAdminModel;
import com.instaguard.user.Models.ProfileModel;
import com.instaguard.user.Models.ProfileUpdateModel;
import com.instaguard.user.Models.VehicleCategoryData;
import com.instaguard.user.Models.VehicleCategoryModel;
import com.instaguard.user.Models.VehicleSubCategoryData;
import com.instaguard.user.Models.VehicleSubCategoryModel;
import com.instaguard.user.R;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.UnaryOperator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends LocalizationActivity implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback, GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraMoveCanceledListener, GoogleMap.OnCameraIdleListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ValueEventListener, EditSettingsFragment.SuccessResponseEditProfile {


    /*Geocoder and Fusedlocation Start*/
    private static final int ACCESS_FINE_LOCATION_INTENT_ID = 3;
    private static final String ADDRESS_REQUESTED_KEY = "address-request-pending";
    private static final String LOCATION_ADDRESS_KEY = "location-address";
    /*Navigation Drawer End*/

    public static SimpleDraweeView navigationProfileImage;
    public static Location locFusedLocation;
    public static CustomTextView txtOnGoingJobsLabel;
    public static CustomTextView txtSubscriptionPlanSetupLabel;
    @SuppressLint("StaticFieldLeak")
    public static FrameLayout frameMap;
    public static DatabaseReference driverSearchDatabaseBRef;
    public static ValueEventListener driverSearchValueEventListener;
    public static String cameraIdleValue = "1";
    //Requesting Accept Dialog

    public View dailogAcceptDriver;
    public CustomTextView txtViewOnGoingJobs, txtCancel, txtHeaderDialogAcceptDriver, txtDescriptionDialogAcceptDriver;
    public CustomTextView txtViewOnGoingJobs1, txtCancel1, txtHeaderDialogDeclineDriver, txtDescriptionDialogDeclineDriver;
    public RelativeLayout rlSuccess, rlSuccess1, rlParentLytAcceptDriver;
    public static Boolean booleanCheckStatusAccepted = false;

    /*Navigation Drawer start*/
    @SuppressLint("StaticFieldLeak")
    static DrawerLayout drawerLayout;
    /*Geocoder and Fusedlocation End*/
    static String TAG = "MainActivity";
    static SharedPreferences.Editor getState, getStateCurrency, getRequestState;
    /*Googlemap End*/

    public boolean beginMoveCamera = true;
    public SupportMapFragment mapFragment;
    public String bookingId;
    NavigationView navigationView;
    CustomTextView txtVersion, navigationName, navigationWalletAmount, navigationCustomerId;
    SimpleRatingBar navRbuserrating;
    ImageView navigationSetting;
    public static CustomTextView navigationSubsPlan;
    RelativeLayout navigationLogoutUser;
    Dialog myDialog;
    /*Googlemap Start*/
    MaterialEditText edtNoOfGuards;
    MaterialEditText edtHrs;

    CustomEditText edtTellUsEmergency;
    View lytBookNowBookLater;
    CustomButton btnBookNow;
    CustomButton btnBookLater;

    LinearLayout rlPickupDate, rlPickupTime;
    LinearLayout rlPickupDate_End, rlPickupTime_End;
    CustomTextView txtPickupDate,txt_book_a_guard;
    CustomTextView txtPickupDate_End;
    CustomTextView txtPickupTime;
    CustomTextView txtPickupTime_End;
    CustomButton btnContinueScheduleTrip;
    ImageButton imgbtnBackScheduleTrip;
    View lytScheduleTripConfirmView;

    String strdate;

    String strdateView;

    String strtime;

    long difference_In_Days;
    Integer difference_In_Month;

    String strdate_end;
    String strdate_end_view;
    String strtime_end;

    int selectedHour;
    String selectedDay;

    String selectedDate, selectedTime;
    AppCompatButton uniqueBtn;

    /*Category End*/
    /*Category Start*/
    ArrayList<CategoryModel> categoryModelArray;
    private ArrayList<CategorySubModel> categoryModelArraySub;
    private CategorySubModel listFeedSub;

    private CategoryModel listFeed;

    private String getSubcatName, getSubcatId, getSubcatMainId, getSubcatPrice, getSubcatAgentPrice = "0", getSubcatDayPrice = "0";

    RecyclerView rvCategory;
    RecyclerView rvSubCategory;
    MapCategoryAdapter mapCategoryAdapter;
    MapCategorySubAdapter mapCategorySubAdapter;
    LinearLayoutManager linearLayoutManager;
    LinearLayoutManager linearLayoutManagerSub;
    AppCompatImageView leftArrow;
    /*Layout Details*/
    AppCompatImageView rightArrow;
    int currentVisibleItem = 0;

    ProgressBar progressBarMain;
    CustomTextView txtRefresh, txtNoRecords;
    View rlParentLytProgress;
    CustomTextView txtRefreshMain, txtLoadingMain, txtNoRecordsMain;

    RelativeLayout rlPickupLytParent;

    /*Trip Status*/
    View rlParentLytMapMain;
    View lytJobInprogress;
    View lytSMSalert;
    CustomButton btnSmsAlert;
    CustomTextView txtUpdateLangSetup, txtUpdateCurrencySetup, txtSmsAlertContent;
    Dialog dialogCustomerinfo;
    List<String> mobileNumbers = new ArrayList<>();
    String smsTemplateSubject, smsTemplateContent;
    boolean doubleBackToExitPressedOnce = false;
    String minWalletAmount;

    public static CustomTextView txtDropLocation, txtPickupLocation;

    ImageView imgCenterMarker;
    String userId, userAccessToken, tokenExpTime, strCurrencySymbol, strLanguageShortName,
            strLanguage, strCurrency, strUserProPic, strUserfname, currencyCommonPrice, strWalletAmt, deleteMyAccount, strDeleteStatus,
            strUserlname;
    private static Marker markerPickupConfirmBooking, markerDropConfirmBooking, markerCurrentLocationMan;
    private static Polyline directionPolyline;
    double roundedNumber;
    static SharedPreferences spCommon;
    SharedPreferences spCurrency, shPref;
    SharedPreferences.Editor editor;
    LatLng center_point_Idle = new LatLng(0.0, 0.0);
    DatabaseReference customerWalletDatabaseBRef;
    ValueEventListener customerWalletValueEventListener;
    List<AppSettingCurrencyModel> getCurrencyList = null;
    static CustomButton btnDone, btnNext;
    CardView cvDropLyt;
    RelativeLayout rlCurrentLocView, rlCenterMarker, rlDropLytParent;

    View lytMapMainPage;
    ExpansionLayout expansionLayout;
    ImageView ivBackMain;
    ImageView ivAlertMain;
    ImageView ivAlertMainMain;
    private static final String strCatType = "1";
    Dialog dialogCurrency, dialogLanguage;
    RecyclerView rcvLanguage, rcvCurrency;
    LinearLayout llStaticLanguage, llStaticCurrency;
    CurrencyAdapter adapterCurrency;
    LanguageAdapter adapterLanguage;
    List<VehicleCategoryData> getve;
    DatabaseReference requestAcceptrDatabaseBRef;
    ValueEventListener requestAccepValueEventListener;
    private AddressResultReceiver mResultReceiver;
    private String mAddressOutput;
    private static GoogleMap mMap;
    //To check whether user scrolled the recycler view or used arrows to navigate.
    private boolean programaticallyScrolled;
    private static LatLng tmpPickupLatLng = new LatLng(0.0, 0.0)/*,tmpDropLatLng = new LatLng(0.0, 0.0)*/;
    private static LatLng tmpDropLatLng;
    private List<AppSettingLanguageModel> ListModel;
    private List<AppSettingCurrencyModel> ListModelCurrency;
    private static Boolean booleanPickupLatLng = false;

    String EstDistLabel = "0 km";
    String EstDurLabel = "0";
    String EstDist = "0.0";
    String EstDur = "0";

    String strDistanceUnit;
    Integer getCustomerManageDocument = 0;
    Boolean aBoolean = true;
    Boolean bBoolean = false;
    APIInterface apiInterface;

    /*Image Slider Start*/
    private ViewPager viewPager;
    private LinearLayout layout_dots;
    private AdapterImageSlider adapterImageSlider;
    private Runnable runnable = null;
    private final Handler handler = new Handler();
    private View viewImageSlider;
    private List<BannerData> bannerData = new ArrayList<>();
    private RelativeLayout rlEmptyviewAnnouncement;
    /*Image Slider End*/
    private Integer bookGuardMinTime;
    public static ArrayList<String> uniqueIdList;


    /*Geocoder and Fusedlocation Start*/
    private final BroadcastReceiver mLocationUpdated = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Location location = (Location) intent.getParcelableExtra(Utils.LBM_EVENT_LOCATION_UPDATE);

            callLocationOnCommon(location);

        }
    };

    private final BroadcastReceiver mGpsSwitchStateReceiver = new BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Objects.requireNonNull(intent.getAction()).matches("android.location.PROVIDERS_CHANGED")) {
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

    public static Boolean checkDrawerOpen() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            return true;
        else
            return false;
    }

    public static void openDrawerMethod() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void closeDrawerMethod() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }


    public static void searchCurrencyPrice(String Currency) {

        if (driverSearchDatabaseBRef != null) {
            driverSearchDatabaseBRef.removeEventListener(driverSearchValueEventListener);
        }
        driverSearchDatabaseBRef = FirebaseDatabase.getInstance().getReference().child("currency").child(Currency);
        driverSearchValueEventListener = driverSearchDatabaseBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    Object currencyPriceObj = dataSnapshot.getValue();
                    if (currencyPriceObj != null) {
                        Log.i(TAG, "onDataChange: in search currency-->" + currencyPriceObj.toString());
                        ;
                        getStateCurrency.putString(Utils.SP_CURRENCY_COMMON_VALUES, currencyPriceObj.toString());
                        getStateCurrency.apply();
                    } else {
                        getStateCurrency.putString(Utils.SP_CURRENCY_COMMON_VALUES, "1");
                        getStateCurrency.apply();
                    }

                } else {
                    getStateCurrency.putString(Utils.SP_CURRENCY_COMMON_VALUES, "1");
                    getStateCurrency.apply();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG, "onCancelled: error occur " + databaseError);
                getStateCurrency.putString(Utils.SP_CURRENCY_COMMON_VALUES, "1");
                getStateCurrency.apply();
            }
        });
    }

    @SuppressLint({"CommitPrefEdits", "SetTextI18n", "LongLogTag"})
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

            setContentView(R.layout.activity_main);

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            callSharedPreferences();

            EditSettingsFragment.successResponseEditProfile = this;

            //checkExistsUser();

            initViews();

            callLanguageCurrency();

            /*Geocoder and Fusedlocation Start*/
            mResultReceiver = new AddressResultReceiver(new Handler());

            updateValuesFromBundle(savedInstanceState);

            showGpsDailog();

            initGoogleAPIClient();//Init Google API Client

            checkPermissions();//Check Permission

            apiInterface = APIClient.getClient().create(APIInterface.class);

            registerReceiver(mGpsSwitchStateReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));   //Register location button receiver
            /*Geocoder and Fusedlocation End*/

            if (isGpsEnable()) {
                callLocationListener();
                fusedLocationCalled();
            } else {
                Toast.makeText(MainActivity.this, R.string.gps_disable, Toast.LENGTH_SHORT).show();
            }

            if (BaseActivity.isOnline(MainActivity.this)) {
                try
                {
                    CustomKeysAndValues keysAndValues = new CustomKeysAndValues.Builder().putString("user", "user "+userId).build();
                    FirebaseCrashlytics.getInstance().setCustomKeys(keysAndValues);
                }
                catch (Exception e)
                {
                    Log.e(TAG, "onCreate: exception-->" + e.toString());
                }

                callAppSettings(); //API

                getCustomerWalletAmount(); //Firebase

                 callSlideBannerImage("all"); // API

                getOnlineCategoryInfo(); //API

                getOnGoingJobsCall(); //API

                getrequestAcceptLisetner(); //Firebase

            } else {
                txtRefresh.setVisibility(View.VISIBLE);
                rlParentLytProgress.setVisibility(View.VISIBLE);

                txtRefreshMain.setVisibility(View.VISIBLE);
                txtLoadingMain.setVisibility(View.VISIBLE);
                progressBarMain.setVisibility(View.VISIBLE);

                Toast.makeText(MainActivity.this, R.string.internet_is_slow_disconnect, Toast.LENGTH_SHORT).show();
            }

            onClick();

            //Font in Menu Items.
            applyFontInMenuItems();

            Intent i = getIntent();
            if (i.getExtras() != null) {
                String Notify = i.getStringExtra("Notify");
                if (Notify != null) {
                    if (Notify.equals("Offers")) {
                        Bundle args = new Bundle();
                        args.putString("via", "Offers");
                        Utils.switchContent(R.id.frame_mainactivity,
                                Utils.FRAGMENT_YOUR_OFFERS_ANNOCEMENTS,
                                MainActivity.this, args,
                                Utils.AnimationType.SLIDE_IN_SLIDE_OUT);
                    } else if (Notify.equals("Announcements")) {
                        Bundle args = new Bundle();
                        args.putString("via", "Announcements");
                        Utils.switchContent(R.id.frame_mainactivity,
                                Utils.FRAGMENT_YOUR_OFFERS_ANNOCEMENTS,
                                MainActivity.this, args,
                                Utils.AnimationType.SLIDE_IN_SLIDE_OUT);
                    } else if (Notify.equals("Reminder")) {
                        Utils.switchContent(R.id.frame_mainactivity,
                                Utils.FRAGMENT_MY_CALENDER_BOOKING,
                                MainActivity.this, null,
                                Utils.AnimationType.SLIDE_IN_SLIDE_OUT);
                    }
                }
            } else {
                if (BuildConfig.DEBUG) Log.d(TAG, "Exception");
            }


            PackageManager manager = MainActivity.this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(
                    MainActivity.this.getPackageName(), 0);
            String version = info.versionName;
            //Log.e("userid ~~ version ~~", version);
            txtVersion.setText("v" + version);

            if (BuildConfig.DEBUG)
                Log.e("userid ~~ Main ~~", userId);

        } catch (Exception e) {
            Log.e(TAG, "onCreate: exception-->" + e.toString());
        }
    }


    @SuppressLint("SetTextI18n")
    private void initViews() {

        uniqueIdList = new ArrayList<>();
        shPref = getSharedPreferences("shared preferences", MODE_PRIVATE);
        editor = shPref.edit();

        frameMap = (FrameLayout) findViewById(R.id.frame_mainactivity);

        lytJobInprogress = (View) findViewById(R.id.lyt_job_inprogress);
        lytSMSalert = (View) findViewById(R.id.lyt_sms_alert);

        /*Navigation Drawer intialize*/
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        navigationView = (NavigationView) findViewById(R.id.navigationview);
        try {
            navigationView.setItemIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        View header = navigationView.getHeaderView(0);
        navigationName = (CustomTextView) header.findViewById(R.id.txt_nav_user_name);
        navigationWalletAmount = (CustomTextView) header.findViewById(R.id.txt_nav_walletbalance);
        navRbuserrating = (SimpleRatingBar) header.findViewById(R.id.rb_nav_user_rating);
        navigationCustomerId = (CustomTextView) header.findViewById(R.id.txt_nav_user_id);
        navigationProfileImage = (SimpleDraweeView) header.findViewById(R.id.img_nav_profileimage);
        navigationSetting = (ImageView) header.findViewById(R.id.img_nav_setting);
        navigationSubsPlan = header.findViewById(R.id.txt_subscribed_user);

        navigationView.setNavigationItemSelectedListener(this);
        navigationLogoutUser = (RelativeLayout) findViewById(R.id.rl_signout_customer);
        txtVersion = (CustomTextView) findViewById(R.id.txt_version);

        txtSmsAlertContent = (CustomTextView) findViewById(R.id.txt_sms);
        btnSmsAlert = (CustomButton) findViewById(R.id.btn_ok_sms);

        btnNext = (CustomButton) findViewById(R.id.btn_next);
        btnDone = (CustomButton) findViewById(R.id.btn_done);
        cvDropLyt = (CardView) findViewById(R.id.cv_drop_layout);
        rlDropLytParent = (RelativeLayout) findViewById(R.id.rl_drop_layout);

        rlCenterMarker = (RelativeLayout) findViewById(R.id.rl_centermarker);
        rlCurrentLocView = (RelativeLayout) findViewById(R.id.rl_currentLoction);

        lytMapMainPage = findViewById(R.id.lyt_map_main);
        expansionLayout = findViewById(R.id.expansionLayout_main);
        ivBackMain = findViewById(R.id.img_menu);
        ivAlertMain = (ImageView) findViewById(R.id.iv_alert_main);
        ivAlertMainMain = (ImageView) findViewById(R.id.iv_alert_main_main);

        txtUpdateCurrencySetup = (CustomTextView) findViewById(R.id.update_currency_setup);
        txtUpdateLangSetup = (CustomTextView) findViewById(R.id.update_language_setup);
        txtOnGoingJobsLabel = (CustomTextView) findViewById(R.id.update_ongoing_setup);
        txtSubscriptionPlanSetupLabel = (CustomTextView) findViewById(R.id.update_subscription_setup);

        imgCenterMarker = (ImageView) findViewById(R.id.img_centermarker);
        rlPickupLytParent = (RelativeLayout) findViewById(R.id.rl_pickup_layout);

        txtPickupLocation = (CustomTextView) findViewById(R.id.txt_pickup_location);
        txtDropLocation = (CustomTextView) findViewById(R.id.txt_drop_location);
        addMarqueeTextfield(txtPickupLocation);
        addMarqueeTextfield(txtDropLocation);

        edtNoOfGuards = findViewById(R.id.ed_countt);
        edtHrs = findViewById(R.id.ed_hrs);
        edtTellUsEmergency = findViewById(R.id.ed_tell_us_emergency);

        //Schedule trip
        lytScheduleTripConfirmView = findViewById(R.id.lyt_schedule_date_time_selection);
        imgbtnBackScheduleTrip = findViewById(R.id.imgbtn_back_scheduletrip);
        rlPickupDate = findViewById(R.id.rl_pickup_date);
        txt_book_a_guard = findViewById(R.id.txt_book_a_guard);
        rlPickupTime = findViewById(R.id.rl_pickup_time);
        txtPickupDate = findViewById(R.id.txt_pickup_date);
        txtPickupTime = findViewById(R.id.txt_pickup_time);

        rlPickupDate_End = findViewById(R.id.rl_pickup_date_end);
        rlPickupTime_End = findViewById(R.id.rl_pickup_time_end);
        txtPickupDate_End = findViewById(R.id.txt_pickup_date_end);
        txtPickupTime_End = findViewById(R.id.txt_pickup_time_end);
        btnContinueScheduleTrip = findViewById(R.id.btn_continue_scheduletrip);

        layout_dots = findViewById(R.id.layout_dots);
        viewPager = findViewById(R.id.pager);
        viewPager.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        rlEmptyviewAnnouncement = findViewById(R.id.rl_emptyview_announcement);
        viewImageSlider = findViewById(R.id.view_image_slider);
        adapterImageSlider = new AdapterImageSlider(MainActivity.this, new ArrayList<Image>());

        //Schedule trip

        lytBookNowBookLater = findViewById(R.id.ll_btn_ll);
        btnBookNow = findViewById(R.id.btn_booknow);
        btnBookLater = findViewById(R.id.btn_booklater);

        leftArrow = findViewById(R.id.tv_left_arrow);
        rightArrow = findViewById(R.id.tv_right_arrow);

        rlParentLytProgress = findViewById(R.id.rl_pb_main_category);
        txtNoRecords = findViewById(R.id.txt_no_records);
        txtRefresh = findViewById(R.id.txt_refresh);

        rlParentLytMapMain = findViewById(R.id.rl_parent_lyt_map_main);
        progressBarMain = findViewById(R.id.pb_main_category_main);
        txtNoRecordsMain = findViewById(R.id.txt_no_records_main);
        txtLoadingMain = findViewById(R.id.txt_loading_main);
        txtRefreshMain = findViewById(R.id.txt_refresh_main);
        uniqueBtn = findViewById(R.id.uniqueBtn);

        if (userId != null) {
            navigationCustomerId.setText(getResources().getString(R.string.id_) + " :- " + userId);
        } else {
            navigationCustomerId.setVisibility(View.GONE);
            //   navigationCustomerId.setText(getString(R.string.customer_id));
        }
        strCurrencySymbol = spCurrency.getString(Utils.SP_CURRENCY_SYMBOL, null);

        if (strUserfname != null) {
            String upperString = strUserfname.substring(0, 1).toUpperCase() + strUserfname.substring(1);
            navigationName.setText(upperString);
        } else {
            navigationName.setText(getResources().getString(R.string.app_name));
        }

        if (strUserProPic != null) {
            navigationProfileImage.setImageURI(Utils.BASE_URL + strUserProPic);
            int color = getResources().getColor(R.color.colorPrimary);
            RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
            roundingParams.setBorder(color, 1.0f);
            roundingParams.setRoundAsCircle(true);
            navigationProfileImage.getHierarchy().setRoundingParams(roundingParams);
        }
        /*Navigation Drawer end*/

        //Requesting Accept Dialog
        dailogAcceptDriver = findViewById(R.id.lyt_dialog_acceptprovider);

        txtViewOnGoingJobs = (CustomTextView) findViewById(R.id.txt_viewongoingjobs);
        rlParentLytAcceptDriver = (RelativeLayout) findViewById(R.id.reqRelView);
        txtCancel = (CustomTextView) findViewById(R.id.txt_cancel);
        txtViewOnGoingJobs1 = (CustomTextView) findViewById(R.id.txt_viewongoingjobs1);
        txtCancel1 = (CustomTextView) findViewById(R.id.txt_cancel1);
        txtHeaderDialogAcceptDriver = (CustomTextView) findViewById(R.id.txt_header);
        txtHeaderDialogDeclineDriver = (CustomTextView) findViewById(R.id.txt_header1);
        rlSuccess = (RelativeLayout) findViewById(R.id.success);
        rlSuccess1 = (RelativeLayout) findViewById(R.id.success1);
        txtDescriptionDialogAcceptDriver = (CustomTextView) findViewById(R.id.txt_description);
        txtDescriptionDialogDeclineDriver = (CustomTextView) findViewById(R.id.txt_description1);

        rlParentLytAcceptDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        rlParentLytMapMain.setOnClickListener(view -> {

        });

        //Requesting Accept Dialog
        categoryModelArray = new ArrayList<>();
        categoryModelArraySub = new ArrayList<>();
        mapCategoryAdapter = new MapCategoryAdapter(categoryModelArray, MainActivity.this);
        mapCategorySubAdapter = new MapCategorySubAdapter(categoryModelArraySub, MainActivity.this, "0", "0");

        rvCategory = findViewById(R.id.rv_main_category);
        rvSubCategory = findViewById(R.id.rv_sub_category);
        SwipeRefreshLayout swipeRefreshMain = findViewById(R.id.swipe_refresh_layout_main);
        swipeRefreshMain.setOnRefreshListener(() -> {
            swipeRefreshMain.setRefreshing(false);
            getOnlineCategoryInfo();
        });

        //linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        //linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        linearLayoutManagerSub = new LinearLayoutManager(getApplicationContext());
        linearLayoutManagerSub.setOrientation(LinearLayoutManager.HORIZONTAL);

        rvCategory.setLayoutManager(linearLayoutManager);
        rvCategory.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        rvCategory.setAdapter(mapCategoryAdapter);

        /*ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplicationContext(), R.dimen.recyclerview_item_offset);
        rvCategory.addItemDecoration(itemDecoration);
        SnapHelper snapHelper = new StartSnapHelper();
        snapHelper.attachToRecyclerView(rvCategory);*/

        ItemOffsetDecoration itemDecorations = new ItemOffsetDecoration(getApplicationContext(), R.dimen.recyclerview_item_offset);
        rvSubCategory.setLayoutManager(linearLayoutManagerSub);
        rvSubCategory.setAdapter(mapCategorySubAdapter);
        rvSubCategory.addItemDecoration(itemDecorations);
        SnapHelper snapHelpers = new StartSnapHelper();
        snapHelpers.attachToRecyclerView(rvSubCategory);

        leftArrow.setOnClickListener(v -> {
            if (linearLayoutManagerSub.findFirstVisibleItemPosition() > 0) {
                rvSubCategory.smoothScrollToPosition(linearLayoutManagerSub.findFirstVisibleItemPosition() - 1);
            } else {
                rvSubCategory.smoothScrollToPosition(0);
            }
        });
        rightArrow.setOnClickListener(v -> rvSubCategory.smoothScrollToPosition(linearLayoutManagerSub.findLastVisibleItemPosition() + 1));


        rvCategory.addOnItemTouchListener(
                new RecyclerTouchListener(MainActivity.this, rvCategory,
                        new RecyclerTouchListener.OnTouchActionListener() {
                            @SuppressLint("LongLogTag")
                            @Override
                            public void onClick(View view, int position) {
                                if (!txtPickupLocation.getText().toString().equals(getString(R.string.service_not_available)) ||
                                        !txtPickupLocation.getText().toString().equals(getString(R.string.no_address_found)) ||
                                        !txtPickupLocation.getText().toString().equals(getString(R.string.mainacativity_getting_location))) {
                                    Log.i("rvCategory", "onClick: rvCategory-> ");

                                    if (position != -1) {
                                        //beginMoveCamera = true;
                                        //setProgresscount(position);
                                        listFeed = categoryModelArray.get(position);
                                        Log.e("Category_getCategoryName~~", listFeed.getCategoryName());
                                        Log.e("Category_getId~~", listFeed.getCategoryId());
                                        Log.e("getcatType~~", listFeed.getcatType());
                                        mapCategoryAdapter.selected(position);
                                        //pickupLocationShown = true;
                                        imgCenterMarker.setImageResource(R.mipmap.ic_green_pin);
                                        getRequestState.putString(Utils.B_CATEGORY_ID, listFeed.getCategoryId());
                                        getRequestState.putString(Utils.B_CATEGORY_NAME, listFeed.getCategoryEnName());
                                        getRequestState.putString(Utils.B_CATEGORY_NAME_AR, listFeed.getCategoryArName());
                                        getRequestState.putString(Utils.B_CATEGORY_LEVEL_TYPE, listFeed.getLevelType());
                                        getRequestState.putString(Utils.B_CATEGORY_LEVEL_AMOUNT, listFeed.getLevelAmount());
                                        getRequestState.apply();

                                        setVisibility(lytMapMainPage, false);

                                        getOnlineSubCategoryInfo(String.valueOf(listFeed.getCategoryId()));

                                        if (markerPickupConfirmBooking != null)
                                            markerPickupConfirmBooking.remove();
                                        if (markerDropConfirmBooking != null)
                                            markerDropConfirmBooking.remove();

                                        if (directionPolyline != null) {
                                            Log.i(TAG, "onDataChange: Starttrip directionPolyline if ");
                                            directionPolyline.remove();
                                        }

                                        booleanPickupLatLng = false;

                                        currentLocationClick(view);
                                        rlCenterMarker.setVisibility(View.GONE);

                                        /*if (listFeed.getcatType().equals("1")) {
                                            cvDropLyt.setVisibility(View.VISIBLE);
                                            txtDropLocation.setText(getResources().getString(R.string.map_enter_drop_location));
                                            strCatType = "next";
                                            cameraIdleValue = "1";
                                            currentLocationClick(view);
                                            callNext(listFeed.getCategoryId(), listFeed.getCategoryName(), listFeed.getcatType());
                                        } else {
                                            strCatType = "done";
                                            cameraIdleValue = "1";
                                            currentLocationClick(view);
                                            rlCenterMarker.setVisibility(View.VISIBLE);
                                            cvDropLyt.setVisibility(View.GONE);
                                            btnNext.setVisibility(View.GONE);
                                            callDone("NoDrop", listFeed.getCategoryId(), listFeed.getCategoryName(), listFeed.getcatType());
                                        }*/
                                    }
                                } else {
                                    Toast.makeText(MainActivity.this, R.string.toast_pls_check_location, Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onRightSwipe(View view, int position) {

                            }

                            @Override
                            public void onLeftSwipe(View view, int position) {
                            }
                        }));

        rvSubCategory.addOnItemTouchListener(
                new RecyclerTouchListener(MainActivity.this, rvSubCategory,
                        new RecyclerTouchListener.OnTouchActionListener() {
                            @SuppressLint("LongLogTag")
                            @Override
                            public void onClick(View view, int position) {
                                if (!txtPickupLocation.getText().toString().equals(getString(R.string.service_not_available)) ||
                                        !txtPickupLocation.getText().toString().equals(getString(R.string.no_address_found)) ||
                                        !txtPickupLocation.getText().toString().equals(getString(R.string.mainacativity_getting_location))) {
                                    Log.i("rvCategory", "onClick: rvCategory-> ");

                                    if (position != -1) {


                                        listFeedSub = categoryModelArraySub.get(position);
                                        getSubcatName = listFeedSub.getSubCategoryName();
                                        getSubcatId = listFeedSub.getSubCategoryId();
                                        getSubcatMainId = listFeedSub.getSubMainCategoryId();
                                        getSubcatPrice = listFeedSub.getSubCategoryBasefare();

                                        getSubcatAgentPrice = listFeedSub.getSubCategoryAgentPrice().toString();
                                        getSubcatDayPrice = listFeedSub.getSubCategoryDayPrice().toString();

                                        Log.e("Category_getCategoryName~~", getSubcatName);
                                        Log.e("Category_getId~~", getSubcatId);
                                        Log.e("Category_getMainId~~", getSubcatMainId);
                                        Log.e("Category_getSubcatAgentPrice~~", getSubcatAgentPrice.toString());
                                        Log.e("Category_getSubcatDayPrice~~", getSubcatDayPrice.toString());

                                        mapCategorySubAdapter.selected(position);

                                        getRequestState.putString(Utils.B_SUB_CATEGORY_ID, listFeedSub.getSubCategoryId());
                                        getRequestState.putString(Utils.B_SUB_CATEGORY_NAME, listFeedSub.getSubCategoryEnName());
                                        getRequestState.putString(Utils.B_SUB_CATEGORY_NAME_AR, listFeedSub.getSubCategoryArName());
                                        getRequestState.putString(Utils.B_SUB_CATEGORY_PRICE, listFeedSub.getSubCategoryBasefare());
                                        getRequestState.putString(Utils.B_CATEGORY_AGENT_PRICE, getSubcatAgentPrice.toString());
                                        getRequestState.putString(Utils.B_CATEGORY_DAYS_PRICE, getSubcatDayPrice.toString());
                                        // getRequestState.putString(Utils.B_SUB_CATEGORY_PRICE,listFeed.getSubCategoryPricePerKm());
                                        getRequestState.apply();

                                        if (markerPickupConfirmBooking != null)
                                            markerPickupConfirmBooking.remove();
                                        if (markerDropConfirmBooking != null)
                                            markerDropConfirmBooking.remove();

                                        if (directionPolyline != null) {
                                            Log.i(TAG, "onDataChange: Starttrip directionPolyline if ");
                                            directionPolyline.remove();
                                        }

                                        booleanPickupLatLng = false;
                                    }
                                } else {
                                    Toast.makeText(MainActivity.this, R.string.toast_pls_check_location, Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onRightSwipe(View view, int position) {

                            }

                            @Override
                            public void onLeftSwipe(View view, int position) {
                            }
                        }));

    }

    private void callLanguageCurrency() {
        /*Dialog Currency charge*/
        dialogLanguage = new Dialog(MainActivity.this);
        dialogLanguage.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialogLanguage.setContentView(R.layout.dialog_static_language);
        Objects.requireNonNull(dialogLanguage.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogLanguage.setCancelable(true);
        dialogLanguage.getWindow().setWindowAnimations(R.style.DialogAnimationLR);

        WindowManager.LayoutParams lps = new WindowManager.LayoutParams();
        lps.copyFrom(dialogLanguage.getWindow().getAttributes());
        lps.width = WindowManager.LayoutParams.MATCH_PARENT;
        lps.height = WindowManager.LayoutParams.WRAP_CONTENT;

        rcvLanguage = (RecyclerView) dialogLanguage.findViewById(R.id.rcv_language);
        llStaticLanguage = (LinearLayout) dialogLanguage.findViewById(R.id.ll_static_language);

        rcvLanguage.addOnItemTouchListener(
                new RecyclerTouchListener(MainActivity.this, rcvLanguage,
                        new RecyclerTouchListener.OnTouchActionListener() {
                            @SuppressLint({"LongLogTag", "ObsoleteSdkInt"})
                            @Override
                            public void onClick(View view, int position) {
                                Log.i("rcvLanguage", "onClick: rcvLanguage-> ");
                                if (position != -1) {
                                  /*  listFeed  = emergencyModelArray.get(position);
                                    callDeleteEmergencyDetails(listFeed.getContact_id());*/
                                    String langId = String.valueOf(ListModel.get(position).getId());
                                    String someVarA = String.valueOf(ListModel.get(position).getLanguageName());
                                    String someVarB = String.valueOf(ListModel.get(position).getLanguageCode());
                                    Log.e("Language~~Onclick~~", langId + "~~" + someVarA);
                                    dialogLanguage.dismiss();

                                    CallUpdateDetails(langId, someVarA, someVarB, "langDialog");

                                }
                            }

                            @Override
                            public void onRightSwipe(View view, int position) {
                            }

                            @Override
                            public void onLeftSwipe(View view, int position) {
                            }
                        }));

        /*Dialog Currency charge*/
        dialogCurrency = new Dialog(MainActivity.this);
        dialogCurrency.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialogCurrency.setContentView(R.layout.dialog_static_currency);
        Objects.requireNonNull(dialogCurrency.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogCurrency.setCancelable(true);
        dialogCurrency.getWindow().setWindowAnimations(R.style.DialogAnimationLR);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogCurrency.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        rcvCurrency = (RecyclerView) dialogCurrency.findViewById(R.id.rcv_currency);
        llStaticCurrency = (LinearLayout) dialogCurrency.findViewById(R.id.ll_static_currency);

        rcvCurrency.addOnItemTouchListener(
                new RecyclerTouchListener(MainActivity.this, rcvCurrency,
                        new RecyclerTouchListener.OnTouchActionListener() {
                            @SuppressLint("LongLogTag")
                            @Override
                            public void onClick(View view, int position) {
                                Log.i("rvCategory", "onClick: rvCategory-> ");
                                if (position != -1) {

                                    String curId = String.valueOf(ListModelCurrency.get(position).getId());
                                    String curName = String.valueOf(ListModelCurrency.get(position).getCurrency());
                                    String curShortName = String.valueOf(ListModelCurrency.get(position).getShortName());
                                    String curSymbol = String.valueOf(ListModelCurrency.get(position).getSymbol());
                                    Log.e("Language~~Onclick~~", curId + "~~" + curName + "~" + curShortName + "~~" + curSymbol);
                                    dialogCurrency.dismiss();
                                    callCurrencyUpdate(curId, curShortName, "update");
                                }

                            }

                            @Override
                            public void onRightSwipe(View view, int position) {
                            }

                            @Override
                            public void onLeftSwipe(View view, int position) {
                            }
                        }));
    }

    @SuppressLint("CommitPrefEdits")
    private void callSharedPreferences() {

        getState = getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE).edit();
        getStateCurrency = getSharedPreferences(Utils.PREFS_NAME_CURRENCY, MODE_PRIVATE).edit();

        getRequestState = getSharedPreferences(Utils.MY_PREFS_REQUEST_STATE, MODE_PRIVATE).edit();

        spCommon = getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE);
        spCurrency = getSharedPreferences(Utils.PREFS_NAME_CURRENCY, MODE_PRIVATE);

        userId = spCommon.getString(Utils.SP_USERID, null);

        strLanguage = spCurrency.getString(Utils.SP_LANGUAGE, null);
        strLanguageShortName = spCurrency.getString(Utils.SP_LANGUAGE_SHORT_NAME, null);
        strCurrency = spCurrency.getString(Utils.SP_CURRENCY, null);
        strCurrencySymbol = spCurrency.getString(Utils.SP_CURRENCY_SYMBOL, null);

        if (strLanguageShortName == null) {
            strLanguageShortName = "en";
        }

        tokenExpTime = spCommon.getString(Utils.SP_USER_TOKENEXPTIME, null);
        userAccessToken = spCommon.getString(Utils.SP_USER_ACCESSTOKEN, null);
        strUserProPic = spCommon.getString(Utils.SP_USER_PRO_PIC, null);
        strUserfname = spCommon.getString(Utils.SP_USER_FNAME, null);
        strUserlname = spCommon.getString(Utils.SP_USER_LNAME, null);
        strWalletAmt = spCommon.getString(Utils.SP_USER_WALLET_AMT, null);

        currencyCommonPrice = spCurrency.getString(Utils.SP_CURRENCY_COMMON_VALUES, "1");
        Log.i(TAG, "convertCurrencyPrice: Main -->" + currencyCommonPrice);

        strCurrencySymbol = spCurrency.getString(Utils.SP_CURRENCY_SYMBOL, null);
        strCurrency = spCurrency.getString(Utils.SP_CURRENCY, null);
        searchCurrencyPrice(strCurrency);

        // getCurrencyList = loadSharedPreferencesLogList(MainActivity.this);
    }

    private void applyFontInMenuItems() {
        Menu m = navigationView.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }
            //the method we have create in activity
            applyFontToMenuItem(mi);
        }
    }

    private void applyFontToMenuItem(MenuItem mi) {

        Typeface font = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.App_Font));
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    private boolean checkStates(CustomTextView view) {
        if (center_point_Idle.longitude == 0.0) {
            if (center_point_Idle.latitude == 0.0) {
                return false;
            }
        }

        if (view.getText().toString().equals(getString(R.string.service_not_available))) {
            return false;
        } else if (view.getText().toString().equals(getString(R.string.no_address_found))) {
            return false;
        } else if (view.getText().toString().equals(getString(R.string.mainacativity_getting_location))) {
            return false;
        } else
            return !view.getText().toString().equals(getString(R.string.map_enter_drop_location));
    }

    /*Navigation Drawer end*/

    private void callNext(String categoryId, String categoryName, String categoryType) {

        btnNext.setVisibility(View.VISIBLE);
        btnDone.setVisibility(View.GONE);
        btnNext.setOnClickListener(view -> {

            if (txtUpdateCurrencySetup.getVisibility() == View.VISIBLE) {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.update_currency_setup), Toast.LENGTH_SHORT).show();
            } else {
                if (checkStates(txtPickupLocation)) {

                    if (checkStates(txtDropLocation)) {

                        Log.i("rvCategory", "onClick: rvCategory-> ");
                        if (markerPickupConfirmBooking != null)
                            markerPickupConfirmBooking.remove();
                        if (markerDropConfirmBooking != null)
                            markerDropConfirmBooking.remove();

                        if (markerCurrentLocationMan != null) {
                            markerCurrentLocationMan.remove();
                        }

                        if (rlCenterMarker.getVisibility() == View.VISIBLE) {
                            rlCenterMarker.setVisibility(View.GONE);
                        }

                        Log.e(TAG, "btn next onClick:----->>>>>>>>>lat " + center_point_Idle.latitude + "lng" + center_point_Idle.longitude);
                        tmpPickupLatLng = new LatLng(center_point_Idle.latitude, center_point_Idle.longitude);

                        Log.e(TAG, "onClick:----->>>>>>>>> " + CalculationByDistance(tmpPickupLatLng, tmpDropLatLng));

                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        markerPickupConfirmBooking = mMap.addMarker(new MarkerOptions().position(tmpPickupLatLng).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_green_pin)));
                        markerDropConfirmBooking = mMap.addMarker(new MarkerOptions().position(tmpDropLatLng).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_red_pin)));
                        builder.include(markerPickupConfirmBooking.getPosition());
                        builder.include(markerDropConfirmBooking.getPosition());
                        LatLngBounds bounds1 = builder.build();

                        //  CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds1, 10);
                        cameraIdleValue = "2";
                        try {
                            if (CalculationByDistance(tmpPickupLatLng, tmpDropLatLng) <= 2) {
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
                                // mMap.setPadding(75, 5, 75, 5);
                            } else {
                                int padding = 90; // offset from edges of the map in pixels
                                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds1, padding);
                                //googleMap.setPadding(left, top, right, bottom)
                                mMap.setPadding(65, 5, 65, 5);
                                mMap.animateCamera(cu);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        if (directionPolyline != null) {
                            directionPolyline.remove();
                        }

                        String unit;
                        if (strDistanceUnit != null && !strDistanceUnit.equals("null") && !strDistanceUnit.equals("")
                                && !strDistanceUnit.equals(" ")) {
                            if (strDistanceUnit.equals("km")) {
                                unit = String.valueOf(Unit.METRIC);
                            } else {
                                unit = String.valueOf(Unit.IMPERIAL);
                            }
                        } else {
                            unit = String.valueOf(Unit.METRIC);
                        }

                        GoogleDirection.withServerKey(BaseActivity.getGoogleApiKey(MainActivity.this))
                                .from(tmpPickupLatLng)
                                .to(tmpDropLatLng)
                                .unit(unit)
                                .avoid(AvoidType.FERRIES)
                                .avoid(AvoidType.INDOOR)
                                .transportMode(TransportMode.DRIVING)
                                .transitMode(TransitMode.BUS)
                                .alternativeRoute(true)
                                .execute(new DirectionCallback() {
                                    @Override
                                    public void onDirectionSuccess(Direction direction, String rawBody) {

                                        System.out.println("direction ===>>>" + direction.getStatus());

                                        if (direction.getStatus().equals("OK")) {

                                            if (direction.getRouteList().size() > 0) {

                                                Route route = direction.getRouteList().get(0);
                                                ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
                                                directionPolyline = mMap.addPolyline(DirectionConverter.createPolyline(MainActivity.this,
                                                        directionPositionList, 2, Utils.POLYLINE_COLOR_ACCEPTTRIP));

                                                List<Leg> leg = direction.getRouteList().get(0).getLegList();
                                                List<Step> step = leg.get(0).getStepList();

                                                if (BuildConfig.DEBUG) {

                                                   /* Log.e(TAG, "onDirectionSuccess: ~~~~~getDistance~~~~~~~step~~~~~" + step.get(0).getDistance().getText());
                                                    Log.e(TAG, "onDirectionSuccess: ~~~~~getDistance~~~~~~~step~~~~~" + step.get(0).getDistance().getValue());
                                                    Log.e(TAG, "onDirectionSuccess: ~~~~~~~~~getDuration~~~step~~~~~" + step.get(0).getDuration().getText());
                                                    Log.e(TAG, "onDirectionSuccess: ~~~~~~~~~getDuration~~~step~~~~" + step.get(0).getDuration().getValue());*/

                                                    Log.e(TAG, "onDirectionSuccess: ~~getDuration~~~" + route.getLegList().get(0).getDuration().getValue());
                                                    Log.e(TAG, "onDirectionSuccess: ~~getDistance~~~" + route.getLegList().get(0).getDistance().getValue());
                                                    Log.e(TAG, "onDirectionSuccess: ~~getDuration~~~" + route.getLegList().get(0).getDuration().getText());
                                                    Log.e(TAG, "onDirectionSuccess: ~~getDistance~~~" + route.getLegList().get(0).getDistance().getText());
                                                    /*
                                                     *  onDirectionSuccess: ~~getDuration~~~722
                                                     *  onDirectionSuccess: ~~getDistance~~~4378
                                                     *  onDirectionSuccess: ~~getDuration~~~12 mins
                                                     *  onDirectionSuccess: ~~getDistance~~~4.4 km
                                                     * */
                                                }

                                                EstDistLabel = route.getLegList().get(0).getDistance().getText();
                                                EstDurLabel = route.getLegList().get(0).getDuration().getText();

                                                String EstDist_ = route.getLegList().get(0).getDistance().getValue();
                                                String EstDur_ = route.getLegList().get(0).getDuration().getValue();

                                                double dists = Double.parseDouble(EstDist_) / 1000;
                                                double durs = Double.parseDouble(EstDur_) / 60;

                                                double roundedNumberDist = DecimalUtils.round(dists, 2);
                                                double roundedNumberDur = DecimalUtils.round(durs, 2);

                                                double roundedNumberDistMath = Math.round(roundedNumberDist);
                                                double roundedNumberDurMath = Math.round(roundedNumberDur);

                                                if (BuildConfig.DEBUG) {

                                                    Log.e(TAG, "onResult: ~~~~~~~~EstDist_~~~~" + EstDist_);
                                                    Log.e(TAG, "onResult: ~~~~~~~~dists~~~~" + dists);
                                                    Log.e(TAG, "onResult: ~~~~~~~~roundedNumberDist~~~~" + roundedNumberDist);
                                                    Log.e(TAG, "onResult: ~~~~~~~~roundedNumberDistMath~~~~" + roundedNumberDistMath);

                                                    Log.e(TAG, "onResult: ~~~~~~~~EstDur_~~~~" + EstDur_);
                                                    Log.e(TAG, "onResult: ~~~~~~~~durs~~~~" + durs);
                                                    Log.e(TAG, "onResult: ~~~~~~~~roundedNumberDur~~~~" + roundedNumberDur);
                                                    Log.e(TAG, "onResult: ~~~~~~~~roundedNumberDurMath~~~~" + roundedNumberDurMath);
                                                }

                                                    /*if (BaseActivity.convertDistanceUnit(MainActivity.this).equals("km")) {
                                                    EstDist = String.valueOf(roundedNumberDistMath);
                                                    } else {
                                                    EstDist = String.valueOf(convertKmsToMiles(roundedNumberDistMath));
                                                    }
                                                    onResult: ~~~~~~~~EstDist_~~~~4517
                                                    onResult: ~~~~~~~~dists~~~~4.517
                                                    onResult: ~~~~~~~~roundedNumberDist~~~~4.52
                                                    onResult: ~~~~~~~~roundedNumberDistMath~~~~5.0
                                                    */

                                                if (BaseActivity.convertDistanceUnit(MainActivity.this).equals("km")) {
                                                    EstDist = String.valueOf(dists);
                                                    EstDur = String.valueOf(durs);
                                                } else {
                                                    EstDist = String.valueOf(convertKmsToMiles(dists));
                                                    EstDur = String.valueOf(durs);
                                                }

                                            } else {

                                                if (BaseActivity.convertDistanceUnit(MainActivity.this).equals("km")) {
                                                    EstDistLabel = "0 km";
                                                    EstDurLabel = "0";
                                                } else {
                                                    EstDistLabel = "0 mile";
                                                    EstDurLabel = "0";
                                                }

                                                EstDist = "0.0";
                                                EstDur = "0";

                                                System.out.println("direction size===>>>" + direction.getRouteList());
                                            }

                                        } else {

                                            if (BaseActivity.convertDistanceUnit(MainActivity.this).equals("km")) {
                                                EstDistLabel = "0 km";
                                                EstDurLabel = "0";
                                            } else {
                                                EstDistLabel = "0 mile";
                                                EstDurLabel = "0";
                                            }

                                            EstDist = "0.0";
                                            EstDur = "0";
                                        }

                                        getRequestState.putString(Utils.B_DISTANCE, EstDist);
                                        getRequestState.putString(Utils.B_DISTANCE_LABEL, EstDistLabel);
                                        getRequestState.putString(Utils.B_DURATION, EstDur);
                                        getRequestState.putString(Utils.B_DURATION_LABEL, EstDurLabel);
                                        getRequestState.apply();

                                    }

                                    @Override
                                    public void onDirectionFailure(Throwable t) {
                                        Log.e(TAG, "onDirectionFailure: ~~~~" + t.toString());
                                        if (BaseActivity.convertDistanceUnit(MainActivity.this).equals("km")) {
                                            EstDistLabel = "0 km";
                                            EstDurLabel = "0";
                                        } else {
                                            EstDistLabel = "0 mile";
                                            EstDurLabel = "0";
                                        }
                                        EstDist = "0.0";
                                        EstDur = "0";


                                        getRequestState.putString(Utils.B_DISTANCE, EstDist);
                                        getRequestState.putString(Utils.B_DISTANCE_LABEL, EstDistLabel);
                                        getRequestState.putString(Utils.B_DURATION, EstDur);
                                        getRequestState.putString(Utils.B_DURATION_LABEL, EstDurLabel);
                                        getRequestState.apply();
                                    }
                                });

                        getRequestState.putString(Utils.B_DISTANCE, EstDist);
                        getRequestState.putString(Utils.B_DISTANCE_LABEL, EstDistLabel);
                        getRequestState.putString(Utils.B_DURATION, EstDur);
                        getRequestState.putString(Utils.B_DURATION_LABEL, EstDurLabel);
                        getRequestState.apply();


                        callDone("Drop", categoryId, categoryName, categoryType);
                        btnNext.setVisibility(View.GONE);

                    } else {
                        Toast.makeText(MainActivity.this, R.string.map_enter_drop_location, Toast.LENGTH_SHORT).show();
                    }

               /* if (!txtDropLocation.getText().toString().equals(getString(R.string.service_not_available)) ||
                        !txtDropLocation.getText().toString().equals(getString(R.string.no_address_found))||
                        !txtDropLocation.getText().toString().equalsIgnoreCase(getResources().getString(R.string.map_enter_drop_location)))  else {
                        Toast.makeText(MainActivity.this, R.string.map_enter_drop_location, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, R.string.toast_pls_check_location, Toast.LENGTH_SHORT).show();
                }*/

                } else {
                    Toast.makeText(MainActivity.this, R.string.toast_pls_check_location, Toast.LENGTH_SHORT).show();
                    currentLocationClick(view);
                    mMap.setOnCameraIdleListener(MainActivity.this);
                }
            }
        });

    }

    private void callDone(String key, String categoryId, String categoryName, String categoryType) {

        btnDone.setVisibility(View.VISIBLE);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (txtUpdateCurrencySetup.getVisibility() == View.VISIBLE) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.update_currency_setup), Toast.LENGTH_SHORT).show();
                } else {
                    getRequestState.putString(Utils.B_PICKUP, txtPickupLocation.getText().toString());
                    getRequestState.putString(Utils.B_PICKUP_LAT, String.valueOf(center_point_Idle.latitude));
                    getRequestState.putString(Utils.B_PICKUP_LNG, String.valueOf(center_point_Idle.longitude));

                    if (key.equals("Drop")) {

                        /*if (edtCountt.getText().toString().trim().equals("") || Double.parseDouble(edtCountt.getText().toString().trim()) <= 0) {
                            Toast.makeText(MainActivity.this, getResources().getString(R.string.enter_valid_no_of_agents), Toast.LENGTH_SHORT).show();
                        } else if (edtHrs.getText().toString().trim().equals("") || Double.parseDouble(edtHrs.getText().toString().trim()) <= 0) {
                            Toast.makeText(MainActivity.this, getResources().getString(R.string.enter_valid_no_of_days), Toast.LENGTH_SHORT).show();
                        } else*/
                        if (checkStates(txtDropLocation)) {
                            Log.i("rvCategory", "onClick: rvCategory-> ");

                            cameraIdleValue = "1";
                            getRequestState.putString(Utils.B_DROP, txtDropLocation.getText().toString());
                            getRequestState.putString(Utils.B_DROP_LAT, String.valueOf(tmpDropLatLng.latitude));
                            getRequestState.putString(Utils.B_DROP_LNG, String.valueOf(tmpDropLatLng.longitude));
                            getRequestState.apply();

                          /*  Log.e(TAG, "btnDone onClick:----->>>>>>>>>lat " + center_point_Idle.latitude + "lng" + center_point_Idle.longitude);
                            String pickup = center_point_Idle.latitude + "," + center_point_Idle.longitude;
                            String drop = tmpDropLatLng.latitude + "," + tmpDropLatLng.longitude;
                            getDistanceInfoEndTrip(pickup, drop, categoryId, categoryName, categoryType);*/

                            //Go to Subcategory page
                            Bundle args = new Bundle();
                            args.putString(Utils.B_CATEGORY_ID, categoryId);
                            args.putString(Utils.B_CATEGORY_NAME, categoryName);
                            args.putString(Utils.B_CATEGORY_TYPE, categoryType);
                            args.putString(Utils.B_CATEGORY_GUARDS_COUNT, Objects.requireNonNull(edtNoOfGuards.getText()).toString());
                            args.putString(Utils.B_CATEGORY_DAYS, Objects.requireNonNull(edtHrs.getText()).toString());
                            args.putString(Utils.B_CATEGORY_TELL_US_EMERGENCY, edtTellUsEmergency.getText().toString());
                            Utils.switchContent(R.id.frame_mainactivity,
                                    Utils.FRAGMENT_SUBCATEGORY,
                                    MainActivity.this, args,
                                    Utils.AnimationType.SLIDE_IN_SLIDE_OUT);


                        } else {
                            Toast.makeText(MainActivity.this, R.string.map_enter_drop_location, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        /*if (edtCountt.getText().toString().trim().equals("") || Double.parseDouble(edtCountt.getText().toString().trim()) <= 0) {
                            Toast.makeText(MainActivity.this, getResources().getString(R.string.enter_valid_no_of_agents), Toast.LENGTH_SHORT).show();
                        } else if (edtHrs.getText().toString().trim().equals("") || Double.parseDouble(edtHrs.getText().toString().trim()) <= 0) {
                            Toast.makeText(MainActivity.this, getResources().getString(R.string.enter_valid_no_of_days), Toast.LENGTH_SHORT).show();
                        } else {*/
                        cameraIdleValue = "1";
                        //Go to Subcategory page
                        Bundle args = new Bundle();
                        args.putString(Utils.B_CATEGORY_ID, categoryId);
                        args.putString(Utils.B_CATEGORY_NAME, categoryName);
                        args.putString(Utils.B_CATEGORY_TYPE, categoryType);
                        args.putString(Utils.B_CATEGORY_GUARDS_COUNT, Objects.requireNonNull(edtNoOfGuards.getText()).toString());
                        args.putString(Utils.B_CATEGORY_DAYS, Objects.requireNonNull(edtHrs.getText()).toString());
                        args.putString(Utils.B_CATEGORY_TELL_US_EMERGENCY, edtTellUsEmergency.getText().toString());
                        Utils.switchContent(R.id.frame_mainactivity,
                                Utils.FRAGMENT_SUBCATEGORY,
                                MainActivity.this, args,
                                Utils.AnimationType.SLIDE_IN_SLIDE_OUT);
                        getRequestState.putString(Utils.B_DROP, "NoDrop");
                        getRequestState.putString(Utils.B_DROP_LAT, "0.0");
                        getRequestState.putString(Utils.B_DROP_LNG, "0.0");
                        getRequestState.putString(Utils.B_DISTANCE, "NoDistance");
                        getRequestState.apply();

                        /* }*/
                    }
                }
            }
        });
    }

    //get distance between two location
    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        System.out.println("Lat---" + StartP + "Long---" + EndP);
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
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.parseInt(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.parseInt(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return DecimalUtils.round(valueResult, 2);
    }

    public Double convertKmsToMiles(Double kms) {
        double miles = 0.0;
        if (BaseActivity.convertDistanceUnit(MainActivity.this).equals("mile")) {
            miles = 0.621371 * kms;
        } else if (BaseActivity.convertDistanceUnit(MainActivity.this).equals("km")) {
            miles = kms;
        }
        return DecimalUtils.round(miles, 3);
    }

    private void onClick() {

        uniqueBtn.setOnClickListener(view -> {

            if (edtNoOfGuards.getText() == null || edtNoOfGuards.getText().toString().isEmpty()) {
                Toast.makeText(this, R.string.number_of_guard_size, Toast.LENGTH_SHORT).show();
            } else {
                int guardCount = Integer.parseInt(edtNoOfGuards.getText().toString());
                GuardUniqueIdFragment bottomSheet = new GuardUniqueIdFragment(guardCount);
                bottomSheet.show(getSupportFragmentManager(), "ModalBottomSheet");
            }

        });

        rlCurrentLocView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

                    if (cvDropLyt.getVisibility() == View.VISIBLE) {
                        if (cameraIdleValue.equals("1") || cameraIdleValue.equals("2")) {
                            startIntentService(locFusedLocation, "normal");
                        }
                    } else {
                        if (cameraIdleValue.equals("1") || cameraIdleValue.equals("2")) {
                            startIntentService(locFusedLocation, "normal");
                        }
                    }

                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.mainacativity_getting_location), Toast.LENGTH_SHORT).show();
                }
            }
        });

        txtUpdateLangSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogLanguage != null) {
                    dialogLanguage.show();
                }
            }
        });

        txtUpdateCurrencySetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogCurrency != null) {
                    dialogCurrency.show();
                }
            }
        });

        txtOnGoingJobsLabel.setOnClickListener(v -> {
            Utils.CURRENT_TAG = null;
            Utils.switchContent(R.id.frame_mainactivity,
                    Utils.FRAGMENT_ONGOINGJOBS,
                    MainActivity.this, null,
                    Utils.AnimationType.SLIDE_IN_SLIDE_OUT);
        });

        txtRefresh.setOnClickListener(v -> txtRefreshMain.performClick());
        txtRefreshMain.setOnClickListener(v -> {
            callAppSettings();
            getOnlineCategoryInfo();
            callViewProfileDetails();
            getOnGoingJobsCall();
        });

        navigationLogoutUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutAlertDialog();
            }
        });

        navigationSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawerMethod();
                Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainActivityIntent);
            }
        });

        btnSmsAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lytSMSalert.setVisibility(View.GONE);
            }
        });
        rlPickupLytParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraIdleValue = "2";
                //  beginMoveCamera = true;
                Bundle args = new Bundle();
                args.putString(Utils.B_AUTOCOMPLETELAYOUT, Utils.B_PICKUPAUTOCOMPLETELAYOUT);
                Utils.switchContent(R.id.frame_mainactivity,
                        Utils.FRAGMENT_PLACESAUTOCOMPLETE,
                        MainActivity.this, args,
                        Utils.AnimationType.FADE_IN);
            }
        });

        rlDropLytParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraIdleValue = "2";
                //beginMoveCamera = true;
                Bundle args = new Bundle();
                args.putString(Utils.B_AUTOCOMPLETELAYOUT, Utils.B_DROPAUTOCOMPLETELAYOUT);
                Utils.switchContent(R.id.frame_mainactivity,
                        Utils.FRAGMENT_PLACESAUTOCOMPLETE,
                        MainActivity.this, args,
                        Utils.AnimationType.FADE_IN);
            }
        });


        ivBackMain.setOnClickListener(v -> {
            setVisibility(lytMapMainPage, true);
            setVisibility(lytBookNowBookLater, false);
            if (expansionLayout.isExpanded()) {
                expansionLayout.toggle(false);
            }

            if (categoryModelArraySub != null)
                categoryModelArraySub.clear();
        });

        ivAlertMain.setOnClickListener(v -> callViewEmergencyDetails());
        ivAlertMainMain.setOnClickListener(v -> ivAlertMainMain.performClick());

        edtNoOfGuards.setOnClickListener(view -> {

            if (Objects.requireNonNull(edtNoOfGuards.getText()).toString().isEmpty()) {
                showEditTextDialog("", "guard");
            } else {
                showEditTextDialog(edtNoOfGuards.getText().toString(), "guard");
            }
        });

        edtTellUsEmergency.setOnClickListener(view -> {
            if (Objects.requireNonNull(edtTellUsEmergency.getText()).toString().isEmpty()) {
                showEditTextDialog("", "tell");
            } else {
                showEditTextDialog(edtTellUsEmergency.getText().toString(), "tell");
            }
        });

        imgbtnBackScheduleTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lytScheduleTripConfirmView.getVisibility() == View.VISIBLE) {
                    lytScheduleTripConfirmView.setVisibility(View.GONE);
                }
            }
        });

        btnBookNow.setOnClickListener(view -> {

            if (txtUpdateCurrencySetup.getVisibility() == View.VISIBLE) {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.update_currency_setup), Toast.LENGTH_SHORT).show();
            } else if (edtNoOfGuards.getText().toString().isEmpty()) {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.number_of_guard_must_greater_than_equals_of_two), Toast.LENGTH_SHORT).show();
            } else if (Integer.parseInt(edtNoOfGuards.getText().toString()) < 2) {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.number_of_guard_must_greater_than_equals_of_two), Toast.LENGTH_SHORT).show();
            } else {
                getRequestState.putString(Utils.B_PICKUP, txtPickupLocation.getText().toString());
                getRequestState.putString(Utils.B_PICKUP_LAT, String.valueOf(center_point_Idle.latitude));
                getRequestState.putString(Utils.B_PICKUP_LNG, String.valueOf(center_point_Idle.longitude));
                getRequestState.putString(Utils.B_DROP, "NoDrop");
                getRequestState.putString(Utils.B_DROP_LAT, String.valueOf(0.0));
                getRequestState.putString(Utils.B_DROP_LNG, String.valueOf(0.0));

                getRequestState.putString(Utils.B_TRIP_TYPE, "normaltrip");

                getRequestState.putString(Utils.B_CATEGORY_GUARDS_COUNT, Objects.requireNonNull(edtNoOfGuards.getText()).toString());
                getRequestState.putString(Utils.B_CATEGORY_TELL_US_EMERGENCY, Objects.requireNonNull(edtTellUsEmergency.getText()).toString());
                getRequestState.apply();

                Intent intent = new Intent(MainActivity.this, ProviderListActivity.class);
                overridePendingTransition(R.anim.fade_in_speed, R.anim.fade_out_speed);
                startActivity(intent);
            }
        });

        btnBookLater.setOnClickListener(view -> {

            if (txtUpdateCurrencySetup.getVisibility() == View.VISIBLE) {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.update_currency_setup), Toast.LENGTH_SHORT).show();
            } else if (edtNoOfGuards.getText().toString().isEmpty()) {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.enter_no_of_bodyguards), Toast.LENGTH_SHORT).show();
            } else {
                if (lytScheduleTripConfirmView.getVisibility() == View.GONE) {
                    lytScheduleTripConfirmView.setVisibility(View.VISIBLE);
                }
            }
        });

        rlPickupDate.setOnClickListener(v -> {
            dialogDatePickerLight("start","");
            txtPickupTime.setText(getString(R.string.scheduled_trip_select_start_time));
            txtPickupDate_End.setText(getString(R.string.scheduled_trip_select_end_date));
        });

        rlPickupTime.setOnClickListener(v -> {
            if (txtPickupDate.getText().toString().equals(getString(R.string.scheduled_trip_select_start_date))) {
                Toast.makeText(MainActivity.this, R.string.scheduled_trip_select_start_date, Toast.LENGTH_SHORT).show();
            } else {
                dialogTimePickerLight("start");
            }
        });

        rlPickupDate_End.setOnClickListener(v -> {
            String sTime = selectedTime.substring(0,2);

            String[] separated = strdateView.split("-");
            String yearTime = separated[0]; //yyyy
            Integer monthDate = Integer.valueOf(separated[1]); // MM
            Integer day = Integer.valueOf(separated[2]); // dd

            out.println("sMonth::"+monthDate);
            out.println("sDay::"+day);
            out.println("sYear::"+yearTime);

            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String cDate = simpleDateFormat.format(new Date());

                /*SimpleDateFormat smpMonFormat = new SimpleDateFormat("MM");
                Date date = new Date();
                Integer SMF = Integer.valueOf(smpMonFormat.format(date));*/

                Calendar calendar = Calendar.getInstance();
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                out.println("DayOfMonth"+dayOfMonth);

                /*// seleceted month 6 < current month 5
                out.println("MonthDiff::"+ SMF + " " + monthDate);
                if (monthDate > SMF) {
                    difference_In_Month = monthDate - SMF;
                    out.println("diffInMonths::"+difference_In_Days);
                    dialogDatePickerLight("end", "tmMon");
                } else*/ if (day > dayOfMonth ) { //

                    Date date1 = simpleDateFormat.parse(strdateView);
                    Date date2 = simpleDateFormat.parse(cDate);

                    assert date1 != null;
                    assert date2 != null;
                    long difference_In_Time = date1.getTime() - date2.getTime(); // sub from selected date to current date
                    difference_In_Days = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;

                    out.println("diffInDays::"+difference_In_Days);

                    //if the time greater than 7 pm then move to next day
                        dialogDatePickerLight("end", "tmDay");
                        out.println("moveTotDate");
                } else {
                    //if the time greater than 7 pm then move to next day
                    if (Integer.parseInt(sTime) >= 19 ) {
                        dialogDatePickerLight("end", "nDay");
                        out.println("moveToNDate");
                    } else {
                        dialogDatePickerLight("end", "tDay");
                        out.println("moveTotDate");
                    }
                }

                txtPickupTime_End.setText(getString(R.string.scheduled_trip_select_end_time));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        rlPickupTime_End.setOnClickListener(v -> {
            if (txtPickupDate_End.getText().toString().equals(getString(R.string.scheduled_trip_select_end_date))) {
                Toast.makeText(MainActivity.this, R.string.scheduled_trip_select_end_date, Toast.LENGTH_SHORT).show();
            } else {
                dialogTimePickerLight("end");
            }
        });


        btnContinueScheduleTrip.setOnClickListener(v -> {

            //String strdate = txtPickupDate.getText().toString();
            //String strtime = txtPickupTime.getText().toString();
            //String strdate_end = txtPickupDate_End.getText().toString();
            //String strtime_end = txtPickupTime_End.getText().toString();

            if (BuildConfig.DEBUG) {
                System.out.println("~~~~~~strdate~~~`" + strdate +
                        "~~~~~~strtime~~~`" + strtime +
                        "~~~~~~strdate_end~~~`" + strdate_end +
                        "~~~~~~strtime_end~~~`" + strtime_end);
            }

            if (txtUpdateCurrencySetup.getVisibility() == View.VISIBLE) {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.update_currency_setup), Toast.LENGTH_SHORT).show();
            } else if (strdate == null || txtPickupDate.getText().toString().equals(getString(R.string.scheduled_trip_select_start_date))) {
                Toast.makeText(MainActivity.this, R.string.scheduled_trip_select_start_date, Toast.LENGTH_SHORT).show();
            } else if (strtime == null || txtPickupTime.getText().toString().equals(getString(R.string.scheduled_trip_select_start_time))) {
                Toast.makeText(MainActivity.this, R.string.scheduled_trip_select_start_time, Toast.LENGTH_SHORT).show();
            } else if (strdate_end == null || txtPickupDate_End.getText().toString().equals(getString(R.string.scheduled_trip_select_end_date))) {
                Toast.makeText(MainActivity.this, R.string.scheduled_trip_select_end_date, Toast.LENGTH_SHORT).show();
            } else if (strtime_end == null || txtPickupTime_End.getText().toString().equals(getString(R.string.scheduled_trip_select_end_time))) {
                Toast.makeText(MainActivity.this, R.string.scheduled_trip_select_end_time, Toast.LENGTH_SHORT).show();
            }else {
                if (BuildConfig.DEBUG) {
                    System.out.println("~~~~~~txtPickupDate~~~`" + txtPickupDate.getText().toString());
                    System.out.println("~~~~~~txtPickupDate_End~~~`" + txtPickupDate_End.getText().toString());
                    System.out.println("~~~~~~txtPickupTime~~~`" + txtPickupTime.getText().toString());
                    System.out.println("~~~~~~txtPickupTime_End~~~`" + txtPickupTime_End.getText().toString());
                }

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                try {
                    String finalStrTime = strdateView +" "+ strtime;
                    String finalEndTime = strdate_end_view + " " + strtime_end;
                    out.println("finalStrTime"+finalStrTime);
                    out.println("finalEndTime"+finalEndTime);

                    Date startTime = format.parse(finalStrTime);
                    Date endTime = format.parse(finalEndTime);

                    long differenceInMillis = endTime.getTime() - startTime.getTime();

                    out.println("DiffTime"+endTime.getTime());
                    out.println("DiffTime"+startTime.getTime());
                    int differenceInHours = (int) (differenceInMillis / (1000 * 60 * 60));

                    out.println("TotalTime"+differenceInHours);

                    if (differenceInHours >= bookGuardMinTime) {
                        if (txtPickupDate.getText().toString().equals(txtPickupDate_End.getText().toString())) {
                            if (txtPickupTime.getText().toString().equals(txtPickupTime_End.getText().toString())) {
                                Toast.makeText(MainActivity.this, R.string.scheduled_trip_select_end_time, Toast.LENGTH_SHORT).show();
                            } else {
                                callBookaGuardNextView_();
                            }
                        } else {
                            callBookaGuardNextView_();
                        }
                    } else {
                        if (bookGuardMinTime > 1) {
                            Toast.makeText(this, "To Book a Guard, Select a minimum of" + " " +bookGuardMinTime + " " + "hours", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "To Book a Guard, Select a minimum of" + " " +bookGuardMinTime + " " + "hour", Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void callBookaGuardNextView_() {

        getRequestState.putString(Utils.B_PICKUP, txtPickupLocation.getText().toString());
        getRequestState.putString(Utils.B_PICKUP_LAT, String.valueOf(center_point_Idle.latitude));
        getRequestState.putString(Utils.B_PICKUP_LNG, String.valueOf(center_point_Idle.longitude));
        getRequestState.putString(Utils.B_DROP, "NoDrop");
        getRequestState.putString(Utils.B_DROP_LAT, String.valueOf(0.0));
        getRequestState.putString(Utils.B_DROP_LNG, String.valueOf(0.0));

        getRequestState.putString(Utils.B_TRIP_TYPE, "scheduletrip");

        getRequestState.putString(Utils.B_PICKUP_DATE_LABEL, txtPickupDate.getText().toString());
        getRequestState.putString(Utils.B_PICKUP_TIME_LABEL, txtPickupTime.getText().toString());
        getRequestState.putString(Utils.B_PICKUP_DATE_END_LABEL, txtPickupDate_End.getText().toString());
        getRequestState.putString(Utils.B_PICKUP_TIME_END_LABEL, txtPickupTime_End.getText().toString());

        getRequestState.putString(Utils.B_PICKUP_DATE, strdate);
        getRequestState.putString(Utils.B_PICKUP_TIME, strtime);
        getRequestState.putString(Utils.B_PICKUP_DATE_END, strdate_end);
        getRequestState.putString(Utils.B_PICKUP_TIME_END, strtime_end);

        if (BuildConfig.DEBUG) {
            System.out.println("~~~~~strdate~~" + strdate);
            System.out.println("~~~~~strtime~~" + strtime);
            System.out.println("~~~~~strdate_end~~" + strdate_end);
            System.out.println("~~~~~strtime_end~~" + strtime_end);
        }

        getRequestState.putString(Utils.B_CATEGORY_GUARDS_COUNT, Objects.requireNonNull(edtNoOfGuards.getText()).toString());
        getRequestState.putString(Utils.B_CATEGORY_TELL_US_EMERGENCY, Objects.requireNonNull(edtTellUsEmergency.getText()).toString());
        getRequestState.apply();

        Intent intent = new Intent(MainActivity.this, ProviderListActivity.class);
        overridePendingTransition(R.anim.fade_in_speed, R.anim.fade_out_speed);
        startActivity(intent);
    }

    public void showEditTextDialog(String from, String key) {

        AlertDialog b;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.alertdialog_edittext_main, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setCancelable(true);

        Typeface tfc = Typeface.createFromAsset(this.getAssets(), getString(R.string.App_Font));

        BaseActivity.CustomTFSpan tfSpanc = new BaseActivity.CustomTFSpan(tfc);

        SpannableString spannableStringsc = new SpannableString(getString(R.string.ok));
        spannableStringsc.setSpan(tfSpanc, 0, spannableStringsc.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString spannableStringssc = new SpannableString(getString(R.string.cancel));
        spannableStringssc.setSpan(tfSpanc, 0, spannableStringssc.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        final CustomEditText edt = dialogView.findViewById(R.id.edt_comment);

        String msg_;
        if (key.equals("tell")) {
            msg_ = getResources().getString(R.string.please_tell_us_emergency);
            edt.setHint(getResources().getString(R.string.enter_tell_us_emeregency));
            edt.setInputType(
                    InputType.TYPE_TEXT_FLAG_AUTO_CORRECT
                            | InputType.TYPE_TEXT_FLAG_MULTI_LINE
                            | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
            // Example: Setting input type as password
        } else {
            msg_ = getResources().getString(R.string.number_of_bodyguards);
            edt.setHint(getResources().getString(R.string.enter_no_of_bodyguards));
            edt.setInputType(InputType.TYPE_CLASS_NUMBER);
        }

        edt.setText(from);
        edt.setSelection(Objects.requireNonNull(edt.getText()).toString().length());
        edt.setFilters(new InputFilter[]{EMOJI_FILTER});

        //dialogBuilder.setTitle(spannableStringa);

        SpannableString spannableStringc = new SpannableString(msg_);
        spannableStringc.setSpan(tfSpanc, 0, spannableStringc.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        dialogBuilder.setMessage(spannableStringc);

        dialogBuilder.setPositiveButton(spannableStringsc, (dialog, whichButton) -> {
            //do something with edt.getText().toString();
            if (key.equals("tell")) {
                edtTellUsEmergency.setText(Objects.requireNonNull(edt.getText()).toString().trim());
            } else {
                if (edt.getText().toString().length() <= 2) {
                    if (!edt.getText().toString().isEmpty()) {
                        int guardsCount = Integer.parseInt(edt.getText().toString());
                        if (guardsCount >= 1) {

                            if (result_ != null) {
                                result_.delete(0, result_.length()); // removing data after success
                                uniqueIdList.clear();
                                editor.clear();
                                editor.apply();

                                out.println("old unique id details was cleared size "+ uniqueIdList.size());
                            }


                            edtNoOfGuards.setText(Objects.requireNonNull(edt.getText()).toString().trim());
                            edtNoOfGuards.setSelection(Objects.requireNonNull(edt.getText()).toString().length());
                        } else {
                            edtNoOfGuards.setText("");
                            edtNoOfGuards.setSelection(Objects.requireNonNull(edtNoOfGuards.getText()).toString().length());
                        }
                    } else {
                        Toast.makeText(this, "Please enter the correct count of guards.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Please enter a number of guard between 1 and 99 to book a guard", Toast.LENGTH_SHORT).show();
                }
            }
            dialog.dismiss();
        });

        dialogBuilder.setNegativeButton(spannableStringssc, (dialog, whichButton) -> {
            //pass
            if (key.equals("tell")) {
                edtTellUsEmergency.setText("");
            } else {
                edtNoOfGuards.setText("");
            }
            dialog.dismiss();
        });

        b = dialogBuilder.create();
        b.show();
    }


    /*Navigation Drawer intialize*/
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_myprofile) {
            /*Utils.CURRENT_TAG = null;*/
            Utils.switchContent(R.id.frame_mainactivity,
                    Utils.FRAGMENT_SETTINGS,
                    MainActivity.this, null,
                    Utils.AnimationType.SLIDE_IN_SLIDE_OUT);
        } else if (id == R.id.nav_yourjobs) {
            Bundle args = new Bundle();
            args.putString("viascheduletrip", "home");
            args.putString("from", "home");
            Utils.switchContent(R.id.frame_mainactivity,
                    Utils.FRAGMENT_YOURJOBS,
                    MainActivity.this, args,
                    Utils.AnimationType.SLIDE_IN_SLIDE_OUT);
        } else if (id == R.id.nav_mywallet) {

            if (txtUpdateCurrencySetup.getVisibility() == View.VISIBLE) {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.update_currency_setup), Toast.LENGTH_SHORT).show();
            } else {
                //frameClickable(true);
                Utils.switchContent(R.id.frame_mainactivity,
                        Utils.FRAGMENT_MYWALLET,
                        MainActivity.this, null,
                        Utils.AnimationType.SLIDE_IN_SLIDE_OUT);
            }

        } else if (id == R.id.nav_subscription_plans) {
            Bundle argss = new Bundle();
            argss.putString("from", "main");
            Utils.switchContent(R.id.frame_mainactivity,
                    Utils.FRAGMENT_SUBSCRIPTION_PLAN,
                    MainActivity.this, argss,
                    Utils.AnimationType.SLIDE_IN_SLIDE_OUT);
        } else if (id == R.id.nav_ongoingjob) {
            Utils.switchContent(R.id.frame_mainactivity,
                    Utils.FRAGMENT_ONGOINGJOBS,
                    MainActivity.this, null,
                    Utils.AnimationType.SLIDE_IN_SLIDE_OUT);
        } else if (id == R.id.nav_emergencycontact) {
            Utils.switchContent(R.id.frame_mainactivity,
                    Utils.FRAGMENT_EMERGENCY_CONTACTS,
                    //  Utils.FRAGMENT_PROVIDERLIST,
                    MainActivity.this, null,
                    Utils.AnimationType.SLIDE_IN_SLIDE_OUT);

        } else if (id == R.id.nav_invitefriends) {
            Utils.switchContent(R.id.frame_mainactivity,
                    Utils.FRAGMENT_INVITEFRIENDS,
                    MainActivity.this, null,
                    Utils.AnimationType.SLIDE_IN_SLIDE_OUT);
        } else if (id == R.id.nav_support) {
            Bundle args = new Bundle();
            args.putString(Utils.B_SUPPORT_FROM, "main");
            Utils.switchContent(R.id.frame_mainactivity,
                    Utils.FRAGMENT_SUPPORT,
                    MainActivity.this, args,
                    Utils.AnimationType.SLIDE_IN_SLIDE_OUT);
        } else if (id == R.id.nav_notification) {
            Utils.switchContent(R.id.frame_mainactivity,
                    Utils.FRAGMENT_YOUR_OFFERS_ANNOCEMENTS,
                    MainActivity.this, null,
                    Utils.AnimationType.SLIDE_IN_SLIDE_OUT);
        } else if (id == R.id.nav_language) {
            if (dialogLanguage != null) {
                dialogLanguage.show();
            }
        } else if (id == R.id.nav_currency) {
            if (dialogCurrency != null) {
                dialogCurrency.show();
            }
        } else if (id == R.id.menu_finger) {
            try {
                Utils.CURRENT_TAG = null;

                Intent i = new Intent(getApplicationContext(), SettingsFaceFingerscreen.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                //  finish();
            } catch (Exception e) {
                Log.e(TAG, "onNavigationItemSelected: " + e.toString());
                e.printStackTrace();
            }
        } else if (id == R.id.nav_managedocument) {

            Intent i = new Intent(MainActivity.this, DocumentActivity.class);
            i.putExtra("from", "vehicle");
            startActivity(i);
        } else if (id == R.id.nav_my_event_booking) {

            Utils.CURRENT_TAG = null;
            Utils.switchContent(R.id.frame_mainactivity, Utils.FRAGMENT_MY_CALENDER_BOOKING,
                    MainActivity.this, null, Utils.AnimationType.SLIDE_IN_SLIDE_OUT);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    public void menubuttonClick(View view) {
        openDrawerMethod();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult: in MainActivity");
    }

    @Override
    public void onCameraIdle() {

        if (cvDropLyt.getVisibility() == View.VISIBLE) {
            /* if (txtDropLocation.getText().toString().equals(getResources().getString(R.string.map_enter_drop_location))) {*/
            if (btnDone.getVisibility() != View.VISIBLE) {
                center_point_Idle = mMap.getCameraPosition().target;
                Log.i(TAG, "onCameraIdle: " + center_point_Idle);
                Location locMapCenterLocation = new Location(LocationManager.GPS_PROVIDER);
                locMapCenterLocation.setLatitude(center_point_Idle.latitude);
                locMapCenterLocation.setLongitude(center_point_Idle.longitude);
                if (cameraIdleValue.equals("1") || cameraIdleValue.equals("2")) {
                    startIntentService(locMapCenterLocation, "normal");
                }
            }
            /*}*/
        } else {
            center_point_Idle = mMap.getCameraPosition().target;
            Log.i(TAG, "onCameraIdle: " + center_point_Idle);
            Location locMapCenterLocation = new Location(LocationManager.GPS_PROVIDER);
            locMapCenterLocation.setLatitude(center_point_Idle.latitude);
            locMapCenterLocation.setLongitude(center_point_Idle.longitude);
            if (cameraIdleValue.equals("1")) {
                startIntentService(locMapCenterLocation, "normal");
            }
        }
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
        mMap.setMyLocationEnabled(true);
        mMap.setOnCameraIdleListener(this);
        mMap.setOnCameraMoveStartedListener(this);
        mMap.setOnCameraMoveListener(this);
        mMap.setOnCameraMoveCanceledListener(this);
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        float minZoomLevel = 15.0f; // Example minimum zoom level
        float maxZoomLevel = 17.0f; // Example maximum zoom level
        mMap.setMinZoomPreference(minZoomLevel);
        mMap.setMaxZoomPreference(maxZoomLevel);

    }

    private void callLocationOnCommon(Location location) {

        try {

            Log.i(TAG, "onReceive: Lat:" + location.getLatitude() + "  Lon:  " + location.getLongitude());
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            locFusedLocation = new Location(LocationManager.GPS_PROVIDER);
            locFusedLocation.setLatitude(location.getLatitude());
            locFusedLocation.setLongitude(location.getLongitude());

            if (rlCenterMarker.getVisibility() == View.VISIBLE) {
                if (markerCurrentLocationMan != null) {
                    markerCurrentLocationMan.remove();
                    markerCurrentLocationMan = mMap.addMarker(new MarkerOptions().position(new LatLng(locFusedLocation.getLatitude(), locFusedLocation.getLongitude())).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_man_marker)));
                }
                if (markerCurrentLocationMan == null)
                    markerCurrentLocationMan = mMap.addMarker(new MarkerOptions().position(new LatLng(locFusedLocation.getLatitude(), locFusedLocation.getLongitude())).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_man_marker)));

            }
            // startIntentService(locFusedLocation,"normal");
            if (beginMoveCamera) {
                moveCamera(location.getLatitude(), location.getLongitude());
                beginMoveCamera = false;
            }

        } catch (Exception e) {
            //
        }
    }

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

    @Override
    public void onStart() {
        Log.e(TAG, "onStart: ");
        super.onStart();
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED)
                requestLocationPermission();
            else {
                // start location updates
                FusedLocationSingleton.getInstance().startLocationUpdates();
                // register observer for location updates
                LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(mLocationUpdated,
                        new IntentFilter(Utils.INTENT_FILTER_LOCATION_UPDATE));
            }
        } else {
            // start location updates
            FusedLocationSingleton.getInstance().startLocationUpdates();
            // register observer for location updates
            LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(mLocationUpdated,
                    new IntentFilter(Utils.INTENT_FILTER_LOCATION_UPDATE));
        }
    }

    @Override
    public void onResume() {
        Log.e(TAG, "onResume:~~~~~~~~~~~~~~~~~~~~~~ ");
        super.onResume();


        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED)
                requestLocationPermission();
            else {
                // start location updates
                FusedLocationSingleton.getInstance().startLocationUpdates();
                // register observer for location updates
                LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(mLocationUpdated,
                        new IntentFilter(Utils.INTENT_FILTER_LOCATION_UPDATE));
            }
        } else {
            // start location updates
            FusedLocationSingleton.getInstance().startLocationUpdates();
            // register observer for location updates
            LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(mLocationUpdated,
                    new IntentFilter(Utils.INTENT_FILTER_LOCATION_UPDATE));
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause: ~~~~~~~~~~~~~~~~~~~~");
        // stop location updates
        FusedLocationSingleton.getInstance().stopLocationUpdates();
        // unregister observer
        LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(mLocationUpdated);
    }

    /* Initiate Google API Client  */
    private void initGoogleAPIClient() {
        //Without Google API Client Auto Location Dialog will not work
        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.this)
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
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
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
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_INTENT_ID);

        } else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_INTENT_ID);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case ACCESS_FINE_LOCATION_INTENT_ID: {
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
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void showGpsDailog() {
        String title = getString(R.string.mainactivity_gps_enable_dialog_title);
        String message = getString(R.string.mainactivity_gps_enable_dialog_message);
        String negativeButton = getString(R.string.mainactivity_gps_enable_dialog_negativebutton);
        String positiveButton = getString(R.string.mainactivity_gps_enable_dialog_positivebutton);

        myDialog = DialogUtils.createDialogMessage(this, title, message,
                negativeButton, positiveButton, false, new DialogUtils.DialogListener() {
                    @Override
                    public void onPositiveButton() {
                        myDialog.dismiss();
                        //finish();
                        //System.exit(0);
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
        super.onDestroy();
        try {
            if (runnable != null) handler.removeCallbacks(runnable);
            Utils.CURRENT_TAG = null;
            unregisterReceiver(mGpsSwitchStateReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "onConnected: called");
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
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

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

    public void displayAddressOutput() {
        Log.i(TAG, "onReceiveResult: geocoder-->" + mAddressOutput);
        txtPickupLocation.setText(mAddressOutput);
    }

    public void onPlaceSelected(String layout, String locationText, Prediction prediction, LatLng latlng) {
        if (layout.equals("pickup")) {
            Log.i(TAG, "onPlaceSelected: pickup->" + locationText + " " + latlng);
            txtPickupLocation.setText(locationText);
            tmpPickupLatLng = latlng;
            moveCamera(latlng.latitude, latlng.longitude);
            //boolPickupPlacesAutocomplete = true;

            if (markerPickupConfirmBooking != null)
                markerPickupConfirmBooking.remove();
            if (markerDropConfirmBooking != null)
                markerDropConfirmBooking.remove();

            if (markerCurrentLocationMan != null) {
                markerCurrentLocationMan.remove();
            }

            if (directionPolyline != null) {
                directionPolyline.remove();
            }

            if (strCatType.equals("next")) {
                btnDone.setVisibility(View.GONE);
                btnNext.setVisibility(View.VISIBLE);
            } else if (strCatType.equals("done")) {
                btnDone.setVisibility(View.VISIBLE);
                btnNext.setVisibility(View.GONE);
            } else {
                btnDone.setVisibility(View.GONE);
                btnNext.setVisibility(View.VISIBLE);
            }

        } else {
            Log.i(TAG, "onPlaceSelected: drop->" + locationText + " " + latlng);
            txtDropLocation.setText(locationText);
            tmpDropLatLng = latlng;
            //  moveCamera(latlng.latitude,latlng.longitude);
            //  boolDropPlacesAutocomplete = true;

            if (markerPickupConfirmBooking != null)
                markerPickupConfirmBooking.remove();
            if (markerDropConfirmBooking != null)
                markerDropConfirmBooking.remove();

            if (markerCurrentLocationMan != null) {
                markerCurrentLocationMan.remove();
            }

            if (directionPolyline != null) {
                Log.i(TAG, "onDataChange: Starttrip directionPolyline if ");
                directionPolyline.remove();
            }

            if (strCatType.equals("next")) {
                btnDone.setVisibility(View.GONE);
                btnNext.setVisibility(View.VISIBLE);
            } else if (strCatType.equals("done")) {
                btnDone.setVisibility(View.VISIBLE);
                btnNext.setVisibility(View.GONE);
            } else {
                btnDone.setVisibility(View.GONE);
                btnNext.setVisibility(View.VISIBLE);
            }
        }
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

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Check savedInstanceState to see if the address was previously requested.
            if (savedInstanceState.keySet().contains(ADDRESS_REQUESTED_KEY)) {
                boolean mAddressRequested = savedInstanceState.getBoolean(ADDRESS_REQUESTED_KEY);
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
    public static void moveCamera(Double lat, Double lng) {
        LatLng coordinates = new LatLng(lat, lng);
        CameraUpdate current = CameraUpdateFactory.newLatLngZoom(coordinates, Utils.MAP_DEFAULT_ZOOM);
        mMap.moveCamera(current);
    }

    public void addMarqueeTextfield(TextView textview) {
        textview.setHorizontallyScrolling(true);
        textview.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textview.setSingleLine();
        textview.setSelected(true);
    }

    private void getOnlineCategoryInfo() {

        userAccessToken = spCommon.getString(Utils.SP_USER_ACCESSTOKEN, null);

        if (categoryModelArray != null)
            categoryModelArray.clear();
        HashMap<String, Object> map = new HashMap<>();
        map.put("language", strLanguageShortName);

        Call<VehicleCategoryModel> call2 = apiInterface.doGetVehiclecategoryUpdateDetails("Bearer " + userAccessToken, Utils.POST_ACCEPT_CONTENT_TYPE, Utils.POST_ACCEPT_CONTENT_TYPE, map);
        progressBarMain.setVisibility(View.VISIBLE);
        call2.enqueue(new Callback<VehicleCategoryModel>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<VehicleCategoryModel> call, @NonNull retrofit2.Response<VehicleCategoryModel> response) {
                // hideProgressDialog();
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {

                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("categories listed successfully.")) {

                        getve = Objects.requireNonNull(response.body()).getVehicleCategoryData();

                        if (getve != null && getve.size() > 0) {

                            String[] items = new String[getve.size()];
                            Log.i("itemsitems", "" + Arrays.toString(items));

                            txtNoRecordsMain.setVisibility(View.GONE);
                            txtLoadingMain.setVisibility(View.GONE);
                            txtRefreshMain.setVisibility(View.GONE);
                            progressBarMain.setVisibility(View.GONE);

                            for (int i = 0; i < getve.size(); i++) {
                                CategoryModel categoryModel = new CategoryModel();

                                if (strLanguageShortName.equals("ar")) {
                                    categoryModel.setCategoryName(getve.get(i).getCategoryNameSpanish());
                                } else {
                                    categoryModel.setCategoryName(getve.get(i).getCategoryName());
                                }
                                categoryModel.setCategoryEnName(String.valueOf(getve.get(i).getCategoryName()));
                                categoryModel.setCategoryArName(String.valueOf(getve.get(i).getCategoryNameSpanish()));
                                categoryModel.setCategoryId(String.valueOf(getve.get(i).getId()));
                                categoryModel.setCategoryImage(getve.get(i).getCategoryImage());
                                categoryModel.setcatType(String.valueOf(getve.get(i).getCategoryType()));
                                categoryModel.setLevelType(String.valueOf(getve.get(i).getLevelType()));
                                categoryModel.setLevelAmount(String.valueOf(getve.get(i).getLevelAmount()));
                                if (i == 0)
                                    categoryModel.setIsSelected(true);
                                else
                                    categoryModel.setIsSelected(false);

                                categoryModelArray.add(categoryModel);

                                items[i] = String.valueOf(getve.get(i));

                            }

                            mapCategoryAdapter.selected(0);

                            getRequestState.putString(Utils.B_CATEGORY_ID, String.valueOf(getve.get(0).getId()));
                            getRequestState.putString(Utils.B_CATEGORY_NAME, String.valueOf(getve.get(0).getCategoryName()));
                            getRequestState.putString(Utils.B_CATEGORY_NAME_AR, String.valueOf(getve.get(0).getCategoryNameSpanish()));
                            getRequestState.putString(Utils.B_CATEGORY_LEVEL_TYPE, String.valueOf(getve.get(0).getLevelType()));
                            getRequestState.putString(Utils.B_CATEGORY_LEVEL_AMOUNT, String.valueOf(getve.get(0).getLevelAmount()));
                            getRequestState.apply();

                            /*if (String.valueOf(getve.get(0).getCategoryType()).equals("1")) {
                                cvDropLyt.setVisibility(View.VISIBLE);
                                txtDropLocation.setText(getResources().getString(R.string.map_enter_drop_location));
                                strCatType = "next";
                                callNext(String.valueOf(getve.get(0).getId()), String.valueOf(getve.get(0).getCategoryName()), String.valueOf(getve.get(0).getCategoryType()));
                            } else {
                                strCatType = "done";
                                rlCenterMarker.setVisibility(View.VISIBLE);
                                cvDropLyt.setVisibility(View.GONE);
                                btnNext.setVisibility(View.GONE);
                                callDone("NoDrop", String.valueOf(getve.get(0).getId()), String.valueOf(getve.get(0).getCategoryName()), String.valueOf(getve.get(0).getCategoryType()));
                            }*/


                            mapCategoryAdapter.notifyDataSetChanged();

                            //getOnlineSubCategoryInfo(String.valueOf(getve.get(0).getId()));

                        } else {
                            txtNoRecordsMain.setVisibility(View.VISIBLE);
                            txtLoadingMain.setVisibility(View.GONE);
                            txtRefreshMain.setVisibility(View.GONE);
                            progressBarMain.setVisibility(View.GONE);
                        }

                    } else {
                        txtNoRecordsMain.setVisibility(View.VISIBLE);
                        txtLoadingMain.setVisibility(View.GONE);
                        txtRefreshMain.setVisibility(View.GONE);
                        progressBarMain.setVisibility(View.GONE);
                    }

                } else {
                    txtNoRecordsMain.setVisibility(View.GONE);
                    txtLoadingMain.setVisibility(View.GONE);
                    txtRefreshMain.setVisibility(View.GONE);
                    progressBarMain.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<VehicleCategoryModel> call, @NonNull Throwable t) {
                Log.i("errror", "errr" + t.toString());
                txtNoRecordsMain.setVisibility(View.GONE);
                txtLoadingMain.setVisibility(View.VISIBLE);
                txtRefreshMain.setVisibility(View.VISIBLE);
                progressBarMain.setVisibility(View.GONE);
                call.cancel();
            }
        });

    }

    private void getOnlineSubCategoryInfo(String categoryId) {

        userAccessToken = spCommon.getString(Utils.SP_USER_ACCESSTOKEN, null);

        if (categoryModelArraySub != null)
            categoryModelArraySub.clear();

        HashMap<String, Object> map = new HashMap<>();
        map.put("id", categoryId);
        map.put("language", strLanguageShortName);

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<VehicleSubCategoryModel> call2 = apiInterface.doGetVehicleSubcategoryDetails("Bearer " + userAccessToken, Utils.POST_ACCEPT_CONTENT_TYPE, map);
        rlParentLytProgress.setVisibility(View.VISIBLE);
        call2.enqueue(new Callback<VehicleSubCategoryModel>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<VehicleSubCategoryModel> call, @NonNull retrofit2.Response<VehicleSubCategoryModel> response) {
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {


                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("categories services listed successfully.")) {

                        List<VehicleSubCategoryData> getve = Objects.requireNonNull(response.body()).getVehicleSubCategoryData();

                        String[] items = new String[getve.size()];
                        Log.i("itemsitems", Arrays.toString(items));

                        for (int i = 0; i < getve.size(); i++) {

                            CategorySubModel categoryModel = new CategorySubModel();

                            if (strLanguageShortName.equals("ar")) {
                                categoryModel.setSubCategoryName(getve.get(i).getServiceNameSpanish());
                            } else {
                                categoryModel.setSubCategoryName(getve.get(i).getServiceName());
                            }
                            categoryModel.setSubCategoryEnName(getve.get(i).getServiceName());
                            categoryModel.setSubCategoryArName(getve.get(i).getServiceNameSpanish());
                            categoryModel.setSubCategoryId(String.valueOf(getve.get(i).getId()));
                            categoryModel.setSubCategoryImage(getve.get(i).getServiceImage());
                            categoryModel.setSubCategoryBasefare(String.valueOf(getve.get(i).getBaseFare()));
                            categoryModel.setSubCategoryAdminComm(String.valueOf(getve.get(i).getAdminCommission()));
                            categoryModel.setSubMainCategoryId(String.valueOf(getve.get(i).getCategoryId()));
                            categoryModel.setSubCategoryPricePerKm(String.valueOf(getve.get(i).getPricePerKm()));
                            categoryModel.setSubCategoryVistiPrice(String.valueOf(getve.get(i).getVisitFare()));
                            categoryModel.setSubCategoryAgentPrice(getve.get(i).getPricePerAgent());
                            categoryModel.setSubCategoryDayPrice(getve.get(i).getPricePerDay());
                            bookGuardMinTime = getve.get(i).getBook_guard_min_time();

                            if (bookGuardMinTime > 1) {
                                txt_book_a_guard.setText("To Book a Guard, Select a minimum of" + " " +bookGuardMinTime + " " + "hours");
                            } else  {
                                txt_book_a_guard.setText("To Book a Guard, Select a minimum of" + " " +bookGuardMinTime + " " + "hour");
                            }

                            out.println("bookLaterTimer"+bookGuardMinTime);

                            if (i == 0)
                                categoryModel.setIsSelected(true);
                            else
                                categoryModel.setIsSelected(false);
                            categoryModelArraySub.add(categoryModel);

                            items[i] = String.valueOf(getve.get(i));

                        }

                        if (getve.size() >= 3) {
                            leftArrow.setVisibility(View.VISIBLE);
                            rightArrow.setVisibility(View.VISIBLE);
                        } else {
                            leftArrow.setVisibility(View.INVISIBLE);
                            rightArrow.setVisibility(View.INVISIBLE);
                        }

                        getRequestState.putString(Utils.B_SUB_CATEGORY_ID, String.valueOf(getve.get(0).getId()));
                        getRequestState.putString(Utils.B_SUB_CATEGORY_NAME, String.valueOf(getve.get(0).getServiceName()));
                        getRequestState.putString(Utils.B_SUB_CATEGORY_NAME_AR, String.valueOf(getve.get(0).getServiceNameFrench()));
                        getRequestState.putString(Utils.B_SUB_CATEGORY_PRICE, String.valueOf(getve.get(0).getBaseFare()));
                        getRequestState.putString(Utils.B_CATEGORY_AGENT_PRICE, "0");
                        getRequestState.putString(Utils.B_CATEGORY_DAYS_PRICE, "0");
                        getRequestState.apply();

                        mapCategorySubAdapter.selected(0);
                        mapCategorySubAdapter.notifyDataSetChanged();

                        if (!expansionLayout.isExpanded()) {
                            expansionLayout.toggle(true);
                        }
                        setVisibility(lytBookNowBookLater, true);

                        setVisibility(txtNoRecords, false);
                        setVisibility(txtRefresh, false);
                        setVisibility(rlParentLytProgress, false);

                    } else if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("No records Found.")) {
                        Toast.makeText(MainActivity.this, R.string.no_record_found, Toast.LENGTH_SHORT).show();

                        setVisibility(txtNoRecords, true);
                        setVisibility(txtRefresh, false);
                        setVisibility(rlParentLytProgress, false);
                    } else {
                        setVisibility(txtNoRecords, true);
                        setVisibility(txtRefresh, false);
                        setVisibility(rlParentLytProgress, false);
                    }
                } else {
                    setVisibility(txtNoRecords, true);
                    setVisibility(txtRefresh, false);
                    setVisibility(rlParentLytProgress, false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<VehicleSubCategoryModel> call, @NonNull Throwable t) {
                Log.i("errror", "errr" + t);
                call.cancel();
                setVisibility(txtNoRecords, true);
                setVisibility(txtRefresh, false);
                setVisibility(rlParentLytProgress, false);
            }
        });
    }

    private void getOnGoingJobsCall() {

        userAccessToken = spCommon.getString(Utils.SP_USER_ACCESSTOKEN, null);
        HashMap<String, Object> map = new HashMap<>();
        map.put("customer_id", userId);
        map.put("language", strLanguageShortName);

        Call<OnGoingJobsModel> call2 = apiInterface.doCustomerOnGoingJobs("Bearer " + userAccessToken, map);
        call2.enqueue(new Callback<OnGoingJobsModel>() {
            @Override
            public void onResponse(@NonNull Call<OnGoingJobsModel> call, @NonNull retrofit2.Response<OnGoingJobsModel> response) {
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    hideProgressDialog();
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Fetching ongoing job successful.")) {
                        List<OnGoingJobsData> getve = response.body().getServiceRequest();
                        Log.i(TAG, "onResponse: getOnGoingJobsCall-->" + getve);
                        if (getve.size() > 0) {
                            txtOnGoingJobsLabel.setVisibility(View.VISIBLE);
                        } else {
                            txtOnGoingJobsLabel.setVisibility(View.GONE);
                        }
                    } else {
                        //callRefreshToken("Category");
                        txtOnGoingJobsLabel.setVisibility(View.GONE);
                    }
                } else if (response.code() == RESPONSECODE_401) {
                    //callRefreshToken("Category");
                    txtOnGoingJobsLabel.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<OnGoingJobsModel> call, @NonNull Throwable t) {
                Log.i("errror", "errr" + t);
                call.cancel();
                txtOnGoingJobsLabel.setVisibility(View.GONE);
            }
        });

    }

    private void callAppSettings() {

        /*if(appSettingArray!=null)
            appSettingArray.clear();*/


        Call<AppSettingWebModel> call2 = apiInterface.doGetAppSettings();
        call2.enqueue(new Callback<AppSettingWebModel>() {
            @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
            @Override
            public void onResponse(@NonNull Call<AppSettingWebModel> call, @NonNull Response<AppSettingWebModel> response) {
                hideProgressDialog();
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {

                    String STRIPE_PUB_KEY = Objects.requireNonNull(response.body()).getAppSetting().getPaymentPublisherKey();
                    String STRIPE_SECRET_KEY = Objects.requireNonNull(response.body()).getAppSetting().getPaymentSecretKey();
                    String DISTANCE_LIMIT = String.valueOf(Objects.requireNonNull(response.body()).getAppSetting().getDistanceLimit());
                    String requestTime = String.valueOf(Objects.requireNonNull(response.body()).getAppSetting().getTimeOutDelay());

                    String strTowkChatUrl = String.valueOf(Objects.requireNonNull(response.body()).getAppSetting().getTawkUrl());
                    String strTowkPropertyUrl = String.valueOf(Objects.requireNonNull(response.body()).getAppSetting().getPropertyUrl());
                    String strTowkPropertyId = String.valueOf(Objects.requireNonNull(response.body()).getAppSetting().getPropertyId());
                    String adminPaypalStatus = String.valueOf(Objects.requireNonNull(response.body()).getAppSetting().getPaypalStatus());
                    getCustomerManageDocument = Objects.requireNonNull(response.body()).getAppSetting().getCustomerManageDocument();

                    strDistanceUnit = response.body().getAppSetting().getDistanceUnit(); //miles

                    if (BuildConfig.DEBUG) {
                        Log.e(TAG, "onResponse: StripeKeys Admin" + STRIPE_PUB_KEY + "~~" + STRIPE_SECRET_KEY);
                        Log.e(TAG, "onResponse: Distance Limit" + response.body().getAppSetting().getDistanceLimit());
                    }

                    getState.putInt(Utils.SP_COMMON_CUSTOMER_MANAGE_DOCUMENT, getCustomerManageDocument);

                    getState.putString(Utils.SP_COMMON_STRIPE_PUBLISH_KEY, STRIPE_PUB_KEY);
                    getState.putString(Utils.SP_COMMON_STRIPE_SECRET_KEY, STRIPE_SECRET_KEY);
                    getState.putString(Utils.SP_COMMON_DISTANCE_LIMIT, DISTANCE_LIMIT);
                    getState.putString(Utils.SP_COMMON_REQUEST_TIME_DELAY_ACCEPT, requestTime);
                    getState.putString(Utils.SP_COMMON_DISTANCE_UNIT, strDistanceUnit);
                    getState.putString(Utils.SP_COMMON_ADMIN_PAYPAL_STATUS, adminPaypalStatus);

                    getState.putString(Utils.SP_COMMON_TOWK_CHAT_URL, strTowkChatUrl);
                    getState.putString(Utils.SP_COMMON_TOWK_PROPERTY_URL, strTowkPropertyUrl);
                    getState.putString(Utils.SP_COMMON_TOWK_PROPERTY_ID, strTowkPropertyId);

                    getState.apply();

                    List<AppSettingSmsTemplate> smsTemplates = Objects.requireNonNull(response.body()).getSmsTemplates();
                    minWalletAmount = String.valueOf(Objects.requireNonNull(response.body()).getAppSetting().getWalletMinimum());

                    for (int i = 0; i < smsTemplates.size(); i++) {
                       /* AppSettingModel appSettingModel = new AppSettingModel();
                        appSettingModel.setsmssubject(smsTemplates.get(i).getSmsSubject());
                        appSettingModel.setsmscontent(smsTemplates.get(i).getSmsContent());*/
                        smsTemplateSubject = smsTemplates.get(i).getSmsSubject();
                        smsTemplateContent = smsTemplates.get(i).getSmsContent();
                        Log.e("smsTemplates.get(i).getSmsSubject()~~", smsTemplates.get(i).getSmsSubject());
                        Log.e("smsTemplates.get(i).getSmsContent()~~", smsTemplates.get(i).getSmsContent());
                        //appSettingArray.add(appSettingModel);
                    }
                    Log.i(TAG, "onResponse: walletamount-->" + minWalletAmount);
                    getState.putString(Utils.SP_USER_WALLET_AMT_MIN, minWalletAmount);
                    getState.apply();

                    List<AppSettingLanguageModel> languageTemplates = Objects.requireNonNull(response.body()).getLanguage();
                    if (languageTemplates.size() > 0) {
                        ListModel = languageTemplates;
                        rcvLanguage.setVisibility(View.VISIBLE);
                        llStaticLanguage.setVisibility(View.GONE);
                        adapterLanguage = new LanguageAdapter(MainActivity.this, languageTemplates);
                        LinearLayoutManager horizontalLayoutManager
                                = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
                        rcvLanguage.setLayoutManager(horizontalLayoutManager);
                        rcvLanguage.setAdapter(adapterLanguage);
                        adapterLanguage.notifyDataSetChanged();
                    } else {
                        rcvLanguage.setVisibility(View.GONE);
                        llStaticLanguage.setVisibility(View.VISIBLE);
                    }

                    List<AppSettingCurrencyModel> currencyTemplates = Objects.requireNonNull(response.body()).getCurrency();
                    if (currencyTemplates.size() > 0) {
                        ListModelCurrency = currencyTemplates;
                        rcvCurrency.setVisibility(View.VISIBLE);
                        llStaticCurrency.setVisibility(View.GONE);
                        adapterCurrency = new CurrencyAdapter(MainActivity.this, currencyTemplates);
                        LinearLayoutManager horizontalLayoutManager
                                = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
                        rcvCurrency.setLayoutManager(horizontalLayoutManager);
                        rcvCurrency.setAdapter(adapterCurrency);
                        adapterCurrency.notifyDataSetChanged();
                    } else {
                        rcvCurrency.setVisibility(View.GONE);
                        llStaticCurrency.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AppSettingWebModel> call, @NonNull Throwable t) {
                call.cancel();
            }
        });

        callViewProfileDetails(); //API

    }

    private void callViewEmergencyDetails() {
        showProgressDialog(MainActivity.this);
        userAccessToken = spCommon.getString(Utils.SP_USER_ACCESSTOKEN, null);
        HashMap<String, Object> map = new HashMap<>();
        map.put("customer_id", userId);
//        map.put("token",userAccessToken );
        Log.i("callViewProfileDetails", "~~~~" + map);

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

                        /*String[] months = {"+91 88701 46888", "+919789354285", "+91 97860 52373"};
                        ArrayList<String> monthNames = new ArrayList<String>(Arrays.asList(months));*/

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

                    } else if (response.body().getMessage().equalsIgnoreCase("No records Found.")) {
                        hideProgressDialog();
                        Utils.switchContent(R.id.frame_mainactivity,
                                Utils.FRAGMENT_EMERGENCY_CONTACTS,
                                MainActivity.this, null,
                                Utils.AnimationType.SLIDE_IN_SLIDE_OUT);

                    } else {
                        //callRefreshToken("viewemergency");
                    }
                    hideProgressDialog();
                } else if (response.code() == RESPONSECODE_401) {

                    //callRefreshToken("viewemergency");
                }
            }

            @Override
            public void onFailure(@NonNull Call<EmergencyViewModel> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: ~~~~~~~EmergencyViewModel~~~~~~~~~~" + t);
                hideProgressDialog();
                call.cancel();
            }
        });
    }

    //get wallet amount in firebase
    private void getCustomerWalletAmount() {

        if (customerWalletDatabaseBRef != null) {
            customerWalletDatabaseBRef.removeEventListener(customerWalletValueEventListener);
        }
        customerWalletDatabaseBRef = FirebaseDatabase.getInstance().getReference().child(Utils.FIREBASE_USER_HEADER).child(userId);
        customerWalletValueEventListener = customerWalletDatabaseBRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    Object walletAmountObj = dataSnapshot.child("wallet").getValue();
                    Object ratingObj = dataSnapshot.child("rating").getValue();
                    Object currencyObj = dataSnapshot.child("currency").getValue();
                    Object walletBalAmountObj = dataSnapshot.child("balance_amount").getValue();

                    if (currencyObj != null) {
                        if (!currencyObj.toString().equals("")) {
                            String strSplitCurrency = currencyObj.toString();
                            String[] splitStr = strSplitCurrency.split(":");
                            strCurrency = splitStr[0];
                            strCurrencySymbol = splitStr[1];
                        }/*else{
                            strCurrency = "LKR";
                            strCurrencySymbol ="Rs ";
                        }*/

                    }/*else{
                        strCurrency = "LKR";
                        strCurrencySymbol ="Rs ";
                    }*/

                    if (walletAmountObj != null) {
                        strWalletAmt = walletAmountObj.toString();
                    } else {
                        strWalletAmt = "0";
                    }

                    if (ratingObj != null) {
                        navRbuserrating.setRating(Float.parseFloat(ratingObj.toString()));
                    } else {
                        navRbuserrating.setRating(0);
                    }

                    getState.putString(Utils.SP_USER_WALLET_AMT, strWalletAmt);
                    getState.apply();

                    strCurrencySymbol = spCurrency.getString(Utils.SP_CURRENCY_SYMBOL, null);

                    Log.i(TAG, "onDataChange: wallet strCurrencySymbol-->" + strCurrencySymbol);
                    System.out.println(">" + strWalletAmt);

                    if (strWalletAmt != null) {
                        if (!strWalletAmt.equals("0")) {
                            // convertPrice(strWalletAmt);
                            navigationWalletAmount.setText(getString(R.string.nav_wallet_bal_text) + " " + strCurrencySymbol + " " +
                                    convertCurrencyPrice(strWalletAmt, "1"));
                        } else {
                            navigationWalletAmount.setText(getString(R.string.nav_wallet_bal_text) + " " + strCurrencySymbol + " " + strWalletAmt);
                        }
                    } else {
                        navigationWalletAmount.setText(getString(R.string.nav_wallet_bal_text) + " " + strCurrencySymbol + " " + "0");
                    }


                    if (dataSnapshot.child("delete_status").getValue() != null) {
                        strDeleteStatus = dataSnapshot.child("delete_status").getValue().toString();
                    } else {
                        strDeleteStatus = "0";
                    }
                    System.out.println("strDeleteStatus-->" + strDeleteStatus);
                    deleteMyAccount = spCommon.getString(Utils.SP_USER_DELETE_ACCOUNT, "0");
                    System.out.println("deleteMyAccount-->" + deleteMyAccount);
                    if (strDeleteStatus.equals("1")) {
                        if (deleteMyAccount.equals("0")) {
                            Toast.makeText(MainActivity.this, getString(R.string.account_deleted_msg), Toast.LENGTH_LONG).show();
                            clearSPmodePrivate();
                        }
                    }


                } else {
                    Log.i(TAG, "onDataChange else -->" + strWalletAmt);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void callViewProfileDetails() {

        userAccessToken = spCommon.getString(Utils.SP_USER_ACCESSTOKEN, null);
        if (userAccessToken == null) {
            userAccessToken = "";
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", userId);
        map.put("token", userAccessToken);
        Log.i("callViewProfileDetails", "~~~~" + map);

        Call<ProfileModel> call2 = apiInterface.doGetprofileDetails("Bearer " + userAccessToken, Utils.POST_ACCEPT_CONTENT_TYPE, map);
        // showProgressDialog(MainActivity.this);
        call2.enqueue(new Callback<ProfileModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<ProfileModel> call, @NonNull Response<ProfileModel> response) {
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Profile listed successfully.")) {

                        try {

                            DatabaseReference refs = FirebaseDatabase.getInstance().getReference();

                            String getProfPic = String.valueOf(Objects.requireNonNull(response.body()).getProfileData().getAvatar());
                            getState.putString(Utils.SP_USER_PRO_PIC, getProfPic);
                            getState.apply();

                            String strwalletAmt = String.valueOf(Objects.requireNonNull(response.body()).getProfileData().getWalletBalance());

                            System.out.println("view_profile==>" + strwalletAmt);
                            if (!strwalletAmt.equals("null") && !strwalletAmt.isEmpty()) {
                                String s = String.valueOf(DecimalUtils.round(Double.parseDouble(strwalletAmt), 2));
                                Log.e(TAG, "onResponse: ~~~~" + s);
                                refs.child(Utils.FIREBASE_USER_HEADER).child(userId).child("wallet").setValue(s);
                                getState.putString(Utils.SP_USER_WALLET_AMT, strwalletAmt);
                                getState.apply();
                            } else {
                                refs.child(Utils.FIREBASE_USER_HEADER).child(userId).child("wallet").setValue("0");
                            }

                            /*if (response.body().getProfileData().getCurrency() != null) {
                                Log.i(TAG, "onResponse: profile details-->" + response.body().getProfileData().getCurrency());
                                if (!response.body().getProfileData().getCurrency().equals("")) {
                                    searchCurrencyPrice(response.body().getProfileData().getCurrency());
                                    txtUpdateCurrencySetup.setVisibility(View.GONE);
                                } else {
                                    txtUpdateCurrencySetup.setVisibility(View.VISIBLE);
                                }
                            } else {
                                Log.i(TAG, "onResponse: profile details else-->");
                                txtUpdateCurrencySetup.setVisibility(View.VISIBLE);
                            }*/

                            if (response.body().getProfileData().getCurrency() != null &&
                                    !response.body().getProfileData().getCurrency().equals("null") &&
                                    !response.body().getProfileData().getCurrency().isEmpty()) {
                                // txtUpdateCurrencySetupOnline.setVisibility(View.VISIBLE);
                                txtUpdateCurrencySetup.setVisibility(View.GONE);
                                if (ListModelCurrency != null && ListModelCurrency.size() > 0) {
                                    String curShortName = String.valueOf(ListModelCurrency.get(0).getShortName());
                                    String curId = String.valueOf(ListModelCurrency.get(0).getId());
                                    callCurrencyUpdate(curId, curShortName, "view");
                                } else {
                                    callCurrencyUpdate("1", "GBP", "view");
                                }
                            } else {
                                txtUpdateCurrencySetup.setVisibility(View.GONE);
                            }

                            if (response.body().getProfileData().getLanguage() != null &&
                                    !response.body().getProfileData().getLanguage().equals("null") &&
                                    !response.body().getProfileData().getLanguage().isEmpty()) {
                                txtUpdateLangSetup.setVisibility(View.GONE);
                            } else {
                                //txtUpdateLangSetup.setVisibility(View.VISIBLE);
                                txtUpdateLangSetup.setVisibility(View.GONE);
                                CallUpdateDetails("1", "English", "en", "view");
                            }

                            String rating = String.valueOf(Objects.requireNonNull(response.body()).getRating());
                            refs.child(Utils.FIREBASE_USER_HEADER).child(userId).child("rating").setValue(rating);

                            String strUserfname = Objects.requireNonNull(response.body()).getProfileData().getFirstName();
                            //edtLname.setText(Objects.requireNonNull(response.body()).getProfileData().getLastName());

                            if (strUserfname != null) {
                                String upperString = strUserfname.substring(0, 1).toUpperCase() + strUserfname.substring(1);
                                navigationName.setText(upperString);
                            } else {
                                navigationName.setText("User");
                            }

                            if (!getProfPic.equals("null")) {
                                navigationProfileImage.setImageURI(Utils.BASE_URL + getProfPic);
                                int color = getResources().getColor(R.color.colorPrimary);
                                RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
                                roundingParams.setBorder(color, 1.0f);
                                roundingParams.setRoundAsCircle(true);
                                navigationProfileImage.getHierarchy().setRoundingParams(roundingParams);
                            }


                            String strSubscriptionId = String.valueOf(Objects.requireNonNull(response.body()).
                                    getProfileData().getSubscriptionId());
                            System.out.println("~~~~strSubscriptionId~~~~" + strSubscriptionId);

                            String strSubscriptionStatus = String.valueOf(Objects.requireNonNull(response.body()).
                                    getProfileData().getSubscriptionStatus());
                            System.out.println("~~~~strSubscriptionStatus~~~~" + strSubscriptionStatus);

                            String strSubscriptionName = String.valueOf(Objects.requireNonNull(response.body()).
                                    getProfileData().getSubscriptionName());
                            /*String strSubscriptionAmount = String.valueOf(Objects.requireNonNull(response.body()).
                                    getProfileData().getSubscriptionAmount());
                            String strSubscriptionExpire = String.valueOf(Objects.requireNonNull(response.body()).
                                    getProfileData().getSubscriptionExpiry());*/

                            //refs.child(Utils.FIREBASE_USER_HEADER).child(userId).child("remaining_job").setValue("0");

                            getState.putString(Utils.SP_USER_SUBSCRIPTION_CODE, strSubscriptionName);
                            getState.putString(Utils.SP_USER_SUBSCRIPTION_CODE_ID, strSubscriptionId);
                            getState.apply();

                            MenuItem nav_subscription_plans = navigationView.getMenu().findItem(R.id.nav_subscription_plans);
                            //Remove Sikka - added vignesh - Feb7th24
                            if (!strSubscriptionStatus.equals("null") && !strSubscriptionStatus.equals("")) {
                                switch (strSubscriptionStatus) {
                                    case "0": //Default
                                    case "0.0": //Default

                                        nav_subscription_plans.setTitle(getResources().getString(R.string.nav_mymembership_not));

                                        // txtSubscriptionPlanSetupLabel.setVisibility(View.VISIBLE);
                                        txtSubscriptionPlanSetupLabel.setVisibility(View.GONE);
                                        txtSubscriptionPlanSetupLabel.setText(getResources().getString(R.string.your_subs_plan_choose));
                                        //  txtSubscriptionPlanSetupLabel.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                                        // txtSubscriptionPlanSetupLabel.setTextColor(getResources().getColor(R.color.colorPrimary));

                                        navigationSubsPlan.setVisibility(View.GONE);
                                        navigationSubsPlan.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray_very_light)));
                                        navigationSubsPlan.setText(getResources().getString(R.string.plan_choose));

                                        break;
                                    case "1": //Active
                                    case "1.0": //Active

                                        nav_subscription_plans.setTitle(getResources().getString(R.string.nav_mymembership));

                                        //txtSubscriptionPlanSetupLabel.setVisibility(View.GONE);
                                        txtSubscriptionPlanSetupLabel.setVisibility(View.GONE);
                                        txtSubscriptionPlanSetupLabel.setText(getResources().getString(R.string.your_subs_plan_active));
                                        //  txtSubscriptionPlanSetupLabel.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.holo_green_dark)));
                                        //  txtSubscriptionPlanSetupLabel.setTextColor(getResources().getColor(R.color.white));

                                        navigationSubsPlan.setVisibility(View.VISIBLE);
                                        navigationSubsPlan.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.holo_green_dark)));
                                        navigationSubsPlan.setText(getResources().getString(R.string.plan_active));

                                        break;
                                    case "2": //Expired
                                    case "2.0": //Expired

                                        nav_subscription_plans.setTitle(getResources().getString(R.string.nav_mymembership_not));

                                        txtSubscriptionPlanSetupLabel.setVisibility(View.VISIBLE);
                                        txtSubscriptionPlanSetupLabel.setText(getResources().getString(R.string.your_subs_plan_expiry));
                                        //   txtSubscriptionPlanSetupLabel.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.holo_red_dark)));
                                        //   txtSubscriptionPlanSetupLabel.setTextColor(getResources().getColor(R.color.white));

                                        navigationSubsPlan.setVisibility(View.VISIBLE);
                                        navigationSubsPlan.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.holo_red_dark)));
                                        navigationSubsPlan.setText(getResources().getString(R.string.plan_expired));

                                        break;
                                }
                            } else {
                                nav_subscription_plans.setTitle(getResources().getString(R.string.nav_mymembership_not));
                                navigationSubsPlan.setVisibility(View.GONE);
                                txtSubscriptionPlanSetupLabel.setVisibility(View.GONE);
                            }

                            MenuItem nav_manage_documents = navigationView.getMenu().findItem(R.id.nav_managedocument);
                            out.println("~~~~~CUSTOMER_MANAGE_DOCUMENT~~~>> " + spCommon.getInt(Utils.SP_COMMON_CUSTOMER_MANAGE_DOCUMENT, 0));
                            if (getCustomerManageDocument == 1) {
                                nav_manage_documents.setVisible(true);
                            } else {
                                if (spCommon.getInt(Utils.SP_COMMON_CUSTOMER_MANAGE_DOCUMENT, 0) == 1) {
                                    nav_manage_documents.setVisible(true);
                                } else {
                                    nav_manage_documents.setVisible(false);
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        //callRefreshToken("viewprofile");
                    }
                    hideProgressDialog();
                } else if (response.code() == RESPONSECODE_401) {

                    // callRefreshToken("viewprofile");
                } else if (response.code() == RESPONSECODE_500) {
                    // callRefreshToken("viewprofile");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProfileModel> call, @NonNull Throwable t) {
                hideProgressDialog();
                Log.e(TAG, "onFailure: ~~~~view profile~~~~" + t.toString());
                call.cancel();
            }
        });
    }

    private void callPanicAddAdmin(String message) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("customer_id", userId);
        map.put("mode", "customer");
        map.put("location_link", message);

        showProgressDialog(MainActivity.this);
        Log.i("callPanicAddAdmin", "callPanicAddAdmin~~~" + map);

        Call<PanicAddAdminModel> call2 = apiInterface.doUpdateAddPanic(map);
        call2.enqueue(new Callback<PanicAddAdminModel>() {
            @Override
            public void onResponse(@NonNull Call<PanicAddAdminModel> call, @NonNull Response<PanicAddAdminModel> response) {
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Panic Call details updated successfully.")) {
                        hideProgressDialog();
                        String panicAddDate = Objects.requireNonNull(response.body()).getPanicAddAdminDataModel().getPanicDate();
                        String panicAddId = String.valueOf(Objects.requireNonNull(response.body()).getPanicAddAdminDataModel().getId());

                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<PanicAddAdminModel> call, @NonNull Throwable t) {
                call.cancel();
            }
        });
    }

    public void removeFragment(String Tag) {

        Utils.CURRENT_TAG = null;
        //frameClickable(false);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag(Tag)).commit();

    }

    /*-------------------
     *
     * *Logout
     *
     *--------------------- */
    public void showLogoutAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);

        Typeface tf = Typeface.createFromAsset(MainActivity.this.getAssets(),
                getString(R.string.App_Font));

        BaseActivity.CustomTFSpan tfSpan = new BaseActivity.CustomTFSpan(tf);
        SpannableString spannableString = new SpannableString(getString(R.string.logout));
        spannableString.setSpan(tfSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString spannableStrings = new SpannableString(getString(R.string.are_u_sure_logout));
        spannableStrings.setSpan(tfSpan, 0, spannableStrings.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString spannableStringss = new SpannableString(getString(R.string.yes));
        spannableStringss.setSpan(tfSpan, 0, spannableStringss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString spannableStringsss = new SpannableString(getString(R.string.no));
        spannableStringsss.setSpan(tfSpan, 0, spannableStringsss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setTitle(spannableString);

        builder.setMessage(spannableStrings);
        builder.setPositiveButton(spannableStringss,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // dialog.dismiss();
                        CallLogout(dialog);
                        // clearSPmodePrivate();

                    }
                });
        builder.setNegativeButton(spannableStringsss,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                });


        builder.show();
    }

    private void CallLogout(DialogInterface dialog) {
        userAccessToken = spCommon.getString(Utils.SP_USER_ACCESSTOKEN, null);
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", userId);
        map.put("token", userAccessToken);
        Log.i("CallLogout", "CallLogout~~~" + map);
        showProgressDialog(MainActivity.this);

        Call<LogoutModel> call2 = apiInterface.doGetLogoutDetails(map);
        call2.enqueue(new Callback<LogoutModel>() {
            @Override
            public void onResponse(@NonNull Call<LogoutModel> call, @NonNull Response<LogoutModel> response) {
                hideProgressDialog();
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Successfully logged out")) {
                        clearSPmodePrivate();
                    } else {
                        //   callRefreshToken("Logout");
                        clearSPmodePrivate();
                    }
                } else if (response.code() == RESPONSECODE_401) {
                    //  callRefreshToken("Logout");
                    clearSPmodePrivate();
                } else if (response.code() == RESPONSECODE_500) {
                    clearSPmodePrivate();
                }

                dialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<LogoutModel> call, @NonNull Throwable t) {
                call.cancel();
            }
        });
    }

    private void clearSPmodePrivate() {
        SharedPreferences preferences = getSharedPreferences(Utils.MY_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        // deleteCache(MainActivity.this);
        Intent logout = new Intent(MainActivity.this, HomeActivity.class);
        logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(logout);
        finish();
    }


    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            this.finishAffinity();
            int pid = android.os.Process.myPid();
            android.os.Process.killProcess(pid);
            System.exit(0);
            finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.back_pressed, Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    public String convertCurrencyPrice(String price, String commonPrice) {

        //currencyCommonPrice = spCurrency.getString(Utils.SP_CURRENCY_COMMON_VALUES, "1");
        if (commonPrice.equals("1")) {
            currencyCommonPrice = spCurrency.getString(Utils.SP_CURRENCY_COMMON_VALUES, "1");
        } else {
            currencyCommonPrice = commonPrice;
        }

        if (currencyCommonPrice != null) {
            Log.i(TAG, "convertCurrencyPrice: -->" + currencyCommonPrice);
            Double multiplePrice = Double.parseDouble(currencyCommonPrice) * Double.parseDouble(price);
            roundedNumber = DecimalUtils.round(multiplePrice, 2);
        } else {
            roundedNumber = 0;
        }
        return String.valueOf(roundedNumber);
    }

    private void callCurrencyUpdate(String curId, String curShortName, String from) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("id", userId);
        map.put("currency", curShortName);
        Log.i("CallUpdateDetails", "~~~~" + map);
        showProgressDialog(MainActivity.this);

        Call<ProfileUpdateModel> call2 = apiInterface.doGetprofileUpdateDetails("Bearer " + userAccessToken, map);
        call2.enqueue(new Callback<ProfileUpdateModel>() {
            @Override
            public void onResponse(@NonNull Call<ProfileUpdateModel> call, @NonNull Response<ProfileUpdateModel> response) {

                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Customer updated successfully.")) {
                        hideProgressDialog();

                        String strCurrencySymbol = Objects.requireNonNull(response.body()).getProfileUpdateData().getCurrencySymbol();
                        String strCurrenc = Objects.requireNonNull(response.body()).getProfileUpdateData().getCurrency();

                        System.out.println("strCurrenc-->" + strCurrenc.toString());
                        searchCurrencyPrice(strCurrenc.toString());

                        DatabaseReference refs = FirebaseDatabase.getInstance().getReference();
                        refs.child(Utils.FIREBASE_USER_HEADER).child(userId).child("currency").setValue(strCurrenc + ":" + strCurrencySymbol);
                        getStateCurrency.putString(Utils.SP_CURRENCY, strCurrenc);
                        getStateCurrency.putString(Utils.SP_CURRENCY_SYMBOL, strCurrencySymbol);
                        getStateCurrency.apply();

                        if (!from.equals("view")) {
                            Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(mainActivityIntent);
                        }

                    } else {
                        Toast.makeText(MainActivity.this, R.string.try_again, Toast.LENGTH_SHORT).show();
                        hideProgressDialog();
                    }
                } else if (response.code() == RESPONSECODE_401) {
                    hideProgressDialog();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProfileUpdateModel> call, @NonNull Throwable t) {
                // CommonUtil.showRetrofitError(LoginActivity.this, error);
                hideProgressDialog();
                call.cancel();
            }
        });
    }

    private void CallUpdateDetails(String strlang, String strlangName, String
            strlangCode, String key) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("id", userId);
        map.put("language", strlang);
        Log.i("CallUpdateDetails", "~~~~" + map);

        if (!key.equals("view")) {
            showProgressDialog(MainActivity.this);
        }

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<ProfileUpdateModel> call2 = apiInterface.doGetprofileUpdateDetails("Bearer " + userAccessToken, map);
        call2.enqueue(new Callback<ProfileUpdateModel>() {
            @Override
            public void onResponse(@NonNull Call<ProfileUpdateModel> call, @NonNull Response<ProfileUpdateModel> response) {

                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {

                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Customer updated successfully.")) {

                        try {

                            String getChangeLang = Objects.requireNonNull(response.body()).getProfileUpdateData().getLanguage();

                            DatabaseReference refs = FirebaseDatabase.getInstance().getReference();
                            refs.child(Utils.FIREBASE_USER_HEADER).child(userId).child("language").setValue(getChangeLang);

                            if (key.equals("view")) {

                                getStateCurrency.putString(Utils.SP_LANGUAGE, strlangName);
                                getStateCurrency.putString(Utils.SP_LANGUAGE_SHORT_NAME, "en");
                                getStateCurrency.putString(Utils.SP_LANGUAGE_COUNTRY, "US");
                                getStateCurrency.apply();

                            } else if (key.equals("langDialog")) {

                                hideProgressDialog();

                                if (strlangCode != null) {
                                    if (!strlangCode.equals("null")) {
                                        if (!strlangCode.equals("")) {
                                            callSwitch();
                                        } else {
                                            callSwitch();
                                        }
                                    } else {
                                        callSwitch();
                                    }
                                } else {
                                    callSwitch();
                                }

                                hideProgressDialog();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            hideProgressDialog();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, R.string.try_again, Toast.LENGTH_SHORT).show();
                        hideProgressDialog();
                    }
                } else if (response.code() == RESPONSECODE_401) {
                    hideProgressDialog();
                }
            }

            private void callSwitch() {
                switch (strlangName) {
                    case "Sinhala":
                        onSuccessEditProfile(strlangName, "si", "LK", "normal");
                        break;
                    case "Finnish":
                        onSuccessEditProfile(strlangName, "fi", "FI", "normal");
                        break;
                    case "Arabic":
                    case "عربى":
                    case "عربى-Arabic":
                    case "عربى - Arabic":
                    case "Ar":
                    case "ar":
                        onSuccessEditProfile(strlangName, "ar", "AE", "normal");
                        break;
                    case "English":
                    default:
                        onSuccessEditProfile(strlangName, "en", "US", "normal");
                        break;
                }

            }

            @Override
            public void onFailure(@NonNull Call<ProfileUpdateModel> call, @NonNull Throwable t) {
                // CommonUtil.showRetrofitError(LoginActivity.this, error);
                hideProgressDialog();
                call.cancel();
            }
        });
    }

    public void onSuccessEditProfile(String langname, String code, String country, String key) {

        LocaleListCompat appLocale = LocaleListCompat.forLanguageTags(code + "-" + country);
        // Call this on the main thread as it may require Activity.restart()
        AppCompatDelegate.setApplicationLocales(appLocale);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            setLanguage(code);
            getStateCurrency.putString(Utils.SP_LANGUAGE, langname);
            getStateCurrency.putString(Utils.SP_LANGUAGE_SHORT_NAME, code);
            getStateCurrency.apply();
        } else {
            setLanguageBelow23(code, country);
            getStateCurrency.putString(Utils.SP_LANGUAGE, langname);
            getStateCurrency.putString(Utils.SP_LANGUAGE_SHORT_NAME, code);
            getStateCurrency.putString(Utils.SP_LANGUAGE_COUNTRY, country);
            getStateCurrency.apply();
        }

       /* Intent intent = getIntent();
        finish();
        startActivity(intent);*/

       /* Intent news = new Intent(MainActivity.this,LaunchActivity.class);
        news.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        news.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //news.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(news);
        MainActivity.this.finish();*/

        if (mMap != null) {
            mMap.clear();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // MainActivity.this.recreate();
            MainActivity.this.startActivity(new Intent(MainActivity.this.getApplicationContext(), MainActivity.class));
            MainActivity.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            MainActivity.this.finish();
        } else {
            MainActivity.this.finish();
            MainActivity.this.startActivity(MainActivity.this.getIntent());
            // MainActivity.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onRestart: ~~~~~~~~~~~~~~~~~~~~~~~");
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    Locale getCurrentLocale(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return context.getResources().getConfiguration().getLocales().get(0);
        } else {
            //noinspection deprecation
            return context.getResources().getConfiguration().locale;
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
            String gettingType = resultData.getString(Utils.RESULT_DATA);
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

    @SuppressLint("NewApi")
    static class MyOperator<T> implements UnaryOperator<T> {
        T varc1;

        public T apply(T varc) {
            return varc1;
        }
    }

    private void dialogDatePickerLight(String from, String mDate) {
        DecimalFormat nFormat = new DecimalFormat("####");
        Calendar cur_calender = Calendar.getInstance();
        // cur_calender.add(Calendar.DAY_OF_YEAR, 1);
        if (from.equals("end") && mDate.equals("nDay")) {
            cur_calender.add(Calendar.DATE, 1);
        } else if(from.equals("end") && mDate.equals("tmDay")) {
            cur_calender.add(Calendar.DATE, Integer.parseInt(nFormat.format(difference_In_Days)));
        }

        DatePickerDialog datePicker = DatePickerDialog.newInstance(
                (view, year, monthOfYear, dayOfMonth) -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    long date_ship_millis = calendar.getTimeInMillis();

                    SimpleDateFormat newFormat_Api = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    //String selectedDate_api = newFormat_Api.format(new Date(date_ship_millis));

                    SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    SimpleDateFormat newFormatView = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    SimpleDateFormat newFormatViewEnd = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    selectedDate = newFormat.format(new Date(date_ship_millis));
                    if (from.equals("start")) {
                        txtPickupDate.setText(selectedDate);
                        strdate = newFormat_Api.format(new Date(date_ship_millis));
                        strdateView = newFormatView.format(new Date(date_ship_millis));

                        out.println("startDate"+strdateView);
                    } else {
                        strdate_end = newFormat_Api.format(new Date(date_ship_millis));
                        strdate_end_view = newFormatViewEnd.format(new Date(date_ship_millis));
                        txtPickupDate_End.setText(selectedDate);
                    }

                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
                    Date date1;
                    try {
                        date1 = dateformat.parse(selectedDate);
                        DateFormat dayFormate = new SimpleDateFormat("EEEE", Locale.US);
                        String dayFromDate = null;
                        if (date1 != null) {
                            dayFromDate = dayFormate.format(date1);
                        }

                        selectedDay = dayFromDate;

                        Log.d("day text-->", "----------:: " + dayFromDate);
                        Log.i("day text-->", "onDateSet: " + dayFromDate);

                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    Log.i(TAG, "onDateSet: -->" + selectedDate);
                    //    tvSelectTime.setText("");
                },
                cur_calender.get(Calendar.YEAR),
                cur_calender.get(Calendar.MONTH),
                cur_calender.get(Calendar.DAY_OF_MONTH)
        );
        //set dark light
        datePicker.setThemeDark(false);
        datePicker.setLocale(Locale.US);
        datePicker.setAccentColor(getResources().getColor(R.color.colorPrimary));
        datePicker.setMinDate(cur_calender);
        datePicker.show(MainActivity.this.getSupportFragmentManager(), "Datepickerdialog");
    }

    private void dialogTimePickerLight(String from) {

        Calendar cur_calender = Calendar.getInstance();
        int timeOfTheDay = cur_calender.get(Calendar.MINUTE);
        //cur_calender.add(Calendar.DAY_OF_YEAR, 1);
        out.println("currentMin"+ timeOfTheDay);
        /*if (timeOfTheDay > 1) {
            out.println("moveToNextHr");
            cur_calender.add(Calendar.HOUR_OF_DAY, 1);
        }*/

        //SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        String formattedDate = df.format(cur_calender.getTime());
        System.out.println("formattedDate date==" + formattedDate);

        TimePickerDialog timePicker = TimePickerDialog.newInstance((view, hourOfDay, minute, second) -> {

            System.out.println("TimePickerDialog==>>>hourOfDay--->>>" + hourOfDay);
            System.out.println("TimePickerDialog==>>>minute--->>>" + minute);
            System.out.println("TimePickerDialog==>>>second--->>>" + second);
            System.out.println("bookLaterTime"+bookGuardMinTime.toString());

            selectedHour = hourOfDay + 1;

            Calendar calendar = Calendar.getInstance();

            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.AM_PM, calendar.get(Calendar.AM_PM));
            long time_millis = calendar.getTimeInMillis();

//            SimpleDateFormat newFormat_Api = new SimpleDateFormat("hh:mm a", Locale.US);
            //SimpleDateFormat newFormat = new SimpleDateFormat("HH:mm a", Locale.US); // 18:25 PM
            SimpleDateFormat newFormat = new SimpleDateFormat("HH:mm", Locale.US); //18:25
            selectedTime = newFormat.format(new Date(time_millis));
            if (from.equals("start")) {
                txtPickupTime.setText(selectedTime);
                strtime = newFormat.format(new Date(time_millis));
                out.println("sTime::"+strtime);
            } else {
                txtPickupTime_End.setText(selectedTime);
                strtime_end = newFormat.format(new Date(time_millis));
            }

            Log.i(TAG, "onTimeSet: -->" + selectedTime);

        }, cur_calender.get(Calendar.HOUR_OF_DAY), cur_calender.get(Calendar.MINUTE), true);
        //set dark light

        timePicker.setThemeDark(false);
        timePicker.setLocale(Locale.US);
        timePicker.setTimeInterval(1, 30);
        timePicker.setAccentColor(getResources().getColor(R.color.colorPrimary));

        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, 30);

        Log.i(TAG, "dialogTimePickerLight: int the list--->" + txtPickupDate.getText().toString() + "~~~" + formattedDate);
        if (from.equals("start")) {
            if (txtPickupDate.getText().toString().equals(getResources().getString(R.string.scheduled_trip_select_start_time))) {
                Toast.makeText(MainActivity.this, R.string.scheduled_trip_select_start_date, Toast.LENGTH_SHORT).show();
            } else {
                if (txtPickupDate.getText().toString().trim().equals(formattedDate)) {
                    int curMin = c.get(Calendar.MINUTE);
                    out.println("curMin"+curMin);
                    if (curMin < 30) { // need to show 30
                        int diff = 30 - curMin;
                        int finalMin = curMin + diff;
                        c.set(Calendar.MINUTE, finalMin);
                        out.println("curMinBelow"+curMin);
                        out.println("curMinBelow1"+finalMin);
                        timePicker.setMinTime(c.get(Calendar.HOUR_OF_DAY),30,0);
                    } else { // need to show 00
                        int diff = 60 - curMin;
                        int finalMin = curMin + diff;
                        c.set(Calendar.MINUTE, finalMin);
                        out.println("curMinAbove"+curMin);
                        timePicker.setMinTime(c.get(Calendar.HOUR_OF_DAY), 0,0);
                    }
                    Log.i(TAG, "dialogTimePickerLight: in the list ---->" + roundup15(c.get(Calendar.MINUTE)));

                    timePicker.show(MainActivity.this.getSupportFragmentManager(), "Timepickerdialog");
                } else {
                    timePicker.setMinTime(0, roundup15(c.get(Calendar.MINUTE)), 0);
                    timePicker.show(MainActivity.this.getSupportFragmentManager(), "Timepickerdialog");
                }
            }
        } else {
            if (txtPickupDate_End.getText().toString().equals(getResources().getString(R.string.scheduled_trip_select_end_time))) {
                Toast.makeText(MainActivity.this, R.string.scheduled_trip_select_end_date, Toast.LENGTH_SHORT).show();
            } else {
                if (txtPickupDate_End.getText().toString().trim().equals(formattedDate)) {
                    int curMin = c.get(Calendar.MINUTE);
                    out.println("curMin"+curMin);
                    if (curMin < 30) { // need to show 30
                        int diff = 30 - curMin;
                        int finalMin = curMin + diff;
                        c.set(Calendar.MINUTE, finalMin);
                        out.println("curMinBelow"+curMin);
                        out.println("curMinBelow1"+finalMin);
                        timePicker.setMinTime(c.get(Calendar.HOUR_OF_DAY),30,0);
                    } else { // need to show 00
                        int diff = 60 - curMin;
                        int finalMin = curMin + diff;
                        c.set(Calendar.MINUTE, finalMin);
                        out.println("curMinAbove"+curMin);
                        timePicker.setMinTime(c.get(Calendar.HOUR_OF_DAY), 0,0);
                    }
                    timePicker.show(MainActivity.this.getSupportFragmentManager(), "Timepickerdialog");
                } else {
                    timePicker.setMinTime(0, roundup15(c.get(Calendar.MINUTE)), 0);
                    timePicker.show(MainActivity.this.getSupportFragmentManager(), "Timepickerdialog");
                }
            }
        }
    }

    /*public int roundup15(int n) {
        return ((n - 1) / 15 + 1) * 15;
    }*/

    public int roundup15(int n) {
        return ((n - 1) / 15 + 1) * 15;
    }

    private void callSlideBannerImage(String key) {

        userAccessToken = spCommon.getString(Utils.SP_USER_ACCESSTOKEN, null);
        HashMap<String, Object> map = new HashMap<>();
        map.put("mode", key);
        Log.i("callSlideBannerImage", "~~~~" + map);
        Call<BannerModel> call = apiInterface.doSlideBannerImages("Bearer " + userAccessToken, map);
        call.enqueue(new Callback<BannerModel>() {
            @Override
            public void onResponse(@NonNull Call<BannerModel> call, @NonNull Response<BannerModel> response) {
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    if (response.body() != null && response.body().getMessage().equals("Success")) {
                        bannerData = response.body().getData();
                        if (bannerData != null) {
                            if (bannerData.size() != 0) {
                                try {
                                    initComponent();
                                } catch (Exception e) {
                                    Log.i("", "banner exception --->" + e);
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
                //Toast.makeText(MainActivity.this, R.string.try_again, Toast.LENGTH_SHORT).show();
                System.out.println("~~~~Banner~~~onFailure~~~" + t);
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
                dots[i] = new ImageView(MainActivity.this);
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
            Log.i("dgssdf", "addBottomDots: crash in addbottom -- >" + e.toString());
        }
    }

    private void startAutoSlider(final int count) {
        Log.i("dfsf", "startAutoSlider: runnable--->" + runnable);
        runnable = () -> {
            int pos = viewPager.getCurrentItem();
            pos = pos + 1;
            if (pos >= count) pos = 0;
            viewPager.setCurrentItem(pos);
            handler.postDelayed(runnable, 5000);
        };
        handler.postDelayed(runnable, 5000);
    }

    private static class AdapterImageSlider extends PagerAdapter {

        private Activity act;
        private List<Image> items;

        private AdapterImageSlider.OnItemClickListener onItemClickListener;

        private interface OnItemClickListener {
            void onItemClick(View view, Image obj);
        }

        public void setOnItemClickListener(AdapterImageSlider.OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        // constructor
        private AdapterImageSlider(Activity activity, List<Image> items) {
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
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
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

            //  Log.i("image", "instantiateItem: "+o.image);
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

    public static class Image {
        public String name;
        public String caption;
        public String image;
        public Integer counter = null;

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

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                    if (dataSnapshot.child(Utils.FIREBASE_ACCEPT_STATUS).getValue() != null) {
                        String status = dataSnapshot.child(Utils.FIREBASE_ACCEPT_STATUS).getValue().toString();
                        Log.i(TAG, "onDataChange in getrequestAcceptLisetner: -->" + status);

                        switch (status) {
                            case "yes":

                                try {
                                    if (ProviderListActivity.countDownTimer != null) {
                                        ProviderListActivity.countDownTimer.cancel();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                bookingId = dataSnapshot.child(Utils.FIREBASE_ACCEPT_STATUS).getValue().toString();

                                if (dailogAcceptDriver.getVisibility() == View.GONE) {
                                    dailogAcceptDriver.setVisibility(View.VISIBLE);
                                }


                                BaseActivity.setVisibility(rlSuccess, true);
                                BaseActivity.setVisibility(rlSuccess1, false);

                                txtViewOnGoingJobs.setOnClickListener(v -> {

                                    if (dailogAcceptDriver.getVisibility() == View.VISIBLE) {
                                        dailogAcceptDriver.setVisibility(View.GONE);
                                    }

                                    removeCustomerAcceptStatus();

                                    Utils.switchContent(R.id.frame_mainactivity, //frame_mainactivity
                                            Utils.FRAGMENT_ONGOINGJOBS,
                                            MainActivity.this, null,
                                            Utils.AnimationType.SLIDE_IN_SLIDE_OUT);
                                });

                                break;
                            case "2":

                                try {
                                    if (ProviderListActivity.countDownTimer != null) {
                                        ProviderListActivity.countDownTimer.cancel();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if (dailogAcceptDriver.getVisibility() == View.GONE) {
                                    dailogAcceptDriver.setVisibility(View.VISIBLE);
                                }


                                BaseActivity.setVisibility(rlSuccess, false);
                                BaseActivity.setVisibility(rlSuccess1, true);

                                txtCancel1.setOnClickListener(view -> {

                                    if (dailogAcceptDriver.getVisibility() == View.VISIBLE) {
                                        dailogAcceptDriver.setVisibility(View.GONE);
                                    }

                                    removeCustomerAcceptStatus();
                                });

                                break;
                            case "1":
                                System.out.println("requesting--->");
                                break;
                            case "3":

                                try {
                                    if (ProviderListActivity.countDownTimer != null) {
                                        ProviderListActivity.countDownTimer.cancel();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if (dailogAcceptDriver.getVisibility() == View.VISIBLE) {
                                    dailogAcceptDriver.setVisibility(View.GONE);
                                }
                                removeCustomerAcceptStatus();

                                break;
                        }

                    } else {

                        reference.child(Utils.FIREBASE_USER_HEADER).child(userId).
                                child(Utils.FIREBASE_TRIP_HEADER).child(Utils.FIREBASE_ACCEPT_STATUS).setValue("0");
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: ~~~~" + databaseError.toString());
            }
        });

    }

    private void afterAcceptRequest() {
        if (requestAcceptrDatabaseBRef != null) {
            requestAcceptrDatabaseBRef.removeEventListener(requestAccepValueEventListener);
            //booleanCheckStatusAccepted = false;
        }
        /*if (dialogRequesting!=null) {
            if (dialogRequesting.isShowing()) {
                dialogRequesting.dismiss();
            }
        }*/
    }

    public void removeCustomerAcceptStatus() {
        FirebaseDatabase.getInstance().getReference().child(Utils.FIREBASE_USER_HEADER).child(userId).
                child(Utils.FIREBASE_TRIP_HEADER).child(Utils.FIREBASE_ACCEPT_STATUS).setValue("0");
        FirebaseDatabase.getInstance().getReference().child(Utils.FIREBASE_USER_HEADER).child(userId).
                child(Utils.FIREBASE_TRIP_HEADER).child(Utils.FIREBASE_BOOKING_ID).setValue("0");
    }

}
