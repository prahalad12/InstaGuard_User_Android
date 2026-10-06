package com.instaguard.user.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.instaguard.user.Adapter.TimeSlotAdapter;
import com.instaguard.user.CustomViews.CustomButton;
import com.instaguard.user.CustomViews.CustomTextView;
import com.instaguard.user.Helpers.Retrofit.APIClient;
import com.instaguard.user.Helpers.Retrofit.APIInterface;
import com.instaguard.user.Helpers.Utils;
import com.instaguard.user.Models.CategoryModel;
import com.instaguard.user.Models.CategorySubModel;
import com.instaguard.user.Models.CheckAvailablityModel;
import com.instaguard.user.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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

public class ScheduleTripFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, TimeSlotAdapter.SuccessResponse {

    ImageButton imgbtnBack;
    View view;

    //Schedule trip Dialog
    LinearLayout rlPickupDate, rlPickupTime;
    LinearLayout rlPickupDate_End, rlPickupTime_End;
    CustomTextView txtPickupDate, txt_book_a_guard;
    CustomTextView txtPickupDate_End;
    CustomTextView txtPickupTime;
    CustomTextView txtPickupTime_End;
    CustomTextView txtDropLocation;
    CustomTextView txtCustomerLocation;
    CustomButton btnContinue;
    DatePickerDialog dpd;
    TimePickerDialog tpd;
    LinearLayout llDropHeader;
    CustomTextView txtHeaderAddress;

    SharedPreferences sprefs, spAcceptDetails;
    SharedPreferences.Editor putSpState, getRequestState;

    String userId, userAccessToken;

    RecyclerView rvTimesSlot;
    private List<String> timeArray;
    int numberOfColumns = 3;

    String strPickupAddress, strPickupLat, strPickupLng, strDropAddress, strDropLat, strDropLng;
    Calendar now;
    SharedPreferences spUpdateProfile;
    String categoryId, subCategoryId, categoryName, subCategoryName;

    int selectedHour;
    String selectedDay;

    String selectedDate, selectedTime;

    public String TAG = "ScheduleTripFragment";

    public ScheduleTripFragment() {
        // Required empty public constructor
    }

