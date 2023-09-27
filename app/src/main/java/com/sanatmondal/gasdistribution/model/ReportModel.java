package com.sanatmondal.gasdistribution.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ReportModel implements Serializable {


    @SerializedName("ID")
    @Expose
    private Double id;
    @SerializedName("InvoiceNo")
    @Expose
    private Object invoiceNo;
    @SerializedName("OrderNo")
    @Expose
    private String orderNo;
    @SerializedName("OrderDate")
    @Expose
    private Object orderDate;
    @SerializedName("DeliveryPersonNo")
    @Expose
    private Object deliveryPersonNo;
    @SerializedName("Item_Id")
    @Expose
    private String itemId;
    @SerializedName("InventoryType")
    @Expose
    private Object inventoryType;
    @SerializedName("SalesType")
    @Expose
    private Object salesType;
    @SerializedName("SalesQty")
    @Expose
    private Object salesQty;
    @SerializedName("CollectionQty")
    @Expose
    private Object collectionQty;
    @SerializedName("CustomerID")
    @Expose
    private Object customerID;
    @SerializedName("SlenderSalesPrice")
    @Expose
    private Double slenderSalesPrice;
    @SerializedName("GasSalesPrice")
    @Expose
    private Double gasSalesPrice;
    @SerializedName("TotalSPrice")
    @Expose
    private Double totalSPrice;
    @SerializedName("TotalItemPrice")
    @Expose
    private Double totalItemPrice;
    @SerializedName("AddCharge")
    @Expose
    private Object addCharge;
    @SerializedName("DiscountAmt")
    @Expose
    private Object discountAmt;
    @SerializedName("NetTotalAmt")
    @Expose
    private Object netTotalAmt;
    @SerializedName("CustomerPreviousAmount")
    @Expose
    private Object customerPreviousAmount;
    @SerializedName("CusTotalBillAmount")
    @Expose
    private Double cusTotalBillAmount;
    @SerializedName("CollectAmount")
    @Expose
    private Double collectAmount;
    @SerializedName("CustCurrentDueAmount")
    @Expose
    private Object custCurrentDueAmount;
    @SerializedName("Remarks")
    @Expose
    private Object remarks;
    @SerializedName("CreateBy")
    @Expose
    private Object createBy;
    @SerializedName("UpdateBy")
    @Expose
    private Object updateBy;
    @SerializedName("UpdateDate")
    @Expose
    private Object updateDate;
    @SerializedName("OrderCompleteStatus")
    @Expose
    private Object orderCompleteStatus;
    @SerializedName("WarehouseID")
    @Expose
    private Object warehouseID;
    @SerializedName("ExpenceAmt")
    @Expose
    private Double expenceAmt;
    @SerializedName("ItemName")
    @Expose
    private String itemName;
    @SerializedName("CustomerName")
    @Expose
    private String customerName;
    @SerializedName("PhoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("CREATE_DATE")
    @Expose
    private String createDate;
    @SerializedName("SumCollectAmount")
    @Expose
    private Double sumCollectAmount;
    @SerializedName("Cylinder")
    @Expose
    private Double cylinder;
    @SerializedName("Gas_Cylinder_Both")
    @Expose
    private Double gasCylinderBoth;
    @SerializedName("Gas_Only")
    @Expose
    private Double gasOnly;
    @SerializedName("AssignQty")
    @Expose
    private Double AssignQty;
    @SerializedName("DeliveryPersonName")
    @Expose
    private String DeliveryPersonName;

    public String getDeliveryPersonName() {
        return DeliveryPersonName;
    }

    public void setDeliveryPersonName(String deliveryPersonName) {
        DeliveryPersonName = deliveryPersonName;
    }

    public Double getId() {
        return id;
    }

    public void setId(Double id) {
        this.id = id;
    }

    public Object getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(Object invoiceNo) {
        this.invoiceNo = invoiceNo;
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

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Object getInventoryType() {
        return inventoryType;
    }

    public void setInventoryType(Object inventoryType) {
        this.inventoryType = inventoryType;
    }

    public Object getSalesType() {
        return salesType;
    }

    public void setSalesType(Object salesType) {
        this.salesType = salesType;
    }

    public Object getSalesQty() {
        return salesQty;
    }

    public void setSalesQty(Object salesQty) {
        this.salesQty = salesQty;
    }

    public Object getCollectionQty() {
        return collectionQty;
    }

    public void setCollectionQty(Object collectionQty) {
        this.collectionQty = collectionQty;
    }

    public Object getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Object customerID) {
        this.customerID = customerID;
    }

    public Double getSlenderSalesPrice() {
        return slenderSalesPrice;
    }

    public void setSlenderSalesPrice(Double slenderSalesPrice) {
        this.slenderSalesPrice = slenderSalesPrice;
    }

    public Double getGasSalesPrice() {
        return gasSalesPrice;
    }

    public void setGasSalesPrice(Double gasSalesPrice) {
        this.gasSalesPrice = gasSalesPrice;
    }

    public Double getTotalSPrice() {
        return totalSPrice;
    }

    public void setTotalSPrice(Double totalSPrice) {
        this.totalSPrice = totalSPrice;
    }

    public Double getTotalItemPrice() {
        return totalItemPrice;
    }

    public void setTotalItemPrice(Double totalItemPrice) {
        this.totalItemPrice = totalItemPrice;
    }

    public Object getAddCharge() {
        return addCharge;
    }

    public void setAddCharge(Object addCharge) {
        this.addCharge = addCharge;
    }

    public Object getDiscountAmt() {
        return discountAmt;
    }

    public void setDiscountAmt(Object discountAmt) {
        this.discountAmt = discountAmt;
    }

    public Object getNetTotalAmt() {
        return netTotalAmt;
    }

    public void setNetTotalAmt(Object netTotalAmt) {
        this.netTotalAmt = netTotalAmt;
    }

    public Object getCustomerPreviousAmount() {
        return customerPreviousAmount;
    }

    public void setCustomerPreviousAmount(Object customerPreviousAmount) {
        this.customerPreviousAmount = customerPreviousAmount;
    }

    public Double getCusTotalBillAmount() {
        return cusTotalBillAmount;
    }

    public void setCusTotalBillAmount(Double cusTotalBillAmount) {
        this.cusTotalBillAmount = cusTotalBillAmount;
    }

    public Double getCollectAmount() {
        return collectAmount;
    }

    public void setCollectAmount(Double collectAmount) {
        this.collectAmount = collectAmount;
    }

    public Object getCustCurrentDueAmount() {
        return custCurrentDueAmount;
    }

    public void setCustCurrentDueAmount(Object custCurrentDueAmount) {
        this.custCurrentDueAmount = custCurrentDueAmount;
    }

    public Object getRemarks() {
        return remarks;
    }

    public void setRemarks(Object remarks) {
        this.remarks = remarks;
    }

    public Object getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Object createBy) {
        this.createBy = createBy;
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

    public Object getOrderCompleteStatus() {
        return orderCompleteStatus;
    }

    public void setOrderCompleteStatus(Object orderCompleteStatus) {
        this.orderCompleteStatus = orderCompleteStatus;
    }

    public Object getWarehouseID() {
        return warehouseID;
    }

    public void setWarehouseID(Object warehouseID) {
        this.warehouseID = warehouseID;
    }

    public Double getExpenceAmt() {
        return expenceAmt;
    }

    public void setExpenceAmt(Double expenceAmt) {
        this.expenceAmt = expenceAmt;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Double getSumCollectAmount() {
        return sumCollectAmount;
    }

    public void setSumCollectAmount(Double sumCollectAmount) {
        this.sumCollectAmount = sumCollectAmount;
    }

    public Double getCylinder() {
        return cylinder;
    }

    public void setCylinder(Double cylinder) {
        this.cylinder = cylinder;
    }

    public Double getGasCylinderBoth() {
        return gasCylinderBoth;
    }

    public void setGasCylinderBoth(Double gasCylinderBoth) {
        this.gasCylinderBoth = gasCylinderBoth;
    }

    public Double getGasOnly() {
        return gasOnly;
    }

    public void setGasOnly(Double gasOnly) {
        this.gasOnly = gasOnly;
    }

    public Double getAssignQty() {
        return AssignQty;
    }

    public void setAssignQty(Double assignQty) {
        AssignQty = assignQty;
    }
}