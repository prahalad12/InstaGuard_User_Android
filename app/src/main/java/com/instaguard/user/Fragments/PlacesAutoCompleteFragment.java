package com.instaguard.user.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

/*import com.spotnride.customer.Activities.MainActivity;
import com.spotnride.customer.CustomViews.CustomTextView;
import com.spotnride.customer.Helpers.Controller;
import com.spotnride.customer.Helpers.PlaceAutoComplete.Adapter.PlacesAdapter;
import com.spotnride.customer.Helpers.PlaceAutoComplete.PlacesAutoComplete;
import com.spotnride.customer.Helpers.PlaceAutoComplete.Prediction;
import com.spotnride.customer.Helpers.Retrofit.APIInterface;
import com.spotnride.customer.Helpers.Utils;
import com.spotnride.customer.R;*/

import com.instaguard.user.Activities.BaseActivity;
import com.instaguard.user.Activities.MainActivity;
import com.instaguard.user.CustomViews.CustomTextView;
import com.instaguard.user.Helpers.Controller;
import com.instaguard.user.Helpers.PlaceAutoComplete.Adapter.PlacesAdapter;
import com.instaguard.user.Helpers.PlaceAutoComplete.PlacesAutoComplete;
import com.instaguard.user.Helpers.PlaceAutoComplete.Prediction;
import com.instaguard.user.Helpers.Retrofit.APIInterface;
import com.instaguard.user.Helpers.Utils;
import com.instaguard.user.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.instaguard.user.Activities.MainActivity.cameraIdleValue;
import static com.instaguard.user.Activities.MainActivity.locFusedLocation;


public class PlacesAutoCompleteFragment extends Fragment implements TextWatcher, Callback<PlacesAutoComplete> {

    EditText edtPlacesAutoComplete;
    ImageButton imgbtnClear,imgbtnBack;
    RecyclerView rvPlacesAutoComplete;
    RelativeLayout rlPlacesAutoComplete;

    private APIInterface retrofitAPI;
    private PlacesAdapter placesAdapter;
    private ArrayList<Prediction> placesList = new ArrayList<>();
    private String mCurrentLocation,placeAutoCompleteLayout;
    CustomTextView txtToolbarTitle;
    String strLayout;

    public PlacesAutoCompleteFragment() {
        // Required empty public constructor
    }

    public static PlacesAutoCompleteFragment newInstance() {
        return new PlacesAutoCompleteFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_placesautocomplete, container, false);

        Controller.setCacheInstance(Utils.AUTO_COMPLETE_URL);
        retrofitAPI = Controller.getCacheService();

        edtPlacesAutoComplete = (EditText)view.findViewById(R.id.edt_placesautocomplete);
        txtToolbarTitle = (CustomTextView) view.findViewById(R.id.txt_toolbartitle);
        imgbtnClear = (ImageButton) view.findViewById(R.id.imgbtn_clear);
        imgbtnBack = (ImageButton) view.findViewById(R.id.imgbtn_back);
        rvPlacesAutoComplete = (RecyclerView)view.findViewById(R.id.rv_placesautocomplete);
        rlPlacesAutoComplete = (RelativeLayout) view.findViewById(R.id.rl_placesearchlayout);
        //rlPlacesAutoComplete.setVisibility(View.GONE);
        // mCurrentLocation = getArguments().getString("strLocation");
        /*if (mCurrentLocation == null)
            mCurrentLocation = "";*/
        if(getArguments() != null) {
            placeAutoCompleteLayout = getArguments().getString(Utils.B_AUTOCOMPLETELAYOUT);
            if(placeAutoCompleteLayout.equals(Utils.B_PICKUPAUTOCOMPLETELAYOUT)){
                strLayout = "pickup";
                txtToolbarTitle.setText(getString(R.string.map_enter_pickup_location));
                edtPlacesAutoComplete.setHint(getString(R.string.map_enter_pickup_location));
            }else {
                strLayout = "drop";
                txtToolbarTitle.setText(getString(R.string.map_enter_drop_location));
                edtPlacesAutoComplete.setHint(getString(R.string.map_enter_drop_location));
            }
        }

        view.findViewById(R.id.rl_parent_lyt_place).setOnClickListener(view1 -> {});

        imgbtnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard();
                if(placeAutoCompleteLayout.equals(Utils.B_PICKUPAUTOCOMPLETELAYOUT)){
                    edtPlacesAutoComplete.setText("");
                    edtPlacesAutoComplete.setHint(getString(R.string.map_enter_pickup_location));
                }else {
                    edtPlacesAutoComplete.setText("");
                    edtPlacesAutoComplete.setHint(getString(R.string.map_enter_drop_location));
                }
                placesList.clear();
                placesAdapter.notifyDataSetChanged();
            }
        });
        imgbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard();
                backButtonFunction();
               /* edtPlacesAutoComplete.setText("");
                placesList.clear();
                placesAdapter.notifyDataSetChanged();*/
            }
        });

        return view;
    }
    public void backButtonFunction() {
        cameraIdleValue = "1";
       // MainActivity.frameClickable(false);
        removeFragment(Utils.FRAGMENT_PLACESAUTOCOMPLETE);
    }
    public void removeFragment(String Tag) {
        Utils.CURRENT_TAG = null;
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag(Tag)).commit();
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),
                LinearLayoutManager.VERTICAL, false);
        rvPlacesAutoComplete.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecorator(ContextCompat.getDrawable(getActivity(), R.drawable.ic_divider_line));
        rvPlacesAutoComplete.addItemDecoration(dividerItemDecoration);
        placesAdapter = new PlacesAdapter(placesList, getActivity(),getContext(),strLayout,Utils.AUTOCOMPLETE_MAINACTIVITY);
        rvPlacesAutoComplete.setAdapter(placesAdapter);
        edtPlacesAutoComplete.addTextChangedListener(this);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        rlPlacesAutoComplete.setVisibility(View.VISIBLE);
        // rlPlacesAutoComplete.animate().translationY(10);
        //rlPlacesAutoComplete.setAlpha(0.0f);
        // Start the animation
        rlPlacesAutoComplete.animate()
                .translationY(5)
                .alpha(1.0f)
                .setListener(null);
        try{
            if (editable.length() == 0){
                rlPlacesAutoComplete.animate()
                        .translationY(0)
                        .alpha(0.0f)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                rlPlacesAutoComplete.setVisibility(View.GONE);
                            }
                        });
                //  rlPlacesAutoComplete.setVisibility(View.GONE);
                placesList.clear();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        getPlacesList(edtPlacesAutoComplete.getText().toString().trim());
    }

    private void getPlacesList(String query) {

        if (locFusedLocation != null) {
            Log.i("", "currentLocationClick: "+ locFusedLocation.getLatitude() + " " + locFusedLocation.getLongitude());
            mCurrentLocation =  locFusedLocation.getLatitude() + "," + locFusedLocation.getLongitude();
            Log.e("", "getPlacesList: ~~~~~~~~~~~~~~~~~"+mCurrentLocation);
            Call<PlacesAutoComplete> autoCompleteCall = retrofitAPI.getPlaces(query, mCurrentLocation,
                    BaseActivity.getGoogleApiKey(requireActivity()), "5000");
                    //getString(R.string.map_api_key), "5000");
            autoCompleteCall.enqueue(this);
            System.out.println("getPlacesList-->"+autoCompleteCall.request().url().toString());

        } else {
            Call<PlacesAutoComplete> autoCompleteCall = retrofitAPI.getPlaces(query, mCurrentLocation,
                    BaseActivity.getGoogleApiKey(requireActivity()), "5000");
                    //getString(R.string.map_api_key), "5000");
            autoCompleteCall.enqueue(this);
            System.out.println("getPlacesList-->"+autoCompleteCall.request().url().toString());
        }

    }

    @Override
    public void onResponse(@NonNull Call<PlacesAutoComplete> call, @NonNull Response<PlacesAutoComplete> response) {
        PlacesAutoComplete placesAutoComplete = response.body();
        Log.e("PlaceAutocomplet~~~", String.valueOf(placesAutoComplete));
        if (placesAutoComplete == null) {
            return;
        }
        if (placesAutoComplete.getStatus().equals("OK")) {
            placesAdapter.setPlacesAutoComplete(placesAutoComplete);
            placesList.clear();
            placesList.addAll(placesAutoComplete.getPredictions());
            placesAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFailure(@NonNull Call<PlacesAutoComplete> call, @NonNull Throwable t) {
        t.printStackTrace();
    }

    public class DividerItemDecorator extends RecyclerView.ItemDecoration {
        private Drawable mDivider;

        public DividerItemDecorator(Drawable divider) {
            mDivider = divider;
        }

        @Override
        public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
            int dividerLeft = parent.getPaddingLeft();
            int dividerRight = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i <= childCount - 2; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int dividerTop = child.getBottom() + params.bottomMargin;
                int dividerBottom = dividerTop + mDivider.getIntrinsicHeight();

                mDivider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
                mDivider.draw(canvas);
            }
        }
    }

    public void hideKeyBoard()
    {
        // Check if no view has focus:
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}