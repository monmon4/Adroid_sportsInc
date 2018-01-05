package com.quantumsit.sportsinc.COACHES.ReportsFragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.quantumsit.sportsinc.Aaa_looks.MyCustomLayoutManager;
import com.quantumsit.sportsinc.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;


public class CoachReportsAttendanceFragment extends Fragment {

    MyCustomLayoutManager layoutManager;
    RecyclerView recyclerView;
    RecyclerView_Adapter_reportattendance recyclerView_adapter_reportattendance;

    ArrayList<item_report_attendance> list_items;

    MaterialBetterSpinner month_spinner;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_coach_reports_attendance,container,false);

        layoutManager = new MyCustomLayoutManager(getActivity());
        recyclerView = root.findViewById(R.id.recyclerView_reportsattendance);
        list_items = new ArrayList<>();

        month_spinner = root.findViewById(R.id.monthSpinner_reportsattendance);
        ArrayAdapter<CharSequence> month_spinner_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.month_array, android.R.layout.simple_spinner_item);
        month_spinner.setAdapter(month_spinner_adapter);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.smoothScrollToPosition(recyclerView.getVerticalScrollbarPosition());


        for (int i=0; i<15; i++) {
            list_items.add(new item_report_attendance(i + 1 +"/5/2017", "yes", "Course name", "Class 5"));
        }


        recyclerView_adapter_reportattendance = new RecyclerView_Adapter_reportattendance(list_items, getContext());
        recyclerView.setAdapter(recyclerView_adapter_reportattendance);



        return root;
    }

}
