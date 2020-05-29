package com.tbg.yamoov;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tbg.yamoov.core.Ipresenteur;
import com.tbg.yamoov.core.Ivue;
import com.tbg.yamoov.presenteur.RegisterPresenteur;

public class RegisterActivity extends AppCompatActivity implements Ivue.RegisterVue {

    Button btn,btnCreate;
    EditText nomU, emailU, passU, phoneU, datebU;
    ProgressBar progressBar;
    Ipresenteur.RegisterPresenteur ipresenteur;
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
                String  datebUt =  datebU.getText().toString();

                if (TextUtils.isEmpty(nomUt)){
                    nomU.setError("Entrer votre nom");
                }else if(TextUtils.isEmpty(emailUt)){
                    emailU.setError("Entrer votre email");
                }else if(passUt.length()< 8){
                    passU.setError("Entrer plus de 8 caractéres");
                }else if(TextUtils.isEmpty(phoneUt)){
                    nomU.setError("Entrer votre nom");
                }else if(TextUtils.isEmpty(datebUt)){
                    nomU.setError("Entrer votre nom");
                }else {
                    ipresenteur.onSetProgressBarVisibility(View.VISIBLE);
                    ipresenteur.createAccount(
                            nomUt.trim(),
                            emailUt.trim(),
                            passUt.trim(),
                            phoneUt.trim(),
                            datebUt.trim()
                    );

                }

            }
        });
    }

    public void gotoNext(View view) {
        Intent dsp = new Intent(RegisterActivity.this,MainActivity.class);
        startActivity(dsp);
        finish();
    }

    public void LognIn(View view) {
        /* */Intent ds = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(ds);
        finish();

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
}
