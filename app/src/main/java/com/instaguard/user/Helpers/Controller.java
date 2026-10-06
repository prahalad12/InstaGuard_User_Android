package com.instaguard.user.Helpers;

import static com.facebook.BuildConfig.DEBUG;
import static com.facebook.FacebookSdk.setAdvertiserIDCollectionEnabled;
import static com.facebook.FacebookSdk.setAutoLogAppEventsEnabled;
import static com.facebook.ads.AdSettings.IntegrationErrorMode.INTEGRATION_ERROR_CRASH_DEBUG_MODE;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookSdk;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.FirebaseApp;
import com.instaguard.user.Helpers.FrescoCache.ImagePipelineConfigFactory;
import com.instaguard.user.Helpers.Retrofit.APIInterface;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Controller extends Application {

	public static final String TAG = Controller.class.getSimpleName();


	private static Controller mInstance;
	private static Retrofit instance;
	private static APIInterface service;
	private static APIInterface cache_service;
	private static OkHttpClient okHttpClient;
	private RequestQueue mRequestQueue;
	//Geocoder
	private static Context mContextApplication;

	@Override
	public void onCreate() {
		super.onCreate();

		//Geocoder
		mContextApplication = getApplicationContext();

		MultiDex.install(this);
		mInstance = this;
		FirebaseApp.initializeApp(this);
		Fresco.initialize(this, ImagePipelineConfigFactory.getImagePipelineConfig(this));
		int cacheSize = 10 * 1024 * 1024; // 10 MB
		Cache cache = new Cache(getCacheDir(), cacheSize);

		FacebookSdk.sdkInitialize(mContextApplication);
		AppEventsLogger.activateApp(this);
		FacebookSdk.setAutoInitEnabled(true);
		FacebookSdk.fullyInitialize();

		AudienceNetworkAds.initialize(mContextApplication);
		//AdSettings.setMultiprocessSupportMode(AdSettings.MultiprocessSupportMode.MULTIPROCESS_SUPPORT_MODE_OFF);
		AdSettings.setIntegrationErrorMode(INTEGRATION_ERROR_CRASH_DEBUG_MODE);

		setAdvertiserIDCollectionEnabled(true);
		setAutoLogAppEventsEnabled(true);

		MobileAds.initialize(mContextApplication, initializationStatus -> {
		});

//		sAnalytics = GoogleAnalytics.getInstance(this);

		okHttpClient = new OkHttpClient.Builder()
				.connectTimeout(100, TimeUnit.SECONDS)
				.readTimeout(100, TimeUnit.SECONDS)
				.cache(cache)
				.build();
	}

	public static synchronized Controller getInstance() {
		return mInstance;
	}
	//Geocoder
	public static Context getAppContext(){
		return mContextApplication;
	}


	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext(),new OkHttpStack());
		}

		return mRequestQueue;
	}
	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}
	public synchronized static void setCacheInstance (String baseUrl) {

		Retrofit cache_instance = new Retrofit.Builder()
				.client(okHttpClient)
				.baseUrl(baseUrl)
				.addConverterFactory(GsonConverterFactory.create())
				.build();

		cache_service = cache_instance.create(APIInterface.class);
	}

	public static APIInterface getCacheService () {

		return cache_service;
	}

	public static class AudienceNetworkInitializeHelper
			implements AudienceNetworkAds.InitListener {

		/**
		 * It's recommended to call this method from Application.onCreate().
		 * Otherwise you can call it from all Activity.onCreate()
		 * methods for Activities that contain ads.
		 *
		 * @param context Application or Activity.
		 */
		void initialize(Context context) {
			if (!AudienceNetworkAds.isInitialized(context)) {
				if (DEBUG) {
					AdSettings.turnOnSDKDebugger(context);
				}

				AudienceNetworkAds
						.buildInitSettings(context)
						.withInitListener(new AudienceNetworkInitializeHelper())
						.initialize();
			}
		}

		@Override
		public void onInitialized(AudienceNetworkAds.InitResult result) {
			Log.d(AudienceNetworkAds.TAG, result.getMessage());
		}
	}
}