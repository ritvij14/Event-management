package com.inner_wheel.event_management.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegistrationResponse {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("success")
    @Expose
    private boolean success;

    public String getMessage() { return message; }

    public boolean isSuccess() { return success; }

    public void setMessage(String message) { this.message = message; }

    public void setSuccess(boolean success) { this.success = success; }
}
