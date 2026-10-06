package com.instaguard.user.Fragments;

import static android.content.Context.MODE_PRIVATE;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_200;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_201;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
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
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.instaguard.user.Activities.BaseActivity;
import com.instaguard.user.Adapter.SubscriptionPlanHistoryAdapter;
import com.instaguard.user.CustomViews.CustomTextView;
import com.instaguard.user.Helpers.Retrofit.APIClient;
import com.instaguard.user.Helpers.Retrofit.APIInterface;
import com.instaguard.user.Helpers.Utils;
import com.instaguard.user.Models.Subscription.CancelAutoRenewPlanModel;
import com.instaguard.user.Models.Subscription.SubsHistoryData;
import com.instaguard.user.Models.Subscription.SubsHistoryModel;
import com.instaguard.user.R;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubscriptionPlanHistoryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, SubscriptionPlanHistoryAdapter.SuccessResponse {

    View view;

    ImageButton imgbtnBack;
    CustomTextView txtNoRecordsPlan;
    RecyclerView rcvSubscriptionPlan;
    RelativeLayout progressBar;
    SwipeRefreshLayout swipeRefreshLayout;

    String userId;
    String strCurrencySymbol;
    String userAccessToken;
    String strSubscriptionViewId = "0";
    String strSubscriptionId = "0";

    SharedPreferences.Editor getUpdate;
    SharedPreferences spUpdate;
    SharedPreferences getspCurrency;

    APIInterface apiInterface;

    List<SubsHistoryData> getve;

    SubscriptionPlanHistoryAdapter adapter;

    String strFrom = "";

    public SubscriptionPlanHistoryFragment() {
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
        view = inflater.inflate(R.layout.fragment_subscription_plan_history, container, false);

        getUpdate = requireActivity().getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE).edit();
        spUpdate = requireActivity().getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE);
        userId = spUpdate.getString(Utils.SP_USERID, null);

        getspCurrency = requireActivity().getSharedPreferences(Utils.PREFS_NAME_CURRENCY, MODE_PRIVATE);
        strCurrencySymbol = getspCurrency.getString(Utils.SP_CURRENCY_SYMBOL, null);
        if (strCurrencySymbol == null) {
            strCurrencySymbol = "CAD";
        }

        imgbtnBack = view.findViewById(R.id.imgbtn_back);
        progressBar = view.findViewById(R.id.rl_progress_bar_cnf);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout_plan);
        swipeRefreshLayout.setOnRefreshListener(this);

        txtNoRecordsPlan = view.findViewById(R.id.txt_no_records);
        rcvSubscriptionPlan = view.findViewById(R.id.rcv_subscription_plan);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        SubscriptionPlanHistoryAdapter.successResponse = this;

        onRefresh();

        OnClick();

        if (getArguments() != null) {
            strFrom = getArguments().getString("from");
        }

        onBackButton(view);

        return view;
    }

    private void OnClick() {

        imgbtnBack.setOnClickListener(v -> {
            hideKeyBoard();
            backButtonFunction();
        });

        progressBar.setOnClickListener(v -> {

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
            switch (strFrom) {
                case "login":
                case "main_perks":
                case "main":
                default:
                    removeFragment(Utils.FRAGMENT_SUBSCRIPTION_PLAN_HISTORY);
                    break;
            }
        } else {
            removeFragment(Utils.FRAGMENT_SUBSCRIPTION_PLAN_HISTORY);
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
    private void showDialog(String title, String periodType, String message, String amount, String strCurrencySymbol) {

        try {

            Dialog dialogBuilder = new Dialog(requireActivity(), R.style.AppTheme);
            dialogBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
            dialogBuilder.setContentView(R.layout.dialog_subscription_plan_view);
            dialogBuilder.setCancelable(true);
            dialogBuilder.getWindow().setWindowAnimations(R.style.DialogAnimationFadeINOUT);

            CustomTextView txtTitle = dialogBuilder.findViewById(R.id.txt_title);
            CustomTextView txtPeriodType = dialogBuilder.findViewById(R.id.txt_period_type);
            CustomTextView txtMessage = dialogBuilder.findViewById(R.id.txt_description);
            CustomTextView txtAmount = dialogBuilder.findViewById(R.id.txt_amount);
            ImageView ivCancel = dialogBuilder.findViewById(R.id.imgbtn_back);

            txtTitle.setText(title);

            switch (periodType) {
                case "1":
                case "1.0":
                    txtPeriodType.setText(getResources().getString(R.string.days));
                    break;
                case "2":
                case "2.0":
                    txtPeriodType.setText(getResources().getString(R.string.monthly));
                    break;
                case "3":
                case "3.0":
                    txtPeriodType.setText(getResources().getString(R.string.yearly));
                    break;
                default:
                    txtPeriodType.setText(periodType);
                    break;
            }

            txtAmount.setText(strCurrencySymbol + " " + amount);

            txtMessage.setText(Html.fromHtml(message));
            Linkify.addLinks(txtMessage, Linkify.ALL);

            ivCancel.setOnClickListener(v -> dialogBuilder.dismiss());

            dialogBuilder.show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireActivity(), R.string.try_again, Toast.LENGTH_SHORT).show();
        }

    }


    private void callSubscriptionListApi() {

        userAccessToken = spUpdate.getString(Utils.SP_USER_ACCESSTOKEN, "123");
        if (userAccessToken == null) {
            userAccessToken = "";
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("cus_id", userId);
        map.put("offset", "0");
        map.put("user_type", "1");

        /*cus_id:146
        offset:0
        user_type:1*/

        Log.i("callSubscriptionIdApi", "~~~~" + map);

        showProgressDialog_(requireActivity());

        Call<SubsHistoryModel> call2 = apiInterface.doSubscriptionHistory(map);

        call2.enqueue(new Callback<SubsHistoryModel>() {
            @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
            @Override
            public void onResponse(@NonNull Call<SubsHistoryModel> call, @NonNull Response<SubsHistoryModel> response) {

                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {

                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Listed Successfully")) {

                        try {

                            getve = response.body().getData();

                            if (getve != null && getve.size() > 0) {
                                Log.i("TAG", "onResponse: -->" + getve.size());

                                txtNoRecordsPlan.setVisibility(View.GONE);
                                rcvSubscriptionPlan.setVisibility(View.VISIBLE);

                                adapter = new SubscriptionPlanHistoryAdapter(requireActivity(), getve);
                                LinearLayoutManager horizontalLayoutManager
                                        = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                                rcvSubscriptionPlan.setLayoutManager(horizontalLayoutManager);
                                rcvSubscriptionPlan.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                            } else {
                                txtNoRecordsPlan.setVisibility(View.VISIBLE);
                                rcvSubscriptionPlan.setVisibility(View.GONE);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        txtNoRecordsPlan.setVisibility(View.VISIBLE);
                        rcvSubscriptionPlan.setVisibility(View.GONE);
                        Toast.makeText(requireActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    hideProgressDialog_();
                } else {

                    txtNoRecordsPlan.setVisibility(View.VISIBLE);
                    rcvSubscriptionPlan.setVisibility(View.GONE);

                    Toast.makeText(requireActivity(), "500 -->", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SubsHistoryModel> call, @NonNull Throwable t) {
                hideProgressDialog_();
                Log.e("TAG", "onFailure: ~~~~view  ~~~~" + t);
                Toast.makeText(requireActivity(), t.toString(), Toast.LENGTH_SHORT).show();
                call.cancel();

                txtNoRecordsPlan.setVisibility(View.VISIBLE);
                rcvSubscriptionPlan.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        callSubscriptionListApi();
    }

    @Override
    public void onSuccess(HashMap<String, Object> map) {
        System.out.println("~~onSuccess~~");
        String click = String.valueOf(map.get("click"));
        int position = (int) map.get("position");
        SubsHistoryData listModel = (SubsHistoryData) map.get("data");

        if (listModel != null) {

            if(click.equals("disable")){
                showAlertDialog(requireActivity(), String.valueOf(listModel.getId()));
            }

         }
/*
        update auto renew	/api/v3/updateAutoRenew	"customer_id:1
        subscription_id:1
        action:""disable""

                - Enable -> action - enable
                - Disable -> action - disable

                -- Check subscription History API"*/
    }

    private void showAlertDialog(Activity self, String strSubscriptionId) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(self, R.style.AppCompatAlertDialogStyle);
        builder.setCancelable(false);

        Typeface tf = Typeface.createFromAsset(self.getAssets(), getString(R.string.App_Font));

        BaseActivity.CustomTFSpan tfSpan = new BaseActivity.CustomTFSpan(tf);

        SpannableString spannableStrings = new SpannableString(getString(R.string.yes));
        spannableStrings.setSpan(tfSpan, 0, spannableStrings.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString spannableStringss = new SpannableString(getString(R.string.no));
        spannableStringss.setSpan(tfSpan, 0, spannableStringss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        String label = getResources().getString(R.string.plan_has_been_disable_autorenew);

        SpannableString spannableString = new SpannableString(label);
        spannableString.setSpan(tfSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        builder.setMessage(spannableString);

        builder.setPositiveButton(spannableStrings,
                (dialog, which) -> {
                    dialog.dismiss();
                    callCheckBuyPlan(strSubscriptionId);
                });

            builder.setNegativeButton(spannableStringss,
                    (dialog, which) -> dialog.dismiss());

        builder.show();

    }

    private void callCheckBuyPlan(String strSubscriptionId) {

        /* update auto renew	/api/v3/updateAutoRenew	"customer_id:1
        subscription_id:1
        action:""disable"*/

        HashMap<String, Object> map = new HashMap<>();
        map.put("customer_id", userId);
        map.put("subscription_id", strSubscriptionId);
        map.put("action", "disable");

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
                        Toast.makeText(requireActivity(), getResources().getString(R.string.auto_renew_disable), Toast.LENGTH_SHORT).show();
                        onRefresh();
                    } else {
                        Toast.makeText(requireActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    hideProgressDialog_();

                } else {
                    Toast.makeText(requireActivity(), "500-->"+R.string.try_again, Toast.LENGTH_SHORT).show();
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

}