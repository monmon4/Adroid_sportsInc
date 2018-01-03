package com.quantumsit.sportsinc.Home_fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.quantumsit.sportsinc.Adapters.CoursesAdapter;
import com.quantumsit.sportsinc.CourseDetailsActivity;
import com.quantumsit.sportsinc.Entities.CourseEntity;
import com.quantumsit.sportsinc.R;

import java.util.ArrayList;
import java.util.List;

public class CoursesFragment extends Fragment {
    private CoursesAdapter adapter;
    private List<CourseEntity> courseList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_courses,container,false);
        ListView listView = root.findViewById(R.id.courses_listview);
        listView.setEmptyView(root.findViewById(R.id.empty_text));
        courseList=new ArrayList<>();

        courseList.add(new CourseEntity("Course 1","11/12/2018","12/01/2019","900","Beginner level","10","hello from the bottom of my heart"));
        courseList.add(new CourseEntity("Course 3","11/02/2018","12/12/2017","2000","Expert level","17","hello from the bottom of my heart"));

        adapter = new CoursesAdapter(getContext(),R.layout.course_list_item,courseList);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), CourseDetailsActivity.class);
                intent.putExtra("MyCourse",courseList.get(i));
                startActivity(intent);
            }
        });
        return root;
    }
}
