package com.inner_wheel.event_management.fragments.competition;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inner_wheel.event_management.R;
import com.inner_wheel.event_management.api.RetrofitClient;
import com.inner_wheel.event_management.api.models.SelectCompetition;
import com.inner_wheel.event_management.databinding.FragmentUpcomingCompIntroBinding;
import com.inner_wheel.event_management.utils.SharedPrefs;

import net.steamcrafted.loadtoast.LoadToast;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpcomingCompIntroFragment extends Fragment {

    FragmentUpcomingCompIntroBinding introBinding;
    UpcomingCompRegisterFragment registerFragment;
    SharedPrefs sharedPrefs;
    private String firstPrize;
    private String secondPrize;
    private String thirdPrize;
    private String fees;
    private String id, name, topic, date, startTime, endTime;
    LoadToast lt;
    public UpcomingCompIntroFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        introBinding = FragmentUpcomingCompIntroBinding
            .inflate(inflater, container, false);
        Bundle b = Objects.requireNonNull(getActivity()).getIntent().getExtras();
        id = Objects.requireNonNull(b).getString("ID");
        assert id != null;
        Log.d("INTRO FRAGMENT", id);
        sharedPrefs = new SharedPrefs(Objects.requireNonNull(getContext()));
        firstPrize = "First prize is Rs ";
        secondPrize = "Second prize is Rs ";
        thirdPrize = "Third prize is Rs ";
        fees = "Rs ";
        lt = new LoadToast(getContext());
        lt.setText("Loading...");
        lt.setTranslationY(100).setBorderColor(getResources().getColor(R.color.color_accent));
        lt.show();

        fetchCompetitionData();
        introBinding.registerForCompButton.setOnClickListener(v -> getActivity()
                .getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_competitions_frame, registerFragment)
                .addToBackStack(null)
                .commit());

        introBinding.backButton.setOnClickListener(v -> getActivity().onBackPressed());

        return introBinding.getRoot();
    }

    private void fetchCompetitionData() {

        Call<SelectCompetition> call = RetrofitClient.getClient().getSelectedCompetition(sharedPrefs.getToken(), id);
        call.enqueue(new Callback<SelectCompetition>() {
            @Override
            public void onResponse(@NotNull Call<SelectCompetition> call, @NotNull Response<SelectCompetition> response) {
                SelectCompetition competition = response.body();
                if (competition != null) {
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

                    // getting and changing data
                    name = competition.getCompetition().getTitle();
                    topic = competition.getCompetition().getCategory();
                    firstPrize += competition.getCompetition().getRewards().get(0);
                    secondPrize += competition.getCompetition().getRewards().get(1);
                    thirdPrize += competition.getCompetition().getRewards().get(2);
                    fees += competition.getCompetition().getFees() + "/-";
                    registerFragment = new UpcomingCompRegisterFragment(name, topic, date, startTime, endTime);

                    // setting data
                    introBinding.competitionHeader.competitionName
                            .setText(name);
                    introBinding.competitionHeader.category
                            .setText(topic);
                    introBinding.competitionHeader.date.setText(date);
                    introBinding.competitionHeader.startTime.setText(startTime);
                    introBinding.competitionHeader.endTime.setText(endTime);
                    introBinding.prizeListBody.firstPrizeText.setText(firstPrize);
                    introBinding.prizeListBody.secondPrizeText.setText(secondPrize);
                    introBinding.prizeListBody.thirdPrizeText.setText(thirdPrize);
                    introBinding.competitionInstructions.rule1.setText(competition.getCompetition().getDescription());
                    introBinding.feesAmount.setText(fees);

                    lt.success();
                    lt.hide();
                }
            }

            @Override
            public void onFailure(@NotNull Call<SelectCompetition> call, @NotNull Throwable t) {
                Log.d("SELECTED COMPETITION", "failed");
                lt.error();
                lt.hide();
            }
        });
    }
}