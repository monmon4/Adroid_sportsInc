package com.quantumsit.sportsinc.RegisterationForm_fragments;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.hbb20.CountryCodePicker;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Entities.Booking_info;
import com.quantumsit.sportsinc.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_third_form);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Registration (3)");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        globalVars = (GlobalVars) getApplication();

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
            }
        } else if (!TextUtils.isEmpty(firstPhone)) {
            if(!isValidPhone1(first_ccp.getFullNumber(), first_ccp.getSelectedCountryNameCode())) {
                firstPhone_editText.setError("Invalid phone");
                all_good = false;
            }
        }

        secondName = secondName_editText.getText().toString();
        secondNationality = secondNationality_editText.getText().toString();
        secondAddress = secondAddress_editText.getText().toString();
        secondEmail = secondEmail_editText.getText().toString();
        secondPhone = secondPhone_editText.getText().toString();

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
            Intent intent = new Intent(BookingThirdFormActivity.this, BookingForthFormActivity.class);
            booking_info.setThird(firstName, firstNationality, firstAddress, firstEmail, firstPhone,
                    secondName, secondNationality, secondAddress, secondEmail, secondPhone);
            intent.putExtra("booking_info", booking_info);
            startActivity(intent);

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
}
