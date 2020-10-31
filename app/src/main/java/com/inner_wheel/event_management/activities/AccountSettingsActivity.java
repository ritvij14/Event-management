package com.inner_wheel.event_management.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.inner_wheel.event_management.databinding.ActivityAccountSettingsBinding;

public class AccountSettingsActivity extends AppCompatActivity {

    ActivityAccountSettingsBinding accountBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountBinding = ActivityAccountSettingsBinding.inflate(getLayoutInflater());
        setContentView(accountBinding.getRoot());
    }
}