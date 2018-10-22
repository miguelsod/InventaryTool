package com.idbarcodesolutions.mainactivity.models;

import io.realm.RealmObject;

public class Right extends RealmObject {
    private String rightID;

    public Right(){

    }

    public Right(String rightID) {
        this.rightID = rightID;
    }

    public String getRightID() {
        return rightID;
    }

    public void setRightID(String rightID) {
        this.rightID = rightID;
    }
}
