package com.idbarcodesolutions.mainactivity.models;

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

    public User(String username, String password, UserRight right) {
        this.userID = MyApplication.userID.incrementAndGet();
        this.username = username;
        this.password = password;
        this.right = right;
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

    public Warehouse createNewWarehouse(String warehouseName, User user) {
        // Get the user's right
        UserRight right = user.getRight();

        // Check if user is able to create a new Warehouse
        // Only ADMIN user can create a new Warehouse
        if (right.getId_right() == User.ADMIN) { // TODO: CHANGE IT LATER TO VIP USER!!
            Warehouse warehouse = new Warehouse(warehouseName);
            return warehouse;
        } else {
            return null;
        }
    }

    public User createNewUser(String name, String password, Warehouse warehouse, User parentUser) {
        // Check if user is able to create a new user
        if (parentUser.getRight().getId_right() == User.ADMIN) {
            User newUser = new User(name, password);
            RealmList<Warehouse> warehouseList = parentUser.getWarehouseList(); // Add the Administrator's default warehouse
            warehouseList.add(warehouse);
            return newUser;
        } else {
            return null;
        }
    }
}
