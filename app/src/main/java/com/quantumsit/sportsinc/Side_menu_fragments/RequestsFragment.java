package com.quantumsit.sportsinc.Side_menu_fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Aaa_looks.ListView_Adapter_request;
import com.quantumsit.sportsinc.Aaa_looks.item_request;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.CustomView.myCustomListView;
import com.quantumsit.sportsinc.CustomView.myCustomListViewListener;
import com.quantumsit.sportsinc.R;
import com.quantumsit.sportsinc.Activities.Request_addActivity;
import com.quantumsit.sportsinc.util.ConnectionUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class RequestsFragment extends Fragment {

    GlobalVars globalVars;

    FloatingActionButton add_request_button;

    ListView listView;
    myCustomListView customListView;
    myCustomListViewListener listViewListener;
    int limitValue,currentStart;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<item_request> list_items;
    ListView_Adapter_request arrayAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_requests,container,false);

        globalVars = (GlobalVars) getActivity().getApplication();
        limitValue = getResources().getInteger(R.integer.selectLimit);
        currentStart = 0;

        add_request_button = root.findViewById(R.id.floatingActionButton);

        add_request_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Request_addActivity.class);
                startActivity(intent);
            }
        });

        mSwipeRefreshLayout = root.findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentStart = 0;
                getRequests(false);
            }
        });
        customListView = root.findViewById(R.id.customListView);
        customListView.setmEmptyView(R.drawable.ic_fading_requests,R.string.no_requests);

        customListView.setOnRetryClick(new myCustomListView.OnRetryClick() {
            @Override
            public void onRetry() {
                currentStart = 0;
                getRequests(false);
            }
        });
        listView = customListView.getListView();
        listViewListener = new myCustomListViewListener(listView , mSwipeRefreshLayout) {
            @Override
            public void loadMoreData() {
                if (list_items.size()>=limitValue)
                    listLoadMore();
            }
        };
        listView.setOnScrollListener(listViewListener);
        list_items = new ArrayList<>();


        arrayAdapter = new ListView_Adapter_request(getContext(), list_items);
        listView.setAdapter(arrayAdapter);

        if (savedInstanceState == null)
            getRequests(false);

        else
            fillBySavedState(savedInstanceState);

        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("ScrollPosition", listView.onSaveInstanceState());
        outState.putSerializable("listItems",  list_items);
    }


    private void fillBySavedState(Bundle savedInstanceState) {
        ArrayList<item_request> list1 = (ArrayList<item_request>) savedInstanceState.getSerializable("listItems");
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
                getRequests(true);
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
   private void getRequests(final boolean loadMore) {
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
            where_info.put("requests.from_id",globalVars.getId());


            HashMap<String,String> params = new HashMap<>();
            JSONObject limit_info = new JSONObject();
            limit_info.put("start", currentStart);
            limit_info.put("limit", limitValue);
            params.put("limit",limit_info.toString());
            params.put("table","requests");
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
            list_items.clear();
        if(response!= null){
            Date date;
            DateFormat outdateFormat = new SimpleDateFormat("dd MMMM, yyyy");
            DateFormat creationDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DateFormat requestDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            for (int i=0; i<response.length(); i++){
                try {
                    JSONObject result = response.getJSONObject(i);
                    String creation_date = result.getString("c_date");
                    date = creationDateFormat.parse(creation_date);
                    creation_date = outdateFormat.format(date);
                    String request_date = result.getString("date_request");
                    if (!request_date.equals("0000-00-00")) {
                        date = requestDateFormat.parse(request_date);
                        request_date = outdateFormat.format(date);
                    } else {
                        request_date = " ";
                    }

                    String title = result.getString("title");
                    String content = result.getString("content");
                    list_items.add(new item_request(creation_date, title + ": " +request_date+"\n     " +content, ""));

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }

        customListView.notifyChange(list_items.size());
        arrayAdapter.notifyDataSetChanged();
        listViewListener.setLoading(false);
    }

}
