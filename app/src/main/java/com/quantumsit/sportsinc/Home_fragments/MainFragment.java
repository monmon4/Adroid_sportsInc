package com.quantumsit.sportsinc.Home_fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Adapters.EventsRecyclerAdapter;
import com.quantumsit.sportsinc.Adapters.NewsRecyclerAdapter;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.Entities.EventEntity;
import com.quantumsit.sportsinc.Entities.NewsEntity;
import com.quantumsit.sportsinc.R;
import com.quantumsit.sportsinc.util.ConnectionUtilities;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainFragment extends Fragment {

    SwipeRefreshLayout mSwipeRefreshLayout;

    TextView AboutAcademy;
    ImageView Logo;
    RecyclerView newsRecyclerView , eventsRecyclerView;
    NewsRecyclerAdapter newsAdapter ;
    EventsRecyclerAdapter eventsAdapter;
    LinearLayout newsLayout , eventsLayout;
    TextView newsMore , eventMore;
    List<NewsEntity> NewsList;
    List<EventEntity> eventsList;

    ProgressBar progressBar;
    LinearLayout retry;
    RelativeLayout loading;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main,container,false);
        setHasOptionsMenu(false);

        mSwipeRefreshLayout = root.findViewById(R.id.swiperefresh);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getContent();
            }
        });

        NewsList = new ArrayList<>();
        eventsList = new ArrayList<>();
        newsLayout = root.findViewById(R.id.homeNewsSection);
        eventsLayout = root.findViewById(R.id.homeNewsSection);
        newsRecyclerView = root.findViewById(R.id.NewsList);
        eventsRecyclerView = root.findViewById(R.id.EventsList);
        progressBar = root.findViewById(R.id.progress_bar);
        loading = root.findViewById(R.id.LoadingData);

        retry = root.findViewById(R.id.layout_retry);
        LinearLayoutManager newsLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL , false);
        newsAdapter = new NewsRecyclerAdapter(getContext(),NewsList);
        newsRecyclerView.setLayoutManager(newsLayoutManager);
        newsRecyclerView.setAdapter(newsAdapter);

        LinearLayoutManager eventsLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL , false);
        eventsAdapter = new EventsRecyclerAdapter(getContext(),eventsList);
        eventsRecyclerView.setLayoutManager(eventsLayoutManager);
        eventsRecyclerView.setAdapter(eventsAdapter);

        newsMore = root.findViewById(R.id.newsMore);
        eventMore = root.findViewById(R.id.eventsMore);

        newsMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // startActivity(new Intent(getContext(), NewsActivity.class));
            }
        });

        eventMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getContext(), EventsActivity.class));
            }
        });
        AboutAcademy = root.findViewById(R.id.AboutAcademy);
        Logo = root.findViewById(R.id.AcademyLogo);

        getContent();

        return root;
    }

    private void getContent(){
        if (checkConnection())
            refreshContent();

        else {
            progressBar.setVisibility(View.GONE);
            retry.setVisibility(View.VISIBLE);
        }
    }
    private void refreshContent() {
        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.POST);
        httpCall.setUrl(Constants.selectData);
        HashMap<String,String> params = new HashMap<>();
        params.put("table","info_academy");

        httpCall.setParams(params);
        new HttpRequest(){
            @Override
            public void onResponse(JSONArray response) {
                super.onResponse(response);
                fillView(response);
            }
        }.execute(httpCall);
    }

    private void fillView(JSONArray response) {
        mSwipeRefreshLayout.setRefreshing(false);
        if (response != null) {
            try {
                JSONObject object = response.getJSONObject(0);

                String logo ="";// object.getString("logo");
                String brief = object.getString("about");

                if (!logo.equals("")) {
                    Picasso.with(getContext()).load(logo).into(Logo);
                }

                AboutAcademy.setText(brief);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        initializeNews();
    }

    private void initializeNews() {
        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.POST);
        httpCall.setUrl(Constants.selectData);
        HashMap<String,String> params = new HashMap<>();
        params.put("table","news");

        httpCall.setParams(params);
        new HttpRequest(){
            @Override
            public void onResponse(JSONArray response) {
                super.onResponse(response);
                fillNewsAdapter(response);
            }
        }.execute(httpCall);
    }

    private void fillNewsAdapter(JSONArray response) {
        NewsList.clear();
        newsAdapter.notifyDataSetChanged();
        if (response != null) {
            try {
                for (int i = 0; i < response.length()&& i<4; i++) {
                    NewsList.add(new NewsEntity( response.getJSONObject(i)));
                }
                newsAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        initilizeEvents();
    }

    private void initilizeEvents() {
        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.POST);
        httpCall.setUrl(Constants.selectData);
        HashMap<String,String> params = new HashMap<>();
        params.put("table","events");

        httpCall.setParams(params);
        new HttpRequest(){
            @Override
            public void onResponse(JSONArray response) {
                super.onResponse(response);
                fillEventAdapter(response);
            }
        }.execute(httpCall);
    }

    private void fillEventAdapter(JSONArray response) {
        eventsList.clear();
        if (response != null) {
            try {
                for (int i = 0; i < response.length() && i<4; i++) {
                    eventsList.add(new EventEntity(response.getJSONObject(i)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        eventsAdapter.notifyDataSetChanged();

        viewData();
    }

    private boolean checkConnection() {
        // first, check connectivity
        if (ConnectionUtilities
                .checkInternetConnection(getContext())) {
            return true;
        }
        return false;
    }
    private  void viewData(){
        loading.setVisibility(View.GONE);
    }
}
