package com.inner_wheel.event_management.fragments.competition;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.inner_wheel.event_management.R;
import com.inner_wheel.event_management.adapters.ResultsAdapter;
import com.inner_wheel.event_management.api.RetrofitClient;
import com.inner_wheel.event_management.api.models.AgeGroup;
import com.inner_wheel.event_management.api.models.RegisteredParticipants;
import com.inner_wheel.event_management.api.models.SelectCompetition;
import com.inner_wheel.event_management.databinding.FragmentCompetitionResultBinding;
import com.inner_wheel.event_management.models.RegisteredListItem;
import com.inner_wheel.event_management.utils.SharedPrefs;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import net.steamcrafted.loadtoast.LoadToast;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompetitionResultFragment extends Fragment {

    FragmentCompetitionResultBinding resultBinding;
    SharedPrefs sharedPrefs;
    private int submissionCount = 0;
    private FirebaseRemoteConfig firebaseRemoteConfig;
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
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(5)
                .build();
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings);
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
        hasParticipated();
        fetchCompetitionData();
        resultsAdapter = new ResultsAdapter(ageGroupList, getContext());
        resultBinding.resultsRv.setAdapter(resultsAdapter);
        resultsAdapter.notifyDataSetChanged();

        if (submissionCount > 0) {
            resultBinding.downloadCertificateBtn.setVisibility(View.VISIBLE);
            /*firebaseRemoteConfig.fetchAndActivate()
                    .addOnCompleteListener((Executor) this, task -> {
                        if (task.isSuccessful()) {
                            boolean updated = task.getResult();
                            Log.d("CERTIFICATE_LINK", "Config params updated: " + updated);
                        } else {
                            Log.d("CERTIFICATE_LINK", "Fetch failed");
                        }
                    });*/
        }
        resultBinding.downloadCertificateBtn.setOnClickListener(view -> {
            for (int i = 1; i <= submissionCount; i++) {
                String certificateLink = firebaseRemoteConfig.getString("certificate_link");
                // Toast.makeText(getContext(), certificateLink, Toast.LENGTH_SHORT).show();
                Log.d("CERTIFICATE_LINK", certificateLink);
                resultBinding.certificateImg.setVisibility(View.VISIBLE);
                Picasso.get().load(certificateLink).resize(500, 500).into(resultBinding.certificateImg);
            }
        });
        return resultBinding.getRoot();
    }

    private void hasParticipated() {
        Call<RegisteredParticipants> call = RetrofitClient.getClient().getRegisteredParticipants(sharedPrefs.getToken(), id);
        call.enqueue(new Callback<RegisteredParticipants>() {
            @Override
            public void onResponse(Call<RegisteredParticipants> call, Response<RegisteredParticipants> response) {
                RegisteredParticipants res = response.body();
                if (res != null) {
                    if (res.isSuccess()) {
                        for (RegisteredParticipants.RegisteredParticipant r: res.getRegisteredParticipantList()) {
                            if (r.isSubmitted()) {
                                submissionCount++;
                            }
                        }
                        if (submissionCount > 0) {
                            resultBinding.downloadCertificateBtn.setVisibility(View.VISIBLE);
                            firebaseRemoteConfig.fetchAndActivate()
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            boolean updated = task.getResult();
                                            Log.d("CERTIFICATE_LINK", "Config params updated: " + updated);
                                        } else {
                                            Log.d("CERTIFICATE_LINK", "Fetch failed");
                                        }
                                    });
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisteredParticipants> call, Throwable t) {

            }
        });
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
                    // Toast.makeText(getContext(), name, Toast.LENGTH_SHORT).show();

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