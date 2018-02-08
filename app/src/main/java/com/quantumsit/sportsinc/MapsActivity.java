package com.quantumsit.sportsinc;

import android.*;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.quantumsit.sportsinc.Aaa_data.Academy_info;
import com.quantumsit.sportsinc.Aaa_data.Constants;
import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.Aaa_looks.ListView_Adapter_contact_us;
import com.quantumsit.sportsinc.Aaa_looks.item_contact_us;
import com.quantumsit.sportsinc.Backend.HttpCall;
import com.quantumsit.sportsinc.Backend.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class MapsActivity extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Double lat=0.0, lng=0.0;
    private String Title;

    ListView openingHours_listView;
    ListView_Adapter_contact_us listView_adapter;
    ArrayList<item_contact_us> list_items;

    Academy_info academy_info;
    GlobalVars globalVars;

    MapView mapView;
    TextView maps_textView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_maps,container,false);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //mapFragment = (SupportMapFragment) root.getSupportFragmentManager().findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);

        // Gets the MapView from the XML layout and creates it
        mapView = root.findViewById(R.id.map);
        maps_textView = root.findViewById(R.id.maps_textView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);



        globalVars = (GlobalVars) getActivity().getApplication();
        academy_info = new Academy_info();

        if (globalVars.getMyDB().Academy_empty())
            get_info();

        else
            academy_info = globalVars.getMyDB().getAcademyInfo();
            setLatandLng();
            mapView.onStart();

        openingHours_listView = root.findViewById(R.id.openingHoursListView_maps);
        list_items = new ArrayList<>();

        list_items.add(new item_contact_us("Saturday", "09:00 ~ 06:00"));
        list_items.add(new item_contact_us("Sunday", "09:00 ~ 06:00"));
        list_items.add(new item_contact_us("Monday", "09:00 ~ 06:00"));
        list_items.add(new item_contact_us("Tuesday", "09:00 ~ 06:00"));
        list_items.add(new item_contact_us("Thursday", "09:00 ~ 06:00"));
        list_items.add(new item_contact_us("Wednesday", "09:00 ~ 06:00"));

        listView_adapter = new ListView_Adapter_contact_us(getActivity(), list_items);
        openingHours_listView.setAdapter(listView_adapter);

        TextView call = root.findViewById(R.id.callTextView_contactus);
        TextView email = root.findViewById(R.id.emailTextView_contactus);
        TextView directions = root.findViewById(R.id.direcctionTextView_contactus);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call_academy();
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_academy();
            }
        });

        directions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                direction_academy();
            }
        });

        return root;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker and move the camera
        LatLng academy = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(academy).title(Title));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12.0f));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(academy, 12.0f));
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        Checklatlng();
    }

    @SuppressLint("StaticFieldLeak")
    private void get_info() {
        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.POST);
        httpCall.setUrl(Constants.selectData);
        HashMap<String, String> params = new HashMap<>();
        params.put("table", "info_academy");

        httpCall.setParams(params);
        //progressDialog.show();
        new HttpRequest() {
            @Override
            public void onResponse(JSONArray response) {
                super.onResponse(response);
                try {

                    if (response != null) {

                        JSONObject result = response.getJSONObject(0);
                        academy_info.setName(result.getString("name"));
                        academy_info.setAddress(result.getString("address"));
                        academy_info.setLat(result.getString("address_Lat"));
                        academy_info.setLng(result.getString("address_Lng"));
                        academy_info.setPhone(result.getString("phone"));
                        academy_info.setEmail(result.getString("email"));

                        globalVars.getMyDB().addAcademyInfo(academy_info);
                        setLatandLng();
                        mapView.onStart();



                    } else {
                        show_toast("Error will get Academy Information.");
                    }
                    //progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }.execute(httpCall);
    }

    private void setLatandLng() {

        if (!academy_info.getLat().equals(""))
            lat = Double.parseDouble(academy_info.getLat());
        else
            lat = 0.0;

        if (!academy_info.getLng().equals(""))
            lng = Double.parseDouble(academy_info.getLng());
        else
            lng = 0.0;

        Title = academy_info.getName();
    }

    private void show_toast(String s) {
        Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();
    }

    public void call_academy() {

        String academy_phone = academy_info.getPhone();


        try {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + academy_phone));
            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                Toast.makeText(getActivity(),"need a permission to call", Toast.LENGTH_SHORT).show();
                return;
            }
            startActivity(callIntent);
        } catch (ActivityNotFoundException activityException) {
            Log.e("Calling a Phone Number", "Call failed", activityException);
        }

    }

    public void email_academy() {

        //mail_card_view.setVisibility(View.VISIBLE);
        Intent mail_intent = new Intent(Intent.ACTION_SENDTO);
        //mail_intent.setType("message/rfc822");
        mail_intent.putExtra(Intent.EXTRA_EMAIL  , new String[]{academy_info.getAddress()});
        mail_intent.setData(Uri.parse("mailto:" + academy_info.getAddress()));
        mail_intent.putExtra(Intent.EXTRA_PHONE_NUMBER, academy_info.getPhone());

        try {
            startActivity(Intent.createChooser(mail_intent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }

    }

    public void direction_academy() {
        String geoUri = "http://maps.google.com/maps?q=loc:" + lat + "," + lng + " (" + academy_info.getName() + ")";
        String uri = String.format(Locale.ENGLISH, "geo:%f,%f", lat, lng);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
        //Checklatlng();
    }


    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        //Checklatlng();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
        //Checklatlng();
    }

    private  void Checklatlng(){
        if(lat == 0.0 && lng == 0.0) {
            maps_textView.setVisibility(View.VISIBLE);
        } else {
            maps_textView.setVisibility(View.INVISIBLE);
        }
    }
}
