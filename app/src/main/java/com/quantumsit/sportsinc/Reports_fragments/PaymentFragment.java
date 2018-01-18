package com.quantumsit.sportsinc.Reports_fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Aaa_looks.MyCustomLayoutManager;
import com.quantumsit.sportsinc.Aaa_looks.RecyclerView_Adapter_reportcourses;
import com.quantumsit.sportsinc.Aaa_looks.RecyclerView_Adapter_reportpayment;
import com.quantumsit.sportsinc.Aaa_looks.item_reports_payment;
import com.quantumsit.sportsinc.Aaa_looks.item_single_reports_courses;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentFragment extends Fragment {

    private MyCustomLayoutManager layoutManager;
    RecyclerView recyclerView;
    RecyclerView_Adapter_reportpayment recyclerView_adapter_reportpayment;

    ArrayList<item_reports_payment> list_item;

    GlobalVars globalVars;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_payment,container,false);

        globalVars = (GlobalVars) getActivity().getApplication();

        recyclerView = root.findViewById(R.id.recyclerView_reportspayment);
        recyclerView.setHasFixedSize(false);

        list_item = new ArrayList<>();
        //fill_payment_list();

        layoutManager = new MyCustomLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.smoothScrollToPosition(recyclerView.getVerticalScrollbarPosition());

        //
        for(int i=0; i<10; i++){
            if (i == 0 || i == 1) {
                list_item.add(new item_reports_payment("Course1", "1/5/5017", 500, "Due: 20/5/2017", 0));
            } else {
                list_item.add(new item_reports_payment("Course1", "1/5/5017", 500, "Due: 20/5/2017", 1));

            }
        }
        //

        recyclerView_adapter_reportpayment = new RecyclerView_Adapter_reportpayment(list_item, getActivity());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerView_adapter_reportpayment);



        return root;
    }

    @SuppressLint("StaticFieldLeak")
    private void fill_payment_list() {

        JSONObject where_info = new JSONObject();
        String on_condition;
        try {
            where_info.put("payment.trainee_id",globalVars.getId());
            on_condition = "payment.course_id = courses.id";
            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.joinData);
            HashMap<String,String> params = new HashMap<>();
            params.put("table1","payment");
            params.put("table2","courses");
            params.put("where",where_info.toString());
            params.put("on",on_condition);

            httpCall.setParams(params);

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    try {

                        if (response != null) {
                            JSONObject result;
                            Date date_due, date_creation;
                            DateFormat outdateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            DateFormat creationDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            DateFormat dueDateFormat = new SimpleDateFormat("yyyy-MM-dd");

                            for (int i=0; i<response.length(); i++){
                                result = response.getJSONObject(i);
                                String course_name = result.getString("name");
                                String due_date = result.getString("due_date");
                                date_due = dueDateFormat.parse(due_date);
                                due_date = outdateFormat.format(date_due);
                                String creation_date = result.getString("c_date");
                                date_creation = creationDateFormat.parse(creation_date);
                                creation_date = outdateFormat.format(date_creation);
                                int amount = result.getInt("due_amount");
                                int status = result.getInt("status");

                                list_item.add(new item_reports_payment(course_name, creation_date, amount, due_date, status));
                            }



                        } else {
                            Toast.makeText(getContext(), "An error occurred ", Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }


            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



}
