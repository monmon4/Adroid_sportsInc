package com.quantumsit.sportsinc.MyClasses_fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Aaa_looks.MyCustomLayoutManager;
import com.quantumsit.sportsinc.Aaa_looks.RecyclerView_Adapter_scores;
import com.quantumsit.sportsinc.Aaa_looks.item1_reports_courses;
import com.quantumsit.sportsinc.Aaa_looks.item_single_scores;
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


public class ScoresFragment extends Fragment {

    private MyCustomLayoutManager layoutManager;
    private RecyclerView recycler_view;
    myCustomRecyclerView customRecyclerView;
    myCustomRecyclerViewListener listener;
    int limitValue, currentStart;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView_Adapter_scores recycler_view_adapter;
    public List<item_single_scores> list_item;

    GlobalVars globalVars;
    int user_id;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_scores,container,false);

        globalVars = (GlobalVars) getActivity().getApplication();
        user_id = globalVars.getId();
        limitValue = getResources().getInteger(R.integer.selectLimit);
        currentStart = 0;

        mSwipeRefreshLayout = root.findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentStart = 0;
                fill_list(false);
            }
        });
        customRecyclerView = root.findViewById(R.id.customRecyclerView);
        customRecyclerView.setmEmptyView(R.drawable.ic_faded_my_classes,R.string.no_scores);

        customRecyclerView.setOnRetryClick(new myCustomRecyclerView.OnRetryClick() {
            @Override
            public void onRetry() {
                currentStart = 0;
                fill_list(false);
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
        recycler_view_adapter = new RecyclerView_Adapter_scores(list_item, getActivity());
        recycler_view.setAdapter(recycler_view_adapter);

        if (savedInstanceState == null)
            fill_list(false);


        return root;
    }


    private void listLoadMore() {
        currentStart = list_item.size();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fill_list(true);
            }
        }, 1200);
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
    private void fill_list(final boolean loadMore) {
        if (!checkConnection()){
            customRecyclerView.retry();
            return;
        }

        JSONObject where_info = new JSONObject();
        try {
            where_info.put("trainee_id",globalVars.getId());

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.traineeClassScores);
            HashMap<String,String> params = new HashMap<>();
            JSONObject limit_info = new JSONObject();
            limit_info.put("start", currentStart);
            limit_info.put("limit", limitValue);
            params.put("limit",limit_info.toString());
            params.put("where", where_info.toString());

            httpCall.setParams(params);
            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    fill_recycler_view(response , loadMore);

                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fill_recycler_view(JSONArray response , boolean loadMore){
        if (!loadMore)
            list_item.clear();
        mSwipeRefreshLayout.setRefreshing(false);
        try {
            if (response != null) {
                JSONObject first_result = response.getJSONObject(0);
                int Num_classes = first_result.getInt("Num_classes");
                int num_attended_classes = 0;
                for (int i=0; i<response.length(); i++){
                    JSONObject result = response.getJSONObject(i);
                    String course_name = result.getString("course_name");
                    String group_name = result.getString("group_name");
                    String class_date = result.getString("class_date");
                    int class_number = result.getInt("class_number");
                    int attend = result.getInt("attend");

                    if (attend == 1)
                        num_attended_classes++;

                    int score = num_attended_classes;
                    String coach_name = result.getString("coach_name");
                    String coach_notes = result.getString("coach_note");
                    list_item.add(new item_single_scores(course_name, group_name, class_date, coach_name, coach_notes, attend, score, class_number));
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        customRecyclerView.notifyChange(list_item.size());
        customRecyclerView.finishLoading();
        recycler_view_adapter.notifyDataSetChanged();
        listener.setLoading(false);

    }
}
