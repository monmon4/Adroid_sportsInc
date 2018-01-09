package com.quantumsit.sportsinc.Home_fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Adapters.NewsAdapter;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.Entities.NewsEntity;
import com.quantumsit.sportsinc.NewsDetailsActivity;
import com.quantumsit.sportsinc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class NewsFragment extends Fragment {
    NewsAdapter adapter;
    List<NewsEntity> NewsList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_news,container,false);
        ListView listView = root.findViewById(R.id.news_listview);
        listView.setEmptyView(root.findViewById(R.id.empty_text));

        NewsList = new ArrayList<>();

        initilizeNews();

        adapter = new NewsAdapter(getContext(),R.layout.list_item_news,NewsList);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), NewsDetailsActivity.class);
                intent.putExtra("MyNews",NewsList.get(i));
                startActivity(intent);
            }
        });

        return root;
    }

    private void initilizeNews() {
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
                fillAdapter(response);
            }
        }.execute(httpCall);
    }

    private void fillAdapter(JSONArray response) {
        try {
            for (int i=0 ; i<response.length() ;i++){
                NewsList.add(new NewsEntity((JSONObject) response.get(i)));
                adapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}