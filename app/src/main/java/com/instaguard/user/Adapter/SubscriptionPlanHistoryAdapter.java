package com.instaguard.user.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.instaguard.user.CustomViews.CustomTextView;
import com.instaguard.user.Helpers.DecimalUtils;
import com.instaguard.user.Helpers.Utils;
import com.instaguard.user.Models.Subscription.SubsHistoryData;
import com.instaguard.user.R;

import java.util.HashMap;
import java.util.List;


public class SubscriptionPlanHistoryAdapter extends RecyclerView.Adapter<SubscriptionPlanHistoryAdapter.MyViewHolder> {

    private final Context mContext;
    private final List<SubsHistoryData> orderListModel;

    public static SuccessResponse successResponse;

    public static int selected_pos = -1;

    SharedPreferences spCurrency;
    String strCurrency, strCurrencySymbol, strLanguage;

    public interface SuccessResponse {
        void onSuccess(HashMap<String, Object> map);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        View llPastjobsParent;
        CustomTextView txtOrderId;
        CustomTextView txtTitle;
        CustomTextView txtPlanPeriod;
        CustomTextView txtPurchaseDate;
        CustomTextView txtExpire;
        CustomTextView txtAmount;
        CustomTextView txtActiveExpire;
        CustomTextView txtMoreInfo;
        CustomTextView txtDisableAutoRenew;
        ImageView imgActive;

        MyViewHolder(View view) {
            super(view);
            txtOrderId = view.findViewById(R.id.txt_orderId);
            txtTitle = view.findViewById(R.id.txt_title);
            txtPlanPeriod = view.findViewById(R.id.txt_plan_period);
            txtPurchaseDate = view.findViewById(R.id.txt_date);
            txtExpire = view.findViewById(R.id.txt_date_expiry);
            txtAmount = view.findViewById(R.id.txt_message);
            txtActiveExpire = view.findViewById(R.id.txt_active_expire);
            txtMoreInfo = view.findViewById(R.id.txt_more_info);
            txtDisableAutoRenew = view.findViewById(R.id.txt_disable_auto_renew);
            imgActive = view.findViewById(R.id.img_check);
            llPastjobsParent = view.findViewById(R.id.rl_top_subs_plan_lyt);
        }
    }

    public SubscriptionPlanHistoryAdapter(Context mContext, List<SubsHistoryData> contact) {
        this.mContext = mContext;
        this.orderListModel = contact;
    }

