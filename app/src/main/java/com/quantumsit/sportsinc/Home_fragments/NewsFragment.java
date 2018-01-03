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

import com.quantumsit.sportsinc.Adapters.NewsAdapter;
import com.quantumsit.sportsinc.Entities.NewsEntity;
import com.quantumsit.sportsinc.NewsDetailsActivity;
import com.quantumsit.sportsinc.R;

import java.util.ArrayList;
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

        NewsList.add(new NewsEntity("this the news test number one please work from the first run mr compiler","http://thesportsinc.com/wp-content/uploads/2014/10/events.jpg"));
        NewsList.add(new NewsEntity("this the news test number one please work from the first run mr compiler","http://thesportsinc.com/wp-content/uploads/revslider/FullWidth/events-3720x1200.jpg"));
        NewsList.add(new NewsEntity("this the news test 2 number one please work from the first run mr compiler with an image","http://thesportsinc.com/wp-content/uploads/2014/10/gears.jpg"));

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
}
