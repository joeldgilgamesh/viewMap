package com.tbg.yamoov;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tbg.yamoov.core.Ipresenteur;
import com.tbg.yamoov.core.Ivue;
import com.tbg.yamoov.presenteur.LoginPresenteur;

public class LoginActivity extends AppCompatActivity implements Ivue.LoginVue {

    ImageView img;
    Button btnLog;
    EditText passL, emailL;
    ProgressBar progressBar;
    Ipresenteur.LoginPresenteur loginPresenteur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        img = findViewById(R.id.GoBackIcon);
        loginPresenteur = new LoginPresenteur(this);

        passL = (EditText) findViewById(R.id.password);
        emailL = (EditText) findViewById(R.id.email);
        btnLog = (Button) findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBar2);

        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  emailUt =  emailL.getText().toString();
                String  passUt =  passL.getText().toString();

                if (TextUtils.isEmpty(emailUt)){
                    emailL.setError("Entrer votre email");
                }else if(passUt.length()< 8){
                    passL.setError("Entrer plus de 8 caractéres");
                }else{
                    loginPresenteur.onSetProgressBarVisibility(View.VISIBLE);
                    loginPresenteur.loginuser(emailUt.trim(),passUt.trim());
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(loginPresenteur.loadUser() == true){
            Intent dsp = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(dsp);
            finish();
            Toast.makeText(LoginActivity.this, "Content de vous revoir", Toast.LENGTH_SHORT).show();
        }

    }

    public void goback(View view) {
        Intent ic = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(ic);
        finish();
    }

    @Override
    public void success(String message) {
        loginPresenteur.onSetProgressBarVisibility(View.INVISIBLE);
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
        Intent dsp = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(dsp);
        finish();
    }

    @Override
    public void error(String message) {
        loginPresenteur.onSetProgressBarVisibility(View.INVISIBLE);
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSetProgressBarVisibility(int visibility) {
        progressBar.setVisibility(visibility);
    }
}
