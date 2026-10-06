package com.instaguard.user.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.instaguard.user.Activities.BaseActivity;
import com.instaguard.user.Activities.HomeActivity;
import com.instaguard.user.Adapter.TransactionMoneyInOutAdapter;
import com.instaguard.user.Helpers.Retrofit.APIClient;
import com.instaguard.user.Helpers.Retrofit.APIInterface;
import com.instaguard.user.Helpers.Utils;
import com.instaguard.user.Models.RefreshTokenModel;
import com.instaguard.user.Models.TransactionModel;
import com.instaguard.user.Models.WalletAllData;
import com.instaguard.user.Models.WalletAllModel;
import com.instaguard.user.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.instaguard.user.Activities.BaseActivity.hideProgressDialog;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_200;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_201;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_401;


public class YourWalletTransMoneyOutFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    View view;

    SharedPreferences SP_Prefs;

    SharedPreferences.Editor putSpState;
    String userId;
    ImageButton imgbtnBack;
    SharedPreferences spCommon;
    String userAccessToken;
    RecyclerView rvPastJobs;
    RelativeLayout rlEmptyview;

    ArrayList<TransactionModel> transactionModelArray;
    private TransactionModel listFeed;

    TransactionMoneyInOutAdapter adapter;
    public String TAG="OnPastJobsFragment";


    public YourWalletTransMoneyOutFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static YourWalletTransMoneyOutFragment newInstance(String param1, String param2) {
        YourWalletTransMoneyOutFragment fragment = new YourWalletTransMoneyOutFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint({"LongLogTag", "CommitPrefEdits"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_yourwallet_moneyinout, container, false);

        SP_Prefs = Objects.requireNonNull(getActivity()).getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE);
        userId = SP_Prefs.getString(Utils.SP_USERID, null);
        Log.e("userId~~OnGoingJobsFragment~~", userId);
        spCommon = Objects.requireNonNull(getActivity()).getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE);
        putSpState = Objects.requireNonNull(getActivity()).getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE).edit();

        rvPastJobs = (RecyclerView) view.findViewById(R.id.rv_pastjobs);
        rlEmptyview = (RelativeLayout) view.findViewById(R.id.rl_emptyview);

        /*rvPastJobs.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvPastJobs.setLayoutManager(mLayoutManager3);
        rvPastJobs.setNestedScrollingEnabled(false);
        rvPastJobs.setAdapter(adapter);*/



        transactionModelArray = new ArrayList<TransactionModel>();
        adapter = new TransactionMoneyInOutAdapter(getActivity(),transactionModelArray );
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvPastJobs.setLayoutManager(linearLayoutManager);
        rvPastJobs.setAdapter(adapter);


        getWalletTransactionAllCall();
        onBackButton(view);
        return view;
    }

    private void getWalletTransactionAllCall() {

        SharedPreferences  getspCurrency = getActivity().getSharedPreferences(Utils.PREFS_NAME_CURRENCY, MODE_PRIVATE);
       // String strLanguage = getspCurrency.getString(Utils.SP_LANGUAGE, null);
        String strLanguageShortName = getspCurrency.getString(Utils.SP_LANGUAGE_SHORT_NAME, null);
        if (strLanguageShortName == null) {
            strLanguageShortName = "en";
        }

        userAccessToken = spCommon.getString(Utils.SP_USER_ACCESSTOKEN, null);
        HashMap<String, Object> map = new HashMap<>();
        map.put("customer_id", userId);
        map.put("language",strLanguageShortName);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<WalletAllModel> call2 = apiInterface.doWalletTransactionAll("Bearer "+userAccessToken,Utils.POST_ACCEPT_CONTENT_TYPE,map);
         //showProgressDialog(getActivity());
        call2.enqueue(new Callback<WalletAllModel>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(@NonNull Call<WalletAllModel> call, @NonNull Response<WalletAllModel> response) {
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    hideProgressDialog();
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Transactions listed successfully.")) {
                        List<WalletAllData> getve = Objects.requireNonNull(response.body()).getWalletAllData();
                        Log.i(TAG, "onResponse: getUpcomingJobsCall-->"+getve);
                        if(getve.size() > 0) {

                            for (int i= 0; i<getve.size();i++){

                                String type = getve.get(i).getTransactionType();
                                if (type.equals("debit")||type.equals("Debit")){
                                    Log.e("inside debit option -- ",type);

                                    TransactionModel transactionModel = new TransactionModel();
                                    transactionModel.setTransactionType(getve.get(i).getTransactionType());
                                    transactionModel.setTransactionAmount(String.valueOf(getve.get(i).getTransactionAmount()));
                                    transactionModel.setTransactionDate(String.valueOf(getve.get(i).getTransactionDate()));
                                    transactionModel.setTransactionNotes(String.valueOf(getve.get(i).getTransactionNotes()));
                                    transactionModel.setTransactionNotesAr(String.valueOf(getve.get(i).getTransactionNotesAr()));
                                    transactionModel.setTransactionMode(String.valueOf(getve.get(i).getTransactionMode()));

                                    if (i == 0)
                                        transactionModel.setIsSelected(true);
                                    else
                                        transactionModel.setIsSelected(false);
                                    transactionModelArray.add(transactionModel);

                                } /*else
                                if (type.equals("credit")){

                                    Log.e("inside credit option -- ",type);

                                    TransactionModel transactionModel = new TransactionModel();
                                    transactionModel.setTransactionType(getve.get(i).getTransactionType());
                                    transactionModel.setTransactionAmount(String.valueOf(getve.get(i).getTransactionAmount()));
                                    transactionModel.setTransactionDate(String.valueOf(getve.get(i).getTransactionDate()));
                                    transactionModel.setTransactionNotes(String.valueOf(getve.get(i).getTransactionNotes()));
                                    if (i == 0)
                                        transactionModel.setIsSelected(true);
                                    else
                                        transactionModel.setIsSelected(false);
                                    transactionModelArray.add(transactionModel);
                                }*/
                            }

                            if (transactionModelArray != null) {
                                if (transactionModelArray.size() > 0) {
                                    rlEmptyview.setVisibility(View.GONE);
                                    rvPastJobs.setVisibility(View.VISIBLE);
                                } else {
                                    rlEmptyview.setVisibility(View.VISIBLE);
                                    rvPastJobs.setVisibility(View.GONE);
                                }
                            }else {
                                rlEmptyview.setVisibility(View.VISIBLE);
                                rvPastJobs.setVisibility(View.GONE);
                            }

                            adapter.notifyDataSetChanged();

                            hideProgressDialog();
                        }else{
                            rlEmptyview.setVisibility(View.VISIBLE);
                            rvPastJobs.setVisibility(View.GONE);
                        }
                    }else if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("No records Found.")) {
                        rlEmptyview.setVisibility(View.VISIBLE);
                        rvPastJobs.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(getActivity(), R.string.try_again, Toast.LENGTH_SHORT).show();
                        callRefreshToken("Category");
                    }
                }else if (response.code() == RESPONSECODE_401){
                    callRefreshToken("Category");
                }
            }
            @Override
            public void onFailure(Call<WalletAllModel> call, Throwable t) {
                Log.i("errror", "errr" + t);
                call.cancel();
                Toast.makeText(getActivity(), "please try again", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void callRefreshToken(String logout) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", userId);
        map.put("token",userAccessToken );
        Log.e("Logout~~Refresh~~~", "Refresh~~~" + map);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<RefreshTokenModel> call2 = apiInterface.doGetRefreshToken(Utils.POST_ACCEPT_CONTENT_TYPE,Utils.POST_ACCEPT_CONTENT_TYPE,map);
        call2.enqueue(new Callback<RefreshTokenModel>() {
            @Override
            public void onResponse(@NonNull Call<RefreshTokenModel> call, @NonNull Response<RefreshTokenModel> response) {
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    String refreshToken = Objects.requireNonNull(response.body()).getAccessToken();
                    String refreshTokenType = Objects.requireNonNull(response.body()).getTokenType();
                    String refreshTokenTimeExp = String.valueOf(Objects.requireNonNull(response.body()).getExpiresIn());
                    savesharedpreference(refreshToken,refreshTokenType,refreshTokenTimeExp);
                    if (logout.equalsIgnoreCase("Category")){
                        getWalletTransactionAllCall();
                    }
                }else if (response.code() == RESPONSECODE_401){
                    showTimeOut();
                }
            }
            @Override
            public void onFailure(@NonNull Call<RefreshTokenModel> call, @NonNull Throwable t) {
                call.cancel();
            }
        });
    }
    private void savesharedpreference(String access_token,String token_type,String exp_time) {

        putSpState.putString(Utils.SP_USER_ACCESSTOKEN,access_token);
        putSpState.putString(Utils.SP_USER_TOKENTYPE,token_type);
        putSpState.putString(Utils.SP_USER_TOKENEXPTIME,exp_time);
        putSpState.apply();
    }
    public void showTimeOut() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),
                getString(R.string.App_Font));

        BaseActivity.CustomTFSpan tfSpan = new BaseActivity.CustomTFSpan(tf);
        SpannableString spannableString = new SpannableString(getString(R.string.timeout_title));
        spannableString.setSpan(tfSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString spannableStrings = new SpannableString(getString(R.string.your_session_time_out_content));
        spannableStrings.setSpan(tfSpan, 0, spannableStrings.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString spannableStringss = new SpannableString(getString(R.string.ok));
        spannableStringss.setSpan(tfSpan, 0, spannableStringss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        builder.setMessage(spannableStrings);
        builder.setPositiveButton(spannableStringss,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearSPmodePrivate();
                    }
                });

        builder.show();
    }
    private void clearSPmodePrivate() {
        SharedPreferences preferences = Objects.requireNonNull(getActivity()).getSharedPreferences(Utils.MY_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        // deleteCache(MainActivity.this);
        Intent logout = new Intent(getActivity(), HomeActivity.class);
        logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(logout);
        getActivity().finish();
    }

    public void showAlertDialogs(String message) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),
                getString(R.string.App_Font));

        BaseActivity.CustomTFSpan tfSpan = new BaseActivity.CustomTFSpan(tf);
        SpannableString spannableString = new SpannableString(message);
        spannableString.setSpan(tfSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableString spannableStrings = new SpannableString(getString(R.string.ok));
        spannableStrings.setSpan(tfSpan, 0, spannableStrings.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        builder.setMessage(spannableString).setPositiveButton(spannableStrings,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

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

    public void backButtonFunction() {
       // MainActivity.frameClickable(false);
//        removeFragment(Utils.FRAGMENT_YOUR_WALLET_TRANSACTION);
        Utils.switchContent(R.id.frame_mainactivity,
                Utils.FRAGMENT_MYWALLET,
                Objects.requireNonNull(getActivity()), null,
                Utils.AnimationType.SLIDE_IN_SLIDE_OUT);
    }


    public void removeFragment(String Tag) {
        Utils.CURRENT_TAG = null;
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag(Tag)).commit();
    }
}
