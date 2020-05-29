package com.tbg.yamoov;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tbg.yamoov.core.Ipresenteur;
import com.tbg.yamoov.core.Ivue;
import com.tbg.yamoov.presenteur.ProfilePresenteur;

public class ProfilActivity extends AppCompatActivity implements Ivue.ProfilVue {
    Button btn;
    Ipresenteur.ProfilPresenteur profilPresenteur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        profilPresenteur = new ProfilePresenteur(this);
        btn = findViewById(R.id.joinus);

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
