package com.quantumsit.sportsinc.ADMINS;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.R;

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

        current_time = Calendar.getInstance().getTime();

        progressDialog = new ProgressDialog(getContext());
        expandableListView = root.findViewById(R.id.expandableListView_admincurrentclasses);

        list_headers = new ArrayList<>();
        list_children = new ArrayList<>();
        list_children.add(" ");
        hash_children = new HashMap<>();

        initializeCurrentClasses();

        expandableListView_adapter = new ListViewExpandable_Adapter_currentClasses(getContext(),Admin_CurrentClassesFragment.this, list_headers, hash_children );
        expandableListView.setAdapter(expandableListView_adapter);

        return root;
    }


    @SuppressLint("StaticFieldLeak")
    private void initializeCurrentClasses() {
        progressDialog.show();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String Today = df.format(c.getTime());

        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.POST);
        httpCall.setUrl(Constants.admin_currentClasses);

        HashMap<String, String> params = new HashMap<>();
        params.put("id",String.valueOf(globalVars.getId()));
        params.put("date",Today);

        httpCall.setParams(params);

        new HttpRequest() {
            @Override
            public void onResponse(JSONArray response) {
                super.onResponse(response);
                Log.d(TAG,String.valueOf(response));
                fillAdapter(response);
            }
        }.execute(httpCall);

    }

    private void fillAdapter(JSONArray response) {
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
    }

    private void check_time() {

        ArrayList <String> list = new ArrayList<>();

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
            if ( splitin_time[1].equals("PM") && current_time_double - 12 > 1) {
                current_time_double += 12.00;
            }

            if (status == 3) {
                if (start_time_double - current_time_double > 1.0) {
                    //list_children.clear();
                    list_new_child.add("Postpone class");
                    list_new_child.add("Cancel class");
                } else if (start_time_double - current_time_double < 0.11) {
                    //list_children.clear();
                    list_new_child.add("Start class");
                } else {
                    list_new_child.add("Up coming");
                }
            } else if (status == 0) {
                //list_children.clear();
                list_new_child.add("End class");
            } else {
                list_new_child.add("Up coming");
            }

            hash_children.put(entity.getId(),list_new_child);
        }

        expandableListView_adapter.notifyDataSetChanged();
        progressDialog.dismiss();
    }

    ////// me4 b el child position b l name eih!
    public void clickChildListener(int groupPosition ,String child){
        CurrentPosition = groupPosition;
        switch (child){
            case "Start class":
                startClass();
                break;
            case "Postpone class":
                postpondClass();
                break;
            case "Cancel class":
                writeNote(1);
                break;
            case "End class":
                endClass();
        }
    }

    private void startClass() {
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
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm");
            String date = df.format(PostponeDate);

            df = new SimpleDateFormat("yyyy-MM-dd");
            String postponedDate = df.format(PostponeDate);
            df = new SimpleDateFormat("hh:mm");
            String postponedTime = df.format(PostponeDate);

            JSONObject values_info = new JSONObject();
            values_info.put("class_number",postponed_class.getClass_number());
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

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    if(checkResponse(response)){
                        try {
                            int id = response.getInt(0);
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
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm");
            String date = df.format(PostponeDate);
            final String msg = list_headers.get(CurrentPosition).class_number+" has been postponed to\n\t"+date;
            df = new SimpleDateFormat("yyyy-MM-dd");
            String postponedDate = df.format(PostponeDate);
            df = new SimpleDateFormat("hh:mm");
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

            progressDialog.show();
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
    private void endClass() {

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
                        show_toast("Fail To end class...");
                    }
                    progressDialog.dismiss();
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
