package com.instaguard.user.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.instaguard.user.Helpers.DecimalUtils;
import com.instaguard.user.Helpers.Utils;
import com.instaguard.user.Models.CategorySubModel;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public abstract class Radio_old_MapCategorySubAdapter extends RadioMapSubCategoryAdapter<CategorySubModel> {
    //*****************Remove abstract************************
    private ArrayList<CategorySubModel> categoryArrayList;
    private static int selected_pos = -1;
    private Context context;
    private String  strCurrencySymbol;
    private SharedPreferences getspCommonState;

    public Radio_old_MapCategorySubAdapter(Context context, List<CategorySubModel> items) {
        super(context, items);
        getspCommonState = context.getSharedPreferences(Utils.PREFS_NAME_CURRENCY, MODE_PRIVATE);
        strCurrencySymbol = getspCommonState.getString(Utils.SP_CURRENCY_SYMBOL, null);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull Radio_old_MapCategorySubAdapter.ViewHolder viewHolder, int i) {
        super.onBindViewHolder(viewHolder, i);
        viewHolder.txtCategoryName.setText(mItems.get(i).getSubCategoryName());
        viewHolder.txtCategoryBasefare.setText(strCurrencySymbol+" "+ convertCurrencyPrice(mItems.get(i).getSubCategoryBasefare()));
    }
    public String convertCurrencyPrice(String price){
        String currencyCommonPrice = getspCommonState.getString(Utils.SP_CURRENCY_COMMON_VALUES, "1");
        Double multiplePrice = Double.parseDouble(currencyCommonPrice) * Double.parseDouble(price);
        double roundedNumber = DecimalUtils.round(multiplePrice, 2);
        return String.valueOf(roundedNumber);
    }
}