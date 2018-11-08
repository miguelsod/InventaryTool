package com.idbarcodesolutions.mainactivity.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.idbarcodesolutions.mainactivity.R;

public class DrawerAdapter extends ArrayAdapter<String> {
    private String[] mOptionList;
    private int resLayout;
    private Context mContext;

    public DrawerAdapter(Context context, int layout, String[] mOptionList) {
        super(context, layout, mOptionList);
        this.mOptionList = mOptionList;
        this.resLayout = layout;
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resLayout, null);
        }

        String option = getItem(position);

        if (!option.isEmpty()) {
            TextView textView = v.findViewById(R.id.textViewOption);
            textView.setText(option);
        }

        return v;
    }
}
