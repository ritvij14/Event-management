package com.inner_wheel.event_management.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Competition {

    @SerializedName("competition_id")
    @Expose
    private String competitionID;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("start_time")
    @Expose
    private String startTime;

    @SerializedName("end_time")
    @Expose
    private String endTime;

    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("winner_rewards")
    @Expose
    private String[] winnerRewards;

    @SerializedName("registration_fee")
    @Expose
    private String registrationFee;

    @SerializedName("genre")
    @Expose
    private String category;

    public String getCompetitionID() {
        return competitionID;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getState() {
        return state;
    }

    public String[] getWinnerRewards() {
        return winnerRewards;
    }

    public String getRegistrationFee() {
        return registrationFee;
    }

    public String getCategory() {
        return category;
    }

    public void setCompetitionID(String competitionID) { this.competitionID = competitionID; }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setWinnerRewards(String[] winnerRewards) {
        this.winnerRewards = winnerRewards;
    }

    public void setRegistrationFee(String registrationFee) { this.registrationFee = registrationFee; }

    public void setCategory(String category) {
        this.category = category;
    }
}
