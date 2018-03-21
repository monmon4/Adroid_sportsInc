package com.quantumsit.sportsinc.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Entities.CourseEntity;
import com.quantumsit.sportsinc.R;
import com.quantumsit.sportsinc.RegisterationForm_fragments.BookingFirstFormActivity;
import com.quantumsit.sportsinc.RegisterationForm_fragments.BookingForthFormActivity;
import com.squareup.picasso.Picasso;

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
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_level, null);
        }
        CourseEntity mycourse = getItem(position);

        TextView SessionNum = view.findViewById(R.id.sessionNum);
        TextView SessionDur = view.findViewById(R.id.sessionDuration);
        TextView CourseName = view.findViewById(R.id.CourseName);
        ImageView icon = view.findViewById(R.id.Course_icon);
        /*ImageButton booking = view.findViewById(R.id.imageButton_booking);

        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, BookingFirstFormActivity.class));
            }
        });*/

        String name = mycourse.getCourseName();
        String ImageUrl = mycourse.getImageUrl();
        //fillImage(name,icon);
        if(!ImageUrl.equals("")) {
            Picasso.with(context).load(Constants.others_host + ImageUrl).into(icon, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Log.d("Image Loading ","ERROR In Loading");
                }
            });
        }
        CourseName.setText(name);
        SessionNum.setText(mycourse.getClasses_Num());
        SessionDur.setText(mycourse.getClassDur());

        return  view;
    }

    public void fillImage(String name , ImageView levelImage){
        switch (name){
            case "Star fish":
                levelImage.setImageResource(R.drawable.star);
                break;
            case "Dolphin":
                levelImage.setImageResource(R.drawable.dolphin);
                break;
            case "Duck":
                levelImage.setImageResource(R.drawable.duck);
                break;
            case "Frog":
                levelImage.setImageResource(R.drawable.frog);
                break;
            case "Jelly fish":
                levelImage.setImageResource(R.drawable.jellyfish);
                break;
            case "Nemo":
                levelImage.setImageResource(R.drawable.nemo);
                break;
            case "Penguin":
                levelImage.setImageResource(R.drawable.penguin);
                break;
            case "Seal":
                levelImage.setImageResource(R.drawable.seal);
                break;
            case "Shark":
                levelImage.setImageResource(R.drawable.shark);
                break;
        }
    }
}