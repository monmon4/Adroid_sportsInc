package com.quantumsit.sportsinc.RegisterationForm_fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.hbb20.CountryCodePicker;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Aaa_data.Trainees_info;
import com.quantumsit.sportsinc.Activities.HomeActivity;
import com.quantumsit.sportsinc.Activities.startPageActivity;
import com.quantumsit.sportsinc.Entities.Booking_info;
import com.quantumsit.sportsinc.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.app.DialogFragment;
import java.util.Calendar;

public class BookingFirstFormActivity extends AppCompatActivity {

    EditText firstName_editText, lastName_editText, phone_editText
            , nationality_editText, address_editText, mail_editText;

    TextView day_editText, month_editText, year_editText;
    String day_of_birth, month_of_birth, year_of_birth;

    String name, first_name, last_name, phone, nationality, address, mail;

    TextView gender_textView, maleGender_textView, femaleGender_textView, date_textView;
    int gender = 2;

    GlobalVars globalVars;
    CountryCodePicker ccp;

    Booking_info booking_info;

    static boolean isActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_first_form);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Step1");
        globalVars = (GlobalVars) getApplication();
        booking_info = new Booking_info();
        isActive = true;

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        firstName_editText = findViewById(R.id.first_name_first);
        lastName_editText = findViewById(R.id.last_name_first);

        gender_textView = findViewById(R.id.genderTextView);
        maleGender_textView = findViewById(R.id.male_first);
        femaleGender_textView = findViewById(R.id.female_first);

        date_textView = findViewById(R.id.dateTextView);
        day_editText = findViewById(R.id.dayEditText);
        month_editText = findViewById(R.id.monthEditText);
        year_editText = findViewById(R.id.yearEditText);
        day_editText.setClickable(true);
        month_editText.setClickable(true);
        year_editText.setClickable(true);

        ccp = findViewById(R.id.ccp_first);
        phone_editText = findViewById(R.id.phone_first);
        ccp.registerCarrierNumberEditText(phone_editText);

        nationality_editText = findViewById(R.id.nationality_first);
        address_editText = findViewById(R.id.address_first);
        mail_editText = findViewById(R.id.mail_first);

        /*if(globalVars.getBooking_info()== null){
            firstName_editText.setText(globalVars.getName());
            mail_editText.setText(globalVars.getMail());
            phone_editText .setText(globalVars.getPhone());
        }*/


        maleGender_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                male_pressed();
            }
        });

        femaleGender_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                female_pressed();
            }
        });

        day_editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initialize a new date picker dialog fragment
                DialogFragment dFragment = new DatePickerFragment();

                // Show the date picker dialog fragment
                dFragment.show(getFragmentManager(), "Date Picker");

            }
        });

        month_editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initialize a new date picker dialog fragment
                DialogFragment dFragment = new DatePickerFragment();

                // Show the date picker dialog fragment
                dFragment.show(getFragmentManager(), "Date Picker");

            }
        });

    }

    public void next_first(View view) {

        if(validateForm()){
            Intent intent = new Intent(BookingFirstFormActivity.this, BookingSecondFormActivity.class);
            booking_info.setFirst(first_name+" "+last_name, mail, phone, nationality,
                    day_of_birth, month_of_birth, year_of_birth, address, gender);

            intent.putExtra("booking_info", booking_info);
            startActivity(intent);
        }


    }

    public void back_first(View view) {
        startActivity(new Intent(BookingFirstFormActivity.this, HomeActivity.class));
        finish();
        //startActivity(new Intent(BookingFirstFormActivity.this, BookingSecondFormActivity.class));
    }

    private void male_pressed() {
        gender = 0;
        maleGender_textView.setBackgroundResource(R.drawable.cornered_background_red);
        femaleGender_textView.setBackgroundResource(R.drawable.cornered_background_gray);
    }

    private void female_pressed() {
        gender = 1;
        maleGender_textView.setBackgroundResource(R.drawable.cornered_background_gray);
        femaleGender_textView.setBackgroundResource(R.drawable.cornered_background_red);
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

        first_name = firstName_editText.getText().toString().trim();
        last_name = lastName_editText.getText().toString().trim();

        phone = phone_editText.getText().toString().trim();

        day_of_birth = day_editText.getText().toString().trim();
        month_of_birth = month_editText.getText().toString().trim();
        year_of_birth = year_editText.getText().toString().trim();

        nationality = nationality_editText.getText().toString().trim();
        address = address_editText.getText().toString().trim();
        mail = mail_editText.getText().toString().trim();

        if (TextUtils.isEmpty(first_name)) {
            firstName_editText.setFocusable(true);
            firstName_editText.setError("Required.");
            return false;
        } else if (gender == 2) {
            gender_textView.setError("Required.");
            return false;
        }else if (!TextUtils.isEmpty(phone)) {
            if (!isValidPhone(ccp.getFullNumber(), ccp.getSelectedCountryNameCode())) {
                phone_editText.setFocusable(true);
                phone_editText.setError("Invalid phone number");
                return false;
            }
        } else if(TextUtils.isEmpty(day_of_birth) || TextUtils.isEmpty(month_of_birth) || TextUtils.isEmpty(year_of_birth)){
            year_editText.setError("Required.");
            year_editText.setFocusable(true);
            return false;

        } else {


            int day = Integer.valueOf(day_of_birth);
            int month = Integer.valueOf(month_of_birth);
            int year = Integer.valueOf(year_of_birth);
            int current_year = Calendar.getInstance().get(Calendar.YEAR);

            if (day==0 || month==0 || year==0 || day>31 || month>12 || year>current_year-4 || year < current_year-60) {
                year_editText.setError("not a valid birthday format");
                year_editText.setFocusable(true);
                return false;
            }
        }


        if (TextUtils.isEmpty(nationality)) {
            nationality_editText.setError("Required.");
            return false;
        }else if (TextUtils.isEmpty(address)) {
            address_editText.setError("Required.");
            return false;
        }//else if (TextUtils.isEmpty(mail)) {
            //mail_editText.setError("Required.");
           // return false;
       // }
        else if(!TextUtils.isEmpty(mail)) {
            if (!isValidMail(mail)){
                mail_editText.setError("Invalid email");
                return false;
            }
        }



        name +=  first_name + last_name;
        mail_editText.setError(null);
        firstName_editText.setError(null);
        mail_editText.setError(null);

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){

            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);


            // DatePickerDialog THEME_DEVICE_DEFAULT_LIGHT
            DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                    AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,this,year,month,day);

            // DatePickerDialog THEME_DEVICE_DEFAULT_DARK
            DatePickerDialog dpd2 = new DatePickerDialog(getActivity(),
                    AlertDialog.THEME_DEVICE_DEFAULT_DARK,this,year,month,day);

            // DatePickerDialog THEME_HOLO_LIGHT
            DatePickerDialog dpd3 = new DatePickerDialog(getActivity(),
                    AlertDialog.THEME_HOLO_LIGHT,this,year,month,day);

            // DatePickerDialog THEME_HOLO_DARK
            DatePickerDialog dpd4 = new DatePickerDialog(getActivity(),
                    AlertDialog.THEME_HOLO_DARK,this,year,month,day);

            // DatePickerDialog THEME_TRADITIONAL
            DatePickerDialog dpd5 = new DatePickerDialog(getActivity(),
                    AlertDialog.THEME_TRADITIONAL,this,year,month,day);

            // Return the DatePickerDialog
            return  dpd3;
        }

        public void onDateSet(DatePicker view, int year, int month, int day){
            // Do something with the chosen date

            TextView day_edit_text = getActivity().findViewById(R.id.dayEditText);
            TextView month_edit_text = getActivity().findViewById(R.id.monthEditText);
            TextView year_edit_text = getActivity().findViewById(R.id.yearEditText);

            day_edit_text.setText(String.valueOf(day));
            month_edit_text.setText(String.valueOf(month+1));
            year_edit_text.setText(String.valueOf(year));
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        isActive = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isActive = false;
    }
}
