package com.instaguard.user.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.common.util.IOUtils;
import com.google.common.reflect.TypeToken;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.instaguard.user.Activities.BaseActivity;
import com.instaguard.user.Activities.HomeActivity;
import com.instaguard.user.Activities.MainActivity;
import com.instaguard.user.Adapter.CurrencyAdapter;
import com.instaguard.user.Adapter.LanguageAdapter;
import com.instaguard.user.CustomViews.CustomButton;
import com.instaguard.user.CustomViews.CustomEditText;
import com.instaguard.user.CustomViews.CustomTextView;
import com.instaguard.user.CustomViews.CustomTypefaceSpan;
import com.instaguard.user.Helpers.CountryCode.CountryCodePicker;
import com.instaguard.user.Helpers.Localizationactivity.core.OnLocaleChangedListener;
import com.instaguard.user.Helpers.RecyclerViewsTools.RecyclerTouchListener;
import com.instaguard.user.Helpers.Retrofit.APIClient;
import com.instaguard.user.Helpers.Retrofit.APIInterface;
import com.instaguard.user.Helpers.Utils;
import com.instaguard.user.Helpers.imagezipper.ImageZipper;
import com.instaguard.user.Models.AppSettingCurrencyModel;
import com.instaguard.user.Models.AppSettingLanguageModel;
import com.instaguard.user.Models.AppSettingWebModel;
import com.instaguard.user.Models.ChangePasswordModel;
import com.instaguard.user.Models.ProfileModel;
import com.instaguard.user.Models.ProfilePicModel;
import com.instaguard.user.Models.ProfileUpdateModel;
import com.instaguard.user.Models.RefreshTokenModel;
import com.instaguard.user.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;
import static com.instaguard.user.Activities.BaseActivity.hideProgressDialog;
import static com.instaguard.user.Activities.BaseActivity.showProgressDialog;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_200;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_201;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_401;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_500;


