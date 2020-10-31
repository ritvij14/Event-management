package com.inner_wheel.event_management.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.inner_wheel.event_management.databinding.ActivityAboutBinding;

public class AboutActivity extends AppCompatActivity {

    ActivityAboutBinding aboutBinding;
    private FirebaseRemoteConfig firebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aboutBinding = ActivityAboutBinding.inflate(getLayoutInflater());
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(5)
                .build();
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        setContentView(aboutBinding.getRoot());

        firebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        boolean updated = task.getResult();
                        Log.d("ABOUT_US_TEXT", "Config params updated: " + updated);
                    } else {
                        Log.d("ABOUT_US_TEXT", "Fetch failed");
                    }
                });
        String aboutUsText = firebaseRemoteConfig.getString("about_us");
        aboutBinding.aboutUsTextview.setText(aboutUsText);

        aboutBinding.aboutBackButton.setOnClickListener(v -> onBackPressed());
    }
}