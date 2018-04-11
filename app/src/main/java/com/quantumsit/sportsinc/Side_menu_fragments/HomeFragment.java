package com.quantumsit.sportsinc.Side_menu_fragments;

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
import com.quantumsit.sportsinc.COACHES.CurrentClassFragments.Coach_StartClassFragment;
import com.quantumsit.sportsinc.Activities.LevelsFragment;
import com.quantumsit.sportsinc.Home_fragments.MainFragment;
import com.quantumsit.sportsinc.Home_fragments.ProgramsFragment;
import com.quantumsit.sportsinc.R;


public class HomeFragment extends Fragment {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    GlobalVars globalVars;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home,container,false);

        globalVars = (GlobalVars) getActivity().getApplication();
        mViewPager = (ViewPager) root.findViewById(R.id.homecontainer);

        setupViewPager(mViewPager);
        TabLayout tabLayout = (TabLayout) root.findViewById(R.id.hometabs);
        tabLayout.setupWithViewPager(mViewPager);

        int Tab = getActivity().getIntent().getIntExtra("Tab",0);
        mViewPager.setCurrentItem(Tab);

        return root;
    }

    public void setupViewPager(ViewPager mViewPager){
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mSectionsPagerAdapter.addFragment(new MainFragment(),getString(R.string.homeTab));
        if (globalVars.getType() == 1)
            mSectionsPagerAdapter.addFragment(new Coach_StartClassFragment(),getString(R.string.sessionsTab));
        else
            mSectionsPagerAdapter.addFragment(new ProgramsFragment(),getString(R.string.levelTab));

        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

}