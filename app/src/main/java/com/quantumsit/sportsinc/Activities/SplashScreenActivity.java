package com.quantumsit.sportsinc.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Entities.UserEntity;
import com.quantumsit.sportsinc.R;
import com.quantumsit.sportsinc.util.ConnectionUtilities;

public class SplashScreenActivity extends AppCompatActivity {

    private static String TAG = SplashScreenActivity.class.getSimpleName();


    // Splash screen timer
    private static int SPLASH_TIME_OUT = 4500;

    //Intent
    private Intent intent;

    Animation anim;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        intent = new Intent(SplashScreenActivity.this,startPageActivity.class);
        checkConnection();
        initializeUser();

        image = findViewById(R.id.splashScreem_imageView);
        anim = AnimationUtils.loadAnimation(SplashScreenActivity.this, R.anim.splash_screen);
        image.setAnimation(anim);

        new Handler().postDelayed(new Runnable() {
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                checkConnection();
                startActivity(intent);
                finish();
                // close this activity
            }
        }, SPLASH_TIME_OUT);

    }

    private void initializeUser() {
        Gson gson = new Gson();
        SharedPreferences mPrefs = getSharedPreferences("UserFile", MODE_PRIVATE);
        String json = mPrefs.getString("CurrentUser", "");
        UserEntity userEntity = gson.fromJson(json, UserEntity.class);
        if (userEntity != null) {
            GlobalVars globalVars = (GlobalVars) getApplication();
            globalVars.setUser(userEntity);
            globalVars.setPerson_id(userEntity.getId());
            intent = new Intent(SplashScreenActivity.this,HomeActivity.class);
        }
    }

    private void checkConnection() {
        // first, check connectivity
        if (!ConnectionUtilities
                .checkInternetConnection(SplashScreenActivity.this)) {
            // as it seems there's no Internet connection
            // ask the user to activate it
            new AlertDialog.Builder(SplashScreenActivity.this)
                    .setTitle("Connection failed")
                    .setMessage("This application requires network access. Please, enable " +
                            "mobile network or Wi-Fi.")
                    .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // THIS IS WHAT YOU ARE DOING, Jul
                            SplashScreenActivity.this.startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), 0);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SplashScreenActivity.this.finish();
                        }
                    })
                    .show();
        }
    }
}
