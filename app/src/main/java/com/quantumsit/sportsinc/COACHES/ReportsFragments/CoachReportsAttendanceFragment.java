package com.quantumsit.sportsinc.COACHES.ReportsFragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.quantumsit.sportsinc.ADMINS.item_current_classes;
import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Aaa_looks.MyCustomLayoutManager;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.CustomView.myCustomRecyclerView;
import com.quantumsit.sportsinc.R;
import com.quantumsit.sportsinc.util.ConnectionUtilities;
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
    myCustomRecyclerView customRecyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView recyclerView;
    RecyclerView_Adapter_reportattendance recyclerView_adapter_reportattendance;

    ArrayList<item_report_attendance> list_items;
    ArrayList<item_report_attendance> all_items;

    MaterialBetterSpinner month_spinner;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_coach_reports_attendance,container,false);

        globalVars = (GlobalVars) getActivity().getApplication();

        layoutManager = new MyCustomLayoutManager(getActivity());
        mSwipeRefreshLayout = root.findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initilizeAttendList();
            }
        });
        customRecyclerView = root.findViewById(R.id.customRecyclerView);
        customRecyclerView.setmEmptyView(R.drawable.ic_faded_certificates,R.string.no_certificates);

        customRecyclerView.setOnRetryClick(new myCustomRecyclerView.OnRetryClick() {
            @Override
            public void onRetry() {
                initilizeAttendList();
            }
        });
        recyclerView = customRecyclerView.getRecyclerView();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });
        list_items = new ArrayList<>();
        all_items = new ArrayList<>();

        month_spinner = root.findViewById(R.id.monthSpinner_reportsattendance);
        final ArrayAdapter<CharSequence> month_spinner_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.month_array, android.R.layout.simple_spinner_item);
        month_spinner.setAdapter(month_spinner_adapter);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.smoothScrollToPosition(recyclerView.getVerticalScrollbarPosition());


        initilizeAttendList();

        recyclerView_adapter_reportattendance = new RecyclerView_Adapter_reportattendance(list_items, getContext());
        recyclerView.setAdapter(recyclerView_adapter_reportattendance);

        month_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String text = month_spinner.getText().toString();
                if (text.equals("None"))
                    month_spinner.setText("");
                filterMonths(position);
            }
        });

        return root;
    }

    private void filterMonths(int month) {
        list_items.clear();
        Calendar calendar = Calendar.getInstance();
        for (item_report_attendance item:all_items){
            calendar.setTime(item.class_date);
            int AttendMonth = calendar.get(Calendar.MONTH);
            if (AttendMonth == month || month == 12){
                list_items.add(item);
            }
        }
        recyclerView_adapter_reportattendance.notifyDataSetChanged();
    }
    private boolean checkConnection() {
        // first, check connectivity
        if (ConnectionUtilities
                .checkInternetConnection(getContext())) {
            return true;
        }
        return false;
    }

    private void initilizeAttendList() {
        if (!checkConnection()){
            customRecyclerView.retry();
            return;
        }
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
        mSwipeRefreshLayout.setRefreshing(false);
        list_items.clear();
        all_items.clear();
        if (response != null) {
            try {
                for (int i = 0; i < response.length(); i++) {
                    item_report_attendance entity = new item_report_attendance(response.getJSONObject(i));
                    list_items.add(entity);
                    all_items.add(entity);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        recyclerView_adapter_reportattendance.notifyDataSetChanged();
        customRecyclerView.notifyChange(list_items.size());
    }

}
