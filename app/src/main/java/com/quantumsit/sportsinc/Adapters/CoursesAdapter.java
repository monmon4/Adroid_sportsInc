package com.quantumsit.sportsinc.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.quantumsit.sportsinc.Entities.CourseEntity;
import com.quantumsit.sportsinc.R;

import java.util.List;

/**
 * Created by Bassam on 1/3/2018.
 */

public class CoursesAdapter extends ArrayAdapter<CourseEntity> {
    Context context ;
    List<CourseEntity> mycourses;


    public CoursesAdapter(@NonNull Context context, int resource, List<CourseEntity> mycourses) {
        super(context, resource);
        this.context = context;
        this.mycourses = mycourses;
    }

    @Override
    public int getCount() {
        return mycourses.size();
    }

    @Nullable
    @Override
    public CourseEntity getItem(int position) {
        return mycourses.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.course_list_item, null);
        }
        CourseEntity mycourse = getItem(position);

        TextView Title = view.findViewById(R.id.course_item_name);
        TextView level = view.findViewById(R.id.course_item_level);
        TextView price = view.findViewById(R.id.course_item_price);

        Title.setText(mycourse.getCourseName());
        level.setText(mycourse.getLevel());
        price.setText("$ "+mycourse.getPrice());

        return  view;
    }
}