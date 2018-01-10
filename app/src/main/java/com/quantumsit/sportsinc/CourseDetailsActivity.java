package com.quantumsit.sportsinc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.quantumsit.sportsinc.Entities.CourseEntity;

import java.text.SimpleDateFormat;

public class CourseDetailsActivity extends AppCompatActivity {
    TextView CourseName ,ClassesNum ,CoursePrice ,description ,startDate ,endDate ,CourseLevel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        CourseName = findViewById(R.id.course_details_name);
        ClassesNum = findViewById(R.id.course_details_no_classes);
        CoursePrice = findViewById(R.id.course_details_price);
        description = findViewById(R.id.course_details_description);
        startDate = findViewById(R.id.course_details_start_date);
        endDate = findViewById(R.id.course_details_end_date);
        CourseLevel = findViewById(R.id.course_details_level);

        CourseEntity myCourse = (CourseEntity) getIntent().getSerializableExtra("MyCourse");

        if(myCourse != null){
            fillView(myCourse);
        }

    }

    private void fillView(CourseEntity courseEntity) {
        CourseName.setText(courseEntity.getCourseName());
        ClassesNum.setText(courseEntity.getClasses_Num());
        CoursePrice.setText(courseEntity.getPrice());
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String date = df.format(courseEntity.getStartDate());
        startDate.setText(date);
        date = df.format(courseEntity.getEndDate());
        endDate.setText(date);
        CourseLevel.setText(courseEntity.getLevel());
        description.setText(courseEntity.getDescription());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
