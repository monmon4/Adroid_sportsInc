package com.quantumsit.sportsinc.COACHES;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class CoachRequestFragment extends Fragment {

    GlobalVars globalVars;

    FloatingActionButton add_request_button;

    ListView listView;
    ArrayList<item_request_coach> list_items;
    ListView_Adapter_request_coach arrayAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_coah_request,container,false);

        globalVars = (GlobalVars) getActivity().getApplication();

        add_request_button = root.findViewById(R.id.floatingActionButton_coachrequest);
        add_request_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityAddRequest_coach.class);
                startActivity(intent);
            }
        });

        listView = root.findViewById(R.id.coachrequests_listview);
        list_items = new ArrayList<>();

        initilizeRequests();

        arrayAdapter = new ListView_Adapter_request_coach(getContext(), list_items);
        listView.setAdapter(arrayAdapter);

        return root;
    }

    private void initilizeRequests() {
        try {
            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.selectData);
            JSONObject where_info = new JSONObject();
            where_info.put("requests.from_id",globalVars.getId());


            HashMap<String,String> params = new HashMap<>();
            params.put("table","requests");

            params.put("where",where_info.toString());

            httpCall.setParams(params);
            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    fillAdapter(response);
                }
            }.execute(httpCall);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillAdapter(JSONArray response) {
        list_items.clear();
        if (response != null) {
            try {
                for (int i = 0; i < response.length(); i++) {
                    list_items.add(new item_request_coach(response.getJSONObject(i)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        arrayAdapter.notifyDataSetChanged();
    }
}
