package com.quantumsit.sportsinc;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

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
        register_rl = (RelativeLayout) findViewById(R.id.register_rl);

        name_edittext = (EditText) findViewById(R.id.nameEditText_register);
        phone_edittext = (EditText) findViewById(R.id.phoneEditText_register);
        mail_edittext = (EditText) findViewById(R.id.mailEditText_register);
        pass_edittext = (EditText) findViewById(R.id.passEditText_register);
        repass_edittext = (EditText) findViewById(R.id.repassEditText_register);

        day_edittext = (EditText) findViewById(R.id.dayEditText_register);
        month_edittext = (EditText) findViewById(R.id.monthEditText_register);
        year_edittext = (EditText) findViewById(R.id.yearEditText_register);

        gender_spinner = (Spinner) findViewById(R.id.genderSpinner_register);


        ArrayAdapter<CharSequence> gender_spinner_adapter = ArrayAdapter.createFromResource(this, R.array.Gender_array, android.R.layout.simple_spinner_item);
        gender_spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender_spinner.setAdapter(gender_spinner_adapter);

    }


    public void done_register(View view) {

        show_toast(gender);
        verfication();

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

            int gender_int;
            if (gender.equals("Male")){
                gender_int = 0;
            } else {
                gender_int = 1;
            }
            int current_year = Calendar.getInstance().get(Calendar.YEAR);
            int year = Integer.valueOf(year_of_birth);
            int age = current_year - year;


            JSONObject info = new JSONObject();
            try {
                info.put("name",user_name);
                info.put("phone",phone);
                info.put("email",mail);
                info.put("gender",gender_int);
                info.put("pass",pass);
                info.put("age",age);
                info.put("type",0);

                HttpCall httpCall = new HttpCall();
                httpCall.setMethodtype(HttpCall.POST);
                httpCall.setUrl(Constants.insertData);
                HashMap<String,String> params = new HashMap<>();
                params.put("table","users");
                params.put("values",info.toString());

                httpCall.setParams(params);

                new HttpRequest(){
                    @Override
                    public void onResponse(JSONObject response) {
                        super.onResponse(response);
                        String res = response.toString();
                        show_toast("res");
                    }
                }.execute(httpCall);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            verfication();
        }

    }

    private void verfication(){

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

        final EditText verify_edit_text = (EditText) customView.findViewById(R.id.verficationEditText_verify);
        Button done_button = (Button) customView.findViewById(R.id.doneButton_verify);
        verify_edit_text.setEnabled(true);

        done_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String verifcation = verify_edit_text.getText().toString();
                show_toast("Success" + verifcation);
                verfication_popup_window.dismiss();
                globalVars.setUser_is(4);
                Intent intent = new Intent(RegisterActivity.this , HomeActivity.class);
                startActivity(intent);
                finish();
            }
        } );

        verfication_popup_window.showAtLocation(register_rl, Gravity.CENTER,0,0);
        verfication_popup_window.setFocusable(true);
        verify_edit_text.setFocusable(true);
        verfication_popup_window.setOutsideTouchable(false);
        verfication_popup_window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        verfication_popup_window.update();
    }

    public void show_toast(String msg){
        Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
