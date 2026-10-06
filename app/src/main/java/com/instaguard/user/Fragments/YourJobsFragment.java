package com.instaguard.user.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import com.facebook.ads.AdView;
import com.instaguard.user.Activities.BaseActivity;
import com.instaguard.user.Activities.MainActivity;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.instaguard.user.CustomViews.CustomTextView;
import com.instaguard.user.Helpers.Utils;
import com.instaguard.user.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class YourJobsFragment extends Fragment {

    View view;
    SharedPreferences SP_Prefs;
    String driverId;
    ImageButton imgbtnBack;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter adapter;

    AdView adViewFacebook;
    com.google.android.gms.ads.AdView adViewGoogle;

    LinearLayout adContainer;

    public static String strfromJob = "";

    public YourJobsFragment() {
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

    @SuppressLint("LongLogTag")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_yourjobs, container, false);

        SP_Prefs = getActivity().getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE);
        driverId = SP_Prefs.getString(Utils.SP_USERID, null);
        Log.e("userId~~RideHistoryFragment~~", driverId);

        initView(view);

        BaseActivity baseActivity = new BaseActivity();
        adContainer = view.findViewById(R.id.banner_container);
        baseActivity.callFacebookAdsCommon(requireActivity(), adViewFacebook, adContainer);

        adViewGoogle = view.findViewById(R.id.adView);
        baseActivity.callGoogleAdsCommon(requireActivity(), adViewGoogle);


        onBackButton(view);

        return view;

    }

    void initView(View view) {


        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        imgbtnBack = (ImageButton) view.findViewById(R.id.imgbtn_back);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        AppBarLayout layout_top = (AppBarLayout) view.findViewById(R.id.layout_top);

        wrapTabIndicatorToTitle(tabLayout, 30, 30);

        if (getArguments() != null) {
            if (Objects.requireNonNull(getArguments().getString("viascheduletrip")).equals("scheduletrip")) {
                System.out.print("called in the schedulet trip");
                selectPage(1);
            }

            if (Objects.equals(getArguments().getString("from"), "home")) {
                strfromJob = "home";
            } else {
                strfromJob = "trip";
            }
        }

        imgbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButtonFunction();
            }
        });
    }


    void selectPage(int pageIndex) {
        tabLayout.setScrollPosition(pageIndex, 0f, true);
        viewPager.setCurrentItem(pageIndex);
    }

    private void setupViewPager(ViewPager viewPager) {

        adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new YourJobsPastFragment(), getString(R.string.yourjobs_past));
        adapter.addFragment(new YourJobsUpcomingFragment(), getString(R.string.request));
        //viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(adapter);

    }


    @SuppressLint("ObsoleteSdkInt")
    private void setMargin(ViewGroup.MarginLayoutParams layoutParams, int start, int end) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            layoutParams.setMarginStart(start);
            layoutParams.setMarginEnd(end);
        } else {
            layoutParams.leftMargin = start;
            layoutParams.rightMargin = end;
        }
    }

    public void wrapTabIndicatorToTitle(TabLayout tabLayout, int externalMargin, int internalMargin) {
        View tabStrip = tabLayout.getChildAt(0);
        if (tabStrip instanceof ViewGroup) {
            ViewGroup tabStripGroup = (ViewGroup) tabStrip;
            int childCount = ((ViewGroup) tabStrip).getChildCount();
            for (int i = 0; i < childCount; i++) {
                View tabView = tabStripGroup.getChildAt(i);
                //set minimum width to 0 for instead for small texts, indicator is not wrapped as expected
                tabView.setMinimumWidth(0);
                // set padding to 0 for wrapping indicator as title
                tabView.setPadding(0, tabView.getPaddingTop(), 0, tabView.getPaddingBottom());
                // setting custom margin between tabs
                if (tabView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) tabView.getLayoutParams();
                    if (i == 0) {
                        // left
                        setMargin(layoutParams, externalMargin, internalMargin);
                    } else if (i == childCount - 1) {
                        // right
                        setMargin(layoutParams, internalMargin, externalMargin);
                    } else {
                        // internal
                        setMargin(layoutParams, internalMargin, internalMargin);
                    }
                }
            }

            tabLayout.requestLayout();
        }
    }


    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private int noOfTabs;

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();


        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            this.noOfTabs = noOfTabs;
        }


        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
            // return noOfTabs;
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);

        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onDestroy() {
        if (adViewFacebook != null) {
            adViewFacebook.destroy();
        }
        if (adViewGoogle != null) {
            adViewGoogle.destroy();
        }
        super.onDestroy();
        Log.e("TAG", "onDestroy: ~~~~~~~~~~~~~~~~~~~");
    }

    @Override
    public void onDestroyView() {
        if (adViewFacebook != null) {
            adViewFacebook.destroy();
        }
        if (adViewGoogle != null) {
            adViewGoogle.destroy();
        }
        super.onDestroyView();
        Log.e("TAG", "onDestroyView: ~~~~~~~~~~~~~~~~~~~~~~~~~~");
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
        /*if (getArguments() != null) {
            if (Objects.equals(getArguments().getString("from"), "home")) {
                removeFragment(Utils.FRAGMENT_YOURJOBS);
            } else {*/
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        requireActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        requireActivity().finish();
           /* }
        } else {
            removeFragment(Utils.FRAGMENT_YOURJOBS);
        }*/
    }

    public void removeFragment(String Tag) {
        Utils.CURRENT_TAG = null;
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(Objects.requireNonNull(fragmentManager.findFragmentByTag(Tag))).commit();
    }

}
