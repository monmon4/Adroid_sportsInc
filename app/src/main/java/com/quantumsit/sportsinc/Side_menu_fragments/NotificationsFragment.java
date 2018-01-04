package com.quantumsit.sportsinc.Side_menu_fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.quantumsit.sportsinc.Adapters.NotificationAdapter;
import com.quantumsit.sportsinc.Entities.NotificationEntity;
import com.quantumsit.sportsinc.NotificationDetailsActivity;
import com.quantumsit.sportsinc.R;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private NotificationAdapter adapter ;
    private List<NotificationEntity> notificationList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications,container,false);
        notificationList = new ArrayList<>();
        ListView listView = root.findViewById(R.id.notification_listview);
        listView.setEmptyView(root.findViewById(R.id.empty_text));

        notificationList.add(new NotificationEntity("Class 1 has been Postponed","the class canceled by admin","21 Jun","Ahmed Ali","notification",0));
        notificationList.add(new NotificationEntity("Complain from trainee","Ahmed Ali request an abcent","11 Jun","Ahmed Ali","Request",0));
        notificationList.add(new NotificationEntity("Class 1 has been canceled","the class canceled by admin","31 Dec","Ahmed Ali","notification",1));
        notificationList.add(new NotificationEntity("Ahmed ali request an abcent","Ahmed Ali request an abcent","21 Dec","Ahmed Ali","Request",1));
        adapter = new NotificationAdapter(getContext(),R.layout.list_item_notification,notificationList);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), NotificationDetailsActivity.class);
                intent.putExtra("MyNotification",notificationList.get(i));
                startActivity(intent);
            }
        });
        return root;
    }
}
