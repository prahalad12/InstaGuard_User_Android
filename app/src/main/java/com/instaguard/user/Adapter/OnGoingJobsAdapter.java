package com.instaguard.user.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.instaguard.user.CustomViews.CustomButton;
import com.instaguard.user.CustomViews.CustomTextView;
import com.instaguard.user.Helpers.RatingBar.SimpleRatingBar;
import com.instaguard.user.Helpers.Utils;
import com.instaguard.user.Models.OnGoingJobsData;
import com.instaguard.user.R;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;


public class OnGoingJobsAdapter extends RecyclerView.Adapter<OnGoingJobsAdapter.MyViewHolder> {

    private Context mContext;
    private List<OnGoingJobsData> orderListModel;
    public static SuccessResponse successResponse;
    OnGoingJobsData listModel;
    SharedPreferences prefs;
    DatabaseReference driverWalletDatabaseBRef, driverEstDistanceDurationDatabaseBRef;
    ValueEventListener driverWalletValueEventListener, driverEstDistanceDurationValueEventListener;
    String strdriverIdFB;
    int i = 1;

    private SharedPreferences getspCurrency;
    private String strLanguage, strLanguageShortName;

    public interface SuccessResponse {
        void onSuccess(HashMap<String, Object> map);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView itemName, itemPrice, itemQty, itemTotal;

        SimpleDraweeView imgBookingDriver;
        SimpleRatingBar rbProvider;
        public CustomButton btnCancelRequest, btnViewDetails;
        public CustomTextView txtBookingId, txtBookLaterLabel, txtBookingDate, txtBookingCategory, txtProviderName, txtProviderAddress, txtCompanyName;


        public MyViewHolder(View view) {
            super(view);

            imgBookingDriver = (SimpleDraweeView) view.findViewById(R.id.img_booking_driver);
            txtBookingId = (CustomTextView) view.findViewById(R.id.txt_bookingid);
            txtBookingDate = (CustomTextView) view.findViewById(R.id.txt_booking_date);
            txtBookingCategory = (CustomTextView) view.findViewById(R.id.txt_booking_category);
            txtProviderName = (CustomTextView) view.findViewById(R.id.txt_providername);
            txtProviderAddress = (CustomTextView) view.findViewById(R.id.txt_provideraddress);
            rbProvider = (SimpleRatingBar) view.findViewById(R.id.rb_provider);
            txtCompanyName = (CustomTextView) view.findViewById(R.id.txt_Driver_company);
            txtBookLaterLabel = (CustomTextView) view.findViewById(R.id.txt_booklater_label);

            btnViewDetails = (CustomButton) view.findViewById(R.id.btn_viewdetails);
            btnCancelRequest = (CustomButton) view.findViewById(R.id.btn_cancel_request);

        }
    }

    public OnGoingJobsAdapter(Context mContext, List<OnGoingJobsData> contact) {
        this.mContext = mContext;
        this.orderListModel = contact;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_ongoingjobs, parent, false);

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

        holder.txtProviderName.setText(listModel.getDriverName());

        if (listModel.getDriverAvatar() != null) {
            holder.imgBookingDriver.setImageURI(Utils.BASE_URL_IMAGE_UPLOADS + listModel.getDriverAvatar());
            int color = mContext.getResources().getColor(R.color.colorPrimary);
            RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
            roundingParams.setBorder(color, 1.0f);
            roundingParams.setRoundAsCircle(true);
            holder.imgBookingDriver.getHierarchy().setRoundingParams(roundingParams);
        } else {
            holder.imgBookingDriver.setImageURI("http://demo.uplogictech.com/uberx/admin/images/driver/61128183_w640_h640_rabota_voditelem_spb1.png");
            int color = mContext.getResources().getColor(R.color.colorPrimary);
            RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
            roundingParams.setBorder(color, 1.0f);
            roundingParams.setRoundAsCircle(true);
            holder.imgBookingDriver.getHierarchy().setRoundingParams(roundingParams);
        }

        // if(listModel.getCustomerLocation()!=null)
        //  {
        //  }

        holder.txtProviderAddress.setText(listModel.getCustomerLocation());

        System.out.println("listModel.getDriverId==value >>>" + listModel.getDriverId());
        //getDriverWalletAmount(listModel.getDriverId(), holder);

        if (listModel.getCompanyName() == null || listModel.getCompanyName().equals("null") || listModel.getCompanyName().equals("")) {
            holder.txtCompanyName.setText("Company :-" + "Individual");
        } else {
            holder.txtCompanyName.setText("Company :-" + listModel.getCompanyName());
        }

        if (listModel.getServiceStatus() == 13) {
            holder.btnCancelRequest.setVisibility(View.VISIBLE);
            holder.btnViewDetails.setVisibility(View.GONE);
            holder.txtBookLaterLabel.setText(R.string.upcomingjobs);
            holder.txtBookLaterLabel.setBackgroundTintList(ColorStateList.valueOf(mContext.getColor(R.color.colorPrimary)));
        } else {
            holder.btnCancelRequest.setVisibility(View.GONE);
            holder.btnViewDetails.setVisibility(View.VISIBLE);
            holder.txtBookLaterLabel.setText(R.string.ongoingjobs);
            holder.txtBookLaterLabel.setBackgroundTintList(ColorStateList.valueOf(mContext.getColor(R.color.colorAccent)));
        }

        /*System.out.println("job rating ==>"+Float.parseFloat(listModel.getRating()));
        holder.rbProvider.setRating(Float.parseFloat(listModel.getRating()));*/

        /*holder.btnCancelRequest.setTag(position);
        holder.btnCancelRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pos = (int) v.getTag();
                OnGoingJobsData listModel = orderListModel.get(pos);
                HashMap<String, Object> map = new HashMap<>();
                map.put("id", "hai");
                map.put("booking_id", String.valueOf(listModel.getBookingId()));
                map.put("customer_location",String.valueOf(listModel.getCustomerLocation()));
                if(successResponse!=null)
                    successResponse.onSuccess(map);
            }
        });*/

    }

    private void getDriverWalletAmount(String strdriverIdFB, MyViewHolder viewHolder) {
        if (driverWalletDatabaseBRef != null) {
            driverWalletDatabaseBRef.removeEventListener(driverWalletValueEventListener);
        }
        driverWalletDatabaseBRef = FirebaseDatabase.getInstance().getReference().child(Utils.FIREBASE_PROVIDERS_HEADER).child(strdriverIdFB);
        driverWalletValueEventListener = driverWalletDatabaseBRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    Object rating = dataSnapshot.child("rating").getValue();
                    if (rating != null) {
                        //ratingbarJobinprogress.setRating(Float.parseFloat(rating.toString()));
                        //  System.out.println("rating==value >>>"+Float.parseFloat(rating.toString()));
                        viewHolder.rbProvider.setRating(Float.parseFloat(rating.toString()));
                    } else {
                        //ratingbarJobinprogress.setRating(0);
                        viewHolder.rbProvider.setRating(0);
                    }
                    if (driverWalletDatabaseBRef != null) {
                        driverWalletDatabaseBRef.removeEventListener(driverWalletValueEventListener);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("TAG", "onCancelled: ~~~~" + databaseError.toString());
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderListModel.size();
    }


  /*  public void updateList(List<Contact> list){
        orderListModel = list;
        notifyDataSetChanged();
    }*/


    //get wallet amount & rating in firebase

}

