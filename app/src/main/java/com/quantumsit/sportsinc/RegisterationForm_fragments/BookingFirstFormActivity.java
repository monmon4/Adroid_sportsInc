package com.quantumsit.sportsinc.RegisterationForm_fragments;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.quantumsit.sportsinc.R;

public class BookingFirstFormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_first_form);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Registration");


    }

    public void next_first(View view) {
        startActivity(new Intent(BookingFirstFormActivity.this, BookingSecondFormActivity.class));
    }
}
