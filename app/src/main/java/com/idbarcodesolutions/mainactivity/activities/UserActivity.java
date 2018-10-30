package com.idbarcodesolutions.mainactivity.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.idbarcodesolutions.mainactivity.R;
import com.idbarcodesolutions.mainactivity.models.User;
import com.idbarcodesolutions.mainactivity.models.UserRight;

import io.realm.Realm;

public class UserActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonCreateUser;
    private Realm realm;
    private User currentUser;
    private static int USERNAME_MIN_LENGTH = 6;
    private static int PASSWORD_MIN_LENGTH = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        // Realm instance
        realm = Realm.getDefaultInstance();
        // Get current user
        currentUser = getUser();
        // UI elements
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonCreateUser = findViewById(R.id.buttonCreateUser);

        buttonCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();
                if (username.length() >= USERNAME_MIN_LENGTH && password.length() >= PASSWORD_MIN_LENGTH) {
                    User user = new User(username, password);
                    UserRight userRight = new UserRight(user.getUserID(), User.USER);
                    user.setRight(userRight);
                    saveUser(user);
                } else {
                    Toast.makeText(UserActivity.this, "Please enter a valid username/password", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private User getUser() {
        Intent intent = getIntent();
        String username = intent.getExtras().get("username").toString();
        return realm.where(User.class).equalTo("username", username).findFirst();
    }

    private void saveUser(final User user){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(user);
            }
        });
    }

}
