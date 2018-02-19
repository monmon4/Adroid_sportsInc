package com.quantumsit.sportsinc.Side_menu_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Activities.ComplainDetailsActivity;
import com.quantumsit.sportsinc.Activities.ComplainsAddActivity;
import com.quantumsit.sportsinc.Adapters.ComplainsAdapter;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.CustomView.myCustomListView;
import com.quantumsit.sportsinc.CustomView.myCustomListViewListener;
import com.quantumsit.sportsinc.Entities.ComplainEntity;
import com.quantumsit.sportsinc.R;
import com.quantumsit.sportsinc.util.ConnectionUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Bassam on 2/12/2018.
 */

public class ComplainsFragment extends Fragment {
    private static final String TAG = ComplainsFragment.class.getSimpleName();

    GlobalVars globalVars;

    FloatingActionButton add_complain_button;
    myCustomListView customListView;
    myCustomListViewListener listViewListener;
    int limitValue,currentStart;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ListView listView;

    private ComplainsAdapter adapter;
    private List<ComplainEntity> ReviewedcomplainList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_complains,container,false);
        setHasOptionsMenu(true);

        globalVars = (GlobalVars) getActivity().getApplication();
        limitValue = getResources().getInteger(R.integer.selectLimit);
        currentStart = 0;
        ReviewedcomplainList = new ArrayList<>();

        add_complain_button = root.findViewById(R.id.floatingActionButton);
        add_complain_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ComplainsAddActivity.class);
                startActivity(intent);
            }
        });

        mSwipeRefreshLayout = root.findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentStart = 0;
                initilizeComplains(false);
            }
        });
        customListView = root.findViewById(R.id.customListView);
        customListView.setmEmptyView(R.drawable.ic_assignment,R.string.no_complains);

        customListView.setOnRetryClick(new myCustomListView.OnRetryClick() {
            @Override
            public void onRetry() {
                currentStart = 0;
                initilizeComplains(false);
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
        initilizeComplains(false);

        adapter = new ComplainsAdapter(getContext(),R.layout.list_item_complains, ReviewedcomplainList);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i>= ReviewedcomplainList.size())
                    return;
                Intent intent = new Intent(getContext(), ComplainDetailsActivity.class);
                intent.putExtra("MyComplain", ReviewedcomplainList.get(i));
                startActivity(intent);
            }
        });

        return root;
    }

    private void listLoadMore() {
        customListView.loadMore();
        currentStart = ReviewedcomplainList.size();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initilizeComplains(true);
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

    private void initilizeComplains(final boolean loadMore) {
        if (!checkConnection()){
            customListView.retry();
            return;
        }
        try {
            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.join);

            HashMap<String, String> params = new HashMap<>();

            JSONObject where_info = new JSONObject();
            where_info.put("complains.to_id",globalVars.getId());

            JSONObject limit_info = new JSONObject();
            limit_info.put("start", currentStart);
            limit_info.put("limit", limitValue);
            params.put("limit",limit_info.toString());
            String OnCondition = "complains.user_id = users.id";

            params.put("table1", "complains");
            params.put("table2", "users");

            params.put("where",where_info.toString());
            params.put("on", OnCondition);

            httpCall.setParams(params);
            new HttpRequest() {
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
        mSwipeRefreshLayout.setRefreshing(false);
        if(!loadMore)
            ReviewedcomplainList.clear();
        if (response != null) {
            try {
                for (int i = 0; i < response.length(); i++) {
                    ReviewedcomplainList.add(new ComplainEntity(response.getJSONObject(i)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        customListView.notifyChange(ReviewedcomplainList.size());
        adapter.notifyDataSetChanged();
        listViewListener.setLoading(false);

    }
}

