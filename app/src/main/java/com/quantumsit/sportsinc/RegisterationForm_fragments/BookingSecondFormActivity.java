package com.quantumsit.sportsinc.RegisterationForm_fragments;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.quantumsit.sportsinc.R;

public class BookingSecondFormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_second_form);
    }

    public void back_second(View view) {
        startActivity(new Intent(BookingSecondFormActivity.this, BookingFirstFormActivity.class));

    }


    public void next_second(View view) {

    }


}
