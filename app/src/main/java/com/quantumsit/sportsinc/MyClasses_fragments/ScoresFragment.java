package com.quantumsit.sportsinc.MyClasses_fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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

        mSwipeRefreshLayout = root.findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fill_list();
            }
        });
        customRecyclerView = root.findViewById(R.id.customRecyclerView);
        customRecyclerView.setmEmptyView(R.drawable.ic_faded_my_classes,R.string.no_scores);

        customRecyclerView.setOnRetryClick(new myCustomRecyclerView.OnRetryClick() {
            @Override
            public void onRetry() {
                fill_list();
            }
        });
        recycler_view = customRecyclerView.getRecyclerView();
        recycler_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });

        recycler_view.setHasFixedSize(false);

        list_item = new ArrayList<>();

        layoutManager = new MyCustomLayoutManager(getActivity());
        recycler_view.setLayoutManager(layoutManager);
        recycler_view.smoothScrollToPosition(recycler_view.getVerticalScrollbarPosition());
        fill_list();


        recycler_view_adapter = new RecyclerView_Adapter_scores(list_item, getActivity());
        recycler_view.setAdapter(recycler_view_adapter);

        return root;
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
    private void fill_list() {
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
            params.put("where",where_info.toString());

            httpCall.setParams(params);
            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    fill_recycler_view(response);

                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fill_recycler_view(JSONArray response){
        list_item.clear();
        mSwipeRefreshLayout.setRefreshing(false);
        try {
            if (response != null) {
                for (int i=0; i<response.length(); i++){
                    JSONObject result = response.getJSONObject(i);
                    String course_name = result.getString("course_name");
                    String group_name = result.getString("group_name");
                    String class_date = result.getString("class_date");
                    int class_number = result.getInt("class_number");
                    int score = result.getInt("attend");
                    int attend = result.getInt("score");
                    String coach_name = result.getString("coach_name");
                    String coach_notes = result.getString("coach_note");
                    list_item.add(new item_single_scores(course_name, group_name, class_date, coach_name, coach_notes, attend, score, class_number));
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        recycler_view_adapter.notifyDataSetChanged();
        customRecyclerView.notifyChange(list_item.size());

    }
}
