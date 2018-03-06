package com.quantumsit.sportsinc.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.quantumsit.sportsinc.Aaa_data.Config;
import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.Entities.UserEntity;
import com.quantumsit.sportsinc.R;
import com.quantumsit.sportsinc.util.ConnectionUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    GlobalVars globalVars;

    TextView forgetPassword;
    PopupWindow verfication_popup_window;

    EditText phone_edittext, pass_edittext;
    String phone, pass;

    String received_pass, received_mail, received_name, received_imgUrl ,received_date_of_birth;
    int received_id, received_gender, received_type;

    ProgressDialog progressDialog;

    boolean all_good;
    private LinearLayout login_ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        globalVars = (GlobalVars) getApplication();

        login_ll = findViewById(R.id.ll_login);
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.login_configure));

        phone_edittext = findViewById(R.id.phoneEditText_login);
        pass_edittext = findViewById(R.id.passEditText_login);

        forgetPassword = findViewById(R.id.forgetpassTextView_login);
        forgetPassword.setPaintFlags(forgetPassword.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = phone_edittext.getText().toString();
                if (phone.equals(""))
                    show_toast("Enter your phone number first...");

                else
                    checkPhone();
            }
        });

    }

    private void checkPhone() {

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

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);

                    if (response == null) {
                        show_toast("Phone not exists");

                    } else {
                        try {
                            JSONObject result = response.getJSONObject(0);
                            received_id = result.getInt("id");
                            received_name = result.getString("name");
                            received_imgUrl = result.getString("ImageUrl");
                            received_gender = result.getInt("gender");
                            received_type = result.getInt("type");
                            received_mail = result.getString("email");
                            received_date_of_birth = result.getString("date_of_birth");
                            verfication();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }.execute(httpCall);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void verfication(){
        progressDialog.dismiss();
        Random random_num = new Random();
        final int verfication_num = random_num.nextInt(9999 - 1000) + 1000;
        Log.d("Verfication","Code: "+verfication_num);
        //verification_msg = "" + verfication_num;


        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.window_verficationcode_layout,null);

        verfication_popup_window = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        if(Build.VERSION.SDK_INT>=21){
            verfication_popup_window.setElevation(5.0f);
        }

        final EditText verify_edit_text =  customView.findViewById(R.id.verficationEditText_verify);
        Button done_button =  customView.findViewById(R.id.doneButton_verify);
        verify_edit_text.setEnabled(true);

        verfication_popup_window.showAtLocation(login_ll, Gravity.CENTER,0,0);
        verfication_popup_window.setFocusable(true);
        verify_edit_text.setFocusable(true);
        verfication_popup_window.setOutsideTouchable(false);
        verfication_popup_window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        verfication_popup_window.update();

        done_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String verifcation = verify_edit_text.getText().toString().trim();

                if (verifcation.equals(String.valueOf(verfication_num))){
                    verfication_popup_window.dismiss();
                    newPasswordWindow();
                } else {
                    show_toast("Wrong code");
                }

            }
        } );


        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.POST);
        httpCall.setUrl(Constants.sendSMS);
        HashMap<String,String> params = new HashMap<>();
        params.put("phone",phone);
        params.put("message",String.valueOf(verfication_num));
        httpCall.setParams(params);

        new HttpRequest(){
            @Override
            public void onResponse(JSONArray response) {
                super.onResponse(response);

                if(response != null){
                    show_toast("Code has been sent");

                } else {
                    show_toast("An error has occurred");
                    verfication_popup_window.dismiss();
                }

            }
        }.execute(httpCall);
    }

    private void newPasswordWindow() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(LoginActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.change_password_dialog,null);

        final EditText OldPassword = mView.findViewById(R.id.OldPassword);
        OldPassword.setVisibility(View.GONE);
        final EditText NewPassword = mView.findViewById(R.id.MyNewPassword);
        final EditText ConfirmPassword = mView.findViewById(R.id.ConfirmMyNewPassword);

        mBuilder.setView(mView);

        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton("Change Password", new DialogInterface.OnClickListener() {@Override public void onClick(DialogInterface dialogInterface, int i) { }});
        final AlertDialog alertdialog = mBuilder.create();
        alertdialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = alertdialog.getButton((AlertDialog.BUTTON_POSITIVE));
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    //Write your Logic. It will never dismiss the dialog unless your condition satisifies
                    String pass = NewPassword.getText().toString();
                    String Confir = ConfirmPassword.getText().toString();
                    if (!TextUtils.isEmpty(NewPassword.getText()) &&pass.equals(Confir)){
                        updatePassword(pass,alertdialog);
                    }

                    }
                });
            }
        });
        alertdialog.show();
    }

    private void updatePassword(final String pass, final AlertDialog alertdialog) {
        try {
            JSONObject values = new JSONObject();
            values.put("pass",pass);

            JSONObject where = new JSONObject();
            where.put("id",globalVars.getId());

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.updateData);
            final HashMap<String,String> params = new HashMap<>();
            params.put("table","users");
            params.put("where",where.toString());
            params.put("values",values.toString());
            httpCall.setParams(params);

            progressDialog.show();
            alertdialog.dismiss();
            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    if(checkResponse(response)) {
                        show_toast("Password Changed");
                        received_pass = pass;
                        ActiveUser();
                    }else {
                        show_toast("Failed");
                        progressDialog.dismiss();
                    }
                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean checkResponse(JSONArray response) {
        if (response != null){
            try {
                String result = response.getString(0);
                if (result.equals("DONE"))
                    return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @SuppressLint("StaticFieldLeak")
    public void registerClicked(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    private boolean checkConnection() {
        // first, check connectivity
        if (ConnectionUtilities
                .checkInternetConnection(this)) {
            return true;
        }
        return false;
    }

    @SuppressLint("StaticFieldLeak")
    public void loginClicked(View view) {
        if (!checkConnection()){
            show_toast(getResources().getString(R.string.no_connection));
            return;
        }
        phone = phone_edittext.getText().toString();
        pass = pass_edittext.getText().toString();
        all_good = true;

        if (phone.equals("") ){
            show_toast("Phone is missing");

        } else if (pass.equals("")) {
            show_toast("Password is missing");

        } else {
            all_good = true;
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
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
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
                                    received_imgUrl = result.getString("ImageUrl");
                                    received_gender= result.getInt("gender");
                                    received_type = result.getInt("type");
                                    received_mail = result.getString("email");
                                    received_date_of_birth = result.getString("date_of_birth");
                                    ActiveUser();
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

    private void ActiveUser() {
        try {
            SharedPreferences tokenPref = getSharedPreferences(Config.SHARED_PREF, MODE_PRIVATE);
            String user_token = tokenPref.getString("regId", "");

            JSONObject where_info = new JSONObject();
            where_info.put("id",received_id);

            JSONObject values = new JSONObject();
            values.put("active",1);
            if (!user_token.equals(""))
                values.put("token",user_token);
            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.updateData);
            HashMap<String,String> params = new HashMap<>();
            params.put("table","users");
            params.put("values",values.toString());
            params.put("where",where_info.toString());

            httpCall.setParams(params);

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    go_to_home();
                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void go_to_home(){

        globalVars.settAll(received_name,received_imgUrl, phone, pass, received_mail,
                            received_id, received_type, received_gender, received_date_of_birth);

        UserEntity userEntity = new UserEntity(received_name,received_imgUrl, phone,pass, received_mail,
                received_id, received_type, received_gender,received_date_of_birth);

        SharedPreferences.Editor preferences = getSharedPreferences("UserFile", MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(userEntity);
        preferences.putString("CurrentUser", json);
        preferences.apply();

        progressDialog.dismiss();
        Intent intent= new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();

    }

    public void show_toast(String msg){
        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
