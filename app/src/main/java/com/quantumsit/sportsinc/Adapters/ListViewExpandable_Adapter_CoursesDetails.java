package com.quantumsit.sportsinc.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.quantumsit.sportsinc.COACHES.item_finished_classes;
import com.quantumsit.sportsinc.Entities.item1_courses_details;
import com.quantumsit.sportsinc.Entities.item2_courses_details;
import com.quantumsit.sportsinc.Entities.item2_notfinished_course_group;
import com.quantumsit.sportsinc.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Mona on 27-Dec-17.
 */

public class ListViewExpandable_Adapter_CoursesDetails extends BaseExpandableListAdapter {

    public Context context;
    public List<item1_courses_details> header_list;
    public HashMap<Integer, item2_courses_details> child_hashmap;

    public ListViewExpandable_Adapter_CoursesDetails(Context context, List<item1_courses_details> listDataHeader,
                                                     HashMap<Integer, item2_courses_details> listChildData) {
        this.context = context;
        this.header_list = listDataHeader;
        this.child_hashmap = listChildData;
    }

    @Override
    public int getGroupCount() {return this.header_list.size();}

    @Override
    public int getChildrenCount(int groupPosition) {return 1;}

    @Override
    public long getGroupId(int groupPosition) {return groupPosition;}

    @Override
    public long getChildId(int groupPosition, int childPosition) {return childPosition;}

    @Override
    public Object getGroup(int groupPosition) {
        return this.header_list.get(groupPosition);
    }


    @Override
    public Object getChild(int groupPosition, int childPosititon) {

        return this.child_hashmap.get(this.header_list.get(groupPosition).getClass_id());
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        item1_courses_details  header = (item1_courses_details) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item1_courses_details, null);
        }

        TextView class_name_textview =  convertView.findViewById(R.id.classNameTextView_item1coursesdetails);
        TextView start_date_textview =  convertView.findViewById(R.id.dateTextView_item1coursesdetails);

        class_name_textview.setText(header.getClass_name());
        start_date_textview.setText(header.getStart_date());

        return convertView;
    }


    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        item2_courses_details child = (item2_courses_details) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item2_courses_details, null);
        }

        TextView coach_name_textview = convertView.findViewById(R.id.coachNameTextView_item2coursedetails);
        TextView days_textview = convertView.findViewById(R.id.daysTextView_item2coursedetails);
        TextView times_textview = convertView.findViewById(R.id.timesTextView_item2coursedetails);

        coach_name_textview.setText(child.getCoach_name());
        StringBuilder days = new StringBuilder();
        StringBuilder times = new StringBuilder();

        for (int i=0; i<days.length(); i++) {
            days.append(child.getDay()[i]).append("\n");
            times.append(child.getTime()[i]).append("\n");
        }

        days_textview.setText(days.toString());
        times_textview.setText(times.toString());
        return convertView;
    }



    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
