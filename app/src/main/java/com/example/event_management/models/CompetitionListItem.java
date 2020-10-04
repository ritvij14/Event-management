package com.example.event_management.models;

public class CompetitionListItem {

    String competitionName;
    String date;
    String category;

    public CompetitionListItem(String competitionName, String date, String category) {
        this.competitionName = competitionName;
        this.date = date;
        this.category = category;
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
}
