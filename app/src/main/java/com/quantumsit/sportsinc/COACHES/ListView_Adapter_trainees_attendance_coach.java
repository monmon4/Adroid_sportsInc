package com.quantumsit.sportsinc.COACHES;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.quantumsit.sportsinc.R;

import java.util.ArrayList;

/**
 * Created by Mona on 27-Dec-17.
 */

public class ListView_Adapter_trainees_attendance_coach extends ArrayAdapter<item_trainee_attendance> {

    public ListView_Adapter_trainees_attendance_coach(Context context, ArrayList<item_trainee_attendance> attendances) {
        super(context, 0, attendances);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        item_trainee_attendance attendance = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_coach_trainee_attendance, parent, false);
        }
        // Lookup view for data population
        TextView trainee_name =  convertView.findViewById(R.id.traineeNameTextView_singleItemTraineeAttendance);

        ImageView attended = convertView.findViewById(R.id.attendedImageView_singleItemTraineeAttendance);

        // Populate the data into the template view using the data object
        trainee_name.setText(attendance.name);
        if (!attendance.attended) {
            attended.setBackgroundResource(R.drawable.ic_not_checked);
        }
        // Return the completed view to render on screen
        return convertView;
    }
}
