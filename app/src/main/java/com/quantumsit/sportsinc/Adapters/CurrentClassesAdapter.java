package com.quantumsit.sportsinc.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.DB_Sqlite_Handler;
import com.quantumsit.sportsinc.Aaa_data.Rule_info;
import com.quantumsit.sportsinc.Aaa_data.Trainees_info;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.COACHES.ActivityCurrentClass_coach;
import com.quantumsit.sportsinc.Entities.CourseEntity;
import com.quantumsit.sportsinc.Entities.CurrentClassesEntity;
import com.quantumsit.sportsinc.Entities.classesEntity;
import com.quantumsit.sportsinc.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Bassam on 15/3/2018.
 */

public class CurrentClassesAdapter  extends ArrayAdapter<classesEntity>{
    Context context;
    ArrayList<classesEntity> classesList;
    ProgressDialog progressDialog;
    DB_Sqlite_Handler myDB;
    private Button  startBtn, endBtn;
    ImageView editBtn;
    int currentPosition;

    public CurrentClassesAdapter(@NonNull Context context, int resource, ArrayList<classesEntity> classesList, DB_Sqlite_Handler myDB) {
        super(context, resource);
        this.context = context;
        this.myDB = myDB;
        progressDialog = new ProgressDialog(context);
        this.classesList = classesList;
    }
    @Override
    public int getCount() {
        return classesList.size();
    }

    @Nullable
    @Override
    public classesEntity getItem(int position) {
        return classesList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_current_class, null);
        }
        currentPosition = position;
        classesEntity item = getItem(position);

        TextView ClassName = view.findViewById(R.id.ClassName);
        TextView SessionTime = view.findViewById(R.id.class_time);
        TextView SessionName = view.findViewById(R.id.sessionName);
        TextView TraineeCount = view.findViewById(R.id.traineeNum);

        ClassName.setText(item.getGroupName());
        SessionName.setText(""+item.getClassNum());
        SessionTime.setText(item.getStartTime()+":00");

        startBtn = view.findViewById(R.id.btnStart);
        endBtn = view.findViewById(R.id.btnEnd);
        editBtn = view.findViewById(R.id.btnEdit);

        if (item.getState() == 0){
            startBtn.setVisibility(View.GONE);
            editBtn.setVisibility(View.VISIBLE);
            endBtn.setVisibility(View.VISIBLE);
        }
        else if (item.getState() == 3){
            if(checkClassTime(item))
                startBtn.setVisibility(View.VISIBLE);
            else
                startBtn.setVisibility(View.GONE);

            editBtn.setVisibility(View.GONE);
            endBtn.setVisibility(View.GONE);
        }

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Starting Session");
                progressDialog.show();
                startClass();
            }
        });

        endBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ActivityCurrentClass_coach.class);
                intent.putExtra(context.getString(R.string.Key_RunningClass),classesList.get(currentPosition));
                intent.putExtra("position",1);
                context.startActivity(intent);
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ActivityCurrentClass_coach.class);
                intent.putExtra(context.getString(R.string.Key_RunningClass),classesList.get(currentPosition));
                context.startActivity(intent);
            }
        });

        return  view;
    }

    private void startSession() {
        classesList.get(currentPosition).setState(0);
        this.notifyDataSetChanged();
        initializeClassTrainee(classesList.get(currentPosition).getGroup_id() ,classesList.get(currentPosition).getClass_id());
    }
    private void startClass() {
        try {
            JSONObject where_info = new JSONObject();
            where_info.put("id", classesList.get(currentPosition).getClass_id());

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
                        startSession();
                    }else {
                        Toast.makeText(context, "Failed To start the session", Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                }
            }.execute(httpCall);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void initializeClassTrainee(int group_id, final int class_id) {
        /*
        * get Class Trainee information to attend him and give him score
        * */
        try {
            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.joinData);
            JSONObject where = new JSONObject();
            where.put("group_id",group_id);

            String onCondition = "group_trainee.trainee_id = users.id";
            HashMap<String, String> params = new HashMap<>();
            params.put("table1", "group_trainee");
            params.put("table2", "users");
            params.put("on", onCondition);
            params.put("where",where.toString());

            httpCall.setParams(params);

            new HttpRequest() {
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    insertTraineesInSql(response,class_id);
                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void insertTraineesInSql(JSONArray response, int class_id) {
        /*
        * Cache Class Trainees Information
        * */
        if (response != null) {
            try {
                for (int i = 0; i < response.length(); i++) {
                    Trainees_info info = new Trainees_info(response.getJSONObject(i),class_id);
                    myDB.addTrainee(info);
                }
                progressDialog.dismiss();
                Intent intent = new Intent(context, ActivityCurrentClass_coach.class);
                intent.putExtra(context.getString(R.string.Key_RunningClass),classesList.get(currentPosition));
                context.startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else
            progressDialog.dismiss();
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

    private boolean checkClassTime(classesEntity entity) {
        double current_time_double = 0;
        double start_time_double = Double.valueOf(entity.getStartTime().replace(":", "."));
        double end_time_double = Double.valueOf(entity.getEndTime().replace(":", "."));
        Date current_time = Calendar.getInstance(Locale.ENGLISH).getTime();
        DateFormat time_format = new SimpleDateFormat("hh:mm a",Locale.ENGLISH);
        String time = time_format.format(current_time);
        String[] splitin_time = time.split(" ");

        current_time_double = Double.valueOf(splitin_time[0].replace(":", "."));
        if ( splitin_time[1].equals("PM") && current_time_double - 12 < 1) {
            current_time_double += 12.00;
        }
        if (start_time_double - current_time_double < 0.11)
            return true;
        return false;
    }

}
