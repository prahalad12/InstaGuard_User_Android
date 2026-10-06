package com.instaguard.user.Fragments;

import static android.content.Context.MODE_PRIVATE;
import static com.instaguard.user.Activities.MainActivity.navigationSubsPlan;
import static com.instaguard.user.Activities.MainActivity.txtSubscriptionPlanSetupLabel;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_200;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_201;
import static java.lang.System.out;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Picture;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.instaguard.user.Activities.BaseActivity;
import com.instaguard.user.Activities.MainActivity;
import com.instaguard.user.Adapter.SubscriptionPlanAdapter;
import com.instaguard.user.BuildConfig;
import com.instaguard.user.CustomViews.CustomButton;
import com.instaguard.user.CustomViews.CustomTextView;
import com.instaguard.user.Helpers.Retrofit.APIClient;
import com.instaguard.user.Helpers.Retrofit.APIInterface;
import com.instaguard.user.Helpers.Utils;
import com.instaguard.user.Models.ProfileModel;
import com.instaguard.user.Models.Subscription.BuyPlanModel;
import com.instaguard.user.Models.Subscription.BuyPlanWebHookModel;
import com.instaguard.user.Models.Subscription.CancelAutoRenewPlanModel;
import com.instaguard.user.Models.Subscription.ListViewSubsData;
import com.instaguard.user.Models.Subscription.ListViewSubsModel;
import com.instaguard.user.Models.Subscription.UpdateSubsModel;
import com.instaguard.user.Models.Subscription.ViewSubsModel;
import com.instaguard.user.R;
import com.stripe.android.payments.paymentlauncher.PaymentLauncher;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubscriptionPlanFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, SubscriptionPlanAdapter.SuccessResponse {

    View view;

    ImageButton imgbtnBack;
    View llParentLytSubsPlan;
    CustomTextView txtSubsPlanViewLabel;
    CustomTextView txtSubsPlanTitleView;
    CustomTextView txtSubsPlanAmountView;
    CustomTextView txtSubsPlanExpireView;
    CustomTextView txtNoRecordsPlan;
    CustomTextView btnSkipSubsPlan;
    RelativeLayout rlParentLytSubsPlanViewLabel;
    ImageView ivInfoView;
    RecyclerView rcvSubscriptionPlan;
    CustomButton btnContinueBuySubsPlan;
    CustomButton btnViewHistory;
    CustomButton btnBuyPlan;

    CustomTextView btnEnableAutoRenew;
    CustomTextView btnDisableAutoRenew;

    View progressBar;
    SwipeRefreshLayout swipeRefreshLayout;
    View viewSubscPlanFocusView;


    String userId;
    String strCurrencySymbol;
    String userAccessToken;
    String strSubscriptionViewId = "0";
    String strSubscriptionHistoryId = "0";
    String strSubscriptionId = "0";

    SharedPreferences.Editor getUpdate;
    SharedPreferences spUpdate;
    SharedPreferences getspCurrency;

    APIInterface apiInterface;

    List<ListViewSubsData> getve;
    SubscriptionPlanAdapter adapter;

    String strFrom = "";


    View lytSubscriptionDetailsView;
    View rlParentLytSubscriptionDetailsView;
    CustomTextView txtTitleSubscriptionDetailsView;
    CustomTextView txtPeriodTypeSubscriptionDetailsView;
    CustomTextView txtMessageSubscriptionDetailsView;
    CustomTextView txtAmountSubscriptionDetailsView;

    CustomTextView txtPlanTypeSubscriptionDetailsView;
    CustomTextView txtFreeKMSubscriptionDetailsView;
    CustomTextView txtFreeJobRequestSubscriptionDetailsView;
    CustomTextView txtRemainingFreeJobRequestSubscriptionDetailsView;
    CustomButton txtBuyPlanSubscriptionDetailsView;

    ImageView ivCancelSubscriptionDetailsView;


    View lytGotLabel;
    View lytPaymentWebView;
    RelativeLayout rlParentLytPaymentWebView;
    ImageView imgClosePaymentWebView;
    WebView webView;
    String generateTokenAPI = "";
    String intentURL = "";
    WebSettings settings;

    private View lytSuccess;
    CustomTextView txtSuccessTitle;
    CustomButton btnSuccessOkay;
    RelativeLayout rlParentSuccess;
    ImageView imgFailed;
    ImageView imgSuccess;

    String STRIPE_SECRETKEY, STRIPE_PUBLISHKEY;
    String paymentId;
    private ActivityResultLauncher<Intent> paymentLauncherRegister;
    PaymentLauncher paymentLauncher;

    public SubscriptionPlanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_subscription_plan, container, false);

        getUpdate = requireActivity().getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE).edit();
        spUpdate = requireActivity().getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE);
        userId = spUpdate.getString(Utils.SP_USERID, null);

        getspCurrency = requireActivity().getSharedPreferences(Utils.PREFS_NAME_CURRENCY, MODE_PRIVATE);
        strCurrencySymbol = getspCurrency.getString(Utils.SP_CURRENCY_SYMBOL, null);
        if (strCurrencySymbol == null) {
            strCurrencySymbol = "CAD";
        }

        initViews();

        apiInterface = APIClient.getClient().create(APIInterface.class);

        SubscriptionPlanAdapter.successResponse = this;

        onRefresh();

        getCustomerDetailsFB();

        OnClick();

        if (getArguments() != null) {
            strFrom = getArguments().getString("from");
            if (strFrom.equals("login")) {
                if (btnSkipSubsPlan.getVisibility() == View.GONE) {
                    btnSkipSubsPlan.setVisibility(View.VISIBLE);
                }
                if (btnViewHistory.getVisibility() == View.VISIBLE) {
                    btnViewHistory.setVisibility(View.INVISIBLE);
                }
            } else if (strFrom.equals("main")) {
                if (btnSkipSubsPlan.getVisibility() == View.VISIBLE) {
                    btnSkipSubsPlan.setVisibility(View.GONE);
                }
                if (btnViewHistory.getVisibility() == View.GONE || btnViewHistory.getVisibility() == View.INVISIBLE) {
                    btnViewHistory.setVisibility(View.VISIBLE);
                }
            } else if (strFrom.equals("main_perks")) {
                if (btnSkipSubsPlan.getVisibility() == View.VISIBLE) {
                    btnSkipSubsPlan.setVisibility(View.GONE);
                }
                if (btnViewHistory.getVisibility() == View.GONE || btnViewHistory.getVisibility() == View.INVISIBLE) {
                    btnViewHistory.setVisibility(View.VISIBLE);
                }
            } else {
                if (btnSkipSubsPlan.getVisibility() == View.VISIBLE) {
                    btnSkipSubsPlan.setVisibility(View.GONE);
                }
                if (btnViewHistory.getVisibility() == View.VISIBLE) {
                    btnViewHistory.setVisibility(View.INVISIBLE);
                }
            }
        }


        callSubscriptionListApi();

        onBackButton(view);

        return view;
    }

    private void initViews() {

        imgbtnBack = view.findViewById(R.id.imgbtn_back);
        progressBar = view.findViewById(R.id.rl_progress_bar_cnf);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout_plan);
        swipeRefreshLayout.setOnRefreshListener(this);

        viewSubscPlanFocusView = view.findViewById(R.id.view_recycler_view);

        llParentLytSubsPlan = view.findViewById(R.id.ll_your_subscription_view_lyt);
        txtSubsPlanViewLabel = view.findViewById(R.id.txt_view_subscr_plan_label);
        rlParentLytSubsPlanViewLabel = view.findViewById(R.id.rl_top_subs_plan_lyt);

        txtSubsPlanTitleView = view.findViewById(R.id.txt_title_subs);
        txtSubsPlanAmountView = view.findViewById(R.id.txt_message);
        txtSubsPlanExpireView = view.findViewById(R.id.txt_date);

        ivInfoView = view.findViewById(R.id.img_check);

        txtNoRecordsPlan = view.findViewById(R.id.txt_no_records);
        rcvSubscriptionPlan = view.findViewById(R.id.rcv_subscription_plan);

        btnContinueBuySubsPlan = view.findViewById(R.id.btn_continue_subscr_plan);
        btnSkipSubsPlan = view.findViewById(R.id.txt_skip_subs_plan_header);
        btnViewHistory = view.findViewById(R.id.btn_continue_subscr_plan_view_history);
        btnBuyPlan = view.findViewById(R.id.btn_buy_subscr_plan_);

        btnEnableAutoRenew = view.findViewById(R.id.txt_enable_autorenew);
        btnDisableAutoRenew = view.findViewById(R.id.txt_disable_autorenew);

        lytPaymentWebView = view.findViewById(R.id.lyt_payment_paynow_view);
        lytGotLabel = view.findViewById(R.id.got_paynow);
        rlParentLytPaymentWebView = view.findViewById(R.id.rl_parent_lyt_payment_paynow);
        imgClosePaymentWebView = view.findViewById(R.id.img_close_paynow);
        webView = view.findViewById(R.id.webview);
        progressBar = view.findViewById(R.id.progressBar_paynow);

        lytSubscriptionDetailsView = view.findViewById(R.id.lyt_subscription_plan_view);
        rlParentLytSubscriptionDetailsView = view.findViewById(R.id.rl_parent_lyt_subs_details_view);
        txtTitleSubscriptionDetailsView = view.findViewById(R.id.txt_title_dialog);
        txtPeriodTypeSubscriptionDetailsView = view.findViewById(R.id.txt_period_type);
        txtMessageSubscriptionDetailsView = view.findViewById(R.id.txt_description);
        txtAmountSubscriptionDetailsView = view.findViewById(R.id.txt_amount);

        txtPlanTypeSubscriptionDetailsView = view.findViewById(R.id.txt_plan_type);
        txtFreeKMSubscriptionDetailsView = view.findViewById(R.id.txt_free_km);
        txtFreeJobRequestSubscriptionDetailsView = view.findViewById(R.id.txt_free_trips);
        txtRemainingFreeJobRequestSubscriptionDetailsView = view.findViewById(R.id.txt_remaining);
        txtBuyPlanSubscriptionDetailsView = view.findViewById(R.id.btn_continue_subscr_plan_details_view);

        ivCancelSubscriptionDetailsView = view.findViewById(R.id.imgbtn_back_details_view);

        parentSuccess(view);

    }

    private void parentSuccess(View view) {

        lytSuccess = view.findViewById(R.id.lyt_success);
        txtSuccessTitle = view.findViewById(R.id.txt_title);
        btnSuccessOkay = view.findViewById(R.id.btn_okay);
        rlParentSuccess = view.findViewById(R.id.rl_parent_success_alert);
        imgSuccess = view.findViewById(R.id.img_success);

        lytSuccess.setOnClickListener(v -> {
        });

        btnSuccessOkay.setOnClickListener(v -> {

            hideKeyBoard();

            onRefresh();

            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
            databaseRef.child(Utils.FIREBASE_USER_HEADER).child(userId)
                    .child("subs_buy_status").setValue("0");

            if (webView != null) {
                webView.clearCache(true);
                webView.clearFormData();
                webView.clearHistory();
                webView.clearSslPreferences();
            }
        });
    }


    private void OnClick() {

        imgbtnBack.setOnClickListener(v -> {
            hideKeyBoard();
            backButtonFunction();
        });

        progressBar.setOnClickListener(v -> {

        });

        viewSubscPlanFocusView.setOnClickListener(v -> {

        });

        ivInfoView.setOnClickListener(view -> {
            if (strSubscriptionViewId != null && !strSubscriptionViewId.equals("null")
                    && !strSubscriptionViewId.equals("") && !strSubscriptionViewId.equals("0")) {
                //callSubscriptionIdApi(strSubscriptionViewId);
                callViewProfileDetails("view");
            } else {
                Toast.makeText(requireActivity(), getResources().getString(R.string.subs_id_empty), Toast.LENGTH_SHORT).show();
            }
        });


        btnContinueBuySubsPlan.setOnClickListener(view -> {
            showAlertDialog(requireActivity(), "update");
        });

        btnSkipSubsPlan.setOnClickListener(view -> backButtonFunction());

        btnViewHistory.setOnClickListener(view1 -> {

            Bundle argss = new Bundle();
            argss.putString("from", strFrom);
            Utils.switchContent(R.id.frame_subs_plan,
                    Utils.FRAGMENT_SUBSCRIPTION_PLAN_HISTORY,
                    requireActivity(), argss,
                    Utils.AnimationType.SLIDE_IN_SLIDE_OUT);
        });

        btnBuyPlan.setOnClickListener(view -> callCheckBuyPlan_redirect_webview());

        btnEnableAutoRenew.setOnClickListener(view -> showAlertDialog(requireActivity(), "enable"));

        btnDisableAutoRenew.setOnClickListener(view -> showAlertDialog(requireActivity(), "disable"));

        rlParentLytPaymentWebView.setOnClickListener(view -> {
        });

        imgClosePaymentWebView.setOnClickListener(view -> {
            //WebView
            if (lytPaymentWebView.getVisibility() == View.VISIBLE) {
                lytPaymentWebView.setVisibility(View.GONE);
            }

            if (lytSubscriptionDetailsView.getVisibility() == View.VISIBLE) {
                lytSubscriptionDetailsView.setVisibility(View.GONE);
            }
        });
    }

    private void showProgressDialog_(FragmentActivity requireActivity) {
        if (requireActivity != null) {
            if (progressBar.getVisibility() == View.GONE) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }
    }

    private void hideProgressDialog_() {
        //if (requireActivity!=null){
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }
        // }
    }

    public void onBackButton(View view) {
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP
                    && keyCode == KeyEvent.KEYCODE_BACK) {
                backButtonFunction();
            }
            return true;
        });
    }

    public void backButtonFunction() {
        if (strFrom != null && !strFrom.equals("null") && !strFrom.equals("")) {
            if (strFrom.equals("login")) {
                Utils.CURRENT_TAG = null;
                Intent news = new Intent(requireActivity(), MainActivity.class);
                news.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                news.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(news);
                requireActivity().finish();
            } else if (strFrom.equals("main") || strFrom.equals("main_perks")) {
                if (llParentLytSubsPlan.getVisibility() == View.VISIBLE) {
                    Utils.CURRENT_TAG = null;
                    Intent news = new Intent(requireActivity(), MainActivity.class);
                    news.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    news.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(news);
                    requireActivity().finish();
                } else {
                    removeFragment(Utils.FRAGMENT_SUBSCRIPTION_PLAN);
                }
            } else {
                removeFragment(Utils.FRAGMENT_SUBSCRIPTION_PLAN);
            }
        } else {
            removeFragment(Utils.FRAGMENT_SUBSCRIPTION_PLAN);
        }
    }

    public void removeFragment(String Tag) {
        Utils.CURRENT_TAG = null;
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(Objects.requireNonNull(fragmentManager.findFragmentByTag(Tag))).commit();
    }

    public void hideKeyBoard() {
        // Check if no view has focus:
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            Objects.requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @SuppressLint("SetTextI18n")
    private void showDialog(String title, Integer period, String periodType, String message,
                            String amount, String planType, Integer freekm, Integer freejobreq, String strCurrencySymbol,
                            Integer strRemaingCount, String strSubscriptionId_) {

        try {

            /*Dialog dialogBuilder = new Dialog(requireActivity(), R.style.AppTheme);
            dialogBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
            dialogBuilder.setContentView(R.layout.dialog_subscription_plan_view);
            dialogBuilder.setCancelable(true);
            Objects.requireNonNull(dialogBuilder.getWindow()).setWindowAnimations(R.style.DialogAnimationFadeINOUT);

            CustomTextView txtTitle = dialogBuilder.findViewById(R.id.txt_title_dialog);
            CustomTextView txtPeriodType = dialogBuilder.findViewById(R.id.txt_period_type);
            CustomTextView txtMessage = dialogBuilder.findViewById(R.id.txt_description);
            CustomTextView txtAmount = dialogBuilder.findViewById(R.id.txt_amount);

            CustomTextView txtPlanType = dialogBuilder.findViewById(R.id.txt_plan_type);
            CustomTextView txtFreeKM = dialogBuilder.findViewById(R.id.txt_free_km);
            CustomTextView txtFreeJobRequest = dialogBuilder.findViewById(R.id.txt_free_trips);
            CustomTextView txtRemainingFreeJobRequest = dialogBuilder.findViewById(R.id.txt_remaining);
            CustomButton txtBuyPlan = dialogBuilder.findViewById(R.id.btn_continue_subscr_plan);

            ImageView ivCancel = dialogBuilder.findViewById(R.id.imgbtn_back);*/

            txtTitleSubscriptionDetailsView.setText(title);

            if (strRemaingCount != null) {
               // txtRemainingFreeJobRequestSubscriptionDetailsView.setVisibility(View.VISIBLE);
                txtRemainingFreeJobRequestSubscriptionDetailsView.setVisibility(View.GONE);
                if (strRemaingCount > 0) {
                    txtRemainingFreeJobRequestSubscriptionDetailsView.setText(getString(R.string.remaining) + " " + strRemaingCount + " " + getResources().getString(R.string.free_job_request));
                } else {
                    txtRemainingFreeJobRequestSubscriptionDetailsView.setText(getResources().getString(R.string.free_job_request_no));
                }
            } else {
                txtRemainingFreeJobRequestSubscriptionDetailsView.setVisibility(View.GONE);
            }

            txtFreeKMSubscriptionDetailsView.setText(freekm + " " + getResources().getString(R.string.free_km));

            txtFreeJobRequestSubscriptionDetailsView.setText(freejobreq + " " + getResources().getString(R.string.free_job_request));

            switch (planType) {
                case "2":
                case "2.0":
                    txtPlanTypeSubscriptionDetailsView.setText(getResources().getString(R.string.plan_applicable_for) + " " + getResources().getString(R.string.book_home_appointment));
                    txtFreeKMSubscriptionDetailsView.setVisibility(View.GONE);
                    txtFreeJobRequestSubscriptionDetailsView.setVisibility(View.VISIBLE);
                    break;
                case "0":
                case "0.0":
                case "1":
                case "1.0":
                    txtPlanTypeSubscriptionDetailsView.setText(getResources().getString(R.string.plan_applicable_for) + " " + getResources().getString(R.string.roadside_assitance));
                    txtFreeKMSubscriptionDetailsView.setVisibility(View.VISIBLE);
                    txtFreeJobRequestSubscriptionDetailsView.setVisibility(View.VISIBLE);
                    break;
                case "1,2":
                case "1.0,2.0":
                    txtPlanTypeSubscriptionDetailsView.setText(getResources().getString(R.string.plan_applicable_for) + " " + getResources().getString(R.string.both) +
                            "\n" + getResources().getString(R.string.roadside_assitance)
                            + " & " + getResources().getString(R.string.book_home_appointment));
                    txtFreeKMSubscriptionDetailsView.setVisibility(View.VISIBLE);
                    txtFreeJobRequestSubscriptionDetailsView.setVisibility(View.VISIBLE);
                    break;
                default:
                    txtPlanTypeSubscriptionDetailsView.setText("");
                    break;
            }


            switch (periodType) {
                case "0":
                case "0.0":
                    if (period <= 1) {
                        txtPeriodTypeSubscriptionDetailsView.setText(period + " " + getResources().getString(R.string.day));
                    } else {
                        txtPeriodTypeSubscriptionDetailsView.setText(period + " " + getResources().getString(R.string.days));
                    }
                    break;
                case "1":
                case "1.0":
                    if (period <= 1) {
                        txtPeriodTypeSubscriptionDetailsView.setText(period + " " + getResources().getString(R.string.month));
                    } else {
                        txtPeriodTypeSubscriptionDetailsView.setText(period + " " + getResources().getString(R.string.monthly));
                    }
                    break;
                case "2":
                case "2.0":
                    if (period <= 1) {
                        txtPeriodTypeSubscriptionDetailsView.setText(period + " " + getResources().getString(R.string.year));
                    } else {
                        txtPeriodTypeSubscriptionDetailsView.setText(period + " " + getResources().getString(R.string.yearly));
                    }
                    break;
                default:
                    txtPeriodTypeSubscriptionDetailsView.setText(periodType);
                    break;
            }

            txtAmountSubscriptionDetailsView.setText(strCurrencySymbol + " " + amount);

            txtMessageSubscriptionDetailsView.setText(Html.fromHtml(message));
            Linkify.addLinks(txtMessageSubscriptionDetailsView, Linkify.ALL);

            ivCancelSubscriptionDetailsView.setOnClickListener(v -> {
                if (lytSubscriptionDetailsView.getVisibility() == View.VISIBLE) {
                    lytSubscriptionDetailsView.setVisibility(View.GONE);
                }
            });

            rlParentLytSubscriptionDetailsView.setOnClickListener(v -> {
            });


            if (strSubscriptionId_ == null) {
                txtBuyPlanSubscriptionDetailsView.setVisibility(View.GONE);
            } else {
                txtBuyPlanSubscriptionDetailsView.setVisibility(View.VISIBLE);
            }

            txtBuyPlanSubscriptionDetailsView.setOnClickListener(v -> {
                        strSubscriptionId = strSubscriptionId_;
                        //  showAlertDialog(requireActivity(), "update");
                        if (strSubscriptionId != null && !strSubscriptionId.equals("null")
                                && !strSubscriptionId.equals("") && !strSubscriptionId.equals("0")) {
                            progressBar.setVisibility(View.VISIBLE);
                            callCheckBuyPlan();
                        } else {
                            Toast.makeText(requireActivity(), getResources().getString(R.string.subs_id_empty), Toast.LENGTH_SHORT).show();
                        }
                    }
            );

            if (lytSubscriptionDetailsView.getVisibility() == View.GONE) {
                lytSubscriptionDetailsView.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireActivity(), R.string.try_again, Toast.LENGTH_SHORT).show();
        }

    }

    private void callViewProfileDetails(String from) {

        userAccessToken = spUpdate.getString(Utils.SP_USER_ACCESSTOKEN, null);
        if (userAccessToken == null) {
            userAccessToken = "";
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", userId);
        map.put("token", userAccessToken);
        Log.i("callViewProfileDetails", "~~~~" + map);

        showProgressDialog_(requireActivity());

        Call<ProfileModel> call2 = apiInterface.doGetprofileDetails("Bearer " + userAccessToken, Utils.POST_ACCEPT_CONTENT_TYPE, map);

        call2.enqueue(new Callback<ProfileModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<ProfileModel> call, @NonNull Response<ProfileModel> response) {
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Profile listed successfully.")) {

                        try {

                            DatabaseReference refs = FirebaseDatabase.getInstance().getReference();

                            strSubscriptionViewId = Objects.requireNonNull(response.body()).
                                    getProfileData().getSubscriptionId().toString();
                            System.out.println("~~~~strSubscriptionId~~~~" + strSubscriptionViewId);

                            strSubscriptionHistoryId = Objects.requireNonNull(response.body()).
                                    getProfileData().getSubscriptionHistoryId().toString();
                            System.out.println("~~~~strSubscriptionHistoryId~~~~" + strSubscriptionHistoryId);

                            String strSubscriptionStatus = String.valueOf(Objects.requireNonNull(response.body()).
                                    getProfileData().getSubscriptionStatus());
                            System.out.println("~~~~strSubscriptionStatus~~~~" + strSubscriptionStatus);

                            String strSubscriptionName = String.valueOf(Objects.requireNonNull(response.body()).
                                    getProfileData().getSubscriptionName());
                            String strSubscriptionAmount = String.valueOf(Objects.requireNonNull(response.body()).
                                    getProfileData().getSubscriptionAmount());
                            String strSubscriptionExpire = String.valueOf(Objects.requireNonNull(response.body()).
                                    getProfileData().getSubscriptionExpiry());


                            /*Integer getDiscountPercentage = response.body().
                                    getProfileData().getDiscountPercentage();

                            int strRemaingCount;

                            if (response.body().getProfileData().getRemeaningJob() != null
                                    && !response.body().getProfileData().getRemeaningJob().toString().equals("null")
                                    && !response.body().getProfileData().getRemeaningJob().toString().equals("")) {
                                strRemaingCount = response.body().getProfileData().getRemeaningJob();
                            } else {
                                strRemaingCount = 0;
                            }*/


                            refs.child(Utils.FIREBASE_USER_HEADER).child(userId).child("remaining_job").setValue(String.valueOf("0"));

                            /*String strGetTowServiceId = String.valueOf(Objects.requireNonNull(response.body()).
                                    getProfileData().getTowServiceId());
                            String strGetAppointmentServiceId = String.valueOf(Objects.requireNonNull(response.body()).
                                    getProfileData().getAppointmentServiceId());
                            String strFreeAppointmentService = String.valueOf(Objects.requireNonNull(response.body()).
                                    getProfileData().getFreeAppointmentService());*/

                            getUpdate.putString(Utils.SP_USER_SUBSCRIPTION_CODE, strSubscriptionName);
                            getUpdate.putString(Utils.SP_USER_SUBSCRIPTION_CODE_ID, strSubscriptionViewId);
                            /*getUpdate.putInt(Utils.SP_USER_SUBSCRIPTION_CODE_DISCOUNT_PERCENTAGE, getDiscountPercentage);
                            getUpdate.putInt(Utils.SP_USER_SUBSCRIPTION_REMAINING_COUNT, strRemaingCount);
                            getUpdate.putString(Utils.SP_USER_SUBSCRIPTION_SERVICE_IDS, strGetTowServiceId);
                            getUpdate.putString(Utils.SP_USER_SUBSCRIPTION_APPOINTMENT_SERVICE_IDS, strGetAppointmentServiceId);
                            getUpdate.putString(Utils.SP_USER_SUBSCRIPTION_FREE_APPOINTMENT_SERVICE, strFreeAppointmentService);*/
                            getUpdate.apply();

                            if (!strSubscriptionName.equals("null") && !strSubscriptionName.equals("")) {
                                txtSubsPlanTitleView.setText(strSubscriptionName);
                            } else {
                                txtSubsPlanTitleView.setText("--");
                            }

                            if (!strSubscriptionAmount.equals("null") && !strSubscriptionAmount.equals("")) {
                                txtSubsPlanAmountView.setText(strCurrencySymbol + " " + strSubscriptionAmount);
                            } else {
                                txtSubsPlanAmountView.setText("--");
                            }

                            if (!strSubscriptionExpire.equals("null") && !strSubscriptionExpire.equals("")) {
                                txtSubsPlanExpireView.setText(getResources().getString(R.string.expire_on) + " " + strSubscriptionExpire);
                            } else {
                                txtSubsPlanExpireView.setText("--");
                            }

                            int strAutoRenew;
                            if (response.body().getProfileData().getAutoRenew() != null
                                    && !response.body().getProfileData().getAutoRenew().toString().equals("null")
                                    && !response.body().getProfileData().getAutoRenew().toString().equals("")) {
                                strAutoRenew = response.body().getProfileData().getAutoRenew();
                            } else {
                                strAutoRenew = 0;
                            }

                            if (strAutoRenew == 0) {
                                btnEnableAutoRenew.setVisibility(View.VISIBLE);
                                btnDisableAutoRenew.setVisibility(View.GONE);
                            } else if (strAutoRenew == 1) {
                                btnEnableAutoRenew.setVisibility(View.GONE);
                                btnDisableAutoRenew.setVisibility(View.VISIBLE);
                            }

                            if (!strSubscriptionStatus.equals("null") && !strSubscriptionStatus.equals("")) {
                                switch (strSubscriptionStatus) {
                                    case "0": //Default
                                    case "0.0": //Default

                                        if (btnBuyPlan.getVisibility() == View.GONE) {
                                            // btnBuyPlan.setVisibility(View.VISIBLE);
                                            btnBuyPlan.setVisibility(View.GONE);
                                        }

                                        if (llParentLytSubsPlan.getVisibility() == View.VISIBLE) {
                                            llParentLytSubsPlan.setVisibility(View.GONE);
                                        }
                                        txtSubsPlanViewLabel.setText(getResources().getString(R.string.your_subs_plan));

                                        if (viewSubscPlanFocusView.getVisibility() == View.VISIBLE) {
                                            viewSubscPlanFocusView.setVisibility(View.GONE);
                                        }

                                        txtSubscriptionPlanSetupLabel.setVisibility(View.GONE);
                                        txtSubscriptionPlanSetupLabel.setText(getResources().getString(R.string.your_subs_plan_choose));
                                      //  txtSubscriptionPlanSetupLabel.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                                      //  txtSubscriptionPlanSetupLabel.setTextColor(getResources().getColor(R.color.colorPrimary));

                                        navigationSubsPlan.setVisibility(View.VISIBLE);
                                        navigationSubsPlan.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray_very_light)));
                                        navigationSubsPlan.setText(getResources().getString(R.string.plan_choose));


                                        break;
                                    case "1": //Active
                                    case "1.0": //Active

                                        txtSubsPlanViewLabel.setText(getResources().getString(R.string.your_subs_plan_active));
                                        if (llParentLytSubsPlan.getVisibility() == View.GONE) {
                                            llParentLytSubsPlan.setVisibility(View.VISIBLE);
                                            rcvSubscriptionPlan.setVisibility(View.GONE);
                                            btnContinueBuySubsPlan.setVisibility(View.GONE);
                                        }
                                        llParentLytSubsPlan.setAlpha(1f);

                                        if (viewSubscPlanFocusView.getVisibility() == View.GONE) {
                                            viewSubscPlanFocusView.setVisibility(View.VISIBLE);
                                        }

                                        if (btnBuyPlan.getVisibility() == View.VISIBLE) {
                                            btnBuyPlan.setVisibility(View.GONE);
                                        }

                                        //txtSubscriptionPlanLabel.setVisibility(View.GONE);
                                        txtSubscriptionPlanSetupLabel.setVisibility(View.GONE);
                                        txtSubscriptionPlanSetupLabel.setText(getResources().getString(R.string.your_subs_plan_active));
                                       // txtSubscriptionPlanSetupLabel.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.holo_green_dark)));
                                       // txtSubscriptionPlanSetupLabel.setTextColor(getResources().getColor(R.color.white));

                                        navigationSubsPlan.setVisibility(View.VISIBLE);
                                        navigationSubsPlan.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.holo_green_dark)));
                                        navigationSubsPlan.setText(getResources().getString(R.string.plan_active));


                                        break;
                                    case "2": //Expired
                                    case "2.0": //Expired

                                        txtSubsPlanViewLabel.setText(getResources().getString(R.string.your_subs_plan_expiry));

                                        if (llParentLytSubsPlan.getVisibility() == View.GONE) {
                                            llParentLytSubsPlan.setVisibility(View.VISIBLE);
                                            rcvSubscriptionPlan.setVisibility(View.GONE);
                                            btnContinueBuySubsPlan.setVisibility(View.GONE);
                                        }
                                        llParentLytSubsPlan.setAlpha(0.5f);

                                        if (viewSubscPlanFocusView.getVisibility() == View.VISIBLE) {
                                            viewSubscPlanFocusView.setVisibility(View.GONE);
                                        }

                                        if (btnBuyPlan.getVisibility() == View.GONE) {
                                            // btnBuyPlan.setVisibility(View.VISIBLE);
                                            btnBuyPlan.setVisibility(View.GONE);
                                        }

                                        txtSubscriptionPlanSetupLabel.setVisibility(View.VISIBLE);
                                        txtSubscriptionPlanSetupLabel.setText(getResources().getString(R.string.your_subs_plan_expiry));
                                        //txtSubscriptionPlanSetupLabel.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.holo_red_dark)));
                                        //txtSubscriptionPlanSetupLabel.setTextColor(getResources().getColor(R.color.white));

                                        navigationSubsPlan.setVisibility(View.VISIBLE);
                                        navigationSubsPlan.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.holo_red_dark)));
                                        navigationSubsPlan.setText(getResources().getString(R.string.plan_expired));


                                        break;
                                }
                            } else {
                                if (llParentLytSubsPlan.getVisibility() == View.VISIBLE) {
                                    llParentLytSubsPlan.setVisibility(View.GONE);
                                }

                                if (viewSubscPlanFocusView.getVisibility() == View.VISIBLE) {
                                    viewSubscPlanFocusView.setVisibility(View.GONE);
                                }

                                txtSubscriptionPlanSetupLabel.setVisibility(View.GONE);
                                navigationSubsPlan.setVisibility(View.GONE);
                            }


                            if (from.equals("view")) {

                                try {

                                    String strSubPlan;
                                    String strSubPlanDes;
                                    String strSubPlanAmount;
                                    String strSubPlanPeriod;
                                    String strSubPlanType;

                                    int strSubPlanFreeKM;
                                    int strSubPlanFreeJobRequest;
                                    int strSubPlanPeriodCount;

                                    if (response.body().getProfileData().getSubscriptionName() != null
                                            && !response.body().getProfileData().getSubscriptionName().equals("null")
                                            && !response.body().getProfileData().getSubscriptionName().equals("")) {
                                        strSubPlan = response.body().getProfileData().getSubscriptionName().toString();
                                    } else {
                                        strSubPlan = "";
                                    }

                                    if (response.body().getProfileData().getSubscriptionDescription() != null
                                            && !response.body().getProfileData().getSubscriptionDescription().toString().equals("null")
                                            && !response.body().getProfileData().getSubscriptionDescription().toString().equals("")) {
                                        strSubPlanDes = response.body().getProfileData().getSubscriptionDescription().toString();
                                    } else {
                                        strSubPlanDes = "";
                                    }

                                    if (response.body().getProfileData().getSubscriptionAmount() != null
                                            && !response.body().getProfileData().getSubscriptionAmount().toString().equals("null")
                                            && !response.body().getProfileData().getSubscriptionAmount().toString().equals("")) {
                                        strSubPlanAmount = response.body().getProfileData().getSubscriptionAmount().toString();
                                    } else {
                                        strSubPlanAmount = "";
                                    }

                                    if (response.body().getProfileData().getPeriodType() != null
                                            && !response.body().getProfileData().getPeriodType().toString().equals("null")
                                            && !response.body().getProfileData().getPeriodType().toString().equals("")) {
                                        strSubPlanPeriod = response.body().getProfileData().getPeriodType().toString();
                                    } else {
                                        strSubPlanPeriod = "";
                                    }

                                    if (response.body().getProfileData().getPeriod() != null
                                   /* && !response.body().getResult().getPeriod().toString().equals("null")
                                    && !response.body().getResult().getPeriod().toString().equals("")*/) {
                                        strSubPlanPeriodCount = Integer.parseInt(String.valueOf(response.body().getProfileData().getPeriod()));
                                    } else {
                                        strSubPlanPeriodCount = 0;
                                    }

                                    if (response.body().getProfileData().getPlanType() != null
                                            && !response.body().getProfileData().getPlanType().toString().equals("null")
                                            && !response.body().getProfileData().getPlanType().toString().equals("")) {
                                        strSubPlanType = response.body().getProfileData().getPlanType().toString();
                                    } else {
                                        strSubPlanType = "";
                                    }

                                    if (response.body().getProfileData().getFreeKm() != null
                                            && !response.body().getProfileData().getFreeKm().toString().equals("null")
                                            && !response.body().getProfileData().getFreeKm().toString().equals("")) {
                                        strSubPlanFreeKM = response.body().getProfileData().getFreeKm();
                                    } else {
                                        strSubPlanFreeKM = 0;
                                    }

                                    if (response.body().getProfileData().getFreeJobRequest() != null
                                            && !response.body().getProfileData().getFreeJobRequest().toString().equals("null")
                                            && !response.body().getProfileData().getFreeJobRequest().toString().equals("")) {
                                        strSubPlanFreeJobRequest = response.body().getProfileData().getFreeJobRequest();
                                    } else {
                                        strSubPlanFreeJobRequest = 0;
                                    }


                                    String strSubscriptionCode = String.valueOf(Objects.requireNonNull(response.body()).
                                            getProfileData().getSubscriptionCode());

                                    if (strSubscriptionCode != null && !strSubscriptionCode.equals("null")
                                            && !strSubscriptionCode.equals("")) {
                                        refs.child(Utils.FIREBASE_USER_HEADER).child(userId).child("subs_code").setValue(strSubscriptionCode);
                                    } else {
                                        refs.child(Utils.FIREBASE_USER_HEADER).child(userId).child("subs_code").setValue("0");
                                    }

                                    showDialog(strSubPlan, strSubPlanPeriodCount, strSubPlanPeriod, strSubPlanDes,
                                            strSubPlanAmount, strSubPlanType, strSubPlanFreeKM, strSubPlanFreeJobRequest,
                                            strCurrencySymbol, 0, null);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {

                        if (llParentLytSubsPlan.getVisibility() == View.VISIBLE) {
                            llParentLytSubsPlan.setVisibility(View.GONE);
                        }

                        if (viewSubscPlanFocusView.getVisibility() == View.VISIBLE) {
                            viewSubscPlanFocusView.setVisibility(View.GONE);
                        }

                        txtSubscriptionPlanSetupLabel.setVisibility(View.GONE);
                        navigationSubsPlan.setVisibility(View.GONE);

                        // callRefreshToken("viewprofile");
                    }

                    hideProgressDialog_();

                } else {

                    if (llParentLytSubsPlan.getVisibility() == View.VISIBLE) {
                        llParentLytSubsPlan.setVisibility(View.GONE);
                    }

                    if (viewSubscPlanFocusView.getVisibility() == View.VISIBLE) {
                        viewSubscPlanFocusView.setVisibility(View.GONE);
                    }

                    txtSubscriptionPlanSetupLabel.setVisibility(View.GONE);
                    navigationSubsPlan.setVisibility(View.GONE);

                    // callRefreshToken("viewprofile");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProfileModel> call, @NonNull Throwable t) {
                hideProgressDialog_();
                Log.e("TAG", "onFailure: ~~~~view profile~~~~" + t);
                call.cancel();
            }
        });
    }

    private void callSubscriptionListApi() {

        userAccessToken = spUpdate.getString(Utils.SP_USER_ACCESSTOKEN, "123");
        if (userAccessToken == null) {
            userAccessToken = "";
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("user_type", "1");

        Log.i("callSubscriptionIdApi", "~~~~" + map);

        showProgressDialog_(requireActivity());

        Call<ListViewSubsModel> call2 = apiInterface.doGetsubscriptionList(map);

        call2.enqueue(new Callback<ListViewSubsModel>() {
            @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
            @Override
            public void onResponse(@NonNull Call<ListViewSubsModel> call, @NonNull Response<ListViewSubsModel> response) {
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Listed Successfully")
                            || Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("success")) {

                        try {

                            getve = response.body().getResult();

                            if (getve != null && getve.size() > 0) {
                                Log.i("TAG", "onResponse: -->" + getve.size());

                                txtNoRecordsPlan.setVisibility(View.GONE);
//                                rcvSubscriptionPlan.setVisibility(View.GONE);

                                adapter = new SubscriptionPlanAdapter(requireActivity(), getve);
                                LinearLayoutManager horizontalLayoutManager
                                        = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                                rcvSubscriptionPlan.setLayoutManager(horizontalLayoutManager);
                                rcvSubscriptionPlan.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                adapter.selected(-1);

                            } else {

                                txtNoRecordsPlan.setVisibility(View.VISIBLE);
                                rcvSubscriptionPlan.setVisibility(View.GONE);
                                btnContinueBuySubsPlan.setVisibility(View.GONE);

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        txtNoRecordsPlan.setVisibility(View.VISIBLE);
                        rcvSubscriptionPlan.setVisibility(View.GONE);
                        btnContinueBuySubsPlan.setVisibility(View.GONE);

                        Toast.makeText(requireActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    hideProgressDialog_();
                } else {

                    txtNoRecordsPlan.setVisibility(View.VISIBLE);
                    rcvSubscriptionPlan.setVisibility(View.GONE);
                    btnContinueBuySubsPlan.setVisibility(View.GONE);

                    Toast.makeText(requireActivity(), "500 -->", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ListViewSubsModel> call, @NonNull Throwable t) {
                hideProgressDialog_();
                Log.e("TAG", "onFailure: ~~~~view profile~~~~" + t);
                Toast.makeText(requireActivity(), t.toString(), Toast.LENGTH_SHORT).show();
                call.cancel();

                txtNoRecordsPlan.setVisibility(View.VISIBLE);
                rcvSubscriptionPlan.setVisibility(View.GONE);
                btnContinueBuySubsPlan.setVisibility(View.GONE);

            }
        });
    }

    private void callSubscriptionIdApi(String strSubscriptionId) {

        userAccessToken = spUpdate.getString(Utils.SP_USER_ACCESSTOKEN, "123");
        if (userAccessToken == null) {
            userAccessToken = "";
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("plan_id", strSubscriptionId);

        Log.i("callSubscriptionIdApi", "~~~~" + map);

        showProgressDialog_(requireActivity());

        Call<ViewSubsModel> call2 = apiInterface.doGetsubscriptionGetList("Bearer " + userAccessToken, Utils.POST_ACCEPT_CONTENT_TYPE, map);

        call2.enqueue(new Callback<ViewSubsModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<ViewSubsModel> call, @NonNull Response<ViewSubsModel> response) {
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Listed Successfully")) {

                        /*try {

                            String strSubPlan;
                            String strSubPlanDes;
                            String strSubPlanAmount;
                            String strSubPlanPeriod;
                            String strSubPlanType;

                            Integer strSubPlanFreeKM;
                            Integer strSubPlanFreeJobRequest;
                            Integer strSubPlanPeriodCount;

                            if (response.body().getResult().getName() != null
                                    && !response.body().getResult().getName().equals("null")
                                    && !response.body().getResult().getName().equals("")) {
                                strSubPlan = response.body().getResult().getName();
                            } else {
                                strSubPlan = "";
                            }

                            if (response.body().getResult().getDescription() != null
                                    && !response.body().getResult().getDescription().toString().equals("null")
                                    && !response.body().getResult().getDescription().toString().equals("")) {
                                strSubPlanDes = response.body().getResult().getDescription().toString();
                            } else {
                                strSubPlanDes = "";
                            }

                            if (response.body().getResult().getAmount() != null
                                    && !response.body().getResult().getAmount().toString().equals("null")
                                    && !response.body().getResult().getAmount().toString().equals("")) {
                                strSubPlanAmount = response.body().getResult().getAmount().toString();
                            } else {
                                strSubPlanAmount = "";
                            }

                            if (response.body().getResult().getPeriodType() != null
                                    && !response.body().getResult().getPeriodType().toString().equals("null")
                                    && !response.body().getResult().getPeriodType().toString().equals("")) {
                                strSubPlanPeriod = response.body().getResult().getPeriodType().toString();
                            } else {
                                strSubPlanPeriod = "";
                            }

                            if (response.body().getResult().getPeriod() != null
                                   *//* && !response.body().getResult().getPeriod().toString().equals("null")
                                    && !response.body().getResult().getPeriod().toString().equals("")*//*) {
                                strSubPlanPeriodCount = response.body().getResult().getPeriod();
                            } else {
                                strSubPlanPeriodCount = 0;
                            }

                            if (response.body().getResult().getPlanType() != null
                                    && !response.body().getResult().getPlanType().toString().equals("null")
                                    && !response.body().getResult().getPlanType().toString().equals("")) {
                                strSubPlanType = response.body().getResult().getPlanType().toString();
                            } else {
                                strSubPlanType = "";
                            }

                            if (response.body().getResult().getFreeKm() != null
                                    && !response.body().getResult().getFreeKm().toString().equals("null")
                                    && !response.body().getResult().getFreeKm().toString().equals("")) {
                                strSubPlanFreeKM = response.body().getResult().getFreeKm();
                            } else {
                                strSubPlanFreeKM = 0;
                            }

                            if (response.body().getResult().getFreeJobRequest() != null
                                    && !response.body().getResult().getFreeJobRequest().toString().equals("null")
                                    && !response.body().getResult().getFreeJobRequest().toString().equals("")) {
                                strSubPlanFreeJobRequest = response.body().getResult().getFreeJobRequest();
                            } else {
                                strSubPlanFreeJobRequest = 0;
                            }

                            showDialog(strSubPlan, strSubPlanPeriodCount, strSubPlanPeriod, strSubPlanDes, strSubPlanAmount, strSubPlanType, strSubPlanFreeKM, strSubPlanFreeJobRequest, strCurrencySymbol);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }*/
                    } else {
                        Toast.makeText(requireActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    hideProgressDialog_();
                } else {
                    Toast.makeText(requireActivity(), R.string.try_again, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ViewSubsModel> call, @NonNull Throwable t) {
                hideProgressDialog_();
                Log.e("TAG", "onFailure: ~~~~view profile~~~~" + t.toString());
                Toast.makeText(requireActivity(), R.string.try_again, Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }

    private void callSubscriptionUpdateIdApi(String strSubscriptionId) {

        userAccessToken = spUpdate.getString(Utils.SP_USER_ACCESSTOKEN, "123");
        if (userAccessToken == null) {
            userAccessToken = "";
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("customer_id", userId);
        map.put("plan_id", strSubscriptionId);

        Log.i("callSubscriptionIdApi", "~~~~" + map);

        showProgressDialog_(requireActivity());

        Call<UpdateSubsModel> call2 = apiInterface.doUpdatesubscriptionList(map);

        call2.enqueue(new Callback<UpdateSubsModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<UpdateSubsModel> call, @NonNull Response<UpdateSubsModel> response) {
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    hideProgressDialog_();
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Subscription Buy Successfully")) {
                        try {
                            showAlertDialog(requireActivity(), "update_view");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(requireActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    hideProgressDialog_();
                } else {
                    hideProgressDialog_();
                    Toast.makeText(requireActivity(), R.string.try_again, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UpdateSubsModel> call, @NonNull Throwable t) {
                hideProgressDialog_();
                Log.e("TAG", "onFailure: ~~~~view profile~~~~" + t.toString());
                Toast.makeText(requireActivity(), R.string.try_again, Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        callViewProfileDetails("home");
        callSubscriptionListApi();
    }

    @Override
    public void onSuccess(HashMap<String, Object> map) {
        System.out.println("~~onSuccess~~");

        String click = String.valueOf(map.get("click"));
        int position = (int) map.get("position");
        ListViewSubsData listModel = (ListViewSubsData) map.get("data");

        if (listModel != null) {

            String strSubPlan;
            String strSubPlanDes;
            String strSubPlanAmount;
            String strSubPlanPeriod;
            String strSubPlanType;

            Integer strSubPlanFreeKM;
            Integer strSubPlanFreeJobRequest;
            Integer strSubPlanPeriodCount;

            if (listModel.getName() != null
                    && !listModel.getName().equals("null")
                    && !listModel.getName().equals("")) {
                strSubPlan = listModel.getName();
            } else {
                strSubPlan = "";
            }

            if (listModel.getDescription() != null
                    && !listModel.getDescription().toString().equals("null")
                    && !listModel.getDescription().toString().equals("")) {
                strSubPlanDes = listModel.getDescription().toString();
            } else {
                strSubPlanDes = "";
            }

            if (listModel.getAmount() != null
                    && !listModel.getAmount().toString().equals("null")
                    && !listModel.getAmount().toString().equals("")) {
                strSubPlanAmount = listModel.getAmount().toString();
            } else {
                strSubPlanAmount = "";
            }

            if (listModel.getPeriodType() != null
                    && !listModel.getPeriodType().toString().equals("null")
                    && !listModel.getPeriodType().toString().equals("")) {
                strSubPlanPeriod = listModel.getPeriodType().toString();
            } else {
                strSubPlanPeriod = "";
            }

            if (listModel.getPeriod() != null
                    /*&& !listModel.getPeriod().toString().equals("null")
                    && !listModel.getPeriod().toString().equals("")*/) {
                strSubPlanPeriodCount = listModel.getPeriod();
            } else {
                strSubPlanPeriodCount = 0;
            }

            if (listModel.getPlanType() != null
                    && !listModel.getPlanType().toString().equals("null")
                    && !listModel.getPlanType().toString().equals("")) {
                strSubPlanType = listModel.getPlanType().toString();
            } else {
                strSubPlanType = "0";
            }

            if (listModel.getFreeKm() != null
                    && !listModel.getFreeKm().toString().equals("null")
                    && !listModel.getFreeKm().toString().equals("")) {
                strSubPlanFreeKM = listModel.getFreeKm();
            } else {
                strSubPlanFreeKM = 0;
            }

            if (listModel.getFreeJobRequest() != null
                    && !listModel.getFreeJobRequest().toString().equals("null")
                    && !listModel.getFreeJobRequest().toString().equals("")) {
                strSubPlanFreeJobRequest = listModel.getFreeJobRequest();
            } else {
                strSubPlanFreeJobRequest = 0;
            }

            if (click.equals("info")) {
                strSubscriptionId = String.valueOf(listModel.getId());
                showDialog(strSubPlan, strSubPlanPeriodCount, strSubPlanPeriod, strSubPlanDes, strSubPlanAmount,
                        strSubPlanType, strSubPlanFreeKM, strSubPlanFreeJobRequest, strCurrencySymbol, null, strSubscriptionId);
            } else if (click.equals("select")) {

                strSubscriptionId = String.valueOf(listModel.getId());

                if (adapter != null) {
                    adapter.selected(position);
                }

                btnContinueBuySubsPlan.setVisibility(View.VISIBLE);

            }

        }

    }

    private void showAlertDialog(Activity self, String from) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(self, R.style.AppCompatAlertDialogStyle);
        builder.setCancelable(false);

        Typeface tf = Typeface.createFromAsset(self.getAssets(), getString(R.string.App_Font));

        BaseActivity.CustomTFSpan tfSpan = new BaseActivity.CustomTFSpan(tf);

        SpannableString spannableStrings = new SpannableString(getString(R.string.ok));
        spannableStrings.setSpan(tfSpan, 0, spannableStrings.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString spannableStringss = new SpannableString(getString(R.string.cancel));
        spannableStringss.setSpan(tfSpan, 0, spannableStringss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        String label = "";
        switch (from) {
            case "update":
                label = getResources().getString(R.string.are_you_sure_want_subcs_plan);
                break;
            case "update_view":
                label = getResources().getString(R.string.plan_has_been_subscribed);
                break;
            case "enable":
                label = getResources().getString(R.string.plan_has_been_enable_autorenew);
                break;
            case "disable":
                label = getResources().getString(R.string.plan_has_been_disable_autorenew);
                break;
        }

        SpannableString spannableString = new SpannableString(label);
        spannableString.setSpan(tfSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        builder.setMessage(spannableString);

        builder.setPositiveButton(spannableStrings,
                (dialog, which) -> {
                    dialog.dismiss();
                    switch (from) {
                        case "update":
                            if (strSubscriptionId != null && !strSubscriptionId.equals("null")
                                    && !strSubscriptionId.equals("") && !strSubscriptionId.equals("0")) {
                                // callSubscriptionUpdateIdApi(strSubscriptionId);
                                callCheckBuyPlan();
                            } else {
                                Toast.makeText(requireActivity(), getResources().getString(R.string.subs_id_empty), Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case "update_view":
                            onRefresh();
                            break;
                        case "enable":
                            callDisablePlan("enable", strSubscriptionHistoryId);
                            break;
                        case "disable":
                            callDisablePlan("disable", strSubscriptionHistoryId);
                            break;
                    }
                });

        if (from.equals("update") || from.equals("enable") || from.equals("disable")) {
            builder.setNegativeButton(spannableStringss,
                    (dialog, which) -> dialog.dismiss());
        }

        builder.show();

    }

    private void callCheckBuyPlan() {

        HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("plan_id", strSubscriptionId);
        map.put("auto_renew", "1");
        map.put("type", "1");

        /*user_id:102
plan_id:19
auto_renew:0
type:2*/

        Log.i("callViewProfileDetails", "~~~~" + map);

        showProgressDialog_(requireActivity());

        Call<BuyPlanWebHookModel> call2 = apiInterface.doCreateCheckoutSessionSubscription(map);

        call2.enqueue(new Callback<BuyPlanWebHookModel>() {
            @SuppressLint({"SetTextI18n", "SetJavaScriptEnabled"})
            @Override
            public void onResponse(@NonNull Call<BuyPlanWebHookModel> call, @NonNull Response<BuyPlanWebHookModel> response) {

                hideProgressDialog_();

                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {

                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("checkout session created successfully")) {

                        //generateTokenAPI = response.body().getResult().getAccessToken();

                        progressBar.setVisibility(View.VISIBLE);

                        try {

                            String urlPaynow = response.body().getResult().getUrl();

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
                                            Log.v("TAG", "download() Method completed ");
                                        }
                                    }
                                }

                                @Override
                                public void onPageFinished(WebView view, String url) {
                                    Log.e("TAG", "onPageFinished: " + url);
                                    super.onPageFinished(view, url);
                                    progressBar.setVisibility(View.GONE);

                                }

                                @Override
                                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                    Log.e("TAG", "onPageStarted: " + url);
                                    progressBar.setVisibility(View.VISIBLE);
                                }
                            });

                            if (lytPaymentWebView.getVisibility() == View.GONE) {
                                lytPaymentWebView.setVisibility(View.VISIBLE);
                            }

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(requireActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(requireActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    hideProgressDialog_();

                } else {
                    Toast.makeText(requireActivity(), "500-->" + R.string.try_again, Toast.LENGTH_SHORT).show();
                    hideProgressDialog_();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BuyPlanWebHookModel> call, @NonNull Throwable t) {
                hideProgressDialog_();
                Log.e("TAG", "onFailure: ~~~~view profile~~~~" + t);
                call.cancel();
            }
        });
    }


    private void callCheckBuyPlan_redirect_webview() {

        HashMap<String, Object> map = new HashMap<>();
        map.put("customer_id", userId);
        Log.i("callViewProfileDetails", "~~~~" + map);

        showProgressDialog_(requireActivity());

        Call<BuyPlanModel> call2 = apiInterface.doCheckSubscription(map);

        call2.enqueue(new Callback<BuyPlanModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<BuyPlanModel> call, @NonNull Response<BuyPlanModel> response) {

                hideProgressDialog_();

                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {

                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Subscription plan exist")) {
                        Toast.makeText(requireActivity(), "You are already subscribed.", Toast.LENGTH_SHORT).show();
                        onRefresh();
                    } else if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Subscription plan not exist")) {
                        String url = Utils.BASE_URL + response.body().getResult();
                        Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse(url));
                        startActivity(viewIntent);
                    } else {
                        Toast.makeText(requireActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    hideProgressDialog_();

                } else {
                    Toast.makeText(requireActivity(), "500-->" + R.string.try_again, Toast.LENGTH_SHORT).show();
                    hideProgressDialog_();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BuyPlanModel> call, @NonNull Throwable t) {
                hideProgressDialog_();
                Log.e("TAG", "onFailure: ~~~~view profile~~~~" + t);
                call.cancel();
            }
        });
    }

    private void callDisablePlan(String from, String strSubscriptionId) {

        /* update auto renew	/api/v3/updateAutoRenew	"customer_id:1
        subscription_id:1
        action:""disable"*/

        HashMap<String, Object> map = new HashMap<>();
        map.put("customer_id", userId);
        map.put("subscription_id", Integer.parseInt(strSubscriptionId));
        map.put("user_type", "1");
        if (from.equals("disable")) {
            map.put("action", "disable");
            map.put("auto_renew", "0");
        } else {
            map.put("action", "enable");
            map.put("auto_renew", "1");
        }

        Log.i("callViewProfileDetails", "~~~~" + map);

        showProgressDialog_(requireActivity());

        Call<CancelAutoRenewPlanModel> call2 = apiInterface.doUpdateAutoRenew(map);

        call2.enqueue(new Callback<CancelAutoRenewPlanModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<CancelAutoRenewPlanModel> call, @NonNull Response<CancelAutoRenewPlanModel> response) {

                hideProgressDialog_();

                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {

                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("auto renew updated successfully")) {
                        if (from.equals("disable")) {
                            Toast.makeText(requireActivity(), getResources().getString(R.string.auto_renew_disable), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireActivity(), getResources().getString(R.string.auto_renew_enable), Toast.LENGTH_SHORT).show();
                        }
                        onRefresh();
                    } else {
                        Toast.makeText(requireActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    hideProgressDialog_();

                } else {
                    Toast.makeText(requireActivity(), "500-->" + R.string.try_again, Toast.LENGTH_SHORT).show();
                    hideProgressDialog_();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CancelAutoRenewPlanModel> call, @NonNull Throwable t) {
                hideProgressDialog_();
                Log.e("TAG", "onFailure: ~~~~view profile~~~~" + t);
                call.cancel();
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (webView != null) {
            webView.clearCache(true);
            webView.clearFormData();
            webView.clearHistory();
            webView.clearSslPreferences();
        }

        if (paymentLauncherRegister != null) {
            paymentLauncherRegister.unregister();
        }

        if (customerWalletDatabaseBRef != null) {
            customerWalletDatabaseBRef.removeEventListener(customerWalletValueEventListener);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (webView != null) {
            webView.clearCache(true);
            webView.clearFormData();
            webView.clearHistory();
            webView.clearSslPreferences();
        }

        if (paymentLauncherRegister != null) {
            paymentLauncherRegister.unregister();
        }

        if (customerWalletDatabaseBRef != null) {
            customerWalletDatabaseBRef.removeEventListener(customerWalletValueEventListener);
        }

    }


    String strSubsPayStatus;
    DatabaseReference customerWalletDatabaseBRef;
    ValueEventListener customerWalletValueEventListener;

    private void getCustomerDetailsFB() {

        try {
            if (getActivity() != null) {

                if (customerWalletDatabaseBRef != null) {
                    customerWalletDatabaseBRef.removeEventListener(customerWalletValueEventListener);
                }

                customerWalletDatabaseBRef = FirebaseDatabase.getInstance().getReference().child(Utils.FIREBASE_USER_HEADER).child(userId);
                customerWalletValueEventListener = customerWalletDatabaseBRef.addValueEventListener(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {

                            Object remainingJobObj = dataSnapshot.child("subs_buy_status").getValue();

                            if (remainingJobObj != null) {
                                strSubsPayStatus = remainingJobObj.toString();
                            } else {
                                strSubsPayStatus = "0";
                            }

                            Log.e("TAG", "onDataChange: ~~~~~~~~" + "~~~~" + strSubsPayStatus);

                            if (strSubsPayStatus.equals("1")) {

                                if (lytPaymentWebView.getVisibility() == View.VISIBLE) {
                                    lytPaymentWebView.setVisibility(View.GONE);
                                }

                                if (lytSubscriptionDetailsView.getVisibility() == View.VISIBLE) {
                                    lytSubscriptionDetailsView.setVisibility(View.GONE);
                                }

                                if (lytSuccess.getVisibility() == View.GONE) {
                                    lytSuccess.setVisibility(View.VISIBLE);
                                }

                                txtSuccessTitle.setText(getString(R.string.update_membership));

                            } else if (strSubsPayStatus.equals("2")) {

                                if (lytPaymentWebView.getVisibility() == View.VISIBLE) {
                                    lytPaymentWebView.setVisibility(View.GONE);
                                }

                                if (lytSubscriptionDetailsView.getVisibility() == View.VISIBLE) {
                                    lytSubscriptionDetailsView.setVisibility(View.GONE);
                                }

                                if (lytSuccess.getVisibility() == View.GONE) {
                                    lytSuccess.setVisibility(View.VISIBLE);
                                }


                                txtSuccessTitle.setText(getString(R.string.not_update_membership));

                            } else {

                                if (lytPaymentWebView.getVisibility() == View.VISIBLE) {
                                    lytPaymentWebView.setVisibility(View.GONE);
                                }

                                if (lytSubscriptionDetailsView.getVisibility() == View.VISIBLE) {
                                    lytSubscriptionDetailsView.setVisibility(View.GONE);
                                }

                                if (lytSuccess.getVisibility() == View.VISIBLE) {
                                    lytSuccess.setVisibility(View.GONE);
                                }

                                txtSuccessTitle.setText(getString(R.string.not_update_membership));

                            }

                        } else {
                            //callViewProfileDetails("home"); //API
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        Log.e("TAG", "onCancelled: ~~databaseError~~~~~~~~~" + databaseError.toString());
                        //callViewProfileDetails("home"); //API
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println("~~~~Exception ~``" + e);
        }
    }


}