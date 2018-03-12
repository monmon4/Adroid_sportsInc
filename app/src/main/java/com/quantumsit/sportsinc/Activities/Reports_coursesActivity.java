package com.quantumsit.sportsinc.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Aaa_looks.ListViewExpandable_Adapter_singlecourse;
import com.quantumsit.sportsinc.Aaa_looks.item1_reports_courses;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.CustomView.CustomLoadingView;
import com.quantumsit.sportsinc.CustomView.myCustomExpandableListViewListener;
import com.quantumsit.sportsinc.R;
import com.quantumsit.sportsinc.util.ConnectionUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Reports_coursesActivity extends AppCompatActivity {

    ExpandableListView expandableListView;
    ListViewExpandable_Adapter_singlecourse listViewExpandable_adapter_singlecourse;

    ArrayList<String> item1_list;
    HashMap<String, item1_reports_courses> item2_hashmap;

    SwipeRefreshLayout mSwipeRefreshLayout;

    CustomLoadingView loadingView;


    GlobalVars globalVars;

    int course_id = 0;
    String course_name = "", group_name = "", coach_name = "", pool_name = "";
    TextView courseAndGroup_textView, pool_textView, coach_textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_courses);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        globalVars = (GlobalVars) getApplication();

        course_id = (int) getIntent().getExtras().get("course_id");
        course_name = (String) getIntent().getExtras().get("course_name");
        group_name = (String) getIntent().getExtras().get("group_name");

        loadingView = findViewById(R.id.LoadingView);
        loadingView.setOnRetryClick(new CustomLoadingView.OnRetryClick() {
            @Override
            public void onRetry() {
                fill_lists();
            }
        });

        mSwipeRefreshLayout = findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fill_lists();
            }
        });

        courseAndGroup_textView = findViewById(R.id.courseNameAndGroupNumberTextView_reportscoursessinglea);
        coach_textView = findViewById(R.id.coachNameTextView_reportscoursessingle);
        pool_textView = findViewById(R.id.poolNumberTextView_reportscoursessinglea);

        item1_list = new ArrayList<>();
        item2_hashmap = new HashMap<>();
        expandableListView = findViewById(R.id.singleCourseExpandableListView_singlecoursereport);
        myCustomExpandableListViewListener listener = new myCustomExpandableListViewListener(expandableListView, mSwipeRefreshLayout) {
            @Override
            public void loadMoreData() {
               // Don't Load More
            }
        };
        expandableListView.setOnScrollListener(listener);

        listViewExpandable_adapter_singlecourse = new ListViewExpandable_Adapter_singlecourse(Reports_coursesActivity.this, item1_list, item2_hashmap);

        expandableListView.setAdapter(listViewExpandable_adapter_singlecourse);

        if (savedInstanceState == null)
            fill_lists();
        else
            fillBySavedState(savedInstanceState);
    }



    private void fillBySavedState(Bundle savedInstanceState) {
        ArrayList<String> list1 = (ArrayList<String>) savedInstanceState.getSerializable("SessionsList");
        HashMap<String, item1_reports_courses> myHashMap = (HashMap<String, item1_reports_courses>) savedInstanceState.getSerializable("HashMap");
        item1_list.addAll(list1);
        item2_hashmap.putAll(myHashMap);
        Parcelable mListInstanceState = savedInstanceState.getParcelable("ScrollPosition");
        listViewExpandable_adapter_singlecourse.notifyDataSetChanged();
        expandableListView.onRestoreInstanceState(mListInstanceState);
        loadingView.success();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("ScrollPosition", expandableListView.onSaveInstanceState());
        outState.putSerializable("SessionsList", item1_list);
        outState.putSerializable("HashMap",item2_hashmap);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private boolean checkConnection() {
        // first, check connectivity
        if (ConnectionUtilities
                .checkInternetConnection(this)) {
            return true;
        }
        return false;
    }

    @SuppressLint("StaticFieldLeak")
    private void fill_lists() {
        if (!checkConnection()){
            loadingView.enableRetry();
            loadingView.fails();
            return;
        }

        JSONObject where_info = new JSONObject();
        try {
            where_info.put("trainee_id",globalVars.getId());
            where_info.put("classes_details.course_id",course_id);

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.traineeClassScores);
            HashMap<String,String> params = new HashMap<>();
            params.put("report","1");
            params.put("where",where_info.toString());

            httpCall.setParams(params);
            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    try {
                        item1_list.clear();
                        item2_hashmap.clear();
                        if (response != null) {
                            JSONObject first_result = response.getJSONObject(0);
                            coach_name = first_result.getString("coach_name");
                            pool_name = first_result.getString("Pool_Name");

                            for (int i=0; i<response.length(); i++){
                                JSONObject result = response.getJSONObject(i);
                                int class_number = result.getInt("class_number");
                                String class_date = result.getString("class_date");
                                String coach_note = result.getString("coach_note");
                                int attend = result.getInt("attend");
                                int score = result.getInt("score");
                                String class_name = "Class" + String.valueOf(class_number) + ", " + class_date;
                                item1_list.add(class_name);
                                item2_hashmap.put(class_name, new item1_reports_courses("", attend, score, coach_note));

                            }

                        }
                        fill_list_view();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private  void fill_list_view(){
        mSwipeRefreshLayout.setRefreshing(false);
        String title = course_name + ", " + group_name;
        String coach = getResources().getString(R.string.coach) +": " + coach_name;
        String pool = getResources().getString(R.string.pool_number) +": " + pool_name;
        courseAndGroup_textView.setText(title);
        coach_textView.setText(coach);
        pool_textView.setText(pool);
        listViewExpandable_adapter_singlecourse.notifyDataSetChanged();
        loadingView.success();

    }
}
