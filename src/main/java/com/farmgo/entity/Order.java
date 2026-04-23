package com.farmgo.entity;

import java.math.BigDecimal;

public class Order {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String pmode;
    private String products;
    private BigDecimal amountPaid;
    
    public Order() {}
    
    public Order(int id, String name, String email, String phone, String address, String pmode, String products, BigDecimal amountPaid) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.pmode = pmode;
        this.products = products;
        this.amountPaid = amountPaid;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getPmode() {
        return pmode;
    }
    
    public void setPmode(String pmode) {
        this.pmode = pmode;
    }
    
    public String getProducts() {
        return products;
    }
    
    public void setProducts(String products) {
        this.products = products;
    }
    
    public BigDecimal getAmountPaid() {
        return amountPaid;
    }
    
    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }
    
    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", pmode='" + pmode + '\'' +
                ", products='" + products + '\'' +
                ", amountPaid=" + amountPaid +
                '}';
    }
}