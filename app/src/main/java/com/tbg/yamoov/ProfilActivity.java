package com.tbg.yamoov;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ProfilActivity extends AppCompatActivity {
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        btn = findViewById(R.id.joinus);

    }

    public void gotoNext(View view) {
        Intent dsp = new Intent(ProfilActivity.this,MainActivity.class);
        startActivity(dsp);
    }

    public void LognIn(View view) {
       /* Intent ds = new Intent(ProfilActivity.this,LoginActivity.class);
        startActivity(ds);*/

    }
}
