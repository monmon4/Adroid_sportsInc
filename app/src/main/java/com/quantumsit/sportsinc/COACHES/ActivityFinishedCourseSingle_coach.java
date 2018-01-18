package com.quantumsit.sportsinc.COACHES;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.quantumsit.sportsinc.COACHES.ReportsFragments.ListViewFinishedCoursesSingle_Adapter;
import com.quantumsit.sportsinc.COACHES.ReportsFragments.item_finsihed_course_single;
import com.quantumsit.sportsinc.R;

import java.util.ArrayList;

public class ActivityFinishedCourseSingle_coach extends AppCompatActivity {

    TextView course_name_text_view, group_number_text_view, pool_number_text_view;

    //EditText coach_note_edit_text;

    //FloatingActionButton done_button;

    ListView listView;
    ListViewFinishedCoursesSingle_Adapter listView_adapter;

    ArrayList<item_finsihed_course_single> list_items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_finished_course_single);

        course_name_text_view = findViewById(R.id.courseNameTextView_singleFinishedCourse);
        group_number_text_view = findViewById(R.id.groupNumberTextView_singleFinishedCourse);
        pool_number_text_view = findViewById(R.id.poolNumberTextView_singleFinishedCourse);

        //coach_note_edit_text = findViewById(R.id.coachNoteEditText_singleFinishedCourse);

        //done_button = findViewById(R.id.floatingActionButton_singleFinishedCourse);

        //setButtonEnabled(true, View.VISIBLE, true);

        /*done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String note = coach_note_edit_text.getText().toString();

            }
        });*/


        listView = findViewById(R.id.listView_singleFinishedCourse);
        list_items = new ArrayList<>();

        for (int i=1 ; i<10 ; i++) {
            list_items.add(new item_finsihed_course_single("Class " + i, "5/5/2017", "80%"));
        }

        listView_adapter = new ListViewFinishedCoursesSingle_Adapter(ActivityFinishedCourseSingle_coach.this, list_items);
        listView.setAdapter(listView_adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ActivityFinishedCourseSingle_coach.this, ActivityCourseSingleClass_coach.class);
                startActivity(intent);

            }
        });



    }

    private void setButtonEnabled(boolean edit_text, int button_vis, boolean button) {
        //coach_note_edit_text.setEnabled(edit_text);
        //done_button.setVisibility(button_vis);
        //done_button.setEnabled(button);
    }
}
