package com.example.event_management.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String token = "token", name = "name", email = "email", userAuthStatus = "AuthStatus";

    @SuppressLint("CommitPrefEdits")
    public SharedPrefs(Context context) {
        sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setToken(String key) {
        editor.putString(token, key);
        editor.commit();
    }

    public void setName(String key) {
        editor.putString(name, key);
        editor.commit();
    }

    public void setEmail(String value) {
        editor.putString(email, value);
        editor.commit();
    }

    public void setUserAuthStatus(String value) {
        editor.putString(userAuthStatus, value);
    }

    public String getEmail() { return sharedPreferences.getString(email, null); }

    public String getUserAuthStatus() { return sharedPreferences.getString(userAuthStatus, null); }

    public String getToken() {
        return sharedPreferences.getString(token, null);
    }

    public String getName() {
        return sharedPreferences.getString(name, null);
    }
}
