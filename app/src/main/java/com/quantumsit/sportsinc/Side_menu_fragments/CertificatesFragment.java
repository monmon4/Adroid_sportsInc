package com.quantumsit.sportsinc.Side_menu_fragments;

import android.annotation.SuppressLint;
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
import com.quantumsit.sportsinc.Aaa_looks.RecyclerView_Adapter_certificate;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.COACHES.ReportsFragments.item_report_attendance;
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


public class CertificatesFragment extends Fragment {

    myCustomRecyclerView customRecyclerView;
    myCustomRecyclerViewListener listener;
    int limitValue, currentStart;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView certificates_recyclerView;
    MyCustomLayoutManager layoutManager;
    RecyclerView_Adapter_certificate certificates_recyclerView_adapter;

    List<String> certificates_list;
    GlobalVars globalVars;

    private int currentVisiblePosition = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_certificates,container,false);

        globalVars = (GlobalVars) getActivity().getApplication();
        limitValue = getResources().getInteger(R.integer.selectLimit);
        currentStart = 0;

        mSwipeRefreshLayout = root.findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentStart = 0;
                fill_certificates(false);
            }
        });
        customRecyclerView = root.findViewById(R.id.customRecyclerView);
        customRecyclerView.setmEmptyView(R.drawable.ic_faded_certificates,R.string.no_certificates);

        customRecyclerView.setOnRetryClick(new myCustomRecyclerView.OnRetryClick() {
            @Override
            public void onRetry() {
                currentStart = 0;
                fill_certificates(false);
            }
        });
        certificates_recyclerView = customRecyclerView.getRecyclerView();

        layoutManager = new MyCustomLayoutManager(getActivity());
        certificates_recyclerView.setLayoutManager(layoutManager);
        certificates_recyclerView.smoothScrollToPosition(certificates_recyclerView.getVerticalScrollbarPosition());

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
                if (certificates_list.size()>=limitValue)
                    listLoadMore();
            }};

        certificates_recyclerView.addOnScrollListener(listener);
        certificates_list = new ArrayList<>();
        certificates_recyclerView_adapter = new RecyclerView_Adapter_certificate(certificates_list, getContext());
        certificates_recyclerView.setAdapter(certificates_recyclerView_adapter);

        if (savedInstanceState == null)
            fill_certificates(false);

        else
            fillBySavedState(savedInstanceState);

        return root;
    }


    private void fillBySavedState(Bundle savedInstanceState) {
        ArrayList<String> list1 =savedInstanceState.getStringArrayList("list_items");
        certificates_list.addAll(list1);
        currentVisiblePosition = savedInstanceState.getInt("Position");
        customRecyclerView.notifyChange(certificates_list.size());
        certificates_recyclerView_adapter.notifyDataSetChanged();
        certificates_recyclerView.smoothScrollToPosition(currentVisiblePosition);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("list_items", (ArrayList<String>) certificates_list);
        outState.putInt("Position",currentVisiblePosition);
    }

    @Override
    public void onPause() {
        super.onPause();
        currentVisiblePosition = 0;
        currentVisiblePosition = layoutManager.findFirstCompletelyVisibleItemPosition();
    }

    private void listLoadMore() {
        currentStart = certificates_list.size();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fill_certificates(true);
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

    @SuppressLint("StaticFieldLeak")
    private void fill_certificates(final boolean loadMore) {
        if (!isAdded()) {
            return;
        }
        if (!checkConnection()){
            customRecyclerView.retry();
            return;
        }
        JSONObject where_info = new JSONObject();
        try {
            where_info.put("user_id",globalVars.getId());

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.selectData);
            HashMap<String,String> params = new HashMap<>();
            params.put("table","certification");
            JSONObject limit_info = new JSONObject();
            limit_info.put("start", currentStart);
            limit_info.put("limit", limitValue);
            params.put("limit",limit_info.toString());
            params.put("where",where_info.toString());

            httpCall.setParams(params);

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    fill_Adapter(response , loadMore);

                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fill_Adapter(JSONArray response , boolean loadMore){
        mSwipeRefreshLayout.setRefreshing(false);
        if (!loadMore)
            certificates_list.clear();
        if (response != null) {
            for (int i=0; i< response.length(); i++){
                try {
                    JSONObject obj = response.getJSONObject(i);
                    String img_url = obj.getString("img");
                    certificates_list.add(img_url);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        customRecyclerView.notifyChange(certificates_list.size());
        customRecyclerView.finishLoading();
        certificates_recyclerView_adapter.notifyDataSetChanged();
        listener.setLoading(false);
    }


}


