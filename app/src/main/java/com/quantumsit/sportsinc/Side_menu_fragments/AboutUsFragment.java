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
import com.quantumsit.sportsinc.AboutUs_fragments.AboutFragment;
import com.quantumsit.sportsinc.AboutUs_fragments.ConditionsFragment;
import com.quantumsit.sportsinc.AboutUs_fragments.PaymentRulesFragment;
import com.quantumsit.sportsinc.Adapters.SectionsPagerAdapter;
import com.quantumsit.sportsinc.R;


public class AboutUsFragment extends Fragment {

    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager view_pager;
    TabLayout tabLayout;

    GlobalVars globalVars;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_about_us,container,false);

        globalVars =  (GlobalVars) getActivity().getApplication();



        view_pager =  root.findViewById(R.id.about_us_viewpager);
        tabLayout =  root.findViewById(R.id.about_us_tabs);
        tabLayout.setupWithViewPager(view_pager);


        setupViewPager(view_pager);

        return root;
    }

    public void setupViewPager(ViewPager viewPager) {

        sectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());

        sectionsPagerAdapter.addFragment(new ConditionsFragment(),"Handbook");
        sectionsPagerAdapter.addFragment(new PaymentRulesFragment(),"Payment Rules");

        viewPager.setAdapter(sectionsPagerAdapter);

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (getActivity() != null)
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.about_us);
    }

}
