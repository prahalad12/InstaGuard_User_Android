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
import androidx.recyclerview.widget.GridLayoutManager;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.ads.AdView;
import com.instaguard.user.Activities.BaseActivity;
import com.instaguard.user.Activities.HomeActivity;
import com.instaguard.user.Adapter.MapCategorySubAdapter;
import com.instaguard.user.CustomViews.CustomButton;
import com.instaguard.user.CustomViews.CustomTextView;
import com.instaguard.user.Helpers.DecimalUtils;
import com.instaguard.user.Helpers.RecyclerViewsTools.RecyclerTouchListener;
import com.instaguard.user.Helpers.Retrofit.APIClient;
import com.instaguard.user.Helpers.Retrofit.APIInterface;
import com.instaguard.user.Helpers.Utils;
import com.instaguard.user.Models.Banner.BannerData;
import com.instaguard.user.Models.Banner.BannerModel;
import com.instaguard.user.Models.CategorySubModel;
import com.instaguard.user.Models.RefreshTokenModel;
import com.instaguard.user.Models.VehicleSubCategoryData;
import com.instaguard.user.Models.VehicleSubCategoryModel;
import com.instaguard.user.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

public class SubCategoryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private View view;
    private SharedPreferences getspCommonState, spRequest, spCommon;
    private SharedPreferences.Editor getUpdateProfile, getRequestState;

    private String userAccessToken, userId, categoryId, levelAmount, levelType,
            categoryType, categoryName, strDistance, strCurrency, strLanguageShortName,
            strLanguage, strCurrencySymbol, categoryCount, categoryDays;
    private ImageButton imgbtnBack;

    private CustomButton btnBookNow, btnBookLater;
    private CustomTextView txtTitle;

    private ImageView ivRefresh;
    private SwipeRefreshLayout swipeRefresh;

    /* int n = 39;//Total Category
     String[] strCategoryName = new String[n];
     String[] strCategoryId = new String[n];
     String[] strCategoryImage = new String[n];
     String[] strPriceKm = new String[n];
     String[] strMinFare = new String[n];*/
    private ArrayList<CategorySubModel> categoryModelArray;
    private CategorySubModel listFeed;
    private RecyclerView rvSubCategory;
    private RadioGroup rgCategory;
    private String getSubcatName, getSubcatId, getSubcatMainId, getSubcatPrice;
    private MapCategorySubAdapter mapCategorySubAdapter;

    Double getSubcatAgentPrice, getSubcatDayPrice;
    SharedPreferences sprefs;
    SharedPreferences.Editor putSpState;

    /*Image Slider Start*/
    private View parent_view;
    private ViewPager viewPager;
    private LinearLayout layout_dots;
    private AdapterImageSlider adapterImageSlider;
    private Runnable runnable = null;
    private Handler handler = new Handler();
    private View progresView, viewImageSlider;
    private RecyclerView.LayoutManager layoutManager;
    private List<BannerData> bannerData = new ArrayList<>();
    private RelativeLayout rlEmptyviewAnnouncement, rlEmptyView;
    /*Image Slider End*/

    private RelativeLayout rlLevelEstAmount, rlFareEstAmount;
    private CustomTextView tvEstBaseFareAmt, tvVisitFareLevel, tvVisitFare, tvEstPricePerKmAmt, tvBasefareSublevelCatLabel, tvBasefareMainlevelCatLabel, tvBasefareSublevelCat, tvBasefareMainlevelCat, tvEstDistanceCal, tvEstDistance, tvEstlevelAmt, tvEstLevelAmtLabel, tvEstFareAmtLabel, tvEstFareAmt;
    private TableRow trVisitFareLevel, trVisitFare;
    private int numberOfColumns = 2;

    AdView adViewGoogle;

    public SubCategoryFragment() {
        // Required empty public constructor
    }


    @SuppressLint("LongLogTag")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryId = getArguments().getString(Utils.B_CATEGORY_ID);
            categoryName = getArguments().getString(Utils.B_CATEGORY_NAME);
            categoryType = getArguments().getString(Utils.B_CATEGORY_TYPE);
            categoryCount = getArguments().getString(Utils.B_CATEGORY_GUARDS_COUNT);
            categoryDays = getArguments().getString(Utils.B_CATEGORY_DAYS);


        }
    }

    @SuppressLint({"LongLogTag", "CommitPrefEdits"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sub_category, container, false);

        getUpdateProfile = requireActivity().getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE).edit();

        getRequestState = requireActivity().getSharedPreferences(Utils.MY_PREFS_REQUEST_STATE, MODE_PRIVATE).edit();


        getspCommonState = requireActivity().getSharedPreferences(Utils.PREFS_NAME_CURRENCY, MODE_PRIVATE);


        spRequest = requireActivity().getSharedPreferences(Utils.MY_PREFS_REQUEST_STATE, MODE_PRIVATE);

        spCommon = requireActivity().getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE);
        userId = spCommon.getString(Utils.SP_USERID, null);
        userAccessToken = spCommon.getString(Utils.SP_USER_ACCESSTOKEN, null);

        strDistance = spRequest.getString(Utils.B_DISTANCE, null);
        levelType = spRequest.getString(Utils.B_CATEGORY_LEVEL_TYPE, null);
        levelAmount = spRequest.getString(Utils.B_CATEGORY_LEVEL_AMOUNT, null);

        strLanguage = getspCommonState.getString(Utils.SP_LANGUAGE, null);
        strLanguageShortName = getspCommonState.getString(Utils.SP_LANGUAGE_SHORT_NAME, null);
        strCurrency = getspCommonState.getString(Utils.SP_CURRENCY, null);
        strCurrencySymbol = getspCommonState.getString(Utils.SP_CURRENCY_SYMBOL, null);

        if (strLanguageShortName == null) {
            strLanguageShortName = "en";
        }

        // levelType = getArguments().getString(Utils.B_CATEGORY_LEVEL_TYPE);
        // levelAmount = getArguments().getString(Utils.B_CATEGORY_LEVEL_AMOUNT);

        Log.e("SubCategoryFragment~~~", categoryId + "~~" + categoryName + "~~~" + categoryType + "~~~" + levelType + "~~~" + levelAmount);

        Log.e("strDistance~~SubCategoryFragment~~", strDistance);

        Log.e("userId~~SubCategoryFragment~~", userId);

        // rvMain.setLayoutManager(layoutManager);
        callSlideBannerImage("7");

        initView(view);

        BaseActivity baseActivity = new BaseActivity();
        adViewGoogle = view.findViewById(R.id.adView);
        baseActivity.callGoogleAdsCommon(requireActivity(), adViewGoogle);

        onBackButton(view);

        return view;

    }

    void initView(View view) {

        imgbtnBack = (ImageButton) view.findViewById(R.id.imgbtn_back);
        btnBookNow = (CustomButton) view.findViewById(R.id.btn_booknow);
        btnBookLater = (CustomButton) view.findViewById(R.id.btn_booklater);
        rgCategory = (RadioGroup) view.findViewById(R.id.rg_selcet_category);
        rvSubCategory = (RecyclerView) view.findViewById(R.id.rcv_sub_category);
        rvSubCategory.setVisibility(View.VISIBLE);


        layout_dots = (LinearLayout) view.findViewById(R.id.layout_dots);
        viewPager = (ViewPager) view.findViewById(R.id.pager);
        viewPager.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        rlEmptyviewAnnouncement = (RelativeLayout) view.findViewById(R.id.rl_emptyview_announcement);
        viewImageSlider = (View) view.findViewById(R.id.view_image_slider);

        txtTitle = (CustomTextView) view.findViewById(R.id.textView);
        ivRefresh = view.findViewById(R.id.iv_menu_refresh);
        swipeRefresh = view.findViewById(R.id.swipe_refresh_layout_sub_category);
        swipeRefresh.setOnRefreshListener(this);

        rlFareEstAmount = view.findViewById(R.id.rl_fare_est_amount);
        tvEstFareAmtLabel = view.findViewById(R.id.tv_est_fare_label);
        tvEstFareAmt = view.findViewById(R.id.tv_fare_amount);

        rlLevelEstAmount = view.findViewById(R.id.rl_level_est_amount);
        tvEstLevelAmtLabel = view.findViewById(R.id.tv_est_level_label);
        tvEstlevelAmt = view.findViewById(R.id.tv_level_amount);

        tvEstBaseFareAmt = view.findViewById(R.id.tv_est_fare_base_fare_label);
        tvEstPricePerKmAmt = view.findViewById(R.id.tv_est_fare_price_per_km_label);
        tvEstDistance = view.findViewById(R.id.tv_est_fare_distance_label);
        tvEstDistanceCal = view.findViewById(R.id.tv_est_fare_distance_cal_label);

        tvBasefareMainlevelCat = view.findViewById(R.id.tv_basefare_main_level_amount);
        tvBasefareSublevelCat = view.findViewById(R.id.tv_basefare_sub_level_amount);

        tvBasefareMainlevelCatLabel = view.findViewById(R.id.tv_basefare_main_level_label);
        tvBasefareSublevelCatLabel = view.findViewById(R.id.tv_basefare_sub_level_label);

        tvVisitFare = view.findViewById(R.id.tv_est_fare_visit_fare_label);
        tvVisitFareLevel = view.findViewById(R.id.tv_visitfare_sub_level_amount);

        trVisitFare = view.findViewById(R.id.tr_visit_fare);
        trVisitFareLevel = view.findViewById(R.id.tr_visit_fare_level);

        //viewPager.setLayoutMode(View.LAYOUT_DIRECTION_LTR);
        //viewPager.setRotationY(180);
        // viewPager.setRtlOriented(false);
        Log.i("SubCategoryFragment", "onCreateView: viewpager rotation--->" + viewPager.getRotationY());
        adapterImageSlider = new AdapterImageSlider(getActivity(), new ArrayList<Image>());

        layoutManager = new LinearLayoutManager(getActivity());

        if (categoryName != null) {
            txtTitle.setText(categoryName);
        }


        onClick();

        if (BaseActivity.isOnline(requireActivity())) {
            getOnlineSubCategoryInfo();
        } else {
            ivRefresh.setVisibility(View.VISIBLE);
        }

    }



    @SuppressLint("UseCompatLoadingForDrawables")
    private void onClick() {

        imgbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButtonFunction();
            }
        });

        ivRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOnlineSubCategoryInfo();
            }
        });

        btnBookNow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.e("TAGonClick:", "onClick: btnBookNow " + getSubcatName);
                if (getSubcatName != null) {

                    btnBookNow.setBackground(getResources().getDrawable(R.drawable.bg_rect_filled_booknowlater_select_cp));
                    btnBookNow.setTextColor(getResources().getColor(R.color.white));
                    // mapCategorySubAdapter.selected(-2);

                    Bundle args = new Bundle();
                    args.putString(Utils.B_CATEGORY_ID, getSubcatId);
                    args.putString(Utils.B_CATEGORY_NAME, getSubcatName);
                    args.putString(Utils.B_REQUEST_TYPE, "normaltrip");
                    args.putString(Utils.B_CATEGORY_GUARDS_COUNT, categoryCount);
                    args.putString(Utils.B_CATEGORY_DAYS, categoryDays);
                    args.putString(Utils.B_CATEGORY_AGENT_PRICE, getSubcatAgentPrice.toString());
                    args.putString(Utils.B_CATEGORY_DAYS_PRICE, getSubcatDayPrice.toString());
                    getRequestState.putString(Utils.B_TRIP_TYPE, "normaltrip");
                    getRequestState.apply();

                    Utils.switchContent(R.id.frame_sub_category,

                            Utils.FRAGMENT_PROVIDERLIST,
                            requireActivity(), args,
                            Utils.AnimationType.SLIDE_IN_SLIDE_OUT);
                } else {
                    Toast.makeText(getActivity(), R.string.toast_select_service, Toast.LENGTH_SHORT).show();
                    btnBookNow.setBackground(getResources().getDrawable(R.drawable.bg_rect_filled_booknowlater_unselect));
                    btnBookNow.setTextColor(getResources().getColor(R.color.black));
                }
            }
        });
        btnBookLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSubcatName != null) {

                    btnBookLater.setBackground(getResources().getDrawable(R.drawable.bg_rect_filled_booknowlater_select_cp));
                    btnBookLater.setTextColor(getResources().getColor(R.color.white));

                    // mapCategorySubAdapter.selected(-2);

                    Bundle args = new Bundle();
                    args.putString(Utils.B_CATEGORY_ID, getSubcatId);
                    args.putString(Utils.B_CATEGORY_NAME, getSubcatName);
                    args.putString(Utils.B_REQUEST_TYPE, "scheduletrip");
                    args.putString(Utils.B_CATEGORY_GUARDS_COUNT, categoryCount);
                    args.putString(Utils.B_CATEGORY_DAYS, categoryDays);
                    args.putString(Utils.B_CATEGORY_AGENT_PRICE, getSubcatAgentPrice.toString());
                    args.putString(Utils.B_CATEGORY_DAYS_PRICE, getSubcatDayPrice.toString());
                    getRequestState.putString(Utils.B_TRIP_TYPE, "scheduletrip");
                    getRequestState.apply();

                    Utils.switchContent(R.id.frame_sub_category,
                            Utils.FRAGMENT_SCHEDULETRIP,
                            requireActivity(), args,
                            Utils.AnimationType.SLIDE_IN_SLIDE_OUT);
                } else {
                    Toast.makeText(getActivity(), R.string.toast_select_service, Toast.LENGTH_SHORT).show();
                    btnBookLater.setBackground(getResources().getDrawable(R.drawable.bg_rect_filled_booknowlater_unselect));
                    btnBookLater.setTextColor(getResources().getColor(R.color.black));
                }
            }
        });

        categoryModelArray = new ArrayList<CategorySubModel>();
        mapCategorySubAdapter = new MapCategorySubAdapter(categoryModelArray, getActivity(), categoryCount, categoryDays);

        // LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        // linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        // rvSubCategory.setLayoutManager(linearLayoutManager);

        rvSubCategory.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvSubCategory.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));

        rvSubCategory.setAdapter(mapCategorySubAdapter);

        rvSubCategory.addOnItemTouchListener(
                new RecyclerTouchListener(getActivity(), rvSubCategory,
                        new RecyclerTouchListener.OnTouchActionListener() {
                            @SuppressLint("LongLogTag")
                            @Override
                            public void onClick(View view, int position) {
                                Log.i("rvSubCategory", "onClick: rvSubCategory-> ");
                                if (position != -1) {
                                    //setProgresscount(position);
                                    listFeed = categoryModelArray.get(position);
                                    getSubcatName = listFeed.getSubCategoryName();
                                    getSubcatId = listFeed.getSubCategoryId();
                                    getSubcatMainId = listFeed.getSubMainCategoryId();
                                    getSubcatPrice = listFeed.getSubCategoryBasefare();
                                    getSubcatAgentPrice = listFeed.getSubCategoryAgentPrice();
                                    getSubcatDayPrice = listFeed.getSubCategoryDayPrice();

                                    Log.e("Category_getCategoryName~~", getSubcatName);
                                    Log.e("Category_getId~~", getSubcatId);
                                    Log.e("Category_getMainId~~", getSubcatMainId);
                                    Log.e("Category_getSubcatAgentPrice~~", getSubcatAgentPrice.toString());
                                    Log.e("Category_getSubcatDayPrice~~", getSubcatDayPrice.toString());

                                    mapCategorySubAdapter.selected(position);

                                    getRequestState.putString(Utils.B_SUB_CATEGORY_ID, listFeed.getSubCategoryId());
                                    getRequestState.putString(Utils.B_SUB_CATEGORY_NAME, listFeed.getSubCategoryEnName());
                                    getRequestState.putString(Utils.B_SUB_CATEGORY_NAME_AR, listFeed.getSubCategoryArName());
                                    getRequestState.putString(Utils.B_SUB_CATEGORY_PRICE, listFeed.getSubCategoryBasefare());
                                    getRequestState.putString(Utils.B_CATEGORY_AGENT_PRICE, getSubcatAgentPrice.toString());
                                    getRequestState.putString(Utils.B_CATEGORY_DAYS_PRICE, getSubcatDayPrice.toString());
                                    // getRequestState.putString(Utils.B_SUB_CATEGORY_PRICE,listFeed.getSubCategoryPricePerKm());
                                    getRequestState.apply();

                                    btnBookNow.setBackground(getResources().getDrawable(R.drawable.bg_rect_filled_booknowlater_select_cp));
                                    btnBookLater.setBackground(getResources().getDrawable(R.drawable.bg_rect_filled_booknowlater_select_cp));
                                    btnBookNow.setTextColor(getResources().getColor(R.color.white));
                                    btnBookLater.setTextColor(getResources().getColor(R.color.white));
                                    // callEstFare(listFeed.getSubCategoryBasefare(),listFeed.getSubCategoryPricePerKm(),listFeed.getSubCategoryName(),listFeed.getSubCategoryVistiPrice());
                                }

                            }

                            @Override
                            public void onRightSwipe(View view, int position) {
                            }

                            @Override
                            public void onLeftSwipe(View view, int position) {
                            }
                        }));
    }

    @SuppressLint("SetTextI18n")
    private void callEstFare(String subCategoryBasefare, String subCategoryPricePerKm, String subCategoryName, String subCategoryVistiPrice) {

        if (levelType.equals("1")) {
            rlLevelEstAmount.setVisibility(View.VISIBLE);

            DecimalFormat f1 = new DecimalFormat("##.##");
            double fares = ((Double.parseDouble(levelAmount) * Double.parseDouble(subCategoryBasefare)) + Double.parseDouble(subCategoryVistiPrice));
            Log.e("fare~~", "callEstFare: " + f1.format(fares));

            tvBasefareMainlevelCatLabel.setText(getResources().getString(R.string.base_fare) + "(" + categoryName + ")");
            tvBasefareSublevelCatLabel.setText(getResources().getString(R.string.base_fare) + "(" + subCategoryName + ")");
            tvBasefareMainlevelCat.setText(strCurrencySymbol + "" + convertCurrencyPrice(levelAmount));
            tvBasefareSublevelCat.setText(strCurrencySymbol + "" + convertCurrencyPrice(subCategoryBasefare));

            if (!subCategoryVistiPrice.equals("0")) {
                trVisitFareLevel.setVisibility(View.VISIBLE);
                tvVisitFareLevel.setText(strCurrencySymbol + "" + convertCurrencyPrice(subCategoryVistiPrice));
                tvEstlevelAmt.setText("(" + categoryName + " * " + subCategoryName + ") +" + getResources().getString(R.string.visit_fare_vf_short) + " =" + strCurrencySymbol + "" +/*f1.format(*/convertCurrencyPrice(String.valueOf(fares)))/*)*/;
            } else {
                trVisitFareLevel.setVisibility(View.GONE);
                tvEstlevelAmt.setText(categoryName + "*" + subCategoryName + " =" + strCurrencySymbol + "" +/*f1.format(String.valueOf(*/convertCurrencyPrice(String.valueOf(fares)))/*))*/;
            }

            if (categoryType.equals("1")) {
                rlFareEstAmount.setVisibility(View.VISIBLE);
                DecimalFormat f = new DecimalFormat("##.##");
                double fareD = (Double.parseDouble(strDistance) * Double.parseDouble(subCategoryPricePerKm));
                double fare = ((Double.parseDouble(strDistance) * Double.parseDouble(subCategoryPricePerKm)) + Double.parseDouble(subCategoryBasefare) + Double.parseDouble(subCategoryVistiPrice));
                Log.e("fare~~", "callEstFare: " + f.format(fare));

                tvEstBaseFareAmt.setText(strCurrencySymbol + "" + convertCurrencyPrice(subCategoryBasefare));
                tvEstPricePerKmAmt.setText(strCurrencySymbol + "" + convertCurrencyPrice(subCategoryPricePerKm));
                tvEstDistance.setText(strDistance + getResources().getString(R.string.km));

                if (!subCategoryVistiPrice.equals("0")) {

                    trVisitFare.setVisibility(View.VISIBLE);
                    tvVisitFare.setText(strCurrencySymbol + "" + convertCurrencyPrice(subCategoryVistiPrice));

                    tvEstFareAmt.setText(getResources().getString(R.string.sub_category_short_form_bf_vf_edc) + strCurrencySymbol + "" +/*f1.format(String.valueOf(*/convertCurrencyPrice(String.valueOf(fare)))/*))*/;

                } else {
                    trVisitFare.setVisibility(View.GONE);
                    tvEstFareAmt.setText(getResources().getString(R.string.sub_category_short_form_bf_edc) + strCurrencySymbol + "" +/*f1.format(String.valueOf(*/convertCurrencyPrice(String.valueOf(fare)))/*))*/;

                }

                tvEstDistanceCal.setText(getResources().getString(R.string.sub_category_short_form_ppkm_ed) + "" + strCurrencySymbol + "" +/*f.format(String.valueOf(*/convertCurrencyPrice(String.valueOf(fareD)))/*))*/;
                // tvEstDistanceCal.setText(getResources().getString(R.string.sub_category_short_form_ppkm_ed)+f.format(fareD)+getResources().getString(R.string.km));

                getRequestState.putString(Utils.B_TOTAL_EST_AMT, String.valueOf(f.format(fare)));
                getRequestState.apply();

            } else {
                rlFareEstAmount.setVisibility(View.GONE);

                getRequestState.putString(Utils.B_TOTAL_EST_AMT, "0");
                getRequestState.apply();

            }
        } else {

            rlLevelEstAmount.setVisibility(View.GONE);

            if (categoryType.equals("1")) {
                rlFareEstAmount.setVisibility(View.VISIBLE);
                DecimalFormat f = new DecimalFormat("##.##");
                double fareD = (Double.parseDouble(strDistance) * Double.parseDouble(subCategoryPricePerKm));
                double fare = ((Double.parseDouble(strDistance) * Double.parseDouble(subCategoryPricePerKm)) + Double.parseDouble(subCategoryBasefare) + Double.parseDouble(subCategoryVistiPrice));
                Log.e("fare~~", "callEstFare: " + f.format(fare));

                tvEstBaseFareAmt.setText(strCurrencySymbol + "" + convertCurrencyPrice(subCategoryBasefare));
                tvEstPricePerKmAmt.setText(strCurrencySymbol + "" + convertCurrencyPrice(subCategoryPricePerKm));
                tvEstDistance.setText(strDistance + getResources().getString(R.string.km));

                if (!subCategoryVistiPrice.equals("0")) {

                    trVisitFare.setVisibility(View.VISIBLE);
                    tvVisitFare.setText(strCurrencySymbol + "" + convertCurrencyPrice(subCategoryVistiPrice));

                    tvEstFareAmt.setText(getResources().getString(R.string.sub_category_short_form_bf_vf_edc) + strCurrencySymbol + "" +/*f.format(String.valueOf(*/convertCurrencyPrice(String.valueOf(fare)))/*))*/;

                } else {
                    trVisitFare.setVisibility(View.GONE);
                    tvEstFareAmt.setText(getResources().getString(R.string.sub_category_short_form_bf_edc) + strCurrencySymbol + "" +/*f.format(String.valueOf(*/convertCurrencyPrice(String.valueOf(fare)))/*))*/;

                }

                tvEstDistanceCal.setText(getResources().getString(R.string.sub_category_short_form_ppkm_ed) + "" + strCurrencySymbol + "" +/*f.format(String.valueOf(*/convertCurrencyPrice(String.valueOf(fareD)))/*))*/;
                //   tvEstDistanceCal.setText(getResources().getString(R.string.sub_category_short_form_ppkm_ed)+f.format(fareD)+getResources().getString(R.string.km));

                getRequestState.putString(Utils.B_TOTAL_EST_AMT, String.valueOf(f.format(fare)));
                getRequestState.apply();
            } else {
                rlFareEstAmount.setVisibility(View.GONE);

                getRequestState.putString(Utils.B_TOTAL_EST_AMT, "0");
                getRequestState.apply();

            }
        }


    }

