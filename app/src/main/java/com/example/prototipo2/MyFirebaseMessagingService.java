package com.example.prototipo2;

import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

        Log.d("MyFirebaseMessagingService", "Token: " + token);
        new Thread(() -> {
            try {
                Log.d("MyFirebaseMessagingService", "Registrando el token en el servidor");
                sendRegistrationToServer(token);
                Intent intent = new Intent();
                intent.setAction(getString(R.string.broadcast_action_2));
                sendBroadcast(intent);
            } catch (Exception e) {
                Log.e("Error al registrar el token en el servidor: ", e.toString());
            }
        }).start();
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        Log.d("onMessageReceived()", "From: " + message.getFrom());

        if (!message.getData().isEmpty()) {
            Log.d("onMessageReceived()", "Message Data Payload: " + message.getData());

            /*
            if (/* Check if data needs to be processed by long running job *//* true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }
            */
        }

        if (message.getNotification() != null) {
            Log.d("onMessageReceived()", "Message Notification Body: " + message.getNotification().getBody());
        }
    }

    public void sendRegistrationToServer(String token) {
        try {
            URL url = new URL("https://trabajo-terminal-servidor.uc.r.appspot.com");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            try {
                String body = "{\"token\": \"" + token + "\", \"info\": \"Fabricante:" + Build.MANUFACTURER + ";Marca:" + Build.BRAND + ";Modelo:" + Build.MODEL + ";Android:" + Build.VERSION.RELEASE + "\"}";

                urlConnection.setDoOutput(true);
                //urlConnection.setChunkedStreamingMode(0);
                urlConnection.setFixedLengthStreamingMode(body.getBytes().length);
                urlConnection.setConnectTimeout(10000);

                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accion", "Registro");

                OutputStream os = new BufferedOutputStream(urlConnection.getOutputStream());
                os.write(body.getBytes());
                os.flush();
                os.close();

                InputStream is = new BufferedInputStream(urlConnection.getInputStream());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int nRead;
                while ((nRead = is.read(buffer)) != -1) {
                    baos.write(buffer, 0, nRead);
                }
                String response = baos.toString();

                Log.i("sendRegistrationToServer(): ", response);
            } catch (Exception e) {
                throw new Exception(e.getCause());
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            Log.e("Error - sendRegistrationToServer(): ", e.toString());
        }
    }

}
