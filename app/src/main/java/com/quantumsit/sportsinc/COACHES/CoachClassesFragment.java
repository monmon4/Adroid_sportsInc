package com.quantumsit.sportsinc.COACHES;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Aaa_data.MyClass_info;
import com.quantumsit.sportsinc.Aaa_data.Rule_info;
import com.quantumsit.sportsinc.Aaa_data.Trainees_info;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.COACHES.ReportsFragments.item_reports_finished_courses;
import com.quantumsit.sportsinc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CoachClassesFragment extends Fragment {

    private static String TAG = CoachClassesFragment.class.getSimpleName();

    GlobalVars globalVars;

    ExpandableListView not_finished_courses_expandable_listview;

    ListViewExpandable_Adapter_NotFinishedCourses not_finished_courses_adapter;

    ArrayList<item2_notfinished_course_group> header_list;
    HashMap<Integer, List<item_finished_classes>> child_hashmap;

    FloatingActionButton current_class_button;
    MyClass_info current_class;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_coach_classes,container,false);

        globalVars = (GlobalVars) getActivity().getApplication();

        not_finished_courses_expandable_listview = root.findViewById(R.id.notFinishedCoursesExpandableListView_coachclasses);
        current_class_button = root.findViewById(R.id.currentClassFloatingActionButton);

        current_class_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityCurrentClass_coach.class);
                intent.putExtra("MyRunningClass",current_class);
                Toast.makeText(getContext(),"Class "+current_class.getClass_name() +" ,"+ current_class.getClass_id(),Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        header_list = new ArrayList<>();
        child_hashmap = new HashMap<>();

        initilizeRunningClass();
        initilizeFinishedList();

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

        return root;
    }

    private void initilizeFinishedList() {
        try {
            JSONObject where_info = new JSONObject();
            where_info.put("coach_id", globalVars.getId());

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.finished_classes);

            HashMap<String, String> params = new HashMap<>();
            params.put("where", where_info.toString());

            httpCall.setParams(params);

            new HttpRequest() {
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    Log.d(TAG,String.valueOf(response));
                    fillAdapter(response);
                }
            }.execute(httpCall);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillAdapter(JSONArray response) {
        header_list.clear();
        child_hashmap.clear();
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
        not_finished_courses_adapter.notifyDataSetChanged();
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

        initilizeAvailableClass();
    }

    private void initilizeAvailableClass() {
        List<MyClass_info> info = globalVars.getMyDB().getAllClasses();
        if (info.size() != 0){
            current_class = info.get(0);
            current_class_button.setVisibility(View.VISIBLE);
        }
    }

    private void insertClassInSql(JSONArray response) {
        /*Log.d(TAG,"Response: "+String.valueOf(response));
        S/tring Tables = globalVars.getMyDB().DBTablesName();
        Log.d(TAG ,"Tables: \n"+Tables);*/
        if (response != null) {
            try {
                for (int i = 0; i < response.length(); i++) {
                    MyClass_info info = new MyClass_info(response.getJSONObject(i));
                    initilizeClassInfo(info.getClass_id(),info.getGroup_id());
                    globalVars.getMyDB().addClass(info);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        List<MyClass_info> info = globalVars.getMyDB().getAllClasses();
       /* String x = "";
        for (int i=0;i<info.size();i++){
            x = info.get(i).getClass_name()+" "+info.get(i).getClass_id()+" "+info.get(i).getClass_date()+"\n";
        }
        Log.d(TAG,"SQL Result: \n\t"+ x);*/
    }

    private void initilizeClassInfo(int class_id, int group_id) {
        initilizeClassRules(class_id);
        initilizeClassTrainee(group_id,class_id);
    }

    private void initilizeClassRules(final int class_id) {
        try {
            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.selectData);

            JSONObject where = new JSONObject();
            where.put("Type", 0);

            HashMap<String, String> params = new HashMap<>();

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
    }

    private void initilizeClassTrainee(int group_id, final int class_id) {
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
