package com.instaguard.user.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.instaguard.user.Activities.BaseActivity;
import com.instaguard.user.CustomViews.CustomButton;
import com.instaguard.user.CustomViews.CustomEditText;
import com.instaguard.user.CustomViews.CustomTextView;
import com.instaguard.user.Helpers.CountryCode.CountryCodeDialog;
import com.instaguard.user.Helpers.CountryCode.CountryCodePicker;
import com.instaguard.user.Helpers.Logger;
import com.instaguard.user.Helpers.Retrofit.APIClient;
import com.instaguard.user.Helpers.Retrofit.APIInterface;
import com.instaguard.user.Helpers.Utility;
import com.instaguard.user.Helpers.Utils;
import com.instaguard.user.Models.ForgetPassModel;
import com.instaguard.user.Models.ForgotPasswordModel;
import com.instaguard.user.Models.LoginModel;
import com.instaguard.user.Models.ResetPasswordModel;
import com.instaguard.user.Models.VerifyMobileModel;
import com.instaguard.user.Models.VerifyOtpModel;
import com.instaguard.user.R;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;
import static com.instaguard.user.Activities.BaseActivity.hideProgressDialog;
import static com.instaguard.user.Activities.BaseActivity.showProgressDialog;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_200;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_201;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_401;

public class ForgotPasswordFragment extends Fragment {

    private static final String TAG = "ForgotPasswordFragment";
    View view;
    ImageButton imgbtnBack;
    ImageButton imgbtnClearEmail, imgbtnClearPassword, imgbtnClearCnfPassword;

    CustomButton btnResetForgot, btnConfirmForgot;
    CustomEditText edtCountryCode, edtMobile, edtEmail, edtResetpwd, edtCnfResetpwd;
    CountryCodePicker ccp;
    RelativeLayout rlMbileVerification, rlEmailVerification;
    SharedPreferences OTP_shared_status;
    String strCountyCode, strMobile, UserId, OTP_STATUS, WEB_OTP;
    private AwesomeValidation Validation;
    Dialog OTPDialog;

    String userId;
    LinearLayout llMobilelyt, llResetPasswordLyt;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth auth;
    Utility utility;
    String verificationID = "";
    boolean resend = false;
    private boolean boolSignupBtnClick = false;
    Dialog dialogBuilder;
    BaseActivity baseActivity;
    FrameLayout contentView;


    public ForgotPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        auth = FirebaseAuth.getInstance();
        utility = new Utility();
        baseActivity = new BaseActivity();

        imgbtnBack = (ImageButton) view.findViewById(R.id.imgbtn_back);
        btnConfirmForgot = (CustomButton) view.findViewById(R.id.btn_confirmforgot);
        btnResetForgot = (CustomButton) view.findViewById(R.id.btn_resetforgot);

        ccp = (CountryCodePicker) view.findViewById(R.id.ccp);

        edtCountryCode = (CustomEditText) view.findViewById(R.id.edt_country_code);
        edtMobile = (CustomEditText) view.findViewById(R.id.edt_mobile);
        edtEmail = (CustomEditText) view.findViewById(R.id.edt_email);

        edtResetpwd = (CustomEditText) view.findViewById(R.id.edt_resetpwd);
        edtCnfResetpwd = (CustomEditText) view.findViewById(R.id.edt_confirepwd);

        llMobilelyt = (LinearLayout) view.findViewById(R.id.ll_mobile_lyt);
        llResetPasswordLyt = (LinearLayout) view.findViewById(R.id.ll_reset_password);

        rlEmailVerification = (RelativeLayout) view.findViewById(R.id.rl_email_verification);

        imgbtnClearEmail = (ImageButton) view.findViewById(R.id.imgbtn_clear_email);
        imgbtnClearPassword = (ImageButton) view.findViewById(R.id.imgbtn_clear_pwd);
        imgbtnClearCnfPassword = (ImageButton) view.findViewById(R.id.imgbtn_clear_cnf_pwd);
        contentView = view.findViewById(R.id.frame_forgot);

        // validation initalize
        Validation = new AwesomeValidation(BASIC);

        onClearViews(edtEmail, imgbtnClearEmail);
        onClearViews(edtResetpwd, imgbtnClearPassword);
        onClearViews(edtCnfResetpwd, imgbtnClearCnfPassword);
        OnClick();

