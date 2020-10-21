package com.inner_wheel.event_management.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SelectedTransactionResponse {

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("details")
    @Expose
    private SelectedTransaction details;

    public boolean isSuccess() {
        return success;
    }

    public SelectedTransaction getDetails() {
        return details;
    }

    public class SelectedTransaction extends TransactionItem {

        @SerializedName("competition")
        @Expose
        private TransactionResponseCompetition competition;

        @SerializedName("participant")
        @Expose
        private TransactionResponseParticipant participant;

        public TransactionResponseCompetition getCompetition() {
            return competition;
        }

        public TransactionResponseParticipant getParticipant() {
            return participant;
        }

        public void setCompetition(TransactionResponseCompetition competition) {
            this.competition = competition;
        }

        public void setParticipant(TransactionResponseParticipant participant) {
            this.participant = participant;
        }
    }

    public class TransactionResponseCompetition {
        @SerializedName("competition_id")
        @Expose
        private String competitionID;

        @SerializedName("title")
        @Expose
        private String title;

        @SerializedName("description")
        @Expose
        private String description;

        public String getCompetitionID() {
            return competitionID;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public void setCompetitionID(String competitionID) {
            this.competitionID = competitionID;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public class TransactionResponseParticipant {
        @SerializedName("participant_id")
        @Expose
        private String participantID;

        @SerializedName("name")
        @Expose
        private String name;

        @SerializedName("age")
        @Expose
        private int age;

        public String getParticipantID() {
            return participantID;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public void setParticipantID(String participantID) {
            this.participantID = participantID;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}
