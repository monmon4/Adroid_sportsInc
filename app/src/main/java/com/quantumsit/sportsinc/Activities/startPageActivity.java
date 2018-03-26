package com.quantumsit.sportsinc.Activities;

import android.app.Dialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
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
import com.quantumsit.sportsinc.CustomView.MyDialog;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Images = new ArrayList<>();
        Images.add(R.drawable.starthome1);
        Images.add(R.drawable.starthome2);

        viewpager =  findViewById(R.id.viewPager);
        indicator =  findViewById(R.id.indicator);

        fillViewPager();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimeTask() , 2000,4000);


    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void fillViewPager() {
        myAdapter = new ViewPagerAdapter(getApplicationContext(),Images);
        viewpager.setAdapter(myAdapter);
        indicator.setViewPager(viewpager);
    }


    public void signUp(View view){

        FragmentManager manager = getFragmentManager();

        MyDialog myDialog = new MyDialog();

        myDialog.show(manager, "My Dialog");

    }

    public void singIn(View view) {
        startActivity(new Intent(startPageActivity.this, LoginActivity.class));
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
