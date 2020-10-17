package com.inner_wheel.event_management.models;

public class AddChildListItem {

    String name;
    String age;
    String school;
    String id;

    public AddChildListItem(String name, String age, String school, String id) {
        this.name = name;
        this.age = age;
        this.school = school;
        this.id = id;
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

    public String getId() { return id; }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public void setId(String id) { this.id = id; }
}
