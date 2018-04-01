package com.quantumsit.sportsinc.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.quantumsit.sportsinc.Aaa_data.Config;
import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Adapters.TraineeChildAdapter;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.COACHES.ReportsFragments.CoachReportsFragment;
import com.quantumsit.sportsinc.COACHES.RequestFragment.CoachRequestFragment;
import com.quantumsit.sportsinc.COACHES.RequestFragment.CoachRequestSentFragment;
import com.quantumsit.sportsinc.Entities.UserEntity;
import com.quantumsit.sportsinc.Side_menu_fragments.ContactUsFragment;
import com.quantumsit.sportsinc.R;
import com.quantumsit.sportsinc.Side_menu_fragments.AboutUsFragment;
import com.quantumsit.sportsinc.Side_menu_fragments.CertificatesFragment;
import com.quantumsit.sportsinc.Side_menu_fragments.HomeFragment;
import com.quantumsit.sportsinc.Side_menu_fragments.MyClassesFragment;
import com.quantumsit.sportsinc.Side_menu_fragments.NotificationsFragment;
import com.quantumsit.sportsinc.Side_menu_fragments.ReportsFragment;
import com.quantumsit.sportsinc.Side_menu_fragments.RequestsFragment;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static FragmentManager fragmentManager;
    public NavigationView navigationView;
    private ActionBar actionBar;
    private DrawerLayout drawer;

    boolean doubleBackToExitPressedOnce = false;

    GlobalVars globalVars;

    boolean coach = false, parent= false, non_register = false;//, admin= false ;
    private ImageView viewChild;
    private ListView childAccount;
    private boolean isPickerShown = false;
    private TextView userName;

    ArrayList<UserEntity> children;
    TraineeChildAdapter adapter;

    private int PROFILE_CODE = 7;
    private ImageView profileImage;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        globalVars = (GlobalVars) getApplication();
        int type = globalVars.getType();
        if(type == 5 || type==6) {
            non_register = true;
            parent = true;

            /*if (!checkRegistered()) {
                non_register = true;
            } else {
                globalVars.setType(0);
                parent = true;
                updateDB_type_to_trainee();
            }*/
        }else if (type == 0 ) {
            parent = true;
        }else if (type == 1) {
            parent = true;
            coach = true;
        }


        setContentView(R.layout.activity_home);



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawer =  findViewById(R.id.drawer_layout);

        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //////
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        toggle.setHomeAsUpIndicator(R.drawable.rsz_logo_white2);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu navigationMenu = navigationView.getMenu();

        if(non_register) {
            setSideMenu(globalVars.getType(), navigationMenu);
        }
        if (coach){
            setSideMenu(globalVars.getType(), navigationMenu);
        }

        RelativeLayout header = (RelativeLayout) navigationView.getHeaderView(0);
        profileImage = header.findViewById(R.id.profile_image);
        String ImageUrl = globalVars.getImgUrl();
        if (ImageUrl != null && !ImageUrl.equals("")){
            Picasso.with(getApplicationContext()).load(Constants.profile_host + ImageUrl).into(profileImage);
        }
        viewChild = header.findViewById(R.id.childView);
        childAccount = findViewById(R.id.childAccountList);
        userName = header.findViewById(R.id.user_name);
        //userPhone = header.findViewById(R.id.user_phone);
        userName.setText(globalVars.getName());
        //userPhone.setText(globalVars.getMail());

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               openProfile();
            }
        });

        userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProfile();
            }
        });

        /*userPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProfile();
            }
        });*/

        children = new ArrayList<>();
        adapter = new TraineeChildAdapter(getApplicationContext(),R.layout.list_item_trainee_child,children);
        childAccount.setAdapter(adapter);

        childAccount.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                userName.setText(children.get(position).getName());
                //userPhone.setText(children.get(position).getPhone());
                String ImageUrl = children.get(position).getImgUrl();
                if (ImageUrl != null && !ImageUrl.equals("")){
                    Picasso.with(getApplicationContext()).load(Constants.profile_host + ImageUrl).into(profileImage);
                }
                else
                    profileImage.setImageResource(R.mipmap.ic_profile_round);
                UserEntity Account = globalVars.getUser();
                globalVars.setUser(children.get(position));
                updateChildList(position, Account);
                toggleMenu();
            }
        });

        viewChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleMenu();
            }
        });

       getParentChildren();

        actionBar = getSupportActionBar();

        if (savedInstanceState == null) {
            Fragment fragment = null;
            Class fragmentClass = HomeFragment.class;
            int navigationPos = getIntent().getIntExtra(getString(R.string.Key_Home_Side),0);

            if(navigationPos == Config.NOTIFICATION_ID)
                fragmentClass = NotificationsFragment.class;

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PROFILE_CODE && resultCode == AppCompatActivity.RESULT_OK){
           // UserEntity userEntity = (UserEntity) data.getSerializableExtra("userUpdated");
           // Toast.makeText(getApplicationContext(),userEntity.getName(),Toast.LENGTH_LONG).show();
            userName.setText(globalVars.getName());
            //userPhone.setText(globalVars.getPhone());
            String ImageUrl = globalVars.getImgUrl();
            if (ImageUrl != null && !ImageUrl.equals("")){
                Picasso.with(getApplicationContext()).load(Constants.profile_host + ImageUrl).into(profileImage);
            }
            else
                profileImage.setImageResource(R.mipmap.ic_profile_round);
        }
    }

    private void toggleMenu() {
        if (!isPickerShown) {
            viewChild.setImageResource(R.drawable.ic_arrow_drop_up);
            setMenuItemsVisible(false);
            childAccount.setVisibility(View.VISIBLE);
            isPickerShown = true;
        } else {
            viewChild.setImageResource(R.drawable.ic_arrow_drop_down);
            setMenuItemsVisible(true);
            childAccount.setVisibility(View.GONE);
            isPickerShown = false;
        }
    }

    private void setMenuItemsVisible(boolean b) {
        Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); ++i) {
            menu.getItem(i).setVisible(b);
        }
        if (!parent)
            menu.findItem(R.id.nav_certificates).setVisible(false);
    }

    @SuppressLint("StaticFieldLeak")
    private void updateDB_type_to_trainee() {

        JSONObject where_info = new JSONObject();
        JSONObject values_info = new JSONObject();
        try {
            where_info.put(getString(R.string.where_id),globalVars.getId());
            values_info.put(getString(R.string.where_type), globalVars.getType());

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.selectData);
            HashMap<String,String> params = new HashMap<>();
            params.put(getString(R.string.parameter_table),getString(R.string.Table_Users));
            params.put(getString(R.string.parameter_where),where_info.toString());
            params.put(getString(R.string.parameter_values),values_info.toString());

            httpCall.setParams(params);

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);

                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private boolean checkRegistered() {

        final boolean[] check = {false};

        JSONObject where_info = new JSONObject();
        try {
            where_info.put(getString(R.string.where_user_id),globalVars.getId());

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.selectData);
            HashMap<String,String> params = new HashMap<>();
            params.put(getString(R.string.parameter_table),"info_trainee");
            params.put("where",where_info.toString());

            httpCall.setParams(params);

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);

                        if (response != null){
                            check[0] = true;
                        } else {
                            check[0] = false;
                        }


                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return check[0];
    }

    
    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                finishAffinity();
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
            if (coach)
                actionBar.setTitle(R.string.my_groups);
            else
                actionBar.setTitle(R.string.my_classes);
            fragmentClass = MyClassesFragment.class;
        } else if (id == R.id.nav_notifications) {
            actionBar.setTitle(R.string.notifications);
            fragmentClass = NotificationsFragment.class;
        } else if (id == R.id.nav_requests) {
            actionBar.setTitle(R.string.request);
            if (coach){
                fragmentClass = CoachRequestSentFragment.class;
            } else {
                fragmentClass = RequestsFragment.class;
            }

        }else if (id == R.id.nav_reports) {
            actionBar.setTitle(R.string.reports);
            if (coach){
                fragmentClass = CoachReportsFragment.class;
            } else{
                fragmentClass = ReportsFragment.class;
            }

        } else if (id == R.id.nav_certificates) {
            actionBar.setTitle(R.string.certificates);
            fragmentClass = CertificatesFragment.class;
        } /*else if (id == R.id.nav_website) {
            //link to the academy's page so opens a web page
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://thesportsinc.com/"));
            startActivity(browserIntent);
        }*/ else if(id == R.id.nav_logout){
            // LogOut From the System
            progressDialog = new ProgressDialog(HomeActivity.this);
            progressDialog.setMessage("Logging Out...");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            unActiveUser(globalVars.getId());
            return true;
        }  else if (id == R.id.nav_contact_us) {
            actionBar.setTitle(R.string.contact_us);
            fragmentClass = ContactUsFragment.class;
        }  else if (id == R.id.nav_about_us) {
            actionBar.setTitle(R.string.terms_amp_conditions);
            fragmentClass = AboutUsFragment.class;
        } else if (id == R.id.nav_booking) {
            actionBar.setTitle(R.string.booking);
            fragmentClass = PaymentFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();


        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void unActiveUser(int user_id) {
        try {
            JSONObject where_info = new JSONObject();
            where_info.put("id",user_id);

            JSONObject values = new JSONObject();
            values.put("active",0);

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.updateData);
            HashMap<String,String> params = new HashMap<>();
            params.put("table","users");
            params.put("values",values.toString());
            params.put("where",where_info.toString());

            httpCall.setParams(params);

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    logOut(response);
                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void logOut(JSONArray response) {
        if(response == null) {
            show_toast("fail to logout");
            return;
        }
        try {
            String result = String.valueOf(response.get(0));
            if (result.equals("ERROR")) {
                show_toast("fail to logout");
                return;
            }
            SharedPreferences.Editor preferences = getSharedPreferences("UserFile", MODE_PRIVATE).edit();
            preferences.clear();
            preferences.apply();
            startActivity(new Intent(HomeActivity.this, startPageActivity.class));
            progressDialog.dismiss();
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void show_toast(String msg) {
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }

    public void getParentChildren() {
        try {
            JSONObject where_info = new JSONObject();
            where_info.put("parent_id",globalVars.getPerson_id());

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.selectData);
            HashMap<String,String> params = new HashMap<>();
            params.put("table","users");
            params.put("where",where_info.toString());

            httpCall.setParams(params);

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    fillChildList(response);
                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillChildList(JSONArray response) {
        children.clear();
        Log.d("ChildList",String.valueOf(response));
        if (response != null){
            try {
                for (int i = 0; i < response.length(); i++) {
                    UserEntity entity = new UserEntity(response.getJSONObject(i));
                    children.add(entity);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapter.notifyDataSetChanged();
        if (children.size() > 0) {
            globalVars.setMyAccount(globalVars.getUser());
            viewChild.setVisibility(View.VISIBLE);
        }
        else
            viewChild.setVisibility(View.GONE);
    }

    private void updateChildList(int position , UserEntity myAccount){
        children.remove(position);
        if (globalVars.getPerson_id() == myAccount.getId())
            children.add(0,myAccount);
        else
            children.add(position,myAccount);
        adapter.notifyDataSetChanged();

    }

    private void openProfile (){
        Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
        drawer.closeDrawer(GravityCompat.START);
        startActivityForResult(intent ,PROFILE_CODE);
    }

    private void setSideMenu(int type, Menu navigationMenu){

        if(type == 5|| type == 6) {
            navigationMenu.findItem(R.id.nav_certificates).setVisible(false);
            navigationMenu.findItem(R.id.nav_reports).setVisible(false);
            navigationMenu.findItem(R.id.nav_requests).setVisible(false);
            navigationMenu.findItem(R.id.nav_notifications).setVisible(false);
            navigationMenu.findItem(R.id.nav_myClasses).setVisible(false);
        } else if(type == 1) {
            navigationMenu.findItem(R.id.nav_booking).setVisible(false);
            navigationMenu.findItem(R.id.nav_certificates).setVisible(false);
        }
    }
}
