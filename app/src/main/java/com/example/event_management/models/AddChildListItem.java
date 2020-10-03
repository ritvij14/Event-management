package com.example.event_management.models;

public class AddChildListItem {

    String name;
    String age;
    String school;

    public AddChildListItem(String name, String age, String school) {
        this.name = name;
        this.age = age;
        this.school = school;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age + "yrs.";
    }

    public String getSchool() {
        return school;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setSchool(String school) {
        this.school = school;
    }
}
