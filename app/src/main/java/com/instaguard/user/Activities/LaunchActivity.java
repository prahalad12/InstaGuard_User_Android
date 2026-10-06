package com.instaguard.user.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.instaguard.user.Helpers.NetworkManager.Tovuti;
import com.instaguard.user.Helpers.Utils;
import com.instaguard.user.R;

import java.util.concurrent.Executor;

@SuppressLint("CustomSplashScreen")
public class LaunchActivity extends AppCompatActivity {

    SharedPreferences getStatePref, spCurrency;
    SharedPreferences.Editor putState, putSpCurrencye;
    String UserId;

    SharedPreferences getStatePrefNew;
    String fingerUserId, fingerStatus, fingerNoValue;
    //String faceUserId, faceStatus, faceNoValue;
    //View ivFacePrint;
    View ivFingerPrint;

    Activity activity;


    private Tovuti tovuti;
    RelativeLayout rlLowInternet;
    String TAG = "LaunchActivity";

    private FirebaseAuth mAuth;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        activity = LaunchActivity.this;

        putState = getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE).edit();
        getStatePref = getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE);
        spCurrency = getSharedPreferences(Utils.PREFS_NAME_CURRENCY, MODE_PRIVATE);

        putSpCurrencye = getSharedPreferences(Utils.PREFS_NAME_CURRENCY, MODE_PRIVATE).edit();

        UserId = getStatePref.getString(Utils.SP_USERID, null);

        String strCurrencySymbol = spCurrency.getString(Utils.SP_CURRENCY, null);
        if (strCurrencySymbol == null) {
            putSpCurrencye.putString(Utils.SP_CURRENCY, "USD");
            putSpCurrencye.putString(Utils.SP_CURRENCY_SYMBOL, "$");
            putSpCurrencye.apply();
        }

        getStatePrefNew = getSharedPreferences(Utils.MY_PREFS_NAME_NEW, MODE_PRIVATE);

        fingerUserId = getStatePrefNew.getString(Utils.spFingerUserId, null);
        fingerStatus = getStatePrefNew.getString(Utils.spFingerStatus, null);
        fingerNoValue = getStatePrefNew.getString(Utils.spFingerNoStatus, null);

        //faceUserId = getStatePrefNew.getString(Utils.spFaceUserId, null);
        //faceStatus = getStatePrefNew.getString(Utils.spFaceStatus, null);
        //faceNoValue = getStatePrefNew.getString(Utils.spFaceNoStatus, null);


        ivFingerPrint = findViewById(R.id._rl_face_finger_lyt);
        //ivFacePrint = findViewById(R.id.iv_face_print);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    // Get new FCM registration token
                    String token = task.getResult();
                    System.out.println("refreshedToken =token=>" + token);

                    SharedPreferences.Editor getState = getApplicationContext().getSharedPreferences(Utils.GLOBAL_STORED_ID, MODE_PRIVATE).edit();
                    getState.putString(Utils.SP_DEVICE_ID, token);
                    getState.apply();
                });


        mAuth = FirebaseAuth.getInstance();

        // callApp();

        checkDeviceCanAuthenticateWithBiometrics();

        enableFingerPrint("home");

        ivFingerPrint.setOnClickListener(v -> enableFingerPrint("finger"));

        //ivFacePrint.setOnClickListener(v -> enableFingerPrint("face"));

    }

    private void callApp(int SPLASH_DISPLAY_LENGTH, String key) {

        new Handler().postDelayed(() -> {
            if (UserId != null) {
                /*String strCurrentBookingId = getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE)
                        .getString(Utils.SP_USER_CURRENT_BOOKING_ID, "229");
                System.out.println("~~~~User~~Splash~~~." + UserId);
                System.out.println("~~~~Booking~~Splash~~~." + strCurrentBookingId);
                if (strCurrentBookingId != null) {
                    Intent intent = new Intent(LaunchActivity.this, MainJobProgressActivity.class);
                    intent.putExtra("from", "main");
                    intent.putExtra("customer_location", "");
                    intent.putExtra("booking_id", strCurrentBookingId);
                    overridePendingTransition(R.anim.slide_left, R.anim.slide_out_left);
                    startActivity(intent);
                    finish();
                } else {*/
                DatabaseReference selectcarreference = FirebaseDatabase.getInstance().getReference().child("users");
                selectcarreference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint({"LongLogTag", "UnsafeIntentLaunch"})
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(UserId).getValue() != null) {
                            Log.e("if USERId~~Splash~~~~~~~~.", UserId);
                            //do something

                            try {
                                String values = "1";
                                if (getIntent().getExtras() != null) {
                                    for (String key : getIntent().getExtras().keySet()) {
                                        Object value = getIntent().getExtras().get(key);
                                        Log.d(TAG, "Key: " + key + " Value: " + value);

                                        assert value != null;
                                        if (value.toString().equals("Offers")) {
                                            Log.e(TAG, "onCreate: Inside offers~~~");
                                            values = "2";
                                        } else if (value.toString().equals("Announcements")) {
                                            Log.e(TAG, "onCreate: Inside Announcements~~~");
                                            values = "3";
                                        } else if (value.toString().equals("Reminder")) {
                                            Log.e(TAG, "onCreate: Inside Reminder~~~");
                                            values = "4";
                                        }
                                    }
                                    overridePendingTransition(R.anim.slide_left, R.anim.slide_out_left);
                                    startActivity(getIntent(values));
                                    finish();
                                } else {
                                    LaunchActivity.this.startActivity(new Intent(LaunchActivity.this.getApplicationContext(), MainActivity.class));
                                    LaunchActivity.this.overridePendingTransition(R.anim.slide_left, R.anim.slide_out_left);
                                    LaunchActivity.this.finish();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e(TAG, "onDataChange: ~~~~~~~~~" + e.toString());
                            }
                        } else {
                            Log.e("else~~", "USERId~~~~~~~~~~.");
                            LaunchActivity.this.startActivity(new Intent(LaunchActivity.this.getApplicationContext(), HomeActivity.class));
                            LaunchActivity.this.overridePendingTransition(R.anim.slide_left, R.anim.slide_out_left);
                            LaunchActivity.this.finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.i(TAG, "onCancelled: -->" + databaseError);
                    }
                });
                /* }*/
            } else {
                LaunchActivity.this.startActivity(new Intent(LaunchActivity.this.getApplicationContext(), HomeActivity.class));
                LaunchActivity.this.overridePendingTransition(R.anim.slide_left, R.anim.slide_out_left);
                LaunchActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

    }

    @NonNull
    private Intent getIntent(String values) {
        Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
        switch (values) {
            case "2":
                intent.putExtra("Notify", "Offers");
                break;
            case "3":
                intent.putExtra("Notify", "Announcements");
                break;
            case "4":
                intent.putExtra("Notify", "Reminder");
                break;
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utils.CURRENT_TAG = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth != null) {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                System.out.println("-----not null----");
            } else {
                mAuth.signInAnonymously()
                        .addOnCompleteListener(LaunchActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInAnonymously:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInAnonymously:failure", task.getException());
                                    // Toast.makeText(LaunchActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }
    }


    public void enableFingerPrint(String key) {
        switch (key) {
            case "home":

                if (fingerNoValue == null) {
                    if (fingerUserId != null) {
                        if (fingerStatus != null) {
                            if (fingerStatus.equals("1")) {
                                ivFingerPrint.setVisibility(View.VISIBLE);

                                /*if (faceStatus != null) {
                                    if (faceStatus.equals("1")) {
                                        ivFacePrint.setVisibility(View.VISIBLE);
                                    } else {
                                        ivFacePrint.setVisibility(View.GONE);
                                    }
                                } else {
                                    ivFacePrint.setVisibility(View.GONE);
                                }*/

                            } else {
                                ivFingerPrint.setVisibility(View.GONE);
                                /*if (faceStatus != null) {
                                    if (faceStatus.equals("1")) {
                                        ivFacePrint.setVisibility(View.VISIBLE);
                                    } else {
                                        ivFacePrint.setVisibility(View.GONE);
                                        callApp(1500, "4");
                                    }
                                } else {
                                    ivFacePrint.setVisibility(View.GONE);
                                    callApp(1500, "5");
                                }*/

                                callApp(1500, "4");
                            }
                        } else {
                            ivFingerPrint.setVisibility(View.GONE);
                            /*if (faceStatus != null) {
                                if (faceStatus.equals("1")) {
                                    ivFacePrint.setVisibility(View.VISIBLE);
                                } else {
                                    ivFacePrint.setVisibility(View.GONE);
                                    callApp(1500, "4");
                                }
                            } else {
                                ivFacePrint.setVisibility(View.GONE);
                                callApp(1500, "5");
                            }*/

                            callApp(1500, "4");
                        }
                    } else {
                        ivFingerPrint.setVisibility(View.GONE);
                       /* if (faceStatus != null) {
                            if (faceStatus.equals("1")) {
                                ivFacePrint.setVisibility(View.VISIBLE);
                            } else {
                                ivFacePrint.setVisibility(View.GONE);
                                callApp(1500, "4");
                            }
                        } else {
                            ivFacePrint.setVisibility(View.GONE);
                            callApp(1500, "5");
                        }*/

                        callApp(1500, "4");
                    }
                }


                break;

            case "face":
                /*if (faceStatus != null) {
                    if (faceStatus.equals("1")) {
                        Toast.makeText(activity, " Face Login", Toast.LENGTH_SHORT).show();
                        faceRecognitionState();
                    } else {
                        Toast.makeText(activity, "No Face Login", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(activity, "No Face Login", Toast.LENGTH_SHORT).show();
                }*/
                break;


            case "finger":
                if (fingerNoValue == null) {
                    if (fingerUserId != null) {
                        if (fingerStatus != null) {
                            if (fingerStatus.equals("1")) {
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
                                } else {
                                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                                        if (CheckKeyGaurd()) {
                                            biometricPrompt.authenticate(promptInfoKey);
                                        } else {
                                            // setUpDeviceLockInAPIBelow23Intent();
                                            System.out.println("Else~~~~1");
                                            callApp(1500, "6");
                                        }
                                    } else {
                                        if (CheckKeyGaurd()) {
                                            biometricPrompt.authenticate(promptInfoKey);
                                        } else {
                                            // setUpDeviceLockInAPIBelow23Intent();
                                            System.out.println("Else~~~2");
                                            callApp(1500, "7");
                                        }
                                    }
                                }
                            } else {
                                Toast.makeText(activity, "No Finger Login", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(activity, "No Finger Login", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(activity, "No Finger Login", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            default:
                Toast.makeText(activity, "No Login", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    /*-*******************************************************************************/

    boolean fingerBoolean = false;
    KeyguardManager keyguardManager;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private BiometricPrompt.PromptInfo promptInfoKey;
    private BiometricManager biometricManager;
    int RC_BIOMETRICS_ENROLL = 10;
    int RC_DEVICE_CREDENTIAL_ENROLL = 18;

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

        biometricManager = BiometricManager.from(activity);

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

        executor = ContextCompat.getMainExecutor(activity);
        biometricPrompt = new BiometricPrompt(LaunchActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                /*Toast.makeText(activity,
                        "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();*/
                Toast.makeText(activity,
                                "Authentication error: " + getErrorMessage(errorCode, errString), Toast.LENGTH_SHORT)
                        .show();
                //setUpdateFingerPrintStatus("0");

            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                Toast.makeText(activity,
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();

                //setUpdateFingerPrintStatus("1");
                ivFingerPrint.setVisibility(View.GONE);

                callApp(100, "8");
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(activity, "Authentication failed",
                                Toast.LENGTH_SHORT)
                        .show();
                // setUpdateFingerPrintStatus("0");
            }
        });

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
                        .setConfirmationRequired(true)
                        .setAllowedAuthenticators(authFlag)
                        .build();
            } else {
                promptInfoKey = new BiometricPrompt.PromptInfo.Builder()
                        .setTitle(titleKey)
                        .setSubtitle(subtitleKey)
                        .setDescription(descriptionKey)
                        .setConfirmationRequired(true)
                        .setDeviceCredentialAllowed(true)
                        .build();
            }

            promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle(title)
                    .setSubtitle(subtitle)
                    .setDescription(description)
                    .setConfirmationRequired(true)
                    .setNegativeButtonText(Cancel)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity, "FP Ex Try again -- " + e, Toast.LENGTH_SHORT).show();
        }
    }


    private String getErrorMessage(int errorCode, CharSequence errString) {
        String label;
        switch (errorCode) {
            case BiometricPrompt.ERROR_NEGATIVE_BUTTON:
                label = activity.getResources().getString(R.string.message_user_app_authentication);
                break;
            case BiometricPrompt.ERROR_HW_UNAVAILABLE:
                label = activity.getResources().getString(R.string.error_hw_unavailable);
                break;
            case BiometricPrompt.ERROR_UNABLE_TO_PROCESS:
                label = activity.getResources().getString(R.string.error_unable_to_process);
                break;
            case BiometricPrompt.ERROR_TIMEOUT:
                label = activity.getResources().getString(R.string.error_time_out);
                break;
            case BiometricPrompt.ERROR_NO_SPACE:
                label = activity.getResources().getString(R.string.error_no_space);
                break;
            case BiometricPrompt.ERROR_CANCELED:
                label = activity.getResources().getString(R.string.error_canceled);
                break;
            case BiometricPrompt.ERROR_LOCKOUT:
                label = activity.getResources().getString(R.string.error_lockout);
                break;
            case BiometricPrompt.ERROR_VENDOR:
                label = activity.getResources().getString(R.string.error_vendor);
                break;
            case BiometricPrompt.ERROR_LOCKOUT_PERMANENT:
                label = activity.getResources().getString(R.string.error_lockout_permanent);
                break;
            case BiometricPrompt.ERROR_USER_CANCELED:
                label = activity.getResources().getString(R.string.error_user_canceled);
                break;
            case BiometricPrompt.ERROR_NO_BIOMETRICS:
                //checkAPILevelAndProceed();
                label = activity.getResources().getString(R.string.error_no_biometrics);
                break;
            case BiometricPrompt.ERROR_HW_NOT_PRESENT:
                label = activity.getResources().getString(R.string.error_hw_not_present);
                break;
            case BiometricPrompt.ERROR_NO_DEVICE_CREDENTIAL:
                //callBiometricEntrollIntent();
                label = activity.getResources().getString(R.string.error_no_device_credentials);
                break;
            case BiometricPrompt.ERROR_SECURITY_UPDATE_REQUIRED:
                label = activity.getResources().getString(R.string.error_security_update_required);
                break;
            default:
                label = errString.toString();
                break;
        }

        return label;
    }

    private Boolean CheckKeyGaurd() {
        keyguardManager = (KeyguardManager) activity.getSystemService(KEYGUARD_SERVICE);
        return keyguardManager.isKeyguardSecure();
    }

    /*-*******************************************************************************/

}
