package com.quantumsit.sportsinc.Side_menu_fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.quantumsit.sportsinc.Aaa_looks.ListView_Adapter_request;
import com.quantumsit.sportsinc.Aaa_looks.item_request;
import com.quantumsit.sportsinc.R;
import com.quantumsit.sportsinc.Request_addActivity;

import java.util.ArrayList;


public class RequestsFragment extends Fragment {

    FloatingActionButton add_request_button;

    ListView listView;
    ArrayList<item_request> list_items;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_requests,container,false);

        add_request_button = root.findViewById(R.id.floatingActionButton);
        add_request_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Request_addActivity.class);
                startActivity(intent);
            }
        });

        listView = root.findViewById(R.id.requests_listview);
        list_items = new ArrayList<>();

        for (int j=0; j<10; j++){
            list_items.add(new item_request("4/4/2017", "Course1, class 5", "10/4/2017"));
        }

        ListView_Adapter_request arrayAdapter = new ListView_Adapter_request(getContext(), list_items);
        listView.setAdapter(arrayAdapter);

        return root;
    }
}
