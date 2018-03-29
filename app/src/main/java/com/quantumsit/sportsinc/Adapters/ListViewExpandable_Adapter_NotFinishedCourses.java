package com.quantumsit.sportsinc.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.quantumsit.sportsinc.Entities.item2_notfinished_course_group;
import com.quantumsit.sportsinc.COACHES.Entities.item_finished_classes;
import com.quantumsit.sportsinc.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Mona on 27-Dec-17.
 */

public class ListViewExpandable_Adapter_NotFinishedCourses extends BaseExpandableListAdapter {

    public Context context;
    public List<item2_notfinished_course_group> header_list;
    public HashMap<Integer, List<item_finished_classes>> child_hashmap;

    public ListViewExpandable_Adapter_NotFinishedCourses(Context context, List<item2_notfinished_course_group> listDataHeader,
                                                         HashMap<Integer, List<item_finished_classes>> listChildData) {
        this.context = context;
        this.header_list = listDataHeader;
        this.child_hashmap = listChildData;
    }

    @Override
    public int getGroupCount() {return this.header_list.size();}

    @Override
    public int getChildrenCount(int groupPosition) {return this.child_hashmap.get(header_list.get(groupPosition).getGroup_id()).size();}

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

        return this.child_hashmap.get(this.header_list.get(groupPosition).getGroup_id())
                .get(childPosititon);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        item2_notfinished_course_group  header = (item2_notfinished_course_group) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_text_view, null);
        }

        TextView course_group_text_view =  convertView.findViewById(R.id.courseNameAndGroupTextView_notfinishedcourseheaderitem);

        course_group_text_view.setTypeface(null, Typeface.BOLD);
        course_group_text_view.setText(header.getCourseName()+context.getResources().getString(R.string.Coma)+header.getGroupName());
        //class_text_view.setText(header.class_number);

        return convertView;
    }


    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        item_finished_classes child = (item_finished_classes) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_coach_child_notfinishedcoursessingle, null);
        }

        TextView class_number_date = convertView.findViewById(R.id.classNumberAndDateTextView_childsingleitem);

        String item = child.getClass_name() + context.getString(R.string.Coma) + child.getClass_date();
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
