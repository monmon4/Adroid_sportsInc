package com.quantumsit.sportsinc.Side_menu_fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Adapters.SectionsPagerAdapter;
import com.quantumsit.sportsinc.COACHES.CurrentClassFragments.CoachClassesFragment;
import com.quantumsit.sportsinc.MyClasses_fragments.CalendarFragment;
import com.quantumsit.sportsinc.MyClasses_fragments.ScoresFragment;
import com.quantumsit.sportsinc.R;


public class MyClassesFragment extends Fragment {

    private SectionsPagerAdapter myclasses_sectionsPagerAdapter;
    private ViewPager myclasses_view_pager;
    TabLayout tabLayout;

    GlobalVars globalVars;
    boolean trainee = false, coach = false, admin = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_classes,container,false);

        globalVars =  (GlobalVars) getActivity().getApplication();
        int type = globalVars.getType();

        /*
        *   Fragment To Determine the content of classes view (what appear with the calender)
        *   depend on user Type
        *   Trainee = 0 , Coach = 1 , Admin = 2
        *
        * */

        if (type == getResources().getInteger(R.integer.Trainee)) {
            trainee = true;
        }else if (type == getResources().getInteger(R.integer.Coach)) {
            coach = true;
        }

        myclasses_view_pager =  root.findViewById(R.id.my_classes_viewpager);
        tabLayout =  root.findViewById(R.id.my_classes_tabs);
        tabLayout.setupWithViewPager(myclasses_view_pager);

        setupViewPager(myclasses_view_pager);

        return root;
    }

    public void setupViewPager(ViewPager viewPager) {

        myclasses_sectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        if (coach){
            myclasses_sectionsPagerAdapter.addFragment(new CoachClassesFragment(),getString(R.string.my_groups));
        }
        else {
            myclasses_sectionsPagerAdapter.addFragment(new ScoresFragment(),getString(R.string.scores));
        }
        myclasses_sectionsPagerAdapter.addFragment(new CalendarFragment(),getString(R.string.calendar));

        myclasses_view_pager.setAdapter(myclasses_sectionsPagerAdapter);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (getActivity() != null)
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.my_groups));
    }
}
