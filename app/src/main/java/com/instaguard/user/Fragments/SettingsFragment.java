package com.instaguard.user.Fragments;

import static android.content.Context.MODE_PRIVATE;
import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;
import static com.instaguard.user.Activities.BaseActivity.hideProgressDialog;
import static com.instaguard.user.Activities.BaseActivity.showProgressDialog;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_200;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_201;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_401;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_500;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.chaos.view.PinView;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.instaguard.user.Activities.BaseActivity;
import com.instaguard.user.Activities.HomeActivity;
import com.instaguard.user.CustomViews.CustomButton;
import com.instaguard.user.CustomViews.CustomEditText;
import com.instaguard.user.CustomViews.CustomTextView;
import com.instaguard.user.CustomViews.CustomTypefaceSpan;
import com.instaguard.user.Helpers.Retrofit.APIClient;
import com.instaguard.user.Helpers.Retrofit.APIInterface;
import com.instaguard.user.Helpers.Utils;
import com.instaguard.user.Models.DeleteModel;
import com.instaguard.user.Models.ProfileModel;
import com.instaguard.user.Models.RefreshTokenModel;
import com.instaguard.user.R;

import java.util.HashMap;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SettingsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    View view;

    Toolbar toolbarSignin;
    ImageButton imgbtnBack;
    CustomEditText edtFname,edtLname,edtEmail,edtMobile,edtLanguage,edtCurrency;

    SimpleDraweeView ivBackcoverUserPro,ivUserPro;
    ImageView ivMenuProfile;


    SharedPreferences.Editor getState;
    SharedPreferences spPrefs;

    String userId,userAccessToken,strCurrency,strLanguage,userProfPic;
    public String TAG = "LoginFragment";
    String deviceId;
    private RoundingParams roundingParams;



    //Delete Account
    Dialog dialogDeleteAccount, dialogOtp, dialogToast;
    CustomTextView txtOkay, txtCancel, txtDescription, txtResend, txtMobile;
    CustomButton btnConfirm;
    ImageView imgBack, imgDelete;
    PinView pinOtp;
    String strPinOtp;
    CustomButton btnDeleteAccount;
    boolean success = false;
    //Delete Account



    public SettingsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        getState = requireActivity().getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE).edit();

        SharedPreferences pref = requireActivity().getSharedPreferences(Utils.GLOBAL_STORED_ID, MODE_PRIVATE);
        deviceId = pref.getString(Utils.SP_DEVICE_ID, null);

        spPrefs = requireActivity().getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE);
        userId = spPrefs.getString(Utils.SP_USERID, null);
        userAccessToken = spPrefs.getString(Utils.SP_USER_ACCESSTOKEN, null);
        userProfPic = spPrefs.getString(Utils.SP_USER_PRO_PIC, null);
        strLanguage = spPrefs.getString(Utils.SP_LANGUAGE, null);
        strCurrency = spPrefs.getString(Utils.SP_CURRENCY, null);

        Log.i(TAG, "token in singinpage--> "+deviceId);

        toolbarSignin = (Toolbar) view.findViewById(R.id.toolbar_setting);
        imgbtnBack = (ImageButton) view.findViewById(R.id.imgbtn_back);
        ivBackcoverUserPro = (SimpleDraweeView) view.findViewById(R.id.iv_backcover_user_profile);
        ivUserPro = (SimpleDraweeView) view.findViewById(R.id.iv_user_profile);
        edtFname = (CustomEditText) view.findViewById(R.id.edt_firstname);
        edtLname = (CustomEditText) view.findViewById(R.id.edt_lastname);
        edtEmail = (CustomEditText) view.findViewById(R.id.edt_email);
        edtMobile = (CustomEditText) view.findViewById(R.id.edt_mobile);
        edtLanguage = (CustomEditText) view.findViewById(R.id.edt_language);
        edtCurrency = (CustomEditText) view.findViewById(R.id.edt_currency);

        ivMenuProfile = (ImageView) view.findViewById(R.id.iv_menu_profile);

        int color = getResources().getColor(R.color.colorPrimary);
        roundingParams = RoundingParams.fromCornersRadius(5f);
        roundingParams.setBorder(color, 1.0f);
        roundingParams.setRoundAsCircle(true);

        if (userProfPic!= null){
            ivUserPro.setImageURI(Utils.BASE_URL+userProfPic);
            ivUserPro.getHierarchy().setRoundingParams(roundingParams);
        }else {
            ivUserPro.setImageURI("http://demo.uplogictech.com/uberx/admin/images/driver/61128183_w640_h640_rabota_voditelem_spb1.png");
            ivUserPro.getHierarchy().setRoundingParams(roundingParams);
        }

        if (strCurrency != null){
            edtCurrency.setText(strCurrency);
        }else {
            edtCurrency.setText("LKR");
        }

        if (strLanguage !=null){
            edtLanguage.setText(strLanguage);
        }else {
            edtLanguage.setText("ENGLISH");
        }

