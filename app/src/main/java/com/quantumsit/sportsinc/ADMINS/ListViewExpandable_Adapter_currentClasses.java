package com.quantumsit.sportsinc.ADMINS;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.quantumsit.sportsinc.AdminStartClassActivity;
import com.quantumsit.sportsinc.COACHES.ActivityCurrentClass_coach;
import com.quantumsit.sportsinc.COACHES.item2_notfinished_course_group;
import com.quantumsit.sportsinc.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


public class ListViewExpandable_Adapter_currentClasses extends BaseExpandableListAdapter {

    public Context context;
    public List<item_current_classes> header_list;
    public HashMap<String, List<String>> child_hashmap;
    int CurrentPosition;

    public ListViewExpandable_Adapter_currentClasses(Context context, List<item_current_classes> listDataHeader,
                                                     HashMap<String, List<String>> listChildData) {
        this.context = context;
        this.header_list = listDataHeader;
        this.child_hashmap = listChildData;
    }

    @Override
    public int getGroupCount() {return this.header_list.size();}

    @Override
    public int getChildrenCount(int groupPosition) {return this.child_hashmap.get(header_list.get(groupPosition).class_number).size();}


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

        return this.child_hashmap.get(this.header_list.get(groupPosition).class_number)
                .get(childPosititon);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        item_current_classes  header = (item_current_classes) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item1_admin_current_classes, null);
        }

        TextView class_number =  convertView.findViewById(R.id.classNumberTextView_adminCurrentClassesItem);
        TextView class_date =  convertView.findViewById(R.id.dateTextView_adminCurrentClassesItem);
        TextView class_time =  convertView.findViewById(R.id.timeTextView_adminCurrentClassesItem);

        class_number.setText(header.class_number);
        class_date.setText(header.class_date);
        class_time.setText(header.time);
        //class_text_view.setText(header.class_number);

        return convertView;
    }


    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String child = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item2_admin_current_classes, null);
        }

        TextView class_start = convertView.findViewById(R.id.class_adminCurrentClassesItem);
        ImageButton class_image = convertView.findViewById(R.id.imageButton_adminCurrentClassesItem);

        class_start.setText(child);

        if (childPosition == 0){
            class_image.setBackgroundResource(R.drawable.ic_class_swim);
        } else if (childPosition == 1) {
            class_image.setBackgroundResource(R.drawable.ic_class_time);
        } else {
            class_image.setBackgroundResource(R.drawable.ic_class_cancel);
        }


        class_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(child.equals("Start class")) {
                    Intent intent = new Intent(context, AdminStartClassActivity.class);
                    context.startActivity(intent);

                } else if (child.equals("Postpone class")){

                    CurrentPosition = groupPosition;
                    postpondClass();

                }else {

                }
            }
        });

        return convertView;
    }

    int Year ,Month ,Day ,Hour ,Minute;

    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TimePickerDialog.OnTimeSetListener timeSetListener;

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
    private void initialTimeListener() {
        timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                Hour = hour;
                Minute = minute;
                showSavedTime();
                savePostpondedTime();
            }

        };
    }

    private void showSavedTime() {
        Calendar dateCal = Calendar.getInstance();
        dateCal.set(Calendar.YEAR,Year);
        dateCal.set(Calendar.MONTH,Month);
        dateCal.set(Calendar.DAY_OF_MONTH,Day);
        dateCal.set(Calendar.HOUR,Hour);
        dateCal.set(Calendar.MINUTE,Minute);

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        String date = df.format(dateCal.getTime());
        Toast toast = Toast.makeText(context,""+header_list.get(CurrentPosition).class_number+" has been postponded to\n\t"+date,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();

        header_list.remove(CurrentPosition);

        notifyDataSetChanged();
    }

    private void savePostpondedTime() {
        //Save Data to server

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

    private void myTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, timeSetListener ,Hour ,Minute ,true);
        timePickerDialog.show();
    }

    private void myDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, dateSetListener,Year,Month,Day);
        datePickerDialog.show();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
