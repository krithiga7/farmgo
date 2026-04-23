package com.farmgo.entity;

import java.math.BigDecimal;

public class Product {
    private int id;
    private String productImage;
    private String productName;
    private BigDecimal productPrice;
    private int productQty;
    private String productCode;
    
    public Product() {}
    
    public Product(int id, String productImage, String productName, BigDecimal productPrice, int productQty, String productCode) {
        this.id = id;
        this.productImage = productImage;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQty = productQty;
        this.productCode = productCode;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getProductImage() {
        return productImage;
    }
    
    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public BigDecimal getProductPrice() {
        return productPrice;
    }
    
    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }
    
    public int getProductQty() {
        return productQty;
    }
    
    public void setProductQty(int productQty) {
        this.productQty = productQty;
    }
    
    public String getProductCode() {
        return productCode;
    }
    
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
    
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", productImage='" + productImage + '\'' +
                ", productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                ", productQty=" + productQty +
                ", productCode='" + productCode + '\'' +
                '}';
    }
}