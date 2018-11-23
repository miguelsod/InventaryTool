package com.idbarcodesolutions.mainactivity.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.idbarcodesolutions.mainactivity.R;
import com.idbarcodesolutions.mainactivity.fragments.UserListFragment;
import com.idbarcodesolutions.mainactivity.fragments.WarehouseListFragment;
import com.idbarcodesolutions.mainactivity.models.User;

import io.realm.Realm;

public class WarehouseList extends AppCompatActivity implements WarehouseListFragment.FragmentChangeListener {

    private Realm realm;

    private FloatingActionButton floatingActionButton;
    private User user;

    public final int CREATE_USER = 2;
    public final int SHOW_USER_LIST = 1;

    // Navigation Drawer elements
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.warehouse_list);
        realm = Realm.getDefaultInstance();
        setTitle("Warehouses");

        // Bind UI elements
        binUIElements();

        // Get the User from extras
        Intent intent = getIntent();
        final String username = intent.getStringExtra("username");
        user = realm.where(User.class).equalTo("username", username).findFirst();

        // Show WarehouseListFragment by default
        replaceFragment(new WarehouseListFragment());

        // Floating action button functionality
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(WarehouseList.this, user.getUsername(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(WarehouseList.this, UserActivity.class);
                intent.putExtra("username", user.getUsername());
                intent.putExtra("action", CREATE_USER);
                startActivity(intent);
            }
        });
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        floatingActionButton = findViewById(R.id.floatingActionButton);
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
                case R.id.warehousesItem:
                    // TODO: Call to Warehouse Fragment
                    setTitle("Warehouses");
                    showOtherFragment(item.getItemId());
                    Toast.makeText(WarehouseList.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                    break;
                case R.id.usersItem:
                    // TODO: Call User Fragment
                    setTitle("Users");
                    showOtherFragment(item.getItemId());
                    Toast.makeText(WarehouseList.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                    break;
                case R.id.settingsItem:
                    // TODO: Call settings fragment
                    setTitle("Settings");
                    showOtherFragment(item.getItemId());
                    Toast.makeText(WarehouseList.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                    break;
                case R.id.logOutItem:
                    // TODO: Call logout function
                    Intent intent = new Intent(WarehouseList.this, LoginActivity.class);
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

    public void showOtherFragment(int item){
        Fragment fragment;
        switch (item){
            case R.id.warehousesItem:
                // TODO: Call to Warehouse Fragment
                fragment = new WarehouseListFragment();
                replaceFragment(fragment);
                break;
            case R.id.usersItem:
                // TODO: Call User Fragment
                fragment = new UserListFragment();
                replaceFragment(fragment);
                break;
            case R.id.settingsItem:
                // TODO: Call settings fragment
                break;
            default:
                break;
        }
    }

    @Override
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment, fragment.toString());
        fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.commit();
    }
}
