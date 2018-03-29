package com.quantumsit.sportsinc.COACHES.RequestFragment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.quantumsit.sportsinc.COACHES.Entities.item_classSpinner_coach;
import com.quantumsit.sportsinc.COACHES.Entities.item_courseSpinner_coach;
import com.quantumsit.sportsinc.COACHES.Entities.item_request_coach;
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

    Date current_date;

    item_request_coach added_request ;
    private int classPosition;
    private int requestType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_add_request);

        getSupportActionBar().setTitle("Compose");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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

        request_for_spinner.setAdapter(request_for_spinner_adapter);
        course_name_spinner.setAdapter(courseNameAdapter);
        class_number_spinner.setAdapter(classNameAdapter);

        request_for_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                requestType = position;
            }
        });

        course_name_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                ClassDate = "";
                classPosition = -1;
                class_number_spinner.setText("");
                classesFilter(classesMap.get(courseEntities.get(position).getCourse_id()));
            }
        });

        class_number_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (classEntities.size() == 0) {
                    class_number_spinner.setText("");
                    return;
                }
                 ClassDate = classEntities.get(position).getClass_date();
                 classPosition = position;
                 class_number_spinner.setText(ClassDate);
            }
        });

        if (savedInstanceState == null){
            initilizeCoursesSpinner();
            initilizeClassesSpinner();
        }
        else
            fillBySavedState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("CourseEntitiesList",courseEntities);
        outState.putSerializable("ClassesEntitiesList",classEntities);
        outState.putStringArrayList("CourseNameList", (ArrayList<String>) courseList);
        outState.putStringArrayList("ClassesNameList", (ArrayList<String>) classList);
        outState.putSerializable("HashMap",classesMap);
    }


    private void fillBySavedState(Bundle savedInstanceState) {
        courseEntities = (ArrayList<item_courseSpinner_coach>) savedInstanceState.getSerializable("CourseEntitiesList");
        classEntities = (ArrayList<item_classSpinner_coach>) savedInstanceState.getSerializable("ClassesEntitiesList");
        ArrayList<String> list1 = savedInstanceState.getStringArrayList("CourseNameList");
        ArrayList<String> list2 = savedInstanceState.getStringArrayList("ClassesNameList");
        HashMap<Integer , ArrayList<item_classSpinner_coach> > hashMap = (HashMap<Integer, ArrayList<item_classSpinner_coach>>) savedInstanceState.getSerializable("HashMap");
        classesMap.putAll(hashMap);
        courseList.addAll(list1);
        classList.addAll(list2);

        ArrayAdapter<String> courseNameAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,courseList);
        courseNameAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        course_name_spinner.setAdapter(courseNameAdapter);

        ArrayAdapter<String> classNameAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,classList);
        classNameAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        class_number_spinner.setAdapter(classNameAdapter);

    }



    private void classesFilter(ArrayList<item_classSpinner_coach> classSpinner_coachList) {
        classList.clear();
        classEntities = new ArrayList<>();
        current_date = Calendar.getInstance().getTime();
        DateFormat date_format = new SimpleDateFormat("dd/MM/yyyy");
        String current_date_string = date_format.format(current_date);
        String[] current_date_split = current_date_string.split("/");

        if(classSpinner_coachList!= null) {
            for (item_classSpinner_coach item : classSpinner_coachList){
                if(item.getClass_status() == 3) {
                    String class_date = item.getClass_date();
                    String[] date_split = class_date.split("/");

                    if(date_split[1].equals(current_date_split[1])) {
                        if(Integer.valueOf(date_split[0]) - Integer.valueOf(current_date_split[0]) > 2) {
                            classList.add(item.getClass_name() + " , " + item.getClass_date());
                            classEntities.add(item);
                        }
                    } else {
                        classList.add(item.getClass_name() + " , " + item.getClass_date());
                        classEntities.add(item);
                    }
                }

            }
        } else {
            classList.add("");
        }

        if(classList.size() == 0){

            //class_number_spinner.setHint(R.string.no_sessions);
            classList.add(getString(R.string.no_sessions));
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

            where_info.put("groups.coach_id", globalVars.getId());
            params.put("where",where_info.toString());


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
                /*case 2:
                    where_info.put("groups.admin_id", globalVars.getId());
                    params.put("where",where_info.toString());
                    break;*/
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
                    if (classesMap.get(entity.getCourse_id()) == null){
                        classesMap.put(entity.getCourse_id(),new ArrayList<item_classSpinner_coach>());
                    }
                    classesMap.get(entity.getCourse_id()).add(entity);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public void send_clicked() {
        checkRequestInfo();
    }

    private void checkRequestInfo(){
        String requestFor = request_for_spinner.getText().toString();
        String CourseName = course_name_spinner.getText().toString();
        String ClassNum = class_number_spinner.getText().toString();

        if (requestFor.equals(""))
            show_toast(getString(R.string.select_request_for_what));
         else if (CourseName.equals(""))
            show_toast(getString(R.string.select_request_for_which_level));
        else if (ClassNum.equals("") || ClassNum.equals(getString(R.string.no_sessions)))
            show_toast(getString(R.string.select_the_session_date));
        else
            sendRequest();



    }

    private void sendRequest() {
        JSONObject values = new JSONObject();
        try {
            show_toast("sending...");
            String title = request_for_spinner.getText().toString();
            String Message = reason.getText().toString();
            String CourseName = course_name_spinner.getText().toString();
            //String[] class_split = class_number_spinner.getText().toString().split(",");
            String ClassName = "";
            if (classPosition != -1)
                ClassName = classEntities.get(classPosition).getClass_name();//class_split[0].trim();

            String date_request = ClassDate;
            DateFormat outdateFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat DateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = DateFormat.parse(date_request);
            date_request = outdateFormat.format(date);
            Date c = Calendar.getInstance().getTime();
            String creation_date = outdateFormat.format(c);

            added_request = new item_request_coach(creation_date,title,Message ,CourseName,ClassName,date_request,1);

            values.put("title",title);
            values.put("content",Message);
            values.put("from_id",globalVars.getId());
            values.put("course_name",CourseName);
            values.put("sub_course_name",ClassName);
            if (classPosition != -1)
                values.put("session_id",classEntities.get(classPosition).getClass_id());
            values.put("date_request",date_request);
            values.put("request_type",requestType);

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
                            else {
                                show_toast("Successfully sent");
                                requestResult();
                            }
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
        } catch (ParseException e) {
            e.printStackTrace();
        }
        onBackPressed();
    }

    private void requestResult(){
        Intent intentResult = new Intent();
        intentResult.putExtra("newRequest",added_request);
        setResult(AppCompatActivity.RESULT_OK ,intentResult);
        finish();
    }

    private void show_toast(String s) {
        Toast.makeText(ActivityAddRequest_coach.this,s,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
}
