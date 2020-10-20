package com.inner_wheel.event_management.fragments.competition;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.inner_wheel.event_management.R;
import com.inner_wheel.event_management.adapters.SubmissionRecyclerAdapter;
import com.inner_wheel.event_management.api.RetrofitClient;
import com.inner_wheel.event_management.api.models.AgeGroup;
import com.inner_wheel.event_management.api.models.RegisteredParticipants;
import com.inner_wheel.event_management.api.models.SelectCompetition;
import com.inner_wheel.event_management.databinding.FragmentCompetitionSubmissionBinding;
import com.inner_wheel.event_management.models.AddChildListItem;
import com.inner_wheel.event_management.models.RegisteredListItem;
import com.inner_wheel.event_management.utils.SharedPrefs;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompetitionSubmissionFragment extends Fragment {

    FragmentCompetitionSubmissionBinding submissionBinding;
    SharedPrefs sharedPrefs;
    private String firstPrize = "First prize is Rs ";
    private String secondPrize = "Second prize is Rs ";
    private String thirdPrize = "Third prize is Rs ";
    private String id, date, startTime, endTime;
    ArrayList<RegisteredListItem> list;
    SubmissionRecyclerAdapter submissionRecyclerAdapter;
    public CompetitionSubmissionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        submissionBinding = FragmentCompetitionSubmissionBinding
                .inflate(inflater, container, false);
        Bundle b = Objects.requireNonNull(getActivity()).getIntent().getExtras();
        id = Objects.requireNonNull(b).getString("ID");
        assert id != null;
        Log.d("INTRO FRAGMENT", id);
        sharedPrefs = new SharedPrefs(Objects.requireNonNull(getContext()));
        list = new ArrayList<>();

        // initialise recyclerview
        submissionBinding.registeredParticipantRv.setHasFixedSize(true);
        submissionBinding.registeredParticipantRv.setLayoutManager(new LinearLayoutManager(
                getContext(),
                RecyclerView.VERTICAL,
                false
        ));

        // function calls to get data for different components
        fetchCompetitionData();
        fetchRegisteredParticipants();

        // setting data in recyclerview
        submissionRecyclerAdapter = new SubmissionRecyclerAdapter(list, getContext());
        submissionBinding.registeredParticipantRv.setAdapter(submissionRecyclerAdapter);
        submissionRecyclerAdapter.notifyDataSetChanged();

        return submissionBinding.getRoot();
    }

    private void fetchCompetitionData() {

        Call<SelectCompetition> call = RetrofitClient.getClient().getSelectedCompetition(sharedPrefs.getToken(), id);
        call.enqueue(new Callback<SelectCompetition>() {
            @Override
            public void onResponse(@NotNull Call<SelectCompetition> call, @NotNull Response<SelectCompetition> response) {
                SelectCompetition competition = response.body();

                // formatting date and time
                SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
                SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
                dateTimeFormat.setTimeZone(TimeZone.getTimeZone("IST"));
                Date startDate = null, endDate = null;
                try {
                    startDate = dateTimeFormat.parse(competition.getCompetition().getStart());
                    endDate = dateTimeFormat.parse(competition.getCompetition().getEnd());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                date = dateFormat.format(Objects.requireNonNull(startDate));
                startTime = timeFormat.format(startDate);
                endTime = timeFormat.format(Objects.requireNonNull(endDate));

                // processing data for prize body
                firstPrize += competition.getCompetition().getRewards().get(0);
                secondPrize += competition.getCompetition().getRewards().get(1);
                thirdPrize += competition.getCompetition().getRewards().get(2);
                // setting data for competition details
                submissionBinding.competitionHeader.competitionName
                        .setText(competition.getCompetition().getTitle());
                submissionBinding.competitionHeader.category
                        .setText(competition.getCompetition().getCategory());
                submissionBinding.competitionHeader.date.setText(date);
                submissionBinding.competitionHeader.startTime.setText(startTime);
                submissionBinding.competitionHeader.endTime.setText(endTime);
                // setting data for age group topics
                submissionBinding.groupOneTopic.setText(competition.getCompetition().getAgeGroups().get(0).getName());
                submissionBinding.groupTwoTopic.setText(competition.getCompetition().getAgeGroups().get(1).getName());
                submissionBinding.groupThreeTopic.setText(competition.getCompetition().getAgeGroups().get(2).getName());
                //setting data for prize body
                submissionBinding.prizeListBody.firstPrizeText.setText(firstPrize);
                submissionBinding.prizeListBody.secondPrizeText.setText(secondPrize);
                submissionBinding.prizeListBody.thirdPrizeText.setText(thirdPrize);
            }

            @Override
            public void onFailure(@NotNull Call<SelectCompetition> call, @NotNull Throwable t) {
                Log.d("SELECTED COMPETITION", "failed");
                Log.d("SELECTED COMPETITION", sharedPrefs.getToken());
            }
        });
    }

    private void fetchRegisteredParticipants() {
        Call<RegisteredParticipants> call = RetrofitClient.getClient().getRegisteredParticipants(sharedPrefs.getToken(), id);
        call.enqueue(new Callback<RegisteredParticipants>() {
            @Override
            public void onResponse(Call<RegisteredParticipants> call, Response<RegisteredParticipants> response) {
                RegisteredParticipants res = response.body();
                if (res != null) {
                    if (res.isSuccess()) {
                        for (RegisteredParticipants.RegisteredParticipant r:
                                res.getRegisteredParticipantList()) {
                            list.add(new RegisteredListItem(
                                    r.getParticipantID(),
                                    r.getName(),
                                    r.getAge(),
                                    r.getSchool(),
                                    r.getAgeGroup().getName(),
                                    r.getAgeGroup().getGrpId(),
                                    r.isSubmitted()
                            ));
                            submissionRecyclerAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "No participants registered by you for this event.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisteredParticipants> call, Throwable t) {
                Toast.makeText(getContext(), "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}