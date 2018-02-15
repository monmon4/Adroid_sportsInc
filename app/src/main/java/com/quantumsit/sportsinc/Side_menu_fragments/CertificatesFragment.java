package com.quantumsit.sportsinc.Side_menu_fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
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
import com.quantumsit.sportsinc.CustomView.myCustomRecyclerView;
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
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView certificates_recyclerView;
    MyCustomLayoutManager layoutManager;
    RecyclerView_Adapter_certificate certificates_recyclerView_adapter;

    List<Integer> list_items;

    List<String> certificates_list;
    GlobalVars globalVars;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_certificates,container,false);

        globalVars = (GlobalVars) getActivity().getApplication();

        mSwipeRefreshLayout = root.findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fill_certificates();
            }
        });
        customRecyclerView = root.findViewById(R.id.customRecyclerView);
        customRecyclerView.setmEmptyView(R.drawable.ic_faded_certificates,R.string.no_certificates);

        customRecyclerView.setOnRetryClick(new myCustomRecyclerView.OnRetryClick() {
            @Override
            public void onRetry() {
                fill_certificates();
            }
        });
        certificates_recyclerView = customRecyclerView.getRecyclerView();
        certificates_recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });

        layoutManager = new MyCustomLayoutManager(getActivity());
        certificates_recyclerView.setLayoutManager(layoutManager);
        certificates_recyclerView.smoothScrollToPosition(certificates_recyclerView.getVerticalScrollbarPosition());

        list_items = new ArrayList<>();


        certificates_list = new ArrayList<>();
        fill_certificates();

        certificates_recyclerView_adapter = new RecyclerView_Adapter_certificate(list_items, getContext());
        certificates_recyclerView.setAdapter(certificates_recyclerView_adapter);

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

    @SuppressLint("StaticFieldLeak")
    private void fill_certificates() {
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
            params.put("where",where_info.toString());

            httpCall.setParams(params);

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    fill_Adapter(response);

                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fill_Adapter(JSONArray response){
        mSwipeRefreshLayout.setRefreshing(false);
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
        certificates_recyclerView_adapter.notifyDataSetChanged();
        customRecyclerView.notifyChange(certificates_list.size());
    }


}


