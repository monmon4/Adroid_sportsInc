package com.quantumsit.sportsinc.AboutUs_fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Adapters.ListView_Adapter_about_us;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.Entities.item_about;
import com.quantumsit.sportsinc.CustomView.myCustomListViewListener;
import com.quantumsit.sportsinc.R;
import com.quantumsit.sportsinc.util.ConnectionUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class AboutFragment extends Fragment {

    ListView listView;
    ListView_Adapter_about_us listView_adapter;

    SwipeRefreshLayout mSwipeRefreshLayout;
    LinearLayout retry;
    LinearLayout timeOut;
    ProgressBar progressBar;
    RelativeLayout loading;

    ArrayList<item_about> items;
    private boolean connectionStatus;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_about,container,false);

        mSwipeRefreshLayout = root.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                get_list_items();
            }
        });
        loading = root.findViewById(R.id.LoadingData);
        progressBar = root.findViewById(R.id.progress_bar);
        retry = root.findViewById(R.id.layout_retry);
        timeOut = root.findViewById(R.id.layout_timeOut);

        listView = root.findViewById(R.id.listview_about_us);
        listView.setSelector(android.R.color.transparent);
        listView.setOnScrollListener(new myCustomListViewListener(listView, mSwipeRefreshLayout) {
            @Override
            public void loadMoreData() {

            }
        });
        items = new ArrayList<>();

        get_list_items();

        listView_adapter = new ListView_Adapter_about_us(getContext(), items);
        listView.setAdapter(listView_adapter);


        return root;
    }

    private void get_list_items(){
        if (!checkConnection()){
            progressBar.setVisibility(View.GONE);
            timeOut.setVisibility(View.GONE);
            retry.setVisibility(View.VISIBLE);
            return;
        }
        JSONObject where_info = new JSONObject();
        try {
            where_info.put("type",0);

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.selectData);
            HashMap<String,String> params = new HashMap<>();
            params.put("table","rules");
            params.put("where",where_info.toString());

            httpCall.setParams(params);

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    connectionStatus = connectionTimeOut;
                    fill_list_items(response );
                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fill_list_items (JSONArray response) {
        items.clear();
        mSwipeRefreshLayout.setRefreshing(false);
        if (response != null) {
            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject result = null;

                    result = response.getJSONObject(i);
                    int type = result.getInt("Type");
                    String title = result.getString("Title");
                    String content = result.getString("Content");
                    items.add(new item_about(type, title, content));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (connectionStatus){
            progressBar.setVisibility(View.GONE);
            timeOut.setVisibility(View.VISIBLE);
            return;
        }
        listView_adapter.notifyDataSetChanged();
        loading.setVisibility(View.GONE);
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
