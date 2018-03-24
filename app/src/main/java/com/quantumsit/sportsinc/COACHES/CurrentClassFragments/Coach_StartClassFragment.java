package com.quantumsit.sportsinc.COACHES.CurrentClassFragments;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Activities.CourseDetailsActivity;
import com.quantumsit.sportsinc.Adapters.CurrentClassesAdapter;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.CustomView.myCustomListView;
import com.quantumsit.sportsinc.CustomView.myCustomListViewListener;
import com.quantumsit.sportsinc.Entities.classesEntity;
import com.quantumsit.sportsinc.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Bassam on 15/3/2018.
 */

public class Coach_StartClassFragment extends Fragment {

    myCustomListView customListView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ListView listView;
    myCustomListViewListener listViewListener;

    ArrayList<classesEntity>classesList;
    CurrentClassesAdapter adapter;
    private GlobalVars globalVars;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_coach__start_class,container,false);
        globalVars = (GlobalVars) getActivity().getApplication();
        mSwipeRefreshLayout = root.findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initializeClasses();
            }
        });
        customListView = root.findViewById(R.id.customListView);
        customListView.setmEmptyView(R.drawable.ic_assignment,R.string.no_sessions);

        customListView.setOnRetryClick(new myCustomListView.OnRetryClick() {
            @Override
            public void onRetry() {
                initializeClasses();
            }
        });
        listView = customListView.getListView();
        listViewListener = new myCustomListViewListener(listView ,mSwipeRefreshLayout) {
            @Override
            public void loadMoreData() {
            }
        };
        listView.setOnScrollListener(listViewListener);
        classesList =new ArrayList<>();

        adapter = new CurrentClassesAdapter(getContext(),R.layout.list_item_current_class,classesList , globalVars.getId() ,globalVars.getMyDB());
        listView.setAdapter(adapter);

        initializeClasses();
        return root;
    }

    private void initializeClasses() {
        /*
        * get the running classes(the classes of today that is started.) and it's trainees
        *
        * */
        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.POST);
        httpCall.setUrl(Constants.coach_running_class);

        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat(getString(R.string.server_dateFormate), Locale.ENGLISH);
        String TodayDate = df.format(c.getTime());
        df = new SimpleDateFormat(getString(R.string.server_timeFormate), Locale.ENGLISH);
        String TodayTime = df.format(c.getTime());

        HashMap<String, String> params = new HashMap<>();
        params.put(getString(R.string.parameter_id), String.valueOf(globalVars.getId()));
        params.put(getString(R.string.parameter_date),TodayDate);
        params.put(getString(R.string.parameter_time),TodayTime);

        httpCall.setParams(params);

        new HttpRequest() {
            @Override
            public void onResponse(JSONArray response) {
                super.onResponse(response);
                fillAdapter(response);
            }
        }.execute(httpCall);
    }

    private void fillAdapter(JSONArray response) {
        /*
        * Cache the running Classes in the phone to edit it's info. local anyTime then
        * upload this info. to the Server in the end.
        * */
        mSwipeRefreshLayout.setRefreshing(false);
        classesList.clear();
        if (response != null) {
            try {
                for (int i = 0; i < response.length(); i++) {
                    classesEntity info = new classesEntity();
                    info.initClass(response.getJSONObject(i));
                   classesList.add(info);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        customListView.notifyChange(classesList.size());
        adapter.notifyDataSetChanged();
    }
}
