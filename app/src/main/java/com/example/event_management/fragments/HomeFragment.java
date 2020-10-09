package com.example.event_management.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.event_management.adapters.CompetitionRecyclerAdapter;
import com.example.event_management.api.RetrofitClient;
import com.example.event_management.api.models.Competition;
import com.example.event_management.api.models.GetCompetitions;
import com.example.event_management.databinding.FragmentHomeBinding;
import com.example.event_management.models.CompetitionListItem;
import com.example.event_management.utils.SharedPrefs;

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

public class HomeFragment extends Fragment {

    FragmentHomeBinding homeBinding;
    SharedPrefs sharedPrefs;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        sharedPrefs = new SharedPrefs(Objects.requireNonNull(getContext()));

        fetchCompetitions();
        return homeBinding.getRoot();
    }

    private void setPastCompetitions(ArrayList<CompetitionListItem> pastCompetitions) {
        homeBinding.pastEventsRv.setHasFixedSize(true);
        homeBinding.pastEventsRv.setLayoutManager(new LinearLayoutManager(
                getContext(),
                RecyclerView.VERTICAL,
                false
        ));
        CompetitionRecyclerAdapter pastCompetitionRvAdapter = new CompetitionRecyclerAdapter(
                pastCompetitions,
                getContext()
        );
        homeBinding.pastEventsRv.setAdapter(pastCompetitionRvAdapter);
        pastCompetitionRvAdapter.notifyDataSetChanged();
    }

    private void setUpcomingCompetitions(ArrayList<CompetitionListItem> upcomingCompetitions) {
        homeBinding.upcomingEventsRv.setHasFixedSize(true);
        homeBinding.upcomingEventsRv.setLayoutManager(new LinearLayoutManager(
                getContext(),
                RecyclerView.VERTICAL,
                false
        ));
        CompetitionRecyclerAdapter upcomingCompetitionRvAdapter = new CompetitionRecyclerAdapter(
                upcomingCompetitions,
                getContext()
        );
        homeBinding.upcomingEventsRv.setAdapter(upcomingCompetitionRvAdapter);
        upcomingCompetitionRvAdapter.notifyDataSetChanged();
    }

    private void setRegisteredCompetitions(ArrayList<CompetitionListItem> registeredCompetitions) {
        homeBinding.registeredEventsRv.setHasFixedSize(true);
        homeBinding.registeredEventsRv.setLayoutManager(new LinearLayoutManager(
                getContext(),
                RecyclerView.VERTICAL,
                false
        ));
        CompetitionRecyclerAdapter registeredCompetitionRvAdapter = new CompetitionRecyclerAdapter(
                registeredCompetitions,
                getContext()
        );
        homeBinding.pastEventsRv.setAdapter(registeredCompetitionRvAdapter);
        registeredCompetitionRvAdapter.notifyDataSetChanged();
    }

    private void fetchCompetitions() {

        Call<GetCompetitions> call = RetrofitClient.getClient().getCompetitionList(sharedPrefs.getToken());
        call.enqueue(new Callback<GetCompetitions>() {
            @Override
            public void onResponse(Call<GetCompetitions> call, Response<GetCompetitions> response) {
                GetCompetitions getCompetitions = response.body();
                if (getCompetitions != null) {
                    if (getCompetitions.getSuccess()) {
                        List<Competition> competitionList = getCompetitions.getCompetitionsList();
                        if (competitionList != null) {

                            ArrayList<CompetitionListItem> upcomingCompetitions = new ArrayList<>();
                            ArrayList<CompetitionListItem> registeredCompetitions = new ArrayList<>();
                            ArrayList<CompetitionListItem> pastCompetitions = new ArrayList<>();

                            for (Competition competition: competitionList) {
                                competition.setStartTime(competition.getStartTime().split("T", 2)[0]);

                                if (competition.getState().equals("UPCOMING")) {
                                    CompetitionListItem competitionListItem = new CompetitionListItem(
                                            competition.getTitle(),
                                            competition.getStartTime(),
                                            competition.getCategory(),
                                            competition.getState()
                                    );
                                    upcomingCompetitions.add(competitionListItem);
                                }

                                if (competition.getState().equals("REGISTERED")) {
                                    CompetitionListItem competitionListItem = new CompetitionListItem(
                                            competition.getTitle(),
                                            competition.getStartTime(),
                                            competition.getCategory(),
                                            competition.getState()
                                    );
                                    registeredCompetitions.add(competitionListItem);
                                }

                                if (competition.getState().equals("PAST")) {
                                    CompetitionListItem competitionListItem = new CompetitionListItem(
                                            competition.getTitle(),
                                            competition.getStartTime(),
                                            competition.getCategory(),
                                            competition.getState()
                                    );
                                    pastCompetitions.add(competitionListItem);
                                }
                            }

                            setUpcomingCompetitions(upcomingCompetitions);
                            setRegisteredCompetitions(registeredCompetitions);
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
            public void onFailure(Call<GetCompetitions> call, Throwable t) {
                // show error message
                Toast.makeText(getContext(), "Unable to load competitions", Toast.LENGTH_SHORT).show();
            }
        });
    }
}