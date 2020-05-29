package com.tbg.yamoov.model.implement;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tbg.yamoov.core.Imodel;
import com.tbg.yamoov.core.Ipresenteur;

import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class RegisterImplement implements Imodel.Iregister {

    private FirebaseAuth fAuth ;
// ...
// Initialize Firebase Auth
    private String userID;
    private FirebaseFirestore fstore = FirebaseFirestore.getInstance();

    Ipresenteur.RegisterPresenteur registerPresenteur;
    public RegisterImplement(Ipresenteur.RegisterPresenteur registerPresenteur) {
        this.registerPresenteur = registerPresenteur;
    }



    @Override
    public void createAccount(String nom, String email, String password, String phone, String date) {

        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            userID = FirebaseAuth.getInstance().getUid();
                           // DocumentReference documentReference = fstore.collection(users).document(userId);
                            DocumentReference documentReference = fstore.collection("utilisateurs").document(userID);
                            Map<String, Object> user = new HashMap<>();
                            user.put("nom", nom);
                            user.put("email", email);
                            user.put("password", password);
                            user.put("phone", phone);
                            user.put("date", date);
                            user.put("position", "");
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, userID + "task.getException().getMessage()");
                                }
                            });
                            registerPresenteur.pSuccess("utilisateur créé");


                        }else{
                            registerPresenteur.pError("erreur de creation");
                        }
                    }
                });
    }

}
