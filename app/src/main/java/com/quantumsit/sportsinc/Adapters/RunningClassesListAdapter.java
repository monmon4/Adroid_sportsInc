package com.quantumsit.sportsinc.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.quantumsit.sportsinc.Aaa_data.MyClass_info;
import com.quantumsit.sportsinc.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bassam on 2/11/2018.
 */

public class RunningClassesListAdapter extends ArrayAdapter<MyClass_info> {
    Context context ;
    List<MyClass_info> runningClasses;


    public RunningClassesListAdapter(@NonNull Context context, int resource, List<MyClass_info> runningClasses) {
        super(context, resource);
        this.context = context;
        this.runningClasses = runningClasses;
        if (runningClasses == null)
            this.runningClasses = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return runningClasses.size();
    }

    @Nullable
    @Override
    public MyClass_info getItem(int position) {
        return runningClasses.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_running_classes, null);
        }
        MyClass_info item = getItem(position);

        TextView content = view.findViewById(R.id.classe_name);

        content.setText(item.getClass_name());

        return  view;
    }
}