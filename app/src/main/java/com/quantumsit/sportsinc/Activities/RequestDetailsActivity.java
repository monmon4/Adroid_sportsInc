package com.quantumsit.sportsinc.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.COACHES.Entities.item_request_coach;
import com.quantumsit.sportsinc.CustomView.CustomLoadingView;
import com.quantumsit.sportsinc.R;
import com.quantumsit.sportsinc.util.ConnectionUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Bassam on 2/12/2018.
 */

public class RequestDetailsActivity extends AppCompatActivity {
    TextView subject ,content ,person ,date ,status,Course ,Group;
    LinearLayout mybuttons ,statusLayout;
    TextView accept , reject;
    boolean received = false;

    ProgressDialog progressDialog;
    CustomLoadingView loadingView;
    private int ID;
    private int requestID;
    private int requestStatus;
    private int loadingTime = 1200;
    private int REQUEST_UPDATE = 7;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Request details");

        loadingView = findViewById(R.id.LoadingView);
        loadingView.setOnRetryClick(new CustomLoadingView.OnRetryClick() {
            @Override
            public void onRetry() {
                retrieveRequest(ID);
            }
        });
        progressDialog = new ProgressDialog(RequestDetailsActivity.this);
        subject = findViewById(R.id.requestReviewSubject);
        content = findViewById(R.id.requestReviewContent);
        person = findViewById(R.id.requestReviewPerson);
        status = findViewById(R.id.requestReviewStatus);
        date = findViewById(R.id.requestReviewDate);
        Course = findViewById(R.id.requestReviewCourse);
        Group = findViewById(R.id.requestReviewGroup);
        statusLayout = findViewById(R.id.request_statusLayout);
        mybuttons = findViewById(R.id.RequestReviewButtons);
        accept = findViewById(R.id.RequestReviewAccept);
        reject = findViewById(R.id.RequestReviewReject);

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

    private void updateRequest(final int value){
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
                    progressDialog.dismiss();
                    Log.d("RequestDetailsUpdate",String.valueOf(response));
                    if (checkResponse(response)) {
                        mybuttons.setVisibility(View.GONE);
                        requestStatus = value;
                    }
                    else
                        Toast.makeText(getApplicationContext(),"An error occurred",Toast.LENGTH_LONG).show();

                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        final item_request_coach myRequest = (item_request_coach) getIntent().getSerializableExtra("MyRequest");
        final int notify_id = getIntent().getIntExtra("notify_id",-1);
        int requestType = getIntent().getIntExtra("requestType",-1);

        if (requestType == 1)
            received = true;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(myRequest != null){
                    fillView(myRequest);
                }
                else if (notify_id != -1){
                    retrieveRequest(notify_id);
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
                    retrieveRequest(notify_id);
                }
                else {
                    loadingView.fails();
                }
            }
        },loadingTime);
    }

    private void retrieveRequest(int notify_id) {
        if (!checkConnection()){
            ID = notify_id;
            loadingView.fails();
            loadingView.enableRetry();
            return;
        }
        try {
            received = true;
            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.join);
            JSONObject where_info = new JSONObject();
            where_info.put("requests.id", notify_id);

            String OnCondition = "requests.to_id = users.id";

            HashMap<String, String> params = new HashMap<>();
            params.put("table1", "requests");
            params.put("table2", "users");

            params.put("where", where_info.toString());
            params.put("on", OnCondition);

            httpCall.setParams(params);
            new HttpRequest() {
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    try {
                        if (response != null)
                            fillView(new item_request_coach(response.getJSONObject(0)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }.execute(httpCall);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillView(item_request_coach myRequest) {
       /* if(myRequest.getPersonType().equals("Trainee")) {
            LinearLayout groupLayout = findViewById(R.id.requestGroupLayout);
            groupLayout.setVisibility(View.VISIBLE);
            Group.setText(myRequest.getGroup());
        }*/

        requestID = myRequest.getRequest_ID();
        requestStatus = myRequest.getStatus();

        String Status = "Waiting";

        if (requestStatus == 1) {
            Status = "Accepted";
            status.setTextColor(Color.parseColor("#22a630"));
        }
        else if (requestStatus == 0) {
            Status = "Rejected";
            status.setTextColor(Color.parseColor("#df1b1c"));
        }
        else if (requestStatus == 2) {
            Status = "Waiting";
            status.setTextColor(Color.parseColor("#f98a03"));
        }

        if(requestStatus==2&&received) {
            mybuttons.setVisibility(View.VISIBLE);
            statusLayout.setVisibility(View.GONE);
        }else {
            mybuttons.setVisibility(View.GONE);
            statusLayout.setVisibility(View.VISIBLE);
        }

        status.setText(Status);
        subject.setText(myRequest.getRequest_for());
        person.setText(myRequest.getPerson());
        content.setText(myRequest.getContent());
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
        String ReqDate = formatter.format(myRequest.getRequestDate());
        date.setText(ReqDate);
        Course.setText(myRequest.getCourse());
        loadingView.success();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private boolean checkResponse(JSONArray response) {
        if (response != null){
            try {
                String result = response.getString(0);
                if (!result.equals("ERROR"))
                    return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean checkConnection() {
        // first, check connectivity
        if (ConnectionUtilities
                .checkInternetConnection(this)) {
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("request_status",requestStatus);
        setResult(AppCompatActivity.RESULT_OK ,resultIntent);
        finish();
    }
}
