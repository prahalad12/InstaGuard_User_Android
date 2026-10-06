package com.instaguard.user.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.util.Linkify;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.instaguard.user.CustomViews.CustomTextView;
import com.instaguard.user.Helpers.Retrofit.APIClient;
import com.instaguard.user.Helpers.Retrofit.APIInterface;
import com.instaguard.user.Helpers.Utils;
import com.instaguard.user.Models.AppSupportDataModel;
import com.instaguard.user.Models.AppSupportModel;
import com.instaguard.user.R;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

import static android.content.Context.MODE_PRIVATE;
import static com.instaguard.user.Activities.BaseActivity.hideProgressDialog;
import static com.instaguard.user.Activities.BaseActivity.showProgressDialog;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_200;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_201;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_401;
import static com.instaguard.user.Helpers.Utils.RESPONSECODE_500;

public class SupportSubFragment extends Fragment {

    ImageButton imgbtnBack;
    View view;
    LinearLayout llDescription, llContactUs;
    CustomTextView txtSupportSub, txtDescription;

    String id, descrip, userAccessToken, strFrom="main", pageKey, pageName;


    SharedPreferences spCommon;
    SharedPreferences.Editor getUpdateProfile, getRequestState;

    public SupportSubFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint({"LongLogTag", "CommitPrefEdits"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_supportsub, container, false);

        getUpdateProfile = Objects.requireNonNull(requireActivity()).getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE).edit();

        getRequestState = Objects.requireNonNull(requireActivity()).getSharedPreferences(Utils.MY_PREFS_REQUEST_STATE, MODE_PRIVATE).edit();

