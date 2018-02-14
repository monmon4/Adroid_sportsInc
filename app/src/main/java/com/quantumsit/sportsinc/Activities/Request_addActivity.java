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
import java.util.Date;
import java.util.HashMap;

public class Request_addActivity extends AppCompatActivity {

    EditText reason_editText;
    MaterialBetterSpinner date_spinner;
    ArrayList<String> date_list;

    GlobalVars globalVars;

    ProgressDialog progressDialog;
    String[] empty = {""};

    int group_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_add);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        globalVars = (GlobalVars) getApplication();
        progressDialog = new ProgressDialog(Request_addActivity.this);
        progressDialog.setMessage("Please wait.....");

        date_spinner = findViewById(R.id.dateSpinner_requestadd);
        reason_editText = findViewById(R.id.reasonEditText_requestadd);

        date_list = new ArrayList<>();

        ArrayAdapter<String> dates_spinner_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, empty);
        date_spinner.setAdapter(dates_spinner_adapter);

        progressDialog.show();
        fillDateList();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @SuppressLint("StaticFieldLeak")
    private void fillDateList() {

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
                            DateFormat outdateFormat = new SimpleDateFormat("dd MMMM, yyyy");
                            DateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd");

                            for (int i=0; i<response.length(); i++){
                                result = response.getJSONObject(i);
                                int status = result.getInt("status");
                                String date_class = "";
                                if (status == 3 || status == 5) {

                                    date_class = result.getString("class_date");
                                    date = DateFormat.parse(date_class);
                                    date_class = outdateFormat.format(date);


                                } else if (status == 2) {
                                    date_class = result.getString("postpone_date");
                                    date = DateFormat.parse(date_class);
                                    date_class = outdateFormat.format(date);
                                    date_class += "  postponed";
                                }

                                if (!date_class.equals("")){
                                    date_list.add(date_class);
                                }
                            }

                            setDate_spinnerAdapter();

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


    private void setDate_spinnerAdapter() {
        ArrayAdapter<String> dates_spinner_adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, date_list);
        date_spinner.setAdapter(dates_spinner_adapter);
        progressDialog.dismiss();
    }

    public void send_clicked() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Request_addActivity.this,
                R.style.MyAlertDialogStyle);
        builder.setTitle(Request_addActivity.this.getResources().getString(R.string.app_name));
        builder.setCancelable(false);
        builder.setMessage("     Are you sure?");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        send_to_DB();
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

        if(date_spinner.getText().toString().equals("")) {
            Toast.makeText(Request_addActivity.this, "Please select a date", Toast.LENGTH_SHORT).show();

        } else {
            String date_request = date_spinner.getText().toString();
            try {
                date = DateFormat.parse(date_request);
                date_request = outdateFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            String title = "request for absence";
            String content = reason_editText.getText().toString();
            if (content.equals("")){ content = "none";}
            int type = 1;
            int from_id = globalVars.getId();

            JSONObject values_info = new JSONObject();
            try {
                values_info.put("type",type);
                values_info.put("from_id",from_id);
                values_info.put("to_id",to_id);
                values_info.put("title",title);
                values_info.put("course_name","");
                values_info.put("sub_course_name","");
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
                            onBackPressed();
                            finish();
                        } else {
                            show_toast("An error occurred ");
                        }
                    }

                }.execute(httpCall);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
        Toast.makeText(Request_addActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
