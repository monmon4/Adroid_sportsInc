package com.quantumsit.sportsinc.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.quantumsit.sportsinc.Aaa_data.Config;
import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.Entities.UserEntity;
import com.quantumsit.sportsinc.R;
import com.quantumsit.sportsinc.RegisterationForm_fragments.BookingFirstFormActivity;
import com.quantumsit.sportsinc.RegisterationForm_fragments.BookingFormActivity;
import com.quantumsit.sportsinc.util.ConnectionUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    CallbackManager callbackManager;


    LoginButton fbLoginButton;
    SignInButton googleSignInButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle(getResources().getString(R.string.log_in));

        setTitleColor(getResources().getColor(R.color.colorLogoRed));

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
                    show_toast(getString(R.string.requiredField));
                else
                    checkMail();
            }
        });


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        googleSignInButton = findViewById(R.id.ga_login_button);
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleAccountLogin();
            }
        });
        fbLoginButton = findViewById(R.id.fb_login_button);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        List< String > permissionNeeds = Arrays.asList("user_photos", "email",
                "user_birthday", "public_profile");
        fbLoginButton.setReadPermissions(permissionNeeds);
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
               // show_toast(loginResult.getAccessToken().getToken() );
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {@Override
                        public void onCompleted(JSONObject object,
                                                GraphResponse response) {

                            Log.i("FaceBookLoginActivity",
                                    response.toString());
                            try {
                                String id = object.getString("id");
                                try {
                                    URL profile_pic = new URL(
                                            "http://graph.facebook.com/" + id + "/picture?type=large");
                                    Log.i("profile_pic",
                                            profile_pic + "");

                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }
                                received_name = object.getString("name");
                                mail = object.getString("email");
                                String gender = object.getString("gender");
                                String birthday = object.getString("birthday");
                                socialMediaLogIn();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields",
                        "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                show_toast(getString(R.string.loginCanceled));
            }

            @Override
            public void onError(FacebookException error) {
                show_toast(getString(R.string.loginFail));
            }
        });
    }

    private void googleAccountLogin() {
        Toast.makeText(getApplicationContext(), R.string.GLogIn,Toast.LENGTH_LONG).show();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        else
            callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    private void googleSignOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount acct = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            if (acct != null) {
                String personName = acct.getDisplayName();
                //String personGivenName =acct.getGivenName();
                String personFamilyName = acct.getFamilyName();

                String personEmail = acct.getEmail();
                //String personId = acct.getId();
                //Uri personPhoto = acct.getPhotoUrl();
                received_name = personName+" "+personFamilyName;
                mail = personEmail;
                socialMediaLogIn();
            }
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("LogInActivity", "signInResult:failed code=" + e.getStatusCode());
            show_toast("Google LogIn Failed");
        }
    }

    private void socialMediaLogIn() {
        progressDialog.setMessage(getString(R.string.login));
        progressDialog.show();
        JSONObject where_info = new JSONObject();

        try {
            where_info.put("email",mail);
            SharedPreferences tokenPref = getSharedPreferences(Config.SHARED_PREF, MODE_PRIVATE);
            String user_token = tokenPref.getString(getString(R.string.Key_regID), "");
            JSONObject values = new JSONObject();
            values.put(getString(R.string.select_users_name),received_name);
            values.put(getString(R.string.select_users_type),5);
            values.put(getString(R.string.select_users_active),1);
            values.put(getString(R.string.select_users_mail),mail);
            values.put(getString(R.string.select_users_token),user_token);
            where_info.put(getString(R.string.where_users_mail),mail);

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.socialLogin);
            HashMap<String,String> params = new HashMap<>();
            params.put(getString(R.string.parameter_table),getString(R.string.Table_Users));
            params.put(getString(R.string.parameter_values),values.toString());
            params.put(getString(R.string.parameter_where),where_info.toString());
            httpCall.setParams(params);

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);

                    if (response == null) {
                        show_toast("Email does not exist");
                        show_toast(getString(R.string.loginError));

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
                            received_id = result.getInt(getString(R.string.select_users_id));
                            received_name = result.getString(getString(R.string.select_users_name));
                            received_imgUrl = result.getString(getString(R.string.select_users_image));
                            received_gender= result.getInt(getString(R.string.select_users_gendar));
                            received_type = result.getInt(getString(R.string.select_users_type));
                            received_phone = result.getString(getString(R.string.select_users_phone));
                            received_date_of_birth = result.getString(getString(R.string.select_users_birthdate));
                            SocialMediaLogOut();
                            go_to_home();
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

    private void SocialMediaLogOut() {
        googleSignOut();
        facebookSignOut();
    }

    private void facebookSignOut() {
        LoginManager.getInstance().logOut();
    }

    private void checkMail() {

        JSONObject where_info = new JSONObject();

        try {
            where_info.put(getString(R.string.where_users_mail),mail);
            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.selectData);
            HashMap<String,String> params = new HashMap<>();
            params.put(getString(R.string.parameter_table),getString(R.string.Table_Users));
            params.put(getString(R.string.parameter_where),where_info.toString());
            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);

                    if (response == null) {
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
        progressDialog.dismiss();
        Random random_num = new Random();
        final int verfication_num = random_num.nextInt(9999 - 1000) + 1000;
        Log.d("Verfication","Code: "+verfication_num);
        //verification_msg = "" + verfication_num;


        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.window_verficationcode_layout,null);

        final EditText verify_edit_text =  customView.findViewById(R.id.verficationEditText_verify);
        Button done_button =  customView.findViewById(R.id.doneButton_verify);
        verify_edit_text.setEnabled(true);

        verfication_popup_window.showAtLocation(login_ll, Gravity.CENTER,0,0);
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
                    verfication_popup_window.dismiss();
                    newPasswordWindow();
                } else {
                    show_toast(getString(R.string.VerifyWrong));
                }

            }
        } );


        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.POST);
        httpCall.setUrl(Constants.sendSMS);
        HashMap<String,String> params = new HashMap<>();
        params.put("phone",getString(R.string.NoUseVerify));
        params.put("message",String.valueOf(verfication_num));
        httpCall.setParams(params);

        new HttpRequest(){
            @Override
            public void onResponse(JSONArray response) {
                super.onResponse(response);

                if(response != null){
                    show_toast(getString(R.string.VerifySent));

                } else {
                    show_toast(getString(R.string.ToastError));
                    verfication_popup_window.dismiss();
                }

            }
        }.execute(httpCall);
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

        Intent intent = new Intent(LoginActivity.this, BookingFirstFormActivity.class);
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
                where_info.put("email",mail);
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
            String user_token = tokenPref.getString(getString(R.string.Key_regID), "");

            JSONObject where_info = new JSONObject();
            where_info.put(getString(R.string.where_user_id),received_id);

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

}
