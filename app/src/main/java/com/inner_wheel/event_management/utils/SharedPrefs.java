package com.inner_wheel.event_management.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;
    private final String token = "token", name = "name", email = "email", userAuthStatus = "AuthStatus", isJustRegistered = "just_registered",
            contactNumber = "contact_number", address = "address", city = "city", state = "state", fcmToken = "fcm_token";

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

    public void setFcmToken(String value) {
        editor.putString(fcmToken, value);
        editor.commit();
    }

    public void setIsJustRegistered(Boolean justRegistered) {
        editor.putBoolean(isJustRegistered, justRegistered);
        editor.commit();
    }

    public boolean getIsJustRegistered() { return sharedPreferences.getBoolean(isJustRegistered, false); }

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

    public String getFcmToken() { return sharedPreferences.getString(fcmToken, null); }
}
