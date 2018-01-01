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


        holder.course_name.setText(List_Item.get(position).course_name);
        holder.date.setText(List_Item.get(position).date);
        holder.class_number.setText(List_Item.get(position).class_number);
        holder.score.setText(List_Item.get(position).class_score);

        holder.scores_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, MyClasses_scoresActivity.class);
                intent.putExtra("course_name", List_Item.get(position).course_name);
                intent.putExtra("date", List_Item.get(position).date);
                intent.putExtra("class_number", List_Item.get(position).class_number);
                intent.putExtra("score", List_Item.get(position).class_score);
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
            scores_card = (CardView) view.findViewById(R.id.scoresCardView);
            course_name = (TextView) view.findViewById(R.id.courseNameTextView_scoresitem);
            date = (TextView) view.findViewById(R.id.dateTextView_scoresitem);
            class_number = (TextView) view.findViewById(R.id.attendanveTextView_reportscourses);
            score = (TextView) view.findViewById(R.id.scoreTextView_scoresitem);

        }

    }



}
