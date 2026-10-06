package com.instaguard.user.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.instaguard.user.Activities.BaseActivity;
import com.instaguard.user.Activities.HomeActivity;
import com.instaguard.user.CustomViews.CustomButton;
import com.instaguard.user.CustomViews.CustomEditText;
import com.instaguard.user.CustomViews.CustomTextView;
import com.instaguard.user.Helpers.DecimalUtils;
import com.instaguard.user.Helpers.RatingBar.SimpleRatingBar;
import com.instaguard.user.Helpers.Retrofit.APIClient;
import com.instaguard.user.Helpers.Retrofit.APIInterface;
import com.instaguard.user.Helpers.Utils;
import com.instaguard.user.Models.PastJobsData;
import com.instaguard.user.Models.RatingDriverModel;
import com.instaguard.user.Models.RefreshTokenModel;
import com.instaguard.user.R;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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


public class PastJobsAdapter extends RecyclerView.Adapter<PastJobsAdapter.MyViewHolder> {

    private Context mContext;
    private List<PastJobsData> orderListModel;
    private static SuccessResponse successResponse;
    SharedPreferences prefs;
    private Dialog dialogShowTripDetail;

    private SharedPreferences getspCommonState, getspCommonCurrency;
    private String userId, strLanguage, strLanguageShortName, strCurrencySymbol, userAccessToken;
    private SharedPreferences.Editor putSpState;


    private DatabaseReference driverWalletDatabaseBRef;
    private ValueEventListener driverWalletValueEventListener;

    int i = 1;

    public interface SuccessResponse {
        void onSuccess(HashMap<String, Object> map);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llPastjobsParent;
        CardView cvPastjobsParent;
        CustomTextView txtBookingId, txtBookingDate, txtBookingCategory, txtProviderAddress, txtStatus, txtFreeLabel;


        MyViewHolder(View view) {
            super(view);

            txtBookingId = (CustomTextView) view.findViewById(R.id.txt_bookingid);
            txtBookingDate = (CustomTextView) view.findViewById(R.id.txt_booking_date);
            txtBookingCategory = (CustomTextView) view.findViewById(R.id.txt_booking_category);
            txtProviderAddress = (CustomTextView) view.findViewById(R.id.txt_job_location);
            txtStatus = (CustomTextView) view.findViewById(R.id.txt_status);
            llPastjobsParent = (LinearLayout) view.findViewById(R.id.ll_pastjobs_parent);
            cvPastjobsParent = (CardView) view.findViewById(R.id.cv_pastjobs_parent);

            txtFreeLabel = view.findViewById(R.id.txt_free_label);

        }
    }

    public PastJobsAdapter(Context mContext, List<PastJobsData> contact) {
        this.mContext = mContext;
        this.orderListModel = contact;
    }

