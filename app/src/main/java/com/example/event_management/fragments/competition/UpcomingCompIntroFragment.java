package com.example.event_management.fragments.competition;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.event_management.R;
import com.example.event_management.api.RetrofitClient;
import com.example.event_management.api.models.SelectCompetition;
import com.example.event_management.databinding.FragmentUpcomingCompIntroBinding;
import com.example.event_management.utils.SharedPrefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpcomingCompIntroFragment extends Fragment {

    FragmentUpcomingCompIntroBinding introBinding;
    UpcomingCompRegisterFragment registerFragment;
    SharedPrefs sharedPrefs;
    private String firstPrize = "First prize is Rs ";
    private String secondPrize = "Second prize is Rs ";
    private String thirdPrize = "Third prize is Rs ";
    private String fees = "Rs ";
    private String id;
    public UpcomingCompIntroFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        introBinding = FragmentUpcomingCompIntroBinding
            .inflate(inflater, container, false);
        Bundle b = Objects.requireNonNull(getActivity()).getIntent().getExtras();
        id = Objects.requireNonNull(b).getString("ID");
        Log.d("INTRO FRAGMENT", id);
        sharedPrefs = new SharedPrefs(Objects.requireNonNull(getContext()));
        registerFragment = new UpcomingCompRegisterFragment();

        fetchCompetitionData();

        introBinding.registerForCompButton.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_competitions_frame, registerFragment)
                    .commit();
        });

        return introBinding.getRoot();
    }

    private void fetchCompetitionData() {

        Call<SelectCompetition> call = RetrofitClient.getClient().getSelectedCompetition(sharedPrefs.getToken(), id);
        call.enqueue(new Callback<SelectCompetition>() {
            @Override
            public void onResponse(Call<SelectCompetition> call, Response<SelectCompetition> response) {
                SelectCompetition competition = response.body();
                Log.d("SELECTED COMPETITION", competition.getCompetition().getTitle());
                firstPrize += competition.getCompetition().getRewards().get(0);
                secondPrize += competition.getCompetition().getRewards().get(1);
                thirdPrize += competition.getCompetition().getRewards().get(2);
                fees += competition.getCompetition().getFees() + "/-";
                introBinding.competitionHeader.competitionName
                        .setText(competition.getCompetition().getTitle());
                introBinding.competitionHeader.category
                        .setText(competition.getCompetition().getCategory());
                introBinding.competitionHeader.date
                        .setText(competition.getCompetition().getStart().split("T", 2)[0]);
                introBinding.competitionHeader.startTime
                        .setText(competition.getCompetition().getStart());
                introBinding.competitionHeader.endTime
                        .setText(competition.getCompetition().getEnd());
                introBinding.prizeListBody.firstPrizeText.setText(firstPrize);
                introBinding.prizeListBody.secondPrizeText.setText(secondPrize);
                introBinding.prizeListBody.thirdPrizeText.setText(thirdPrize);
                introBinding.feesAmount.setText(fees);
            }

            @Override
            public void onFailure(Call<SelectCompetition> call, Throwable t) {
                Log.d("SELECTED COMPETITION", "failed");
                Log.d("SELECTED COMPETITION", sharedPrefs.getToken());
            }
        });
    }
}