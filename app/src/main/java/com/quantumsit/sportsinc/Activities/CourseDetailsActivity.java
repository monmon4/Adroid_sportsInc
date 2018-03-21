package com.quantumsit.sportsinc.Activities;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Adapters.ListViewExpandable_Adapter_CoursesDetails;
import com.quantumsit.sportsinc.CustomView.CustomLoadingView;
import com.quantumsit.sportsinc.Entities.CourseEntity;
import com.quantumsit.sportsinc.Entities.item1_courses_details;
import com.quantumsit.sportsinc.Entities.item2_courses_details;
import com.quantumsit.sportsinc.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class CourseDetailsActivity extends AppCompatActivity {
    TextView CourseName ,SessionsNum ,description ;
    TextView DurationOfSession;

    ImageView levelImage;

    ExpandableListView expandableListView;
    ListViewExpandable_Adapter_CoursesDetails adapter_coursesDetails;

    ArrayList<item1_courses_details> header_list;
    HashMap < Integer, item2_courses_details> child_list;



    CustomLoadingView loadingView;
    int loadingTime = 1200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //CourseName = findViewById(R.id.course_details_name);
        loadingView = findViewById(R.id.LoadingView);
        levelImage = findViewById(R.id.Course_icon);
        SessionsNum = findViewById(R.id.course_details_no_classes);
        expandableListView = findViewById(R.id.course_details_expandableListView);
        //CoursePrice = findViewById(R.id.course_details_price);
        description = findViewById(R.id.course_details_description);
        //startDate = findViewById(R.id.course_details_start_date);
        //endDate = findViewById(R.id.course_details_end_date);
        //CourseLevel = findViewById(R.id.course_details_level);


        header_list = new ArrayList<>();
        header_list.add(new item1_courses_details("firstClass", "15/2/2018", 1));
        header_list.add(new item1_courses_details("secondClass", "20/2/2018", 2));
        header_list.add(new item1_courses_details("thirdClass", "15/2/2018", 3));

        String[] days = {"Sat", "Mon"};
        String[] times = {"15:00", "13:00"};

        child_list = new HashMap<>();
        child_list.put(1, new item2_courses_details("Omar", days, times));
        child_list.put(2, new item2_courses_details("Omar", days, times));
        child_list.put(3, new item2_courses_details("Omar", days, times));

        adapter_coursesDetails = new ListViewExpandable_Adapter_CoursesDetails(CourseDetailsActivity.this, header_list, child_list);
        expandableListView.setAdapter(adapter_coursesDetails);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                setListViewHeight(parent, groupPosition);
                return false;
            }
        });

        //final CourseEntity myCourse = (CourseEntity) getIntent().getSerializableExtra("MyCourse");

        /*if (savedInstanceState!=null)
            loadingTime = 0;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(myCourse != null){
                    fillView(myCourse);
                }
                else
                    loadingView.fails(); }
        }, loadingTime);*/

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

    private void fillView(CourseEntity courseEntity) {
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
        //CoursePrice.setText(courseEntity.getPrice());
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        String date = df.format(courseEntity.getStartDate());
        //startDate.setText(date);
        date = df.format(courseEntity.getEndDate());
        //endDate.setText(date);
        //CourseLevel.setText(courseEntity.getLevel());
        description.setText(courseEntity.getDescription());
        loadingView.success();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
