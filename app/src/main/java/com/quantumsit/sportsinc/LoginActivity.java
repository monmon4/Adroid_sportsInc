package com.quantumsit.sportsinc;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.quantumsit.sportsinc.Aaa_data.GlobalVars;

public class LoginActivity extends AppCompatActivity {

    GlobalVars globalVars;

    TextView register_textview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        globalVars = (GlobalVars) getApplication();

        register_textview = findViewById(R.id.registerTextView_login);
        register_textview.setPaintFlags(register_textview.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        register_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    public void loginClicked(View view) {

        globalVars.setUser_is(3);
        Intent intent= new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
    }


}
