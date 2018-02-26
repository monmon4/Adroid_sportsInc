package com.quantumsit.sportsinc.COACHES.ReportsFragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.COACHES.ActivityFinishedCourseSingle_coach;
import com.quantumsit.sportsinc.CustomView.myCustomListView;
import com.quantumsit.sportsinc.CustomView.myCustomListViewListener;
import com.quantumsit.sportsinc.CustomView.myCustomRecyclerView;
import com.quantumsit.sportsinc.R;
import com.quantumsit.sportsinc.util.ConnectionUtilities;

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
    myCustomListView customListView;
    myCustomListViewListener listViewListener;
    int limitValue,currentStart;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ListViewFinishedCoursesReports_Adapter listView_adapter;

    ArrayList<item_reports_finished_courses> list_items;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_coach_reports_finished_courses,container,false);

        limitValue = getResources().getInteger(R.integer.selectLimit);
        currentStart = 0;

        mSwipeRefreshLayout = root.findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentStart = 0;
                initilizeFinishedList(false);
            }
        });
        customListView = root.findViewById(R.id.customListView);
        customListView.setmEmptyView(R.drawable.ic_assignment,R.string.no_finished);

        customListView.setOnRetryClick(new myCustomListView.OnRetryClick() {
            @Override
            public void onRetry() {
                currentStart = 0;
                initilizeFinishedList(false);
            }
        });
        listView = customListView.getListView();
        listViewListener = new myCustomListViewListener(listView , mSwipeRefreshLayout) {
            @Override
            public void loadMoreData() {
                if (list_items.size() >= limitValue)
                    listLoadMore();
            }
        };
        listView.setOnScrollListener(listViewListener);

        globalVars = (GlobalVars) getActivity().getApplication();

        list_items = new ArrayList<>();
        listView_adapter = new ListViewFinishedCoursesReports_Adapter(getContext(), list_items);
        listView.setAdapter(listView_adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= list_items.size())
                    return;
                Intent intent = new Intent(getActivity(), ActivityFinishedCourseSingle_coach.class);
                intent.putExtra("finishedGroup",list_items.get(position));
                startActivity(intent);
            }
        });

        if (savedInstanceState == null)
            initilizeFinishedList(false);

        else
            fillBySavedState(savedInstanceState);
        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("listItems",list_items);
        outState.putParcelable("ScrollPosition",listView.onSaveInstanceState());
    }

    private void fillBySavedState(Bundle savedInstanceState) {
        ArrayList<item_reports_finished_courses> list1 = (ArrayList<item_reports_finished_courses>) savedInstanceState.getSerializable("listItems");
        list_items.addAll(list1);
        Parcelable mListInstanceState = savedInstanceState.getParcelable("ScrollPosition");
        customListView.notifyChange(list_items.size());
        listView_adapter.notifyDataSetChanged();
        listView.onRestoreInstanceState(mListInstanceState);
    }

    private void listLoadMore() {
        customListView.loadMore();
        currentStart = list_items.size();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initilizeFinishedList(true);
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

    private void initilizeFinishedList(final boolean loadMore) {
        if (!isAdded()) {
            return;
        }
        if (!checkConnection()){
            customListView.retry();
            return;
        }
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
            JSONObject limit_info = new JSONObject();
            limit_info.put("start", currentStart);
            limit_info.put("limit", limitValue);
            params.put("limit",limit_info.toString());

            params.put("where", where_info.toString());

            httpCall.setParams(params);

            new HttpRequest() {
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    Log.d(TAG,String.valueOf(response));
                    fillAdapter(response , loadMore);
                }
            }.execute(httpCall);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillAdapter(JSONArray response , boolean loadMore) {
        if (!loadMore)
            list_items.clear();
        mSwipeRefreshLayout.setRefreshing(false);
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
        customListView.notifyChange(list_items.size());
        listView_adapter.notifyDataSetChanged();
        listViewListener.setLoading(false);
    }
}
