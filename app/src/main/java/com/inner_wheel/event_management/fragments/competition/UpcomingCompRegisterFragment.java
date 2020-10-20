package com.inner_wheel.event_management.fragments.competition;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.inner_wheel.event_management.adapters.SavedParticipantRecyclerAdapter;
import com.inner_wheel.event_management.api.RetrofitClient;
import com.inner_wheel.event_management.api.models.Participant;
import com.inner_wheel.event_management.api.models.ParticipantsResponse;
import com.inner_wheel.event_management.databinding.FragmentUpcomingCompRegisterBinding;
import com.inner_wheel.event_management.models.AddChildListItem;
import com.inner_wheel.event_management.utils.SharedPrefs;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpcomingCompRegisterFragment extends Fragment {

    FragmentUpcomingCompRegisterBinding registerBinding;
    SharedPrefs sharedPrefs;
    ArrayList<AddChildListItem> childListItems;
    SavedParticipantRecyclerAdapter participantRecyclerAdapter;
    String name, topic, date, startTime, endTime;
    public UpcomingCompRegisterFragment(String name, String topic, String date, String startTime, String endTime) {
        this.name = name;
        this.topic = topic;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        registerBinding = FragmentUpcomingCompRegisterBinding.inflate(inflater, container, false);
        sharedPrefs = new SharedPrefs(Objects.requireNonNull(getContext()));
        childListItems = new ArrayList<>();

        registerBinding.savedParticipantsRv.setHasFixedSize(true);
        registerBinding.savedParticipantsRv.setLayoutManager(new LinearLayoutManager(
                getContext(),
                RecyclerView.VERTICAL,
                false
        ));
        fetchParticipants();
        participantRecyclerAdapter = new SavedParticipantRecyclerAdapter(childListItems, getContext());
        registerBinding.savedParticipantsRv.setAdapter(participantRecyclerAdapter);
        participantRecyclerAdapter.notifyDataSetChanged();

        registerBinding.competitionHeader.competitionName.setText(name);
        registerBinding.competitionHeader.category.setText(topic);
        registerBinding.competitionHeader.date.setText(date);
        registerBinding.competitionHeader.startTime.setText(startTime);
        registerBinding.competitionHeader.endTime.setText(endTime);

        registerBinding.addChildInfoButton.setOnClickListener(v -> {
            String name = Objects.requireNonNull(registerBinding.participantNameField.getText()).toString();
            String age = Objects.requireNonNull(registerBinding.participantAgeField.getText()).toString();
            String school = Objects.requireNonNull(registerBinding.participantSchoolField.getText()).toString();
            saveParticipant(name, age, school);
        });

        return registerBinding.getRoot();
    }

    private void saveParticipant(String name, String age, String school) {
        if (name.matches("") || age.matches("") || school.matches("")) {
            Toast.makeText(getContext(), "Please add complete info", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<Object> call = RetrofitClient.getClient().addParticipant(sharedPrefs.getToken(), name, age, school);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NotNull Call<Object> call, @NotNull Response<Object> response) {
                fetchParticipants();
                childListItems.clear();
                Toast.makeText(getContext(), "Your child has been registered", Toast.LENGTH_SHORT).show();
                registerBinding.participantNameField.setText("");
                registerBinding.participantAgeField.setText("");
                registerBinding.participantSchoolField.setText("");
            }

            @Override
            public void onFailure(@NotNull Call<Object> call, @NotNull Throwable t) {
                // error
                Toast.makeText(getContext(), "Error registering child", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchParticipants() {
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
                                    p.getSchoolName(),
                                    p.getId()
                            ));
                            participantRecyclerAdapter.notifyDataSetChanged();
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