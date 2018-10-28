package com.idbarcodesolutions.mainactivity.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.idbarcodesolutions.mainactivity.R;
import com.idbarcodesolutions.mainactivity.models.Right;
import com.idbarcodesolutions.mainactivity.models.User;
import com.idbarcodesolutions.mainactivity.models.UserRight;
import com.idbarcodesolutions.mainactivity.models.Warehouse;

import io.realm.Realm;
import io.realm.RealmResults;

public class InitialSetup extends AppCompatActivity {

    // Get UI elements
    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonNext;

    // Get user list
    private RealmResults<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_setup);

        // Get UI reference
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonNext = (Button) findViewById(R.id.buttonNextSetup);

        // Get Realm instance
        final Realm realm = Realm.getDefaultInstance();
        userList = realm.where(User.class).findAll();

        // Check if any user already exists
        if (userList.size() > 0) {
            // TODO: USER EXISTS!
            // Send user to log in
            Intent intent = new Intent(InitialSetup.this, LoginActivity.class);
            startActivity(intent);
        } else {
            // TODO: CREATE USER IF DOESN'T EXISTS
            // When button is clicked...
            buttonNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Validate password field
                    String password = editTextPassword.getText().toString();
                    if (!password.isEmpty()) {
                        // Create new ADMIN user
                        final User user = new User("admin", password);
                        user.setRight(new UserRight(user.getUserID(), User.ADMIN));
                        // Pass it to Realm db
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.copyToRealmOrUpdate(user);
                                Intent intent = new Intent(InitialSetup.this, LoginActivity.class);
                                intent.putExtra("username", "admin");
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
