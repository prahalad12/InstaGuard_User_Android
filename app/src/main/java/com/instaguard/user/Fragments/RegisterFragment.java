package com.instaguard.user.Fragments;

import static android.content.Context.MODE_PRIVATE;
import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;
import static com.instaguard.user.Activities.BaseActivity.hideProgressDialog;
import static com.instaguard.user.Activities.BaseActivity.showProgressDialog;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_200;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_201;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.os.ConfigurationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
import com.instaguard.user.Models.EmailMobileCheckingModel;
import com.instaguard.user.Models.RegisterModel;
import com.instaguard.user.R;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterFragment extends Fragment {

    View view;

    CustomEditText edtFirstName, edtLastName, edtEmail, edtMobile, edtReferralcode, edtPassword, edtCountryCode;
    CustomButton btnConfirmSignup;
    ImageButton imgbtnBack;
    CustomTextView txtAlreadySignin,txtTermsService, txtPrivacyPolicy;
    CountryCodePicker ccp;
    CheckBox cbIAgree;

    ImageButton imgbtnClearFirstName, imgbtnClearLastName, imgbtnClearEmail, imgbtnClearPassword, imgbtnClearMobile, imgbtnClearReflCode;

    public String TAG = "RegisterFragment";
    String deviceId;
    SharedPreferences sprefsGlobalDeviceId;

    String strCountyCode, strMobile, strEmail;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth auth;
    Utility utility;
    String verificationID = "";
    boolean resend = false;
    Dialog dialogBuilder;

    private AwesomeValidation Validation;

    private String strLanguage;
    BaseActivity baseActivity;
    FrameLayout contentView;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_register, container, false);

        auth = FirebaseAuth.getInstance();
        utility = new Utility();
        baseActivity = new BaseActivity();

        sprefsGlobalDeviceId = requireActivity().getSharedPreferences(Utils.GLOBAL_STORED_ID, MODE_PRIVATE);
        deviceId = sprefsGlobalDeviceId.getString(Utils.SP_DEVICE_ID, null);

        Log.e(TAG, "token in RegisterFragment--> " + deviceId);
        if (deviceId == null)
            deviceId = "123";

        SharedPreferences getspCommonCurrency = requireActivity().getSharedPreferences(Utils.PREFS_NAME_CURRENCY, MODE_PRIVATE);
        strLanguage = getspCommonCurrency.getString(Utils.SP_LANGUAGE, null);
        if (strLanguage == null) {
            strLanguage = "English";
            //   strLanguage = "Spanish";
        }

        contentView = view.findViewById(R.id.frame_signup);
        edtFirstName =  view.findViewById(R.id.edt_firstname);
        edtLastName =  view.findViewById(R.id.edt_lastname);
        edtEmail =  view.findViewById(R.id.edt_email);
        edtCountryCode =  view.findViewById(R.id.edt_country_code);
        edtMobile =  view.findViewById(R.id.edt_mobile);
        edtPassword =  view.findViewById(R.id.edt_password);
        edtReferralcode =  view.findViewById(R.id.edt_referralcode);
        cbIAgree =  view.findViewById(R.id.cb_i_agree);

        ccp =  view.findViewById(R.id.ccp);

        txtAlreadySignin =  view.findViewById(R.id.txt_alreadysignin);

        imgbtnBack =  view.findViewById(R.id.imgbtn_back);
        btnConfirmSignup =  view.findViewById(R.id.btn_confirmsignup);

        imgbtnClearFirstName =  view.findViewById(R.id.imgbtn_clear_first_name);
        imgbtnClearLastName =  view.findViewById(R.id.imgbtn_clear_last_name);
        imgbtnClearEmail =  view.findViewById(R.id.imgbtn_clear_email);
        imgbtnClearPassword =  view.findViewById(R.id.imgbtn_clear_pwd);
        imgbtnClearMobile =  view.findViewById(R.id.imgbtn_clear_mobile);
        imgbtnClearReflCode =  view.findViewById(R.id.imgbtn_clear_referralcode);

        txtPrivacyPolicy = view.findViewById(R.id.txt_i_agree_ll);
        txtTermsService = view.findViewById(R.id.txt_i_agree_llll);


        // validation initalize
        Validation = new AwesomeValidation(BASIC);

        OnClick();

        onClearViews(edtFirstName, imgbtnClearFirstName);
        onClearViews(edtLastName, imgbtnClearLastName);
        onClearViews(edtEmail, imgbtnClearEmail);
        onClearViews(edtMobile, imgbtnClearMobile);
        onClearViews(edtPassword, imgbtnClearPassword);
        onClearViews(edtReferralcode, imgbtnClearReflCode);

        onBackButton(view);

        return view;

    }


    private void GetCountryZipCode() {

        String CountryZipCode = "";
        Locale locales = ConfigurationCompat.getLocales(Resources.getSystem().getConfiguration()).get(0);

//        Log.e(TAG, "onCreateView: ~~~~~~3~~~~~~~~"+locales);
//        Log.e(TAG, "onCreateView: ~~~~~~4~~~~~~~~"+locales.getDisplayName());
//        Log.e(TAG, "onCreateView: ~~~~~~5~~~~~~~~"+locales.getLanguage());
        Log.e(TAG, "onCreateView: ~~~~~~6~~~~~~~~" + locales.getCountry());
        Log.e(TAG, "onCreateView: ~~~~~~7~~~~~~~~" + locales.getDisplayCountry());

        String[] rl = requireActivity().getResources().getStringArray(R.array.CountryCodes);

        for (int i = 0; i < rl.length; i++) {
            String[] g = rl[i].split(",");
            if (g[1].trim().equals(locales.getCountry().trim())) {
                CountryZipCode = g[0];
                break;
            }
        }

        edtCountryCode.setText(CountryZipCode);

        Log.e(TAG, "GetCountryZipCode: ~~~~~CountryZipCode~~~~~~" + CountryZipCode);
    }


    private void OnClick() {

        if (strLanguage != null) {
            if (strLanguage.equals("Sinhala")) {
                ccp.changeLanguage(CountryCodePicker.Language.SINHALA);
            } else {
                ccp.changeLanguage(CountryCodePicker.Language.ENGLISH);
            }
        } else {
            ccp.changeLanguage(CountryCodePicker.Language.ENGLISH);
        }

        ccp.setOnCountryChangeListener(() -> edtCountryCode.setText(ccp.getSelectedCountryCodeWithPlus()));

        edtCountryCode.setOnClickListener(view -> CountryCodeDialog.openCountryCodeDialog(ccp));

        btnConfirmSignup.setOnClickListener(v -> {
            hideKeyBoard();
            btnConfirmSignup.setEnabled(false);
            ValidationForEditText();
        });

        txtAlreadySignin.setOnClickListener(v -> Utils.switchContent(R.id.frame_launch_container,
                Utils.FRAGMENT_SIGNIN,
                requireActivity(), null,
                Utils.AnimationType.SLIDE_IN_SLIDE_OUT));
        imgbtnBack.setOnClickListener(v -> {
            hideKeyBoard();
            backButtonFunction();
        });


        txtPrivacyPolicy.setText(getResources().getString(R.string.privacy));
        txtTermsService.setText(getResources().getString(R.string.terms_of_service));

        SpannableString ss = new SpannableString(txtPrivacyPolicy.getText().toString());

        ClickableSpan clickableTerms = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View textView) {
                // show toast here
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);

            }
        };
        ss.setSpan(clickableTerms, 0, txtPrivacyPolicy.getText().toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtPrivacyPolicy.setText(ss);
        txtPrivacyPolicy.setMovementMethod(LinkMovementMethod.getInstance());
        txtPrivacyPolicy.setHighlightColor(Color.TRANSPARENT);

        SpannableString sss = new SpannableString(txtTermsService.getText().toString());

        ClickableSpan clickableTermss = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View textView) {
                // show toast here
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);

            }
        };
        sss.setSpan(clickableTermss, 0, txtTermsService.getText().toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtTermsService.setText(sss);
        txtTermsService.setMovementMethod(LinkMovementMethod.getInstance());
        txtTermsService.setHighlightColor(Color.TRANSPARENT);


        txtPrivacyPolicy.setOnClickListener(view -> {
            Bundle args = new Bundle();
            args.putString(Utils.B_SUPPORT_FROM,"register");
            Utils.switchContent(R.id.frame_signup,
                    Utils.FRAGMENT_SUPPORT,
                    requireActivity(), args,
                    Utils.AnimationType.SLIDE_IN_SLIDE_OUT);
        });
        txtTermsService.setOnClickListener(view -> {
            Bundle args = new Bundle();
            args.putString(Utils.B_SUPPORT_FROM,"register");
            Utils.switchContent(R.id.frame_signup,
                    Utils.FRAGMENT_SUPPORT,
                    requireActivity(), args,
                    Utils.AnimationType.SLIDE_IN_SLIDE_OUT);
        });


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

        imgBtnClear.setOnClickListener(v -> customEditText.setText(""));

        customEditText.setOnFocusChangeListener((v, hasFocus) -> {
            Log.e(TAG, "onFocusChange: ~~~~" + hasFocus);
            if (!hasFocus) {
                imgBtnClear.setVisibility(View.GONE);
            } else {
                imgBtnClear.setVisibility(View.VISIBLE);
            }
        });

    }

    //validation controller
    private void ValidationForEditText() {

        // String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";
        String regexPassword = "(?=.*[a-z])(?=.*[0-9]).{8,}";

        Validation.addValidation(requireActivity(), R.id.edt_firstname, "[a-zA-Z\\s]+", R.string.register_error_firstname);
        Validation.addValidation(requireActivity(), R.id.edt_lastname, "[a-zA-Z\\s]+", R.string.register_error_lastname);
        Validation.addValidation(requireActivity(), R.id.edt_mobile, RegexTemplate.TELEPHONE, R.string.register_error_enter_validnum);
        // Validation.addValidation(requireActivity(), R.id.edt_country_code, RegexTemplate.TELEPHONE, R.string.register_error_enter_validcountry_code);
        Validation.addValidation(requireActivity(), R.id.edt_email, Patterns.EMAIL_ADDRESS, R.string.register_error_email);
        // Validation.addValidation(requireActivity(), R.id.edt_password, "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}", R.string.error_password);
        Validation.addValidation(requireActivity(), R.id.edt_password, regexPassword, R.string.log_in_error_password);
        if (Validation.validate()) {
            if (!validatePhone()) {
                baseActivity.showSnackbar(requireActivity(),contentView,getString(R.string.error_all_fields_required), "Field");
                btnConfirmSignup.setEnabled(true);
            } else if (!validateCountryCode()) {
                edtCountryCode.setError(getString(R.string.register_error_enter_validcountry_code));
                baseActivity.showSnackbar(requireActivity(),contentView,getString(R.string.error_all_fields_required), "Field");
                btnConfirmSignup.setEnabled(true);
            } else if (!validateUsing_libphonenumber()) {
                edtMobile.setError(getString(R.string.register_error_invalid_mobile_number));
                btnConfirmSignup.setEnabled(true);
            } else {

                strEmail = edtEmail.getText().toString();
                strCountyCode = edtCountryCode.getText().toString().trim();
                strMobile = edtMobile.getText().toString().trim();
                strEmail = strEmail.replaceAll("%40", "");
                strEmail = strEmail.replaceAll("%20", "");

                if (cbIAgree.isChecked()) {
                    callMobileEmailChecking(strCountyCode, strMobile, strEmail);
                } else {
                    btnConfirmSignup.setEnabled(true);
                    baseActivity.showSnackbar(requireActivity(),contentView,getString(R.string.register_terms_condition), "warning");
                }
            }

        } else {
            btnConfirmSignup.setEnabled(true);
            onBackButton(view);
        }
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


    private void callMobileEmailChecking(String WithCC, String mobileNumber, String strEmail) {
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("phone_number", mobileNumber);
        map1.put("email", strEmail);
        map1.put("mode", "customer");
        Log.i("callMobileEmailChecking", "~~~~" + map1);

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<EmailMobileCheckingModel> call2 = apiInterface.doGetEmailMobileChecking(map1);
        showProgressDialog(requireActivity());
        call2.enqueue(new Callback<EmailMobileCheckingModel>() {
            @Override
            public void onResponse(@NonNull Call<EmailMobileCheckingModel> call, @NonNull Response<EmailMobileCheckingModel> response) {

                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Email or Phone Number not exists in our db.")) {
                        //   showProgressDialog(requireActivity());
                        // sentVerificationCode(WithCC, mobileNumber,strEmail);
                        CallRegisterDetails(WithCC, mobileNumber, strEmail);

                    } else if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Phone Number already exists.")) {
                        hideProgressDialog();
                        btnConfirmSignup.setEnabled(true);
                        baseActivity.showSnackbar(requireActivity(),contentView,getString(R.string.register_mobile_already_exist), "warning");
                    } else if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Email already exists.")) {
                        hideProgressDialog();
                        btnConfirmSignup.setEnabled(true);
                        baseActivity.showSnackbar(requireActivity(),contentView,getString(R.string.register_email_already_exist), "warning");
                    } else {
                        hideProgressDialog();
                        btnConfirmSignup.setEnabled(true);
                        baseActivity.showSnackbar(requireActivity(),contentView,getString(R.string.register_email_mobile_already_exist), "warning");
                    }
                } else {
                    hideProgressDialog();
                }
            }

            @Override
            public void onFailure(@NonNull Call<EmailMobileCheckingModel> call, @NonNull Throwable t) {
                call.cancel();
                hideProgressDialog();
            }
        });
    }

    private void CallRegisterDetails(String WithCC, String mobileNumber, String strEmail) {

        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("first_name", edtFirstName.getText().toString());
        map1.put("last_name", edtLastName.getText().toString());
        map1.put("email", strEmail);
        map1.put("country_code", WithCC);
        map1.put("phone_number", mobileNumber);
        map1.put("password", edtPassword.getText().toString());
        map1.put("device_token", deviceId);
        map1.put("referral_code", edtReferralcode.getText().toString());
        Log.i("CallRegisterDetails", "~~~~" + map1);

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<RegisterModel> call2 = apiInterface.doGetRegisterDetails(map1);

        call2.enqueue(new Callback<RegisterModel>() {
            @Override
            public void onResponse(@NonNull Call<RegisterModel> call, @NonNull Response<RegisterModel> response) {

                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {

                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Registered Successfully")) {

                        // String access_token = Objects.requireNonNull(response.body()).getAccessToken();
                        // String token_type = Objects.requireNonNull(response.body()).getTokenType();
                        //  String expires_in = String.valueOf(Objects.requireNonNull(response.body()).getExpiresIn());

                        String UserId = String.valueOf(Objects.requireNonNull(response.body()).getRegisterData().getId());
                        String Firstname = Objects.requireNonNull(response.body()).getRegisterData().getFirstName();
                        String Lastname = Objects.requireNonNull(response.body()).getRegisterData().getLastName();
                        String Email = Objects.requireNonNull(response.body()).getRegisterData().getEmail();
                        String UserProPic = String.valueOf(Objects.requireNonNull(response.body()).getRegisterData().getAvatar());
                        String Mobile = String.valueOf(Objects.requireNonNull(response.body()).getRegisterData().getPhoneNumber());
                        String Countrycode = String.valueOf(Objects.requireNonNull(response.body()).getRegisterData().getCountryCode());

                        /*String Dob = String.valueOf(Objects.requireNonNull(response.body()).getRegisterData().getBirthDate());
                        String State = String.valueOf(Objects.requireNonNull(response.body()).getRegisterData().getStateId());
                        String Country = String.valueOf(Objects.requireNonNull(response.body()).getRegisterData().getCountryId());
                        String Address = String.valueOf(Objects.requireNonNull(response.body()).getRegisterData().getAddress());
                        String Currency = String.valueOf(Objects.requireNonNull(response.body()).getRegisterData().getCurrency());
                        String PhoneVerified = String.valueOf(Objects.requireNonNull(response.body()).getRegisterData().getPhoneVerified());
                        String EmailVerified = String.valueOf(Objects.requireNonNull(response.body()).getRegisterData().getEmailVerified());*/


                        saveInFirebase(UserId, Firstname, Lastname, Email, Countrycode, Mobile, UserProPic);

                        removeRememberme();

                        hideProgressDialog();

                        showProfileUpdateAlertDialog(requireActivity(), getResources().getString(R.string.register_successfully));

                    } else {
                        hideProgressDialog();
                        btnConfirmSignup.setEnabled(true);
                        baseActivity.showSnackbar(requireActivity(),contentView,response.body().getMessage(), "warning");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<RegisterModel> call, @NonNull Throwable t) {
                call.cancel();
                hideProgressDialog();
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
                (dialog, which) -> {
                    dialog.dismiss();
                    Utils.switchContent(R.id.frame_launch_container,
                            Utils.FRAGMENT_SIGNIN,
                            requireActivity(), null,
                            Utils.AnimationType.SLIDE_IN_SLIDE_OUT);
                }).show();
    }


    public void removeRememberme() {
        SharedPreferences.Editor getStateRemember;
        getStateRemember = Objects.requireNonNull(requireActivity()).getSharedPreferences(Utils.MY_PREFS_NAME_REMEMBER, MODE_PRIVATE).edit();
        getStateRemember.putString(Utils.SP_USER_EMAIL_REMEMBERME, "");
        getStateRemember.putString(Utils.SP_USER_PASS_REMEMBERME, "");
        getStateRemember.putString(Utils.SP_CHECKED_REMEMBERME, "false");
        getStateRemember.apply();
    }

    private void saveInFirebase(String userId, String firstname, String lastname, String email, String countrycode, String mobile, String profilepic) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(Utils.FIREBASE_USER_HEADER).child(userId);
        Map<String, Object> updates = new HashMap<String, Object>();
        updates.put(Utils.FIREBASE_USER_FNAME, firstname);
        updates.put(Utils.FIREBASE_USER_LNAME, lastname);
        updates.put(Utils.FIREBASE_USER_EMAIL, email);
        updates.put(Utils.FIREBASE_USER_CC, countrycode);
        updates.put(Utils.FIREBASE_USER_MOBILE, mobile);
        updates.put(Utils.FIREBASE_USER_PRO_PIC, profilepic);
        updates.put("currency", "");
        updates.put("language", "");
        updates.put("status", "0");
        updates.put("wallet", "0");
        updates.put("balance_amount", "0");
        updates.put("rating", "0");

        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child(Utils.FIREBASE_USER_HEADER).child(userId).child(Utils.FIREBASE_TRIP_HEADER);
        Map<String, Object> updates1 = new HashMap<String, Object>();
        updates1.put("accept_status", "0");
        updates1.put("booking_id", "0");

        ref.setValue(updates, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                System.out.println("DATA SAVED SUCCESSFULLY");
                if (databaseError != null) {
                    System.out.println("DATA SAVED SUCCESSFULLY");
                }
            }
        });
        ref1.setValue(updates1, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                System.out.println("DATA SAVED SUCCESSFULLY");
                if (databaseError != null) {
                    System.out.println("DATA SAVED SUCCESSFULLY");
                }
            }
        });

    }

    private void sentVerificationCode(String strCCode, String phone, String strEmail) {
        Log.e("sentVerificationCode~~", strCCode + phone);
        String phoneNo = strCCode + phone;
        Logger.printDebug("codeeee___" + phoneNo);
        callback_verificvation(strCCode, phone, strEmail);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNo, 60, TimeUnit.SECONDS, Objects.requireNonNull(requireActivity()), mCallbacks);
    }

    private void callback_verificvation(String strCCode, String phone, String strEmail) {

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Logger.printDebug("Verification_complete");
                Toast.makeText(requireActivity(), "Your mobile number already verified", Toast.LENGTH_SHORT).show();
                hideProgressDialog();
                btnConfirmSignup.setEnabled(true);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Logger.printDebug(e.getMessage());
                System.out.println("enter the exeption otp verfication" + e.getMessage());

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Utility.showToastMessage(getContext(), "failed Sending OTP_InvalidRequest");
                    btnConfirmSignup.setEnabled(true);
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Utility.showToastMessage(getContext(), "failed Sending OTP_ManyRequest");
                    btnConfirmSignup.setEnabled(true);
                } else {
                    Utility.showToastMessage(getContext(), "verification message Sending failed..");
                    btnConfirmSignup.setEnabled(true);
                }

            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                Logger.printDebug("code send");
                Logger.printDebug(verificationId);
                verificationID = verificationId;
                if (!resend) {
                    showVerifyOTPDialog(strCCode, phone, strEmail);
                }
            }
        };
    }

    @SuppressLint("SetTextI18n")
    private void showVerifyOTPDialog(String strCCode, String phone, String strEmail) {
        // AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getContext()), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);

        dialogBuilder = new Dialog(requireActivity(), R.style.AppTheme);
        dialogBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialogBuilder.setContentView(R.layout.layout_account_verification);
        //Objects.requireNonNull(dialogBuilder.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogBuilder.setCancelable(true);
        dialogBuilder.getWindow().setWindowAnimations(R.style.DialogAnimationLR);

       /* Dialog dialogBuilder = new Dialog(Objects.requireNonNull(getContext()));
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams")
        View dialogView = Objects.requireNonNull(inflater).inflate(R.layout.layout_account_verification, null);*/

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
                //etResend.setBackgroundColor(Color.rgb(55,103,228));
            }
        }.start();

        //dialogBuilder.setView(dialogView);
