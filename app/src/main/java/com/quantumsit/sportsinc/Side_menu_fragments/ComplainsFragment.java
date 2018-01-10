package com.quantumsit.sportsinc.Side_menu_fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.HomeActivity;
import com.quantumsit.sportsinc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class ComplainsFragment extends Fragment {

    Button send_button;
    EditText title_edittext, content_edittext;

    GlobalVars globalVars;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_complains,container,false);

        globalVars = (GlobalVars) getActivity().getApplication();

        send_button = root.findViewById(R.id.sendButton_complains);
        title_edittext = root.findViewById(R.id.subjectEditText_complains);
        content_edittext = root.findViewById(R.id.contentEditText_complains);

        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = title_edittext.getText().toString();
                String content = content_edittext.getText().toString();

                if (title.equals("")){
                    Toast.makeText(getContext(), "Please enter a subject for the complain", Toast.LENGTH_SHORT).show();
                } else if (content.equals("")){
                    Toast.makeText(getContext(), "There is no content for the complain", Toast.LENGTH_SHORT).show();
                } else {
                    send_to_DB(title, content);
                }


            }
        });

        return root;
    }

    @SuppressLint("StaticFieldLeak")
    private void send_to_DB(String title, String content) {

        JSONObject values_info = new JSONObject();
        try {
            values_info.put("title",title);
            values_info.put("user_id",globalVars.getId());
            values_info.put("Content",content);
            values_info.put("readable",0);

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.insertData);
            HashMap<String,String> params = new HashMap<>();
            params.put("table","complains");
            params.put("values",values_info.toString());

            httpCall.setParams(params);

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);

                    if (response != null) {
                        Toast.makeText(getContext(), "Your complain was successfully sent", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                    }

                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
