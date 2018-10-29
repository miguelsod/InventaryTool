package com.idbarcodesolutions.mainactivity.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.idbarcodesolutions.mainactivity.R;
import com.idbarcodesolutions.mainactivity.models.User;
import com.idbarcodesolutions.mainactivity.models.Warehouse;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

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

        // When user clicks on Login button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();
                if (login(username, password)) {
                    nextActivity(username);
                }
            }
        });
    }

    private void nextActivity(String username) {
        User user = realm.where(User.class).equalTo("username", username).findFirst();
        final RealmResults<Warehouse> userWarehouseList = realm.where(Warehouse.class).findAll();
        if(userWarehouseList != null && userWarehouseList.size() <= 0){
            Intent intent = new Intent(LoginActivity.this, CreateWarehouse.class);
            intent.putExtra("username", user.getUsername());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            Intent intent = new Intent(LoginActivity.this, WarehouseList.class);
            intent.putExtra("username", user.getUsername());
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
