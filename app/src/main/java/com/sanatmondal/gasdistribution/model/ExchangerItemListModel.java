package com.sanatmondal.gasdistribution.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExchangerItemListModel {

    @SerializedName("totalItemCount")
    @Expose
    private Integer totalItemCount;
    @SerializedName("ID")
    @Expose
    private Double id;
    @SerializedName("ReturnDate")
    @Expose
    private String returnDate;
    @SerializedName("ItemID")
    @Expose
    private String itemID;
    @SerializedName("ItemInfo")
    @Expose
    private String itemInfo;
    @SerializedName("RQty")
    @Expose
    private Double rQty;
    @SerializedName("GQty")
    @Expose
    private Double gQty;
    @SerializedName("CreateBy")
    @Expose
    private String createBy;
    @SerializedName("CreateDate")
    @Expose
    private String createDate;
    @SerializedName("InvType")
    @Expose
    private String invType;
    @SerializedName("CompanySupplierID")
    @Expose
    private Object companySupplierID;
    @SerializedName("WHID")
    @Expose
    private Object whid;
    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("Comment")
    @Expose
    private Object comment;

    public Integer getTotalItemCount() {
        return totalItemCount;
    }

    public void setTotalItemCount(Integer totalItemCount) {
        this.totalItemCount = totalItemCount;
    }

    public Double getId() {
        return id;
    }

    public void setId(Double id) {
        this.id = id;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getItemInfo() {
        return itemInfo;
    }

    public void setItemInfo(String itemInfo) {
        this.itemInfo = itemInfo;
    }

    public Double getRQty() {
        return rQty;
    }

    public void setRQty(Double rQty) {
        this.rQty = rQty;
    }

    public Double getGQty() {
        return gQty;
    }

    public void setGQty(Double gQty) {
        this.gQty = gQty;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getInvType() {
        return invType;
    }

    public void setInvType(String invType) {
        this.invType = invType;
    }

    public Object getCompanySupplierID() {
        return companySupplierID;
    }

    public void setCompanySupplierID(Object companySupplierID) {
        this.companySupplierID = companySupplierID;
    }

    public Object getWhid() {
        return whid;
    }

    public void setWhid(Object whid) {
        this.whid = whid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getComment() {
        return comment;
    }

    public void setComment(Object comment) {
        this.comment = comment;
    }

}
