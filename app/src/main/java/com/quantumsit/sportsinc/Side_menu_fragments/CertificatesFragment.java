package com.quantumsit.sportsinc.Side_menu_fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.quantumsit.sportsinc.Aaa_looks.MyCustomLayoutManager;
import com.quantumsit.sportsinc.Aaa_looks.RecyclerView_Adapter_certificate;
import com.quantumsit.sportsinc.R;

import java.util.ArrayList;
import java.util.List;


public class CertificatesFragment extends Fragment {

    MyCustomLayoutManager layoutManager;
    RecyclerView certificates_recyclerView;
    RecyclerView_Adapter_certificate certificates_recyclerView_adapter;

    List<Integer> list_items;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_certificates,container,false);

        layoutManager = new MyCustomLayoutManager(getActivity());
        certificates_recyclerView = root.findViewById(R.id.recyclerView_certificates);
        certificates_recyclerView.setLayoutManager(layoutManager);
        certificates_recyclerView.smoothScrollToPosition(certificates_recyclerView.getVerticalScrollbarPosition());

        list_items = new ArrayList<>();

        list_items.add(R.drawable.img1);
        list_items.add(R.drawable.img2);
        list_items.add(R.drawable.img3);
        list_items.add(R.drawable.img4);
        list_items.add(R.drawable.img5);
        list_items.add(R.drawable.img6);
        list_items.add(R.drawable.img7);
        list_items.add(R.drawable.img8);

        certificates_recyclerView_adapter = new RecyclerView_Adapter_certificate(list_items, getContext());
        certificates_recyclerView.setAdapter(certificates_recyclerView_adapter);

        return root;
    }

}


