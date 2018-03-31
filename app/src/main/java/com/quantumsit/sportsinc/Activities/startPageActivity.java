package com.quantumsit.sportsinc.Activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.quantumsit.sportsinc.Adapters.ViewPagerAdapter;
import com.quantumsit.sportsinc.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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

        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.quantumsit.sportsinc", PackageManager.GET_SIGNATURES);
            for(Signature signature: info.signatures){
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("HashKey", Base64.encodeToString(md.digest(),Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

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

        RegisterDialogFragment myDialog = new RegisterDialogFragment();

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