public class EditSettingsFragment extends Fragment implements OnLocaleChangedListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    View view;

    Toolbar toolbarSignin;
    ImageButton imgbtnBack;
    ImageView ivMenuProfile, ivCamera;
    CustomEditText edtFname, edtLname, edtEmail, edtMobile, edtLanguage, edtCurrency, edtCountryCode;
    SimpleDraweeView ivBackcoverUserPro, ivUserPro;
    private AwesomeValidation Validation;
    CustomButton btnChangePassword, btnUpdateSettings;
    SharedPreferences.Editor getSpCurrency, getUpdateProfile;
    SharedPreferences spCurrency, spUpdateProfile;
    String strfname, strlname, strlang, strcurrency, stremail, strMobile, strCountyCode;
    String curId, curName, curShortName, curSymbol;
    CountryCodePicker ccp;

    ImageButton imgbtnClearFirstName, imgbtnClearLastName;


    String strLanguage, strCurrency, strLanguageCountry, strLanguageShortName, strCurrencySymbol;

    private static final int CAMERA_IMAGE_REQUEST = 101;
    int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    File fi;
    Bitmap bitmap;
    String fileBinaryData, strProPic;
    private String imageName, imagee;
    private int PICK_IMAGE_REQUEST = 1;
    private Uri filePath;
    private String userChoosenTask;
    public String TAG = "SettingFragment";
    private Uri fileUri;
    Uri selectedImageUri;

    Dialog dialogChangePassword;
    String userId, socialType, userProfPic, userAccessToken;

    Dialog dialogCurrency, dialogLanguage;
    CustomTextView txtUSD, txtCHF, txtEUR, txtNGS, txtEnglish, txtSwahili, txtFrench, txtSpanish;
    RecyclerView rcvLanguage, rcvCurrency;
    LinearLayout llStaticLanguage, llStaticCurrency;
    CurrencyAdapter adapterCurrency;
    LanguageAdapter adapterLanguage;

    private List<AppSettingLanguageModel> ListModel;
    private List<AppSettingCurrencyModel> ListModelCurrency;
    List<AppSettingCurrencyModel> getCurrencyList = null;


    public DatabaseReference driverSearchDatabaseBRef;
    public ValueEventListener driverSearchValueEventListener;

    String getLang_ViewProfile, getLang_EditProfile;


    public static SuccessResponseEditProfile successResponseEditProfile;
    Locale locale;
    private RoundingParams roundingParams;
    BaseActivity baseActivity;
    FrameLayout contentView;

    public interface SuccessResponseEditProfile {
        void onSuccessEditProfile(String langname, String code, String country, String key);
    }

    public EditSettingsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static EditSettingsFragment newInstance(String param1, String param2) {
        EditSettingsFragment fragment = new EditSettingsFragment();
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

    @SuppressLint({"CommitPrefEdits", "LongLogTag"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_editsettings, container, false);

        getUpdateProfile = Objects.requireNonNull(getActivity()).getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE).edit();
        getSpCurrency = Objects.requireNonNull(getActivity()).getSharedPreferences(Utils.PREFS_NAME_CURRENCY, MODE_PRIVATE).edit();

        baseActivity = new BaseActivity();

        Fresco.initialize(getActivity());
        spUpdateProfile = getActivity().getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE);
        userId = spUpdateProfile.getString(Utils.SP_USERID, null);
        socialType = spUpdateProfile.getString(Utils.SP_USER_SOCIAL_LOGIN_TYPE, null);
        userAccessToken = spUpdateProfile.getString(Utils.SP_USER_ACCESSTOKEN, null);
        userProfPic = spUpdateProfile.getString(Utils.SP_USER_PRO_PIC, null);

        spCurrency = getActivity().getSharedPreferences(Utils.PREFS_NAME_CURRENCY, MODE_PRIVATE);
        strLanguage = spCurrency.getString(Utils.SP_LANGUAGE, null);
        strLanguageShortName = spCurrency.getString(Utils.SP_LANGUAGE_SHORT_NAME, null);
        strLanguageCountry = spCurrency.getString(Utils.SP_LANGUAGE_COUNTRY, null);
        strCurrency = spCurrency.getString(Utils.SP_CURRENCY, null);
        strCurrencySymbol = spCurrency.getString(Utils.SP_CURRENCY_SYMBOL, null);

        if (strLanguageShortName == null) {
            strLanguageShortName = "en";
        }

        locale = getCurrentLocale(getActivity());

        Log.e("UserId~~SettingsFragment~~", userId);
        Log.e("SP_Prof_pic~~SettingsFragment~~", userProfPic);

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
        edtCountryCode = (CustomEditText) view.findViewById(R.id.edt_country_code);
        ccp = (CountryCodePicker) view.findViewById(R.id.ccp);

        contentView = view.findViewById(R.id.frame_editprofile);

        //  ivMenuProfile = (ImageView) view.findViewById(R.id.iv_menu_profile);


        /*Dialog Currency charge*/
        dialogCurrency = new Dialog(getActivity());
        dialogCurrency.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialogCurrency.setContentView(R.layout.dialog_static_currency);
        Objects.requireNonNull(dialogCurrency.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogCurrency.setCancelable(true);
        dialogCurrency.getWindow().setWindowAnimations(R.style.DialogAnimationLR);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogCurrency.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        /*Dialog Currency charge*/
        dialogLanguage = new Dialog(getActivity());
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

        rcvCurrency = (RecyclerView) dialogCurrency.findViewById(R.id.rcv_currency);
        llStaticCurrency = (LinearLayout) dialogCurrency.findViewById(R.id.ll_static_currency);

        txtUSD = (CustomTextView) dialogCurrency.findViewById(R.id.txt_usd);
        txtCHF = (CustomTextView) dialogCurrency.findViewById(R.id.txt_chf);
        txtEUR = (CustomTextView) dialogCurrency.findViewById(R.id.txt_eur);
        txtNGS = (CustomTextView) dialogCurrency.findViewById(R.id.txt_ngs);

        txtEnglish = (CustomTextView) dialogLanguage.findViewById(R.id.txt_english);
        txtSwahili = (CustomTextView) dialogLanguage.findViewById(R.id.txt_swahili);
        txtFrench = (CustomTextView) dialogLanguage.findViewById(R.id.txt_french);
        txtSpanish = (CustomTextView) dialogLanguage.findViewById(R.id.txt_spanish);

        if (socialType.equals("facebook") || socialType.equals("google") || socialType.equals("twitter")) {
            edtMobile.setFocusable(true);
            edtMobile.setFocusableInTouchMode(true);
            edtMobile.setClickable(true);
            // edtMobile.setFocusable(true);
        }

        if (strCurrency != null) {
            edtCurrency.setText(strCurrency);
        } else {
            edtCurrency.setText("USD");
        }

        if (strLanguage != null) {
            edtLanguage.setText(strLanguage);
        } else {
            edtLanguage.setText("ENGLISH");
        }

        btnChangePassword = (CustomButton) view.findViewById(R.id.btn_update_change_password);
        btnUpdateSettings = (CustomButton) view.findViewById(R.id.btn_updatesettings);

        ivCamera = (ImageView) view.findViewById(R.id.iv_camera);

        int color = getResources().getColor(R.color.colorPrimary);
        roundingParams = RoundingParams.fromCornersRadius(5f);
        roundingParams.setBorder(color, 1.0f);
        roundingParams.setRoundAsCircle(true);

        if (userProfPic != null) {
            ivUserPro.setImageURI(Utils.BASE_URL + userProfPic);
            ivUserPro.getHierarchy().setRoundingParams(roundingParams);
        } else {
            ivUserPro.setImageURI(Utils.SAMPLE_PROFILE_PIC);
            ivUserPro.getHierarchy().setRoundingParams(roundingParams);
        }

        imgbtnClearFirstName = (ImageButton) view.findViewById(R.id.imgbtn_clear_first_name);
        imgbtnClearLastName = (ImageButton) view.findViewById(R.id.imgbtn_clear_last_name);

        // validation initalize
        Validation = new AwesomeValidation(BASIC);

        onClearViews(edtFname, imgbtnClearFirstName);
        onClearViews(edtLname, imgbtnClearLastName);

        rcvLanguage.addOnItemTouchListener(
                new RecyclerTouchListener(getActivity(), rcvLanguage,
                        new RecyclerTouchListener.OnTouchActionListener() {
                            @SuppressLint({"LongLogTag", "NewApi"})
                            @Override
                            public void onClick(View view, int position) {
                                Log.i("rvCategory", "onClick: rvCategory-> ");
                                if (position != -1) {
                                  /*  listFeed  = emergencyModelArray.get(position);
                                    callDeleteEmergencyDetails(listFeed.getContact_id());*/
                                    String langId = String.valueOf(ListModel.get(position).getId());
                                    String langName = String.valueOf(ListModel.get(position).getLanguageName());
                                    Log.e("Language~~Onclick~~", langId + "~~" + langName);
                                    dialogLanguage.dismiss();
                                    edtLanguage.setText(langName);

                                    //  LanguageSetting.setLanguage(getActivity(), Locale.forLanguageTag(getLanguage(langName)));
                                }

                            }

                            @Override
                            public void onRightSwipe(View view, int position) {
                            }

                            @Override
                            public void onLeftSwipe(View view, int position) {
                            }
                        }));

        rcvCurrency.addOnItemTouchListener(
                new RecyclerTouchListener(getActivity(), rcvCurrency,
                        new RecyclerTouchListener.OnTouchActionListener() {
                            @SuppressLint("LongLogTag")
                            @Override
                            public void onClick(View view, int position) {
                                Log.i("rvCategory", "onClick: rvCategory-> ");
                                if (position != -1) {
                                  /*  listFeed  = emergencyModelArray.get(position);
                                    callDeleteEmergencyDetails(listFeed.getContact_id());*/
                                    curId = String.valueOf(ListModelCurrency.get(position).getId());
                                    curName = String.valueOf(ListModelCurrency.get(position).getCurrency());
                                    curShortName = String.valueOf(ListModelCurrency.get(position).getShortName());
                                    curSymbol = String.valueOf(ListModelCurrency.get(position).getSymbol());
                                    Log.e("Language~~Onclick~~", curId + "~~" + curName + "~" + curShortName + "~~" + curSymbol);
                                    dialogCurrency.dismiss();
                                    edtCurrency.setText(curShortName);

                                    /*getSpCurrency.putString(Utils.SP_CURRENCY,curShortName);
                                    getSpCurrency.putString(Utils.SP_CURRENCY_SYMBOL,curSymbol);
                                    getSpCurrency.apply();*/
                                }

                            }

                            @Override
                            public void onRightSwipe(View view, int position) {
                            }

                            @Override
                            public void onLeftSwipe(View view, int position) {
                            }
                        }));
        getCurrencyList = loadSharedPreferencesLogList(getActivity());

        onClick();

        CallViewProfileDetails();

        callAppSettings();

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

    private void onClick() {
        btnUpdateSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Validation.addValidation(getActivity(), R.id.edt_firstname, "[a-zA-Z\\s]+", R.string.register_error_firstname);
                Validation.addValidation(getActivity(), R.id.edt_lastname, "[a-zA-Z\\s]+", R.string.register_error_lastname);
                if (Validation.validate()) {
                    /*if (!validatePhone()) {
                        edtCountryCode.setError("");
                        showSnackbar(getString(R.string.register_error_enter_validnum), "Field");
                    } else if (!validateCountryCode()) {
                        showSnackbar(getString(R.string.register_error_enter_validnum), "Field");
                    } else if (!validateUsing_libphonenumber()) {
                        edtMobile.setError(getString(R.string.register_error_invalid_mobile_number));
                    } else if (edtLanguage.getText().toString().equals("")) {
                        edtLanguage.setError(getString(R.string.editsettings_error_enter_language));
                    } else*/
                    if (edtCurrency.getText().toString().equals("")) {
                        edtCurrency.setError(getString(R.string.editsettings_error_enter_currency));
                    } else {
                        strCountyCode = edtCountryCode.getText().toString().trim();
                        strMobile = edtMobile.getText().toString().trim();
                        strfname = edtFname.getText().toString();
                        strlname = edtLname.getText().toString();
                        stremail = edtEmail.getText().toString();
                        strcurrency = edtCurrency.getText().toString();
                        strlang = edtLanguage.getText().toString();
                        strlang = getLanguageIds(edtLanguage.getText().toString());
                        CallUpdateDetails();
                    }
                } else {
                    onBackButton(view);
                }
            }
        });

        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
            }
        });
       /* ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
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
        });*/

        imgbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard();
                backButtonFunction();
            }
        });


        edtLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Testing", Toast.LENGTH_SHORT).show();
                dialogLanguage.show();
            }
        });

        edtCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getActivity(), "Testing", Toast.LENGTH_SHORT).show();
                dialogCurrency.show();
            }
        });


        txtUSD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCurrency.dismiss();
                edtCurrency.setText("USD");
                getSpCurrency.putString(Utils.SP_CURRENCY, "USD");
                getSpCurrency.putString(Utils.SP_CURRENCY_SYMBOL, "Rs ");
                getSpCurrency.apply();

            }
        });

        txtCHF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCurrency.dismiss();
                edtCurrency.setText("CHF");
                getSpCurrency.putString(Utils.SP_CURRENCY, "CHF");
                getSpCurrency.putString(Utils.SP_CURRENCY_SYMBOL, "CNF ");
                getSpCurrency.apply();
            }
        });

        txtEUR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCurrency.dismiss();
                edtCurrency.setText("EUR");
                getSpCurrency.putString(Utils.SP_CURRENCY, "EUR");
                getSpCurrency.putString(Utils.SP_CURRENCY_SYMBOL, "€ ");
                getSpCurrency.apply();
            }
        });

        txtNGS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCurrency.dismiss();
                edtCurrency.setText("NGN");
                getSpCurrency.putString(Utils.SP_CURRENCY, "NGN");
                getSpCurrency.putString(Utils.SP_CURRENCY_SYMBOL, "₦ ");
                getSpCurrency.apply();
            }
        });


        txtEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogLanguage.dismiss();
                edtLanguage.setText("English");
                getSpCurrency.putString(Utils.SP_LANGUAGE, "English");
                getSpCurrency.apply();

            }
        });

        txtSwahili.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogLanguage.dismiss();
                edtLanguage.setText("Swahili");
                getSpCurrency.putString(Utils.SP_LANGUAGE, "swahili");
                getSpCurrency.apply();
            }
        });

        txtFrench.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogLanguage.dismiss();
                edtLanguage.setText("French");
                getSpCurrency.putString(Utils.SP_LANGUAGE, "french");
                getSpCurrency.apply();
            }
        });

        txtSpanish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogLanguage.dismiss();
                edtLanguage.setText("Spanish");
                getSpCurrency.putString(Utils.SP_LANGUAGE, "spanish");
                getSpCurrency.apply();
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogChangePassword();
            }
        });


