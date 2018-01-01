package com.quantumsit.sportsinc.MyClasses_fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quantumsit.sportsinc.CustomCalendar.CalendarCustomView;
import com.quantumsit.sportsinc.Entities.classesEntity;
import com.quantumsit.sportsinc.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment {

    CalendarCustomView calendarView;
    // TextView dateDisplay;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_calendar,container,false);

            calendarView =  root.findViewById(R.id.custom_calendar);

            List<classesEntity> classesList = new ArrayList<>();
            classesList.add(new classesEntity("Course#1","Group#1","Class#1","Current","Coach: Ahmed","Admin: Ali","Pool#1","12:00pm","3:00pm"));
            classesList.add(new classesEntity("Course#1","Group#2","Class#1","Canceled","Coach: Ahmed","Admin: Ali","Pool#1","12:00pm","3:00pm","asdasdsadasdasda"));
            HashMap<String, List<classesEntity> > map = new HashMap<>();
            map.put("01/01/2018",classesList);

            List<classesEntity> classesList2 = new ArrayList<>();
            classesList2.add(new classesEntity("Course#2","Group#1","Class#1","Current","Coach: Ahmed","Admin: Ali","Pool#1","12:00pm","3:00pm"));
            classesList2.add(new classesEntity("Course#3","Group#2","Class#1","Canceled","Coach: Ahmed","Admin: Ali","Pool#1","12:00pm","3:00pm","asdasdsadasdasda"));

            map.put("02/01/2018",classesList2);

            calendarView.setMyEvents(map);
        return root;
    }

}