/*
    private String addNumbers() {
        int number1,number2,number3,number4;

     */
/*   if(!txtCurrentCharge.getText().toString().equals("") && txtCurrentCharge.getText().length() > 0) {
            number4 = (int) Math.round(Double.parseDouble(txtCurrentCharge.getText().toString()));
            Log.e("number4~~", String.valueOf(number3));
        } else {
            number4 = 0;
        }*//*


        return Integer.toString( Integer.valueOf(strDistance) * Integer.valueOf(subCategoryBasefare));
    }
*/


    private void getOnlineSubCategoryInfo() {

        userAccessToken = spCommon.getString(Utils.SP_USER_ACCESSTOKEN, null);

        if (categoryModelArray != null)
            categoryModelArray.clear();
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", categoryId);
        map.put("language", strLanguageShortName);

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<VehicleSubCategoryModel> call2 = apiInterface.doGetVehicleSubcategoryDetails("Bearer " + userAccessToken, Utils.POST_ACCEPT_CONTENT_TYPE, map);
        showProgressDialog(getActivity());
        call2.enqueue(new Callback<VehicleSubCategoryModel>() {
            @Override
            public void onResponse(@NonNull Call<VehicleSubCategoryModel> call, @NonNull retrofit2.Response<VehicleSubCategoryModel> response) {
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    hideProgressDialog();
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("categories services listed successfully.")) {

                        List<VehicleSubCategoryData> getve = Objects.requireNonNull(response.body()).getVehicleSubCategoryData();
                        String[] items = new String[getve.size()];
                        Log.i("itemsitems", "" + Arrays.toString(items));
                        for (int i = 0; i < getve.size(); i++) {
                            CategorySubModel categoryModel = new CategorySubModel();

                            if (strLanguageShortName.equals("ar")) {
                                categoryModel.setSubCategoryName(getve.get(i).getServiceNameSpanish());
                            } else {
                                categoryModel.setSubCategoryName(getve.get(i).getServiceName());
                            }
                            categoryModel.setSubCategoryEnName(getve.get(i).getServiceName());
                            categoryModel.setSubCategoryArName(getve.get(i).getServiceNameSpanish());
                            categoryModel.setSubCategoryId(String.valueOf(getve.get(i).getId()));
                            categoryModel.setSubCategoryImage(getve.get(i).getServiceImage());
                            categoryModel.setSubCategoryBasefare(String.valueOf(getve.get(i).getBaseFare()));
                            categoryModel.setSubCategoryAdminComm(String.valueOf(getve.get(i).getAdminCommission()));
                            categoryModel.setSubMainCategoryId(String.valueOf(getve.get(i).getCategoryId()));
                            categoryModel.setSubCategoryPricePerKm(String.valueOf(getve.get(i).getPricePerKm()));
                            categoryModel.setSubCategoryVistiPrice(String.valueOf(getve.get(i).getVisitFare()));
                            categoryModel.setSubCategoryAgentPrice(getve.get(i).getPricePerAgent());
                            categoryModel.setSubCategoryDayPrice(getve.get(i).getPricePerDay());

                            if (i == 0)
                                categoryModel.setIsSelected(true);
                            else
                                categoryModel.setIsSelected(false);
                            categoryModelArray.add(categoryModel);
                            items[i] = String.valueOf(getve.get(i));

                        }

                        ivRefresh.setVisibility(View.GONE);
                    } else if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("No records Found.")) {
                        Toast.makeText(getActivity(), R.string.no_record_found, Toast.LENGTH_SHORT).show();
                        ivRefresh.setVisibility(View.VISIBLE);
                    } else {
                        callRefreshToken("Category");
                    }
                    mapCategorySubAdapter.selected(-2);
                    mapCategorySubAdapter.notifyDataSetChanged();
                } else if (response.code() == RESPONSECODE_401) {
                    callRefreshToken("Category");
                    ivRefresh.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<VehicleSubCategoryModel> call, Throwable t) {
                Log.i("errror", "errr" + t);
                call.cancel();
                ivRefresh.setVisibility(View.VISIBLE);
            }
        });

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
                Toast.makeText(getActivity(), R.string.try_again, Toast.LENGTH_SHORT).show();
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

    private void initComponent() {
        final List<Image> items = new ArrayList<>();

        Log.i("fgds", "initComponent: " + bannerData.size());
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
            Log.i("dgssdf", "addBottomDots: crash in addbottom -- >" + e.toString());
        }
    }

    private void startAutoSlider(final int count) {
        Log.i("dfsf", "startAutoSlider: runnable--->" + runnable);
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

    @Override
    public void onRefresh() {

        swipeRefresh.setRefreshing(false);
        getOnlineSubCategoryInfo();
        callSlideBannerImage("7");
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
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final Image o = items.get(position);
            LayoutInflater inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.item_slider_image_main, container, false);

            SimpleDraweeView image = (SimpleDraweeView) v.findViewById(R.id.image);
            CustomTextView title = (CustomTextView) v.findViewById(R.id.title);
            CustomTextView brief = (CustomTextView) v.findViewById(R.id.brief);

            title.setText(o.name);
            brief.setText(o.caption);

            //  Log.i("image", "instantiateItem: "+o.image);
            Uri imageUri = Uri.parse(Utils.BASE_URL_BANNER_IMAGE + o.image);
            Log.i("ddd", "instantiateItem: int the list--->" + o.image);
            //  Log.i("image", "instantiateItem: "+(Utils.BASE_IMAGE_URL+o.image.toString()));
            image.setImageURI(imageUri);

            ((ViewPager) container).addView(v);

            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((RelativeLayout) object);

        }

    }

    public class Image {
        public String name;
        public String caption;
        public String image;
        public Integer counter = null;

    }
    /*Image Slider End*/

    @Override
    public void onDestroy() {
        if (runnable != null) handler.removeCallbacks(runnable);
        if (adViewGoogle != null) {
            adViewGoogle.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        if (runnable != null) handler.removeCallbacks(runnable);
        if (adViewGoogle != null) {
            adViewGoogle.destroy();
        }
        super.onDestroyView();
    }


    private void callRefreshToken(String logout) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", userId);
        map.put("token", userAccessToken);
        Log.e("Logout~~Refresh~~~", "Refresh~~~" + map);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<RefreshTokenModel> call2 = apiInterface.doGetRefreshToken(Utils.POST_ACCEPT_CONTENT_TYPE, Utils.POST_ACCEPT_CONTENT_TYPE, map);
        call2.enqueue(new Callback<RefreshTokenModel>() {
            @Override
            public void onResponse(@NonNull Call<RefreshTokenModel> call, @NonNull Response<RefreshTokenModel> response) {
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    String refreshToken = Objects.requireNonNull(response.body()).getAccessToken();
                    String refreshTokenType = Objects.requireNonNull(response.body()).getTokenType();
                    String refreshTokenTimeExp = String.valueOf(Objects.requireNonNull(response.body()).getExpiresIn());
                    savesharedpreference(refreshToken, refreshTokenType, refreshTokenTimeExp);
                    if (logout.equalsIgnoreCase("Category")) {
                        getOnlineSubCategoryInfo();
                    }
                } else if (response.code() == RESPONSECODE_401) {
                    showTimeOut();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RefreshTokenModel> call, @NonNull Throwable t) {
                call.cancel();
            }
        });
    }

    private void savesharedpreference(String access_token, String token_type, String exp_time) {

        getUpdateProfile.putString(Utils.SP_USER_ACCESSTOKEN, access_token);
        getUpdateProfile.putString(Utils.SP_USER_TOKENTYPE, token_type);
        getUpdateProfile.putString(Utils.SP_USER_TOKENEXPTIME, exp_time);
        getUpdateProfile.apply();
    }

    public void showTimeOut() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),
                getString(R.string.App_Font));

        BaseActivity.CustomTFSpan tfSpan = new BaseActivity.CustomTFSpan(tf);
        SpannableString spannableString = new SpannableString(getString(R.string.timeout_title));
        spannableString.setSpan(tfSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString spannableStrings = new SpannableString(getString(R.string.your_session_time_out_content));
        spannableStrings.setSpan(tfSpan, 0, spannableStrings.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString spannableStringss = new SpannableString(getString(R.string.ok));
        spannableStringss.setSpan(tfSpan, 0, spannableStringss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        builder.setMessage(spannableStrings);
        builder.setPositiveButton(spannableStringss,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearSPmodePrivate();
                    }
                });

        builder.show();
    }

    private void clearSPmodePrivate() {
        SharedPreferences preferences = requireActivity().getSharedPreferences(Utils.MY_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        // deleteCache(MainActivity.this);
        Intent logout = new Intent(getActivity(), HomeActivity.class);
        logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(logout);
        getActivity().finish();
    }

    public void onBackButton(View view) {
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP
                        && keyCode == KeyEvent.KEYCODE_BACK) {
                    backButtonFunction();
                }
                return true;
            }
        });
    }

    public void backButtonFunction() {
        //  MainActivity.frameClickable(false);
        try {
            mapCategorySubAdapter.selected(-2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        removeFragment(Utils.FRAGMENT_SUBCATEGORY);
    }

    public void removeFragment(String Tag) {
        Utils.CURRENT_TAG = null;
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag(Tag)).commit();
    }

    public String convertCurrencyPrice(String price) {
        String currencyCommonPrice = getspCommonState.getString(Utils.SP_CURRENCY_COMMON_VALUES, "1");
        Double multiplePrice = Double.parseDouble(currencyCommonPrice) * Double.parseDouble(price);
        double roundedNumber = DecimalUtils.round(multiplePrice, 2);
        return String.valueOf(roundedNumber);
    }

}
