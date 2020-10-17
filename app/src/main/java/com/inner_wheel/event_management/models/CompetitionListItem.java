package com.inner_wheel.event_management.models;

public class CompetitionListItem {

    String competitionName;
    String date;
    String category;
    String id;

    public CompetitionListItem(String competitionName, String date, String category, String id) {
        this.competitionName = competitionName;
        this.date = date;
        this.category = category;
        this.id = id;
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

    public String getId() { return id; }
}
