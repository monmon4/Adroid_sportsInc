package com.quantumsit.sportsinc.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
        ImageView icon = view.findViewById(R.id.Course_icon);

        String name = mycourse.getCourseName();

        switch (name){
            case "Star fish":
                icon.setImageResource(R.drawable.star);
                break;
            case "Dolfin":
                icon.setImageResource(R.drawable.dolphin);
                break;
            case "Duck":
                icon.setImageResource(R.drawable.duck);
                break;
            case "Frog":
                icon.setImageResource(R.drawable.frog);
                break;
            case "Jelly fish":
                icon.setImageResource(R.drawable.jellyfish);
                break;
            case "Nemo":
                icon.setImageResource(R.drawable.nemo);
                break;
            case "Penguin":
                icon.setImageResource(R.drawable.penguin);
                break;
            case "Seal":
                icon.setImageResource(R.drawable.seal);
                break;
            case "Shark":
                icon.setImageResource(R.drawable.shark);
                break;

        }

        Title.setText(name);
        level.setText(mycourse.getLevel());
        price.setText("$ "+mycourse.getPrice());

        return  view;
    }
}