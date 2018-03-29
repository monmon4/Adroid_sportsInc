package com.quantumsit.sportsinc.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Activities.PaymentActivity;
import com.quantumsit.sportsinc.Backend.Functions;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.Entities.BookingCourseEntity;
import com.quantumsit.sportsinc.Entities.CourseEntity;
import com.quantumsit.sportsinc.Entities.item1_courses_details;
import com.quantumsit.sportsinc.Entities.item2_courses_details;
import com.quantumsit.sportsinc.Entities.item_name_id;
import com.quantumsit.sportsinc.R;
import com.quantumsit.sportsinc.RegisterationForm_fragments.BookingFirstFormActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by Mona on 27-Dec-17.
 */

public class ListViewExpandable_Adapter_CoursesDetails extends BaseExpandableListAdapter {

    private GlobalVars globalVars;
    private Functions functions;
    public Context context;
    public List<item1_courses_details> header_list;
    public HashMap<Integer, item2_courses_details> child_hashmap;

    private CourseEntity courseEntity;
    private ArrayList<item_name_id> trainee_names;

    PopupWindow popup_window;
    private LinearLayout ll;

    public ListViewExpandable_Adapter_CoursesDetails(Context context, List<item1_courses_details> listDataHeader,
                                                     HashMap<Integer, item2_courses_details> listChildData, CourseEntity courseEntity, ArrayList<item_name_id> trainee_names) {
        this.context = context;
        this.header_list = listDataHeader;
        this.child_hashmap = listChildData;
        this.courseEntity = courseEntity;
        this.globalVars = (GlobalVars) context.getApplicationContext();
        this.functions = new Functions(context);
        this.trainee_names = trainee_names;
        check_enable();
    }



    public void setLl(LinearLayout ll) {
        this.ll = ll;
    }

    @Override
    public int getGroupCount() {return this.header_list.size();}

    @Override
    public int getChildrenCount(int groupPosition) {return 1;}

    @Override
    public long getGroupId(int groupPosition) {return groupPosition;}

    @Override
    public long getChildId(int groupPosition, int childPosition) {return childPosition;}

    @Override
    public Object getGroup(int groupPosition) {
        return this.header_list.get(groupPosition);
    }


