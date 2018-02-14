package com.quantumsit.sportsinc.ADMINS;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AdminStartClassActivity extends AppCompatActivity {

    EditText note_editText;
    CheckBox coach_name_checkBox;

    String note;
    int coach_id , class_id , attend;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_start_class);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressDialog = new ProgressDialog(AdminStartClassActivity.this);
        progressDialog.setMessage("Please wait......");

        note_editText = findViewById(R.id.attendancenotesEditText_admincurrentclass2);
        coach_name_checkBox = findViewById(R.id.coachNameCheckBox_admincurrentclass2);

        attend = 0;

        item_current_classes classes = (item_current_classes) getIntent().getSerializableExtra("adminClass");

        if (classes != null){
            fillView(classes);
        }
    }

    private void fillView(item_current_classes classes) {
        coach_name_checkBox.setText(classes.getCoach_Name());
        coach_id = classes.getCoach_id();
        class_id = classes.getId();
    }

    public void done_pressed(View view) {
        /*
        progressDialog.show();
        note = note_editText.getText().toString();
        boolean checked = coach_name_checkBox.isChecked();
        if (checked)
            attend = 1;
        else
            attend = 0;

        insertCoachAttend();
        */
        onBackPressed();
    }

    private  void insertCoachAttend(){
        try {

            JSONObject values = new JSONObject();
            values.put("class_id",class_id);
            values.put("user_id",coach_id);
            values.put("attend",attend);
            values.put("notes",note);

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.insertData);

            HashMap<String, String> params = new HashMap<>();
            params.put("table","class_info");
            params.put("values",values.toString());

            httpCall.setParams(params);

            new HttpRequest() {
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    if(checkResponse(response)) {
                        //updateClassStatus();
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(AdminStartClassActivity.this, "Failed To Attend the coach", Toast.LENGTH_SHORT).show();
                    }
                }
            }.execute(httpCall);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
