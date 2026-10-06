package com.instaguard.user.Helpers.Fcm.service;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.instaguard.user.Activities.LaunchActivity;
import com.instaguard.user.Activities.MainJobProgressActivity;
import com.instaguard.user.BuildConfig;
import com.instaguard.user.Helpers.Fcm.Util.NotificationUtils;
import com.instaguard.user.Helpers.SoundPoolManager;
import com.instaguard.user.Helpers.Utils;
import com.instaguard.user.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.twilio.voice.CallInvite;
import com.twilio.voice.CancelledCallInvite;
import com.twilio.voice.MessageListener;
import com.twilio.voice.Voice;

import java.util.Map;
import java.util.Objects;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private static final int THUMBNAIL_SIZE = 450;

    private NotificationUtils notificationUtils;
    private static int count = 0;
    String strDataLis, strImage, strTitle, strMessage;

    private static final String NOTIFICATION_ID_KEY = "NOTIFICATION_ID";
    private static final String CALL_SID_KEY = "CALL_SID";
    private static final String VOICE_CHANNEL = "default";
    String strTwilios;
    private NotificationManager notificationManager;
    int strstatus;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        storeRegIdInPref(s);
        Log.e(TAG, "onNewToken: " + s);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private void storeRegIdInPref(String token) {
        SharedPreferences.Editor getState = getApplicationContext().getSharedPreferences(Utils.GLOBAL_STORED_ID, MODE_PRIVATE).edit();
        getState.putString(Utils.SP_DEVICE_ID, token);
        getState.apply();
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        if (BuildConfig.DEBUG)
            Log.e(TAG, "onMessageReceived: ~~~~~~~~~~~~~~~~" + remoteMessage.getData().toString());
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getData() != null)
            getImage(remoteMessage);

    }


    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            sendNotification(bitmap, "image");
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            Log.e(TAG, "onBitmapFailed: ~~~~~~~~~~" + e.toString());
        }


        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    private void sendNotification(Bitmap bitmap, String key) {

        if (bitmap == null) {

            Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            Intent intent = new Intent(getApplicationContext(), LaunchActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            PendingIntent pendingIntent;
            try {
                pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                                | PendingIntent.FLAG_IMMUTABLE);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                        intent, PendingIntent.FLAG_UPDATE_CURRENT);
            }

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            String NOTIFICATION_CHANNEL_ID = "101";

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                @SuppressLint("WrongConstant")
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_MAX);

                //Configure Notification Channel
                notificationChannel.setDescription("Notifications");
                notificationChannel.enableLights(true);
                notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                notificationChannel.enableVibration(true);

                notificationManager.createNotificationChannel(notificationChannel);
            }

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle(strTitle)
                    .setAutoCancel(true)
                    .setSound(defaultSound)
                    .setContentText(strMessage)
                    .setContentIntent(pendingIntent)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setWhen(System.currentTimeMillis())
                    .setPriority(Notification.PRIORITY_MAX);

            notificationManager.notify(1, notificationBuilder.build());


        } else {

            NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
            style.bigPicture(bitmap);


            Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            Intent intent = new Intent(getApplicationContext(), LaunchActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            PendingIntent pendingIntent;
            try {
                pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                                | PendingIntent.FLAG_IMMUTABLE);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                        intent, PendingIntent.FLAG_UPDATE_CURRENT);
            }

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            String NOTIFICATION_CHANNEL_ID = "101";

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                @SuppressLint("WrongConstant")
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_MAX);

                //Configure Notification Channel
                notificationChannel.setDescription("Notifications");
                notificationChannel.enableLights(true);
                notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                notificationChannel.enableVibration(true);

                notificationManager.createNotificationChannel(notificationChannel);
            }

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle(strTitle)
                    .setAutoCancel(true)
                    .setSound(defaultSound)
                    .setContentText(strMessage)
                    .setContentIntent(pendingIntent)

                    .setStyle(style)
                    .setLargeIcon(bitmap)

                    .setWhen(System.currentTimeMillis())
                    .setPriority(Notification.PRIORITY_MAX);


            notificationManager.notify(1, notificationBuilder.build());

        }

    }

    private void getImage(final RemoteMessage remoteMessage) {

        Map<String, String> data = remoteMessage.getData();
        if (BuildConfig.DEBUG) Log.e(TAG, "getImage: ~~~~~~~~~~~~~~~~" + data);

        //String strData = data.get("{a_data=my_data}");
        //String strDatas = data.get("{a_data=pn_city, body=123, image=, title=Test}");
        //{a_data=my_data, status=1}

        strDataLis = data.get("a_data");
        strTwilios = data.get("twi_account_sid");

        System.out.println("str data==>" + strDataLis + "--" + strTwilios);

        if (data.get("status") != null && !String.valueOf(data.get("status")).equals("") && !String.valueOf(data.get("status")).equals("null")) {
            strstatus = Integer.parseInt(Objects.requireNonNull(data.get("status")));
            /*SharedPreferences.Editor getState = getApplicationContext().getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE).edit();
            getState.putInt(Utils.Notification_Unread, strstatus);
            getState.apply();*/
            System.out.println("notification Checking " + strstatus);

            try {
                if (strstatus == 1) {
                    /*getApplicationContext().runOnUiThread((Runnable) () -> {
                        // Stuff that updates the UI
                        if (MapsActivity_annotation.unreadImg != null) {
                            MapsActivity_annotation.unreadImg.setVisibility(View.VISIBLE);
                        }
                    });*/
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        if (strDataLis != null) {

            if (strDataLis.equals("pn_city")) {

                strImage = data.get("image");
                strTitle = data.get("title");
                strMessage = data.get("body");

                if (strImage != null && !strImage.equals("") && !strImage.isEmpty()) {
                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            // Get image from data Notification
                            Picasso.get()
                                    .load(strImage)
                                    .into(target);
                        }
                    });
                } else {
                    //Log.e(TAG, "getImage: ~~~~~~~~~~Else~~~~~~~~~~~");
                    Bitmap bitmap = null;
                    sendNotification(bitmap, "noimage");
                }
            } else {

                strTitle = remoteMessage.getNotification().getTitle();
                strMessage = remoteMessage.getNotification().getBody();

                if (BuildConfig.DEBUG) Log.e(TAG, "getNotification: ~~~~~~~~~~~~~~~~" + strTitle);
                if (BuildConfig.DEBUG) Log.e(TAG, "getNotification: ~~~~~~~~~~~~~~~~" + strMessage);

                Bitmap bitmap = null;
                sendNotification(bitmap, "noimage");

            }
        } else if (strTwilios != null) {

            //~{twi_account_sid=ACxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, twi_to=client:Hyride_user6,
            // twi_message_type=twilio.voice.cancel, twi_call_sid=CAdac461de8185c017b2693ba927397092,
            // twi_message_id=RU3f6b36ad5e2c16d4827f3204232b57ec, twi_from=client:Hyride_driver113}


//            //twi_account_sid=ACxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,
//            twi_to=client:Hyride_user6, twi_answer_timeout=-1,
//
//            twi_message_type=twilio.voice.call,
//            twi_call_sid=CA496dcf2fcbf5e86adbb7f1831692545f,
//            twi_message_id=RUbcc8acf465382e9f54d698b765eabd47,
//            twi_from=client:Hyride_driver113}.
//
            Log.e(TAG, "NavigationDrawerData Payload: " + remoteMessage.getData().toString());

            try {

                Map<String, String> datas = remoteMessage.getData();
                final int notificationId = (int) System.currentTimeMillis();

                boolean valid = Voice.handleMessage(remoteMessage.getData(), new MessageListener() {
                    @Override
                    public void onCallInvite(@NonNull CallInvite callInvite) {

                        System.out.println("~onCallInvite~~getFrom~" + callInvite.getFrom());
                        System.out.println("~onCallInvite~~getTo~" + callInvite.getTo());

                        final int notificationId = (int) System.currentTimeMillis();
                        MyFirebaseMessagingService.this.notify(callInvite, notificationId);
                        MyFirebaseMessagingService.this.sendCallInviteToActivity(callInvite, notificationId);
                    }

                    @Override
                    public void onCancelledCallInvite(@NonNull CancelledCallInvite cancelledCallInvite) {
                        MyFirebaseMessagingService.this.cancelNotification(cancelledCallInvite);
                        MyFirebaseMessagingService.this.sendCancelledCallInviteToActivity(
                                cancelledCallInvite);
                    }
                });
                if (!valid) {
                    Log.e(TAG, "The message was not a valid Twilio Voice SDK payload: " +
                            remoteMessage.getData());
                }

            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        } else {

            strTitle = remoteMessage.getNotification().getTitle();
            strMessage = remoteMessage.getNotification().getBody();

            if (BuildConfig.DEBUG) Log.e(TAG, "getNotification: ~~~~~~~~~~~~~~~~" + strTitle);
            if (BuildConfig.DEBUG) Log.e(TAG, "getNotification: ~~~~~~~~~~~~~~~~" + strMessage);

            Bitmap bitmap = null;
            sendNotification(bitmap, "noimage");
        }

    }

    private static int getPowerOfTwoForSampleRatio(double ratio) {
        int k = Integer.highestOneBit((int) Math.floor(ratio));
        if (k == 0) return 1;
        else return k;
    }

    //For Android Version Orio and greater than orio.
    private void notify(CallInvite callInvite, int notificationId) {

        System.out.println("~onCallInvite~~notify~");

        if (getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE).getString(Utils.SP_USER_CURRENT_BOOKING_ID, null) != null) {

            Intent intent = new Intent(this, MainJobProgressActivity.class);

            intent.putExtra("from", "main");
            intent.putExtra("customer_location", "");
            intent.putExtra("booking_id", getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE)
                    .getString(Utils.SP_USER_CURRENT_BOOKING_ID, null));

            intent.setAction(MainJobProgressActivity.ACTION_INCOMING_CALL);
            intent.putExtra(MainJobProgressActivity.INCOMING_CALL_NOTIFICATION_ID, notificationId);
            intent.putExtra(MainJobProgressActivity.INCOMING_CALL_INVITE, callInvite);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        /*PendingIntent pendingIntent =
                PendingIntent.getActivity(this, notificationId, intent, PendingIntent.FLAG_CANCEL_CURRENT);*/

            PendingIntent pendingIntent;
            try {
                pendingIntent = PendingIntent.getActivity(this, notificationId, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                                | PendingIntent.FLAG_IMMUTABLE);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                pendingIntent =
                        PendingIntent.getActivity(this, notificationId, intent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            }

            /*
             * Pass the notification id and call sid to use as an identifier to cancel the
             * notification later
             */
            Bundle extras = new Bundle();
            extras.putInt(NOTIFICATION_ID_KEY, notificationId);
            extras.putString(CALL_SID_KEY, callInvite.getCallSid());

            Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel callInviteChannel = new NotificationChannel(VOICE_CHANNEL,
                        "Primary Voice Channel", NotificationManager.IMPORTANCE_HIGH);
                callInviteChannel.setLightColor(Color.GREEN);
                callInviteChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
                notificationManager.createNotificationChannel(callInviteChannel);

                Notification notification =
                        /* buildNotification(callInvite.getFrom() + " is calling.",*/
                        buildNotification(getString(R.string.app_name) + " " + getString(R.string.is_calling),
                                pendingIntent,
                                extras);
                notificationManager.notify(notificationId, notification);
            } else {
                NotificationCompat.Builder notificationBuilder =
                        new NotificationCompat.Builder(MyFirebaseMessagingService.this)
                                .setSmallIcon(R.drawable.ic_call_end_white_24dp)
                                .setContentTitle(getString(R.string.app_name))
                                .setContentText(getString(R.string.app_name) + " " + getString(R.string.is_calling))
                                .setAutoCancel(true)
                                .setExtras(extras)
                                .setSound(defaultSound)
                                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                                .setContentIntent(pendingIntent)
                                .setGroup("Insta-Guard-Notification")
                                .setColor(Color.rgb(214, 10, 37));

                notificationManager.notify(notificationId, notificationBuilder.build());
            }
        }
    }

    private void cancelNotification(CancelledCallInvite cancelledCallInvite) {
        SoundPoolManager.getInstance((this)).stopRinging();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            /*
             * If the incoming call was cancelled then remove the notification by matching
             * it with the call sid from the list of notifications in the notification drawer.
             */
            StatusBarNotification[] activeNotifications =
                    notificationManager.getActiveNotifications();
            for (StatusBarNotification statusBarNotification : activeNotifications) {
                Notification notification = statusBarNotification.getNotification();
                Bundle extras = notification.extras;
                String notificationCallSid = extras.getString(CALL_SID_KEY);

                if (cancelledCallInvite.getCallSid().equals(notificationCallSid)) {
                    notificationManager.cancel(extras.getInt(NOTIFICATION_ID_KEY));
                }
            }
        } else {
            /*
             * Prior to Android M the notification manager did not provide a list of
             * active notifications so we lazily clear all the notifications when
             * receiving a CancelledCallInvite.
             *
             * In order to properly cancel a notification using
             * NotificationManager.cancel(notificationId) we should store the call sid &
             * notification id of any incoming calls using shared preferences or some other form
             * of persistent storage.
             */
            notificationManager.cancelAll();
        }
    }

    /*
     * Send the CallInvite to the VoiceActivity. Start the activity if it is not running already.
     */
    private void sendCallInviteToActivity(CallInvite callInvite, int notificationId) {

        System.out.println("~onCallInvite~~sendCallInviteToActivity~");

        Intent intent = new Intent(this, MainJobProgressActivity.class);

        intent.putExtra("from", "main");
        intent.putExtra("customer_location", "");
        intent.putExtra("booking_id", getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE)
                .getString(Utils.SP_USER_CURRENT_BOOKING_ID, null));

        intent.setAction(MainJobProgressActivity.ACTION_INCOMING_CALL);
        intent.putExtra(MainJobProgressActivity.INCOMING_CALL_NOTIFICATION_ID, notificationId);
        intent.putExtra(MainJobProgressActivity.INCOMING_CALL_INVITE, callInvite);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);

    }

    /*
     * Send the CancelledCallInvite to the VoiceActivity
     */
    private void sendCancelledCallInviteToActivity(CancelledCallInvite cancelledCallInvite) {
        Intent intent = new Intent(MainJobProgressActivity.ACTION_CANCEL_CALL);
        intent.putExtra(MainJobProgressActivity.CANCELLED_CALL_INVITE, cancelledCallInvite);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    /**
     * Build a notification.
     *
     * @param text          the text of the notification
     * @param pendingIntent the body, pending intent for the notification
     * @param extras        extras passed with the notification
     * @return the builder
     */
    @TargetApi(Build.VERSION_CODES.O)
    public Notification buildNotification(String text, PendingIntent pendingIntent, Bundle extras) {
        return new Notification.Builder(getApplicationContext(), VOICE_CHANNEL)
                .setSmallIcon(R.drawable.ic_call_end_white_24dp)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(text)
                .setContentIntent(pendingIntent)
                .setExtras(extras)
                .setAutoCancel(true)
                .build();
    }


}