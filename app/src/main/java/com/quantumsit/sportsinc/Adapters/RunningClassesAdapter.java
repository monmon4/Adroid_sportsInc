package com.quantumsit.sportsinc.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.quantumsit.sportsinc.Aaa_data.MyClass_info;
import com.quantumsit.sportsinc.Activities.NewsDetailsActivity;
import com.quantumsit.sportsinc.COACHES.ActivityCurrentClass_coach;
import com.quantumsit.sportsinc.Entities.NewsEntity;
import com.quantumsit.sportsinc.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Bassam on 2/11/2018.
 */

public class RunningClassesAdapter extends RecyclerView.Adapter<RunningClassesAdapter.ClassViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private List<MyClass_info> newsList;

    public RunningClassesAdapter(Context context, List<MyClass_info> newsList) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.newsList = newsList;
    }

    @Override
    public ClassViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_recycler_news, parent, false);
        ClassViewHolder holder = new ClassViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ClassViewHolder holder, int position) {
        MyClass_info entity = newsList.get(position);
        holder.className.setText(entity.getClass_name());
        holder.setListener(entity,context);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class ClassViewHolder extends RecyclerView.ViewHolder{

        public TextView className;

        public ClassViewHolder(View view) {
            super(view);
            className = view.findViewById(R.id.classe_name);
        }

        public void setListener(final MyClass_info entity , final Context context){

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ActivityCurrentClass_coach.class);
                    intent.putExtra("MyRunningClass",entity);
                    context.startActivity(intent);
                }
            });
        }
    }
}
