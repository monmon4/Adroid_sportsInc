package com.quantumsit.sportsinc.COACHES;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.quantumsit.sportsinc.Aaa_looks.item1_reports_courses;
import com.quantumsit.sportsinc.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CoachClassesFragment extends Fragment {

    ExpandableListView not_finished_courses_expandable_listview;
    ListView finished_courses;

    ListViewExpandable_Adapter_NotFinishedCourses not_finished_courses_adapter;

    ArrayList<String> header_list;
    HashMap<String, List<item2_notfinished_course_group>> child_hashmap;

    FloatingActionButton current_class_button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_coach_classes,container,false);

        not_finished_courses_expandable_listview = root.findViewById(R.id.notFinishedCoursesExpandableListView_coachclasses);
        //finished_courses = root.findViewById(R.id.finishedCoursesListView_coachclasses);
        current_class_button = root.findViewById(R.id.currentClassFloatingActionButton);

        current_class_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityCurrentClass_coach.class);
                startActivity(intent);
            }
        });

        header_list = new ArrayList<>();
        child_hashmap = new HashMap<>();
        fill_lists();

        not_finished_courses_adapter = new ListViewExpandable_Adapter_NotFinishedCourses(getContext(), header_list, child_hashmap);
        not_finished_courses_expandable_listview.setAdapter(not_finished_courses_adapter);

        not_finished_courses_expandable_listview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent intent = new Intent(getActivity(), ActivityCourseSingleClass_coach.class);
                String course_name_and_group = not_finished_courses_adapter.header_list.get(groupPosition);
                String class_name = not_finished_courses_adapter.child_hashmap.get(course_name_and_group).get(childPosition).class_name;
                String class_date = not_finished_courses_adapter.child_hashmap.get(course_name_and_group).get(childPosition).class_date;

                intent.putExtra("course_name_and_group",course_name_and_group);
                intent.putExtra("class_name",class_name);
                intent.putExtra("class_date",class_date);

                getActivity().startActivity(intent);

                return false;
            }
        });

        return root;
    }

    private void fill_lists() {

        List<item2_notfinished_course_group> item2_list = new ArrayList<>();
        for (int i = 0; i<5; i++){
            header_list.add("Course 1, Group " + i + 1);
            item2_list.add(new item2_notfinished_course_group("Class " + i + 1, "5/5/2017") );
        }
        for(int j=0; j<5; j++){
            child_hashmap.put(header_list.get(j), item2_list);
        }
    }

}
