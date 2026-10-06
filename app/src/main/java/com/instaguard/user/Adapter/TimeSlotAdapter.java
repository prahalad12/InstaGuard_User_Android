package com.instaguard.user.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.instaguard.user.Helpers.CustomCheckButton.CheckableButton;
import com.instaguard.user.R;

import java.util.HashMap;
import java.util.List;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.MyViewHolder> {

    @SuppressLint("UseSparseArrays")
    private HashMap<Integer, Boolean> isChecked = new HashMap<>();
    private Context mContext;
    private LayoutInflater inflater;
    private List<String> produkList;
    public static SuccessResponse successResponse;

    public TimeSlotAdapter(Context context, List<String> produkList){
        this.mContext = context;
        this.produkList = produkList;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public TimeSlotAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = inflater.inflate(R.layout.adapter_timeslot, parent, false);

        MyViewHolder holder = new MyViewHolder(rootView);
        return holder;
    }
    public interface SuccessResponse{
        void onSuccess(String text);
    }
    @Override
    public void onBindViewHolder(@NonNull TimeSlotAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.rbRimeSlot.setText(produkList.get(position));
       /* if (isChecked.containsKey(position)){
            //holder.cbProduk.setChecked(isChecked.get(position));
        } else {
            // holder.cbProduk.setChecked(false);
        }*/
        holder.rbRimeSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.rbRimeSlot.setSelected(true);
                Log.i("selected position", "onClick: -->"+position);
                if(successResponse!=null)
                    successResponse.onSuccess("hai");
            }
        });
    }

    @Override
    public int getItemCount() {
        return produkList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        // CheckBox cbProduk;
        public static CheckableButton checkableBtn;
        public static RadioButton rbRimeSlot;

        public MyViewHolder(View itemView) {
            super(itemView);

            //cbProduk = (CheckBox) itemView.findViewById(R.id.cbProduk);
            checkableBtn = (CheckableButton) itemView.findViewById(R.id.cb_timeslot);
            rbRimeSlot = (RadioButton) itemView.findViewById(R.id.rb_timeslot);


            checkableBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*String Checked= "Button Unchecked";
                    if(checkableBtn.isChecked()){
                        Checked = "Button Checked";
                    }
                    Toast.makeText(mContext, Checked, Toast.LENGTH_SHORT).show();*/
                  /*  if(successResponse!=null)
                        successResponse.onSuccess("hai");*/

                }
            });

        }
    }
}