package com.idbarcodesolutions.mainactivity.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.idbarcodesolutions.mainactivity.R;
import com.idbarcodesolutions.mainactivity.adapters.UserAdapter;
import com.idbarcodesolutions.mainactivity.models.User;

import io.realm.Realm;
import io.realm.RealmResults;

public class UserActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonCreateUser;
    private Realm realm;
    private User currentUser;
    private static int USERNAME_MIN_LENGTH = 6;
    private static int PASSWORD_MIN_LENGTH = 6;
    public final int CREATE_USER = 2;
    public final int SHOW_USER_LIST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Realm instance
        realm = Realm.getDefaultInstance();
        // Get current user
        currentUser = getUser();

        // Get intent in order to get the action
        Intent intent = getIntent();
        int action = intent.getIntExtra("action", 0);
        switch (action) {
            case CREATE_USER:
                createUser();
                break;
            case SHOW_USER_LIST:
                showUserList();
                break;
            default:
                super.onCreate(savedInstanceState);
        }
    }

    private User getUser() {
        Intent intent = getIntent();
        String username = intent.getExtras().get("username").toString();
        return realm.where(User.class).equalTo("username", username).findFirst();
    }

    public void createUser() {
        setContentView(R.layout.create_user);
        // UI elements
        editTextUsername = findViewById(R.id.editTextUsernameNew);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonCreateUser = findViewById(R.id.buttonCreateUser);
        buttonCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();
                if (username.length() >= USERNAME_MIN_LENGTH) {
                    if (password.length() >= PASSWORD_MIN_LENGTH) {
                        User user = new User(username, password, User.USER);
                        saveUser(user);
                        Toast.makeText(UserActivity.this, "User created", Toast.LENGTH_LONG).show();
                        //Intent intent = new Intent(UserActivity.this, MainActivity.class);
                        //startActivity(intent);
                    } else {
                        Toast.makeText(UserActivity.this, "Please enter a 6 characters long password", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(UserActivity.this, "Please enter a 6 characters long username", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void showUserList() {
        setContentView(R.layout.user_list);
        RealmResults<User> userList = realm.where(User.class).findAll();
        RecyclerView mRecyclerView = findViewById(R.id.recyclerViewUserList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        UserAdapter mUserAdapter = new UserAdapter(userList, R.layout.cardview_user_item, new UserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String name, int position) {
                Toast.makeText(UserActivity.this, name, Toast.LENGTH_LONG).show();
            }
        });

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mUserAdapter);
    }

    private void saveUser(final User user) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(user);
            }
        });
    }
}
