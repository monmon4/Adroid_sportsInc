package com.quantumsit.sportsinc.Home_fragments;

import android.app.ProgressDialog;
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
import android.widget.GridView;
import android.widget.ListView;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Activities.CourseDetailsActivity;
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

public class Courses2Fragment extends Fragment {

    private GridView courses_gridView;
    private  GridView_courses_Adapter gridView_courses_adapter;

    ArrayList<item_courses> items;

    ProgressDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_courses2,container,false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait ....");

        items = new ArrayList<>();
        courses_gridView = root.findViewById(R.id.gridView_courses);
        gridView_courses_adapter = new GridView_courses_Adapter(getContext(), items);
        courses_gridView.setAdapter(gridView_courses_adapter);

        progressDialog.show();
        get_courses();

        return root;
    }

    /*private void initilizeCourses(final boolean loadMore) {
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

            JSONObject limit_info = new JSONObject();
            limit_info.put("start", currentStart);
            limit_info.put("limit", limitValue);
            HashMap<String, String> params = new HashMap<>();
            params.put("table", "courses");
            params.put("limit", limit_info.toString());

            httpCall.setParams(params);
            new HttpRequest() {
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    fillAdapter(response, loadMore);
                }
            }.execute(httpCall);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/

    private void get_courses() {

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.selectData);

            HashMap<String, String> params = new HashMap<>();
            params.put("table", "courses");

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

        if (response != null) {
            try {
                for (int i = 0; i < response.length(); i++) {
                    items.add(new item_courses( response.getJSONObject(i)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        gridView_courses_adapter.notifyDataSetChanged();
        progressDialog.dismiss();
    }

    /*private void fillAdapter(JSONArray response ,boolean loadMore) {
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
        customListView.notifyChange(courseList.size());
        adapter.notifyDataSetChanged();
        listViewListener.setLoading(false);
    }*/

    private boolean checkConnection() {
        // first, check connectivity
        if (ConnectionUtilities
                .checkInternetConnection(getContext())) {
            return true;
        }
        return false;
    }
}
