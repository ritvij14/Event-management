package com.inner_wheel.event_management.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RegisteredParticipants {

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("details")
    @Expose
    List<RegisteredParticipant> registeredParticipantList;

    public boolean isSuccess() {
        return success;
    }

    public List<RegisteredParticipant> getRegisteredParticipantList() {
        return registeredParticipantList;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setRegisteredParticipantList(List<RegisteredParticipant> registeredParticipantList) {
        this.registeredParticipantList = registeredParticipantList;
    }

    public class RegisteredParticipant {

        @SerializedName("participant_id")
        @Expose
        private String participantID;

        @SerializedName("name")
        @Expose
        private String name;

        @SerializedName("school")
        @Expose
        private String school;

        @SerializedName("age")
        @Expose
        private String age;

        @SerializedName("age_group")
        @Expose
        private AgeGroup ageGroup;

        @SerializedName("submitted")
        @Expose
        private boolean submitted;

        public String getParticipantID() {
            return participantID;
        }

        public String getName() {
            return name;
        }

        public String getSchool() {
            return school;
        }

        public String getAge() {
            return age;
        }

        public AgeGroup getAgeGroup() {
            return ageGroup;
        }

        public boolean isSubmitted() {
            return submitted;
        }

        public void setParticipantID(String participantID) {
            this.participantID = participantID;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setSchool(String school) {
            this.school = school;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public void setAgeGroup(AgeGroup ageGroup) {
            this.ageGroup = ageGroup;
        }

        public void setSubmitted(boolean submitted) {
            this.submitted = submitted;
        }
    }
}
