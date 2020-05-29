package com.tbg.yamoov.core;

public interface Ivue {
    interface RegisterVue{

        void success(String message);
        void error(String message);
        void onSetProgressBarVisibility(int visibility);
    }

    interface LoginVue{
        void success(String message);
        void error(String message);
        void onSetProgressBarVisibility(int visibility);
    }
    interface ProfilVue{
        void successLogout(String message);
    }
}