        onBackButton(view);
        return view;
    }

    private void onClearViews(CustomEditText customEditText, ImageButton imgBtnClear) {

        customEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    imgBtnClear.setVisibility(View.GONE);
                } else {
                    imgBtnClear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    imgBtnClear.setVisibility(View.GONE);
                } else {
                    imgBtnClear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        imgBtnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customEditText.setText("");
            }
        });

        customEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.e(TAG, "onFocusChange: ~~~~" + hasFocus);
                if (!hasFocus) {
                    imgBtnClear.setVisibility(View.GONE);
                } else {
                    imgBtnClear.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void OnClick() {

        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                edtCountryCode.setText(ccp.getSelectedCountryCodeWithPlus());
            }
        });

        edtCountryCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CountryCodeDialog.openCountryCodeDialog(ccp);
            }
        });

        imgbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard();
                backButtonFunction();
            }
        });

        btnConfirmForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard();
                String edtemail = edtEmail.getText().toString().trim();
                String strmobile = edtMobile.getText().toString().trim();
                String strcc = edtCountryCode.getText().toString().trim();


                if (!isEmailValid(edtEmail.getText().toString())) {
                    edtEmail.setError(getString(R.string.register_error_email));
                } else {
                    callForgotPasswordDetail(edtemail);
                }

                /*if (!validatePhone()) {
                    showSnackbar(getString(R.string.error_all_fields_required), "Field");
                } else if (!validateCountryCode()) {
                    showSnackbar(getString(R.string.error_all_fields_required), "Field");
                } else if (!validateUsing_libphonenumber()) {
                    edtMobile.setError(getString(R.string.register_error_invalid_mobile_number));
                }else {
                callMobileNumberVerification(strcc,strmobile);
                }*/
            }
        });


        btnResetForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*String strPwd = edtResetpwd.getText().toString().trim();
                String strCnfPwd = edtCnfResetpwd.getText().toString().trim();
                if (strPwd.equals(strCnfPwd)) {
                    callResetPasswordUpdate(strCnfPwd);
                } else {
                    showSnackbar(getString(R.string.forgoet_password_reset_missmatch), "warning");
                }*/

                String strPwd = Objects.requireNonNull(edtResetpwd.getText()).toString().trim();
                String strCnfPwd = Objects.requireNonNull(edtCnfResetpwd.getText()).toString().trim();

                //String regexPassword = "(?=.*[a-z])(?=.*[0-9]).{8,}";

                //Validation.addValidation(edtResetpwd, regexPassword, getResources().getString(R.string.log_in_error_password));
                //Validation.addValidation(edtCnfResetpwd, regexPassword, getResources().getString(R.string.log_in_error_password));

                //if (Validation.validate()) {
                if (strPwd.isEmpty()) {
                    baseActivity.showSnackbar(requireActivity(),contentView,getString(R.string.error_all_fields_required), "warning");
                } else if (strCnfPwd.isEmpty()) {
                    baseActivity.showSnackbar(requireActivity(),contentView,getString(R.string.error_all_fields_required), "warning");
                } else {
                    if (strPwd.equals(strCnfPwd)) {
                        callResetPasswordUpdate(strCnfPwd);
                    } else {
                        baseActivity.showSnackbar(requireActivity(),contentView,getString(R.string.forgoet_password_reset_missmatch), "warning");
                    }
                }
               /* } else {
                    showSnackbar(getString(R.string.error_required), "warning");
                }*/

            }
        });
    }

    @SuppressLint("LongLogTag")
    private void callResetPasswordUpdate(String strCnfPwd) {
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("id", userId);
        map1.put("password", strCnfPwd);
        map1.put("mode", "customer");
        Log.i("callResetPasswordUpdate~~", "~~~~" + map1);
        showProgressDialog(getActivity());
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ResetPasswordModel> call2 = apiInterface.doUpdateResetPassword(map1);
        call2.enqueue(new Callback<ResetPasswordModel>() {
            @Override
            public void onResponse(@NonNull Call<ResetPasswordModel> call, @NonNull Response<ResetPasswordModel> response) {
                hideProgressDialog();
                Log.i(TAG, "onResponse: resonse code--> " + response.code());
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Customer password updated successfuly.")) {
                        showProfileUpdateAlertDialog(getActivity(), getString(R.string.editsettings_update_successfully));
                    } else {
                        baseActivity.showSnackbar(requireActivity(),contentView,getString(R.string.try_again), "warning");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResetPasswordModel> call, Throwable t) {
                call.cancel();
            }
        });

    }

    @SuppressLint("LongLogTag")
    private void callMobileNumberVerification(String strcc, String strmobile) {
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("phone_number", strmobile);
        map1.put("mode", "customer");
        Log.i("callForgotPasswordDetail~~", "~~~~" + map1);
        showProgressDialog(getActivity());
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<VerifyMobileModel> call2 = apiInterface.doUpdateverifyMobile(map1);
        call2.enqueue(new Callback<VerifyMobileModel>() {
            @Override
            public void onResponse(@NonNull Call<VerifyMobileModel> call, @NonNull Response<VerifyMobileModel> response) {
                hideProgressDialog();
                Log.i(TAG, "onResponse: resonse code--> " + response.code());
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Customer available with the given phone number.")) {
                        //showProfileUpdateAlertDialog(getActivity(), getString(R.string.editsettings_update_successfully));
                        userId = String.valueOf(Objects.requireNonNull(response.body()).getId());
                        // llMobilelyt.setVisibility(View.GONE);
                        //llResetPasswordLyt.setVisibility(View.VISIBLE);
                        showProgressDialog(getActivity());
                        sentVerificationCode(strcc, strmobile);  //vignesh
                    } else {
                        llMobilelyt.setVisibility(View.VISIBLE);
                        llResetPasswordLyt.setVisibility(View.GONE);
                        baseActivity.showSnackbar(requireActivity(),contentView,getString(R.string.forgot_password_enter_register_mobile), "warning");
                    }
                } else {
                    llMobilelyt.setVisibility(View.VISIBLE);
                    llResetPasswordLyt.setVisibility(View.GONE);
                    baseActivity.showSnackbar(requireActivity(),contentView,getString(R.string.forgot_password_Internet), "warning");
                }
            }

            @Override
            public void onFailure(@NonNull Call<VerifyMobileModel> call, Throwable t) {
                call.cancel();
            }
        });

    }

    @SuppressLint("LongLogTag")
    private void callForgotPasswordDetail(String edtemail) {

        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("email", edtemail);
        map1.put("mode", "customer");
        Log.i(TAG, "forgetpass~~~~" + map1);
        showProgressDialog(getActivity());
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ForgetPassModel> call2 = apiInterface.doForgotPassword(map1);
        call2.enqueue(new Callback<ForgetPassModel>() {
            @Override
            public void onResponse(@NonNull Call<ForgetPassModel> call, @NonNull Response<ForgetPassModel> response) {
                hideProgressDialog();
                Log.i(TAG, "onResponse: resonse code--> " + response.code());
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Mail sent successfully")) {
                        //showProfileUpdateAlertDialog(getActivity(), getString(R.string.editsettings_update_successfully));
                        showVerifyOTPDialog1(edtemail);

                    } else if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Invalid mail id")) {
                        baseActivity.showSnackbar(requireActivity(),contentView,getString(R.string.log_in_error_email), "warning");
                    }
                } else if (response.code() == RESPONSECODE_401) {
                    baseActivity.showSnackbar(requireActivity(),contentView,getString(R.string.log_in_error_email), "warning");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ForgetPassModel> call, Throwable t) {
                call.cancel();
            }
        });
    }

    @SuppressLint("LongLogTag")
    private void callResendEmailDetail(String edtemail) {

        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("email", edtemail);
        map1.put("mode", "customer");
        Log.i(TAG, "forgetpass~~~~" + map1);
        showProgressDialog(getActivity());
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ForgetPassModel> call2 = apiInterface.doForgotPassword(map1);
        call2.enqueue(new Callback<ForgetPassModel>() {
            @Override
            public void onResponse(@NonNull Call<ForgetPassModel> call, @NonNull Response<ForgetPassModel> response) {
                hideProgressDialog();
                Log.i(TAG, "onResponse: resonse code--> " + response.code());
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Mail sent successfully")) {
                        //showProfileUpdateAlertDialog(getActivity(), getString(R.string.editsettings_update_successfully));
                        // String strOtp="1234";
                        //showVerifyOTPDialog1(edtemail);
                        baseActivity.showSnackbar(requireActivity(),contentView,response.body().getMessage(), "warning");

                    } else if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Invalid Mail id")) {
                        baseActivity.showSnackbar(requireActivity(),contentView,getString(R.string.log_in_error_email), "warning");
                    }
                } else if (response.code() == RESPONSECODE_401) {
                    baseActivity.showSnackbar(requireActivity(),contentView,getString(R.string.log_in_error_email), "warning");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ForgetPassModel> call, Throwable t) {
                call.cancel();
            }
        });
    }

    @SuppressLint("LongLogTag")
    private void callConfirmOtpDetail(String edtemail, String otp) {

        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("email", edtemail);
        map1.put("mode", "customer");
        map1.put("otp", otp);
        Log.i("callForgotPasswordDetail~~", "~~~~" + map1);
        showProgressDialog(getActivity());
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<VerifyOtpModel> call2 = apiInterface.doVerifyOtp(map1);
        call2.enqueue(new Callback<VerifyOtpModel>() {
            @Override
            public void onResponse(@NonNull Call<VerifyOtpModel> call, @NonNull Response<VerifyOtpModel> response) {
                hideProgressDialog();
                Log.i(TAG, "onResponse: resonse code--> " + response.code());
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Code verified successfuly")) {
                        userId = String.valueOf(Objects.requireNonNull(response.body()).getResult().getId());
                        System.out.println("inside successfully" + userId);
                        dialogBuilder.dismiss();
                        llResetPasswordLyt.setVisibility(View.VISIBLE);
                        llMobilelyt.setVisibility(View.GONE);

                    } else if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Invalid OTP Code")) {
                        //showSnackbar(Objects.requireNonNull(response.body()).getMessage(), "warning");
                        Toast.makeText(getActivity(), Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();

                        llMobilelyt.setVisibility(View.VISIBLE);
                        llResetPasswordLyt.setVisibility(View.GONE);
                    }
                } else if (response.code() == RESPONSECODE_401) {
                    //showSnackbar("Invalid OTP Code", "warning");
                    Toast.makeText(getActivity(), "Invalid OTP Code", Toast.LENGTH_SHORT).show();
                    llMobilelyt.setVisibility(View.VISIBLE);
                    llResetPasswordLyt.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<VerifyOtpModel> call, Throwable t) {
                call.cancel();
            }
        });
    }

    private void showProfileUpdateAlertDialog(Activity self, String message) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(self);
        builder.setCancelable(false);
        Typeface tf = Typeface.createFromAsset(self.getAssets(),
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
                        dialog.dismiss();
                        Utils.switchContent(R.id.frame_launch_container,
                                Utils.FRAGMENT_SIGNIN, Objects.requireNonNull(getActivity()), null,
                                Utils.AnimationType.SLIDE_IN_SLIDE_OUT);
                    }
                }).show();
    }


    @SuppressLint("ObsoleteSdkInt")
    private boolean validateUsing_libphonenumber() {
        if (edtMobile.getText().toString().length() <= 1) {
            return false;
        } else if (edtMobile.getText().toString().substring(0, 1).matches("0")) {
            edtMobile.setError(getString(R.string.register_error_enter_validnum));
            return false;
        } else {
            strCountyCode = edtCountryCode.getText().toString();
            strMobile = edtMobile.getText().toString();
            if (validatePhone() && validateCountryCode()) {
                System.out.println("CountryCode==>" + strCountyCode);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    strCountyCode = strCountyCode.replace("+", "");
                }
                System.out.println("SDK_VERSION==>" + Build.VERSION.SDK_INT);
                System.out.println("SDK_VERSION_RELEASE" + Build.VERSION.RELEASE);
                System.out.println("CountryCode1==>" + strCountyCode);
                PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
                String isoCode = phoneNumberUtil.getRegionCodeForCountryCode(Integer.parseInt(strCountyCode));
                Phonenumber.PhoneNumber phoneNumber = null;

                try {
                    //phoneNumber = phoneNumberUtil.parse(phNumber, "IN");  //if you want to pass region code
                    phoneNumber = phoneNumberUtil.parse(strMobile, isoCode);
                    boolean isValid = phoneNumberUtil.isValidNumber(phoneNumber);
                    if (isValid) {
                        String internationalFormat = phoneNumberUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
                        return true;
                    } else {
                        edtMobile.setError(getString(R.string.register_error_enter_a_valid_mobile_number));
                        return false;
                    }
                } catch (NumberParseException e) {
                    System.err.println(e);
                }

            }
            return true;
        }

    }

    private boolean validateCountryCode() {

        if (edtCountryCode.getText().toString().trim().isEmpty()) {
            edtCountryCode.setError(getString(R.string.register_error_enter_country_code));
            return false;
        } else if (edtCountryCode.getText().toString().equals("CC")) {
            edtCountryCode.setError(getString(R.string.register_error_enter_country_code));
            return false;
        } else {
            edtCountryCode.setError(null);
        }
        return true;
    }

    private boolean validatePhone() {

        if (edtMobile.getText().toString().trim().isEmpty()) {
            edtMobile.setError(getString(R.string.register_error_enter_mobile_number));
            return false;
        } else if (edtCountryCode.getText().toString().trim().isEmpty()) {
            return false;
        } else if (!edtMobile.getText().toString().trim().isEmpty()) {
            if (edtMobile.getText().toString().substring(0, 1).matches("0")) {
                edtMobile.setError(getString(R.string.register_error_enter_validnum));
                return false;
            } else {
                int maxLengthofEditText = 15;
                edtMobile.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLengthofEditText)});
                edtMobile.setError(null);
            }
            return true;
        }

        return true;
    }

    private void sentVerificationCode(String strCCode, String phone) {
        Log.e("sentVerificationCode~~", strCCode + phone);
        String phoneNo = strCCode + phone;
        Logger.printDebug("codeeee___" + phoneNo);
        callback_verificvation(strCCode, phone);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNo, 60, TimeUnit.SECONDS, Objects.requireNonNull(getActivity()), mCallbacks);
    }

    private void callback_verificvation(String strCCode, String phone) {

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Logger.printDebug("Verification_complete");
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Logger.printDebug(e.getMessage());
                System.out.println("enter the exeption otp verfication" + e.getMessage());

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Utility.showToastMessage(getContext(), "failed Sending OTP_InvalidRequest");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Utility.showToastMessage(getContext(), "failed Sending OTP_ManyRequest");
                } else {
                    Utility.showToastMessage(getContext(), "verification message Sending failed..");
                }
                boolSignupBtnClick = false;
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                Logger.printDebug("code send");
                Logger.printDebug(verificationId);
                verificationID = verificationId;
                if (!resend) {
                    showVerifyOTPDialog(strCCode, phone);
                }
            }
        };
    }

    @SuppressLint("SetTextI18n")
    private void showVerifyOTPDialog(String strCCode, String phone) {

        dialogBuilder = new Dialog(getActivity(), R.style.AppTheme);
        dialogBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialogBuilder.setContentView(R.layout.layout_account_verification);
        dialogBuilder.setCancelable(true);
        dialogBuilder.getWindow().setWindowAnimations(R.style.DialogAnimationLR);

        CustomEditText etOTP = (CustomEditText) dialogBuilder.findViewById(R.id.edt_otp);
        CustomTextView btnVerify = (CustomTextView) dialogBuilder.findViewById(R.id.btn_confirmsignupotp);
        ImageView ivCancel = (ImageView) dialogBuilder.findViewById(R.id.imgbtn_back);
        final TextView etResend = (TextView) dialogBuilder.findViewById(R.id.txt_resendotp);

        final TextView txt_mobilenum = (TextView) dialogBuilder.findViewById(R.id.txt_mobilenum);

        txt_mobilenum.setText(strCCode + phone);

        new CountDownTimer(30000, 1000) {

            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                etResend.setText(String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
                etResend.setEnabled(false);
            }

            @SuppressLint("SetTextI18n")
            public void onFinish() {
                etResend.setText("RESEND CODE");
                etResend.setEnabled(true);
            }
        }.start();

        dialogBuilder.setCancelable(false);
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
            }
        });

        etResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resend = true;
                new CountDownTimer(30000, 1000) {

                    @SuppressLint({"DefaultLocale", "SetTextI18n"})
                    public void onTick(long millisUntilFinished) {
                        int seconds = (int) (millisUntilFinished / 1000);
                        int minutes = seconds / 60;
                        seconds = seconds % 60;
                        etResend.setText(String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
                        etResend.setEnabled(false);
                    }

                    public void onFinish() {
                        etResend.setText("RESEND CODE");
                        etResend.setEnabled(true);
                        //etResend.setBackgroundColor(Color.rgb(55,103,228));
                    }
                }.start();
                sentVerificationCode(strCCode, phone);
            }
        });
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("verificationID--" + verificationID);
                System.out.println("etOTP--" + etOTP.getText().toString().trim());
                if (etOTP.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please Enter Verification Code", Toast.LENGTH_SHORT).show();
                } else {
                    dialogBuilder.dismiss();
                    showProgressDialog(getActivity());
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, etOTP.getText().toString().trim());
                    System.out.println("credential--" + credential);
                    verifyOTP(credential);
                }
            }
        });
        hideProgressDialog();
        dialogBuilder.show();
    }

    //firebase verify otp
    private void verifyOTP(PhoneAuthCredential credential) {
        System.out.println("credential----" + credential);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(Objects.requireNonNull(getActivity()), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Logger.printDebug("signInWithCredential:success");
                            resend = false;
                            hideProgressDialog();
                            llMobilelyt.setVisibility(View.GONE);
                            llResetPasswordLyt.setVisibility(View.VISIBLE);
                        } else {
                            dialogBuilder.dismiss();
                            llMobilelyt.setVisibility(View.VISIBLE);
                            llResetPasswordLyt.setVisibility(View.GONE);
                            Logger.printDebug(Objects.requireNonNull(task.getException()).toString());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Logger.printDebug("Invalid");
                                System.out.println("yayini" + task.getException().getMessage());
                                Utility.showToastMessage(getActivity(), task.getException().getMessage());
                            }
                        }
                    }
                });
    }

    @SuppressLint("SetTextI18n")
    private void showVerifyOTPDialog1(String email) {

        dialogBuilder = new Dialog(getActivity(), R.style.AppTheme);
        dialogBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialogBuilder.setContentView(R.layout.layout_account_verification1);
        dialogBuilder.setCancelable(true);
        dialogBuilder.getWindow().setWindowAnimations(R.style.DialogAnimationLR);

        CustomEditText etOTP = (CustomEditText) dialogBuilder.findViewById(R.id.edt_otp);
        CustomTextView btnVerify = (CustomTextView) dialogBuilder.findViewById(R.id.btn_confirmsignupotp);
        ImageView ivCancel = (ImageView) dialogBuilder.findViewById(R.id.imgbtn_back);
        final TextView etResend = (TextView) dialogBuilder.findViewById(R.id.txt_resendotp);

        final TextView txt_mobilenum = (TextView) dialogBuilder.findViewById(R.id.txt_mobilenum);
        final ImageButton imgbtnClearOtp = (ImageButton) dialogBuilder.findViewById(R.id.imgbtn_clear_otp);

        txt_mobilenum.setText(email);

        onClearViews(etOTP, imgbtnClearOtp);

        dialogBuilder.setCancelable(false);

        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
            }
        });

        etResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // resend = true;
                callResendEmailDetail(email);
            }
        });
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // System.out.println("verificationID--" + verificationID);
                System.out.println("etOTP--" + etOTP.getText().toString().trim());
                if (etOTP.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please Enter Verification Code", Toast.LENGTH_SHORT).show();
                } else {

                    callConfirmOtpDetail(email, etOTP.getText().toString());

                   /* llMobilelyt.setVisibility(View.GONE);
                    llResetPasswordLyt.setVisibility(View.VISIBLE);*/
                }
            }
        });
        hideProgressDialog();
        dialogBuilder.show();
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
        Utils.switchContent(R.id.frame_launch_container,
                Utils.FRAGMENT_SIGNIN,
                getActivity(), null,
                Utils.AnimationType.SLIDE_IN_SLIDE_OUT_RIGHT);
    }


    public void hideKeyBoard() {
        // Check if no view has focus:
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static boolean isEmailValid(String email) {
        boolean check;
        Pattern p;
        Matcher m;

        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        p = Pattern.compile(EMAIL_STRING);

        m = p.matcher(email);
        check = m.matches();

        return check;
    }
}
