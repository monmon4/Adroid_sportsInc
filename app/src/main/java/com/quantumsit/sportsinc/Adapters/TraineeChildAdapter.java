package com.quantumsit.sportsinc.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Entities.UserEntity;
import com.quantumsit.sportsinc.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Bassam on 2/19/2018.
 */

public class TraineeChildAdapter extends ArrayAdapter<UserEntity> {

    private static final String TAG = NewsAdapter.class.getSimpleName();
    Context context ;
    List<UserEntity> userEntityList;
    GlobalVars globalVars;


    public TraineeChildAdapter(@NonNull Context context, int resource, List<UserEntity> userEntityList) {
        super(context, resource);
        this.context = context;
        this.userEntityList = userEntityList;
        globalVars = (GlobalVars) context;

    }

    @Override
    public int getCount() {
        return userEntityList.size();
    }

    @Nullable
    @Override
    public UserEntity getItem(int position){return  userEntityList.get(position);}

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_trainee_child, null);
        }
        UserEntity userEntity = getItem(position);

        TextView nameView = view.findViewById(R.id.child_name);
        ImageView imageView = view.findViewById(R.id.child_image);
        /*final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        String ImageUrl = eventEntity.getImg();

        if(!ImageUrl.equals("")) {
            Picasso.with(context).load(ImageUrl).into(imageView, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError() {

                }
            });
        }else {
            progressBar.setVisibility(View.GONE);
        }*/
        imageView.setImageResource(R.mipmap.ic_launcher_round);

        String meString = "";
        if (globalVars.getPerson_id() == userEntity.getId())
            meString = " (me)";
        nameView.setText(userEntity.getName()+meString);
        return  view;
    }
}