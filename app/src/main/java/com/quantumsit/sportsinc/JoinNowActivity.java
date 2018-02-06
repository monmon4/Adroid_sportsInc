package com.quantumsit.sportsinc;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.quantumsit.sportsinc.Aaa_data.Academy_info;
import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;

import javax.xml.datatype.Duration;

public class JoinNowActivity extends AppCompatActivity {

    GlobalVars globalVars;
    ProgressDialog progressDialog;

    TextView location_textview, phone_textview;

    MaterialBetterSpinner date_spinner, time_spinner ;

    Academy_info academy_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_now);
        globalVars = (GlobalVars) getApplication();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait......");

        location_textview = findViewById(R.id.locationTextView_joinnow);
        phone_textview = findViewById(R.id.phoneTextView_joinnow);



        if (globalVars.getMyDB().Academy_empty())
            get_info();

        else
            academy_info = globalVars.getMyDB().getAcademyInfo();

        date_spinner = findViewById(R.id.dateSpinner_joinnow);
        time_spinner = findViewById(R.id.timeSpinner_joinnow);

        ArrayAdapter<CharSequence> date_adapter = ArrayAdapter.createFromResource(JoinNowActivity.this, R.array.date_array,android.R.layout.simple_spinner_item );
        date_adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        date_spinner.setAdapter(date_adapter);
        ArrayAdapter<CharSequence> time_adapter = ArrayAdapter.createFromResource(JoinNowActivity.this, R.array.time_array,android.R.layout.simple_spinner_item );
        time_adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        time_spinner.setAdapter(time_adapter);

    }

    //map btn is pressed
    public void go_to_maps(View view) {

        //Intent intent = new Intent(JoinNowActivity.this, MapsActivity.class);
        //intent.putExtra("Academy_Lat",academy_info.getLat());
        //intent.putExtra("Academy_Lng",academy_info.getLng());
        //intent.putExtra("Academy_Name",academy_info.getName());
        //startActivity(intent);

        double lat = 0.0 , lng = 0.0;

        if (!academy_info.getLat().equals(""))
            lat = Double.parseDouble(academy_info.getLat());

        if (!academy_info.getLng().equals(""))
            lng = Double.parseDouble(academy_info.getLng());

        String geoUri = "http://maps.google.com/maps?q=loc:" + lat + "," + lng + " (" + academy_info.getName() + ")";
        String uri = String.format(Locale.ENGLISH, "geo:%f,%f", lat, lng);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }

    @SuppressLint("StaticFieldLeak")
    private void get_info() {
        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.POST);
        httpCall.setUrl(Constants.selectData);
        HashMap<String, String> params = new HashMap<>();
        params.put("table", "info_academy");

        httpCall.setParams(params);
        progressDialog.show();
        new HttpRequest() {
            @Override
            public void onResponse(JSONArray response) {
                super.onResponse(response);
                try {

                    if (response != null) {

                        JSONObject result = response.getJSONObject(0);
                        academy_info = new Academy_info();
                        academy_info.setAddress(result.getString("name"));
                        academy_info.setAddress(result.getString("address"));
                        academy_info.setLat(result.getString("address_Lat"));
                        academy_info.setLng(result.getString("address_Lng"));
                        academy_info.setPhone(result.getString("phone"));

                        globalVars.getMyDB().addAcademyInfo(academy_info);

                    } else {
                        show_toast("Error will get Academy Information.");
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }.execute(httpCall);
    }

    private void show_toast(String s) {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    //call btn is pressed
    public void on_call(View view) {

        String academy_phone = academy_info.getPhone();

        try {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + academy_phone));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                Toast.makeText(JoinNowActivity.this,"need a permission to call", Toast.LENGTH_SHORT).show();
                return;
            }
            startActivity(callIntent);
        } catch (ActivityNotFoundException activityException) {
            Log.e("Calling a Phone Number", "Call failed", activityException);
        }
    }
}
