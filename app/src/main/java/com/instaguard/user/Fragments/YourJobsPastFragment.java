package com.instaguard.user.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.instaguard.user.Activities.MainActivity;
import com.instaguard.user.Adapter.PastJobsAdapter;
import com.instaguard.user.CustomViews.CustomButton;
import com.instaguard.user.CustomViews.CustomEditText;
import com.instaguard.user.CustomViews.CustomTextView;
import com.instaguard.user.Helpers.RatingBar.SimpleRatingBar;
import com.instaguard.user.Helpers.RecyclerTouchListener;
import com.instaguard.user.Helpers.Retrofit.APIClient;
import com.instaguard.user.Helpers.Retrofit.APIInterface;
import com.instaguard.user.Helpers.Utils;

import com.instaguard.user.Models.GroupDriverDatum;
import com.instaguard.user.Models.PastJobsData;
import com.instaguard.user.Models.PastJobsModel;
import com.instaguard.user.Models.RatingDriverModel;
import com.instaguard.user.R;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.instaguard.user.Activities.BaseActivity.convertCurrencyPrice;
import static com.instaguard.user.Activities.BaseActivity.convertHoursToDaysHoursMinutes;
import static com.instaguard.user.Activities.BaseActivity.setVisibility;
import static com.instaguard.user.Activities.BaseActivity.showProgressDialog;
import static com.instaguard.user.Fragments.YourJobsFragment.strfromJob;
import static com.instaguard.user.Activities.BaseActivity.hideProgressDialog;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_200;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_201;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_401;


