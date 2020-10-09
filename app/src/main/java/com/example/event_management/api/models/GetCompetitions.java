package com.example.event_management.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetCompetitions {

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("competitions")
    @Expose
    private List<Competition> competitions;

    public Boolean getSuccess() {
        return success;
    }

    public List<Competition> getCompetitionsList() {
        return competitions;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public void setCompetitions(List<Competition> competitions) {
        this.competitions = competitions;
    }
}
