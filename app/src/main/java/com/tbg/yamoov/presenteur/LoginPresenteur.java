package com.tbg.yamoov.presenteur;

import com.tbg.yamoov.core.Ipresenteur;
import com.tbg.yamoov.core.Ivue;
import com.tbg.yamoov.model.implement.LoginImplement;

public class LoginPresenteur implements Ipresenteur.LoginPresenteur {

    Ivue.LoginVue loginVue;
    LoginImplement loginImplement;

    public LoginPresenteur(Ivue.LoginVue loginVue) {
        this.loginVue = loginVue;
        loginImplement = new LoginImplement(this);
    }

    @Override
    public void loginuser(String email, String password) {
        loginImplement.loginAccount(email,password);
    }

    @Override
    public void pSuccess(String message) {
        loginVue.success(message);
    }

    @Override
    public void pError(String message) {
        loginVue.error(message);
    }

    @Override
    public void onSetProgressBarVisibility(int visibility) {
        loginVue.onSetProgressBarVisibility(visibility);
    }

    @Override
    public Boolean loadUser() {

        return loginImplement.loadUser();
    }
}
