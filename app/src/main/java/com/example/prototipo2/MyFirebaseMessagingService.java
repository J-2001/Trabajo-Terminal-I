package com.example.prototipo2;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;

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
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("MyFirebaseMessagingService", "Registrando el token en el servidor");
                    sendRegistrationToServer(token);
                    Log.d("MyFirebaseMessagingService", "Registro Correcto!");
                } catch (Exception e) {
                    Log.e("Error al registrar el token en el servidor", e.toString());
                }
            }
        }).start();
    }

    public void sendRegistrationToServer(String token) {
        try {
            URL url = new URL("https://trabajo-terminal-servidor.uc.r.appspot.com");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            try {
                String body = "{\"token\": \"" + token + "\"}";

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
                while ( is.read(buffer) != -1 ) {
                    baos.write(buffer);
                }
                String response = baos.toString();

                Log.i("sendRegistrationToServer", response);

            } finally {
                urlConnection.disconnect();
            }
            
        } catch (Exception e) {
            Log.e("Error al leer y conectarse a la URL del servidor: ", e.toString());
        }
    }

}
