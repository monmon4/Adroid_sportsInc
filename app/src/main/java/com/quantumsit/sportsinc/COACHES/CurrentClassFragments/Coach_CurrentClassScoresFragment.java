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

import com.quantumsit.sportsinc.Aaa_data.DB_Sqlite_Handler;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Aaa_data.MyClass_info;
import com.quantumsit.sportsinc.Aaa_data.Trainees_info;
import com.quantumsit.sportsinc.Entities.classesEntity;
import com.quantumsit.sportsinc.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Coach_CurrentClassScoresFragment extends Fragment {

    ViewPager viewPager;
    FloatingActionButton done_button;
    ImageView checked_image_view;
    ListView_scores_Adapter adapter_scores_listview;
    ListView listView;

    ArrayList<Trainees_info> list_items;

    GlobalVars global;
    classesEntity info;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_coach__current_class_scores,container,false);
        info = (classesEntity) getActivity().getIntent().getSerializableExtra("MyRunningClass");
        global = (GlobalVars) getActivity().getApplication();

        viewPager = getActivity().findViewById(R.id.coach_current_class_viewpager);
        listView = root.findViewById(R.id.rulesCheckListView_coachcurrentclassscoresfragment);
        list_items = new ArrayList<>();

        done_button = root.findViewById(R.id.doneFloatingActionButton_coachcurrentclassscoresfragment);

        adapter_scores_listview = new ListView_scores_Adapter(getContext(),R.layout.item_coach_trainee_score, list_items);
        listView.setAdapter(adapter_scores_listview);



        done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateTrainee();

                /*boolean allgood = false;


                StringBuffer responseText = new StringBuffer();
                responseText.append("These are the scores...\n");

                ArrayList<Trainees_info> list = adapter_scores_listview.list_items;
                for(int i=0;i<list.size();i++){
                    Trainees_info item = list.get(i);
                    String score = String.valueOf(item.getTrainee_score());

                    if (score.equals("")){
                        allgood = false;
                        Toast.makeText(getContext(), item.getTrainee_name() + " score is missing", Toast.LENGTH_SHORT).show();
                        break;
                    } else {
                        responseText.append(item.getTrainee_name()+ " score is " + score + "\n");
                        allgood = true;
                    }
                }

                if (allgood) {
                    Toast.makeText(getContext(), responseText, Toast.LENGTH_LONG).show();
                    viewPager.setCurrentItem(3);
                }*/

                viewPager.setCurrentItem(2);
            }
        });


        return root;
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            if (info != null)
                initializeTrainees(info.getClass_id());
        }
    }

    private void updateTrainee() {
        DB_Sqlite_Handler handler = global.getMyDB();
        for (Trainees_info item: list_items){
            handler.updateTrainee(item);
        }

    }

    private void initializeTrainees(int class_id) {
        List<Trainees_info> trainees = global.getMyDB().getClassAttendTrainees(class_id);
        list_items.clear();
        for (Trainees_info item : trainees){
            list_items.add(item);
        }
        adapter_scores_listview.notifyDataSetChanged();
    }

}
