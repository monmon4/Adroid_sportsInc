package com.quantumsit.sportsinc.ADMINS;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.ProfileActivity;
import com.quantumsit.sportsinc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminStartClassActivity extends AppCompatActivity {

    EditText note_editText;
    CheckBox coach_name_checkBox;

    String note;
    int coach_id , class_id , attend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_start_class);

        note_editText = findViewById(R.id.notesEditText_admincurrentclass);
        coach_name_checkBox = findViewById(R.id.coachNameCheckBox_admincurrentclass);

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

        note = note_editText.getText().toString();
        boolean checked = coach_name_checkBox.isChecked();
        if (checked)
            attend = 1;
        else
            attend = 0;

        insertCoachAttend();

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
                        updateClassStatus();
                    }else {
                        Toast.makeText(AdminStartClassActivity.this, "Failed To Attend the coach", Toast.LENGTH_SHORT).show();
                    }
                }
            }.execute(httpCall);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateClassStatus() {
        try {
            JSONObject where_info = new JSONObject();
            where_info.put("id",class_id);

            JSONObject values = new JSONObject();
            values.put("status",0);

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.updateData);

            HashMap<String, String> params = new HashMap<>();
            params.put("table","classes");
            params.put("values",values.toString());
            params.put("where", where_info.toString());

            httpCall.setParams(params);

            new HttpRequest() {
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    if(checkResponse(response)) {
                        Toast.makeText(AdminStartClassActivity.this,"class has been started",Toast.LENGTH_SHORT).show();
                        onBackPressed();
                        finish();
                    }else {
                        Toast.makeText(AdminStartClassActivity.this, "Failed To start the class", Toast.LENGTH_SHORT).show();
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
                if (result.equals("DONE"))
                    return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
