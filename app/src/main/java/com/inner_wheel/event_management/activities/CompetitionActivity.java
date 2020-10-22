package com.inner_wheel.event_management.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.inner_wheel.event_management.R;
import com.inner_wheel.event_management.fragments.competition.CompetitionSubmissionFragment;
import com.inner_wheel.event_management.fragments.competition.UpcomingCompIntroFragment;

public class CompetitionActivity extends AppCompatActivity {

    UpcomingCompIntroFragment introFragment;
    CompetitionSubmissionFragment submissionFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition);
        introFragment = new UpcomingCompIntroFragment();
        submissionFragment = new CompetitionSubmissionFragment();
        Bundle b = getIntent().getExtras();
        assert b != null;
        String compStatus = b.getString("STATUS");
        // Toast.makeText(this, compStatus, Toast.LENGTH_SHORT).show();

        assert compStatus != null;
        switch (compStatus) {
            case "UPCOMING":
                // for future competitions
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_competitions_frame, introFragment)
                        .commit();
                break;
            case "ONGOING":
                // for competitions on current day
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_competitions_frame, submissionFragment)
                        .commit();
                break;
            case "COMPLETED":
                // for competitions that have been completed

                break;
        }
    }
}