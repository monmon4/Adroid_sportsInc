package com.quantumsit.sportsinc.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.quantumsit.sportsinc.Entities.item_about;
import com.quantumsit.sportsinc.R;

import java.util.ArrayList;

/**
 * Created by Mona on 27-Dec-17.
 */

public class ListView_Adapter_about_us extends ArrayAdapter<item_about> {

    public ListView_Adapter_about_us(Context context, ArrayList<item_about> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        item_about item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_about_us, parent, false);
        }
        // Lookup view for data population
        TextView title =  convertView.findViewById(R.id.titleTextView_aboutitem);
        TextView content =  convertView.findViewById(R.id.contentTextView_aboutitem);

        // Populate the data into the template view using the data object
        if (item != null) {
            title.setText(item.getTitle());
            content.setText(item.getContent());
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
