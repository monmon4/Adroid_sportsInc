package com.quantumsit.sportsinc.COACHES.Adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.quantumsit.sportsinc.COACHES.Entities.item_finsihed_course_single;
import com.quantumsit.sportsinc.R;

import java.util.ArrayList;

/**
 * Created by Mona on 27-Dec-17.
 */

public class ListViewFinishedCoursesSingle_Adapter extends ArrayAdapter<item_finsihed_course_single> {

    public ArrayList<item_finsihed_course_single> list_items;

    public ListViewFinishedCoursesSingle_Adapter(Context context, ArrayList<item_finsihed_course_single> list_items) {
        super(context, 0, list_items);

        this.list_items = list_items;
    }

    @Override
    public int getCount() {
        return list_items.size();
    }

    @Nullable
    @Override
    public item_finsihed_course_single getItem(int position) {
        return list_items.get(position);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_coach_finished_course_single, parent, false);
        }

        // Get the data item for this position
        item_finsihed_course_single item = list_items.get(position);

        // Lookup view for data population
        TextView class_number_textview =  convertView.findViewById(R.id.classNumberTextView_coachFinishedCoursesSingleItem);
        TextView class_date_textview =  convertView.findViewById(R.id.dateTextView_coachFinishedCoursesSingleItem);
        TextView attendance_percentage_textview =  convertView.findViewById(R.id.attendanceTextView_coachFinishedCoursesSingleItem);

        // Populate the data into the template view using the data object

        class_number_textview.setText(item.getClass_name());
        class_date_textview.setText(item.getClass_date());
        attendance_percentage_textview.setText(item.getAttendance_percentage());

        return convertView;
    }
}
