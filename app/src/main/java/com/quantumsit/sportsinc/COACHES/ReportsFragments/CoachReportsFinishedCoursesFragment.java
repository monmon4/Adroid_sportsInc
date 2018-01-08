package com.quantumsit.sportsinc.COACHES.ReportsFragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.quantumsit.sportsinc.COACHES.ActivityFinishedCourseSingle_coach;
import com.quantumsit.sportsinc.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CoachReportsFinishedCoursesFragment extends Fragment {

    ListView listView;
    ListViewFinishedCoursesReports_Adapter listView_adapter;

    ArrayList<item_reports_finished_courses> list_items;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_coach_reports_finished_courses,container,false);

        listView = root.findViewById(R.id.finishedCoursesListView_coachreports);

        list_items = new ArrayList<>();
        for (int i=1; i < 20 ; i++){
            list_items.add(new item_reports_finished_courses("Course " + i , "Group name"));
        }

        listView_adapter = new ListViewFinishedCoursesReports_Adapter(getContext(), list_items);
        listView.setAdapter(listView_adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ActivityFinishedCourseSingle_coach.class);

                startActivity(intent);
            }
        });

        return root;
    }


}
