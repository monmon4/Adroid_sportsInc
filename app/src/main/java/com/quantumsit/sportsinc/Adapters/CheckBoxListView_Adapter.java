package com.quantumsit.sportsinc.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.quantumsit.sportsinc.R;

import java.util.ArrayList;

/**
 * Created by mona_ on 12/30/2017.
 */

public class CheckBoxListView_Adapter extends ArrayAdapter<item_checkbox> {

    public ArrayList<item_checkbox> list_items;

    public CheckBoxListView_Adapter(Context context, int textViewResourceId,
                           ArrayList<item_checkbox> list_items) {
        super(context, textViewResourceId, list_items);
        this.list_items = new ArrayList<item_checkbox>();
        this.list_items.addAll(list_items);
    }

    private class ViewHolder {
        //TextView code;
        CheckBox checkBox;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_checkbox, parent, false);


            holder = new ViewHolder();
            holder.checkBox =  convertView.findViewById(R.id.checkBox_item);
            convertView.setTag(holder);

            holder.checkBox.setOnClickListener( new View.OnClickListener() {
                public void onClick(View v) {

                    CheckBox cb = (CheckBox) v ;
                    item_checkbox item = (item_checkbox) cb.getTag();

                    item.setSelected(cb.isChecked());
                }
            });
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        item_checkbox item = list_items.get(position);

        holder.checkBox.setText(item.getName());
        holder.checkBox.setChecked(item.getSelected());
        holder.checkBox.setTag(item);

        return convertView;

    }



}
