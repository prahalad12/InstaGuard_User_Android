package com.instaguard.user.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.instaguard.user.CustomViews.CustomTextView;
import com.instaguard.user.Helpers.Utils;
import com.instaguard.user.Models.InternalPaymentMethod_;
import com.instaguard.user.R;

import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {
    private final List<InternalPaymentMethod_> InternalPaymentMethod_s;
    private final Context context;
    private String strWalletAmt;
    private String strCurrencySymbol;
    private static int selected_pos = -1;

    public PaymentAdapter(Context context, List<InternalPaymentMethod_> InternalPaymentMethod_s) {
        this.InternalPaymentMethod_s = InternalPaymentMethod_s;
        this.context = context;
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_payment, parent, false);
        strWalletAmt = context.getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE).getString(Utils.SP_USER_WALLET_AMT, "0");
        strCurrencySymbol = context.getSharedPreferences(Utils.PREFS_NAME_CURRENCY, MODE_PRIVATE).getString(Utils.SP_CURRENCY_SYMBOL, null);
        return new PaymentViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void selected(int position) {
        Log.i("selected", "selected: ");
        selected_pos = position;
        notifyDataSetChanged();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {

        InternalPaymentMethod_ paymentMethod_ = InternalPaymentMethod_s.get(position);

        holder.nameTextView.setText(paymentMethod_.getName());

        switch (paymentMethod_.getKey()) {
            case "card":
                holder.ivPaymentImageG.setVisibility(View.INVISIBLE);
                holder.ivPaymentImage.setVisibility(View.VISIBLE);
                holder.ivPaymentImage.setImageDrawable(context.getDrawable(R.drawable.stripe_logo_logotyp_us));
                holder.ivPaymentImage.setBackground(context.getDrawable(R.drawable.bg_rect_border));
                holder.ivPaymentImage.setPadding(8, 8, 8, 8);
                break;
            case "gpay":
                holder.ivPaymentImageG.setVisibility(View.VISIBLE);
                holder.ivPaymentImage.setVisibility(View.INVISIBLE);
                holder.ivPaymentImage.setImageDrawable(context.getDrawable(R.drawable.google_pay_mark_800));
                holder.ivPaymentImage.setBackground(null);
                //holder.ivPaymentImage.setPadding(0, 0, 0, 0);
                if (paymentMethod_.getStatus() == 0) {
                    //if (!paymentMethod_.isBgpay()) {
                    if (paymentMethod_.getKey().equals("gpay")) {
                        //holder.nameTextView.append(" (Not in device)");
                    }
                }

                break;
            case "paypal":
                holder.ivPaymentImageG.setVisibility(View.INVISIBLE);
                holder.ivPaymentImage.setVisibility(View.VISIBLE);
                holder.ivPaymentImage.setImageDrawable(context.getDrawable(com.paypal.android.ui.R.drawable.logo_paypal_color));
                holder.ivPaymentImage.setBackground(context.getDrawable(R.drawable.bg_rect_border));
                holder.ivPaymentImage.setPadding(8, 8, 8, 8);
                break;
            case "wallet":
                holder.ivPaymentImageG.setVisibility(View.INVISIBLE);
                holder.ivPaymentImage.setVisibility(View.VISIBLE);
                holder.ivPaymentImage.setImageDrawable(context.getDrawable(R.mipmap.ic_menu_wallet));
                holder.ivPaymentImage.setBackground(context.getDrawable(R.drawable.bg_rect_border));
                holder.ivPaymentImage.setPadding(3, 3, 3, 3);
                if (paymentMethod_.getKey().equals("wallet")) {
                    if (strWalletAmt != null && !strWalletAmt.equals("null")
                            && !strWalletAmt.equals("")
                            && !strWalletAmt.equals("0") && !strWalletAmt.equals("0.0")) {
                        holder.nameTextView.append(" (" + strCurrencySymbol + "" + strWalletAmt + ")");
                    }
                }

                break;
            default:
                holder.ivPaymentImageG.setVisibility(View.INVISIBLE);
                holder.ivPaymentImage.setVisibility(View.VISIBLE);
                holder.ivPaymentImage.setImageDrawable(context.getDrawable(com.paypal.android.paypalnativepayments.R.drawable.card_network));
                holder.ivPaymentImage.setBackground(context.getDrawable(R.drawable.bg_rect_border));
                holder.ivPaymentImage.setPadding(3, 3, 3, 3);
                break;
        }

        if (selected_pos == position) {
            holder.ivSelector.setImageResource(R.drawable.ic_radio_selector);
        } else {
            holder.ivSelector.setImageResource(R.drawable.ic_radio_unselector);
        }

        //holder.statusTextView.setText(InternalPaymentMethod_.getStatus() == 1 ? "Enabled" : "Disabled");
        if (paymentMethod_.getStatus() == 1) {
            holder.viewDisable.setVisibility(View.GONE);
        } else {
            holder.viewDisable.setVisibility(View.VISIBLE);
        }

        holder.viewDisable.setTag(position);
        holder.viewDisable.setOnClickListener(view -> {
        });

        if (position == InternalPaymentMethod_s.size() - 1) {
            holder.viewLine.setVisibility(View.GONE);
        } else {
            holder.viewLine.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return InternalPaymentMethod_s.size();
    }

    static class PaymentViewHolder extends RecyclerView.ViewHolder {
        CustomTextView nameTextView;
        ImageView ivPaymentImage, ivPaymentImageG, ivSelector;
        View viewDisable, viewLine;

        public PaymentViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.txt_payment_name);
            ivPaymentImage = itemView.findViewById(R.id.iv_image);
            ivPaymentImageG = itemView.findViewById(R.id.iv_image_g);
            ivSelector = itemView.findViewById(R.id.iv_image_radio);

            viewDisable = itemView.findViewById(R.id.view_disable);
            viewLine = itemView.findViewById(R.id.view_line);
        }
    }
}
