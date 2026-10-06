package com.instaguard.user.Fragments;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.Toast;

import com.judemanutd.autostarter.AutoStartPermissionHelper;
import com.instaguard.user.Helpers.Utils;
import com.instaguard.user.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class YourAnnocementsOffersFragment extends Fragment {

    View view;
    SharedPreferences SP_Prefs;
    String driverId;
    ImageButton imgbtnBack;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter adapter;
    public YourAnnocementsOffersFragment() {
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
        view = inflater.inflate(R.layout.fragment_annoc_offer, container, false);

        SP_Prefs = getActivity().getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE);
        driverId = SP_Prefs.getString(Utils.SP_USERID, null);
        Log.e("userId~~RideHistoryFragment~~", driverId);

        initView(view);


        SharedPreferences.Editor getState = getActivity().getSharedPreferences(Utils.GLOBAL_STORED_ID, MODE_PRIVATE).edit();
        getState.putString(Utils.SP_DEVICE_NOTIFY_TYPE, null);
        getState.apply();


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

        if(getArguments()!=null){
             if(getArguments().getString("via").equals("Offers")) {
                 System.out.print("called in the schedulet trip");
                selectPage(0);
            }else if(getArguments().getString("via").equals("Announcements")) {
                 System.out.print("called in the schedulet trip");
                selectPage(1);
            }
        }

        imgbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButtonFunction();
            }
        });


        ImageButton imageButton = view.findViewById(R.id.imgbtn_backs);

        if (AutoStartPermissionHelper.getInstance().isAutoStartPermissionAvailable(requireActivity())) {
            imageButton.setVisibility(View.VISIBLE);
            imageButton.setOnClickListener(view1 -> {
                AutoStartPermissionHelper.getInstance().getAutoStartPermission(requireActivity());
                Toast.makeText(requireActivity(), getResources().getString(R.string.enable_auto_start), Toast.LENGTH_SHORT).show();
            });
        } else {
            imageButton.setVisibility(View.GONE);
            //Toast.makeText(requireActivity(), getResources().getString(R.string.disable_auto_start), Toast.LENGTH_SHORT).show();
        }

    }
    void selectPage(int pageIndex){
        tabLayout.setScrollPosition(pageIndex,0f,true);
        viewPager.setCurrentItem(pageIndex);
    }
    private void setupViewPager(ViewPager viewPager) {

        adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new YourOffersFragment(), getString(R.string.yourjobs_offer));
        adapter.addFragment(new YourAnnocementFragment(), getString(R.string.yourjobs_annocement));
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
        removeFragment(Utils.FRAGMENT_YOUR_OFFERS_ANNOCEMENTS);
    }

    public void removeFragment(String Tag) {
        Utils.CURRENT_TAG = null;
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag(Tag)).commit();
    }


}
