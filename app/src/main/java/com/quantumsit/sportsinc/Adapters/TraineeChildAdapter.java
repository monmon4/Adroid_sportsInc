package com.quantumsit.sportsinc.Adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Activities.Request_addActivity;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.Entities.UserEntity;
import com.quantumsit.sportsinc.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
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
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_trainee_child, null);
        }
        final UserEntity userEntity = getItem(position);

        TextView nameView = view.findViewById(R.id.child_name);
        ImageView imageView = view.findViewById(R.id.child_image);
        ImageButton removeImageButton = view.findViewById(R.id.removeImageButton);
        //final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        String ImageUrl = userEntity.getImgUrl();
        imageView.setImageResource(R.mipmap.ic_launcher_round);

        if(!ImageUrl.equals("")) {
            Picasso.with(context).load(Constants.profile_host + ImageUrl).into(imageView, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    //progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError() {

                }
            });
        }

        String meString = "";
        if (globalVars.getPerson_id() == userEntity.getId()) {
            meString = " (me)";
            removeImageButton.setVisibility(View.GONE);
        }
        else
            removeImageButton.setVisibility(View.VISIBLE);

        nameView.setText(userEntity.getName()+meString);

        final View finalView = view;
        removeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmRemove(finalView.getRootView().getContext() ,position);
            }
        });
        return  view;
    }

    private void confirmRemove(Context context ,final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context,
                R.style.MyAlertDialogStyle);
        builder.setTitle(context.getResources().getString(R.string.app_name));
        builder.setCancelable(false);
        builder.setMessage("     Are you sure?");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        removeChildTrainee(getItem(position).getId());
                        userEntityList.remove(position);
                        TraineeChildAdapter.this.notifyDataSetChanged();
                    }
                });
        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.show();

    }
    private void removeChildTrainee(int id) {
        try {
            JSONObject where_info = new JSONObject();
            where_info.put("id",id);
            Log.d(TAG,where_info.toString());
            String value_str = "{\"parent_id\":null}";

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.updateData);
            HashMap<String,String> params = new HashMap<>();
            params.put("table","users");
            params.put("values",value_str);
            params.put("where",where_info.toString());

            httpCall.setParams(params);

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    Log.d(TAG,String.valueOf(response));
                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}