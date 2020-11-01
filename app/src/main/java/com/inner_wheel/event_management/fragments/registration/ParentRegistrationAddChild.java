package com.inner_wheel.event_management.fragments.registration;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.inner_wheel.event_management.R;
import com.inner_wheel.event_management.activities.MainActivity;
import com.inner_wheel.event_management.adapters.AddChildRecyclerAdapter;
import com.inner_wheel.event_management.api.RetrofitClient;
import com.inner_wheel.event_management.api.models.Participant;
import com.inner_wheel.event_management.api.models.ParticipantsResponse;
import com.inner_wheel.event_management.databinding.FragmentParentRegistrationAddChildBinding;
import com.inner_wheel.event_management.models.AddChildListItem;
import com.inner_wheel.event_management.utils.SharedPrefs;

import net.steamcrafted.loadtoast.LoadToast;

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
    ParentLoginFragment loginFragment;
    LoadToast lt;
    AlertDialog dialog;
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
        loginFragment = new ParentLoginFragment();
        lt = new LoadToast(getContext());
        lt.setText("Loading...");
        lt.setTranslationY(100).setBorderColor(getResources().getColor(R.color.color_accent));
        childListItems = new ArrayList<>();
        childBinding.childList.setHasFixedSize(true);
        childBinding.childList.setLayoutManager(new LinearLayoutManager(getContext(),
            RecyclerView.VERTICAL, false));
        lt.show();
        fetchChildren();
        childRecyclerAdapter = new AddChildRecyclerAdapter(childListItems, getContext());
        childBinding.childList.setAdapter(childRecyclerAdapter);
        childRecyclerAdapter.notifyDataSetChanged();

        buildAddChildDialog();

        childBinding.addChildButton.setOnClickListener(view -> dialog.show());

        childBinding.skipChildButton.setOnClickListener(v -> {
            sharedPrefs.setIsJustRegistered(false);
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).finish();
        });
        return childBinding.getRoot();
    }

    private void buildAddChildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater li = LayoutInflater.from(getContext());
        View addChildDialogView = li.inflate(R.layout.add_child_dialog, null);
        builder.setView(addChildDialogView);
        dialog = builder.create();

        final TextInputEditText childName = addChildDialogView.findViewById(R.id.child_name_field);
        final TextInputEditText childAge = addChildDialogView.findViewById(R.id.child_age_field);
        final TextInputEditText childSchool = addChildDialogView.findViewById(R.id.child_school_field);
        final MaterialButton addChildBtn = addChildDialogView.findViewById(R.id.add_child_info_button);

        addChildBtn.setOnClickListener(view -> {
            String name = childName.getText().toString();
            String age = childAge.getText().toString();
            String school = childSchool.getText().toString();

            if (checkFields(name, age, school)) {
                registerChildParticipant(name, age, school);
            }
        });
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
                lt.success();
                lt.hide();
                childBinding.skipChildButton.setText(R.string.next);
            }

            @Override
            public void onFailure(@NotNull Call<Object> call, @NotNull Throwable t) {
                // error
                lt.error();
                lt.hide();
                Toast.makeText(getContext(), "Error. Please check connection and try again.", Toast.LENGTH_SHORT).show();
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
                        if (participants.size() > 0) {
                            childBinding.skipChildButton.setText(R.string.next);
                        }
                        childListItems.clear();
                        for (Participant p: participants) {
                            childListItems.add(new AddChildListItem(
                                    p.getName(),
                                    p.getAge(),
                                    p.getSchoolName(),
                                    p.getId()
                            ));
                            childRecyclerAdapter.notifyDataSetChanged();
                            lt.success();
                            lt.hide();
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "No children registered yet", Toast.LENGTH_SHORT).show();
                    lt.hide();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ParticipantsResponse> call, @NotNull Throwable t) {
                Toast.makeText(getContext(), "Error!", Toast.LENGTH_SHORT).show();
                lt.error();
                lt.hide();
            }
        });
    }
}