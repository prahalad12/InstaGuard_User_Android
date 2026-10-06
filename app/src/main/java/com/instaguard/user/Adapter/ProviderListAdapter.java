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

import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.instaguard.user.CustomViews.CustomButton;
import com.instaguard.user.CustomViews.CustomTextView;
import com.instaguard.user.Helpers.RatingBar.SimpleRatingBar;
import com.instaguard.user.Helpers.Utils;
import com.instaguard.user.Models.ProviderListData;
import com.instaguard.user.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class ProviderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    String TAG = "ProviderListAdapter";
    private ArrayList<ProviderListData> providerArrayList;
    private static int selected_pos = -1;
    private Context context;
    public static SuccessResponse successResponse;
    public SharedPreferences spPrefs;
    String categoryId, subCategoryId;
    private String categoryCount;
    private String categoryDays;
    private String CompanyName;

    public ProviderListAdapter(Context context, ArrayList<ProviderListData> providerArrayList) {
        this.providerArrayList = providerArrayList;
        this.context = context;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_providerlist, parent, false);
        spPrefs = context.getSharedPreferences(Utils.MY_PREFS_REQUEST_STATE, Context.MODE_PRIVATE);
        categoryId = spPrefs.getString(Utils.B_CATEGORY_ID, null);
        subCategoryId = spPrefs.getString(Utils.B_SUB_CATEGORY_ID, null);
        Log.i(TAG, "onCreateViewHolder: " + categoryId + " " + subCategoryId);

        return new MyViewHolder(view);
    }

    public interface SuccessResponse {
        void onSuccess(HashMap<String, Object> map);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final ProviderListAdapter.MyViewHolder myViewHolder = (ProviderListAdapter.MyViewHolder) holder;

        Log.e("current position   " + position, "selected position   " + selected_pos);

        Log.i(TAG, "onBindViewHolder: in the profile image-->" + providerArrayList.get(position).getPropic());
        if (!providerArrayList.get(position).getPropic().equals("null")) {
            myViewHolder.imgProviderImage.setImageURI(Utils.BASE_URL + providerArrayList.get(position).getPropic());
            int color = context.getResources().getColor(R.color.colorPrimary);
            RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
            roundingParams.setBorder(color, 1.0f);
            roundingParams.setRoundAsCircle(true);
            myViewHolder.imgProviderImage.getHierarchy().setRoundingParams(roundingParams);
        } else {
//            myViewHolder.imgProviderImage.setImageURI(get);
            int color = context.getResources().getColor(R.color.colorPrimary);
            RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
            roundingParams.setBorder(color, 1.0f);
            roundingParams.setRoundAsCircle(true);
            myViewHolder.imgProviderImage.getHierarchy().setRoundingParams(roundingParams);
        }
        myViewHolder.txtProviderName.setText(providerArrayList.get(position).getFname()/*+" "+providerArrayList.get(position).getLname()*/);

        if (!providerArrayList.get(position).getMiles().equals("null")) {

            //  myViewHolder.txtProviderMiles.setText(providerArrayList.get(position).getMiles());

            String currentString = providerArrayList.get(position).getMiles();
            String[] separated = currentString.split(" ");
            String km = separated[0];
            myViewHolder.txtProviderMiles.setText(km + " KM");
            // myViewHolder.txtProviderMiles.setText(String.valueOf(roundTwoDecimals(convertIntoMiles(Double.parseDouble(km))))+" Miles");

        } else {
            myViewHolder.txtProviderMiles.setVisibility(View.GONE);
        }
        if (providerArrayList.get(position).getProviderType() != null &&
                !providerArrayList.get(position).getProviderType().equals("null") &&
                !providerArrayList.get(position).getProviderType().equals("null")
        ) {
            if (providerArrayList.get(position).getProviderType().equals("individual") ||
                    providerArrayList.get(position).getProviderType().equals("Individual")) {
                myViewHolder.txtProviderCompany.setVisibility(View.VISIBLE);
                myViewHolder.txtProviderCompany.setText(providerArrayList.get(position).getCompanyName());
            } else {
                myViewHolder.txtProviderCompany.setVisibility(View.VISIBLE);
                myViewHolder.txtProviderCompany.setText(context.getResources().getString(R.string.company_name) + ":- " + providerArrayList.get(position).getCompanyName());
            }
        } else {
            myViewHolder.txtProviderCompany.setVisibility(View.GONE);
        }
        myViewHolder.btnProviderMoreinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("btnProviderMoreinfo", "onClick: btnProviderMoreinfo-> ");
                HashMap<String, Object> map = new HashMap<>();
                map.put("id", providerArrayList.get(position).getId());
                map.put("fname", providerArrayList.get(position).getFname());
                map.put("lname", providerArrayList.get(position).getLname());
                map.put("date", providerArrayList.get(position).getCurrentDate());
                map.put("time", providerArrayList.get(position).getCurrentTime());
                map.put("miles", providerArrayList.get(position).getMiles());
                map.put("pro_pic", providerArrayList.get(position).getPropic());
                map.put("service_desc", providerArrayList.get(position).getServiceDesc());
                map.put("categoryid", categoryId);
                map.put("rating", providerArrayList.get(position).getRating());
                map.put("click", "moreinfo");
                map.put("subcategoryid", subCategoryId);

                if (successResponse != null)
                    successResponse.onSuccess(map);
            }
        });

        myViewHolder.imgProviderImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("btnProviderMoreinfo", "onClick: btnProviderMoreinfo-> ");
                HashMap<String, Object> map = new HashMap<>();
                map.put("id", providerArrayList.get(position).getId());
                map.put("fname", providerArrayList.get(position).getFname());
                map.put("lname", providerArrayList.get(position).getLname());
                map.put("date", providerArrayList.get(position).getCurrentDate());
                map.put("time", providerArrayList.get(position).getCurrentTime());
                map.put("miles", providerArrayList.get(position).getMiles());
                map.put("pro_pic", providerArrayList.get(position).getPropic());
                map.put("service_desc", providerArrayList.get(position).getServiceDesc());
                map.put("categoryid", categoryId);
                map.put("rating", providerArrayList.get(position).getRating());
                map.put("click", "profileimage");
                map.put("subcategoryid", subCategoryId);

                if (successResponse != null)
                    successResponse.onSuccess(map);
            }
        });

        myViewHolder.btnProviderBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("btnProviderMoreinfo", "onClick: btnProviderMoreinfo-> ");
                HashMap<String, Object> map = new HashMap<>();
                map.put("id", providerArrayList.get(position).getId());
                map.put("fname", providerArrayList.get(position).getFname());
                map.put("lname", providerArrayList.get(position).getLname());
                map.put("date", providerArrayList.get(position).getCurrentDate());
                map.put("time", providerArrayList.get(position).getCurrentTime());
                map.put("miles", providerArrayList.get(position).getMiles());
                map.put("pro_pic", providerArrayList.get(position).getPropic());
                map.put("service_desc", providerArrayList.get(position).getServiceDesc());
                map.put("categoryid", categoryId);
                map.put("rating", providerArrayList.get(position).getRating());
                map.put("click", "profileimage");
                map.put("subcategoryid", subCategoryId);

                if (successResponse != null)
                    successResponse.onSuccess(map);
            }
        });

        Log.i(TAG, "onBindViewHolder: in the position --->" + providerArrayList.get(position).getRating());
        if (providerArrayList.get(position).getRating() == null) {
            Log.i(TAG, "onBindViewHolder: rating is null-->" + providerArrayList.get(position).getId());
        } else {
            try {
                int i = Integer.parseInt(providerArrayList.get(position).getRating());
                System.out.println("It's an integer: " + i);
                myViewHolder.rbProviderRating.setRating(i);
            } catch (NumberFormatException e) {
                try {
                    Float d = Float.parseFloat(providerArrayList.get(position).getRating());
                    myViewHolder.rbProviderRating.setRating(d);
                    //System.out.println("It's a double: " + d);
                    //doFoo(d);
                } catch (NumberFormatException e2) {
                    //System.out.println("It's a String: " + x);
                    // doFoo(x);
                }
            }
        }
        // myViewHolder.rbProviderRating.setRating(Integer.parseInt(providerArrayList.get(position).getRating()));


    }

    public void selected(int position) {
        Log.i("selected", "selected: ");
        selected_pos = position;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return providerArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView imgProviderImage;
        CustomTextView txtProviderName, txtProviderMiles, txtProviderCompany;
        SimpleRatingBar rbProviderRating;
        CustomButton btnProviderMoreinfo, btnProviderBookNow;

        MyViewHolder(View itemView) {
            super(itemView);
            imgProviderImage = (SimpleDraweeView) itemView.findViewById(R.id.img_provider);

            txtProviderName = (CustomTextView) itemView.findViewById(R.id.txt_provider_name);
            txtProviderCompany = (CustomTextView) itemView.findViewById(R.id.txt_Driver_company);
            txtProviderMiles = (CustomTextView) itemView.findViewById(R.id.txt_provider_miles);
            rbProviderRating = (SimpleRatingBar) itemView.findViewById(R.id.rb_provider_rating);

            btnProviderMoreinfo = (CustomButton) itemView.findViewById(R.id.btn_provider_moreinfo);
            btnProviderBookNow = (CustomButton) itemView.findViewById(R.id.btn_provider_booknow);
        }
    }

    static double convertIntoMiles(double km) {
        double miles = km / 1.609;
        return miles;
    }

    double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }
}

