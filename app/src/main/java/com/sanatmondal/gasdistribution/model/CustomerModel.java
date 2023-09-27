
package com.sanatmondal.gasdistribution.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerModel {

    @SerializedName("CustomerID")
    @Expose
    private String customerID;
    @SerializedName("CustomerName")
    @Expose
    private String customerName;
    @SerializedName("ContactName")
    @Expose
    private String contactName;
    @SerializedName("DealerBussinessName")
    @Expose
    private Object dealerBussinessName;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("Email")
    @Expose
    private Object email;
    @SerializedName("PhoneNumber")
    @Expose
    private Object phoneNumber;
    @SerializedName("Mobile1")
    @Expose
    private String mobile1;
    @SerializedName("Mobile2")
    @Expose
    private Object mobile2;
    @SerializedName("DueLimit")
    @Expose
    private Object dueLimit;
    @SerializedName("Gread")
    @Expose
    private Object gread;
    @SerializedName("CREATE_BY")
    @Expose
    private Object createBy;
    @SerializedName("CREATE_DATE")
    @Expose
    private Object createDate;
    @SerializedName("UPDATE_BY")
    @Expose
    private Object updateBy;
    @SerializedName("UPDATE_DATE")
    @Expose
    private Object updateDate;
    @SerializedName("InActive")
    @Expose
    private Object inActive;
    @SerializedName("AdvanceAmt")
    @Expose
    private Object advanceAmt;
    @SerializedName("DueAmount")
    @Expose
    private Object dueAmount;
    @SerializedName("OpeningQty")
    @Expose
    private Object openingQty;
    @SerializedName("DueAmt")
    @Expose
    private Double dueAmt;
    @SerializedName("StockQty")
    @Expose
    private Double stockQty;

    public CustomerModel(String customerID, String customerName, String phoneNumber, String mobile1, String address, Double stockQty) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.mobile1 = mobile1;
        this.address = address;
        this.stockQty = stockQty;
    }

    public Double getStockQty() {
        return stockQty;
    }

    public void setStockQty(Double stockQty) {
        this.stockQty = stockQty;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Object getDealerBussinessName() {
        return dealerBussinessName;
    }

    public void setDealerBussinessName(Object dealerBussinessName) {
        this.dealerBussinessName = dealerBussinessName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Object getEmail() {
        return email;
    }

    public void setEmail(Object email) {
        this.email = email;
    }

    public Object getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Object phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMobile1() {
        return mobile1;
    }

    public void setMobile1(String mobile1) {
        this.mobile1 = mobile1;
    }

    public Object getMobile2() {
        return mobile2;
    }

    public void setMobile2(Object mobile2) {
        this.mobile2 = mobile2;
    }

    public Object getDueLimit() {
        return dueLimit;
    }

    public void setDueLimit(Object dueLimit) {
        this.dueLimit = dueLimit;
    }

    public Object getGread() {
        return gread;
    }

    public void setGread(Object gread) {
        this.gread = gread;
    }

    public Object getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Object createBy) {
        this.createBy = createBy;
    }

    public Object getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Object createDate) {
        this.createDate = createDate;
    }

    public Object getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Object updateBy) {
        this.updateBy = updateBy;
    }

    public Object getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Object updateDate) {
        this.updateDate = updateDate;
    }

    public Object getInActive() {
        return inActive;
    }

    public void setInActive(Object inActive) {
        this.inActive = inActive;
    }

    public Object getAdvanceAmt() {
        return advanceAmt;
    }

    public void setAdvanceAmt(Object advanceAmt) {
        this.advanceAmt = advanceAmt;
    }

    public Object getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(Object dueAmount) {
        this.dueAmount = dueAmount;
    }

    public Object getOpeningQty() {
        return openingQty;
    }

    public void setOpeningQty(Object openingQty) {
        this.openingQty = openingQty;
    }

    public Double getDueAmt() {
        return dueAmt;
    }

    public void setDueAmt(Double dueAmt) {
        this.dueAmt = dueAmt;
    }


}
