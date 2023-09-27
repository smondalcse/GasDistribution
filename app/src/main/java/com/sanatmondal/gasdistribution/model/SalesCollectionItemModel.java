package com.sanatmondal.gasdistribution.model;

import java.io.Serializable;

public class SalesCollectionItemModel implements Serializable {
    ItemModel ItemModel;
    String Qty;
    String ItemType;

    public SalesCollectionItemModel() {
    }

    public ItemModel getItemModel() {
        return ItemModel;
    }

    public void setItemModel(ItemModel itemModel) {
        ItemModel = itemModel;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    public String getItemType() {
        return ItemType;
    }

    public void setItemType(String itemType) {
        ItemType = itemType;
    }
}
