package com.idbarcodesolutions.mainactivity.models;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class UserRight extends RealmObject implements Serializable {

    @PrimaryKey
    private int id_user;
    private int id_right;

    public UserRight(){ }

    public UserRight(int id_user, int id_right) {
        this.id_user = id_user;
        this.id_right = id_right;
    }

    public int getId_user() {
        return id_user;
    }

    public int getId_right() {
        return id_right;
    }
}
