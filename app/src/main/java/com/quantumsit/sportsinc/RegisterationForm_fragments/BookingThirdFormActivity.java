package com.quantumsit.sportsinc.RegisterationForm_fragments;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.quantumsit.sportsinc.R;

public class BookingThirdFormActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_third_form);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Registration (3)");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        TextView first_person = findViewById(R.id.textView3);
        TextView second_person = findViewById(R.id.textView4);
        first_person.setPaintFlags(first_person.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        second_person.setPaintFlags(second_person.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


    }


    public void back_third(View view) {
        onBackPressed();
        //startActivity(new Intent(BookingSecondFormActivity.this, BookingFirstFormActivity.class));

    }


    public void next_third(View view) {
        startActivity(new Intent(BookingThirdFormActivity.this, BookingForthFormActivity.class));
    }
}
