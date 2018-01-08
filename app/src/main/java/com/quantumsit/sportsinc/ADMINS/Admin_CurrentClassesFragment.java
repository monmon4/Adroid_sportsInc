package com.quantumsit.sportsinc.ADMINS;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quantumsit.sportsinc.COACHES.ActivityCurrentClass_coach;
import com.quantumsit.sportsinc.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class Admin_CurrentClassesFragment extends Fragment {


    ExpandableListView expandableListView;
    ListViewExpandable_Adapter_currentClasses expandableListView_adapter;

    List<item_current_classes> list_headers;
    List<String> list_children;

    View root;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_admin__current_classes, container, false);

        expandableListView = root.findViewById(R.id.expandableListView_admincurrentclasses);

        list_headers = new ArrayList<>();
        list_children = new ArrayList<>();
        list_children.add("Start class");
        list_children.add("Postpone class");
        list_children.add("Cancel class");
        HashMap<String, List<String>> hash_children = new HashMap<>();

        for (int i = 1 ; i< 8; i++){
            list_headers.add(new item_current_classes("Class " + i, "5 Jan", "02:00 ~ 05:00"));
            hash_children.put("Class " + i, list_children);
        }

        expandableListView_adapter = new ListViewExpandable_Adapter_currentClasses(getContext(), list_headers, hash_children );
        expandableListView.setAdapter(expandableListView_adapter);


        return root;
    }

}
