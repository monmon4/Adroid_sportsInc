package com.quantumsit.sportsinc.COACHES.RequestFragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Activities.RequestDetailsActivity;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.COACHES.Adapter.ListView_Adapter_request_coach;
import com.quantumsit.sportsinc.COACHES.Entities.item_request_coach;
import com.quantumsit.sportsinc.CustomView.myCustomListView;
import com.quantumsit.sportsinc.CustomView.myCustomListViewListener;
import com.quantumsit.sportsinc.R;
import com.quantumsit.sportsinc.util.ConnectionUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Bassam on 2/12/2018.
 */

public class CoachRequestReceivedFragment extends Fragment {

    GlobalVars globalVars;

    ListView listView;
    myCustomListView customListView;
    myCustomListViewListener listViewListener;
    int limitValue,currentStart;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<item_request_coach> list_items;
    ListView_Adapter_request_coach arrayAdapter;

    private int REQUEST_UPDATE = 7;
    private int request_position = -1;
    private boolean connectionStatus;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_coach_request_received,container,false);

        globalVars = (GlobalVars) getActivity().getApplication();
        limitValue = getResources().getInteger(R.integer.selectLimit);
        currentStart = 0;

        mSwipeRefreshLayout = root.findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentStart = 0;
                initializeRequests(false);
            }
        });
        customListView = root.findViewById(R.id.customListView);
        customListView.setmEmptyView(R.drawable.ic_assignment,R.string.no_requests);

        customListView.setOnRetryClick(new myCustomListView.OnRetryClick() {
            @Override
            public void onRetry() {
                currentStart = 0;
                initializeRequests(false);
            }
        });
        listView = customListView.getListView();
        listViewListener = new myCustomListViewListener(listView ,mSwipeRefreshLayout) {
            @Override
            public void loadMoreData() {
                if (list_items.size() >= limitValue)
                    listLoadMore();
            }
        };
        listView.setOnScrollListener(listViewListener);
        list_items = new ArrayList<>();

        arrayAdapter = new ListView_Adapter_request_coach(getContext(), list_items);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i >= list_items.size())
                    return;
                Intent intent = new Intent(getContext(), RequestDetailsActivity.class);
                request_position = i;
                intent.putExtra("MyRequest", list_items.get(i));
                intent.putExtra("requestType",1);
                startActivityForResult(intent ,REQUEST_UPDATE);
            }
        });
        if (savedInstanceState == null)
            initializeRequests(true);

        else
            fillBySavedState(savedInstanceState);

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_UPDATE && resultCode == AppCompatActivity.RESULT_OK && data != null){
            int new_status = data.getIntExtra("request_status", 2);
            list_items.get(request_position).setStatus(new_status);
            arrayAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("ScrollPosition", listView.onSaveInstanceState());
        outState.putSerializable("RequestsList", list_items);
    }


    private void fillBySavedState(Bundle savedInstanceState) {
        ArrayList<item_request_coach>list1 = (ArrayList<item_request_coach>) savedInstanceState.getSerializable("RequestsList");
        list_items.addAll(list1);
        Parcelable mListInstanceState = savedInstanceState.getParcelable("ScrollPosition");
        customListView.notifyChange(list_items.size());
        arrayAdapter.notifyDataSetChanged();
        listView.onRestoreInstanceState(mListInstanceState);
    }

    private void listLoadMore() {
        customListView.loadMore();
        currentStart = list_items.size();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initializeRequests(true);
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


    private void initializeRequests(final boolean loadMore) {
        if (!isAdded())
            return;
        if (!checkConnection()){
            customListView.retry();
            return;
        }
        try {
            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.join);
            JSONObject where_info = new JSONObject();
            where_info.put("requests.to_id", globalVars.getId());
            JSONObject limit_info = new JSONObject();
            limit_info.put("start", currentStart);
            limit_info.put("limit", limitValue);
            String OnCondition = "requests.from_id = users.id";

            HashMap<String, String> params = new HashMap<>();
            params.put("table1", "requests");
            params.put("table2", "users");

            params.put("where", where_info.toString());
            params.put("limit",limit_info.toString());
            params.put("on", OnCondition);

            httpCall.setParams(params);
            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    connectionStatus =connectionTimeOut;
                    fillAdapter(response , loadMore);
                }
            }.execute(httpCall);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillAdapter(JSONArray response , boolean loadMore) {
        mSwipeRefreshLayout.setRefreshing(false);
        if (!loadMore)
            list_items.clear();
        if (response != null) {
            try {
                for (int i = 0; i < response.length(); i++) {
                    item_request_coach item = new item_request_coach();
                    item.fillRequest(response.getJSONObject(i));
                    list_items.add(item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (connectionStatus){
            customListView.timeOut();
            return;
        }
        customListView.notifyChange(list_items.size());
        arrayAdapter.notifyDataSetChanged();
        listViewListener.setLoading(false);
    }
}