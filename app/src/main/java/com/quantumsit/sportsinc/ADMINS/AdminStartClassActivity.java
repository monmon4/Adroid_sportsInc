package com.quantumsit.sportsinc.ADMINS;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.quantumsit.sportsinc.Aaa_data.DB_Sqlite_Handler;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Aaa_data.MyClass_info;
import com.quantumsit.sportsinc.Aaa_data.Rule_info;
import com.quantumsit.sportsinc.Aaa_data.StartClass_info;
import com.quantumsit.sportsinc.Aaa_data.Trainees_info;
import com.quantumsit.sportsinc.Activities.HomeActivity;
import com.quantumsit.sportsinc.Activities.RegisterActivity;
import com.quantumsit.sportsinc.Adapters.item_checkbox;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.CustomCalendar.ListViewAdapter;
import com.quantumsit.sportsinc.CustomView.CustomLoadingView;
import com.quantumsit.sportsinc.R;
import com.quantumsit.sportsinc.util.ConnectionUtilities;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class AdminStartClassActivity extends AppCompatActivity {

    EditText attendance_note_editText, rules_note_editText;
    CheckBox coach_name_checkBox, rules_checkBox;

    int coach_id , class_id , attend, rules, new_coach_id, old_coach_id;

    PopupWindow coach_reassign_popup_window;

    ProgressDialog progressDialog;
    CustomLoadingView loadingView;
    List<String> coach_names_list;

    HashMap<String, Integer> coaches_using_names;
    HashMap<Integer, String> coaches_using_ids;
    boolean reassign;
    //ArrayAdapter<String> reassign_spinner_adapter;

    MaterialBetterSpinner reassign_spinner;
    String[] empty = {""};

    StartClass_info startClass_info;
    GlobalVars globalVars;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_start_class);

        globalVars = (GlobalVars) getApplication();
        startClass_info = globalVars.getMyDB().getStartClass(class_id);
        reassign = false;

        loadingView = findViewById(R.id.LoadingView);
        loadingView.setOnRetryClick(new CustomLoadingView.OnRetryClick() {
            @Override
            public void onRetry() {
                fill_coach_names_list();
            }
        });

        progressDialog = new ProgressDialog(AdminStartClassActivity.this);
        progressDialog.setMessage("Please wait......");

        //admin_start_class_Context = AdminStartClassActivity.this;
        //admin_start_class_rl =  findViewById(R.id.ll_adminstartclass);
        reassign_spinner =findViewById(R.id.reassignSpinner);

        attendance_note_editText = findViewById(R.id.attendancenotesEditText_admincurrentclass2);
        rules_note_editText = findViewById(R.id.rulesnotesEditText_admincurrentclass);
        coach_name_checkBox = findViewById(R.id.coachNameCheckBox_admincurrentclass2);
        rules_checkBox = findViewById(R.id.rulesCheckBox_admincurrentclass);

        //Button reassign = findViewById(R.id.reassign_admincurrentclass2);

        coach_names_list = fill_coach_names_list();
        coaches_using_names = new HashMap<>();
        coaches_using_ids = new HashMap<>();


        ArrayAdapter<String> reassign_spinner_adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,empty);
        //reassign_spinner_adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        reassign_spinner.setAdapter(reassign_spinner_adapter);

        item_current_classes classes = (item_current_classes) getIntent().getSerializableExtra("adminClass");

        if (classes != null){
            fillView(classes);
        }

        reassign_enabling();
        coach_name_checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reassign_enabling();
            }
        });

        if (savedInstanceState == null)
            coach_names_list = fill_coach_names_list();
        else
            coach_names_list = savedInstanceState.getStringArrayList("CoachesList");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("CoachesList", (ArrayList<String>) coach_names_list);
    }


    private void fillView(item_current_classes classes) {
        coach_name_checkBox.setText(classes.getCoach_Name());
        coach_id = classes.getCoach_id();
        old_coach_id = classes.getCoach_id();
        class_id = classes.getId();

        check_reassign (class_id);
        set_coach_attendance();
        set_rules_checking();
    }


    private void set_coach_attendance() {
        Trainees_info coach_info_old = globalVars.getMyDB().getCoachInfo(class_id);
        if (coach_info_old != null) {
            coach_name_checkBox.setChecked(coach_info_old.getSelected());
            attendance_note_editText.setText(coach_info_old.getTrainee_note());
            coach_name_checkBox.setText(coach_info_old.getTrainee_name());
            if (reassign) {
                String coach_name_new = coach_info_old.getTrainee_name() + "(Reassigned)";
                coach_name_checkBox.setText(coach_name_new);
            }
        }

    }

    private void set_rules_checking() {
        Rule_info rule_info = globalVars.getMyDB().getRule(class_id);
        if (rule_info != null) {
            rules_checkBox.setChecked(rule_info.getSelected());
            rules_note_editText.setText(rule_info.getRule_note());
        }

    }

    private void update_coach_attendance_local(Trainees_info info) {
        DB_Sqlite_Handler handler = globalVars.getMyDB();
        handler.updateTrainee(info);

    }

    private void add_coach_attendance_local(Trainees_info info) {
        DB_Sqlite_Handler handler = globalVars.getMyDB();
        handler.addTrainee(info);

    }

    private void update_rule_checking_local(Rule_info info) {
        DB_Sqlite_Handler handler = globalVars.getMyDB();
        handler.updateRule(info);

    }

    private void add_rule_checking_local(Rule_info info) {
        DB_Sqlite_Handler handler = globalVars.getMyDB();
        handler.addRule(info);

    }

    public void done_pressed(View view) {

        String attendance_note = attendance_note_editText.getText().toString();
        String rules_note = rules_note_editText.getText().toString();
        boolean attendance_checked = coach_name_checkBox.isChecked();
        boolean rules_checked = rules_checkBox.isChecked();



        String reassign_coach = reassign_spinner.getText().toString();

        if (!reassign_coach.equals("Reassign to")) {
            new_coach_id = coaches_using_names.get(reassign_coach);
            coach_id = new_coach_id;

            if (!reassign)
                insert_reassign_coach (class_id,old_coach_id, new_coach_id);
            else
                update_reassign_coach(class_id, old_coach_id,new_coach_id);
        }


        if (attendance_checked)
            attend = 1;
        else
            attend = 0;


        if (rules_checked)
            rules = 1;
        else
            rules = 0;

        Trainees_info coach_info_old = globalVars.getMyDB().getCoachInfo(class_id);
        Rule_info rule = globalVars.getMyDB().getRule(class_id);

        if (coach_info_old == null)
            add_coach_attendance_local(new Trainees_info(0, coach_id, coaches_using_ids.get(coach_id), class_id, attend, 0, attendance_note ));
        else
            update_coach_attendance_local(new Trainees_info(coach_info_old.getID(), coach_id, coaches_using_ids.get(coach_id), class_id, attend, 0, attendance_note ));


        if (rule == null)
            add_rule_checking_local(new Rule_info(0, class_id, rules, rules_note, globalVars.getId()));
        else
            update_rule_checking_local(new Rule_info(rule.getRule_id(), class_id, rules, rules_note, globalVars.getId()));

        onBackPressed();
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
        finish();
        if (coach_reassign_popup_window != null && coach_reassign_popup_window.isShowing()) {
            coach_reassign_popup_window.dismiss();
        } else {
            super.onBackPressed();
        }
    }

    private boolean checkConnection() {
        // first, check connectivity
        if (ConnectionUtilities
                .checkInternetConnection(this)) {
            return true;
        }
        return false;
    }

    @SuppressLint("StaticFieldLeak")
    private List<String> fill_coach_names_list()  {
        if (!checkConnection()){
            loadingView.enableRetry();
            loadingView.fails();
            return new ArrayList<>();
        }

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
                                int coach_id = result.getInt("id");

                                coaches_using_names.put(coach, coach_id);
                                coaches_using_ids.put(coach_id, coach);

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

                    } else {
                        Toast.makeText(AdminStartClassActivity.this,"An error occurred",Toast.LENGTH_SHORT);
                    }

                    loadingView.success();
                }
            }.execute(httpCall);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return coach_names_list2;
    }

    @SuppressLint("StaticFieldLeak")
    private void insert_reassign_coach (int session_id, int old_coach_id, int new_coach_id) {

        JSONObject info = new JSONObject();
        try {
            info.put("class_id",session_id);
            info.put("old_coach_id",old_coach_id);
            info.put("new_coach_id",new_coach_id);

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.insertData);
            HashMap<String,String> params = new HashMap<>();
            params.put("table","reassign_coach");
            params.put("values",info.toString());

            httpCall.setParams(params);

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);

                    if(response != null){
                        Toast.makeText(AdminStartClassActivity.this, "successfully reassigned", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AdminStartClassActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();

                    }

                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void update_reassign_coach (int session_id, int old_coach_id, int new_coach_id) {

        JSONObject where = new JSONObject();
        JSONObject values = new JSONObject();
        try {
            where.put("class_id",session_id);
            values.put("new_coach_id",new_coach_id);

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.updateData);
            HashMap<String,String> params = new HashMap<>();
            params.put("table","reassign_coach");
            params.put("where",where.toString());
            params.put("values",values.toString());

            httpCall.setParams(params);

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);

                    if(response != null){
                        Toast.makeText(AdminStartClassActivity.this, "successfully reassigned", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AdminStartClassActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();

                    }

                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void check_reassign (int id_class){
        JSONObject where = new JSONObject();
        try {
            where.put("class_id",id_class);

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.selectData);
            HashMap<String,String> params = new HashMap<>();
            params.put("table","reassign_coach");
            params.put("where",where.toString());

            httpCall.setParams(params);

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);

                    if(response != null){
                        try {
                            new_coach_id = response.getJSONObject(0).getInt("new_coach_id");
                            String coach_new_name = coaches_using_ids.get(new_coach_id);
                            coach_name_checkBox.setText(coach_new_name);
                            reassign = true;

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        reassign = false;
                    }

                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void reassign_enabling(){
        if(coach_name_checkBox.isChecked()) {
            reassign_spinner.setEnabled(false);
            reassign_spinner.setClickable(false);
        } else {
            reassign_spinner.setEnabled(true);
            reassign_spinner.setClickable(true);
        }
    }



}
