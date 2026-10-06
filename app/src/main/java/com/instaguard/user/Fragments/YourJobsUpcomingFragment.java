package com.instaguard.user.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.instaguard.user.Activities.BaseActivity;
import com.instaguard.user.Activities.HomeActivity;
import com.instaguard.user.Activities.MainActivity;
import com.instaguard.user.Adapter.UpcomingJobsAdapter;
import com.instaguard.user.Adapter.UpcomingJobsOffSetNewAdapter;
import com.instaguard.user.Helpers.Retrofit.APIClient;
import com.instaguard.user.Helpers.Retrofit.APIInterface;
import com.instaguard.user.Helpers.Utils;
import com.instaguard.user.Models.RideHistoryData;
import com.instaguard.user.Models.RideHistoryModel;
import com.instaguard.user.Models.UpcomingCancelJobsModel;
import com.instaguard.user.Models.UpcomingJobsData;
import com.instaguard.user.Models.RefreshTokenModel;
import com.instaguard.user.Models.UpcomingJobsModel;
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
import static com.instaguard.user.Fragments.YourJobsFragment.strfromJob;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_200;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_201;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_401;


public class YourJobsUpcomingFragment extends Fragment implements UpcomingJobsOffSetNewAdapter.SuccessResponse, SwipeRefreshLayout.OnRefreshListener {
    // TODO: Rename parameter arguments, choose names that match

    View view;
    View lytProgressBar;
    SharedPreferences SP_Prefs;

    SharedPreferences.Editor putSpState;
    String userId;
    ImageButton imgbtnBack;
    SharedPreferences spCommon;
    String userAccessToken;
    RecyclerView rvYourJobs;
    RelativeLayout rlEmptyview;
    String strCancelReason = "";
    String strBookid;
    UpcomingJobsAdapter adapter;
    UpcomingJobsOffSetNewAdapter upcomingJobsOffSetNewAdapter;

    /*Pagination process */
    private boolean stillavailable = true;
    private int mCurrentPage = 0;
    private boolean loading = true;
    private int visibleItemCount, totalItemCount, lastVisibleItemPosition, firstVisibleItemPosition;
    /*Pagination process */

    private RecyclerView.LayoutManager layoutManager;


    public String TAG = "YourJobsUpcomingFragment";

    private SwipeRefreshLayout swipeRefreshLayout;

    private RideHistoryData riderHistoryData;

    private ArrayList<RideHistoryData> eArrayList;

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        //getUpcomingJobsCall();
        getUpcomingJobsCall("home");
    }
