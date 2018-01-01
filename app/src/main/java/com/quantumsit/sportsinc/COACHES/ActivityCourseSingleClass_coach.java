package com.quantumsit.sportsinc.COACHES;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.quantumsit.sportsinc.R;

import java.util.ArrayList;

public class ActivityCourseSingleClass_coach extends AppCompatActivity {

    ListView listView;
    ListView_Adapter_trainees_attendance_coach adapter_listView;

    ArrayList<item_trainee_attendance> list_items;

    TextView class_date_textView, course_name_textView, group_number_textView,
             pool_number_textView, attendance_textView, coach_note_textView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_couch_course_single_class);

        String course_and_group = getIntent().getStringExtra("course_name_and_group");

        listView = findViewById(R.id.traineesAttendanceListView_coachCourseSingleClass);
        list_items = new ArrayList<>();

        for (int i=1; i<20; i++) {
            if (i == 2 || i == 4 ||i == 10 ||i == 15 ){
                list_items.add(new item_trainee_attendance("trainee " + i, false));
            } else {
                list_items.add(new item_trainee_attendance("trainee " + i, true));
            }

        }

        adapter_listView = new ListView_Adapter_trainees_attendance_coach(ActivityCourseSingleClass_coach.this, list_items);
        listView.setAdapter(adapter_listView);

    }
}
