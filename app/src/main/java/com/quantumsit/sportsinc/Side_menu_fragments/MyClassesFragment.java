package com.quantumsit.sportsinc.Side_menu_fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quantumsit.sportsinc.ADMINS.Admin_CurrentClassesFragment;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Adapters.SectionsPagerAdapter;
import com.quantumsit.sportsinc.COACHES.CoachClassesFragment;
import com.quantumsit.sportsinc.MyClasses_fragments.CalendarFragment;
import com.quantumsit.sportsinc.MyClasses_fragments.ScoresFragment;
import com.quantumsit.sportsinc.R;


public class MyClassesFragment extends Fragment {

    private SectionsPagerAdapter myclasses_sectionsPagerAdapter;
    private ViewPager myclasses_view_pager;
    TabLayout tabLayout;

    GlobalVars globalVars;
    boolean parent = false, coach = false, admin = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_classes,container,false);

        globalVars =  (GlobalVars) getActivity().getApplication();
        int type = globalVars.getType();

        if (type == 0) {
            parent = true;
        }else if (type == 1) {
            coach = true;
        } else if (type == 2) {
            admin = true;
        }

        myclasses_view_pager = (ViewPager) root.findViewById(R.id.my_classes_viewpager);
        tabLayout = (TabLayout) root.findViewById(R.id.my_classes_tabs);
        tabLayout.setupWithViewPager(myclasses_view_pager);

        setupViewPager(myclasses_view_pager);

        return root;
    }

    public void setupViewPager(ViewPager viewPager) {
        myclasses_sectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        myclasses_sectionsPagerAdapter.addFragment(new CalendarFragment(),"Calender");

        if(parent){
            myclasses_sectionsPagerAdapter.addFragment(new ScoresFragment(),"Scores");
        }else if (coach){
            myclasses_sectionsPagerAdapter.addFragment(new CoachClassesFragment(),"Classes");
        } else {
            myclasses_sectionsPagerAdapter.addFragment(new Admin_CurrentClassesFragment(),"Current classes");
        }


        myclasses_view_pager.setAdapter(myclasses_sectionsPagerAdapter);

    }
}
