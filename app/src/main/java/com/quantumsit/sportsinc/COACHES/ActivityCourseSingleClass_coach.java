package com.quantumsit.sportsinc.COACHES;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.COACHES.ReportsFragments.item_finsihed_course_single;
import com.quantumsit.sportsinc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivityCourseSingleClass_coach extends AppCompatActivity {

    ListView listView;
    ListView_Adapter_trainees_attendance_coach adapter_listView;

    ArrayList<item_trainee_attendance> list_items;

    item_finsihed_course_single myClass;
    TextView class_date_textView, course_name_textView, group_number_textView,
             pool_number_textView, coach_note_textView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_couch_course_single_class);

        String course_name = getIntent().getStringExtra("courseName");
        String group_name = getIntent().getStringExtra("groupName");
        String pool_name = getIntent().getStringExtra("poolName");
        myClass = (item_finsihed_course_single) getIntent().getSerializableExtra("finishedClass");

        class_date_textView = findViewById(R.id.classDateTextView_coachCourseSingleClass);
        course_name_textView = findViewById(R.id.courseNameTextView_coachCourseSingleClass);
        group_number_textView = findViewById(R.id.groupNumberTextView_coachCourseSingleClass);
        pool_number_textView  = findViewById(R.id.poolNumberTextView_coachCourseSingleClass);
        coach_note_textView = findViewById(R.id.coachNotesTextView_coachCourseSingleClass);


        fillView(course_name,group_name,pool_name);

        listView = findViewById(R.id.traineesAttendanceListView_coachCourseSingleClass);
        list_items = new ArrayList<>();

        initilizeTraineeList();

        adapter_listView = new ListView_Adapter_trainees_attendance_coach(ActivityCourseSingleClass_coach.this, list_items);
        listView.setAdapter(adapter_listView);

    }

    private void fillView(String course_name, String group_name, String pool_name) {
        course_name_textView.setText(course_name);
        group_number_textView.setText(group_name);
        pool_number_textView.setText(pool_name);
        if (myClass!=null) {
            coach_note_textView.setText(myClass.getCoach_note());
            class_date_textView.setText(myClass.getClass_date());
        }
    }

    private void initilizeTraineeList() {
        try {
            JSONObject where_info = new JSONObject();
            where_info.put("class_info.class_id",myClass.getClass_id());

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.ClassesTrainee);

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
                    item_trainee_attendance entity = new item_trainee_attendance(response.getJSONObject(i));
                    list_items.add(entity);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapter_listView.notifyDataSetChanged();
    }
}
