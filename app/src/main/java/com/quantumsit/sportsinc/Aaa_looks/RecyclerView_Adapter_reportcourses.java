package com.quantumsit.sportsinc.Aaa_looks;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quantumsit.sportsinc.MyClasses_scoresActivity;
import com.quantumsit.sportsinc.R;
import com.quantumsit.sportsinc.Reports_coursesActivity;

import java.util.List;

/**
 * Created by Mona on 26-Dec-17.
 */

public class RecyclerView_Adapter_reportcourses extends RecyclerView.Adapter<RecyclerView_Adapter_reportcourses.ViewHolder>{


    private List<item_single_reports_courses> List_Item;
    private Context context;
    //ClientGlobal clientGlobal;


    public RecyclerView_Adapter_reportcourses(List<item_single_reports_courses> list_Item, Context context) {
        List_Item = list_Item;
        this.context = context;
        //clientGlobal = (ClientGlobal) context.getApplicationContext();
    }

    @Override
    public RecyclerView_Adapter_reportcourses.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reports_courses, parent, false);
        RecyclerView_Adapter_reportcourses.ViewHolder viewHolder = new RecyclerView_Adapter_reportcourses.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView_Adapter_reportcourses.ViewHolder holder, final int position) {


        holder.course_name.setText(List_Item.get(position).course_name);
        holder.attendance.setText(List_Item.get(position).attendance);
        holder.score.setText(List_Item.get(position).score);

        holder.course_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, Reports_coursesActivity.class);
                //intent.putExtra("course_name", List_Item.get(position).course_name);
                //intent.putExtra("attendance", List_Item.get(position).attendance);
                //intent.putExtra("score", List_Item.get(position).score);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != List_Item ? List_Item.size() : 0);
    }

    protected class ViewHolder extends RecyclerView.ViewHolder{
        private CardView course_card;
        private TextView course_name, attendance, score;


        public ViewHolder(View view) {
            super(view);
            course_card = (CardView) view.findViewById(R.id.reportsCoursesCardView);
            course_name = (TextView) view.findViewById(R.id.courseNameTextView_reportscourses);
            attendance = (TextView) view.findViewById(R.id.attendanceTextView_reportscourses);
            score = (TextView) view.findViewById(R.id.scoreTextView_reportscourses);

        }

    }



}
