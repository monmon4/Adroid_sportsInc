package com.quantumsit.sportsinc.COACHES;

import android.content.Intent;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.COACHES.ReportsFragments.ListViewFinishedCoursesSingle_Adapter;
import com.quantumsit.sportsinc.COACHES.ReportsFragments.item_finsihed_course_single;
import com.quantumsit.sportsinc.COACHES.ReportsFragments.item_reports_finished_courses;
import com.quantumsit.sportsinc.CustomView.CustomLoadingView;
import com.quantumsit.sportsinc.R;
import com.quantumsit.sportsinc.util.ConnectionUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivityFinishedCourseSingle_coach extends AppCompatActivity {

    private static String TAG =  ActivityFinishedCourseSingle_coach.class.getSimpleName();

    TextView course_name_text_view, group_number_text_view, pool_number_text_view;

    ListView listView;
    ListViewFinishedCoursesSingle_Adapter listView_adapter;

    ArrayList<item_finsihed_course_single> list_items;

    item_reports_finished_courses group;
    CustomLoadingView loadingView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_finished_course_single);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        loadingView = findViewById(R.id.LoadingView);
        loadingView.setOnRetryClick(new CustomLoadingView.OnRetryClick() {
            @Override
            public void onRetry() {
                initilizeClassesList();
            }
        });

        group = (item_reports_finished_courses) getIntent().getSerializableExtra("finishedGroup");

        course_name_text_view = findViewById(R.id.courseNameTextView_singleFinishedCourse);
        group_number_text_view = findViewById(R.id.groupNumberTextView_singleFinishedCourse);
        pool_number_text_view = findViewById(R.id.poolNumberTextView_singleFinishedCourse);


        fillView();

        listView = findViewById(R.id.listView_singleFinishedCourse);
        list_items = new ArrayList<>();

        listView_adapter = new ListViewFinishedCoursesSingle_Adapter(ActivityFinishedCourseSingle_coach.this, list_items);
        listView.setAdapter(listView_adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ActivityFinishedCourseSingle_coach.this, ActivityCourseSingleClass_coach.class);
                intent.putExtra(getString(R.string.Key_Course_name),group.getCourse_name());
                intent.putExtra(getString(R.string.Key_Group_name),group.getGroup_name());
                intent.putExtra(getString(R.string.Key_Pool_name),group.getPool_name());
                intent.putExtra(getString(R.string.Key_UserType),2);
                intent.putExtra("finishedClass",list_items.get(position));
                startActivity(intent);

            }
        });


        if (savedInstanceState == null)
            initilizeClassesList();

        else
            fillBySavedState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("finishedClass", list_items);
    }


    private void fillBySavedState(Bundle savedInstanceState) {
        ArrayList<item_finsihed_course_single> list1 = (ArrayList<item_finsihed_course_single>) savedInstanceState.getSerializable("ClassesList");
        list_items.addAll(list1);
        listView_adapter.notifyDataSetChanged();
        loadingView.success();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void fillView() {
        if (group != null){
            course_name_text_view.setText(group.getCourse_name());
            group_number_text_view.setText(group.getGroup_name());
            pool_number_text_view.setText(group.getPool_name());
        }
    }

    private boolean checkConnection() {
        // first, check connectivity
        if (ConnectionUtilities
                .checkInternetConnection(this)) {
            return true;
        }
        return false;
    }

    private void initilizeClassesList() {
        if (!checkConnection()){
            loadingView.fails();
            loadingView.enableRetry();
            return;
        }
        try {
            JSONObject where_info = new JSONObject();
            where_info.put("group_id",group.getGroup_id());

            Log.d(TAG,where_info.toString());
            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.finishedClasses);

            HashMap<String, String> params = new HashMap<>();
            params.put("where", where_info.toString());

            httpCall.setParams(params);

            new HttpRequest() {
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    fillAdapter(response);
                }
            }.execute(httpCall);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillAdapter(JSONArray response) {
        list_items.clear();
        if (response != null) {
            try {
                for (int i = 0; i < response.length(); i++) {
                    item_finsihed_course_single entity = new item_finsihed_course_single(response.getJSONObject(i));
                    list_items.add(entity);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        listView_adapter.notifyDataSetChanged();
        loadingView.success();
    }
}
