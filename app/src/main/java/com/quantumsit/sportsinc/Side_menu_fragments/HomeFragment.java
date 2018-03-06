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

import com.quantumsit.sportsinc.Adapters.SectionsPagerAdapter;
import com.quantumsit.sportsinc.Home_fragments.Courses2Fragment;
import com.quantumsit.sportsinc.Home_fragments.CoursesFragment;
import com.quantumsit.sportsinc.Home_fragments.EventFragment;
import com.quantumsit.sportsinc.Home_fragments.MainFragment;
import com.quantumsit.sportsinc.Home_fragments.NewsFragment;
import com.quantumsit.sportsinc.Home_fragments.RulesFragment;
import com.quantumsit.sportsinc.R;


public class HomeFragment extends Fragment {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home,container,false);

        mViewPager = (ViewPager) root.findViewById(R.id.homecontainer);

        setupViewPager(mViewPager);
        TabLayout tabLayout = (TabLayout) root.findViewById(R.id.hometabs);
        tabLayout.setupWithViewPager(mViewPager);

        return root;
    }

    public void setupViewPager(ViewPager mViewPager){
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mSectionsPagerAdapter.addFragment(new MainFragment(),"Home");
        mSectionsPagerAdapter.addFragment(new Courses2Fragment(),"Courses");

        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

}