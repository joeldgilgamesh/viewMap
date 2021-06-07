package com.tbg.yamoov.presenteur;

import com.tbg.yamoov.core.Ipresenteur;
import com.tbg.yamoov.core.Ivue;
import com.tbg.yamoov.model.implement.ProfilImplement;

public class ProfilePresenteur implements Ipresenteur.ProfilPresenteur {

    Ivue.ProfilVue profilVue;
    ProfilImplement profilImplement;

    public ProfilePresenteur(Ivue.ProfilVue profilVue) {
        this.profilVue = profilVue;
        profilImplement = new ProfilImplement(this);
    }

    @Override
    public void logOut() {
        profilImplement.logOut();
    }

    @Override
    public void pSuccess(String message) {
        profilVue.successLogout(message);
    }
}
