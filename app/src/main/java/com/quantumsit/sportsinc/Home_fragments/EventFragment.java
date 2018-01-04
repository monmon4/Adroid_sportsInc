package com.quantumsit.sportsinc.Home_fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.quantumsit.sportsinc.Adapters.EventAdapter;
import com.quantumsit.sportsinc.Entities.EventEntity;
import com.quantumsit.sportsinc.EventsDetailsActivity;
import com.quantumsit.sportsinc.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class EventFragment extends Fragment {
    EventAdapter adapter;
    List<EventEntity> eventsList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_event,container,false);

        ListView listView = root.findViewById(R.id.events_listview);
        listView.setEmptyView(root.findViewById(R.id.empty_text));
        eventsList = new ArrayList<>();
        Date date1 = new Date("01/11/2018");
        Date date2 = new Date("01/21/2018");
        eventsList.add(new EventEntity("Event #1 Title",date1,"10:00 am","hello from the pool num 1"));
        eventsList.add(new EventEntity("Event #2 Title",date2,"12:00 pm","hello from the pool num 2"));
        adapter = new EventAdapter(getContext(),R.layout.list_item_event,eventsList);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), EventsDetailsActivity.class);
                intent.putExtra("MyEvent",eventsList.get(i));
                startActivity(intent);
            }
        });
        return root;
    }
}
