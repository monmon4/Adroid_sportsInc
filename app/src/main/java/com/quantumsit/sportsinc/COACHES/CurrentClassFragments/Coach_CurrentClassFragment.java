package com.quantumsit.sportsinc.COACHES.CurrentClassFragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quantumsit.sportsinc.Adapters.SectionsPagerAdapter;
import com.quantumsit.sportsinc.R;


public class Coach_CurrentClassFragment extends Fragment {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_coach__current_class, container, false);

        mViewPager = (ViewPager) root.findViewById(R.id.coach_current_class_viewpager);

        setupViewPager(mViewPager);
        TabLayout tabLayout = root.findViewById(R.id.coach_current_class_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        return root;
    }

    public void setupViewPager(ViewPager mViewPager){
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mSectionsPagerAdapter.addFragment(new Coach_CurrentClassRulesFragment(),"Rules");
        mSectionsPagerAdapter.addFragment(new Coach_CurrentClassAttendanceFragment(),"Attendance");
        mSectionsPagerAdapter.addFragment(new Coach_CurrentClassScoresFragment(),"Scores");
        mSectionsPagerAdapter.addFragment(new Coach_CurrentClassNoteFragment(),"Notes");

        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

}
