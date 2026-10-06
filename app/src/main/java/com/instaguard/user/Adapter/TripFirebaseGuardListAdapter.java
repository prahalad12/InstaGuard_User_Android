package com.instaguard.user.Adapter;

import static android.content.Context.MODE_PRIVATE;

import static com.instaguard.user.Activities.BaseActivity.callClickAction;
import static com.instaguard.user.Activities.BaseActivity.setVisibility;
import static java.lang.System.out;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.instaguard.user.CustomViews.CustomTextView;
import com.instaguard.user.Helpers.RatingBar.SimpleRatingBar;
import com.instaguard.user.Helpers.Utils;
import com.instaguard.user.Models.AppSettingCurrencyModel;
import com.instaguard.user.Models.TripsFirebaseGuardListModel;
import com.instaguard.user.R;

import java.util.HashMap;
import java.util.List;


public class TripFirebaseGuardListAdapter extends RecyclerView.Adapter<TripFirebaseGuardListAdapter.MyViewHolder> {

    private final Context mContext;
    private final List<TripsFirebaseGuardListModel> orderListModel;
    public static SuccessResponse successResponse;
    SharedPreferences spCurrency;
    String strCurrency, strCurrencySymbol, strLanguage;

    public interface SuccessResponse {
        void onSuccessTripFirebaseGuardList(HashMap<String, Object> map);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView ivGuardProfPic_multi;
        CustomTextView txtCount_multi, txtGuardName_multi, txtOneMessage_multi, txtOneTime_multi,
                txtTwoMessage_multi, txtTwoTime_multi,
                txtThreeMessage_multi, txtThreeTime_multi,
                txtFourMessage_multi, txtFourTime_multi,
                txtFiveMessage_multi, txtFiveTime_multi,
                txtSixMessage_multi, txtSixTime_multi;
        SimpleRatingBar rbarGuardRating_multi;

        View llOneTripStatus_multi;
        View llTwoTripStatus_multi;
        View llThreeTripStatus_multi;
        View llFourTripStatus_multi;
        View llFiveTripStatus_multi;
        View llSixTripStatus_multi;
        View ivPhone_multi;
        View ivMessage_multi;

        MyViewHolder(View view) {
            super(view);

            ivGuardProfPic_multi = view.findViewById(R.id.iv_user_prof_pic_job_inprogress);
            txtCount_multi = view.findViewById(R.id.iv_count);
            txtGuardName_multi = view.findViewById(R.id.txt_username_job_inprogress);
            rbarGuardRating_multi = view.findViewById(R.id.ratingbar_job_inprogress);

            llOneTripStatus_multi = view.findViewById(R.id.ll_trip_one);
            txtOneMessage_multi = view.findViewById(R.id.txt_accept_one);
            txtOneTime_multi = view.findViewById(R.id.txt_accept_time_one);

            llTwoTripStatus_multi = view.findViewById(R.id.ll_trip_two);
            txtTwoMessage_multi = view.findViewById(R.id.txt_accept_two);
            txtTwoTime_multi = view.findViewById(R.id.txt_accept_time_two);

            llThreeTripStatus_multi = view.findViewById(R.id.ll_trip_three);
            txtThreeMessage_multi = view.findViewById(R.id.txt_accept_three);
            txtThreeTime_multi = view.findViewById(R.id.txt_accept_time_three);

            llFourTripStatus_multi = view.findViewById(R.id.ll_trip_four);
            txtFourMessage_multi = view.findViewById(R.id.txt_accept_four);
            txtFourTime_multi = view.findViewById(R.id.txt_accept_time_four);

            llFiveTripStatus_multi = view.findViewById(R.id.ll_trip_five);
            txtFiveMessage_multi = view.findViewById(R.id.txt_accept_five);
            txtFiveTime_multi = view.findViewById(R.id.txt_accept_time_five);

            llSixTripStatus_multi = view.findViewById(R.id.ll_trip_six);
            txtSixMessage_multi = view.findViewById(R.id.txt_accept_six);
            txtSixTime_multi = view.findViewById(R.id.txt_accept_time_six);

            ivPhone_multi = view.findViewById(R.id.img_phone);
            ivMessage_multi = view.findViewById(R.id.img_message);

        }
    }

