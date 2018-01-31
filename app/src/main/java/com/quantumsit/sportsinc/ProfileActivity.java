package com.quantumsit.sportsinc;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class ProfileActivity extends AppCompatActivity {

    static final String TAG = ProfileActivity.class.getSimpleName();

    GlobalVars globalVars;
    ProgressDialog progressDialog;

    EditText Name ,Phone ,Mail;
    TextView ChangePassword,Gender , DateOfBirth;
    Button Edit, Save ,Cancel;
    LinearLayout EditButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        globalVars = (GlobalVars) getApplication();
        progressDialog = new ProgressDialog(ProfileActivity.this);
        progressDialog.setMessage("Saving...");

        Name = findViewById(R.id.profile_userName);
        Phone = findViewById(R.id.profile_userPhone);
        Mail = findViewById(R.id.profile_userMail);
        Gender = findViewById(R.id.profile_gender);
        DateOfBirth = findViewById(R.id.profile_date_birth);

        ChangePassword = findViewById(R.id.profile_change_password);

        Edit = findViewById(R.id.profile_edit_btn);
        Save = findViewById(R.id.profile_save_btn);
        Cancel = findViewById(R.id.profile_cancel_btn);

        EditButtons = findViewById(R.id.EDitButtons);


        ChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeProfilePassword();
            }
        });

        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeProfileEditable(true);
                ChangePassword.setVisibility(View.VISIBLE);
                EditButtons.setVisibility(View.VISIBLE);
                Edit.setVisibility(View.GONE);
            }
        });

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                updateProfile();
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editableProfile();
                fillProfileData();
            }
        });

        fillProfileData();
    }

    private void editableProfile(){

        makeProfileEditable(false);
        ChangePassword.setVisibility(View.GONE);
        EditButtons.setVisibility(View.GONE);
        Edit.setVisibility(View.VISIBLE);
    }
    private void fillProfileData() {
        Name.setText(globalVars.getName());
        Phone.setText(globalVars.getPhone());
        Mail.setText(globalVars.getMail());
        Gender.setText(globalVars.getPersonGender());
        DateOfBirth.setText(globalVars.getDate_of_birth());
    }

    private void changeProfilePassword() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ProfileActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.change_password_dialog,null);

        final EditText OldPassword = mView.findViewById(R.id.OldPassword);
        final EditText NewPassword = mView.findViewById(R.id.MyNewPassword);
        final EditText ConfirmPassword = mView.findViewById(R.id.ConfirmMyNewPassword);

        mBuilder.setView(mView);

        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton("Change Password", new DialogInterface.OnClickListener() {@Override public void onClick(DialogInterface dialogInterface, int i) { }});
        mBuilder.setNegativeButton("Cancel",null);
        final AlertDialog alertdialog = mBuilder.create();
        alertdialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = alertdialog.getButton((AlertDialog.BUTTON_POSITIVE));
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Write your Logic. It will never dismiss the dialog unless your condition satisifies
                        if(checkProfileOldPassword(OldPassword.getText().toString())){
                            String pass = NewPassword.getText().toString();
                            String Confir = ConfirmPassword.getText().toString();
                            if (!TextUtils.isEmpty(NewPassword.getText()) &&pass.equals(Confir)){
                                updatePassword(pass,alertdialog);
                            }
                        }
                    }
                });
            }
        });
        alertdialog.show();
    }


    private void updateProfile() {
        try {
            final String NewName = Name.getText().toString();
            final String NewMail = Mail.getText().toString();
            final String NewPhone = Phone.getText().toString();

            JSONObject values = new JSONObject();
            values.put("name",NewName);
            values.put("phone",NewPhone);
            values.put("email",NewMail);

            JSONObject where = new JSONObject();
            where.put("id",globalVars.getId());

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.updateData);
            final HashMap<String,String> params = new HashMap<>();
            params.put("table","users");
            params.put("where",where.toString());
            params.put("values",values.toString());

            httpCall.setParams(params);

            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    if (checkResponse(response)){
                        globalVars.setName(NewName);
                        globalVars.setMail(NewMail);
                        globalVars.setPhone(NewPhone);
                        editableProfile();
                        Toast.makeText(ProfileActivity.this,"Changing saved",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(ProfileActivity.this,"Edit Fail",Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                }
            }.execute(httpCall);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updatePassword(final String pass, final AlertDialog alertdialog) {
        try {
            JSONObject values = new JSONObject();
            values.put("pass",pass);

            JSONObject where = new JSONObject();
            where.put("id",globalVars.getId());

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl(Constants.updateData);
            final HashMap<String,String> params = new HashMap<>();
            params.put("table","users");
            params.put("where",where.toString());
            params.put("values",values.toString());

            httpCall.setParams(params);

            progressDialog.show();
            alertdialog.dismiss();
            new HttpRequest(){
                @Override
                public void onResponse(JSONArray response) {
                    super.onResponse(response);
                    Log.d(TAG, params.toString());
                    Log.d(TAG, "Update Response: " + String.valueOf(response));
                    if(checkResponse(response)) {
                        Toast.makeText(ProfileActivity.this, "Password Changed", Toast.LENGTH_SHORT).show();
                        globalVars.setPass(pass);
                    }else {
                        Toast.makeText(ProfileActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
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
                if (result.equals("DONE"))
                    return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean checkProfileOldPassword(String password) {
        if(!TextUtils.isEmpty(password)){
            if(password.equals(globalVars.getPass()))
                return true;
        }
        return false;
    }

    private void makeProfileEditable(boolean b) {
        Name.setEnabled(b);
        Phone.setEnabled(b);
        Mail.setEnabled(b);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
