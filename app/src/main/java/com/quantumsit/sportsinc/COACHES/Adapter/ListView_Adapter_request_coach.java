package com.quantumsit.sportsinc.COACHES.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.quantumsit.sportsinc.COACHES.Entities.item_request_coach;
import com.quantumsit.sportsinc.R;

import java.util.ArrayList;

/**
 * Created by Mona on 27-Dec-17.
 */

public class ListView_Adapter_request_coach extends ArrayAdapter<item_request_coach> {

    public ListView_Adapter_request_coach(Context context, ArrayList<item_request_coach> requests) {
        super(context, 0, requests);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        item_request_coach request = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_coach_request, parent, false);
        }
        // Lookup view for data population
        TextView creation_date =  convertView.findViewById(R.id.creationDate_coachrequestitem);
        TextView coursename_and_class =  convertView.findViewById(R.id.courseNameAndClassNumberTextView_coachrequestitem);
        TextView request_for =  convertView.findViewById(R.id.requestforTextView_coachrequestitem);
        TextView date =  convertView.findViewById(R.id.classDate_coachrequestitem);

        ImageButton accept = convertView.findViewById(R.id.acceptImageButton_coachrequestitem);

        // Populate the data into the template view using the data object
        creation_date.setText(request.getCreation_date());
        coursename_and_class.setText(request.getCourse_name()+", "+request.getClass_name());
        date.setText(request.getDate());
        request_for.setText(request.getRequest_for());

        if (request.getStatus() == 0)
            accept.setBackgroundResource(R.drawable.ic_not_checked);
        else if (request.getStatus() == 2)
            accept.setBackgroundResource(R.drawable.ic_class_waitingg);
        else
            accept.setBackgroundResource(R.drawable.ic_check_circle);
        // Return the completed view to render on screen
        return convertView;
    }
}
