package com.sanatmondal.gasdistribution.model;

import java.io.Serializable;

public class TEMPReplacementModel implements Serializable {

    String ItemID, ItemInfo, InvType, RQty, GQty, ReturnDate, CreateBy, Type;
    //ItemID, ItemInfo   InvType =cycender RQty GQty ReturnDate    CreateBy Type=give/get


    public TEMPReplacementModel() {
    }

    public String getItemID() {
        return ItemID;
    }

    public void setItemID(String itemID) {
        ItemID = itemID;
    }

    public String getItemInfo() {
        return ItemInfo;
    }

    public void setItemInfo(String itemInfo) {
        ItemInfo = itemInfo;
    }

    public String getInvType() {
        return InvType;
    }

    public void setInvType(String invType) {
        InvType = invType;
    }

    public String getRQty() {
        return RQty;
    }

    public void setRQty(String RQty) {
        this.RQty = RQty;
    }

    public String getGQty() {
        return GQty;
    }

    public void setGQty(String GQty) {
        this.GQty = GQty;
    }

    public String getReturnDate() {
        return ReturnDate;
    }

    public void setReturnDate(String returnDate) {
        ReturnDate = returnDate;
    }

    public String getCreateBy() {
        return CreateBy;
    }

    public void setCreateBy(String createBy) {
        CreateBy = createBy;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
