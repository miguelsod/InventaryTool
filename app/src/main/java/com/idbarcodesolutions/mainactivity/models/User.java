package com.idbarcodesolutions.mainactivity.models;

import android.widget.Toast;

import com.idbarcodesolutions.mainactivity.application.MyApplication;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class User extends RealmObject {
    @PrimaryKey
    private String username;
    @Required
    private String password;
    private int userID;
    private RealmList<Warehouse> warehouseList;
    private UserRight right;

    public final static int ADMIN = 700;
    public final static int USER = 300;

    public User() {
    }

    public User(String username, String password) {
        this.userID = MyApplication.userID.incrementAndGet();
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public RealmList<Warehouse> getWarehouseList() {
        return warehouseList;
    }

    public void setWarehouseList(RealmList<Warehouse> warehouseList) {
        this.warehouseList = warehouseList;
    }

    public UserRight getRight() {
        if (this.right != null) {
            return this.right;
        }
        return null;
    }

    public void setRight(UserRight right) {
        this.right = right;
    }

    public Warehouse createNewWarehouse(String name, User user) {
        // Get the user's right
        UserRight right = user.getRight();

        // Check if user is able to create a new Warehouse
        // Only ADMIN user can create a new Warehouse
        if (right.getId_right() == User.ADMIN) { // TODO: CHANGE IT LATER TO VIP USER!!
            Warehouse warehouse = new Warehouse(name);
            return warehouse;
        }
        return null;
    }
}
