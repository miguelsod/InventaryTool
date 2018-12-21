package com.idbarcodesolutions.mainactivity.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.idbarcodesolutions.mainactivity.R;
import com.idbarcodesolutions.mainactivity.fragments.FragmentChangeListener;
import com.idbarcodesolutions.mainactivity.fragments.ProductListFragment;
import com.idbarcodesolutions.mainactivity.fragments.StoreListFragment;
import com.idbarcodesolutions.mainactivity.fragments.UserListFragment;
import com.idbarcodesolutions.mainactivity.models.User;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity implements FragmentChangeListener {

    private Realm realm;
    public User user;

    public final int CREATE_USER = 2;
    public final int SHOW_USER_LIST = 1;

    // Navigation Drawer elements
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        realm = Realm.getDefaultInstance();
        setTitle("Products");

        // Bind UI elements
        binUIElements();

        // Get the User from extras
        Intent intent = getIntent();
        final String username = intent.getStringExtra("username");
        user = realm.where(User.class).equalTo("username", username).findFirst();


        if (user != null && user.getPassword().equals("password")) {
            showChangePasswordDialog();
        }

        // Show StoreListFragment by default
        replaceFragment(new ProductListFragment());
    }

    private void showChangePasswordDialog() {
        View changePassword = getLayoutInflater().inflate(R.layout.change_password_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Change password")
                .setMessage("Set a new password")
                .setPositiveButton("DONE", null)
                .setView(changePassword);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public User getCurrentUser() {
        return this.user;
    }

    private void binUIElements() {
        // Bind drawer UI elements
        mDrawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(navigationListener);

        // Create toggle and set it as DrawerListener
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);

        // Synchronize drawer state (open|close) to change the toggle button state
        mToggle.syncState();

        // Drawer header elements
        TextView username = (TextView) findViewById(R.id.textViewUsername);
        TextView warehouse = (TextView) findViewById(R.id.textViewWarehouse);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // Open/Close drawer using the toggle button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Listener to know which option has been pressed
    private NavigationView.OnNavigationItemSelectedListener navigationListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.productsItem:
                    setTitle("Products");
                    showOtherFragment(R.id.productsItem);
                    break;
                case R.id.storesItem:
                    // Call to Store Fragment
                    setTitle("Stores");
                    showOtherFragment(item.getItemId());
                    break;
                case R.id.usersItem:
                    // Call User Fragment
                    setTitle("Users");
                    showOtherFragment(item.getItemId());
                    break;
                case R.id.settingsItem:
                    // Call settings fragment
                    setTitle("Settings");
                    showOtherFragment(item.getItemId());
                    break;
                case R.id.logOutItem:
                    // Call logout function
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
            mDrawerLayout.closeDrawer(navigationView);
            return true;
        }
    };

    public void showOtherFragment(int item) {
        Fragment fragment;
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        switch (item) {
            case R.id.productsItem:
                fragment = new ProductListFragment();
                replaceFragment(fragment);
                break;
            case R.id.storesItem:
                // Call to Store Fragment
                fragment = new StoreListFragment();
                replaceFragment(fragment);
                break;
            case R.id.usersItem:
                // Call User Fragment
                fragment = new UserListFragment();
                replaceFragment(fragment);
                break;
            case R.id.settingsItem:
                // Call settings fragment
                break;
            default:
                break;
        }
    }


    @Override
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment, fragment.toString());
        fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.commit();
    }
}
