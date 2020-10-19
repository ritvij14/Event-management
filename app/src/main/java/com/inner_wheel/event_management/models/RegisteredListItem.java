package com.inner_wheel.event_management.models;

public class RegisteredListItem {

    String id, name, age, school, topic;
    boolean isSubmitted;

    public RegisteredListItem(String id, String name, String age, String school, String topic, boolean isSubmitted) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.school = school;
        this.topic = topic;
        this.isSubmitted = isSubmitted;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getSchool() {
        return school;
    }

    public String getTopic() {
        return topic;
    }

    public boolean isSubmitted() {
        return isSubmitted;
    }

    public void setId(String id) {
        this.id = id;
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

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setSubmitted(boolean submitted) {
        isSubmitted = submitted;
    }
}
