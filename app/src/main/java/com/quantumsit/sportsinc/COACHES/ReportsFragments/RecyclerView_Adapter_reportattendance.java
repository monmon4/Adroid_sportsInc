package com.quantumsit.sportsinc.COACHES.ReportsFragments;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.quantumsit.sportsinc.R;

import java.util.List;

/**
 * Created by Mona on 26-Dec-17.
 */

public class RecyclerView_Adapter_reportattendance extends RecyclerView.Adapter<RecyclerView_Adapter_reportattendance.ViewHolder>{


    private List<item_report_attendance> List_Item;
    private Context context;
    //ClientGlobal clientGlobal;


    public RecyclerView_Adapter_reportattendance(List<item_report_attendance> list_Item, Context context) {
        List_Item = list_Item;
        this.context = context;
        //clientGlobal = (ClientGlobal) context.getApplicationContext();
    }

    @Override
    public RecyclerView_Adapter_reportattendance.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coach_report_attendance, parent, false);
        RecyclerView_Adapter_reportattendance.ViewHolder viewHolder = new RecyclerView_Adapter_reportattendance.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView_Adapter_reportattendance.ViewHolder holder, final int position) {


        holder.course_name.setText(List_Item.get(position).course_name);
        holder.date.setText(List_Item.get(position).date);
        holder.class_number.setText(List_Item.get(position).class_number);

        int attend = List_Item.get(position).attend;
        if (attend == 0){
            holder.attended_image_button.setBackgroundResource(R.drawable.ic_not_checked);
        }
        else
            holder.attended_image_button.setBackgroundResource(R.drawable.ic_check_circle);
    }

    @Override
    public int getItemCount() {
        return (null != List_Item ? List_Item.size() : 0);
    }

    protected class ViewHolder extends RecyclerView.ViewHolder{
        private CardView attendance_card;
        private TextView course_name, date, class_number;
        private ImageButton attended_image_button;


        public ViewHolder(View view) {
            super(view);
            attendance_card =  view.findViewById(R.id.reportsAttendanceCoachCardView);
            course_name =  view.findViewById(R.id.courseNameTextView_coachreposrtattendanceitem);
            date =  view.findViewById(R.id.dateTextView_coachreposrtattendanceitem);
            class_number =  view.findViewById(R.id.classNumberTextView_coachreposrtattendanceitem);
            attended_image_button = view.findViewById(R.id.imageButton_coachreposrtattendanceitem);
        }

    }



}
