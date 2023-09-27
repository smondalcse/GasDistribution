package com.sanatmondal.gasdistribution.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReportNewModel {
    @SerializedName("CollectionHistory")
    @Expose
    private List<ReportModel> collectionHistory = null;
    @SerializedName("CollectionExpense")
    @Expose
    private ReportModel collectionExpense;

    public List<ReportModel> getCollectionHistory() {
        return collectionHistory;
    }

    public void setCollectionHistory(List<ReportModel> collectionHistory) {
        this.collectionHistory = collectionHistory;
    }

    public ReportModel getCollectionExpense() {
        return collectionExpense;
    }

    public void setCollectionExpense(ReportModel collectionExpense) {
        this.collectionExpense = collectionExpense;
    }

}
