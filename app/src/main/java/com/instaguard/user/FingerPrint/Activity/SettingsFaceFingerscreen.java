package com.instaguard.user.FingerPrint.Activity;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.kyleduo.switchbutton.SwitchButton;
import com.instaguard.user.CustomViews.CustomTextView;
import com.instaguard.user.Helpers.Localizationactivity.ui.LocalizationActivity;
import com.instaguard.user.Helpers.Utils;
import com.instaguard.user.R;

import java.util.concurrent.Executor;

@SuppressLint("CustomSplashScreen")
public class SettingsFaceFingerscreen extends LocalizationActivity {

    protected static AppCompatActivity activity;

    SharedPreferences getStatePref, getStatePrefNew;
    static SharedPreferences.Editor putState, putStateNew;
    String faceUserId, faceStatus, faceNoValue;
    static String userId;
    public static String TAG = "SettingsFaceFingerFragment";

    private CustomTextView tvFingerPrintLabel;
    private CustomTextView tvFaceLabel;
    private View ivFacePrint;
    private View ivFingerPrint;

    private CustomTextView tvFingerPrintStatus;
    private SwitchButton sbOnOffFingerprint;
    private static CustomTextView tvFaceStatus;
    private static SwitchButton sbOnOffFace;
    private RelativeLayout rlParentViews;
    private ImageView imgBtnBack;

    String fingerUserId, fingerStatus, fingerNoValue, strType;


    String emailCode;
    CustomTextView tvToolbar;

    private View vLineVertical1;
    private View vLine2;
    private ImageView ivEmail;
    private CustomTextView tvEmailLabel;
    private CustomTextView tvEmailLabel1;
    private CustomTextView tvEmailStatus;
    private SwitchButton sbOnOffEmail;
    public boolean enableState = false;

    private static final int RC_HANDLE_CAMERA_PERM_RGB = 1;
    private static final int RC_HANDLE_CAMERA_PERM_GRAY = 2;

    KeyguardManager keyguardManager;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private BiometricPrompt.PromptInfo promptInfoKey;
    int RC_BIOMETRICS_ENROLL = 10;
    int RC_DEVICE_CREDENTIAL_ENROLL = 18;


