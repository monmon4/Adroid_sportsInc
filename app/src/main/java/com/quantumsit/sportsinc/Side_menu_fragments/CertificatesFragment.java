package com.quantumsit.sportsinc.Side_menu_fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Aaa_looks.MyCustomLayoutManager;
import com.quantumsit.sportsinc.Aaa_looks.RecyclerView_Adapter_certificate;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.HomeActivity;
import com.quantumsit.sportsinc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CertificatesFragment extends Fragment {

    MyCustomLayoutManager layoutManager;
    RecyclerView certificates_recyclerView;
    RecyclerView_Adapter_certificate certificates_recyclerView_adapter;

    List<Integer> list_items;

    ProgressDialog progressDialog;
    List<String> certificates_list;
    GlobalVars globalVars;

    TextView certficate_textView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_certificates,container,false);

        globalVars = (GlobalVars) getActivity().getApplication();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait ....");
        certficate_textView = root.findViewById(R.id.textView_certficates);

        layoutManager = new MyCustomLayoutManager(getActivity());
        certificates_recyclerView = root.findViewById(R.id.recyclerView_certificates);
        certificates_recyclerView.setLayoutManager(layoutManager);
        certificates_recyclerView.smoothScrollToPosition(certificates_recyclerView.getVerticalScrollbarPosition());

        list_items = new ArrayList<>();


        certificates_list = new ArrayList<>();
        fill_certificates();
        certficate_textView.setVisibility(View.VISIBLE);
        certificates_recyclerView.setVisibility(View.INVISIBLE);

        certificates_recyclerView_adapter = new RecyclerView_Adapter_certificate(list_items, getContext());
        certificates_recyclerView.setAdapter(certificates_recyclerView_adapter);

        return root;
    }

    @SuppressLint("StaticFieldLeak")
    private void fill_certificates() {
        progressDialog.show();
        JSONObject where_info = new JSONObject();
        try {
            where_info.put("user_id",globalVars.getId());

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.selectData);
            HashMap<String,String> params = new HashMap<>();
            params.put("table","certification");
            params.put("where",where_info.toString());

            httpCall.setParams(params);

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);

                    if (response != null) {
                       for (int i=0; i< response.length(); i++){
                           try {
                               JSONObject obj = response.getJSONObject(i);
                               String img_url = obj.getString("img");
                               certificates_list.add(img_url);
                           } catch (JSONException e) {
                               e.printStackTrace();
                           }
                       }
                       set_recycler_view();

                    } else {
                        certficate_textView.setVisibility(View.VISIBLE);
                        certificates_recyclerView.setVisibility(View.INVISIBLE);
                        progressDialog.dismiss();
                    }

                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void set_recycler_view() {
        certificates_recyclerView_adapter.notifyDataSetChanged();
        progressDialog.dismiss();
        certficate_textView.setVisibility(View.INVISIBLE);
        certificates_recyclerView.setVisibility(View.VISIBLE);

    }

}


