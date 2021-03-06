package com.tbg.yamoov.model.implement;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tbg.yamoov.core.Imodel;
import com.tbg.yamoov.core.Ipresenteur;

public class LoginImplement implements Imodel.Ilogin {

    Ipresenteur.LoginPresenteur loginPresenteur;
    FirebaseAuth fAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    boolean user;

    public LoginImplement(Ipresenteur.LoginPresenteur loginPresenteur) {
        this.loginPresenteur = loginPresenteur;
    }

    @Override
    public void loginAccount(String email, String password) {

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    loginPresenteur.pSuccess("Connexion");
                }else {
                    loginPresenteur.pError("Erreur de connexion vérifier vos informations");
                }
            }
        });

    }

    @Override
    public Boolean loadUser() {

        if ( FirebaseAuth.getInstance().getCurrentUser() != null){
            return user = true;
        }
        return user;
    }

    public void selectUser(){
        db.collection("utilisateurs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                               // Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                           // Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
