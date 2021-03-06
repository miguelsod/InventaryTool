package com.idbarcodesolutions.mainactivity.models;

import io.realm.RealmObject;

public class Product extends RealmObject {
    private String SKU;
    private int quantiy;

    public Product() {
    }

    public Product(String SKU, int quantiy) {
        this.SKU = SKU;
        this.quantiy = quantiy;
    }

    public String getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    public int getQuantiy() {
        return quantiy;
    }

    public void setQuantiy(int quantiy) {
        this.quantiy = quantiy;
    }
}
