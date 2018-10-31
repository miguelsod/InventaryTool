package com.idbarcodesolutions.mainactivity.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.idbarcodesolutions.mainactivity.R;
import com.idbarcodesolutions.mainactivity.models.User;
import com.idbarcodesolutions.mainactivity.models.Warehouse;

import io.realm.RealmResults;

public class WarehouseAdapter extends RecyclerView.Adapter<WarehouseAdapter.ViewHolder> {

    private RealmResults<Warehouse> warehouseList;
    private int layout;
    private OnItemClickListener itemClickListener;

    public WarehouseAdapter(RealmResults<Warehouse> warehouseList, int layout, OnItemClickListener onItemClickListener) {
        this.warehouseList = warehouseList;
        this.layout = layout;
        this.itemClickListener = onItemClickListener;
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
        holder.bind(warehouseList.get(position).getWarehouseID(), itemClickListener);
    }

    @Override
    public int getItemCount() {
        return warehouseList.size();
    }

    // ViewHolder Pattern
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewWarehouseName;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewWarehouseName = itemView.findViewById(R.id.textViewWarehouseName);
        }

        public void bind(final String name, final OnItemClickListener listener) {
            textViewWarehouseName.setText(name);
            // onClickListener for each item
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(name, getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String name, int position);
    }
}
