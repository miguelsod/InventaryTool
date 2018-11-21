package com.idbarcodesolutions.mainactivity.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.idbarcodesolutions.mainactivity.R;
import com.idbarcodesolutions.mainactivity.models.User;
import com.idbarcodesolutions.mainactivity.models.Warehouse;

import io.realm.Realm;
import io.realm.RealmResults;

public class WarehouseList extends AppCompatActivity {

//    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Realm realm;
    private RealmResults<Warehouse> warehouseList;

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
                    Toast.makeText(WarehouseList.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                    break;
                case R.id.usersItem:
                    // TODO: Call User Fragment
                    setTitle("Users");
                    Toast.makeText(WarehouseList.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                    break;
                case R.id.settingsItem:
                    // TODO: Call settings fragment
                    setTitle("Settings");
                    Toast.makeText(WarehouseList.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                    break;
                case R.id.logOutItem:
                    // TODO: Call logout function
                    setTitle("Logout");
                    Toast.makeText(WarehouseList.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            mDrawerLayout.closeDrawer(navigationView);
            return true;
        }
    };

}
