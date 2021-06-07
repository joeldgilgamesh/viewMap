package com.tbg.yamoov.model.implement;

import com.google.firebase.auth.FirebaseAuth;
import com.tbg.yamoov.core.Imodel;
import com.tbg.yamoov.core.Ipresenteur;

public class ProfilImplement implements Imodel.iprofil {

    Ipresenteur.ProfilPresenteur profilPresenteur;

    public ProfilImplement(Ipresenteur.ProfilPresenteur profilPresenteur) {
        this.profilPresenteur = profilPresenteur;
    }

    @Override
    public void logOut() {
        FirebaseAuth.getInstance().signOut();
        profilPresenteur.pSuccess("Déconnexion Terminer");
    }
}
