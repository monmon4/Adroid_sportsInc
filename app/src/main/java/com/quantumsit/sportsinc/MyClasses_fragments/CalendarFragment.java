package com.quantumsit.sportsinc.MyClasses_fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.CustomCalendar.CalendarCustomView;
import com.quantumsit.sportsinc.Entities.classesEntity;
import com.quantumsit.sportsinc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment {

    GlobalVars globalVars;

    CalendarCustomView calendarView;

    HashMap<String, List<classesEntity> > EventsMap;
    List<classesEntity> classesList ;

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

    private void fileEventsMap() {
        for (classesEntity entity : classesList){
            String ClassDate = entity.getClassdate();
            List<classesEntity> MapList = EventsMap.get(ClassDate);
            if (MapList == null)
                MapList = new ArrayList<>();
            MapList.add(entity);
            EventsMap.put(ClassDate,MapList);
        }
        calendarView.setMyEvents(EventsMap);
    }

    private void initilizeClassesList() {
        try {
            JSONObject where_info = new JSONObject();

            HashMap<String, String> params = new HashMap<>();

            switch (globalVars.getType()){
                case 0:
                    where_info.put("group_trainee.trainee_id",globalVars.getId());
                    params.put("where", where_info.toString());
                    break;
                case 1:
                    where_info.put("groups.coach_id",globalVars.getId());
                    params.put("where", where_info.toString());
                    break;
                case 2:
                    where_info.put("groups.admin_id",globalVars.getId());
                    params.put("where", where_info.toString());
                    break;
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
                    if(!classesList.contains(entity))
                        classesList.add(entity);
                }
                fileEventsMap();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
