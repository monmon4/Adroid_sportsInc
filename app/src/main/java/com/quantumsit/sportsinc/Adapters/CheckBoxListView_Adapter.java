package com.quantumsit.sportsinc.Adapters;

import android.content.Context;
import android.os.Build;
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

import com.quantumsit.sportsinc.R;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by mona_ on 12/30/2017.
 */

public class CheckBoxListView_Adapter extends ArrayAdapter<item_checkbox> {

    public List<item_checkbox> list_items;

    PopupWindow note_popup_window;
    private Context context;
    private RelativeLayout rl;

    public CheckBoxListView_Adapter(Context context, int textViewResourceId,
                           List<item_checkbox> list_items) {
        super(context, textViewResourceId, list_items);
        this.list_items = list_items;
        this.context = context;
    }

    public void setRL (RelativeLayout rl){
        this.rl = rl;
    }

    private class ViewHolder {
        //TextView code;
        CheckBox checkBox;
        ImageView edit_imgview;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        final item_checkbox item = list_items.get(position);


        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_checkbox, parent, false);


            holder = new ViewHolder();
            holder.checkBox =  convertView.findViewById(R.id.checkBox_item);
            holder.edit_imgview = convertView.findViewById(R.id.editImageView_item);
            convertView.setTag(holder);

            final ViewHolder finalHolder = holder;

            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    CheckBox cb = (CheckBox) v ;
                    item_checkbox item = (item_checkbox) cb.getTag();

                    item.setSelected(cb.isChecked());
                    if(cb.isChecked()){
                        finalHolder.edit_imgview.setEnabled(false);
                        finalHolder.edit_imgview.setVisibility(View.INVISIBLE);
                    } else {
                        finalHolder.edit_imgview.setEnabled(true);
                        finalHolder.edit_imgview.setVisibility(View.VISIBLE);
                    }
                }
            });

            holder.edit_imgview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    note_popup(item);
                }
            });
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }



        holder.checkBox.setText(item.getName());
        holder.checkBox.setChecked(item.getSelected());
        holder.checkBox.setTag(item);

        if(item.getSelected()){
            holder.edit_imgview.setEnabled(false);
            holder.edit_imgview.setVisibility(View.INVISIBLE);
        } else {
            holder.edit_imgview.setEnabled(true);
            holder.edit_imgview.setVisibility(View.VISIBLE);
        }

        return convertView;

    }

    public void note_popup(final item_checkbox item_checkbox){

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.window_write_note_layout,null);

        note_popup_window = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        if(Build.VERSION.SDK_INT>=21){
            note_popup_window.setElevation(5.0f);
        }

        final EditText note_edit_text = customView.findViewById(R.id.noteEditText_notewindow);
        Button done_button = customView.findViewById(R.id.doneButton_notewindow);
        note_edit_text.setEnabled(true);
        note_edit_text.setText(item_checkbox.getNote());

        done_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String note = note_edit_text.getText().toString();
                //show_toast("Success" + verifcation);
                item_checkbox.setNote(note);
                note_popup_window.dismiss();
            }
        } );

        note_popup_window.showAtLocation(rl, Gravity.CENTER,0,0);
        note_popup_window.setFocusable(true);
        note_edit_text.setFocusable(true);
        note_popup_window.setOutsideTouchable(false);
        note_popup_window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        note_popup_window.update();
    }

}
