package com.quantumsit.sportsinc.RegisterationForm_fragments;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;

import com.quantumsit.sportsinc.R;

public class BookingSecondFormActivity extends AppCompatActivity {

    CheckBox firstIllness_checkBox, secondIllness_checkBox, thirdIllness_checkBox, forthIllness_checkBox;
    EditText otherIllness_editText;

    String[] received_info;
    int gender;

    StringBuilder illness = new StringBuilder();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_second_form);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Registration (2)");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        received_info = getIntent().getStringArrayExtra("info");
        gender = getIntent().getIntExtra("gender",0);

        firstIllness_checkBox = findViewById(R.id.checkBox1_second);
        secondIllness_checkBox = findViewById(R.id.checkBox2_second);
        thirdIllness_checkBox = findViewById(R.id.checkBox3_second);
        forthIllness_checkBox = findViewById(R.id.checkBox4_second);

        otherIllness_editText = findViewById(R.id.other_second);

    }

    public void back_second(View view) {
        onBackPressed();
        finish();
        //startActivity(new Intent(BookingSecondFormActivity.this, BookingFirstFormActivity.class));

    }


    public void next_second(View view) {

        if(firstIllness_checkBox.isChecked()) {
            illness .append("1@") ;
        } else {
            illness .append("0@") ;
        }

        if(secondIllness_checkBox.isChecked()) {
            illness .append("1@") ;
        } else {
            illness .append("0@") ;
        }

        if(thirdIllness_checkBox.isChecked()) {
            illness .append("1@") ;
        } else {
            illness .append("0@") ;
        }

        if(forthIllness_checkBox.isChecked()) {
            illness .append("1@") ;
        } else {
            illness .append("0@") ;
        }

        illness.append(otherIllness_editText.getText());


        Intent intent = new Intent(BookingSecondFormActivity.this, BookingThirdFormActivity.class);
        intent.putExtra("first_info", received_info);
        intent.putExtra("gender", gender);
        intent.putExtra("second_info", illness.toString());
        startActivity(intent);
    }


}
