package com.quantumsit.sportsinc.RegisterationForm_fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.hbb20.CountryCodePicker;
import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Backend.Functions;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.Entities.Booking_info;
import com.quantumsit.sportsinc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookingThirdFormActivity extends AppCompatActivity {

    EditText firstName_editText, firstNationality_editText, firstAddress_editText, firstEmail_editText, firstPhone_editText;
    EditText secondName_editText, secondNationality_editText, secondAddress_editText, secondEmail_editText, secondPhone_editText;

    String firstName, firstNationality, firstAddress, firstEmail, firstPhone;
    String secondName, secondNationality, secondAddress, secondEmail, secondPhone;

    CountryCodePicker first_ccp, second_ccp;

    Booking_info booking_info;

    GlobalVars globalVars;
    Functions functions;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_third_form);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Step3");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        progressDialog = new ProgressDialog(BookingThirdFormActivity.this);
        progressDialog.setMessage("Please wait ....");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        globalVars = (GlobalVars) getApplication();
        functions = new Functions(BookingThirdFormActivity.this);

        TextView first_person = findViewById(R.id.textView3);
        TextView second_person = findViewById(R.id.textView4);
        first_person.setPaintFlags(first_person.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        second_person.setPaintFlags(second_person.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        booking_info = (Booking_info) getIntent().getSerializableExtra("booking_info");

        firstName_editText = findViewById(R.id.full_name1_third);
        firstNationality_editText = findViewById(R.id.nationality1_third);
        firstAddress_editText = findViewById(R.id.address1_third);
        firstEmail_editText = findViewById(R.id.mail1_third);
        firstPhone_editText = findViewById(R.id.phone1_third);
        first_ccp = findViewById(R.id.ccp1_third);
        first_ccp.registerCarrierNumberEditText(firstPhone_editText);

        secondName_editText = findViewById(R.id.full_name2_third);
        secondNationality_editText = findViewById(R.id.nationality2_third);
        secondAddress_editText = findViewById(R.id.address2_third);
        secondEmail_editText = findViewById(R.id.mail2_third);
        secondPhone_editText = findViewById(R.id.phone2_third);
        second_ccp = findViewById(R.id.ccp2_third);
        second_ccp.registerCarrierNumberEditText(firstPhone_editText);

        if (globalVars.getBooking_info() != null){
           firstName_editText.setText(globalVars.getBooking_info().getF_name());
            firstNationality_editText.setText(globalVars.getBooking_info().getF_nationality());
            firstAddress_editText.setText(globalVars.getBooking_info().getF_address());
            firstEmail_editText.setText(globalVars.getBooking_info().getF_mail());
            firstPhone_editText.setText(globalVars.getBooking_info().getF_phone());
            secondName_editText.setText(globalVars.getBooking_info().getM_name());
            secondNationality_editText.setText(globalVars.getBooking_info().getM_nationality());
            secondAddress_editText.setText(globalVars.getBooking_info().getM_address());
            secondEmail_editText.setText(globalVars.getBooking_info().getM_mail());
            secondPhone_editText.setText(globalVars.getBooking_info().getM_phone());
        }

    }


    public void back_third(View view) {
        onBackPressed();
        finish();

    }


    public void next_third(View view) {

        boolean all_good = true;
        firstName = firstName_editText.getText().toString();
        firstNationality = firstNationality_editText.getText().toString();
        firstAddress = firstAddress_editText.getText().toString();
        firstEmail = firstEmail_editText.getText().toString();
        firstPhone = firstPhone_editText.getText().toString();

        if(!TextUtils.isEmpty(firstEmail)) {
            if(!isValidMail(firstEmail)) {
                firstEmail_editText.setError("Invalid mail");
                all_good = false;
            } else if(firstEmail.equals(booking_info.getMail())) {
                firstEmail_editText.setError("Parent mail same as trainee mail");
                all_good = false;
            }
        } else if (!TextUtils.isEmpty(firstPhone)) {
            if(!isValidPhone1(first_ccp.getFullNumber(), first_ccp.getSelectedCountryNameCode())) {
                firstPhone_editText.setError("Invalid phone");
                all_good = false;
            }
        }

        secondName = secondName_editText.getText().toString().trim();
        secondNationality = secondNationality_editText.getText().toString().trim();
        secondAddress = secondAddress_editText.getText().toString().trim();
        secondEmail = secondEmail_editText.getText().toString().trim();
        secondPhone = secondPhone_editText.getText().toString().trim();

        if(!TextUtils.isEmpty(secondEmail)) {
            if(!isValidMail(secondEmail)) {
                secondEmail_editText.setError("Invalid mail");
                all_good = false;
            }
        }else if (!TextUtils.isEmpty(secondPhone)) {
            if(!isValidPhone2(second_ccp.getFullNumber(), second_ccp.getSelectedCountryNameCode())) {
                secondPhone_editText.setError("Invalid phone");
                all_good = false;
            }
        }



        if (all_good) {

            progressDialog.show();
            booking_info.setThird(firstName, firstNationality, firstAddress, firstEmail, firstPhone,
                    secondName, secondNationality, secondAddress, secondEmail, secondPhone);

            if(booking_info.getMail().equals(globalVars.getMail())) {
                check_parent();
            } else {
                int parent_id = globalVars.getId();
                open_forth_form(parent_id);
            }

        }
    }

    public boolean isValidPhone1(String phone_num, String country)
    {
        boolean isValid = false;

        String NumberStr = phone_num;                                       //number to validate
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber NumberProto = phoneUtil.parse(NumberStr, country);            //with default country
            isValid = phoneUtil.isValidNumber(NumberProto);                  //returns true
            firstPhone = phoneUtil.format(NumberProto, PhoneNumberUtil.PhoneNumberFormat.NATIONAL); //(202) 555-0100
        }  catch (com.google.i18n.phonenumbers.NumberParseException e) {
            e.printStackTrace();
        }

        return isValid;
    }

    public boolean isValidPhone2(String phone_num, String country)
    {
        boolean isValid = false;

        String NumberStr = phone_num;                                       //number to validate
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber NumberProto = phoneUtil.parse(NumberStr, country);            //with default country
            isValid = phoneUtil.isValidNumber(NumberProto);                  //returns true
            secondPhone = phoneUtil.format(NumberProto, PhoneNumberUtil.PhoneNumberFormat.NATIONAL); //(202) 555-0100
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void check_parent() {

        if (!booking_info.getF_mail().equals("")) {
            JSONObject where_info = new JSONObject();

            try {
                where_info.put("email",booking_info.getF_mail());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            HttpCall httpCall = functions.searchDB("users", where_info);

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    if(response != null){
                        try {
                            JSONObject result = response.getJSONObject(0);
                            int parent_id = result.getInt("id");
                            open_forth_form(parent_id);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        String random_pass = generateRandomPass();
                        insert_parent_to_DB(booking_info.getF_name(),
                                booking_info.getF_mail(), booking_info.getF_phone(),
                                booking_info.getF_address(), random_pass);
                        //verfication();
                    }
                }
            }.execute(httpCall);

        }else if (!booking_info.getM_mail().equals("")) {
            JSONObject where_info = new JSONObject();

            try {
                where_info.put("email",booking_info.getM_mail());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            HttpCall httpCall = functions.searchDB("users", where_info);

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    if(response != null){
                        try {
                            JSONObject result = response.getJSONObject(0);
                            int parent_id = result.getInt("id");
                            open_forth_form(parent_id);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        String random_pass = generateRandomPass();
                        insert_parent_to_DB(booking_info.getM_name(),
                                booking_info.getMail(), booking_info.getM_phone(),
                                booking_info.getM_address(), random_pass);
                        //verfication();
                    }
                }
            }.execute(httpCall);
        }
    }

    private String generateRandomPass() {
        Random generator = new Random();

        Random random_num = new Random();
        final int verfication_num = random_num.nextInt(9999999 - 1000) + 1000;

        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(10);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }

        return String.valueOf(verfication_num);
        //return randomStringBuilder.toString();
    }

    @SuppressLint("StaticFieldLeak")
    private void insert_parent_to_DB(String name, final String mail, String phone, String address, final String pass) {


        JSONObject where_info = new JSONObject();

        try {
            where_info.put("name",name);
            where_info.put("type",5);
            where_info.put("email",mail);
            where_info.put("phone",phone);
            where_info.put("address",address);
            where_info.put("pass",pass);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpCall httpCall = functions.insertToDB("users", where_info);

        new HttpRequest(){
            @Override
            public void onResponse(JSONArray response) {
                super.onResponse(response);
                if(response != null){
                    try {
                        final int parent_id = response.getInt(0);

                        HttpCall httpCall = new HttpCall();
                        httpCall.setMethodtype(HttpCall.POST);
                        httpCall.setUrl(Constants.sendMail);
                        HashMap<String,String> params = new HashMap<>();
                        Log.d("Verification","Mail: "+mail+" , code: "+pass);
                        params.put("email",mail);
                        params.put("code",pass);
                        httpCall.setParams(params);

                        new HttpRequest(){
                            @Override
                            public void onResponse(JSONArray response) {
                                super.onResponse(response);
                                Toast.makeText(BookingThirdFormActivity.this, "A random password has been sent to:\n" + mail, Toast.LENGTH_LONG).show();
                                open_forth_form(parent_id);

                            }
                        }.execute(httpCall);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    ////// a password yetbe3et l mail el parent dah w atl3lo msg en fii mail etb3atlo
                }
            }
        }.execute(httpCall);
    }


    private void open_forth_form(int parent_id){
        progressDialog.dismiss();
        Intent intent = new Intent(BookingThirdFormActivity.this, BookingForthFormActivity.class);
        intent.putExtra("booking_info", booking_info);
        intent.putExtra("parent_id", parent_id);
        startActivity(intent);
    }
}
