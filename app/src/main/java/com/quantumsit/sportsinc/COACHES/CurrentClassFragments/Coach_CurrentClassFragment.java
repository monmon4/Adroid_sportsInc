package com.quantumsit.sportsinc.COACHES.CurrentClassFragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Adapters.SectionsPagerAdapter;
import com.quantumsit.sportsinc.R;


public class Coach_CurrentClassFragment extends Fragment {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    GlobalVars globalVars;
    int type;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_coach__current_class, container, false);

        globalVars = (GlobalVars) getActivity().getApplication();
        type = globalVars.getType();

        mViewPager =  root.findViewById(R.id.coach_current_class_viewpager);

        setupViewPager(mViewPager);
        TabLayout tabLayout = root.findViewById(R.id.coach_current_class_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        return root;
    }

    public void setupViewPager(ViewPager mViewPager){
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());

        if (type == 1) {
            mSectionsPagerAdapter.addFragment(new Coach_CurrentClassAttendanceFragment(),"Attendance");
            mSectionsPagerAdapter.addFragment(new Coach_CurrentClassScoresFragment(),"Scores");
            mSectionsPagerAdapter.addFragment(new Coach_CurrentClassNoteFragment(),"Notes");

        } /*else {
            mSectionsPagerAdapter.addFragment(new Admin_CurrentClassAttendanceFragment(),"Attendance");
            mSectionsPagerAdapter.addFragment(new Admin_CurrentClassRulesFragment(),"Rules");
            mSectionsPagerAdapter.addFragment(new Admin_CurrentClassNoteFragment(),"Notes");

        }*/

        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

}
