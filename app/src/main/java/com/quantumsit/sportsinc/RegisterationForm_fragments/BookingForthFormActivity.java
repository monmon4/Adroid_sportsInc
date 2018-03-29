package com.quantumsit.sportsinc.RegisterationForm_fragments;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.hbb20.CountryCodePicker;
import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Activities.HomeActivity;
import com.quantumsit.sportsinc.Backend.Functions;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.Entities.Booking_info;
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
import java.util.Locale;
import java.util.Random;

public class BookingForthFormActivity extends AppCompatActivity {

    EditText E_firstName_editText, E_firstPhone_editText, E_secondName_editText, E_secondPhone_editText;
    CountryCodePicker ccp1, ccp2;
    String E_firstName, E_firstPhone, E_secondName, E_secondPhone;

    RadioGroup radioGroup;
    RadioButton other_radioButton, first_radioButton, second_radioButton, third_radioButton;
    EditText other_editText;
    String hear_about_us;

    CheckBox first_checkBox, second_checkBox;
    Functions functions;
    int parent_id = -1;

    Booking_info booking_info;
    GlobalVars globalVars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_forth_form);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Registration (4)");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        globalVars = (GlobalVars) getApplication();

        functions = new Functions(BookingForthFormActivity.this);

        booking_info = (Booking_info) getIntent().getSerializableExtra("booking_info");
        E_firstName_editText = findViewById(R.id.full_name1_forth);
        E_firstPhone_editText = findViewById(R.id.phone1_forth);
        ccp1 = findViewById(R.id.ccp1_forth);
        ccp1.registerCarrierNumberEditText(E_firstPhone_editText);
        E_secondName_editText = findViewById(R.id.full_name2_forth);
        E_secondPhone_editText = findViewById(R.id.phone2_forth);
        ccp2 = findViewById(R.id.ccp2_forth);
        ccp2.registerCarrierNumberEditText(E_secondPhone_editText);

        radioGroup = findViewById(R.id.radioGroup_forth);
        other_radioButton = findViewById(R.id.radioButton_4);
        other_editText = findViewById(R.id.otherEditText_forth);
        other_editText.setEnabled(false);

        first_checkBox = findViewById(R.id.checkBox1_forth);
        second_checkBox = findViewById(R.id.checkBox2_forth);

        first_radioButton = findViewById(R.id.radioButton_1);
        second_radioButton = findViewById(R.id.radioButton_2);
        third_radioButton = findViewById(R.id.radioButton_3);

        other_radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OtherEditTextEnabling();
            }
        });

        first_radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OtherEditTextEnabling();
            }
        });
        second_radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OtherEditTextEnabling();
            }
        });
        third_radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OtherEditTextEnabling();
            }
        });

        check_parent();
    }

    private void OtherEditTextEnabling() {
        if (other_radioButton.isChecked())
            other_editText.setEnabled(true);
        else
            other_editText.setEnabled(false);
    }


    public void back_forth(View view) {
        onBackPressed();
        finish();

    }

    public void done_forth(View view) {

        //boolean all_good = true;
        int selected_radioButton_id = radioGroup.getCheckedRadioButtonId();
        hear_about_us = "";

        E_firstName = E_firstName_editText.getText().toString();
        E_firstPhone = E_firstPhone_editText.getText().toString();

        E_secondName = E_secondName_editText.getText().toString();
        E_secondPhone = E_secondPhone_editText.getText().toString();

        if(TextUtils.isEmpty(E_firstName)&&TextUtils.isEmpty(E_firstName)) {
            //E_firstName_editText
        }

        if (!TextUtils.isEmpty(E_firstPhone)) {
            if(!isValidPhone1(ccp1.getFullNumber(), ccp1.getSelectedCountryNameCode())) {
                E_firstPhone_editText.setError("Invalid phone");
                //all_good = false;
                return;
            }
        }

        if (!TextUtils.isEmpty(E_secondPhone)) {
            if(!isValidPhone2(ccp2.getFullNumber(), ccp2.getSelectedCountryNameCode())) {
                E_secondPhone_editText.setError("Invalid phone");
                //all_good = false;
                return;
            }
        }

        if (selected_radioButton_id != -1) {
            RadioButton selected = findViewById(selected_radioButton_id);
            if (selected == other_radioButton){
                if(TextUtils.isEmpty(other_editText.getText().toString())) {
                    other_editText.setError("Please enter how you heard about us");
                    // all_good = false;
                    return;
                } else
                    hear_about_us = other_editText.getText().toString();

            } else {
                hear_about_us = selected.getText().toString();
            }

        } else {
            Toast.makeText(BookingForthFormActivity.this,"Please select how you heard about us", Toast.LENGTH_SHORT).show();
            //all_good = false;
            return;
        }

        if (!first_checkBox.isChecked() || !second_checkBox.isChecked()) {
            Toast.makeText(BookingForthFormActivity.this,"You have to accept our policy first", Toast.LENGTH_SHORT).show();
            // all_good = false;
            return;
        }

        booking_info.setForth(E_firstName, E_firstPhone, E_secondName, E_secondPhone, hear_about_us);
        check_mail();

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
                            parent_id = result.getInt("id");

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
                            parent_id = result.getInt("id");

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
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(10);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    private void insert_parent_to_DB(String name, final String mail, String phone, String address, final String pass) {


        JSONObject where_info = new JSONObject();

        try {
            where_info.put("name",name);
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
                        JSONObject result = response.getJSONObject(0);
                        parent_id = result.getInt("id");

                        HttpCall httpCall = new HttpCall();
                        httpCall.setMethodtype(HttpCall.POST);
                        httpCall.setUrl(Constants.sendMail);
                        HashMap<String,String> params = new HashMap<>();
                        Log.d("Verification","Mail: "+mail+" , code: "+pass);
                        params.put("email",mail);
                        params.put("code",String.valueOf(pass));
                        httpCall.setParams(params);

                        new HttpRequest(){
                            @Override
                            public void onResponse(JSONArray response) {
                                super.onResponse(response);
                                Toast.makeText(BookingForthFormActivity.this, "A random password has been sent to:\n" + mail, Toast.LENGTH_LONG).show();

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

    public boolean isValidPhone1(String phone_num, String country)
    {
        boolean isValid = false;

        String NumberStr = phone_num;                                       //number to validate
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber NumberProto = phoneUtil.parse(NumberStr, country);            //with default country
            isValid = phoneUtil.isValidNumber(NumberProto);                  //returns true
            E_firstPhone = phoneUtil.format(NumberProto, PhoneNumberUtil.PhoneNumberFormat.NATIONAL); //(202) 555-0100
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
            E_secondPhone = phoneUtil.format(NumberProto, PhoneNumberUtil.PhoneNumberFormat.NATIONAL); //(202) 555-0100
        }  catch (com.google.i18n.phonenumbers.NumberParseException e) {
            e.printStackTrace();
        }

        return isValid;
    }

    private  void check_mail(){

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
                        int the_id = result.getInt("id");
                        globalVars.setType(6);
                        update_db(the_id);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    insert_to_DB();
                }
            }
        }.execute(httpCall);

    }

    private void update_db(final int the_id) {

        String date_of_birth = booking_info.getYear_of_birth() + "-" +
                booking_info.getMonth_of_birth()+ "-" +
                booking_info.getDay_of_birth();
        Date date;
        DateFormat outdateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        DateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        try {
            date = DateFormat.parse(date_of_birth);
            date_of_birth = outdateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject where = new JSONObject();
        JSONObject values = new JSONObject();

        try {
            where.put("id", the_id);
            values.put("phone",booking_info.getPhone());
            if(parent_id != -1)
                values.put("parent_id",parent_id);
            values.put("email",booking_info.getMail());
            values.put("gender",booking_info.getGender());
            values.put("type",6);
            values.put("date_of_birth",date_of_birth);
            values.put("address",booking_info.getAddress());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpCall httpCall = functions.updateDB("users", where, values);

        //final String finalDate_of_birth = date_of_birth;
        new HttpRequest(){
            @Override
            public void onResponse(JSONArray response) {
                super.onResponse(response);

                if(response != null){
                    try {
                        int ID = response.getInt(0);
                        globalVars.setType(6);
                        insert_to_DB_info(the_id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    //show_toast("An error occurred");
                }

            }
        }.execute(httpCall);

    }

    private void insert_to_DB() {

        String date_of_birth = booking_info.getYear_of_birth() + "-" +
                booking_info.getMonth_of_birth()+ "-" +
                booking_info.getDay_of_birth();
        Date date;
        DateFormat outdateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        DateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        try {
            date = DateFormat.parse(date_of_birth);
            date_of_birth = outdateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        JSONObject info = new JSONObject();
        try {
            info.put("name",booking_info.getName());
            if (parent_id != -1) {
                info.put("parent_id",parent_id);
            }
            info.put("phone",booking_info.getPhone());
            info.put("email",booking_info.getMail());
            info.put("gender",booking_info.getGender());
            info.put("type",6);
            info.put("date_of_birth",date_of_birth);
            info.put("address",booking_info.getAddress());

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
                            int ID = response.getInt(0);
                            globalVars.setType(6);
                            insert_to_DB_info(ID);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        //show_toast("An error occurred");
                    }

                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void insert_to_DB_info(int id){

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();

        if (hear_about_us.equals(getString(R.string.friends)))
            hear_about_us = "0";

        else if (hear_about_us.equals(getString(R.string.social_media)))
            hear_about_us = "1";

        else if(hear_about_us.equals(getString(R.string.brochures)))
            hear_about_us = "2";

        JSONObject info = new JSONObject();
        try {
            info.put("user_id", id);
            info.put("date_register", dateFormat.format(date));
            info.put("father_name",booking_info.getF_name());
            info.put("father_phone",booking_info.getF_phone());
            info.put("father_email",booking_info.getF_mail());
            info.put("father_address",booking_info.getF_address());
            info.put("father_nationality",booking_info.getF_nationality());
            info.put("mother_name",booking_info.getM_name());
            info.put("mother_phone",booking_info.getM_phone());
            info.put("mother_email",booking_info.getM_mail());
            info.put("mother_address",booking_info.getM_address());
            info.put("mother_nationality",booking_info.getM_nationality());
            info.put("E_name",E_firstName);
            info.put("E_phone",E_firstPhone);
            info.put("E_name1",E_secondName);
            info.put("E_phone1",E_secondPhone);
            info.put("medical_notes",booking_info.getMedical());
            info.put("heard_of_us",hear_about_us);

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.insertData);
            HashMap<String,String> params = new HashMap<>();
            params.put("table","info_trainee");
            params.put("values",info.toString());

            httpCall.setParams(params);

            //final String finalDate_of_birth = date_of_birth;
            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);

                    if(response != null){
                        try {
                            int ID = response.getInt(0);
                            Toast.makeText(BookingForthFormActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(BookingForthFormActivity.this, HomeActivity.class));
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        //show_toast("An error occurred");
                    }

                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
