package com.quantumsit.sportsinc.ADMINS;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Adapters.CheckBoxListView_Adapter;
import com.quantumsit.sportsinc.Adapters.item_checkbox;
import com.quantumsit.sportsinc.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Admin_CurrentClassAttendanceFragment extends Fragment {

    ViewPager viewPager;
    FloatingActionButton done_button;
    //ImageView checked_image_view;

    CheckBoxListView_Adapter checkBoxListView_adapter;
    RelativeLayout attendance_rl;
    ListView listView;

    ArrayList<item_checkbox> list_items;

    GlobalVars global;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_admin__current_class_attendance,container,false);

        global = (GlobalVars) getActivity().getApplication();

        viewPager = getActivity().findViewById(R.id.coach_current_class_viewpager);
        listView = root.findViewById(R.id.rulesCheckListView_admincurrentclassattendancefragment);
        list_items = new ArrayList<>();

        //checked_image_view = root.findViewById(R.id.checkedImageView_coachcurrentclassattendancefragment);
        done_button = root.findViewById(R.id.doneFloatingActionButton_admincurrentclassattendancefragment);

        for (int i=1; i<20; i++){
            if (i == 1){
                list_items.add(new item_checkbox(getResources().getString(R.string.coach) + "  " , false));
            } else {
                list_items.add(new item_checkbox(getResources().getString(R.string.trainee) +"  " + i , false));
            }

        }

        checkBoxListView_adapter = new CheckBoxListView_Adapter(getContext(), R.layout.item_checkbox, list_items);
        attendance_rl = root.findViewById(R.id.attendance_admin_rl);
        checkBoxListView_adapter.setRL(attendance_rl);
        listView.setAdapter(checkBoxListView_adapter);



        done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StringBuffer responseText = new StringBuffer();
                responseText.append("The following were selected...\n");

                List<item_checkbox> list = checkBoxListView_adapter.list_items;
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
