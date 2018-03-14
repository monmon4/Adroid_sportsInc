package com.quantumsit.sportsinc.COACHES.CurrentClassFragments;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.quantumsit.sportsinc.Aaa_data.Trainees_info;
import com.quantumsit.sportsinc.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mona_ on 12/30/2017.
 */

public class ListView_scores_Adapter extends ArrayAdapter<Trainees_info> {

    public ArrayList<Trainees_info> list_items;


    public HashMap<Integer, String> hashMap_edittext;

    public ListView_scores_Adapter(Context context, int textViewResourceId,
                                   ArrayList<Trainees_info> list_items) {
        super(context, textViewResourceId, list_items);
        this.list_items = list_items;
        hashMap_edittext = new HashMap<>();

    }

    private class ViewHolder {
        private TextView trainee_name_textview;
        private EditText scores_edittext;
        private int position;

        public ViewHolder(View view) {
            trainee_name_textview = view.findViewById(R.id.traineeNameTextView_itemcoachscores);
            scores_edittext = view.findViewById(R.id.scoreEditText_itemcoachscores);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;


        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_coach_trainee_score, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.position = position;
        holder.trainee_name_textview.setText(list_items.get(position).getTrainee_name());
        hashMap_edittext.put(list_items.get(position).getID(), String.valueOf(list_items.get(position).getTrainee_score()));
        holder.scores_edittext.setText(hashMap_edittext.get(list_items.get(position).getID()));


        holder.scores_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                hashMap_edittext.put(list_items.get(holder.position).getID(), s.toString());
                if (!s.toString().equals(""))
                    list_items.get(holder.position).setTrainee_score(Integer.parseInt(s.toString()));
            }
        });

        return convertView;

    }


}