    String categoryCount, categoryDays;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            categoryCount = getArguments().getString(Utils.B_CATEGORY_GUARDS_COUNT);
            categoryDays = getArguments().getString(Utils.B_CATEGORY_DAYS);

        }
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_scheduletrip, container, false);

        sprefs = requireActivity().getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE);
        userId = sprefs.getString(Utils.SP_USERID, null);
        userAccessToken = sprefs.getString(Utils.SP_USER_ACCESSTOKEN, null);


        spAcceptDetails = requireActivity().getSharedPreferences(Utils.MY_PREFS_REQUEST_STATE, Context.MODE_PRIVATE);

        categoryId = spAcceptDetails.getString(Utils.B_CATEGORY_ID, null);
        categoryName = spAcceptDetails.getString(Utils.B_CATEGORY_NAME, null);
        subCategoryId = spAcceptDetails.getString(Utils.B_SUB_CATEGORY_ID, null);
        subCategoryName = spAcceptDetails.getString(Utils.B_SUB_CATEGORY_NAME, null);

        getRequestState = requireActivity().getSharedPreferences(Utils.MY_PREFS_REQUEST_STATE, MODE_PRIVATE).edit();

        strPickupAddress = spAcceptDetails.getString(Utils.B_PICKUP, null);
        strPickupLat = spAcceptDetails.getString(Utils.B_PICKUP_LAT, null);
        strPickupLng = spAcceptDetails.getString(Utils.B_PICKUP_LNG, null);

        strDropAddress = spAcceptDetails.getString(Utils.B_DROP, null);
        strDropLat = spAcceptDetails.getString(Utils.B_DROP_LAT, null);
        strDropLng = spAcceptDetails.getString(Utils.B_DROP_LNG, null);

        TimeSlotAdapter.successResponse = this;

        imgbtnBack = view.findViewById(R.id.imgbtn_back_scheduletrip);
        //Schedule trip
        rlPickupDate = view.findViewById(R.id.rl_pickup_date);
        rlPickupTime = view.findViewById(R.id.rl_pickup_time);
        txtPickupDate = view.findViewById(R.id.txt_pickup_date);
        txt_book_a_guard = view.findViewById(R.id.txt_book_a_guard);
        txtPickupTime = view.findViewById(R.id.txt_pickup_time);

        rlPickupDate_End = view.findViewById(R.id.rl_pickup_date_end);
        rlPickupTime_End = view.findViewById(R.id.rl_pickup_time_end);
        txtPickupDate_End = view.findViewById(R.id.txt_pickup_date_end);
        txtPickupTime_End = view.findViewById(R.id.txt_pickup_time_end);


        txtCustomerLocation = view.findViewById(R.id.txt_customer_location);
        txtDropLocation = view.findViewById(R.id.txt_drop_location);

        btnContinue = view.findViewById(R.id.btn_continue_scheduletrip);
        rvTimesSlot = view.findViewById(R.id.rv_timesslot);

        llDropHeader = view.findViewById(R.id.ll_header_drop);
        txtHeaderAddress = view.findViewById(R.id.txt_header_address_label);

        if (strDropAddress.equals("NoDrop")) {
            txtHeaderAddress.setText("Job Location");
            if (llDropHeader.getVisibility() == View.VISIBLE) {
                llDropHeader.setVisibility(View.GONE);

            }
            txtDropLocation.setText(strDropAddress);
        } else {
            if (llDropHeader.getVisibility() == View.GONE) {
                llDropHeader.setVisibility(View.VISIBLE);
            }
            txtDropLocation.setText(strDropAddress);
        }

        txtCustomerLocation.setText(strPickupAddress);

        timeArray = new ArrayList<>();

        for (int i = 0; i < Utils.timeArray.length; i++) {
            timeArray.add(Utils.timeArray[i]);
        }

        rvTimesSlot.setLayoutManager(new LinearLayoutManager(getActivity()));

        rvTimesSlot.setLayoutManager(new GridLayoutManager(requireActivity(), numberOfColumns));
        rvTimesSlot.setAdapter(new TimeSlotAdapter(requireActivity(), timeArray));


        now = Calendar.getInstance();
        dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        tpd = TimePickerDialog.newInstance(
                ScheduleTripFragment.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
        );
        OnClick();
        onBackButton(view);
        return view;
    }

    private void OnClick() {
        imgbtnBack.setOnClickListener(v -> {
            hideKeyBoard();
            backButtonFunction();
        });

        rlPickupDate.setOnClickListener(v -> {
            dialogDatePickerLight("start");
            txtPickupTime.setText(getString(R.string.scheduled_trip_select_start_time));
        });

        rlPickupTime.setOnClickListener(v -> {
            String strdate = txtPickupDate.getText().toString();
            if (strdate.equals(getString(R.string.scheduled_trip_select_start_date))) {
                Toast.makeText(requireActivity(), R.string.scheduled_trip_select_start_date, Toast.LENGTH_SHORT).show();
            } else {
                dialogTimePickerLight("start");
            }
        });

        rlPickupDate_End.setOnClickListener(v -> {
            dialogDatePickerLight("end");
            txtPickupTime_End.setText(getString(R.string.scheduled_trip_select_end_time));
        });

        rlPickupTime_End.setOnClickListener(v -> {
            String strdate = txtPickupDate_End.getText().toString();
            if (strdate.equals(getString(R.string.scheduled_trip_select_end_date))) {
                Toast.makeText(requireActivity(), R.string.scheduled_trip_select_end_date, Toast.LENGTH_SHORT).show();
            } else {
                dialogTimePickerLight("end");
            }
        });


        btnContinue.setOnClickListener(v -> {

            String strdate = txtPickupDate.getText().toString();
            String strtime = txtPickupTime.getText().toString();

            String strdate_end = txtPickupDate_End.getText().toString();
            String strtime_end = txtPickupTime_End.getText().toString();

            if (strdate.equals(getString(R.string.scheduled_trip_select_start_date))) {
                Toast.makeText(requireActivity(), R.string.scheduled_trip_select_start_date, Toast.LENGTH_SHORT).show();
            } else if (strtime.equals(getString(R.string.scheduled_trip_select_start_time))) {
                Toast.makeText(requireActivity(), R.string.scheduled_trip_select_start_time, Toast.LENGTH_SHORT).show();
            } else if (strdate_end.equals(getString(R.string.scheduled_trip_select_end_date))) {
                Toast.makeText(requireActivity(), R.string.scheduled_trip_select_end_date, Toast.LENGTH_SHORT).show();
            } else if (strtime_end.equals(getString(R.string.scheduled_trip_select_end_time))) {
                Toast.makeText(requireActivity(), R.string.scheduled_trip_select_end_time, Toast.LENGTH_SHORT).show();
            } else {
                callCheckAvailablity();
            }
        });
    }

    private void dialogDatePickerLight(String from) {

        Calendar cur_calender = Calendar.getInstance();
        DatePickerDialog datePicker = DatePickerDialog.newInstance(
                (view, year, monthOfYear, dayOfMonth) -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    long date_ship_millis = calendar.getTimeInMillis();

                    SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                    selectedDate = newFormat.format(new Date(date_ship_millis));
                    if (from.equals("start")) {
                        txtPickupDate.setText(selectedDate);
                    } else {
                        txtPickupDate_End.setText(selectedDate);
                    }
                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
                    Date date1;
                    try {
                        date1 = dateformat.parse(selectedDate);
                        DateFormat dayFormate = new SimpleDateFormat("EEEE", Locale.US);
                        String dayFromDate = null;
                        if (date1 != null) {
                            dayFromDate = dayFormate.format(date1);
                        }

                        selectedDay = dayFromDate;

                        Log.d("day text-->", "----------:: " + dayFromDate);
                        Log.i("day text-->", "onDateSet: " + dayFromDate);

                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    Log.i(TAG, "onDateSet: -->" + selectedDate);
                    //    tvSelectTime.setText("");
                },
                cur_calender.get(Calendar.YEAR),
                cur_calender.get(Calendar.MONTH),
                cur_calender.get(Calendar.DAY_OF_MONTH)
        );
        //set dark light
        datePicker.setThemeDark(false);
        datePicker.setLocale(Locale.US);
        datePicker.setAccentColor(getResources().getColor(R.color.colorPrimary));
        datePicker.setMinDate(cur_calender);
        datePicker.show(requireActivity().getSupportFragmentManager(), "Datepickerdialog");
    }

    private void dialogTimePickerLight(String from) {
        Calendar cur_calender = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        String formattedDate = df.format(cur_calender.getTime());
        System.out.println("formattedDate date==" + formattedDate);

        TimePickerDialog timePicker = TimePickerDialog.newInstance((view, hourOfDay, minute, second) -> {

            selectedHour = hourOfDay + 1;

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.AM_PM, calendar.get(Calendar.AM_PM));
            long time_millis = calendar.getTimeInMillis();

            SimpleDateFormat newFormat = new SimpleDateFormat("hh:mm a", Locale.US);
            selectedTime = newFormat.format(new Date(time_millis));
            if (from.equals("start")) {
                txtPickupTime.setText(selectedTime);
            } else {
                txtPickupTime_End.setText(selectedTime);
            }
            Log.i(TAG, "onTimeSet: -->" + selectedTime);

        }, cur_calender.get(Calendar.HOUR_OF_DAY), cur_calender.get(Calendar.MINUTE), false);
        //set dark light

        timePicker.setThemeDark(false);
        timePicker.setLocale(Locale.US);
        //  timePicker.setTimeInterval(1,15);
        timePicker.setAccentColor(getResources().getColor(R.color.colorPrimary));

        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR_OF_DAY, 1);

        Log.i(TAG, "dialogTimePickerLight: int the list--->" + txtPickupDate.getText().toString() + "~~~" + formattedDate);
        if (from.equals("start")) {
            if (txtPickupDate.getText().toString().trim().length() == 0) {
                Toast.makeText(requireActivity(), R.string.scheduled_trip_select_start_date, Toast.LENGTH_SHORT).show();
            } else {
                if (txtPickupDate.getText().toString().trim().equals(formattedDate)) {
                    Log.i(TAG, "dialogTimePickerLight: in the list ---->" + roundup15(c.get(Calendar.MINUTE)));
                    timePicker.setMinTime(c.get(Calendar.HOUR_OF_DAY), /*roundup15(c.get(Calendar.MINUTE)*/
                            c.get(Calendar.MINUTE),
                            c.get(Calendar.SECOND));
                    timePicker.show(requireActivity().getSupportFragmentManager(), "Timepickerdialog");
                } else {
                    timePicker.setMinTime(0, 0, 0);
                    timePicker.show(requireActivity().getSupportFragmentManager(), "Timepickerdialog");
                }
            }
        } else {
            if (txtPickupDate_End.getText().toString().trim().length() == 0) {
                Toast.makeText(requireActivity(), R.string.scheduled_trip_select_end_date, Toast.LENGTH_SHORT).show();
            } else {
                if (txtPickupDate_End.getText().toString().trim().equals(formattedDate)) {
                    Log.i(TAG, "dialogTimePickerLight: in the list ---->" + roundup15(c.get(Calendar.MINUTE)));
                    timePicker.setMinTime(c.get(Calendar.HOUR_OF_DAY), /*roundup15(*/c.get(Calendar.MINUTE)/*)*/,
                            c.get(Calendar.SECOND));
                    timePicker.show(requireActivity().getSupportFragmentManager(), "Timepickerdialog");
                } else {
                    timePicker.setMinTime(0, 0, 0);
                    timePicker.show(requireActivity().getSupportFragmentManager(), "Timepickerdialog");
                }
            }
        }
    }

    public int roundup15(int n) {
        return ((n - 1) / 15 + 1) * 15;
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
        removeFragment(Utils.FRAGMENT_SCHEDULETRIP);
    }

    public void removeFragment(String Tag) {
        Utils.CURRENT_TAG = null;
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag(Tag)).commit();
    }


    public void hideKeyBoard() {
        // Check if no view has focus:
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            Objects.requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {


        System.out.println("monthofyear=" + monthOfYear);
        String month = "", day = "";
        if (dayOfMonth < 10) {
            day = "0" + dayOfMonth;
            System.out.println("day=" + day);
        } else {
            day = String.valueOf(dayOfMonth);
        }
        if (monthOfYear < 10) {
            month = "0" + (++monthOfYear);
            System.out.println("month=" + month);
        } else {
            month = String.valueOf(++monthOfYear);
        }
        //    String date = dayOfMonth+"-"+(++monthOfYear)+"-"+year;
        String date = day + "-" + month + "-" + year;
        System.out.println("new date of settext==" + date);

        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
        Date date1;
        try {
            date1 = dateformat.parse(date);
            DateFormat dayFormate = new SimpleDateFormat("EEEE");
            String dayFromDate = dayFormate.format(date1);

            selectedDay = dayFromDate;

            Log.d("day text-->", "----------:: " + dayFromDate);
            Log.i("day text-->", "onDateSet: " + dayFromDate);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        txtPickupDate.setText(date);
    }

    //Inteface called
    public void onSuccess(String text) {
        Log.i("scheduletrip page", "onSuccess: interface called in myavailability sub fragment-->" + text);

    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        int hour = hourOfDay;
        int minutes = minute;
        int hours = hourOfDay;
        Log.i("Schedule trip", "onTimeSet-->: " + hourOfDay);

        selectedHour = hourOfDay + 1;

        String timeSet = "";
        if (hours > 12) {
            hour -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hour += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";

        String min = "";
        if (minutes < 10)
            min = "0" + minutes;
        else
            min = String.valueOf(minutes);
        String hr = "";

        if (hour < 10)
            hr = "0" + hour;
        else
            hr = String.valueOf(hour);


        System.out.println("hour====" + hr);

        CategorySubModel categoryModel = new CategorySubModel();
        Integer bookLater = categoryModel.getBookGuardMinTime();

        System.out.println("bookLaterTime"+bookLater.toString());

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hr+bookLater).append(':')
                .append(min).append(" ").append(timeSet).toString();

        /*String aTime = new StringBuilder().append(hr+bookLater).append(':')
                .append(min).append(" ").append(timeSet).toString();*/
        Log.i("Schedule", "onTimeSet: Current time--->" + aTime);
        txtPickupTime.setText(aTime);
    }

    @SuppressLint("LongLogTag")
    private void callCheckAvailablity() {
        userAccessToken = sprefs.getString(Utils.SP_USER_ACCESSTOKEN, null);
        HashMap<String, Object> map = new HashMap<>();
        map.put("day", selectedDay);
        map.put("hour", String.valueOf(selectedHour));
        map.put("service", subCategoryId);
        map.put("pick_lat", strPickupLat);
        map.put("pick_lon", strPickupLng);
        Log.i("callCheckAvailablity", "~~~~" + map);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<CheckAvailablityModel> call2 = apiInterface.doAvailabilityCheck("Bearer " + userAccessToken, Utils.POST_ACCEPT_CONTENT_TYPE, map);
        showProgressDialog(getActivity());
        call2.enqueue(new Callback<CheckAvailablityModel>() {
            @Override
            public void onResponse(@NonNull Call<CheckAvailablityModel> call, @NonNull Response<CheckAvailablityModel> response) {
                hideProgressDialog();
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("available Drivers")) {
                        String availableDrivers = response.body().getResult();
                        Log.i("Scheduletrip", "onResponse:availableDrivers--> " + availableDrivers);
                        // showProfileUpdateAlertDialog(requireActivity(), getString(R.string.editsettings_update_successfully));
                        if (availableDrivers != null) {
                            Log.i(TAG, "onResponse: --> insise--" + availableDrivers);
                            String strdate = txtPickupDate.getText().toString();
                            String strtime = txtPickupTime.getText().toString();

                            getRequestState.putString(Utils.B_PICKUP_DATE, strdate);
                            getRequestState.putString(Utils.B_PICKUP_TIME, strtime);
                            getRequestState.putString(Utils.B_SCH_TRIP_DRIVERID, availableDrivers);

                            getRequestState.apply();
                            Bundle args = new Bundle();
                            args.putString(Utils.B_CATEGORY_GUARDS_COUNT, categoryCount);
                            args.putString(Utils.B_CATEGORY_DAYS, categoryDays);
                            Utils.switchContent(R.id.frame_schedule_trip,
                                    Utils.FRAGMENT_PROVIDERLIST,
                                    requireActivity(), args,
                                    Utils.AnimationType.SLIDE_IN_SLIDE_OUT);
                            Log.e(TAG, "onResponse: categoryDays" + categoryDays);
                        } else {
                            Toast.makeText(requireActivity(), R.string.scheduled_trip_no_driver_avail_timeslot, Toast.LENGTH_SHORT).show();
                        }

                    } else if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("no available Drivers")) {
                        Toast.makeText(requireActivity(), R.string.scheduled_trip_no_driver_requires, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireActivity(), R.string.try_again, Toast.LENGTH_SHORT).show();
//                        callRefreshToken("changepassword",strold,strcnfnew);
                    }
                } else if (response.code() == RESPONSECODE_401) {
                    //callRefreshToken("changepassword",strold,strcnfnew);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CheckAvailablityModel> call, @NonNull Throwable t) {
                // CommonUtil.showRetrofitError(LoginActivity.this, error);
                hideProgressDialog();
                call.cancel();
            }
        });
    }

}