    @SuppressLint("CommitPrefEdits")
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_subscription_plan_view_history, parent, false);
        spCurrency = mContext.getSharedPreferences(Utils.PREFS_NAME_CURRENCY, MODE_PRIVATE);
        strCurrency = spCurrency.getString(Utils.SP_CURRENCY, null);
        strCurrencySymbol = spCurrency.getString(Utils.SP_CURRENCY_SYMBOL, null);
        strLanguage = spCurrency.getString(Utils.SP_LANGUAGE, null);

        if (strLanguage == null) {
            strLanguage = "English";
        }

        if (strCurrency == null) {
            strCurrency = "CAD";
        }

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        SubsHistoryData listModel = orderListModel.get(position);

        if (listModel.getName() != null && !listModel.getName().equals("null")
                && !listModel.getName().equals("")) {
            holder.txtTitle.setText(listModel.getName());
        } else {
            holder.txtTitle.setText("--");
        }

        if (listModel.getId() != null && !listModel.getId().equals("null")
                && !listModel.getId().equals("")) {
            holder.txtOrderId.setText(mContext.getResources().getString(R.string.purchase_id)+":-"+listModel.getId());
        } else {
            holder.txtOrderId.setText("--");
        }

        if (listModel.getPlanPeriod() != null && !listModel.getPlanPeriod().equals("null")
                && !listModel.getPlanPeriod().equals("")) {
            holder.txtPlanPeriod.setText(listModel.getPlanPeriod());
        } else {
            holder.txtPlanPeriod.setText("--");
        }

        if (listModel.getPurchaseDate() != null && !listModel.getPurchaseDate().equals("null")
                && !listModel.getPurchaseDate().equals("")) {
            holder.txtPurchaseDate.setText(mContext.getResources().getString(R.string.purchase_date) + " - " + listModel.getPurchaseDate());
        } else {
            holder.txtPurchaseDate.setText("--");
        }

        if (listModel.getExpiryDate() != null && !listModel.getExpiryDate().equals("null")
                && !listModel.getExpiryDate().equals("")) {
            holder.txtExpire.setText(mContext.getResources().getString(R.string.expire_date) + " - " + listModel.getExpiryDate());
        } else {
            holder.txtExpire.setText("--");
        }

        if (listModel.getAmount() != null && !listModel.getAmount().toString().equals("null")
                && !listModel.getAmount().toString().equals("")) {
            holder.txtAmount.setText(strCurrencySymbol + " " + convertCurrencyPrice(listModel.getAmount().toString()));
        } else {
            holder.txtAmount.setText("--");
        }

        if (listModel.getStatus() != null && !listModel.getStatus().toString().equals("null")
                && !listModel.getStatus().toString().equals("")) {
            if (listModel.getStatus().toString().equals("1") || listModel.getStatus().toString().equals("1.0")) {
                holder.txtActiveExpire.setText(mContext.getResources().getString(R.string.active));
                holder.txtActiveExpire.setTextColor(mContext.getResources().getColor(R.color.holo_green_dark));
                // holder.imgActive.setVisibility(View.VISIBLE);
                // holder.imgActive.setImageTintList(ColorStateList.valueOf(mContext.getResources().getColor(R.color.holo_green_dark)));
            } else if (listModel.getStatus().toString().equals("2") || listModel.getStatus().toString().equals("2.0")) {
                holder.txtActiveExpire.setText(mContext.getResources().getString(R.string.expire));
                holder.txtActiveExpire.setTextColor(mContext.getResources().getColor(R.color.holo_red_dark));
                // holder.imgActive.setVisibility(View.GONE);
                //  holder.imgActive.setImageTintList(ColorStateList.valueOf(mContext.getResources().getColor(R.color.holo_red_dark)));
            } else {
                holder.txtActiveExpire.setText("--");
                holder.txtActiveExpire.setTextColor(mContext.getResources().getColor(R.color.gray_light));
                // holder.imgActive.setVisibility(View.VISIBLE);
                //  holder.imgActive.setImageTintList(ColorStateList.valueOf(mContext.getResources().getColor(R.color.gray_light)));
            }
        } else {
            holder.txtActiveExpire.setText("--");
            holder.txtActiveExpire.setTextColor(mContext.getResources().getColor(R.color.gray_light));
            // holder.imgActive.setVisibility(View.VISIBLE);
            // holder.imgActive.setImageTintList(ColorStateList.valueOf(mContext.getResources().getColor(R.color.gray_light)));
        }

        /*if (listModel.getAutoRenew() != null && !listModel.getAutoRenew().toString().equals("null")
                && !listModel.getAutoRenew().toString().equals("")) {
            if (listModel.getAutoRenew().toString().equals("1") || listModel.getAutoRenew().toString().equals("1.0")) {// Enable
                holder.txtDisableAutoRenew.setVisibility(View.VISIBLE);
            } else if (listModel.getAutoRenew().toString().equals("2") || listModel.getAutoRenew().toString().equals("2.0")) { //Disable
                holder.txtDisableAutoRenew.setVisibility(View.VISIBLE);
            } else {
                 holder.txtDisableAutoRenew.setVisibility(View.GONE);
            }
        } else {
             holder.txtDisableAutoRenew.setVisibility(View.GONE);
        }

        holder.txtDisableAutoRenew.setTag(position);
        holder.txtDisableAutoRenew.setOnClickListener(view -> {
            int pos = (int)view.getTag();
            SubsHistoryData listModel1 = orderListModel.get(pos);
            HashMap<String, Object> map = new HashMap<>();
            map.put("data", listModel1);
            map.put("position", pos);
            map.put("click", "disable");
            if (successResponse != null)
                successResponse.onSuccess(map);

        });*/

    }

    @SuppressLint("NotifyDataSetChanged")
    public void selected(int position) {
        Log.i("selected", "selected: ");
        selected_pos = position;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return orderListModel.size();
    }


    private String convertCurrencyPrice(String price) {
        String currencyCommonPrice = spCurrency.getString(Utils.SP_CURRENCY_COMMON_VALUES, "1");
        String price_;
        System.out.println("~~~~~" + currencyCommonPrice + "~~~~~" + price);
        if (price != null) {
            if (!price.equals("null")) {
                if (!price.equals("")) {
                    price_ = price;
                } else {
                    price_ = "0";
                }
            } else {
                price_ = "0";
            }
        } else {
            price_ = "0";
        }

        double multiplePrice = Double.parseDouble(currencyCommonPrice) * Double.parseDouble(price_);
        double roundedNumber = DecimalUtils.round(multiplePrice, 2);
        return String.valueOf(roundedNumber);
    }

}

