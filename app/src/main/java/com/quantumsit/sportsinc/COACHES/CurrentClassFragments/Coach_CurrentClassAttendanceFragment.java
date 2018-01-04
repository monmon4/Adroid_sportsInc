package com.quantumsit.sportsinc.COACHES.CurrentClassFragments;


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

/**
 * A simple {@link Fragment} subclass.
 */
public class Coach_CurrentClassAttendanceFragment extends Fragment {

    ViewPager viewPager;
    FloatingActionButton done_button;
    ImageView checked_image_view;

    CheckBoxListView_Adapter checkBoxListView_adapter;
    RelativeLayout attendance_rl;
    ListView listView;

    ArrayList<item_checkbox> list_items;

    GlobalVars global;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_coach__current_class_attendance,container,false);

        global = (GlobalVars) getActivity().getApplication();

        viewPager = getActivity().findViewById(R.id.coach_current_class_viewpager);
        listView = root.findViewById(R.id.rulesCheckListView_coachcurrentclassattendancefragment);
        list_items = new ArrayList<>();

        checked_image_view = root.findViewById(R.id.checkedImageView_coachcurrentclassattendancefragment);
        done_button = root.findViewById(R.id.doneFloatingActionButton_coachcurrentclassattendancefragment);

        for (int i=1; i<20; i++){
            list_items.add(new item_checkbox("Trainee  " + i , false));
        }

        checkBoxListView_adapter = new CheckBoxListView_Adapter(getContext(), R.layout.item_checkbox, list_items);
        attendance_rl = root.findViewById(R.id.attendance_rl);
        checkBoxListView_adapter.setRL(attendance_rl);
        listView.setAdapter(checkBoxListView_adapter);

        if (global.isCoach_currentclass_attendance()){
            set_enabled(true, View.VISIBLE, true, View.VISIBLE, View.INVISIBLE);
        } else {
            set_enabled(false, View.INVISIBLE, false, View.INVISIBLE, View.VISIBLE);
        }


        done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                global.setCoach_currentclass_attendance(false);

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


                viewPager.setCurrentItem(2);

                set_enabled(false, View.INVISIBLE, false, View.INVISIBLE, View.VISIBLE);

            }
        });

        return root;
    }



    private void set_enabled(boolean list, int vis_list,
                             boolean btn, int vis_btn, int vis_image){
        listView.setEnabled(list);
        listView.setVisibility(vis_list);
        done_button.setEnabled(btn);
        done_button.setVisibility(vis_btn);
        checked_image_view.setVisibility(vis_image);
    }


}
