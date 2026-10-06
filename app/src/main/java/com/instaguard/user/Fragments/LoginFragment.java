package com.instaguard.user.Fragments;

import static android.content.Context.MODE_PRIVATE;
import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;
import static com.instaguard.user.Activities.BaseActivity.callClickAction;
import static com.instaguard.user.Activities.BaseActivity.hideProgressDialog;
import static com.instaguard.user.Activities.BaseActivity.showProgressDialog;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_200;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_201;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_401;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.facebook.AccessTokenTracker;
import com.facebook.BuildConfig;
import com.facebook.CallbackManager;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.common.reflect.TypeToken;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.instaguard.user.Activities.BaseActivity;
import com.instaguard.user.CustomViews.CustomButton;
import com.instaguard.user.CustomViews.CustomEditText;
import com.instaguard.user.CustomViews.CustomTextView;
import com.instaguard.user.Helpers.Retrofit.APIClient;
import com.instaguard.user.Helpers.Retrofit.APIInterface;
import com.instaguard.user.Helpers.SocialNetworks.facebookSignIn.FacebookHelper;
import com.instaguard.user.Helpers.SocialNetworks.googleAuthSignin.GoogleSignInHelper;
import com.instaguard.user.Helpers.SocialNetworks.googleSignIn.GooglePlusSignInHelper;
import com.instaguard.user.Helpers.Utils;
import com.instaguard.user.Models.AppSettingCurrencyModel;
import com.instaguard.user.Models.LoginModel;
import com.instaguard.user.Models.LoginNewModel;
import com.instaguard.user.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    View view;

    Toolbar toolbarSignin;
    ImageButton imgbtnBack, imgbtnClearEmail, imgbtnClearPwd;
    CustomEditText edtEmail, edtPassword;
    ImageView ivFacebook, ivTwitter, ivGoogle;
    CustomButton btnConfirmsignin;
    CustomTextView txtSignup, txtForgotpassword;
    private AwesomeValidation Validation;

    private FacebookHelper mFbHelper;
    private GooglePlusSignInHelper mGHelper;
    private GoogleSignInHelper mGAuthHelper;

    SharedPreferences.Editor getState, getStateRemember, getSpCurrency;

    public String TAG = "LoginFragment";
    String deviceId;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    GoogleSignInOptions gso;
    GoogleApiClient mGoogleApiClient;
    private int GOOGLE_SIGN_IN = 100;
    //Tracker mTracker;
    //Facebook Declaration
    private CallbackManager callbackManager;

    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    Bundle parameters;

    String fbEmail, fbFullName, fbFirstName, fbLastName, fbUserProfile, fbID, fbToken;
    String googleEmail, googleFirstName, googleLastName, googleUserProfile, googleID, googleIDToken;
    String signInStatus, signInMessage;

    boolean facebookLogin = false;
    String spRememberChecked;
    SharedPreferences sprefs, spCurrency, sprefsGlobalDeviceId, sprefsRemember, sprefsCurrency;
    CheckBox cbRememberMe;
    boolean boolRememberChecked = false;
    List<AppSettingCurrencyModel> getCurrencyList = null;
    public DatabaseReference driverSearchDatabaseBRef;
    public ValueEventListener driverSearchValueEventListener;

    public String strCurrency;

    public static SuccessResponseLogin SuccessResponseLogin;
    Locale locale;
    BaseActivity baseActivity;
    FrameLayout contentView;

    public interface SuccessResponseLogin {
        void SuccessResponseLogin(String langname, String code, String country, String key);
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);
        getState = requireActivity().getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE).edit();
        getStateRemember = requireActivity().getSharedPreferences(Utils.MY_PREFS_NAME_REMEMBER, MODE_PRIVATE).edit();
        getSpCurrency = getActivity().getSharedPreferences(Utils.PREFS_NAME_CURRENCY, MODE_PRIVATE).edit();

        sprefs = getActivity().getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE);
        sprefsRemember = getActivity().getSharedPreferences(Utils.MY_PREFS_NAME_REMEMBER, MODE_PRIVATE);
        sprefsCurrency = getActivity().getSharedPreferences(Utils.PREFS_NAME_CURRENCY, MODE_PRIVATE);

        strCurrency = sprefsCurrency.getString(Utils.SP_CURRENCY, null);

        spRememberChecked = sprefsRemember.getString(Utils.SP_CHECKED_REMEMBERME, null);
        // SharedPreferences pref = getActivity().getSharedPreferences(Utils.GLOBAL_STORED_ID, MODE_PRIVATE);
        // deviceId = pref.getString(Utils.SP_DEVICE_ID, null);
        if (spRememberChecked == null) {
            spRememberChecked = "null";
        } else {
            Log.i(TAG, "onCreateView: spRemeberme-->" + spRememberChecked);
        }

        sprefsGlobalDeviceId = getActivity().getSharedPreferences(Utils.GLOBAL_STORED_ID, MODE_PRIVATE);
        deviceId = sprefsGlobalDeviceId.getString(Utils.SP_DEVICE_ID, null);


        Log.e(TAG, "token in LoginFragment--> " + deviceId);
        if (deviceId == null)
            deviceId = "123";

        baseActivity = new BaseActivity();


        toolbarSignin = (Toolbar) view.findViewById(R.id.toolbar_signin);
        imgbtnBack = (ImageButton) view.findViewById(R.id.imgbtn_back);
        imgbtnClearEmail = (ImageButton) view.findViewById(R.id.imgbtn_clear_email);
        imgbtnClearPwd = (ImageButton) view.findViewById(R.id.imgbtn_clear_password);

        ivFacebook = (ImageView) view.findViewById(R.id.iv_facebook);
        ivTwitter = (ImageView) view.findViewById(R.id.iv_twitter);
        ivGoogle = (ImageView) view.findViewById(R.id.iv_google);

        edtEmail = (CustomEditText) view.findViewById(R.id.edt_email);
        edtPassword = (CustomEditText) view.findViewById(R.id.edt_password);

        btnConfirmsignin = (CustomButton) view.findViewById(R.id.btn_confirmsignin);

        txtForgotpassword = (CustomTextView) view.findViewById(R.id.txt_forgotpassword);
        txtSignup = (CustomTextView) view.findViewById(R.id.txt_createanewaccount);

        cbRememberMe = (CheckBox) view.findViewById(R.id.cb_rememberme);

        contentView = view.findViewById(R.id.frame_signin);

        getCurrencyList = loadSharedPreferencesLogList(getActivity());

        if (spRememberChecked.equals("true")) {
            edtEmail.setText(sprefsRemember.getString(Utils.SP_USER_EMAIL_REMEMBERME, null));
            // edtPassword.setText(sprefsRemember.getString(Utils.SP_USER_PASS_REMEMBERME, null));
            cbRememberMe.setChecked(true);
            boolRememberChecked = true;
            imgbtnClearEmail.setVisibility(View.VISIBLE);
            imgbtnClearPwd.setVisibility(View.GONE);
        } else {
            imgbtnClearEmail.setVisibility(View.GONE);
            imgbtnClearPwd.setVisibility(View.GONE);
        }
        // validation initalize
        Validation = new AwesomeValidation(BASIC);

        onCLick();
        searchCurrencyPrice(strCurrency); //vig27
        onBackButton(view);
        return view;
    }

    private void onCLick() {

        imgbtnClearEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtEmail.setText("");
            }
        });

        imgbtnClearPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtPassword.setText("");
            }
        });

        edtEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.e(TAG, "onFocusChange: ~~~~" + hasFocus);
                if (!hasFocus) {
                    imgbtnClearEmail.setVisibility(View.GONE);
                } else {
                    imgbtnClearEmail.setVisibility(View.VISIBLE);
                }
            }
        });
        edtPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.e(TAG, "onFocusChange: ~~~~" + hasFocus);
                if (!hasFocus) {
                    imgbtnClearPwd.setVisibility(View.GONE);
                } else {
                    imgbtnClearPwd.setVisibility(View.VISIBLE);
                }
            }
        });

        edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    imgbtnClearEmail.setVisibility(View.GONE);
                } else {
                    imgbtnClearEmail.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    imgbtnClearEmail.setVisibility(View.GONE);
                } else {
                    imgbtnClearEmail.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    imgbtnClearPwd.setVisibility(View.GONE);
                } else {
                    imgbtnClearPwd.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    imgbtnClearPwd.setVisibility(View.GONE);
                } else {
                    imgbtnClearPwd.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cbRememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolRememberChecked = isChecked;
                if (!isChecked) {
                    getStateRemember.putString(Utils.SP_USER_EMAIL_REMEMBERME, "");
                    getStateRemember.putString(Utils.SP_USER_PASS_REMEMBERME, "");
                    getStateRemember.putString(Utils.SP_CHECKED_REMEMBERME, "false");
                    getStateRemember.apply();
                }

            }
        });
        btnConfirmsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard();
                ValidationForEditText();
            }
        });

        txtForgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.switchContent(R.id.frame_launch_container,
                        Utils.FRAGMENT_FORGOTPASSWORD,
                        getActivity(), null,
                        Utils.AnimationType.SLIDE_IN_SLIDE_OUT);
            }
        });

        //  CallPaymentDetails();

        txtSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.switchContent(R.id.frame_launch_container,
                        Utils.FRAGMENT_SIGNUP,
                        getActivity(), null,
                        Utils.AnimationType.SLIDE_IN_SLIDE_OUT);
            }
        });
        ivFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mFbHelper.performSignIn(getActivity());
                //   LoginManager.getInstance().logOut();//Logout Facebook
                //   LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile", "email"));
            }
        });
        ivGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mGHelper.performSignIn();
                // mGAuthHelper.performSignIn(getActivity());
                //   Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                //   startActivityForResult(signInIntent,GOOGLE_SIGN_IN);
            }
        });
        ivTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  mTwitterHelper.performSignIn();
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


    //validation controller
    private void ValidationForEditText() {
        // Validation.addValidation(getActivity(), R.id.edt_password, "(?=.*[a-z]).{8,}", R.string.log_in_error_password);
        if (isValidMobile(Objects.requireNonNull(edtEmail.getText()).toString())) {// Phone number is valid
            Log.i(TAG, "ValidationForEditText: valid mobile");
            callLogin("mobile");
            callClickAction(btnConfirmsignin, true);
        } else if (isEmailValid(edtEmail.getText().toString())) {//Email is valid
            Log.i(TAG, "ValidationForEditText: valid email");
            callLogin("email");
            callClickAction(btnConfirmsignin, true);
        } else {// Not valid email or phone number
            Log.i(TAG, "ValidationForEditText: valid else");
            edtEmail.setError(getString(R.string.log_in_error_email));
            baseActivity.showSnackbar(requireActivity(), contentView, getString(R.string.error_all_fields_required), "Field");
            callClickAction(btnConfirmsignin, true);
           /* if (Validation.validate()) {
            } else {
                onBackButton(view);
            }*/
        }
    }

    public void callLogin(String type) {
        //if (Validation.validate()) {
        if (Objects.requireNonNull(edtPassword.getText()).toString().isEmpty()) {
            baseActivity.showSnackbar(requireActivity(), contentView, getString(R.string.error_all_fields_required), "Field");
            callClickAction(btnConfirmsignin, true);
        } else {
            callClickAction(btnConfirmsignin, true);
            //  onBackButton(view);
            CallLoginDetails(type);
        }
    }


    private void CallLoginDetails(String type) {
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("email", edtEmail.getText().toString().toLowerCase());
        map1.put("password", edtPassword.getText().toString());
        map1.put("device_token", deviceId);
        map1.put("type", "app");

        Log.i("CallLoginDetails", "~~~~" + map1);
        showProgressDialog(getActivity());
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<LoginNewModel> call2 = apiInterface.doGetLoginDetails(map1);
        call2.enqueue(new Callback<LoginNewModel>() {
            @Override
            public void onResponse(@NonNull Call<LoginNewModel> call, @NonNull Response<LoginNewModel> response) {
                hideProgressDialog();
                Log.i(TAG, "onResponse: resonse code--> " + response.code());
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {

                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("loggedin successfully")) {

                        String access_token = Objects.requireNonNull(response.body().getLoginModel()).getAccessToken();
                        String token_type = Objects.requireNonNull(response.body().getLoginModel()).getTokenType();
                        String expires_in = String.valueOf(Objects.requireNonNull(response.body().getLoginModel()).getExpiresIn());
                        String UserId = String.valueOf(Objects.requireNonNull(response.body().getLoginModel()).getUserData().getId());
                        String Firstname = Objects.requireNonNull(response.body().getLoginModel()).getUserData().getFirstName();
                        String Lastname = Objects.requireNonNull(response.body().getLoginModel()).getUserData().getLastName();
                        String Email = Objects.requireNonNull(response.body().getLoginModel()).getUserData().getEmail();
                        String UserProPic = String.valueOf(Objects.requireNonNull(response.body().getLoginModel()).getUserData().getAvatar());
                        String Dob = Objects.requireNonNull(response.body().getLoginModel()).getUserData().getBirthDate();
                        String State = String.valueOf(Objects.requireNonNull(response.body().getLoginModel()).getUserData().getStateId());
                        String Country = String.valueOf(Objects.requireNonNull(response.body().getLoginModel()).getUserData().getCountryId());
                        String Address = String.valueOf(Objects.requireNonNull(response.body().getLoginModel()).getUserData().getAddress());
                        // String Currency = String.valueOf(Objects.requireNonNull(response.body().getLoginModel()).getUserData().getCurrency());
                        String Mobile = String.valueOf(Objects.requireNonNull(response.body().getLoginModel()).getUserData().getPhoneNumber());
                        String Countrycode = String.valueOf(Objects.requireNonNull(response.body().getLoginModel()).getUserData().getCountryCode());
                        // String PhoneVerified = String.valueOf(Objects.requireNonNull(response.body().getLoginModel()).getUserData().getPhoneVerified());
                        // String EmailVerified = String.valueOf(Objects.requireNonNull(response.body().getLoginModel()).getUserData().getEmailVerified());
                        String WalletAmount = String.valueOf(Objects.requireNonNull(response.body().getLoginModel()).getUserData().getWalletBalance());
                        String Referal = String.valueOf(Objects.requireNonNull(response.body().getLoginModel()).getUserData().getReferralCode());
                        String InviteCode = String.valueOf(Objects.requireNonNull(response.body().getLoginModel()).getUserData().getInviteCode());
                        String language = String.valueOf(Objects.requireNonNull(response.body().getLoginModel()).getUserData().getLanguage());
                        String currency = String.valueOf(Objects.requireNonNull(response.body().getLoginModel()).getUserData().getCurrency());
                        String currencysymbol = String.valueOf(Objects.requireNonNull(response.body().getLoginModel()).getUserData().getCurrencySymbol());

                        savesharedpreference(access_token, token_type, expires_in, UserId, Firstname, Lastname, Email, UserProPic,
                                Dob, State, Country, Address, Mobile, Countrycode, WalletAmount, Referal, InviteCode, currency,
                                currencysymbol, language, edtPassword.getText().toString().trim());

                        // Toast.makeText(getActivity(),UserId+access_token+token_type+expires_in+Firstname+Lastname+Email+UserProPic+Dob+State+Country+Address+Currency, Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onResponse: success");

                    } else {
                        hideProgressDialog();
                        if (response.body() != null) {
                            baseActivity.showSnackbar(requireActivity(), contentView, response.body().getMessage(), "warning");
                        }
                    }
                } else if (response.code() == RESPONSECODE_401) {
                    baseActivity.showSnackbar(requireActivity(), contentView, "401 " + getString(R.string.login_Invalid_credentials), "warning");
                } else {
                    baseActivity.showSnackbar(requireActivity(), contentView, "500 Error" + getString(R.string.login_Invalid_credentials), "warning");
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginNewModel> call, @NonNull Throwable t) {
                call.cancel();
                baseActivity.showSnackbar(requireActivity(),contentView,"onFailure" + t.toString(), "warning");
            }
        });
    }

    private void savesharedpreference(String access_token, String token_type, String exp_time, String userId, String firstname,
                                      String lastname, String email, String userProPic, String dob, String state, String country,
                                      String address, String Mobile, String Countrycode, String WalletAmount, String referal,
                                      String inviteCode, String currency, String currencysymbol, String language, String password) {

        getState.putString(Utils.SP_USER_ACCESSTOKEN, access_token);
        getState.putString(Utils.SP_USER_TOKENTYPE, token_type);
        getState.putString(Utils.SP_USER_TOKENEXPTIME, exp_time);
        getState.putString(Utils.SP_USERID, userId);
        getState.putString(Utils.SP_USER_FNAME, firstname);
        getState.putString(Utils.SP_USER_LNAME, lastname);
        getState.putString(Utils.SP_USER_EMAIL, email);
        getState.putString(Utils.SP_USER_PASSWORD, password);
        getState.putString(Utils.SP_USER_PRO_PIC, userProPic);
        getState.putString(Utils.SP_USER_DOB, dob);
        getState.putString(Utils.SP_USER_STATE, state);
        getState.putString(Utils.SP_USER_COUNTRY, country);
        getState.putString(Utils.SP_USER_ADDRESS, address);
        //getState.putString(Utils.SP_CURRENCY,currency);
        getState.putString(Utils.SP_USER_MOBILE, Mobile);
        getState.putString(Utils.SP_USER_COUNTRYCODE, Countrycode);
        getState.putString(Utils.SP_USER_WALLET_AMT, WalletAmount);
        getState.putString(Utils.SP_USER_REFERAL_CODE, referal);
        getState.putString(Utils.SP_USER_INVITE_CODE, inviteCode);
        getState.putString(Utils.SP_USER_DELETE_ACCOUNT, "0");
        getState.putString(Utils.SP_USER_SOCIAL_LOGIN_TYPE, "normal");
        getState.putString(Utils.SP_CHECKED_REMEMBERME, "false");
        if (boolRememberChecked) {
            Log.i(TAG, "savesharedpreference: in boolRememberChecked");
            getStateRemember.putString(Utils.SP_USER_EMAIL_REMEMBERME, edtEmail.getText().toString());
            getStateRemember.putString(Utils.SP_USER_PASS_REMEMBERME, edtPassword.getText().toString());
            getStateRemember.putString(Utils.SP_CHECKED_REMEMBERME, String.valueOf(boolRememberChecked));
            getStateRemember.apply();
        }

        if (currency.equals("")) {
            strCurrency = sprefsCurrency.getString(Utils.SP_CURRENCY, null);
            Log.i(TAG, "savesharedpreference: if");
            //getCurrencyPrice("LKR");
            searchCurrencyPrice(strCurrency);
        } else {
            Log.i(TAG, "savesharedpreference: else");
            getSpCurrency.putString(Utils.SP_CURRENCY, currency);
            getSpCurrency.putString(Utils.SP_CURRENCY_SYMBOL, currencysymbol);
            searchCurrencyPrice(currency);
            getSpCurrency.apply();
        }

        getState.putString(Utils.SP_USERID, userId);
        getState.putString(Utils.SP_USER_FNAME, firstname);
        getState.putString(Utils.SP_USER_LNAME, lastname);
        getState.putString(Utils.SP_USER_EMAIL, email);
        getState.putString(Utils.SP_USER_PRO_PIC, userProPic);
        getState.putString(Utils.SP_USER_DOB, dob);
        getState.putString(Utils.SP_USER_STATE, state);
        getState.putString(Utils.SP_USER_COUNTRY, country);
        getState.putString(Utils.SP_USER_ADDRESS, address);
        // getState.putString(Utils.SP_CURRENCY,currency);
        getState.apply();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.e("MissingPermission~~~", "currentLocationClick~~MissingPermission");
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
                btnConfirmsignin.setEnabled(true);

            } else {
               /* hideProgressDialog();
                removeFragment(Utils.FRAGMENT_SIGNIN);
                Intent news = new Intent(getActivity(),MainActivity.class);
                startActivity(news);
                backButtonFunction();*/

                if (language.equals("")) {
                    if (BuildConfig.DEBUG) Log.e(TAG, "savesharedpreference: ");
                    if (SuccessResponseLogin != null)
                        SuccessResponseLogin.SuccessResponseLogin(language, "en", "US", "login");
                } else {
                    /*getSpCurrency.putString(Utils.SP_LANGUAGE,language);
                    getSpCurrency.apply();*/
                    //  Toast.makeText(getActivity(), "Check Language", Toast.LENGTH_SHORT).show();
                    switch (language) {
                        case "English":
                        default:
                            if (SuccessResponseLogin != null)
                                SuccessResponseLogin.SuccessResponseLogin(language, "en", "US", "login");
                            break;
                        case "Sinhala":
                            //onSuccessEditProfile(strlangName, "si", "LK", "normal");
                            //case "Arabic":
                            if (SuccessResponseLogin != null)
                                SuccessResponseLogin.SuccessResponseLogin(language, "si", "LK", "login");
                            break;
                    }
                }


            }

        } else {
            /*hideProgressDialog();
            removeFragment(Utils.FRAGMENT_SIGNIN);
            Intent news = new Intent(getActivity(),MainActivity.class);
            startActivity(news);
            backButtonFunction();*/

            if (language.equals("")) {
                if (BuildConfig.DEBUG) Log.e(TAG, "savesharedpreference: ");
                if (SuccessResponseLogin != null)
                    SuccessResponseLogin.SuccessResponseLogin(language, "en", "US", "login");
            } else {
               /* getSpCurrency.putString(Utils.SP_LANGUAGE,language);
                getSpCurrency.apply();*/
                // Toast.makeText(getActivity(), "Check Languages", Toast.LENGTH_SHORT).show();
                switch (language) {
                    case "English":
                    default:
                        if (SuccessResponseLogin != null)
                            SuccessResponseLogin.SuccessResponseLogin(language, "en", "US", "login");
                        break;
                    case "Sinhala":
                        //onSuccessEditProfile(strlangName, "si", "LK", "normal");
                        //  case "Arabic":
                        if (SuccessResponseLogin != null)
                            SuccessResponseLogin.SuccessResponseLogin(language, "si", "LK", "login");
                        break;
                }
            }
        }

    }


    /*******************************Get Firebase States********************************************/

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
        removeFragment(Utils.FRAGMENT_SIGNIN);
    }

    public void removeFragment(String Tag) {
        Utils.CURRENT_TAG = null;
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag(Tag)).commit();
    }

    //request_time=09%3A45%20PM&payment_type=wallet&customer_lat=9.92081181563785&category=1&mode=accept&special_instruction=&customer_lon=78.09257917106152&service_id=2&customer_id=42&driver_id=15&promocode=&job_date=19-07-2018&customer_location=Ponmeni%20Muniyaandi%20koil%20main%20road%2C%20Chandragandhi%20Nagar%2C%20Ponmeni%2C%20Madurai%2C%20Tamil%20Nadu%20625016%2C%20India
    public static boolean isValidMobile(String phone) {
        boolean check = false;
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            if (phone.length() < 9 || phone.length() > 13) {
                // if(phone.length() != 10) {
                check = false;
                // txtPhone.setError("Not Valid Number");
            } else {
                check = android.util.Patterns.PHONE.matcher(phone).matches();
            }
        } else {
            check = false;
        }
        return check;
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

    public void hideKeyBoard() {
        // Check if no view has focus:
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            Objects.requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult: in loginfragment");
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: called in login fragment");
        super.onDestroy();
        // mGHelper.disconnectApiClient();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /**
     * Record a screen view hit for the visible {@link } displayed
     * inside {@link }.
     */
    private void sendScreenImageName() {
        String name = "LaunchActivity";

        // [START screen_view_hit]
        Log.i(TAG, "Setting screen name: " + name);
        //mTracker.setScreenName("Image~" + name);
        //mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        // [END screen_view_hit]
    }

    //get Facebook Details
    private void displayMessage(Profile profile) {

        if (profile != null) {
            //Toast.makeText(getApplicationContext(),"displaymessage",Toast.LENGTH_LONG).show();
            fbFullName = profile.getName();
            fbFirstName = profile.getFirstName();
            fbLastName = profile.getLastName();
            fbID = profile.getId();
            fbUserProfile = profile.getProfilePictureUri(100, 100).toString();

            if (fbEmail != null) {
                if (!fbEmail.equals("")) {
                    System.out.print("fbEmail empty");
//                    callFaceBookLogIn();
                } else if (fbEmail.isEmpty()) {
                    Toast.makeText(getActivity(), "Enter your E-mail address and Password to log into facebook", Toast.LENGTH_LONG).show();
                    LoginManager.getInstance().logOut();
                }
            }

            //String email1= this.getIntent().getExtras().getString("fields");
            //System.out.println("bundle email"+email1);
            System.out.println("Facebook Fullname" + fbFullName);
            System.out.println("Facebook Firstname" + fbFirstName);
            System.out.println("Facebook Lastname" + fbLastName);
            System.out.println("Facebook Profile" + fbUserProfile);
            System.out.println("Facebook ID" + fbID);
            System.out.println("Facebook Email" + fbEmail);
            System.out.println("Facebook Access Token" + fbToken);

        }
    }

    public static List<AppSettingCurrencyModel> loadSharedPreferencesLogList(Context context) {
        List<AppSettingCurrencyModel> callLog = new ArrayList<AppSettingCurrencyModel>();
        SharedPreferences mPrefs = context.getSharedPreferences(Utils.MY_PREFS_NAME, context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("myJsonCurrency", "");
        if (json.isEmpty()) {
            callLog = new ArrayList<AppSettingCurrencyModel>();
        } else {
            Type type = new TypeToken<List<AppSettingCurrencyModel>>() {
            }.getType();
            callLog = gson.fromJson(json, type);
        }
        return callLog;
    }

    public void getCurrencyPrice(String currencyCode) {
        Log.i(TAG, "getCurrencyPrice: currency code-->" + currencyCode);
        String search = currencyCode;
        int searchListLength = getCurrencyList.size();
        for (int i = 0; i < searchListLength; i++) {
            Log.i(TAG, "getCurrencyPrice: " + getCurrencyList.get(i).getShortName());
            if (getCurrencyList.get(i).getShortName().contains(search)) {
                Log.i(TAG, "getCurrencyPrice: search-->" + getCurrencyList.get(i).getConverionsRatio());
                getSpCurrency.putString(Utils.SP_CURRENCY_COMMON_VALUES, String.valueOf(getCurrencyList.get(i).getConverionsRatio()));
                //getSpCurrency.putString(Utils.SP_CURRENCY_SYMBOL,String.valueOf(getCurrencyList.get(i).getSymbol()));
                getSpCurrency.apply();
                break;
            }
        }
    }

    public void searchCurrencyPrice(String Currency) {
        Log.i(TAG, "searchCurrencyPrice: currency -->" + Currency);
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
                        getSpCurrency.putString(Utils.SP_CURRENCY_COMMON_VALUES, currencyPriceObj.toString());
                        getSpCurrency.apply();
                    } else {
                        getSpCurrency.putString(Utils.SP_CURRENCY_COMMON_VALUES, "1");
                        getSpCurrency.apply();
                    }
                    if (driverSearchDatabaseBRef != null) {
                        driverSearchDatabaseBRef.removeEventListener(driverSearchValueEventListener);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG, "onCancelled: error occur " + databaseError);
                getSpCurrency.putString(Utils.SP_CURRENCY_COMMON_VALUES, "1");
                getSpCurrency.apply();

            }
        });
    }

}
