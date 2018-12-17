package com.idbarcodesolutions.mainactivity.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.idbarcodesolutions.mainactivity.R;
import com.idbarcodesolutions.mainactivity.adapters.StoreAdapter;
import com.idbarcodesolutions.mainactivity.models.Store;

import io.realm.Realm;
import io.realm.RealmResults;

public class StoreListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Realm realm;
    private RealmResults<Store> storeList;

    private Context context;

    public StoreListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.store_list_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addItemStoreList:
                createWarehouse();
                Toast.makeText(context, "Store added", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        realm = Realm.getDefaultInstance();
        View view = inflater.inflate(R.layout.fragment_warehouse_list, container, false);
        bindUIElements(view);

        return view;
    }

    public void bindUIElements(View view) {

        storeList = realm.where(Store.class).findAll();
        mRecyclerView = view.findViewById(R.id.recyclerViewFragment);
        mLayoutManager = new LinearLayoutManager(context);

        mAdapter = new StoreAdapter(storeList, R.layout.cardview_warehouse_item, new StoreAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String name, int position) {
                Toast.makeText(context, name, Toast.LENGTH_LONG).show();
            }
        });

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void createWarehouse() {

    }
}
