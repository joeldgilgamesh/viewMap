package com.tbg.yamoov.core;

public interface Imodel {
    interface Iregister{
        void createAccount(String nom, String email, String password, String phone);
        void logOut();
    }

    interface Ilogin{
        void loginAccount(String email, String password);
        Boolean loadUser();
    }
    interface iprofil{
        void logOut();
    }
}
