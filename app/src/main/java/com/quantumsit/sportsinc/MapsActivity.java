package com.quantumsit.sportsinc;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Double Lat,Lng;
    private String Title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        String Address_Lat = getIntent().getStringExtra("Academy_Lat");
        if (Address_Lat.equals(""))
            Lat = 0.0;
        else
            Lat = Double.parseDouble(Address_Lat);
        String Address_Lng = getIntent().getStringExtra("Academy_Lng");
        if (Address_Lng.equals(""))
            Lng = 0.0;
        else
            Lng = Double.parseDouble(Address_Lng);
        Title = getIntent().getStringExtra("Academy_Name");
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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(Lat, Lng);
        mMap.addMarker(new MarkerOptions().position(sydney).title(Title));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12.0f));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12.0f));
    }
}
