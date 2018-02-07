package com.quantumsit.sportsinc.AboutUs_fragments;

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
import com.quantumsit.sportsinc.Reports_fragments.CorsesFragment;
import com.quantumsit.sportsinc.Reports_fragments.PaymentFragment;


public class AboutFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_about,container,false);


        return root;
    }

}
