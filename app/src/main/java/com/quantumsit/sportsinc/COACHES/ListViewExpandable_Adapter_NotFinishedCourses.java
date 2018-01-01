package com.quantumsit.sportsinc.COACHES;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.quantumsit.sportsinc.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Mona on 27-Dec-17.
 */

public class ListViewExpandable_Adapter_NotFinishedCourses extends BaseExpandableListAdapter {

    public Context context;
    public List<String> header_list;
    public HashMap<String, List<item2_notfinished_course_group>> child_hashmap;

    public ListViewExpandable_Adapter_NotFinishedCourses(Context context, List<String> listDataHeader,
                                                         HashMap<String, List<item2_notfinished_course_group>> listChildData) {
        this.context = context;
        this.header_list = listDataHeader;
        this.child_hashmap = listChildData;
    }

    @Override
    public int getGroupCount() {return this.header_list.size();}

    @Override
    public int getChildrenCount(int groupPosition) {return this.child_hashmap.get(header_list.get(groupPosition)).size();}


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

        return this.child_hashmap.get(this.header_list.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        String  header = (String) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_coach_header_notfinishedcourses, null);
        }

        TextView course_group_text_view =  convertView.findViewById(R.id.courseNameAndGroupTextView_notfinishedcourseheaderitem);

        course_group_text_view.setTypeface(null, Typeface.BOLD);
        course_group_text_view.setText(header);
        //class_text_view.setText(header.class_number);

        return convertView;
    }


    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final item2_notfinished_course_group child = (item2_notfinished_course_group) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_coach_child_notfinishedcoursessingle, null);
        }

        TextView class_number_date = convertView.findViewById(R.id.classNumberAndDateTextView_childsingleitem);

        String item = child.class_name + "," + child.class_date;
        class_number_date.setText(item);

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
