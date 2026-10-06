package com.instaguard.user.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.instaguard.user.CustomViews.CustomTextView;
import com.instaguard.user.Helpers.DecimalUtils;
import com.instaguard.user.Helpers.Utils;
import com.instaguard.user.Models.TransactionModel;
import com.instaguard.user.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class TransactionMoneyInOutAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<TransactionModel> categoryArrayList;
    private static int selected_pos = -1;
    private Context context;
    private SharedPreferences getspCommonState, getspCommonCurrency;
    private String userId, strLanguageShortName, strCurrencySymbol, userAccessToken;
    private SharedPreferences.Editor putSpState;


    public TransactionMoneyInOutAdapter(Context context, ArrayList<TransactionModel> categoryArrayList) {
        this.categoryArrayList = categoryArrayList;
        this.context = context;
    }

    @SuppressLint("CommitPrefEdits")
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_wallet_history, parent, false);
        putSpState = context.getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE).edit();

        getspCommonState = context.getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE);
        getspCommonCurrency = context.getSharedPreferences(Utils.PREFS_NAME_CURRENCY, MODE_PRIVATE);
        strCurrencySymbol = getspCommonCurrency.getString(Utils.SP_CURRENCY_SYMBOL, null);
        strLanguageShortName = getspCommonCurrency.getString(Utils.SP_LANGUAGE_SHORT_NAME, null);

        if (strLanguageShortName == null) {
            strLanguageShortName = "en";
        }

        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final TransactionMoneyInOutAdapter.MyViewHolder myViewHolder = (TransactionMoneyInOutAdapter.MyViewHolder) holder;


        String upperString;
        String upperStringAr;

        if (categoryArrayList.get(position).getTransactionNotes() != null) {
            if (!categoryArrayList.get(position).getTransactionNotes().equals("null")) {
                if (!categoryArrayList.get(position).getTransactionNotes().equals("")) {
                    if (!categoryArrayList.get(position).getTransactionNotes().equals(" ")) {
                        upperString = categoryArrayList.get(position).getTransactionNotes().substring(0, 1).toUpperCase()
                                + categoryArrayList.get(position).getTransactionNotes().substring(1);
                    } else {
                        upperString = "--";
                    }
                } else {
                    upperString = "--";
                }
            } else {
                upperString = "--";
            }
        } else {
            upperString = "--";
        }

        if (categoryArrayList.get(position).getTransactionNotesAr() != null) {
            if (!categoryArrayList.get(position).getTransactionNotesAr().equals("null")) {
                if (!categoryArrayList.get(position).getTransactionNotesAr().equals("")) {
                    if (!categoryArrayList.get(position).getTransactionNotesAr().equals(" ")) {
                        upperStringAr = categoryArrayList.get(position).getTransactionNotesAr().substring(0, 1).toUpperCase()
                                + categoryArrayList.get(position).getTransactionNotesAr().substring(1);
                    } else {
                        upperStringAr = "--";
                    }
                } else {
                    upperStringAr = "--";
                }
            } else {
                upperStringAr = "--";
            }
        } else {
            upperStringAr = "--";
        }


        if (strLanguageShortName.equals("en")) {
            myViewHolder.txtTransactionBookingid.setText(upperString);
        } else if (strLanguageShortName.equals("ar")) {
            myViewHolder.txtTransactionBookingid.setText(upperStringAr);
        } else {
            myViewHolder.txtTransactionBookingid.setText(upperString);
        }

        //myViewHolder.txtTransactionBookingid.setText(categoryArrayList.get(position).getTransactionNotes());

        //  myViewHolder.txtTransactionDate.setText(categoryArrayList.get(position).getTransactionDate());

        final String dateList;
        String dateListEs = null;
        // dateList = categoryArrayList.get(position).getTransactionDate(); //"Thu,Jan 30,2020 05:12 AM",
        dateList = categoryArrayList.get(position).getTransactionDate(); //"2020-03-10 10:03:42",
        /*try {

            String[] separated = dateList.split(",");
            String day = separated[0]; //Thu
            String monthDate = separated[1]; // Jan 30
            String yearTime = separated[2]; // 2020 05:12 AM"

            String[] separate2 = monthDate.split(" ");
            String month = separate2[0]; // Jan
            String date = separate2[1]; // 30

            // Log.e("onBindViewHolder: ~~~~~~~~1~~~~~~~", day + "~~" + monthDate + "~~~~" + yearTime + "~~~~");
            // Log.e("onBindViewHolder: ~~~~~~~~2~~~~~~~", "~~" + month + "~~~~" + date);

            //ronBindViewHolder: ~~~~~~~~1~~~~~~~: Thu ~~ Jan 30~~~~2020 05:12 AM~~~~
            //onBindViewHolder: ~~~~~~~~2~~~~~~~: ~~Jan~~~~30

            String dayLabel = "-";
            switch (day) {
                case "Sun":
                case "sun":
                    dayLabel = mContext.getResources().getString(R.string.Sunday_3);
                    break;
                case "Mon":
                case "mon":
                    dayLabel = mContext.getResources().getString(R.string.Monday_3);
                    break;
                case "Tue":
                case "tue":
                    dayLabel = mContext.getResources().getString(R.string.Tuesday_3);
                    break;
                case "Wed":
                case "wed":
                    dayLabel = mContext.getResources().getString(R.string.Wednesday_3);
                    break;
                case "Thu":
                case "thu":
                    dayLabel = mContext.getResources().getString(R.string.Thursday_3);
                    break;
                case "Fri":
                case "fri":
                    dayLabel = mContext.getResources().getString(R.string.Friday_3);
                    break;
                case "Sat":
                case "sat":
                    dayLabel = mContext.getResources().getString(R.string.Saterday_3);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + day);
            }

            String monthLabel = "-";
            switch (month) {
                case "Jan":
                case "jan":
                    monthLabel = mContext.getResources().getString(R.string.January_3);
                    break;
                case "Feb":
                case "feb":
                    monthLabel = mContext.getResources().getString(R.string.February_3);
                    break;
                case "Mar":
                case "mar":
                    monthLabel = mContext.getResources().getString(R.string.March_3);
                    break;
                case "Apr":
                case "apr":
                    monthLabel = mContext.getResources().getString(R.string.April_3);
                    break;
                case "May":
                case "may":
                    monthLabel = mContext.getResources().getString(R.string.May_3);
                    break;
                case "Jun":
                case "jun":
                    monthLabel = mContext.getResources().getString(R.string.June_3);
                    break;
                case "Jul":
                case "jul":
                    monthLabel = mContext.getResources().getString(R.string.July_3);
                    break;
                case "Aug":
                case "aug":
                    monthLabel = mContext.getResources().getString(R.string.August_3);
                    break;
                case "Sep":
                case "sep":
                    monthLabel = mContext.getResources().getString(R.string.September_3);
                    break;
                case "Oct":
                case "oct":
                    monthLabel = mContext.getResources().getString(R.string.October_3);
                    break;
                case "Nov":
                case "nov":
                    monthLabel = mContext.getResources().getString(R.string.November_3);
                    break;
                case "Dec":
                case "dec":
                    monthLabel = mContext.getResources().getString(R.string.December_3);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + month);
            }

            dateListEs = dayLabel + "," + monthLabel + " " + date + "," + yearTime;

        } catch (Exception e) {
            e.printStackTrace();
            dateListEs = null;
        }*/

        //if (dateListEs != null) {
        //  holder.txtTransactionDate.setText(dateListEs);
        // } else {
        myViewHolder.txtTransactionDate.setText(dateList);
        // }

       /* } else {
            holder.txtBookingDate.setText(dateList);
        }*/

        myViewHolder.txtTransactionBalance.setText(strCurrencySymbol + " " + convertCurrencyPrice(String.valueOf(categoryArrayList.get(position).getTransactionAmount())));

        switch (categoryArrayList.get(position).getTransactionType()) {
            case "debit":
                myViewHolder.iv_credit_debit_img.setImageResource(R.mipmap.ic_debit);

                break;
            case "Debit":
                myViewHolder.iv_credit_debit_img.setImageResource(R.mipmap.ic_debit);

                break;
            case "credit":
                myViewHolder.iv_credit_debit_img.setImageResource(R.mipmap.ic_credit);
                break;
            default:
                myViewHolder.iv_credit_debit_img.setImageResource(R.mipmap.ic_close_detail);
                break;
        }

        if (categoryArrayList.get(position).getTransactionMode() != null && !categoryArrayList.get(position).getTransactionMode().equals("null")
                && !categoryArrayList.get(position).getTransactionMode().equals("") && !categoryArrayList.get(position).getTransactionMode().equals(" "))
            switch (String.valueOf(categoryArrayList.get(position).getTransactionMode())) {
                case "card":
                case "stripe":
                    myViewHolder.txtTransactionMode.setText(context.getResources().getString(R.string.paid_with) + " " + context.getResources().getString(R.string.select_payment_pay_online));
                    break;
                case "wallet":
                    myViewHolder.txtTransactionMode.setText(context.getResources().getString(R.string.paid_with_wallet));
                    break;
                case "stripe_gpay":
                    myViewHolder.txtTransactionMode.setText(context.getResources().getString(R.string.paid_with_google_pay));
                    break;
                case "paypal":
                    myViewHolder.txtTransactionMode.setText(context.getResources().getString(R.string.paid_with_paypal));
                    break;
                default:
                    myViewHolder.txtTransactionMode.setText(String.valueOf(categoryArrayList.get(position).getTransactionMode()));
                    break;
            }
        else {
            myViewHolder.txtTransactionMode.setText("");
        }
    }

    public void selected(int position) {
        Log.i("selected", "selected: ");
        selected_pos = position;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CustomTextView txtTransactionBalance, txtTransactionDate, txtTransactionBookingid, txtTransactionMode;
        ImageView iv_credit_debit_img;
        LinearLayout ll_adapter_wallet;

        MyViewHolder(View view) {
            super(view);

            ll_adapter_wallet = (LinearLayout) view.findViewById(R.id.ll_adapter_wallet);
            iv_credit_debit_img = (ImageView) view.findViewById(R.id.iv_credit_debit_img);

            txtTransactionBalance = (CustomTextView) view.findViewById(R.id.txt_transaction_balance);
            txtTransactionBookingid = (CustomTextView) view.findViewById(R.id.txt_transaction_bookingid);
            txtTransactionDate = (CustomTextView) view.findViewById(R.id.txt_transaction_date);
            txtTransactionMode = (CustomTextView) view.findViewById(R.id.txt_transaction_mode);

        }
    }

    public String convertCurrencyPrice(String price) {
        String currencyCommonPrice = getspCommonCurrency.getString(Utils.SP_CURRENCY_COMMON_VALUES, null);
        Double multiplePrice = Double.parseDouble(currencyCommonPrice) * Double.parseDouble(price);
        double roundedNumber = DecimalUtils.round(multiplePrice, 2);
        return String.valueOf(roundedNumber);
    }
}
