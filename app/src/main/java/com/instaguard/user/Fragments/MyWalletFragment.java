package com.instaguard.user.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.widget.Toolbar;
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
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.instaguard.user.Activities.MainActivity;
import com.instaguard.user.CustomViews.CustomButton;
import com.instaguard.user.CustomViews.CustomEditText;
import com.instaguard.user.CustomViews.CustomTextView;
import com.instaguard.user.Helpers.DecimalUtils;
import com.instaguard.user.Helpers.Utils;
import com.instaguard.user.R;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;


public class MyWalletFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public String TAG = "MyWalletFragment";
    View view;
    Toolbar toolbarSignin;
    ImageButton imgbtnBack;
    CustomEditText edtRechargeAmt;
    CustomButton btnViewTransaction, btnAddMoney;
    CustomTextView txtYourBalance, txtWalletAmount, txtAddMoney, txtAddMoneyTag, txtAddmoneyOne, txtAddmoneyTwo, txtAddmoneyThree, txtPolicyline, txtTermsPolicy;
    SharedPreferences.Editor putUpdateWallet;
    SharedPreferences getUpdateWallet, getspCurrency;
    String userId, strCurrencySymbol, strWalletAmt, strWalletBalAmt, strWalletRechargeAmt;
    DatabaseReference customerWalletDatabaseBRef;
    ValueEventListener customerWalletValueEventListener;
    String deviceId;
    DatabaseReference currencyDatabaseBRef;
    ValueEventListener currencyValueEventListener;
    String strUsdValue;
    private AwesomeValidation Validation;

    public MyWalletFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MyWalletFragment newInstance(String param1, String param2) {
        MyWalletFragment fragment = new MyWalletFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            strWalletRechargeAmt = getArguments().getString(Utils.B_WALLET_RECHARGE_AMOUNT);
            String strWalletPage = getArguments().getString(Utils.B_WALLET_PAGE);
            Log.e(TAG, "Recharge Amount & page: " + strWalletRechargeAmt + " " + strWalletPage);
        }
    }

    @SuppressLint({"CommitPrefEdits", "SetTextI18n"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_wallet, container, false);

        putUpdateWallet = Objects.requireNonNull(getActivity()).getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE).edit();
        getUpdateWallet = getActivity().getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE);
        getspCurrency = getActivity().getSharedPreferences(Utils.PREFS_NAME_CURRENCY, MODE_PRIVATE);
        userId = getUpdateWallet.getString(Utils.SP_USERID, null);
        strWalletAmt = getUpdateWallet.getString(Utils.SP_USER_WALLET_AMT, null);

        strCurrencySymbol = getspCurrency.getString(Utils.SP_CURRENCY_SYMBOL, null);

        Log.e(TAG, "strWalletAmt~~ " + strWalletAmt);

        toolbarSignin = (Toolbar) view.findViewById(R.id.toolbar_wallet);
        imgbtnBack = (ImageButton) view.findViewById(R.id.imgbtn_back);


        txtYourBalance = (CustomTextView) view.findViewById(R.id.txt_yourbalance);
        txtWalletAmount = (CustomTextView) view.findViewById(R.id.txt_walletamount);
        txtAddMoney = (CustomTextView) view.findViewById(R.id.txt_addmoney);
        txtAddMoneyTag = (CustomTextView) view.findViewById(R.id.txt_addmoney_tag);

        txtAddmoneyOne = (CustomTextView) view.findViewById(R.id.txt_addmoney_one);
        txtAddmoneyTwo = (CustomTextView) view.findViewById(R.id.txt_addmoney_two);
        txtAddmoneyThree = (CustomTextView) view.findViewById(R.id.txt_addmoney_three);

        txtPolicyline = (CustomTextView) view.findViewById(R.id.txt_policy);
        txtTermsPolicy = (CustomTextView) view.findViewById(R.id.txt_terms);

        edtRechargeAmt = (CustomEditText) view.findViewById(R.id.edt_recharge_amt);

        btnViewTransaction = (CustomButton) view.findViewById(R.id.btn_viewtransactions);
        btnAddMoney = (CustomButton) view.findViewById(R.id.btn_addmoney);

        view.findViewById(R.id.rl_parent_lyt_wallet).setOnClickListener(view1 -> {});

        // validation initalize
        Validation = new AwesomeValidation(BASIC);
        txtPolicyline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.switchContent(R.id.frame_mainactivity,
                        Utils.FRAGMENT_SUPPORT,
                        Objects.requireNonNull(getActivity()), null,
                        Utils.AnimationType.SLIDE_IN_SLIDE_OUT);
            }
        });

        btnViewTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Bundle args = new Bundle();
                //  args.putString(Utils.B_WALLET_RECHARGE_AMOUNT, edtRechargeAmount);
                //  args.putString(Utils.B_WALLET_PAGE, "wallet");
                Utils.switchContent(R.id.frame_mainactivity,
                        Utils.FRAGMENT_YOUR_WALLET_TRANSACTION,
                        Objects.requireNonNull(getActivity()), null,
                        Utils.AnimationType.SLIDE_IN_SLIDE_OUT);
            }
        });


        imgbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard();
                backButtonFunction();
            }
        });


        txtAddmoneyOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtRechargeAmt.setText("300");

                /*txtAddmoneyOne.setTextColor(getContext().getResources().getColor(R.color.black));
                txtAddmoneyTwo.setTextColor(getContext().getResources().getColor(R.color.white));
                txtAddmoneyThree.setTextColor(getContext().getResources().getColor(R.color.white));*/
                txtAddmoneyOne.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_wallet_border_color));
                txtAddmoneyTwo.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_wallet_border));
                txtAddmoneyThree.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_wallet_border));
            }
        });
        txtAddmoneyTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtRechargeAmt.setText("500");
          /*      txtAddmoneyOne.setTextColor(getContext().getResources().getColor(R.color.white));
                txtAddmoneyTwo.setTextColor(getContext().getResources().getColor(R.color.black));
                txtAddmoneyThree.setTextColor(getContext().getResources().getColor(R.color.white));*/
                txtAddmoneyOne.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_wallet_border));
                txtAddmoneyTwo.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_wallet_border_color));
                txtAddmoneyThree.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_wallet_border));
            }
        });
        txtAddmoneyThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtRechargeAmt.setText("1000");
                /*txtAddmoneyOne.setTextColor(getContext().getResources().getColor(R.color.white));
                txtAddmoneyTwo.setTextColor(getContext().getResources().getColor(R.color.white));
                txtAddmoneyThree.setTextColor(getContext().getResources().getColor(R.color.black));*/
                txtAddmoneyOne.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_wallet_border));
                txtAddmoneyTwo.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_wallet_border));
                txtAddmoneyThree.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_wallet_border_color));
            }
        });

        btnAddMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String edtRechargeAmount = edtRechargeAmt.getText().toString().trim();

                if (edtRechargeAmount.length() == 0) {
                    edtRechargeAmt.setError(getString(R.string.error_wallet_enter_rechrge_amount));
                } else {

                    //600 > 500
                   // Log.e(TAG, "Balance differernce: " + convertCurrencyPrice(strWalletBalAmt) + "~~" + convertCurrencyDividePrice(edtRechargeAmount));

                        hideKeyBoard();

                        if (strCurrencySymbol != null) {
                            if (strCurrencySymbol.equals("$")) {

                                Log.i(TAG, "onClick: in the edtRechargeAmount-->" + edtRechargeAmount);
                                Log.i(TAG, "onClick: in the btnAddmoney-->" + convertCurrencyDividePrice(edtRechargeAmount));

                                double value = Double.parseDouble(edtRechargeAmount) / Double.parseDouble(strUsdValue);
                                double roundedNumber = DecimalUtils.round(value, 2);

                                System.out.println("onClick: ~~~~~~~~~~~~" + String.valueOf(roundedNumber));

                                if (Double.parseDouble(strWalletBalAmt) >= Double.parseDouble(edtRechargeAmount)) {
                                    Toast.makeText(getActivity(), getResources().getString(R.string.please_greater_than_wallet_bal) + " " +
                                            strWalletBalAmt, Toast.LENGTH_LONG).show();
                                } else {
                                  //  Log.e(TAG, "onClick: ~~~If~~~1111111");
                                    //95>0.50
                                    if (roundedNumber > 0.50) {
                                       // Log.e(TAG, "onClick: ~~~If~~~");

                                       // MainActivity.frameClickable(true);
                                        Bundle args = new Bundle();
                                        args.putString(Utils.B_WALLET_RECHARGE_AMOUNT, convertCurrencyDividePrice(edtRechargeAmount));
                                        args.putString(Utils.B_WALLET_PAGE, "wallet");
                                        Utils.switchContent(R.id.frame_mainactivity,
                                                Utils.FRAGMENT_STRIPE_PAYMENT,
                                                Objects.requireNonNull(getActivity()), args,
                                                Utils.AnimationType.SLIDE_IN_SLIDE_OUT);

                                    } else {



                                        Toast.makeText(getActivity(), getResources().getString(R.string.amt_must_greater_than), Toast.LENGTH_SHORT).show();
                                       // Log.e(TAG, "onClick: ~~~Else~~~");
                                    }
                                }
                            } else {

                                Log.i(TAG, "onClick: in the edtRechargeAmount-->" + edtRechargeAmount);
                                Log.i(TAG, "onClick: in the btnAddmoney-->" + convertCurrencyDividePrice(edtRechargeAmount));

                                double value = Double.parseDouble(convertCurrencyDividePrice(edtRechargeAmount)) * Double.parseDouble(strUsdValue);
                                double roundedNumber = DecimalUtils.round(value, 2);

                              //  System.out.println("onClick: ~~~~~~~~~~~~" + String.valueOf(roundedNumber));

                                if (Double.parseDouble(convertCurrencyPrice(strWalletBalAmt)) >=
                                        Double.parseDouble(convertCurrencyDividePrice(edtRechargeAmount))) {
                                    Toast.makeText(getActivity(), getResources().getString(R.string.please_greater_than_wallet_bal) + " " +
                                            convertCurrencyPrice(strWalletBalAmt), Toast.LENGTH_LONG).show();
                                } else {

                                  //  Log.e(TAG, "onClick: ~~~If~~~122211");

                                    System.out.println("amount-->"+roundedNumber);
                                    System.out.println("edtTextconvertamount-->"+ Double.parseDouble(convertCurrencyDividePrice(edtRechargeAmount)));
                                    System.out.println("edtTextamount-->"+ Double.parseDouble(edtRechargeAmount));
                                    System.out.println("basevalue-->"+ Double.parseDouble(strUsdValue));

                                    if (roundedNumber > 0.50) {
                                      //  Log.e(TAG, "onClick: ~~~If~~~");

                                       // MainActivity.frameClickable(true);
                                        Bundle args = new Bundle();
                                        args.putString(Utils.B_WALLET_RECHARGE_AMOUNT, convertCurrencyDividePrice(edtRechargeAmount));
                                        args.putString(Utils.B_WALLET_PAGE, "wallet");
                                        Utils.switchContent(R.id.frame_mainactivity,
                                                Utils.FRAGMENT_STRIPE_PAYMENT,
                                                Objects.requireNonNull(getActivity()), args,
                                                Utils.AnimationType.SLIDE_IN_SLIDE_OUT);
                                    } else {


                                        Toast.makeText(getActivity(), getResources().getString(R.string.amt_must_greater_than), Toast.LENGTH_SHORT).show();
                                      //  Log.e(TAG, "onClick: ~~~Else~~~");
                                    }
                                }
                            }

                    }
                }
            }
        });

        getCustomerWalletAmount();

        callUSDCurrencyValue();

        onBackButton(view);

        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    /*******************************Get Firebase States********************************************/

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
        hideKeyBoard();
       // MainActivity.frameClickable(false);
        removeFragment(Utils.FRAGMENT_MYWALLET);
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

    //get wallet amount in firebase
    private void getCustomerWalletAmount() {
        if (customerWalletDatabaseBRef != null) {
            customerWalletDatabaseBRef.removeEventListener(customerWalletValueEventListener);
        }
        customerWalletDatabaseBRef = FirebaseDatabase.getInstance().getReference().child(Utils.FIREBASE_USER_HEADER).child(userId);
        customerWalletValueEventListener = customerWalletDatabaseBRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Object walletAmountObj = dataSnapshot.child("wallet").getValue();
                    Object walletBalAmountObj = dataSnapshot.child("balance_amount").getValue();

                    if (walletAmountObj != null) {
                        strWalletAmt = walletAmountObj.toString();
                    } else {
                        strWalletAmt = "0";
                    }

                    if (walletBalAmountObj != null) {
                        strWalletBalAmt = walletBalAmountObj.toString();
                    } else {
                        strWalletBalAmt = "0";
                    }
                    Log.i(TAG, "onDataChange in walletAmount: -->" + strWalletAmt);

                    putUpdateWallet.putString(Utils.SP_USER_WALLET_AMT, strWalletAmt);
                    putUpdateWallet.apply();

                    if (strWalletAmt != null) {
                        txtWalletAmount.setText(strCurrencySymbol + " " + convertCurrencyPrice(strWalletAmt));
                    } else {
                        txtWalletAmount.setText(strCurrencySymbol + "0");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public String convertCurrencyPrice(String price) {
        String currencyCommonPrice = getspCurrency.getString(Utils.SP_CURRENCY_COMMON_VALUES, "1");
        Double multiplePrice = Double.parseDouble(currencyCommonPrice) * Double.parseDouble(price);
        double roundedNumber = DecimalUtils.round(multiplePrice, 2);
        return String.valueOf(roundedNumber);
    }

    public String convertCurrencyDividePrice(String price) {
        String currencyCommonPrice = getspCurrency.getString(Utils.SP_CURRENCY_COMMON_VALUES, "1");
        Double multiplePrice = Double.parseDouble(price) / Double.parseDouble(currencyCommonPrice);
        double roundedNumber = DecimalUtils.round(multiplePrice, 2);
        return String.valueOf(roundedNumber);
    }

    private void callUSDCurrencyValue() {

        if (currencyDatabaseBRef != null) {
            currencyDatabaseBRef.removeEventListener(currencyValueEventListener);
        }
        currencyDatabaseBRef = FirebaseDatabase.getInstance().getReference();
        currencyValueEventListener = currencyDatabaseBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("currency").getValue() != null) {
                    if (dataSnapshot.child("currency").child("USD").getValue() != null) {
                        strUsdValue = dataSnapshot.child("currency").child("USD").getValue().toString();
                    } else if (dataSnapshot.child("currency").child("usd").getValue() != null) {
                        strUsdValue = dataSnapshot.child("currency").child("usd").getValue().toString();
                    } else {
                        strUsdValue = "1";
                    }
                } else {
                    strUsdValue = "1";
                }

                System.out.println("onDataChange: ~~~~~~~USD currency~~~~" + strUsdValue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("", "onCancelled: error occur " + databaseError);
                strUsdValue = "1";
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
