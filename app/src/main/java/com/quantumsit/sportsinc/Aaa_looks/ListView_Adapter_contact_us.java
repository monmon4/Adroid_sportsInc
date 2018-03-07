package com.quantumsit.sportsinc.Aaa_looks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.quantumsit.sportsinc.R;

import java.util.ArrayList;

/**
 * Created by Mona on 27-Dec-17.
 */

public class ListView_Adapter_contact_us extends ArrayAdapter<item_contact_us> {

    public ListView_Adapter_contact_us(Context context, ArrayList<item_contact_us> item) {
        super(context, 0, item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        item_contact_us item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_contact_us, parent, false);
        }
        // Lookup view for data population
        TextView day_textView = convertView.findViewById(R.id.dayTextView_contactUs);
        TextView time_textView = convertView.findViewById(R.id.timeTextView_contactUs);
        // Populate the data into the template view using the data object
        day_textView.setText(item.date);
        time_textView.setText(item.start_time+" ~ "+item.end_time);
        return convertView;
    }
}
