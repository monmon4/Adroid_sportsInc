package com.quantumsit.sportsinc.RegisterationForm_fragments;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.hbb20.CountryCodePicker;
import com.quantumsit.sportsinc.R;

public class BookingFirstFormActivity extends AppCompatActivity {

    EditText firstName_editText, lastName_editText, phone_editText
            , nationality_editText, address_editText, mail_editText;

    TextView maleGender_textView, femaleGender_textView;
    int gender = 2;

    CountryCodePicker ccp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_first_form);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Registration (1)");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        firstName_editText = findViewById(R.id.first_name_first);
        lastName_editText = findViewById(R.id.last_name_first);

        maleGender_textView = findViewById(R.id.male_first);
        femaleGender_textView = findViewById(R.id.female_first);

        ccp = findViewById(R.id.ccp_first);
        phone_editText = findViewById(R.id.phone_first);

        nationality_editText = findViewById(R.id.nationality_first);
        address_editText = findViewById(R.id.address_first);
        mail_editText = findViewById(R.id.mail_first);

    }

    public void next_first(View view) {

        startActivity(new Intent(BookingFirstFormActivity.this, BookingSecondFormActivity.class));
    }

    public void back_first(View view) {
        onBackPressed();
        finish();
        //startActivity(new Intent(BookingFirstFormActivity.this, BookingSecondFormActivity.class));
    }

    public void male_pressed(View view) {
        gender = 0;
        maleGender_textView.setBackgroundResource(R.drawable.cornered_background_red);
        femaleGender_textView.setBackgroundResource(R.drawable.cornered_background_gray);
    }

    public void female_pressed(View view) {
        gender = 1;
        maleGender_textView.setBackgroundResource(R.drawable.cornered_background_gray);
        femaleGender_textView.setBackgroundResource(R.drawable.cornered_background_red);
    }
}
