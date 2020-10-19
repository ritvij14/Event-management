package com.inner_wheel.event_management.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.inner_wheel.event_management.adapters.CompetitionRecyclerAdapter;
import com.inner_wheel.event_management.api.RetrofitClient;
import com.inner_wheel.event_management.api.models.Competition;
import com.inner_wheel.event_management.api.models.GetCompetitions;
import com.inner_wheel.event_management.databinding.FragmentHomeBinding;
import com.inner_wheel.event_management.models.CompetitionListItem;
import com.inner_wheel.event_management.utils.SharedPrefs;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    FragmentHomeBinding homeBinding;
    SharedPrefs sharedPrefs;

    public HomeFragment() {
        // Required empty public constructor
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        sharedPrefs = new SharedPrefs(Objects.requireNonNull(getContext()));

        homeBinding.homeGreetingText.setText("Hi " + sharedPrefs.getName());

        fetchCompetitions();
        return homeBinding.getRoot();
    }

    // populate Past competitions recycler view
    private void setPastCompetitions(ArrayList<CompetitionListItem> pastCompetitions) {
        homeBinding.pastEventsRv.setHasFixedSize(true);
        homeBinding.pastEventsRv.setLayoutManager(new LinearLayoutManager(
                getContext(),
                RecyclerView.VERTICAL,
                false
        ));
        CompetitionRecyclerAdapter pastCompetitionRvAdapter = new CompetitionRecyclerAdapter(
                pastCompetitions,
                "COMPLETED",
                getContext()
        );
        homeBinding.pastEventsRv.setAdapter(pastCompetitionRvAdapter);
        pastCompetitionRvAdapter.notifyDataSetChanged();
    }

    // populate upcoming competitions recycler view
    private void setUpcomingCompetitions(ArrayList<CompetitionListItem> upcomingCompetitions) {
        homeBinding.upcomingEventsRv.setHasFixedSize(true);
        homeBinding.upcomingEventsRv.setLayoutManager(new LinearLayoutManager(
                getContext(),
                RecyclerView.VERTICAL,
                false
        ));
        CompetitionRecyclerAdapter upcomingCompetitionRvAdapter = new CompetitionRecyclerAdapter(
                upcomingCompetitions,
                "UPCOMING",
                getContext()
        );
        homeBinding.upcomingEventsRv.setAdapter(upcomingCompetitionRvAdapter);
        upcomingCompetitionRvAdapter.notifyDataSetChanged();
    }

    // populate ongoing competitions recycler view
    private void setOngoingCompetitions(ArrayList<CompetitionListItem> ongoingCompetitions) {
        homeBinding.ongoingEventsRv.setHasFixedSize(true);
        homeBinding.ongoingEventsRv.setLayoutManager(new LinearLayoutManager(
                getContext(),
                RecyclerView.VERTICAL,
                false
        ));
        CompetitionRecyclerAdapter ongoingCompetitionRvAdapter = new CompetitionRecyclerAdapter(
                ongoingCompetitions,
                "ONGOING",
                getContext()
        );
        homeBinding.ongoingEventsRv.setAdapter(ongoingCompetitionRvAdapter);
        ongoingCompetitionRvAdapter.notifyDataSetChanged();
    }

    private void fetchCompetitions() {

        Call<GetCompetitions> call = RetrofitClient.getClient().getCompetitionList(sharedPrefs.getToken());
        call.enqueue(new Callback<GetCompetitions>() {
            @Override
            public void onResponse(@NotNull Call<GetCompetitions> call, @NotNull Response<GetCompetitions> response) {
                GetCompetitions getCompetitions = response.body();
                if (getCompetitions != null) {
                    if (getCompetitions.getSuccess()) {
                        List<Competition> competitionList = getCompetitions.getCompetitionsList();
                        if (competitionList != null) {

                            ArrayList<CompetitionListItem> upcomingCompetitions = new ArrayList<>();
                            ArrayList<CompetitionListItem> ongoingCompetitions = new ArrayList<>();
                            ArrayList<CompetitionListItem> pastCompetitions = new ArrayList<>();

                            for (Competition competition: competitionList) {
                                competition.setStartTime(competition.getStartTime().split("T", 2)[0]);

                                if (competition.getState().equals("UPCOMING")) {
                                    CompetitionListItem competitionListItem = new CompetitionListItem(
                                            competition.getTitle(),
                                            competition.getStartTime(),
                                            competition.getCategory(),
                                            competition.getCompetitionID()
                                    );
                                    upcomingCompetitions.add(competitionListItem);
                                }

                                if (competition.getState().equals("ONGOING")) {
                                    CompetitionListItem competitionListItem = new CompetitionListItem(
                                            competition.getTitle(),
                                            competition.getStartTime(),
                                            competition.getCategory(),
                                            competition.getCompetitionID()
                                    );
                                    ongoingCompetitions.add(competitionListItem);
                                }

                                if (competition.getState().equals("COMPLETED")) {
                                    CompetitionListItem competitionListItem = new CompetitionListItem(
                                            competition.getTitle(),
                                            competition.getStartTime(),
                                            competition.getCategory(),
                                            competition.getCompetitionID()
                                    );
                                    pastCompetitions.add(competitionListItem);
                                }
                            }

                            setUpcomingCompetitions(upcomingCompetitions);
                            setOngoingCompetitions(ongoingCompetitions);
                            setPastCompetitions(pastCompetitions);
                        } else {
                            Toast.makeText(getContext(), "Unable to load competitions at the moment", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    // show error message
                    Toast.makeText(getContext(), "Unable to load competitions", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<GetCompetitions> call, @NotNull Throwable t) {
                // show error message
                Toast.makeText(getContext(), "Unable to load competitions", Toast.LENGTH_SHORT).show();
            }
        });
    }
}