    @SuppressLint("CommitPrefEdits")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_face_finger);

        getStatePref = getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE);
        putStateNew = getSharedPreferences(Utils.MY_PREFS_NAME_NEW, MODE_PRIVATE).edit();
        getStatePrefNew = getSharedPreferences(Utils.MY_PREFS_NAME_NEW, MODE_PRIVATE);

        fingerUserId = getStatePrefNew.getString(Utils.spFingerUserId, null);
        fingerStatus = getStatePrefNew.getString(Utils.spFingerStatus, null);
        fingerNoValue = getStatePrefNew.getString(Utils.spFingerNoStatus, null);

        faceUserId = getStatePrefNew.getString(Utils.spFaceUserId, null);
        faceStatus = getStatePrefNew.getString(Utils.spFaceStatus, null);
        faceNoValue = getStatePrefNew.getString(Utils.spFaceNoStatus, null);

        Log.i(TAG, "onCreateView: fingerprint--->" + userId + "~~~" + fingerUserId + "~~~" + fingerStatus + "~~~~" + fingerNoValue);
        Log.i(TAG, "onCreateView: faceprint--->" + userId + "~~~" + faceUserId + "~~~" + faceStatus + "~~~~" + faceNoValue);

        userId = getStatePref.getString(Utils.SP_USERID, null);

        initView();

        //initToolbar();

        //callProfileDetails();

        enableFingerPrint();

        checkDeviceCanAuthenticateWithBiometrics();

        if (fingerBoolean) {
            callClickActionView(sbOnOffFingerprint, true);
        } else {
            callClickActionView(sbOnOffFingerprint, false);
            sbOnOffFingerprint.setChecked(false);
        }

        tvFingerPrintLabel.setOnClickListener(view -> {
            // biometricPrompt.authenticate(promptInfo);
            if (fingerBoolean) {
                sbOnOffFingerprint.performClick();
            } else {
                checkAPILevelAndProceed();
            }
        });

        ivFingerPrint.setOnClickListener(view -> {
            tvFingerPrintLabel.performClick();
        });

        tvFaceLabel.setOnClickListener(view -> {
            sbOnOffFace.performClick();
        });

        ivFacePrint.setOnClickListener(view -> {
            tvFaceLabel.performClick();
        });

        sbOnOffFingerprint.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                tvFingerPrintStatus.setText(getString(R.string.settings_status_on));
                if (fingerBoolean) {

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        if (CheckKeyGaurd()) {
                            biometricPrompt.authenticate(promptInfoKey);
                        } else {
                            setUpDeviceLockInAPIBelow23Intent();
                        }
                    } else {
                        if (CheckKeyGaurd()) {
                            biometricPrompt.authenticate(promptInfoKey);
                        } else {
                            System.out.println("Else!!0");
                            biometricPrompt.authenticate(promptInfo);
                        }
                    }

                    /*if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        keyguardManager = (KeyguardManager) SettingsFaceFingerscreen.this.getSystemService(KEYGUARD_SERVICE);
                        if (keyguardManager.isKeyguardSecure()) {
                            biometricPrompt.authenticate(promptInfoKey);
                        } else {
                            setUpDeviceLockInAPIBelow23Intent();
                        }
                    } else {
                        System.out.println("Else");
                        biometricPrompt.authenticate(promptInfo);
                    }*/
                } else {

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        if (CheckKeyGaurd()) {
                            biometricPrompt.authenticate(promptInfoKey);
                        } else {
                            setUpDeviceLockInAPIBelow23Intent();
                        }
                    } else {
                        if (CheckKeyGaurd()) {
                            biometricPrompt.authenticate(promptInfoKey);
                        } else {
                            System.out.println("Else!!1");
                            biometricPrompt.authenticate(promptInfo);
                        }
                    }

                    /*if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        keyguardManager = (KeyguardManager) SettingsFaceFingerscreen.this.getSystemService(KEYGUARD_SERVICE);
                        if (keyguardManager.isKeyguardSecure()) {
                            biometricPrompt.authenticate(promptInfoKey);
                        } else {
                            setUpDeviceLockInAPIBelow23Intent();
                        }
                    } else {
                        tvFingerPrintStatus.setText(getString(R.string.settings_status_off));
                    }*/
                }
            } else {
                setUpdateFingerPrintStatus("0");
                tvFingerPrintStatus.setText(getString(R.string.settings_status_off));
            }
        });

        sbOnOffFace.setOnCheckedChangeListener((buttonView, isChecked) -> {
            /*if (isChecked) {
                int rc = ActivityCompat.checkSelfPermission(SettingsFaceFingerscreen.this, Manifest.permission.CAMERA);
                if (rc == PackageManager.PERMISSION_GRANTED) {
                    Intent faceRecognisation = new Intent(SettingsFaceFingerscreen.this, FaceRecognistaionActivity.class);
                    faceRecognisation.putExtra("page", "setting");
                    startActivity(faceRecognisation);
                } else {
                    requestCameraPermission(RC_HANDLE_CAMERA_PERM_GRAY);
                }
            } else {
                tvFaceStatus.setText(getString(R.string.settings_status_off));
                putStateNew.putString(Utils.spFaceStatus, null);
                putStateNew.apply();
            }*/

            Toast.makeText(SettingsFaceFingerscreen.this, "Under Construct", Toast.LENGTH_SHORT).show();
        });

        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButtonFunction();
            }
        });
        rlParentViews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void callClickActionView(View view, boolean b) {
        view.setEnabled(b);
        view.setClickable(b);
        view.setFocusableInTouchMode(b);
        view.setFocusable(b);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @SuppressLint("SetTextI18n")
    public static void faceRecognistiaonState(boolean state) {
        Log.i(TAG, "faceRecognistiaonState: userid--->" + userId);
        if (state) {
            //Toast.makeText(activity, R.string.regonize_face_active, Toast.LENGTH_SHORT).show();
            sbOnOffFace.setChecked(state);
            tvFaceStatus.setText(R.string.settings_status_on);
            putStateNew.putString(Utils.spFaceStatus, "1");

            putStateNew.putString(Utils.spFaceUserId, userId);
            putStateNew.putString(Utils.spFingerUserId, userId);

            putStateNew.apply();
        } else {
            //Toast.makeText(activity, R.string.regonize_face_not_active, Toast.LENGTH_SHORT).show();
            sbOnOffFace.setChecked(state);
            tvFaceStatus.setText(R.string.settings_status_off);
            putStateNew.putString(Utils.spFaceStatus, "0");

            putStateNew.putString(Utils.spFaceUserId, userId);
            putStateNew.putString(Utils.spFingerUserId, userId);

            putStateNew.apply();
        }

    }


    public void setUpdateFingerPrintStatus(String status) {

        Log.i(TAG, "setUpdateFingerPrintStatus: userid--->" + userId);

        putStateNew.putString(Utils.spFingerUserId, userId);
        putStateNew.putString(Utils.spFaceUserId, userId);

        putStateNew.putString(Utils.spFingerStatus, status);
        putStateNew.apply();

        if (status.equals("0")) {
            sbOnOffFingerprint.setChecked(false);
        } else {
            sbOnOffFingerprint.setChecked(true);
        }
    }

    public void enableFingerPrint() {

        if (fingerNoValue == null) {
            if (fingerUserId != null) {

                if (fingerStatus != null) {
                    if (fingerStatus.equals("1")) {
                        tvFingerPrintStatus.setText(getString(R.string.settings_status_on));
                        sbOnOffFingerprint.setChecked(true);
                    } else {
                        tvFingerPrintStatus.setText(getString(R.string.settings_status_off));
                        sbOnOffFingerprint.setChecked(false);
                    }
                }
            } else {
                tvFingerPrintStatus.setText(getString(R.string.settings_status_off));
                sbOnOffFingerprint.setChecked(false);
            }
        }

        if (faceStatus != null) {
            if (faceStatus.equals("1")) {
                tvFaceStatus.setText(getString(R.string.settings_status_on));
                sbOnOffFace.setChecked(true);
            } else {
                tvFaceStatus.setText(getString(R.string.settings_status_off));
                sbOnOffFace.setChecked(false);
            }
        } else {
            tvFaceStatus.setText(getString(R.string.settings_status_off));
            sbOnOffFace.setChecked(false);
        }

    }

    private void requestCameraPermission(final int RC_HANDLE_CAMERA_PERM) {
        Log.w(TAG, "Camera permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.CAMERA};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, RC_HANDLE_CAMERA_PERM);
        }
        // ActivityCompat.requestPermissions(activity, permissions, RC_HANDLE_CAMERA_PERM);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i("permission", "Request Code: " + requestCode);
        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && requestCode == RC_HANDLE_CAMERA_PERM_GRAY) {
           /* Intent faceRecognisation = new Intent(activity, FaceRecognistaionActivity.class);
            faceRecognisation.putExtra("page", "setting");
            startActivity(faceRecognisation);*/
            return;
        } else {
            sbOnOffFace.setChecked(false);
        }

        Log.e(TAG, "Permission not granted: results len = " + grantResults.length +
                " Result code = " + (grantResults.length > 0 ? grantResults[0] : "(empty)"));
    }

    /*@SuppressLint("MissingPermission")
    public void checkFingerPrintHardware() {
        // Check if we're running on Android 6.0 (M) or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Fingerprint API only available on from Android 6.0 (M)
            FingerprintManager fingerprintManager = (FingerprintManager) SettingsFaceFingerscreen.this.getSystemService(Context.FINGERPRINT_SERVICE);
            if (!fingerprintManager.isHardwareDetected()) {
                // Device doesn't support fingerprint authentication
                tvFingerPrintStatus.setText(getString(R.string.settings_fingerprint_no));
                sbOnOffFingerprint.setChecked(false);
                sbOnOffFingerprint.setEnabled(false);
                sbOnOffFingerprint.setVisibility(View.GONE);
            } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                // User hasn't enrolled any fingerprints to authenticate with
                tvFingerPrintStatus.setText(getString(R.string.settings_fingerprint_not_configured));
                sbOnOffFingerprint.setChecked(false);
                sbOnOffFingerprint.setEnabled(false);
                sbOnOffFingerprint.setVisibility(View.VISIBLE);
            } else {
                // Everything is ready for fingerprint authentication
                sbOnOffFingerprint.setEnabled(true);
                sbOnOffFingerprint.setVisibility(View.VISIBLE);
            }
        }
    }*/

    private void initView() {

        tvFingerPrintLabel = (CustomTextView) findViewById(R.id.tv_finger_print_label);
        ivFingerPrint = findViewById(R.id.iv_finger_print);
        tvFingerPrintStatus = (CustomTextView) findViewById(R.id.tv_finger_print_status);
        sbOnOffFingerprint = (SwitchButton) findViewById(R.id.sb_on_off_fingerprint);

        tvFaceLabel = (CustomTextView) findViewById(R.id.tv_face_label);
        ivFacePrint = findViewById(R.id.iv_face_print);
        tvFaceStatus = (CustomTextView) findViewById(R.id.tv_face_status);
        sbOnOffFace = (SwitchButton) findViewById(R.id.sb_on_off_face);

        vLineVertical1 = (View) findViewById(R.id.v_line_vertical_1);
        vLine2 = (View) findViewById(R.id.v_line_2);
        ivEmail = (ImageView) findViewById(R.id.iv_email);
        tvEmailLabel = (CustomTextView) findViewById(R.id.tv_email_label);
        tvEmailLabel1 = (CustomTextView) findViewById(R.id.tv_email_label_1);
        tvEmailStatus = (CustomTextView) findViewById(R.id.tv_email_status);
        sbOnOffEmail = (SwitchButton) findViewById(R.id.sb_on_off_email);

        rlParentViews = (RelativeLayout) findViewById(R.id.rl_parent_settings);
        imgBtnBack = findViewById(R.id.imgbtn_back_profile);
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
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /*private Intent biometricsEnrollIntent() {
     *//* return new Intent(Settings.ACTION_BIOMETRIC_ENROLL).addFlags( {
            putExtra(
                    Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                    BiometricManager.Authenticators.BIOMETRIC_WEAK or BiometricManager.Authenticators.DEVICE_CREDENTIAL
            )
        }*//*

        Intent enrollIntent;
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
        enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.DEVICE_CREDENTIAL);
        //}
        return new Intent(enrollIntent);

        *//*final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
        enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                BIOMETRIC_STRONG | DEVICE_CREDENTIAL);
        startActivityForResult(enrollIntent, REQUEST_CODE);*//*
    }
*/

    /*-*******************************************************************************/

    boolean fingerBoolean = false;

    private void setUpDeviceLockInAPIBelow23Intent() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                Intent enrollIntent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                startActivityForResult(enrollIntent, RC_DEVICE_CREDENTIAL_ENROLL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //return new Intent(Settings.ACTION_SECURITY_SETTINGS);
    }

    private void checkDeviceCanAuthenticateWithBiometrics() {

        BiometricManager biometricManager = BiometricManager.from(SettingsFaceFingerscreen.this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            if (CheckKeyGaurd()) {

                switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG
                        | BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
                    case BiometricManager.BIOMETRIC_SUCCESS:
                        Log.d("MY_APP_TAG", "App can authenticate using biometrics.");
                        fingerBoolean = true;
                        break;
                    case BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED:
                    case BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED:
                        fingerBoolean = true;
                        break;
                    case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                    case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                        Log.e("MY_APP_TAG", "No biometric features available on this device.");
                    case BiometricManager.BIOMETRIC_STATUS_UNKNOWN:
                        Log.e("MY_APP_TAG", "Biometric features are currently unavailable.");
                    case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                        fingerBoolean = false;
                        Log.e("MY_APP_TAG", "No Biometric features are currently unavailable.");
                        break;
                }

            } else {
                switch (biometricManager.canAuthenticate()) {
                    case BiometricManager.BIOMETRIC_SUCCESS:
                        Log.d("MY_APP_TAG", "App can authenticate using biometrics.~~~1~~");
                        fingerBoolean = true;
                        break;
                    case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                    case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                    case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                    case BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED:
                    case BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED:
                    case BiometricManager.BIOMETRIC_STATUS_UNKNOWN:
                        fingerBoolean = false;
                        Log.e("MY_APP_TAG", "No Biometric features are currently unavailable.~~1~~");
                        break;
                }
            }

        } else {
            if (CheckKeyGaurd()) {

                switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG
                        | BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
                    case BiometricManager.BIOMETRIC_SUCCESS:
                        Log.d("MY_APP_TAG", "App can authenticate using biometrics.");
                        fingerBoolean = true;
                        break;
                    case BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED:
                    case BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED:
                        fingerBoolean = true;
                        break;
                    case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                    case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                        Log.e("MY_APP_TAG", "No biometric features available on this device.");
                    case BiometricManager.BIOMETRIC_STATUS_UNKNOWN:
                        Log.e("MY_APP_TAG", "Biometric features are currently unavailable.");
                    case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                        fingerBoolean = false;
                        Log.e("MY_APP_TAG", "No Biometric features are currently unavailable.");
                        break;
                }

            } else {
                switch (biometricManager.canAuthenticate()) {
                    case BiometricManager.BIOMETRIC_SUCCESS:
                        Log.d("MY_APP_TAG", "App can authenticate using biometrics.~~~2~~");
                        fingerBoolean = true;
                        break;
                    case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                    case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                    case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                    case BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED:
                    case BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED:
                    case BiometricManager.BIOMETRIC_STATUS_UNKNOWN:
                        fingerBoolean = false;
                        Log.e("MY_APP_TAG", "No Biometric features are currently unavailable.~~2~~");
                        break;
                }
            }
        }

        init();

    }

    private void init() {

        /*System.out.println("~~~~1~~~>>>" + BiometricUtils.isBiometricPromptEnabled());
        System.out.println("~~~~2~~~>>>" + BiometricUtils.isFingerprintAvailable(this));
        System.out.println("~~~~3~~~>>>" + BiometricUtils.isHardwareSupported(this));
        System.out.println("~~~~4~~~>>>" + BiometricUtils.isPermissionGranted(this));*/

        executor = ContextCompat.getMainExecutor(SettingsFaceFingerscreen.this);
        biometricPrompt = new BiometricPrompt(SettingsFaceFingerscreen.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                /*Toast.makeText(SettingsFaceFingerscreen.this,
                        "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();*/
                Toast.makeText(SettingsFaceFingerscreen.this,
                        "Authentication error: " + getErrorMessage(errorCode), Toast.LENGTH_SHORT)
                        .show();

                setUpdateFingerPrintStatus("0");

            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                // Log.e(TAG, "onAuthenticationSucceeded: ~~~"+result.getAuthenticationType());
                // Log.e(TAG, "onAuthenticationSucceeded: ~~~"+result.getCryptoObject());

                Toast.makeText(SettingsFaceFingerscreen.this,
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();

                setUpdateFingerPrintStatus("1");
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(SettingsFaceFingerscreen.this, "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show();

                setUpdateFingerPrintStatus("0");
            }
        });

       /* promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(getResources().getString(R.string.app_name))
                .setSubtitle(getResources().getString(R.string.biometric_crediental))

                //setDeviceCredentialAllowed(true)
                // Can't call setNegativeButtonText() and  // setAllowedAuthenticators(... or DEVICE_CREDENTIAL) at the same time.//
                //.setNegativeButtonText("Use account password") //

                .setConfirmationRequired(true)
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG
                        | BiometricManager.Authenticators.BIOMETRIC_WEAK
                        | BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                .build();*/


        /*-****************If Split this content message for crediental com.bharathvishal.androidbiometricauthentication.Activities*/

        try {

            String titleKey = getResources().getString(R.string.PASSWORD_PIN_AUTHENTICATION);
            String subtitleKey = getResources().getString(R.string.PASSWORD_PIN_AUTHENTICATION_SUBTITLE);
            String descriptionKey = getResources().getString(R.string.PASSWORD_PIN_AUTHENTICATION_DESCRIPTION);

            String title = getResources().getString(R.string.BIOMETRIC_AUTHENTICATION);
            String subtitle = getResources().getString(R.string.BIOMETRIC_AUTHENTICATION_SUBTITLE);
            String description = getResources().getString(R.string.BIOMETRIC_AUTHENTICATION_DESCRIPTION);
            String Cancel = getResources().getString(R.string.BIOMETRIC_AUTHENTICATION_CANCEL);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                int authFlag = BiometricManager.Authenticators.BIOMETRIC_STRONG
                        | BiometricManager.Authenticators.BIOMETRIC_WEAK
                        | BiometricManager.Authenticators.DEVICE_CREDENTIAL;

                promptInfoKey = new BiometricPrompt.PromptInfo.Builder()
                        .setTitle(titleKey)
                        .setSubtitle(subtitleKey)
                        .setDescription(descriptionKey)
                        .setAllowedAuthenticators(authFlag)
                        .build();
            } else {
                promptInfoKey = new BiometricPrompt.PromptInfo.Builder()
                        .setTitle(titleKey)
                        .setSubtitle(subtitleKey)
                        .setDescription(descriptionKey)
                        .setDeviceCredentialAllowed(true)
                        .build();
            }

            promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle(title)
                    .setSubtitle(subtitle)
                    .setDescription(description)
                    .setNegativeButtonText(Cancel)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(SettingsFaceFingerscreen.this, "FP Ex Try again -- " + e, Toast.LENGTH_SHORT).show();
        }

        /*
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(getResources().getString(R.string.app_name))
                .setSubtitle(getResources().getString(R.string.biometric_verify))

                //setDeviceCredentialAllowed(true)
                // Can't call setNegativeButtonText() and  // setAllowedAuthenticators(... or DEVICE_CREDENTIAL) at the same time.//
                //.setNegativeButtonText("Use account password") //

                .setConfirmationRequired(true)
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG
                        | BiometricManager.Authenticators.BIOMETRIC_WEAK
                        | BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                .build();
                */


    }

    private void checkAPILevelAndProceed() {
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            setUpDeviceLockInAPIBelow23Intent();
            //startActivityForResult(setUpDeviceLockInAPIBelow23Intent(), RC_DEVICE_CREDENTIAL_ENROLL);
        } else {
            callBiometricEntrollIntent();
            // startActivityForResult(biometricsEnrollIntent(), RC_BIOMETRICS_ENROLL);
        }
    }

    private void callBiometricEntrollIntent() {
        try {
            //System.out.println("Inside 1~~~bio ~intent~~~");
            final Intent enrollIntent;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.DEVICE_CREDENTIAL);
                startActivityForResult(enrollIntent, RC_BIOMETRICS_ENROLL);
            }
        } catch (ActivityNotFoundException ex) {
            ex.printStackTrace();
            //System.out.println("Inside 2~~~bio ~intent~~~" + ex);

            // https://stackoverflow.com/questions/39526631/intent-to-launch-fingerprint-enrollment-screen
            startActivity(new Intent(Settings.ACTION_SECURITY_SETTINGS));

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Inside 2.1~~~bio ~intent~~~" + e);
        }
    }

    private String getErrorMessage(int errorCode) {
        String label;
        switch (errorCode) {
            case BiometricPrompt.ERROR_NEGATIVE_BUTTON:
                label = SettingsFaceFingerscreen.this.getResources().getString(R.string.message_user_app_authentication);
                break;
            case BiometricPrompt.ERROR_HW_UNAVAILABLE:
                label = SettingsFaceFingerscreen.this.getResources().getString(R.string.error_hw_unavailable);
                break;
            case BiometricPrompt.ERROR_UNABLE_TO_PROCESS:
                label = SettingsFaceFingerscreen.this.getResources().getString(R.string.error_unable_to_process);
                break;
            case BiometricPrompt.ERROR_TIMEOUT:
                label = SettingsFaceFingerscreen.this.getResources().getString(R.string.error_time_out);
                break;
            case BiometricPrompt.ERROR_NO_SPACE:
                label = SettingsFaceFingerscreen.this.getResources().getString(R.string.error_no_space);
                break;
            case BiometricPrompt.ERROR_CANCELED:
                label = SettingsFaceFingerscreen.this.getResources().getString(R.string.error_canceled);
                break;
            case BiometricPrompt.ERROR_LOCKOUT:
                label = SettingsFaceFingerscreen.this.getResources().getString(R.string.error_lockout);
                break;
            case BiometricPrompt.ERROR_VENDOR:
                label = SettingsFaceFingerscreen.this.getResources().getString(R.string.error_vendor);
                break;
            case BiometricPrompt.ERROR_LOCKOUT_PERMANENT:
                label = SettingsFaceFingerscreen.this.getResources().getString(R.string.error_lockout_permanent);
                break;
            case BiometricPrompt.ERROR_USER_CANCELED:
                label = SettingsFaceFingerscreen.this.getResources().getString(R.string.error_user_canceled);
                break;
            case BiometricPrompt.ERROR_NO_BIOMETRICS:
                checkAPILevelAndProceed();
                label = SettingsFaceFingerscreen.this.getResources().getString(R.string.error_no_biometrics);
                break;
            case BiometricPrompt.ERROR_HW_NOT_PRESENT:
                label = SettingsFaceFingerscreen.this.getResources().getString(R.string.error_hw_not_present);
                break;
            case BiometricPrompt.ERROR_NO_DEVICE_CREDENTIAL:

                callBiometricEntrollIntent();
                label = SettingsFaceFingerscreen.this.getResources().getString(R.string.error_no_device_credentials);
                break;
            case BiometricPrompt.ERROR_SECURITY_UPDATE_REQUIRED:
                label = SettingsFaceFingerscreen.this.getResources().getString(R.string.error_security_update_required);
                break;
            default:
                label = SettingsFaceFingerscreen.this.getResources().getString(R.string.error_unknown);
                break;
        }

        return label;
    }


    private Boolean CheckKeyGaurd() {
        keyguardManager = (KeyguardManager) SettingsFaceFingerscreen.this.getSystemService(KEYGUARD_SERVICE);
        return keyguardManager.isKeyguardSecure();
    }
    /*-*******************************************************************************/
}
