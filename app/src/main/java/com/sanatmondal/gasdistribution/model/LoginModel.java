package com.sanatmondal.gasdistribution.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoginModel implements Serializable {

    @SerializedName("Success")
    @Expose
    private Boolean success;
    @SerializedName("Msg")
    @Expose
    private String msg;
    @SerializedName("Counts")
    @Expose
    private Integer counts;
    @SerializedName("Data")
    @Expose
    private UserModel user;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCounts() {
        return counts;
    }

    public void setCounts(Integer counts) {
        this.counts = counts;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel data) {
        this.user = data;
    }
}
