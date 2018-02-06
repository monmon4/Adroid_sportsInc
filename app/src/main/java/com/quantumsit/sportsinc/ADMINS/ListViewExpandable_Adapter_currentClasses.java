package com.quantumsit.sportsinc.ADMINS;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.quantumsit.sportsinc.COACHES.ActivityCurrentClass_coach;
import com.quantumsit.sportsinc.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


public class ListViewExpandable_Adapter_currentClasses extends BaseExpandableListAdapter {

    public Context context;
    public List<item_current_classes> header_list;
    public HashMap<Integer, List<String>> child_hashmap;

    Admin_CurrentClassesFragment myFragment;

    public ListViewExpandable_Adapter_currentClasses(Context context, List<item_current_classes> listDataHeader,
                                                     HashMap<Integer, List<String>> listChildData) {
        this.context = context;
        this.header_list = listDataHeader;
        this.child_hashmap = listChildData;
    }

    public ListViewExpandable_Adapter_currentClasses(Context context, Admin_CurrentClassesFragment fragment, List<item_current_classes> listDataHeader,
                                                     HashMap<Integer, List<String>> listChildData) {
        this.myFragment = fragment;
        this.context = context;
        this.header_list = listDataHeader;
        this.child_hashmap = listChildData;
    }

    @Override
    public int getGroupCount() {return this.header_list.size();}

    @Override
    public int getChildrenCount(int groupPosition) {return this.child_hashmap.get(header_list.get(groupPosition).id).size();}


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
        return this.child_hashmap.get(this.header_list.get(groupPosition).id)
                .get(childPosititon);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        item_current_classes  header = (item_current_classes) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item1_admin_current_classes, null);
        }

        TextView class_number =  convertView.findViewById(R.id.classNumberTextView_adminCurrentClassesItem);
        TextView class_pool =  convertView.findViewById(R.id.dateTextView_adminCurrentClassesItem);
        TextView class_time =  convertView.findViewById(R.id.timeTextView_adminCurrentClassesItem);

        class_number.setText(header.class_number);
        class_pool.setText(header.poolName);
        if (header.status == 2)
            class_time.setText(header.postpone_startTime+" ~ "+header.postpone_endTime);
        else
            class_time.setText(header.startTime+" ~ "+header.endTime);
        //class_text_view.setText(header.class_number);

        return convertView;
    }


    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String child = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item2_admin_current_classes, null);
        }

        TextView class_start = convertView.findViewById(R.id.class_adminCurrentClassesItem);
        ImageButton class_image = convertView.findViewById(R.id.imageButton_adminCurrentClassesItem);

        class_start.setText(child);

        if (child.equals("Start class")){
            class_image.setBackgroundResource(R.drawable.ic_class_swim);
        } else if (child.equals("Postpone class")) {
            class_image.setBackgroundResource(R.drawable.ic_class_time);
        } else if (child.equals("Cancel class")){
            class_image.setBackgroundResource(R.drawable.ic_class_cancel);
        } else if (child.equals("End class")){
            class_image.setBackgroundResource(R.drawable.ic_class_end);
        } else if (child.equals("Up coming")){
            class_image.setBackgroundResource(R.drawable.ic_class_waitingg);
            class_start.setTextColor(Color.parseColor("#ed4e4d4d"));
        }


        class_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myFragment.clickChildListener(groupPosition,child);
            }
        });

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
