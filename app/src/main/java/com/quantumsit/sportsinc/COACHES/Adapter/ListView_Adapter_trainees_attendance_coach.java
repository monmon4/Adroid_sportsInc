package com.quantumsit.sportsinc.COACHES.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.quantumsit.sportsinc.Entities.item_trainee_attendance;
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
        TextView trainee_notes = convertView.findViewById(R.id.traineeAttendanceNoteTextView_singleItemTraineeAttendance);
        ImageView attended = convertView.findViewById(R.id.attendedImageView_singleItemTraineeAttendance);

        // Populate the data into the template view using the data object
        trainee_name.setText(attendance.getName());
        trainee_notes.setText(attendance.getAttendance_notes());
       // trainee_notes.setText(attendance.get);
        if (attendance.isAttended()) {
            attended.setImageResource(R.drawable.ic_attended);
        } else {
            attended.setImageResource(R.drawable.ic_not_attended);
        }
        // Return the completed view to render on screen
        return convertView;
    }
}
