package com.quantumsit.sportsinc.COACHES;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Aaa_data.MyClass_info;
import com.quantumsit.sportsinc.Aaa_data.Trainees_info;
import com.quantumsit.sportsinc.Adapters.ListViewExpandable_Adapter_NotFinishedCourses;
import com.quantumsit.sportsinc.Adapters.RunningClassesListAdapter;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.CustomView.myCustomExpandableListView;
import com.quantumsit.sportsinc.CustomView.myCustomExpandableListViewListener;
import com.quantumsit.sportsinc.Entities.item2_notfinished_course_group;
import com.quantumsit.sportsinc.R;
import com.quantumsit.sportsinc.util.ConnectionUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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
        /*
        * Fragment that Show the Coach Current Groups (the groups that have some finished classes but not all)
        *
        * show button for view today running classes(the classes of today that is started and the coach didn't finish entering it's info.)
        *
        * */

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

        /**
         *  the button that view running classes (not finish classes of the coach)
         *  if it's one class it open the class entities to edit it(class trainees and their scores)
         *  else show list of the classes
         */
        current_class_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow != null && popupWindow.isShowing()){
                    popupWindow.dismiss();
                    return;
                }
                if (current_class.size()>1){
                    initializePopUpWindow(root);
                    return;
                }
                Intent intent = new Intent(getActivity(), ActivityCurrentClass_coach.class);
                intent.putExtra(getString(R.string.Key_RunningClass),current_class.get(0));
                startActivity(intent);
            }
        });

        /*
        * Coach Current Groups show Compound with their Courses
        * */

        header_list = new ArrayList<>();
        child_hashmap = new HashMap<>();

        not_finished_courses_adapter = new ListViewExpandable_Adapter_NotFinishedCourses(getContext(), header_list, child_hashmap);
        not_finished_courses_expandable_listview.setAdapter(not_finished_courses_adapter);

        not_finished_courses_expandable_listview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            /*
            * on group click it view this Group details
            * */
            Intent intent = new Intent(getActivity(), ActivityCourseSingleClass_coach.class);
            String course_name = header_list.get(groupPosition).getCourseName();
            String group_name = header_list.get(groupPosition).getGroupName();
            String pool_name = header_list.get(groupPosition).getPoolName();
            item_finished_classes myClass = child_hashmap.get(header_list.get(groupPosition).getGroup_id()).get(childPosition);

            intent.putExtra(getString(R.string.Key_Course_name),course_name);
            intent.putExtra(getString(R.string.Key_Group_name),group_name);
            intent.putExtra(getString(R.string.Key_Pool_name),pool_name);
            intent.putExtra(getString(R.string.Key_UserType),globalVars.getType());
            intent.putExtra(getString(R.string.Key_FinishedClass),myClass);

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
        outState.putParcelable(getString(R.string.Key_Scroll_Posit), not_finished_courses_expandable_listview.onSaveInstanceState());
        outState.putSerializable(getString(R.string.Key_List_items), header_list);
        outState.putSerializable(getString(R.string.Key_expand_map),child_hashmap);
    }


    private void fillBySavedState(Bundle savedInstanceState) {
        ArrayList<item2_notfinished_course_group>list1 = (ArrayList<item2_notfinished_course_group>) savedInstanceState.getSerializable(getString(R.string.Key_List_items));
        HashMap<Integer, List<item_finished_classes>>  myHashMap = (HashMap<Integer, List<item_finished_classes>> ) savedInstanceState.getSerializable(getString(R.string.Key_expand_map));
        header_list.addAll(list1);
        child_hashmap.putAll(myHashMap);
        Parcelable mListInstanceState = savedInstanceState.getParcelable(getString(R.string.Key_Scroll_Posit));
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
        /*
        * Get the Current Groups From Server in this method
        * */
        if (!isAdded()) { // this check for stop load more while rotation to avoid crash
            return;
        }
        if (!checkConnection()){
            customExpandableListView.retry();
            return;
        }
        try {
            JSONObject where_info = new JSONObject();
            where_info.put(getString(R.string.where_coach_id), globalVars.getId());

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.finished_classes);
            JSONObject limit_info = new JSONObject();
            limit_info.put(getString(R.string.select_start), currentStart);
            limit_info.put(getString(R.string.select_limit), limitValue);
            HashMap<String, String> params = new HashMap<>();
            params.put(getString(R.string.parameter_where), where_info.toString());
           // params.put(getString(R.string.parameter_limit),limit_info.toString());
            params.put("where", where_info.toString());
            //params.put("limit",limit_info.toString());

            httpCall.setParams(params);

            new HttpRequest() {
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    fillAdapter(response , loadMore) ;
                }
            }.execute(httpCall);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillAdapter(JSONArray response , boolean loadMore) {
        /*
        * fill Current Group Expandable List from server Response
        * */
        mSwipeRefreshLayout.setRefreshing(false);
        if (!loadMore) {
            header_list.clear();
            child_hashmap.clear();
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
        //notify data change to view the data
        customExpandableListView.notifyChange(header_list.size());
        not_finished_courses_adapter.notifyDataSetChanged();
        //make The load more notify Finished to be able to load more
        listener.setLoading(false);
    }

    private void initilizeRunningClass() {
        /*
        * get the running classes(the classes of today that is started.) and it's trainees
        *
        * */
        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.POST);
        httpCall.setUrl(Constants.coach_running_class);

        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat(getString(R.string.server_dateFormate), Locale.ENGLISH);
        String TodayDate = df.format(c.getTime());
        df = new SimpleDateFormat(getString(R.string.server_timeFormate), Locale.ENGLISH);
        String TodayTime = df.format(c.getTime());
        Toast.makeText(getContext(),TodayDate,Toast.LENGTH_LONG).show();

        HashMap<String, String> params = new HashMap<>();
        params.put(getString(R.string.parameter_id), String.valueOf(globalVars.getId()));
        params.put(getString(R.string.parameter_date),TodayDate);
        params.put(getString(R.string.parameter_time),TodayTime);

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
        /*
        * get All Cached running classes in the phone
        * */
        List<MyClass_info> info = globalVars.getMyDB().getAllClasses();
        if (info.size() != 0){
            current_class = info;
            current_class_button.setVisibility(View.VISIBLE);
        }
    }

    private void initializePopUpWindow(View root){
        /*
        * Show All Available Classes in popWindow
        * */
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
        popupWindow.setOutsideTouchable(true);
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
                /*
                * Open Running Class information Details to view and edit it and can finish the class.
                * */
                Intent intent = new Intent(getActivity(), ActivityCurrentClass_coach.class);
                intent.putExtra(getString(R.string.Key_RunningClass),current_class.get(i));
                startActivity(intent);
            }
        });

    }
    private void insertClassInSql(JSONArray response) {
        /*
        * Cache the running Classes in the phone to edit it's info. local anyTime then
        * upload this info. to the Server in the end.
        * */
        if (response != null) {
            try {
                for (int i = 0; i < response.length(); i++) {
                    MyClass_info info = new MyClass_info(response.getJSONObject(i));
                    Log.d(TAG+"Running","Info."+i+" \n"+info.toString());
                    boolean inserted = globalVars.getMyDB().addClass(info);
                    if (inserted)
                        initializeClassTrainee(info.getGroup_id(),info.getClass_id());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        initializeAvailableClass();
    }

   /* private void initializeClassInfo(int class_id, int group_id) {
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
    }*/

    private void initializeClassTrainee(int group_id, final int class_id) {
        /*
        * get Class Trainee information to attend him and give him score
        * */
        try {
            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.joinData);

            JSONObject where = new JSONObject();
            where.put("group_id",group_id);

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
        /*
        * Cache Class Trainees Information
        * */
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
