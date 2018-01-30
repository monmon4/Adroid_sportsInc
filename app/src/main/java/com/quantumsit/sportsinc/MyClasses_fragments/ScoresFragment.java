package com.quantumsit.sportsinc.MyClasses_fragments;

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
import com.quantumsit.sportsinc.Aaa_looks.RecyclerView_Adapter_scores;
import com.quantumsit.sportsinc.Aaa_looks.item1_reports_courses;
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


public class ScoresFragment extends Fragment {

    private MyCustomLayoutManager layoutManager;
    private RecyclerView recycler_view;
    private RecyclerView_Adapter_scores recycler_view_adapter;
    public List<item_single_scores> list_item;

    GlobalVars globalVars;
    int user_id;

    ProgressDialog progressDialog;
    TextView scores_textView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_scores,container,false);

        globalVars = (GlobalVars) getActivity().getApplication();
        user_id = globalVars.getId();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait.....");
        scores_textView = root.findViewById(R.id.textView_scores);

        recycler_view = root.findViewById(R.id.recyclerView_scores);

        recycler_view.setHasFixedSize(false);

        list_item = new ArrayList<>();

        layoutManager = new MyCustomLayoutManager(getActivity());
        recycler_view.setLayoutManager(layoutManager);
        recycler_view.smoothScrollToPosition(recycler_view.getVerticalScrollbarPosition());

        scores_textView.setVisibility(View.VISIBLE);
        recycler_view.setVisibility(View.INVISIBLE);
        fill_list();


        recycler_view_adapter = new RecyclerView_Adapter_scores(list_item, getActivity());
        recycler_view.setAdapter(recycler_view_adapter);

        return root;
    }

    @SuppressLint("StaticFieldLeak")
    private void fill_list() {
        progressDialog.show();

        JSONObject where_info = new JSONObject();
        try {
            where_info.put("trainee_id",globalVars.getId());

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.traineeClassScores);
            HashMap<String,String> params = new HashMap<>();
            params.put("where",where_info.toString());

            httpCall.setParams(params);
            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    try {

                        if (response != null) {

                            for (int i=0; i<response.length(); i++){
                                JSONObject result = response.getJSONObject(i);
                                String course_name = result.getString("Course_name");
                                String group_name = result.getString("Groups_Name");
                                String class_date = result.getString("class_date");
                                int class_number = result.getInt("class_num");
                                int score = result.getInt("score");
                                String coach_name = result.getString("coach_name");
                                String coach_notes = result.getString("coach_notes");
                                list_item.add(new item_single_scores(course_name, group_name, class_date, coach_name, coach_notes, score, class_number));
                            }
                            fill_recycler_view();

                        } else {
                            progressDialog.dismiss();
                            scores_textView.setVisibility(View.VISIBLE);
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

    private void fill_recycler_view(){
        recycler_view_adapter.notifyDataSetChanged();
        scores_textView.setVisibility(View.INVISIBLE);
        recycler_view.setVisibility(View.VISIBLE);
        progressDialog.dismiss();

    }
}
