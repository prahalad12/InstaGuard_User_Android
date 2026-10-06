package com.instaguard.user.Adapter;

import static android.content.Context.MODE_PRIVATE;
import static com.instaguard.user.Activities.BaseActivity.setVisibility;
import static java.lang.System.out;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.instaguard.user.CustomViews.CustomButton;
import com.instaguard.user.CustomViews.CustomEditText;
import com.instaguard.user.CustomViews.CustomTextView;
import com.instaguard.user.Helpers.RatingBar.SimpleRatingBar;
import com.instaguard.user.Helpers.Utils;
import com.instaguard.user.Models.TripsFirebaseGuardListModel;
import com.instaguard.user.R;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class TripFirebaseGuardListRatingAdapter extends RecyclerView.Adapter<TripFirebaseGuardListRatingAdapter.MyViewHolder> {

    private final Context mContext;
    private final String targetItemId;
    private final List<TripsFirebaseGuardListModel> orderListModel;
    public static SuccessResponseRating successResponse;
    SharedPreferences spCurrency;
    String strCurrency, strCurrencySymbol, strLanguage;

    public interface SuccessResponseRating {
        void onSuccessTripFirebaseGuardListRating(HashMap<String, Object> map);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView ivGuardProfPic_multi;
        SimpleRatingBar ratingGuard;
        CustomTextView txtCount_multi, txtGuardName_multi;
        SimpleRatingBar rbarGuardRating_multi;
        CustomEditText edtCommentGuard;
        CustomButton btnRateGuard;
        View rlParentLytRateView;
        CustomTextView successRateView;

        MyViewHolder(View view) {
            super(view);

            ivGuardProfPic_multi = view.findViewById(R.id.iv_user_prof_pic_job_inprogress);
            txtCount_multi = view.findViewById(R.id.iv_count);
            txtGuardName_multi = view.findViewById(R.id.txt_username_job_inprogress);
            rbarGuardRating_multi = view.findViewById(R.id.ratingbar_job_inprogress);

            ratingGuard = view.findViewById(R.id.ratingbar_trip_summary);
            edtCommentGuard = view.findViewById(R.id.edt_comment_trip_summary);
            btnRateGuard = view.findViewById(R.id.btn_rating_payment);
            rlParentLytRateView = view.findViewById(R.id.rl_parent_rating_edt_rate_lyt);
            successRateView = view.findViewById(R.id.ufxratingDriverHTxt);
        }
    }

    public TripFirebaseGuardListRatingAdapter(Context mContext, List<TripsFirebaseGuardListModel> contact, String targetItemId) {
        this.mContext = mContext;
        this.orderListModel = contact;
        this.targetItemId = targetItemId;
    }

    @SuppressLint("CommitPrefEdits")
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_guard_rating_view, parent, false);
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        TripsFirebaseGuardListModel firebaseGuardListModel = orderListModel.get(position);

        // String status = String.valueOf(categoryModel.getStatus());
        //String strBookingId = String.valueOf(categoryModel.getBookingId());

        String getTrip_status = String.valueOf(firebaseGuardListModel.getTrip_status());

        String getDriver_avatar = String.valueOf(firebaseGuardListModel.getDriver_avatar());
        String getDriver_first = String.valueOf(firebaseGuardListModel.getDriver_first());
        String getDriver_last = String.valueOf(firebaseGuardListModel.getDriver_last());

        out.println("setBlogPost: ~~~~~getDriver_first~~~~" + getDriver_first);
        out.println("setBlogPost: ~~~~~getDriver_last~~~~" + getDriver_last);
        out.println("setBlogPost: ~~~~~targetItemId~~~~" + targetItemId);

        holder.txtGuardName_multi.setText(getDriver_first + " " + getDriver_last);
        holder.txtCount_multi.setText("" + (position + 1));

        // Find and update the rating_status to '1' for a specific item
        //String targetItemId = "2"; // Replace with the actual ID you are looking for
        if (!targetItemId.equals("0")) {
            for (TripsFirebaseGuardListModel item : orderListModel) {
                if (item.getDriver_id().equals(targetItemId)) {
                    // Update the rating_status to '1'
                    item.setRating_status("1");
                    break; // Exit the loop once the item is found and updated
                }
            }


            // Display the updated list
            for (TripsFirebaseGuardListModel item : orderListModel) {
                System.out.println("Item ID: " + item.getDriver_id() + ", Rating Status: " + item.getRating_status());

                if (item.getRating_status().equals("1")) {
                    holder.successRateView.setVisibility(View.VISIBLE);
                    holder.rlParentLytRateView.setVisibility(View.GONE);
                } else {
                    holder.successRateView.setVisibility(View.GONE);
                    holder.rlParentLytRateView.setVisibility(View.VISIBLE);
                }
            }
        }

        out.println("RatingStatus::"+firebaseGuardListModel.getRating_status());

        if (firebaseGuardListModel.getRating_status().equals("1")) {
            holder.successRateView.setVisibility(View.VISIBLE);
            holder.rlParentLytRateView.setVisibility(View.GONE);
        } else {
            holder.successRateView.setVisibility(View.GONE);
            holder.rlParentLytRateView.setVisibility(View.VISIBLE);
        }

        System.out.println("Item ID: " + firebaseGuardListModel.getDriver_id() + ", Rating Status: " + firebaseGuardListModel.getRating_status());

        switch (getTrip_status) {
            case "9": //cancel
                holder.successRateView.setVisibility(View.VISIBLE);
                holder.rlParentLytRateView.setVisibility(View.GONE);
                holder.successRateView.setText(mContext.getResources().getString(R.string.upcoming_job_cancelled_driver));
                break;
            default:
                if (firebaseGuardListModel.getRating_status().equals("1")) {
                    holder.successRateView.setVisibility(View.VISIBLE);
                    holder.rlParentLytRateView.setVisibility(View.GONE);
                    holder.successRateView.setText(mContext.getResources().getString(R.string.review_successfully_updated));
                } else {
                    holder.successRateView.setVisibility(View.GONE);
                    holder.rlParentLytRateView.setVisibility(View.VISIBLE);
                }
                break;
        }


        holder.btnRateGuard.setTag(position);

        holder.btnRateGuard.setOnClickListener(view -> {
            if (Objects.requireNonNull(holder.edtCommentGuard.getText()).toString().length() == 0) {
                holder.edtCommentGuard.setError(mContext.getResources().getString(R.string.error_required));
            } else {

                String ratings = String.valueOf(holder.ratingGuard.getRating());
                String edtcomments = holder.edtCommentGuard.getText().toString();

                int pos = (int) view.getTag();
                TripsFirebaseGuardListModel listModel = orderListModel.get(pos);
                HashMap<String, Object> map = new HashMap<>();
                map.put("from", "rate");
                map.put("data", listModel);
                map.put("edtComments", edtcomments);
                map.put("ratings", ratings);
                if (successResponse != null)
                    successResponse.onSuccessTripFirebaseGuardListRating(map);
            }
        });

        int color = mContext.getResources().getColor(R.color.white);
        RoundingParams roundingParams = RoundingParams.fromCornersRadius(10f);
        roundingParams.setBorder(color, 1.5f);
        roundingParams.setRoundAsCircle(true);

        holder.ivGuardProfPic_multi.setImageURI(Utils.BASE_URL_IMAGE_UPLOADS + getDriver_avatar);
        holder.ivGuardProfPic_multi.getHierarchy().setRoundingParams(roundingParams);

    }

    @Override
    public int getItemCount() {
        out.println("RatingListModelSize "+ orderListModel.size());
        return orderListModel.size();
    }

}

