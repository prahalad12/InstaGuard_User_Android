package com.instaguard.user.Fragments;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
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

import com.instaguard.user.CustomViews.CustomTextView;
import com.instaguard.user.Helpers.Utils;
import com.instaguard.user.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class YourWalletTransactionFragment extends Fragment {

    View view;
    SharedPreferences SP_Prefs;
    String driverId;
    ImageButton imgbtnBack;
    TabLayout tabLayout;
    ViewPager viewPager;
    CustomTextView txtheader;

    public YourWalletTransactionFragment() {
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_yourjobs, container, false);

        SP_Prefs = Objects.requireNonNull(getActivity()).getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE);
        driverId = SP_Prefs.getString(Utils.SP_USERID, null);
        Log.e("userId~~RideHistoryFragment~~", driverId);

        initView(view);

        onBackButton(view);

        return view;

    }

    void initView(View view) {


        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        imgbtnBack = (ImageButton) view.findViewById(R.id.imgbtn_back);
        txtheader = (CustomTextView) view.findViewById(R.id.txt_signin);
        txtheader.setText(getString(R.string.your_transaction_header));
        setupViewPager(viewPager);


        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        AppBarLayout layout_top = (AppBarLayout) view.findViewById(R.id.layout_top);

        wrapTabIndicatorToTitle(tabLayout, 30, 30);

        if(getArguments()!=null){
            if(getArguments().getString("viascheduletrip").equals("scheduletrip")) {
                System.out.printf("called in the schedulet trip");
               // selectPage(1);
            }
        }
        imgbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButtonFunction();
            }
        });

    }
    void selectPage(int pageIndex){
        tabLayout.setScrollPosition(pageIndex,0f,true);
        viewPager.setCurrentItem(pageIndex);
    }
    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new YourWalletTransAllFragment(), getString(R.string.yourwalletTransaction_all));
        adapter.addFragment(new YourWalletTransMoneyInFragment(), getString(R.string.yourwalletTransaction_money_in));
        adapter.addFragment(new YourWalletTransMoneyOutFragment(), getString(R.string.yourwalletTransaction_money_out));
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


        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

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
      //  removeFragment(Utils.FRAGMENT_YOUR_WALLET_TRANSACTION);
        Utils.switchContent(R.id.frame_mainactivity,
                Utils.FRAGMENT_MYWALLET,
                Objects.requireNonNull(getActivity()), null,
                Utils.AnimationType.SLIDE_IN_SLIDE_OUT);
    }

    public void removeFragment(String Tag) {
        Utils.CURRENT_TAG = null;
        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag(Tag)).commit();
    }


}
