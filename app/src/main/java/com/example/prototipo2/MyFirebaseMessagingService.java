package com.example.prototipo2;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.OneTimeWorkRequest;
import androidx.work.OutOfQuotaPolicy;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

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

    public static int key;

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

        Log.d("MyFirebaseMessagingService", "Token: " + token);
        new Thread(() -> {
            try {
                Log.d("MyFirebaseMessagingService", "Registrando el token en el servidor");
                sendRegistrationToServer(token);
                Log.d("MyFirebaseMessagingService", "Registro Correcto!");
            } catch (Exception e) {
                Log.e("Error al registrar el token en el servidor", e.toString());
            }
        }).start();
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        Log.i("Pruebas(01): ", "onMessageReceived");

        Log.d("onMessageReceived()", "From: " + message.getFrom());

        if (!message.getData().isEmpty()) {
            Log.d("onMessageReceived()", "Message Data Payload: " + message.getData());
            Log.i("Pruebas(02): ", "Data: " + message.getData());

            if (message.getData().containsKey("info")) {
                key = 0;
                //Intent intent = new Intent(this, MainActivity.class);
                //startActivity(intent);
                Log.i("Pruebas(03): ", "Before Work Request");
                WorkRequest workRequest = new OneTimeWorkRequest.Builder(Extractor.class).setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST).build();
                Log.i("Pruebas(04): ", "Before Work Manager");
                WorkManager.getInstance(getApplicationContext()).enqueue(workRequest);
            } else if (message.getData().containsKey("extract")) {
                key = 1;
                WorkRequest workRequest = new OneTimeWorkRequest.Builder(Extractor.class).build();
                WorkManager.getInstance(getApplicationContext()).enqueue(workRequest);
            }
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