//        int color = getResources().getColor(R.color.colorPrimary);
//        RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
//        roundingParams.setBorder(color, 1.0f);
//        roundingParams.setRoundAsCircle(true);
//        navigationProfileImage.getHierarchy().setRoundingParams(roundingParams);


        ivMenuProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(requireActivity(), ivMenuProfile);
                popup.getMenuInflater().inflate(R.menu.profile_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        /*switch (id) {
                            case R.id.upcoming_edit:

                                Utils.switchContent(R.id.frame_mainactivity,
                                        Utils.FRAGMENT_EDITSETTINGS,requireActivity(),null,
                                        Utils.AnimationType.SLIDE_IN_SLIDE_OUT);
                                break;

                            case R.id.upcoming_cancel:


                                break;

                        }*/
                        if (id == R.id.upcoming_edit) {
                            Utils.switchContent(R.id.frame_mainactivity,
                                    Utils.FRAGMENT_EDITSETTINGS,requireActivity(),null,
                                    Utils.AnimationType.SLIDE_IN_SLIDE_OUT);

                        } else if (id == R.id.upcoming_cancel) {
                            // Handle upcoming_cancel here
                        }
                        return false;
                    }
                });
                popup.show();

                Menu menu = popup.getMenu();
                for (int i = 0; i < menu.size(); i++) {
                    MenuItem mi = menu.getItem(i);
                    applyFontToMenuItem(mi);
                }

            }
        });



        imgbtnBack.setOnClickListener(v -> {
            hideKeyBoard();
            backButtonFunction();
        });

        //Delete Account
        view.findViewById(R.id.btn_delete).setOnClickListener(v -> {
            //showDeleteAlertDialog();
            deleteAccountDialog();
        });
        //Delete Account

        CallViewProfileDetails();

        onBackButton(view);
        return view;
    }

    private void applyFontToMenuItem(MenuItem mi) {

        Typeface font = Typeface.createFromAsset(requireActivity().getAssets(), getResources().getString(R.string.App_Font));
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }


    private void CallViewProfileDetails() {
        userAccessToken = spPrefs.getString(Utils.SP_USER_ACCESSTOKEN, null);
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", userId);
        map.put("token",userAccessToken );
        Log.i("CallViewProfileDetails", "~~~~" + map);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ProfileModel> call2 = apiInterface.doGetprofileDetails("Bearer " + userAccessToken,Utils.POST_ACCEPT_CONTENT_TYPE,map);
        showProgressDialog(getActivity());
        call2.enqueue(new Callback<ProfileModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<ProfileModel> call, @NonNull Response<ProfileModel> response) {
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Profile listed successfully.")) {

                        edtFname.setText(Objects.requireNonNull(response.body()).getProfileData().getFirstName());
                        edtLname.setText(Objects.requireNonNull(response.body()).getProfileData().getLastName());
                        edtEmail.setText(Objects.requireNonNull(response.body()).getProfileData().getEmail());
                        edtMobile.setText(Objects.requireNonNull(response.body()).getProfileData().getCountryCode()+Objects.requireNonNull(response.body()).getProfileData().getPhoneNumber());
                        edtLanguage.setText(Objects.requireNonNull((Objects.requireNonNull(response.body())).getProfileData().getLanguage().toString()));
                        edtCurrency.setText(Objects.requireNonNull((Objects.requireNonNull(response.body())).getProfileData().getCurrency().toString()));

                        String getProfPic = String.valueOf(Objects.requireNonNull(response.body()).getProfileData().getAvatar());
                        getProfPic = getProfPic.replaceAll(" ","%20");
                        ivUserPro.setImageURI(Utils.BASE_URL+getProfPic);
                        ivUserPro.getHierarchy().setRoundingParams(roundingParams);
                        ivBackcoverUserPro.setImageURI(getProfPic);

                    }else {
                        callRefreshToken("viewprofile");
                    }
                    hideProgressDialog();
                }else if (response.code() == RESPONSECODE_401){

                    callRefreshToken("viewprofile");
                }else if (response.code() == RESPONSECODE_500){

                    callRefreshToken("viewprofile");
                }
            }
            @Override
            public void onFailure(@NonNull Call<ProfileModel> call, @NonNull Throwable t) {
                hideProgressDialog();
                call.cancel();
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
                    if (logout.equalsIgnoreCase("viewprofile")){
                        CallViewProfileDetails();
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

        getState.putString(Utils.SP_USER_ACCESSTOKEN,access_token);
        getState.putString(Utils.SP_USER_TOKENTYPE,token_type);
        getState.putString(Utils.SP_USER_TOKENEXPTIME,exp_time);
        getState.apply();
    }
    /*******************************Get Firebase States********************************************/

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
         removeFragment(Utils.FRAGMENT_SETTINGS);
    }

    public void removeFragment(String Tag) {
        Utils.CURRENT_TAG = null;
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(Objects.requireNonNull(fragmentManager.findFragmentByTag(Tag))).commit();
    }


    public void hideKeyBoard()
    {
        // Check if no view has focus:
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }



    public void showTimeOut() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);

        Typeface tf = Typeface.createFromAsset(requireActivity().getAssets(),
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
                (dialog, which) -> clearSPmodePrivate());

        builder.show();
    }

    private void clearSPmodePrivate() {
        SharedPreferences preferences = requireActivity().getSharedPreferences(Utils.MY_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        // deleteCache(MainActivity.this);
        Intent logout = new Intent(requireActivity(), HomeActivity.class);
        logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(logout);
        requireActivity().finish();
    }


    //Delete Account
    private void CallDeleteAccount(DialogInterface dialog) {
        userAccessToken = spPrefs.getString(Utils.SP_USER_ACCESSTOKEN, null);
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", userId);
        map.put("type", "customer");
        map.put("mobile_type", "android");
        map.put("token", userAccessToken);
        Log.i("CallDeleteAccount", "CallDeleteAccount~~~" + map);
        showProgressDialog(requireActivity());
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<DeleteModel> call2 = apiInterface.doGetDeleteDetails(map);
        call2.enqueue(new Callback<DeleteModel>() {
            @Override
            public void onResponse(@NonNull Call<DeleteModel> call, @NonNull Response<DeleteModel> response) {
                hideProgressDialog();
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("successfully unique code sent to mail")) {
                        String emails = response.body().getData().getEmail();
                        String strOtp = String.valueOf(response.body().getData().getUniqueCode());
                        System.out.println("Email-->" + emails);
                        System.out.println("Otp-->" + strOtp);
                        sentVerificationCodeNew(emails, strOtp);
                    } else {
                        //clearSPmodePrivate();
                        Toast.makeText(requireActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else if (response.code() == RESPONSECODE_401) {

                    // clearSPmodePrivate();
                } else if (response.code() == RESPONSECODE_500) {
                    //  clearSPmodePrivate();
                }

                dialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<DeleteModel> call, @NonNull Throwable t) {
                call.cancel();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void deleteAccountDialog() {
        dialogDeleteAccount = new Dialog(requireActivity());
        dialogDeleteAccount.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialogDeleteAccount.setContentView(R.layout.dialog_delete_account);
        Objects.requireNonNull(dialogDeleteAccount.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogDeleteAccount.setCancelable(true);
        dialogDeleteAccount.getWindow().setWindowAnimations(R.style.DialogAnimationLR);

        WindowManager.LayoutParams lps = new WindowManager.LayoutParams();
        lps.copyFrom(dialogDeleteAccount.getWindow().getAttributes());
        lps.width = WindowManager.LayoutParams.MATCH_PARENT;
        lps.height = WindowManager.LayoutParams.WRAP_CONTENT;

        txtCancel = (CustomTextView) dialogDeleteAccount.findViewById(R.id.txt_cancel);
        txtOkay = (CustomTextView) dialogDeleteAccount.findViewById(R.id.txt_okay);
        txtDescription = (CustomTextView) dialogDeleteAccount.findViewById(R.id.txt_description_delete);
        imgDelete = (ImageView) dialogDeleteAccount.findViewById(R.id.img_accept);
        txtDescription.setText(getString(R.string.delete_msg) + " " + getString(R.string.app_name) + " " + getString(R.string.delete_database) + " " + getString(R.string.delete_desc_end));
        dialogDeleteAccount.show();

        ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.RELATIVE_TO_SELF, .5f, ScaleAnimation.RELATIVE_TO_SELF, .5f);
        scale.setDuration(900);
        scale.setInterpolator(new OvershootInterpolator());

        imgDelete.startAnimation(scale);
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDeleteAccount.dismiss();
            }
        });
        txtOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CallDeleteAccount(dialogDeleteAccount);
            }
        });


    }

    private void sentVerificationCodeNew(String strEmail, String strReferralCode) {

        dialogOtp = new Dialog(requireActivity());
        dialogOtp.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialogOtp.setContentView(R.layout.dialog_delete_otp);
        Objects.requireNonNull(dialogOtp.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogOtp.setCancelable(true);
        dialogOtp.getWindow().setWindowAnimations(R.style.DialogAnimationLR);

        WindowManager.LayoutParams lps = new WindowManager.LayoutParams();
        lps.copyFrom(dialogOtp.getWindow().getAttributes());
        lps.width = WindowManager.LayoutParams.MATCH_PARENT;
        lps.height = WindowManager.LayoutParams.WRAP_CONTENT;

        pinOtp = (PinView) dialogOtp.findViewById(R.id.edt_otp);
        txtResend = (CustomTextView) dialogOtp.findViewById(R.id.txt_resendotp);
        btnConfirm = (CustomButton) dialogOtp.findViewById(R.id.btn_confirmsignupotp);
        txtMobile = (CustomTextView) dialogOtp.findViewById(R.id.txt_mobilenum);
        imgBack = (ImageView) dialogOtp.findViewById(R.id.iv_menu_profile);

        txtMobile.setText(strEmail);
        dialogOtp.show();

        ScaleAnimation scale1 = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.RELATIVE_TO_SELF, .5f, ScaleAnimation.RELATIVE_TO_SELF, .5f);
        scale1.setDuration(900);
        scale1.setInterpolator(new OvershootInterpolator());

        imgBack.startAnimation(scale1);


        txtResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pinOtp.setText("");
                callOTPResendChecking();
                imgBack.setAnimation(null);
            }
        });
       /* imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogOtp.dismiss();
                imgBack.setAnimation(null);
            }
        });*/

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgBack.setAnimation(null);
                strPinOtp = pinOtp.getText().toString();
                System.out.println("pinvalue-->" +  strPinOtp);
                if (strPinOtp.length() == 0) {
                    Toast.makeText(requireActivity(), "Enter Otp", Toast.LENGTH_SHORT).show();
                }/*else if (!strPinOtp.equalsIgnoreCase(otp)){
                    Toast.makeText(requireActivity(),"Enter Valid Otp",Toast.LENGTH_SHORT).show();
                }*/ else {
                    getState.putString(Utils.SP_USER_DELETE_ACCOUNT, "1");
                    getState.apply();

                    callVerifyOTP(strPinOtp, dialogOtp);
                }
            }
        });

    }

    private void callOTPResendChecking() {
        userAccessToken = spPrefs.getString(Utils.SP_USER_ACCESSTOKEN, null);
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", userId);
        map.put("type", "customer");
        map.put("mobile_type", "android");
        map.put("token", userAccessToken);
        Log.i("CallDeleteAccount", "CallDeleteAccount~~~" + map);
        showProgressDialog(requireActivity());
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<DeleteModel> call2 = apiInterface.doGetDeleteDetails(map);
        call2.enqueue(new Callback<DeleteModel>() {
            @Override
            public void onResponse(@NonNull Call<DeleteModel> call, @NonNull Response<DeleteModel> response) {
                hideProgressDialog();
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("successfully unique code sent to mail")) {
                        String emails = response.body().getData().getEmail();
                        String strOtp = String.valueOf(response.body().getData().getUniqueCode());
                        System.out.println("Email-->" + emails);
                        System.out.println("Otp-->" + strOtp);
                        // sentVerificationCodeNew(emails,strOtp);
                        Toast.makeText(requireActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        //clearSPmodePrivate();
                        Toast.makeText(requireActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else if (response.code() == RESPONSECODE_401) {

                    // clearSPmodePrivate();
                } else if (response.code() == RESPONSECODE_500) {
                    //  clearSPmodePrivate();
                }

            }

            @Override
            public void onFailure(@NonNull Call<DeleteModel> call, @NonNull Throwable t) {
                call.cancel();
            }
        });
    }

    private void callVerifyOTP(String otp, Dialog dialogOtp) {

        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("id", userId);
        map1.put("type", "customer");
        map1.put("unique_code", otp);
        Log.i("callVerifyOTP", "~~~~" + map1);

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<DeleteModel> call2 = apiInterface.doGetDeleteConfirmDetails(map1);
        showProgressDialog(requireActivity());
        call2.enqueue(new Callback<DeleteModel>() {
            @Override
            public void onResponse(@NonNull Call<DeleteModel> call, @NonNull Response<DeleteModel> response) {
                hideProgressDialog();
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    if (Objects.requireNonNull(response.body()).getResultMessage().equalsIgnoreCase("success")) {
                        // Toast.makeText(requireActivity(),response.body().getMessage(),Toast.LENGTH_LONG).show();

                        dialogOtp.dismiss();
                        startRepeatingTask();
                        showToastAlertDialog(response.body().getMessage());
                        // clearSPmodePrivate();
                    } else {
                        Toast.makeText(requireActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<DeleteModel> call, @NonNull Throwable t) {
                call.cancel();
            }
        });
    }

    public void showToastAlertDialog(String strMsg) {

        dialogToast = new Dialog(requireActivity());
        dialogToast.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialogToast.setContentView(R.layout.dialog_toast);
        dialogToast.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogToast.setCancelable(false);
        dialogToast.getWindow().setWindowAnimations(R.style.DialogAnimationLR);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogToast.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        CustomTextView txtMsg = (CustomTextView) dialogToast.findViewById(R.id.txt_msg);
        CustomButton btnOkay = (CustomButton) dialogToast.findViewById(R.id.btn_okay);
        txtMsg.setText(strMsg);

        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                success = false;
                stopRepeatingTask();
                dialogToast.dismiss();
                clearSPmodePrivate();
            }
        });
        success = true;
        dialogToast.show();
        dialogToast.getWindow().setAttributes(lp);
    }

    private Handler mHandler2 = new Handler();
    Runnable mHandlerTask = new Runnable() {
        @Override
        public void run() {
            Log.i(TAG, "Inside called---Run>");
            /*stopRepeatingTask();
            clearSPmodePrivate();*/
            System.out.println("insideboolean-->" + success);
            if (success) {
                success = false;
                clearSPmodePrivate();
                stopRepeatingTask();
            }

            mHandler2.postDelayed(mHandlerTask, Utils.DELETE_ACCOUNT_CALL_INTERVAL);
        }
    };

    void startRepeatingTask() {
        Log.i(TAG, "startRepeatingTask: ");
        if (mHandlerTask != null)
            mHandlerTask.run();
    }

    void stopRepeatingTask() {
        Log.i(TAG, "stopRepeatingTask: ");
        if (mHandlerTask != null)
            mHandler2.removeCallbacks(mHandlerTask);
    }
    //Delete Account
}
