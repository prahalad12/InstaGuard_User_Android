package com.instaguard.user.Helpers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;


/**
 * Created by Farman on 7/20/2017.
 */

public class Utility {
    public ProgressDialog progressDialog;

    public static void showToastMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void showProgressDialog(Context context, String msg) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(msg);
        //progressDialog.show();
    }

    public void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.hide();
        }
    }

    public void generateHashkey(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    "com.instaguard.user",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("tag", "hash_" + Base64.encodeToString(md.digest(), Base64.NO_WRAP));
                /*Logger.printDebug("HashKey_ "+Base64.encodeToString(md.digest(),
                        Base64.NO_WRAP));*/
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("Name not found", e.getMessage(), e);

        } catch (NoSuchAlgorithmException e) {
            Log.d("Error", e.getMessage(), e);
        }
    }

    public ArrayList<String> getExpertAreaList(String expertArea) {
        ArrayList<String> items = new ArrayList<String>(Arrays.asList(expertArea.split("\\s*,\\s*")));
        return items;
    }

    public String getExpertAreaString(ArrayList<String> selectedExpertAreaList) {
        String expertAreaList = "";
        for (int i = 0; i < selectedExpertAreaList.size(); i++) {
            if (i == 0) {
                expertAreaList = selectedExpertAreaList.get(i).toString();
            } else {
                expertAreaList = expertAreaList + "," + selectedExpertAreaList.get(i).toString();
            }
        }
        return expertAreaList;
    }

    public int get_count_of_days(String Created_date_String) {
        Logger.printDebug("PostedDate" + Created_date_String);
        String Expire_date_String = GetToday();
        Logger.printDebug("PostedDate" + Expire_date_String);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date Created_convertedDate = null, Expire_CovertedDate = null, todayWithZeroTime = null;
        try {
            Created_convertedDate = dateFormat.parse(Created_date_String);
            Expire_CovertedDate = dateFormat.parse(Expire_date_String);

            Date today = new Date();

            todayWithZeroTime = dateFormat.parse(dateFormat.format(today));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long timeDiff = Expire_CovertedDate.getTime() - Created_convertedDate.getTime();
        return (int) TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);


    }

    public String GetToday() {
        Date presentTime_Date = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(presentTime_Date);
    }

    public long getImageSize(Uri imagePath, Context context) {

        File file = new File(getRealPathFromURI(context, imagePath));
        long length = file.length();
        length = length / 1024;
        return length;

    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            assert cursor != null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public String getDeviceUniqueID(Context activity) {
        @SuppressLint("HardwareIds")
        String device_unique_id = Settings.Secure.getString(activity.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return device_unique_id;
    }

    @SuppressLint({"HardwareIds", "MissingPermission"})
    public String getIMEI(Activity activity) {
        TelephonyManager telephonyManager = (TelephonyManager) activity
                .getSystemService(Context.TELEPHONY_SERVICE);
        assert telephonyManager != null;
        return telephonyManager.getDeviceId();
    }

 /*   static public Drawable textImage(String string) {
        if (string == null) string = "L";
        else if (string.length() > 1) string = string.substring(0, 1);
        else string = "L";

      *//*  ColorGenerator generator = ColorGenerator.MATERIAL;
        int color1 = generator.getRandomColor();
        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .width(60)  // width in px
                .height(60)
                .textColor(Color.WHITE)
                .useFont(fontUtility.getRobotoRegular())
                .fontSize(30) *//**//* size in px *//**//*
                .bold()
                .toUpperCase()
                .endConfig()
                .buildRect(string, color1);*//*
        return drawable;
    }*/

    public static String Base64Encode(String strings) {
        // Sending side
        try {
            byte[] data = new byte[0];
            data = strings.getBytes("UTF-8");
            strings = Base64.encodeToString(data, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strings;
    }

    public static String Base64Decode(String strings) {
        try {
            byte[] bytes = Base64.decode(strings, Base64.DEFAULT);
            return new String(bytes, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
