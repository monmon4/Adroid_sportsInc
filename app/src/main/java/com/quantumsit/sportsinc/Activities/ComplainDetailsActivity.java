package com.quantumsit.sportsinc.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.Entities.ComplainEntity;
import com.quantumsit.sportsinc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;

public class ComplainDetailsActivity extends AppCompatActivity {

    TextView date ,person ,content,subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain_details);

        getSupportActionBar().setTitle("Complain Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        date = findViewById(R.id.complainReviewDate);
        person = findViewById(R.id.complainReviewPerson);
        content = findViewById(R.id.complainReviewContent);
        subject = findViewById(R.id.complainReviewSubject);

    }

    @Override
    protected void onResume() {
        super.onResume();
        ComplainEntity complain = (ComplainEntity) getIntent().getSerializableExtra("MyComplain");
        int notify_id = getIntent().getIntExtra("notify_id",-1);

        if (notify_id != -1)
            retrieveComplain(notify_id);
        else if (complain != null)
            fillView(complain);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int notify_id = intent.getIntExtra("notify_id",-1);
        if (notify_id != -1)
            retrieveComplain(notify_id);
    }

    private void retrieveComplain(int notify_id) {
        try {
            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.join);
            JSONObject where_info = new JSONObject();
            where_info.put("complains.id",notify_id);
            String OnCondition = "complains.user_id = users.id";

            HashMap<String, String> params = new HashMap<>();
            params.put("table1", "complains");
            params.put("table2", "users");

            params.put("on", OnCondition);

            httpCall.setParams(params);
            new HttpRequest() {
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    try {
                        if (response != null)
                            fillView(new ComplainEntity(response.getJSONObject(0)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }.execute(httpCall);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillView(ComplainEntity complain) {
        subject.setText(complain.getTitle());
        SimpleDateFormat format = new SimpleDateFormat("dd MMM, yyyy hh:mm");
        String str = format.format(complain.getDate());
        date.setText(str);
        person.setText(complain.getPersonName());
        content.setText(complain.getContent());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}