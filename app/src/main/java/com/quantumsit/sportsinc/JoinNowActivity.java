package com.quantumsit.sportsinc;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import javax.xml.datatype.Duration;

public class JoinNowActivity extends AppCompatActivity {

    TextView location_textview, phone_textview;

    MaterialBetterSpinner date_spinner, time_spinner ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_now);

        location_textview = findViewById(R.id.locationTextView_joinnow);
        phone_textview = findViewById(R.id.phoneTextView_joinnow);

        date_spinner = findViewById(R.id.dateSpinner_joinnow);
        time_spinner = findViewById(R.id.timeSpinner_joinnow);

        ArrayAdapter<CharSequence> date_adapter = ArrayAdapter.createFromResource(JoinNowActivity.this, R.array.date_array,android.R.layout.simple_spinner_item );
        date_adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        date_spinner.setAdapter(date_adapter);
        ArrayAdapter<CharSequence> time_adapter = ArrayAdapter.createFromResource(JoinNowActivity.this, R.array.date_array,android.R.layout.simple_spinner_item );
        time_adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        time_spinner.setAdapter(time_adapter);

    }

    public void go_to_maps(View view) {

        Intent intent = new Intent(JoinNowActivity.this, MapsActivity.class);
        startActivity(intent);
    }

    //call btn is pressed
    public void on_call(View view) {

        String academy_phone = "0115703711";

        try {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + academy_phone));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                Toast.makeText(JoinNowActivity.this,"need a permission to call", Toast.LENGTH_SHORT).show();
                return;
            }
            startActivity(callIntent);
        } catch (ActivityNotFoundException activityException) {
            Log.e("Calling a Phone Number", "Call failed", activityException);
        }
    }
}
