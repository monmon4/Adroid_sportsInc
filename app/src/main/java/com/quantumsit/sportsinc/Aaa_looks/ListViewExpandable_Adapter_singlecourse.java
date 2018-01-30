package com.quantumsit.sportsinc.Aaa_looks;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.quantumsit.sportsinc.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Mona on 27-Dec-17.
 */

public class ListViewExpandable_Adapter_singlecourse extends BaseExpandableListAdapter {

    private Context context;
    private List<String> item1_list;
    private HashMap<String, item1_reports_courses> item2_hashmap;

    public ListViewExpandable_Adapter_singlecourse(Context context, List<String> listDataHeader,
                                                   HashMap<String, item1_reports_courses> listChildData) {
        this.context = context;
        this.item1_list = listDataHeader;
        this.item2_hashmap = listChildData;
    }

    @Override
    public int getGroupCount() {return this.item1_list.size();}

    @Override
    public int getChildrenCount(int groupPosition) {return 1;}


    @Override
    public long getGroupId(int groupPosition) {return groupPosition;}

    @Override
    public long getChildId(int groupPosition, int childPosition) {return childPosition;}

    @Override
    public Object getGroup(int groupPosition) {
        return this.item1_list.get(groupPosition);
    }


    @Override
    public Object getChild(int groupPosition, int childPosititon) {

        return this.item2_hashmap.get(this.item1_list.get(groupPosition));
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        String  header = (String) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item1_single_course_report, null);
        }

        TextView date_text_view =  convertView.findViewById(R.id.classDateTextView_singlecoursereportitem1);

        date_text_view.setTypeface(null, Typeface.BOLD);
        date_text_view.setText(header);
        //class_text_view.setText(header.class_number);

        return convertView;
    }


    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final item1_reports_courses child = (item1_reports_courses) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item2_sub_single_course_report, null);
        }

        ImageButton attendance_image_button = convertView.findViewById(R.id.attendaceImageButton_singlecoursereportitem2);
        TextView class_number = convertView.findViewById(R.id.classNumberTextView_singlecoursereportitem2);
        TextView score_text_view = convertView.findViewById(R.id.scoreTextView_singlecoursereportitem2);
        TextView coach_note_text_view = convertView.findViewById(R.id.coachNoteTextView_singlecoursereportitem2);

        if (child.attend == 1) {
            attendance_image_button.setBackgroundResource(R.drawable.ic_check_circle);
        } else {
            attendance_image_button.setBackgroundResource(R.drawable.ic_not_checked);
        }
        String score_string = "Score: " + String.valueOf(child.score);
        String coach_note_string = "Coach's note: \n    " + String.valueOf(child.coach_note);
        score_text_view.setText(score_string);
        coach_note_text_view.setText(coach_note_string);
        class_number.setText(child.class_number);
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
