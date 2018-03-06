package com.quantumsit.sportsinc.AboutUs_fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class ConditionsFragment extends Fragment {

    ListView listView;
    ListView_Adapter_about_us listView_adapter;

    ProgressDialog progressDialog;

    ArrayList<item_about> items;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_conditions,container,false);


        listView = root.findViewById(R.id.listView_conditions);
        items = new ArrayList<>();

        progressDialog= new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait");

        progressDialog.show();
        get_list_items();

        listView_adapter = new ListView_Adapter_about_us(getContext(), items);
        listView.setAdapter(listView_adapter);


        return root;
    }

    private void get_list_items(){

        JSONObject where_info = new JSONObject();
        try {
            where_info.put("type",2);

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.selectData);
            HashMap<String,String> params = new HashMap<>();
            params.put("table","rules");
            params.put("where",where_info.toString());

            httpCall.setParams(params);

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    fill_list_items(response );
                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fill_list_items (JSONArray response) {

        try {
            for (int i=0; i<response.length(); i++) {
                JSONObject result = response.getJSONObject(i);
                int type = result.getInt("Type");
                String title = result.getString("Title");
                String content = result.getString("Content");
                items.add(new item_about(type, title, content));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        listView_adapter.notifyDataSetChanged();
        progressDialog.dismiss();
    }

}
