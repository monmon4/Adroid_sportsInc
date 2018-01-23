package com.quantumsit.sportsinc.COACHES.ReportsFragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.COACHES.ActivityFinishedCourseSingle_coach;
import com.quantumsit.sportsinc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class CoachReportsFinishedCoursesFragment extends Fragment {

    private static String TAG = CoachReportsFinishedCoursesFragment.class.getSimpleName();

    GlobalVars globalVars;

    ListView listView;
    ListViewFinishedCoursesReports_Adapter listView_adapter;

    ArrayList<item_reports_finished_courses> list_items;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_coach_reports_finished_courses,container,false);

        listView = root.findViewById(R.id.finishedCoursesListView_coachreports);

        globalVars = (GlobalVars) getActivity().getApplication();

        list_items = new ArrayList<>();

        initilizeFinishedList();

        listView_adapter = new ListViewFinishedCoursesReports_Adapter(getContext(), list_items);
        listView.setAdapter(listView_adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ActivityFinishedCourseSingle_coach.class);
                intent.putExtra("finishedGroup",list_items.get(position));
                startActivity(intent);
            }
        });

        return root;
    }

    private void initilizeFinishedList() {
        try {
            HashMap<String, String> params = new HashMap<>();

            JSONObject where_info = new JSONObject();

            switch (globalVars.getType()){
                case 1:
                    where_info.put("coach_id", globalVars.getId());
                    params.put("where",where_info.toString());
                    break;
                case 2:
                    where_info.put("admin_id",globalVars.getId());
                    params.put("where",where_info.toString());
                    break;
            }
            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.finished_groups);

            params.put("where", where_info.toString());

            httpCall.setParams(params);

            new HttpRequest() {
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    Log.d(TAG,String.valueOf(response));
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
                    item_reports_finished_courses entity = new item_reports_finished_courses(response.getJSONObject(i));
                    list_items.add(entity);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        listView_adapter.notifyDataSetChanged();
    }
}
