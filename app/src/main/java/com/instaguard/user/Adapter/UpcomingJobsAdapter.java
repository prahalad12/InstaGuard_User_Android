package com.instaguard.user.Adapter;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.instaguard.user.CustomViews.CustomButton;
import com.instaguard.user.CustomViews.CustomTextView;
import com.instaguard.user.Helpers.Utils;
import com.instaguard.user.Models.UpcomingJobsData;
import com.instaguard.user.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;


public class UpcomingJobsAdapter extends RecyclerView.Adapter<UpcomingJobsAdapter.MyViewHolder> {

    public static SuccessResponse successResponse;
    UpcomingJobsData listModel;
    SharedPreferences prefs;
    int i = 1;
    private Context mContext;
    private List<UpcomingJobsData> orderListModel;
    private SharedPreferences getspCurrency;
    private String strLanguage, strLanguageShortName;

    public UpcomingJobsAdapter(Context mContext, List<UpcomingJobsData> contact) {
        this.mContext = mContext;
        this.orderListModel = contact;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_upcomingjobs, parent, false);

        getspCurrency = mContext.getSharedPreferences(Utils.PREFS_NAME_CURRENCY, MODE_PRIVATE);
        strLanguage = getspCurrency.getString(Utils.SP_LANGUAGE, null);
        strLanguageShortName = getspCurrency.getString(Utils.SP_LANGUAGE_SHORT_NAME, null);
        if (strLanguageShortName == null) {
            strLanguageShortName = "en";
        }
        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        listModel = orderListModel.get(position);

        Log.i("bind--", "onBindViewHolder-->: " + listModel.getBookingId());
        holder.txtBookingId.setText(String.valueOf(listModel.getBookingId()));
        holder.txtBookingDate.setText(listModel.getJobDate());

        holder.txtDriverName.setText(mContext.getResources().getString(R.string.provider_name) + ":-" + listModel.getDriverName());
        holder.txtDriverPhone.setText(mContext.getResources().getString(R.string.provider_phone) + ":-" + listModel.getDriverPhoneNumber());

        if (strLanguageShortName.equals("en")) {
            holder.txtBookingCategory.setText(listModel.getCategoryName() + " - " + listModel.getServiceName());
        } else {
            if (!listModel.getCategoryAr().equals(" ")) {
                if (!listModel.getServiceAr().equals(" ")) {
                    holder.txtBookingCategory.setText(listModel.getCategoryAr() + " - " + listModel.getServiceAr());
                } else {
                    holder.txtBookingCategory.setText(listModel.getCategoryName() + " - " + listModel.getServiceName());
                }
            } else {
                holder.txtBookingCategory.setText(listModel.getCategoryName() + " - " + listModel.getServiceName());
            }
        }


