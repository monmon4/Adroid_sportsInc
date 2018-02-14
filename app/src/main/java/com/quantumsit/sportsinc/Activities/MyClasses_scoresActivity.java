package com.quantumsit.sportsinc.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.quantumsit.sportsinc.R;

public class MyClasses_scoresActivity extends AppCompatActivity {

    TextView course_name_textview, date_textview,group_number_textview, class_number_textview,
             attend_textview ,score_textview, coach_name_textview, coach_note_textview;

    String course_name = "", class_date = "", group_name = "", coach_name = "", coach_notes = "";
    int attend = 0, score = 0, class_number = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_classes_scores);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        course_name_textview = findViewById(R.id.courseNameTextView_my_classes_scores);
        date_textview = findViewById(R.id.dateTextView_my_classes_scores);
        group_number_textview = findViewById(R.id.groupNumberTextView_my_classes_scores);
        class_number_textview = findViewById(R.id.classNumberTextView_my_classes_scores);
        score_textview = findViewById(R.id.scoreTextView_my_classes_scores);
        attend_textview = findViewById(R.id.scoreTextView_my_classes_absent);
        coach_name_textview = findViewById(R.id.coachNameTextView_my_classes_scores);
        coach_note_textview = findViewById(R.id.coachNoteTextView_my_classes_scores);


        course_name = getIntent().getExtras().getString("course_name");
        group_name = getIntent().getExtras().getString("group_name");
        class_date = getIntent().getExtras().getString("class_date");
        coach_name = getIntent().getExtras().getString("coach_name");
        coach_notes = getIntent().getExtras().getString("coach_notes");
        attend = getIntent().getExtras().getInt("attend");
        score = getIntent().getExtras().getInt("score");
        class_number = getIntent().getExtras().getInt("class_number");

        String course = "Course: " + course_name;
        String group = "Group: " + group_name;
        String class_name = "Class " + String.valueOf(class_number);
        String score_text = "Score: " + String.valueOf(score);

        course_name_textview.setText(course);
        date_textview.setText(class_date);
        group_number_textview.setText(group);
        class_number_textview.setText(class_name);
        score_textview.setText(score_text);
        if (attend == 0)
            attend_textview.setVisibility(View.VISIBLE);
        coach_name_textview.setText(coach_name);
        coach_note_textview.setText(coach_notes);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
