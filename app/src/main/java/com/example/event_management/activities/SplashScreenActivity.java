package com.example.event_management.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;

import com.example.event_management.databinding.ActivitySplashScreenBinding;

public class SplashScreenActivity extends AppCompatActivity {

    ActivitySplashScreenBinding splashBinding;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashBinding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(splashBinding.getRoot());
        handler = new Handler();

        AlphaAnimation logoAnimation = new AlphaAnimation(0.0f, 1.0f);
        AlphaAnimation textAnimation = new AlphaAnimation(0.0f, 1.0f);
        AlphaAnimation plantAnimation = new AlphaAnimation(0.0f, 1.0f);
        logoAnimation.setDuration(500);
        splashBinding.splashLogo.startAnimation(logoAnimation);
        textAnimation.setDuration(500);
        splashBinding.splashTitle.startAnimation(textAnimation);
        plantAnimation.setDuration(1500);
        splashBinding.splashArtist.startAnimation(plantAnimation);
        splashBinding.splashPlantTree.startAnimation(plantAnimation);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
            }
        }, 1500);
    }
}