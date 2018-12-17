package com.idbarcodesolutions.mainactivity.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Store extends RealmObject {
    @PrimaryKey
    private String warehouseID;
    private int qty = 0;

    public Store() {
    }

    public Store(String warehouseID) {
        this.warehouseID = warehouseID;
    }

    public String getWarehouseID() {
        return warehouseID;
    }

    public void setWarehouseID(String warehouseID) {
        this.warehouseID = warehouseID;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
