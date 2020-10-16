package com.example.event_management.fragments.competition;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.event_management.R;
import com.example.event_management.api.RetrofitClient;
import com.example.event_management.api.models.AgeGroup;
import com.example.event_management.api.models.SelectCompetition;
import com.example.event_management.databinding.FragmentCompetitionPaymentBinding;
import com.example.event_management.utils.SharedPrefs;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompetitionPaymentFragment extends Fragment {

    FragmentCompetitionPaymentBinding paymentBinding;
    String name, school, age;
    SharedPrefs sharedPrefs;
    String ageGroup = "Age Group: ";
    String theme = "Theme: ";
    public CompetitionPaymentFragment(String name, String school, String age) {
        this.name = name;
        this.school = school;
        this.age = age;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        paymentBinding = FragmentCompetitionPaymentBinding.inflate(inflater, container, false);
        sharedPrefs = new SharedPrefs(Objects.requireNonNull(getContext()));
        paymentBinding.participantInfoName.setText(name);
        paymentBinding.participantInfoAge.setText(age);
        paymentBinding.participantInfoSchool.setText(school);

        getAgeGroupInfo();
        return paymentBinding.getRoot();
    }

    private void getAgeGroupInfo() {
        String id = Objects.requireNonNull(Objects.requireNonNull(getActivity()).getIntent().getExtras()).getString("ID");
        Call<SelectCompetition> call = RetrofitClient.getClient().getSelectedCompetition(sharedPrefs.getToken(), id);
        call.enqueue(new Callback<SelectCompetition>() {
            @Override
            public void onResponse(Call<SelectCompetition> call, Response<SelectCompetition> response) {
                SelectCompetition competition = response.body();
                Log.d("SELECTED COMPETITION", competition.getCompetition().getTitle());
                List<AgeGroup> ageGroups = competition.getCompetition().getAgeGroups();
                int childAge = Integer.parseInt(age.substring(0, age.indexOf('y')));

                for (AgeGroup group: ageGroups) {
                    if (Integer.parseInt(group.getEndAge()) > childAge) {
                        Log.d("PAYMENT_FRAGMENT", group.getName());
                        ageGroup += group.getStartAge() + " to " + group.getEndAge() + " years";
                        theme += group.getName();
                        paymentBinding.ageGroupInfo.setText(ageGroup);
                        paymentBinding.ageGroupThemeInfo.setText(theme);
                        break;
                    }
                }
            }

            @Override
            public void onFailure(Call<SelectCompetition> call, Throwable t) {
                Log.d("SELECTED COMPETITION", "failed");
                Log.d("SELECTED COMPETITION", sharedPrefs.getToken());
            }
        });
    }
}