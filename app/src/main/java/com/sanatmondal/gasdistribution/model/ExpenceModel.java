package com.sanatmondal.gasdistribution.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ExpenceModel implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("DailyExpenceTypeName")
    @Expose
    private String dailyExpenceTypeName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDailyExpenceTypeName() {
        return dailyExpenceTypeName;
    }

    public void setDailyExpenceTypeName(String dailyExpenceTypeName) {
        this.dailyExpenceTypeName = dailyExpenceTypeName;
    }

}
