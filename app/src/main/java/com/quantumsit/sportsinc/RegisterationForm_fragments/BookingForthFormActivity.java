package com.quantumsit.sportsinc.RegisterationForm_fragments;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.quantumsit.sportsinc.R;

public class BookingForthFormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_forth_form);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Registration (4)");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }


    public void back_forth(View view) {
        onBackPressed();
        //startActivity(new Intent(BookingSecondFormActivity.this, BookingFirstFormActivity.class));

    }


    public void done_forth(View view) {
        //startActivity(new Intent(BookingForthFormActivity.this, BookingForthFormActivity.class));
    }
}
