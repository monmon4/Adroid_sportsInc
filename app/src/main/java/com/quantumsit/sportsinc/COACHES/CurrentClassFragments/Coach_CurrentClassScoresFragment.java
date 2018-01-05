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
import android.widget.Toast;

import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Adapters.CheckBoxListView_Adapter;
import com.quantumsit.sportsinc.Adapters.item_checkbox;
import com.quantumsit.sportsinc.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Coach_CurrentClassScoresFragment extends Fragment {

    ViewPager viewPager;
    FloatingActionButton done_button;
    ImageView checked_image_view;
    ListView_scores_Adapter adapter_scores_listview;
    ListView listView;

    ArrayList<item_score> list_items;

    GlobalVars global;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_coach__current_class_scores,container,false);

        global = (GlobalVars) getActivity().getApplication();

        viewPager = getActivity().findViewById(R.id.coach_current_class_viewpager);
        listView = root.findViewById(R.id.rulesCheckListView_coachcurrentclassscoresfragment);
        list_items = new ArrayList<>();

        checked_image_view = root.findViewById(R.id.checkedImageView_coachcurrentclassscoresfragment);
        done_button = root.findViewById(R.id.doneFloatingActionButton_coachcurrentclassscoresfragment);

        for (int i=1; i<20; i++){
            list_items.add(new item_score("Trainee " + i));
        }

        adapter_scores_listview = new ListView_scores_Adapter(getContext(),R.layout.item_coach_trainee_score, list_items);
        listView.setAdapter(adapter_scores_listview);



        done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean allgood = false;



                StringBuffer responseText = new StringBuffer();
                responseText.append("These are the scores...\n");

                ArrayList<item_score> list = adapter_scores_listview.list_items;
                for(int i=0;i<list.size();i++){
                    item_score item = list.get(i);
                    String score = item.score;

                    if (score.equals("")){
                        allgood = false;
                        Toast.makeText(getContext(), item.name + " score is missing", Toast.LENGTH_SHORT).show();
                        break;
                    } else {
                        responseText.append(item.name+ " score is " + score + "\n");
                        allgood = true;
                    }
                }

                if (allgood) {
                    Toast.makeText(getContext(), responseText, Toast.LENGTH_LONG).show();
                    viewPager.setCurrentItem(3);
                }


            }
        });


        return root;
    }

}
