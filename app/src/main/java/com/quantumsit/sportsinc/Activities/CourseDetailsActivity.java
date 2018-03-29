package com.quantumsit.sportsinc.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Adapters.ListViewExpandable_Adapter_CoursesDetails;
import com.quantumsit.sportsinc.Backend.Functions;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.CustomView.CustomLoadingView;
import com.quantumsit.sportsinc.Entities.CourseEntity;
import com.quantumsit.sportsinc.Entities.item1_courses_details;
import com.quantumsit.sportsinc.Entities.item2_courses_details;
import com.quantumsit.sportsinc.Entities.item_name_id;
import com.quantumsit.sportsinc.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class CourseDetailsActivity extends AppCompatActivity {
    TextView SessionsNum ,description, durationOfSession, noClsses ;

    ImageView levelImage;

    ExpandableListView expandableListView;
    ListViewExpandable_Adapter_CoursesDetails adapter_coursesDetails;

    ArrayList<item1_courses_details> header_list;
    HashMap < Integer, item2_courses_details> child_list;

    Functions functions;
    GlobalVars globalVars;

    CustomLoadingView loadingView;
    int loadingTime = 1200;
    ProgressDialog progressDialog;
    ScrollView scrollView;
    LinearLayout ll;

    int itemMeasure = 0;
    private ArrayList<item_name_id> trainee_names;

    String[] weekDays = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Course details");

       // progressDialog = new ProgressDialog(CourseDetailsActivity.this);

        functions = new Functions(CourseDetailsActivity.this);
        globalVars = (GlobalVars) getApplication();

        scrollView = findViewById(R.id.scrollView);
        loadingView = findViewById(R.id.LoadingView);
        levelImage = findViewById(R.id.Course_icon);
        SessionsNum = findViewById(R.id.course_details_no_classes);
        expandableListView = findViewById(R.id.course_details_expandableListView);
        description = findViewById(R.id.course_details_description);
        durationOfSession = findViewById(R.id.course_details_session_duration);
        noClsses = findViewById(R.id.noClassesTextView_coursedetails);

        header_list = new ArrayList<>();
        child_list = new HashMap<>();
        trainee_names = new ArrayList<>();

        //expandableListView.setAdapter(adapter_coursesDetails);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                setListViewHeight(parent, groupPosition);
                return false;
            }
        });

        final CourseEntity myCourse = (CourseEntity) getIntent().getSerializableExtra("MyCourse");

        String ImageUrl = myCourse.getImageUrl();
        //fillImage(name,icon);
        if(!ImageUrl.equals("")) {
            Picasso.with(CourseDetailsActivity.this).load(Constants.others_host + ImageUrl).into(levelImage, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Log.d("Image Loading ","ERROR In Loading");
                }
            });
        }
        SessionsNum.setText(myCourse.getClasses_Num());
        durationOfSession.setText(myCourse.getClassDur());
        description.setText(myCourse.getDescription());


       // progressDialog.show();
       ll = findViewById(R.id.ll_coursesdetails);

        if (savedInstanceState!=null)
            fillViewBySavedInstanceState(savedInstanceState,myCourse);
        else
            checkParent(myCourse);
        //fill_list_view(myCourse);

    }

    private void setListViewHeight(ExpandableListView listView,
                                   int group) {
        ListViewExpandable_Adapter_CoursesDetails listAdapter = (ListViewExpandable_Adapter_CoursesDetails) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            itemMeasure = groupItem.getMeasuredHeight();
            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                    totalHeight += listItem.getMeasuredHeight();
                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;

        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private void setListViewHeight(int itemMeasure ,ExpandableListView listView) {
        ListViewExpandable_Adapter_CoursesDetails listAdapter = (ListViewExpandable_Adapter_CoursesDetails) listView.getExpandableListAdapter();
        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getGroupCount(); i++)
            totalHeight += itemMeasure;

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;

        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }

    private void fillView(CourseEntity courseEntity) {
        scrollView.scrollTo(0,0);
        // CourseName.setText(courseEntity.getCourseName());
        getSupportActionBar().setTitle(courseEntity.getCourseName());
        String ImageUrl = courseEntity.getImageUrl();
        if(!ImageUrl.equals("")) {
            Picasso.with(getApplicationContext()).load(Constants.others_host + ImageUrl).into(levelImage, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Log.d("Image Loading ", "ERROR In Loading");
                }
            });
        }
        SessionsNum.setText(courseEntity.getClasses_Num());
        durationOfSession.setText(courseEntity.getClassDur());
        description.setText(courseEntity.getDescription());
        loadingView.success();
    }

    public void fill_list_view(final CourseEntity myCourse) {
        header_list.clear();
        child_list.clear();
        JSONObject where_info = new JSONObject();
        try {
            where_info.put("groups.course_id", myCourse.getCourse_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String OnCondition = "groups.coach_id = users.id";
        String select = "groups.id AS groups_id, groups.name AS group_name, users.name AS user_name, groups.group_sdate," +
                "groups.days, groups.daystime";

        HttpCall httpCall = functions.joinDB("groups", "users", where_info, OnCondition, select);

        new HttpRequest(){
            @Override
            public void onResponse(JSONArray response) {
                super.onResponse(response);

                if(response != null){

                    try {
                    for (int i=0; i<response.length(); i++){

                            JSONObject result = response.getJSONObject(i);
                            int class_id = result.getInt("groups_id");
                            String class_name = result.getString("group_name");
                            String coach_name = result.getString("user_name");
                            String start_date = result.getString("group_sdate");
                            String[] days = get_days(result.getString("days"));

                            if( days.length!=0 ) {
                                String[] daystime = result.getString("daystime").split("@");
                                header_list.add(new item1_courses_details(class_name, start_date,class_id));
                                child_list.put(class_id, new item2_courses_details(coach_name, days, daystime));

                            }
                    }
                        noClsses.setVisibility(View.GONE);
                        expandableListView.setVisibility(View.VISIBLE);
                        adapter_coursesDetails.notifyDataSetChanged();
                        expandableListView.setAdapter(adapter_coursesDetails);
                        setListViewHeight(expandableListView, -1);
                       // progressDialog.dismiss();

                    } catch (JSONException e) {
                       // progressDialog.dismiss();
                        expandableListView.setVisibility(View.GONE);
                        e.printStackTrace();

                    }

                } else {
                    noClsses.setVisibility(View.VISIBLE);
                    expandableListView.setVisibility(View.GONE);
                    //progressDialog.dismiss();
                    //checkMail();
                    //verfication();
                }
                fillView(myCourse);
            }
        }.execute(httpCall);


    }

    private String[] get_days(String dayss) {
        String[] dayss_split;

        if(dayss != null && !dayss.equals("")) {
            dayss_split = dayss.split("@");
            for (int i=0; i<dayss_split.length; i++) {
                dayss_split[i] = weekDays[Integer.valueOf(dayss_split[i])];
            }
        } else  {
            dayss_split = new String[]{" "};
        }

        return dayss_split;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @SuppressLint("StaticFieldLeak")
    public void check_course(final CourseEntity myCourse) {

        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.POST);
        httpCall.setUrl(Constants.course_of_trainee);
        HashMap<String,String> params = new HashMap<>();
        params.put("user_id",String.valueOf(globalVars.getId()));
        params.put("level_id",String.valueOf(myCourse.getCourse_id()));

        httpCall.setParams(params);
        new HttpRequest(){
            @Override
            public void onResponse(JSONArray response) {
                super.onResponse(response);

                if (response!= null){
                    if(response.length()>0) {
                        try {
                            JSONObject result = response.getJSONObject(0);
                            String msg;
                            if (result.getBoolean("enabled"))
                                fill_list_view(myCourse);
                            else {
                                msg = result.getString("bookedReason");
                                if (msg.equals(""))
                                    msg = result.getString("levelReason");
                                disable_classes(msg);
                                fillView(myCourse);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        fill_list_view(myCourse);
                    }
                }else {
                    fill_list_view(myCourse);
                }

            }
        }.execute(httpCall);
    }

    private  void disable_classes(String msg){

        noClsses.setVisibility(View.VISIBLE);
        expandableListView.setVisibility(View.GONE);
        noClsses.setText(msg);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("header_list",header_list);
        outState.putSerializable("child_list",child_list);
        outState.putInt("itemMeasure",itemMeasure);
    }

    private void  fillViewBySavedInstanceState(Bundle savedInstanceState , CourseEntity myCourse){
        ArrayList<item1_courses_details> new_header_list = (ArrayList<item1_courses_details>) savedInstanceState.getSerializable("header_list");
        HashMap<Integer, item2_courses_details> new_child_list = (HashMap<Integer, item2_courses_details>) savedInstanceState.getSerializable("child_list");
        itemMeasure = savedInstanceState.getInt("itemMeasure");
        header_list.addAll(new_header_list);
        child_list.putAll(new_child_list);
        adapter_coursesDetails.notifyDataSetChanged();
        expandableListView.setAdapter(adapter_coursesDetails);
        if (header_list.size() > 0) {
            noClsses.setVisibility(View.GONE);
            expandableListView.setVisibility(View.VISIBLE);
            setListViewHeight(itemMeasure,expandableListView);
        }
        else {
            noClsses.setVisibility(View.VISIBLE);
            expandableListView.setVisibility(View.GONE);
        }
        fillView(myCourse);
    }

    @SuppressLint("StaticFieldLeak")
    private void checkParent(final CourseEntity myCourse){

        try {
            JSONObject where_info = new JSONObject();
            where_info.put("parent_id", globalVars.getId());
            HttpCall httpCall = functions.searchDB("users", where_info);
            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    if(response != null){
                        try {
                            if(globalVars.getType() != 5) {
                                trainee_names.add(new item_name_id(globalVars.getId(), "Me"));
                            }
                            for (int i=0; i<response.length(); i++){
                                JSONObject result = response.getJSONObject(i);
                                int id = result.getInt("id");
                                String name = result.getString("name");
                                trainee_names.add(new item_name_id(id, name));
                            }
                            globalVars.setParent(true);
                            adapter_coursesDetails = new ListViewExpandable_Adapter_CoursesDetails(CourseDetailsActivity.this, header_list, child_list, myCourse, trainee_names);
                            adapter_coursesDetails.setLl(ll);
                            fill_list_view(myCourse);

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    } else {
                        globalVars.setParent(false);
                        adapter_coursesDetails = new ListViewExpandable_Adapter_CoursesDetails(CourseDetailsActivity.this, header_list, child_list, myCourse, trainee_names);
                        adapter_coursesDetails.setLl(ll);
                        check_course(myCourse);
                        trainee_names.clear();
                    }

                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
