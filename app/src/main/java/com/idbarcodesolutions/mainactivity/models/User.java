package com.idbarcodesolutions.mainactivity.models;

import android.support.annotation.NonNull;

import com.idbarcodesolutions.mainactivity.application.MyApplication;

import java.io.Serializable;

import javax.annotation.Nullable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class User extends RealmObject implements Serializable {
    @PrimaryKey
    private String username;
    @Required
    private String password;
    @NonNull
    private int userID;
    @Nullable
    private Store store;
    @NonNull
    private UserRight right;

    public static final int ADMIN = 700;
    public static final int USER = 300;

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

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public UserRight getRight() {
        return right;
    }

    public void setRight(UserRight right) {
        this.right = right;
    }

    public Store createNewWarehouse(String warehouseName, User user) {
        // Get the user's right
        UserRight right = user.getRight();

        // Check if user is able to create a new Store
        // Only ADMIN user can create a new Store
        if (right.getId_right() == User.ADMIN) { // TODO: CHANGE IT LATER TO VIP USER!!
            Store store = new Store(warehouseName);
            return store;
        } else {
            return null;
        }
    }

}