    public TripFirebaseGuardListAdapter(Context mContext, List<TripsFirebaseGuardListModel> contact) {
        this.mContext = mContext;
        this.orderListModel = contact;
    }

    @SuppressLint("CommitPrefEdits")
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_guard_details_list_main, parent, false);
        spCurrency = mContext.getSharedPreferences(Utils.PREFS_NAME_CURRENCY, MODE_PRIVATE);

        strCurrency = spCurrency.getString(Utils.SP_CURRENCY, null);
        strCurrencySymbol = spCurrency.getString(Utils.SP_CURRENCY_SYMBOL, null);
        strLanguage = spCurrency.getString(Utils.SP_LANGUAGE, null);

        if (strLanguage == null) {
            strLanguage = "English";
        }

        if (strCurrency == null) {
            strCurrency = "USD";
        }
        return new MyViewHolder(itemView);
    }

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        TripsFirebaseGuardListModel firebaseGuardListModel = orderListModel.get(position);

        // String status = String.valueOf(categoryModel.getStatus());
        //String strBookingId = String.valueOf(categoryModel.getBookingId());

        String getDriver_avatar = String.valueOf(firebaseGuardListModel.getDriver_avatar());
        String getDriver_first = String.valueOf(firebaseGuardListModel.getDriver_first());
        String getDriver_last = String.valueOf(firebaseGuardListModel.getDriver_last());
        String getTrip_status = String.valueOf(firebaseGuardListModel.getTrip_status());

        String getAccept_time = String.valueOf(firebaseGuardListModel.getAccept_time());
        String getStart_time = String.valueOf(firebaseGuardListModel.getStart_time());
        String getReach_time = String.valueOf(firebaseGuardListModel.getReach_time());
        String getService_start_time = String.valueOf(firebaseGuardListModel.getService_start_time());

        try {

            out.println("setBlogPost: ~~~~~getDriver_first~~~~" + getDriver_first);
            out.println("setBlogPost: ~~~~~getDriver_last~~~~" + getDriver_last);

            holder.txtGuardName_multi.setText(getDriver_first + " " + getDriver_last);
            holder.txtCount_multi.setText("" + (position + 1));

            holder.rbarGuardRating_multi.setOnTouchListener((v, event) -> true);


            switch (getTrip_status) {
                case "2": //accept
                    callClickAction(holder.llOneTripStatus_multi, true);
                    callClickAction(holder.llTwoTripStatus_multi, false);
                    callClickAction(holder.llThreeTripStatus_multi, false);
                    callClickAction(holder.llFourTripStatus_multi, false);
                    callClickAction(holder.llFiveTripStatus_multi, false);
                    callClickAction(holder.llSixTripStatus_multi, false);

                    holder.txtOneTime_multi.setText(getAccept_time);

                    holder.ivPhone_multi.setVisibility(View.VISIBLE);
                    holder.ivMessage_multi.setVisibility(View.VISIBLE);

                    break;
                case "3": //start
                    callClickAction(holder.llOneTripStatus_multi, true);
                    callClickAction(holder.llTwoTripStatus_multi, true);
                    callClickAction(holder.llThreeTripStatus_multi, false);
                    callClickAction(holder.llFourTripStatus_multi, false);
                    callClickAction(holder.llFiveTripStatus_multi, false);
                    callClickAction(holder.llSixTripStatus_multi, false);

                    holder.txtOneTime_multi.setText(getAccept_time);
                    holder.txtTwoTime_multi.setText(getStart_time);

                    holder.ivPhone_multi.setVisibility(View.VISIBLE);
                    holder.ivMessage_multi.setVisibility(View.VISIBLE);


                    break;
                case "4": //arrivied
                    callClickAction(holder.llOneTripStatus_multi, true);
                    callClickAction(holder.llTwoTripStatus_multi, true);
                    callClickAction(holder.llThreeTripStatus_multi, true);
                    callClickAction(holder.llFourTripStatus_multi, false);
                    callClickAction(holder.llFiveTripStatus_multi, false);
                    callClickAction(holder.llSixTripStatus_multi, false);

                    holder.txtOneTime_multi.setText(getAccept_time);
                    holder.txtTwoTime_multi.setText(getStart_time);
                    holder.txtThreeTime_multi.setText(getReach_time);

                    holder.ivPhone_multi.setVisibility(View.VISIBLE);
                    holder.ivMessage_multi.setVisibility(View.VISIBLE);

                    break;
                case "5": //begin
                    callClickAction(holder.llOneTripStatus_multi, true);
                    callClickAction(holder.llTwoTripStatus_multi, true);
                    callClickAction(holder.llThreeTripStatus_multi, true);
                    callClickAction(holder.llFourTripStatus_multi, true);
                    callClickAction(holder.llFiveTripStatus_multi, false);
                    callClickAction(holder.llSixTripStatus_multi, false);

                    holder.txtOneTime_multi.setText(getAccept_time);
                    holder.txtTwoTime_multi.setText(getStart_time);
                    holder.txtThreeTime_multi.setText(getReach_time);
                    holder.txtFourTime_multi.setText(getService_start_time);

                    holder.ivPhone_multi.setVisibility(View.VISIBLE);
                    holder.ivMessage_multi.setVisibility(View.VISIBLE);

                    break;
                case "6": //end
                    callClickAction(holder.llOneTripStatus_multi, true);
                    callClickAction(holder.llTwoTripStatus_multi, true);
                    callClickAction(holder.llThreeTripStatus_multi, true);
                    callClickAction(holder.llFourTripStatus_multi, true);
                    callClickAction(holder.llFiveTripStatus_multi, true);
                    callClickAction(holder.llSixTripStatus_multi, false);

                    holder.txtOneTime_multi.setText(getAccept_time);
                    holder.txtTwoTime_multi.setText(getStart_time);
                    holder.txtThreeTime_multi.setText(getReach_time);
                    holder.txtFourTime_multi.setText(getService_start_time);

                    holder.ivPhone_multi.setVisibility(View.INVISIBLE);
                    holder.ivMessage_multi.setVisibility(View.INVISIBLE);

                    break;

                case "9":

                    callClickAction(holder.llOneTripStatus_multi, false);
                    callClickAction(holder.llTwoTripStatus_multi, false);
                    callClickAction(holder.llThreeTripStatus_multi, false);
                    callClickAction(holder.llFourTripStatus_multi, false);
                    callClickAction(holder.llFiveTripStatus_multi, false);
                    callClickAction(holder.llSixTripStatus_multi, true);

                    holder.ivPhone_multi.setVisibility(View.INVISIBLE);
                    holder.ivMessage_multi.setVisibility(View.INVISIBLE);

                    break;
            }


            holder.ivPhone_multi.setTag(position);
            holder.ivPhone_multi.setOnClickListener(view -> {
                int pos = (int) view.getTag();
                TripsFirebaseGuardListModel listModel = orderListModel.get(pos);
                HashMap<String, Object> map = new HashMap<>();
                map.put("from", "phone");
                map.put("data", listModel);
                if (successResponse != null)
                    successResponse.onSuccessTripFirebaseGuardList(map);
            });

            holder.ivMessage_multi.setTag(position);
            holder.ivMessage_multi.setOnClickListener(view -> {
                int pos = (int) view.getTag();
                TripsFirebaseGuardListModel listModel = orderListModel.get(pos);
                HashMap<String, Object> map = new HashMap<>();
                map.put("from", "chat");
                map.put("data", listModel);
                if (successResponse != null)
                    successResponse.onSuccessTripFirebaseGuardList(map);
            });


            int color = mContext.getResources().getColor(R.color.white);
            RoundingParams roundingParams = RoundingParams.fromCornersRadius(10f);
            roundingParams.setBorder(color, 1.5f);
            roundingParams.setRoundAsCircle(true);

            holder.ivGuardProfPic_multi.setImageURI(Utils.BASE_URL_IMAGE_UPLOADS + getDriver_avatar);
            holder.ivGuardProfPic_multi.getHierarchy().setRoundingParams(roundingParams);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return orderListModel.size();
    }

}

