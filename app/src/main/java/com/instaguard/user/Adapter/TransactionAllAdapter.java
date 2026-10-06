package com.instaguard.user.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.instaguard.user.CustomViews.CustomTextView;
import com.instaguard.user.Helpers.DecimalUtils;
import com.instaguard.user.Helpers.Utils;
import com.instaguard.user.Models.WalletAllData;
import com.instaguard.user.R;

import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class TransactionAllAdapter extends RecyclerView.Adapter<TransactionAllAdapter.MyViewHolder> {

    private Context mContext;
    private List<WalletAllData> orderListModel;
    public static SuccessResponse successResponse;
    private WalletAllData listModel;
    SharedPreferences prefs;
    int i = 1;
    private SharedPreferences getspCommonState, getspCommonCurrency;
    private String userId, strLanguageShortName, strCurrencySymbol, userAccessToken;
    private SharedPreferences.Editor putSpState;


    public interface SuccessResponse {
        void onSuccess(HashMap<String, Object> map);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

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

    public TransactionAllAdapter(Context mContext, List<WalletAllData> contact) {
        this.mContext = mContext;
        this.orderListModel = contact;
    }

    @SuppressLint("CommitPrefEdits")
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_wallet_history, parent, false);

        putSpState = mContext.getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE).edit();

        getspCommonState = mContext.getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE);
        getspCommonCurrency = mContext.getSharedPreferences(Utils.PREFS_NAME_CURRENCY, MODE_PRIVATE);
        userId = getspCommonState.getString(Utils.SP_USERID, null);
        userAccessToken = getspCommonState.getString(Utils.SP_USER_ACCESSTOKEN, null);
        strCurrencySymbol = getspCommonCurrency.getString(Utils.SP_CURRENCY_SYMBOL, null);
        strLanguageShortName = getspCommonCurrency.getString(Utils.SP_LANGUAGE_SHORT_NAME, null);

        if (strLanguageShortName == null) {
            strLanguageShortName = "en";
        }

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        listModel = orderListModel.get(position);

        String upperString;
        String upperStringAr;

        if (listModel.getTransactionNotes() != null) {
            if (!listModel.getTransactionNotes().equals("null")) {
                if (!listModel.getTransactionNotes().equals("")) {
                    if (!listModel.getTransactionNotes().equals(" ")) {
                        upperString = listModel.getTransactionNotes().substring(0, 1).toUpperCase() + listModel.getTransactionNotes().substring(1);
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

        if (listModel.getTransactionNotesAr() != null) {
            if (!listModel.getTransactionNotesAr().equals("null")) {
                if (!listModel.getTransactionNotesAr().equals("")) {
                    if (!listModel.getTransactionNotesAr().equals(" ")) {
                        upperStringAr = listModel.getTransactionNotesAr().substring(0, 1).toUpperCase() + listModel.getTransactionNotesAr().substring(1);
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
            holder.txtTransactionBookingid.setText(upperString);
        } else if (strLanguageShortName.equals("ar")) {
            holder.txtTransactionBookingid.setText(upperStringAr);
        } else {
            holder.txtTransactionBookingid.setText(upperString);
        }


        // holder.txtTransactionDate.setText(listModel.getTransactionDate());

        final String dateList;
        String dateListEs = null;
        // dateList = listModel.getTransactionDate(); //"Thu,Jan 30,2020 05:12 AM",
        dateList = listModel.getTransactionDate(); //"2020-03-10 10:03:42",
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
        holder.txtTransactionDate.setText(dateList);
        // }

       /* } else {
            holder.txtBookingDate.setText(dateList);
        }*/

        holder.txtTransactionBalance.setText(strCurrencySymbol + " " + convertCurrencyPrice(String.valueOf(listModel.getTransactionAmount())));

        switch (listModel.getTransactionType()) {
            case "debit":
                holder.iv_credit_debit_img.setImageResource(R.mipmap.ic_debit);

                break;
            case "debit ":
                holder.iv_credit_debit_img.setImageResource(R.mipmap.ic_debit);

                break;
            case "Debit":
                holder.iv_credit_debit_img.setImageResource(R.mipmap.ic_debit);

                break;
            case "credit":
                holder.iv_credit_debit_img.setImageResource(R.mipmap.ic_credit);
                break;
            default:
                holder.iv_credit_debit_img.setImageResource(R.mipmap.ic_close_detail);
                break;
        }

        if (listModel.getTransactionMode() != null && !listModel.getTransactionMode().equals("null")
                && !listModel.getTransactionMode().equals("") && !listModel.getTransactionMode().equals(" "))
            switch (String.valueOf(listModel.getTransactionMode())) {
                case "card":
                case "stripe":
                    holder.txtTransactionMode.setText(mContext.getResources().getString(R.string.paid_with) + " " + mContext.getResources().getString(R.string.select_payment_pay_online));
                    break;
                case "wallet":
                    holder.txtTransactionMode.setText(mContext.getResources().getString(R.string.paid_with_wallet));
                    break;
                case "stripe_gpay":
                    holder.txtTransactionMode.setText(mContext.getResources().getString(R.string.paid_with_google_pay));
                    break;
                case "paypal":
                    holder.txtTransactionMode.setText(mContext.getResources().getString(R.string.paid_with_paypal));
                    break;
                default:
                    holder.txtTransactionMode.setText(String.valueOf(listModel.getTransactionMode()));
                    break;
            }
        else {
            holder.txtTransactionMode.setText("");
        }

    }

    @Override
    public int getItemCount() {
        return orderListModel.size();
    }

    /*  public void updateList(List<Contact> list){
          orderListModel = list;
          notifyDataSetChanged();
      }*/
    public String convertCurrencyPrice(String price) {
        String currencyCommonPrice = getspCommonCurrency.getString(Utils.SP_CURRENCY_COMMON_VALUES, "1");
        double multiplePrice = Double.parseDouble(currencyCommonPrice) * Double.parseDouble(price);
        double roundedNumber = DecimalUtils.round(multiplePrice, 2);
        return String.valueOf(roundedNumber);
    }
}

