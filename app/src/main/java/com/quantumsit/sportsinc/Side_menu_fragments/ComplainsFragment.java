package com.quantumsit.sportsinc.Side_menu_fragments;

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
import android.widget.AdapterView;
import android.widget.ListView;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Activities.ComplainDetailsActivity;
import com.quantumsit.sportsinc.Activities.ComplainsAddActivity;
import com.quantumsit.sportsinc.Activities.Request_addActivity;
import com.quantumsit.sportsinc.Adapters.ComplainsAdapter;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.CustomView.myCustomListView;
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
                initilizeComplains();
            }
        });
        customListView = root.findViewById(R.id.customListView);
        customListView.setmEmptyView(R.drawable.ic_assignment,R.string.no_complains);

        customListView.setOnRetryClick(new myCustomListView.OnRetryClick() {
            @Override
            public void onRetry() {
                initilizeComplains();
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
        initilizeComplains();

        adapter = new ComplainsAdapter(getContext(),R.layout.list_item_complains, ReviewedcomplainList);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), ComplainDetailsActivity.class);
                intent.putExtra("MyComplain", ReviewedcomplainList.get(i));
                startActivity(intent);
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

    private void initilizeComplains() {
        if (!checkConnection()){
            customListView.retry();
            return;
        }
        //try {
            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.join);

            /*JSONObject where_info = new JSONObject();
            where_info.put("complains.to_id",globalVars.getId());*/
            String OnCondition = "complains.user_id = users.id";

            HashMap<String, String> params = new HashMap<>();
            params.put("table1", "complains");
            params.put("table2", "users");

           // params.put("where",where_info.toString());
            params.put("on", OnCondition);

            httpCall.setParams(params);
            new HttpRequest() {
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    fillAdapter(response);
                }
            }.execute(httpCall);
        /*} catch (JSONException e) {
            e.printStackTrace();
        }*/
    }

    private void fillAdapter(JSONArray response) {
        mSwipeRefreshLayout.setRefreshing(false);
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
        adapter.notifyDataSetChanged();
        customListView.notifyChange(ReviewedcomplainList.size());
    }
}

