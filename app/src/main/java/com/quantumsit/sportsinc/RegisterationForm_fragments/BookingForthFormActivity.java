package com.quantumsit.sportsinc.RegisterationForm_fragments;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.quantumsit.sportsinc.Activities.HomeActivity;
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
import java.util.Locale;

public class BookingForthFormActivity extends AppCompatActivity {

    String[] first_form_info, third_form_info;
    int gender;
    String second_form_info;

    EditText E_firstName_editText, E_firstPhone_editText, E_secondName_editText, E_secondPhone_editText;
    CountryCodePicker ccp1, ccp2;
    String E_firstName, E_firstPhone, E_secondName, E_secondPhone;

    RadioGroup radioGroup;
    RadioButton other_radioButton;
    EditText other_editText;
    String hear_about_us;

    CheckBox first_checkBox, second_checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_forth_form);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Registration (4)");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        first_form_info = getIntent().getStringArrayExtra("first_info");
        gender = getIntent().getIntExtra("gender", gender);
        second_form_info = getIntent().getStringExtra("second_info");
        third_form_info = getIntent().getStringArrayExtra("third_info");

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

        other_radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (other_radioButton.isChecked())
                    other_editText.setEnabled(true);
                else
                    other_editText.setEnabled(false);
            }
        });

    }


    public void back_forth(View view) {
        onBackPressed();
        finish();
        //startActivity(new Intent(BookingSecondFormActivity.this, BookingFirstFormActivity.class));

    }


    public void done_forth(View view) {

        //boolean all_good = true;
        int selected_radioButton_id = radioGroup.getCheckedRadioButtonId();
        hear_about_us = "";

        E_firstName = E_firstName_editText.getText().toString();
        E_firstPhone = E_firstPhone_editText.getText().toString();

        E_secondName = E_secondName_editText.getText().toString();
        E_secondPhone = E_secondPhone_editText.getText().toString();

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
            Toast.makeText(BookingForthFormActivity.this,"You have to accept ouu policy first", Toast.LENGTH_SHORT).show();
           // all_good = false;
            return;
        }



            insert_to_DB();
            startActivity(new Intent(BookingForthFormActivity.this, HomeActivity.class));
            finish();


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

    private void insert_to_DB() {

        String date_of_birth = first_form_info[8] + "-" + first_form_info[7] + "-" + first_form_info[6];
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
            info.put("name",first_form_info[0]+first_form_info[1]);
            info.put("phone",first_form_info[2]);
            info.put("email",first_form_info[5]);
            info.put("gender",gender);
            info.put("type",first_form_info[5]);
            info.put("date_of_birth",date_of_birth);
            info.put("address",first_form_info[4]);

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
            info.put("father_name",third_form_info[0]);
            info.put("father_phone",third_form_info[4]);
            info.put("father_email",third_form_info[3]);
            info.put("father_address",third_form_info[2]);
            info.put("father_nationality",third_form_info[1]);
            info.put("mother_name",third_form_info[5]);
            info.put("mother_phone",third_form_info[9]);
            info.put("mother_email",third_form_info[8]);
            info.put("mother_address",third_form_info[7]);
            info.put("mother_nationality",third_form_info[6]);
            info.put("E_name",E_firstName);
            info.put("E_phone",E_firstPhone);
            info.put("E_name1",E_secondName);
            info.put("E_phone1",E_secondPhone);
            info.put("medical_notes",second_form_info);
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
