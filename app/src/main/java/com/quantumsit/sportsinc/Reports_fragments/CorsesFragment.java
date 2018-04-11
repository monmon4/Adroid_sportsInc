package com.quantumsit.sportsinc.Reports_fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Aaa_looks.MyCustomLayoutManager;
import com.quantumsit.sportsinc.Aaa_looks.RecyclerView_Adapter_reportcourses;
import com.quantumsit.sportsinc.Aaa_looks.item_single_reports_courses;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.CustomView.myCustomRecyclerView;
import com.quantumsit.sportsinc.CustomView.myCustomRecyclerViewListener;
import com.quantumsit.sportsinc.R;
import com.quantumsit.sportsinc.util.ConnectionUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CorsesFragment extends Fragment {

    private MyCustomLayoutManager layoutManager;
    myCustomRecyclerView customRecyclerView;
    myCustomRecyclerViewListener listener;
    int limitValue, currentStart;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView recycler_view;
    private RecyclerView_Adapter_reportcourses recycler_view_adapter;
    public List<item_single_reports_courses> list_item;

    GlobalVars globalVars;
    private boolean connectionStatus;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_corses,container,false);

        globalVars = (GlobalVars) getActivity().getApplication();
        limitValue = getResources().getInteger(R.integer.selectLimit);
        currentStart = 0;

        mSwipeRefreshLayout = root.findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.parseColor("#df1b1c"));
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#FFFFFF"));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentStart = 0;
                fill_list_items(false);
            }
        });
        customRecyclerView = root.findViewById(R.id.customRecyclerView);
        customRecyclerView.setmEmptyView(R.drawable.ic_faded_reports,R.string.no_courses_reports);

        customRecyclerView.setOnRetryClick(new myCustomRecyclerView.OnRetryClick() {
            @Override
            public void onRetry() {
                currentStart =0 ;
                fill_list_items(false);
            }
        });
        recycler_view = customRecyclerView.getRecyclerView();
        layoutManager = new MyCustomLayoutManager(getActivity());
        recycler_view.setLayoutManager(layoutManager);
        recycler_view.smoothScrollToPosition(recycler_view.getVerticalScrollbarPosition());

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
                if (list_item.size() >= limitValue)
                    listLoadMore();
            }};

        recycler_view.addOnScrollListener(listener);

        recycler_view.setHasFixedSize(false);

        list_item = new ArrayList<>();
        recycler_view_adapter = new RecyclerView_Adapter_reportcourses(list_item, getActivity());
        recycler_view.setAdapter(recycler_view_adapter);

        if (savedInstanceState == null)
            fill_list_items(false);

        return root;
    }

    private void listLoadMore() {
        currentStart = list_item.size();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fill_list_items(true);
            }
        }, 1500);
    }

    private boolean checkConnection() {
        // first, check connectivity
        if (ConnectionUtilities
                .checkInternetConnection(getContext())) {
            return true;
        }
        return false;
    }

    @SuppressLint("StaticFieldLeak")
    private void fill_list_items(final boolean loadMore) {
        if (!checkConnection()){
            customRecyclerView.retry();
            return;
        }

        JSONObject where_info = new JSONObject();
        JSONObject or_where_info = new JSONObject();
        try {
            where_info.put("trainee_id",globalVars.getId());
            or_where_info.put("parent_id",globalVars.getId());

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.traineeCoursesData);
            HashMap<String,String> params = new HashMap<>();
            JSONObject limit_info = new JSONObject();
            limit_info.put("start", currentStart);
            limit_info.put("limit", limitValue);

            params.put("where", where_info.toString());
            params.put("or_where", or_where_info.toString());
            params.put("limit",limit_info.toString());

            httpCall.setParams(params);
            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    connectionStatus = connectionTimeOut;
                    fill_recycler_view(response , loadMore);

                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fill_recycler_view(JSONArray response , boolean loadMore) {
        if (!loadMore)
            list_item.clear();
        mSwipeRefreshLayout.setRefreshing(false);
        try {
            if (response != null) {
                for (int i=0; i<response.length(); i++){
                    JSONObject result = response.getJSONObject(i);
                    int course_id = result.getInt("course_id");
                    int group_id = result.getInt("group_id");
                    String course_name = result.getString("course_name");
                    String group_name = result.getString("group_name");
                    int classes_num = result.getInt("Num_classes");
                    int attend_num = result.getInt("attend_num");
                    double attendance = ((double) attend_num/(double) classes_num) *100.0;
                    int total_score = result.getInt("total_score");
                    int trainee_id = result.getInt("trainee_id");
                    String trainee_name = result.getString("trainee_name");
                    list_item.add(new item_single_reports_courses(trainee_id ,trainee_name ,course_name, group_name, course_id, group_id, attendance, total_score));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (connectionStatus){
            customRecyclerView.timeOut();
            return;
        }
        customRecyclerView.notifyChange(list_item.size());
        customRecyclerView.finishLoading();
        recycler_view_adapter.person_id = globalVars.getId();
        if (globalVars.myAccount == null)
            recycler_view_adapter.parent = false;
        else
            recycler_view_adapter.parent = true;
        recycler_view_adapter.notifyDataSetChanged();
        listener.setLoading(false);
    }
}
