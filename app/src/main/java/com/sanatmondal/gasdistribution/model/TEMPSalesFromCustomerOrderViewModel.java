package com.sanatmondal.gasdistribution.model;

import java.io.Serializable;

public class TEMPSalesFromCustomerOrderViewModel implements Serializable {
    String OrderNo, ItemInfo, CustomerID, Item_Id, SalesType, SalesQty;

    public TEMPSalesFromCustomerOrderViewModel() {
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getItemInfo() {
        return ItemInfo;
    }

    public void setItemInfo(String itemInfo) {
        ItemInfo = itemInfo;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String customerID) {
        CustomerID = customerID;
    }

    public String getItem_Id() {
        return Item_Id;
    }

    public void setItem_Id(String item_Id) {
        Item_Id = item_Id;
    }

    public String getSalesType() {
        return SalesType;
    }

    public void setSalesType(String salesType) {
        SalesType = salesType;
    }

    public String getSalesQty() {
        return SalesQty;
    }

    public void setSalesQty(String salesQty) {
        SalesQty = salesQty;
    }
}
