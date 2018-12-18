package com.idbarcodesolutions.mainactivity.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.idbarcodesolutions.mainactivity.R;
import com.idbarcodesolutions.mainactivity.adapters.ProductAdapter;
import com.idbarcodesolutions.mainactivity.models.Product;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductListFragment extends Fragment implements FragmentChangeListener {

    private Context context;
    private Realm realm;
    private ProductAdapter productAdapter;
    private RealmResults<Product> productResults;
    private RealmList<Product> productRealmList;

    public ProductListFragment() {
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

        inflater.inflate(R.menu.product_list_fragment_menu, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                productAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addItemProductList:
                openAddDialog();
                return true;
            case R.id.exportItemsProductList:
                // TODO: Validate user privileges
                // TODO: Call export dialog/fragment
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Get realm instance
        realm = Realm.getDefaultInstance();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);

        // Get productList before show any component
        productResults = realm.where(Product.class).findAll();
        productRealmList = new RealmList<>();
        productRealmList.addAll(realm.copyFromRealm(productResults));

        // Bind RecyclerView it's LayoutManager
        RecyclerView mRecyclerView = view.findViewById(R.id.productListRecyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        // Create an Custom Adapter instance
        productAdapter = new ProductAdapter(productRealmList, R.layout.cardview_product_item, new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final String sku, int qty, int position) {
                showEditDialog(sku, qty, position);
            }
        });

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(productAdapter);

        if (productRealmList.size() == 0) {
            openAddDialog();
        }

        return view;
    }

    private void refreshProductList() {
        RealmResults<Product> realmResults = realm.where(Product.class).findAll();
        productRealmList.clear();
        productRealmList.addAll(realm.copyFromRealm(realmResults));
        productAdapter.notifyDataSetChanged();
    }

    private void openAddDialog() {

        View customDialog = getActivity().getLayoutInflater().inflate(R.layout.add_product_dialog, null);

        final EditText editText = customDialog.findViewById(R.id.editTextSKU);
        final NumberPicker numberPicker = customDialog.findViewById(R.id.numberPickerQty);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(1000);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add product")
                .setMessage("Enter product information")
                .setNegativeButton("CANCEL", null)
                .setPositiveButton("DONE", null);
        builder.setView(customDialog);

        final AlertDialog dialog = builder.create();

        // Prevent closing Dialog if fields are not filled
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button b = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                b.setFocusable(true);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String sku = editText.getText().toString();
                        int qty = numberPicker.getValue();
                        if (!sku.isEmpty()) {
                            saveOrUpdate(sku, qty);
                            dialog.dismiss();
                        } else {
                            Toast.makeText(context, "Please, enter product information", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        dialog.show();
    }

    private void saveOrUpdate(final String sku, final int qty) {

        final Product productHolder = realm.where(Product.class).equalTo("sku", sku).findFirst();
        if (productHolder != null && productHolder.getSku().equals(sku)) {

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    int currentQty = productHolder.getQuantity();
                    productHolder.setQuantity(currentQty + qty);
                    realm.copyToRealmOrUpdate(productHolder);
                }
            });
            refreshProductList();
            Toast.makeText(context, "Product: " + sku + " has been updated.", Toast.LENGTH_SHORT).show();

        } else {

            final Product newProduct = new Product(sku, qty);
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(newProduct);
                }
            });
            refreshProductList();
            Toast.makeText(context, "SAVED", Toast.LENGTH_SHORT).show();
        }
    }

    private void showEditDialog(final String sku, int qty, int position) {

        final NumberPicker numberPicker = new NumberPicker(context);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(1000);
        numberPicker.setValue(qty);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setView(numberPicker);
        builder.setTitle(sku)
                .setMessage("Edit product")
                .setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final int currentValue = numberPicker.getValue();
                        final Product editProduct = productResults.where().equalTo("sku", sku).findFirst();
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                editProduct.setQuantity(currentValue);
                                realm.copyToRealmOrUpdate(editProduct);
                            }
                        });
                        refreshProductList();
                        productAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("DELETE PRODUCT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        showConfirmationDialog(sku);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.negativeButton));
    }

    private void showConfirmationDialog(final String sku) {

        AlertDialog.Builder builderNegative = new AlertDialog.Builder(getActivity());
        builderNegative
                .setTitle("Confirm delete")
                .setMessage("Are you sure you want to delete this product?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final Product product = realm.where(Product.class).equalTo("sku", sku).findFirst();
                        if (product != null) {
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    product.deleteFromRealm();
                                }
                            });
                            refreshProductList();
                            productAdapter.notifyDataSetChanged();
                            Toast.makeText(context, "DELETED", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("NO", null);
        AlertDialog confirmationDialog = builderNegative.create();
        confirmationDialog.show();
    }

    @Override
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment, fragment.toString());
        fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.commit();
    }


}
