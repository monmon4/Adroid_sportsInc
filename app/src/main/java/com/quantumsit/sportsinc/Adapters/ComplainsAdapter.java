package com.quantumsit.sportsinc.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.quantumsit.sportsinc.Entities.ComplainEntity;
import com.quantumsit.sportsinc.R;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Bassam on 2/12/2018.
 */

public class ComplainsAdapter extends ArrayAdapter<ComplainEntity> {
    Context context ;
    List<ComplainEntity> mycomplain;


    public ComplainsAdapter(@NonNull Context context, int resource, List<ComplainEntity> mycomplain) {
        super(context, resource);
        this.context = context;
        this.mycomplain = mycomplain;
    }

    @Override
    public int getCount() {
        return mycomplain.size();
    }

    @Nullable
    @Override
    public ComplainEntity getItem(int position) {
        return mycomplain.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_complains, null);
        }
        ComplainEntity complain = getItem(position);

        TextView content = view.findViewById(R.id.complainContent);
        TextView Date = view.findViewById(R.id.complainDate);
        TextView Title = view.findViewById(R.id.complainFrom);

        content.setText(complain.getContent());
        SimpleDateFormat format = new SimpleDateFormat("MMM dd");
        String str = format.format(complain.getDate());
        Date.setText(str);
        Title.setText(complain.getTitle());

        return  view;
    }
}

