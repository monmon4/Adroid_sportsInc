package com.quantumsit.sportsinc.ADMINS;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Adapters.SectionsPagerAdapter;
import com.quantumsit.sportsinc.COACHES.CurrentClassFragments.Coach_CurrentClassAttendanceFragment;
import com.quantumsit.sportsinc.COACHES.CurrentClassFragments.Coach_CurrentClassNoteFragment;
import com.quantumsit.sportsinc.COACHES.CurrentClassFragments.Coach_CurrentClassRulesFragment;
import com.quantumsit.sportsinc.COACHES.CurrentClassFragments.Coach_CurrentClassScoresFragment;
import com.quantumsit.sportsinc.R;

import java.util.ArrayList;


public class Admin_CurrentClassesFragment extends Fragment {

    ListView listView;
    ListViewCurrentClasses_Adapter listView_adapter;

    ArrayList<item_current_classes> list_items;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_admin__current_classes, container, false);



        return root;
    }

}
