package com.example.event_management.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String token = "token", name = "name", email = "email", userAuthStatus = "AuthStatus", contactNumber = "contact_number",
            address = "address", city = "city", state = "state";

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

    public void setContactNumber(String value) {
        editor.putString(contactNumber, value);
        editor.commit();
    }

    public void setAddress(String value) {
        editor.putString(address, value);
        editor.commit();
    }

    public void setCity(String value) {
        editor.putString(city, value);
        editor.commit();
    }

    public void setState(String value) {
        editor.putString(state, value);
        editor.commit();
    }

    public void setUserAuthStatus(String value) {
        editor.putString(userAuthStatus, value);
        editor.commit();
    }

    public String getContactNumber() { return sharedPreferences.getString(contactNumber, null); }

    public String getAddress() { return sharedPreferences.getString(address, null); }

    public String getCity() { return sharedPreferences.getString(city, null); }

    public String getState() { return sharedPreferences.getString(state, null); }

    public String getEmail() { return sharedPreferences.getString(email, null); }

    public String getUserAuthStatus() { return sharedPreferences.getString(userAuthStatus, null); }

    public String getToken() {
        return sharedPreferences.getString(token, null);
    }

    public String getName() {
        return sharedPreferences.getString(name, null);
    }
}
