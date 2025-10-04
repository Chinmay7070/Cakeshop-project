package com.nt.CakeService;

public class CartDetail {
    private Long cartId;
    private Long cakeId;
    private int quantity;
    private String cakeName;
    private double cakePrice;
    private String cakeImage_path;

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Long getCakeId() {
        return cakeId;
    }

    public void setCakeId(Long cakeId) {
        this.cakeId = cakeId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCakeName() {
        return cakeName;
    }

    public void setCakeName(String cakeName) {
        this.cakeName = cakeName;
    }

    public double getCakePrice() {
        return cakePrice;
    }

    public void setCakePrice(double cakePrice) {
        this.cakePrice = cakePrice;
    }

    public String getCakeImage_path() {
        return cakeImage_path;
    }

    public void setCakeImage_path(String cakeImagePath) {
        this.cakeImage_path = cakeImage_path;
    }
}