//     final AlertDialog  alertDialog = dialogBuilder.create();
        // alertDialog = dialogBuilder.create();
        dialogBuilder.setCancelable(false);
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // boolSignupBtnClick = false;
                btnConfirmSignup.setEnabled(true);
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
                sentVerificationCode(strCCode, phone, strEmail);
            }
        });
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("verificationID--" + verificationID);
                System.out.println("etOTP--" + etOTP.getText().toString().trim());
                if (etOTP.getText().toString().isEmpty()) {
                    Toast.makeText(requireActivity(), "Please Enter Verification Code", Toast.LENGTH_SHORT).show();
                } else {
                    dialogBuilder.dismiss();
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
                .addOnCompleteListener(Objects.requireNonNull(requireActivity()), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Logger.printDebug("signInWithCredential:success");
                            resend = false;
                            CallRegisterDetails(strCountyCode, strMobile, strEmail);  //vignesh
                        } else {
                            // Sign in failed, display a message and update the UI
                            btnConfirmSignup.setEnabled(true);
                            Logger.printDebug(Objects.requireNonNull(task.getException()).toString());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Logger.printDebug("Invalid");
                                System.out.println("yayini" + task.getException().getMessage());
                                Utility.showToastMessage(requireActivity(), task.getException().getMessage());
                            }
                        }
                    }
                });
    }

    public void onBackButton(View view) {
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.e("Onbackpressed", String.valueOf(event));
                if (event.getAction() == KeyEvent.ACTION_UP
                        && keyCode == KeyEvent.KEYCODE_BACK) {
                    Log.e("Onbackpressed~~~", String.valueOf(event));
                    backButtonFunction();
                }
                return true;
            }
        });
    }

    public void backButtonFunction() {
        removeFragment(Utils.FRAGMENT_SIGNUP);
    }

    public void removeFragment(String Tag) {
        Utils.CURRENT_TAG = null;
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag(Tag)).commit();
    }

    public void hideKeyBoard() {
        // Check if no view has focus:
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
