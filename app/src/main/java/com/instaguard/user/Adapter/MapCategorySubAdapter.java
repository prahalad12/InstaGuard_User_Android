package com.instaguard.user.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.instaguard.user.CustomViews.CustomTextView;
import com.instaguard.user.Helpers.DecimalUtils;
import com.instaguard.user.Helpers.Utils;
import com.instaguard.user.Models.CategorySubModel;
import com.instaguard.user.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class MapCategorySubAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ArrayList<CategorySubModel> categoryArrayList;
    private static int selected_pos = -1;
    private final Context context;
    private String  strCurrencySymbol;
    private String  categoryCount;
    private String  categoryDays;
    private SharedPreferences getspCommonState;

    public MapCategorySubAdapter(ArrayList<CategorySubModel> categoryArrayList, Context context, String categoryCount, String categoryDays) {
        this.categoryArrayList=categoryArrayList;
        this.context=context;
        this.categoryCount=categoryCount;
        this.categoryDays=categoryDays;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mapsubcategory_main, parent, false);
        getspCommonState = context.getSharedPreferences(Utils.PREFS_NAME_CURRENCY, MODE_PRIVATE);
        strCurrencySymbol = getspCommonState.getString(Utils.SP_CURRENCY_SYMBOL, null);
        return new MyViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final MyViewHolder myViewHolder = (MyViewHolder) holder;

        Log.e("current position   "+position,"selected position   "+selected_pos);

        myViewHolder.imgCategory.setImageURI(Utils.BASE_URL_CATEGORY_IMAGE + categoryArrayList.get(position).getSubCategoryImage());

        if (selected_pos == position) {
            myViewHolder.imgBgCategory.setImageResource(R.drawable.bg_round_shape_corner_color);
        } else {
            myViewHolder.imgBgCategory.setImageResource(R.drawable.bg_round_shape_corner_trans);
        }

        /*if (selected_pos == position) {
            myViewHolder.llParentLyt.setBackground(context.getResources().getDrawable(R.drawable.bg_square_shape_corner_color));
           *//* myViewHolder.cvParentLyt.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            myViewHolder.txtBaseFareLabel.setTextColor(context.getResources().getColor(R.color.white));
            myViewHolder.txtBaseFare.setTextColor(context.getResources().getColor(R.color.white));
            myViewHolder.txtCategoryName.setTextColor(context.getResources().getColor(R.color.white));*//*

        }else if (selected_pos == -2) {
            myViewHolder.llParentLyt.setBackground(context.getResources().getDrawable(R.drawable.bg_square_shape_corner_color_gray));
            *//*myViewHolder.cvParentLyt.setCardBackgroundColor(context.getResources().getColor(R.color.hintgray_category));
            myViewHolder.txtBaseFareLabel.setTextColor(context.getResources().getColor(R.color.black));
            myViewHolder.txtBaseFare.setTextColor(context.getResources().getColor(R.color.black));
            myViewHolder.txtCategoryName.setTextColor(context.getResources().getColor(R.color.black));*//*

        } else {
            myViewHolder.llParentLyt.setBackground(context.getResources().getDrawable(R.drawable.bg_square_shape_corner_color_gray));
            *//*myViewHolder.cvParentLyt.setCardBackgroundColor(context.getResources().getColor(R.color.hintgray_category));
            myViewHolder.txtBaseFareLabel.setTextColor(context.getResources().getColor(R.color.black));
            myViewHolder.txtBaseFare.setTextColor(context.getResources().getColor(R.color.black));
            myViewHolder.txtCategoryName.setTextColor(context.getResources().getColor(R.color.black));*//*
        }*/

        myViewHolder.txtBaseFareLabel.setText(context.getResources().getString(R.string.base_fare));
        DecimalFormat format = new DecimalFormat("0.#");
        myViewHolder.txtBaseFare.setText((strCurrencySymbol+format.format(DecimalUtils.round(Double.parseDouble(convertCurrencyPrice(categoryArrayList.get(position).getSubCategoryBasefare(),categoryCount,categoryDays)),0))));

        String tempCategoryName = categoryArrayList.get(position).getSubCategoryName();
        tempCategoryName = tempCategoryName.replaceAll("_", " ");

        myViewHolder.txtCategoryName.setText(tempCategoryName);
        myViewHolder.imgCategory.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                Log.i("imgCategory", "onClick: imgCategory-> ");
                selected_pos = position;
                notifyDataSetChanged();
                //  selected_pos=-1;
            }
        });
        myViewHolder.imgBgCategory.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                Log.i("imgBgCategory", "onClick: imgBgCategory-> ");
                selected_pos = position;
                notifyDataSetChanged();
                //  selected_pos=-1;
            }
        });
    }

    public String convertCurrencyPrice(String subCategoryBasefare, String categoryCount, String price){
        Log.e("Map", "convertCurrencyPrices: "+categoryCount +" "+price );
        String currencyCommonPrice = getspCommonState.getString(Utils.SP_CURRENCY_COMMON_VALUES, "1");
     //   double multiplePrice = (!categoryCount.equals(""))?(Double.parseDouble(currencyCommonPrice) * Double.parseDouble(price)* Double.parseDouble(subCategoryBasefare)* Double.parseDouble(categoryCount)):(Double.parseDouble(currencyCommonPrice) * Double.parseDouble(subCategoryBasefare));
        double multiplePrice = Double.parseDouble(currencyCommonPrice) * Double.parseDouble(subCategoryBasefare);
        double roundedNumber = DecimalUtils.round(multiplePrice, 2);
        return String.valueOf(roundedNumber);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void selected(int position){
        Log.i("selected", "selected: ");
        selected_pos = position;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView imgCategory;
        ImageView imgBgCategory;
        CustomTextView txtCategoryName,txtBaseFare,txtBaseFareLabel;
        LinearLayout llParentLyt;
       // CardView cvParentLyt;

        MyViewHolder(View itemView) {
            super(itemView);
            imgCategory = (SimpleDraweeView) itemView.findViewById(R.id.img_category);
            imgBgCategory = (ImageView) itemView.findViewById(R.id.img_category_bg);
            txtCategoryName = (CustomTextView) itemView.findViewById(R.id.txt_category_name);
            txtBaseFareLabel = (CustomTextView) itemView.findViewById(R.id.txt_base_fare_label);
            txtBaseFare = (CustomTextView) itemView.findViewById(R.id.txt_base_fare);
            llParentLyt = (LinearLayout) itemView.findViewById(R.id.ll_parent_lyt);
           // cvParentLyt = (CardView) itemView.findViewById(R.id.cv_parent_lyt);
        }
    }

}