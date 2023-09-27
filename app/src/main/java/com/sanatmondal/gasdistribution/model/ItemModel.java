
package com.sanatmondal.gasdistribution.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemModel {

    @SerializedName("Item_Id")
    @Expose
    private String itemId;
    @SerializedName("ItemName")
    @Expose
    private String itemName;
    @SerializedName("CategoryID")
    @Expose
    private Object categoryID;
    @SerializedName("BrandID")
    @Expose
    private Object brandID;
    @SerializedName("SizeID")
    @Expose
    private Object sizeID;
    @SerializedName("UomID")
    @Expose
    private Object uomID;
    @SerializedName("SupplierCompanyID")
    @Expose
    private Object supplierCompanyID;
    @SerializedName("ProductBarcode")
    @Expose
    private Object productBarcode;
    @SerializedName("BarCode1")
    @Expose
    private Object barCode1;
    @SerializedName("Barcode2")
    @Expose
    private Object barcode2;
    @SerializedName("SelenderPurchasePrice")
    @Expose
    private Object selenderPurchasePrice;
    @SerializedName("GassPurchasePrice")
    @Expose
    private Object gassPurchasePrice;
    @SerializedName("SlenderSalesPrice")
    @Expose
    private Object slenderSalesPrice;
    @SerializedName("GasSalesPrice")
    @Expose
    private Object gasSalesPrice;
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
    @SerializedName("OpeningStock")
    @Expose
    private Object openingStock;
    @SerializedName("MinimumAlertQty")
    @Expose
    private Object minimumAlertQty;
    @SerializedName("Brand")
    @Expose
    private Object brand;
    @SerializedName("Category")
    @Expose
    private Object category;
    @SerializedName("Item1")
    @Expose
    private Object item1;
    @SerializedName("Item2")
    @Expose
    private Object item2;
    @SerializedName("Size")
    @Expose
    private Object size;
    @SerializedName("SupplierCompany")
    @Expose
    private Object supplierCompany;
    @SerializedName("UOM")
    @Expose
    private Object uom;
    @SerializedName("ItemReceives")
    @Expose
    private List<Object> itemReceives = null;
    @SerializedName("CustomerOrders")
    @Expose
    private List<Object> customerOrders = null;
    @SerializedName("CategoryName")
    @Expose
    private Object categoryName;
    @SerializedName("BrandName")
    @Expose
    private Object brandName;
    @SerializedName("ModelName")
    @Expose
    private Object modelName;
    @SerializedName("SizeName")
    @Expose
    private Object sizeName;
    @SerializedName("ColourName")
    @Expose
    private Object colourName;
    @SerializedName("UOMName")
    @Expose
    private Object uOMName;
    @SerializedName("CompanyName")
    @Expose
    private Object companyName;
    @SerializedName("ItemInfo")
    @Expose
    private Object itemInfo;
    @SerializedName("InvQty")
    @Expose
    private Double invQty;
    @SerializedName("InvType")
    @Expose
    private Object invType;
    @SerializedName("IsLossSale")
    @Expose
    private Object isLossSale;
    @SerializedName("MinimumDiscountPersent")
    @Expose
    private Object minimumDiscountPersent;
    @SerializedName("AllowDisPersentEdit")
    @Expose
    private Object allowDisPersentEdit;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Object getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Object categoryID) {
        this.categoryID = categoryID;
    }

    public Object getBrandID() {
        return brandID;
    }

    public void setBrandID(Object brandID) {
        this.brandID = brandID;
    }

    public Object getSizeID() {
        return sizeID;
    }

    public void setSizeID(Object sizeID) {
        this.sizeID = sizeID;
    }

    public Object getUomID() {
        return uomID;
    }

    public void setUomID(Object uomID) {
        this.uomID = uomID;
    }

    public Object getSupplierCompanyID() {
        return supplierCompanyID;
    }

    public void setSupplierCompanyID(Object supplierCompanyID) {
        this.supplierCompanyID = supplierCompanyID;
    }

    public Object getProductBarcode() {
        return productBarcode;
    }

    public void setProductBarcode(Object productBarcode) {
        this.productBarcode = productBarcode;
    }

    public Object getBarCode1() {
        return barCode1;
    }

    public void setBarCode1(Object barCode1) {
        this.barCode1 = barCode1;
    }

    public Object getBarcode2() {
        return barcode2;
    }

    public void setBarcode2(Object barcode2) {
        this.barcode2 = barcode2;
    }

    public Object getSelenderPurchasePrice() {
        return selenderPurchasePrice;
    }

    public void setSelenderPurchasePrice(Object selenderPurchasePrice) {
        this.selenderPurchasePrice = selenderPurchasePrice;
    }

    public Object getGassPurchasePrice() {
        return gassPurchasePrice;
    }

    public void setGassPurchasePrice(Object gassPurchasePrice) {
        this.gassPurchasePrice = gassPurchasePrice;
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

    public Object getOpeningStock() {
        return openingStock;
    }

    public void setOpeningStock(Object openingStock) {
        this.openingStock = openingStock;
    }

    public Object getMinimumAlertQty() {
        return minimumAlertQty;
    }

    public void setMinimumAlertQty(Object minimumAlertQty) {
        this.minimumAlertQty = minimumAlertQty;
    }

    public Object getBrand() {
        return brand;
    }

    public void setBrand(Object brand) {
        this.brand = brand;
    }

    public Object getCategory() {
        return category;
    }

    public void setCategory(Object category) {
        this.category = category;
    }

    public Object getItem1() {
        return item1;
    }

    public void setItem1(Object item1) {
        this.item1 = item1;
    }

    public Object getItem2() {
        return item2;
    }

    public void setItem2(Object item2) {
        this.item2 = item2;
    }

    public Object getSize() {
        return size;
    }

    public void setSize(Object size) {
        this.size = size;
    }

    public Object getSupplierCompany() {
        return supplierCompany;
    }

    public void setSupplierCompany(Object supplierCompany) {
        this.supplierCompany = supplierCompany;
    }

    public Object getUom() {
        return uom;
    }

    public void setUom(Object uom) {
        this.uom = uom;
    }

    public List<Object> getItemReceives() {
        return itemReceives;
    }

    public void setItemReceives(List<Object> itemReceives) {
        this.itemReceives = itemReceives;
    }

    public List<Object> getCustomerOrders() {
        return customerOrders;
    }

    public void setCustomerOrders(List<Object> customerOrders) {
        this.customerOrders = customerOrders;
    }

    public Object getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(Object categoryName) {
        this.categoryName = categoryName;
    }

    public Object getBrandName() {
        return brandName;
    }

    public void setBrandName(Object brandName) {
        this.brandName = brandName;
    }

    public Object getModelName() {
        return modelName;
    }

    public void setModelName(Object modelName) {
        this.modelName = modelName;
    }

    public Object getSizeName() {
        return sizeName;
    }

    public void setSizeName(Object sizeName) {
        this.sizeName = sizeName;
    }

    public Object getColourName() {
        return colourName;
    }

    public void setColourName(Object colourName) {
        this.colourName = colourName;
    }

    public Object getUOMName() {
        return uOMName;
    }

    public void setUOMName(Object uOMName) {
        this.uOMName = uOMName;
    }

    public Object getCompanyName() {
        return companyName;
    }

    public void setCompanyName(Object companyName) {
        this.companyName = companyName;
    }

    public Object getItemInfo() {
        return itemInfo;
    }

    public void setItemInfo(Object itemInfo) {
        this.itemInfo = itemInfo;
    }

    public Double getInvQty() {
        return invQty;
    }

    public void setInvQty(Double invQty) {
        this.invQty = invQty;
    }

    public Object getInvType() {
        return invType;
    }

    public void setInvType(Object invType) {
        this.invType = invType;
    }

    public Object getIsLossSale() {
        return isLossSale;
    }

    public void setIsLossSale(Object isLossSale) {
        this.isLossSale = isLossSale;
    }

    public Object getMinimumDiscountPersent() {
        return minimumDiscountPersent;
    }

    public void setMinimumDiscountPersent(Object minimumDiscountPersent) {
        this.minimumDiscountPersent = minimumDiscountPersent;
    }

    public Object getAllowDisPersentEdit() {
        return allowDisPersentEdit;
    }

    public void setAllowDisPersentEdit(Object allowDisPersentEdit) {
        this.allowDisPersentEdit = allowDisPersentEdit;
    }

}
