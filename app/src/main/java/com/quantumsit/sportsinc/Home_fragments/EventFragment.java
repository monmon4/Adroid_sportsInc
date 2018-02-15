package com.quantumsit.sportsinc.Home_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Adapters.EventAdapter;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.Entities.EventEntity;
import com.quantumsit.sportsinc.Activities.EventsDetailsActivity;
import com.quantumsit.sportsinc.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
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
        initilizeEvents();

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
    private void initilizeEvents() {
        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.POST);
        httpCall.setUrl(Constants.selectData);
        HashMap<String,String> params = new HashMap<>();
        params.put("table","events");

        httpCall.setParams(params);
        new HttpRequest(){
            @Override
            public void onResponse(JSONArray response) {
                super.onResponse(response);
                fillAdapter(response);
            }
        }.execute(httpCall);
    }

    private void fillAdapter(JSONArray response) {
        if (response != null) {
            try {
                for (int i = 0; i < response.length(); i++) {
                    eventsList.add(new EventEntity(response.getJSONObject(i)));
                    adapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
