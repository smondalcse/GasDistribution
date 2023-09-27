package com.sanatmondal.gasdistribution.network;

public class ApiURL {
    String url = "http://173.208.142.72/delowarenterprise/gasapi/version1/";

    public String getLoginURL(String UserID, String Pass){
        String method = "login?";
        String params = "UserID=" + UserID + "&Password=" + Pass;

        return (url + method + params);
    }

    public String getAllCustomerList(){
        String method = "allcustomerlist?";
        return (url + method);
    }

    public String getALLExchangerList(){
        String method = "ALLExchangerList?";
        return (url + method);
    }

    public String getWarehouseList(){
        String method = "WarehouseList?";
        return (url + method);
    }

    public String getOrderItemList(String Orderno){
        String method = "orderitemlist?";
        String params = "orderno=" + Orderno;
        return (url + method + params);
    }

    public String getAllItemList(){
        String method = "allitemlist";
        return (url + method);
    }

    public String getTempOrderSales(String OrderNo){
        String method = "SelectOrderbyOrderNoForSalesCollection/" + OrderNo;
        return (url + method);
    }

    public String saveFinally(){
        String method = "FinallySave/";
        return (url + method);
    }

    public String FinallySaveExchange(){
        String method = "FinallySaveExchange/";
        return (url + method);
    }

    public String addToTempTableOrderSales(){
        String method = "AddToTempTableOrderSales/";
        return (url + method);
    }

    public String AddToTEMPReplacement(){
        String method = "AddToTEMPReplacement/";
        return (url + method);
    }

    public String addToTempTableOrderCollect(){
        String method = "AddToTempTableOrderCollect/";
        return (url + method);
    }

    public String deleteTempDataUserWise(String OrderNo){
        String method = "DeleteTempDataUserWise/" + OrderNo;
        return (url + method);
    }

    public String DeleteTempDataUserWiseSINGLE(String UserID, String ItemID,String SalesType){
        String method = "DeleteTempDataUserWiseSINGLE?";
        String params = "UserID=" + UserID + "&ItemID=" + ItemID + "&SalesType=" + SalesType;
        return (url + method + params);
    }

    public String DeleteTempDataUserWiseSINGLEExchange(String UserID, String ItemID,String Type){
        String method = "DeleteTempDataUserWiseSINGLEExchange?";
        String params = "UserID=" + UserID + "&ItemID=" + ItemID + "&Type=" + Type;
        return (url + method + params);
    }

    public String customerEntry(){
        String method = "CustomerEntry/";
        return (url + method);
    }

    public String customerPaymentAdd(){
        String method = "CustomerPaymentAdd/";
        return (url + method);
    }

    public String getALLExpenseHEADList(){
        String method = "ALLExpenseHEADList/";
        return (url + method);
    }

    public String dailyExpenseInsert(){
        String method = "DailyExpenseInsert/";
        return (url + method);
    }

    public String getOrderWiseReportList(String OrderNo){
        String method = "OrderWiseReportList?";
        String params = "OrderNo=" + OrderNo;
        return (url + method + params);
    }
}
