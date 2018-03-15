package com.quantumsit.sportsinc.MyClasses_fragments;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Activities.ClassesDetailsActivity;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.CustomCalendar.CalendarCustomView;
import com.quantumsit.sportsinc.CustomCalendar.ListViewAdapter;
import com.quantumsit.sportsinc.Entities.classesEntity;
import com.quantumsit.sportsinc.Interfaces.MyItemClickListener;
import com.quantumsit.sportsinc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment {

    GlobalVars globalVars;

    CalendarCustomView calendarView;

    HashMap<String, List<classesEntity> > EventsMap;
    List<classesEntity> classesList ;
    private int REQUEST_CODE = 1 , event_positions;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_calendar,container,false);
        globalVars = (GlobalVars) getActivity().getApplication();

        calendarView = root.findViewById(R.id.custom_calendar);

        classesList = new ArrayList<>();
        EventsMap = new HashMap<>();

        initilizeClassesList();

        return root;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(isVisibleToUser) {
            if (getActivity() != null)
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }
        else {
            if (getActivity() != null)
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getActivity() != null){
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }
        if (requestCode == REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK && data != null){
            int new_status = data.getIntExtra("ClassStatus",-1);
            if (new_status != -1) {
                calendarView.getMyEvents().get(calendarView.SelectedDate).get(event_positions).setState(new_status);
                calendarView.setUpEventsAapter(calendarView.SelectedDate);
            }

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getActivity() != null)
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
    }

    private void fileEventsMap() {
        for (classesEntity entity : classesList){
            String ClassDate = entity.getClassdate();
            List<classesEntity> MapList = EventsMap.get(ClassDate);
            if (MapList == null)
                MapList = new ArrayList<>();
            MapList.add(entity);
            EventsMap.put(ClassDate,MapList);
        }
        calendarView.setEventsListener(new MyItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                event_positions = position;
                Intent intent = new Intent(getContext(), ClassesDetailsActivity.class);
                intent.putExtra("Myclass",calendarView.getMyEvents().get(calendarView.SelectedDate).get(position));
                startActivityForResult(intent , REQUEST_CODE);
            }
        });
        calendarView.setMyEvents(EventsMap);
    }

    private void initilizeClassesList() {
        try {
            JSONObject where_info = new JSONObject();
            JSONObject or_where_info = new JSONObject();

            HashMap<String, String> params = new HashMap<>();

            switch (globalVars.getType()){
                case 0:
                    where_info.put("group_trainee.trainee_id",globalVars.getId());
                    or_where_info.put("person.parent_id",globalVars.getId());
                    params.put("where", where_info.toString());
                    params.put("or_where", or_where_info.toString());
                    Log.d("CalendarFragment",where_info.toString()+" "+or_where_info.toString());
                    break;
                case 1:
                    where_info.put("groups.coach_id",globalVars.getId());
                    or_where_info.put("reassign_coach.new_reassign_id",globalVars.getId());
                    params.put("where", where_info.toString());
                    params.put("or_where", or_where_info.toString());
                    params.put("user_type",String.valueOf(globalVars.getType()));
                    break;
                /*case 2:
                    where_info.put("groups.admin_id",globalVars.getId());
                    params.put("where", where_info.toString());
                    break;*/
            }
            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.classesData);

            httpCall.setParams(params);

            new HttpRequest() {
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    fillAdapter(response);
                }
            }.execute(httpCall);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillAdapter(JSONArray response) {
        classesList.clear();
        if (response != null) {
            try {
                for (int i = 0; i < response.length(); i++) {
                    classesEntity entity = new classesEntity( response.getJSONObject(i));
                    //if(!classesList.contains(entity))
                    classesList.add(entity);
                }
                calendarView.person_id = globalVars.getId();
                fileEventsMap();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
