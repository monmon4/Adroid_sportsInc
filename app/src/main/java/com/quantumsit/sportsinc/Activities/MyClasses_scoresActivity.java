package com.quantumsit.sportsinc.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.quantumsit.sportsinc.Backend.Functions;
import com.quantumsit.sportsinc.R;

import java.util.Arrays;

public class MyClasses_scoresActivity extends AppCompatActivity {

    TextView course_name_textview, date_textview,group_number_textview, class_number_textview,
             attend_textview ,score_textview, coach_name_textview, coach_note_textview;

    String course_name = "", class_date = "", group_name = "", coach_name = "", coach_notes = "";
    int attend = 0, score = 0, class_number = 0;

    Functions functions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*
        * View The Trainee Class Details
        * the data would be passed from ScoresFragment (item list Adapter on click listener)
        * */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_classes_scores);
        getSupportActionBar().setTitle(R.string.session_score);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        functions = new Functions(getApplicationContext());

        course_name_textview = findViewById(R.id.courseNameTextView_my_classes_scores);
        date_textview = findViewById(R.id.dateTextView_my_classes_scores);
        group_number_textview = findViewById(R.id.groupNumberTextView_my_classes_scores);
        class_number_textview = findViewById(R.id.classNumberTextView_my_classes_scores);
        score_textview = findViewById(R.id.scoreTextView_my_classes_scores);
        attend_textview = findViewById(R.id.scoreTextView_my_classes_absent);
        coach_name_textview = findViewById(R.id.coachNameTextView_my_classes_scores);
        coach_note_textview = findViewById(R.id.coachNoteTextView_my_classes_scores);


        course_name = getIntent().getStringExtra(getResources().getString(R.string.Key_Course_name));
        group_name = getIntent().getStringExtra(getResources().getString(R.string.Key_Group_name));
        class_date = getIntent().getStringExtra(getResources().getString(R.string.Key_Class_date));
        coach_name = getIntent().getStringExtra(getResources().getString(R.string.Key_Coach_name));
        coach_notes = getIntent().getStringExtra(getResources().getString(R.string.Key_Coach_note));
        attend = getIntent().getIntExtra(getResources().getString(R.string.Key_Attend),getResources().getInteger(R.integer.default_int));
        score = getIntent().getIntExtra(getResources().getString(R.string.Key_Score),getResources().getInteger(R.integer.default_int));
        class_number = getIntent().getIntExtra(getResources().getString(R.string.Key_Class_number),getResources().getInteger(R.integer.default_int));

        String course = getResources().getString(R.string.course_name)+": " + course_name;
        String group = getResources().getString(R.string.group_number) +": " + group_name;
        String class_name = getResources().getString(R.string.session) +" " + String.valueOf(class_number);
        String score_text = getResources().getString(R.string.score) +": " + String.valueOf(score) + "out of" + String.valueOf(class_number);

        String emoji = functions.getEmoji(coach_notes);


        course_name_textview.setText(course);
        date_textview.setText(class_date);
        group_number_textview.setText(group);
        class_number_textview.setText(class_name);
        score_textview.setText(score_text);
        /*
        * The Trainee was Absent for this Class show "absent' word in the screen
        * */
        if (attend == getResources().getInteger(R.integer.Absent))
            attend_textview.setVisibility(View.VISIBLE);
        coach_name_textview.setText(coach_name);
        coach_note_textview.setText(emoji);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
