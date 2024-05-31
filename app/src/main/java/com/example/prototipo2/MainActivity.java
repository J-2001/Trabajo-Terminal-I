package com.example.prototipo2;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Log.d("Permiso de Notificaciones", "FCM SDK puede enviar notificaciones!");
                } else {
                    Log.d("Permiso de Notificaciones", "El usuario no recibira notificaciones");
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread.setDefaultUncaughtExceptionHandler((t, e) -> new Thread(() -> {
            try {
                HttpURLConnection urlConnection = (HttpURLConnection) new URL("https://trabajo-terminal-servidor.uc.r.appspot.com").openConnection();
                byte[] body = ("{\"error\": \"" + e + "\"}").getBytes();
                try {
                    urlConnection.setDoOutput(true);
                    urlConnection.setFixedLengthStreamingMode(body.length);
                    urlConnection.setConnectTimeout(10000);
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setRequestProperty("Accion", "Exception");
                    urlConnection.setRequestProperty("Name", new DateHandler().newFormattedString());
                    OutputStream os = new BufferedOutputStream(urlConnection.getOutputStream());
                    os.write(body);
                    os.flush();
                    os.close();
                    InputStream is = new BufferedInputStream(urlConnection.getInputStream());
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int nRead;
                    while ((nRead = is.read(buffer)) != -1) {
                        baos.write(buffer, 0, nRead);
                    }
                    Log.i("ExceptionHandler", baos.toString());
                } catch (Exception ex) {
                    throw new Exception(ex);
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception ex) {
                Log.e("ExceptionHandler", ex.toString());
            }

            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                Log.i("ExceptionHandler", "No se tiene permiso para mostrar notificaciones!");
                return;
            }

            NotificationManagerCompat.from(getApplicationContext()).notify(3, new NotificationCompat.Builder(getApplicationContext(), getString(R.string.channel_id_3))
                    .setSmallIcon(R.drawable.ic_logoappfondo).setContentTitle("Ocurrió Algo Inesperado")
                    .setContentText("Expanda para mayor información...")
                    .setStyle(new NotificationCompat.BigTextStyle().bigText("Durante la ejecución de la aplicación, se generó un error no antes previsto.\nEl reporte correspondiente ha sido envíado al servidor.\nComuníquese con los administradores, para indicar que esta situación se generó en su dispositivo.\nQuede a la espera de una nueva versión de la aplicación (con las implementaciones necesarias aplicadas)."))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT).setAutoCancel(true).build());

            Process.killProcess(Process.myPid());
        }).start());

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        createNotificationChannel();

        CoordinatorLayout coordinatorLayout = this.findViewById(R.id.mainActivity);

        Button btn01 = this.findViewById(R.id.main_btn_01);

        btn01.setOnClickListener(v -> {
            Intent secondActivity = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(secondActivity);
        });

        Button btn02 = this.findViewById(R.id.main_btn_02);

        btn02.setOnClickListener(v -> {
            Intent thirdActivity = new Intent(MainActivity.this, ThirdActivity.class);
            startActivity(thirdActivity);
        });

        Button btn03 = this.findViewById(R.id.main_btn_03);

        btn03.setOnClickListener(v -> {
            Intent fourthActivity = new Intent(MainActivity.this, FourthActivity.class);
            startActivity(fourthActivity);
        });

        Button btn04 = this.findViewById(R.id.main_btn_04);

        btn04.setOnClickListener(v -> {
            Intent fifthActivity = new Intent(MainActivity.this, FifthActivity.class);
            startActivity(fifthActivity);
        });

        TextView tv = this.findViewById(R.id.main_tv);

        tv.setOnClickListener(v -> {
            Intent sixthActivity = new Intent(MainActivity.this, SixthActivity.class); //NotificationActivity.class);
            startActivity(sixthActivity);
        });

        askNotificationPermission();

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.w("FirebaseMessaging:", "Fetching FCM registration token failed: " + task.getException());
                return;
            }

            String token = task.getResult();

            Log.d("FCM Token:", token);
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                WorkManager.getInstance(getApplicationContext()).enqueue(new OneTimeWorkRequest.Builder(UploadWorker.class).setInputData(new Data.Builder().putString("TOKEN", task.getResult()).build()).build());
            }
        });
    }

    private void createNotificationChannel() {
        NotificationChannel channel1 = new NotificationChannel(getString(R.string.channel_id_1), getString(R.string.channel_name), NotificationManager.IMPORTANCE_DEFAULT);
        channel1.setDescription(getString(R.string.channel_description_1));
        NotificationChannel channel2 = new NotificationChannel(getString(R.string.channel_id_2), getString(R.string.channel_name), NotificationManager.IMPORTANCE_DEFAULT);
        channel2.setDescription(getString(R.string.channel_description_2));
        NotificationChannel channel3 = new NotificationChannel(getString(R.string.channel_id_3), getString(R.string.channel_name), NotificationManager.IMPORTANCE_DEFAULT);
        channel3.setDescription(getString(R.string.channel_description_3));
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel1);
        notificationManager.createNotificationChannel(channel2);
        notificationManager.createNotificationChannel(channel3);
    }

    private void askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                Log.i("Permiso de Notificaciones", "FCM SDK puede enviar notificaciones");
                //} else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // UI que le pida al usuario permiso para recibir las notificaciones
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }
}