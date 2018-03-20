package com.quantumsit.sportsinc.Aaa_looks;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quantumsit.sportsinc.R;
import com.quantumsit.sportsinc.Activities.Reports_coursesActivity;

import java.util.List;

/**
 * Created by Mona on 26-Dec-17.
 */

public class RecyclerView_Adapter_reportcourses extends RecyclerView.Adapter<RecyclerView_Adapter_reportcourses.ViewHolder>{


    private List<item_single_reports_courses> List_Item;
    private Context context;
    public int person_id;
    public boolean parent;
    //ClientGlobal clientGlobal;


    public RecyclerView_Adapter_reportcourses(List<item_single_reports_courses> list_Item, Context context) {
        List_Item = list_Item;
        this.context = context;
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
        String attendance_string = context.getResources().getString(R.string.attendance)+": " + String.valueOf(List_Item.get(position).attendance) + "%";
        holder.attendance.setText(attendance_string);
        String score_string = context.getResources().getString(R.string.score)+": " + String.valueOf(List_Item.get(position).score);
        holder.score.setText(score_string);
        holder.childName.setText(List_Item.get(position).trainee_name);
        if (List_Item.get(position).trainee_id == person_id)
            holder.childName.setText("me");

        if (!parent)
            holder.childName.setVisibility(View.GONE);
        else
            holder.childName.setVisibility(View.VISIBLE);
        holder.course_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, Reports_coursesActivity.class);
                intent.putExtra("course_id", List_Item.get(position).course_id);
                intent.putExtra("group_name", List_Item.get(position).group_name);
                intent.putExtra("course_name", List_Item.get(position).course_name);
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
        private TextView course_name, attendance, score , childName;


        public ViewHolder(View view) {
            super(view);
            course_card = view.findViewById(R.id.reportsCoursesCardView);
            course_name =  view.findViewById(R.id.courseNameTextView_reportscourses);
            attendance =  view.findViewById(R.id.attendanceTextView_reportscourses);
            score =  view.findViewById(R.id.scoreTextView_reportscourses);
            childName = view.findViewById(R.id.child_name);
        }

    }



}
