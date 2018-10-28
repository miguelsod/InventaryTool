package com.idbarcodesolutions.mainactivity.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Right extends RealmObject {
    @PrimaryKey
    private int rightID;

    public Right(){ }

    public Right(int rightID) {
        this.rightID = rightID;
    }

    public int getRightID() {
        return rightID;
    }
}
