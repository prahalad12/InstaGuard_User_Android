package com.instaguard.user.Fragments;

import static android.content.Context.MODE_PRIVATE;

import static com.instaguard.user.Activities.MainActivity.uniqueIdList;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.instaguard.user.Adapter.UniqueIdAdapter;
import com.instaguard.user.Helpers.Retrofit.APIClient;
import com.instaguard.user.Helpers.Retrofit.APIInterface;
import com.instaguard.user.Helpers.UniqueIdCallback;
import com.instaguard.user.Models.UniqeIdResModel;
import com.instaguard.user.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuardUniqueIdFragment extends BottomSheetDialogFragment {
    View binding;
    Integer guardCount;
    AppCompatButton validateUniqueIdBtn, submit;
    RecyclerView rvGuard;
    AppCompatEditText uniqueId;
    UniqueIdAdapter adapter;
    View lyt_progressBar;
    SharedPreferences sharedPreferences;

    public GuardUniqueIdFragment(int guardCount) {
        this.guardCount = guardCount;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = inflater.inflate(R.layout.fragment_guard_unique_id, container, false);

        initViews();
        onClick();
        return binding;
    }

    private void initViews() {

        validateUniqueIdBtn = binding.findViewById(R.id.validateUniqueIdBtn);
        rvGuard = binding.findViewById(R.id.rvGuard);
        uniqueId = binding.findViewById(R.id.uniqueId);
        submit = binding.findViewById(R.id.submit);
        lyt_progressBar = binding.findViewById(R.id.lyt_progressBar);

        sharedPreferences = requireActivity().getSharedPreferences("shared preferences", MODE_PRIVATE);
    }

    private void onClick() {

        validateUniqueIdBtn.setOnClickListener(view -> {
            System.out.println("uniqueId Size and guard count " + uniqueIdList.size() + " ~~~ " + guardCount);

            if (uniqueId.getText() == null || uniqueId.getText().toString().isEmpty()) {
                showToast("Enter Guard's Unique Id");
            } else if (uniqueId.getText().toString().isEmpty()) {
                showToast("Enter Guard's Unique Id");
            } else if (uniqueIdList.size() + 1 > guardCount) {
                showToast("You Have Enter Maximum Number Of Guards");
            } else if (uniqueIdList.contains(uniqueId.getText().toString())) {
                showToast("You Have Already Entered this Guard's Unique Id");
            } else {
                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                validateCall(uniqueId.getText().toString());
            }
        });

        submit.setOnClickListener(view -> {

            SharedPreferences.Editor editor = sharedPreferences.edit();

            JSONArray jsonArray = new JSONArray(uniqueIdList);
            String jsonString = jsonArray.toString();
            System.out.println("uniqueIdList json "+ jsonString);

            editor.putString("uniqueIdList", jsonString);
            editor.apply();

            dismiss();
        });
    }

    private void validateCall(String string) {
        HashMap<String, String> map = new HashMap<>();
        map.put("reference_id", string);

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<UniqeIdResModel> call2 = apiInterface.verifyUniqueId(map);
        lyt_progressBar.setVisibility(View.VISIBLE);
        call2.enqueue(new Callback<UniqeIdResModel>() {
            @Override
            public void onResponse(@NonNull Call<UniqeIdResModel> call, @NonNull Response<UniqeIdResModel> response) {
                if (response.body() != null) {
                    lyt_progressBar.setVisibility(View.GONE);

                    if (response.body().getMessage().equals("success")) {
                        uniqueIdList.addAll(response.body().getResult());
                        uniqueId.setText("");

                        System.out.println("added value is " + uniqueIdList.size());

                        setUniqueIdAdapter();

//                        showToast(response.body().getMessage());
                    } else {
                        showToast(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UniqeIdResModel> call, @NonNull Throwable t) {
                lyt_progressBar.setVisibility(View.GONE);
                System.out.println("unique id validation error " + t.getMessage());
            }
        });
    }

    private void setUniqueIdAdapter() {

        if (!uniqueIdList.isEmpty()) {
            rvGuard.setVisibility(View.VISIBLE);
            submit.setVisibility(View.VISIBLE);

            LinearLayoutManager llm = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            rvGuard.setLayoutManager(llm);
            adapter = new UniqueIdAdapter(uniqueIdList, new UniqueIdCallback() {
                @Override
                public void onClick(Integer id) {
                    int dltAt = id;
                    uniqueIdList.remove(dltAt);
                    adapter.notifyDataSetChanged();
                }
            });

            rvGuard.setAdapter(adapter);
        } else {
            rvGuard.setVisibility(View.GONE);
            submit.setVisibility(View.GONE);
        }

    }

    private void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        String jsonString = sharedPreferences.getString("uniqueIdList", null);
        if (jsonString != null) {
            try {
                JSONArray jsonArray = new JSONArray(jsonString);
                uniqueIdList.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    uniqueIdList.add(jsonArray.getString(i));
                }

                setUniqueIdAdapter();
                System.out.println("get unique id list "+jsonArray);
            }catch (JSONException e) {
                showToast(e.getMessage());
            }
        } else if (!uniqueIdList.isEmpty()) {
            setUniqueIdAdapter();
        }
    }
}