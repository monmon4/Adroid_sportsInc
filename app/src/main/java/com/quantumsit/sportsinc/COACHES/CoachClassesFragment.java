package com.quantumsit.sportsinc.COACHES;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Aaa_data.MyClass_info;
import com.quantumsit.sportsinc.Aaa_data.Rule_info;
import com.quantumsit.sportsinc.Aaa_data.Trainees_info;
import com.quantumsit.sportsinc.Adapters.RunningClassesAdapter;
import com.quantumsit.sportsinc.Adapters.RunningClassesListAdapter;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.COACHES.ReportsFragments.item_reports_finished_courses;
import com.quantumsit.sportsinc.CustomView.myCustomExpandableListView;
import com.quantumsit.sportsinc.CustomView.myCustomExpandableListViewListener;
import com.quantumsit.sportsinc.R;
import com.quantumsit.sportsinc.util.ConnectionUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class CoachClassesFragment extends Fragment {

    private static String TAG = CoachClassesFragment.class.getSimpleName();

    GlobalVars globalVars;

    View root;
    PopupWindow popupWindow;
    myCustomExpandableListView customExpandableListView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ExpandableListView not_finished_courses_expandable_listview;

    ListViewExpandable_Adapter_NotFinishedCourses not_finished_courses_adapter;

    ArrayList<item2_notfinished_course_group> header_list;
    HashMap<Integer, List<item_finished_classes>> child_hashmap;

    FloatingActionButton current_class_button;
    List<MyClass_info> current_class;

    myCustomExpandableListViewListener listener;
    int limitValue,currentStart;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_coach_classes,container,false);

        globalVars = (GlobalVars) getActivity().getApplication();
        limitValue = getResources().getInteger(R.integer.selectLimit);
        currentStart = 0;

        mSwipeRefreshLayout = root.findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentStart = 0;
                initilizeRunningClass();
                initilizeFinishedList(false);
            }
        });
        customExpandableListView = root.findViewById(R.id.customExpandableListView);
        customExpandableListView.setmEmptyView(R.drawable.ic_assignment,R.string.no_finished);

        customExpandableListView.setOnRetryClick(new myCustomExpandableListView.OnRetryClick() {
            @Override
            public void onRetry() {
                initilizeRunningClass();
                currentStart = 0;
                initilizeFinishedList(false);
            }
        });
        not_finished_courses_expandable_listview = customExpandableListView.getExpandableListView();
        listener = new myCustomExpandableListViewListener(not_finished_courses_expandable_listview , mSwipeRefreshLayout) {
            @Override
            public void loadMoreData() {
                if (header_list.size() >= limitValue)
                    listLoadMore();
            }
        };
        not_finished_courses_expandable_listview.setOnScrollListener(listener);
        current_class_button = root.findViewById(R.id.currentClassFloatingActionButton);

        current_class_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current_class.size()>1){
                    initializePopUpWindow(root);
                    return;
                }
                Intent intent = new Intent(getActivity(), ActivityCurrentClass_coach.class);
                intent.putExtra("MyRunningClass",current_class.get(0));
                startActivity(intent);
            }
        });

        header_list = new ArrayList<>();
        child_hashmap = new HashMap<>();

        not_finished_courses_adapter = new ListViewExpandable_Adapter_NotFinishedCourses(getContext(), header_list, child_hashmap);
        not_finished_courses_expandable_listview.setAdapter(not_finished_courses_adapter);

        not_finished_courses_expandable_listview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent intent = new Intent(getActivity(), ActivityCourseSingleClass_coach.class);
                String course_name = header_list.get(groupPosition).getCourseName();
                String group_name = header_list.get(groupPosition).getGroupName();
                String pool_name = header_list.get(groupPosition).getPoolName();
                item_finished_classes myClass = child_hashmap.get(header_list.get(groupPosition).getGroup_id()).get(childPosition);

                intent.putExtra("courseName",course_name);
                intent.putExtra("groupName",group_name);
                intent.putExtra("poolName",pool_name);
                intent.putExtra("UserType",globalVars.getType());
                intent.putExtra("finishedClass",myClass);

                getActivity().startActivity(intent);

                return false;
            }
        });


        initilizeRunningClass();
        if (savedInstanceState == null)
            initilizeFinishedList(false);
        else
            fillBySavedState(savedInstanceState);

        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("ScrollPosition", not_finished_courses_expandable_listview.onSaveInstanceState());
        outState.putSerializable("CoursesList", header_list);
        outState.putSerializable("HashMap",child_hashmap);
    }


    private void fillBySavedState(Bundle savedInstanceState) {
        ArrayList<item2_notfinished_course_group>list1 = (ArrayList<item2_notfinished_course_group>) savedInstanceState.getSerializable("CoursesList");
        HashMap<Integer, List<item_finished_classes>>  myHashMap = (HashMap<Integer, List<item_finished_classes>> ) savedInstanceState.getSerializable("HashMap");
        header_list.addAll(list1);
        child_hashmap.putAll(myHashMap);
        Parcelable mListInstanceState = savedInstanceState.getParcelable("ScrollPosition");
        customExpandableListView.notifyChange(header_list.size());
        not_finished_courses_adapter.notifyDataSetChanged();
        not_finished_courses_expandable_listview.onRestoreInstanceState(mListInstanceState);
    }

    private void listLoadMore() {
        customExpandableListView.loadMore();
        currentStart = header_list.size();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initilizeFinishedList(true);
            }
        }, 1500);
    }

    private boolean checkConnection() {
        // first, check connectivity
        if (ConnectionUtilities
                .checkInternetConnection(getContext())) {
            return true;
        }
        return false;
    }


    private void initilizeFinishedList(final boolean loadMore) {
        if (!isAdded()) {
            return;
        }
        if (!checkConnection()){
            customExpandableListView.retry();
            return;
        }
        try {
            JSONObject where_info = new JSONObject();
            where_info.put("coach_id", globalVars.getId());

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.finished_classes);
            JSONObject limit_info = new JSONObject();
            limit_info.put("start", currentStart);
            limit_info.put("limit", limitValue);
            HashMap<String, String> params = new HashMap<>();
            params.put("where", where_info.toString());
            params.put("limit",limit_info.toString());

            httpCall.setParams(params);

            new HttpRequest() {
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    Log.d(TAG,String.valueOf(response));
                    fillAdapter(response , loadMore) ;
                }
            }.execute(httpCall);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillAdapter(JSONArray response , boolean loadMore) {
        mSwipeRefreshLayout.setRefreshing(false);
        if (!loadMore) {
            header_list.clear();
            child_hashmap.clear();
           /* for (int i=0;i<30;i++){

                item2_notfinished_course_group entity = new item2_notfinished_course_group("LEVEL "+i,"Group "+i,"pool 1" , (i+1),(i+10),3,4);
                header_list.add(entity);
                item_finished_classes finished_class = new item_finished_classes("class TEST","2019-11-22");
                if (child_hashmap.get(entity.getGroup_id())==null){
                    child_hashmap.put(entity.getGroup_id(),new ArrayList<item_finished_classes>());
                }
                child_hashmap.get(entity.getGroup_id()).add(finished_class);
            }
            customExpandableListView.notifyChange(header_list.size());
            not_finished_courses_adapter.notifyDataSetChanged();
            listener.setLoading(false);
            return;*/
        }
        if (response != null) {
            try {
                for (int i = 0; i < response.length(); i++) {
                    item2_notfinished_course_group entity = new item2_notfinished_course_group(response.getJSONObject(i));
                    item_finished_classes finished_class = new item_finished_classes(response.getJSONObject(i));
                    if (!header_list.contains(entity)) {
                        header_list.add(entity);
                    }
                    if (child_hashmap.get(entity.getGroup_id())==null){
                        child_hashmap.put(entity.getGroup_id(),new ArrayList<item_finished_classes>());
                    }
                    child_hashmap.get(entity.getGroup_id()).add(finished_class);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        customExpandableListView.notifyChange(header_list.size());
        not_finished_courses_adapter.notifyDataSetChanged();
        listener.setLoading(false);
    }

    private void initilizeRunningClass() {
        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.POST);
        httpCall.setUrl(Constants.coach_running_class);

        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String TodayDate = df.format(c.getTime());
        df = new SimpleDateFormat("HH:mm:ss");
        String TodayTime = df.format(c.getTime());

        HashMap<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(globalVars.getId()));
        params.put("date",TodayDate);
        params.put("time",TodayTime);

        httpCall.setParams(params);

        new HttpRequest() {
            @Override
            public void onResponse(JSONArray response) {
                super.onResponse(response);
                insertClassInSql(response);
            }
        }.execute(httpCall);

    }

    private void initializeAvailableClass() {
        List<MyClass_info> info = globalVars.getMyDB().getAllClasses();
        if (info.size() != 0){
            current_class = info;
            current_class_button.setVisibility(View.VISIBLE);
        }
    }

    private void initializePopUpWindow(View root){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.window_coach_classes_layout,null);

        ListView runningClasses  =  customView.findViewById(R.id.classes_listView);

        RunningClassesListAdapter arrayAdapter = new RunningClassesListAdapter(getContext(),R.layout.list_item_running_classes,current_class);
        runningClasses.setAdapter(arrayAdapter);

        popupWindow = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );


        final LinearLayout parentView = root.findViewById(R.id.coach_classes_Layout);
        popupWindow.showAtLocation(parentView, Gravity.CENTER,-20,-180);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        Drawable dim = new ColorDrawable(Color.BLACK);
        dim.setBounds(0, 0, parentView.getWidth(), parentView.getHeight());
        dim.setAlpha((int) (255 * 0.5f));

        ViewGroupOverlay overlay = parentView.getOverlay();
        overlay.add(dim);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ViewGroupOverlay overlay = parentView.getOverlay();
                overlay.clear();
            }
        });

        popupWindow.update();

       runningClasses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(),"I :"+i,Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), ActivityCurrentClass_coach.class);
                intent.putExtra("MyRunningClass",current_class.get(i));
                startActivity(intent);
            }
        });

    }
    private void insertClassInSql(JSONArray response) {
        if (response != null) {
            try {
                for (int i = 0; i < response.length(); i++) {
                    MyClass_info info = new MyClass_info(response.getJSONObject(i));
                    boolean inserted = globalVars.getMyDB().addClass(info);
                    if (inserted)
                        initializeClassInfo(info.getClass_id(),info.getGroup_id());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        initializeAvailableClass();
    }

    private void initializeClassInfo(int class_id, int group_id) {
        initializeClassRules(class_id);
        initializeClassTrainee(group_id,class_id);
    }

    private void initializeClassRules(final int class_id) {
        try {
            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.selectData);

            JSONObject where = new JSONObject();
            where.put("Type", 0);

            HashMap<String, String> params = new HashMap<>();
            params.put("table","rules");
            params.put("where",where.toString());

            httpCall.setParams(params);

            new HttpRequest() {
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    insertRulesInSql(response, class_id);
                }
            }.execute(httpCall);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void insertRulesInSql(JSONArray response, int class_id) {
        Log.d(TAG,"Rules"+String.valueOf(response));
        if (response != null) {
            try {
                for (int i = 0; i < response.length(); i++) {
                    Rule_info info = new Rule_info(response.getJSONObject(i),class_id);
                    globalVars.getMyDB().addRule(info);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void initializeClassTrainee(int group_id, final int class_id) {
        try {
            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.joinData);

            JSONObject where = new JSONObject();
            where.put("group_id", group_id);

            String onCondition = "group_trainee.trainee_id = users.id";
            HashMap<String, String> params = new HashMap<>();
            params.put("table1", "group_trainee");
            params.put("table2", "users");
            params.put("on", onCondition);
            params.put("where",where.toString());

            httpCall.setParams(params);

            new HttpRequest() {
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    insertTraineesInSql(response,class_id);
                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void insertTraineesInSql(JSONArray response, int class_id) {
        if (response != null) {
            try {
                for (int i = 0; i < response.length(); i++) {
                    Trainees_info info = new Trainees_info(response.getJSONObject(i),class_id);
                    globalVars.getMyDB().addTrainee(info);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
