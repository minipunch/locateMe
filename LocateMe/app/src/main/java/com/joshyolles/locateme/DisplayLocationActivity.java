package com.joshyolles.locateme;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DisplayLocationActivity extends AppCompatActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks {
    private ArrayList<String> data;
    List<Address> addresses;
    String location;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_location);

        data = new ArrayList<String>();

        Intent intent = getIntent();
        data = intent.getExtras().getStringArrayList("test");
        location = "Address:\n" +
                    data.get(0) +
                    "\n\nCity: " +
                    data.get(1) +
                    "\n\nCountry:\n" +
                    data.get(2) +
                    "\n\nLatitude:\n" +
                    data.get(3) +
                    "\n\nLatitude:\n" +
                    data.get(4);
        textView = new TextView(this);
        textView.setTextSize(17);
        textView.setText(location);

        ViewGroup layout = (ViewGroup)findViewById(R.id.activity_display_location);
        layout.addView(textView);
    }

    @Override
    public void onLocationChanged(Location location) {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String locationStr = "Address:\n" +
                addresses.get(0).getAddressLine(0)+
                "\n\nCity: " +
                addresses.get(0).getLocality() +
                "\n\nCountry:\n" +
                addresses.get(0).getCountryName() +
                "\n\nLatitude:\n" +
                addresses.get(0).getLatitude() +
                "\n\nLongitude:\n" +
                addresses.get(0).getLongitude();
        textView.setText(locationStr);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
