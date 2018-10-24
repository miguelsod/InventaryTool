package com.idbarcodesolutions.mainactivity.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.idbarcodesolutions.mainactivity.R;
import com.idbarcodesolutions.mainactivity.models.User;
import com.idbarcodesolutions.mainactivity.models.Warehouse;

import io.realm.Realm;
import io.realm.RealmList;

public class LoginActivity extends AppCompatActivity {

    //private SharedPreferences prefs;

    // Declare UI elements LOGIN
    private EditText editTextUsername;
    private EditText editTextPassword;
    private Switch switchRemember;
    private Button btnLogin;
    private Realm realm;

    // Declare UI elements Warehouse
    private EditText editTextWarehouseName;
    private EditText editTextWarehouseQty;
    private Button buttonCreateWarehouse;

    // User handled
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Bind UI elements
        bindUI();

        // Get Realm instance
        realm = Realm.getDefaultInstance();

        // Get extras
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");

        editTextUsername.setText(username);

        // When user clicks on Login button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();
                if (login(username, password)) {
                    nextActivity();
                }
            }
        });
    }

    private void nextActivity() {

        final RealmList<Warehouse> userWarehouseList = user.getWarehouseList();

        if (userWarehouseList.size() == 0) {
            // TODO: User does not have warehouse
            final LayoutInflater inflater = getLayoutInflater();
            final View viewToInflate = inflater.inflate(R.layout.create_new_warehouse, null);
            setContentView(R.layout.create_new_warehouse);
            buttonCreateWarehouse = (Button) findViewById(R.id.buttonCreateWarehouse);
            buttonCreateWarehouse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view = viewToInflate;
                    editTextWarehouseName = (EditText) findViewById(R.id.editTextWarehouseName);
                    editTextWarehouseQty = (EditText) findViewById(R.id.editTextWarehouseQty);
                    int qty = Integer.parseInt(editTextWarehouseQty.getText().toString());
                    if(!editTextWarehouseName.getText().toString().isEmpty() && qty >= 0){
                        final Warehouse newWarehouse = new Warehouse(editTextWarehouseName.getText().toString(), qty);
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                userWarehouseList.add(newWarehouse);
                                user.setWarehouseList(userWarehouseList);
                                realm.copyToRealmOrUpdate(newWarehouse);
                                realm.copyToRealmOrUpdate(user);
                            }
                        });

                        Intent intent = new Intent(LoginActivity.this, WarehouseActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Please, enter a warehouse name", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            // TODO: User has a warehouse
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private boolean login(String username, String password) {
        // TODO: Check if user exists
        user = realm.where(User.class).equalTo("username", username).and().equalTo("password", password).findFirst();
        if (user != null) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isEmailValid(String email) {
        return !email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 4;
    }

    private void bindUI() {
        editTextUsername = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        switchRemember = (Switch) findViewById(R.id.switchRemember);
        btnLogin = (Button) findViewById(R.id.buttonLogin);
    }
}
