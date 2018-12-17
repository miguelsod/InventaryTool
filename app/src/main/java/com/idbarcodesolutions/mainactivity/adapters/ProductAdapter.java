package com.idbarcodesolutions.mainactivity.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.idbarcodesolutions.mainactivity.R;
import com.idbarcodesolutions.mainactivity.models.Product;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> implements Filterable {
    private Realm realm = Realm.getDefaultInstance();

    private RealmList<Product> mProductList;
    private List<Product> exampleListFull;

    private int layout;
    private OnItemClickListener itemClickListener;

    public ProductAdapter(RealmList<Product> mProductList, int layout, OnItemClickListener onItemClickListener) {
        this.mProductList = mProductList;
        exampleListFull = new ArrayList<>(mProductList);
        this.layout = layout;
        this.itemClickListener = onItemClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewSku;
        TextView textViewQty;

        private ViewHolder(View itemView) {
            super(itemView);
            textViewSku = itemView.findViewById(R.id.skuTextView);
            textViewQty = itemView.findViewById(R.id.textViewQty);
        }

        private void bind(final String sku, final int qty, final OnItemClickListener listener) {
            textViewSku.setText(sku);
            textViewQty.setText(String.valueOf(qty));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(sku, qty, getAdapterPosition());
                }
            });
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mProductList.get(position).getSku(), mProductList.get(position).getQuantity(), itemClickListener);
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(String sku, int qty, int position);
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Product> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0) {
                filteredList.addAll(exampleListFull);
            } else {
                String filteredPattern  = constraint.toString().toLowerCase().trim();

                for (Product product : exampleListFull) {
                    if (product.getSku().toLowerCase().contains(filteredPattern)) {
                        filteredList.add(product);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mProductList.clear();
            mProductList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
