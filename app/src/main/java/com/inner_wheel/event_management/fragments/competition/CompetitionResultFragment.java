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
import com.inner_wheel.event_management.adapters.ResultsAdapter;
import com.inner_wheel.event_management.api.RetrofitClient;
import com.inner_wheel.event_management.api.models.AgeGroup;
import com.inner_wheel.event_management.api.models.SelectCompetition;
import com.inner_wheel.event_management.databinding.FragmentCompetitionResultBinding;
import com.inner_wheel.event_management.utils.SharedPrefs;

import net.steamcrafted.loadtoast.LoadToast;

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

public class CompetitionResultFragment extends Fragment {

    FragmentCompetitionResultBinding resultBinding;
    SharedPrefs sharedPrefs;
    private String id, name, topic, date, startTime, endTime;
    private ResultsAdapter resultsAdapter;
    ArrayList<AgeGroup> ageGroupList;
    LoadToast lt;
    public CompetitionResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        resultBinding = FragmentCompetitionResultBinding.inflate(inflater, container, false);
        sharedPrefs = new SharedPrefs(Objects.requireNonNull(getContext()));
        Bundle b = Objects.requireNonNull(getActivity()).getIntent().getExtras();
        id = Objects.requireNonNull(b).getString("ID");
        ageGroupList = new ArrayList<>();
        lt = new LoadToast(getContext());
        lt.setText("Loading...");
        lt.setTranslationY(100).setBorderColor(getResources().getColor(R.color.color_accent));
        lt.show();

        resultBinding.resultsRv.setHasFixedSize(true);
        resultBinding.resultsRv.setLayoutManager(new LinearLayoutManager(
                getContext(),
                RecyclerView.VERTICAL,
                false
        ));
        fetchCompetitionData();
        resultsAdapter = new ResultsAdapter(ageGroupList, getContext());
        resultBinding.resultsRv.setAdapter(resultsAdapter);
        resultsAdapter.notifyDataSetChanged();

        return resultBinding.getRoot();
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
                    Toast.makeText(getContext(), name, Toast.LENGTH_SHORT).show();

                    // setting data
                    resultBinding.competitionHeader.competitionName.setText(name);
                    resultBinding.competitionHeader.category.setText(topic);
                    resultBinding.competitionHeader.date.setText(date);
                    resultBinding.competitionHeader.startTime.setText(startTime);
                    resultBinding.competitionHeader.endTime.setText(endTime);

                    ageGroupList.addAll(competition.getCompetition().getAgeGroups());
                    resultsAdapter.notifyDataSetChanged();
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