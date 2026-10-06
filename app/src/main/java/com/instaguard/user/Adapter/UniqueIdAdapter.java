package com.instaguard.user.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.instaguard.user.Helpers.UniqueIdCallback;
import com.instaguard.user.R;

import java.util.ArrayList;

public class UniqueIdAdapter extends RecyclerView.Adapter<UniqueIdAdapter.UniqueIdVH>{
    ArrayList<String> list;
    UniqueIdCallback callback;

    public UniqueIdAdapter(ArrayList<String> uniqueIdList, UniqueIdCallback uniqueIdCallback) {
        this.list = uniqueIdList;
        this.callback = uniqueIdCallback;
    }

    @NonNull
    @Override
    public UniqueIdVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_guard_chip, parent, false);
        return new UniqueIdVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UniqueIdVH holder, int position) {
        holder.chipText.setText(list.get(position));

        holder.chipDlt.setOnClickListener(view -> {
            callback.onClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class UniqueIdVH extends RecyclerView.ViewHolder {
        AppCompatTextView chipText;
        AppCompatImageView chipDlt;
        public UniqueIdVH(@NonNull View itemView) {
            super(itemView);

            chipText = itemView.findViewById(R.id.chipText);
            chipDlt = itemView.findViewById(R.id.chipDlt);
        }
    }
}
