package com.instaguard.user.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.instaguard.user.Activities.BaseActivity;
import com.instaguard.user.Activities.HomeActivity;
import com.instaguard.user.Adapter.EmergencyViewAdapter;
import com.instaguard.user.CustomViews.CustomButton;
import com.instaguard.user.CustomViews.CustomTextView;
import com.instaguard.user.Helpers.RecyclerViewsTools.RecyclerTouchListener;
import com.instaguard.user.Helpers.Retrofit.APIClient;
import com.instaguard.user.Helpers.Retrofit.APIInterface;
import com.instaguard.user.Helpers.Utils;
import com.instaguard.user.Models.EmergencyAddModel;
import com.instaguard.user.Models.EmergencyDeleteModel;
import com.instaguard.user.Models.EmergencyModel;
import com.instaguard.user.Models.EmergencyViewDataModel;
import com.instaguard.user.Models.EmergencyViewModel;
import com.instaguard.user.Models.RefreshTokenModel;
import com.instaguard.user.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.instaguard.user.Activities.BaseActivity.hideProgressDialog;
import static com.instaguard.user.Activities.BaseActivity.showProgressDialog;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_200;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_201;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_401;

public class EmergencyContactsFragment extends Fragment {

    private static final String TAG = "EmergencyContactsFragment";
    View view;
    SharedPreferences SP_Prefs;
    String driverId;
    ImageButton imgbtnBack;

    CustomButton btnAddContact;
    CustomTextView txtNotify;
    LinearLayout llBbottomAddcontact, rlContactImageArea;

    RecyclerView rcvEmergencyView;

    ArrayList<EmergencyModel> emergencyModelArray;
    private EmergencyModel listFeed;
    EmergencyViewAdapter emergencyViewAdapter;


    SharedPreferences.Editor getUpdateEmergncy;
    SharedPreferences spGetEmergency;

    String userId, userAccessToken,contact_id,contact_num,contact_name,contact_status;
    int PICK_CONTACT = 2121;

