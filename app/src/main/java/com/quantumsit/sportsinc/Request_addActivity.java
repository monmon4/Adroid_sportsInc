package com.quantumsit.sportsinc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.quantumsit.sportsinc.Side_menu_fragments.RequestsFragment;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

public class Request_addActivity extends AppCompatActivity {

    MaterialBetterSpinner course_name_spinner, date_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_add);

        course_name_spinner = findViewById(R.id.courseNameSpinner_requestadd);
        date_spinner = findViewById(R.id.dateSpinner_requestadd);


        ArrayAdapter<CharSequence> courses_spinner_adapter = ArrayAdapter.createFromResource(this, R.array.course_names_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> dates_spinner_adapter = ArrayAdapter.createFromResource(this, R.array.date_array, android.R.layout.simple_spinner_item);

        course_name_spinner.setAdapter(courses_spinner_adapter);
        date_spinner.setAdapter(dates_spinner_adapter);

    }

    public void send_clicked(View view) {
        onBackPressed();
        finish();
    }
}
