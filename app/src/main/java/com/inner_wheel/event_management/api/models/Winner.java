package com.inner_wheel.event_management.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Winner {
    @SerializedName("first")
    @Expose
    private String first;

    @SerializedName("second")
    @Expose
    private String second;

    @SerializedName("third")
    @Expose
    private String third;

    public String getFirst() { return first; }

    public String getSecond() { return second; }

    public String getThird() { return third; }

    public void setFirst(String first) { this.first = first; }

    public void setSecond(String second) { this.second = second; }

    public void setThird(String third) { this.third = third; }
}
