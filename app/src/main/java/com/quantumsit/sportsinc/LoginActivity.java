package com.quantumsit.sportsinc;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
    String phone, pass;

    String received_pass, received_mail, received_name;
    int received_id, received_gender, received_type, received_age;

    ProgressDialog progressDialog;

    boolean all_good;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        globalVars = (GlobalVars) getApplication();

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Configuring user.....");

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

    @SuppressLint("StaticFieldLeak")
    public void loginClicked(View view) {

        phone = phone_edittext.getText().toString();
        pass = pass_edittext.getText().toString();
        all_good = true;

        if (phone.equals("") ){
            show_toast("Phone is missing");

        } else if (pass.equals("")) {
            show_toast("Password is missing");

        } else {
            all_good = true;
            go_to_home();
            JSONObject where_info = new JSONObject();
            try {
                where_info.put("phone",phone);

                HttpCall httpCall = new HttpCall();
                httpCall.setMethodtype(HttpCall.POST);
                httpCall.setUrl(Constants.selectData);
                HashMap<String,String> params = new HashMap<>();
                params.put("table","users");
                params.put("where",where_info.toString());

                httpCall.setParams(params);
                progressDialog.show();
                new HttpRequest(){
                    @Override
                    public void onResponse(JSONArray response) {
                        super.onResponse(response);
                        try {

                            if (response != null) {

                                JSONObject result = response.getJSONObject(0);
                                received_pass = result.getString("pass");

                                if (received_pass.equals(pass)) {
                                    all_good = true;
                                    received_id = result.getInt("id");
                                    received_name = result.getString("name");
                                    received_gender= result.getInt("gender");
                                    received_type = result.getInt("type");
                                    received_mail = result.getString("email");
                                    received_age = result.getInt("age");
                                    go_to_home();


                                } else {
                                    progressDialog.dismiss();
                                    show_toast("Password is incorrect");
                                }

                            } else {
                                progressDialog.dismiss();
                                show_toast("Phone doesn't exist");
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

    }

    private void go_to_home(){

        globalVars.settAll(received_name, phone, pass, received_mail,
                            received_id, received_type, received_gender, received_age);
        if (phone.equals("1") || phone.equals("04")) {
            received_type = 0;
        } else if (phone.equals("2")) {
            received_type = 1;
        } else {
            received_type = 2;
        }
        progressDialog.dismiss();
        Intent intent= new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();

    }

    public void show_toast(String msg){
        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

}
