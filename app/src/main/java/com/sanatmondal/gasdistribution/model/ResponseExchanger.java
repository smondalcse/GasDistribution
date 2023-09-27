
package com.sanatmondal.gasdistribution.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseExchanger {

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
    private List<ExchangerModel> data = null;

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

    public List<ExchangerModel> getData() {
        return data;
    }

    public void setData(List<ExchangerModel> data) {
        this.data = data;
    }

}
