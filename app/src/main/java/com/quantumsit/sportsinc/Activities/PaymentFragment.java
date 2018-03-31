package com.quantumsit.sportsinc.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Adapters.ListView_Adapter_checkout;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.CustomView.NonScrollListView;
import com.quantumsit.sportsinc.Entities.BookingCourseEntity;
import com.quantumsit.sportsinc.Entities.BookingCourseEntityFragment;
import com.quantumsit.sportsinc.Entities.CourseEntity;
import com.quantumsit.sportsinc.Entities.item_checkout;
import com.quantumsit.sportsinc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PaymentFragment extends Fragment {

    TextView  total_textview, no_booking_textView;
    LinearLayout all_layout;

    NonScrollListView listView;
    ListView_Adapter_checkout listView_adapter;

    ArrayList<item_checkout> list_items;

    CardView cardView;

    CardView totalPrice_cardview;
    GlobalVars globalVars;

    Button confirm_button, pay_later_button;
    ProgressDialog progressDialog;

    ArrayList<BookingCourseEntityFragment> booked;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_payment,container,false);

        globalVars = (GlobalVars) getActivity().getApplication();
        progressDialog = new ProgressDialog(getActivity());

        total_textview = root.findViewById(R.id.totalTextView_checkout);
        listView = root.findViewById(R.id.listView_checkout);
        totalPrice_cardview = root.findViewById(R.id.checkoutCardView);
        cardView = root.findViewById(R.id.paymentMethodCardView);
        confirm_button = root.findViewById(R.id.confirmButton_checkout);
        pay_later_button = root.findViewById(R.id.payLaterButton_checkout);
        cardView.setVisibility(View.GONE);

        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmClicked();
            }
        });

        no_booking_textView = root.findViewById(R.id.nobookingTextView_checkout);
        all_layout = root.findViewById(R.id.allLayout_checkout);

        list_items = new ArrayList<>();
        listView_adapter = new ListView_Adapter_checkout(getActivity(), list_items);

        booked = new ArrayList<>();
        progressDialog.show();
        fill_booked_list();

        return root;
    }



    public void confirmClicked() {
        for (int i=0; i<list_items.size();i++) {
            String[] trainee_ids = list_items.get(i).getIds().split("@");
            for (int j=0; j<trainee_ids.length;j++)
                insert_to_db(trainee_ids[j],1);
        }
        globalVars.setType(0);
        startActivity(new Intent(getActivity(), HomeActivity.class));
    }

    private void fill_booked_list(){
            booked.clear();

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.selectBooking);
            HashMap<String,String> params = new HashMap<>();
            params.put("user_id",String.valueOf(globalVars.getId()));

            httpCall.setParams(params);

            //final String finalDate_of_birth = date_of_birth;
            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);

                    if(response != null){
                        try {
                            for(int i=0; i<response.length(); i++) {
                                JSONObject result = response.getJSONObject(i);
                                String course_name = result.getString("course_name");
                                int course_id = result.getInt("course_id");
                                String class_name = result.getString("group_name");
                                int class_id = result.getInt("group_id");
                                String course_image = result.getString("ImageUrl");
                                String course_price = result.getString("price");
                                String trainee_name = result.getString("user_name");
                                int trainee_id = result.getInt("trainee_id");
                                booked.add(new BookingCourseEntityFragment(trainee_name, trainee_id,
                                        course_name, course_id, course_image, course_price,
                                        class_name, class_id));
                            }
                            fill_list_view();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        progressDialog.dismiss();
                        no_booking_textView.setVisibility(View.VISIBLE);
                        all_layout.setVisibility(View.GONE);
                    }

                }
            }.execute(httpCall);
    }

    private void insert_to_db(String trainee_id, int payment_type) {

        JSONObject where = new JSONObject();
        JSONObject value = new JSONObject();

        try {
            where.put("trainee_id", trainee_id);
            value.put("status", payment_type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.POST);
        httpCall.setUrl(Constants.updateData);
        HashMap<String,String> params = new HashMap<>();
        params.put("table","payment");
        params.put("where", where.toString());
        params.put("values",value.toString());


        httpCall.setParams(params);
        new HttpRequest(){
            @Override
            public void onResponse(JSONArray response) {
                super.onResponse(response);
                if (response!= null) {
                    try {
                        int id = response.getInt(0);
                        globalVars.setType(0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.execute(httpCall);
    }

    private void fill_list_view(){

        if ( booked!= null ){
            if(booked.size()!= 0) {
                list_items.clear();
                no_booking_textView.setVisibility(View.GONE);
                all_layout.setVisibility(View.VISIBLE);

                for (int i=0; i<booked.size(); i++) {
                    String image = booked.get(i).getCourse_image();
                    String class_name = booked.get(i).getClass_name();
                    String trainee_name = booked.get(i).getTrainee_name();
                    String price = booked.get(i).getPrice();
                    String id = String.valueOf(booked.get(i).getTrainee_id());
                    if (i > 0 && booked.get(i).getClass_id() == booked.get(i-1).getClass_id()) {
                       trainee_name =  booked.get(i-1).getTrainee_name() + "\n" + trainee_name;
                       id = booked.get(i-1).getTrainee_id() + "@" + id;
                       list_items.remove(list_items.size()-1);

                    }

                    String trainee_num = String.valueOf(trainee_name.split("\n").length);
                    int all_price = Integer.valueOf(price.trim()) * Integer.valueOf(trainee_num.trim());

                    item_checkout item = new item_checkout(image, class_name, trainee_num
                            ,trainee_name, String.valueOf(all_price));
                    item.setIds(id);
                    list_items.add(item);

                }
            } else {
                progressDialog.dismiss();
                no_booking_textView.setVisibility(View.VISIBLE);
                all_layout.setVisibility(View.GONE);
            }

        } else {
            progressDialog.dismiss();
            no_booking_textView.setVisibility(View.VISIBLE);
            all_layout.setVisibility(View.GONE);
        }

        if(list_items.size() > 1) {
            int total_price = 0;
            for (int i=0; i<list_items.size(); i++) {
                total_price += Integer.valueOf(list_items.get(i).getPrice());
            }
            total_textview.setText(String.valueOf(total_price));
        } else {
            totalPrice_cardview.setVisibility(View.GONE);
        }

        listView_adapter.notifyDataSetChanged();
        listView.setAdapter(listView_adapter);
        progressDialog.dismiss();

    }



}
