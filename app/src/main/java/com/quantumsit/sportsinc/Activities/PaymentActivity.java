package com.quantumsit.sportsinc.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.quantumsit.sportsinc.R;

public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Check out");


    }
}
