package com.example.prototipo2;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

        Log.d("MyFirebaseMessagingService", "Token: " + token);
        // sendRegistrationToServer(token)
    }

}
