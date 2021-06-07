package com.tbg.yamoov;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.LocaleList;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.tbg.yamoov.core.Ipresenteur;
import com.tbg.yamoov.core.Ivue;
import com.tbg.yamoov.presenteur.ProfilePresenteur;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ProfilActivity extends AppCompatActivity implements Ivue.ProfilVue, LocationListener {

    Button btn,btnp;
    EditText nomp,emailp,passp,phonep,adressp;
    Ipresenteur.ProfilPresenteur profilPresenteur;
    FusedLocationProviderClient fusedLocationProviderClient;
    Ipresenteur.RegisterPresenteur registerPresenteur;
    LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        grantPermission();
        checkLocationIsEnableOrNot();
        getLocation();
        profilPresenteur = new ProfilePresenteur(this);
      //  registerPresenteur = new R
        btn = findViewById(R.id.joinus);
        btnp = findViewById(R.id.button3);
        nomp = findViewById(R.id.nom);
        emailp = findViewById(R.id.email);
        passp = findViewById(R.id.pass);
        phonep = findViewById(R.id.phone);
        adressp = findViewById(R.id.adress);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        btnp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // profilPresenteur.
            }
        });

    }

    private void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500,5,(LocationListener) this);
        }catch (SecurityException e){
            e.printStackTrace();
        }

    }

    public void gotoNext(View view) {

        Intent dsp = new Intent(ProfilActivity.this,MainActivity.class);
        startActivity(dsp);
    }

    public void LognIn(View view) {
      profilPresenteur.logOut();

    }

    private void grantPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }
    }
    private void checkLocationIsEnableOrNot(){
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnable = false;
        boolean networkEnable = false;

        try {
            gpsEnable = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            networkEnable = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }catch (Exception e){
            e.printStackTrace();
        }

        if (!gpsEnable && !networkEnable){
            new AlertDialog.Builder(ProfilActivity.this)
                    .setTitle("Activé service GPS")
                    .setCancelable(false)
                    .setPositiveButton("activé", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    }).setNegativeButton("Cancel",null)
                    .show();
        }
    }


    @Override
    public void successLogout(String message) {
        Toast.makeText(ProfilActivity.this, message, Toast.LENGTH_SHORT).show();
        Intent dsp = new Intent(ProfilActivity.this,LoginActivity.class);
        startActivity(dsp);
        finish();
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(), 1);
           /* latit.setText(String.valueOf(addresses.get(0).getAdminArea()));
            longit.setText(String.valueOf(addresses.get(0).getAddressLine(0)));
            count.setText(String.valueOf(addresses.get(0).getCountryName()));
            local.setText(String.valueOf(addresses.get(0).getLocality()));
            adr.setText(String.valueOf(addresses.get(0).getLongitude()));*/

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
