package com.quantumsit.sportsinc.COACHES.CurrentClassFragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Adapters.CheckBoxListView_Adapter;
import com.quantumsit.sportsinc.Adapters.item_checkbox;
import com.quantumsit.sportsinc.R;
import com.quantumsit.sportsinc.Side_menu_fragments.HomeFragment;

import java.util.ArrayList;


public class Coach_CurrentClassRulesFragment extends Fragment {

    ViewPager viewPager;
    FloatingActionButton done_button;
    ImageView checked_image_view;

    CheckBoxListView_Adapter checkBoxListView_adapter;
    RelativeLayout rules_rl;
    ListView listView;

    ArrayList<item_checkbox> list_items;

    GlobalVars global;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_coach__current_class_rules,container,false);

        global = (GlobalVars) getActivity().getApplication();

        viewPager = getActivity().findViewById(R.id.coach_current_class_viewpager);
        listView = root.findViewById(R.id.rulesCheckListView_coachcurrentclassrulesfragment);
        list_items = new ArrayList<>();

        done_button = root.findViewById(R.id.doneFloatingActionButton_coachcurrentclassrulesfragment);

        for (int i=0; i<20; i++){
            list_items.add(new item_checkbox("Rules number " + i , false));
        }

        checkBoxListView_adapter = new CheckBoxListView_Adapter(getContext(), R.layout.item_checkbox, list_items);
        rules_rl = root.findViewById(R.id.rules_rl);
        checkBoxListView_adapter.setRL(rules_rl);
        listView.setAdapter(checkBoxListView_adapter);



        done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                StringBuffer responseText = new StringBuffer();
                responseText.append("The following were selected...\n");

                ArrayList<item_checkbox> list = checkBoxListView_adapter.list_items;
                for(int i=0;i<list.size();i++){
                    item_checkbox item = list.get(i);
                    if(item.getSelected()){
                        responseText.append("\n" + item.getName());
                    }
                }

                Toast.makeText(getContext(), responseText, Toast.LENGTH_LONG).show();
                viewPager.setCurrentItem(1);


            }
        });


        return root;
    }

}
