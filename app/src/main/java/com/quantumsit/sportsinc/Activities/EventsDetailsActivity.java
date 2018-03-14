package com.quantumsit.sportsinc.Activities;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.quantumsit.sportsinc.Entities.EventEntity;
import com.quantumsit.sportsinc.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class EventsDetailsActivity extends AppCompatActivity {

    TextView title, date, time, description;

    Button addToCalendar;
    private EventEntity eventEntity;

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
        addToCalendar = findViewById(R.id.addToCalendar);

        addToCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEventToCalendar();
            }
        });

        eventEntity = (EventEntity) getIntent().getSerializableExtra("MyEvent");
        if (eventEntity != null) {
            fillView(eventEntity);
        }

    }

    private void addEventToCalendar() {
        if(eventEntity == null)
            return;
        SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
        Calendar dt = Calendar.getInstance();

        // Where untilDate is a date instance of your choice, for example 30/01/2012
        dt.setTime(eventEntity.getDate());
        String[] myTime = eventEntity.getTime().split(":");
        dt.add(Calendar.HOUR, Integer.parseInt(myTime[0])-1);
        dt.add(Calendar.MINUTE, Integer.parseInt(myTime[1]));

        String dtUntill = yyyyMMdd.format(dt.getTime());

        ContentResolver cr = getApplicationContext().getContentResolver();
        ContentValues values = new ContentValues();

        values.put(CalendarContract.Events.TITLE, getString(R.string.CalendarEventTitle));
        values.put(CalendarContract.Events.DESCRIPTION, eventEntity.getTitle());
        //values.put(CalendarContract.Events.EVENT_LOCATION, "My Home");
        values.put(CalendarContract.Events.DTSTART, dt.getTimeInMillis());
        TimeZone timeZone = TimeZone.getDefault();
        values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());

// Default calendar
        values.put(CalendarContract.Events.CALENDAR_ID, 1);

        values.put(CalendarContract.Events.RRULE, "FREQ=DAILY;UNTIL=" + dtUntill);
// Set Period for 1 Hour
        values.put(CalendarContract.Events.DURATION, "+P1H");

        values.put(CalendarContract.Events.HAS_ALARM, 1);

// Insert event to calendar
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(getApplicationContext(),"Not Granted",Toast.LENGTH_LONG).show();
            return;
        }
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
        Toast.makeText(getApplicationContext(),"Added to Calendar",Toast.LENGTH_LONG).show();
    }

    private void fillView(EventEntity eventEntity) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        String formattedDate = df.format(eventEntity.getDate());
        title.setText(eventEntity.getTitle());
        time.setText(eventEntity.getTime());
        date.setText(formattedDate);
        description.setText(eventEntity.getDescription());
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
