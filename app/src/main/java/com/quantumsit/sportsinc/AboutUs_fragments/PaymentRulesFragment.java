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


public class PaymentRulesFragment extends Fragment {

    ListView listView;
    ListView_Adapter_about_us listView_adapter;

    SwipeRefreshLayout mSwipeRefreshLayout;
    LinearLayout retry;
    ProgressBar progressBar;
    RelativeLayout loading;

    ArrayList<item_about> items;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_payment_rules,container,false);

        listView = root.findViewById(R.id.listView_paymentRules);
        listView.setSelector(android.R.color.transparent);
        items = new ArrayList<>();

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

        get_list_items();

        listView_adapter = new ListView_Adapter_about_us(getContext(), items);
        listView.setAdapter(listView_adapter);
        listView.setOnScrollListener(new myCustomListViewListener(listView, mSwipeRefreshLayout) {
            @Override
            public void loadMoreData() {

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

    private void get_list_items(){
        if (!checkConnection()){
            progressBar.setVisibility(View.GONE);
            retry.setVisibility(View.VISIBLE);
            return;
        }
        JSONObject where_info = new JSONObject();
        try {
            where_info.put("type",1);

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
                    fill_list_items(response );
                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fill_list_items (JSONArray response) {
        mSwipeRefreshLayout.setRefreshing(false);
        try {
            for (int i=0; i<response.length(); i++) {
                JSONObject result = response.getJSONObject(i);
                int type = result.getInt("Type");
                String title = result.getString("Title");
                String content = result.getString("Content");
                items.add(new item_about(type, title, content));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        listView_adapter.notifyDataSetChanged();
        loading.setVisibility(View.GONE);
    }

}
