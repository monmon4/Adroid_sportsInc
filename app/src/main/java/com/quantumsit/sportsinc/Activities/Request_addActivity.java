package com.quantumsit.sportsinc.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Request_addActivity extends AppCompatActivity {

    EditText reason_editText;
    MaterialBetterSpinner date_spinner, request_for_spinner;
    ArrayList<String> date_list, group_list/*elly hwa esmo group zman*/;
    ArrayList<Integer> group_id_list;

    GlobalVars globalVars;

    ProgressDialog progressDialog;
    String[] empty = {""};

    int group_id;
    private int selectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_add);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Send a request");

        globalVars = (GlobalVars) getApplication();
        progressDialog = new ProgressDialog(Request_addActivity.this);
        progressDialog.setMessage("Please wait.....");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        date_spinner = findViewById(R.id.dateSpinner_requestadd);
        request_for_spinner = findViewById(R.id.requestforSpinner_requestadd);
        reason_editText = findViewById(R.id.reasonEditText_requestadd);

        date_list = new ArrayList<>();
        group_list = new ArrayList<>();
        group_id_list = new ArrayList<>();

        ArrayAdapter<String> dates_spinner_adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, empty);
        ArrayAdapter<CharSequence> requestfor_spinner_adapter = ArrayAdapter.createFromResource(this, R.array.trainee_request_for_array, android.R.layout.simple_dropdown_item_1line);
        date_spinner.setAdapter(dates_spinner_adapter);
        request_for_spinner.setAdapter(requestfor_spinner_adapter);


        request_for_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                progressDialog.show();

                if (position == 0) {
                    if(!date_spinner.getText().toString().equals("")){
                        date_spinner.setText("");
                    }
                    date_spinner.setHint("Date");
                    fillDateList();
                } else {
                    if(!date_spinner.getText().toString().equals("")){
                        date_spinner.setText("");
                    }
                    date_spinner.setHint("Course");
                    fillClassList();
                }
            }


        });


        date_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String request_for = request_for_spinner.getText().toString();
                if (request_for.equals("")) {
                    show_toast("Please choose whether the request is for absence or switch course");
                }
            }
        });

        date_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selectedPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @SuppressLint("StaticFieldLeak")
    private void fillDateList() {
        date_list.clear();
        JSONObject where_info = new JSONObject();
        String on_condition;
        try {
            where_info.put("group_trainee.trainee_id",globalVars.getId());
            on_condition = "group_trainee.group_id = classes.group_id";
            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.joinData);
            HashMap<String,String> params = new HashMap<>();
            params.put("table1","group_trainee");
            params.put("table2","classes");
            params.put("where",where_info.toString());
            params.put("on",on_condition);

            httpCall.setParams(params);

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    try {

                        if (response != null) {
                            JSONObject first_result = response.getJSONObject(0);
                            group_id = first_result.getInt("group_id");
                            //status = 2 postponed, 3 upcoming, 5 current;
                            JSONObject result;
                            Date date;
                            DateFormat outdateFormat = new SimpleDateFormat("dd MMMM, yyyy", Locale.ENGLISH);
                            DateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

                            for (int i=0; i<response.length(); i++){
                                result = response.getJSONObject(i);
                                int status = result.getInt("status");
                                String date_class = "";
                                if (status == 3 ) {
                                    date_class = result.getString("class_date");
                                    date = DateFormat.parse(date_class);
                                    date_class = outdateFormat.format(date);

                                }

                                if (!date_class.equals("")){
                                    date_list.add(date_class);
                                }
                            }
                            setDate_spinnerAdapter(0);

                        } else {
                            date_list.add("NO Sessions");
                            setDate_spinnerAdapter(0); }


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

    private void setDate_spinnerAdapter(int type) {
        ArrayAdapter<String> dates_spinner_adapter;
        if(type == 0) {
            dates_spinner_adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, date_list);
        } else {
            dates_spinner_adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, group_list);

        }
        date_spinner.setAdapter(dates_spinner_adapter);
        progressDialog.dismiss();
    }


    @SuppressLint("StaticFieldLeak")
    private void fillClassList() {
        date_list.clear();
            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.traineeSwitchGroup);
            HashMap<String,String> params = new HashMap<>();
            params.put("trainee_id",String.valueOf(globalVars.getId()));

            httpCall.setParams(params);

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    try {

                        if (response != null) {
                            for (int i=0; i<response.length(); i++) {
                                JSONObject result = response.getJSONObject(i);
                                String group_name = result.getString("name");
                                int group_id = result.getInt("id");
                                group_id_list.add(group_id);
                                group_list.add(group_name);
                            }
                            setDate_spinnerAdapter(1);

                        } else {
                            group_list.add("No Courses available");
                            setDate_spinnerAdapter(1);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }.execute(httpCall);

    }



    public void send_clicked() {
        String selectedTitle = request_for_spinner.getText().toString();
        if (selectedTitle.equals("")) {
            show_toast("Please choose what is the request for");
            return;
        }
        if (date_spinner.getText().toString().equals("")){
            switch (selectedTitle){
                case "Absence":
                    show_toast("Please select session data");
                    break;
                case "Switch class":
                    show_toast("Please select which course");
                    break;
            }
            if (!selectedTitle.equals("Other"))
                return;
        }
        if (reason_editText.getText().toString().equals("")) {
            Toast.makeText(Request_addActivity.this, "Please specify your reason", Toast.LENGTH_SHORT).show();
            return;
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(Request_addActivity.this,
                R.style.MyAlertDialogStyle);
        builder.setTitle(Request_addActivity.this.getResources().getString(R.string.app_name));
        builder.setCancelable(false);
        builder.setMessage("     Are you sure?");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (request_for_spinner.getText().toString().equals("Absence"))
                            send_to_DB();
                        else
                            insert_to_db();
                        dialogInterface.dismiss();
                    }
                });
        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.show();
    }


    @SuppressLint("StaticFieldLeak")
    private void send_to_DB() {

        JSONObject where_info = new JSONObject();
        String on_condition;

        try {
            where_info.put("groups.id",group_id);
            on_condition = "groups.coach_id = users.id";
            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.joinData);
            HashMap<String,String> params = new HashMap<>();
            params.put("table1","groups");
            params.put("table2","users");
            params.put("where",where_info.toString());
            params.put("on",on_condition);

            httpCall.setParams(params);

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    if (response != null) {
                        try {
                            JSONObject object = response.getJSONObject(0);
                            int user_id = object.getInt("id");
                            insert_to_db(user_id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                        show_toast("Error in sending");
                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public void insert_to_db(int to_id) {

        Date date;
        DateFormat outdateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat DateFormat = new SimpleDateFormat("dd MMMM, yyyy");
        String date_request = "";

        if(date_spinner.getText().toString().equals("")) {
            Toast.makeText(Request_addActivity.this, "Please select a date", Toast.LENGTH_SHORT).show();

        } else if (request_for_spinner.getText().toString().equals("Absence")) {
            date_request = date_spinner.getText().toString();
            try {
                date = DateFormat.parse(date_request);
                date_request = outdateFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String title = "request for " + request_for_spinner.getText().toString();
            String content = reason_editText.getText().toString();
            if (content.equals("")){ content = "none";}
            int type = 1;
            int from_id = globalVars.getId();

            show_toast("Sending...");
            JSONObject values_info = new JSONObject();
            try {
                values_info.put("Type",type);
                values_info.put("request_type",6);
                values_info.put("from_id",from_id);
                values_info.put("to_id",to_id);
                values_info.put("title",title);
                values_info.put("course_name","");
                values_info.put("sub_course_name","");
                values_info.put("session_id",group_id);
                values_info.put("content",content);
                values_info.put("date_request",date_request);

                HttpCall httpCall = new HttpCall();
                httpCall.setMethodtype(HttpCall.POST);
                httpCall.setUrl(Constants.insertData);
                HashMap<String,String> params = new HashMap<>();
                params.put("table","requests");
                params.put("notify","1");
                params.put("manager","1");
                params.put("values",values_info.toString());

                httpCall.setParams(params);

                new HttpRequest(){
                    @Override
                    public void onResponse(JSONArray response) {
                        super.onResponse(response);
                        Log.d("AddRequest","response : "+String.valueOf(response));
                        if (response != null) {
                            show_toast("Success ");
                        } else {
                            show_toast("An error occurred ");
                        }
                    }

                }.execute(httpCall);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            onBackPressed();
            finish();
        }
    }


    @SuppressLint("StaticFieldLeak")
    public void insert_to_db() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String date_request = df.format(c.getTime());
        int switchGroup = 0;
        int request_type = 3;
        String title = "request for " + request_for_spinner.getText().toString();
        if (request_for_spinner.getText().toString().equals("Switch class")) {
            title += " with " + date_spinner.getText().toString();
            switchGroup = group_id_list.get(selectedPosition) ;
            request_type++;
        }

        String content = reason_editText.getText().toString();
        if (content.equals("")){ content = "none";}
        int type = 1;
        int from_id = globalVars.getId();
        show_toast("Sending...");

        JSONObject values_info = new JSONObject();
        try {
            values_info.put("Type",type);
            values_info.put("request_type",request_type);
            values_info.put("from_id",from_id);
            values_info.put("title",title);
            values_info.put("course_name","");
            values_info.put("sub_course_name","");
            if (switchGroup != 0)
                values_info.put("session_id",switchGroup);
            values_info.put("content",content);
            values_info.put("date_request",date_request);

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.insertData);
            HashMap<String,String> params = new HashMap<>();
            params.put("table","requests");
            params.put("notify","1");
            params.put("values",values_info.toString());

            httpCall.setParams(params);

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    Log.d("AddRequest","response : "+String.valueOf(response));
                    if (response != null) {
                        show_toast("Success ");
                    } else {
                        show_toast("An error occurred ");
                    }
                }

            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.send_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.send_message){
            send_clicked();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void show_toast(String msg){
        progressDialog.dismiss();
        Toast.makeText(Request_addActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
