package com.quantumsit.sportsinc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

public class JoinNowActivity extends AppCompatActivity {

    TextView location_textview, phone_textview;

    MaterialBetterSpinner date_spinner, time_spinner ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_now);

        location_textview = findViewById(R.id.locationTextView_joinnow);
        phone_textview = findViewById(R.id.phoneTextView_joinnow);

        date_spinner = findViewById(R.id.dateSpinner_joinnow);
        time_spinner = findViewById(R.id.timeSpinner_joinnow);

        ArrayAdapter<CharSequence> date_adapter = ArrayAdapter.createFromResource(JoinNowActivity.this, R.array.date_array,android.R.layout.simple_spinner_item );
        date_adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> time_spinner = ArrayAdapter.createFromResource(JoinNowActivity.this, R.array.date_array,android.R.layout.simple_spinner_item );
        time_spinner.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);


    }
}
