package com.instaguard.user.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.ads.AdView;
import com.instaguard.user.Activities.BaseActivity;
import com.instaguard.user.Adapter.SupportPagesAdapter;
import com.instaguard.user.BuildConfig;
import com.instaguard.user.Helpers.RecyclerTouchListener;
import com.instaguard.user.Helpers.Retrofit.APIClient;
import com.instaguard.user.Helpers.Retrofit.APIInterface;
import com.instaguard.user.Helpers.Utils;
import com.instaguard.user.Models.AppSettingWebModel;
import com.instaguard.user.Models.AppSupportPagesData;
import com.instaguard.user.Models.AppSupportPagesModel;
import com.instaguard.user.R;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.instaguard.user.Activities.BaseActivity.hideProgressDialog;
import static com.instaguard.user.Activities.BaseActivity.showProgressDialog;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_200;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_201;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_401;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_500;

public class SupportFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    ImageButton imgbtnBack;
    View view;
    LinearLayout llAboutUs, llPrivacy, llTermsAndCondition, llContactUs, llFaq;

    LinearLayout llStaticPages;
    RecyclerView rcvPages;
    SwipeRefreshLayout swipeRefresh;

    List<AppSupportPagesData> getve;
    String key;

    String userAccessToken;

    SharedPreferences spCommon;
    SharedPreferences.Editor getUpdateProfile, getRequestState;

    ImageButton fabChat;

    String strTowkChatURL;
    Dialog dialogTowkChat;
    WebView webView;
    ImageButton ivBackDialog;
    RelativeLayout progressBar;
    WebSettings settings;


    AdView adView;
    LinearLayout adContainer;

    public SupportFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    String strFrom = "main";

    @SuppressLint({"CommitPrefEdits", "LongLogTag", "SetJavaScriptEnabled"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_support, container, false);

        if (getArguments() != null) {
            strFrom = getArguments().getString(Utils.B_SUPPORT_FROM);
        }

        getUpdateProfile = requireActivity().getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE).edit();

        getRequestState = requireActivity().getSharedPreferences(Utils.MY_PREFS_REQUEST_STATE, MODE_PRIVATE).edit();

        spCommon = requireActivity().getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE);

        userAccessToken = spCommon.getString(Utils.SP_USER_ACCESSTOKEN, "123");
        strTowkChatURL = spCommon.getString(Utils.SP_COMMON_TOWK_CHAT_URL, null);

        if (BuildConfig.DEBUG) {
            System.out.println("driverId~~SupportSubFragment~~" + strTowkChatURL);
        }

        if (strTowkChatURL == null) {
            callAppSettings();
        }

        llStaticPages = view.findViewById(R.id.ll_static_pages);
        rcvPages = view.findViewById(R.id.rcv_pages);
        swipeRefresh = view.findViewById(R.id.swipe_refresh_layout_page);
        swipeRefresh.setOnRefreshListener(this);

        imgbtnBack = view.findViewById(R.id.imgbtn_back);
        llAboutUs = view.findViewById(R.id.ll_aboutus);
        llPrivacy = view.findViewById(R.id.ll_privacy);
        llTermsAndCondition = view.findViewById(R.id.ll_termsandcondition);
        llContactUs = view.findViewById(R.id.ll_contactus);
        llFaq = view.findViewById(R.id.ll_faq);

        fabChat = view.findViewById(R.id.fab_chat);
        if (strFrom != null && strFrom.equals("main")) {
            fabChat.setVisibility(View.GONE);
        } else {
            fabChat.setVisibility(View.GONE);
        }

        rcvPages.addOnItemTouchListener(
                new RecyclerTouchListener(requireActivity(), rcvPages,
                        new RecyclerTouchListener.OnTouchActionListener() {
                            @SuppressLint("LongLogTag")
                            @Override
                            public void onClick(View view, int position) {
                                Log.i("rcvPages", "onClick: rcvPages-> ");
                                if (position != -1) {
                                    AppSupportPagesData listFeed = getve.get(position);
                                    Log.e("getPageName~~~~~", listFeed.getPageName());

                                    callSubSupportFragment(Utils.B_ABOUTUS, listFeed.getPageName(), "dynamic");

                                }
                            }

                            @Override
                            public void onRightSwipe(View view, int position) {
                            }

                            @Override
                            public void onLeftSwipe(View view, int position) {
                            }
                        }));


        dialogTowkChat = new Dialog(requireActivity(), R.style.AppTheme);
        dialogTowkChat.setContentView(R.layout.dialog_towk_chat_web);
        dialogTowkChat.setCancelable(false);
        dialogTowkChat.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogTowkChat.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        webView = dialogTowkChat.findViewById(R.id.webView);
        ivBackDialog = dialogTowkChat.findViewById(R.id.imgbtn_back);
        progressBar = dialogTowkChat.findViewById(R.id.progressBar);

        //txtTimerCount = (CustomTextView) paynowDialog.findViewById(R.id.txt_timer_count);

        settings = webView.getSettings();
        settings.setDomStorageEnabled(true);
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setJavaScriptEnabled(true);
        settings.setUserAgentString("Android Webview");
        //  webView.setWebChromeClient();

        OnClick();

        getSupportPage();

        BaseActivity baseActivity = new BaseActivity();
        adContainer = view.findViewById(R.id.banner_container);
        baseActivity.callFacebookAdsCommon(requireActivity(), adView, adContainer);

        onBackButton(view);

        return view;
    }

    private void callSubSupportFragment(String bAboutus, String listFeed, String key) {

        Bundle args = new Bundle();
        args.putString(Utils.B_SUPPORT, bAboutus);
        args.putString(Utils.B_SUPPORT_STATIC_DYNAMIC, key);
        args.putString(Utils.B_SUPPORT_STATIC_DYNAMIC_PAGE, listFeed);
        args.putString(Utils.B_SUPPORT_FROM, strFrom);
        System.out.println("~~~strFrom~`~" + strFrom);
        int i;
        if (strFrom.equals("register")) {
            i = R.id.frame_signup;
        } else if (strFrom.equals("register_social")) {
            i = R.id.frame_signup_social;
        } else {
            i = R.id.frame_mainactivity;
        }
        Utils.switchContent(i,
                Utils.FRAGMENT_SUPPORT_SUBPAGES,
                requireActivity(), args,
                Utils.AnimationType.SLIDE_IN_SLIDE_OUT);
    }

    private void getSupportPage() {

        userAccessToken = spCommon.getString(Utils.SP_USER_ACCESSTOKEN, "123");

        HashMap<String, Object> map = new HashMap<>();
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<AppSupportPagesModel> call2 = apiInterface.doGetSupportDetails("Bearer " + userAccessToken, map);
        showProgressDialog(requireActivity());
        call2.enqueue(new Callback<AppSupportPagesModel>() {
            @Override
            public void onResponse(@NonNull Call<AppSupportPagesModel> call, @NonNull retrofit2.Response<AppSupportPagesModel> response) {
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    hideProgressDialog();
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Pages listed successfully.")) {
                        getve = Objects.requireNonNull(response.body()).getData();
                        if (getve != null) {
                            if (getve.size() > 0) {
                                rcvPages.setVisibility(View.VISIBLE);
                                llStaticPages.setVisibility(View.GONE);
                                key = "dynamic";

                                SupportPagesAdapter adapter = new SupportPagesAdapter(getve);
                                LinearLayoutManager horizontalLayoutManager
                                        = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                                rcvPages.setLayoutManager(horizontalLayoutManager);
                                rcvPages.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                hideProgressDialog();

                            } else {
                                rcvPages.setVisibility(View.GONE);
                                llStaticPages.setVisibility(View.VISIBLE);
                                key = "static";
                            }
                        } else {
                            rcvPages.setVisibility(View.GONE);
                            llStaticPages.setVisibility(View.VISIBLE);
                            key = "static";
                        }
                        hideProgressDialog();

                    } else if (response.body().getMessage().equals("No records Found.")) {
                        rcvPages.setVisibility(View.GONE);
                        llStaticPages.setVisibility(View.VISIBLE);
                        key = "static";
                    } else {
                        rcvPages.setVisibility(View.GONE);
                        llStaticPages.setVisibility(View.VISIBLE);
                        key = "static";
                    }

                    hideProgressDialog();
                } else if (response.code() == RESPONSECODE_401) {
                    hideProgressDialog();
                    Toast.makeText(requireActivity(), R.string.no_record_found, Toast.LENGTH_SHORT).show();
                } else if (response.code() == RESPONSECODE_500) {
                    hideProgressDialog();
                    Toast.makeText(requireActivity(), "Error 500", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<AppSupportPagesModel> call, @NonNull Throwable t) {
                Log.i("errror", "errr" + t);
                call.cancel();
                hideProgressDialog();
            }
        });

    }


    private void OnClick() {

        imgbtnBack.setOnClickListener(v -> {
            hideKeyBoard();
            backButtonFunction();
        });

        ivBackDialog.setOnClickListener(v -> {
            if (dialogTowkChat != null) {
                if (dialogTowkChat.isShowing()) {
                    dialogTowkChat.dismiss();
                }
            }
        });

        fabChat.setOnClickListener(view -> {

            strTowkChatURL = spCommon.getString(Utils.SP_COMMON_TOWK_CHAT_URL, null);

            if (strTowkChatURL != null) {

                try {
                    String afterDecode = URLDecoder.decode(strTowkChatURL, "UTF-8");

                    webView.loadUrl(afterDecode);

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

                    dialogTowkChat.show();


                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(requireActivity(), R.string.try_again, Toast.LENGTH_SHORT).show();
                callAppSettings();
            }

        });


        llAboutUs.setOnClickListener(v -> {
            callSubSupportFragment(Utils.B_ABOUTUS, "key", "static");
        });

        llTermsAndCondition.setOnClickListener(v -> {
            callSubSupportFragment(Utils.B_TERMS, "key", "static");
        });
        llPrivacy.setOnClickListener(v -> {
            callSubSupportFragment(Utils.B_PRIVACY, "key", "static");
        });
        llContactUs.setOnClickListener(v -> {
            callSubSupportFragment(Utils.B_CONTACTUS, "key", "static");
        });
        llFaq.setOnClickListener(v -> {
            callSubSupportFragment(Utils.B_FAQ, "key", "static");
        });
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
        removeFragment(Utils.FRAGMENT_SUPPORT);
    }

    public void removeFragment(String Tag) {
        Utils.CURRENT_TAG = null;
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(Objects.requireNonNull(fragmentManager.findFragmentByTag(Tag))).commit();
    }


    public void hideKeyBoard() {
        // Check if no view has focus:
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onRefresh() {
        swipeRefresh.setRefreshing(false);
        getSupportPage();
    }

    private void callAppSettings() {

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<AppSettingWebModel> call2 = apiInterface.doGetAppSettings();
        call2.enqueue(new Callback<AppSettingWebModel>() {
            @SuppressLint({"LongLogTag", "CommitPrefEdits"})
            @Override
            public void onResponse(@NonNull Call<AppSettingWebModel> call, @NonNull Response<AppSettingWebModel> response) {
                hideProgressDialog();
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {

                    String strTowkChatUrl = String.valueOf(Objects.requireNonNull(response.body()).getAppSetting().getTawkUrl());
                    String strTowkPropertyUrl = String.valueOf(Objects.requireNonNull(response.body()).getAppSetting().getPropertyUrl());
                    String strTowkPropertyId = String.valueOf(Objects.requireNonNull(response.body()).getAppSetting().getPropertyId());

                    getUpdateProfile.putString(Utils.SP_COMMON_TOWK_CHAT_URL, strTowkChatUrl);
                    getUpdateProfile.putString(Utils.SP_COMMON_TOWK_PROPERTY_URL, strTowkPropertyUrl);
                    getUpdateProfile.putString(Utils.SP_COMMON_TOWK_PROPERTY_ID, strTowkPropertyId);
                    getUpdateProfile.apply();
                }
            }

            @Override
            public void onFailure(@NonNull Call<AppSettingWebModel> call, @NonNull Throwable t) {
                call.cancel();
            }
        });
    }


    @Override
    public void onDestroyView() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }


}

