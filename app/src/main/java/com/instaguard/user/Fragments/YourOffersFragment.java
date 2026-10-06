package com.instaguard.user.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.text.util.Linkify;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.instaguard.user.CustomViews.CustomEditText;
import com.instaguard.user.CustomViews.CustomTextView;
import com.instaguard.user.Helpers.Utils;
import com.instaguard.user.Models.FirebaseModel;
import com.instaguard.user.R;

import java.util.ArrayList;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static com.instaguard.user.Activities.BaseActivity.hideProgressDialog;


public class YourOffersFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    View view;
    View lytProgressBar;

    SharedPreferences SP_Prefs;

    SharedPreferences.Editor putSpState;
    String userId;
    ImageButton imgbtnBack;
    SharedPreferences spCommon;
    String userAccessToken;
    RecyclerView rcvOffers;
    RelativeLayout rlEmptyview;

    public String TAG = "YourOffersFragment";

    public FirebaseRecyclerAdapter<FirebaseModel, MainCategoryViewHolder> firebaseRecyclerAdapter;

    private ArrayList<FirebaseModel> firebaseModel = new ArrayList<>();

    Query offersQuery;

    DatabaseReference offersDatabase;
    ValueEventListener offersValueEventListener;

    public YourOffersFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static YourOffersFragment newInstance(String param1, String param2) {
        YourOffersFragment fragment = new YourOffersFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint({"LongLogTag", "CommitPrefEdits"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_offers, container, false);

        SP_Prefs = Objects.requireNonNull(getActivity()).getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE);
        userId = SP_Prefs.getString(Utils.SP_USERID, null);
        Log.e("userId~~OnGoingJobsFragment~~", userId);
        spCommon = Objects.requireNonNull(getActivity()).getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE);
        putSpState = Objects.requireNonNull(getActivity()).getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE).edit();

        rcvOffers = (RecyclerView) view.findViewById(R.id.rv_offers);
        rlEmptyview = (RelativeLayout) view.findViewById(R.id.rl_emptyview);
        lytProgressBar = view.findViewById(R.id.lyt_progressBar);

       // rcvOffers.setLayoutManager(new LinearLayoutManager(getActivity()));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rcvOffers.setLayoutManager(layoutManager);

        callFirebaseStatusListoners();

        getDataFromFirebaseListoner();

        onBackButton(view);

        return view;
    }

    public void callFirebaseStatusListoners() {

        if (offersDatabase != null) {
            offersDatabase.removeEventListener(offersValueEventListener);
        }

        offersDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.FIREBASE_NOTIFICATIONS_HEADER).child(Utils.FIREBASE_OFFERS_HEADER)
                .child(Utils.FIREBASE_OFFER_USER_HEADER).orderByChild("status").equalTo("1").getRef();

        offersValueEventListener = offersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getChildrenCount() == 0) {
                    if (rcvOffers.getVisibility() == View.VISIBLE) {
                        rcvOffers.setVisibility(View.GONE);
                    }

                    if (rlEmptyview.getVisibility() == View.GONE) {
                        rlEmptyview.setVisibility(View.VISIBLE);
                    }

                } else if (dataSnapshot.getChildrenCount() > 0) {
                    if (rcvOffers.getVisibility() == View.GONE) {
                        rcvOffers.setVisibility(View.VISIBLE);
                    }

                    if (rlEmptyview.getVisibility() == View.VISIBLE) {
                        rlEmptyview.setVisibility(View.GONE);
                    }

                }/* else {
                    if (rcvOffers.getVisibility() == View.VISIBLE) {
                        rcvOffers.setVisibility(View.GONE);
                    }

                    if (rlEmptyview.getVisibility() == View.GONE) {
                        rlEmptyview.setVisibility(View.VISIBLE);
                    }
                }*/

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void getDataFromFirebaseListoner() {

        offersQuery = FirebaseDatabase.getInstance().getReference().child(Utils.FIREBASE_NOTIFICATIONS_HEADER).child(Utils.FIREBASE_OFFERS_HEADER)
                .child(Utils.FIREBASE_OFFER_USER_HEADER)
                .orderByChild("status").equalTo("1");

        offersQuery.keepSynced(true);
        FirebaseRecyclerOptions<FirebaseModel> options =
                new FirebaseRecyclerOptions.Builder<FirebaseModel>()
                        .setQuery(offersQuery, FirebaseModel.class)
                        .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FirebaseModel, MainCategoryViewHolder>(options) {
            @Override
            public MainCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.adapter_offers, parent, false);

                return new MainCategoryViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(MainCategoryViewHolder holder, int position, FirebaseModel model) {
                // Bind the image_details object to the MainCategoryViewHolder
                Log.e(TAG, "onBindViewHolder~~~~~~~~~~~~: " + model.getId());
                holder.setBlogPost(model);

            }
        };

        firebaseRecyclerAdapter.startListening();
        rcvOffers.setVisibility(View.VISIBLE);
        rcvOffers.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();

    }

    public class MainCategoryViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rlTopOfferLyt, llRideHistoryHeader;
        ImageView ivPaymentMethod;
        CustomTextView txtTitle, txtMessage,txtDate;

        MainCategoryViewHolder(View itemView) {
            super(itemView);
            rlTopOfferLyt = (RelativeLayout) itemView.findViewById(R.id.rl_top_offer_lyt);
            txtTitle = (CustomTextView) itemView.findViewById(R.id.txt_title);
            txtMessage = (CustomTextView) itemView.findViewById(R.id.txt_message);
            txtDate = (CustomTextView) itemView.findViewById(R.id.txt_date);
        }

        @SuppressLint("SetTextI18n")
        void setBlogPost(FirebaseModel categoryModel) {

            String Id = String.valueOf(categoryModel.getId());
            String title = String.valueOf(categoryModel.getTitle());
            String message = String.valueOf(categoryModel.getMessage());
            String status = String.valueOf(categoryModel.getStatus());
            String Date = String.valueOf(categoryModel.getDate());

            txtTitle.setText(title);

            txtMessage.setText(Html.fromHtml(message));
            Linkify.addLinks(txtMessage,Linkify.ALL);

            txtDate.setText(Date);

            rlTopOfferLyt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showDialog(title, message,Date);
                }
            });

        }

        private void showDialog(String title, String message, String Date) {

            Dialog dialogBuilder = new Dialog(getActivity(), R.style.AppTheme);
            dialogBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
            dialogBuilder.setContentView(R.layout.dialog_offer_annoc_view);
            dialogBuilder.setCancelable(true);
            dialogBuilder.getWindow().setWindowAnimations(R.style.DialogAnimationFadeINOUT);

            CustomTextView txtHeader = (CustomTextView) dialogBuilder.findViewById(R.id.textView);
            CustomTextView txtTitle = (CustomTextView) dialogBuilder.findViewById(R.id.txt_title);
            CustomTextView txtMessage = (CustomTextView) dialogBuilder.findViewById(R.id.txt_message);
            CustomTextView txtDate = (CustomTextView) dialogBuilder.findViewById(R.id.txt_date);
            ImageView ivCancel = (ImageView) dialogBuilder.findViewById(R.id.imgbtn_back);

            txtHeader.setText(getResources().getString(R.string.yourjobs_offer));
            txtTitle.setText(title);

            txtMessage.setText(Html.fromHtml(message));
            Linkify.addLinks(txtMessage,Linkify.ALL);

            txtDate.setText(Date);

            ivCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogBuilder.dismiss();
                }
            });

            dialogBuilder.show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (firebaseRecyclerAdapter != null) {
            firebaseRecyclerAdapter.stopListening();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (firebaseRecyclerAdapter != null) {
            firebaseRecyclerAdapter.stopListening();
        }
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
        // MainActivity.frameClickable(false);
        removeFragment(Utils.FRAGMENT_YOUR_OFFERS_ANNOCEMENTS);
    }

    public void removeFragment(String Tag) {
        Utils.CURRENT_TAG = null;
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag(Tag)).commit();
    }
}
