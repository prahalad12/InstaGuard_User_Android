package com.instaguard.user.Helpers;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.instaguard.user.R;

import java.util.ArrayList;
import java.util.List;


public class PermissionUtils {
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    public static String checkPermission_Notification(final Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.POST_NOTIFICATIONS)) {
                    return "0";
                } else {
                    return "2";
                }
            } else {
                return "1";
            }
        } else {
            return "1";
        }
    }

    public static String checkPermission_ReadWrite_Media_Storage(final Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_MEDIA_IMAGES)) {
                    return "0";
                } else {
                    return "2";
                }
            } else {
                return "1";
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        return "0";
                    } else {
                        return "2";
                    }
                } else {
                    return "1";
                }
            } else {
                return "1";
            }
        }

    }


    public static String[] storage_permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] storage_permissions_33 = {
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO
    };

    public static String[] permissions() {
        String[] p;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = storage_permissions_33;
        } else {
            p = storage_permissions;
        }
        return p;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean neverAskAgainSelected(final Activity activity, final String permission) {
        final boolean prevShouldShowStatus = getRatinaleDisplayStatus(activity, permission);
        final boolean currShouldShowStatus = activity.shouldShowRequestPermissionRationale(permission);
        return prevShouldShowStatus != currShouldShowStatus;
    }

    public static void setShouldShowStatus(final Context context, final String permission) {
        SharedPreferences genPrefs = context.getSharedPreferences("GENERIC_PREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = genPrefs.edit();
        editor.putBoolean(permission, true);
        editor.apply();
    }

    public static boolean getRatinaleDisplayStatus(final Context context, final String permission) {
        SharedPreferences genPrefs = context.getSharedPreferences("GENERIC_PREFERENCES", Context.MODE_PRIVATE);
        return genPrefs.getBoolean(permission, false);
    }

    public static boolean checkPermission_old(final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    androidx.appcompat.app.AlertDialog.Builder alertBuilder = new androidx.appcompat.app.AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle(R.string.permission_necessary);
                    alertBuilder.setMessage(R.string.external_storage_permission_necessary);
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }



    private boolean checkAndRequestPermissions(Context context) {


        int permissionReadExternalStorage;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            permissionReadExternalStorage = ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_MEDIA_IMAGES);
        else
            permissionReadExternalStorage = ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE);


        int permissionWriteExtarnalStorage;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            permissionWriteExtarnalStorage = ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_MEDIA_AUDIO);
        else
            permissionWriteExtarnalStorage = ContextCompat.checkSelfPermission(context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);


        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionWriteExtarnalStorage != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                listPermissionsNeeded.add(Manifest.permission.READ_MEDIA_AUDIO);
            else
                listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        }

        if (permissionReadExternalStorage != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                listPermissionsNeeded.add(Manifest.permission.READ_MEDIA_IMAGES);
            else
                listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            int permissionVideoStorage = ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_MEDIA_VIDEO);
            if (permissionVideoStorage != PackageManager.PERMISSION_GRANTED) {

                listPermissionsNeeded.add(Manifest.permission.READ_MEDIA_VIDEO);

            }

            int notificationPermission = ContextCompat.checkSelfPermission(context,
                    Manifest.permission.POST_NOTIFICATIONS);

            if (notificationPermission != PackageManager.PERMISSION_GRANTED) {

                listPermissionsNeeded.add(Manifest.permission.POST_NOTIFICATIONS);

            }

        }


        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) context, listPermissionsNeeded.toArray(new String[0]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


/*
    @SuppressWarnings("ConstantConditions")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    perms.put(Manifest.permission.READ_MEDIA_IMAGES, PackageManager.PERMISSION_GRANTED);
                    perms.put(Manifest.permission.READ_MEDIA_AUDIO, PackageManager.PERMISSION_GRANTED);
                    perms.put(Manifest.permission.READ_MEDIA_VIDEO, PackageManager.PERMISSION_GRANTED);
                    perms.put(Manifest.permission.POST_NOTIFICATIONS, PackageManager.PERMISSION_GRANTED);


                }else  {
                    perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                    perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                }



                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {


                        if (    perms.get(Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
                                && perms.get(Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED
                                && perms.get(Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED
                                && perms.get(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED

                        ) {
                            Toast.makeText(this, "For Granting Permission.", Toast.LENGTH_LONG).show();
                            //else any one or both the permissions are not granted
                        } else {
                            if (        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_MEDIA_IMAGES)
                                    || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_MEDIA_AUDIO)
                                    || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_MEDIA_VIDEO)
                                    || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.POST_NOTIFICATIONS)



                            ) {
                                showDialogOK("Necessary Permissions required for this app",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                switch (which) {
                                                    case DialogInterface.BUTTON_POSITIVE:
                                                        checkAndRequestPermissions();
                                                        break;
                                                    case DialogInterface.BUTTON_NEGATIVE:
                                                        // proceed with logic by disabling the related features or quit the app.
                                                        Toast.makeText(_StartActivity_First.this, "Necessary Permissions required for this app", Toast.LENGTH_LONG).show();
                                                        // permissionSettingScreen ( );
                                                        //  finish();
                                                        break;
                                                }
                                            }
                                        });
                            }
                            //permission is denied (and never ask again is  checked)
                            //shouldShowRequestPermissionRationale will return false
                            else {
                                permissionSettingScreen();

                            }
                        }


                    }else {


                        if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                                && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

                        ) {
                            Toast.makeText(this, "For Granting Permission.", Toast.LENGTH_LONG).show();
                            //else any one or both the permissions are not granted
                        } else {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            ) {
                                showDialogOK("Necessary Permissions required for this app",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                switch (which) {
                                                    case DialogInterface.BUTTON_POSITIVE:
                                                        checkAndRequestPermissions();
                                                        break;
                                                    case DialogInterface.BUTTON_NEGATIVE:
                                                        // proceed with logic by disabling the related features or quit the app.
                                                        Toast.makeText(_StartActivity_First.this, "Necessary Permissions required for this app", Toast.LENGTH_LONG).show();
                                                        // permissionSettingScreen ( );
                                                        //  finish();
                                                        break;
                                                }
                                            }
                                        });
                            }
                            //permission is denied (and never ask again is  checked)
                            //shouldShowRequestPermissionRationale will return false
                            else {
                                permissionSettingScreen();

                            }
                        }
                    }
                }
            }
        }

    }
*/

   /* private void permissionSettingScreen() {
        Toast.makeText(this, "Enable All permissions, Click On Permission", Toast.LENGTH_LONG)
                .show();

        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
        // finishAffinity();
        //finish();

    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }*/
}
