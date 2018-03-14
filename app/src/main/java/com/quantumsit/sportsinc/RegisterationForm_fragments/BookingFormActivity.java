package com.quantumsit.sportsinc.RegisterationForm_fragments;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.quantumsit.sportsinc.R;

public class BookingFormActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_form);

        bottomNavigation = findViewById(R.id.bottom_navigation_booking);

        bottomNavigation.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.action_step1:
                                selectedFragment = BookingForm_FirstFragment.newInstance();
                                break;
                            case R.id.action_step2:
                                selectedFragment = BookingForm_SecondFragment.newInstance();
                                break;
                            case R.id.action_step3:
                                selectedFragment = BookingForm_FirstFragment.newInstance();
                                break;
                            case R.id.action_step4:
                                selectedFragment = BookingForm_FirstFragment.newInstance();
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout_booking, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout_booking, BookingForm_FirstFragment.newInstance());
        transaction.commit();

    }
}
