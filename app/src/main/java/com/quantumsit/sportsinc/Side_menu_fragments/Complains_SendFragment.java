package com.quantumsit.sportsinc.Side_menu_fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.Activities.HomeActivity;
import com.quantumsit.sportsinc.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class Complains_SendFragment extends Fragment {

    EditText title_edittext, content_edittext;

    MaterialBetterSpinner regarding_spinner;

    GlobalVars globalVars;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_complains_send,container,false);
        setHasOptionsMenu(true);

        globalVars = (GlobalVars) getActivity().getApplication();

        title_edittext = root.findViewById(R.id.subjectEditText_complains);
        content_edittext = root.findViewById(R.id.contentEditText_complains);

        regarding_spinner = root.findViewById(R.id.regardingSpinner_complains);

        ArrayAdapter<CharSequence> regarding_spinner_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.regarding_array, android.R.layout.simple_dropdown_item_1line);
        regarding_spinner_adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        regarding_spinner.setAdapter(regarding_spinner_adapter);

        /*send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String title = title_edittext.getText().toString();
                final String content = content_edittext.getText().toString();

                if (title.equals("")){
                    Toast.makeText(getContext(), "Please enter a subject for the complain", Toast.LENGTH_SHORT).show();
                } else if (content.equals("")){
                    Toast.makeText(getContext(), "There is no content for the complain", Toast.LENGTH_SHORT).show();
                } else {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
                            R.style.MyAlertDialogStyle);
                    builder.setTitle(getActivity().getResources().getString(R.string.app_name));
                    builder.setCancelable(false);
                    builder.setMessage("     Are you sure?");
                    builder.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    send_to_DB(title, content);
                                    dialogInterface.dismiss();
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
            }
        });*/


        return root;
    }

    @SuppressLint("StaticFieldLeak")
    private void send_to_DB(String title, String content) {

        JSONObject values_info = new JSONObject();
        try {
            values_info.put("title",title);
            values_info.put("user_id",globalVars.getId());
            values_info.put("Content",content);
            values_info.put("related_to",regarding_spinner.getText().toString());
            values_info.put("readable",0);

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.insertData);
            HashMap<String,String> params = new HashMap<>();
            params.put("table","complains");
            params.put("notify","1");
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

    public void send_clicked(){
        final String title = title_edittext.getText().toString();
        final String content = content_edittext.getText().toString();

        if (title.equals("")){
            Toast.makeText(getContext(), "Please enter a subject for the complain", Toast.LENGTH_SHORT).show();
        } else if (content.equals("")){
            Toast.makeText(getContext(), "There is no content for the complain", Toast.LENGTH_SHORT).show();
        } else {

            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
                    R.style.MyAlertDialogStyle);
            builder.setTitle(getActivity().getResources().getString(R.string.app_name));
            builder.setCancelable(false);
            builder.setMessage("     Are you sure?");
            builder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            send_to_DB(title, content);
                            dialogInterface.dismiss();
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
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.send_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.send_message){
            send_clicked();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
