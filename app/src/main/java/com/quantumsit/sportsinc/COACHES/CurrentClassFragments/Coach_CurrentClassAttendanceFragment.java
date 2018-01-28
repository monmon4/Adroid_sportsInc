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

import com.quantumsit.sportsinc.Aaa_data.DB_Sqlite_Handler;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Aaa_data.MyClass_info;
import com.quantumsit.sportsinc.Aaa_data.Rule_info;
import com.quantumsit.sportsinc.Aaa_data.Trainees_info;
import com.quantumsit.sportsinc.Adapters.CheckBoxListView_Adapter;
import com.quantumsit.sportsinc.Adapters.item_checkbox;
import com.quantumsit.sportsinc.R;

import java.util.ArrayList;
import java.util.List;

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

    List<item_checkbox> list_items;

    GlobalVars global;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_coach__current_class_attendance,container,false);

        MyClass_info info = (MyClass_info) getActivity().getIntent().getSerializableExtra("MyRunningClass");
        global = (GlobalVars) getActivity().getApplication();

        viewPager = getActivity().findViewById(R.id.coach_current_class_viewpager);
        listView = root.findViewById(R.id.rulesCheckListView_coachcurrentclassattendancefragment);
        list_items = new ArrayList<>();

        done_button = root.findViewById(R.id.doneFloatingActionButton_coachcurrentclassattendancefragment);


        checkBoxListView_adapter = new CheckBoxListView_Adapter(getContext(), R.layout.item_checkbox, list_items);
        attendance_rl = root.findViewById(R.id.attendance_rl);
        checkBoxListView_adapter.setRL(attendance_rl);
        listView.setAdapter(checkBoxListView_adapter);

        if (info != null)
            initializeTrainees(info.getClass_id());

        done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateTrainee();
                /*StringBuffer responseText = new StringBuffer();
                responseText.append("The following were selected...\n");

                List<item_checkbox> list = checkBoxListView_adapter.list_items;
                for(int i=0;i<list.size();i++){
                    item_checkbox item = list.get(i);
                    if(item.getSelected()){
                        responseText.append("\n" + item.getName());
                    }
                }

                Toast.makeText(getContext(), responseText, Toast.LENGTH_LONG).show();*/

                viewPager.setCurrentItem(2);

            }
        });

        return root;
    }


    private void updateTrainee() {
        DB_Sqlite_Handler handler = global.getMyDB();
        for (item_checkbox item: list_items){
            handler.updateTrainee(item.getTrainee());
        }

    }

    private void initializeTrainees(int class_id) {
        List<Trainees_info> trainees = global.getMyDB().getClassTrainees(class_id);
        list_items.clear();
        for (Trainees_info item : trainees){
            list_items.add(new item_checkbox(item));
        }
        checkBoxListView_adapter.notifyDataSetChanged();
    }


}