        spCommon = Objects.requireNonNull(requireActivity()).getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE);
        userAccessToken = spCommon.getString(Utils.SP_USER_ACCESSTOKEN, "123");

        imgbtnBack = view.findViewById(R.id.imgbtn_back);
        txtDescription = view.findViewById(R.id.txt_description);
        llDescription = view.findViewById(R.id.ll_description);
        llContactUs = view.findViewById(R.id.ll_contactus);
        txtSupportSub = view.findViewById(R.id.txt_supportsub);

        if (getArguments() != null) {

            strFrom = getArguments().getString(Utils.B_SUPPORT_FROM);

            pageKey = requireArguments().getString(Utils.B_SUPPORT_STATIC_DYNAMIC);
            pageName = requireArguments().getString(Utils.B_SUPPORT_STATIC_DYNAMIC_PAGE);

            if (pageName.equals("key")) {
                switch (Objects.requireNonNull(requireArguments().getString(Utils.B_SUPPORT))) {
                    case Utils.B_ABOUTUS:
                        llDescription.setVisibility(View.VISIBLE);
                        llContactUs.setVisibility(View.GONE);
                        txtSupportSub.setText(getResources().getString(R.string.support_title_about_us));
                        getAppSupportPage("1");
                        break;
                    case Utils.B_TERMS:
                        llDescription.setVisibility(View.VISIBLE);
                        llContactUs.setVisibility(View.GONE);
                        txtSupportSub.setText(getResources().getString(R.string.support_title_terms_conditions));
                        getAppSupportPage("3");
                        break;
                    case Utils.B_PRIVACY:
                        llDescription.setVisibility(View.VISIBLE);
                        llContactUs.setVisibility(View.GONE);
                        txtSupportSub.setText(getResources().getString(R.string.support_title_privacy_policy));
                        getAppSupportPage("2");
                        break;
                    case Utils.B_CONTACTUS:
                        llDescription.setVisibility(View.VISIBLE);
                        llContactUs.setVisibility(View.GONE);
                        txtSupportSub.setText(getResources().getString(R.string.support_contact_us));
                        getAppSupportPage("4");
                        break;
                    case Utils.B_FAQ:
                        llDescription.setVisibility(View.VISIBLE);
                        llContactUs.setVisibility(View.GONE);
                        txtSupportSub.setText(getResources().getString(R.string.faq));
                        getAppSupportPage("5");
                        break;
                }
            } else {
                llDescription.setVisibility(View.VISIBLE);
                llContactUs.setVisibility(View.GONE);
                txtSupportSub.setText(pageName);
                getAppSupportPage("0");
            }
        }

        OnClick();

        onBackButton(view);

        return view;
    }

    private void getAppSupportPage(String key) {

        userAccessToken = spCommon.getString(Utils.SP_USER_ACCESSTOKEN, null);

        HashMap<String, Object> map = new HashMap<>();

        if (pageKey.equals("static")) {
            if (key.equalsIgnoreCase("1")) {
                map.put("page_name", "About Us");
            } else if (key.equalsIgnoreCase("2")) {
                map.put("page_name", "Privacy Policy");
            } else if (key.equalsIgnoreCase("3")) {
                map.put("page_name", "Terms and Conditions");
            } else if (key.equalsIgnoreCase("4")) {
                map.put("page_name", "Contact Us");
            } else if (key.equalsIgnoreCase("5")) {
                map.put("page_name", "FAQ's");
            }
        } else {
            if (key.equalsIgnoreCase("0")) {
                map.put("page_name", pageName);
            }
        }

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<AppSupportModel> call2 = apiInterface.doGetAppSupportDetails("Bearer " + userAccessToken, map);
        showProgressDialog(requireActivity());
        call2.enqueue(new Callback<AppSupportModel>() {
            @Override
            public void onResponse(@NonNull Call<AppSupportModel> call, @NonNull retrofit2.Response<AppSupportModel> response) {
                if (response.code() == RESPONSECODE_200 || response.code() == RESPONSECODE_201) {
                    hideProgressDialog();
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Pages listed successfully.")
                    || Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("Pages listed successfully.")) {
                        List<AppSupportDataModel> getve = Objects.requireNonNull(response.body()).getAppSupportDataModel();

                        for (int i = 0; i < getve.size(); i++) {
                            descrip = getve.get(i).getDescription();
                            id = String.valueOf(getve.get(i).getId());
                            // txtDescription.setText(descrip);
                            System.out.println("~~~~~getDescription~~~~~~"+id);
                            try {
                                txtDescription.setText(Html.fromHtml(descrip));
                                Linkify.addLinks(txtDescription, Linkify.ALL);
                                txtDescription.setLinkTextColor(Color.parseColor("#FF0000"));

                            } catch (Exception e) {
                                e.printStackTrace();
                                txtDescription.setText(HtmlCompat.fromHtml(descrip, HtmlCompat.FROM_HTML_MODE_LEGACY));
                            }
                        }

                        hideProgressDialog();
                    } else if (response.body().getMessage().equals("No records Found.")) {
                        Toast.makeText(requireActivity(), R.string.no_record_found, Toast.LENGTH_SHORT).show();
                    }

                    hideProgressDialog();
                } else if (response.code() == RESPONSECODE_401) {
                    hideProgressDialog();
                    Toast.makeText(requireActivity(), R.string.no_record_found, Toast.LENGTH_SHORT).show();
                } else if (response.code() == RESPONSECODE_500) {
                    hideProgressDialog();
                    Toast.makeText(requireActivity(), "Error 500", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<AppSupportModel> call, @NonNull Throwable t) {
                Log.i("errror", "errr" + t);
                call.cancel();
                hideProgressDialog();
            }
        });

    }

    private void OnClick() {
        imgbtnBack.setOnClickListener(v -> {
            hideKeyBoard();
            backButtonFunction();
        });

    }

    public void onBackButton(View view) {
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP
                    && keyCode == KeyEvent.KEYCODE_BACK) {
                backButtonFunction();
            }
            return true;
        });
    }

    public void backButtonFunction() {
        int i;
        if (strFrom.equals("register")) {
            i = R.id.frame_signup;
        } else if (strFrom.equals("register_social")) {
            i = R.id.frame_signup_social;
        } else {
            i = R.id.frame_mainactivity;
        }
        Bundle args = new Bundle();
        args.putString(Utils.B_SUPPORT_FROM, strFrom);
        Utils.switchContent(i,
                Utils.FRAGMENT_SUPPORT,
                requireActivity(), args,
                Utils.AnimationType.SLIDE_IN_SLIDE_OUT);
    }

    public void removeFragment(String Tag) {
        Utils.CURRENT_TAG = null;
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(Objects.requireNonNull(fragmentManager.findFragmentByTag(Tag))).commit();
    }


    public void hideKeyBoard() {
        // Check if no view has focus:
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}