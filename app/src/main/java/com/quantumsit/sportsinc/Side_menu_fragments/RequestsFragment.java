package com.quantumsit.sportsinc.Side_menu_fragments;

import android.annotation.SuppressLint;
import android.app.LauncherActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Aaa_looks.ListView_Adapter_request;
import com.quantumsit.sportsinc.Aaa_looks.item_request;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.R;
import com.quantumsit.sportsinc.Request_addActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class RequestsFragment extends Fragment {

    GlobalVars globalVars;

    FloatingActionButton add_request_button;

    ListView listView;
    ArrayList<item_request> list_items;
    ListView_Adapter_request arrayAdapter;

    ProgressDialog progressDialog;
    ImageView imageView_requests;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_requests,container,false);

        globalVars = (GlobalVars) getActivity().getApplication();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait ....");

        add_request_button = root.findViewById(R.id.floatingActionButton);
        add_request_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Request_addActivity.class);
                startActivity(intent);
            }
        });

        listView = root.findViewById(R.id.requests_listview);
        imageView_requests = root.findViewById(R.id.imageView_requests);
        list_items = new ArrayList<>();

        list_items.add(new item_request("", "", ""));

        arrayAdapter = new ListView_Adapter_request(getContext(), list_items);
        listView.setAdapter(arrayAdapter);

        progressDialog.show();
        getRequests();

        return root;
    }

    @SuppressLint("StaticFieldLeak")
   private void getRequests() {

        list_items.clear();
        try {
            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.selectData);
            JSONObject where_info = new JSONObject();
            where_info.put("requests.from_id",globalVars.getId());


            HashMap<String,String> params = new HashMap<>();
            params.put("table","requests");
            params.put("where",where_info.toString());

            httpCall.setParams(params);
            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);

                    if(response!= null){
                        Date date;
                        DateFormat outdateFormat = new SimpleDateFormat("dd MMMM, yyyy");
                        DateFormat creationDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        DateFormat requestDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        for (int i=0; i<response.length(); i++){
                            try {
                                JSONObject result = response.getJSONObject(i);
                                String creation_date = result.getString("c_date");
                                date = creationDateFormat.parse(creation_date);
                                creation_date = outdateFormat.format(date);
                                String request_date = result.getString("date_request");
                                date = requestDateFormat.parse(request_date);
                                request_date = outdateFormat.format(date);

                                String title = result.getString("title");
                                String content = result.getString("content");
                                list_items.add(new item_request(creation_date, title + ": " +request_date+"\n     " +content, ""));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }

                        arrayAdapter.notifyDataSetChanged();
                        imageView_requests.setVisibility(View.INVISIBLE);
                        listView.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();
                    } else {
                        imageView_requests.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.INVISIBLE);
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                    }

                }
            }.execute(httpCall);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
