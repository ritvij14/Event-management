package com.inner_wheel.event_management.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.inner_wheel.event_management.R;
import com.inner_wheel.event_management.databinding.ActivityMainBinding;
import com.inner_wheel.event_management.fragments.HomeFragment;
import com.inner_wheel.event_management.utils.SharedPrefs;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mainBinding;
    HomeFragment homeFragment;
    SharedPrefs sharedPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        sharedPrefs = new SharedPrefs(this);
        setContentView(mainBinding.getRoot());


        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.w("FIREBASE_MESSAGING", "Fetching FCM registration token failed", task.getException());
                return;
            }

            // Get new FCM registration token
            String token = task.getResult();
            sharedPrefs.setFcmToken(token);

            // Log and toast
            Log.d("FIREBASE_MESSAGING", token);
            // Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
        });


        initNavigationDrawer();

        homeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_frame_layout, homeFragment)
                .commit();
    }

    @SuppressLint({"SetTextI18n", "NonConstantResourceId"})
    private void initNavigationDrawer() {
        mainBinding.navView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_settings:
                    break;

                case R.id.nav_about_us:
                    startActivity(new Intent(this, AboutActivity.class));
                    break;

                case R.id.nav_transaction_history:
                    startActivity(new Intent(this, TransactionHistoryActivity.class));
                    break;

                case R.id.nav_logout:
                    sharedPrefs.setUserAuthStatus("SIGNED_OUT");
                    startActivity(new Intent(this, RegistrationActivity.class));
                    finish();
                    break;
            }
            return false;
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mainBinding.drawerLayout, R.string.open_drawer, R.string.close_drawer){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mainBinding.drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // setting text for header
        View headerLayout = mainBinding.navView.getHeaderView(0);
        MaterialTextView navHeaderText = headerLayout.findViewById(R.id.nav_header_text);
        navHeaderText.setText("Hi " + sharedPrefs.getName());
    }

    @SuppressLint("RtlHardcoded")
    @Override
    public void onBackPressed() {
        if (mainBinding.drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mainBinding.drawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            super.onBackPressed();
            finish();
        }
    }
}