    public EmergencyContactsFragment() {
        // Required empty public constructor
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Log.e("userId~~RideHistoryFragment~~", "");
        }
    }

    @SuppressLint({"LongLogTag", "CommitPrefEdits"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_emergency_contact, container, false);

        getUpdateEmergncy = Objects.requireNonNull(getActivity()).getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE).edit();
        spGetEmergency = getActivity().getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE);
        userId = spGetEmergency.getString(Utils.SP_USERID, null);
        userAccessToken = spGetEmergency.getString(Utils.SP_USER_ACCESSTOKEN, null);
        Log.e("UserId~~EmergencyContactsFragment~~", userId);
        initView(view);

        onBackButton(view);

        return view;

    }

    void initView(View view) {


        imgbtnBack = (ImageButton) view.findViewById(R.id.imgbtn_back);
        btnAddContact = (CustomButton) view.findViewById(R.id.btn_add_contact);

        txtNotify = (CustomTextView) view.findViewById(R.id.txt_notify);

        llBbottomAddcontact = (LinearLayout) view.findViewById(R.id.rl_bottom_add_contact);
        rlContactImageArea = (LinearLayout) view.findViewById(R.id.rl_noContactArea);

        rcvEmergencyView = (RecyclerView) view.findViewById(R.id.rcv_emergency_view);


        imgbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButtonFunction();
            }
        });

        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (Objects.requireNonNull(getActivity()).checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                        contactIntent();
                    } else {
                        getActivity().requestPermissions(new String[]{android.Manifest.permission.READ_CONTACTS}, 5);
                    }
                } else {
                    contactIntent();
                }
            }
        });

        emergencyModelArray = new ArrayList<EmergencyModel>();
        emergencyViewAdapter = new EmergencyViewAdapter(emergencyModelArray, getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcvEmergencyView.setLayoutManager(linearLayoutManager);
        rcvEmergencyView.setAdapter(emergencyViewAdapter);

        rcvEmergencyView.addOnItemTouchListener(
                new RecyclerTouchListener(getActivity(), rcvEmergencyView,
                        new RecyclerTouchListener.OnTouchActionListener() {
                            @SuppressLint("LongLogTag")
                            @Override
                            public void onClick(View view, int position) {
                                Log.i("rvCategory", "onClick: rvCategory-> ");
                                if(position != -1){
                                    if(emergencyModelArray.size()>0){
                                    listFeed  = emergencyModelArray.get(position);
                                   /* Log.e("getContact_name~~",listFeed.getContact_name());
                                    Log.e("getContact_num~~",listFeed.getContact_num());
                                    Log.e("getContact_cust_id~~",listFeed.getContact_cust_id());
                                    Log.e("getContact_id~~",listFeed.getContact_id());*/
                                    callDeleteEmergencyDetails(listFeed.getContact_id());
                                    }
                                }

                            }
                            @Override
                            public void onRightSwipe(View view, int position) {
                            }
                            @Override
                            public void onLeftSwipe(View view, int position) {
                            }
                        }));

        callViewEmergencyDetails();
    }

    private void contactIntent() {
        Intent paramView = new Intent("android.intent.action.PICK");
        paramView.setType("vnd.android.cursor.dir/contact");
        paramView.setType("vnd.android.cursor.dir/phone_v2");
        startActivityForResult(paramView, PICK_CONTACT);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
        super.onActivityResult(paramInt1, paramInt2, paramIntent);
        Log.i(TAG, "onActivityResult: in Emergency contact" + paramInt1 + "~~" + paramInt2 + "~~" + paramIntent);

        if ((paramInt1 == PICK_CONTACT) && (paramIntent != null)) {
            Object localObject = paramIntent.getData();
            Log.e("localObject~~", String.valueOf(localObject));

            if (localObject != null) {
                paramIntent = null;
                try {
                    ContentResolver contentResolver = Objects.requireNonNull(getActivity()).getContentResolver();
                    Cursor localCursor = contentResolver.query((Uri) localObject, new String[]{"data1", "display_name", "data2"}, null, null, null);
                    localObject = localCursor;
                    if (localCursor != null) {
                        //  paramIntent = (Intent) localObject;
                        boolean bool = localCursor.moveToFirst();
                        Log.e("localObject~~", String.valueOf(bool));
                        if (bool) {
                            //  paramIntent = (Intent) localObject;
                            String str = localCursor.getString(localCursor.getColumnIndex("data1"));
                            //  paramIntent = (Intent) localObject;
                            Log.e("Contact~~~" + localCursor.getString(localCursor.getColumnIndex("display_name")), str);

                            addContact(localCursor.getString(localCursor.getColumnIndex("display_name")), str);
                        }
                    }
                    if (localCursor != null) {
                        localCursor.close();
                    }
                } catch (Throwable localThrowable) {
                    if (paramIntent != null) {
                        paramIntent.clone();
                    }
                    throw localThrowable;
                }
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 5) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                contactIntent();
            } else {
                Toast.makeText(getActivity(), "Please provide permission to use content", Toast.LENGTH_SHORT).show();
            }
        }/*else if (requestCode == 6) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                galleryIntent();
            } else {
                Toast.makeText(getActivity(), "Please provide permission to use gallery", Toast.LENGTH_SHORT).show();
            }
        }*/
    }
    @SuppressLint("LongLogTag")
    public void addContact(String strcontact_name, String number) {
        Log.i(TAG, "addContact:~" + strcontact_name + "~~" + strcontact_name);
        userAccessToken = spGetEmergency.getString(Utils.SP_USER_ACCESSTOKEN, null);
        HashMap<String, Object> map = new HashMap<>();
        map.put("customer_id", userId);
        map.put("phone_number", number);
        map.put("status", "1");
        map.put("contact_name", strcontact_name);
        // map.put("token", userAccessToken);
        Log.i("EmergencyAddModel", "~~~~" + map);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<EmergencyAddModel> call2 = apiInterface.doAddemergencyContactCustomer("Bearer " + userAccessToken, map);
        showProgressDialog(getActivity());
        call2.enqueue(new Callback<EmergencyAddModel>() {
            @Override
            public void onResponse(@NonNull Call<EmergencyAddModel> call, @NonNull Response<EmergencyAddModel> response) {
                hideProgressDialog();
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Inserted successfully")) {

                       /* rcvEmergencyView.setVisibility(View.VISIBLE);
                        rlContactImageArea.setVisibility(View.GONE);

                        emergencyViewAdapter.notifyDataSetChanged();*/
                        if (emergencyModelArray != null){
                            emergencyModelArray.clear();
                            callViewEmergencyDetails();
                        }


                    } else if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Limit Exceeded")) {
                        Toast.makeText(getActivity(), R.string.emergency_contact_remove_contact, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), R.string.try_again, Toast.LENGTH_SHORT).show();
                        callRefreshTokenAddContact("emergency", strcontact_name, number);
                    }
                } else if (response.code() == RESPONSECODE_401) {
                    callRefreshTokenAddContact("emergency", strcontact_name, number);
                }
            }

            @Override
            public void onFailure(@NonNull Call<EmergencyAddModel> call, @NonNull Throwable t) {
                // CommonUtil.showRetrofitError(LoginActivity.this, error);
                hideProgressDialog();
                call.cancel();
            }
        });
    }


    private void callViewEmergencyDetails() {

        userAccessToken = spGetEmergency.getString(Utils.SP_USER_ACCESSTOKEN, null);
        HashMap<String, Object> map = new HashMap<>();
        map.put("customer_id", userId);
//        map.put("token",userAccessToken );
        Log.i("CallViewProfileDetails", "~~~~" + map);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<EmergencyViewModel> call2 = apiInterface.doGetViewEmergencyDetails("Bearer " + userAccessToken, map);
        showProgressDialog(getActivity());
        call2.enqueue(new Callback<EmergencyViewModel>() {
            @SuppressLint({"SetTextI18n", "LongLogTag"})
            @Override
            public void onResponse(@NonNull Call<EmergencyViewModel> call, @NonNull Response<EmergencyViewModel> response) {
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("customer emergency Contact listed successfully.")) {
                        hideProgressDialog();
                        rcvEmergencyView.setVisibility(View.VISIBLE);
                        rlContactImageArea.setVisibility(View.GONE);

                        List<EmergencyViewDataModel> getve = Objects.requireNonNull(response.body()).getEmergencyViewDataModel();
                        Log.e(TAG, "onResponse: "+getve.size());
                        for (int i = 0; i < getve.size(); i++) {
                            EmergencyModel emergencyModel = new EmergencyModel();
                            emergencyModel.setContact_id(String.valueOf(getve.get(i).getId()));
                            emergencyModel.setContact_name(getve.get(i).getContactName());
                            emergencyModel.setContact_num(getve.get(i).getPhoneNumber());
                            emergencyModel.setContact_cust_id(String.valueOf(getve.get(i).getCustomerId()));

                            /*contact_id = Objects.requireNonNull(response.body()).getEmergencyAddDataModel().getCustomerId();
                            contact_num = Objects.requireNonNull(response.body()).getEmergencyAddDataModel().getPhoneNumber();
                            contact_name = Objects.requireNonNull(response.body()).getEmergencyAddDataModel().getContactName();
                            contact_status = Objects.requireNonNull(response.body()).getEmergencyAddDataModel().getStatus();*/

                            emergencyModelArray.add(emergencyModel);
                        }

                        if (getve.size() >= 5) {
                            rcvEmergencyView.setVisibility(View.VISIBLE);
                            llBbottomAddcontact.setVisibility(View.GONE);
                            rlContactImageArea.setVisibility(View.GONE);
                        }else {
                            llBbottomAddcontact.setVisibility(View.VISIBLE);
                        }

                    }else if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("No records Found.")){
                        hideProgressDialog();
                        rcvEmergencyView.setVisibility(View.GONE);
                        llBbottomAddcontact.setVisibility(View.VISIBLE);
                        rlContactImageArea.setVisibility(View.VISIBLE);
                    }else {
                        callRefreshToken("viewemergency","");
                    }
                    emergencyViewAdapter.notifyDataSetChanged();
                    hideProgressDialog();
                } else if (response.code() == RESPONSECODE_401) {

                    callRefreshToken("viewemergency","");
                }
            }

            @Override
            public void onFailure(@NonNull Call<EmergencyViewModel> call, @NonNull Throwable t) {
                hideProgressDialog();
                Toast.makeText(getActivity(),R.string.try_again, Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });


    }



    private void callDeleteEmergencyDetails(String contact_id) {

        userAccessToken = spGetEmergency.getString(Utils.SP_USER_ACCESSTOKEN, null);
        HashMap<String, Object> map = new HashMap<>();
        map.put("contact_id", contact_id);
//        map.put("token",userAccessToken );
        Log.i("CallViewProfileDetails", "~~~~" + map);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<EmergencyDeleteModel> call2 = apiInterface.doGetDeleteEmergencyDetails("Bearer " + userAccessToken,Utils.POST_ACCEPT_CONTENT_TYPE, map);
        showProgressDialog(getActivity());
        call2.enqueue(new Callback<EmergencyDeleteModel>() {
            @SuppressLint({"SetTextI18n", "LongLogTag"})
            @Override
            public void onResponse(@NonNull Call<EmergencyDeleteModel> call, @NonNull Response<EmergencyDeleteModel> response) {
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Deleted successfully.")) {
                        hideProgressDialog();
                        if (emergencyModelArray != null){
                            emergencyModelArray.clear();
                            callViewEmergencyDetails();
                        }
                       // Toast.makeText(getActivity(), "Deleted successfully.", Toast.LENGTH_SHORT).show();
                    }else {
                        callRefreshToken("deleteemergency",contact_id);
                    }
                    hideProgressDialog();
                } else if (response.code() == RESPONSECODE_401) {

                    callRefreshToken("deleteemergency",contact_id);
                }
            }

            @Override
            public void onFailure(@NonNull Call<EmergencyDeleteModel> call, @NonNull Throwable t) {
                hideProgressDialog();
                Toast.makeText(getActivity(), "Please Try Again", Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });


    }
    private void callRefreshTokenAddContact(String logout, String contact_name, String number) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", userId);
        map.put("token", userAccessToken);
        Log.e("Logout~~Refresh~~~", "Refresh~~~" + map);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<RefreshTokenModel> call2 = apiInterface.doGetRefreshToken(Utils.POST_ACCEPT_CONTENT_TYPE, Utils.POST_ACCEPT_CONTENT_TYPE, map);
        call2.enqueue(new Callback<RefreshTokenModel>() {
            @Override
            public void onResponse(@NonNull Call<RefreshTokenModel> call, @NonNull Response<RefreshTokenModel> response) {
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    String refreshToken = Objects.requireNonNull(response.body()).getAccessToken();
                    String refreshTokenType = Objects.requireNonNull(response.body()).getTokenType();
                    String refreshTokenTimeExp = String.valueOf(Objects.requireNonNull(response.body()).getExpiresIn());
                    savesharedpreference(refreshToken, refreshTokenType, refreshTokenTimeExp);
                    if (logout.equalsIgnoreCase("emergency")) {
                        addContact(contact_name, number);
                    }
                } else if (response.code() == RESPONSECODE_401) {
                    showTimeOut();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RefreshTokenModel> call, @NonNull Throwable t) {
                call.cancel();
            }
        });
    }

    private void callRefreshToken(String logout,String delete) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", userId);
        map.put("token", userAccessToken);
        Log.e("Logout~~Refresh~~~", "Refresh~~~" + map);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<RefreshTokenModel> call2 = apiInterface.doGetRefreshToken(Utils.POST_ACCEPT_CONTENT_TYPE, Utils.POST_ACCEPT_CONTENT_TYPE, map);
        call2.enqueue(new Callback<RefreshTokenModel>() {
            @Override
            public void onResponse(@NonNull Call<RefreshTokenModel> call, @NonNull Response<RefreshTokenModel> response) {
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    String refreshToken = Objects.requireNonNull(response.body()).getAccessToken();
                    String refreshTokenType = Objects.requireNonNull(response.body()).getTokenType();
                    String refreshTokenTimeExp = String.valueOf(Objects.requireNonNull(response.body()).getExpiresIn());
                    savesharedpreference(refreshToken, refreshTokenType, refreshTokenTimeExp);
                    if (logout.equalsIgnoreCase("viewemergency")) {
                        callViewEmergencyDetails();
                    }else if (logout.equalsIgnoreCase("deleteemergency")) {
                        callDeleteEmergencyDetails(delete);
                    }
                } else if (response.code() == RESPONSECODE_401) {
                    showTimeOut();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RefreshTokenModel> call, @NonNull Throwable t) {
                call.cancel();
            }
        });
    }

    private void savesharedpreference(String access_token, String token_type, String exp_time) {

        getUpdateEmergncy.putString(Utils.SP_USER_ACCESSTOKEN, access_token);
        getUpdateEmergncy.putString(Utils.SP_USER_TOKENTYPE, token_type);
        getUpdateEmergncy.putString(Utils.SP_USER_TOKENEXPTIME, exp_time);
        getUpdateEmergncy.apply();
    }


    public void showTimeOut() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),
                getString(R.string.App_Font));

        BaseActivity.CustomTFSpan tfSpan = new BaseActivity.CustomTFSpan(tf);
        SpannableString spannableString = new SpannableString(getString(R.string.timeout_title));
        spannableString.setSpan(tfSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString spannableStrings = new SpannableString(getString(R.string.your_session_time_out_content));
        spannableStrings.setSpan(tfSpan, 0, spannableStrings.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString spannableStringss = new SpannableString(getString(R.string.ok));
        spannableStringss.setSpan(tfSpan, 0, spannableStringss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        builder.setMessage(spannableStrings);
        builder.setPositiveButton(spannableStringss,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearSPmodePrivate();
                    }
                });

        builder.show();
    }

    private void clearSPmodePrivate() {
        SharedPreferences preferences = Objects.requireNonNull(getActivity()).getSharedPreferences(Utils.MY_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        // deleteCache(MainActivity.this);
        Intent logout = new Intent(getActivity(), HomeActivity.class);
        logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(logout);
        getActivity().finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public void onBackButton(View view) {
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP
                        && keyCode == KeyEvent.KEYCODE_BACK) {
                    backButtonFunction();
                }
                return true;
            }
        });
    }

    public void backButtonFunction() {
        //  MainActivity.frameClickable(false);
        removeFragment(Utils.FRAGMENT_EMERGENCY_CONTACTS);
    }

    public void removeFragment(String Tag) {
        Utils.CURRENT_TAG = null;
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag(Tag)).commit();
    }


}
