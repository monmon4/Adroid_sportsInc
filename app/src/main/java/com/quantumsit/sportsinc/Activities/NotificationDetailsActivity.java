package com.quantumsit.sportsinc.Activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.Entities.NotificationEntity;
import com.quantumsit.sportsinc.R;
import com.quantumsit.sportsinc.util.NotificationUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class NotificationDetailsActivity extends AppCompatActivity {

    private static String TAG = NotificationDetailsActivity.class.getSimpleName();

    GlobalVars globalVars;
    ProgressDialog progressDialog;

    TextView subject ,content ,person ,date ;
    LinearLayout mybuttons;
    TextView accept , reject;

    int Notification_id ,requestID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        globalVars = (GlobalVars) getApplication();
        progressDialog = new ProgressDialog(NotificationDetailsActivity.this);

        NotificationEntity notification = (NotificationEntity) getIntent().getSerializableExtra("MyNotification");
        int notify_id = getIntent().getIntExtra("notify_id",-1);

        subject = findViewById(R.id.notificationReviewTitle);
        content = findViewById(R.id.notificationReviewContent);
        person = findViewById(R.id.notificationReviewPerson);
        date = findViewById(R.id.notificationReviewDate);
        mybuttons = findViewById(R.id.notificationReviewButtons);
        accept = findViewById(R.id.notificationReviewAccept);
        reject = findViewById(R.id.notificationReviewReject);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                updateRequest(1);
            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                updateRequest(0);
            }
        });

        if (notification != null){
            progressDialog.show();
            fillView(notification);
        }
        else if (notify_id != -1){
            progressDialog.show();
            retrieveNotification(notify_id,globalVars.getId());
        }
    }

    private void retrieveNotification(int notify_id, int user_id) {
        try {
            JSONObject where_info = new JSONObject();
            where_info.put("notification.to_id",2);
            where_info.put("notify_info.id",notify_id);

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.notification);
            HashMap<String,String> params = new HashMap<>();
            params.put("where",where_info.toString());
            httpCall.setParams(params);

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    try {
                        if (response != null)
                            fillView(new NotificationEntity(response.getJSONObject(0)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillView(NotificationEntity notification) {
        Notification_id = notification.getNotify_id();
        requestID = notification.getType_id();

        if (notification.getRead() == 0) {
            updateNotification("notify_read");
            NotificationUtils.Count -- ;
        }

        if(notification.getNotify_type() == 1 && notification.getUpdated() == 0) {
            mybuttons.setVisibility(View.VISIBLE);
        }
        subject.setText(notification.getSubject());
        person.setText(notification.getFrom());
        content.setText(notification.getContent());
        date.setText(notification.getDate());
        progressDialog.dismiss();
    }


    private void updateNotification(String columnName){
        try {
            JSONObject where_info = new JSONObject();
            where_info.put("notification.id",Notification_id);

            JSONObject values = new JSONObject();
            values.put(columnName,1);

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.updateData);
            HashMap<String,String> params = new HashMap<>();
            params.put("table","notification");
            params.put("where",where_info.toString());
            params.put("values",values.toString());

            httpCall.setParams(params);

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    progressDialog.dismiss();
                    Log.d(TAG,String.valueOf(response));
                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateRequest(int value){
        try {
            JSONObject where_info = new JSONObject();
            where_info.put("id",requestID);

            JSONObject values = new JSONObject();
            values.put("status",value);

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.updateData);
            HashMap<String,String> params = new HashMap<>();
            params.put("table","requests");
            params.put("where",where_info.toString());
            params.put("values",values.toString());

            httpCall.setParams(params);

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    mybuttons.setVisibility(View.GONE);
                    updateNotification("notify_updated");
                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}