    @SuppressLint("CommitPrefEdits")
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_pastjobs, parent, false);

        putSpState = mContext.getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE).edit();

        getspCommonState = mContext.getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE);
        getspCommonCurrency = mContext.getSharedPreferences(Utils.PREFS_NAME_CURRENCY, MODE_PRIVATE);

        userId = getspCommonState.getString(Utils.SP_USERID, null);
        userAccessToken = getspCommonState.getString(Utils.SP_USER_ACCESSTOKEN, null);

        strLanguage = getspCommonCurrency.getString(Utils.SP_LANGUAGE, null);
        strLanguageShortName = getspCommonCurrency.getString(Utils.SP_LANGUAGE_SHORT_NAME, null);
        if (strLanguageShortName == null) {
            strLanguageShortName = "en";
        }

        strCurrencySymbol = getspCommonCurrency.getString(Utils.SP_CURRENCY_SYMBOL, null);
        Log.e("onCreateViewHolder: ", strCurrencySymbol);


        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        PastJobsData listModel = orderListModel.get(position);
        //holder.txtBookingDate.setText(listModel.getJobDate());

        final String dateList;
        String dateListEs = null;
        dateList = listModel.getJobDate(); //"Thu,Jan 30,2020 05:12 AM",
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

            System.out.println("yearTime1"+yearTime1);

            System.out.println("yearTime"+date24Format);

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
                    dayLabel = mContext.getResources().getString(R.string.Sunday_3);
                    break;
                case "Mon":
                case "mon":
                    dayLabel = mContext.getResources().getString(R.string.Monday_3);
                    break;
                case "Tue":
                case "tue":
                    dayLabel = mContext.getResources().getString(R.string.Tuesday_3);
                    break;
                case "Wed":
                case "wed":
                    dayLabel = mContext.getResources().getString(R.string.Wednesday_3);
                    break;
                case "Thu":
                case "thu":
                    dayLabel = mContext.getResources().getString(R.string.Thursday_3);
                    break;
                case "Fri":
                case "fri":
                    dayLabel = mContext.getResources().getString(R.string.Friday_3);
                    break;
                case "Sat":
                case "sat":
                    dayLabel = mContext.getResources().getString(R.string.Saterday_3);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + day);
            }

            String monthLabel = "-";
            switch (month) {
                case "Jan":
                case "jan":
                    monthLabel = mContext.getResources().getString(R.string.January_3);
                    break;
                case "Feb":
                case "feb":
                    monthLabel = mContext.getResources().getString(R.string.February_3);
                    break;
                case "Mar":
                case "mar":
                    monthLabel = mContext.getResources().getString(R.string.March_3);
                    break;
                case "Apr":
                case "apr":
                    monthLabel = mContext.getResources().getString(R.string.April_3);
                    break;
                case "May":
                case "may":
                    monthLabel = mContext.getResources().getString(R.string.May_3);
                    break;
                case "Jun":
                case "jun":
                    monthLabel = mContext.getResources().getString(R.string.June_3);
                    break;
                case "Jul":
                case "jul":
                    monthLabel = mContext.getResources().getString(R.string.July_3);
                    break;
                case "Aug":
                case "aug":
                    monthLabel = mContext.getResources().getString(R.string.August_3);
                    break;
                case "Sep":
                case "sep":
                    monthLabel = mContext.getResources().getString(R.string.September_3);
                    break;
                case "Oct":
                case "oct":
                    monthLabel = mContext.getResources().getString(R.string.October_3);
                    break;
                case "Nov":
                case "nov":
                    monthLabel = mContext.getResources().getString(R.string.November_3);
                    break;
                case "Dec":
                case "dec":
                    monthLabel = mContext.getResources().getString(R.string.December_3);
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
            holder.txtBookingDate.setText(dateListEs);
        } else {
            holder.txtBookingDate.setText(dateList);
        }

       /* } else {
            holder.txtBookingDate.setText(dateList);
        }*/


        Log.i("bind--", "onBindViewHolder-->: " + listModel.getBookingId());
        holder.txtBookingId.setText(String.valueOf(listModel.getBookingId()));

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

        if (listModel.getServiceStatus() == 7) {
            holder.txtStatus.setText(R.string.past_job_finished);
            holder.txtStatus.setTextColor(mContext.getResources().getColor(R.color.holo_green_dark));
            holder.cvPastjobsParent.setEnabled(true);
        } else if (listModel.getServiceStatus() == 6) {
            holder.txtStatus.setText(R.string.please_wait_customer_accept_your_completion);
            holder.txtStatus.setTextColor(mContext.getResources().getColor(R.color.field_text_color_error));
            holder.cvPastjobsParent.setEnabled(true);
        } else if (listModel.getServiceStatus() == 8) {
            holder.txtStatus.setText(R.string.upcoming_job_cancelled);
            holder.txtStatus.setTextColor(mContext.getResources().getColor(R.color.field_text_color_error));
            holder.cvPastjobsParent.setEnabled(false);
        } else if (listModel.getServiceStatus() == 9) {
            holder.txtStatus.setText(R.string.upcoming_job_cancelled_driver);
            holder.txtStatus.setTextColor(mContext.getResources().getColor(R.color.field_text_color_error));
            holder.cvPastjobsParent.setEnabled(false);
        } else if (listModel.getServiceStatus() == 14) {
            holder.txtStatus.setText(R.string.upcoming_scheduled_cancel_user);
            holder.txtStatus.setTextColor(mContext.getResources().getColor(R.color.field_text_color_error));
            holder.cvPastjobsParent.setEnabled(false);
        } else if (listModel.getServiceStatus() == 15) {
            holder.txtStatus.setText(R.string.upcoming_scheduled_cancel_driver);
            holder.txtStatus.setTextColor(mContext.getResources().getColor(R.color.field_text_color_error));
            holder.cvPastjobsParent.setEnabled(false);
        } else {
            holder.txtStatus.setText(R.string.past_job_not_complete);
            holder.txtStatus.setTextColor(mContext.getResources().getColor(R.color.field_text_color_error));
            holder.cvPastjobsParent.setEnabled(false);
        }

        holder.txtProviderAddress.setText(String.valueOf(listModel.getCustomerLocation()));

        holder.llPastjobsParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("id", "hai");
                if (successResponse != null)
                    successResponse.onSuccess(map);
                // Toast.makeText(mContext, "This functionality has Under Construction", Toast.LENGTH_SHORT).show();
            }
        });

        if (listModel.getSubscriptionStatus() != null && !listModel.getSubscriptionStatus().equals("null")
                && !listModel.getSubscriptionStatus().equals("")) {
            if (listModel.getSubscriptionStatus().toString().equals("1")
                    || listModel.getSubscriptionStatus().toString().equals("1.0")) {
                holder.txtFreeLabel.setVisibility(View.VISIBLE);
                holder.txtFreeLabel.setText(mContext.getResources().getString(R.string.free_trip));
            } else {
                /*if (listModel.getSubscriptionMaximumDiscountAmount() != null
                        && !listModel.getSubscriptionMaximumDiscountAmount().toString().equals("null")
                        && !listModel.getSubscriptionMaximumDiscountAmount().toString().equals("")
                        && !listModel.getSubscriptionMaximumDiscountAmount().toString().equals(" ")) {
                    if (Double.parseDouble(listModel.getSubscriptionMaximumDiscountAmount().toString()) > 0.0) {
                        holder.txtFreeLabel.setVisibility(View.VISIBLE);
                        holder.txtFreeLabel.setText(mContext.getResources().getString(R.string.free_job__discount));
                    } else {
                        holder.txtFreeLabel.setVisibility(View.INVISIBLE);
                    }
                } else {*/
                holder.txtFreeLabel.setVisibility(View.INVISIBLE);
                /*}*/
            }
        } else {
            holder.txtFreeLabel.setVisibility(View.INVISIBLE);
        }

        /*holder.cvPastjobsParent.setTag(position);
        holder.cvPastjobsParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                PastJobsData listModel = orderListModel.get(pos);
                showDialogTripDetails(String.valueOf(listModel.getBookingId()),listModel.getAcceptTime(), listModel.getCategoryName(), listModel.getCustomerLocation(),
                        listModel.getCustomerName(), String.valueOf(listModel.getDriverDiscount()), listModel.getDriverName(), String.valueOf(listModel.getExtraCharge()),
                        listModel.getJobDate(), listModel.getJobNumber(), listModel.getJobType(), String.valueOf(listModel.getMiscCharge()), listModel.getPaymentType(),
                        listModel.getRating(), listModel.getServiceName(), listModel.getServiceStatus(), String.valueOf(listModel.getSiteCommission()),
                        String.valueOf(listModel.getTotalAmount()), String.valueOf(listModel.getUnitPrice()),String.valueOf(listModel.getDriverId()),
                        String.valueOf(listModel.getCustomerId()),listModel.getRatingStatus(),listModel.getDriverAvatar(),
                        String.valueOf(listModel.getPromoDiscount()), String.valueOf(listModel.getPromocode()),String.valueOf(listModel.getLevelType()),
                        String.valueOf(listModel.getMainLevelAmount()),String.valueOf(listModel.getSubLevelAmount()),
                        String.valueOf(listModel.getVisitFare()),String.valueOf(listModel.getPricePerKm()),String.valueOf(listModel.getTotalDistance()),
                        String.valueOf(listModel.getCategoryAr()),String.valueOf(listModel.getServiceAr()),String.valueOf(listModel.getCompanyName()),String.valueOf(listModel.getTotalAgents()),String.valueOf(listModel.getPriceperAgent()),String.valueOf(listModel.getTotalDays()),String.valueOf(listModel.getPriceperDay()));
            }
        });*/

    }


    //get wallet amount & rating in firebase
