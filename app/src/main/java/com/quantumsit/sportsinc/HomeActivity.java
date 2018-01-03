package com.quantumsit.sportsinc;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.COACHES.CoachReportsAttendanceFragment;
import com.quantumsit.sportsinc.COACHES.CoachReportsFragment;
import com.quantumsit.sportsinc.COACHES.CoachRequestFragment;
import com.quantumsit.sportsinc.Side_menu_fragments.CertificatesFragment;
import com.quantumsit.sportsinc.Side_menu_fragments.ComplainsFragment;
import com.quantumsit.sportsinc.Side_menu_fragments.HomeFragment;
import com.quantumsit.sportsinc.Side_menu_fragments.MyClassesFragment;
import com.quantumsit.sportsinc.Side_menu_fragments.NotificationsFragment;
import com.quantumsit.sportsinc.Side_menu_fragments.ReportsFragment;
import com.quantumsit.sportsinc.Side_menu_fragments.RequestsFragment;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static FragmentManager fragmentManager;
    public NavigationView navigationView;
    private ActionBar actionBar;
    private DrawerLayout drawer;

    boolean doubleBackToExitPressedOnce = false;

    GlobalVars globalVars;

    boolean coach = false, parent= false, non_register = false, admin= false ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        globalVars = (GlobalVars) getApplication();
        int user = globalVars.getUser_is();
        if (user == 0) {
            parent = true;
        } else if (user == 1){
            coach = true;
        } else if (user == 2){
            admin = true;
        } else {
            non_register = true;
        }


        if (non_register){
            setContentView(R.layout.activity_home_nonregister);
        } else {
            setContentView(R.layout.activity_home);
        }


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        RelativeLayout header = (RelativeLayout) navigationView.getHeaderView(0);
        ImageView profileImage = header.findViewById(R.id.profile_image);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                drawer.closeDrawer(GravityCompat.START);
                startActivity(intent);
            }
        });
        actionBar = getSupportActionBar();

        if (savedInstanceState == null) {
            Fragment fragment = null;
            Class fragmentClass = null;
            fragmentClass = HomeFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(non_register){
            getMenuInflater().inflate(R.menu.join_now_button, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.join_now_button) {
            Intent intent = new Intent(HomeActivity.this, JoinNowActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }*/


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;
        Class fragmentClass = HomeFragment.class;

        if (id == R.id.nav_home) {
            actionBar.setTitle(R.string.app_name);
            fragmentClass = HomeFragment.class;
        } else if (id == R.id.nav_myClasses) {
            actionBar.setTitle(R.string.my_classes);
            fragmentClass = MyClassesFragment.class;
        } else if (id == R.id.nav_notifications) {
            actionBar.setTitle(R.string.notifications);
            fragmentClass = NotificationsFragment.class;
        } else if (id == R.id.nav_requests) {
            actionBar.setTitle(R.string.request);
            if (parent){
                fragmentClass = RequestsFragment.class;
            } else if (coach) {
                fragmentClass = CoachRequestFragment.class;
            }

        } else if (id == R.id.nav_complains) {
            actionBar.setTitle(R.string.complains);
            fragmentClass = ComplainsFragment.class;
        } else if (id == R.id.nav_reports) {
            actionBar.setTitle(R.string.reports);
            if (parent){
                fragmentClass = ReportsFragment.class;
            } else if (coach) {
                fragmentClass = CoachReportsFragment.class;
            }

        } else if (id == R.id.nav_certificates) {
            actionBar.setTitle(R.string.certificates);
            fragmentClass = CertificatesFragment.class;
        } else if (id == R.id.nav_website) {
            //link to the academy's page so opens a web page
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://thesportsinc.com/"));
            startActivity(browserIntent);
        } else if(id == R.id.nav_logout){
            //log out

            finish();
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
