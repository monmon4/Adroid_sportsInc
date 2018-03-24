package com.quantumsit.sportsinc.Activities;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Adapters.ListView_Adapter_checkout;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.CustomView.NonScrollListView;
import com.quantumsit.sportsinc.Entities.BookingCourseEntity;
import com.quantumsit.sportsinc.Entities.CourseEntity;
import com.quantumsit.sportsinc.Entities.item_checkout;
import com.quantumsit.sportsinc.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

public class PaymentActivity extends AppCompatActivity {

    TextView  total_textview;

    NonScrollListView listView;
    ListView_Adapter_checkout listView_adapter;

    ArrayList<item_checkout> list_items;

    CardView totalPrice_cardview;
    GlobalVars globalVars;

    Button confirm_button, pay_later_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Check out");

        globalVars = (GlobalVars) getApplication();

        total_textview = findViewById(R.id.totalTextView_checkout);
        listView = findViewById(R.id.listView_checkout);
        totalPrice_cardview = findViewById(R.id.checkoutCardView);
        confirm_button = findViewById(R.id.confirmButton_checkout);
        pay_later_button = findViewById(R.id.payLaterButton_checkout);

        list_items = new ArrayList<>();
        listView_adapter = new ListView_Adapter_checkout(PaymentActivity.this, list_items);

        final ArrayList<BookingCourseEntity> bookedCourses = globalVars.getBookingCourseEntities();


        if ( bookedCourses!= null ){
            list_items.clear();
            for (int i=0; i<bookedCourses.size(); i++) {
                String image = bookedCourses.get(i).getCourseEntity().getImageUrl();
                String class_name = bookedCourses.get(i).getClass_name();
                String trainee_name = bookedCourses.get(i).getTrainee_name();
                String price = bookedCourses.get(i).getCourseEntity().getPrice();
                String trainee_num = String.valueOf(trainee_name.split("\n").length);
                int all_price = Integer.valueOf(price.trim()) * Integer.valueOf(trainee_num.trim());

                list_items.add(new item_checkout(image, class_name, trainee_num
                        ,trainee_name, String.valueOf(all_price)));
            }
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

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    public void payLaterClicked(View view) {
        insert(0);
        startActivity(new Intent(PaymentActivity.this, HomeActivity.class));
        finish();
    }

    public void confirmClicked(View view) {
        insert(1);
        startActivity(new Intent(PaymentActivity.this, HomeActivity.class));
        finish();
    }

    private  void insert(int payment_type) {

        final ArrayList<BookingCourseEntity> bookedCourses = globalVars.getBookingCourseEntities();

        for (int i=0; i<bookedCourses.size();i++) {
            String[] trainee_ids = bookedCourses.get(i).getTrainee_id().split("@");
            for (int j=0; j<trainee_ids.length;j++)
                insert_to_db(trainee_ids[j],
                        bookedCourses.get(i).getClass_id(),
                        bookedCourses.get(i).getCourseEntity().getCourse_id(),
                        payment_type);
        }

    }

    private void insert_to_db(String trainee_id, int selected_class_id, int selected_course_id, int payment_type) {


        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.POST);
        httpCall.setUrl(Constants.register_trainee);
        HashMap<String,String> params = new HashMap<>();
        params.put("user_id",trainee_id);
        params.put("class_id",String.valueOf(selected_class_id));
        params.put("course_id",String.valueOf(selected_course_id));
        params.put("payment_type",String.valueOf(payment_type));


        httpCall.setParams(params);
        new HttpRequest(){
            @Override
            public void onResponse(JSONArray response) {
                super.onResponse(response);
                if (response!= null) {
                    try {
                        int id = response.getInt(0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.execute(httpCall);
    }
}
