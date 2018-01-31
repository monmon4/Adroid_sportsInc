package com.quantumsit.sportsinc.Reports_fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Aaa_looks.MyCustomLayoutManager;
import com.quantumsit.sportsinc.Aaa_looks.RecyclerView_Adapter_reportcourses;
import com.quantumsit.sportsinc.Aaa_looks.RecyclerView_Adapter_scores;
import com.quantumsit.sportsinc.Aaa_looks.item_single_reports_courses;
import com.quantumsit.sportsinc.Aaa_looks.item_single_scores;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CorsesFragment extends Fragment {

    private MyCustomLayoutManager layoutManager;
    private RecyclerView recycler_view;
    private RecyclerView_Adapter_reportcourses recycler_view_adapter;
    public List<item_single_reports_courses> list_item;

    TextView corses_tevtView;
    ProgressDialog progressDialog;
    GlobalVars globalVars;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_corses,container,false);

        globalVars = (GlobalVars) getActivity().getApplication();

        recycler_view = root.findViewById(R.id.recyclerView_reportscourses);
        recycler_view.setHasFixedSize(false);

        corses_tevtView = root.findViewById(R.id.textView_corses);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait......");

        list_item = new ArrayList<>();

        layoutManager = new MyCustomLayoutManager(getActivity());
        recycler_view.setLayoutManager(layoutManager);
        recycler_view.smoothScrollToPosition(recycler_view.getVerticalScrollbarPosition());

        fill_list_items();

        recycler_view_adapter = new RecyclerView_Adapter_reportcourses(list_item, getActivity());
        recycler_view.setAdapter(recycler_view_adapter);
        recycler_view.setVisibility(View.INVISIBLE);
        corses_tevtView.setVisibility(View.VISIBLE);


        return root;
    }

    @SuppressLint("StaticFieldLeak")
    private void fill_list_items() {
        progressDialog.show();

        JSONObject where_info = new JSONObject();
        try {
            where_info.put("trainee_id",globalVars.getId());

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.traineeCoursesData);
            HashMap<String,String> params = new HashMap<>();
            params.put("where",where_info.toString());

            httpCall.setParams(params);
            progressDialog.show();
            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    try {

                        if (response != null) {
                            for (int i=0; i<response.length(); i++){
                                JSONObject result = response.getJSONObject(i);
                                int course_id = result.getInt("course_id");
                                int group_id = result.getInt("group_id");
                                String course_name = result.getString("course_name");
                                String group_name = result.getString("group_name");
                                int classes_num = result.getInt("Num_classes");
                                int attend_num = result.getInt("attend_num");
                                double attendance = ((double) attend_num/(double) classes_num) *100.0;                                int total_score = result.getInt("total_score");
                                list_item.add(new item_single_reports_courses(course_name, group_name, course_id, group_id, attendance, total_score));
                            }
                            fill_recycler_view();

                        } else {
                            progressDialog.dismiss();
                            corses_tevtView.setVisibility(View.VISIBLE);
                            recycler_view.setVisibility(View.INVISIBLE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fill_recycler_view() {
        recycler_view_adapter.notifyDataSetChanged();
        recycler_view.setVisibility(View.VISIBLE);
        corses_tevtView.setVisibility(View.INVISIBLE);
        progressDialog.dismiss();
    }
}
