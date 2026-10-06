package com.instaguard.user.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.facebook.drawee.view.SimpleDraweeView;
import com.instaguard.user.CustomViews.CustomTextView;
import com.instaguard.user.Helpers.Utils;
import com.instaguard.user.Models.CategoryModel;
import com.instaguard.user.Models.VehicleCategoryData;
import com.instaguard.user.R;

import java.util.ArrayList;

public class MapCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<CategoryModel> categoryArrayList;
    private static int selected_pos = -1;
    private Context context;

    public MapCategoryAdapter(ArrayList<CategoryModel> categoryArrayList, Context context) {
        this.categoryArrayList=categoryArrayList;
        this.context=context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mapcategory_main, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final MapCategoryAdapter.MyViewHolder myViewHolder = (MapCategoryAdapter.MyViewHolder) holder;

        Log.e("current position   "+position,"selected position 222222  "+selected_pos);

        myViewHolder.imgCategory.setImageURI(Utils.BASE_URL_CATEGORY_IMAGE + categoryArrayList.get(position).getCategoryImage());

        if (selected_pos == position) {
            myViewHolder.imgBgCategory.setImageResource(R.drawable.bg_square_shape_corner_color);
        } else {
            myViewHolder.imgBgCategory.setImageResource(R.drawable.bg_square_shape_corner_color_gray);
        }

        String tempCategoryName = categoryArrayList.get(position).getCategoryName();
        tempCategoryName = tempCategoryName.replaceAll("_", " ");

        myViewHolder.txtCategoryName.setText(tempCategoryName);

        myViewHolder.imgCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("imgCategory", "onClick: imgCategory-> ");
                selected_pos = position;
                notifyDataSetChanged();
            }
        });
        myViewHolder.imgBgCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("imgBgCategory", "onClick: imgBgCategory-> ");
                selected_pos = position;
                notifyDataSetChanged();
            }
        });
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
        CustomTextView txtCategoryName;

        MyViewHolder(View itemView) {
            super(itemView);
            imgCategory = (SimpleDraweeView) itemView.findViewById(R.id.img_category);
            imgBgCategory = (ImageView) itemView.findViewById(R.id.img_category_bg);
            txtCategoryName = (CustomTextView) itemView.findViewById(R.id.txt_category_name);
        }
    }

}








































/*
public class MapCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<CategoryModel> categoryArrayList;
    private static int selected_pos = -1;
    private Context context;

    public MapCategoryAdapter(ArrayList<CategoryModel> categoryArrayList, Context context) {
        this.categoryArrayList=categoryArrayList;
        this.context=context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mapcategory, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MapCategoryAdapter.MyViewHolder myViewHolder = (MapCategoryAdapter.MyViewHolder) holder;

        Log.e("current position   "+position,"selected position   "+selected_pos);


        if (selected_pos == position) {
           // myViewHolder.imgBgCategory.setImageResource(R.drawable.bg_circle_filled_color);
            if(categoryArrayList.get(position).getCategoryName().equals("4-seater-car")||categoryArrayList.get(position).getCategoryName().equals("Car_4_seater"))
                myViewHolder.imgCategory.setImageResource(R.drawable.ic_hatchback_color);
            else if(categoryArrayList.get(position).getCategoryName().equals("6-seater-car")||categoryArrayList.get(position).getCategoryName().equals("Car_6_seater"))
                myViewHolder.imgCategory.setImageResource(R.drawable.ic_suv_color);
            else if(categoryArrayList.get(position).getCategoryName().equals("Van"))
                myViewHolder.imgCategory.setImageResource(R.drawable.ic_van_color);
            else if(categoryArrayList.get(position).getCategoryName().equals("Boat"))
                myViewHolder.imgCategory.setImageResource(R.drawable.ic_boat_color);
            else if(categoryArrayList.get(position).getCategoryName().equals("Bike"))
                myViewHolder.imgCategory.setImageResource(R.drawable.ic_motorcycle_color);
            else
                myViewHolder.imgCategory.setImageResource(R.drawable.ic_hatchback_selector);

        } else {
           // myViewHolder.imgBgCategory.setImageResource(R.drawable.bg_circle_filled_white);
            if(categoryArrayList.get(position).getCategoryName().equals("4-seater-car")||categoryArrayList.get(position).getCategoryName().equals("Car_4_seater"))
                myViewHolder.imgCategory.setImageResource(R.drawable.ic_hatchback);
            else if(categoryArrayList.get(position).getCategoryName().equals("6-seater-car")||categoryArrayList.get(position).getCategoryName().equals("Car_6_seater"))
                myViewHolder.imgCategory.setImageResource(R.drawable.ic_suv);
            else if(categoryArrayList.get(position).getCategoryName().equals("Van"))
                myViewHolder.imgCategory.setImageResource(R.drawable.ic_van);
            else if(categoryArrayList.get(position).getCategoryName().equals("Boat"))
                myViewHolder.imgCategory.setImageResource(R.drawable.ic_boat);
            else if(categoryArrayList.get(position).getCategoryName().equals("Bike"))
                myViewHolder.imgCategory.setImageResource(R.drawable.ic_motorcycle);
            else
                myViewHolder.imgCategory.setImageResource(R.drawable.ic_hatchback_selector);

        }
        String tempCategoryName = categoryArrayList.get(position).getCategoryName();
        tempCategoryName = tempCategoryName.replaceAll("_", " ");

        myViewHolder.txtCategoryName.setText(tempCategoryName);
        myViewHolder.imgCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("imgCategory", "onClick: imgCategory-> ");
//                selected_pos = position;
//                notifyDataSetChanged();
              //  selected_pos=-1;
            }
        });
       */
/* myViewHolder.imgBgCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("imgBgCategory", "onClick: imgBgCategory-> ");
                selected_pos = position;
                notifyDataSetChanged();
                //  selected_pos=-1;
            }
        });*//*

    }
    public void selected(int position){
        Log.i("selected", "selected: ");
        selected_pos = position;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgCategory,imgBgCategory;
        public CustomTextView txtCategoryName;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgCategory = (ImageView) itemView.findViewById(R.id.img_category);
            imgBgCategory = (ImageView) itemView.findViewById(R.id.img_category1);
            txtCategoryName = (CustomTextView) itemView.findViewById(R.id.txt_category_name);
        }
    }
}
*/
