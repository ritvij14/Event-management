package com.inner_wheel.event_management.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignUpResponse {

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("message")
    @Expose
    private String message;

    public boolean isSuccess() { return success; }

    public String getMessage() { return message; }

    public void setSuccess(boolean success) { this.success = success; }

    public void setMessage(String message) { this.message = message; }
}