//    private void getDriverWalletAmount(String driverid) {
//        if (driverWalletDatabaseBRef != null) {
//            driverWalletDatabaseBRef.removeEventListener(driverWalletValueEventListener);
//        }
//        driverWalletDatabaseBRef = FirebaseDatabase.getInstance().getReference().child(Utils.FIREBASE_PROVIDERS_HEADER).child(driverid);
//        driverWalletValueEventListener = driverWalletDatabaseBRef.addValueEventListener(new ValueEventListener() {
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.getValue() != null) {
//
//                    Object rating = dataSnapshot.child("rating").getValue();
//                    Object pro_pic = dataSnapshot.child("pro_pic").getValue();
//
//                    try {
//
//                        ivprofpicPastjobdetail.setImageURI(Utils.BASE_URL+ String.valueOf(pro_pic));
//                        int color = mContext.getResources().getColor(R.color.colorPrimary);
//                        RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
//                        roundingParams.setBorder(color, 1.0f);
//                        roundingParams.setRoundAsCircle(true);
//                        ivprofpicPastjobdetail.getHierarchy().setRoundingParams(roundingParams);
//
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//
//                    if(rating!=null){
//                        ratingBar_Driver_pastjob_detail.setRating(Float.parseFloat(rating.toString()));
//                    }else{
//                        ratingBar_Driver_pastjob_detail.setRating(0);
//                    }
//                    if (driverWalletDatabaseBRef != null) {
//                        driverWalletDatabaseBRef.removeEventListener(driverWalletValueEventListener);
//                    }
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//    }


    @Override
    public int getItemCount() {
        return orderListModel.size();
    }
}

