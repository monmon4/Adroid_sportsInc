package com.quantumsit.sportsinc.Activities;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
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
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;

import com.google.gson.Gson;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.hbb20.CountryCodePicker;
import com.quantumsit.sportsinc.Aaa_data.Config;
import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.Activities.HomeActivity;
import com.quantumsit.sportsinc.R;
import com.quantumsit.sportsinc.util.ConnectionUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.michaelrocks.libphonenumber.android.NumberParseException;


public class RegisterActivity extends AppCompatActivity {


    EditText name_edittext, lastname_edittext, phone_edittext, mail_edittext, pass_edittext, repass_edittext;

    //Spinner gender_spinner;

    String user_name, phone, mail, pass, repass;

    //PopupWindow verfication_popup_window;
    //private Context register_Context;
    //private RelativeLayout register_rl;
    ProgressDialog progressDialog;

    GlobalVars globalVars;
    CountryCodePicker ccp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Sign Up");

        /*
            // Create a TextView programmatically.
            TextView tv = new TextView(getApplicationContext());

            // Create a LayoutParams for TextView
            LayoutParams lp = new RelativeLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, // Width of TextView
                    LayoutParams.WRAP_CONTENT); // Height of TextView

            // Apply the layout parameters to TextView widget
            tv.setLayoutParams(lp);

            // Set text to display in TextView
            // This will set the ActionBar title text
            tv.setText("Sign Up");

            // Set the text color of TextView
            // This will change the ActionBar title text color
            tv.setTextColor(Color.parseColor("#FFFFFF"));

            // Center align the ActionBar title
            tv.setGravity(Gravity.CENTER);

            // Set the serif font for TextView text
            // This will change ActionBar title text font
            //tv.setTypeface(Typeface.SERIF, Typeface.ITALIC);

            // Underline the ActionBar title text
            //tv.setPaintFlags(tv.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

            // Set the ActionBar title font size
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);

            // Display a shadow around ActionBar title text
            //tv.setShadowLayer(
                   // 1.f, // radius
                   // 2.0f, // dx
                   // 2.0f, // dy
                   // Color.parseColor("#FF8C00") // shadow color
           // );

            // Set the ActionBar display option
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

            // Finally, set the newly created TextView as ActionBar custom view
            getSupportActionBar().setCustomView(tv);
        */

