package com.inner_wheel.event_management.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.inner_wheel.event_management.R;
import com.inner_wheel.event_management.fragments.competition.UpcomingCompIntroFragment;

public class CompetitionActivity extends AppCompatActivity {

    UpcomingCompIntroFragment introFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition);
        introFragment = new UpcomingCompIntroFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_competitions_frame, introFragment)
                .commit();
    }
}