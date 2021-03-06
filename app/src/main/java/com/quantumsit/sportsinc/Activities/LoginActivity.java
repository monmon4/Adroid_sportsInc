package com.quantumsit.sportsinc.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    GlobalVars globalVars;
    ImageButton showpass_button;
    boolean showpass = false;

    TextView forgetPassword;

    EditText mail_edittext, pass_edittext;
    String mail, pass;

    String received_pass, received_mail, received_name, received_imgUrl
            ,received_date_of_birth, received_phone;
    int received_id, received_gender, received_type;

    ProgressDialog progressDialog;

    boolean all_good;
    private LinearLayout login_ll;
    private Dialog customView;
/*
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    CallbackManager callbackManager;


    LoginButton fbLoginButton;
    SignInButton googleSignInButton;
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("");

        setTitleColor(getResources().getColor(R.color.colorLogoRed));

        globalVars = (GlobalVars) getApplication();

        showpass_button = findViewById(R.id.shopassImageButton_login);
        login_ll = findViewById(R.id.ll_login);
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.login_configure));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        mail_edittext = findViewById(R.id.mailEditText_login);
        pass_edittext = findViewById(R.id.passEditText_login);

        forgetPassword = findViewById(R.id.forgetpassTextView_login);
       // forgetPassword.setPaintFlags(forgetPassword.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mail = mail_edittext.getText().toString();
                if (mail.equals(""))
                    show_toast(getString(R.string.requiredField));
                else
                    checkMail();
            }
        });

        showpass_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(showpass) {
                    showpass_button.setBackgroundResource(R.drawable.ic_see_password_faded);
                    showpass = false;
                    pass_edittext.setTransformationMethod(PasswordTransformationMethod.getInstance());

                } else {
                    showpass_button.setBackgroundResource(R.drawable.ic_see_password);
                    showpass = true;
                    pass_edittext.setTransformationMethod(null);
                    pass_edittext.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void checkMail() {
        if (!checkConnection()){
            show_toast("No internet connection, try again...");
            return;
        }

        JSONObject where_info = new JSONObject();

        try {
            where_info.put(getString(R.string.where_users_mail),mail);
            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.selectData);
            HashMap<String,String> params = new HashMap<>();
            params.put(getString(R.string.parameter_table),getString(R.string.Table_Users));
            params.put(getString(R.string.parameter_where),where_info.toString());
            httpCall.setParams(params);
            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);

                    if (response == null) {
                        if (connectionTimeOut){
                            progressDialog.dismiss();
                            show_toast(getString(R.string.TimeOutMsg));
                            return;
                        }
                        show_toast(getString(R.string.mailNoFound));

                    } else {
                        try {
                            JSONObject result = response.getJSONObject(0);
                            received_id = result.getInt(getString(R.string.select_users_id));
                            received_name = result.getString(getString(R.string.select_users_name));
                            received_imgUrl = result.getString(getString(R.string.select_users_image));
                            received_gender= result.getInt(getString(R.string.select_users_gendar));
                            received_type = result.getInt(getString(R.string.select_users_type));
                            received_phone = result.getString(getString(R.string.select_users_phone));
                            received_date_of_birth = result.getString(getString(R.string.select_users_birthdate));
                            globalVars.settAll(received_name,received_imgUrl,received_phone,"",mail,
                                    received_id,received_type,received_gender,received_date_of_birth);
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
        if (!checkConnection()){
            show_toast("No internet connection, try again...");
            return;
        }
        progressDialog.dismiss();
        Random random_num = new Random();
        final int verfication_num = random_num.nextInt(9999 - 1000) + 1000;
       // Log.d("Verfication","Code: "+verfication_num);
        //verification_msg = "" + verfication_num;

        customView = new Dialog(LoginActivity.this);
        customView.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customView.setContentView(R.layout.window_verficationcode_layout);
        customView.setCanceledOnTouchOutside(false);

        final EditText verify_edit_text =  customView.findViewById(R.id.verficationEditText_verify);
        Button done_button =  customView.findViewById(R.id.doneButton_verify);
        verify_edit_text.setEnabled(true);

        done_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String verifcation = verify_edit_text.getText().toString().trim();

                if (verifcation.equals(String.valueOf(verfication_num))){
                    customView.dismiss();
                    newPasswordWindow();
                } else {
                    show_toast(getString(R.string.VerifyWrong));
                }

            }
        } );


        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.POST);
        httpCall.setUrl(Constants.sendMail);
        HashMap<String,String> params = new HashMap<>();
        params.put("email",mail);
        params.put("code",String.valueOf(verfication_num));
        httpCall.setParams(params);

        new HttpRequest(){
            @Override
            public void onResponse(JSONArray response) {
                super.onResponse(response);
                if(response != null){
                    show_toast("Code has been sent");

                } else {
                    show_toast("An error has occurred");
                    customView.dismiss();
                }

            }
        }.execute(httpCall);

        customView.show();
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
                        if (!TextUtils.isEmpty(NewPassword.getText())){
                            if (!pass.equals(Confir)){
                                show_toast("The confirm password is incorrect.");
                                return;
                            }
                            updatePassword(pass,alertdialog);
                        }
                        else {
                            show_toast("please enter the new password.");
                        }
                    }
                });
            }
        });
        alertdialog.show();
    }

    private void updatePassword(final String pass, final AlertDialog alertdialog) {
        try {
            if (!checkConnection()){
                show_toast("No internet connection, try again...");
                return;
            }
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

        //Intent intent = new Intent(LoginActivity.this, BookingFirstFormActivity.class);
        //startActivity(intent);
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

        if (mail.equals("") ){
            show_toast(getString(R.string.MailMissing));

        } else if (pass.equals(""))
            show_toast(getString(R.string.PassMissing));
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
                where_info.put(getString(R.string.where_users_mail),mail);

                HttpCall httpCall = new HttpCall();
                httpCall.setMethodtype(HttpCall.POST);
                httpCall.setUrl(Constants.selectData);
                HashMap<String,String> params = new HashMap<>();
                params.put(getString(R.string.parameter_table),getString(R.string.Table_Users));
                params.put(getString(R.string.parameter_where),where_info.toString());

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
                                received_pass = result.getString(getString(R.string.select_users_pass));
                                int availableUser = result.getInt("deactive");

                                if (availableUser == 1){
                                    show_toast("this account is an deactivated account...");
                                    return;
                                }

                                if (received_pass.equals(pass)) {
                                    all_good = true;
                                    received_id = result.getInt(getString(R.string.select_users_id));
                                    received_name = result.getString(getString(R.string.select_users_name));
                                    received_imgUrl = result.getString(getString(R.string.select_users_image));
                                    received_gender= result.getInt(getString(R.string.select_users_gendar));
                                    received_type = result.getInt(getString(R.string.select_users_type));
                                    received_phone = result.getString(getString(R.string.select_users_phone));
                                    received_date_of_birth = result.getString(getString(R.string.select_users_birthdate));
                                    ActiveUser();
                                } else {
                                    progressDialog.dismiss();
                                    show_toast(getString(R.string.wrongPass));
                                }

                            } else {
                                if (connectionTimeOut){
                                    progressDialog.dismiss();
                                    show_toast(getString(R.string.TimeOutMsg));
                                    return;
                                }
                                progressDialog.dismiss();
                                show_toast(getString(R.string.mailNoFound));
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
            Log.d("WhereID",String.valueOf(received_id));
            where_info.put("id",received_id);

            JSONObject values = new JSONObject();
            values.put(getString(R.string.where_user_active),1);
            if (!user_token.equals(""))
                values.put(getString(R.string.where_user_token),user_token);
            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.updateData);
            HashMap<String,String> params = new HashMap<>();
            params.put(getString(R.string.parameter_table),getString(R.string.Table_Users));
            params.put(getString(R.string.parameter_values),values.toString());
            params.put(getString(R.string.parameter_where),where_info.toString());

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

        globalVars.settAll(received_name,received_imgUrl, received_phone, pass, mail,
                            received_id, received_type, received_gender, received_date_of_birth);

        UserEntity userEntity = new UserEntity(received_name,received_imgUrl, received_phone,pass, mail,
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
        progressDialog.dismiss();
        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(LoginActivity.this , startPageActivity.class));
        finish();
    }
}
