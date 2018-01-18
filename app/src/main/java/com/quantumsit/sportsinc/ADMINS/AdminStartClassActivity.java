package com.quantumsit.sportsinc.ADMINS;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.quantumsit.sportsinc.R;

public class AdminStartClassActivity extends AppCompatActivity {

    EditText note_editText;
    CheckBox coach_name_checkBox;

    String note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_start_class);

        note_editText = findViewById(R.id.notesEditText_admincurrentclass);
        coach_name_checkBox = findViewById(R.id.coachNameCheckBox_admincurrentclass);
    }

    public void done_pressed(View view) {

        note = note_editText.getText().toString();
        onBackPressed();
        finish();

    }
}
