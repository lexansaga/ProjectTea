package com.capstone.projecttea;

public class OrderModel {
    String OrderNumber,OrderID,Subtotal,ShippingFee,GrandTotal,Status;

    public OrderModel(){

    }
    public OrderModel(String orderNumber, String orderID, String subtotal, String shippingFee, String grandTotal, String status) {
        OrderNumber = orderNumber;
        OrderID = orderID;
        Subtotal = subtotal;
        ShippingFee = shippingFee;
        GrandTotal = grandTotal;
        Status = status;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }





    public String getOrderNumber() {
        return OrderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        OrderNumber = orderNumber;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getSubtotal() {
        return Subtotal;
    }

    public void setSubtotal(String subtotal) {
        Subtotal = subtotal;
    }

    public String getShippingFee() {
        return ShippingFee;
    }

    public void setShippingFee(String shippingFee) {
        ShippingFee = shippingFee;
    }

    public String getGrandTotal() {
        return GrandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        GrandTotal = grandTotal;
    }

}
