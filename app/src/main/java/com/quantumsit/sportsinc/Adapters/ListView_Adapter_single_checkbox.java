package com.quantumsit.sportsinc.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.quantumsit.sportsinc.Entities.item_about;
import com.quantumsit.sportsinc.Entities.item_checkbox;
import com.quantumsit.sportsinc.Entities.item_name_id;
import com.quantumsit.sportsinc.R;

import java.util.ArrayList;

/**
 * Created by Mona on 27-Dec-17.
 */

public class ListView_Adapter_single_checkbox extends ArrayAdapter<item_name_id> {

    public ListView_Adapter_single_checkbox(Context context, ArrayList<item_name_id> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final item_name_id item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_checkbox_only, parent, false);
        }
        // Lookup view for data population
        CheckBox checkBox = convertView.findViewById(R.id.checkBox_item);
        TextView textView = convertView.findViewById(R.id.textViewCheckBox_item);

        // Populate the data into the template view using the data object
        if (item != null) {
            textView.setText(item.getName());
            checkBox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    CheckBox cb = (CheckBox) v ;
                    //item_checkbox item = (item_checkbox) cb.getTag();

                    item.setSelected(cb.isChecked());
                }
            });
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
