package com.tbg.yamoov.presenteur;

import com.tbg.yamoov.core.Ipresenteur;
import com.tbg.yamoov.core.Ivue;
import com.tbg.yamoov.model.implement.RegisterImplement;

public class RegisterPresenteur implements Ipresenteur.RegisterPresenteur {

    Ivue.RegisterVue registerVue;
    RegisterImplement registerImplement;

    public RegisterPresenteur(Ivue.RegisterVue registerVue) {
        this.registerVue = registerVue;
        registerImplement = new RegisterImplement(this);
    }

    @Override
    public void createAccount(String nom, String email, String password, String phone) {
        registerImplement.createAccount( nom,  email,  password,  phone);
        //pSuccess("looool");
    }

    @Override
    public void pSuccess(String message) {
        registerVue.success(message);
    }

    @Override
    public void pError(String message) {
        registerVue.error(message);
    }

    @Override
    public void onSetProgressBarVisibility(int visibility) {
        registerVue.onSetProgressBarVisibility(visibility);
    }
    @Override
    public void logOut() {
        registerImplement.logOut();
    }


}
