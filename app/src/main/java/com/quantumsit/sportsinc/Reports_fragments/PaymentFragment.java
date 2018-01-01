package com.quantumsit.sportsinc.Reports_fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quantumsit.sportsinc.Aaa_looks.MyCustomLayoutManager;
import com.quantumsit.sportsinc.Aaa_looks.RecyclerView_Adapter_reportcourses;
import com.quantumsit.sportsinc.Aaa_looks.RecyclerView_Adapter_reportpayment;
import com.quantumsit.sportsinc.Aaa_looks.item_reports_payment;
import com.quantumsit.sportsinc.Aaa_looks.item_single_reports_courses;
import com.quantumsit.sportsinc.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentFragment extends Fragment {

    private MyCustomLayoutManager layoutManager;
    RecyclerView recyclerView;
    RecyclerView_Adapter_reportpayment recyclerView_adapter_reportpayment;

    ArrayList<item_reports_payment> list_item;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_payment,container,false);

        recyclerView = root.findViewById(R.id.recyclerView_reportspayment);
        recyclerView.setHasFixedSize(false);

        list_item = new ArrayList<>();

        layoutManager = new MyCustomLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.smoothScrollToPosition(recyclerView.getVerticalScrollbarPosition());

        //
        for(int i=0; i<10; i++){
            list_item.add(new item_reports_payment("Course1", "1/5/5017", "$500", "Due: 20/5/2017"));
        }

        recyclerView_adapter_reportpayment = new RecyclerView_Adapter_reportpayment(list_item, getActivity());
        recyclerView.setAdapter(recyclerView_adapter_reportpayment);
        //



        return root;
    }

}
