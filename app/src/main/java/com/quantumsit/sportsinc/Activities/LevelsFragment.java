package com.quantumsit.sportsinc.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Adapters.CoursesAdapter;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.CustomView.myCustomListView;
import com.quantumsit.sportsinc.CustomView.myCustomListViewListener;
import com.quantumsit.sportsinc.Entities.CourseEntity;
import com.quantumsit.sportsinc.R;
import com.quantumsit.sportsinc.util.ConnectionUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LevelsFragment extends Fragment {
    private CoursesAdapter adapter;
    private List<CourseEntity> courseList;

    myCustomListView customListView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ListView listView;
    myCustomListViewListener listViewListener;
    int limitValue,currentStart;
    private boolean connectionStatus;
    int program_id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_levels,container,false);
        limitValue = getResources().getInteger(R.integer.selectLimit);
        currentStart = 0;
        program_id = getActivity().getIntent().getIntExtra("program_id",0);
        mSwipeRefreshLayout = root.findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentStart = 0;
                initilizeCourses(false);
            }
        });
        customListView = root.findViewById(R.id.customListView);
        customListView.setmEmptyView(R.drawable.ic_assignment,R.string.no_Courses);

        customListView.setOnRetryClick(new myCustomListView.OnRetryClick() {
            @Override
            public void onRetry() {
                currentStart = 0;
                initilizeCourses(false);
            }
        });
        listView = customListView.getListView();
        listViewListener = new myCustomListViewListener(listView ,mSwipeRefreshLayout) {
            @Override
            public void loadMoreData() {
                if (courseList.size() >= limitValue)
                    ListLoadMore();
            }
        };
        listView.setOnScrollListener(listViewListener);
        courseList=new ArrayList<>();

        adapter = new CoursesAdapter(getContext(),R.layout.list_item_level,courseList);
        listView.setAdapter(adapter);
        listView.setClickable(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i>= courseList.size())
                    return;
                Intent intent = new Intent(getContext(), CourseDetailsActivity.class);
                intent.putExtra("MyCourse",courseList.get(i));
                startActivity(intent);
            }
        });

        if (savedInstanceState == null)
            initilizeCourses(false);
        else
            fillBySavedState(savedInstanceState);
        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("ScrollPosition", listView.onSaveInstanceState());
        outState.putSerializable("CoursesList", (Serializable) courseList);
    }


    private void fillBySavedState(Bundle savedInstanceState) {
        ArrayList<CourseEntity> list1 = (ArrayList<CourseEntity>) savedInstanceState.getSerializable("CoursesList");
        courseList.addAll(list1);
        Parcelable mListInstanceState = savedInstanceState.getParcelable("ScrollPosition");
        customListView.notifyChange(courseList.size());
        adapter.notifyDataSetChanged();
        listView.onRestoreInstanceState(mListInstanceState);
    }

    private void ListLoadMore() {
        customListView.loadMore();
        currentStart = courseList.size();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initilizeCourses(true);
            }
        }, 1500);
    }

    private void initilizeCourses(final boolean loadMore) {
        if (!isAdded()) {
            return;
        }
        if (!checkConnection()){
            customListView.retry();
            return;
        }
        try {
            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.selectData);

            JSONObject where_info = new JSONObject();
            where_info.put(getString(R.string.Key_programID),program_id);

            JSONObject limit_info = new JSONObject();
            limit_info.put("start", currentStart);
            limit_info.put("limit", limitValue);
            HashMap<String, String> params = new HashMap<>();
            params.put("table", "courses");
            params.put("ordered","true");
            params.put("limit", limit_info.toString());
            params.put("where",where_info.toString());

            httpCall.setParams(params);
            new HttpRequest() {
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    connectionStatus = connectionTimeOut;
                    fillAdapter(response, loadMore);
                }
            }.execute(httpCall);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillAdapter(JSONArray response ,boolean loadMore) {
        mSwipeRefreshLayout.setRefreshing(false);
        if (!loadMore)
            courseList.clear();
        if (response != null) {
            try {
                for (int i = 0; i < response.length(); i++) {
                    courseList.add(new CourseEntity( response.getJSONObject(i)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (connectionStatus){
            customListView.timeOut();
            return;
        }
        customListView.notifyChange(courseList.size());
        adapter.notifyDataSetChanged();
        listViewListener.setLoading(false);
    }
    private boolean checkConnection() {
        // first, check connectivity
        if (ConnectionUtilities
                .checkInternetConnection(getContext())) {
            return true;
        }
        return false;
    }
}
