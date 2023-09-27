package com.sanatmondal.gasdistribution.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TempOrderSale implements Serializable {
    @SerializedName("totalItemCount")
    @Expose
    private Integer totalItemCount;
    @SerializedName("ID")
    @Expose
    private Double id;
    @SerializedName("OrderNo")
    @Expose
    private String orderNo;
    @SerializedName("OrderDate")
    @Expose
    private Object orderDate;
    @SerializedName("DeliveryPersonNo")
    @Expose
    private Object deliveryPersonNo;
    @SerializedName("CustomerID")
    @Expose
    private String customerID;
    @SerializedName("Customer")
    @Expose
    private CustomerModel Customer;
    @SerializedName("Item_Id")
    @Expose
    private String itemId;
    @SerializedName("ItemInfo")
    @Expose
    private String itemInfo;
    @SerializedName("InventoryType")
    @Expose
    private String inventoryType;
    @SerializedName("SalesType")
    @Expose
    private String salesType;
    @SerializedName("SalesQty")
    @Expose
    private Double salesQty;
    @SerializedName("CollectionQty")
    @Expose
    private Double collectionQty;
    @SerializedName("SlenderSalesPrice")
    @Expose
    private Object slenderSalesPrice;
    @SerializedName("GasSalesPrice")
    @Expose
    private Object gasSalesPrice;
    @SerializedName("AddCose")
    @Expose
    private Object addCose;
    @SerializedName("TotalMRP")
    @Expose
    private Object totalMRP;
    @SerializedName("NetAmount")
    @Expose
    private Double netAmount;
    @SerializedName("Remarks")
    @Expose
    private Object remarks;
    @SerializedName("CreateBy")
    @Expose
    private String createBy;
    @SerializedName("CreateDate")
    @Expose
    private String createDate;
    @SerializedName("UpdateBy")
    @Expose
    private Object updateBy;
    @SerializedName("UpdateDate")
    @Expose
    private Object updateDate;
    @SerializedName("Status")
    @Expose
    private Object status;

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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Object getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Object orderDate) {
        this.orderDate = orderDate;
    }

    public Object getDeliveryPersonNo() {
        return deliveryPersonNo;
    }

    public void setDeliveryPersonNo(Object deliveryPersonNo) {
        this.deliveryPersonNo = deliveryPersonNo;
    }

    public String getCustomerID() {
        return customerID;
    }

    public CustomerModel getCustomer() {
        return Customer;
    }

    public void setCustomer(CustomerModel customer) {
        Customer = customer;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemInfo() {
        return itemInfo;
    }

    public void setItemInfo(String itemInfo) {
        this.itemInfo = itemInfo;
    }

    public String getInventoryType() {
        return inventoryType;
    }

    public void setInventoryType(String inventoryType) {
        this.inventoryType = inventoryType;
    }

    public String getSalesType() {
        return salesType;
    }

    public void setSalesType(String salesType) {
        this.salesType = salesType;
    }

    public Double getSalesQty() {
        return salesQty;
    }

    public void setSalesQty(Double salesQty) {
        this.salesQty = salesQty;
    }

    public Double getCollectionQty() {
        return collectionQty;
    }

    public void setCollectionQty(Double collectionQty) {
        this.collectionQty = collectionQty;
    }

    public Object getSlenderSalesPrice() {
        return slenderSalesPrice;
    }

    public void setSlenderSalesPrice(Object slenderSalesPrice) {
        this.slenderSalesPrice = slenderSalesPrice;
    }

    public Object getGasSalesPrice() {
        return gasSalesPrice;
    }

    public void setGasSalesPrice(Object gasSalesPrice) {
        this.gasSalesPrice = gasSalesPrice;
    }

    public Object getAddCose() {
        return addCose;
    }

    public void setAddCose(Object addCose) {
        this.addCose = addCose;
    }

    public Object getTotalMRP() {
        return totalMRP;
    }

    public void setTotalMRP(Object totalMRP) {
        this.totalMRP = totalMRP;
    }

    public Double getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(Double netAmount) {
        this.netAmount = netAmount;
    }

    public Object getRemarks() {
        return remarks;
    }

    public void setRemarks(Object remarks) {
        this.remarks = remarks;
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

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }
}
