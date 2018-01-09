package com.quantumsit.sportsinc.Home_fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Adapters.RuleAdapter;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class RulesFragment extends Fragment {
    RuleAdapter adapter;
    List<String> rulesList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rules,container,false);
        setHasOptionsMenu(false);

        ListView listView = root.findViewById(R.id.rules_listview);
        listView.setEmptyView(root.findViewById(R.id.empty_text));
        rulesList = new ArrayList<>();
        initilizeRules();

        adapter = new RuleAdapter(getContext(),R.layout.list_item_rule,rulesList);
        listView.setAdapter(adapter);

        return root;
    }
    private void initilizeRules() {
        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.POST);
        httpCall.setUrl(Constants.selectData);
        HashMap<String,Integer> Condition = new HashMap<>();
        Condition.put("Type",4);
        HashMap<String,String> params = new HashMap<>();
        params.put("table","rules");
        params.put("WHERE",Condition.toString());

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
                    JSONObject object = (JSONObject) response.get(i);
                    String Content = object.getString("Content");
                    rulesList.add(Content);
                    adapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