    @Override
    public Object getChild(int groupPosition, int childPosititon) {

        return this.child_hashmap.get(this.header_list.get(groupPosition).getClass_id());
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        item1_courses_details  header = (item1_courses_details) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item1_courses_details, null);
        }

        TextView class_name_textview =  convertView.findViewById(R.id.classNameTextView_item1coursesdetails);
        TextView start_date_textview =  convertView.findViewById(R.id.dateTextView_item1coursesdetails);

        class_name_textview.setText(header.getClass_name());
        start_date_textview.setText(header.getStart_date());


        return convertView;
    }


    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        item2_courses_details child = (item2_courses_details) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item2_courses_details, null);
        }

        TextView coach_name_textview = convertView.findViewById(R.id.coachNameTextView_item2coursedetails);
        TextView days_textview = convertView.findViewById(R.id.daysTextView_item2coursedetails);
        TextView times_textview = convertView.findViewById(R.id.timesTextView_item2coursedetails);
        Button book_button =  convertView.findViewById(R.id.bookButton_item2coursedetails);

        book_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int type = globalVars.getType();
                if (trainee_names.size()!= 0 || globalVars.isParent()) {
                    open_popup(groupPosition);
                } else if ( type == 0 || type == 6 ) {
                    ArrayList <BookingCourseEntity> bookingCourseEntity = globalVars.getBookingCourseEntities();
                    if (bookingCourseEntity.size() == 0) {
                        globalVars.bookingCourseEntities.add(new BookingCourseEntity(globalVars.getName(),
                                String.valueOf(globalVars.getId()), courseEntity,
                                header_list.get(groupPosition).getClass_name(),
                                header_list.get(groupPosition).getClass_id())) ;
                    } else {
                        Toast.makeText(context, "You already booked in a level", Toast.LENGTH_SHORT).show();
                    }
                    context.startActivity(new Intent(context, PaymentActivity.class));

                }  else if (type == 5) {
                    globalVars.setClass_id(header_list.get(groupPosition).getClass_id());
                    globalVars.setCourse_id(courseEntity.getCourse_id());
                    context.startActivity(new Intent(context, BookingFirstFormActivity.class));
                }

            }
        });
        coach_name_textview.setText(child.getCoach_name());
        StringBuilder days = new StringBuilder();
        StringBuilder times = new StringBuilder();

        for (int i=0; i<child.getDay().length; i++) {
            days.append(child.getDay()[i]).append("\n");
            times.append(child.getTime()[i]).append("\n");
        }

        days_textview.setText(days.toString());
        times_textview.setText(times.toString());
        return convertView;
    }



    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private void open_popup (final int groupPosition){


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.window_booking,null);

        popup_window = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        if(Build.VERSION.SDK_INT>=21){
            popup_window.setElevation(5.0f);
        }

        TextView close_textView = customView.findViewById(R.id.textView12);
        TextView addTrainee_textView = customView.findViewById(R.id.addTraineeTextView_window);
        final ListView trainees_listView = customView.findViewById(R.id.traineeListView_window);
        ImageButton addBooking_button = customView.findViewById(R.id.addButton_window);
        Button checkout_button = customView.findViewById(R.id.checkoutButton_window);

        ListView_Adapter_single_checkbox listView_adapter;

        if(globalVars.getBookingCourseEntities().size()!=0) {
            for (int j=0;j<globalVars.getBookingCourseEntities().size();j++) {
                String[] trainees = globalVars.getBookingCourseEntities().get(j).getTrainee_name().split("\n");
                for (int i=0; i<trainee_names.size(); i++) {
                    for(int k=0; k<trainees.length; k++) {
                        if(trainees[k].equals(trainee_names.get(i).getName()))
                            trainee_names.remove(i);
                    }
                }
            }
        }

        listView_adapter = new ListView_Adapter_single_checkbox(context, trainee_names);
        trainees_listView.setAdapter(listView_adapter);

        checkout_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                boolean all_good = false;
                for(int i=0; i<trainee_names.size();i++){
                    if(trainee_names.get(i).isSelected()) {
                        insert_booking_to_global_var(groupPosition);
                        Intent intent = new Intent(context, PaymentActivity.class);
                        context.startActivity(intent);
                        popup_window.dismiss();
                        all_good = true;
                        i = trainee_names.size();
                    }
                }

                if (!all_good)
                    Toast.makeText(context, "No trainee is chosen", Toast.LENGTH_SHORT).show();


            }
        } );

        addTrainee_textView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                globalVars.setParent(true);
                Intent intent = new Intent(context, BookingFirstFormActivity.class);
                context.startActivity(intent);
                popup_window.dismiss();
            }
        } );

        addBooking_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){


                insert_booking_to_global_var(groupPosition);
                //Intent intent = new Intent(context, CourseDetailsActivity.class);
                //intent.putExtra("MyCourse",courseEntity);
                //context.startActivity(intent);
                ((Activity) context).onBackPressed();
                popup_window.dismiss();
            }
        } );

        close_textView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                popup_window.dismiss();
            }
        } );

        popup_window.showAtLocation(ll, Gravity.CENTER,0,0);
        popup_window.setFocusable(true);
        popup_window.setOutsideTouchable(false);
        popup_window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popup_window.update();

    }



    private void insert_booking_to_global_var(int groupPosition){
        StringBuilder trainees_names_string = new StringBuilder();
        StringBuilder trainees_ids_string = new StringBuilder();
        for(int i=0; i<trainee_names.size(); i++) {
            if (trainee_names.get(i).isSelected()) {
                //insert_to_db(trainee_names.get(i).getId(),header_list.get(groupPosition).getClass_id() ,courseEntity.getCourse_id());
                trainees_names_string.append(trainee_names.get(i).getName()).append("\n");
                trainees_ids_string.append(trainee_names.get(i).getId()).append("@");
            }
        }
        if(!trainees_names_string.toString().equals("")){
            globalVars.bookingCourseEntities.add(new BookingCourseEntity(trainees_names_string.toString(),
                    trainees_ids_string.toString(),courseEntity,
                    header_list.get(groupPosition).getClass_name(),
                    header_list.get(groupPosition).getClass_id()));
        }
    }

    private void check_enable() {
        if(trainee_names!= null){
            if(trainee_names.size()!=0){
                for(int i=0; i<trainee_names.size(); i++){
                    check_course(trainee_names.get(i).getId(), courseEntity.getCourse_id());
                }
            }
        }

    }

    public void check_course(final int user_id, int level_id) {

        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.POST);
        httpCall.setUrl(Constants.course_of_trainee);
        HashMap<String,String> params = new HashMap<>();
        params.put("user_id",String.valueOf(user_id));
        params.put("level_id",String.valueOf(level_id));
        httpCall.setParams(params);
        new HttpRequest(){
            @Override
            public void onResponse(JSONArray response) {
                super.onResponse(response);
                if (response!= null) {
                    try {
                        JSONObject result = response.getJSONObject(0);
                        if (!result.getBoolean("enabled"))
                            remove_trainee(user_id);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.execute(httpCall);
    }

    private void remove_trainee(int user_id) {
        for (int i=0; i<trainee_names.size(); i++){
            if(trainee_names.get(i).getId() == user_id)
                trainee_names.remove(i);
        }
    }
}
