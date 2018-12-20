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
import io.realm.RealmResults;

public class InitialSetup extends AppCompatActivity {

    // Get UI elements
    private EditText editTextPassword;
    private Intent intent;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_setup);
        setTitle("Setup");

        // Get UI reference
        editTextPassword = findViewById(R.id.editTextPassword);
        Button buttonNext = findViewById(R.id.buttonNextSetup);

        // Get Realm instance
        final Realm realm = Realm.getDefaultInstance();
        // Get user list
        final RealmResults<User> userList = realm.where(User.class).findAll();

        // Check if any user already exists
        if (userList.size() > 0) {
            // Send user to login activity
            intent = new Intent(InitialSetup.this, LoginActivity.class);
            startActivity(intent);
        } else {
            // Create if user does not exist.
            buttonNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Validate password field
                    String password = editTextPassword.getText().toString();

                    if (!password.isEmpty()) {
                        // Create new ADMIN user
                        user = new User("admin", password);
                        UserRight adminRight = new UserRight(user.getUserID(), User.ADMIN);
                        user.setRight(adminRight);
                        // Pass it to Realm db
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.copyToRealm(user);
                                intent = new Intent(InitialSetup.this, LoginActivity.class);
                                intent.putExtra("username", user.getUsername());
                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        });
                    } else {
                        Toast.makeText(InitialSetup.this, "Please, set your password", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
