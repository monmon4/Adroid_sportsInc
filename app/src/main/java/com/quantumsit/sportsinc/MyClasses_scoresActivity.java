package com.quantumsit.sportsinc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MyClasses_scoresActivity extends AppCompatActivity {

    TextView course_name_textview, date_textview,group_number_textview, class_number_textview,
                score_textview, coach_name_textview, coach_note_textview;

    String course_name, date, group_number, class_number, score, coach_name, couch_note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_classes_scores);

        course_name_textview = findViewById(R.id.courseNameTextView_my_classes_scores);
        date_textview = findViewById(R.id.dateTextView_my_classes_scores);
        group_number_textview = findViewById(R.id.groupNumberTextView_my_classes_scores);
        class_number_textview = findViewById(R.id.classNumberTextView_my_classes_scores);
        score_textview = findViewById(R.id.scoreTextView_my_classes_scores);
        coach_name_textview = findViewById(R.id.coachNameTextView_my_classes_scores);
        coach_note_textview = findViewById(R.id.coachNoteTextView_my_classes_scores);



    }
}
