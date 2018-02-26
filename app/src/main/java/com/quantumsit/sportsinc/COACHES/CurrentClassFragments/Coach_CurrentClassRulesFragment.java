package com.quantumsit.sportsinc.COACHES.CurrentClassFragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.quantumsit.sportsinc.Aaa_data.DB_Sqlite_Handler;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Aaa_data.MyClass_info;
import com.quantumsit.sportsinc.Aaa_data.Rule_info;
import com.quantumsit.sportsinc.Adapters.CheckBoxListView_Adapter;
import com.quantumsit.sportsinc.Adapters.item_checkbox;
import com.quantumsit.sportsinc.R;
import com.quantumsit.sportsinc.Side_menu_fragments.HomeFragment;

import java.util.ArrayList;
import java.util.List;


public class Coach_CurrentClassRulesFragment extends Fragment {

    private static String TAG = Coach_CurrentClassRulesFragment.class.getSimpleName();

    ViewPager viewPager;
    FloatingActionButton done_button;
    ImageView checked_image_view;

    CheckBoxListView_Adapter checkBoxListView_adapter;
    RelativeLayout rules_rl;
    ListView listView;

    List<item_checkbox> list_items;

    GlobalVars global;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_coach__current_class_rules,container,false);

        MyClass_info info = (MyClass_info) getActivity().getIntent().getSerializableExtra("MyRunningClass");
        global = (GlobalVars) getActivity().getApplication();

        viewPager = getActivity().findViewById(R.id.coach_current_class_viewpager);
        listView = root.findViewById(R.id.rulesCheckListView_coachcurrentclassrulesfragment);
        list_items = new ArrayList<>();

        done_button = root.findViewById(R.id.doneFloatingActionButton_coachcurrentclassrulesfragment);


        checkBoxListView_adapter = new CheckBoxListView_Adapter(getContext(), R.layout.item_checkbox, list_items);
        rules_rl = root.findViewById(R.id.rules_rl);
        checkBoxListView_adapter.setRL(rules_rl);
        listView.setAdapter(checkBoxListView_adapter);

        if (info != null)
            initializeRules(info.getClass_id());


        done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               updateRules();
                /*StringBuffer responseText = new StringBuffer();
                responseText.append("The following were selected...\n");

                List<Rule_info> list = checkBoxListView_adapter.list_items;
                for(int i=0;i<list.size();i++){
                    Rule_info item = list.get(i);
                    if(item.getSelected()){
                        responseText.append("\n" + item.getRule_name());
                    }
                }

                Toast.makeText(getContext(), responseText, Toast.LENGTH_LONG).show();*/
                viewPager.setCurrentItem(1);


            }
        });


        return root;
    }

    private void updateRules() {
        DB_Sqlite_Handler handler = global.getMyDB();
        for (item_checkbox item: list_items){
            handler.updateRule(item.getRule());
        }

    }

    private void initializeRules(int class_id) {
        List<Rule_info> rules = global.getMyDB().getAllRules(class_id);
        list_items.clear();
        for (Rule_info item : rules){
            Log.d(TAG,"RuleNum: "+"name");
            list_items.add(new item_checkbox(item));
        }
        checkBoxListView_adapter.notifyDataSetChanged();
    }

}
