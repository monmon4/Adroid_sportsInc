package com.quantumsit.sportsinc.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quantumsit.sportsinc.Entities.item_single_scores;
import com.quantumsit.sportsinc.Activities.MyClasses_scoresActivity;
import com.quantumsit.sportsinc.R;

import java.util.List;

/**
 * Created by Mona on 26-Dec-17.
 */

public class RecyclerView_Adapter_scores extends RecyclerView.Adapter<RecyclerView_Adapter_scores.ViewHolder>{


    private List<item_single_scores> List_Item;
    private Context context;
    //ClientGlobal clientGlobal;


    public RecyclerView_Adapter_scores(List<item_single_scores> list_Item, Context context) {
        List_Item = list_Item;
        this.context = context;
        //clientGlobal = (ClientGlobal) context.getApplicationContext();
    }

    @Override
    public RecyclerView_Adapter_scores.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scores, parent, false);
        RecyclerView_Adapter_scores.ViewHolder viewHolder = new RecyclerView_Adapter_scores.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView_Adapter_scores.ViewHolder holder, final int position) {
        /*
        * Adapter fill each item in the list by
        * the course name and class name and Trainee score for this class
        * */

        holder.course_name.setText(List_Item.get(position).getCourse_name());
        holder.date.setText(List_Item.get(position).getClass_date());
        String class_number_string = context.getResources().getString(R.string.class_number) + String.valueOf(List_Item.get(position).getClass_number());
        holder.class_number.setText(class_number_string);
        String score_string = context.getResources().getString(R.string.score) + ": " + String.valueOf(List_Item.get(position).getScore());
        holder.score.setText(score_string);

        holder.scores_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /*
            * on each item Click View it's details
            * and pass this item data for details Activity
            * */
            Intent intent = new Intent(context, MyClasses_scoresActivity.class);
            intent.putExtra(context.getString(R.string.Key_Course_name), List_Item.get(position).getCourse_name());
            intent.putExtra(context.getString(R.string.Key_Group_name), List_Item.get(position).getGroup_name());
            intent.putExtra(context.getString(R.string.Key_Class_date), List_Item.get(position).getClass_date());
            intent.putExtra(context.getString(R.string.Key_Coach_name), List_Item.get(position).getCoach_name());
            intent.putExtra(context.getString(R.string.Key_Coach_note), List_Item.get(position).getCoach_notes());
            intent.putExtra(context.getString(R.string.Key_Attend), List_Item.get(position).getAttend());
            intent.putExtra(context.getString(R.string.Key_Score), List_Item.get(position).getScore());
            intent.putExtra(context.getString(R.string.Key_Class_number), List_Item.get(position).getClass_number());
            context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != List_Item ? List_Item.size() : 0);
    }

    protected class ViewHolder extends RecyclerView.ViewHolder{
        private CardView scores_card;
        private TextView course_name, date, class_number, score;
        public ViewHolder(View view) {
            super(view);
            scores_card =  view.findViewById(R.id.scoresCardView);
            course_name =  view.findViewById(R.id.courseNameTextView_scoresitem);
            date =  view.findViewById(R.id.dateTextView_scoresitem);
            class_number =  view.findViewById(R.id.attendanveTextView_reportscourses);
            score =  view.findViewById(R.id.scoreTextView_scoresitem);

        }

    }



}
