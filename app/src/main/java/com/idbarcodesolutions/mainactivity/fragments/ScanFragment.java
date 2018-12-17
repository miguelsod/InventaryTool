package com.idbarcodesolutions.mainactivity.fragments;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.idbarcodesolutions.mainactivity.R;
import com.idbarcodesolutions.mainactivity.models.Product;
import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.EMDKResults;
import com.symbol.emdk.barcode.BarcodeManager;
import com.symbol.emdk.barcode.ScanDataCollection;
import com.symbol.emdk.barcode.Scanner;
import com.symbol.emdk.barcode.ScannerConfig;
import com.symbol.emdk.barcode.ScannerException;
import com.symbol.emdk.barcode.ScannerResults;
import com.symbol.emdk.barcode.StatusData;

import java.util.ArrayList;

import io.realm.Realm;

public class ScanFragment extends Fragment implements EMDKManager.EMDKListener, Scanner.DataListener, Scanner.StatusListener {

    private Realm realm;

    // Store EMDKManager object
    private EMDKManager emdkManager;

    // Store Barcode Manager object
    private BarcodeManager barcodeManager;

    // Scanner device to scan
    private Scanner scanner;
    private int dataLength = 0;

    // UI elements
    private TextView statusTextView;
    private EditText dataView;
    private EditText productQty;
    private Context context;

    public ScanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Get Realm instance
        realm = Realm.getDefaultInstance();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scan, container, false);

        // Reference to UI elements
        bindUI(view);

        EMDKResults results = EMDKManager.getEMDKManager(
                context,
                this
        );

        if (results.statusCode != EMDKResults.STATUS_CODE.SUCCESS) {
            statusTextView.setText("EMDKManager Request Failed");
        }
        return view;
    }

    private void bindUI(View view) {
        statusTextView = view.findViewById(R.id.textViewStatusFragment);
        dataView = view.findViewById(R.id.editTextDataFragment);
        productQty = view.findViewById(R.id.editTextQuantity);

        Button btnCreateProduct = view.findViewById(R.id.buttonCreateProduct);
        btnCreateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sku = dataView.getText().toString();
                int qty = Integer.parseInt(productQty.getText().toString());
                Product newProduct = new Product(sku, qty);
                saveProduct(newProduct);
                closeKeyboard();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    public void initializeScanner() throws ScannerException {
        if (scanner == null) {
            // Get the Barcode Manager object
            barcodeManager = (BarcodeManager) this.emdkManager
                    .getInstance(EMDKManager.FEATURE_TYPE.BARCODE);
            // Get default scanner defined on the device
            scanner = barcodeManager.getDevice(BarcodeManager.DeviceIdentifier.DEFAULT);

            // Add data and status listeners
            scanner.addDataListener(this);
            scanner.addStatusListener(this);
            // Hard trigger. When this mode is set, the user has to manually
            // press the trigger on the device after issuing the read call.
            scanner.triggerType = Scanner.TriggerType.HARD;
            // Enable the scanner
            scanner.enable();
            scanner.read();
        }
    }

    private void deInitializeScanner() throws ScannerException {
        if (scanner != null) {
            try {
                if (scanner.isReadPending()) {
                    scanner.cancelRead();
                }
                scanner.disable();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                scanner.removeDataListener(this);
                scanner.removeStatusListener(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                scanner.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
            scanner = null;
        }
    }

    @Override
    public void onOpened(EMDKManager emdkManager) {
        this.emdkManager = emdkManager;

        try {
            // Call this method to enable Scanner and its listeners
            initializeScanner();
        } catch (ScannerException e) {
            e.printStackTrace();
        }

        try {
            ScannerConfig scannerConfig = scanner.getConfig();
            scannerConfig.scanParams.decodeLEDTime = 800;
            scanner.setConfig(scannerConfig);
        } catch (ScannerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClosed() {
        if (this.emdkManager != null) {
            this.emdkManager.release();
            this.emdkManager = null;
        }
    }

    @Override
    public void onData(ScanDataCollection scanDataCollection) {
        new AsyncDataUpdate().execute(scanDataCollection);
    }

    @Override
    public void onStatus(StatusData statusData) {
        new AsyncStatusUpdate().execute(statusData);
    }

    private class AsyncDataUpdate extends AsyncTask<ScanDataCollection, Void, String> {

        @Override
        protected String doInBackground(ScanDataCollection... scanDataCollections) {

            // Status string that contains both barcode data and type of barcode
            // that is being scanned
            String statusStr = "";
            try {
                scanner.read();
                ScanDataCollection scanDataCollection = scanDataCollections[0];
                if (scanDataCollection != null && scanDataCollection.getResult() == ScannerResults.SUCCESS) {

                    ArrayList<ScanDataCollection.ScanData> scanData = scanDataCollection
                            .getScanData();

                    for (ScanDataCollection.ScanData data : scanData) {
                        statusStr = data.getData();
                    }
                }
            } catch (ScannerException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            // Return result to populate on UI thread
            return statusStr;
        }


        @Override
        protected void onPostExecute(String result) {
            // Update the dataView EditText on UI thread with barcode data and
            // its label type
            if (!result.isEmpty()) {
                if (dataLength++ > 50) {
                    // Clear the cache after 50 scans
                    dataView.getText().clear();
                    dataLength = 0;
                }
                dataView.setText(result);
            }
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private class AsyncStatusUpdate extends AsyncTask<StatusData, Void, String> {

        @Override
        protected String doInBackground(StatusData... statusData) {
            String statusStr = "";
            // Get the current state of scanner in background
            StatusData statusData1 = statusData[0];
            StatusData.ScannerStates state = statusData1.getState();
            // Different states of scanner
            switch (state) {
                case IDLE:
                    try {
                        scanner.read();
                    } catch (ScannerException e) {
                        e.printStackTrace();
                    }
                    statusStr = "Scanner enabled and idle";
                    break;
                case WAITING:
                    try {
                        scanner.read();
                    } catch (ScannerException e) {
                        e.printStackTrace();
                    }
                    statusStr = "Waiting for trigger press";
                    break;
                case ERROR:
                    statusStr = "There is an error!!!!";
                    break;
                case DISABLED:
                    statusStr = "Scanner is disabled";
                    break;
                case SCANNING:
                    statusStr = "Scanning...";
                    break;
            }
            return statusStr;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            statusTextView.setText(result);
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (emdkManager != null) {
            // Clean up the objects created by EMDK manager
            emdkManager.release();
            emdkManager = null;
        }
        try {
            deInitializeScanner();
        } catch (ScannerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            if (scanner != null) {
                // releases the scanner hardware resources for other application
                // to use. You must call this as soon as you're done with the
                // scanning.
                scanner.removeDataListener(this);
                scanner.removeStatusListener(this);
                scanner.disable();
                scanner = null;
            }
        } catch (ScannerException e) {
            e.printStackTrace();
        }
    }

    private void saveProduct(final Product product) {
        Toast.makeText(context, product.getSku(), Toast.LENGTH_SHORT).show();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(product);
            }
        });
    }

    public void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }
}