        globalVars = (GlobalVars) getApplication();
        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.configure));
        progressDialog.setCanceledOnTouchOutside(false);

        //register_Context = getApplicationContext();
        //register_rl =  findViewById(R.id.register_rl);


        name_edittext =  findViewById(R.id.firstnameEditText_register);
        lastname_edittext =  findViewById(R.id.lastnameEditText_register);
        phone_edittext =  findViewById(R.id.phoneEditText_register);
        mail_edittext =  findViewById(R.id.mailEditText_register);
        pass_edittext =  findViewById(R.id.passEditText_register);
        repass_edittext =  findViewById(R.id.repassEditText_register);
        ccp = findViewById(R.id.ccp_register);

        ccp.registerCarrierNumberEditText(phone_edittext);
        //day_edittext =  findViewById(R.id.dayEditText_register);
        //month_edittext =  findViewById(R.id.monthEditText_register);
        //year_edittext =  findViewById(R.id.yearEditText_register);

        //gender_spinner =  findViewById(R.id.genderSpinner_register);

        //ArrayAdapter<CharSequence> gender_spinner_adapter = ArrayAdapter.createFromResource(this, R.array.Gender_array, android.R.layout.simple_dropdown_item_1line);
        //gender_spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //gender_spinner.setAdapter(gender_spinner_adapter);

    }

    private boolean checkConnection() {
        // first, check connectivity
        if (ConnectionUtilities
                .checkInternetConnection(this)) {
            return true;
        }
        return false;
    }

    public boolean isValidPhone(String phone_num, String country)
    {
        boolean isValid = false;

        String NumberStr = phone_num;                                       //number to validate
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber NumberProto = phoneUtil.parse(NumberStr, country);            //with default country
            isValid = phoneUtil.isValidNumber(NumberProto);                  //returns true
            phone = phoneUtil.format(NumberProto, PhoneNumberUtil.PhoneNumberFormat.NATIONAL); //(202) 555-0100
        }  catch (com.google.i18n.phonenumbers.NumberParseException e) {
            e.printStackTrace();
        }

        return isValid;
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

    private boolean validateForm() {

        user_name = name_edittext.getText().toString();
        phone = phone_edittext.getText().toString();
        mail = mail_edittext.getText().toString();
        pass = pass_edittext.getText().toString();
        repass = repass_edittext.getText().toString();

        if (TextUtils.isEmpty(phone)) {
            phone_edittext.setError("Required.");
            return false;
        } else if (!isValidPhone(ccp.getFullNumber(), ccp.getSelectedCountryNameCode())){
            phone_edittext.setFocusable(true);
            phone_edittext.setError("Invalid phone number");
            return false;
        }
        if (TextUtils.isEmpty(user_name)) {
            name_edittext.setError("Required.");
            return false;
        }
        if (TextUtils.isEmpty(mail)) {
            mail_edittext.setError("Required.");
            return false;
        } else if (!isValidMail(mail)){
            mail_edittext.setError("Invalid email");
            return false;
        }
        if (TextUtils.isEmpty(pass)) {
            pass_edittext.setError("Required.");
            return false;
        } else if (pass_edittext.length()<8){
            pass_edittext.setError("Password should be more than 8 characters");
            return false;
        }
        if (!pass.equals(repass)){
            repass_edittext.setError("Passwords don't match");
            return false;
        }

        user_name +=  lastname_edittext.getText().toString();
        mail_edittext.setError(null);
        pass_edittext.setError(null);
        repass_edittext.setError(null);

        return true;
    }

    public void done_register(View view) {
        if (!checkConnection()){
            show_toast(getResources().getString(R.string.no_connection));
            return;
        }

        progressDialog.show();

        //gender = gender_spinner.getSelectedItem().toString();
        //day_of_birth = day_edittext.getText().toString();
        //month_of_birth = month_edittext.getText().toString();
        //year_of_birth = year_edittext.getText().toString();

        if (!validateForm()) {
            progressDialog.dismiss();
            return;
        }

        /*if(day_of_birth.equals("") || month_of_birth.equals("") || year_of_birth.equals("")){
            show_toast("date of birth is missing ");
            return;
        } else {
            int day = Integer.valueOf(day_of_birth);
            int month = Integer.valueOf(month_of_birth);
            int year = Integer.valueOf(year_of_birth);
            int current_year = Calendar.getInstance().get(Calendar.YEAR);

            if (day==0 || month==0 || year==0 || day>31 || month>12 || year>current_year-4 || year < current_year-60) {
                show_toast("not a valid birthday format");
                return;
            }
        }*/

        checkPhone();

    }

    @SuppressLint("StaticFieldLeak")
    private void checkMail() {

        JSONObject where_info = new JSONObject();

        try {
            where_info.put("email",mail);
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
                    progressDialog.dismiss();
                    mail_edittext.setError("Email already exists");

                } else {
                    insert_to_DB();
                    //verfication();
                }

            }
        }.execute(httpCall);
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
                    progressDialog.dismiss();
                    phone_edittext.setError("Phone already exists");

                } else {
                    checkMail();
                    //verfication();
                }

            }
        }.execute(httpCall);
    }

   /* @SuppressLint("StaticFieldLeak")
    private void verfication(){
        progressDialog.dismiss();
        String verification_msg;
        Random random_num = new Random();
        final int verfication_num = random_num.nextInt(9999 - 1000) + 1000;
        Log.d("Verfication","Code: "+verfication_num);
        //verification_msg = "" + verfication_num;


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
    }*/

    @SuppressLint("StaticFieldLeak")
    public void insert_to_DB(){
        progressDialog.setMessage(getResources().getString(R.string.log_in));
        progressDialog.show();

        /*int current_year = Calendar.getInstance().get(Calendar.YEAR);
        int day = Integer.valueOf(day_of_birth);
        int month = Integer.valueOf(month_of_birth);
        int year = Integer.valueOf(year_of_birth);
        int age = current_year - year;

        String date_of_birth = year + "-" + month + "-" + day;
        Date date;
        DateFormat outdateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        DateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        try {
            date = DateFormat.parse(date_of_birth);
            date_of_birth = outdateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/

       /* verfication_popup_window.dismiss();
        globalVars.setType(5);
        finish();*/
        SharedPreferences tokenPref = getSharedPreferences(Config.SHARED_PREF, MODE_PRIVATE);
        String user_token = tokenPref.getString("regId", "");


        JSONObject info = new JSONObject();
        try {
            info.put("name",user_name);
            info.put("phone",phone);
            info.put("email",mail);
            info.put("pass",pass);
            info.put("type",5);
            if (!user_token.equals(""))
                info.put("token",user_token);

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.insertData);
            HashMap<String,String> params = new HashMap<>();
            params.put("table","users");
            params.put("values",info.toString());

            httpCall.setParams(params);

            //final String finalDate_of_birth = date_of_birth;
            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);

                    if(response != null){
                        try {
                            //verfication_popup_window.dismiss();
                            int ID = response.getInt(0);
                            logIn(ID);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        //verfication_popup_window.dismiss();
                        show_toast("An error occurred");
                    }

                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void logIn(int id) {
        Log.d("Verfication","ID: "+id);
        globalVars.settAll(user_name, "", phone, pass, mail, id, 5, 0, "0");
        SharedPreferences.Editor preferences = getSharedPreferences("UserFile", MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(globalVars.getUser());
        preferences.putString("CurrentUser", json);
        preferences.apply();
        progressDialog.dismiss();
        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void show_toast(String msg){
        progressDialog.dismiss();
        Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    private  void SetActionBarText(ActionBar ab){

    }
}
