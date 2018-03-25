package com.quantumsit.sportsinc.Activities;

import android.Manifest;
import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.CustomView.CustomLoadingView;
import com.quantumsit.sportsinc.Entities.EventEntity;
import com.quantumsit.sportsinc.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class EventsDetailsActivity extends AppCompatActivity {

    GlobalVars globalVars;

    TextView  title ,date, time, description , event_link ,eventFile;

    LinearLayout addToCalendar;
    TextView interestedLabel;
    ImageView interestedView;
    private EventEntity eventEntity;
    CustomLoadingView loadingView;
    private ImageView eventImage;
    private ProgressBar progressBar;
    int loadingTime = 1200;
    private int CALENDAR_PERMISSION_CODE = 130;

    DownloadManager downloadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_details);
        getSupportActionBar().setTitle("Event Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        globalVars = (GlobalVars) getApplication();

        loadingView = findViewById(R.id.LoadingView);
        title = findViewById(R.id.event_title);
        time = findViewById(R.id.eventDetailTime);
        date = findViewById(R.id.event_date);
        description = findViewById(R.id.event_description);
        event_link =findViewById(R.id.event_link);
        eventFile =findViewById(R.id.event_file);
        addToCalendar = findViewById(R.id.event_interested);
        interestedView =findViewById(R.id.interestedView);
        interestedLabel = findViewById(R.id.interestedLabel);
        eventImage = findViewById(R.id.event_Image);
        progressBar = findViewById(R.id.progress_bar2);

        addToCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEventToCalendar();
            }
        });

        event_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(eventEntity.getEventUrl()));
                startActivity(browserIntent);
            }
        });

        eventFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadEventFile();
            }
        });

        eventEntity = (EventEntity) getIntent().getSerializableExtra("MyEvent");

        if (savedInstanceState != null)
            loadingTime = 0;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (eventEntity!=null){
                    checkInteresting();
                }
                else
                    loadingView.fails(); }
        }, loadingTime);

    }

    private void downloadEventFile() {
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(Constants.upload_files_host+eventEntity.getEventFileUrl());
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        Long reference = downloadManager.enqueue(request);
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

            //And finally ask for the permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR}, CALENDAR_PERMISSION_CODE);
            //Toast.makeText(getApplicationContext(),"Not Granted",Toast.LENGTH_LONG).show();
            return;
        }
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
        changeInterestedView();
        insertInterestedToDB();
        Toast.makeText(getApplicationContext(),"Added to Calendar",Toast.LENGTH_LONG).show();
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == CALENDAR_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                addEventToCalendar();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Can't access Storage...", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void insertInterestedToDB() {
        JSONObject values = new JSONObject();
        try {
            values.put("person_id",globalVars.getId());
            values.put("event_id",eventEntity.getEvent_id());

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.insertData);
            HashMap<String,String> params = new HashMap<>();
            params.put("table","event_interest");
            params.put("values",values.toString());
            httpCall.setParams(params);
            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void checkInteresting() {
        JSONObject where = new JSONObject();
        try {
            where.put("person_id",globalVars.getId());
            where.put("event_id",eventEntity.getEvent_id());

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.selectData);
            HashMap<String,String> params = new HashMap<>();
            params.put("table","event_interest");
            params.put("where",where.toString());
            httpCall.setParams(params);
            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    if (response != null)
                        changeInterestedView();
                    fillView(eventEntity);
                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void changeInterestedView() {
        addToCalendar.setBackgroundColor(Color.parseColor("#001b51"));
        interestedLabel.setTextColor(Color.parseColor("#FFFFFF"));
        interestedView.setImageResource(R.drawable.ic_star);
    }


    private void fillView(EventEntity eventEntity) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        String formattedDate = df.format(eventEntity.getDate());
        title.setText(eventEntity.getTitle());
        time.setText(eventEntity.getTime());
        date.setText(formattedDate);
        if (eventEntity.getEventUrl() == null || eventEntity.getEventUrl().equals("") )
            event_link.setVisibility(View.GONE);
        else
            event_link.setText(eventEntity.getEventUrl());

        if (eventEntity.getEventFileUrl() == null || eventEntity.getEventFileUrl().equals("") )
            eventFile.setVisibility(View.GONE);
        else
            eventFile.setText(eventEntity.getEventFileUrl());

        description.setText(eventEntity.getDescription());
        String ImageUrl = eventEntity.getImgUrl();

        if(!ImageUrl.equals("")) {
            Picasso.with(getApplicationContext()).load(Constants.others_host + ImageUrl).into(eventImage, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }else {
            progressBar.setVisibility(View.GONE);
        }
        loadingView.success();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
