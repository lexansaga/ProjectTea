package com.capstone.projecttea;

import android.support.v7.widget.RecyclerView;

public class ProductModel {

    String ID;
    int productImage;
    String productName, description,price;
    String[] variations;
    int quantity;

    OrderModel orderModel;
    UserModel userModel;

    public ProductModel(String ID, OrderModel orderModel, UserModel userModel) {
        this.ID = ID;
        this.orderModel = orderModel;
        this.userModel = userModel;
    }


    public OrderModel getOrderModel() {
        return orderModel;
    }

    public void setOrderModel(OrderModel orderModel) {
        this.orderModel = orderModel;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }




    public ProductModel(String ID, int productImage, String productName, String price, String[] variations, int quantity) {
        this.ID = ID;
        this.productImage = productImage;
        this.productName = productName;
        this.price = price;
        this.variations = variations;
        this.quantity = quantity;
    }


    public ProductModel(String ID, int productImage, String productName, String description, String price, String[] variations, int quantity) {
        this.ID = ID;
        this.productImage = productImage;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.variations = variations;
        this.quantity = quantity;
    }

    public ProductModel(int productImage, String productName, String price) {
        this.productImage = productImage;
        this.productName = productName;
        this.price = price;
    }


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public int getProductImage() {
        return productImage;
    }

    public void setProductImage(int productImage) {
        this.productImage = productImage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String[] getVariations() {
        return variations;
    }

    public void setVariations(String[] variations) {
        this.variations = variations;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }







}
