package com.quantumsit.sportsinc.ADMINS;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.CustomCalendar.ListViewAdapter;
import com.quantumsit.sportsinc.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class AdminStartClassActivity extends AppCompatActivity {

    EditText note_editText;
    CheckBox coach_name_checkBox;

    String note;
    int coach_id , class_id , attend;

    PopupWindow coach_reassign_popup_window;
    private Context admin_start_class_Context;
    private LinearLayout admin_start_class_rl;

    ProgressDialog progressDialog;
    List<String> coach_names_list;
    //ArrayAdapter<String> reassign_spinner_adapter;

    MaterialBetterSpinner reassign_spinner;
    String[] empty = {""};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_start_class);

        progressDialog = new ProgressDialog(AdminStartClassActivity.this);
        progressDialog.setMessage("Please wait......");

        admin_start_class_Context = AdminStartClassActivity.this;
        admin_start_class_rl =  findViewById(R.id.ll_adminstartclass);
        reassign_spinner =findViewById(R.id.reassignSpinner);

        note_editText = findViewById(R.id.attendancenotesEditText_admincurrentclass2);
        coach_name_checkBox = findViewById(R.id.coachNameCheckBox_admincurrentclass2);

        Button reassign = findViewById(R.id.reassign_admincurrentclass2);
        reassign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reassign_clicked();
            }
        });

        attend = 0;

        coach_names_list = fill_coach_names_list();


        ArrayAdapter<String> reassign_spinner_adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,empty);
        //reassign_spinner_adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        reassign_spinner.setAdapter(reassign_spinner_adapter);

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
    public void onBackPressed() {
        if (coach_reassign_popup_window != null && coach_reassign_popup_window.isShowing()) {
            coach_reassign_popup_window.dismiss();
        } else {
            super.onBackPressed();
        }
    }

    public void reassign_clicked() {

        note_editText.setEnabled(false);


        LayoutInflater inflater = (LayoutInflater) admin_start_class_Context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.window_reassign_coach_layout,null);

        coach_reassign_popup_window = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        if(Build.VERSION.SDK_INT>=21){
            coach_reassign_popup_window.setElevation(5.0f);
        }

        final ListView coach_reassign_listView = customView.findViewById(R.id.coachesListView);

        final ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<>(this,  android.R.layout.simple_list_item_1, coach_names_list);

        coach_reassign_listView.setAdapter(itemsAdapter);
        coach_reassign_popup_window.showAtLocation(admin_start_class_rl, Gravity.CENTER,0,0);
        coach_reassign_popup_window.setFocusable(false);
        coach_reassign_popup_window.setOutsideTouchable(true);
        coach_reassign_popup_window.update();

        coach_reassign_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TextView textView = view.findViewById(R.id.textView7);
                //String selected_coach = textView.getText().toString();
                String selected_coach = coach_reassign_listView.getItemAtPosition(position).toString();
                Toast.makeText(AdminStartClassActivity.this, selected_coach, Toast.LENGTH_SHORT).show();
                coach_name_checkBox.setText(selected_coach);
                coach_names_list = fill_coach_names_list();
                itemsAdapter.notifyDataSetChanged();
                note_editText.setEnabled(true);
                coach_reassign_popup_window.dismiss();
            }
        });


    }

    @SuppressLint("StaticFieldLeak")
    private List<String> fill_coach_names_list()  {

        progressDialog.show();

        final List<String> coach_names_list2 = new ArrayList<>();

        JSONObject where_info = new JSONObject();

        try {
            where_info.put("type",1);

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.selectData);
            HashMap<String,String> params = new HashMap<>();
            params.put("table","users");
            params.put("where",where_info.toString());
            httpCall.setParams(params);

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);

                    if(response != null){
                        for (int i=0; i<response.length(); i++) {
                            try {
                                JSONObject result = response.getJSONObject(i);
                                String coach = result.getString("name");
                                if(!coach.equals(coach_name_checkBox.getText().toString()))
                                    coach_names_list2.add(coach);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        //reassign_spinner_adapter.notifyDataSetChanged();
                        //reassign_spinner.notify();
                        ArrayAdapter<String> reassign_spinner_adapter = new ArrayAdapter<>(AdminStartClassActivity.this, android.R.layout.simple_dropdown_item_1line,coach_names_list2);
                        reassign_spinner.setAdapter(reassign_spinner_adapter);
                        progressDialog.dismiss();

                    } else {
                        Toast.makeText(AdminStartClassActivity.this,"An error occurred",Toast.LENGTH_SHORT);
                    }

                }
            }.execute(httpCall);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return coach_names_list2;
    }

}
