package com.instaguard.user.Fragments;

import static android.content.Context.MODE_PRIVATE;
import static java.lang.System.out;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.BuildConfig;
import com.instaguard.user.CustomViews.CustomTextView;
import com.instaguard.user.Helpers.Utils;
import com.instaguard.user.R;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;

public class MyCalenderBookingFragment extends Fragment {

    View view;
    SharedPreferences SP_Prefs;
    String userAccessToken, userId;
    ImageButton imgbtnBack;
    RecyclerView rv_user_feedback;
    CustomTextView txt_no_data_found;

    //String url = "https://664e-2401-4900-1ce0----3036-91f8-a6cd-47fc-25ec.ngrok-free.app/calender/customer/109/";
    //String url = "https://664e-2401-4900-1ce0-----3036-91f8-a6cd-47fc-25ec.ngrok-free.app/calender/customer/109/MTItMDItMjAyNA==";

    WebView webView;
    View progressBar;
    WebSettings settings;

    public MyCalenderBookingFragment() {
        // Required empty public constructor
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Log.e("driverId~~RideHistoryFragment~~", "");
        }
    }

    @SuppressLint("LongLogTag")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_calender_booking, container, false);

        SP_Prefs = requireActivity().getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE);
        userId = SP_Prefs.getString(Utils.SP_USERID, null);
        userAccessToken = SP_Prefs.getString(Utils.SP_USER_ACCESSTOKEN, null);
        Log.e("driverId~~UserFeedbackFragment~~", userId);

        initView(view);

        onBackButton(view);

        return view;
    }

    @SuppressLint({"ResourceAsColor", "ResourceType", "SetJavaScriptEnabled"})
    void initView(View view) {

        imgbtnBack = (ImageButton) view.findViewById(R.id.imgbtn_back);
        rv_user_feedback = (RecyclerView) view.findViewById(R.id.rv_user_feedback);
        txt_no_data_found = (CustomTextView) view.findViewById(R.id.txt_no_data_found);
        webView = view.findViewById(R.id.webView);
        progressBar = view.findViewById(R.id.progressBar);

        imgbtnBack.setOnClickListener(v -> backButtonFunction());

        view.findViewById(R.id.iv_menu_profile).setOnClickListener(v -> {
            if (webView != null) {
                webView.reload();
            }
        });

        callView();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
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
        removeFragment(Utils.FRAGMENT_MY_CALENDER_BOOKING);
    }

    public void removeFragment(String Tag) {
        Utils.CURRENT_TAG = null;
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(Objects.requireNonNull(fragmentManager.findFragmentByTag(Tag))).commit();
    }


    private void callView() {
        try {
            progressBar.setVisibility(View.VISIBLE);

            webView.clearCache(true);

            webView.clearFormData();

            webView.clearHistory();

            webView.clearSslPreferences();


            //String dateString = "12-02-2024";

            // Get the current date
            Date currentDate = new Date();

            // Format the current date to "dd-MM-yyyy"
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String dateString = sdf.format(currentDate);

            Log.d("Current Date", dateString);
            // Encode the date string to Base64
            byte[] bytes;
            try {
                bytes = dateString.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return;
            }

            // Encode the byte array to Base64
            String base64Encoded = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                base64Encoded = Base64.getEncoder().encodeToString(bytes);
            }

//            Log.d("Base64 Encoded Date", base64Encoded);
            System.out.println("Base64 Encoded Date: " + base64Encoded);

            if (base64Encoded != null) {
                webView.loadUrl(Utils.CALENDER_WEB_URL + userId + "/" + base64Encoded);
            }

            settings = webView.getSettings();
            settings.setDomStorageEnabled(true);
            settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
            settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
            settings.setJavaScriptEnabled(true);
            settings.setJavaScriptCanOpenWindowsAutomatically(true);
            settings.setUserAgentString("Android Webview");

            webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
            //webView.addJavascriptInterface(new WebAppInterface(), "Android");


            webView.setPictureListener((webView, picture) -> {
                if (picture != null) {
                    out.println("~~~~PictureListener~~" + picture);
                }
            });


            webView.setWebViewClient(new WebViewClient() {

                @Override
                public void onLoadResource(WebView view, String url) {
                    super.onLoadResource(view, url);
                    if (BuildConfig.DEBUG) {
                        Log.e("TAG", "onLoadResource: " + url);
                    }
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    Log.e("TAG", "onPageFinished: " + url);
                    super.onPageFinished(view, url);
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    Log.e("TAG", "onPageStarted: " + url);
                    progressBar.setVisibility(View.VISIBLE);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
