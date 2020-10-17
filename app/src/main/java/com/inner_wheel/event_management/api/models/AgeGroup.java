package com.inner_wheel.event_management.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AgeGroup {

    @SerializedName("start_age")
    @Expose
    String startAge;

    @SerializedName("end_age")
    @Expose
    String endAge;

    @SerializedName("name")
    @Expose
    String name;

    @SerializedName("age_group_id")
    @Expose
    String grpId;

    public String getStartAge() { return startAge; }

    public String getEndAge() { return endAge; }

    public String getName() { return name; }

    public String getGrpId() { return grpId; }

    public void setStartAge(String startAge) { this.startAge = startAge; }

    public void setEndAge(String endAge) { this.endAge = endAge; }

    public void setName(String name) { this.name = name; }

    public void setGrpId(String grpId) { this.grpId = grpId; }
}
