package com.quantumsit.sportsinc.COACHES.CurrentClassFragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Aaa_data.MyClass_info;
import com.quantumsit.sportsinc.Aaa_data.Rule_info;
import com.quantumsit.sportsinc.Aaa_data.Trainees_info;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;
import com.quantumsit.sportsinc.Activities.HomeActivity;
import com.quantumsit.sportsinc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * A simple {@link Fragment} subclass.
 */
public class Coach_CurrentClassNoteFragment extends Fragment {

    private static String TAG = Coach_CurrentClassNoteFragment.class.getSimpleName();

    GlobalVars global;

    ProgressDialog progressDialog;
    EditText notes_edit_text;
    FloatingActionButton done_button;

    MyClass_info info;
    int expectedCounter = 0;
    private static AtomicInteger doneAT = new AtomicInteger(0);
    private static AtomicInteger allAT = new AtomicInteger(0);

    RadioButton Excellent_radioButton, Good_radioButton, Average_radioButton, Bad_radioButton;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_coach__current_class_note,container,false);
        global = (GlobalVars) getActivity().getApplication();

        Excellent_radioButton = root.findViewById(R.id.radioButton1);
        Good_radioButton = root.findViewById(R.id.radioButton2);
        Average_radioButton = root.findViewById(R.id.radioButton3);
        Bad_radioButton = root.findViewById(R.id.radioButton4);

        final RadioGroup radioGroup = root.findViewById(R.id.above_ll);
        String ex = getString(R.string.excellent)+ " " + getEmojiByUnicode(0x1F604);
        Excellent_radioButton.setText(ex);
        ex = getString(R.string.good) + " " +getEmojiByUnicode(0x1F642);
        Good_radioButton.setText(ex);
        ex = getString(R.string.average) + " " +getEmojiByUnicode(0x1F610);
        Average_radioButton.setText(ex);
        ex = getString(R.string.bad) + " " +getEmojiByUnicode(0x1F641);
        Bad_radioButton.setText(ex);
        progressDialog = new ProgressDialog(getContext());

        notes_edit_text = root.findViewById(R.id.notesEditText_coachcurrentclassnotesfragment);
        done_button = root.findViewById(R.id.doneFloatingActionButton_coachcurrentclassnotesfragment);

        done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selected_radioButton_id = radioGroup.getCheckedRadioButtonId();
                String emoji = "";

                if (selected_radioButton_id != -1) {
                    RadioButton selected = root.findViewById(selected_radioButton_id);
                    String emojis[] = selected.getText().toString().split(" ");

                    emoji = emojis[0] + " " ;

                    if (emojis[0].equals("Excellent"))
                        emoji += 0x1F604 ;
                    else if (emojis[0].equals("Good"))
                        emoji += 0x1F642;
                    else if(emojis[0].equals("Average"))
                        emoji += 0x1F610;
                    else if(emojis[0].equals("Bad"))
                        emoji += 0x1F641;
                }

                String notes = emoji + " \n " + notes_edit_text.getText().toString() ;
                updateClass(notes);
            }
        });





        return root;
    }

    private void updateClass(String notes) {
        progressDialog.show();

        info = (MyClass_info) getActivity().getIntent().getSerializableExtra("MyRunningClass");
        info.setClass_note(notes);
        List<Trainees_info> classTrainees = global.getMyDB().getClassTrainees(info.getClass_id());

        expectedCounter = classTrainees.size()+1;

        for (Trainees_info trainee : classTrainees)
            insertTrainee(trainee);

        finishClass();

    }

    private void finishClass() {
        try {
            JSONObject values = new JSONObject();
            values.put("status",4);
            values.put("class_notes",info.getClass_note());

            JSONObject where = new JSONObject();
            where.put("id",info.getClass_id());

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.updateData);
            final HashMap<String,String> params = new HashMap<>();
            params.put("table","classes");
            params.put("where",where.toString());
            params.put("values",values.toString());

            httpCall.setParams(params);

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    int allCounter = 0 , doneCounter = 0;
                    if(checkResponse(response)) {
                        doneCounter = doneAT.addAndGet(1);
                    }else
                        Log.d(TAG,"class update Error");
                    allCounter = allAT.addAndGet(1);
                    Log.d(TAG,"Class ADDing Result: "+allCounter+" "+doneCounter );
                    removeClass(allCounter,doneCounter);
                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void removeClass(int allCounter, int doneCounter) {
        Log.d(TAG,allCounter+" "+doneCounter +" "+expectedCounter);
        if (allCounter==doneCounter){
            if (allCounter == expectedCounter){
                boolean result = global.getMyDB().deleteClass(info);
                progressDialog.dismiss();
                if (result) {
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }else {
                    progressDialog.dismiss();
                    // error will deleting from SQLite
                }
            }
        }else{
            progressDialog.dismiss();
            // error will insertion info. or update class
        }
    }

    private void insertTrainee(Trainees_info trainee) {
        try {
            JSONObject values = new JSONObject();
            values.put("class_id",trainee.getClass_id());
            values.put("user_id",trainee.getTrainee_id());
            values.put("attend",trainee.getTrainee_attend());
            values.put("score",trainee.getTrainee_score());
            values.put("notes",trainee.getTrainee_note());


            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.insertData);
            final HashMap<String,String> params = new HashMap<>();
            params.put("table","class_info");
            params.put("values",values.toString());

            httpCall.setParams(params);

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    int allCounter = 0 , doneCounter = 0;
                    if(checkResponse(response)) {
                        doneCounter = doneAT.addAndGet(1);
                    }else
                        Log.d(TAG,"trainee insert Error");
                    allCounter = allAT.addAndGet(1);
                    Log.d(TAG,"Trainee ADDing Result: "+allCounter+" "+doneCounter );
                    removeClass(allCounter,doneCounter);
                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void insertRule(Rule_info ruleItem) {
        try {
            JSONObject values = new JSONObject();
            values.put("rule_id",ruleItem.getRule_id());
            values.put("class_id",ruleItem.getClass_id());
            values.put("note",ruleItem.getRule_note());
            values.put("user_id",global.getId());


            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.insertData);
            final HashMap<String,String> params = new HashMap<>();
            params.put("table","class_rules");
            params.put("values",values.toString());

            httpCall.setParams(params);

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    int allCounter = 0 , doneCounter = 0;
                    if(checkResponse(response)) {
                        doneCounter = doneAT.addAndGet(1);
                    }else
                        Log.d(TAG,"rule insert Error");
                    allCounter = allAT.addAndGet(1);
                    Log.d(TAG,"Rule ADDing Result: "+allCounter+" "+doneCounter );
                    removeClass(allCounter,doneCounter);
                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private boolean checkResponse(JSONArray response) {
        if (response != null){
            try {
                String result = response.getString(0);
                if (!result.equals("ERROR"))
                    return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public String getEmojiByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }
}
