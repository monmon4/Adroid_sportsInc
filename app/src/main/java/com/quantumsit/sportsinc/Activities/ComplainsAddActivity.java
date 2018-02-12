package com.quantumsit.sportsinc.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.quantumsit.sportsinc.R;

public class ComplainsAddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complains_add);

        getSupportActionBar().setTitle("Compose");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
