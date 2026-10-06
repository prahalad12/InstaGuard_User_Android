package com.instaguard.user.Activities;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.facebook.BuildConfig;
import com.facebook.ads.AdView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.instaguard.user.Helpers.DecimalUtils;
import com.instaguard.user.Helpers.Localizationactivity.ui.LocalizationActivity;
import com.instaguard.user.Helpers.Utils;
import com.instaguard.user.R;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class BaseActivity extends LocalizationActivity {

    private static Dialog progressDialog;

    public static void showProgressDialog(Context context) {
        try {
            if (progressDialog == null) {
                progressDialog = new Dialog
                        (context, R.style.AppTheme_ProgressIndicator);
                @SuppressLint("InflateParams")
                View view = LayoutInflater.from(context).inflate(R.layout.layout_progressbar_view, null);
                Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(R.color.colorprogresstransparent);
                progressDialog.setContentView(view);
            }
            progressDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideProgressDialog() {
        new CountDownTimer(600, 100) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                try {
                    if (progressDialog != null) {
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    }
                    progressDialog = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideProgressDialog();
    }

    public static class CustomTFSpan extends TypefaceSpan {

        private final Typeface typeface;

        public CustomTFSpan(Typeface typeface) {
            super("");
            this.typeface = typeface;
        }

        private static void applyTypeFace(Paint paint, Typeface tf) {
            paint.setTypeface(tf);
        }

        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
            applyTypeFace(ds, typeface);
        }

        @Override
        public void updateMeasureState(@NonNull TextPaint paint) {
            applyTypeFace(paint, typeface);
        }
    }


    public static class MySpinnerAdapter extends ArrayAdapter<String> {
        Typeface font;
        private final Context context;

        public MySpinnerAdapter(Context context_, int resource, String[] items) {
            super(context_, resource, items);
            context = context_;
            font = Typeface.createFromAsset(context_.getAssets(), context_.getString(R.string.App_Font));
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView, parent);
            view.setTypeface(font);
            view.setTextColor(context.getResources().getColor(R.color.black));
            return view;
        }

        @NonNull
        @Override
        public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
            TextView view = (TextView) super.getDropDownView(position, convertView, parent);
            view.setTypeface(font);
            /*if(position %2 == 0){
                view.setBackground(context.getResources().getDrawable(R.color.colorLeftBack));
            } else {
                view.setBackground(context.getResources().getDrawable(R.color.colorRightBack));
            }*/
            return view;
        }
    }

    public static String checkStringEmpty(DataSnapshot data_, String key) {
        String strFrom;
        if (data_.child(key).getValue() != null) {
            strFrom = Objects.requireNonNull(data_.child(key).getValue()).toString();
        } else {
            strFrom = "";
        }
        return strFrom;
    }

    public static String checkStringEmptyZero(DataSnapshot data_, String key) {
        String strFrom;
        if (data_.child(key).getValue() != null) {
            strFrom = Objects.requireNonNull(data_.child(key).getValue()).toString();
        } else {
            strFrom = "0";
        }
        return strFrom;
    }

    public String checkStringEmptyDecimalZero(DataSnapshot data_, String key) {
        String strFrom;
        if (data_.child(key).getValue() != null) {
            strFrom = Objects.requireNonNull(data_.child(key).getValue()).toString();
        } else {
            strFrom = "0.0";
        }
        return strFrom;
    }

    public static String getGoogleApiKey(Context context_) {

        Object value_;

        PackageManager manager_ = context_.getPackageManager();
        PackageInfo info_ = null;
        try {
            info_ = manager_.getPackageInfo(context_.getPackageName(), PackageManager.GET_META_DATA);
            value_ = info_.applicationInfo.metaData.get("com.google.android.geo.API_KEY");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            value_ = "";
            //value_ = BuildConfig.API_KEY;
        }
        return value_.toString();
    }

    public void callGoogleAdsCommon(Context context, com.google.android.gms.ads.AdView adView) {
        /*try {
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    public void callFacebookAdsCommon(Context context, AdView adView, LinearLayout adContainer) {

        /*try {
            if (AudienceNetworkAds.isInitialized(context)) {
                //return;
                System.out.println("~~~~isInAdsProcess~");
            } else {
                System.out.println("~~~~isInAdsProcess~not~~~");
            }

            if (BuildConfig.DEBUG) {
                AdSettings.setTestMode(true);
                //AdSettings.addTestDevice("190165ba-eb58-4bd0-8d29-103a45e7e85d");
            }

            if (BuildConfig.DEBUG) {
                adView = new AdView(context, "IMG_16_9_LINK#1400850877185021_1400851810518261", AdSize.BANNER_HEIGHT_50);
            } else {
                adView = new AdView(context, "1400850877185021_1400851810518261", AdSize.BANNER_HEIGHT_50);
            }
            if (BuildConfig.DEBUG) {
                System.out.println("~~~~adView~~~~" + adView.getPlacementId());
                System.out.println("~~~~adView~~~~" + adView.isAdInvalidated());
            }
            adContainer.setVisibility(View.VISIBLE);
            adContainer.addView(adView);
            // Request an ad
            adView.loadAd();

        } catch (Exception e) {
            e.printStackTrace();
            adContainer.setVisibility(View.INVISIBLE);
        }*/

    }


    public static boolean isOnline(Context context) {

        boolean connection = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        try {
            if (connectivityManager != null) {
                NetworkCapabilities capabilities;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                    if (capabilities != null) {
                        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                            Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR");
                            connection = true;
                        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                            Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI");
                            connection = true;
                        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                            Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET");
                            connection = true;
                        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                            Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET");
                            connection = true;
                        }
                    }
                } else {
                    NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
                    if (networkInfo != null) {
                        for (NetworkInfo info : networkInfo) {
                            if (info.getState() == NetworkInfo.State.CONNECTED) {
                                Log.i("Internet", "Internet is Connected.");
                                connection = true;
                            }
                        }
                    }
                }
            }
        } catch (Throwable tr) {
            connection = false;
        }

        /*if ((activeNetwork != null) && activeNetwork.isConnected()) {
            connection = true;
        }
         connection = Objects.requireNonNull(cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).getState() == NetworkInfo.State.CONNECTED ||
                Objects.requireNonNull(cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).getState() == NetworkInfo.State.CONNECTED;*/

        Log.i("Base Activity", "isOnline: " + connection);

        return connection;

    }

    public static String convertDistanceUnit(Context context) {

        String strDistanceUnit = context.getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE).getString(Utils.SP_COMMON_DISTANCE_UNIT, null);
        String miles;
        if (strDistanceUnit != null && !strDistanceUnit.equals("null") && !strDistanceUnit.equals("") && !strDistanceUnit.equals(" ")) {
            switch (strDistanceUnit) {
                case "miles":
                case "Miles":
                case "mile":
                case "Mile":
                    miles = "mile";
                    break;
                case "KM":
                case "km":
                case "kilometers":
                case "kilometer":
                case "Kilometer":
                case "Kilometers":
                default:
                    miles = "km";
                    break;
            }
        } else {
            miles = "km";
        }
        return miles;
    }

    public static InputFilter EMOJI_FILTER = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            boolean keepOriginal = true;
            StringBuilder sb = new StringBuilder(end - start);
            for (int index = start; index < end; index++) {
                int type = Character.getType(source.charAt(index));
                if (type == Character.SURROGATE || type == Character.OTHER_SYMBOL) {
                    return "";
                }
                char c = source.charAt(index);
                if (isCharAllowed(c))
                    sb.append(c);
                else
                    keepOriginal = false;
            }
            if (keepOriginal)
                return null;
            else {
                if (source instanceof Spanned) {
                    SpannableString sp = new SpannableString(sb);
                    TextUtils.copySpansFrom((Spanned) source, start, sb.length(), null, sp, 0);
                    return sp;
                } else {
                    return sb;
                }
            }
        }
    };

    private static boolean isCharAllowed(char c) {
        return Character.isLetterOrDigit(c) || Character.isSpaceChar(c);
    }


    public static String getSetUpIntentOneSave_(Context context_, String wherfrom) { //setup_intent_one_or_save
        return context_.getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE).getString(wherfrom + "setup_intent_one_or_save", "one_time");
    }

    public static String getHashMapPaymentId(Context context_, String wherfrom) {//_data_hash_map_data_paymentId
        return context_.getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE).getString(wherfrom + "_data_hash_map_data_paymentId", "0");
    }

    @SuppressLint("CommitPrefEdits")  //wherefrom = charter
    public static void saveHashMap_only(Context context_, String wherfrom, HashMap<String, Object> hash_map_data) {
        context_.getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE).edit().putString(wherfrom + "_data_hash_map_data", new Gson().toJson(hash_map_data)).apply();
    }

    @SuppressLint("CommitPrefEdits")
    public static void setHashMapSaveCardStatus(Context context_, String wherfrom, String save_card_status) {
        context_.getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE).edit().putString(wherfrom + "_data_hash_map_data_save_card_status", save_card_status).apply();
    }

    @SuppressLint("CommitPrefEdits")
    public static void setHashMapPaymentId(Context context_, String wherfrom, String paymentId) {
        context_.getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE).edit().putString(wherfrom + "_data_hash_map_data_paymentId", paymentId).apply();
    }

    @SuppressLint("CommitPrefEdits")
    public static void setSetUpIntentOneSave_(Context context_, String wherfrom, String check) {
        context_.getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE).edit().putString(wherfrom + "setup_intent_one_or_save", check).apply();
    }

    @SuppressLint("CommitPrefEdits")  //wherefrom = charter
    public static void saveHashMap(Context context_, String wherfrom, HashMap<String, Object> hash_map_data,
                                   String save_card_status) {
        context_.getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE).edit().putString(wherfrom + "_data_hash_map_data", new Gson().toJson(hash_map_data)).apply();
        context_.getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE).edit().putString(wherfrom + "_data_hash_map_data_save_card_status", save_card_status).apply();
    }


    public static HashMap<String, Object> getHashMap(Context context_, String wherfrom) {
        System.out.println("~~usageStatsMap~~~~" + wherfrom);

        Gson gson = new Gson();
        String json = context_.getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE).getString(wherfrom + "_data_hash_map_data", null);
        Type typeMyType = new TypeToken<HashMap<String, Object>>() {
        }.getType();
        HashMap<String, Object> usageStatsMap = gson.fromJson(json, typeMyType);
        System.out.println("~~usageStatsMap~~~~" + usageStatsMap);
        return usageStatsMap;
    }


    public static void callClickAction(View view, Boolean b) {
        view.setEnabled(b);
        view.setFocusableInTouchMode(b);
        view.setFocusable(b);
        view.setClickable(b);
        if (!b) {
            view.setAlpha(0.1f);
        } else {
            view.setAlpha(1f);
        }
    }


    @SuppressLint("DefaultLocale")
    public static String convertCurrencyPrice(Context context_, String price) {
        try {
            if (price != null) {
                if (!price.equals("null")) {
                    if (!price.equals("")) {
                        String currencyCommonPrice = context_.getSharedPreferences(Utils.PREFS_NAME_CURRENCY, MODE_PRIVATE)
                                .getString(Utils.SP_CURRENCY_COMMON_VALUES, "1");
                        if (BuildConfig.DEBUG) {
                            Log.i("currencyCommonPrice", "~~~" + currencyCommonPrice);
                            Log.i("price", "~~~" + price);
                        }
                        double multiplePrice = Double.parseDouble(currencyCommonPrice) * Double.parseDouble(price);
                        System.out.println("currency common price "+ multiplePrice);
                        double roundedNumber = DecimalUtils.round(multiplePrice, 2);
                        if (BuildConfig.DEBUG) {
                            Log.i("price", "~~String~~" + roundedNumber);
                            Log.i("price", "~~String~~" + String.format("%.2f", roundedNumber));
                        }

                        int roundedValue = (int) Math.round(roundedNumber);
                        if (roundedValue == roundedNumber) {
                            System.out.println("Value is rounded to " + roundedValue);
                            return String.valueOf(roundedValue);
                        } else if (roundedNumber == roundedNumber) {
                            System.out.println("Value is " + roundedNumber);
                            return String.format("%.2f", roundedNumber);
                        } else {
                            return String.format("%.2f", roundedNumber);
                        }

                        //return String.valueOf(roundedNumber);
                        //---? correct return String.format("%.2f", roundedNumber);
                    } else {
                        return "0";
                    }
                } else {
                    return "0";
                }
            } else {
                return "0";
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (BuildConfig.DEBUG)
                Log.e(e.toString(), "convertCurrencyPrice: ~~~~~");
            return "0";
        }
    }

    public static String convertHoursToDaysHoursMinutes(String hours_) {

        try {

            // Convert double to long
            long hours = (long) Double.parseDouble(hours_);
            //long hours = Long.parseLong(longValue);

            long days = TimeUnit.HOURS.toDays(hours);
            long remainingHours = hours % 24;

            if (com.instaguard.user.BuildConfig.DEBUG) {
                System.out.println("~~~~convertHoursToDays~~~~" + days);
                System.out.println("~~~~convertHoursToMinutes~~~~" + remainingHours);
            }

            StringBuilder resultStringBuilder = new StringBuilder();

            if (days > 0) {
                resultStringBuilder.append(days).append(" days ");
            } else {
                resultStringBuilder.append("");
            }

            if (remainingHours > 0) {
                if (remainingHours == 1) {
                    resultStringBuilder.append(remainingHours).append(" hr");
                } else {
                    resultStringBuilder.append(remainingHours).append(" hrs");
                }
            } else {
                resultStringBuilder.append("");
            }

            return resultStringBuilder.toString().trim();

        } catch (Exception e) {
            e.printStackTrace();
            if (Double.parseDouble(hours_) > 0.0) {
                if (Double.parseDouble(hours_) == 1.0) {
                    return hours_ + " hr";
                } else {
                    return hours_ + " hrs";
                }
            } else {
                return hours_ + " hrs";
            }
        }
    }

    public static String callView_(Object o) {
        if (o != null && !o.toString().equals("null") && !o.toString().equals("")) {
            return o.toString();
        } else {
            return "0";
        }
    }


    public static void hideKeyBoard(Activity context) {
        // Check if no view has focus:
        View view = context.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void setVisibility(View view, boolean visibility) {
        view.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    public static void setVisibility_in(View view, boolean visibility) {
        view.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
    }

    public void animateMarkerWithRotation(Marker marker, float bearing, double[] startValues, double[] endValues) {
        ValueAnimator latLngAnimator = ValueAnimator.ofObject(new DoubleArrayEvaluator(), startValues, endValues);
        latLngAnimator.setDuration(800);
        latLngAnimator.setInterpolator(new DecelerateInterpolator());
        latLngAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                double[] animatedValue = (double[]) animation.getAnimatedValue();
                marker.setPosition(new LatLng(animatedValue[0], animatedValue[1]));
            }
        });
        latLngAnimator.start();
        marker.setRotation(bearing);

    }

    public void rotateMarker(Marker marker, final float toRotation, double[] startValues, double[] endValues) {
        // if(!isMarkerRotating) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final float startRotation = marker.getRotation();
        final long duration = 1555;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                //  isMarkerRotating = true;

                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed / duration);

                float rot = t * toRotation + (1 - t) * startRotation;

                marker.setRotation(-rot > 180 ? rot / 2 : rot);
                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    //  isMarkerRotating = false;
                }
            }

        });
        animateMarkerTo(marker, startValues, endValues);
    }

    // Animation handler for old APIs without animation support
    public void animateMarkerTo(Marker marker, double[] startValues, double[] endValues) {
        ValueAnimator latLngAnimator = ValueAnimator.ofObject(new DoubleArrayEvaluator(), startValues, endValues);
        latLngAnimator.setDuration(800);
        latLngAnimator.setInterpolator(new DecelerateInterpolator());
        latLngAnimator.addUpdateListener(animation -> {
            double[] animatedValue = (double[]) animation.getAnimatedValue();
            marker.setPosition(new LatLng(animatedValue[0], animatedValue[1]));
        });
        latLngAnimator.start();
        // marker.setRotation(bearing);
    }

    public static class DoubleArrayEvaluator implements TypeEvaluator<double[]> {

        private double[] mArray;

        /**
         * Create a DoubleArrayEvaluator that does not reuse the animated value. Care must be taken
         * when using this option because on every evaluation a new <code>double[]</code> will be
         * allocated.
         *
         * @see #DoubleArrayEvaluator(double[])
         */
        public DoubleArrayEvaluator() {
        }

        /**
         * Create a DoubleArrayEvaluator that reuses <code>reuseArray</code> for every evaluate() call.
         * Caution must be taken to ensure that the value returned from
         * {@link ValueAnimator#getAnimatedValue()} is not cached, modified, or
         * used across threads. The value will be modified on each <code>evaluate()</code> call.
         *
         * @param reuseArray The array to modify and return from <code>evaluate</code>.
         */
        public DoubleArrayEvaluator(double[] reuseArray) {
            mArray = reuseArray;
        }

        /**
         * Interpolates the value at each index by the fraction. If
         * {@link #DoubleArrayEvaluator(double[])} was used to construct this object,
         * <code>reuseArray</code> will be returned, otherwise a new <code>double[]</code>
         * will be returned.
         *
         * @param fraction   The fraction from the starting to the ending values
         * @param startValue The start value.
         * @param endValue   The end value.
         * @return A <code>double[]</code> where each element is an interpolation between
         * the same index in startValue and endValue.
         */
        @Override
        public double[] evaluate(float fraction, double[] startValue, double[] endValue) {
            double[] array = mArray;
            if (array == null) {
                array = new double[startValue.length];
            }

            for (int i = 0; i < array.length; i++) {
                double start = startValue[i];
                double end = endValue[i];
                array[i] = start + (fraction * (end - start));
            }
            return array;
        }
    }

    public static void showSnackbar(Context context, View view, String s, String key) {

        Snackbar snackbars = Snackbar.make(view, s, Snackbar.LENGTH_LONG).setAction("Action", null);
        View snackbarViews = snackbars.getView();
        TextView textViews = snackbarViews.findViewById(com.google.android.material.R.id.snackbar_text);
        Typeface font = Typeface.createFromAsset(context.getAssets(), context.getResources().getString(R.string.App_Font));
        textViews.setTypeface(font, Typeface.BOLD);

        if (key.equalsIgnoreCase("warning")) {
            snackbarViews.setBackgroundColor(Color.RED);
        } else if (key.equalsIgnoreCase("Field")){
            snackbarViews.setBackgroundColor(Color.GRAY);
        } else if (key.equalsIgnoreCase("other")){
            snackbarViews.setBackgroundColor(Color.GREEN);
        }

        View snackbarView = snackbars.getView();

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
        params.gravity = Gravity.TOP;
        params.height = 150;
        snackbarView.setLayoutParams(params);

        snackbars.show();
    }
}
