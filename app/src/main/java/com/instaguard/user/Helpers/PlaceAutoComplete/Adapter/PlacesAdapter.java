package com.instaguard.user.Helpers.PlaceAutoComplete.Adapter;

import android.app.Activity;
import android.content.Context;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.model.LatLng;
import com.instaguard.user.Activities.BaseActivity;
import com.instaguard.user.Activities.MainActivity;
import com.instaguard.user.Helpers.Controller;
import com.instaguard.user.Helpers.PlaceAutoComplete.PlacesAutoComplete;
import com.instaguard.user.Helpers.PlaceAutoComplete.Prediction;
import com.instaguard.user.Helpers.Utils;
import com.instaguard.user.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.instaguard.user.Activities.BaseActivity.hideProgressDialog;
import static com.instaguard.user.Activities.BaseActivity.showProgressDialog;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder> {

    private ArrayList<Prediction> placesList;
    public Activity activity;
    public Context context;
    String layout,page;

    private PlacesAutoComplete placesAutoComplete;

    public PlacesAdapter(ArrayList<Prediction> placesList, Activity activity,Context context,String layout,String page) {
        this.placesList = placesList;
        this.activity = activity;
        this.context = context;
        this.layout = layout;
        this.page = page;
    }

    public void setPlacesAutoComplete(PlacesAutoComplete placesAutoComplete) {
        this.placesAutoComplete = placesAutoComplete;
    }

    @Override
    public PlacesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (activity).getLayoutInflater();
        View view = inflater.inflate(R.layout.adapter_placesautocomplete, parent, false);
        return new PlacesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlacesAdapter.ViewHolder holder, int position) {

        final Prediction prediction = placesList.get(position);

        if (placesAutoComplete.getStatus().equals("OK")) {

            holder.name.setText(prediction.getStructuredFormatting().getMainText());

            holder.location.setText(prediction.getStructuredFormatting().getSecondaryText());

            holder.places_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideKeyBoard();
                    showProgressDialog(activity);
                    getLatLngFromAddress(layout,page,prediction.getDescription(),prediction.getPlaceId(),prediction);

                    //mapsActivity.onBackPressed();
                }
            });
        }
    }
    public void backButtonFunction(String page) {
       // MainActivity.frameClickable(false);
        if(page.equals(Utils.AUTOCOMPLETE_MAINACTIVITY)){
            removeFragment(Utils.FRAGMENT_PLACESAUTOCOMPLETE);
        }/* else if(page.equals(Utils.AUTOCOMPLETE_SCHEDULETRIP)) {
            removeFragment(Utils.FRAGMENT_SCHEDULETRIP_PLACESAUTOCOMPLETE);
        }*/

    }
    public void removeFragment(String Tag) {
        Utils.CURRENT_TAG = null;
        FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag(Tag)).commit();
    }
    @Override
    public int getItemCount() {
        return placesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, location;
        LinearLayout places_layout;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.place_name);
            location = itemView.findViewById(R.id.place_detail);
            places_layout = itemView.findViewById(R.id.view_layout);
        }
    }

    private void hideKeyBoard()
    {
        // Check if no view has focus:
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void getLatLngFromAddress(String layout,String page,String geoAddress,String place_id, Prediction prediction) {
        String urlJsonObj = "https://maps.googleapis.com/maps/api/geocode/json?place_id=" + place_id + "&key="+
                BaseActivity.getGoogleApiKey(activity);
        System.out.println("json rl" + urlJsonObj);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlJsonObj, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("", response.toString());
                System.out.println("enter after url response" + response);

                try {

                    JSONArray Contacts = response.getJSONArray("results");
                    System.out.println("enter the values of the" + Contacts);
                    JSONObject c = Contacts.getJSONObject(0);
                    String location_string = c.getString("formatted_address");
                    Log.d("test", "formattted address:" + location_string);

                    //getLatLong(response);
                    try {
                        Double  latitude = ((JSONArray)response.get("results")).getJSONObject(0)
                                .getJSONObject("geometry").getJSONObject("location")
                                .getDouble("lat");

                        Double longitute = ((JSONArray)response.get("results")).getJSONObject(0)
                                .getJSONObject("geometry").getJSONObject("location")
                                .getDouble("lng");
                        Log.e("Longitute~~~~", String.valueOf(longitute));
                        Log.e("Latitude~~~~", String.valueOf(latitude));
                        if(latitude!=null &&longitute!=null ){
                            LatLng tempLatLng = new LatLng(latitude,longitute);
                            hideProgressDialog();
                            if(page.equals(Utils.AUTOCOMPLETE_MAINACTIVITY)){
                                Log.i("", "onResponse: if page called-->"+page);
                                MainActivity mapsActivity = (MainActivity) activity;
                                mapsActivity.onPlaceSelected(layout,geoAddress, prediction,tempLatLng);

                            } /*else if(page.equals(Utils.AUTOCOMPLETE_SCHEDULETRIP)){
                                Log.i(TAG, "onResponse: else page called-->"+page);
                                //ScheduleTripFragment scheduleTripFragment = (ScheduleTripFragment) Fragment;
                                ScheduleTripFragment.onPlaceSelected(layout,geoAddress, prediction,tempLatLng);
                            }*/
                            backButtonFunction(page);

                        }else{
                            Toast.makeText(activity, R.string.try_again, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("", "Error: " + error.getMessage());
                System.out.println("exception of the url" + error.getMessage());
                // hide the progress dialog

            }
        });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        Controller.getInstance().addToRequestQueue(jsonObjReq);
    }

    private static void getLatLong(JSONObject jsonObject) {



    }
}