package com.farmgo.entity;

import java.math.BigDecimal;

public class Cart {
    private int id;
    private String productName;
    private BigDecimal productPrice;
    private String productImage;
    private int qty;
    private BigDecimal totalPrice;
    private String productCode;
    
    public Cart() {}
    
    public Cart(int id, String productName, BigDecimal productPrice, String productImage, int qty, BigDecimal totalPrice, String productCode) {
        this.id = id;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImage = productImage;
        this.qty = qty;
        this.totalPrice = totalPrice;
        this.productCode = productCode;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
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
    
    public String getProductImage() {
        return productImage;
    }
    
    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }
    
    public int getQty() {
        return qty;
    }
    
    public void setQty(int qty) {
        this.qty = qty;
    }
    
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
    
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    public String getProductCode() {
        return productCode;
    }
    
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
    
    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                ", productImage='" + productImage + '\'' +
                ", qty=" + qty +
                ", totalPrice=" + totalPrice +
                ", productCode='" + productCode + '\'' +
                '}';
    }
}