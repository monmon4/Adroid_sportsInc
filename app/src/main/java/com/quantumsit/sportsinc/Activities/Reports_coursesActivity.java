package com.quantumsit.sportsinc.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import com.quantumsit.sportsinc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Reports_coursesActivity extends AppCompatActivity {

    ExpandableListView expandableListView;
    ListViewExpandable_Adapter_singlecourse listViewExpandable_adapter_singlecourse;

    ArrayList<String> item1_list;
    HashMap<String, item1_reports_courses> item2_hashmap;

    ProgressDialog progressDialog;

    GlobalVars globalVars;

    int course_id = 0;
    String course_name = "", group_name = "", coach_name = "", pool_name = "";
    TextView courseAndGroup_textView, pool_textView, coach_textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_courses);

        globalVars = (GlobalVars) getApplication();

        course_id = (int) getIntent().getExtras().get("course_id");
        course_name = (String) getIntent().getExtras().get("course_name");
        group_name = (String) getIntent().getExtras().get("group_name");

        courseAndGroup_textView = findViewById(R.id.courseNameAndGroupNumberTextView_reportscoursessinglea);
        coach_textView = findViewById(R.id.coachNameTextView_reportscoursessingle);
        pool_textView = findViewById(R.id.poolNumberTextView_reportscoursessinglea);

        item1_list = new ArrayList<>();
        item2_hashmap = new HashMap<>();
        progressDialog = new ProgressDialog(Reports_coursesActivity.this);
        progressDialog.setMessage("Please wait.....");

        fill_lists();

        expandableListView = findViewById(R.id.singleCourseExpandableListView_singlecoursereport);
        listViewExpandable_adapter_singlecourse = new ListViewExpandable_Adapter_singlecourse(Reports_coursesActivity.this, item1_list, item2_hashmap);

        expandableListView.setAdapter(listViewExpandable_adapter_singlecourse);


    }

    @SuppressLint("StaticFieldLeak")
    private void fill_lists() {
        progressDialog.show();

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
        String title = course_name + ", " + group_name;
        String coach = "Coach: " + coach_name;
        String pool = "Pool: " + pool_name;
        courseAndGroup_textView.setText(title);
        coach_textView.setText(coach);
        pool_textView.setText(pool);
        listViewExpandable_adapter_singlecourse.notifyDataSetChanged();
        progressDialog.dismiss();

    }
}
