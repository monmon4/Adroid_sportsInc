package com.quantumsit.sportsinc.COACHES;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.COACHES.ReportsFragments.item_finsihed_course_single;
import com.quantumsit.sportsinc.CustomView.CustomLoadingView;
import com.quantumsit.sportsinc.Entities.item_trainee_attendance;
import com.quantumsit.sportsinc.R;
import com.quantumsit.sportsinc.util.ConnectionUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivityCourseSingleClass_coach extends AppCompatActivity {

    private static String TAG = ActivityCourseSingleClass_coach.class.getSimpleName();

    ListView listView;
    ListView_Adapter_trainees_attendance_coach adapter_listView;

    ArrayList<item_trainee_attendance> list_items;

    item_finsihed_course_single AdminClass;
    item_finished_classes CoachClass;

    TextView class_date_textView, course_name_textView, group_number_textView,
             pool_number_textView, coach_note_textView;

    CustomLoadingView loadingView;
    private int ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_couch_course_single_class);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        loadingView = findViewById(R.id.LoadingView);
        loadingView.setOnRetryClick(new CustomLoadingView.OnRetryClick() {
            @Override
            public void onRetry() {
                initilizeTraineeList(ID);
            }
        });

        String course_name = getIntent().getStringExtra(getString(R.string.Key_Course_name));
        String group_name = getIntent().getStringExtra(getString(R.string.Key_Group_name));
        String pool_name = getIntent().getStringExtra(getString(R.string.Key_Pool_name));
        int UserType = getIntent().getIntExtra(getString(R.string.Key_UserType),1);

        switch (UserType){
            case 1:
                CoachClass = (item_finished_classes) getIntent().getSerializableExtra("finishedClass");
                break;
            case 2:
                AdminClass = (item_finsihed_course_single) getIntent().getSerializableExtra("finishedClass");
                break;
        }


        class_date_textView = findViewById(R.id.classDateTextView_coachCourseSingleClass);
        course_name_textView = findViewById(R.id.courseNameTextView_coachCourseSingleClass);
        group_number_textView = findViewById(R.id.groupNumberTextView_coachCourseSingleClass);
        pool_number_textView  = findViewById(R.id.poolNumberTextView_coachCourseSingleClass);
        coach_note_textView = findViewById(R.id.coachNotesTextView_coachCourseSingleClass);

        listView = findViewById(R.id.traineesAttendanceListView_coachCourseSingleClass);
        list_items = new ArrayList<>();
        adapter_listView = new ListView_Adapter_trainees_attendance_coach(ActivityCourseSingleClass_coach.this, list_items);
        listView.setAdapter(adapter_listView);

        fillView(course_name,group_name,pool_name,UserType,savedInstanceState);

    }

    private void fillView(String course_name, String group_name, String pool_name,int Type,Bundle savedInstanceState) {
        course_name_textView.setText(course_name);
        group_number_textView.setText(group_name);
        pool_number_textView.setText(pool_name);
        String class_note = "";
        String class_date = "";
        int class_id = 0;
        switch (Type) {
            case 1:
                if (CoachClass !=null){
                    class_note = CoachClass.getClass_note();
                    class_date = CoachClass.getClass_date();
                    class_id = CoachClass.getClass_id();
                }
                break;
            case 2:
                if (AdminClass != null) {
                    class_note = AdminClass.getCoach_note();
                    class_date = AdminClass.getClass_date();
                    class_id = AdminClass.getClass_id();
                }
                break;
        }
        coach_note_textView.setText(class_note);
        class_date_textView.setText(class_date);
        if (savedInstanceState == null)
            initilizeTraineeList(class_id);
        else
            fillBySaveState(savedInstanceState);
    }

    private void fillBySaveState(Bundle savedInstanceState) {
        ArrayList<item_trainee_attendance> list = (ArrayList<item_trainee_attendance>) savedInstanceState.getSerializable("listItems");
        list_items.addAll(list);
        adapter_listView.notifyDataSetChanged();
        loadingView.success();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("listItems",list_items);
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

    private void initilizeTraineeList(int class_id) {
        if (!checkConnection()){
            ID = class_id;
            loadingView.fails();
            loadingView.enableRetry();
            return;
        }
        try {
            JSONObject where_info = new JSONObject();
            where_info.put("class_id", class_id);

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
        loadingView.success();

    }
}
