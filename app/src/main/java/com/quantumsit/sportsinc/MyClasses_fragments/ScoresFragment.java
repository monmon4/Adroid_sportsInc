package com.quantumsit.sportsinc.MyClasses_fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quantumsit.sportsinc.Aaa_looks.MyCustomLayoutManager;
import com.quantumsit.sportsinc.Aaa_looks.RecyclerView_Adapter_scores;
import com.quantumsit.sportsinc.Aaa_looks.item_single_scores;
import com.quantumsit.sportsinc.R;

import java.util.ArrayList;
import java.util.List;


public class ScoresFragment extends Fragment {

    private MyCustomLayoutManager layoutManager;
    private RecyclerView recycler_view;
    private RecyclerView_Adapter_scores recycler_view_adapter;
    public List<item_single_scores> list_item;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_scores,container,false);


        recycler_view = (RecyclerView) root.findViewById(R.id.recyclerView_scores);
        recycler_view.setHasFixedSize(false);

        list_item = new ArrayList<>();

        layoutManager = new MyCustomLayoutManager(getActivity());
        recycler_view.setLayoutManager(layoutManager);
        recycler_view.smoothScrollToPosition(recycler_view.getVerticalScrollbarPosition());



        //
        for(int i=0; i<20; i++){
            list_item.add(new item_single_scores("Course 1", "15/12/2015", "Class5", "Score = 8"));
        }

        recycler_view_adapter = new RecyclerView_Adapter_scores(list_item, getActivity());
        recycler_view.setAdapter(recycler_view_adapter);
        //


        return root;
    }
}
