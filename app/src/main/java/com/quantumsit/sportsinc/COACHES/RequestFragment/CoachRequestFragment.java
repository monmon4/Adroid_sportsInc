package com.quantumsit.sportsinc.COACHES.RequestFragment;


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
import com.quantumsit.sportsinc.COACHES.RequestFragment.CoachRequestReceivedFragment;
import com.quantumsit.sportsinc.COACHES.RequestFragment.CoachRequestSentFragment;
import com.quantumsit.sportsinc.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CoachRequestFragment extends Fragment {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_coach_request, container, false);

        mViewPager =  root.findViewById(R.id.coach_requests_viewpager);

        setupViewPager(mViewPager);
        TabLayout tabLayout =  root.findViewById(R.id.coach_requests_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        return root;
    }

    public void setupViewPager(ViewPager mViewPager) {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mSectionsPagerAdapter.addFragment(new CoachRequestReceivedFragment(), "Received");
        mSectionsPagerAdapter.addFragment(new CoachRequestSentFragment(), "Sent");

        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (getActivity() != null)
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.request);
    }
}