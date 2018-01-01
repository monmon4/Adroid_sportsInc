package com.quantumsit.sportsinc.COACHES;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.quantumsit.sportsinc.Aaa_looks.ListView_Adapter_request;
import com.quantumsit.sportsinc.Aaa_looks.MyCustomLayoutManager;
import com.quantumsit.sportsinc.Aaa_looks.item_request;
import com.quantumsit.sportsinc.R;
import com.quantumsit.sportsinc.Request_addActivity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CoachRequestFragment extends Fragment {


    FloatingActionButton add_request_button;

    ListView listView;
    ArrayList<item_request_coach> list_items;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_coah_request,container,false);

        add_request_button = root.findViewById(R.id.floatingActionButton_coachrequest);
        add_request_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityAddRequest.class);
                startActivity(intent);
            }
        });

        listView = root.findViewById(R.id.coachrequests_listview);
        list_items = new ArrayList<>();

        for (int j=0; j<10; j++){
            list_items.add(new item_request_coach("4/4/2017", "Request for: absence", "Course Name, Class 8", "8/8/2017","yes"));
        }

        ListView_Adapter_request_coach arrayAdapter = new ListView_Adapter_request_coach(getContext(), list_items);
        listView.setAdapter(arrayAdapter);

        return root;
    }


}
