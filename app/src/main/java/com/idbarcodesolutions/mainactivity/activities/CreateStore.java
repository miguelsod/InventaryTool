package com.idbarcodesolutions.mainactivity.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.idbarcodesolutions.mainactivity.R;
import com.idbarcodesolutions.mainactivity.models.Store;
import com.idbarcodesolutions.mainactivity.models.User;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class CreateStore extends AppCompatActivity {
    private EditText editTextWarehouseName;
    private ImageView imageViewIcon;
    private Button buttonCreate;

    private Store newStore;
    private Realm realm;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_warehouse);
        setTitle("Create warehouse");
        realm = Realm.getDefaultInstance();

        imageViewIcon = (ImageView) findViewById(R.id.imageView);
        editTextWarehouseName = (EditText) findViewById(R.id.editTextWarehouseName);
        buttonCreate = findViewById(R.id.buttonCreateWarehouse);
        user = getUser();
        final RealmResults<Store> storeList = realm.where(Store.class).findAll();
        final RealmList<Store> stores = new RealmList<Store>();
        stores.addAll(storeList.subList(0, storeList.size()));

        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String warehouseName = editTextWarehouseName.getText().toString();
                if (!warehouseName.isEmpty()) {
                    newStore = user.createNewWarehouse(warehouseName, user);
                    stores.add(newStore);
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.copyToRealmOrUpdate(newStore);
                            realm.copyToRealmOrUpdate(user);
                        }
                    });
                    Intent intent = new Intent(CreateStore.this, MainActivity.class);
                    intent.putExtra("username", user.getUsername());
                    startActivity(intent);
                } else {
                    Toast.makeText(CreateStore.this, "Please, enter a name.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private User getUser() {
        Intent intent = getIntent();
        String username = intent.getExtras().get("username").toString();
        return realm.where(User.class).equalTo("username", username).findFirst();
    }
}
