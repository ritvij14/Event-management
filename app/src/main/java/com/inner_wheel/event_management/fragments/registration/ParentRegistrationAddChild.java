package com.inner_wheel.event_management.fragments.registration;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.inner_wheel.event_management.adapters.AddChildRecyclerAdapter;
import com.inner_wheel.event_management.api.RetrofitClient;
import com.inner_wheel.event_management.api.models.Participant;
import com.inner_wheel.event_management.api.models.ParticipantsResponse;
import com.inner_wheel.event_management.databinding.FragmentParentRegistrationAddChildBinding;
import com.inner_wheel.event_management.models.AddChildListItem;
import com.inner_wheel.event_management.utils.SharedPrefs;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParentRegistrationAddChild extends Fragment {

    FragmentParentRegistrationAddChildBinding childBinding;
    AddChildRecyclerAdapter childRecyclerAdapter;
    ArrayList<AddChildListItem> childListItems;
    SharedPrefs sharedPrefs;
    public ParentRegistrationAddChild() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        childBinding = FragmentParentRegistrationAddChildBinding
            .inflate(inflater, container, false);
        sharedPrefs = new SharedPrefs(Objects.requireNonNull(getContext()));
        childListItems = new ArrayList<>();
        childBinding.childList.setHasFixedSize(true);
        childBinding.childList.setLayoutManager(new LinearLayoutManager(getContext(),
            RecyclerView.VERTICAL, false));
        fetchChildren();
        childRecyclerAdapter = new AddChildRecyclerAdapter(childListItems, getContext());
        childBinding.childList.setAdapter(childRecyclerAdapter);
        childRecyclerAdapter.notifyDataSetChanged();

        childBinding.addChildButton.setOnClickListener(view -> {
            childBinding.addChildInfoCard.setVisibility(View.VISIBLE);
            childBinding.skipChildButton.setVisibility(View.GONE);
        });

        childBinding.addChildInfoButton.setOnClickListener(view -> {
            String name = Objects.requireNonNull(childBinding.childNameField.getText())
                    .toString();
            String age = Objects.requireNonNull(childBinding.childAgeField.getText())
                    .toString();
            String school = Objects.requireNonNull(childBinding.childSchoolField.getText())
                    .toString();

            if (checkFields(name, age, school)) {

                childBinding.addChildInfoCard.setVisibility(View.GONE);
                childBinding.skipChildButton.setVisibility(View.VISIBLE);

                registerChildParticipant(name, age, school);
            }
        });
        return childBinding.getRoot();
    }

    private boolean checkFields(String name, String age, String school) {

        if (name.matches("")) {
            Toast.makeText(getContext(), "Add name of your child", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (age.matches("")) {
            Toast.makeText(getContext(), "Add age of your child", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (school.matches("")) {
            Toast.makeText(getContext(), "Add school of your child", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void registerChildParticipant(String name, String age, String school) {
        Call<Object> call = RetrofitClient.getClient().addParticipant(sharedPrefs.getToken(), name, age, school);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NotNull Call<Object> call, @NotNull Response<Object> response) {
                fetchChildren();
                Toast.makeText(getContext(), "Your child has been registered", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NotNull Call<Object> call, @NotNull Throwable t) {
                // error
                Toast.makeText(getContext(), "Error registering child", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchChildren() {
        Call<ParticipantsResponse> call = RetrofitClient.getClient().getParticipants(sharedPrefs.getToken());
        call.enqueue(new Callback<ParticipantsResponse>() {
            @Override
            public void onResponse(@NotNull Call<ParticipantsResponse> call, @NotNull Response<ParticipantsResponse> response) {
                ParticipantsResponse res = response.body();
                if (res != null) {
                    if (res.isSuccess()) {
                        List<Participant> participants = res.getParticipants();
                        for (Participant p: participants) {
                            childListItems.add(new AddChildListItem(
                                    p.getName(),
                                    p.getAge(),
                                    p.getSchoolName()
                            ));
                            childRecyclerAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ParticipantsResponse> call, @NotNull Throwable t) {

            }
        });
    }
}