package com.joshyolles.locateme;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    public final static String DATA = "com.joshyolles.locateme.MSG";
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Location mCurrentLocation;
    private String mLastUpdateTime;
    private LocationRequest mLocationRequest;
    private ArrayList<String> data;
    private String location;
    private TextView textView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private com.google.android.gms.common.api.GoogleApiClient client;

    private LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(3000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create an instance of GoogleAPIClient.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


        //LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                //.addLocationRequest(mLocationRequest);

        //PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        /*result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates states = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // satisfied. can initialize location requests from here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // not satisfied, can be fixed:
                        //try {
                        // show dialog and check result
                        //status.startResolutionForResult(OuterClass.this,REQUEST_CHECK_SETTINGS);
                        // } catch(IntentSender.SendIntentException e) {
                        // ignore error
                        //}
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // not satisfied and can't fix either
                        break;
                }
            }
        });
        */

        textView = new TextView(this);
        textView.setTextSize(17);
        ViewGroup layout = (ViewGroup)findViewById(R.id.main_activity);
        layout.addView(textView);
    }

    public void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,this);
    }
/*
    public void displayCoords(View view) throws IOException {

        Intent intent = new Intent(this, DisplayLocationActivity.class);

        String address = addresses.get(0).getAddressLine(0);
        String city = addresses.get(0).getLocality();
        String country = addresses.get(0).getCountryName();
        String latitude = String.valueOf(addresses.get(0).getLatitude());
        String longitude = String.valueOf(addresses.get(0).getLongitude());
        data.add(address);
        data.add(city);
        data.add(country);
        data.add(latitude);
        data.add(longitude);
        intent.putStringArrayListExtra("test", data);
        startActivity(intent);
    }
    */

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationRequest = createLocationRequest();
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        mCurrentLocation = mLastLocation;
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        try {
            updateUI();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateUI() throws IOException {
        String lat = String.valueOf(mCurrentLocation.getLatitude());
        String longitude = String.valueOf(mCurrentLocation.getLongitude());
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocation(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude(),1);
        if(addresses.get(0).getAddressLine(0).equals("24202 Palmek Circle"))
            textView.setText("Welcome Home!\n\nLat: " + lat + "\n\nLong: " + longitude + "\n\nAddress: " + addresses.get(0).getAddressLine(0) + "\n\nCity: " + addresses.get(0).getLocality() + "\n\nCountry: " + addresses.get(0).getCountryCode());
        else
            textView.setText("Lat: " + lat + "\n\nLong: " + longitude + "\n\nAddress: " + addresses.get(0).getAddressLine(0) + "\n\nCity: " + addresses.get(0).getLocality() + "\n\nCountry: " + addresses.get(0).getCountryCode());
    }
}
