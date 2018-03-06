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

public class ListView_Adapter_request extends ArrayAdapter<item_request> {

    public ListView_Adapter_request(Context context, ArrayList<item_request> requests) {
        super(context, 0, requests);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        item_request request = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_request, parent, false);
        }
        // Lookup view for data population
        TextView creation_date =  convertView.findViewById(R.id.creationDate_requestitem);
        TextView coursename_and_class =  convertView.findViewById(R.id.courseNameAndClassNumberTextView_coachrequestitem);
        TextView class_date =  convertView.findViewById(R.id.classDate_requestitem);

        // Populate the data into the template view using the data object
        creation_date.setText(request.creation_date);
        coursename_and_class.setText(request.course_name_and_class_name);
        class_date.setText(request.class_date);
        // Return the completed view to render on screen
        return convertView;
    }
}
