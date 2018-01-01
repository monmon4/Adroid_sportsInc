package com.quantumsit.sportsinc.COACHES;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.quantumsit.sportsinc.COACHES.CurrentClassFragments.item_score;
import com.quantumsit.sportsinc.R;

import java.util.ArrayList;

/**
 * Created by Mona on 27-Dec-17.
 */

public class ListViewFinishedCoursesReports_Adapter extends ArrayAdapter<item_reports_finished_courses> {

    public ArrayList<item_reports_finished_courses> list_items;

    public ListViewFinishedCoursesReports_Adapter(Context context, ArrayList<item_reports_finished_courses> list_items) {
        super(context, 0, list_items);

        this.list_items = new ArrayList<item_reports_finished_courses>();
        this.list_items.addAll(list_items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        item_reports_finished_courses item = list_items.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_coach_header_notfinishedcourses, parent, false);
        }
        // Lookup view for data population
        TextView course_name_and_group_textview =  convertView.findViewById(R.id.courseNameAndGroupTextView_notfinishedcourseheaderitem);

        // Populate the data into the template view using the data object
        String data = item.course_name + ", " + item.group_number;
        course_name_and_group_textview.setText(data);

        return convertView;
    }
}