public class YourJobsPastFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    View view;
    View lytProgressBar;

    SharedPreferences SP_Prefs;
    SharedPreferences.Editor putSpState;
    SharedPreferences getspCurrency;
    String strLanguage, strCurrencySymbol, strLanguageShortName;

    String userId;
    ImageButton imgbtnBack;
    SharedPreferences spCommon;
    String userAccessToken;
    RecyclerView rvPastJobs;
    RelativeLayout rlEmptyview;
    List<PastJobsData> getve;
    PastJobsAdapter adapter;

    public String TAG = "OnPastJobsFragment";


    Dialog dialogShowTripDetail;
    private SimpleDraweeView ivprofpicPastjobdetail;
    private CustomTextView txt_drivername_pastjob_detail, txt_booking_pastjob_detail,
            txt_job_request_date_time, txt_pickup_pastjob_detail, txt_category_past_job_detail,
            txt_paymentmode, txt_current_charge_label, txt_trip_status, txt_Driver_company, txtAgentFeeLabel, txtDayFeeLabel;
    private ImageView imgbtn_back;
    private CustomEditText txt_current_charge, txt_material_fee, txt_misc_fee,
            txt_discount_driver, edt_comment_pastjob_detail,
            txtDistance, txt_promo_code, txtAgentCharge, txtDayCharge, txtBaseCharge;
    private CustomButton btn_rating_payment;
    private CardView cv_rating_lyt;
    private LinearLayout ll_current_charge_lyt, ll_material_fee_lyt, ll_misc_fee_lyt, ll_promo_lyt, ll_discount_lyt, ll_final_total_lyt, ll_distance_lyt;
    private SimpleRatingBar ratingBar_pastjob_detail, ratingBar_Driver_pastjob_detail, ratingBar_Trip_pastjob_detail;

    CustomTextView txt_final_total;
    CustomTextView txt_base_amount_trip_summary;
    CustomTextView txtNoofHrsTripSummary;
    CustomTextView txtPricePerHrsLabelTripSummary;
    CustomTextView txtPricePerHrsTripSummary;
    CustomTextView txtVatAmountTripSummary;

    CustomTextView txtExtendNoofHrsTripSummary;
    TableRow trExtendNoofHrsTripSummary;
    View progressBarView;

    public YourJobsPastFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static YourJobsPastFragment newInstance(String param1, String param2) {
        YourJobsPastFragment fragment = new YourJobsPastFragment();
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
        view = inflater.inflate(R.layout.fragment_yourjobs_past, container, false);

        SP_Prefs = requireActivity().getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE);
        userId = SP_Prefs.getString(Utils.SP_USERID, null);
        Log.e("userId~~OnGoingJobsFragment~~", userId);
        spCommon = requireActivity().getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE);
        putSpState = requireActivity().getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE).edit();

        getspCurrency = requireActivity().getSharedPreferences(Utils.PREFS_NAME_CURRENCY, MODE_PRIVATE);
        strLanguage = getspCurrency.getString(Utils.SP_LANGUAGE, null);
        strLanguageShortName = getspCurrency.getString(Utils.SP_LANGUAGE_SHORT_NAME, null);
        strCurrencySymbol = getspCurrency.getString(Utils.SP_CURRENCY_SYMBOL, null);
        if (strLanguageShortName == null) {
            strLanguageShortName = "en";
        }

        initViews();

        onClicks();

        getPastJobsCall();

        onBackButton(view);

        return view;
    }

    private void onClicks() {
        rvPastJobs.addOnItemTouchListener(
                new RecyclerTouchListener(requireActivity(), rvPastJobs,
                        new RecyclerTouchListener.OnTouchActionListener() {
                            @SuppressLint("LongLogTag")
                            @Override
                            public void onClick(View view, int position) {
                                Log.i("rvSubCategory", "onClick: rvSubCategory-> ");
                                if (position != -1) {
                                    PastJobsData listModel = getve.get(position);
                                    if (listModel.getServiceStatus() == 6) {
                                        Toast.makeText(requireActivity(), getResources().getString(R.string.please_wait_customer_accept_your_completion),
                                                Toast.LENGTH_SHORT).show();
                                    } else if (listModel.getServiceStatus() == 8) {
                                        Toast.makeText(requireActivity(), getResources().getString(R.string.upcoming_job_cancelled),
                                                Toast.LENGTH_SHORT).show();
                                    } else if (listModel.getServiceStatus() == 9) {
                                        Toast.makeText(requireActivity(), getResources().getString(R.string.upcoming_job_cancelled_driver),
                                                Toast.LENGTH_SHORT).show();
                                    } else if (listModel.getServiceStatus() == 14) {
                                        Toast.makeText(requireActivity(), getResources().getString(R.string.upcoming_scheduled_cancel_user),
                                                Toast.LENGTH_SHORT).show();
                                    } else if (listModel.getServiceStatus() == 15) {
                                        Toast.makeText(requireActivity(), getResources().getString(R.string.upcoming_scheduled_cancel_driver),
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        /*Toast.makeText(requireActivity(), getResources().getString(R.string.upcoming_job_not_complete),
                                                Toast.LENGTH_SHORT).show();*/

                                        showDialogTripDetails(listModel);
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

    }

    private void initViews() {

        rvPastJobs = (RecyclerView) view.findViewById(R.id.rv_pastjobs);
        rlEmptyview = (RelativeLayout) view.findViewById(R.id.rl_emptyview);
        lytProgressBar = view.findViewById(R.id.lyt_progressBar);

        //Dialog Show trip details
        dialogShowTripDetail = new Dialog(requireActivity(), R.style.AppTheme);
        dialogShowTripDetail.setContentView(R.layout.layout_pastjob_details);
        dialogShowTripDetail.setCancelable(false);

        imgbtn_back = (ImageView) dialogShowTripDetail.findViewById(R.id.imgbtn_back);

        ivprofpicPastjobdetail = (SimpleDraweeView) dialogShowTripDetail.findViewById(R.id.iv_prof_pic_pastjob_detail);
        txt_drivername_pastjob_detail = (CustomTextView) dialogShowTripDetail.findViewById(R.id.txt_drivername_pastjob_detail);
        txt_Driver_company = (CustomTextView) dialogShowTripDetail.findViewById(R.id.txt_Driver_company);
        txt_booking_pastjob_detail = (CustomTextView) dialogShowTripDetail.findViewById(R.id.txt_booking_pastjob_detail);
        txt_job_request_date_time = (CustomTextView) dialogShowTripDetail.findViewById(R.id.txt_job_request_date_time);
        txt_pickup_pastjob_detail = (CustomTextView) dialogShowTripDetail.findViewById(R.id.txt_pickup_pastjob_detail);
        txt_category_past_job_detail = (CustomTextView) dialogShowTripDetail.findViewById(R.id.category_past_job_detail);

        txt_paymentmode = (CustomTextView) dialogShowTripDetail.findViewById(R.id.txt_paymentmode);
        txt_trip_status = (CustomTextView) dialogShowTripDetail.findViewById(R.id.txt_trip_status);

        txt_current_charge_label = (CustomTextView) dialogShowTripDetail.findViewById(R.id.txt_current_charge_label);


        txt_current_charge = (CustomEditText) dialogShowTripDetail.findViewById(R.id.txt_current_charge);
        txt_material_fee = (CustomEditText) dialogShowTripDetail.findViewById(R.id.edt_material_fee);
        txt_misc_fee = (CustomEditText) dialogShowTripDetail.findViewById(R.id.edt_misc_fee);
        txt_discount_driver = (CustomEditText) dialogShowTripDetail.findViewById(R.id.edt_discount_driver);
        txt_final_total = dialogShowTripDetail.findViewById(R.id.txt_final_total);
        edt_comment_pastjob_detail = (CustomEditText) dialogShowTripDetail.findViewById(R.id.edt_comment_pastjob_detail);
        txt_promo_code = (CustomEditText) dialogShowTripDetail.findViewById(R.id.edt_promo_driver);

        txtAgentFeeLabel = dialogShowTripDetail.findViewById(R.id.txt_agent_fee_label);
        txtDayFeeLabel = dialogShowTripDetail.findViewById(R.id.txt_day_fee_label);
        txtAgentCharge = dialogShowTripDetail.findViewById(R.id.txt_agent_charge);
        txtDayCharge = dialogShowTripDetail.findViewById(R.id.txt_day_charge);
        txtBaseCharge = dialogShowTripDetail.findViewById(R.id.txt_base_charge);
        btn_rating_payment = (CustomButton) dialogShowTripDetail.findViewById(R.id.btn_rating_payment);

        cv_rating_lyt = (CardView) dialogShowTripDetail.findViewById(R.id.cv_rating_lyt);

        ll_current_charge_lyt = (LinearLayout) dialogShowTripDetail.findViewById(R.id.ll_current_charge_lyt);
        ll_material_fee_lyt = (LinearLayout) dialogShowTripDetail.findViewById(R.id.ll_material_fee_lyt);
        ll_misc_fee_lyt = (LinearLayout) dialogShowTripDetail.findViewById(R.id.ll_misc_fee_lyt);
        ll_discount_lyt = (LinearLayout) dialogShowTripDetail.findViewById(R.id.ll_discount_lyt);
        ll_promo_lyt = (LinearLayout) dialogShowTripDetail.findViewById(R.id.ll_promo_lyt);
        ll_final_total_lyt = (LinearLayout) dialogShowTripDetail.findViewById(R.id.ll_final_total_lyt);

        ll_distance_lyt = (LinearLayout) dialogShowTripDetail.findViewById(R.id.ll_distance_lyt);
        txtDistance = (CustomEditText) dialogShowTripDetail.findViewById(R.id.edt_distance_driver);

        ratingBar_pastjob_detail = (SimpleRatingBar) dialogShowTripDetail.findViewById(R.id.ratingBar_pastjob_detail);
        ratingBar_Driver_pastjob_detail = (SimpleRatingBar) dialogShowTripDetail.findViewById(R.id.ratingBar);
        ratingBar_Trip_pastjob_detail = (SimpleRatingBar) dialogShowTripDetail.findViewById(R.id.ratingBar_trip);

        txt_base_amount_trip_summary = dialogShowTripDetail.findViewById(R.id.txt_base_amount_trip_summary);


        txtNoofHrsTripSummary = dialogShowTripDetail.findViewById(R.id.txt_no_of_hrs_trip_summary);
        txtPricePerHrsLabelTripSummary = dialogShowTripDetail.findViewById(R.id.txt_price_per_hrs_trip_summary_label);
        txtPricePerHrsTripSummary = dialogShowTripDetail.findViewById(R.id.txt_price_per_hrs_trip_summary);
        txtVatAmountTripSummary = dialogShowTripDetail.findViewById(R.id.txt_vat_amount_trip_summary);
        trExtendNoofHrsTripSummary = dialogShowTripDetail.findViewById(R.id.tr_extra_no_of_hrs_trip_summary);
        txtExtendNoofHrsTripSummary = dialogShowTripDetail.findViewById(R.id.txt_extra_no_of_hrs_trip_summary);

        progressBarView = dialogShowTripDetail.findViewById(R.id.progressBar);
    }

    private void getPastJobsCall() {

        SharedPreferences getspCurrency = requireActivity().getSharedPreferences(Utils.PREFS_NAME_CURRENCY, MODE_PRIVATE);
        //String strLanguage = getspCurrency.getString(Utils.SP_LANGUAGE, null);
        String strLanguageShortName = getspCurrency.getString(Utils.SP_LANGUAGE_SHORT_NAME, null);
        if (strLanguageShortName == null) {
            strLanguageShortName = "en";
        }

        lytProgressBar.setVisibility(View.VISIBLE);
        userAccessToken = spCommon.getString(Utils.SP_USER_ACCESSTOKEN, null);
        HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("mode", "customer");
        map.put("language", strLanguageShortName);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<PastJobsModel> call2 = apiInterface.doCustomerPastJobs("Bearer " + userAccessToken, Utils.POST_ACCEPT_CONTENT_TYPE, map);
        // showProgressDialog(getActivity());
        call2.enqueue(new Callback<PastJobsModel>() {
            @Override
            public void onResponse(@NonNull Call<PastJobsModel> call, @NonNull retrofit2.Response<PastJobsModel> response) {
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    hideProgressDialog();
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Fetching past jobs successful.")) {
                        getve = Objects.requireNonNull(response.body()).getPastJobsData();
                        Log.i(TAG, "onResponse: getUpcomingJobsCall-->" + getve);
                        if (getve.size() > 0) {
                            rlEmptyview.setVisibility(View.GONE);
                            rvPastJobs.setVisibility(View.VISIBLE);
                            lytProgressBar.setVisibility(View.GONE);

                            adapter = new PastJobsAdapter(getActivity(), getve);
                            LinearLayoutManager horizontalLayoutManager
                                    = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                            rvPastJobs.setLayoutManager(horizontalLayoutManager);
                            rvPastJobs.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } else {
                            rlEmptyview.setVisibility(View.VISIBLE);
                            rvPastJobs.setVisibility(View.GONE);
                            lytProgressBar.setVisibility(View.GONE);

                        }
                    } else {
                        Toast.makeText(getActivity(), R.string.try_again, Toast.LENGTH_SHORT).show();
                        hideProgressDialog();
                    }
                } else {
                    hideProgressDialog();
                }
            }

            @Override
            public void onFailure(@NonNull Call<PastJobsModel> call, @NonNull Throwable t) {
                Log.i("errror", "errr" + t);
                call.cancel();
                hideProgressDialog();
                lytProgressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "please try again", Toast.LENGTH_SHORT).show();
            }
        });

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
                requireActivity().finish();
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


    @SuppressLint("SetTextI18n")
    private void showDialogTripDetails(PastJobsData listModel) {

        String bookingId = String.valueOf(listModel.getBookingId());
        Integer serviceStatus = listModel.getServiceStatus();
        String driverName = listModel.getDriverName();
        String driver_avatar = listModel.getDriverAvatar();
        String jobDate = listModel.getJobDate();
        String driverid = String.valueOf(listModel.getDriverId());
        String getUserid = String.valueOf(listModel.getCustomerId());
        String paymentType = String.valueOf(listModel.getPaymentType());
        String totalAmount = String.valueOf(listModel.getTotalAmount());
        String rating_status = String.valueOf(listModel.getRatingStatus());
        float rating = listModel.getRating();

        String strVatAmountsFB = String.valueOf(listModel.getVatAmount());
        String strTotalHrsFB = String.valueOf(listModel.getTotalHrs());
        String stPricePerHrsFB = String.valueOf(listModel.getPricePerHr());
        String strExtendedStatusFB = String.valueOf(listModel.getExtendedStatus());
        String strExtendedHrsFB = String.valueOf(listModel.getExtendedHrs());
        String strExtendedVatFB = String.valueOf(listModel.getExtendedVat());
        String strExtendedAmountFB = String.valueOf(listModel.getExtendedAmount());
        String strSubLevelAmountFB = String.valueOf(listModel.getSubLevelAmount());
        List<GroupDriverDatum> groupDriverData = listModel.getGroupDriverData();

        ivprofpicPastjobdetail.setImageURI(Utils.BASE_URL + driver_avatar);
        int color = getResources().getColor(R.color.colorPrimary);
        RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
        roundingParams.setBorder(color, 1.0f);
        roundingParams.setRoundAsCircle(true);

        ivprofpicPastjobdetail.getHierarchy().setRoundingParams(roundingParams);
        txt_drivername_pastjob_detail.setText(driverName);
        ratingBar_Trip_pastjob_detail.setRating(rating);
        txt_booking_pastjob_detail.setText(bookingId);

        System.out.println("fare total amt "+ totalAmount);


        final String dateList;
        String dateListEs = null;
        dateList = jobDate; //"Thu,Jan 30,2020 05:12 AM",
        try {

            String[] separated = dateList.split(",");
            String day = separated[0]; //Thu
            String monthDate = separated[1]; // Jan 30
            String yearTime = separated[2]; // 2020 05:12 AM"

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat date12Format = new SimpleDateFormat("yyyy hh:mm a");

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat date24Format = new SimpleDateFormat("yyyy HH:mm");

            String yearTime1 = date24Format.format(Objects.requireNonNull(date12Format.parse(yearTime)));

            String[] separate2 = monthDate.split(" ");
            String month = separate2[0]; // Jan
            String date = separate2[1]; // 30

            // Log.e("onBindViewHolder: ~~~~~~~~1~~~~~~~", day + "~~" + monthDate + "~~~~" + yearTime + "~~~~");
            // Log.e("onBindViewHolder: ~~~~~~~~2~~~~~~~", "~~" + month + "~~~~" + date);

            //ronBindViewHolder: ~~~~~~~~1~~~~~~~: Thu ~~ Jan 30~~~~2020 05:12 AM~~~~
            //onBindViewHolder: ~~~~~~~~2~~~~~~~: ~~Jan~~~~30

            String dayLabel = "-";
            switch (day) {
                case "Sun":
                case "sun":
                    dayLabel = getResources().getString(R.string.Sunday_3);
                    break;
                case "Mon":
                case "mon":
                    dayLabel = getResources().getString(R.string.Monday_3);
                    break;
                case "Tue":
                case "tue":
                    dayLabel = getResources().getString(R.string.Tuesday_3);
                    break;
                case "Wed":
                case "wed":
                    dayLabel = getResources().getString(R.string.Wednesday_3);
                    break;
                case "Thu":
                case "thu":
                    dayLabel = getResources().getString(R.string.Thursday_3);
                    break;
                case "Fri":
                case "fri":
                    dayLabel = getResources().getString(R.string.Friday_3);
                    break;
                case "Sat":
                case "sat":
                    dayLabel = getResources().getString(R.string.Saterday_3);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + day);
            }

            String monthLabel = "-";
            switch (month) {
                case "Jan":
                case "jan":
                    monthLabel = getResources().getString(R.string.January_3);
                    break;
                case "Feb":
                case "feb":
                    monthLabel = getResources().getString(R.string.February_3);
                    break;
                case "Mar":
                case "mar":
                    monthLabel = getResources().getString(R.string.March_3);
                    break;
                case "Apr":
                case "apr":
                    monthLabel = getResources().getString(R.string.April_3);
                    break;
                case "May":
                case "may":
                    monthLabel = getResources().getString(R.string.May_3);
                    break;
                case "Jun":
                case "jun":
                    monthLabel = getResources().getString(R.string.June_3);
                    break;
                case "Jul":
                case "jul":
                    monthLabel = getResources().getString(R.string.July_3);
                    break;
                case "Aug":
                case "aug":
                    monthLabel = getResources().getString(R.string.August_3);
                    break;
                case "Sep":
                case "sep":
                    monthLabel = getResources().getString(R.string.September_3);
                    break;
                case "Oct":
                case "oct":
                    monthLabel = getResources().getString(R.string.October_3);
                    break;
                case "Nov":
                case "nov":
                    monthLabel = getResources().getString(R.string.November_3);
                    break;
                case "Dec":
                case "dec":
                    monthLabel = getResources().getString(R.string.December_3);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + month);
            }

            dateListEs = dayLabel + "," + monthLabel + " " + date + "," + yearTime1;

        } catch (Exception e) {
            e.printStackTrace();
            dateListEs = null;
        }

        /*if (strLanguageShortName.equals("en")) {
            holder.txtBookingDate.setText(dateList);
        } else if (strLanguageShortName.equals("es")) {
            */
        if (dateListEs != null) {

            txt_job_request_date_time.setText(dateListEs);
        } else {
            txt_job_request_date_time.setText(jobDate);
        }

        txt_category_past_job_detail.setText(listModel.getCategoryName() + "-" + listModel.getServiceName());

        txt_pickup_pastjob_detail.setText(String.valueOf(listModel.getCustomerLocation()));

        if (serviceStatus == 7) {
            txt_trip_status.setText(getString(R.string.dialog_pastjob_finish_trip));
        } else if (serviceStatus == 6) {
            txt_trip_status.setText(getString(R.string.please_wait_customer_accept_your_completion));
        } else if (serviceStatus == 8) {
            txt_trip_status.setText(getString(R.string.upcoming_job_cancelled));
        } else if (serviceStatus == 9) {
            txt_trip_status.setText(getString(R.string.upcoming_job_cancelled_driver));
        } else if (serviceStatus == 14) {
            txt_trip_status.setText(getString(R.string.upcoming_scheduled_cancel_user));
        } else if (serviceStatus == 15) {
            txt_trip_status.setText(getString(R.string.upcoming_scheduled_cancel_driver));
        } else {
            txt_trip_status.setText(getString(R.string.dialog_pastjob_not_finish_trip));
        }

        if (!strSubLevelAmountFB.equals("0")) {
            txt_base_amount_trip_summary.setText(strCurrencySymbol + " " + convertCurrencyPrice(requireActivity(), strSubLevelAmountFB));
        } else {
            txt_base_amount_trip_summary.setText(strCurrencySymbol + " ");
        }

        double v = Double.parseDouble(strVatAmountsFB) + Double.parseDouble(strExtendedVatFB);
        if (totalAmount.equalsIgnoreCase("0")) {
            ll_final_total_lyt.setVisibility(View.GONE);
        } else {

            ll_final_total_lyt.setVisibility(View.VISIBLE);

            double faret = Double.parseDouble(totalAmount);
            if (faret <= 0.0) {
                faret = 0.0;
            }

            if (!strVatAmountsFB.equals("0")) {
                if (strExtendedStatusFB.equals("1")) {
                    double fare = v;
                    if (fare <= 0.0) {
                        fare = 0.0;
                    }
                    txt_final_total.setText(strCurrencySymbol + " " + convertCurrencyPrice(requireActivity(), String.valueOf(faret + fare)));
                } else {
                    double fared = Double.parseDouble(strVatAmountsFB) + faret;
                    if (fared <= 0.0) {
                        fared = 0.0;
                    }
//                    txt_final_total.setText(strCurrencySymbol + " " + convertCurrencyPrice(requireActivity(), String.valueOf(faret + fared)));
                    txt_final_total.setText(strCurrencySymbol + " " + convertCurrencyPrice(requireActivity(), totalAmount));
                }
            } else {
                if (strExtendedStatusFB.equals("1")) {
                    txt_final_total.setText(strCurrencySymbol + " " + convertCurrencyPrice(requireActivity(), String.valueOf(faret)));
                } else {
                    txt_final_total.setText(strCurrencySymbol + " " + convertCurrencyPrice(requireActivity(), totalAmount));
                }
            }

            /*if (groupDriverData != null && groupDriverData.size() > 0) {
                double farediv = Double.parseDouble(totalAmount) / groupDriverData.size();
                if (farediv <= 0.0) {
                    farediv = 0.0;
                }
                txt_your_final_total.setText(strCurrencySymbol + "" + convertCurrencyPrice(requireActivity(), String.valueOf(farediv)));
            } else {
                txt_your_final_total.setText("-");
            }*/

        }

        if (rating_status.equalsIgnoreCase("Yes")) {
            cv_rating_lyt.setVisibility(View.GONE);
        } else {
            edt_comment_pastjob_detail.setHint(getString(R.string.dialog_enter_your_reason_hint));
            ratingBar_pastjob_detail.setRating(0);
//            cv_rating_lyt.setVisibility(View.VISIBLE);
            cv_rating_lyt.setVisibility(View.GONE);
        }

        if (!strVatAmountsFB.equals("0")) {
            if (strExtendedStatusFB.equals("1")) {
                double fare = v;
                if (fare <= 0.0) {
                    fare = 0.0;
                }
                txtVatAmountTripSummary.setText(strCurrencySymbol + "" + convertCurrencyPrice(requireActivity(), String.valueOf(fare)));
            } else {
                txtVatAmountTripSummary.setText(strCurrencySymbol + "" + convertCurrencyPrice(requireActivity(), strVatAmountsFB));
            }
        } else {
            txtVatAmountTripSummary.setText("");
        }

        if (!strTotalHrsFB.equals("0")) {
            // txtNoofHrsTripSummary.setText(strTotalHrsFB + " " + getResources().getString(R.string.hrs));
            txtNoofHrsTripSummary.setText(convertHoursToDaysHoursMinutes(strTotalHrsFB));
        } else {
            txtNoofHrsTripSummary.setText("");
        }

        if (strExtendedStatusFB.equals("1")) {
            setVisibility(trExtendNoofHrsTripSummary, true);
            if (!strExtendedHrsFB.equals("0")) {
                txtExtendNoofHrsTripSummary.setText(convertHoursToDaysHoursMinutes(strExtendedHrsFB));
            } else {
                txtExtendNoofHrsTripSummary.setText("-");
            }
        } else {
            setVisibility(trExtendNoofHrsTripSummary, false);
        }

        if (!strTotalHrsFB.equals("0") && !stPricePerHrsFB.equals("0")) {
            String hrs_;
            if (strExtendedStatusFB.equals("1")) {
                hrs_ = String.valueOf(Double.parseDouble(strTotalHrsFB) + Double.parseDouble(strExtendedHrsFB));
            } else {
                hrs_ = strTotalHrsFB;
            }

            String label = "( " + getResources().getString(R.string.price_per_hrs) + " = " + strCurrencySymbol + "" + convertCurrencyPrice(requireActivity(), stPricePerHrsFB) + " * " + hrs_ + " " + getResources().getString(R.string.hrs) + " )";
            txtPricePerHrsLabelTripSummary.setText(label);

            double fare = Double.parseDouble(stPricePerHrsFB) * Double.parseDouble(hrs_);
            if (fare <= 0.0) {
                fare = 0.0;
            }
            txtPricePerHrsTripSummary.setText(strCurrencySymbol + "" + convertCurrencyPrice(requireActivity(), String.valueOf(fare)));
        } else {
            txtPricePerHrsLabelTripSummary.setText(getResources().getString(R.string.price_per_hrs));
            txtPricePerHrsTripSummary.setText("");
        }


        btn_rating_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edt_comment_pastjob_detail.getText().toString().trim().length() == 0) {
                    edt_comment_pastjob_detail.setError(getString(R.string.error_required));
                } else {
                    callUpdateRating(edt_comment_pastjob_detail.getText().toString().trim(), ratingBar_pastjob_detail.getRating(),
                            bookingId, driverid, getUserid);
                }
            }
        });

        imgbtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogShowTripDetail.dismiss();
            }
        });

        if (!paymentType.equals("null") && !paymentType.isEmpty() && !paymentType.equals(" "))
            switch (paymentType) {
                case "card":
                case "stripe":
                    txt_paymentmode.setText(getResources().getString(R.string.paid_with) + " " + getResources().getString(R.string.select_payment_pay_online));
                    break;
                case "wallet":
                    txt_paymentmode.setText(getResources().getString(R.string.paid_with_wallet));
                    break;
                case "stripe_gpay":
                    txt_paymentmode.setText(getResources().getString(R.string.paid_with_google_pay));
                    break;
                case "paypal":
                    txt_paymentmode.setText(getResources().getString(R.string.paid_with_paypal));
                    break;
                default:
                    txt_paymentmode.setText(paymentType);
                    break;
            }
        else {
            txt_paymentmode.setText("");
        }

        if (dialogShowTripDetail != null && !dialogShowTripDetail.isShowing()) {
            dialogShowTripDetail.show();
        }

    }

    @SuppressLint("LongLogTag")
    private void callUpdateRating(String trim, float rating, String strBookingId, String driverid, String getUserid) {

        String ratings = String.valueOf(rating);

        HashMap<String, Object> map = new HashMap<>();
        map.put("from_id", getUserid);   //userid
        map.put("job_id", strBookingId);
        map.put("to_id", driverid);  //driverid
        map.put("rating", ratings);
        map.put("status", "0");
        map.put("feedback", trim);
        Log.e("callUpdateRating adapter", "~~~~" + map);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<RatingDriverModel> call2 = apiInterface.doUpdateRatingUser("Bearer " + "123", Utils.POST_ACCEPT_CONTENT_TYPE, map);

        setVisibility(progressBarView, true);
        call2.enqueue(new Callback<RatingDriverModel>() {
            @Override
            public void onResponse(@NonNull Call<RatingDriverModel> call, @NonNull Response<RatingDriverModel> response) {
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Feedback received")) {
                        Toast.makeText(requireActivity(), R.string.updated, Toast.LENGTH_SHORT).show();
                        cv_rating_lyt.setVisibility(View.GONE);
                        setVisibility(progressBarView, false);
                    } else {
                        Toast.makeText(requireActivity(), R.string.try_again, Toast.LENGTH_SHORT).show();
                        setVisibility(progressBarView, false);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<RatingDriverModel> call, @NonNull Throwable t) {
                call.cancel();
                setVisibility(progressBarView, false);
            }
        });
    }


}
