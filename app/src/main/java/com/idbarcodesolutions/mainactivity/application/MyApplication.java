package com.idbarcodesolutions.mainactivity.application;

import com.idbarcodesolutions.mainactivity.models.User;
import com.idbarcodesolutions.mainactivity.models.UserRight;

import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class MyApplication extends android.app.Application {

    public static AtomicInteger userID = new AtomicInteger();
    public static AtomicInteger rightID = new AtomicInteger();

    @Override
    public void onCreate() {
        super.onCreate();
        setUpRealmConfig();

        Realm realm = Realm.getDefaultInstance();
        //rightID = getIdByTable(realm, UserRight.class);
        realm.close();
    }

    public void setUpRealmConfig() {
        Realm.init(getApplicationContext());
        RealmConfiguration configuration = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .name("myrealm.realm")
                .build();
        Realm.setDefaultConfiguration(configuration);
    }

    private <T extends RealmObject> AtomicInteger getIdByTable(Realm realm, Class<T> anyClass) {
        RealmResults<T> results = realm.where(anyClass).findAll();
        return (results.size() > 0) ? new AtomicInteger(results.max("id").intValue()) : new AtomicInteger();
    }
}
