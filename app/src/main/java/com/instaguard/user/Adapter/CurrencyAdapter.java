package com.instaguard.user.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.instaguard.user.CustomViews.CustomTextView;
import com.instaguard.user.Helpers.Utils;
import com.instaguard.user.Models.AppSettingCurrencyModel;
import com.instaguard.user.R;

import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.MyViewHolder> {

    private Context mContext;
    private List<AppSettingCurrencyModel> orderListModel;
    private static SuccessResponse successResponse;
    SharedPreferences prefs;
    int i = 1;
    SharedPreferences  spCurrency;
    String strCurrency,strCurrencySymbol,strLanguage;
    public interface SuccessResponse {
        void onSuccess(HashMap<String, Object> map);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llPastjobsParent;
        CustomTextView txtCurrency ;
        ImageView imgActive;

        MyViewHolder(View view) {
            super(view);
            txtCurrency = (CustomTextView) view.findViewById(R.id.txt_Currency);
            imgActive = (ImageView) view.findViewById(R.id.img_active);
            llPastjobsParent = (LinearLayout) view.findViewById(R.id.ll_pastjobs_parent);
        }
    }

    public CurrencyAdapter(Context mContext, List<AppSettingCurrencyModel> contact) {
        this.mContext = mContext;
        this.orderListModel = contact;
    }

    @SuppressLint("CommitPrefEdits")
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_currency, parent, false);
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
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        AppSettingCurrencyModel listModel = orderListModel.get(position);

//        Log.i("bind--", "onBindViewHolder-->: " + listModel.getCurrency());
//        Log.i("strCurrency--", "onBindViewHolder-->: " + strCurrency);
//        Log.i("bind--", "onBindViewHolder-->: " + listModel.getSymbol());
//        Log.i("bind--", "onBindViewHolder-->: " + listModel.getConverionsRatio());

        holder.txtCurrency.setText(listModel.getCurrency() + "  ("+listModel.getShortName()+")");
        if (strCurrency!=null){
            if (strCurrency.equals(String.valueOf(listModel.getShortName()))){
                holder.imgActive.setVisibility(View.VISIBLE);
            }else {
                holder.imgActive.setVisibility(View.GONE);
            }
        }else {
            if (String.valueOf(listModel.getShortName()).equals("USD")){
                holder.imgActive.setVisibility(View.VISIBLE);
            }else {
                holder.imgActive.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return orderListModel.size();
    }
  /*  public void updateList(List<Contact> list){
        orderListModel = list;
        notifyDataSetChanged();
    }*/
}

