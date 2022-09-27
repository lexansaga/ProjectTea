package com.capstone.projecttea;

import java.io.Serializable;

public class ProductModel implements Serializable {

    String ID;
    int productImage;
    String imageLink;
    String productName;
    String description;
    String price;


    String addOns;
    String[] variations;

    String variation;
    int quantity;
    boolean isChecked;
    OrderModel orderModel;
    UserModel userModel;

    public ProductModel(){}


    public ProductModel(String ID, OrderModel orderModel, UserModel userModel) {
        this.ID = ID;
        this.orderModel = orderModel;
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


    public ProductModel(String ID, String imageLink, String productName, String price) {
        this.ID = ID;
        this.imageLink = imageLink;
        this.productName = productName;
        this.price = price;
    }


    public ProductModel(String ID, String imageLink, String productName, String price, String variation, int quantity) {
        this.ID = ID;
        this.imageLink = imageLink;
        this.productName = productName;
        this.price = price;
        this.variation = variation;
        this.quantity = quantity;}


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

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
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

    public String getVariation() {
        return variation;
    }

    public void setVariation(String variation) {
        this.variation = variation;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
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

    public String getAddOns() {
        return addOns;
    }

    public void setAddOns(String addOns) {
        this.addOns = addOns;
    }






}
