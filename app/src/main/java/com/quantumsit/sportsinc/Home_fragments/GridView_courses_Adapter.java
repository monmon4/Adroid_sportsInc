package com.quantumsit.sportsinc.Home_fragments;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Mona on 04-Mar-18.
 */

public class GridView_courses_Adapter extends BaseAdapter {

    ArrayList<item_courses> items;
    Context context;
    private static LayoutInflater inflater=null;

    public GridView_courses_Adapter(Context context, ArrayList<item_courses> items) {
        // TODO Auto-generated constructor stub
        this.items = items;
        this.context = context;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView price_textView;
        ImageView level_ImageView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.item_courses2, null);
        holder.price_textView = rowView.findViewById(R.id.priceTextView_courseitem);
        holder.level_ImageView = rowView.findViewById(R.id.imageView_courseitem);

        holder.price_textView.setText("$" + items.get(position).price);
        String img_url = items.get(position).img;
        if(!img_url.equals("")) {
            Picasso.with(context).load(Constants.others_host + img_url).into(holder.level_ImageView, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Log.d("Image Loading ","ERROR In Loading");
                }
            });
        }



        rowView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked "+items.get(position).price, Toast.LENGTH_SHORT).show();
            }
        });

        return rowView;
    }

}