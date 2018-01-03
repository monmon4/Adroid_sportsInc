package com.quantumsit.sportsinc.COACHES;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.quantumsit.sportsinc.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

public class ActivityAddRequest_coach extends AppCompatActivity {

    MaterialBetterSpinner request_for_spinner, course_name_spinner, class_number_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_add_request);

        request_for_spinner = findViewById(R.id.requestforSpinner_addreuestcoach);
        course_name_spinner = findViewById(R.id.courseNameSpinner_addreuestcoach);
        class_number_spinner = findViewById(R.id.classNumberSpinner_addreuestcoach);


        ArrayAdapter<CharSequence> request_for_spinner_adapter = ArrayAdapter.createFromResource(this, R.array.request_for_array, android.R.layout.simple_spinner_item);
        request_for_spinner_adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> course_name_adapter = ArrayAdapter.createFromResource(this, R.array.course_names_array, android.R.layout.simple_spinner_item);
        course_name_adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> class_number_adapter = ArrayAdapter.createFromResource(this, R.array.class_numbers_array, android.R.layout.simple_spinner_item);
        class_number_adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);


        request_for_spinner.setAdapter(request_for_spinner_adapter);
        course_name_spinner.setAdapter(course_name_adapter);
        class_number_spinner.setAdapter(class_number_adapter);


    }

    public void send_clicked(View view) {
        onBackPressed();
        finish();
    }
}
