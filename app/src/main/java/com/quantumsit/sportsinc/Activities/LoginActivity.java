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
import com.quantumsit.sportsinc.RegisterationForm_fragments.BookingFormActivity;
import com.quantumsit.sportsinc.RegisterationForm_fragments.RegisterationFormFragment;
import com.quantumsit.sportsinc.util.ConnectionUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    GlobalVars globalVars;

    TextView forgetPassword;
    PopupWindow verfication_popup_window;

    EditText mail_edittext, pass_edittext;
    String mail, pass;

    String received_pass, received_mail, received_name, received_imgUrl
            ,received_date_of_birth, received_phone;
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

        mail_edittext = findViewById(R.id.mailEditText_login);
        pass_edittext = findViewById(R.id.passEditText_login);

        forgetPassword = findViewById(R.id.forgetpassTextView_login);
        forgetPassword.setPaintFlags(forgetPassword.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mail = mail_edittext.getText().toString();
                if (mail.equals(""))
                    show_toast("Enter your phone number first...");

                else
                    checkMail();
            }
        });

    }

    private void checkMail() {

        JSONObject where_info = new JSONObject();

        try {
            where_info.put("email",mail);

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
                        show_toast("Email does not exist");

                    } else {
                        try {
                            JSONObject result = response.getJSONObject(0);
                            received_phone = result.getString("phone");
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
        //Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        //startActivity(intent);

        Intent intent = new Intent(LoginActivity.this, BookingFormActivity.class);
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
        mail = mail_edittext.getText().toString();
        pass = pass_edittext.getText().toString();
        all_good = true;

        if (TextUtils.isEmpty(mail) ){
            mail_edittext.setError("Email is missing");
        } else if (!isValidMail(mail)) {
            mail_edittext.setError("Wrong email format");
        } else if (TextUtils.isEmpty(pass)) {
            pass_edittext.setError("Password is missing");

        } else {
            all_good = true;
            JSONObject where_info = new JSONObject();
            try {
                where_info.put("email",mail);

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
                                    received_phone = result.getString("phone");
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
                                show_toast("Email doesn't exist");
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

        globalVars.settAll(received_name,received_imgUrl, received_phone, pass, received_mail,
                            received_id, received_type, received_gender, received_date_of_birth);

        UserEntity userEntity = new UserEntity(received_name,received_imgUrl, received_phone,pass, received_mail,
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

    public static boolean isValidMail(String mail)
    {
        String expression = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        CharSequence inputString = mail;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputString);
        if (matcher.matches())
        {
            return true;
        }
        else{
            return false;
        }
    }

    public void show_toast(String msg){
        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

}
