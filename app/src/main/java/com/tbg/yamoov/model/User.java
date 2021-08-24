package com.tbg.yamoov.model;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tbg.yamoov.LoginActivity;
import com.tbg.yamoov.R;
import com.tbg.yamoov.RegisterActivity;
import com.tbg.yamoov.core.Imodel;
import com.tbg.yamoov.core.Ipresenteur;

import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class User  {


   private String nom;
   private String email;
   private String password;
   private String phone;
   private String date;




    public User(String nom, String email, String password, String phone, String date) {
        this.nom = nom;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.date = date;
    }

}
