package com.sanatmondal.gasdistribution.model;

import java.io.Serializable;

public class WarehouseModel implements Serializable {
    String id, WHNo, WarehouseName, Address;

    public WarehouseModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWHNo() {
        return WHNo;
    }

    public void setWHNo(String WHNo) {
        this.WHNo = WHNo;
    }

    public String getWarehouseName() {
        return WarehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        WarehouseName = warehouseName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
