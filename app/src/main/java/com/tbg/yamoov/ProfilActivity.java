package com.tbg.yamoov;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.LocaleList;
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

public class ProfilActivity extends AppCompatActivity implements Ivue.ProfilVue {

    Button btn,btnp;
    EditText latit,longit,count,local,adr;
    Ipresenteur.ProfilPresenteur profilPresenteur;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        profilPresenteur = new ProfilePresenteur(this);
        btn = findViewById(R.id.joinus);
        btnp = findViewById(R.id.button3);
        latit = findViewById(R.id.latitude);
        longit = findViewById(R.id.longitude);
        count = findViewById(R.id.country);
        local = findViewById(R.id.locality);
        adr = findViewById(R.id.adress);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        btnp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fusedLocationProviderClient.getLastLocation()
                        .addOnCompleteListener(new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location != null){

                                    try {
                                        Geocoder geocoder = new Geocoder(ProfilActivity.this, Locale.getDefault());
                                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                                        latit.setText(String.valueOf(addresses.get(0).getLatitude()));
                                        longit.setText(String.valueOf(addresses.get(0).getLongitude()));
                                        count.setText(String.valueOf(addresses.get(0).getCountryName()));
                                        local.setText(String.valueOf(addresses.get(0).getLocality()));
                                        adr.setText(String.valueOf(addresses.get(0).getAddressLine(0)));

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }


                            }
                        });
            }
        });
    }

    public void gotoNext(View view) {
        Intent dsp = new Intent(ProfilActivity.this,MainActivity.class);
        startActivity(dsp);
    }

    public void LognIn(View view) {
      profilPresenteur.logOut();

    }

    @Override
    public void successLogout(String message) {
        Toast.makeText(ProfilActivity.this, message, Toast.LENGTH_SHORT).show();
        Intent dsp = new Intent(ProfilActivity.this,LoginActivity.class);
        startActivity(dsp);
        finish();
    }
}
