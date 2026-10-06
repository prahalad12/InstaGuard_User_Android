package com.instaguard.user.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.instaguard.user.Activities.MainActivity;
import com.facebook.drawee.view.SimpleDraweeView;
import com.instaguard.user.Activities.BaseActivity;
import com.instaguard.user.Activities.HomeActivity;
import com.instaguard.user.Activities.MainJobProgressActivity;
import com.instaguard.user.Adapter.OnGoingJobsAdapter;
import com.instaguard.user.CustomViews.CustomTextView;
import com.instaguard.user.Helpers.RecyclerViewsTools.RecyclerTouchListener;
import com.instaguard.user.Helpers.Retrofit.APIClient;
import com.instaguard.user.Helpers.Retrofit.APIInterface;
import com.instaguard.user.Helpers.Utils;
import com.instaguard.user.Models.Banner.BannerData;
import com.instaguard.user.Models.Banner.BannerModel;
import com.instaguard.user.Models.CancelJobModel;
import com.instaguard.user.Models.OnGoingJobsData;
import com.instaguard.user.Models.OnGoingJobsModel;
import com.instaguard.user.Models.RefreshTokenModel;
import com.instaguard.user.Models.UpcomingCancelJobsModel;
import com.instaguard.user.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.instaguard.user.Activities.BaseActivity.hideProgressDialog;
import static com.instaguard.user.Activities.BaseActivity.showProgressDialog;
import static com.instaguard.user.Activities.MainActivity.txtOnGoingJobsLabel;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_200;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_201;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_401;

public class OnGoingJobsFragment extends Fragment implements OnGoingJobsAdapter.SuccessResponse, SwipeRefreshLayout.OnRefreshListener {

    AdView adViewGoogle;
    View view;
    SharedPreferences SP_Prefs;
    SharedPreferences.Editor putSpState;
    String userId;
    ImageButton imgbtnBack;
    SharedPreferences spCommon;
    String userAccessToken, strCustLoc, strBookid;
    RecyclerView rvOnGoingJobs;
    RelativeLayout rlEmptyview, rlParentLyt;
    SwipeRefreshLayout swipeRefresh;

    OnGoingJobsAdapter adapter;
    public String TAG = "OnGoingJobsFragment";

    /*Image Slider Start*/
    private View parent_view;
    private ViewPager viewPager;
    private LinearLayout layout_dots;
    private AdapterImageSlider adapterImageSlider;
    private Runnable runnable = null;
    private Handler handler = new Handler();
    View progresView, viewImageSlider;
    RecyclerView.LayoutManager layoutManager;
    List<BannerData> bannerData = new ArrayList<>();
    RelativeLayout rlEmptyviewAnnouncement, rlEmptyView;
    /*Image Slider End*/


    List<OnGoingJobsData> getve;
    private OnGoingJobsData listFeed;

