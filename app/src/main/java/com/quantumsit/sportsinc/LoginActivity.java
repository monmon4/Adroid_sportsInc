package com.quantumsit.sportsinc;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    GlobalVars globalVars;

    TextView register_textview;

    EditText phone_edittext, pass_edittext;
    String recieved_pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        globalVars = (GlobalVars) getApplication();

        phone_edittext = findViewById(R.id.phoneEditText_login);
        pass_edittext = findViewById(R.id.passEditText_login);

        register_textview = findViewById(R.id.registerTextView_login);
        register_textview.setPaintFlags(register_textview.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        register_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    public void loginClicked(View view) {

        String phone = phone_edittext.getText().toString();
        String pass = pass_edittext.getText().toString();
        boolean all_good = true;

        if (phone.equals("1")){
            globalVars.setUser_is(1);
        } else if (phone.equals("2")){
            globalVars.setUser_is(2);
        } else if (phone.equals("3")){
            globalVars.setUser_is(3);
        } else {
            Toast.makeText(LoginActivity.this, "Invalid phone", Toast.LENGTH_SHORT).show();
            all_good = false;
        }


        JSONObject where_info = new JSONObject();
        try {
            where_info.put("phone",phone);

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.selectData);
            HashMap<String,String> params = new HashMap<>();
            params.put("table","users");
            params.put("values",where_info.toString());

            httpCall.setParams(params);

            new HttpRequest(){
                @Override
                public void onResponse(JSONObject response) {
                    super.onResponse(response);
                    /*try {

                        JSONArray res_array = response.getJSONArray("data");
                        JSONObject single_obj = res_array.getJSONObject(0);
                        String pass = single_obj.getString("pass");

                        recieved_pass = res.getString("pass");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/


                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }

       if (all_good){
           Intent intent= new Intent(LoginActivity.this, HomeActivity.class);
           startActivity(intent);
           finish();
       }

    }


}