        if (listModel.getStartDateTime()!=null && !listModel.getStartDateTime().equals("null")&& !listModel.getStartDateTime().equals("")) {
//            holder.txtStartDateTime.setText(listModel.getStartDateTime());
            String[] separated = listModel.getStartDateTime().split(",");
            String day = separated[0]; //Thu
            String monthDate = separated[1]; // Jan 30
            String yearTime = separated[2]; // 2020 05:12 AM"
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat date12Format = new SimpleDateFormat("yyyy hh:mm a");

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat date24Format = new SimpleDateFormat("yyyy HH:mm");

            try {
                String yearTime1 = date24Format.format(Objects.requireNonNull(date12Format.parse(yearTime)));
                holder.txtStartDateTime.setText(day+","+monthDate+","+ yearTime1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            holder.txtStartDateTime.setText("-");
        }

        if (listModel.getEndDateTime()!=null && !listModel.getEndDateTime().equals("null")&& !listModel.getEndDateTime().equals("")) {
//            holder.txtEndDateTime.setText(listModel.getEndDateTime());

            String[] separated = listModel.getEndDateTime().split(",");
            String day = separated[0]; //Thu
            String monthDate = separated[1]; // Jan 30
            String yearTime = separated[2]; // 2020 05:12 AM"
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat date12Format = new SimpleDateFormat("yyyy hh:mm a");

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat date24Format = new SimpleDateFormat("yyyy HH:mm");

            try {
                String yearTime1 = date24Format.format(Objects.requireNonNull(date12Format.parse(yearTime)));
                holder.txtEndDateTime.setText(day+","+monthDate+","+ yearTime1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            holder.txtEndDateTime.setText("-");
        }

        Log.i("bind--", "getServiceStatus-->: " + listModel.getServiceStatus());

        if (listModel.getServiceStatus() == 14) {
            holder.txtStatus.setText(R.string.upcoming_job_cancelled);
            holder.btnCancelJobsUpcoming.setVisibility(View.GONE);
            holder.rlProviderDetailsLyt.setVisibility(View.GONE);

        } else if (listModel.getServiceStatus() == 15) {
            holder.txtStatus.setText(R.string.upcoming_job_cancelled_driver);
            holder.btnCancelJobsUpcoming.setVisibility(View.GONE);
            holder.rlProviderDetailsLyt.setVisibility(View.GONE);

        } else if (listModel.getServiceStatus() == 13) {
            holder.txtStatus.setText(R.string.upcoming_job_accepted_driver);
            holder.btnCancelJobsUpcoming.setVisibility(View.GONE);
            holder.rlProviderDetailsLyt.setVisibility(View.VISIBLE);

        } else if (listModel.getServiceStatus() == 12) {
            holder.txtStatus.setText(R.string.booking_requesting);
            holder.btnCancelJobsUpcoming.setVisibility(View.VISIBLE);
            holder.rlProviderDetailsLyt.setVisibility(View.GONE);

        } else {
            holder.txtStatus.setText(R.string.upcoming_job_not_complete);
            holder.btnCancelJobsUpcoming.setVisibility(View.VISIBLE);
            holder.rlProviderDetailsLyt.setVisibility(View.GONE);
        }

        holder.btnCancelJobsUpcoming.setTag(position);
        holder.btnCancelJobsUpcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                UpcomingJobsData listModel = orderListModel.get(pos);

                HashMap<String, Object> map = new HashMap<>();
                map.put("book_id", listModel.getBookingId());
                if (successResponse != null)
                    successResponse.onSuccess(map);
            }
        });

        //  if(listModel.getDriverAddress()!=null)
        // {
        holder.txtProviderAddress.setText(String.valueOf(listModel.getCustomerLocation()));
        // }

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
                UpcomingJobsData listModel = orderListModel.get(pos);
                callDriverTelephonyManager(String.valueOf(listModel.getDriverPhoneNumber()));
            }
        });

        holder.imgDriverMessage.setTag(position);
        holder.imgDriverMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                UpcomingJobsData listModel = orderListModel.get(pos);
                callDriverMessageManager(String.valueOf(listModel.getDriverName()), String.valueOf(listModel.getDriverPhoneNumber()));
            }
        });

        if(listModel.getCompanyName()==null||listModel.getCompanyName().equals("null")||listModel.getCompanyName().equals("")){
            holder.txtCompanyName.setText("Company :-Individual");
        }else{
            holder.txtCompanyName.setText("Company :-"+listModel.getCompanyName());
        }
       // holder.getTxtAgentDays.setText("Total Days :- "+listModel.getTotalDays());
       // holder.txtAgentCount.setText("Total Agent:- "+listModel.getTotalAgents());

        holder.getTxtAgentDays.setText(mContext.getResources().getString(R.string.total_days)+" :- "+listModel.getTotalDays());
        holder.txtAgentCount.setText(mContext.getResources().getString(R.string.total_guard)+":- "+listModel.getTotalAgents());

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
        public CustomTextView txtBookingId, txtBookingDate, txtBookingCategory
                , txtProviderAddress, txtStatus, txtDriverName, txtDriverPhone,txtCompanyName,txtAgentCount,txtEndDateTime,txtStartDateTime,getTxtAgentDays;
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
            txtAgentCount= (CustomTextView) view.findViewById(R.id.txt_agent_count);
            getTxtAgentDays= (CustomTextView) view.findViewById(R.id.txt_agent_days);

            txtStartDateTime= (CustomTextView) view.findViewById(R.id.txt_start_date_time);
            txtEndDateTime= (CustomTextView) view.findViewById(R.id.txt_end_date_time);


        }
    }

}

