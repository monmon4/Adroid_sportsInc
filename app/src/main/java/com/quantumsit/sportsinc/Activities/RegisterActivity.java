package com.quantumsit.sportsinc.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class RegisterActivity extends AppCompatActivity {


    EditText name_edittext, phone_edittext, mail_edittext, pass_edittext, repass_edittext,
            day_edittext, month_edittext, year_edittext;

    Spinner gender_spinner;

    String user_name, phone, mail, pass, repass, day_of_birth, month_of_birth, year_of_birth, gender;

    PopupWindow verfication_popup_window;
    private Context register_Context;
    private RelativeLayout register_rl;

    GlobalVars globalVars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        globalVars = (GlobalVars) getApplication();

        register_Context = getApplicationContext();
        register_rl =  findViewById(R.id.register_rl);

        name_edittext =  findViewById(R.id.nameEditText_register);
        phone_edittext =  findViewById(R.id.phoneEditText_register);
        mail_edittext =  findViewById(R.id.mailEditText_register);
        pass_edittext =  findViewById(R.id.passEditText_register);
        repass_edittext =  findViewById(R.id.repassEditText_register);

        day_edittext =  findViewById(R.id.dayEditText_register);
        month_edittext =  findViewById(R.id.monthEditText_register);
        year_edittext =  findViewById(R.id.yearEditText_register);

        gender_spinner =  findViewById(R.id.genderSpinner_register);

        ArrayAdapter<CharSequence> gender_spinner_adapter = ArrayAdapter.createFromResource(this, R.array.Gender_array, android.R.layout.simple_dropdown_item_1line);
        gender_spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender_spinner.setAdapter(gender_spinner_adapter);

    }


    public void done_register(View view) {

        boolean all_good = false;

        user_name = name_edittext.getText().toString();
        phone = phone_edittext.getText().toString();
        mail = mail_edittext.getText().toString();

        pass = pass_edittext.getText().toString();
        repass = repass_edittext.getText().toString();

        gender = gender_spinner.getSelectedItem().toString();

        day_of_birth = day_edittext.getText().toString();
        month_of_birth = month_edittext.getText().toString();
        year_of_birth = year_edittext.getText().toString();

        if (user_name.equals("")){
            show_toast("user name is empty");
        } else if(phone.equals("")){
            show_toast("Phone is empty");
        } else if (mail.equals("")) {
            show_toast("mail is empty");
        }else if (pass.equals("")){
            show_toast("password is empty");
        } else if (repass.equals("")) {
            show_toast("please, confirm your password");
        } else if ( !pass.equals(repass)){
            show_toast("passwords don't match");
        }else if(day_of_birth.equals("") || month_of_birth.equals("") || year_of_birth.equals("")){
            show_toast("date of birth is missing ");
        } else {
            int day = Integer.valueOf(day_of_birth);
            int month = Integer.valueOf(month_of_birth);
            int year = Integer.valueOf(year_of_birth);
            int current_year = Calendar.getInstance().get(Calendar.YEAR);

            if (day==0 || month==0 || year==0 || day>31 || month>12 || year>current_year-4 || year < current_year-60) {
                show_toast("not a valid birthday format");
            } else {
                all_good = true;
            }
        }

        if (all_good) {
            checkPhone();

        }
    }

    @SuppressLint("StaticFieldLeak")
    private void checkPhone() {

        JSONObject where_info = new JSONObject();

        try {
            where_info.put("phone",phone);
        } catch (JSONException e) {
            e.printStackTrace();
        }

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

                if(response != null){
                    show_toast("Phone already exists");

                } else {
                    verfication();
                }

            }
        }.execute(httpCall);
    }

    @SuppressLint("StaticFieldLeak")
    private void verfication(){

        String verification_msg;
        Random random_num = new Random();
        final int verfication_num = random_num.nextInt(9999 - 1000) + 1000;
        verification_msg = "Your verfication code: " + verfication_num;


        LayoutInflater inflater = (LayoutInflater) register_Context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.window_verficationcode_layout,null);

        verfication_popup_window = new PopupWindow(
                customView,
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );

        if(Build.VERSION.SDK_INT>=21){
            verfication_popup_window.setElevation(5.0f);
        }

        final EditText verify_edit_text =  customView.findViewById(R.id.verficationEditText_verify);
        Button done_button =  customView.findViewById(R.id.doneButton_verify);
        verify_edit_text.setEnabled(true);

        verfication_popup_window.showAtLocation(register_rl, Gravity.CENTER,0,0);
        verfication_popup_window.setFocusable(true);
        verify_edit_text.setFocusable(true);
        verfication_popup_window.setOutsideTouchable(false);
        verfication_popup_window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        verfication_popup_window.update();

        done_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String verifcation = verify_edit_text.getText().toString().trim();
                insert_to_DB();
                if (verifcation.equals(String.valueOf(verfication_num))){
                    insert_to_DB();
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
        params.put("message",verification_msg);
        httpCall.setParams(params);

        new HttpRequest(){
            @Override
            public void onResponse(JSONArray response) {
                super.onResponse(response);

                if(response != null){
                    show_toast("Success");

                } else {
                    show_toast("An error occurred");
                }

            }
        }.execute(httpCall);
    }

    @SuppressLint("StaticFieldLeak")
    public void insert_to_DB(){
        int gender_int;
        if (gender.equals("Male")){
            gender_int = 0;
        } else {
            gender_int = 1;
        }
        int current_year = Calendar.getInstance().get(Calendar.YEAR);
        int day = Integer.valueOf(day_of_birth);
        int month = Integer.valueOf(month_of_birth);
        int year = Integer.valueOf(year_of_birth);
        int age = current_year - year;

        String date_of_birth = year + "-" + month + "-" + day;
        Date date;
        DateFormat outdateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            date = DateFormat.parse(date_of_birth);
            date_of_birth = outdateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        verfication_popup_window.dismiss();
        globalVars.setType(5);
        finish();


       JSONObject info = new JSONObject();
        try {
            info.put("name",user_name);
            info.put("phone",phone);
            info.put("email",mail);
            info.put("gender",gender_int);
            info.put("pass",pass);
            info.put("date_of_birth",date_of_birth);
            info.put("type",5);

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.insertData);
            HashMap<String,String> params = new HashMap<>();
            params.put("table","users");
            params.put("values",info.toString());

            httpCall.setParams(params);

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);

                    if(response != null){
                        verfication_popup_window.dismiss();
                        globalVars.setType(5);
                        Intent intent = new Intent(RegisterActivity.this , HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        show_toast("An error occurred");
                    }

                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void show_toast(String msg){
        Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}