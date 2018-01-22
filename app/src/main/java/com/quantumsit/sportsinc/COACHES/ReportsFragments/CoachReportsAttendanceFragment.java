package com.quantumsit.sportsinc.COACHES.ReportsFragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.quantumsit.sportsinc.ADMINS.item_current_classes;
import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Aaa_looks.MyCustomLayoutManager;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class CoachReportsAttendanceFragment extends Fragment {

    private static String TAG = CoachReportsAttendanceFragment.class.getSimpleName();

    GlobalVars globalVars;

    MyCustomLayoutManager layoutManager;
    RecyclerView recyclerView;
    RecyclerView_Adapter_reportattendance recyclerView_adapter_reportattendance;

    ArrayList<item_report_attendance> list_items;

    MaterialBetterSpinner month_spinner;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_coach_reports_attendance,container,false);

        globalVars = (GlobalVars) getActivity().getApplication();

        layoutManager = new MyCustomLayoutManager(getActivity());
        recyclerView = root.findViewById(R.id.recyclerView_reportsattendance);
        list_items = new ArrayList<>();

        month_spinner = root.findViewById(R.id.monthSpinner_reportsattendance);
        ArrayAdapter<CharSequence> month_spinner_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.month_array, android.R.layout.simple_spinner_item);
        month_spinner.setAdapter(month_spinner_adapter);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.smoothScrollToPosition(recyclerView.getVerticalScrollbarPosition());


        initilizeAttendList();

        recyclerView_adapter_reportattendance = new RecyclerView_Adapter_reportattendance(list_items, getContext());
        recyclerView.setAdapter(recyclerView_adapter_reportattendance);



        return root;
    }

    private void initilizeAttendList() {
        try {
            JSONObject where_info = new JSONObject();
            where_info.put("user_id",globalVars.getId());

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.person_attend);

            HashMap<String, String> params = new HashMap<>();
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
                    item_report_attendance entity = new item_report_attendance(response.getJSONObject(i));
                    list_items.add(entity);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        recyclerView_adapter_reportattendance.notifyDataSetChanged();
    }

}
