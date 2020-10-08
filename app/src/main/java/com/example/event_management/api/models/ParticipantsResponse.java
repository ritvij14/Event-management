package com.example.event_management.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ParticipantsResponse {

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("participants")
    @Expose
    private List<Participant> participants;

    public boolean isSuccess() {
        return success;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }
}
