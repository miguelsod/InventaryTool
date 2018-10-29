package com.idbarcodesolutions.mainactivity.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.idbarcodesolutions.mainactivity.R;

public class CreateWarehouse extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_warehouse);

        Toast.makeText(CreateWarehouse.this, "Hello, and welcome to the warehouse creation wizard", Toast.LENGTH_LONG).show();
        // TODO: User does not have warehouse
//            final LayoutInflater inflater = getLayoutInflater();
//            final View viewToInflate = inflater.inflate(R.layout.create_new_warehouse, null);
//            setContentView(R.layout.create_new_warehouse);
//            buttonCreateWarehouse = (Button) findViewById(R.id.buttonCreateWarehouse);
//            buttonCreateWarehouse.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    view = viewToInflate;
//                    editTextWarehouseName = (EditText) findViewById(R.id.editTextWarehouseName);
//
//                    if (!editTextWarehouseName.getText().toString().isEmpty()) {
//                        final Warehouse newWarehouse = new Warehouse(editTextWarehouseName.getText().toString());
//                        realm.executeTransaction(new Realm.Transaction() {
//                            @Override
//                            public void execute(Realm realm) {
//                                userWarehouseList.add(newWarehouse);
//                                user.setWarehouseList(userWarehouseList);
//                                realm.copyToRealmOrUpdate(newWarehouse);
//                                realm.copyToRealmOrUpdate(user);
//                            }
//                        });
//
//                        Intent intent = new Intent(LoginActivity.this, WarehouseList.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent);
//                        finish();
//                    } else {
//                        Toast.makeText(LoginActivity.this, "Please, enter a warehouse name", Toast.LENGTH_LONG).show();
//                    }
//                }
//            });
    }
}
