package com.quantumsit.sportsinc.COACHES.ReportsFragments;


import android.content.Intent;
import android.os.Bundle;
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
    SwipeRefreshLayout mSwipeRefreshLayout;
    ListViewFinishedCoursesReports_Adapter listView_adapter;

    ArrayList<item_reports_finished_courses> list_items;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_coach_reports_finished_courses,container,false);

        mSwipeRefreshLayout = root.findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initilizeFinishedList();
            }
        });
        customListView = root.findViewById(R.id.customListView);
        customListView.setmEmptyView(R.drawable.ic_assignment,R.string.no_Events);

        customListView.setOnRetryClick(new myCustomListView.OnRetryClick() {
            @Override
            public void onRetry() {
                initilizeFinishedList();
            }
        });
        listView = customListView.getListView();
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition =
                        (listView == null || listView.getChildCount() == 0) ?
                                0 : listView.getChildAt(0).getTop();
                mSwipeRefreshLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });

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

    private boolean checkConnection() {
        // first, check connectivity
        if (ConnectionUtilities
                .checkInternetConnection(getContext())) {
            return true;
        }
        return false;
    }

    private void initilizeFinishedList() {
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
        listView_adapter.notifyDataSetChanged();
        customListView.notifyChange(list_items.size());
    }
}
