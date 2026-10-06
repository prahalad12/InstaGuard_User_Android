package com.instaguard.user.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
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
import com.instaguard.user.Models.Subscription.ListViewSubsData;
import com.instaguard.user.R;

import java.util.HashMap;
import java.util.List;


public class SubscriptionPlanAdapter extends RecyclerView.Adapter<SubscriptionPlanAdapter.MyViewHolder> {

    private final Context mContext;
    private final List<ListViewSubsData> orderListModel;

    public static SuccessResponse successResponse;

    public static int selected_pos = -1;

    SharedPreferences spCurrency;
    String strCurrency, strCurrencySymbol, strLanguage;

    public interface SuccessResponse {
        void onSuccess(HashMap<String, Object> map);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        View llPastjobsParent;
        CustomTextView txtTitle;
        CustomTextView txtPeriodType;
        CustomTextView txtAmount;
        CustomTextView txtMoreInfo;
        ImageView imgActive;

        MyViewHolder(View view) {
            super(view);
            txtTitle = view.findViewById(R.id.txt_title);
            txtPeriodType = view.findViewById(R.id.txt_date);
            txtAmount = view.findViewById(R.id.txt_message);
            txtMoreInfo = view.findViewById(R.id.txt_more_info);
            imgActive = view.findViewById(R.id.img_check);
            llPastjobsParent = view.findViewById(R.id.rl_top_subs_plan_lyt);
        }
    }

    public SubscriptionPlanAdapter(Context mContext, List<ListViewSubsData> contact) {
        this.mContext = mContext;
        this.orderListModel = contact;
    }

    @SuppressLint("CommitPrefEdits")
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_subscription_plan_view, parent, false);
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

        ListViewSubsData listModel = orderListModel.get(position);

        if (listModel.getName() != null && !listModel.getName().equals("null")
                && !listModel.getName().equals("")) {
            holder.txtTitle.setText(listModel.getName());
        } else {
            holder.txtTitle.setText("--");
        }

        if (listModel.getPeriodType() != null && !listModel.getPeriodType().toString().equals("null")
                && !listModel.getPeriodType().toString().equals("")) {

            if(listModel.getPeriodType().toString().equals("1")||listModel.getPeriodType().toString().equals("1.0")){
                if (listModel.getPeriod() > 1) {
                    holder.txtPeriodType.setText(mContext.getResources().getString(R.string.months, listModel.getPeriod().toString()));
                } else {
                    holder.txtPeriodType.setText(mContext.getResources().getString(R.string.month_, listModel.getPeriod().toString()));
                }
            }else if(listModel.getPeriodType().toString().equals("2")||listModel.getPeriodType().toString().equals("2.0")){
                if (listModel.getPeriod() > 1) {
                    holder.txtPeriodType.setText(mContext.getResources().getString(R.string.years, listModel.getPeriod().toString()));
                } else {
                    holder.txtPeriodType.setText(mContext.getResources().getString(R.string.year_, listModel.getPeriod().toString()));
                }
            }else {
                holder.txtPeriodType.setText(listModel.getPeriodType().toString());
            }
        } else {
            holder.txtPeriodType.setText("--");
        }

        if (listModel.getAmount() != null && !listModel.getAmount().toString().equals("null")
                && !listModel.getAmount().toString().equals("")) {
            holder.txtAmount.setText(strCurrencySymbol + " " +convertCurrencyPrice(listModel.getAmount().toString()));
        } else {
            holder.txtAmount.setText("--");
        }

        if (selected_pos == position) {
            holder.imgActive.setImageTintList(ColorStateList.valueOf(mContext.getResources().getColor(R.color.holo_green_dark)));
        } else {
            holder.imgActive.setImageTintList(ColorStateList.valueOf(mContext.getResources().getColor(R.color.gray_light)));
        }

        holder.txtMoreInfo.setTag(position);
        holder.txtMoreInfo.setOnClickListener(view -> {
            int pos = (int) view.getTag();
            ListViewSubsData listModel1 = orderListModel.get(pos);
            HashMap<String, Object> map = new HashMap<>();
            map.put("data", listModel1);
            map.put("position", pos);
            map.put("click", "info");
            if (successResponse != null)
                successResponse.onSuccess(map);
        });

       /* holder.imgActive.setTag(position);
        holder.imgActive.setOnClickListener(view -> {
            int pos = (int) view.getTag();
            ListViewSubsData listModel1 = orderListModel.get(pos);
            HashMap<String, Object> map = new HashMap<>();
            map.put("data", listModel1);
            map.put("position", pos);
            map.put("click", "select");
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

