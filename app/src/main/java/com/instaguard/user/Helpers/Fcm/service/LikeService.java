package com.instaguard.user.Helpers.Fcm.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import android.util.Log;

public class LikeService extends Service {

    public static final String NOTIFICATION_ID_EXTRA = "notificationId";
    public  static final String IMAGE_URL_EXTRA = "imageUrl";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        //Saving action implementation

        Log.e(NOTIFICATION_ID_EXTRA, "onBind: ~~~~~~~~~~~~"+intent.getBundleExtra(IMAGE_URL_EXTRA));

        return null;
    }
}