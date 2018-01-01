package com.quantumsit.sportsinc.COACHES;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quantumsit.sportsinc.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportsFinishedCoursesCoachFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_coach_reports_finished_courses,container,false);
        return root;
    }


}
