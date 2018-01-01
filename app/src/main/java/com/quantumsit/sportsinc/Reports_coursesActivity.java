package com.quantumsit.sportsinc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import com.quantumsit.sportsinc.Aaa_looks.ListViewExpandable_Adapter_singlecourse;
import com.quantumsit.sportsinc.Aaa_looks.item1_reports_courses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Reports_coursesActivity extends AppCompatActivity {

    ExpandableListView expandableListView;
    ListViewExpandable_Adapter_singlecourse listViewExpandable_adapter_singlecourse;

    ArrayList<String> item1_list;
    HashMap<String, List<item1_reports_courses>> item2_hashmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_courses);

        fill_lists();

        expandableListView = findViewById(R.id.singleCourseExpandableListView_singlecoursereport);
        listViewExpandable_adapter_singlecourse = new ListViewExpandable_Adapter_singlecourse(Reports_coursesActivity.this, item1_list, item2_hashmap);

        expandableListView.setAdapter(listViewExpandable_adapter_singlecourse);


    }

    private void fill_lists() {

        item1_list = new ArrayList<>();
        List<item1_reports_courses> item2_list = new ArrayList<>();
        item2_list.add(new item1_reports_courses("Class1","attended", "score: 100", "coach's note: \n"));
        item2_hashmap = new HashMap<>();

        for (int i=0; i<10; i++){
            item1_list.add( i + "/5/2017");
            item2_hashmap.put( item1_list.get(i) , item2_list);
        }

    }
}
