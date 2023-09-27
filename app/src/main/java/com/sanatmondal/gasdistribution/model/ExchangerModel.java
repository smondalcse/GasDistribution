package com.sanatmondal.gasdistribution.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ExchangerModel implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("ExchengerName")
    @Expose
    private String exchengerName;
    @SerializedName("MobileNo")
    @Expose
    private String mobileNo;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("BusinessName")
    @Expose
    private Object businessName;
    @SerializedName("StockQty")
    @Expose
    private Double stockQty;

    public Double getStockQty() {
        return stockQty;
    }

    public void setStockQty(Double stockQty) {
        this.stockQty = stockQty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExchengerName() {
        return exchengerName;
    }

    public void setExchengerName(String exchengerName) {
        this.exchengerName = exchengerName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Object getBusinessName() {
        return businessName;
    }

    public void setBusinessName(Object businessName) {
        this.businessName = businessName;
    }

}
