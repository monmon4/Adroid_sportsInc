package com.quantumsit.sportsinc.COACHES.ReportsFragments;


import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Aaa_looks.MyCustomLayoutManager;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.CustomView.myCustomRecyclerView;
import com.quantumsit.sportsinc.CustomView.myCustomRecyclerViewListener;
import com.quantumsit.sportsinc.R;
import com.quantumsit.sportsinc.util.ConnectionUtilities;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    myCustomRecyclerViewListener listener;
    int limitValue, currentStart;
    int selectedPosition = 12;

    private int currentVisiblePosition = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_coach_reports_attendance,container,false);

        globalVars = (GlobalVars) getActivity().getApplication();
        limitValue = getResources().getInteger(R.integer.selectLimit);
        currentStart = 0;

        layoutManager = new MyCustomLayoutManager(getActivity());
        mSwipeRefreshLayout = root.findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentStart = 0;
                initilizeAttendList(false);
            }
        });
        customRecyclerView = root.findViewById(R.id.customRecyclerView);
        customRecyclerView.setmEmptyView(R.drawable.ic_faded_certificates,R.string.no_attendance);

        customRecyclerView.setOnRetryClick(new myCustomRecyclerView.OnRetryClick() {
            @Override
            public void onRetry() {
                currentStart = 0;
                initilizeAttendList(false);
            }
        });
        recyclerView = customRecyclerView.getRecyclerView();


        recyclerView.setLayoutManager(layoutManager);
        recyclerView.smoothScrollToPosition(recyclerView.getVerticalScrollbarPosition());

        listener =  new myCustomRecyclerViewListener(layoutManager) {
            @Override
            protected void onDownWhileLoading() {
                if (isLoading())
                    customRecyclerView.loadMore();
            }

            @Override
            protected void onUpWhileLoading() {
                customRecyclerView.finishLoading();
            }

            @Override
            public void onLoadMore() {
                if (all_items.size() >= limitValue)
                    AttendanceLoadMore();
            }};

        recyclerView.addOnScrollListener(listener);

        list_items = new ArrayList<>();
        all_items = new ArrayList<>();

        month_spinner = root.findViewById(R.id.monthSpinner_reportsattendance);
        final ArrayAdapter<CharSequence> month_spinner_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.month_array, android.R.layout.simple_spinner_item);
        month_spinner.setAdapter(month_spinner_adapter);

        recyclerView_adapter_reportattendance = new RecyclerView_Adapter_reportattendance(list_items, getContext());
        recyclerView.setAdapter(recyclerView_adapter_reportattendance);

        month_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectedPosition = position;
                String text = month_spinner.getText().toString();
                if (text.equals("None"))
                    month_spinner.setText("");
                filterMonths(position);
            }
        });


        if (savedInstanceState == null)
            initilizeAttendList(false);

        else
            fillBySavedState(savedInstanceState);

        return root;
    }

    private void fillBySavedState(Bundle savedInstanceState) {
        ArrayList<item_report_attendance> list1 = (ArrayList<item_report_attendance>) savedInstanceState.getSerializable("all_items");
        all_items.addAll(list1);
        selectedPosition = savedInstanceState.getInt("SelectedMonth");
        currentVisiblePosition = savedInstanceState.getInt("Position");
        filterMonths(selectedPosition);
       recyclerView.smoothScrollToPosition(currentVisiblePosition);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("all_items",all_items);
        outState.putInt("SelectedMonth",selectedPosition);
        outState.putInt("Position",currentVisiblePosition);
    }

    @Override
    public void onPause() {
        super.onPause();
        currentVisiblePosition = 0;
        currentVisiblePosition = layoutManager.findFirstCompletelyVisibleItemPosition();
    }

    private void AttendanceLoadMore() {
       // customRecyclerView.loadMore();
        currentStart = all_items.size();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initilizeAttendList(true);
            }
        }, 1500);
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
        customRecyclerView.notifyChange(list_items.size());
        customRecyclerView.finishLoading();
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

    private void initilizeAttendList(final boolean loadMore) {
        if (!isAdded()) {
            return;
        }
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

            JSONObject limit_info = new JSONObject();
            limit_info.put("start", currentStart);
            limit_info.put("limit", limitValue);

            params.put("where", where_info.toString());
            params.put("limit",limit_info.toString());

            httpCall.setParams(params);

            new HttpRequest() {
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    fillAdapter(response,loadMore);
                }
            }.execute(httpCall);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillAdapter(JSONArray response , boolean loadMore) {
        mSwipeRefreshLayout.setRefreshing(false);
        if (!loadMore) {
            list_items.clear();
            all_items.clear();
        }
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
        filterMonths(selectedPosition);
        listener.setLoading(false);
    }

}
