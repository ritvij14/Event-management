package com.inner_wheel.event_management.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubmissionResponse {

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("submission")
    @Expose
    private Submission submission;

    public Boolean getSuccess() { return success; }

    public Submission getSubmission() { return submission; }

    private class Submission {

        @SerializedName("submission_id")
        @Expose
        private String submissionID;

        @SerializedName("url")
        @Expose
        private String url;
    }
}
