package com.instaguard.user.Adapter;

import android.graphics.Color;
import android.text.Html;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.instaguard.user.CustomViews.CustomTextView;
import com.instaguard.user.Models.AppSupportPagesData;
import com.instaguard.user.R;

import java.util.List;


public class SupportPagesAdapter extends RecyclerView.Adapter<SupportPagesAdapter.MyViewHolder> {

    private final List<AppSupportPagesData> orderListModel;

    static class MyViewHolder extends RecyclerView.ViewHolder {

        CustomTextView txtTitle;
        ImageView ivImg;
        LinearLayout llParentLyt;

        MyViewHolder(View view) {
            super(view);

            llParentLyt = (LinearLayout) view.findViewById(R.id.ll_aboutus);
            ivImg = (ImageView) view.findViewById(R.id.iv_img);
            txtTitle = (CustomTextView) view.findViewById(R.id.txt_aboutus_);

        }
    }

    public SupportPagesAdapter(List<AppSupportPagesData> contact) {
        this.orderListModel = contact;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_support_pages, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        AppSupportPagesData listModel = orderListModel.get(position);

       // holder.txtTitle.setText(String.valueOf(listModel.getMetaTitle()));
        try {
            holder.txtTitle.setText(Html.fromHtml(listModel.getPageName()));
            Linkify.addLinks( holder.txtTitle, Linkify.ALL);
            holder.txtTitle.setLinkTextColor(Color.parseColor("#FF0000"));
        } catch (Exception e) {
            e.printStackTrace();
            holder.txtTitle.setText(HtmlCompat.fromHtml(listModel.getPageName(), HtmlCompat.FROM_HTML_MODE_LEGACY));
        }

        /*switch (listModel.getTransactionType()) {
            case "debit":
                holder.ivImg.setImageResource(R.mipmap.ic_debit);
                break;
            case "credit":
                holder.ivImg.setImageResource(R.mipmap.ic_credit);
                break;
            default:
                holder.ivImg.setImageResource(R.mipmap.ic_close_detail);
                break;
        }*/

    }
    @Override
    public int getItemCount() {
        return orderListModel.size();
    }
}

