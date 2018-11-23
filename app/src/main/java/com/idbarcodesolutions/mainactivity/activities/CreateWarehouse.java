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
import com.idbarcodesolutions.mainactivity.models.User;
import com.idbarcodesolutions.mainactivity.models.Warehouse;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class CreateWarehouse extends AppCompatActivity {
    private EditText editTextWarehouseName;
    private ImageView imageViewIcon;
    private Button buttonCreate;

    private Warehouse newWarehouse;
    private Realm realm;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_warehouse);
        setTitle("Create warehouse");
        realm = Realm.getDefaultInstance();

        editTextWarehouseName = (EditText) findViewById(R.id.editTextWarehouseName);
        imageViewIcon = (ImageView) findViewById(R.id.imageView);
        buttonCreate = (Button) findViewById(R.id.buttonCreateWarehouse);
        user = getUser();
        final RealmResults<Warehouse> warehouseList = realm.where(Warehouse.class).findAll();
        final RealmList<Warehouse> warehouses = new RealmList<Warehouse>();
        warehouses.addAll(warehouseList.subList(0, warehouseList.size()));

        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String warehouseName = editTextWarehouseName.getText().toString();
                if (!warehouseName.isEmpty()) {
                    newWarehouse = user.createNewWarehouse(warehouseName, user);
                    warehouses.add(newWarehouse);
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.copyToRealmOrUpdate(newWarehouse);
                            realm.copyToRealmOrUpdate(user);
                        }
                    });

                    Intent intent = new Intent(CreateWarehouse.this, WarehouseList.class);
                    intent.putExtra("username", user.getUsername());
                    startActivity(intent);
                } else {
                    Toast.makeText(CreateWarehouse.this, "Please, enter a name.", Toast.LENGTH_LONG).show();
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
