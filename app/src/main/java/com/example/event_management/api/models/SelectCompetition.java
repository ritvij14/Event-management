package com.example.event_management.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SelectCompetition {

    @SerializedName("competition")
    @Expose
    CompetitionObj competition;

    public CompetitionObj getCompetition() {
        return competition;
    }

    public void setCompetition(CompetitionObj competition) {
        this.competition = competition;
    }

    public class CompetitionObj {

        @SerializedName("competition_id")
        @Expose
        String compId;

        @SerializedName("title")
        @Expose
        String title;

        @SerializedName("state")
        @Expose
        String state;

        @SerializedName("description")
        @Expose
        String description;

        @SerializedName("start_time")
        @Expose
        String start;

        @SerializedName("end_time")
        @Expose
        String end;

        @SerializedName("winner_rewards")
        @Expose
        List<String> rewards;

        @SerializedName("age_groups")
        @Expose
        List<AgeGroup> ageGroups;

        @SerializedName("registration_fee")
        @Expose
        String fees;

        @SerializedName("genre")
        @Expose
        String category;

        public String getCompId() { return compId; }

        public String getTitle() { return title; }

        public String getState() { return state; }

        public String getDescription() { return description; }

        public String getStart() { return start; }

        public String getEnd() { return end; }

        public List<String> getRewards() { return rewards; }

        public List<AgeGroup> getAgeGroups() { return ageGroups; }

        public String getFees() { return fees; }

        public String getCategory() { return category; }

        public void setCompId(String compId) { this.compId = compId; }

        public void setTitle(String title) { this.title = title; }

        public void setState(String state) { this.state = state; }

        public void setDescription(String description) { this.description = description; }

        public void setStart(String start) { this.start = start; }

        public void setEnd(String end) { this.end = end; }

        public void setRewards(List<String> rewards) { this.rewards = rewards; }

        public void setAgeGroups(List<AgeGroup> ageGroups) { this.ageGroups = ageGroups; }

        public void setFees(String fees) { this.fees = fees; }

        public void setCategory(String category) { this.category = category; }
    }
}
