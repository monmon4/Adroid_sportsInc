package com.quantumsit.sportsinc.Side_menu_fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quantumsit.sportsinc.Adapters.SectionsPagerAdapter;
import com.quantumsit.sportsinc.COACHES.CoachReportsAttendanceFragment;
import com.quantumsit.sportsinc.R;
import com.quantumsit.sportsinc.Reports_fragments.CorsesFragment;
import com.quantumsit.sportsinc.Reports_fragments.PaymentFragment;


public class ReportsFragment extends Fragment {

    private SectionsPagerAdapter reports_sectionsPagerAdapter;
    private ViewPager reports_view_pager;
    TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_reports,container,false);

        reports_view_pager = (ViewPager) root.findViewById(R.id.reports_viewpager);
        tabLayout = (TabLayout) root.findViewById(R.id.reports_tabs);
        tabLayout.setupWithViewPager(reports_view_pager);

        setupViewPager(reports_view_pager);

        return root;
    }

    public void setupViewPager(ViewPager viewPager) {
        reports_sectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        reports_sectionsPagerAdapter.addFragment(new CorsesFragment(),"Courses");
        reports_sectionsPagerAdapter.addFragment(new PaymentFragment(),"Payment");

        reports_view_pager.setAdapter(reports_sectionsPagerAdapter);

    }
}
