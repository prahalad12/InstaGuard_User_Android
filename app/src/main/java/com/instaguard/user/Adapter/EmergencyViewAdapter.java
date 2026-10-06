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

import com.instaguard.user.CustomViews.CustomTextView;
import com.instaguard.user.Models.EmergencyModel;
import com.instaguard.user.R;

import java.util.ArrayList;

public class EmergencyViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<EmergencyModel> emergencyArrayList;
    private static int selected_pos = -1;
    private Context context;

    public EmergencyViewAdapter(ArrayList<EmergencyModel> categoryArrayList, Context context) {
        this.emergencyArrayList =categoryArrayList;
        this.context=context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_emergencyview, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final EmergencyViewAdapter.MyViewHolder myViewHolder = (EmergencyViewAdapter.MyViewHolder) holder;

        Log.e("current position   "+emergencyArrayList.get(position).getContact_num(),"selected position   "+emergencyArrayList.get(position).getContact_name());

        myViewHolder.txtContactName.setText(emergencyArrayList.get(position).getContact_name());
        myViewHolder.txtContactNum.setText(emergencyArrayList.get(position).getContact_num());

    }
    public void selected(int position){
        Log.i("selected", "selected: ");
        selected_pos = position;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return emergencyArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
         ImageView ivClose;
        CustomTextView txtContactName,txtContactNum;

        MyViewHolder(View itemView) {
            super(itemView);
            ivClose = (ImageView) itemView.findViewById(R.id.iv_close);
            txtContactName = (CustomTextView) itemView.findViewById(R.id.txt_cnt_name);
            txtContactNum = (CustomTextView) itemView.findViewById(R.id.txt_cnt_num);
        }
    }

}