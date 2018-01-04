package com.quantumsit.sportsinc.ADMINS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.quantumsit.sportsinc.COACHES.item_finsihed_course_single;
import com.quantumsit.sportsinc.R;

import java.util.ArrayList;

/**
 * Created by Mona on 27-Dec-17.
 */

public class ListViewCurrentClasses_Adapter extends ArrayAdapter<item_current_classes> {

    public ArrayList<item_current_classes> list_items;

    public ListViewCurrentClasses_Adapter(Context context, ArrayList<item_current_classes> list_items) {
        super(context, 0, list_items);

        this.list_items = new ArrayList<>();
        this.list_items.addAll(list_items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        item_current_classes item = list_items.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_coach_finished_course_single, parent, false);
        }
        // Lookup view for data population
        TextView class_number_textview =  convertView.findViewById(R.id.classNumberTextView_adminCurrentClassesItem);
        TextView class_date_textview =  convertView.findViewById(R.id.dateTextView_adminCurrentClassesItem);
        TextView time_textview =  convertView.findViewById(R.id.timeTextView_adminCurrentClassesItem);

        // Populate the data into the template view using the data object

        class_number_textview.setText(item.class_number);
        class_date_textview.setText(item.class_date);
        time_textview.setText(item.time);

        return convertView;
    }
}
