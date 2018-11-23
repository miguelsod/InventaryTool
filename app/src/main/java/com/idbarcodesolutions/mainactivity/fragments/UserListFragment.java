package com.idbarcodesolutions.mainactivity.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.idbarcodesolutions.mainactivity.R;
import com.idbarcodesolutions.mainactivity.adapters.UserAdapter;
import com.idbarcodesolutions.mainactivity.models.User;

import io.realm.Realm;
import io.realm.RealmResults;

public class UserListFragment extends Fragment {

    private Realm realm;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private UserAdapter mUserAdapter;

    private Context context;

    public UserListFragment() {
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
        // Inflate the layout for this fragment
        realm = Realm.getDefaultInstance();
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);

        RealmResults<User> userList = realm.where(User.class).findAll();
        mRecyclerView = view.findViewById(R.id.recyclerViewUserListFragment);
        mLayoutManager = new LinearLayoutManager(context);
        mUserAdapter = new UserAdapter(userList, R.layout.cardview_user_item, new UserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String name, int position) {
                Toast.makeText(context, name, Toast.LENGTH_LONG).show();
            }
        });

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mUserAdapter);

        return view;
    }

}
