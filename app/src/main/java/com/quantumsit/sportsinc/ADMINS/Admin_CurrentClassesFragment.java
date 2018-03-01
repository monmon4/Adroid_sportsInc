package com.quantumsit.sportsinc.ADMINS;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Aaa_data.Rule_info;
import com.quantumsit.sportsinc.Aaa_data.Trainees_info;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.CustomView.myCustomExpandableListView;
import com.quantumsit.sportsinc.CustomView.myCustomExpandableListViewListener;
import com.quantumsit.sportsinc.R;
import com.quantumsit.sportsinc.util.ConnectionUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class Admin_CurrentClassesFragment extends Fragment {

    private static final String TAG = Admin_CurrentClassesFragment.class.getSimpleName();
    PopupWindow popupWindow;
    ProgressDialog progressDialog;

    myCustomExpandableListView customExpandableListView;
    myCustomExpandableListViewListener listener;
    int limitValue,currentStart;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ExpandableListView expandableListView;
    ListViewExpandable_Adapter_currentClasses expandableListView_adapter;

    List<item_current_classes> list_headers;
    HashMap<Integer, List<String>> hash_children;
    List<String> list_children;

    GlobalVars globalVars;
    View root;
    int CurrentPosition = 0;

    Date current_time;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_admin__current_classes, container, false);
        globalVars = (GlobalVars) getActivity().getApplication();
        limitValue = getResources().getInteger(R.integer.selectLimit);
        currentStart = 0;

        current_time = Calendar.getInstance().getTime();

        progressDialog = new ProgressDialog(getContext());
        mSwipeRefreshLayout = root.findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentStart = 0;
                initializeCurrentClasses(false);
            }
        });
        customExpandableListView = root.findViewById(R.id.customExpandableListView);
        customExpandableListView.setmEmptyView(R.drawable.ic_assignment,R.string.no_classes);

        customExpandableListView.setOnRetryClick(new myCustomExpandableListView.OnRetryClick() {
            @Override
            public void onRetry() {
                currentStart = 0;
                initializeCurrentClasses(false);
            }
        });
        expandableListView = customExpandableListView.getExpandableListView();
        listener = new myCustomExpandableListViewListener(expandableListView , mSwipeRefreshLayout) {
            @Override
            public void loadMoreData() {
                if (list_headers.size() >= limitValue)
                    listLoadMore();
            }
        };
        expandableListView.setOnScrollListener(listener);

        list_headers = new ArrayList<>();
        list_children = new ArrayList<>();
        list_children.add(" ");
        hash_children = new HashMap<>();

        initializeCurrentClasses(false);

        expandableListView_adapter = new ListViewExpandable_Adapter_currentClasses(getContext(),Admin_CurrentClassesFragment.this, list_headers, hash_children );
        expandableListView.setAdapter(expandableListView_adapter);

        return root;
    }

    private void listLoadMore() {
        customExpandableListView.loadMore();
        currentStart = list_headers.size();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initializeCurrentClasses(true);
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

    @SuppressLint("StaticFieldLeak")
    private void initializeCurrentClasses(final boolean loadMore) {
        if (!isAdded()) {
            return;
        }
        if (!checkConnection()){
            customExpandableListView.retry();
            return;
        }
        try {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String Today = df.format(c.getTime());

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.admin_currentClasses);

            JSONObject limit_info = new JSONObject();
            limit_info.put("start", currentStart);
            limit_info.put("limit", limitValue);
            HashMap<String, String> params = new HashMap<>();
            params.put("id", String.valueOf(globalVars.getId()));
            params.put("date", Today);
            params.put("limit",limit_info.toString());

            httpCall.setParams(params);

            new HttpRequest() {
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    Log.d(TAG, String.valueOf(response));
                    fillAdapter(response , loadMore);
                }
            }.execute(httpCall);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void fillAdapter(JSONArray response , boolean loadMore) {
        mSwipeRefreshLayout.setRefreshing(false);
        if (!loadMore)
            list_headers.clear();

        if (response != null) {
            try {
                for (int i = 0; i < response.length(); i++) {
                    item_current_classes entity = new item_current_classes( response.getJSONObject(i));
                    list_headers.add(entity);
                }
                check_time();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        customExpandableListView.notifyChange(list_headers.size());
        expandableListView_adapter.notifyDataSetChanged();
        listener.setLoading(false);
    }

    private void check_time() {

        //ArrayList <String> list = new ArrayList<>();

        for (int i=0; i<list_headers.size(); i++){
            item_current_classes entity = list_headers.get(i);
            String startTime = entity.startTime;
            String endTime = entity.endTime;
            int status = entity.status;
            ArrayList <String> list_new_child = new ArrayList<>();

            double current_time_double = 0;
            double start_time_double = Double.valueOf(startTime.replace(":", "."));
            double end_time_double = Double.valueOf(endTime.replace(":", "."));
            current_time = Calendar.getInstance().getTime();
            DateFormat time_format = new SimpleDateFormat("hh:mm a");
            String time = time_format.format(current_time);
            String[] splitin_time = time.split(" ");

            current_time_double = Double.valueOf(splitin_time[0].replace(":", "."));
            if ( splitin_time[1].equals("PM") && current_time_double - 12 < 1) {
                current_time_double += 12.00;
            }

            if (status == 3) {
                if (start_time_double - current_time_double > 1.0) {
                    //list_children.clear();
                    list_new_child.add(String.valueOf(R.string.check_rules_and_attendance));
                    list_new_child.add(String.valueOf(R.string.postpone_class));
                    list_new_child.add(String.valueOf(R.string.cancel_class));
                } else if (start_time_double - current_time_double < 0.11) {
                    //list_children.clear();
                    list_new_child.add(String.valueOf(R.string.check_rules_and_attendance));
                    list_new_child.add(String.valueOf(R.string.start_class));
                } else {
                    list_new_child.add(String.valueOf(R.string.check_rules_and_attendance));
                }
            } else if (status == 0) {
                //list_children.clear();
                list_new_child.add(String.valueOf(R.string.end_class));
            } else {
                list_new_child.add("Up coming");
            }

            hash_children.put(entity.getId(),list_new_child);
        }
    }

    ////// me4 b el child position b l name eih!
    public void clickChildListener(int groupPosition ,String child){
        CurrentPosition = groupPosition;
        switch (child){
            case "Check rules and attendance":
                checkClass();
                break;
            case "Start session":
                startClass();
                break;
            case "Postpone session":
                postpondClass();
                break;
            case "Cancel session":
                writeNote(1);
                break;
            case "End session":
                endClass();
                break;

        }
    }

    private void checkClass() {
        Intent intent = new Intent(getContext(), AdminStartClassActivity.class);
        intent.putExtra("adminClass",list_headers.get(CurrentPosition));
        startActivity(intent);
    }



    int Year ,Month ,Day ,Hour ,Minute;

    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TimePickerDialog.OnTimeSetListener timeSetListener;

    private void writeNote(final int status){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.window_write_note_layout,null);

        popupWindow = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        if(Build.VERSION.SDK_INT>=21){
            popupWindow.setElevation(5.0f);
        }

        final EditText note_edit_text =  customView.findViewById(R.id.noteEditText_notewindow);
        Button done_button =  customView.findViewById(R.id.doneButton_notewindow);

        done_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String note = note_edit_text.getText().toString();
                popupWindow.dismiss();
                switch (status){
                    case 1:
                        cancelClass(note);
                        break;
                    case 2:
                        insertPostponedClass(note);
                        break;
                }
            }
        } );

        RelativeLayout parentView = root.findViewById(R.id.adminCurrentClasses_rl);
        popupWindow.showAtLocation(parentView,Gravity.CENTER,0,0);
        popupWindow.setFocusable(true);
        note_edit_text.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.update();
    }

    private void postpondClass(){
        Calendar calendar = Calendar.getInstance();
        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);
        Hour = calendar.get(Calendar.HOUR_OF_DAY);
        Minute = calendar.get(Calendar.MINUTE);
        initialDateListener();
        initialTimeListener();
        myDatePicker();
    }

    private void initialDateListener() {
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Year = year;
                Month = month+1;
                Day = day;

                myTimePicker();
            }
        };
    }

    private void initialTimeListener() {
        timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                Hour = hour;
                Minute = minute;
                writeNote(2);
            }

        };
    }

    private void myDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), dateSetListener,Year,Month,Day);
        datePickerDialog.show();
    }

    private void myTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), timeSetListener ,Hour ,Minute ,true);
        timePickerDialog.show();
    }

    private Date showPostponedTime() {
        Calendar dateCal = Calendar.getInstance();
        dateCal.set(Calendar.YEAR,Year);
        dateCal.set(Calendar.MONTH,Month);
        dateCal.set(Calendar.DAY_OF_MONTH,Day);
        dateCal.set(Calendar.HOUR,Hour);
        dateCal.set(Calendar.MINUTE,Minute);

        return dateCal.getTime();
    }

    private void insertPostponedClass(final String notes){
        try {
            item_current_classes postponed_class = list_headers.get(CurrentPosition);
            Date PostponeDate = showPostponedTime();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String date = df.format(PostponeDate);

            df = new SimpleDateFormat("yyyy-MM-dd");
            String postponedDate = df.format(PostponeDate);
            df = new SimpleDateFormat("HH:mm");
            String postponedTime = df.format(PostponeDate);

            JSONObject values_info = new JSONObject();
            values_info.put("class_number",postponed_class.class_number);
            values_info.put("class_date",postponedDate);
            values_info.put("class_time",postponedTime);
            values_info.put("group_id",postponed_class.getGroup_id());
            values_info.put("status",3);

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.insertData);
            HashMap<String,String> params = new HashMap<>();
            params.put("table","classes");
            params.put("values",values_info.toString());

            httpCall.setParams(params);
            progressDialog.show();

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    if(checkResponse(response)){
                        try {
                            int id = response.getInt(0);
                            Log.d(TAG,String.valueOf(id));
                            savePostponedTime(id,notes);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else {
                    show_toast("Fail to postpone class...");
                    }
                }

            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void savePostponedTime(int class_postponed_id ,String note) {
        try {
            Date PostponeDate = showPostponedTime();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String date = df.format(PostponeDate);
            final String msg = list_headers.get(CurrentPosition).class_number+" has been postponed to\n\t"+date;
            df = new SimpleDateFormat("yyyy-MM-dd");
            String postponedDate = df.format(PostponeDate);
            df = new SimpleDateFormat("HH:mm");
            String postponedTime = df.format(PostponeDate);

            JSONObject values = new JSONObject();
            values.put("status",2);
            values.put("postpone_date",postponedDate);
            values.put("postpone_time",postponedTime);
            values.put("postponed_class_id",class_postponed_id);
            values.put("class_notes",note);

            JSONObject where = new JSONObject();
            where.put("id",list_headers.get(CurrentPosition).getId());

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.updateData);

            final HashMap<String,String> params = new HashMap<>();
            params.put("table","classes");
            params.put("notify","1");
            params.put("admin_id",String.valueOf(globalVars.getId()));
            params.put("where",where.toString());
            params.put("values",values.toString());

            httpCall.setParams(params);

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    if(checkResponse(response)) {
                        show_toast(msg);
                        list_headers.remove(CurrentPosition);
                        expandableListView_adapter.notifyDataSetChanged();
                    }else {
                        show_toast("Fail to postpone class...");
                    }
                    progressDialog.dismiss();
                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void cancelClass(String note) {

        try {
            JSONObject values = new JSONObject();
            values.put("status",1);
            values.put("class_notes",note);

            JSONObject where = new JSONObject();
            where.put("id",list_headers.get(CurrentPosition).getId());

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.updateData);
            final HashMap<String,String> params = new HashMap<>();
            params.put("table","classes");
            params.put("notify","1");
            params.put("admin_id",String.valueOf(globalVars.getId()));
            params.put("where",where.toString());
            params.put("values",values.toString());

            httpCall.setParams(params);

            progressDialog.show();
            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    if(checkResponse(response)) {
                        show_toast(list_headers.get(CurrentPosition).class_number+" has been canceled");
                        list_headers.remove(CurrentPosition);
                        expandableListView_adapter.notifyDataSetChanged();
                    }else {
                        show_toast("Fail To cancel class...");
                    }
                    progressDialog.dismiss();
                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




    @SuppressLint("StaticFieldLeak")
    private void startClass() {

        int class_id = list_headers.get(CurrentPosition).getId();
        Trainees_info coach_info = globalVars.getMyDB().getCoachInfo(class_id);
        Rule_info rule_info = globalVars.getMyDB().getRule(class_id);

        if (coach_info == null || rule_info == null) {
            show_toast("you need to check rules and coach's attendance first");
        } else if (!coach_info.getSelected()) {
            show_toast("Can't start session when coach's attendance isn't checked");
        } else if (!rule_info.getSelected()) {
            show_toast("Can't start session when rules isn't checked");
        } else {
            insertCoachAttendToDB(coach_info);
            insertRuleCheckToDB(rule_info);
            try {
                JSONObject where_info = new JSONObject();
                where_info.put("id",list_headers.get(CurrentPosition).getId());

                JSONObject values = new JSONObject();
                values.put("status",0);

                HttpCall httpCall = new HttpCall();
                httpCall.setMethodtype(HttpCall.POST);
                httpCall.setUrl(Constants.updateData);

                HashMap<String, String> params = new HashMap<>();
                params.put("table","classes");
                params.put("values",values.toString());
                params.put("where", where_info.toString());

                httpCall.setParams(params);

                new HttpRequest() {
                    @Override
                    public void onResponse(JSONArray response) {
                        super.onResponse(response);
                        if(checkResponse(response)) {
                            Toast.makeText(getActivity(),"Session has started",Toast.LENGTH_SHORT).show();
                            initializeCurrentClasses(false);

                        }else {
                            Toast.makeText(getActivity(), "Failed To start the session", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                }.execute(httpCall);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    @SuppressLint("StaticFieldLeak")
    private void endClass() {

        int class_id = list_headers.get(CurrentPosition).getId();
        Trainees_info coach_info = globalVars.getMyDB().getCoachInfo(class_id);
        Rule_info rule_info = globalVars.getMyDB().getRule(class_id);

        if (coach_info != null)
            globalVars.getMyDB().deleteClassTrainees(class_id);

        if (rule_info != null)
            globalVars.getMyDB().deleteClassRules(class_id);

        try {
            JSONObject values = new JSONObject();
            values.put("status",4);

            JSONObject where = new JSONObject();
            where.put("id",list_headers.get(CurrentPosition).getId());

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.updateData);
            final HashMap<String,String> params = new HashMap<>();
            params.put("table","classes");
            //params.put("notify","1");
            //params.put("admin_id",String.valueOf(globalVars.getId()));
            params.put("where",where.toString());
            params.put("values",values.toString());

            httpCall.setParams(params);

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    if(checkResponse(response)) {
                        show_toast(list_headers.get(CurrentPosition).class_number+" has been closed");
                        list_headers.remove(CurrentPosition);
                        expandableListView_adapter.notifyDataSetChanged();
                        //initializeCurrentClasses(false);
                        //onStart();
                    }else {
                        show_toast("Fail To end session...");
                    }
                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean checkResponse(JSONArray response) {
        if (response != null){
            try {
                String result = response.getString(0);
                if (!result.equals("ERROR"))
                    return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    @SuppressLint("StaticFieldLeak")
    private  void insertCoachAttendToDB(final Trainees_info info){
        try {

            JSONObject values = new JSONObject();
            values.put("class_id",info.getClass_id());
            values.put("user_id",info.getTrainee_id());
            values.put("attend",info.getTrainee_attend());
            values.put("notes",info.getTrainee_note());

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.insertData);

            HashMap<String, String> params = new HashMap<>();
            params.put("table","class_info");
            params.put("values",values.toString());

            httpCall.setParams(params);

            new HttpRequest() {
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    if(checkResponse(response)) {

                    }

                }
            }.execute(httpCall);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @SuppressLint("StaticFieldLeak")
    private  void insertRuleCheckToDB(final Rule_info info){
        try {

            JSONObject values = new JSONObject();
            values.put("class_id",info.getClass_id());
            values.put("user_id",info.getUser_id());
            values.put("rule_checked",info.getRule_check());
            values.put("note",info.getRule_note());

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.insertData);

            HashMap<String, String> params = new HashMap<>();
            params.put("table","class_rules");
            params.put("values",values.toString());

            httpCall.setParams(params);

            new HttpRequest() {
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    if(checkResponse(response)) {
                    }
                }
            }.execute(httpCall);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




    private void show_toast(String msg){
        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
    }


}
