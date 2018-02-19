package com.quantumsit.sportsinc.Side_menu_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.quantumsit.sportsinc.Adapters.NotificationAdapter;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.CustomView.myCustomListView;
import com.quantumsit.sportsinc.CustomView.myCustomListViewListener;
import com.quantumsit.sportsinc.Entities.NotificationEntity;
import com.quantumsit.sportsinc.Activities.NotificationDetailsActivity;
import com.quantumsit.sportsinc.R;
import com.quantumsit.sportsinc.util.ConnectionUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private static String TAG = NotificationsFragment.class.getSimpleName();

    GlobalVars globalVars;


    private NotificationAdapter adapter ;
    ListView listView;
    myCustomListView customListView;
    myCustomListViewListener listViewListener;
    int limitValue,currentStart;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private List<NotificationEntity> notificationList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications,container,false);
        globalVars = (GlobalVars) getActivity().getApplication();
        limitValue = getResources().getInteger(R.integer.selectLimit);
        currentStart = 0;

        notificationList = new ArrayList<>();
        initializeNotifications(false);
        mSwipeRefreshLayout = root.findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentStart = 0;
                initializeNotifications(false);
            }
        });
        customListView = root.findViewById(R.id.customListView);
        customListView.setmEmptyView(R.drawable.ic_assignment,R.string.no_notifications);

        customListView.setOnRetryClick(new myCustomListView.OnRetryClick() {
            @Override
            public void onRetry() {
                currentStart = 0;
                initializeNotifications(false);
            }
        });
        listView = customListView.getListView();
        listViewListener = new myCustomListViewListener(listView , mSwipeRefreshLayout) {
            @Override
            public void loadMoreData() {
                listLoadMore();
            }
        };
        listView.setOnScrollListener(listViewListener);

        adapter = new NotificationAdapter(getContext(),R.layout.list_item_notification,notificationList);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i>= notificationList.size())
                    return;
                Intent intent = new Intent(getContext(), NotificationDetailsActivity.class);
                intent.putExtra("MyNotification",notificationList.get(i));
                startActivity(intent);
            }
        });
        return root;
    }

    private void listLoadMore() {
        customListView.loadMore();
        currentStart = notificationList.size();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initializeNotifications(true);
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

    private void initializeNotifications(final boolean loadMore) {
        if (!checkConnection()){
            customListView.retry();
            return;
        }
        try {
            JSONObject where_info = new JSONObject();
            where_info.put("notification.to_id",globalVars.getId());

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.notification);
            HashMap<String,String> params = new HashMap<>();
            params.put("where",where_info.toString());
            JSONObject limit_info = new JSONObject();
            limit_info.put("start", currentStart);
            limit_info.put("limit", limitValue);
            params.put("limit",limit_info.toString());
            httpCall.setParams(params);

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    fillAdapter(response , loadMore);
                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillAdapter(JSONArray response , boolean loadMore) {
        Log.d(TAG,String.valueOf(response));
        mSwipeRefreshLayout.setRefreshing(false);
        if (!loadMore)
            notificationList.clear();
        if (response != null) {
            try {
                for (int i = 0; i < response.length(); i++) {
                    NotificationEntity entity = new NotificationEntity(response.getJSONObject(i));
                    notificationList.add(entity);
                    Log.d("NotificationEntityTest",entity.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        customListView.notifyChange(notificationList.size());
        adapter.notifyDataSetChanged();
        listViewListener.setLoading(false);
    }


}
