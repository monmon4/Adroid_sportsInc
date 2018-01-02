package com.quantumsit.sportsinc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quantumsit.sportsinc.Entities.classesEntity;

public class ClassesDetailsActivity extends AppCompatActivity {
    TextView date ,start ,end ,coach ,admin ,groupname ,pool ,classname ,coursename, postpond ,reason;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Class Details");

        date = findViewById(R.id.class_date_Text);
        start = findViewById(R.id.class_start_Text);
        end = findViewById(R.id.class_end_Text);
        coach = findViewById(R.id.class_coach_Text);
        admin = findViewById(R.id.class_admin_Text);
        classname = findViewById(R.id.class_name_Text);
        coursename = findViewById(R.id.class_course_Text);
        groupname = findViewById(R.id.class_group_Text);
        pool = findViewById(R.id.class_pool_Text);
        postpond = findViewById(R.id.class_postpond_Text);
        reason = findViewById(R.id.class_reason_Text);

        classesEntity myclass = (classesEntity) getIntent().getSerializableExtra("Myclass");

        if (myclass != null){

            fillView(myclass);
        }


    }

    private void fillView(classesEntity myclass) {
        date.setText(myclass.getClassdate());
        start.setText(myclass.getStartTime());
        end.setText(myclass.getEndTime());
        coach.setText(myclass.getCoachName());
        admin.setText(myclass.getAdminName());
        classname.setText(myclass.getClassName());
        coursename.setText(myclass.getCourseName());
        groupname.setText(myclass.getGroupName());
        pool.setText(myclass.getPoolName());
        if(myclass.getStatus().equals("Canceled")){
            LinearLayout reasonlayout = findViewById(R.id.canceled_reason);
            reasonlayout.setVisibility(View.VISIBLE);
            reason.setText(myclass.getReason());

            LinearLayout buttons = findViewById(R.id.ChangeStatusButtons);
            buttons.setVisibility(View.GONE);
        }
        else if(myclass.getStatus().equals("Postponded")){
            LinearLayout postpondlayout = findViewById(R.id.postponded_Time);
            postpondlayout.setVisibility(View.VISIBLE);
            postpond.setText(myclass.getPostpondTime());

            LinearLayout buttons = findViewById(R.id.ChangeStatusButtons);
            buttons.setVisibility(View.GONE);

            LinearLayout reasonlayout = findViewById(R.id.canceled_reason);
            reasonlayout.setVisibility(View.VISIBLE);
            reason.setText(myclass.getReason());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