    public OnGoingJobsFragment() {
        // Required empty public constructor
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Log.e("userId~~RideHistoryFragment~~", "");
        }
    }

    @SuppressLint({"LongLogTag", "CommitPrefEdits"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_ongoing_jobs, container, false);
        SP_Prefs = requireActivity().getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE);
        userId = SP_Prefs.getString(Utils.SP_USERID, null);
        Log.e("userId~~OnGoingJobsFragment~~", userId);

        spCommon = requireActivity().getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE);
        putSpState = requireActivity().getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE).edit();

        rvOnGoingJobs = (RecyclerView) view.findViewById(R.id.rv_ongoingjobs);
        rlEmptyview = (RelativeLayout) view.findViewById(R.id.rl_emptyview);
        rlParentLyt = (RelativeLayout) view.findViewById(R.id.rl_parent_lyt);
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefresh.setOnRefreshListener(this);

        rvOnGoingJobs.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false);
        rvOnGoingJobs.setLayoutManager(mLayoutManager3);
        rvOnGoingJobs.setNestedScrollingEnabled(false);
        rvOnGoingJobs.setAdapter(adapter);

        rvOnGoingJobs.addOnItemTouchListener(
                new RecyclerTouchListener(requireActivity(), rvOnGoingJobs,
                        new RecyclerTouchListener.OnTouchActionListener() {
                            @SuppressLint("LongLogTag")
                            @Override
                            public void onClick(View view, int position) {
                                Log.i("rvSubCategory", "onClick: rvSubCategory-> ");
                                if (position != -1) {

                                    listFeed = getve.get(position);
                                    if (String.valueOf(listFeed.getServiceStatus()).equals("21")) {
                                        Toast.makeText(requireActivity(), R.string.timeout_title, Toast.LENGTH_SHORT).show();
                                        showTimeOut(listFeed.getBookingId());
                                    } else if (String.valueOf(listFeed.getServiceStatus()).equals("13")) {
                                        //Toast.makeText(requireActivity(), getResources().getString(R.string.), Toast.LENGTH_SHORT).show();
                                        // showTimeOut(listFeed.getBookingId());
                                        strBookid = listFeed.getBookingId();
                                        cancelSuccessStatus(strBookid, "alert", getResources().getString(R.string.are_you_sure_want_to_cancel_this_job));
                                    } else {
                                        BaseActivity.callClickAction(view, false);

                                        Log.e(TAG, "onClick: ---->" + listFeed.getBookingId());
                                        Log.e(TAG, "onClick: ---->" + listFeed.getCustomerLocation());

                                        SharedPreferences.Editor getState_ = requireActivity().getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE).edit();
                                        getState_.putInt("chat_click", 0);
                                        getState_.apply();

                                        putSpState.putString(Utils.SP_USER_CURRENT_BOOKING_ID, String.valueOf(listFeed.getBookingId()));
                                        putSpState.apply();

                                        Intent mainJobProgressActivity = new Intent(requireActivity(), MainJobProgressActivity.class);
                                        mainJobProgressActivity.putExtra("from", "ongoing");
                                        mainJobProgressActivity.putExtra("booking_id", String.valueOf(listFeed.getBookingId()));
                                        mainJobProgressActivity.putExtra("customer_location", String.valueOf(listFeed.getCustomerLocation()));
                                        requireActivity().overridePendingTransition(R.anim.fade_in_speed, R.anim.fade_out_speed);
                                        startActivity(mainJobProgressActivity);

                                        BaseActivity.callClickAction(view, true);

                                    }
                                }
                            }

                            @Override
                            public void onRightSwipe(View view, int position) {
                            }

                            @Override
                            public void onLeftSwipe(View view, int position) {
                            }
                        }));

        initView(view);

        layout_dots = (LinearLayout) view.findViewById(R.id.layout_dots);
        viewPager = (ViewPager) view.findViewById(R.id.pager);
        viewPager.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        rlEmptyviewAnnouncement = (RelativeLayout) view.findViewById(R.id.rl_emptyview_announcement);
        viewImageSlider = view.findViewById(R.id.view_image_slider);

        //viewPager.setLayoutMode(View.LAYOUT_DIRECTION_LTR);
        //viewPager.setRotationY(180);
        // viewPager.setRtlOriented(false);
        Log.i(TAG, "onCreateView: viewpager rotation--->" + viewPager.getRotationY());
        adapterImageSlider = new AdapterImageSlider(requireActivity(), new ArrayList<Image>());

        layoutManager = new LinearLayoutManager(getActivity());
        // rvMain.setLayoutManager(layoutManager);

        getOnGoingJobsCall();

        callSlideBannerImage("9");

        BaseActivity baseActivity = new BaseActivity();
        adViewGoogle = view.findViewById(R.id.adView);
        baseActivity.callGoogleAdsCommon(requireActivity(), adViewGoogle);

        onBackButton(view);

        return view;

    }

    void initView(View view) {

        imgbtnBack = (ImageButton) view.findViewById(R.id.imgbtn_back);

        imgbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButtonFunction();
            }
        });

        rlParentLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void onDestroy() {
        if (adViewGoogle != null) {
            adViewGoogle.destroy();
        }
        super.onDestroy();
    }

    private void getOnGoingJobsCall() {

        SharedPreferences getspCurrency = requireActivity().getSharedPreferences(Utils.PREFS_NAME_CURRENCY, MODE_PRIVATE);
        String strLanguageShortName = getspCurrency.getString(Utils.SP_LANGUAGE_SHORT_NAME, null);
        if (strLanguageShortName == null) {
            strLanguageShortName = "en";
        }

        userAccessToken = spCommon.getString(Utils.SP_USER_ACCESSTOKEN, null);
        HashMap<String, Object> map = new HashMap<>();
        map.put("customer_id", userId);
        map.put("language", strLanguageShortName);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<OnGoingJobsModel> call2 = apiInterface.doCustomerOnGoingJobs("Bearer " + userAccessToken, map);
        showProgressDialog(getActivity());
        call2.enqueue(new Callback<OnGoingJobsModel>() {
            @Override
            public void onResponse(@NonNull Call<OnGoingJobsModel> call, @NonNull retrofit2.Response<OnGoingJobsModel> response) {
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    hideProgressDialog();
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Fetching ongoing job successful.")) {
                        getve = response.body().getServiceRequest();
                        Log.i(TAG, "onResponse: getOnGoingJobsCall-->" + getve);
                        if (getve.size() > 0) {
                            rlEmptyview.setVisibility(View.GONE);
                            rvOnGoingJobs.setVisibility(View.VISIBLE);
                            if (txtOnGoingJobsLabel != null) {
                                txtOnGoingJobsLabel.setVisibility(View.VISIBLE);
                            }
                            adapter = new OnGoingJobsAdapter(requireActivity(), getve);
                            LinearLayoutManager horizontalLayoutManager
                                    = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                            rvOnGoingJobs.setLayoutManager(horizontalLayoutManager);
                            rvOnGoingJobs.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } else {
                            rlEmptyview.setVisibility(View.VISIBLE);
                            rvOnGoingJobs.setVisibility(View.GONE);
                            if (txtOnGoingJobsLabel != null) {
                                txtOnGoingJobsLabel.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        //callRefreshToken("Category");
                        if (txtOnGoingJobsLabel != null) {
                            txtOnGoingJobsLabel.setVisibility(View.GONE);
                        }
                    }
                } else if (response.code() == RESPONSECODE_401) {
                    //callRefreshToken("Category");
                    if (txtOnGoingJobsLabel != null) {
                        txtOnGoingJobsLabel.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<OnGoingJobsModel> call, @NonNull Throwable t) {
                Log.i("errror", "errr" + t);
                call.cancel();
                if (txtOnGoingJobsLabel != null) {
                    txtOnGoingJobsLabel.setVisibility(View.GONE);
                }
                Toast.makeText(requireActivity(), R.string.try_again, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void showTimeOut(String strBookid) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);

        Typeface tf = Typeface.createFromAsset(requireActivity().getAssets(),
                getString(R.string.App_Font));

        BaseActivity.CustomTFSpan tfSpan = new BaseActivity.CustomTFSpan(tf);
        SpannableString spannableString = new SpannableString(getString(R.string.timeout_title));
        spannableString.setSpan(tfSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString spannableStrings = new SpannableString(getString(R.string.cancel_job));
        spannableStrings.setSpan(tfSpan, 0, spannableStrings.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString spannableStrings1 = new SpannableString(getString(R.string.cancel_job));
        spannableStrings1.setSpan(tfSpan, 0, spannableStrings1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString spannableStringss = new SpannableString(getString(R.string.ok));
        spannableStringss.setSpan(tfSpan, 0, spannableStringss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        builder.setTitle(spannableString);

//        builder.setMessage(spannableStrings1);

        builder.setPositiveButton(spannableStringss,
                (dialog, which) -> {
//                        clearSPmodePrivate();
                    dialog.dismiss();
                });

        builder.setNegativeButton(spannableStrings,
                (dialog, which) -> {
                    dialog.dismiss();
                    callCancelJobUserUpdate(strBookid);
                });

        builder.show();
    }

    private void callCancelJobUserUpdate(String strBookid) {

        userAccessToken = spCommon.getString(Utils.SP_USER_ACCESSTOKEN, null);
        HashMap<String, Object> map = new HashMap<>();
        map.put("booking_id", strBookid);
        map.put("reason", "");
        map.put("mode", "customer");
        // map.put("token", userAccessToken);
        Log.i("CallUpdateDetails", "~~~~" + map);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<CancelJobModel> call2 = apiInterface.doCancelJobDetails("Bearer " + userAccessToken, map);
        showProgressDialog(getActivity());
        call2.enqueue(new Callback<CancelJobModel>() {
            @Override
            public void onResponse(@NonNull Call<CancelJobModel> call, @NonNull Response<CancelJobModel> response) {
                hideProgressDialog();
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Cancelled successfully.")) {

                        getOnGoingJobsCall();

                    } else {

                        Toast.makeText(requireActivity(), R.string.try_again, Toast.LENGTH_SHORT).show();
                        //callRefreshToken("cancelJob");

                    }
                } else if (response.code() == RESPONSECODE_401) {
                    // callRefreshToken("cancelJob");
                }
            }

            @Override
            public void onFailure(@NonNull Call<CancelJobModel> call, @NonNull Throwable t) {
                // CommonUtil.showRetrofitError(LoginActivity.this, error);
                hideProgressDialog();
                call.cancel();
            }
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
        /*if (getve != null) {
            if (getve.size() > 0) {*/
        startActivity(new Intent(requireActivity(), MainActivity.class));
        requireActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        requireActivity().finish();
            /*} else {
                removeFragment(Utils.FRAGMENT_ONGOINGJOBS);
            }
        } else {
            removeFragment(Utils.FRAGMENT_ONGOINGJOBS);
        }*/
    }

    public void removeFragment(String Tag) {
        Utils.CURRENT_TAG = null;
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(Objects.requireNonNull(fragmentManager.findFragmentByTag(Tag))).commit();
    }


    @Override
    public void onSuccess(HashMap<String, Object> map) {
        backButtonFunction();
    }

    private void callSlideBannerImage(String key) {

        userAccessToken = spCommon.getString(Utils.SP_USER_ACCESSTOKEN, null);
        HashMap<String, Object> map = new HashMap<>();
        map.put("mode", key);
        Log.i("callSlideBannerImage", "~~~~" + map);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<BannerModel> call = apiInterface.doSlideBannerImages("Bearer " + userAccessToken, map);
        call.enqueue(new Callback<BannerModel>() {

            @Override
            public void onResponse(@NonNull Call<BannerModel> call, @NonNull Response<BannerModel> response) {

                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {

                    if (response.body().getMessage().equals("Success")) {
                        Log.e("sdfs", response.code() + "");
                        BannerModel resource = response.body();
                        bannerData = response.body().getData();
                        Log.i("sfsdf", "onResponse: callBannerList-->" + bannerData.size());
                        if (bannerData != null) {
                            if (bannerData.size() != 0) {
                                try {
                                    initComponent();
                                } catch (Exception e) {
                                    Log.i("sdff", "onResponse: exception--->" + e);
                                }
                            } else {
                                callFailureBanner(key);
                            }
                        } else {
                            callFailureBanner(key);
                        }
                    } else {
                        callFailureBanner(key);
                    }
                } else {
                    callFailureBanner(key);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BannerModel> call, @NonNull Throwable t) {
                Toast.makeText(requireActivity(), R.string.try_again, Toast.LENGTH_SHORT).show();
                viewImageSlider.setVisibility(View.GONE);
                rlEmptyviewAnnouncement.setVisibility(View.VISIBLE);
            }
        });

    }

    private void callFailureBanner(String key) {
        if (!key.equals("all")) {
            callSlideBannerImage("all");
        } else {
            viewImageSlider.setVisibility(View.GONE);
            rlEmptyviewAnnouncement.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRefresh() {

        swipeRefresh.setRefreshing(false);
        getOnGoingJobsCall();
        callSlideBannerImage("9");
    }

    private static class AdapterImageSlider extends PagerAdapter {

        private Activity act;
        private List<Image> items;

        private AdapterImageSlider.OnItemClickListener onItemClickListener;

        private interface OnItemClickListener {
            void onItemClick(View view, Image obj);
        }

        public void setOnItemClickListener(AdapterImageSlider.OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        // constructor
        private AdapterImageSlider(Activity activity, List<Image> items) {
            this.act = activity;
            this.items = items;
        }

        @Override
        public int getCount() {
            return this.items.size();
        }

        public Image getItem(int pos) {
            return items.get(pos);
        }

        public void setItems(List<Image> items) {
            this.items = items;
            notifyDataSetChanged();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == ((RelativeLayout) object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            final Image o = items.get(position);
            LayoutInflater inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.item_slider_image_main, container, false);

            SimpleDraweeView image = (SimpleDraweeView) v.findViewById(R.id.image);
            CustomTextView title = (CustomTextView) v.findViewById(R.id.title);
            CustomTextView brief = (CustomTextView) v.findViewById(R.id.brief);

            title.setText(o.name);
            brief.setText(o.caption);
            Uri imageUri = Uri.parse(Utils.BASE_URL_BANNER_IMAGE + o.image);
            Log.i("ddd", "instantiateItem: int the list--->" + o.image);
            //  Log.i("image", "instantiateItem: "+(Utils.BASE_IMAGE_URL+o.image.toString()));
            image.setImageURI(imageUri);

            ((ViewPager) container).addView(v);

            return v;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            ((ViewPager) container).removeView((RelativeLayout) object);

        }

    }


    private void initComponent() {
        final List<Image> items = new ArrayList<>();

        Log.i(TAG, "initComponent: " + bannerData.size());
        for (int i = 0; i < bannerData.size(); i++) {
            Image obj = new Image();
            obj.image = bannerData.get(i).getImages();
            obj.name = bannerData.get(i).getTitle();
            obj.caption = bannerData.get(i).getDescription();
            items.add(obj);
        }
        adapterImageSlider.setItems(items);
        viewPager.setAdapter(adapterImageSlider);
        addBottomDots(layout_dots, adapterImageSlider.getCount(), 0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int pos, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int pos) {
                addBottomDots(layout_dots, adapterImageSlider.getCount(), pos);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        startAutoSlider(adapterImageSlider.getCount());
        rlEmptyviewAnnouncement.setVisibility(View.GONE);
        viewImageSlider.setVisibility(View.VISIBLE);

    }


    public class Image {
        public String name;
        public String caption;
        public String image;
        public Integer counter = null;

    }

    private void addBottomDots(LinearLayout layout_dots, int size, int current) {
        try {
            ImageView[] dots = new ImageView[size];

            layout_dots.removeAllViews();
            for (int i = 0; i < dots.length; i++) {
                dots[i] = new ImageView(getActivity());
                int width_height = 15;
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
                params.setMargins(10, 10, 10, 10);
                params.setLayoutDirection(0);
                dots[i].setLayoutParams(params);
                dots[i].setImageResource(R.drawable.shape_circle_outline_slider_image);
                layout_dots.addView(dots[i]);
            }

            if (dots.length > 0) {
                dots[current].setImageResource(R.drawable.shape_circle_slider_image);
            }
        } catch (Exception e) {
            Log.i(TAG, "addBottomDots: crash in addbottom -- >" + e.toString());
        }
    }

    private void startAutoSlider(final int count) {
        Log.i(TAG, "startAutoSlider: runnable--->" + runnable);
        runnable = new Runnable() {
            @Override
            public void run() {
                int pos = viewPager.getCurrentItem();
                pos = pos + 1;
                if (pos >= count) pos = 0;
                viewPager.setCurrentItem(pos);
                handler.postDelayed(runnable, 5000);
            }
        };
        handler.postDelayed(runnable, 5000);
    }

    /*Image Slider End*/


    String strCancelReason = "No comments";

    public void showChangeLangDialog() {
        AlertDialog b;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.alertdialog_edittext, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.edt_comment);

        dialogBuilder.setTitle(getString(R.string.upcomingjobs_cancel_jobs));
        dialogBuilder.setMessage("");

        dialogBuilder.setPositiveButton(getString(R.string.upcomingjobs_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do something with edt.getText().toString();
                strCancelReason = edt.getText().toString();
                //cancelUpcomingJobs();
                dialog.dismiss();
            }
        });
        dialogBuilder.setNegativeButton(getString(R.string.upcomingjobs_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
                dialog.dismiss();

            }
        });
        //AlertDialog b = dialogBuilder.create();
        b = dialogBuilder.create();
        b.show();
    }

    public void cancelSuccessStatus(String strBookid, String from, String msg) {

        AlertDialog.Builder builderc;
        builderc = new AlertDialog.Builder(getActivity());
        builderc.setCancelable(false);
        Typeface tfc = Typeface.createFromAsset(requireActivity().getAssets(),
                getString(R.string.App_Font));

        BaseActivity.CustomTFSpan tfSpanc = new BaseActivity.CustomTFSpan(tfc);

        SpannableString spannableStringc = new SpannableString(getResources().getString(R.string.cancel_booking));
        spannableStringc.setSpan(tfSpanc, 0, spannableStringc.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString spannableStringsc = new SpannableString(msg);
        spannableStringsc.setSpan(tfSpanc, 0, spannableStringsc.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        String lable;
        if (from.equals("alert")) {
            lable = getResources().getString(R.string.yes);
            builderc.setTitle(spannableStringc);
        } else {
            lable = getResources().getString(R.string.ok);
        }

        SpannableString spannableStringssc = new SpannableString(lable);
        spannableStringssc.setSpan(tfSpanc, 0, spannableStringssc.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        builderc.setMessage(spannableStringsc);

        builderc.setPositiveButton(spannableStringssc,
                (dialog, which) -> {
                    dialog.dismiss();
                    if (!from.equals("alert")) {
                        getOnGoingJobsCall();
                    } else {
                        cancelUpcomingJobs(strBookid);
                    }
                });

        if (from.equals("alert")) {
            SpannableString spannableStringssn = new SpannableString(getResources().getString(R.string.no));
            spannableStringssn.setSpan(tfSpanc, 0, spannableStringssn.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builderc.setNegativeButton(spannableStringssn, (dialog, which) -> {
                dialog.dismiss();
            });
        }

        if (!builderc.show().isShowing()) {
            builderc.show();
        } else {
            builderc.setOnDismissListener(DialogInterface::dismiss);
        }
    }

    private void cancelUpcomingJobs(String strBookid) {

        // lytProgressBar.setVisibility(View.VISIBLE);

        showProgressDialog(getActivity());

        SharedPreferences getspCurrency = requireActivity().getSharedPreferences(Utils.PREFS_NAME_CURRENCY, MODE_PRIVATE);
        //String strLanguage = getspCurrency.getString(Utils.SP_LANGUAGE, null);
        String strLanguageShortName = getspCurrency.getString(Utils.SP_LANGUAGE_SHORT_NAME, null);
        if (strLanguageShortName == null) {
            strLanguageShortName = "en";
        }

        userAccessToken = spCommon.getString(Utils.SP_USER_ACCESSTOKEN, null);
        HashMap<String, Object> map = new HashMap<>();
        map.put("booking_id", strBookid);
        map.put("reason", "Job Cancel");
        map.put("mode", "customer");
        map.put("language", strLanguageShortName);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<UpcomingCancelJobsModel> call2 = apiInterface.doCustomerUpcomingCancelJobs("Bearer " + userAccessToken, Utils.POST_ACCEPT_CONTENT_TYPE, map);

        call2.enqueue(new Callback<UpcomingCancelJobsModel>() {
            @Override
            public void onResponse(@NonNull Call<UpcomingCancelJobsModel> call, @NonNull retrofit2.Response<UpcomingCancelJobsModel> response) {
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    hideProgressDialog();
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Cancelled successfully.")) {
                        String getStatus = Objects.requireNonNull(response.body()).getMessage();
                        Log.i(TAG, "onResponse: cancelUpcomingJobs-->" + getStatus);
                        Log.i(TAG, "onResponse: cancelUpcomingJobs bookingid -->" + Objects.requireNonNull(response.body()).getBookingId());
                        if (getStatus.equals("Cancelled successfully.")) {
                            // lytProgressBar.setVisibility(View.GONE);
                            cancelSuccessStatus("", "cancel", getResources().getString(R.string.upcoming_job_cancelled));
                        } else {
                        }
                    } else {
                        // callRefreshToken("cancel");
                    }
                } else {
                    // callRefreshToken("cancel");
                }
            }

            @Override
            public void onFailure(@NonNull Call<UpcomingCancelJobsModel> call, @NonNull Throwable t) {
                Log.i("errror", "errr" + t);
                call.cancel();
                //lytProgressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), R.string.try_again, Toast.LENGTH_SHORT).show();
            }
        });

    }

}
