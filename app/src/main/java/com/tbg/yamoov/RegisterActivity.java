package com.tbg.yamoov;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
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
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tbg.yamoov.core.Ipresenteur;
import com.tbg.yamoov.core.Ivue;
import com.tbg.yamoov.presenteur.RegisterPresenteur;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity implements Ivue.RegisterVue, LocationListener {

    Button btn,btnCreate,btnLocal;
    EditText nomU, emailU, passU, phoneU, datebU;
    ProgressBar progressBar;
    Ipresenteur.RegisterPresenteur ipresenteur;
    FusedLocationProviderClient fusedLocationProviderClient;
    Ipresenteur.RegisterPresenteur registerPresenteur;
    LocationManager locationManager;

    private FirebaseAuth fAuth ;
// ...
// Initialize Firebase Auth

    private FirebaseFirestore fstore = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ipresenteur = new RegisterPresenteur(this);

        btn = (Button) findViewById(R.id.joinus);
        btnCreate = (Button) findViewById(R.id.create);
        btnLocal = (Button) findViewById(R.id.localisation);

        nomU = (EditText) findViewById(R.id.nomUser);
        emailU = (EditText) findViewById(R.id.emailUser);
        passU = (EditText) findViewById(R.id.passUser);
        phoneU = (EditText) findViewById(R.id.phoneUser);
        datebU = (EditText) findViewById(R.id.datebUser);
        progressBar = findViewById(R.id.progressBar);
        //




        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override


            public void onClick(View v) {
                String  nomUt =  nomU.getText().toString();
                String  emailUt =  emailU.getText().toString();
                String  passUt =  passU.getText().toString();
                String  phoneUt =  phoneU.getText().toString();

                if (TextUtils.isEmpty(nomUt)){
                    nomU.setError("Entrer votre nom");
                }else if(TextUtils.isEmpty(emailUt)){
                    emailU.setError("Entrer votre email");
                }else if(passUt.length()< 8){
                    passU.setError("Entrer plus de 8 caractéres");
                }else if(TextUtils.isEmpty(phoneUt)){
                    nomU.setError("Entrer votre nom");
                }else {
                    ipresenteur.onSetProgressBarVisibility(View.VISIBLE);
                    ipresenteur.createAccount(
                            nomUt.trim(),
                            emailUt.trim(),
                            passUt.trim(),
                            phoneUt.trim()
                    );

                }

            }
        });

        btnLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grantPermission();
                checkLocationIsEnableOrNot();
                getLocation();
            }
        });
    }

    public void gotoNext(View view) {
        Intent dsp = new Intent(RegisterActivity.this,MainActivity.class);
        startActivity(dsp);
        finish();
    }

    public void LognIn(View view) {
        /* Intent ds = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(ds);
        finish();*/
        ipresenteur.logOut();
        finish();
    }
    private void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500,5,(LocationListener) this);
        }catch (SecurityException e){
            e.printStackTrace();
        }

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
            new AlertDialog.Builder(RegisterActivity.this)
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
    public void onLocationChanged(Location location) {
        try {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(), 1);
           /* latit.setText(String.valueOf(addresses.get(0).getAdminArea()));
            longit.setText(String.valueOf(addresses.get(0).getAddressLine(0)));
            count.setText(String.valueOf(addresses.get(0).getCountryName()));
            local.setText(String.valueOf(addresses.get(0).getLocality()));
            adr.setText(String.valueOf(addresses.get(0).getLongitude()));*/
            new AlertDialog.Builder(RegisterActivity.this)
                    .setTitle("Votre position actuelle")
                    .setMessage(
                                String.valueOf(addresses.get(0).getCountryName()) +"\n"+
                                String.valueOf(addresses.get(0).getAdminArea()) +"\n"+
                                String.valueOf(addresses.get(0).getLocality()) +"\n"+
                                "Longitude :"+String.valueOf(addresses.get(0).getLongitude()) +"\n"+
                                "Latitude :" +String.valueOf(addresses.get(0).getLatitude())
                                )
                    .setCancelable(false)
                    .setNegativeButton("Cancel",null)
                    .show();

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

    @Override
    public void success(String message) {
        ipresenteur.onSetProgressBarVisibility(View.INVISIBLE);
        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
        Intent dsp = new Intent(RegisterActivity.this,MainActivity.class);
        startActivity(dsp);
        finish();
    }

    @Override
    public void error(String message) {
        ipresenteur.onSetProgressBarVisibility(View.INVISIBLE);
        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSetProgressBarVisibility(int visibility) {
        progressBar.setVisibility(visibility);
    }

    @Override
    public void onPause() {
        super.onPause();
       // if (VERBOSE) Log.v(TAG, "- ON PAUSE -");
    }

    @Override
    public void onStop() {
        super.onStop();
       // if (VERBOSE) Log.v(TAG, "-- ON STOP --");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       // if (VERBOSE) Log.v(TAG, "- ON DESTROY -");
    }
}