//    Timer timer;

    public interface SuccessResponse {
        void onSuccess(HashMap<String, Object> map);
    }

    public YourJobsUpcomingFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static YourJobsUpcomingFragment newInstance(String param1, String param2) {
        YourJobsUpcomingFragment fragment = new YourJobsUpcomingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint({"LongLogTag", "CommitPrefEdits"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_yourjobs_upcoming, container, false);

        SP_Prefs = requireActivity().getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE);
        userId = SP_Prefs.getString(Utils.SP_USERID, null);
        Log.e("userId~~OnGoingJobsFragment~~", userId);

        spCommon = requireActivity().getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE);
        putSpState = requireActivity().getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE).edit();
        UpcomingJobsOffSetNewAdapter.successResponse = this;

        rvYourJobs = (RecyclerView) view.findViewById(R.id.rv_upcomingjobs);
        rlEmptyview = (RelativeLayout) view.findViewById(R.id.rl_emptyview_upcoming);
        lytProgressBar = view.findViewById(R.id.lyt_progressBar);
        layoutManager = new LinearLayoutManager(getActivity());
        rvYourJobs.setLayoutManager(layoutManager);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(this);

        rvYourJobs.addOnScrollListener(new RecyclerView.OnScrollListener() { //*Pagination process 18-8-2016 Start*//*
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    Log.w(TAG, "onScrolled1: " + visibleItemCount + " " + totalItemCount);
                    firstVisibleItemPosition = ((LinearLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager())).findFirstVisibleItemPosition();
                    lastVisibleItemPosition = ((LinearLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager())).findLastVisibleItemPosition();
                    Log.w(TAG, "onScrolled1: " + firstVisibleItemPosition + " " + lastVisibleItemPosition);
                    if (loading) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
                            loading = false;
                            if (mCurrentPage != 0 && stillavailable) {
                                Log.w("Scroll true", "" + "true");
                                lytProgressBar.setVisibility(View.VISIBLE);
                                getUpcomingJobsCall("load");
                            } else {
                                Log.w(TAG, "onScrolled: else ~~~");
                            }
                        }
                    } else {
                        Log.w(TAG, "onScrolled: else ~~~");
                    }
                }
            }
        }); //*Pagination process 18-8-2016 End*/

        getUpcomingJobsCall("home");

        onBackButton(view);

        return view;
    }


    private void getUpcomingJobsCall() {

        SharedPreferences getspCurrency = getActivity().getSharedPreferences(Utils.PREFS_NAME_CURRENCY, MODE_PRIVATE);
        // String strLanguage = getspCurrency.getString(Utils.SP_LANGUAGE, null);
        String strLanguageShortName = getspCurrency.getString(Utils.SP_LANGUAGE_SHORT_NAME, null);
        if (strLanguageShortName == null) {
            strLanguageShortName = "en";
        }

        //lytProgressBar.setVisibility(View.VISIBLE);

        userAccessToken = spCommon.getString(Utils.SP_USER_ACCESSTOKEN, null);
        HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("mode", "customer");
        map.put("language", strLanguageShortName);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<UpcomingJobsModel> call2 = apiInterface.doCustomerUpcomingJobs("Bearer " + userAccessToken, Utils.POST_ACCEPT_CONTENT_TYPE, map);
        // showProgressDialog(getActivity());
        call2.enqueue(new Callback<UpcomingJobsModel>() {
            @Override
            public void onResponse(@NonNull Call<UpcomingJobsModel> call, @NonNull retrofit2.Response<UpcomingJobsModel> response) {
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    hideProgressDialog();
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Fetching upcoming jobs successful.")) {
                        // List<OnGoingJobsData> getve = response.body().getServiceRequest();
                        List<UpcomingJobsData> getve = Objects.requireNonNull(response.body()).getServiceRequest();
                        Log.i(TAG, "onResponse: getUpcomingJobsCall-->" + getve);
                        /*if (getve.size() > 0) {
                            rlEmptyview.setVisibility(View.GONE);
                            rvYourJobs.setVisibility(View.VISIBLE);
                            lytProgressBar.setVisibility(View.GONE);

                            adapter = new UpcomingJobsAdapter(getActivity(), getve);
                            LinearLayoutManager horizontalLayoutManager
                                    = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                            rvYourJobs.setLayoutManager(horizontalLayoutManager);
                            rvYourJobs.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } else {
                            rlEmptyview.setVisibility(View.VISIBLE);
                            rvYourJobs.setVisibility(View.GONE);
                            lytProgressBar.setVisibility(View.GONE);

                        }*/
                    } else {
                        Toast.makeText(getActivity(), R.string.try_again, Toast.LENGTH_SHORT).show();
                    }
                } else {

                }
            }

            @Override
            public void onFailure(@NonNull Call<UpcomingJobsModel> call, @NonNull Throwable t) {
                Log.i("errror", "errr" + t.toString());
                call.cancel();
                //lytProgressBar.setVisibility(View.GONE);
                //Toast.makeText(getActivity(), "please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUpcomingJobsCall(String key) {

        SharedPreferences getspCurrency = getActivity().getSharedPreferences(Utils.PREFS_NAME_CURRENCY, MODE_PRIVATE);
        // String strLanguage = getspCurrency.getString(Utils.SP_LANGUAGE, null);
        String strLanguageShortName = getspCurrency.getString(Utils.SP_LANGUAGE_SHORT_NAME, null);
        if (strLanguageShortName == null) {
            strLanguageShortName = "en";
        }

        lytProgressBar.setVisibility(View.VISIBLE);

        userAccessToken = spCommon.getString(Utils.SP_USER_ACCESSTOKEN, null);
        HashMap<String, Object> map = new HashMap<>();
        map.put("customer_id", userId);
        map.put("offset", mCurrentPage);

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<RideHistoryModel> call2 = apiInterface.doCustomerUpcomingJobs_offset("Bearer " + userAccessToken, Utils.POST_ACCEPT_CONTENT_TYPE, map);

        // showProgressDialog(getActivity());
        call2.enqueue(new Callback<RideHistoryModel>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<RideHistoryModel> call, @NonNull retrofit2.Response<RideHistoryModel> response) {
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    hideProgressDialog();
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("UpcomingJob listed successfully.")) {
                        // List<OnGoingJobsData> getve = response.body().getServiceRequest();
                        List<RideHistoryData> getve = Objects.requireNonNull(response.body()).getResult();
                        Log.i(TAG, "onResponse: getUpcomingJobsCall-->" + getve.size());
                        if (getve.size() > 0) {

                            rlEmptyview.setVisibility(View.GONE);
                            rvYourJobs.setVisibility(View.VISIBLE);
                            lytProgressBar.setVisibility(View.GONE);

                            if (!key.equals("load")) {

                                eArrayList = new ArrayList<>();
                                for (int i = 0; i < getve.size(); i++) {
                                    riderHistoryData = getve.get(i);
                                    eArrayList.add(riderHistoryData);
                                }

                                upcomingJobsOffSetNewAdapter = new UpcomingJobsOffSetNewAdapter(getActivity(), eArrayList);
                                rvYourJobs.setAdapter(upcomingJobsOffSetNewAdapter);
                                upcomingJobsOffSetNewAdapter.notifyDataSetChanged();

                            } else {

                                ArrayList<RideHistoryData> eArrayList = new ArrayList<>();
                                for (int i = 0; i < getve.size(); i++) {
                                    riderHistoryData = getve.get(i);
                                    eArrayList.add(riderHistoryData);
                                }

                                upcomingJobsOffSetNewAdapter.updateAdapter(eArrayList);
                                upcomingJobsOffSetNewAdapter.notifyDataSetChanged();
                            }

                            if (getve.size() == 0 ||
                                    getve.size() < 8) {
                                loading = false;
                                stillavailable = false;
                            } else {
                                mCurrentPage = response.body().getNextPage();
                                loading = true;
                                stillavailable = true;
                            }

                        } else {
                            if (mCurrentPage == 0) {
                                rlEmptyview.setVisibility(View.VISIBLE);
                                rvYourJobs.setVisibility(View.GONE);
                                lytProgressBar.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        // Toast.makeText(getActivity(), R.string.try_again, Toast.LENGTH_SHORT).show();
                        if (mCurrentPage == 0) {
                            rlEmptyview.setVisibility(View.VISIBLE);
                            rvYourJobs.setVisibility(View.GONE);
                            lytProgressBar.setVisibility(View.GONE);
                        }
                    }
                } else {
                    //callRefreshToken("Category");
                    if (mCurrentPage == 0) {
                        rlEmptyview.setVisibility(View.VISIBLE);
                        rvYourJobs.setVisibility(View.GONE);
                        lytProgressBar.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<RideHistoryModel> call, @NonNull Throwable t) {
                Log.i("errror", "errr" + t);
                call.cancel();
                lytProgressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "onFailure--" + t, Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void cancelUpcomingJobs_request(String strBookid) {

        lytProgressBar.setVisibility(View.VISIBLE);

        SharedPreferences getspCurrency = requireActivity().getSharedPreferences(Utils.PREFS_NAME_CURRENCY, MODE_PRIVATE);
        //String strLanguage = getspCurrency.getString(Utils.SP_LANGUAGE, null);
        String strLanguageShortName = getspCurrency.getString(Utils.SP_LANGUAGE_SHORT_NAME, null);
        if (strLanguageShortName == null) {
            strLanguageShortName = "en";
        }

        userAccessToken = spCommon.getString(Utils.SP_USER_ACCESSTOKEN, null);
        HashMap<String, Object> map = new HashMap<>();
        map.put("customer_booking_id", strBookid);
        map.put("mode", "customer_cancel");

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<UpcomingCancelJobsModel> call2 = apiInterface.doCustomerRequestCancelJobs("Bearer " + userAccessToken, Utils.POST_ACCEPT_CONTENT_TYPE, map);

        call2.enqueue(new Callback<UpcomingCancelJobsModel>() {
            @Override
            public void onResponse(@NonNull Call<UpcomingCancelJobsModel> call, @NonNull retrofit2.Response<UpcomingCancelJobsModel> response) {
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Cancelled successfully.")
                            || Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Customer Cancel successfully.")) {
                        String getStatus = Objects.requireNonNull(response.body()).getMessage();
                        if (getStatus.equals("Cancelled successfully.") || getStatus.equals("Customer Cancel successfully.")) {
                            lytProgressBar.setVisibility(View.GONE);
                            cancelSuccessStatus("", "cancel", getResources().getString(R.string.upcoming_job_cancelled));
                        } else {
                            rlEmptyview.setVisibility(View.VISIBLE);
                            rvYourJobs.setVisibility(View.GONE);
                            lytProgressBar.setVisibility(View.GONE);
                        }
                    } else {
                        lytProgressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), R.string.try_again, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    lytProgressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), R.string.try_again, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UpcomingCancelJobsModel> call, @NonNull Throwable t) {
                Log.i("errror", "errr" + t);
                call.cancel();
                lytProgressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), R.string.try_again, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void cancelUpcomingJobs() {

        lytProgressBar.setVisibility(View.VISIBLE);

        SharedPreferences getspCurrency = getActivity().getSharedPreferences(Utils.PREFS_NAME_CURRENCY, MODE_PRIVATE);
        //String strLanguage = getspCurrency.getString(Utils.SP_LANGUAGE, null);
        String strLanguageShortName = getspCurrency.getString(Utils.SP_LANGUAGE_SHORT_NAME, null);
        if (strLanguageShortName == null) {
            strLanguageShortName = "en";
        }

        userAccessToken = spCommon.getString(Utils.SP_USER_ACCESSTOKEN, null);
        HashMap<String, Object> map = new HashMap<>();
        map.put("booking_id", strBookid);
        map.put("reason", strCancelReason);
        map.put("mode", "customer");
        map.put("language", strLanguageShortName);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<UpcomingCancelJobsModel> call2 = apiInterface.doCustomerUpcomingCancelJobs("Bearer " + userAccessToken, Utils.POST_ACCEPT_CONTENT_TYPE, map);

        call2.enqueue(new Callback<UpcomingCancelJobsModel>() {
            @Override
            public void onResponse(@NonNull Call<UpcomingCancelJobsModel> call, @NonNull retrofit2.Response<UpcomingCancelJobsModel> response) {
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {

                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Cancelled successfully.")) {
                        String getStatus = Objects.requireNonNull(response.body()).getMessage();
                        Log.i(TAG, "onResponse: cancelUpcomingJobs-->" + getStatus);
                        Log.i(TAG, "onResponse: cancelUpcomingJobs bookingid -->" + Objects.requireNonNull(response.body()).getBookingId());
                        if (getStatus.equals("Cancelled successfully.")) {
                            lytProgressBar.setVisibility(View.GONE);
                            cancelSuccessStatus("", "cancel", getResources().getString(R.string.upcoming_job_cancelled));
                        } else {
                            rlEmptyview.setVisibility(View.VISIBLE);
                            rvYourJobs.setVisibility(View.GONE);
                            lytProgressBar.setVisibility(View.GONE);
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
                lytProgressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), R.string.try_again, Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void showTimeOut() {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
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
        if (!strfromJob.equals("")) {
            if (Objects.equals(strfromJob, "home")) {
                removeFragment(Utils.FRAGMENT_YOURJOBS);
            } else {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                requireActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                getActivity().finish();
            }
        } else {
            removeFragment(Utils.FRAGMENT_YOURJOBS);
        }
    }

    public void removeFragment(String Tag) {
        Utils.CURRENT_TAG = null;
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(Objects.requireNonNull(fragmentManager.findFragmentByTag(Tag))).commit();
    }

    @Override
    public void onSuccess(HashMap<String, Object> map) {
        strBookid = String.valueOf(map.get("book_id"));
        cancelSuccessStatus(strBookid, "alert", getResources().getString(R.string.are_you_sure_want_to_cancel_this_job));
    }

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
                cancelUpcomingJobs();
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
                        getUpcomingJobsCall("home");
                    } else {
                        cancelUpcomingJobs_request(strBookid);
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

   /*@Override
    public void onStop() {
        super.onStop();
        //timer.cancel();

    }*/
}