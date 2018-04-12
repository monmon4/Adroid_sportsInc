package com.quantumsit.sportsinc.Adapters;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.Entities.BookingCourseEntityFragment;
import com.quantumsit.sportsinc.Entities.item_about;
import com.quantumsit.sportsinc.Entities.item_checkout;
import com.quantumsit.sportsinc.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Mona on 27-Dec-17.
 */

public class ListView_Adapter_checkout extends ArrayAdapter<item_checkout> {

    Context context;
    GlobalVars globalVars;
    ArrayList<item_checkout> items;

    public ListView_Adapter_checkout(Context context, ArrayList<item_checkout> items) {
        super(context, 0, items);
        this.items = items;
        this.context = context;
        globalVars = (GlobalVars) context.getApplicationContext();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final item_checkout item = items.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_checkout, parent, false);
        }
        // Lookup view for data population
        ImageButton close_imageButton = convertView.findViewById(R.id.closeImageButton_itemcheckout);
        ImageView level_imageview = convertView.findViewById(R.id.courseImageView_itemcheckout);
        TextView className_textview = convertView.findViewById(R.id.classNameTextView_itemcheckout);
        TextView noOfTrainees_textview = convertView.findViewById(R.id.traineesNumberTextView_itemcheckout);
        TextView trainessNames_textview = convertView.findViewById(R.id.traineesNamesTextView_itemcheckout);
        TextView price_textview = convertView.findViewById(R.id.priceTextView_itemcheckout);

        close_imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (globalVars.getBookingCourseEntities() != null) {
                    if(globalVars.getBookingCourseEntities().size() != 0){
                        globalVars.bookingCourseEntities.remove(position);
                        items.remove(position);
                        ListView_Adapter_checkout.this.notifyDataSetChanged();
                        ((Activity) context).recreate();
                    } else {
                        final String[] ids = items.get(position).getIds().split("@"); {
                            for (int i=0; i<ids.length; i++) {
                                HttpCall httpCall = new HttpCall();
                                httpCall.setMethodtype(HttpCall.POST);
                                httpCall.setUrl(Constants.deleteBooking);
                                HashMap<String,String> params = new HashMap<>();
                                params.put("trainee_id",ids[i]);
                                httpCall.setParams(params);

                                //final String finalDate_of_birth = date_of_birth;
                                final int finalI = i;
                                new HttpRequest(){
                                    @Override
                                    public void onResponse(JSONArray response) {
                                        super.onResponse(response);

                                        if(response != null){
                                            if(finalI == ids.length-1) {
                                                items.remove(position);
                                                ListView_Adapter_checkout.this.notifyDataSetChanged();
                                                ((FragmentActivity) context).recreate();
                                            }

                                        } else {
                                            Toast.makeText(context, "Unable to delete booking", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }.execute(httpCall);
                            }


                        }
                    }
                }
            }
        });


        // Populate the data into the template view using the data object
        if (item != null) {
            String ImageUrl = item.getImage_url();

            if (!ImageUrl.equals("")) {
                Picasso.with(context).load(Constants.others_host + ImageUrl).into(level_imageview, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });
            }

            className_textview.setText(item.getClass_name());
            noOfTrainees_textview.setText(item.getNumber_of_trainees());
            trainessNames_textview.setText(item.getTrainees_names());
            price_textview.setText(item.getPrice());

        }
        // Return the completed view to render on screen
        return convertView;
    }
}
