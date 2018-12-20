package com.idbarcodesolutions.mainactivity.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.idbarcodesolutions.mainactivity.R;
import com.idbarcodesolutions.mainactivity.models.Store;
import com.idbarcodesolutions.mainactivity.models.User;

import io.realm.Realm;
import io.realm.RealmResults;

public class LoginActivity extends AppCompatActivity {

    //private SharedPreferences prefs;

    // Declare UI elements LOGIN
    private EditText editTextUsername;
    private EditText editTextPassword;
    private Switch switchRemember;
    private Button btnLogin;
    private Realm realm;

    // User handled
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");
        // Bind UI elements
        bindUIElements();

        // TODO: Shared preferences
        // Save User's session if rememberSwitch has been pressed.

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
        Intent intent;
        User user = realm.where(User.class).equalTo("username", username).findFirst();
        final RealmResults<Store> userStoreList = realm.where(Store.class).findAll();
        if (userStoreList != null && userStoreList.size() <= 0) {
            intent = new Intent(LoginActivity.this, CreateStore.class);
            intent.putExtra("username", user.getUsername());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            // TODO: Send User object to MainActivity
            intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("username", user.getUsername());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private boolean login(String username, String password) {
        // Find username in database
        user = realm.where(User.class)
                .equalTo("username", username)
                .and()
                .equalTo("password", password)
                .findFirst();

        // User exists if it's different by null
        if (user != null) {
            return true;
        } else {
            Toast.makeText(LoginActivity.this, "Invalid username or password.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void bindUIElements() {
        editTextUsername = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        switchRemember = findViewById(R.id.switchRemember);
        btnLogin = findViewById(R.id.buttonLogin);
    }

    // TODO: DELETE COMMENTS
//    private boolean isEmailValid(String email) {
//        return !email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches();
//    }
//
//    private boolean isPasswordValid(String password) {
//        return password.length() >= 4;
//    }


}
