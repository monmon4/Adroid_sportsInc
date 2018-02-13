package com.quantumsit.sportsinc.Home_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Adapters.CoursesAdapter;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.Activities.CourseDetailsActivity;
import com.quantumsit.sportsinc.Entities.CourseEntity;
import com.quantumsit.sportsinc.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
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
        initilizeCourses();

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
    private void initilizeCourses() {
        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.POST);
        httpCall.setUrl(Constants.selectData);
        HashMap<String,String> params = new HashMap<>();
        params.put("table","courses");

        httpCall.setParams(params);
        new HttpRequest(){
            @Override
            public void onResponse(JSONArray response) {
                super.onResponse(response);
                fillAdapter(response);
            }
        }.execute(httpCall);
    }

    private void fillAdapter(JSONArray response) {
        if (response != null) {
            try {
                for (int i = 0; i < response.length(); i++) {
                    courseList.add(new CourseEntity( response.getJSONObject(i)));
                    adapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
