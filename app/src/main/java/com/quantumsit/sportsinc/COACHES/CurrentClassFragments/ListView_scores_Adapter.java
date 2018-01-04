package com.quantumsit.sportsinc.COACHES.CurrentClassFragments;

import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quantumsit.sportsinc.Adapters.item_checkbox;
import com.quantumsit.sportsinc.R;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by mona_ on 12/30/2017.
 */

public class ListView_scores_Adapter extends ArrayAdapter<item_score> {

    public ArrayList<item_score> list_items;


    public HashMap<String, String> hashMap_edittext;

    public ListView_scores_Adapter(Context context, int textViewResourceId,
                                   ArrayList<item_score> list_items) {
        super(context, textViewResourceId, list_items);
        this.list_items = new ArrayList<>();
        this.list_items.addAll(list_items);
        hashMap_edittext = new HashMap<>();

        for(int i=0; i<list_items.size(); i++) {
            hashMap_edittext.put(list_items.get(i).name, "");
        }
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
        holder.trainee_name_textview.setText(list_items.get(position).name);
        holder.scores_edittext.setText(hashMap_edittext.get(list_items.get(position).name));


        holder.scores_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                hashMap_edittext.put(list_items.get(holder.position).name, s.toString());
            }
        });

        return convertView;

    }


}
