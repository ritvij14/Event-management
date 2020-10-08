package com.example.event_management.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;

import com.example.event_management.R;
import com.example.event_management.databinding.ActivityResgistrationBinding;
import com.example.event_management.fragments.registration.ParentLoginFragment;
import com.example.event_management.utils.SharedPrefs;

public class RegistrationActivity extends AppCompatActivity {

    ActivityResgistrationBinding binding;
    ParentLoginFragment loginFragment;
    SharedPrefs sharedPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResgistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loginFragment = new ParentLoginFragment();
        sharedPrefs = new SharedPrefs(this);

        if (sharedPrefs.getUserAuthStatus() != null) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.registration_frame_layout, loginFragment)
                    .commit();
        }
    }
}