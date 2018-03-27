package com.quantumsit.sportsinc.COACHES.Activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.COACHES.CurrentClassFragments.Coach_CurrentClassAttendanceFragment;
import com.quantumsit.sportsinc.COACHES.CurrentClassFragments.Coach_CurrentClassNoteFragment;
import com.quantumsit.sportsinc.R;

public class ActivityCurrentClass_coach extends AppCompatActivity {

    public static FragmentManager fragmentManager;
    private android.support.v7.app.ActionBar actionBar;

    GlobalVars global;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_current_class);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        global = (GlobalVars) getApplication();

        int position = getIntent().getIntExtra("position",0);

        actionBar = getSupportActionBar();

        if (savedInstanceState == null) {
            Fragment fragment = null;
            Class fragmentClass = null;
            if (position == 0)
                fragmentClass = Coach_CurrentClassAttendanceFragment.class;
            else
                fragmentClass = Coach_CurrentClassNoteFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.coach_current_class_content_frame, fragment).commit();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
