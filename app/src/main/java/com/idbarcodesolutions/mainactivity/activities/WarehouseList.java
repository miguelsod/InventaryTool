package com.idbarcodesolutions.mainactivity.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.idbarcodesolutions.mainactivity.R;
import com.idbarcodesolutions.mainactivity.adapters.DrawerAdapter;
import com.idbarcodesolutions.mainactivity.adapters.WarehouseAdapter;
import com.idbarcodesolutions.mainactivity.models.User;
import com.idbarcodesolutions.mainactivity.models.Warehouse;

import io.realm.Realm;
import io.realm.RealmResults;

public class WarehouseList extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Realm realm;
    private RealmResults<Warehouse> warehouseList;

    private FloatingActionButton floatingActionButton;
    private User user;
    private Bundle bundle;

    public final int CREATE_USER = 2;
    public final int SHOW_USER_LIST = 1;

    // Navigation Drawer elements
    private String[] mOptions;
    private DrawerLayout mDrawerLayout;
    private ListView mListViewOptions;
    private ActionBarDrawerToggle mToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.warehouse_list);
        realm = Realm.getDefaultInstance();
        setTitle("Warehouses");

        // Get extras to know who is the user
        Intent intent = getIntent();
        final String username = intent.getStringExtra("username");
        user = realm.where(User.class).equalTo("username", username).findFirst();

        // Get all the warehouseList
        warehouseList = realm.where(Warehouse.class).findAll();

        // Bind RecyclerView and FloatingActionButton
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewWarehouseList);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        // Set LinearLayoutManager
        mLayoutManager = new LinearLayoutManager(this);

        // Initialize adapter
        mAdapter = new WarehouseAdapter(warehouseList, R.layout.cardview_warehouse_item, new WarehouseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String name, int position) {
                Toast.makeText(WarehouseList.this, name, Toast.LENGTH_LONG).show();
            }
        });

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

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
}
