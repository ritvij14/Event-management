package com.example.event_management.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;

import com.example.event_management.R;
import com.example.event_management.databinding.ActivityResgistrationBinding;
import com.example.event_management.fragments.registration.ParentLoginFragment;

public class RegistrationActivity extends AppCompatActivity {

    ActivityResgistrationBinding binding;
    ParentLoginFragment loginFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResgistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loginFragment = new ParentLoginFragment();

        getSupportFragmentManager().beginTransaction()
            .replace(R.id.registration_frame_layout, loginFragment)
            .commit();
    }
}