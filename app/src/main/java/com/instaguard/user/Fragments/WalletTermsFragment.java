package com.instaguard.user.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.instaguard.user.Activities.BaseActivity;
import com.instaguard.user.Activities.HomeActivity;
import com.instaguard.user.CustomViews.CustomTextView;
import com.instaguard.user.Helpers.Retrofit.APIClient;
import com.instaguard.user.Helpers.Retrofit.APIInterface;
import com.instaguard.user.Helpers.Utils;
import com.instaguard.user.Models.AppSupportDataModel;
import com.instaguard.user.Models.AppSupportModel;
import com.instaguard.user.Models.RefreshTokenModel;
import com.instaguard.user.R;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.instaguard.user.Activities.BaseActivity.hideProgressDialog;
import static com.instaguard.user.Activities.BaseActivity.showProgressDialog;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_200;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_201;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_401;

public class WalletTermsFragment extends Fragment {

    ImageButton imgbtnBack;
    View view;
    LinearLayout llDescription,llContactUs;
    CustomTextView txtSupportSub,txtDescription ;

    String id,descrip,userAccessToken, driverId,categoryId,categoryName;


    SharedPreferences spCommon;
    SharedPreferences.Editor getUpdateProfile,getRequestState;

    public WalletTermsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint({"LongLogTag", "CommitPrefEdits"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_walletterms, container, false);

        getUpdateProfile = Objects.requireNonNull(getActivity()).getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE).edit();

        getRequestState = Objects.requireNonNull(getActivity()).getSharedPreferences(Utils.MY_PREFS_REQUEST_STATE, MODE_PRIVATE).edit();

        spCommon = Objects.requireNonNull(getActivity()).getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE);
        driverId = spCommon.getString(Utils.SP_USERID, null);
        userAccessToken = spCommon.getString(Utils.SP_USER_ACCESSTOKEN, null);
       // Log.e("driverId~~SupportSubFragment~~", driverId);


        imgbtnBack =(ImageButton)view.findViewById(R.id.imgbtn_back);
        txtDescription =(CustomTextView) view.findViewById(R.id.txt_description);
        llDescription =(LinearLayout)view.findViewById(R.id.ll_description);

        OnClick();
        getAppSupportPage();
        onBackButton(view);
        return view;
    }

    private void getAppSupportPage() {
        userAccessToken = spCommon.getString(Utils.SP_USER_ACCESSTOKEN, null);
        HashMap<String, Object> map = new HashMap<>();
        map.put("page_name", "Terms");
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<AppSupportModel> call2 = apiInterface.doGetAppSupportDetails("Bearer "+userAccessToken,map);
       // showProgressDialog(getActivity());
        call2.enqueue(new Callback<AppSupportModel>() {
            @Override
            public void onResponse(@NonNull Call<AppSupportModel> call, @NonNull Response<AppSupportModel> response) {
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                  //  hideProgressDialog();
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Pages listed successfully.")) {
                        List<AppSupportDataModel> getve = Objects.requireNonNull(response.body()).getAppSupportDataModel();
                        String[] items = new String[getve.size()];
                        Log.i("itemsitems", "" + Arrays.toString(items));
                        for (int i = 0; i < getve.size(); i++) {
                            descrip = getve.get(i).getDescription();
                            id = String.valueOf(getve.get(i).getId());
                            txtDescription.setText(descrip);
                        }
                    } else {
                        callRefreshToken("Category");
                    }
                }else if (response.code() == RESPONSECODE_401){
                    callRefreshToken("Category");
                }
            }
            @Override
            public void onFailure(Call<AppSupportModel> call, Throwable t) {
                Log.i("errror", "errr" + t);
                call.cancel();
            }
        });

    }



    private void OnClick() {
        imgbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard();
                backButtonFunction();
            }
        });

        getAppSupportPage();
    }



    private void callRefreshToken(String logout) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", driverId);
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
                        getAppSupportPage();
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

        getUpdateProfile.putString(Utils.SP_USER_ACCESSTOKEN,access_token);
        getUpdateProfile.putString(Utils.SP_USER_TOKENTYPE,token_type);
        getUpdateProfile.putString(Utils.SP_USER_TOKENEXPTIME,exp_time);
        getUpdateProfile.apply();
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
        // removeFragment(Utils.FRAGMENT_SUPPORT_SUBPAGES);
        Utils.switchContent(R.id.frame_mainactivity,
                Utils.FRAGMENT_MYWALLET,
                getActivity(), null,
                Utils.AnimationType.SLIDE_IN_SLIDE_OUT);
    }

    public void removeFragment(String Tag) {
        Utils.CURRENT_TAG = null;
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag(Tag)).commit();
    }


    public void hideKeyBoard()
    {
        // Check if no view has focus:
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}