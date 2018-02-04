package com.quantumsit.sportsinc.COACHES;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ActivityAddRequest_coach extends AppCompatActivity {

    private static String TAG = ActivityAddRequest_coach.class.getSimpleName();

    GlobalVars globalVars;

    MaterialBetterSpinner request_for_spinner, course_name_spinner, class_number_spinner;

    EditText reason;

    String ClassDate = "";

    //note en diii fi el admin w fii el coach booooth

    List<String> courseList , classList;

    // Entities for filtering selections...
    ArrayList<item_courseSpinner_coach> courseEntities;
    ArrayList<item_classSpinner_coach> classEntities;

    HashMap<Integer , ArrayList<item_classSpinner_coach> > classesMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_add_request);

        globalVars = (GlobalVars) getApplication();

        request_for_spinner = findViewById(R.id.requestforSpinner_addreuestcoach);
        course_name_spinner = findViewById(R.id.courseNameSpinner_addreuestcoach);
        class_number_spinner = findViewById(R.id.classNumberSpinner_addreuestcoach);
        reason = findViewById(R.id.reasonEditText_addreuestcoach);


        ArrayAdapter<CharSequence> request_for_spinner_adapter = ArrayAdapter.createFromResource(this, R.array.request_for_array, android.R.layout.simple_spinner_item);
        request_for_spinner_adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        courseList = new ArrayList<>();
        ArrayAdapter<String> courseNameAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,courseList);
        courseNameAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        classList = new ArrayList<>();
        ArrayAdapter<String> classNameAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,classList);
        classNameAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        if(classList.size() == 0){
            classList.add("");
        }
        classesMap = new HashMap<>();

        initilizeCoursesSpinner();
        initilizeClassesSpinner();

        request_for_spinner.setAdapter(request_for_spinner_adapter);
        course_name_spinner.setAdapter(courseNameAdapter);
        class_number_spinner.setAdapter(classNameAdapter);

        course_name_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                ClassDate = "";
                class_number_spinner.setText("");
                classesFilter(classesMap.get(courseEntities.get(position).getCourse_id()));
            }
        });

        class_number_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                 ClassDate = classEntities.get(position).class_date;
            }
        });

    }

    private void classesFilter(ArrayList<item_classSpinner_coach> classSpinner_coachList) {
        classList.clear();
        classEntities = new ArrayList<>();
        for (item_classSpinner_coach item : classSpinner_coachList){
            classList.add(item.getClass_name());
            classEntities.add(item);
        }
        if(classList.size() == 0){
            classList.add("");
        }

        ArrayAdapter<String> classNameAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,classList);
        classNameAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        class_number_spinner.setAdapter(classNameAdapter);
    }

    private void initilizeCoursesSpinner(){
        try {
            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.coach_courses);


            HashMap<String, String> params = new HashMap<>();

            JSONObject where_info = new JSONObject();
            switch (globalVars.getType()){
                case 1:
                    where_info.put("groups.coach_id", globalVars.getId());
                    params.put("where",where_info.toString());
                    break;
                case 2:
                    where_info.put("groups.admin_id", globalVars.getId());
                    params.put("where",where_info.toString());
                    break;
            }

            httpCall.setParams(params);

            new HttpRequest() {
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    fillCoursesAdapter(response);
                }
            }.execute(httpCall);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void fillCoursesAdapter(JSONArray response) {
        courseList.clear();
        courseEntities = new ArrayList<>();
        if(response != null){
            try {
                for(int i=0;i<response.length();i++){
                    item_courseSpinner_coach entity =new item_courseSpinner_coach(response.getJSONObject(i));
                    courseEntities.add(entity);
                    courseList.add(entity.getCourse_name());
                }

                if(courseList.size() == 0){
                    courseList.add("");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ArrayAdapter<String> courseNameAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,courseList);
        courseNameAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        course_name_spinner.setAdapter(courseNameAdapter);

    }

    private void initilizeClassesSpinner(){
        try {
            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.coach_classes);

            HashMap<String, String> params = new HashMap<>();

            JSONObject where_info = new JSONObject();
            switch (globalVars.getType()){
                case 1:
                    where_info.put("groups.coach_id", globalVars.getId());
                    params.put("where",where_info.toString());
                    break;
                case 2:
                    where_info.put("groups.admin_id", globalVars.getId());
                    params.put("where",where_info.toString());
                    break;
            }

            httpCall.setParams(params);

            new HttpRequest() {
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    fillClassesAdapter(response);
                }
            }.execute(httpCall);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void fillClassesAdapter(JSONArray response) {
        classesMap.clear();
        if(response != null){
            try {
                for(int i=0;i<response.length();i++){
                    item_classSpinner_coach entity =new item_classSpinner_coach(response.getJSONObject(i));
                    if (classesMap.get(entity.course_id) == null){
                        classesMap.put(entity.course_id,new ArrayList<item_classSpinner_coach>());
                    }
                    classesMap.get(entity.course_id).add(entity);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public void send_clicked(View view) {
        checkRequestInfo();
        onBackPressed();
        finish();
    }

    private void checkRequestInfo(){
        String requestFor = request_for_spinner.getText().toString();
        String CourseName = course_name_spinner.getText().toString();

        if (requestFor.equals("")){
            show_toast("Select Request for what");
            return;
        }
        if (CourseName.equals("")){
            show_toast("Select Request for which course");
            return;
        }
        sendRequest();
    }

    private void sendRequest() {
        try {
            JSONObject where_info = new JSONObject();
            where_info.put("type",3);

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
                    if (response != null) {
                        try {
                            JSONObject object = response.getJSONObject(0);
                            int user_id = object.getInt("id");
                            insertRequest(user_id);
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

    private void insertRequest(int ReceiverID) {
        JSONObject values = new JSONObject();
        try {
            show_toast("sending...");
            String title = request_for_spinner.getText().toString();
            String Message = reason.getText().toString();
            String CourseName = course_name_spinner.getText().toString();
            String ClassName = class_number_spinner.getText().toString();


            values.put("title",title);
            values.put("content",Message);
            values.put("from_id",globalVars.getId());
            values.put("to_id",ReceiverID);
            values.put("course_name",CourseName);
            values.put("sub_course_name",ClassName);

            values.put("date_request",ClassDate);

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.insertData);
            HashMap<String,String> params = new HashMap<>();
            params.put("table","requests");
            params.put("notify","1");
            params.put("values",values.toString());

            httpCall.setParams(params);

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);

                    try {
                        if (response != null) {
                            String result = String.valueOf(response.get(0));
                            if (result.equals("ERROR"))
                                show_toast("Failed to send");
                            else
                                show_toast("Successfully sent");
                        }else {
                            show_toast("Failed to send");
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

    private void show_toast(String s) {
        Toast.makeText(ActivityAddRequest_coach.this,s,Toast.LENGTH_SHORT).show();
    }

}
