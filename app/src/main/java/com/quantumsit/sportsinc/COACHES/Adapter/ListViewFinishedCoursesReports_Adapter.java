package com.quantumsit.sportsinc.COACHES.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.quantumsit.sportsinc.COACHES.Entities.item_reports_finished_courses;
import com.quantumsit.sportsinc.R;

import java.util.ArrayList;

/**
 * Created by Mona on 27-Dec-17.
 */

public class ListViewFinishedCoursesReports_Adapter extends ArrayAdapter<item_reports_finished_courses> {

    public ArrayList<item_reports_finished_courses> list_items;

    public ListViewFinishedCoursesReports_Adapter(Context context, ArrayList<item_reports_finished_courses> list_items) {
        super(context, 0, list_items);

        this.list_items = list_items;
    }
    @Override
    public int getCount() {
        return list_items.size();
    }

    @Nullable
    @Override
    public item_reports_finished_courses getItem(int position) {
        return list_items.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_text_view, parent, false);
        }

        // Get the data item for this position
        item_reports_finished_courses item = getItem(position);

        // Lookup view for data population
        TextView course_name_and_group_textview =  convertView.findViewById(R.id.courseNameAndGroupTextView_notfinishedcourseheaderitem);

        // Populate the data into the template view using the data object
        String data = item.getCourse_name() + ", " + item.getGroup_name();
        course_name_and_group_textview.setText(data);
        course_name_and_group_textview.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

        return convertView;
    }
}
