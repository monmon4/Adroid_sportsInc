package com.quantumsit.sportsinc.Reports_fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Aaa_looks.MyCustomLayoutManager;
import com.quantumsit.sportsinc.Aaa_looks.RecyclerView_Adapter_reportpayment;
import com.quantumsit.sportsinc.Aaa_looks.item_reports_payment;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.CustomView.myCustomRecyclerView;
import com.quantumsit.sportsinc.CustomView.myCustomRecyclerViewListener;
import com.quantumsit.sportsinc.R;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentFragment extends Fragment {

    private MyCustomLayoutManager layoutManager;
    myCustomRecyclerView customRecyclerView;
    myCustomRecyclerViewListener listener;
    int limitValue, currentStart;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView recyclerView;
    RecyclerView_Adapter_reportpayment recyclerView_adapter_reportpayment;

    ArrayList<item_reports_payment> list_item;

    GlobalVars globalVars;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_payment,container,false);

        globalVars = (GlobalVars) getActivity().getApplication();
        limitValue = getResources().getInteger(R.integer.selectLimit);
        currentStart = 0;

        mSwipeRefreshLayout = root.findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentStart = 0;
                fill_payment_list(false);
            }
        });
        customRecyclerView = root.findViewById(R.id.customRecyclerView);
        customRecyclerView.setmEmptyView(R.drawable.ic_faded_reports,R.string.no_payment_reports);

        customRecyclerView.setOnRetryClick(new myCustomRecyclerView.OnRetryClick() {
            @Override
            public void onRetry() {
                currentStart = 0;
                fill_payment_list(false);
            }
        });
        recyclerView = customRecyclerView.getRecyclerView();
        layoutManager = new MyCustomLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.smoothScrollToPosition(recyclerView.getVerticalScrollbarPosition());
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
                if (list_item.size() >= limitValue)
                    listLoadMore();
            }};

        recyclerView.addOnScrollListener(listener);
        recyclerView.setHasFixedSize(false);

        list_item = new ArrayList<>();
        /*
        for(int i=0; i<10; i++){
            if (i == 0 || i == 1) {
                list_item.add(new item_reports_payment("Course1", "1/5/5017", 500, "Due: 20/5/2017", 0));
            } else {
                list_item.add(new item_reports_payment("Course1", "1/5/5017", 500, "Due: 20/5/2017", 1));

            }
        }
        */

        recyclerView_adapter_reportpayment = new RecyclerView_Adapter_reportpayment(list_item, getActivity());
       // recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerView_adapter_reportpayment);

        if (savedInstanceState == null)
            fill_payment_list(false);


        return root;
    }

    private void listLoadMore() {
        currentStart = list_item.size();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fill_payment_list(true);
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
    private void fill_payment_list(final boolean loadMore) {
        if (!checkConnection()){
            customRecyclerView.retry();
            return;
        }
        JSONObject where_info = new JSONObject();
        String on_condition;
        try {
            where_info.put("payment.trainee_id",globalVars.getId());
            on_condition = "payment.course_id = courses.id";
            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.joinData);
            HashMap<String,String> params = new HashMap<>();
            params.put("table1","payment");
            params.put("table2","courses");
            JSONObject limit_info = new JSONObject();
            limit_info.put("start", currentStart);
            limit_info.put("limit", limitValue);
            params.put("limit",limit_info.toString());
            params.put("where",where_info.toString());
            params.put("on",on_condition);

            httpCall.setParams(params);

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    fill_recycler_view(response , loadMore);

                }


            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private  void fill_recycler_view(JSONArray response ,boolean loadMore){
        if (!loadMore)
            list_item.clear();
        mSwipeRefreshLayout.setRefreshing(false);
        try {
            if (response != null) {
                JSONObject result;
                Date date_due, date_creation;
                DateFormat outdateFormat = new SimpleDateFormat("dd/MM/yyyy");
                DateFormat creationDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                DateFormat dueDateFormat = new SimpleDateFormat("yyyy-MM-dd");

                for (int i = 0; i < response.length(); i++) {
                    result = response.getJSONObject(i);
                    String course_name = result.getString("name");
                    String due_date = result.getString("due_date");
                    date_due = dueDateFormat.parse(due_date);
                    due_date = outdateFormat.format(date_due);
                    String creation_date = result.getString("c_date");
                    date_creation = creationDateFormat.parse(creation_date);
                    creation_date = outdateFormat.format(date_creation);
                    int amount = result.getInt("due_amount");
                    int status = result.getInt("status");

                    list_item.add(new item_reports_payment(course_name, creation_date, amount, due_date, status));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        customRecyclerView.notifyChange(list_item.size());
        customRecyclerView.finishLoading();
        recyclerView_adapter_reportpayment.notifyDataSetChanged();
        listener.setLoading(false);
    }


}
