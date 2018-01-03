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

import com.quantumsit.sportsinc.Adapters.RuleAdapter;
import com.quantumsit.sportsinc.R;

import java.util.ArrayList;
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
        rulesList.add("Rule Number one in the academey\nis that no one care about your son");
        rulesList.add("Rule Number two in the academey is that your son is a man not achild in our eyes");

        adapter = new RuleAdapter(getContext(),R.layout.list_item_rule,rulesList);
        listView.setAdapter(adapter);
        return root;
    }
}
