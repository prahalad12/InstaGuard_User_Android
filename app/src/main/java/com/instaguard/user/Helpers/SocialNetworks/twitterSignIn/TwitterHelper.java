//package com.instaguard.user.Helpers.SocialNetworks.twitterSignIn;
//
//import android.app.Activity;
//import android.content.Intent;
//import androidx.annotation.NonNull;
//import androidx.annotation.StringRes;
//
//import android.util.Log;
//
//import com.twitter.sdk.android.core.Callback;
//import com.twitter.sdk.android.core.DefaultLogger;
//import com.twitter.sdk.android.core.Result;
//import com.twitter.sdk.android.core.Twitter;
//import com.twitter.sdk.android.core.TwitterApiClient;
//import com.twitter.sdk.android.core.TwitterAuthConfig;
//import com.twitter.sdk.android.core.TwitterAuthToken;
//import com.twitter.sdk.android.core.TwitterConfig;
//import com.twitter.sdk.android.core.TwitterCore;
//import com.twitter.sdk.android.core.TwitterException;
//import com.twitter.sdk.android.core.TwitterSession;
//import com.twitter.sdk.android.core.identity.TwitterAuthClient;
//import com.twitter.sdk.android.core.models.User;
//import com.twitter.sdk.android.core.services.AccountService;
//
//import retrofit2.Call;
//
//
///**
// * This class will handle all the twitter sign in related functionality.
// */
//public class TwitterHelper {
//    private static final String TAG = "TwitterHelper";
//    @NonNull
//    private final Activity mActivity;
//    @NonNull
//    private final TwitterResponse mListener;
//    private TwitterAuthClient mAuthClient;
//
//    /**
//     * Public constructor. This will initialize twitter sdk.
//     *
//     * @param twitterApiKey     twitter api key
//     * @param twitterSecreteKey twitter secrete key
//     * @param response          {@link TwitterResponse} response listener.
//     * @param context           instance of the caller.
//     */
//    public TwitterHelper(@StringRes final int twitterApiKey,
//                         @StringRes final int twitterSecreteKey,
//                         @NonNull TwitterResponse response,
//                         @NonNull Activity context) {
//
//        System.out.println("~~TwitterHelper~~1~~");
//        //noinspection ConstantConditions
//        if (response == null) throw new IllegalArgumentException("TwitterResponse cannot be null.");
//
//        mActivity = context;
//        mListener = response;
//
//        //initialize sdk
//        TwitterConfig authConfig = new TwitterConfig.Builder(context)
//                .logger(new DefaultLogger(Log.DEBUG))
//                .twitterAuthConfig(new TwitterAuthConfig(context.getResources().getString(twitterApiKey),
//                        context.getResources().getString(twitterSecreteKey)))
//                .debug(true)
//                .build();
//        Twitter.initialize(authConfig);
//
//        TwitterAuthToken token = new TwitterAuthToken();
//        System.out.println("~~TwitterHelper~~2~~");
//        mAuthClient = new TwitterAuthClient();
//    }
//
//    /**
//     * Perform twitter sign in. Call this method when user clicks on "Login with Twitter" button.
//     */
//    public void performSignIn() {
//
//        System.out.println("~~TwitterHelper~~3~~");
//        mAuthClient.authorize(mActivity, new Callback<TwitterSession>() {
//            @Override
//            public void success(Result<TwitterSession> result) {
//                System.out.println("~~TwitterHelper~~4~~");
//                TwitterSession session = result.data;
//                mListener.onTwitterSignIn(session.getUserName(), session.getUserId() + " ");
//                String authToken = session.getAuthToken().token;
//                String authTokenSecret = session.getAuthToken().secret;
//
//                System.out.println("~authToken~~~"+authToken);
//                System.out.println("~authTokenSecret~~~"+authTokenSecret);
//
//                //load user data.
//                getUserData();
//            }
//
//            @Override
//            public void failure(TwitterException exception) {
//                mListener.onTwitterError();
//                System.out.println("~~~~ggggggg~"+exception);
//            }
//        });
//    }
//
//    /**
//     * This method handles onActivityResult callbacks from fragment or activity.
//     *
//     * @param requestCode request code received.
//     * @param resultCode  result code received.
//     * @param data        Data intent.
//     */
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (mAuthClient != null)
//            mAuthClient.onActivityResult(requestCode, resultCode, data);
//    }
//
//    /**
//     * Load twitter user profile.
//     */
//    private void getUserData() {
//        System.out.println("~~TwitterHelper~~5~~");
//        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
//        AccountService statusesService = twitterApiClient.getAccountService();
//        Call<User> call = statusesService.verifyCredentials(true, true, true);
//        call.enqueue(new Callback<User>() {
//            @Override
//            public void success(Result<User> userResult) {
//                //Do something with result
//
//                //parse the response
//                TwitterUser user = new TwitterUser();
//                user.screenName = userResult.data.screenName;
//                user.name = userResult.data.name;
//                user.email = userResult.data.email;
//                user.description = userResult.data.description;
//                user.pictureUrl = userResult.data.profileImageUrl;
//                user.bannerUrl = userResult.data.profileBannerUrl;
//                user.language = userResult.data.lang;
//                user.id = userResult.data.id;
//
//                mListener.onTwitterProfileReceived(user);
//            }
//
//            public void failure(TwitterException exception) {
//                //Do something on failure
//                System.out.println("~~System.out.printl~~~~"+exception.toString());
//            }
//        });
//    }
//}
//
