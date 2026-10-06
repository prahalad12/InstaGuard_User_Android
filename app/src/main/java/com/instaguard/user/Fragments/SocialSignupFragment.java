
package com.instaguard.user.Fragments;

import static android.content.Context.MODE_PRIVATE;
import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;
import static com.instaguard.user.Activities.BaseActivity.hideProgressDialog;
import static com.instaguard.user.Activities.BaseActivity.showProgressDialog;
import static com.instaguard.user.Activities.HomeActivity.MY_PERMISSIONS_REQUEST_LOCATION;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_200;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_201;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.app.ActivityCompat;
import androidx.core.os.ConfigurationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.facebook.BuildConfig;
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
import com.instaguard.user.Helpers.Retrofit.APIClient;
import com.instaguard.user.Helpers.Retrofit.APIInterface;
import com.instaguard.user.Helpers.Utils;
import com.instaguard.user.Models.SocialLogModel.SocialSignupModel;
import com.instaguard.user.R;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SocialSignupFragment extends Fragment {

    CustomEditText edtFirstName, edtLastName, edtEmail, edtMobile, edtCountryCode;
    ImageView imgbtnBack;
    CustomButton btnConfirmSignup;
    CountryCodePicker ccp;

    SharedPreferences.Editor getStatePrefs;
    public static String TAG = "SocialSignupFragment";

    String strCountyCode, strMobile, strEmail;

    View view;

    String deviceId;

    private AwesomeValidation Validation;

    ImageButton imgbtnClearFirstName, imgbtnClearLastName, imgbtnClearEmail, imgbtnClearPassword, imgbtnClearMobile, imgbtnClearReflCode;

    View lytSuccess;
    CustomTextView txtSuccessTitle;
    CustomButton btnSuccessOkay;
    RelativeLayout rlParentSuccess;

    CustomEditText edtReferralCode;


    APIInterface apiInterface;

    CheckBox cbIAgree;
    String privacyPolicyLink;
    String privacyPolicyText;
    String termsConditionsLink;
    String termsConditionsText;
    //Terms&Condition

    CustomTextView txtPrivacyPolicy;
    CustomTextView txtTermsService;

    public static SuccessResponseSocialLogin SuccessResponseSocialLogin_;
    Locale locale;
    BaseActivity baseActivity;
    FrameLayout contentView;

    public interface SuccessResponseSocialLogin {
        void SuccessResponseSocialLogin_(String langname, String code, String country, String key);
    }

    public SocialSignupFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    String socialId, fName, lName, type, custId, email;

    @SuppressLint("CommitPrefEdits")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_signup_social, container, false);

        getStatePrefs = getActivity().getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE).edit();
        SharedPreferences pref = requireActivity().getSharedPreferences(Utils.GLOBAL_STORED_ID, MODE_PRIVATE);
        deviceId = pref.getString(Utils.SP_DEVICE_ID, null);
        Log.i(TAG, "token in singinpage--> " + deviceId);
        if (deviceId == null)
            deviceId = "123";
        baseActivity = new BaseActivity();

        edtFirstName = view.findViewById(R.id.edt_firstname);
        edtLastName = view.findViewById(R.id.edt_lastname);
        edtEmail = view.findViewById(R.id.edt_email);
        edtCountryCode = view.findViewById(R.id.edt_country_code);
        edtMobile = view.findViewById(R.id.edt_mobile);
        edtReferralCode = view.findViewById(R.id.edt_referralcode);

        ccp = view.findViewById(R.id.ccp);
        contentView = view.findViewById(R.id.frame_signup_social);

        imgbtnClearFirstName = (ImageButton) view.findViewById(R.id.imgbtn_clear_first_name);
        imgbtnClearLastName = (ImageButton) view.findViewById(R.id.imgbtn_clear_last_name);
        imgbtnClearEmail = (ImageButton) view.findViewById(R.id.imgbtn_clear_email);
        imgbtnClearPassword = (ImageButton) view.findViewById(R.id.imgbtn_clear_pwd);
        imgbtnClearMobile = (ImageButton) view.findViewById(R.id.imgbtn_clear_mobile);
        imgbtnClearReflCode = (ImageButton) view.findViewById(R.id.imgbtn_clear_referralcode);

        cbIAgree = (CheckBox) view.findViewById(R.id.checkbox);

        txtPrivacyPolicy = view.findViewById(R.id.txt_i_agree_ll);
        txtTermsService = view.findViewById(R.id.txt_i_agree_llll);

        imgbtnBack = (ImageView) view.findViewById(R.id.imgbtn_back);
        btnConfirmSignup = (CustomButton) view.findViewById(R.id.btn_confirmsignup);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        if (getArguments() != null) {
            socialId = getArguments().getString("social_id");
            fName = getArguments().getString("fname");
            lName = getArguments().getString("lname");
            type = getArguments().getString("type");
            custId = getArguments().getString("cus_id");
            email = getArguments().getString("email");

            System.out.println("inside social");
        }

        edtFirstName.setText(fName.replaceAll("_", " "));
        edtLastName.setText(lName.replaceAll("_", " "));
        if (email != null) {
            if (!email.equals("")) {
                edtEmail.setText(email);
                edtEmail.setEnabled(false);
                edtEmail.setFocusable(false);
                edtEmail.setFocusableInTouchMode(false);
                edtEmail.setClickable(false);
            } else {
                edtEmail.setEnabled(true);
                edtEmail.setFocusable(true);
                edtEmail.setFocusableInTouchMode(true);
                edtEmail.setClickable(true);
            }
        } else {
            edtEmail.setEnabled(true);
            edtEmail.setFocusable(true);
            edtEmail.setFocusableInTouchMode(true);
            edtEmail.setClickable(true);
        }

        // validation initalize
        Validation = new AwesomeValidation(BASIC);

        OnClick();

        onBackButton(view);

        return view;

    }

    private void OnClick() {

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


        /*txtPrivacyPolicy.setOnClickListener(view -> {
            if (privacyPolicyLink != null) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(privacyPolicyLink));
                    intent.setPackage("com.android.chrome");
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    // chrome app not install
                }
            } else {
                String urls = Utils.PRIVACY_URL;
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(urls));
                    intent.setPackage("com.android.chrome");
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    // chrome app not install
                }
            }
        });

        txtTermsService.setOnClickListener(view -> {
            if (termsConditionsLink != null) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(termsConditionsLink));
                    intent.setPackage("com.android.chrome");
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    // chrome app not install
                }
            } else {
                String urls = Utils.TERMS_SERVICE_URL;
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(urls));
                    intent.setPackage("com.android.chrome");
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    // chrome app not install
                }
                // Toast.makeText(getApplicationContext(), "Terms of service", Toast.LENGTH_SHORT).show();
            }
        });*/

        txtPrivacyPolicy.setOnClickListener(view -> {
            Bundle args = new Bundle();
            args.putString(Utils.B_SUPPORT_FROM,"register_social");
            Utils.switchContent(R.id.frame_signup_social,
                    Utils.FRAGMENT_SUPPORT,
                    requireActivity(), args,
                    Utils.AnimationType.SLIDE_IN_SLIDE_OUT);
        });

        txtTermsService.setOnClickListener(view -> {
            Bundle args = new Bundle();
            args.putString(Utils.B_SUPPORT_FROM,"register_social");
            Utils.switchContent(R.id.frame_signup_social,
                    Utils.FRAGMENT_SUPPORT,
                    requireActivity(), args,
                    Utils.AnimationType.SLIDE_IN_SLIDE_OUT);
        });


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

        btnConfirmSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideKeyBoard();
                ValidationForEditText();
            }
        });


        imgbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard();
                backButtonFunction();
            }
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


    private void GetCountryZipCode() {

        String CountryZipCode = "";
        Locale locales = ConfigurationCompat.getLocales(Resources.getSystem().getConfiguration()).get(0);

//        Log.e(TAG, "onCreateView: ~~~~~~3~~~~~~~~"+locales);
//        Log.e(TAG, "onCreateView: ~~~~~~4~~~~~~~~"+locales.getDisplayName());
//        Log.e(TAG, "onCreateView: ~~~~~~5~~~~~~~~"+locales.getLanguage());
        Log.e(TAG, "onCreateView: ~~~~~~6~~~~~~~~" + locales.getCountry());
        Log.e(TAG, "onCreateView: ~~~~~~7~~~~~~~~" + locales.getDisplayCountry());

        String[] rl = getActivity().getResources().getStringArray(R.array.CountryCodes);

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


    //validation controller
    private void ValidationForEditText() {

        Validation.addValidation(getActivity(), R.id.edt_firstname, "[a-zA-Z\\s]+", R.string.register_error_firstname);
        Validation.addValidation(getActivity(), R.id.edt_lastname, "[a-zA-Z\\s]+", R.string.register_error_lastname);
        Validation.addValidation(getActivity(), R.id.edt_mobile, RegexTemplate.TELEPHONE, R.string.register_error_enter_validnum);
        Validation.addValidation(getActivity(), R.id.edt_email, Patterns.EMAIL_ADDRESS, R.string.register_error_email);

        if (Validation.validate()) {
            // btnConfirmSignup.setEnabled(false);
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
                    CallRegisterDetails(strCountyCode , strMobile);
                } else {
                    btnConfirmSignup.setEnabled(true);
                    baseActivity.showSnackbar(requireActivity(),contentView,getString(R.string.register_terms_condition), "warning");
                }
            }
        } else {
            onBackButton(view);
            // Toast.makeText(getActivity(), "Failure", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateSelectItems(AppCompatSpinner view, String key) {

        if (view == null) {
            return true;
        } else if (Objects.equals(view, "null")) {
            return true;
        } else if (view.getSelectedItem() == null) {
            return true;
        } else if (view.getSelectedItem().equals("null")) {
            return true;
        } else return view.getSelectedItem().equals(key);
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


    private void CallRegisterDetails(String strCountyCode, String strMobNum) {

        String strfname = edtFirstName.getText().toString().trim();
        String strlname = edtLastName.getText().toString().trim();
        String strfnameupperString = strfname.substring(0, 1).toUpperCase() + strfname.substring(1);
        String strlnameupperString = strlname.substring(0, 1).toUpperCase() + strlname.substring(1);

        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("customer_id", custId);
        map1.put("email", edtEmail.getText().toString());
        map1.put("phone_number", strMobNum);
        map1.put("country_code", strCountyCode);
        map1.put("device_id", deviceId);
        map1.put("name", edtFirstName.getText().toString());
        map1.put("last_name", edtLastName.getText().toString());
        map1.put("referral_code", edtReferralCode.getText().toString().trim());


        Log.i("CallRegisterDetails", "~~~~" + map1);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<SocialSignupModel> call2 = apiInterface.doSocialLoginUpdate(map1);
        showProgressDialog(getActivity());
        call2.enqueue(new Callback<SocialSignupModel>() {
            @Override
            public void onResponse(@NonNull Call<SocialSignupModel> call, @NonNull Response<SocialSignupModel> response) {
                hideProgressDialog();
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {

                    if (response.body().getMsg().equalsIgnoreCase("User Details")) {

                        callCommonFBGOOGLE(type, response);

                    } else if (response.body().getMsg().equalsIgnoreCase("Email already exists.")) {
                        Toast.makeText(requireActivity(), R.string.register_email_already_exist, Toast.LENGTH_SHORT).show();
                    } else {
                        //showSnackbar(response.body().getMsg(), "warning");
                        Toast.makeText(requireActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        hideProgressDialog();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<SocialSignupModel> call, @NonNull Throwable t) {
                // showSnackbar(String.valueOf(t), "warning");
                Log.e(TAG, "onFailure:~~~ " + t.toString());
                Toast.makeText(requireActivity(), t.toString(), Toast.LENGTH_SHORT).show();
                hideProgressDialog();
                call.cancel();
            }
        });
    }

    private void callCommonFBGOOGLE(String social_type, @NonNull Response<SocialSignupModel> response) {

        // String access_token = Objects.requireNonNull(response.body()).getAccessToken();
        //  String token_type = Objects.requireNonNull(response.body()).getTokenType();
        //  String expires_in = String.valueOf(Objects.requireNonNull(response.body()).getExpiresIn());

        String UserId = String.valueOf(Objects.requireNonNull(response.body()).getResult().getId());
        String Firstname = Objects.requireNonNull(response.body()).getResult().getFirstName();
        String Lastname = Objects.requireNonNull(response.body()).getResult().getLastName();
        String Email = Objects.requireNonNull(response.body()).getResult().getEmail();
        String UserProPic = String.valueOf(Objects.requireNonNull(response.body()).getResult().getAvatar());
        String Dob = String.valueOf(Objects.requireNonNull(response.body()).getResult().getBirthDate());
        String State = String.valueOf(Objects.requireNonNull(response.body()).getResult().getStateId());
        String Country = String.valueOf(Objects.requireNonNull(response.body()).getResult().getCountryId());
        String Address = String.valueOf(Objects.requireNonNull(response.body()).getResult().getAddress());

        String strbalamount = String.valueOf(Objects.requireNonNull(response.body()).getResult().getWalletBalance());

        String Mobile = String.valueOf(Objects.requireNonNull(response.body()).getResult().getPhoneNumber());
        String Countrycode = String.valueOf(Objects.requireNonNull(response.body()).getResult().getCountryCode());
        String PhoneVerified = String.valueOf(Objects.requireNonNull(response.body()).getResult().getPhoneVerified());
        String EmailVerified = String.valueOf(Objects.requireNonNull(response.body()).getResult().getEmailVerified());
        String WalletAmount = String.valueOf(Objects.requireNonNull(response.body()).getResult().getWalletBalance());
        String Referal = String.valueOf(Objects.requireNonNull(response.body()).getResult().getReferralCode());
        String InviteCode = String.valueOf(Objects.requireNonNull(response.body()).getResult().getInviteCode());
        String Currency = String.valueOf(Objects.requireNonNull(response.body()).getResult().getCurrency());

        String CurrencySymbol = String.valueOf(Objects.requireNonNull(response.body()).getResult().getCurrency());

        String language = String.valueOf(Objects.requireNonNull(response.body()).getResult().getLanguage());

        savesharedpreference(UserId, Firstname, Lastname, Email,
                UserProPic, Dob, State, Country, Address, Mobile, Countrycode, WalletAmount,
                social_type, Referal, InviteCode, Currency, CurrencySymbol, language);
        saveInFirebase(UserId, Firstname, Lastname, Email, Countrycode, Mobile, UserProPic, WalletAmount,
                Currency, CurrencySymbol, language, strbalamount, social_type);

        hideProgressDialog();
    }

    public void removeRememberme() {
        SharedPreferences.Editor getStateRemember;
        getStateRemember = requireActivity().getSharedPreferences(Utils.MY_PREFS_NAME_REMEMBER, MODE_PRIVATE).edit();
        getStateRemember.putString(Utils.SP_USER_EMAIL_REMEMBERME, "");
        getStateRemember.putString(Utils.SP_USER_PASS_REMEMBERME, "");
        getStateRemember.putString(Utils.SP_CHECKED_REMEMBERME, "false");
        getStateRemember.apply();
    }

    private void saveInFirebase(String userId, String firstname, String lastname, String email, String countrycode,
                                String mobile, String profilepic, String wallet, String currency,
                                String currencysymbol, String language, String strbalamount, String social_type) {

        DatabaseReference refs = FirebaseDatabase.getInstance().getReference();
        refs.child(Utils.FIREBASE_USER_HEADER).child(userId).child(Utils.FIREBASE_USER_FNAME).setValue(firstname);
        refs.child(Utils.FIREBASE_USER_HEADER).child(userId).child(Utils.FIREBASE_USER_LNAME).setValue(lastname);
        refs.child(Utils.FIREBASE_USER_HEADER).child(userId).child(Utils.FIREBASE_USER_EMAIL).setValue(email);
        refs.child(Utils.FIREBASE_USER_HEADER).child(userId).child(Utils.FIREBASE_USER_CC).setValue(countrycode);
        refs.child(Utils.FIREBASE_USER_HEADER).child(userId).child(Utils.FIREBASE_USER_MOBILE).setValue(mobile);
        refs.child(Utils.FIREBASE_USER_HEADER).child(userId).child(Utils.FIREBASE_USER_PRO_PIC).setValue(profilepic);

        refs.child(Utils.FIREBASE_USER_HEADER).child(userId).child("language").setValue("");
        refs.child(Utils.FIREBASE_USER_HEADER).child(userId).child("status").setValue("0");
        refs.child(Utils.FIREBASE_USER_HEADER).child(userId).child("wallet").setValue("0");

        refs.child(Utils.FIREBASE_USER_HEADER).child(userId).child("social_type").setValue(social_type);

        refs.child(Utils.FIREBASE_USER_HEADER).child(userId).child("currency").setValue("");

        //check social login
        if (strbalamount != null) {
            if (!strbalamount.equals("null")) {
                if (!strbalamount.equals("")) {
                    refs.child(Utils.FIREBASE_USER_HEADER).child(userId).child("balance_amount").setValue(strbalamount);
                } else {
                    refs.child(Utils.FIREBASE_USER_HEADER).child(userId).child("balance_amount").setValue("0");
                }
            } else {
                refs.child(Utils.FIREBASE_USER_HEADER).child(userId).child("balance_amount").setValue("0");
            }
        } else {
            refs.child(Utils.FIREBASE_USER_HEADER).child(userId).child("balance_amount").setValue("0");
        }

        refs.child(Utils.FIREBASE_USER_HEADER).child(userId).child(Utils.FIREBASE_TRIP_HEADER).child(Utils.FIREBASE_ACCEPT_STATUS).setValue("0");
        refs.child(Utils.FIREBASE_USER_HEADER).child(userId).child(Utils.FIREBASE_TRIP_HEADER).child(Utils.FIREBASE_BOOKING_ID).setValue("0");

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
        removeFragment(Utils.FRAGMENT_SOCIALSIGNUP);
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
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void savesharedpreference(String userId, String firstname,
                                      String lastname, String email, String userProPic, String dob, String state, String country,
                                      String address, String Mobile, String Countrycode, String WalletAmount, String SocialType,
                                      String referal, String inviteCode, String currency, String currencysymbol, String language) {

        getStatePrefs.putString(Utils.SP_USER_ACCESSTOKEN, "123");
        getStatePrefs.putString(Utils.SP_USER_TOKENTYPE, "token");
        getStatePrefs.putString(Utils.SP_USER_TOKENEXPTIME, "123");

        getStatePrefs.putString(Utils.SP_USERID, userId);
        getStatePrefs.putString(Utils.SP_USER_FNAME, firstname);
        getStatePrefs.putString(Utils.SP_USER_LNAME, lastname);
        getStatePrefs.putString(Utils.SP_USER_EMAIL, email);
        getStatePrefs.putString(Utils.SP_USER_PRO_PIC, userProPic);
        getStatePrefs.putString(Utils.SP_USER_DOB, dob);
        getStatePrefs.putString(Utils.SP_USER_STATE, state);
        getStatePrefs.putString(Utils.SP_USER_COUNTRY, country);
        getStatePrefs.putString(Utils.SP_USER_ADDRESS, address);
        // getStatePrefs.putString(Utils.SP_CURRENCY,currency);
        getStatePrefs.putString(Utils.SP_USER_MOBILE, Mobile);
        getStatePrefs.putString(Utils.SP_USER_COUNTRYCODE, Countrycode);
        getStatePrefs.putString(Utils.SP_USER_WALLET_AMT, WalletAmount);

        getStatePrefs.putString(Utils.SP_USER_SOCIAL_LOGIN_TYPE, SocialType);

        getStatePrefs.putString(Utils.SP_USER_REFERAL_CODE, referal);
        getStatePrefs.putString(Utils.SP_USER_INVITE_CODE, inviteCode);
        getStatePrefs.apply();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.e("MissingPermission~~~", "currentLocationClick~~MissingPermission");
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                requestPermissions(
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

            } else {
                callLangauge(language);
            }
        } else {
            callLangauge(language);
        }
    }

    String language;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e(TAG, "onRequestPermissionsResult: ~~~~~~~~~~~~" + requestCode);
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            callLangauge(language);
        }
    }

    private void callLangauge(String language) {

        if (language.equals("")) {
            if (BuildConfig.DEBUG) Log.e(TAG, "savesharedpreference: ");
            SuccessResponseSocialLogin_.SuccessResponseSocialLogin_(language, "en", "US", "login");
        } else {
            // Toast.makeText(getActivity(), "Check Languages", Toast.LENGTH_SHORT).show();
            switch (language) {
                case "English":
                default:
                    SuccessResponseSocialLogin_.SuccessResponseSocialLogin_(language, "en", "US", "login");
                    break;
                case "Sinhala":
                    SuccessResponseSocialLogin_.SuccessResponseSocialLogin_(language, "si", "LK", "login");
                    break;
            }
        }
    }
}