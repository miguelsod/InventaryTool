package com.idbarcodesolutions.mainactivity.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.idbarcodesolutions.mainactivity.R;
import com.idbarcodesolutions.mainactivity.models.User;

import io.realm.RealmResults;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private RealmResults<User> userList;
    private int layout;
    private OnItemClickListener itemClickListener;

    public UserAdapter(RealmResults<User> userList, int layout, OnItemClickListener onItemClickListener) {
        this.userList = userList;
        this.layout = layout;
        this.itemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(userList.get(position).getUsername(), itemClickListener);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewUsername;

        private ViewHolder(View itemView) {
            super(itemView);
            textViewUsername = itemView.findViewById(R.id.textViewUsername);
        }

        private void bind(final String username, final OnItemClickListener listener){

            textViewUsername.setText(username);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(username, getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String name, int position);
    }
}
