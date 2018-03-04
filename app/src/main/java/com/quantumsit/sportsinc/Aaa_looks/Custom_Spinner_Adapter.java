package com.quantumsit.sportsinc.Aaa_looks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quantumsit.sportsinc.R;

import java.util.ArrayList;

/**
 * Created by Mona on 01-Mar-18.
 */

public class Custom_Spinner_Adapter extends BaseAdapter {
    Context context;
    ArrayList<String> items;
    LayoutInflater inflter;

    public Custom_Spinner_Adapter(Context applicationContext, ArrayList<String> items) {
        this.context = applicationContext;
        this.items = items;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.spinner_item, null);
        TextView item = view.findViewById(R.id.spinnerTextView);
        item.setText(items.get(i));
        return view;
    }
}
