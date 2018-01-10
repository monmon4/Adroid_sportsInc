package com.quantumsit.sportsinc.Home_fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class MainFragment extends Fragment {

    TextView AboutAcademy;
    ImageView Logo;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main,container,false);
        AboutAcademy = root.findViewById(R.id.Academybrief);
        Logo = root.findViewById(R.id.AcademyLogo);

        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.POST);
        httpCall.setUrl(Constants.selectData);
        HashMap<String,String> params = new HashMap<>();
        params.put("table","info_academy");

        httpCall.setParams(params);
        new HttpRequest(){
            @Override
            public void onResponse(JSONArray response) {
                super.onResponse(response);
                fillView(response);
            }
        }.execute(httpCall);

        return root;
    }

    private void fillView(JSONArray response) {
        if (response != null) {
            try {
                JSONObject object = response.getJSONObject(0);

                String logo = object.getString("logo");
                String brief = object.getString("about");

                if (!logo.equals("")) {
                    Picasso.with(getContext()).load(logo).into(Logo);
                }

                AboutAcademy.setText(brief);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
