package com.quantumsit.sportsinc.Side_menu_fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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
import com.quantumsit.sportsinc.R;
import com.quantumsit.sportsinc.Activities.Request_addActivity;
import com.quantumsit.sportsinc.util.ConnectionUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<item_request> list_items;
    ListView_Adapter_request arrayAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_requests,container,false);

        globalVars = (GlobalVars) getActivity().getApplication();

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
                getRequests();
            }
        });
        customListView = root.findViewById(R.id.customListView);
        customListView.setmEmptyView(R.drawable.ic_fading_requests,R.string.no_requests);

        customListView.setOnRetryClick(new myCustomListView.OnRetryClick() {
            @Override
            public void onRetry() {
                getRequests();
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
        list_items = new ArrayList<>();

        list_items.add(new item_request("", "", ""));

        arrayAdapter = new ListView_Adapter_request(getContext(), list_items);
        listView.setAdapter(arrayAdapter);

        getRequests();

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
   private void getRequests() {
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
            params.put("table","requests");
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
        list_items.clear();
        mSwipeRefreshLayout.setRefreshing(false);
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
                    date = requestDateFormat.parse(request_date);
                    request_date = outdateFormat.format(date);

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

        arrayAdapter.notifyDataSetChanged();
        customListView.notifyChange(list_items.size());
    }

}
