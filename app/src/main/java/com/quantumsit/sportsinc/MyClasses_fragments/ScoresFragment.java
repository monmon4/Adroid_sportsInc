package com.quantumsit.sportsinc.MyClasses_fragments;

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
import com.quantumsit.sportsinc.Adapters.RecyclerView_Adapter_scores;
import com.quantumsit.sportsinc.Entities.item_single_scores;
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

        /*
        * Fragment Show Trainee Scores For the Finished Classes of
        * his Running Group(the Group he/she is participated in , that not all of it's classes finished)
        * */

        globalVars = (GlobalVars) getActivity().getApplication();
        user_id = globalVars.getId();
        limitValue = getResources().getInteger(R.integer.selectLimit);
        currentStart = 0;

        /*
        *   SwipeRefresh is for reload the fragment Content on swipe down.
        *
        * */
        mSwipeRefreshLayout = root.findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.parseColor("#df1b1c"));
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#FFFFFF"));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentStart = 0;
                fill_list(false);
            }
        });
        /*
        * Custom Recycler View is a View That show no connection view if there is no internet else
        * show progress bar loading while
        * the data be getting from serve then show the data OR empty view if there
        * is no data returned...
        *
        * */
        customRecyclerView = root.findViewById(R.id.customRecyclerView);
        customRecyclerView.setmEmptyView(R.drawable.ic_faded_my_classes,R.string.no_scores);

        /*
        * retry is an element of the custom Recycler View that show if there is no
        * internet connection and on it's click retry to connect to server if there is internet
        *
        * */
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
        /*
        * a listener in the custom Recycler View that load more data on scrolling down
        *
        * */
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
                else
                    listener.setLoading(false);
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
        /*
        *
        *   method to retrieve Trainee Score data from server
        *   by a defined limit to not prevent memory leak as possible
        *
        * */

        JSONObject where_info = new JSONObject();
        JSONObject or_where_info = new JSONObject();
        try {
            where_info.put(getString(R.string.where_trainee_id),globalVars.getId());
            or_where_info.put(getString(R.string.where_parent_id),globalVars.getId());

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.traineeClassScores);
            HashMap<String,String> params = new HashMap<>();
            JSONObject limit_info = new JSONObject();
            limit_info.put(getString(R.string.select_start), currentStart);
            limit_info.put(getString(R.string.select_limit), limitValue);
            params.put(getString(R.string.parameter_limit),limit_info.toString());
            params.put(getString(R.string.parameter_where), where_info.toString());
            params.put(getString(R.string.parameter_or_where), or_where_info.toString());

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
        /*
        * method to fill the items of the recycler view from the response from the server
        *
        * */
        if (!loadMore)
            list_item.clear();
        mSwipeRefreshLayout.setRefreshing(false);
        try {
            if (response != null) {
               /* JSONObject first_result = response.getJSONObject(0);
                int Num_classes = first_result.getInt("Num_classes");
                int num_attended_classes = 0;*/
                for (int i=0; i<response.length(); i++){
                    JSONObject result = response.getJSONObject(i);
                    /*String course_name = result.getString("course_name");
                    String group_name = result.getString("group_name");
                    String class_date = result.getString("class_date");
                    int class_number = result.getInt("class_number");
                    int attend = result.getInt("attend");

                    if (attend == 1)
                        num_attended_classes++;

                    int score = num_attended_classes;
                    String coach_name = result.getString("coach_name");
                    String coach_notes = result.getString("coach_note");*/
                    list_item.add(new item_single_scores(result));
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        recycler_view_adapter.person_id  = globalVars.getId();
        if (globalVars.myAccount != null)
            recycler_view_adapter.parent = true;
        else
            recycler_view_adapter.parent = false;
        customRecyclerView.notifyChange(list_item.size());
        customRecyclerView.finishLoading();
        recycler_view_adapter.notifyDataSetChanged();
        listener.setLoading(false);

    }
}
