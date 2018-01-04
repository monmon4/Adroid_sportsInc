package com.quantumsit.sportsinc.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quantumsit.sportsinc.Entities.NotificationEntity;
import com.quantumsit.sportsinc.R;

import java.util.List;

/**
 * Created by Bassam on 1/4/2018.
 */

public class NotificationAdapter extends ArrayAdapter<NotificationEntity> {
    Context context ;
    List<NotificationEntity> mynotification;


    public NotificationAdapter(@NonNull Context context, int resource, List<NotificationEntity> mynotification) {
        super(context, resource);
        this.context = context;
        this.mynotification = mynotification;
    }

    @Override
    public int getCount() {
        return mynotification.size();
    }

    @Nullable
    @Override
    public NotificationEntity getItem(int position) {
        return mynotification.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_notification, null);
        }

        NotificationEntity notificationEntity = getItem(position);

        LinearLayout background = view.findViewById(R.id.notification_layout);
        TextView subject = view.findViewById(R.id.notificationSubject);
        TextView data = view.findViewById(R.id.notificationDate);

        subject.setText(notificationEntity.getContent());
        data.setText(notificationEntity.getDate());

        int read = notificationEntity.getRead();

        if(read == 0)
            background.setBackgroundColor(Color.parseColor("#ebf1fc"));
        return  view;
    }
}
