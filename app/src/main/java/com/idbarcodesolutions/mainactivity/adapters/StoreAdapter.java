package com.idbarcodesolutions.mainactivity.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.idbarcodesolutions.mainactivity.R;
import com.idbarcodesolutions.mainactivity.models.Store;

import io.realm.RealmResults;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder> {

    private RealmResults<Store> storeList;
    private int layout;
    private OnItemClickListener itemClickListener;

    public StoreAdapter(RealmResults<Store> storeList, int layout, OnItemClickListener onItemClickListener) {
        this.storeList = storeList;
        this.layout = layout;
        this.itemClickListener = onItemClickListener;
    }

    // ViewHolder Pattern
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewWarehouseName;

        private ViewHolder(View itemView) {
            super(itemView);
            textViewWarehouseName = itemView.findViewById(R.id.textViewWarehouseName);
        }

        private void bind(final String name, final OnItemClickListener listener) {
            textViewWarehouseName.setText(name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(name, getAdapterPosition());
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate layout and pass it to ViewHolder constructor
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(storeList.get(position).getWarehouseID(), itemClickListener);
    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(String name, int position);
    }
}
