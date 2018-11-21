package com.idbarcodesolutions.mainactivity.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.idbarcodesolutions.mainactivity.R;
import com.idbarcodesolutions.mainactivity.adapters.WarehouseAdapter;
import com.idbarcodesolutions.mainactivity.models.User;
import com.idbarcodesolutions.mainactivity.models.Warehouse;

import io.realm.Realm;
import io.realm.RealmResults;

public class WarehouseListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Realm realm;
    private RealmResults<Warehouse> warehouseList;

    private FloatingActionButton floatingActionButton;
    private User user;

    private Context context;

    public WarehouseListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
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
        warehouseList = realm.where(Warehouse.class).findAll();
        mRecyclerView = view.findViewById(R.id.recyclerViewFragment);
        mLayoutManager = new LinearLayoutManager(context);
        mAdapter = new WarehouseAdapter(warehouseList, R.layout.cardview_warehouse_item, new WarehouseAdapter.OnItemClickListener() {
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
}
