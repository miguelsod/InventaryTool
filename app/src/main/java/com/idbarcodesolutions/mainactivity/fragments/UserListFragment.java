package com.idbarcodesolutions.mainactivity.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.idbarcodesolutions.mainactivity.R;
import com.idbarcodesolutions.mainactivity.activities.MainActivity;
import com.idbarcodesolutions.mainactivity.adapters.UserAdapter;
import com.idbarcodesolutions.mainactivity.models.User;
import com.idbarcodesolutions.mainactivity.models.UserRight;

import io.realm.Realm;
import io.realm.RealmResults;

public class UserListFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private Realm realm;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private UserAdapter mUserAdapter;
    private User currentUser;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.user_list_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addItemUserListMenu:
                // TODO: Call Add User Dialog
                if (currentUser.getRight().getId_right() == User.ADMIN) {
                    openAddDialog();
                } else {
                    Toast.makeText(context, "You don't have permission to add users.", Toast.LENGTH_SHORT).show();
                }
                // TODO: Validate if current user has Admin privileges.
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openAddDialog() {
        View addDialogView = getActivity().getLayoutInflater().inflate(R.layout.add_user_dialog, null);

        final EditText editTextUsernameDialog = addDialogView.findViewById(R.id.editTextUsernameDialog);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.rights_array, android.R.layout.simple_spinner_item);
        final Spinner spinner = addDialogView.findViewById(R.id.right_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setOnItemSelectedListener(this);
        spinner.setAdapter(adapter);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Add user")
                .setMessage("Enter user information")
                .setNegativeButton("CANCEL", null)
                .setPositiveButton("DONE", null);
        builder.setView(addDialogView);

        final AlertDialog dialog = builder.create();


        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button b = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String username = editTextUsernameDialog.getText().toString();
                        String right = spinner.getSelectedItem().toString();
                        User user;
                        if (!username.isEmpty() && username.length() > 4) {
                            if (right.equals("Administrator")) {
                                user = new User(username, "password");
                                user.setRight(new UserRight(user.getUserID(), User.ADMIN));
                                saveOrUpdate(user);
                            } else if (right.equals("User")){

                                user = new User(username, "password");
                                user.setRight(new UserRight(user.getUserID(), User.USER));
                                saveOrUpdate(user);
                            }
                            Toast.makeText(context, "User created.", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(context, "Please, enter a valid username", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        dialog.show();
    }

    public void saveOrUpdate(final User user) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(user);
            }
        });
        mUserAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        realm = Realm.getDefaultInstance();
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);

        RealmResults<User> userList = realm.where(User.class).findAll();
        currentUser = ((MainActivity)getActivity()).getCurrentUser();
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
