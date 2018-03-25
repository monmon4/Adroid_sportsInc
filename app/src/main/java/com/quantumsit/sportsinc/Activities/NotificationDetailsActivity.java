package com.quantumsit.sportsinc.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
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
import com.quantumsit.sportsinc.CustomView.CustomLoadingView;
import com.quantumsit.sportsinc.Entities.NotificationEntity;
import com.quantumsit.sportsinc.R;
import com.quantumsit.sportsinc.util.ConnectionUtilities;
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

    CustomLoadingView loadingView;
    private int ID , loadingTime = 1200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Notification details");

        globalVars = (GlobalVars) getApplication();
        loadingView = findViewById(R.id.LoadingView);
        loadingView.setOnRetryClick(new CustomLoadingView.OnRetryClick() {
            @Override
            public void onRetry() {
                retrieveNotification(ID,globalVars.getId());
            }
        });
        progressDialog = new ProgressDialog(NotificationDetailsActivity.this);

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

        if (savedInstanceState != null)
            loadingTime = 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"NewResume");
        final NotificationEntity notification = (NotificationEntity) getIntent().getSerializableExtra("MyNotification");
        final int notify_id = getIntent().getIntExtra("notify_id",-1);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (notification != null){
                    fillView(notification);
                }
                else if (notify_id != -1){
                    retrieveNotification(notify_id,globalVars.getId());
                }
                else
                    loadingView.fails(); }
        }, loadingTime);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        final int notify_id = intent.getIntExtra("notify_id",-1);

        loadingView.loading();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (notify_id != -1) {
                    retrieveNotification(notify_id, globalVars.getId());
                }
                else {
                    loadingView.fails();
                }
            }
        },loadingTime);
    }

    private boolean checkConnection() {
        // first, check connectivity
        if (ConnectionUtilities
                .checkInternetConnection(this)) {
            return true;
        }
        return false;
    }

    private void retrieveNotification(int notify_id, int user_id) {
        if (!checkConnection()){
            ID = notify_id;
            loadingView.fails();
            loadingView.enableRetry();
            return;
        }
        try {
            JSONObject where_info = new JSONObject();
            where_info.put(getString(R.string.where_notify_toId),user_id);
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
        }

        if(notification.getNotify_type() == 1 && notification.getUpdated() == 0) {
            mybuttons.setVisibility(View.VISIBLE);
        }
        subject.setText(notification.getSubject());
        person.setText(notification.getFrom());
        content.setText(notification.getContent());
        date.setText(notification.getDate());
        loadingView.success();
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