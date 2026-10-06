package com.instaguard.user.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.instaguard.user.CustomViews.CustomTextView;
import com.instaguard.user.R;

import java.util.List;

public abstract class RadioMapSubCategoryAdapter<T> extends RecyclerView.Adapter<RadioMapSubCategoryAdapter.ViewHolder> {
    private int mSelectedItem = -1;
    List<T> mItems;
    private Context mContext;

    RadioMapSubCategoryAdapter(Context context, List<T> items) {
        mContext = context;
        mItems = items;
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.rbSelectCategory.setChecked(position == mSelectedItem);
    }

    //   holder.rbSelectCategory.setChecked(position == mSelectedItem);

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        final View view = inflater.inflate(R.layout.adapter_mapsubcategory, viewGroup, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        RadioButton rbSelectCategory;
        CustomTextView txtCategoryName,txtCategoryBasefare;

        ViewHolder(final View inflate) {
            super(inflate);
            txtCategoryName = (CustomTextView) inflate.findViewById(R.id.txt_category_name);
           // txtCategoryBasefare = (CustomTextView) inflate.findViewById(R.id.txt_category_base_fare);
           // rbSelectCategory = (RadioButton) inflate.findViewById(R.id.rg_selcet_category);
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedItem = getAdapterPosition();
                    notifyItemRangeChanged(0, mItems.size());
                }
            };
            itemView.setOnClickListener(clickListener);
            rbSelectCategory.setOnClickListener(clickListener);
        }
    }

}
