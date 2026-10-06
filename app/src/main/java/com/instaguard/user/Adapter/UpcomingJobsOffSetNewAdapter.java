package com.instaguard.user.Adapter;

import static android.content.Context.MODE_PRIVATE;

import static java.lang.System.out;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.instaguard.user.CustomViews.CustomButton;
import com.instaguard.user.CustomViews.CustomTextView;
import com.instaguard.user.Helpers.Utils;
import com.instaguard.user.Models.RideHistoryData;
import com.instaguard.user.Models.UpcomingJobsData;
import com.instaguard.user.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class UpcomingJobsOffSetNewAdapter extends RecyclerView.Adapter<UpcomingJobsOffSetNewAdapter.MyViewHolder> {

    public static SuccessResponse successResponse;
    RideHistoryData listModel;
    SharedPreferences prefs;
    int i = 1;
    private Context mContext;
    private ArrayList<RideHistoryData> orderListModel;
    private SharedPreferences getspCurrency;
    private String strLanguage, strLanguageShortName;

    public UpcomingJobsOffSetNewAdapter(Context mContext, ArrayList<RideHistoryData> contact) {
        this.mContext = mContext;
        this.orderListModel = contact;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_upcomingjobs_new, parent, false);

        getspCurrency = mContext.getSharedPreferences(Utils.PREFS_NAME_CURRENCY, MODE_PRIVATE);
        strLanguage = getspCurrency.getString(Utils.SP_LANGUAGE, null);
        strLanguageShortName = getspCurrency.getString(Utils.SP_LANGUAGE_SHORT_NAME, null);
        if (strLanguageShortName == null) {
            strLanguageShortName = "en";
        }
        return new MyViewHolder(itemView);
    }


    public void updateAdapter(List<RideHistoryData> eArrayList) {
        orderListModel.addAll(eArrayList);
        notifyDataSetChanged();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        listModel = orderListModel.get(position);

        Log.i("bind--", "onBindViewHolder-->: " + listModel.getId());
        holder.txtBookingId.setText(String.valueOf(listModel.getId()));
        holder.txtBookingDate.setText(listModel.getJobDate().toString());

        holder.txtDriverName.setText(mContext.getResources().getString(R.string.provider_name) + ":-" + listModel.getDriverFirstName());
        holder.txtDriverPhone.setText(mContext.getResources().getString(R.string.provider_phone) + ":-" + listModel.getDriverPhoneNumber());


        holder.txtBookingCategory.setText(listModel.getCategoryId() + " - " + listModel.getCategoryId());

        if (listModel.getStartDateTime() != null && !listModel.getStartDateTime().equals("null") && !listModel.getStartDateTime().equals("")) {
            System.out.println("timeStart"+listModel.getStartDateTime());
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat date12Format = new SimpleDateFormat("yyyy-MM-dd hh:mm a");

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat date24Format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            try {
                String yearTime1 = date24Format.format(Objects.requireNonNull(date12Format.parse(listModel.getStartDateTime())));
                out.println("strStartDateTimeFB"+yearTime1);
                holder.txtStartDateTime.setText(yearTime1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            holder.txtStartDateTime.setText("-");
        }

        if (listModel.getEndDateTime() != null && !listModel.getEndDateTime().equals("null") && !listModel.getEndDateTime().equals("")) {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat date12Format = new SimpleDateFormat("yyyy-MM-dd hh:mm a");

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat date24Format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            try {
                String yearTime1 = date24Format.format(Objects.requireNonNull(date12Format.parse(listModel.getEndDateTime())));
                holder.txtEndDateTime.setText(yearTime1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            holder.txtEndDateTime.setText("-");
        }

        Log.i("bind--", "getServiceStatus-->: " + listModel.getServiceStatus());

        if (listModel.getServiceStatus() == 1) {
            holder.txtStatus.setText(R.string.booking_requesting);
            holder.btnCancelJobsUpcoming.setVisibility(View.VISIBLE);
            holder.rlProviderDetailsLyt.setVisibility(View.GONE);
        } else if (listModel.getServiceStatus() == 3) {
            holder.txtStatus.setText(R.string.no_guard_available);
            holder.btnCancelJobsUpcoming.setVisibility(View.GONE);
            holder.rlProviderDetailsLyt.setVisibility(View.VISIBLE);
        } else if (listModel.getServiceStatus() == 2) {
            holder.txtStatus.setText(R.string.upcoming_job_accepted_driver);
            holder.btnCancelJobsUpcoming.setVisibility(View.GONE);
            holder.rlProviderDetailsLyt.setVisibility(View.GONE);
        }  else if (listModel.getServiceStatus() == 8) {
            holder.txtStatus.setText(R.string.upcoming_job_cancelled);
            holder.btnCancelJobsUpcoming.setVisibility(View.GONE);
            holder.rlProviderDetailsLyt.setVisibility(View.GONE);
        } else {
            holder.txtStatus.setText(R.string.upcoming_job_not_complete);
            holder.btnCancelJobsUpcoming.setVisibility(View.VISIBLE);
            holder.rlProviderDetailsLyt.setVisibility(View.GONE);
        }

        holder.btnCancelJobsUpcoming.setTag(position);
        holder.btnCancelJobsUpcoming.setOnClickListener(v -> {
            int pos = (int) v.getTag();
            RideHistoryData listModel = orderListModel.get(pos);

            HashMap<String, Object> map = new HashMap<>();
            map.put("book_id", listModel.getId());
            if (successResponse != null)
                successResponse.onSuccess(map);
        });


        holder.txtProviderAddress.setText(String.valueOf(listModel.getCustomerLocation()));
        holder.llPastjobsParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(mContext, "This functionality has Under Construction", Toast.LENGTH_SHORT).show();
            }
        });

        holder.imgDriverPhone.setTag(position);
        holder.imgDriverPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                RideHistoryData listModel = orderListModel.get(pos);
                callDriverTelephonyManager(String.valueOf(listModel.getDriverPhoneNumber()));
            }
        });

        holder.imgDriverMessage.setTag(position);
        holder.imgDriverMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                RideHistoryData listModel = orderListModel.get(pos);
                callDriverMessageManager(String.valueOf(listModel.getDriverFirstName()), String.valueOf(listModel.getDriverPhoneNumber()));
            }
        });

        /*if(listModel.getCompanyName()==null||listModel.getCompanyName().equals("null")||listModel.getCompanyName().equals("")){
            holder.txtCompanyName.setText("Company :-Individual");
        }else{
            holder.txtCompanyName.setText("Company :-"+listModel.getCompanyName());
        }
        holder.getTxtAgentDays.setText(mContext.getResources().getString(R.string.total_days)+" :- "+listModel.getTotalDays());
        holder.txtAgentCount.setText(mContext.getResources().getString(R.string.total_guard)+":- "+listModel.getTotalAgents());*/

    }

    @Override
    public int getItemCount() {
        return orderListModel.size();
    }

    private void callDriverTelephonyManager(String phoneNumber) {
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            try {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                mContext.startActivity(intent);
            } catch (ActivityNotFoundException ex) {

                Toast.makeText(mContext, R.string.no_is_not_found, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(mContext, R.string.no_mobile_num, Toast.LENGTH_SHORT).show();
        }
    }

    private void callDriverMessageManager(String drname, String phoneNumber) {

        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            try {
                try {
                    //  startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", drivermobile, null)));
                    Intent smsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("smsto:" + phoneNumber));
                    smsIntent.putExtra("sms_body", "Hi " + drname);
                    mContext.startActivity(smsIntent);

                } catch (ActivityNotFoundException e) {
                    // Display some sort of error message here.
                }
            } catch (ActivityNotFoundException ex) {

                Toast.makeText(mContext, R.string.no_is_not_found, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(mContext, R.string.no_mobile_num, Toast.LENGTH_SHORT).show();
        }
    }
  /*  public void updateList(List<Contact> list){
        orderListModel = list;
        notifyDataSetChanged();
    }*/

    public interface SuccessResponse {
        void onSuccess(HashMap<String, Object> map);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout llPastjobsParent;
        public CustomButton btnCancelJobsUpcoming;
        public CustomTextView txtBookingId, txtBookingDate, txtBookingCategory, txtProviderAddress, txtStatus, txtDriverName, txtDriverPhone, txtCompanyName, txtAgentCount, txtEndDateTime, txtStartDateTime, getTxtAgentDays;
        RelativeLayout rlProviderDetailsLyt;
        ImageButton imgDriverPhone, imgDriverMessage;

        public MyViewHolder(View view) {
            super(view);

            txtBookingId = (CustomTextView) view.findViewById(R.id.txt_bookingid);
            txtBookingDate = (CustomTextView) view.findViewById(R.id.txt_booking_date);
            txtBookingCategory = (CustomTextView) view.findViewById(R.id.txt_booking_category);
            txtProviderAddress = (CustomTextView) view.findViewById(R.id.txt_job_location);
            txtStatus = (CustomTextView) view.findViewById(R.id.txt_status);
            llPastjobsParent = (LinearLayout) view.findViewById(R.id.ll_pastjobs_parent);
            btnCancelJobsUpcoming = (CustomButton) view.findViewById(R.id.btn_canceljobs_upcoming);

            rlProviderDetailsLyt = (RelativeLayout) view.findViewById(R.id.rl_parent_lyt_provider_details);

            txtDriverName = (CustomTextView) view.findViewById(R.id.txt_Driver_name_label);
            txtDriverPhone = (CustomTextView) view.findViewById(R.id.txt_Driver_ph_label);

            imgDriverPhone = (ImageButton) view.findViewById(R.id.img_phone);
            imgDriverMessage = (ImageButton) view.findViewById(R.id.img_message);
            txtCompanyName = (CustomTextView) view.findViewById(R.id.txt_Driver_company);
            txtAgentCount = (CustomTextView) view.findViewById(R.id.txt_agent_count);
            getTxtAgentDays = (CustomTextView) view.findViewById(R.id.txt_agent_days);

            txtStartDateTime = (CustomTextView) view.findViewById(R.id.txt_start_date_time);
            txtEndDateTime = (CustomTextView) view.findViewById(R.id.txt_end_date_time);


        }
    }

}