/*
        ivMenuProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(Objects.requireNonNull(getActivity()), ivMenuProfile);
                popup.getMenuInflater().inflate(R.menu.editprofile_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        switch (id) {
                            case R.id.view_profile:
                                goToViewProfile();
                                break;
                            case R.id.change_password:
                                showDialogChangePassword();
                                break;
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
*/
    }

    private void applyFontToMenuItem(MenuItem mi) {

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.App_Font));
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }


    public String getLanguageIds(String lang) {
        switch (lang) {
            case "English":
                return "1";
            case "Swahili":
                return "2";
            case "French":
                return "3";
            case "Spanish":
                return "5";
            case "Arabic":
                return "4";
            default:
                return "1";
        }
    }

    private void goToViewProfile() {
        Utils.switchContent(R.id.frame_mainactivity,
                Utils.FRAGMENT_SETTINGS, Objects.requireNonNull(getActivity()), null,
                Utils.AnimationType.SLIDE_IN_SLIDE_OUT);
    }


    private void callAppSettings() {
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<AppSettingWebModel> call2 = apiInterface.doGetAppSettings();
        call2.enqueue(new Callback<AppSettingWebModel>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(@NonNull Call<AppSettingWebModel> call, @NonNull Response<AppSettingWebModel> response) {
                hideProgressDialog();
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    List<AppSettingCurrencyModel> currencyTemplates = Objects.requireNonNull(response.body()).getCurrency();
                    //saveSharedPreferencesLogList(Objects.requireNonNull(getActivity()),currencyTemplates);

                    if (currencyTemplates.size() > 0) {
                        ListModelCurrency = currencyTemplates;
                        rcvCurrency.setVisibility(View.VISIBLE);
                        llStaticCurrency.setVisibility(View.GONE);
                        adapterCurrency = new CurrencyAdapter(getActivity(), currencyTemplates);
                        LinearLayoutManager horizontalLayoutManager
                                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                        rcvCurrency.setLayoutManager(horizontalLayoutManager);
                        rcvCurrency.setAdapter(adapterCurrency);
                        adapterCurrency.notifyDataSetChanged();
                    } else {
                        rcvCurrency.setVisibility(View.GONE);
                        llStaticCurrency.setVisibility(View.VISIBLE);
                    }

                    List<AppSettingLanguageModel> languageTemplates = Objects.requireNonNull(response.body()).getLanguage();
                    if (languageTemplates.size() > 0) {
                        ListModel = languageTemplates;
                        rcvLanguage.setVisibility(View.VISIBLE);
                        llStaticLanguage.setVisibility(View.GONE);
                        adapterLanguage = new LanguageAdapter(getActivity(), languageTemplates);
                        LinearLayoutManager horizontalLayoutManager
                                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                        rcvLanguage.setLayoutManager(horizontalLayoutManager);
                        rcvLanguage.setAdapter(adapterLanguage);
                        adapterLanguage.notifyDataSetChanged();
                    } else {
                        rcvLanguage.setVisibility(View.GONE);
                        llStaticLanguage.setVisibility(View.VISIBLE);
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<AppSettingWebModel> call, @NonNull Throwable t) {
                call.cancel();

            }
        });
    }


    private void showDialogChangePassword() {

        dialogChangePassword = new Dialog(Objects.requireNonNull(getActivity()));
        dialogChangePassword.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialogChangePassword.setContentView(R.layout.dialog_change_password);
        Objects.requireNonNull(dialogChangePassword.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogChangePassword.setCancelable(true);
        dialogChangePassword.getWindow().setWindowAnimations(R.style.DialogAnimationLR);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogChangePassword.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        CustomTextView txtOk = (CustomTextView) dialogChangePassword.findViewById(R.id.txt_continue);
        CustomTextView txtCancel = (CustomTextView) dialogChangePassword.findViewById(R.id.txt_cancel);
        CustomEditText edtOldPwd = (CustomEditText) dialogChangePassword.findViewById(R.id.edt_old_pwd);
        CustomEditText edtNewPwd = (CustomEditText) dialogChangePassword.findViewById(R.id.edt_new_pwd);
        CustomEditText edtCnfNewPwd = (CustomEditText) dialogChangePassword.findViewById(R.id.edt_cmf_new_pwd);

        ImageButton imgbtnClearOldPwd = (ImageButton) dialogChangePassword.findViewById(R.id.imgbtn_clear_old_pwd);
        ImageButton imgbtnClearNewPwd = (ImageButton) dialogChangePassword.findViewById(R.id.imgbtn_clear_new_pwd);
        ImageButton imgbtnClearCnfNewPwd = (ImageButton) dialogChangePassword.findViewById(R.id.imgbtn_clear_cnf_new_pwd);

        onClearViews(edtOldPwd, imgbtnClearOldPwd);
        onClearViews(edtNewPwd, imgbtnClearNewPwd);
        onClearViews(edtCnfNewPwd, imgbtnClearCnfNewPwd);

        String userPassword = spUpdateProfile.getString(Utils.SP_USER_PASSWORD, null);

        AwesomeValidation Validation = new AwesomeValidation(BASIC);

        txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // here I want to dismiss it after SetBackground() method
                String strold = Objects.requireNonNull(edtOldPwd.getText()).toString().trim();
                String strnew = Objects.requireNonNull(edtNewPwd.getText()).toString().trim();
                String strcnfnew = Objects.requireNonNull(edtCnfNewPwd.getText()).toString().trim();


                if (strold.length() == 0 || strnew.length() == 0 || strcnfnew.length() == 0) {
                    edtOldPwd.setError(getString(R.string.error_required));
                    edtNewPwd.setError(getString(R.string.error_required));
                    edtCnfNewPwd.setError(getString(R.string.error_required));
                } else {
                    if (userPassword != null) {
                        if (!userPassword.equals("null")) {
                            if (!userPassword.equals("")) {
                                if (userPassword.matches(strold)) {

                                    //   String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,}";
                                    String regexPassword = "(?=.*[a-z])(?=.*[0-9]).{8,}";

                                    Validation.addValidation(edtNewPwd, regexPassword, getResources().getString(R.string.log_in_error_password));
                                    Validation.addValidation(edtCnfNewPwd, regexPassword, getResources().getString(R.string.log_in_error_password));

                                    if (Validation.validate()) {
                                        callPwdFun(strold, strnew, strcnfnew, edtNewPwd, edtCnfNewPwd);
                                    } else {
                                        baseActivity.showSnackbar(getActivity(),contentView,getString(R.string.error_required), "warning");
                                    }

                                } else {
                                    edtOldPwd.setError(getString(R.string.error_old_password_not_match));
                                }
                            } else {
                                callPwdFun(strold, strnew, strcnfnew, edtNewPwd, edtCnfNewPwd);
                            }
                        } else {
                            callPwdFun(strold, strnew, strcnfnew, edtNewPwd, edtCnfNewPwd);
                        }
                    } else {
                        callPwdFun(strold, strnew, strcnfnew, edtNewPwd, edtCnfNewPwd);
                    }
                }
            }

            private void callPwdFun(String strold, String strnew, String strcnfnew, CustomEditText edtNewPwd, CustomEditText edtCnfNewPwd) {
                if (strnew.equals(strcnfnew)) {
                    dialogChangePassword.dismiss();
                    callChangePasswordDetail(strold, strcnfnew);
                } else {
                    edtNewPwd.setError(getString(R.string.error_password_not_match));
                    edtCnfNewPwd.setError(getString(R.string.error_password_not_match));
                }
            }
        });

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogChangePassword.dismiss();
            }
        });

        dialogChangePassword.show();
        dialogChangePassword.getWindow().setAttributes(lp);
    }

    @SuppressLint("LongLogTag")
    private void callChangePasswordDetail(String strold, String strcnfnew) {
        userAccessToken = spUpdateProfile.getString(Utils.SP_USER_ACCESSTOKEN, null);
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", userId);
        map.put("old_password", strold);
        map.put("new_password", strcnfnew);
        Log.i("callChangePasswordDetail", "~~~~" + map);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ChangePasswordModel> call2 = apiInterface.doUpdateChangePassword("Bearer " + userAccessToken, map);
        showProgressDialog(getActivity());
        call2.enqueue(new Callback<ChangePasswordModel>() {
            @Override
            public void onResponse(@NonNull Call<ChangePasswordModel> call, @NonNull Response<ChangePasswordModel> response) {
                hideProgressDialog();
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Password updated successfully.")) {

                        getUpdateProfile.putString(Utils.SP_USER_PASSWORD, strcnfnew);
                        getUpdateProfile.apply();

                        showProfileUpdateAlertDialog(getActivity(), getString(R.string.editsettings_update_successfully));

                    } else {
                        Toast.makeText(getActivity(), R.string.try_again, Toast.LENGTH_SHORT).show();
                    }
                } else if (response.code() == RESPONSECODE_401) {
                    callRefreshToken("changepassword", strold, strcnfnew);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ChangePasswordModel> call, @NonNull Throwable t) {
                // CommonUtil.showRetrofitError(LoginActivity.this, error);
                hideProgressDialog();
                call.cancel();
            }
        });
    }


    private void CallUpdateDetails() {

        userAccessToken = spUpdateProfile.getString(Utils.SP_USER_ACCESSTOKEN, null);
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", userId);
        map.put("first_name", strfname);
        map.put("last_name", strlname);
        map.put("email", stremail);
        map.put("phone_number", strMobile);
        map.put("country_code", strCountyCode);
        //map.put("language", strlang);
        map.put("currency", strcurrency);

        Log.i("CallUpdateDetails", "~~~~" + map);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ProfileUpdateModel> call2 = apiInterface.doGetprofileUpdateDetails("Bearer " + userAccessToken, map);
        showProgressDialog(getActivity());
        call2.enqueue(new Callback<ProfileUpdateModel>() {
            @Override
            public void onResponse(@NonNull Call<ProfileUpdateModel> call, @NonNull Response<ProfileUpdateModel> response) {
                hideProgressDialog();
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Customer updated successfully.")) {
                        // String strCurrencySymbol = getCurrencySymbol(strCurrency);
                        String strCurrencySymbol = Objects.requireNonNull(response.body()).getProfileUpdateData().getCurrencySymbol();
                        String strCurrenc = Objects.requireNonNull(response.body()).getProfileUpdateData().getCurrency();
                        getLang_EditProfile = Objects.requireNonNull(response.body()).getProfileUpdateData().getLanguage();
                        //MainActivity.searchCurrencyPrice(strCurrenc.toString());
                        searchCurrencyPrice(strCurrenc.toString(), strCurrencySymbol);

                    } else {
                        Toast.makeText(getActivity(), R.string.try_again, Toast.LENGTH_SHORT).show();
                        callRefreshToken("editprofile", "", "");
                    }
                } else if (response.code() == RESPONSECODE_401) {
                    callRefreshToken("editprofile", "", "");
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

    public void searchCurrencyPrice(String Currency, String strCurrencySymbol) {

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
                        getSpCurrency.putString(Utils.SP_CURRENCY_COMMON_VALUES, currencyPriceObj.toString());
                        getSpCurrency.apply();
                        savesharedpreference(strfname, strlname, stremail, strCountyCode, strMobile, Currency, strCurrencySymbol, strlang);
                        showProfileUpdateAlertDialog(getActivity(), getString(R.string.editsettings_update_successfully));
                    } else {
                        getSpCurrency.putString(Utils.SP_CURRENCY_COMMON_VALUES, "1");
                        getSpCurrency.apply();
                        savesharedpreference(strfname, strlname, stremail, strCountyCode, strMobile, Currency, strCurrencySymbol, strlang);
                        showProfileUpdateAlertDialog(getActivity(), getString(R.string.editsettings_update_successfully));
                    }
                    if (driverSearchDatabaseBRef != null) {
                        driverSearchDatabaseBRef.removeEventListener(driverSearchValueEventListener);
                    }

                } else {
                    getSpCurrency.putString(Utils.SP_CURRENCY_COMMON_VALUES, "1");
                    getSpCurrency.apply();
                    savesharedpreference(strfname, strlname, stremail, strCountyCode, strMobile, Currency, strCurrencySymbol, strlang);
                    showProfileUpdateAlertDialog(getActivity(), getString(R.string.editsettings_update_successfully));
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
                savesharedpreference(strfname, strlname, stremail, strCountyCode, strMobile, Currency, strCurrencySymbol, strlang);
                showProfileUpdateAlertDialog(getActivity(), getString(R.string.editsettings_update_successfully));
                if (driverSearchDatabaseBRef != null) {
                    driverSearchDatabaseBRef.removeEventListener(driverSearchValueEventListener);
                }
            }
        });

    }

    private void CallViewProfileDetails() {
        userAccessToken = spUpdateProfile.getString(Utils.SP_USER_ACCESSTOKEN, null);
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", userId);
        map.put("token", userAccessToken);
        Log.i("CallViewProfileDetails", "~~~~" + map);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ProfileModel> call2 = apiInterface.doGetprofileDetails("Bearer " + userAccessToken, Utils.POST_ACCEPT_CONTENT_TYPE, map);
        showProgressDialog(getActivity());
        call2.enqueue(new Callback<ProfileModel>() {
            @Override
            public void onResponse(@NonNull Call<ProfileModel> call, @NonNull Response<ProfileModel> response) {

                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {

                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Profile listed successfully.")) {
                        edtFname.setText(Objects.requireNonNull(response.body()).getProfileData().getFirstName());
                        edtLname.setText(Objects.requireNonNull(response.body()).getProfileData().getLastName());
                        edtEmail.setText(Objects.requireNonNull(response.body()).getProfileData().getEmail());
                        edtMobile.setText(Objects.requireNonNull(response.body()).getProfileData().getPhoneNumber());
                        edtCountryCode.setText(Objects.requireNonNull(response.body()).getProfileData().getCountryCode());
                        getLang_ViewProfile = Objects.requireNonNull((Objects.requireNonNull(response.body())).getProfileData().getLanguage());
                        edtLanguage.setText(getLang_ViewProfile);
                        edtCurrency.setText(Objects.requireNonNull((Objects.requireNonNull(response.body())).getProfileData().getCurrency()));

                        String getProfPic = String.valueOf(Objects.requireNonNull(response.body()).getProfileData().getAvatar());
                        getProfPic = getProfPic.replaceAll(" ", "%20");
                        ivUserPro.setImageURI(Utils.BASE_URL + getProfPic);
                        ivUserPro.getHierarchy().setRoundingParams(roundingParams);

                        ivBackcoverUserPro.setImageURI(getProfPic);
                        hideProgressDialog();
                    } else {
                        callRefreshToken("viewprofile", "", "");
                        hideProgressDialog();
                    }
                    hideProgressDialog();
                } else if (response.code() == RESPONSECODE_401) {
                    callRefreshToken("viewprofile", "", "");
                } else if (response.code() == RESPONSECODE_500) {
                    callRefreshToken("viewprofile", "", "");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProfileModel> call, @NonNull Throwable t) {
                hideProgressDialog();
                call.cancel();
            }
        });

    }


    private void callRefreshToken(String logout, String oldpwd, String newpwd) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", userId);
        map.put("token", userAccessToken);
        Log.e("Logout~~Refresh~~~", "Refresh~~~" + map);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<RefreshTokenModel> call2 = apiInterface.doGetRefreshToken(Utils.POST_ACCEPT_CONTENT_TYPE, Utils.POST_ACCEPT_CONTENT_TYPE, map);
        call2.enqueue(new Callback<RefreshTokenModel>() {
            @Override
            public void onResponse(@NonNull Call<RefreshTokenModel> call, @NonNull Response<RefreshTokenModel> response) {
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    String refreshToken = Objects.requireNonNull(response.body()).getAccessToken();
                    String refreshTokenType = Objects.requireNonNull(response.body()).getTokenType();
                    String refreshTokenTimeExp = String.valueOf(Objects.requireNonNull(response.body()).getExpiresIn());
                    savesharedpreference(refreshToken, refreshTokenType, refreshTokenTimeExp);
                    if (logout.equalsIgnoreCase("viewprofile")) {
                        CallViewProfileDetails();
                    } else if (logout.equalsIgnoreCase("editprofile")) {
                        CallUpdateDetails();
                    } else if (logout.equalsIgnoreCase("changepassword")) {
                        callChangePasswordDetail(oldpwd, newpwd);
                    }
                } else if (response.code() == RESPONSECODE_401) {
                    showTimeOut();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RefreshTokenModel> call, @NonNull Throwable t) {
                call.cancel();
            }
        });
    }

    private void savesharedpreference(String access_token, String token_type, String exp_time) {

        getUpdateProfile.putString(Utils.SP_USER_ACCESSTOKEN, access_token);
        getUpdateProfile.putString(Utils.SP_USER_TOKENTYPE, token_type);
        getUpdateProfile.putString(Utils.SP_USER_TOKENEXPTIME, exp_time);
        getUpdateProfile.apply();
    }

    private void savesharedpreference(String strfname, String strlname, String stremail, String cc, String strmobile, String currency, String currencySymbol, String language) {

        getUpdateProfile.putString(Utils.SP_USER_FNAME, strfname);
        getUpdateProfile.putString(Utils.SP_USER_LNAME, strlname);
        getUpdateProfile.putString(Utils.SP_USER_EMAIL, stremail);
        getUpdateProfile.putString(Utils.SP_USER_MOBILE, strmobile);
        getUpdateProfile.putString(Utils.SP_USER_COUNTRYCODE, cc);
        getUpdateProfile.apply();

        DatabaseReference refs = FirebaseDatabase.getInstance().getReference();
        refs.child(Utils.FIREBASE_USER_HEADER).child(userId).child(Utils.FIREBASE_USER_FNAME).setValue(strfname);
        refs.child(Utils.FIREBASE_USER_HEADER).child(userId).child(Utils.FIREBASE_USER_LNAME).setValue(strlname);
        refs.child(Utils.FIREBASE_USER_HEADER).child(userId).child(Utils.FIREBASE_USER_EMAIL).setValue(stremail);
        refs.child(Utils.FIREBASE_USER_HEADER).child(userId).child(Utils.FIREBASE_USER_CC).setValue(cc);
        refs.child(Utils.FIREBASE_USER_HEADER).child(userId).child(Utils.FIREBASE_USER_MOBILE).setValue(strmobile);
        refs.child(Utils.FIREBASE_USER_HEADER).child(userId).child("currency").setValue(currency + ":" + currencySymbol);
        //refs.child(Utils.FIREBASE_USER_HEADER).child(userId).child("language").setValue(language);
        //refs.child(Utils.FIREBASE_USER_HEADER).child(userId).child("status").setValue("0");

        getSpCurrency.putString(Utils.SP_CURRENCY, currency);
        getSpCurrency.putString(Utils.SP_CURRENCY_SYMBOL, currencySymbol);

       /* getSpCurrency.putString(Utils.SP_LANGUAGE,strlang);
        getSpCurrency.putString(Utils.SP_LANGUAGE_SHORT_NAME,getLanguage(strlang));*/
        getSpCurrency.apply();

    }


    /* public String getCurrencySymbol(String lang){
         switch (lang) {
             case "LKR":
                 return "Rs ";
             case "EUR":
                 return "€ ";
             case "CHF":
                 return "CNF ";
             case "NGN":
                 return "₦ ";
             default:
                 return "Rs ";
         }
     }*/
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
                        Utils.switchContent(R.id.frame_mainactivity,
                                Utils.FRAGMENT_SETTINGS, Objects.requireNonNull(getActivity()), null,
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
            edtMobile.setError(getString(R.string.register_error_enter_country_code));
            return false;
        } else if (edtCountryCode.getText().toString().equals("CC")) {
            edtMobile.setError(getString(R.string.register_error_enter_country_code));
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("onActivity Result----", String.valueOf(requestCode) + String.valueOf(resultCode) + String.valueOf(data));
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_IMAGE_REQUEST) {

                Uri path = Uri.parse(spUpdateProfile.getString("photo", null));
                if (path != null) {

                    String picturePath = getRealPathFromURI(path);
                    Log.i(TAG, "onActivityResult: insideer-->" + picturePath);
                    File finalFile = new File(picturePath);
                    //   File imageZipperFile=new ImageZipper(getActivity()).setQuality(10).setMaxWidth(200).setMaxHeight(200).compressToFile(finalFile);
                    Bitmap photo = null;
                    try {
                        photo = new ImageZipper(getActivity()).setQuality(10).setMaxWidth(200).setMaxHeight(200).compressToBitmap(finalFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                        if (data.getData() != null) {
                            photo = (Bitmap) data.getExtras().get("data");
                        }
                    }

                    fileBinaryData = getStringImage(photo);

                    Random r = new Random();
                    int i1 = r.nextInt(80 - 65) + 65;
                    strProPic = String.valueOf(i1) + ".png";
                    try {
                        ExifInterface exifObject = new ExifInterface(picturePath);
                        int orientation = exifObject.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                        Bitmap imageRotate = rotateBitmap(photo, orientation);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    CallUpdateProfilepicDetails(fileBinaryData, strProPic);
                }

            }
            else if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
                try {

                    String picturePath = null;
                    Bitmap photo = null;
                    Uri selectedImage = data.getData();
                    System.out.println("mSelectedFissslssePath res~~~~~~" + selectedImage);
                    if (Build.VERSION.SDK_INT >= 19) {
                        if (selectedImage != null && !selectedImage.toString().equals("null")) {
                            System.out.println("greater 19:" + "kitkat");
//                        picturePath = getImagePath(selectedImage);
                            if (selectedImage!=null){
                                if (!selectedImage.equals("null")){
                                    picturePath = selectedImage.getPath();
                                }else
                                {
                                    picturePath = getRealPathFromURI(fileUri);
                                }


                            }else
                            {
                                picturePath = getRealPathFromURI(fileUri);
                                Log.i(TAG, "onActivityResult: insideer-->" + picturePath);
                            }
                            System.out.println("mSelectedFissslssePath res" + picturePath);
                        } else {
                            System.out.println("greater 19:" + "not kitkat");
                            picturePath = getPathOfImage(selectedImage);
                            System.out.println("mSelectedFissslePath res" + picturePath);
                        }
                    }
                    // picturePath = getRealPathFromURI(selectedImage);
                    File finalFile = new File(picturePath);
                    try {
                        assert picturePath != null;
                        final int takeflag= data.getFlags() &(Intent.FLAG_GRANT_READ_URI_PERMISSION |Intent.FLAG_GRANT_WRITE_URI_PERMISSION );
                        getActivity().getContentResolver().takePersistableUriPermission(selectedImage,takeflag);

                        AssetFileDescriptor parcelFileDescriptor =  getActivity().getContentResolver().openAssetFileDescriptor(selectedImage, "r", null);

                        FileInputStream inputStream = new FileInputStream(parcelFileDescriptor.getFileDescriptor());
                        File fileDescriptor = new File(getActivity().getCacheDir(),  finalFile.getName());
                        FileOutputStream outputStream = new FileOutputStream(fileDescriptor);
                        IOUtils.copyStream(inputStream, outputStream);
                        photo = new ImageZipper(getActivity()).setQuality(10).setMaxWidth(200).setMaxHeight(200).compressToBitmap(fileDescriptor);
                    } catch (IOException e) {
                        e.printStackTrace();
                        if (data.getData() != null) {
                            try {
                                photo = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());

                                Log.e(TAG, "onActivityResult: ~~0000~" + photo);
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                    filePath = data.getData();
                    //Converting Bitmap to String
                    fileBinaryData = getStringImage(photo);
                    strProPic = finalFile.getName();
                    if (strProPic.equals("")) {
                        Random r = new Random();
                        int i1 = r.nextInt(80 - 65) + 65;
                        strProPic = String.valueOf(i1) + ".png";
                    }
                    //Log.e(TAG, "onActivityResult: ~~~100~~~"+finalFile);
                    // Log.e(TAG, "onActivityResult: ~~~1~~~~~~~"+strProPic);
                    // System.out.println(TAG+ "onActivityResult: ~~~1~~~"+fileBinaryData);
                    CallUpdateProfilepicDetails(fileBinaryData, strProPic);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), R.string.try_again, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), R.string.pic_failed_error,
                        Toast.LENGTH_SHORT).show();
            }

        }
    }

    public String getImagePath(Uri uri) {
        String path = "";
        try {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            String document_id = cursor.getString(0);
            document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
            cursor.close();

            cursor = getActivity().getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
            cursor.moveToFirst();
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            System.out.println("path111:" + path);
            cursor.close();
        } catch (Exception e) {

        }
        return path;
    }

    private String getPathOfImage(Uri uri) {
        String wholeID = DocumentsContract.getDocumentId(uri);

        System.out.println("WholeId:" + wholeID);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = {MediaStore.Images.Media.DATA};

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = getActivity().getContentResolver().
                query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{id}, null);

        String filePath = "";

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }

        System.out.println("File Path1:" + filePath);
        cursor.close();
        return filePath;
    }

    public String getRealPathFromURI(Uri contentUri) {
        try {
            String[] proj = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return contentUri.getPath();
        }
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showCustomDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_upload_photo);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.getWindow().setWindowAnimations(R.style.DialogAnimationLRS);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);

        final ImageButton imgbtn_back = (ImageButton) dialog.findViewById(R.id.imgbtn_back);
        imgbtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUpdateProfile.putString("photo", null);
                getUpdateProfile.apply();

                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.btntake_photo).setOnClickListener(v -> {

            if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                dialog.dismiss();
                cameraIntent();
            } else {
                dialog.cancel();
                PermissionDialog("Camera");
            }
        });

        dialog.findViewById(R.id.btnchoose_photo).setOnClickListener(v -> {
            if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED || getActivity().checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                dialog.dismiss();
                galleryIntent();
            } else {
                dialog.cancel();
                PermissionDialog("Gallery");
            }
        });

        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
    }

    private void galleryIntent() {
       /* @SuppressLint("IntentReset")
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/*");
        startActivityForResult(i, PICK_IMAGE_REQUEST);*/
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent,"select"),PICK_IMAGE_REQUEST);
    }

    private void cameraIntent() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Image File name");
        fileUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        getUpdateProfile.putString("photo", String.valueOf(fileUri));
        getUpdateProfile.apply();

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(cameraIntent, CAMERA_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode <= 5) {
            if (grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                PermissionDialog("Camera");
            } else {
                cameraIntent();
            }
        } else if (requestCode >= 7) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                PermissionDialog("Gallery");
            } else {
                galleryIntent();
            }
        }
    }

    private String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        Log.i("encodedImage", encodedImage);
        return encodedImage;
    }


    @SuppressLint("LongLogTag")
    private void CallUpdateProfilepicDetails(String fileBinaryData, String strProPic) {

        userAccessToken = spUpdateProfile.getString(Utils.SP_USER_ACCESSTOKEN, null);

        HashMap<String, Object> map = new HashMap<>();
        map.put("id", userId);
        map.put("file_binary_data", fileBinaryData);
        map.put("avatar", strProPic);
        Log.i("CallUpdateProfilepicDetails", "~~~" + map);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ProfilePicModel> call2 = apiInterface.doGetprofilePhotoDetails("Bearer " + userAccessToken, map);
        showProgressDialog(getActivity());
        call2.enqueue(new Callback<ProfilePicModel>() {
            @Override
            public void onResponse(@NonNull Call<ProfilePicModel> call, @NonNull Response<ProfilePicModel> response) {

                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("customer updated successfully.")) {

                        String profileResponse = response.body().getProfilePicData().getAvatar();
                        profileResponse = profileResponse.replaceAll(" ", "%20");
                        Log.e("profileResponse", profileResponse);

                        /*if (selectedImageUri == null) {
                            imagee = "";
                            Log.i("ifffff", "ifffffffffff");
                        } else {
                            imagee = selectedImageUri.toString();
                            Log.i("imageeeee", "" + imagee);
                        }*/
                        ivUserPro.setImageURI(Utils.BASE_URL + profileResponse);
                        ivUserPro.getHierarchy().setRoundingParams(roundingParams);

                        String finalProfileResponse = profileResponse;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hideProgressDialog();
                                showProfileImageUpdateAlertDialog(getActivity(), getString(R.string.edit_settings_image_update_successfully),
                                        finalProfileResponse);
                            }
                        }, 1800);

                    } else {
                        hideProgressDialog();
                        baseActivity.showSnackbar(getActivity(),contentView,getString(R.string.update_not_successfully), "warning");
                    }

                } else if (response.code() == RESPONSECODE_401) {
                    hideProgressDialog();
                    callRefreshTokens(fileBinaryData, strProPic, "Profile_pic");
                }

            }

            @Override
            public void onFailure(@NonNull Call<ProfilePicModel> call, @NonNull Throwable t) {
                Log.e("callonFailure~~~", String.valueOf(t));
                hideProgressDialog();
                call.cancel();
            }
        });


    }

    private void callRefreshTokens(String fileBinaryData, String strProPic, String logout) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", userId);
        map.put("token", userAccessToken);
        Log.e("Logout~~Refresh~~~", "Refresh~~~" + map);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<RefreshTokenModel> call2 = apiInterface.doGetRefreshToken(Utils.POST_ACCEPT_CONTENT_TYPE, Utils.POST_ACCEPT_CONTENT_TYPE, map);
        call2.enqueue(new Callback<RefreshTokenModel>() {
            @Override
            public void onResponse(@NonNull Call<RefreshTokenModel> call, @NonNull Response<RefreshTokenModel> response) {
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    String refreshToken = Objects.requireNonNull(response.body()).getAccessToken();
                    String refreshTokenType = Objects.requireNonNull(response.body()).getTokenType();
                    String refreshTokenTimeExp = String.valueOf(Objects.requireNonNull(response.body()).getExpiresIn());
                    savesharedpreference(refreshToken, refreshTokenType, refreshTokenTimeExp);
                    if (logout.equalsIgnoreCase("Profile_pic")) {
                        CallUpdateProfilepicDetails(fileBinaryData, strProPic);
                    }
                } else if (response.code() == RESPONSECODE_401) {
                    showTimeOut();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RefreshTokenModel> call, @NonNull Throwable t) {
                call.cancel();
            }
        });
    }

    public void showProfileImageUpdateAlertDialog(Context context, String message, final String profilepic) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        Typeface tf = Typeface.createFromAsset(Objects.requireNonNull(getActivity()).getAssets(),
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

                        MainActivity.navigationProfileImage.setImageURI(Utils.BASE_URL + profilepic);
                        MainActivity.navigationProfileImage.getHierarchy().setRoundingParams(roundingParams);

                        getUpdateProfile.putString(Utils.SP_USER_PRO_PIC, profilepic);
                        getUpdateProfile.putString("photo", null);
                        getUpdateProfile.apply();

                        DatabaseReference refs = FirebaseDatabase.getInstance().getReference();
                        refs.child(Utils.FIREBASE_USER_HEADER).child(userId).child(Utils.FIREBASE_USER_PRO_PIC).setValue(profilepic);

                        Utils.switchContent(R.id.frame_mainactivity,
                                Utils.FRAGMENT_SETTINGS, Objects.requireNonNull(getActivity()), null,
                                Utils.AnimationType.SLIDE_IN_SLIDE_OUT);
                    }
                }).show();
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
        // removeFragment(Utils.FRAGMENT_EDITSETTINGS);
        Utils.switchContent(R.id.frame_mainactivity,
                Utils.FRAGMENT_SETTINGS,
                getActivity(), null,
                Utils.AnimationType.SLIDE_IN_SLIDE_OUT);
    }

    public void removeFragment(String Tag) {
        Utils.CURRENT_TAG = null;
        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag(Tag)).commit();
    }

    public void hideKeyBoard() {
        // Check if no view has focus:
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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

    public String getCurrencySymbol(String currencyCode) {
        String search = currencyCode;
        String symbol = "";
        int searchListLength = getCurrencyList.size();
        for (int i = 0; i < searchListLength; i++) {
            if (getCurrencyList.get(i).getShortName().contains(search)) {
                Log.i(TAG, "getCurrencyPrice: search-->" + getCurrencyList.get(i).getConverionsRatio());
                symbol = String.valueOf(getCurrencyList.get(i).getSymbol());
                break;
            }
        }
        return symbol;
    }

    @Override
    public void onBeforeLocaleChanged() {

    }

    @Override
    public void onAfterLocaleChanged() {

    }

    Locale getCurrentLocale(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return context.getResources().getConfiguration().getLocales().get(0);
        } else {
            //noinspection deprecation
            return context.getResources().getConfiguration().locale;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //hideProgressDialog();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //hideProgressDialog();
    }


    private void PermissionDialog(String type) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.App_Font));

        BaseActivity.CustomTFSpan tfSpan = new BaseActivity.CustomTFSpan(tf);
        SpannableString spannableStrings = new SpannableString(getResources().getString(R.string.permit_manually));
        spannableStrings.setSpan(tfSpan, 0, spannableStrings.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString spannableStringss = new SpannableString(getResources().getString(R.string.cancel));
        spannableStringss.setSpan(tfSpan, 0, spannableStringss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString spannableStringssd = new SpannableString(getResources().getString(R.string.ok));
        spannableStringssd.setSpan(tfSpan, 0, spannableStringssd.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString spannableString = new SpannableString(getString(R.string.provide_camera_permission));
        spannableString.setSpan(tfSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString spannableStringGallery = new SpannableString(getString(R.string.provide_gallery_permission));
        spannableStringGallery.setSpan(tfSpan, 0, spannableStringGallery.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        if (type.equals("Camera")){
            builder.setMessage(spannableString);
        } else {
            builder.setMessage(spannableStringGallery);
        }

        builder.setCancelable(false);

        builder.setPositiveButton(spannableStringssd, (dialog, which) -> {
            dialog.dismiss();
            Intent intent = new Intent();
            intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        });
        builder.setNegativeButton(spannableStringss, null);
        builder.show();
    }
}

