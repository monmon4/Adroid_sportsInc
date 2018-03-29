package com.quantumsit.sportsinc.COACHES.ReportsFragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quantumsit.sportsinc.Adapters.SectionsPagerAdapter;
import com.quantumsit.sportsinc.R;


public class CoachReportsFragment extends Fragment {

    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;
    TabLayout tabLayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_coach_reports,container,false);

        viewPager =  root.findViewById(R.id.coach_reports_viewpager);
        tabLayout =  root.findViewById(R.id.coach_reports_tabs);
        tabLayout.setupWithViewPager(viewPager);

        setupViewPager(viewPager);

        return root;
    }

    public void setupViewPager(ViewPager viewPager) {
        sectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());

        sectionsPagerAdapter.addFragment(new CoachReportsAttendanceFragment(),"Attendance");
        sectionsPagerAdapter.addFragment(new CoachReportsFinishedCoursesFragment(),"Finished Classes");

        viewPager.setAdapter(sectionsPagerAdapter);

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (getActivity() != null)
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.reports);
    }

}
