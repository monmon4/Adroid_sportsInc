package com.quantumsit.sportsinc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.quantumsit.sportsinc.Entities.EventEntity;

public class EventsDetailsActivity extends AppCompatActivity {

    TextView title ,date ,time ,description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        title = findViewById(R.id.eventDetailsTitle);
        time = findViewById(R.id.eventDetailsTime);
        date = findViewById(R.id.eventDetailsDate);
        description = findViewById(R.id.eventDetailsDescription);

        EventEntity eventEntity = (EventEntity) getIntent().getSerializableExtra("MyEvent");
        if (eventEntity!=null){
            fillView(eventEntity);
        }

    }

    private void fillView(EventEntity eventEntity) {
        title.setText(eventEntity.getTitle());
        time.setText(eventEntity.getTime());
        date.setText(eventEntity.getDate());
        description.setText(eventEntity.getDescription());
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
