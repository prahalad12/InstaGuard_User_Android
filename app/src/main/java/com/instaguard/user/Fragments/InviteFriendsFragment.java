package com.instaguard.user.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.ads.AdView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.instaguard.user.Activities.BaseActivity;
import com.instaguard.user.CustomViews.CustomButton;
import com.instaguard.user.CustomViews.CustomTextView;
import com.instaguard.user.Helpers.Utils;
import com.instaguard.user.R;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class InviteFriendsFragment extends Fragment {

    ImageButton imgbtnBack;
    CustomButton btnInviteFriends;
    CustomTextView txtInviteCode, txtInviteCodeShare;
    View view;
    String strReferralcode, strInvitecode, userId;
    SharedPreferences.Editor getUpdate;
    SharedPreferences spUpdate;
    AdView adViewFacebook;
    com.google.android.gms.ads.AdView adViewGoogle;
    LinearLayout adContainer;

    public InviteFriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_invite_friends, container, false);

        getUpdate = requireActivity().getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE).edit();
        spUpdate = requireActivity().getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE);
        userId = spUpdate.getString(Utils.SP_USERID, null);
        strReferralcode = spUpdate.getString(Utils.SP_USER_REFERAL_CODE, null);
        strInvitecode = spUpdate.getString(Utils.SP_USER_INVITE_CODE, null);
        System.out.println("Invitecode~~"+strInvitecode);

        imgbtnBack =(ImageButton)view.findViewById(R.id.imgbtn_back);
        btnInviteFriends =(CustomButton)view.findViewById(R.id.btn_invitefriends);
        txtInviteCodeShare =(CustomTextView) view.findViewById(R.id.txt_invitecode_share);
        txtInviteCode =(CustomTextView) view.findViewById(R.id.txt_invitecode);

        try {
            if (strInvitecode != null) {
                txtInviteCode.setText(strInvitecode);
            } else {
                txtInviteCode.setText("");
            }

            BaseActivity baseActivity = new BaseActivity();
            adContainer = view.findViewById(R.id.banner_container);
            baseActivity.callFacebookAdsCommon(requireActivity(), adViewFacebook, adContainer);

        } catch (Exception e) {
            e.printStackTrace();
        }

        OnClick();

        onBackButton(view);

        return view;
    }

    private void OnClick() {
        imgbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard();
                backButtonFunction();
            }
        });

        String message = getResources().getString(R.string.invite_friends_Hello_I_am_using) + " " + getResources().getString(R.string.invite_friends_its_an_amazing_app) + " " + strInvitecode.trim();

        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.instaguard.user"))
                .setQuote(message)
                .build();
        ShareButton shareButton = (ShareButton) view.findViewById(R.id.fb_share_button);
        shareButton.setShareContent(content);

        btnInviteFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    Intent smsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("smsto:" + ""));
                    smsIntent.putExtra("sms_body", message);
                    startActivity(smsIntent);

                } catch (Exception e1) {
                    e1.printStackTrace();
                    // Log.e("TAG", "Exception2: ~~~~~~"+e1.toString());
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.invite_friends_share_subject));
                    share.putExtra(Intent.EXTRA_TEXT, message);
                    startActivity(Intent.createChooser(share, "Share using"));
                }
            }
        });
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
        removeFragment(Utils.FRAGMENT_INVITEFRIENDS);
    }

    public void removeFragment(String Tag) {
        Utils.CURRENT_TAG = null;
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag(Tag)).commit();
    }


    public void hideKeyBoard() {
        // Check if no view has focus:
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onDestroy() {
        if (adViewFacebook != null) {
            adViewFacebook.destroy();
        }
        super.onDestroy();
        Log.e("TAG", "onDestroy: ~~~~~~~~~~~~~~~~~~~");
    }

    @Override
    public void onDestroyView() {
        if (adViewFacebook != null) {
            adViewFacebook.destroy();
        }
        super.onDestroyView();
        Log.e("TAG", "onDestroyView: ~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

}
