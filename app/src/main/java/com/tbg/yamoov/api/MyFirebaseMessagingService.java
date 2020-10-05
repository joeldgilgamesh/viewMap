package com.tbg.yamoov.api;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.tbg.yamoov.MainActivity;
import com.tbg.yamoov.R;

class MyFirebaseMessagingService extends FirebaseMessagingService {

    public void onNewToken(String token) {

    }
}
