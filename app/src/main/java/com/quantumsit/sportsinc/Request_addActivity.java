package com.quantumsit.sportsinc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Aaa_looks.RecyclerView_Adapter_reportpayment;
import com.quantumsit.sportsinc.Aaa_looks.item_reports_payment;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.Side_menu_fragments.RequestsFragment;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Request_addActivity extends AppCompatActivity {

    MaterialBetterSpinner course_name_spinner, date_spinner;
    ArrayList<String> courses_list, date_list;

    GlobalVars globalVars;
    HashMap<String, List<String>> hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_add);

        globalVars = (GlobalVars) getApplication();

        course_name_spinner = findViewById(R.id.courseNameSpinner_requestadd);
        date_spinner = findViewById(R.id.dateSpinner_requestadd);

        courses_list = new ArrayList<>();
        date_list = new ArrayList<>();

        ArrayAdapter<CharSequence> courses_spinner_adapter = ArrayAdapter.createFromResource(this, R.array.course_names_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> dates_spinner_adapter = ArrayAdapter.createFromResource(this, R.array.date_array, android.R.layout.simple_spinner_item);

        course_name_spinner.setAdapter(courses_spinner_adapter);
        date_spinner.setAdapter(dates_spinner_adapter);

        fillGroupList();



    }


    @SuppressLint("StaticFieldLeak")
    private void fillGroupList() {

        final String[] group_name = new String[1];
        final int[] group_id = new int[1];
        final int[] course_id = new int[1];

        JSONObject where_info = new JSONObject();
        String on_condition;
        try {
            where_info.put("group_trainee.trainee_id",globalVars.getId());
            on_condition = "group_trainee.group_id = groups.id";
            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.joinData);
            HashMap<String,String> params = new HashMap<>();
            params.put("table1","group_trainee");
            params.put("table2","groups");
            params.put("where",where_info.toString());
            params.put("on",on_condition);

            httpCall.setParams(params);

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    try {

                        if (response != null) {
                            JSONObject result;

                            for (int i=0; i<response.length(); i++){
                                result = response.getJSONObject(i);
                                group_name[0] = result.getString("name");
                                course_id[0] = result.getInt("course_id");
                                group_id[0] = result.getInt("group_id");

                                fillCoursesList(course_id[0], group_name[0]);
                                fillDateList_first(group_id[0]);

                            }

                            //set_courses_spiiner_adapter();

                        } else {
                            Toast.makeText(Request_addActivity.this, "An error occurred ", Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @SuppressLint("StaticFieldLeak")
    private void fillCoursesList(int course_id, final String group_name) {

        final String[] course_name = new String[1];


        JSONObject where_info = new JSONObject();

        try {
            where_info.put("id",course_id);
            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.selectData);
            HashMap<String,String> params = new HashMap<>();
            params.put("table","courses");
            params.put("where",where_info.toString());

            httpCall.setParams(params);

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    try {

                        if (response != null) {
                            JSONObject result;

                            for (int i=0; i<response.length(); i++){
                                result = response.getJSONObject(i);
                                course_name[0] = result.getString("name");

                                courses_list.add(course_name[0] + "," + group_name);

                            }

                        } else {
                            Toast.makeText(Request_addActivity.this, "An error occurred ", Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @SuppressLint("StaticFieldLeak")
    private void fillDateList_first(int group_id) {

        JSONObject where_info = new JSONObject();

        try {
            where_info.put("group_id",group_id);
            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.selectData);
            HashMap<String,String> params = new HashMap<>();
            params.put("table","classes");
            params.put("where",where_info.toString());

            httpCall.setParams(params);

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    try {

                        if (response != null) {
                            JSONObject result;
                            Date date;
                            DateFormat outdateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            DateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd");

                            for (int i=0; i<response.length(); i++){
                                result = response.getJSONObject(i);
                                String date_available = result.getString("class_date");
                                date = DateFormat.parse(date_available);
                                date_available = outdateFormat.format(date);
                                date_list.add(date_available);
                            }

                        } else {
                            Toast.makeText(Request_addActivity.this, "An error occurred ", Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }


            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void send_clicked(View view) {
        onBackPressed();
        finish();
    }
}
