package com.tbg.yamoov.core;

public interface Ipresenteur {

    interface RegisterPresenteur{

        void createAccount(String nom, String email, String password, String phone);
        void pSuccess(String message);
        void pError(String message);
        void onSetProgressBarVisibility(int visibility);
        void logOut();
        //
    }
    interface  LoginPresenteur{
        void loginuser(String email, String password);
        void pSuccess(String message);
        void pError(String message);
        void onSetProgressBarVisibility(int visibility);
        Boolean loadUser();
    }
    interface ProfilPresenteur{
        void logOut();
        void pSuccess(String message);
    }
}
