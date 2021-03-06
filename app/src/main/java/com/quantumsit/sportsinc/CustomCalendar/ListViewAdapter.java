package com.quantumsit.sportsinc.CustomCalendar;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Entities.classesEntity;
import com.quantumsit.sportsinc.R;
import com.quantumsit.sportsinc.Interfaces.MyItemClickListener;

import java.util.List;

/**
 * Created by Bassam on 12/31/2017.
 */

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ViewHolder> {
    private boolean parent;
    private LayoutInflater mInflater;
    Context context ;
    List<classesEntity> myclasses;
    MyItemClickListener clickListener;
    int person_id;

    public ListViewAdapter(@NonNull Context context, int resource, List<classesEntity> classeslist) {
        this.context = context;
        this.myclasses = classeslist;
        mInflater = LayoutInflater.from(context);
    }

    public ListViewAdapter(@NonNull Context context, int person_id ,int resource, List<classesEntity> classeslist) {
        this.context = context;
        this.person_id = person_id;
        this.myclasses = classeslist;
        mInflater = LayoutInflater.from(context);
    }

    public ListViewAdapter(@NonNull Context context, int person_id ,boolean parent ,int resource, List<classesEntity> classeslist) {
        this.context = context;
        this.person_id = person_id;
        this.parent = parent;
        this.myclasses = classeslist;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.classes_list_items, parent,false);

        ViewHolder mViewHolder = new ViewHolder(view);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        classesEntity myclass = getItem(position);

        String classStatus = myclass.getStatus();
        if (classStatus.equals("Running"))
            viewHolder.status.setTextColor(Color.parseColor("#22a630"));
        else if (classStatus.equals("Canceled"))
            viewHolder.status.setTextColor(Color.parseColor("#df1b1c"));
        else if (classStatus.equals("Postponed"))
            viewHolder.status.setTextColor(Color.parseColor("#f98a03"));
        else if (classStatus.equals("Finished"))
            viewHolder.status.setTextColor(Color.parseColor("#ed4e4d4d"));
        else
            viewHolder.status.setTextColor(Color.parseColor("#2a388f"));

        viewHolder.status.setText(classStatus);
        Log.d("ListItemsChild" ,myclass.getPerson_id() +", "+person_id );
        if (parent)
            viewHolder.childName.setVisibility(View.VISIBLE);
        else
            viewHolder.childName.setVisibility(View.GONE);
        if (myclass.getPerson_id() != person_id) {
            viewHolder.childName.setText(myclass.getPerson_name());
        }
        else
            viewHolder.childName.setText("Me");
        viewHolder.Title.setText(myclass.getCourseName()+" "+myclass.getClassName());
        viewHolder.StartTime.setText(myclass.getStartTime());
        viewHolder.Time.setText(myclass.getStartTime()+" to "+myclass.getEndTime());
    }
    public classesEntity getItem(int position) {
        return myclasses.get(position);
    }

    @Override
    public int getItemCount() {
        return myclasses.size();
    }



    public void setClickListener(MyItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    /**
     * View holder to display each RecylerView item
     */
    protected class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        private TextView Title ,StartTime ,Time ,status , childName;

        public ViewHolder(View view) {
            super(view);
            Title = view.findViewById(R.id.class_name);
            childName = view.findViewById(R.id.child_name);
            Time = view.findViewById(R.id.class_start_end);
            StartTime = view.findViewById(R.id.class_start_time);
            status = view.findViewById(R.id.class_status);
            view.setTag(view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }
}