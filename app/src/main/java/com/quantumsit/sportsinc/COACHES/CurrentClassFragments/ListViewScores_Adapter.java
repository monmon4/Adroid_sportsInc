package com.quantumsit.sportsinc.COACHES.CurrentClassFragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.quantumsit.sportsinc.Adapters.item_checkbox;
import com.quantumsit.sportsinc.COACHES.item_request_coach;
import com.quantumsit.sportsinc.R;

import java.util.ArrayList;

/**
 * Created by Mona on 27-Dec-17.
 */

public class ListViewScores_Adapter extends ArrayAdapter<item_score> {

    public ArrayList<item_score> list_items;

    public ListViewScores_Adapter(Context context, ArrayList<item_score> list_items) {
        super(context, 0, list_items);

        this.list_items = new ArrayList<item_score>();
        this.list_items.addAll(list_items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        item_score item = list_items.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_coach_trainee_score, parent, false);
        }
        // Lookup view for data population
        TextView trainee_name_textview =  convertView.findViewById(R.id.traineeNameTextView_itemcoachscores);
        EditText score_edittext =  convertView.findViewById(R.id.scoreEditText_itemcoachscores);

        // Populate the data into the template view using the data object
        trainee_name_textview.setText(item.name);
        item.setScore(score_edittext);
        return convertView;
    }
}
