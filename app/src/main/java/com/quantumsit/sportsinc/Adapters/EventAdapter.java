package com.quantumsit.sportsinc.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.quantumsit.sportsinc.Entities.EventEntity;
import com.quantumsit.sportsinc.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Bassam on 1/3/2018.
 */

public class EventAdapter  extends ArrayAdapter<EventEntity> {
    Context context ;
    List<EventEntity> eventEntityList;


    public EventAdapter(@NonNull Context context, int resource, List<EventEntity> eventEntityList) {
        super(context, resource);
        this.context = context;
        this.eventEntityList = eventEntityList;
    }

    @Override
    public int getCount() {
        return eventEntityList.size();
    }

    @Nullable
    @Override
    public EventEntity getItem(int position){return  eventEntityList.get(position);}

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_event, null);
        }
        EventEntity eventEntity = getItem(position);

        TextView Title = view.findViewById(R.id.eventTitle);
        TextView Month = view.findViewById(R.id.eventMonth);
        TextView Day = view.findViewById(R.id.eventDay);
        //TextView time = view.findViewById(R.id.eventTime);
        //TextView date = view.findViewById(R.id.eventDate);
       // TextView content = view.findViewById(R.id.eventContent);

        Date mDate = eventEntity.getDate();
        SimpleDateFormat df = new SimpleDateFormat("E MMM dd yyyy");
        String formattedDate = df.format(mDate);
        String[] data = formattedDate.split(" ");

        Month.setText(data[1]);
        Day.setText(data[2]);

       Title.setText(eventEntity.getTitle());
       /* time.setText(eventEntity.getTime());
        date.setText(formattedDate);
        content.setText(eventEntity.getDescription());*/
        return  view;
    }
}