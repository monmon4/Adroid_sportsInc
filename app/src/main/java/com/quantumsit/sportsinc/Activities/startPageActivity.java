package com.quantumsit.sportsinc.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.quantumsit.sportsinc.Aaa_data.Config;
import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Adapters.ViewPagerAdapter;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.Entities.UserEntity;
import com.quantumsit.sportsinc.R;
import com.quantumsit.sportsinc.RegisterationForm_fragments.BookingFirstFormActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class startPageActivity extends AppCompatActivity {

    private ViewPager viewpager;
    private CircleIndicator indicator;
    private List<Integer> Images;
    private ViewPagerAdapter myAdapter;

    GlobalVars globalVars;
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    CallbackManager callbackManager;


    LoginButton fbLoginButton;
    SignInButton googleSignInButton;
    String received_pass, received_mail, received_name, received_imgUrl
            ,received_date_of_birth, received_phone;
    int received_id, received_gender, received_type;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        globalVars = (GlobalVars) getApplication();
        Images = new ArrayList<>();
        Images.add(R.drawable.starthome1);
        Images.add(R.drawable.starthome2);

        viewpager =  findViewById(R.id.viewPager);
        indicator =  findViewById(R.id.indicator);

        fillViewPager();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimeTask() , 2000,4000);
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        googleSignInButton = findViewById(R.id.ga_login_button);
        setGooglePlusButtonText(googleSignInButton , "Connect");
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
                                received_mail = object.getString("email");
                                String gender = object.getString("gender");
                                String birthday = object.getString("birthday");
                                //socialMediaLogIn();
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


    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                return;
            }
        }
    }

    public void show_toast(String msg){
       // progressDialog.dismiss();
        Toast.makeText(startPageActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                received_mail = personEmail;
                //socialMediaLogIn();
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
            where_info.put("email",received_mail);
            SharedPreferences tokenPref = getSharedPreferences(Config.SHARED_PREF, MODE_PRIVATE);
            String user_token = tokenPref.getString("regId", "");
            JSONObject values = new JSONObject();
            values.put(getString(R.string.select_users_name),received_name);
            values.put(getString(R.string.select_users_type),5);
            values.put(getString(R.string.select_users_active),1);
            values.put(getString(R.string.select_users_mail),received_mail);
            values.put(getString(R.string.select_users_token),user_token);
            where_info.put(getString(R.string.where_users_mail),received_mail);

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
                            received_mail = result.getString("email");
                            received_pass = result.getString("pass");
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


    private void go_to_home(){

        globalVars.settAll(received_name,received_imgUrl, received_phone, received_pass, received_mail,
                received_id, received_type, received_gender, received_date_of_birth);

        UserEntity userEntity = new UserEntity(received_name,received_imgUrl, received_phone,received_pass, received_mail,
                received_id, received_type, received_gender,received_date_of_birth);

        SharedPreferences.Editor preferences = getSharedPreferences("UserFile", MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(userEntity);
        preferences.putString("CurrentUser", json);
        preferences.apply();

        progressDialog.dismiss();
        Intent intent= new Intent(startPageActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();

    }

    private void fillViewPager() {
        myAdapter = new ViewPagerAdapter(getApplicationContext(),Images);
        viewpager.setAdapter(myAdapter);
        indicator.setViewPager(viewpager);
    }

    public void singIn(View view) {
        startActivity(new Intent(startPageActivity.this, LoginActivity.class));
        finish();
    }

    public void joinAcademy(View view) {
        startActivity(new Intent(startPageActivity.this, RegisterActivity.class));
        //startActivity(new Intent(startPageActivity.this, BookingFirstFormActivity.class));
        finish();
    }

    public class MyTimeTask extends TimerTask{

        @Override
        public void run() {
            int count = viewpager.getAdapter().getCount();
            int current = viewpager.getCurrentItem();
            current++;
            if (count == current) {
                current = 0;
            }
            final int finalCurrent = current;
            runOnUiThread(new Runnable() {
                public void run() {
                    viewpager.setCurrentItem(finalCurrent);
                }
            });
        }
    }
}
