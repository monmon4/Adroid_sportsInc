package com.quantumsit.sportsinc;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class ProfileActivity extends AppCompatActivity {

    EditText Name ,Phone ,Mail;
    TextView ChangePassword;
    Button Edit, Save ,Cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Name = findViewById(R.id.profile_userName);
        Phone = findViewById(R.id.profile_userPhone);
        Mail = findViewById(R.id.profile_userMail);

        ChangePassword = findViewById(R.id.profile_change_password);

        Edit = findViewById(R.id.profile_edit_btn);
        Save = findViewById(R.id.profile_save_btn);
        Cancel = findViewById(R.id.profile_cancel_btn);

        final LinearLayout EditButtons = findViewById(R.id.EDitButtons);


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
                EditButtons.setVisibility(View.VISIBLE);
                Edit.setVisibility(View.GONE);
            }
        });

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeProfileEditable(false);
                EditButtons.setVisibility(View.GONE);
                Edit.setVisibility(View.VISIBLE);
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeProfileEditable(false);
                EditButtons.setVisibility(View.GONE);
                Edit.setVisibility(View.VISIBLE);
            }
        });
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
                                Toast.makeText(ProfileActivity.this,"Password Changed",Toast.LENGTH_SHORT).show();
                                alertdialog.dismiss();
                            }
                        }
                    }
                });
            }
        });
        alertdialog.show();
    }

    private boolean checkProfileOldPassword(String password) {
        if(!TextUtils.isEmpty(password)){
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
