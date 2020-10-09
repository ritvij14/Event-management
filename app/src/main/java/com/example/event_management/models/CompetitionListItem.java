package com.example.event_management.models;

public class CompetitionListItem {

    String competitionName;
    String date;
    String category;
    String state;

    public CompetitionListItem(String competitionName, String date, String category, String state) {
        this.competitionName = competitionName;
        this.date = date;
        this.category = category;
        this.state = state;
    }

    public String getCompetitionName() {
        return competitionName;
    }

    public String getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    public String getState() { return state; }
}
