package com.inner_wheel.event_management.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ParentLogin {

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("user")
    @Expose
    private User user;

    public Boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getAuthToken() {
        return token;
    }

    public User getUser() { return user; }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setAuthToken(String token) {
        this.token = token;
    }

    public void setUser(User user) { this.user = user; }

    public class User {

        @SerializedName("user_id")
        @Expose
        private String userID;

        @SerializedName("name")
        @Expose
        private String name;

        @SerializedName("email")
        @Expose
        private String email;

        @SerializedName("city")
        @Expose
        private String city;

        @SerializedName("state")
        @Expose
        private String state;

        @SerializedName("address")
        @Expose
        private String address;

        @SerializedName("phone_number")
        @Expose
        private String phoneNumber;

        public String getUserID() { return userID; }

        public String getName() { return name; }

        public String getEmail() { return email; }

        public String getCity() { return city; }

        public String getState() { return state; }

        public String getAddress() { return address; }

        public String getPhoneNumber() { return phoneNumber; }

        public void setUserID(String userID) { this.userID = userID; }

        public void setName(String name) { this.name = name; }

        public void setEmail(String email) { this.email = email; }

        public void setCity(String city) { this.city = city; }

        public void setState(String state) { this.state = state; }

        public void setAddress(String address) { this.address = address; }

        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    }
}
