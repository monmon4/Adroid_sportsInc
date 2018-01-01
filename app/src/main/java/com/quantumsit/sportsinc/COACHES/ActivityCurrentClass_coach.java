package com.quantumsit.sportsinc.COACHES;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.COACHES.CurrentClassFragments.Coach_CurrentClassFragment;
import com.quantumsit.sportsinc.R;

public class ActivityCurrentClass_coach extends AppCompatActivity {

    public static FragmentManager fragmentManager;
    private android.support.v7.app.ActionBar actionBar;

    GlobalVars global;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_current_class);

        global = (GlobalVars) getApplication();

        global.setCoach_currentclass_rules(true);
        global.setCoach_currentclass_attendance(true);
        global.setCoach_currentclass_scores(true);
        global.setCoach_currentclass_notes(true);

        actionBar = getSupportActionBar();

        if (savedInstanceState == null) {
            Fragment fragment = null;
            Class fragmentClass = null;
            fragmentClass = Coach_CurrentClassFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.coach_current_class_content_frame, fragment).commit();
        }

    }


}
