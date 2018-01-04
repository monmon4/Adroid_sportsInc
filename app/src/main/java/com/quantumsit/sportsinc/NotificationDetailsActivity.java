package com.quantumsit.sportsinc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quantumsit.sportsinc.Entities.NotificationEntity;

public class NotificationDetailsActivity extends AppCompatActivity {

    TextView subject ,content ,person ,date ;
    LinearLayout mybuttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        NotificationEntity notification = (NotificationEntity) getIntent().getSerializableExtra("MyNotification");

        subject = findViewById(R.id.notificationReviewTitle);
        content = findViewById(R.id.notificationReviewContent);
        person = findViewById(R.id.notificationReviewPerson);
        date = findViewById(R.id.notificationReviewDate);
        mybuttons = findViewById(R.id.notificationReviewButtons);

        if (notification != null){
            fillView(notification);
        }

    }

    private void fillView(NotificationEntity notification) {
        if(notification.getType().equals("Request")) {
            mybuttons.setVisibility(View.VISIBLE);
        }
        subject.setText(notification.getSubject());
        person.setText(notification.getFrom());
        content.setText(notification.getContent());
        date.setText(notification.getDate());
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}