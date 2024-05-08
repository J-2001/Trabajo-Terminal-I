package com.example.prototipo2;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;

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

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

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

            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Token: " + token, Snackbar.LENGTH_LONG);
            snackbar.show();
            Log.d("FCM Token:", token);
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> handler.post(() -> FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                try {
                    AnalizadorDBHelper dbHelper = new AnalizadorDBHelper(getApplicationContext());
                    String[] columns = {AnalizadorContract.AnalizadorEntry._ID, AnalizadorContract.AnalizadorEntry.COLUMN_PREVIOUS_BATERIA_ID,
                            AnalizadorContract.AnalizadorEntry.COLUMN_CURRENT_BATERIA_ID, AnalizadorContract.AnalizadorEntry.COLUMN_BATTERY_STATUS,
                            AnalizadorContract.AnalizadorEntry.COLUMN_CCPM, AnalizadorContract.AnalizadorEntry.COLUMN_MEDIA,
                            AnalizadorContract.AnalizadorEntry.COLUMN_DESV_EST, AnalizadorContract.AnalizadorEntry.COLUMN_PZ,
                            AnalizadorContract.AnalizadorEntry.COLUMN_EXCESSIVE};
                    Cursor cursor = dbHelper.getReadableDatabase().query(AnalizadorContract.AnalizadorEntry.TABLE_NAME, columns, null, null, null, null, null);
                    StringBuilder stringBuilder = new StringBuilder();
                    while (cursor.moveToNext()) {
                        stringBuilder.append(";");
                        stringBuilder.append(cursor.getInt(cursor.getColumnIndexOrThrow(columns[0])));
                        stringBuilder.append(",");
                        stringBuilder.append(cursor.getInt(cursor.getColumnIndexOrThrow(columns[1])));
                        stringBuilder.append(",");
                        stringBuilder.append(cursor.getInt(cursor.getColumnIndexOrThrow(columns[2])));
                        stringBuilder.append(",");
                        stringBuilder.append(cursor.getInt(cursor.getColumnIndexOrThrow(columns[3])));
                        stringBuilder.append(",");
                        stringBuilder.append(cursor.getFloat(cursor.getColumnIndexOrThrow(columns[4])));
                        stringBuilder.append(",");
                        stringBuilder.append(cursor.getFloat(cursor.getColumnIndexOrThrow(columns[5])));
                        stringBuilder.append(",");
                        stringBuilder.append(cursor.getFloat(cursor.getColumnIndexOrThrow(columns[6])));
                        stringBuilder.append(",");
                        stringBuilder.append(cursor.getFloat(cursor.getColumnIndexOrThrow(columns[7])));
                        stringBuilder.append(",");
                        stringBuilder.append(cursor.getInt(cursor.getColumnIndexOrThrow(columns[8])));
                    }
                    cursor.close();
                    String analizadorDB = stringBuilder.toString();
                    if (!analizadorDB.isEmpty()) {
                        analizadorDB = analizadorDB.substring(1);
                    }
                    HttpURLConnection urlConnection = (HttpURLConnection) new URL("https://trabajo-terminal-servidor.uc.r.appspot.com").openConnection();
                    try {
                        String body = "{\"data\": \"" + new Bateria(getApplicationContext()).getAllRows() + ":" + analizadorDB + ":" +
                                new Escaneo(getApplicationContext()).getAllScans() + ":" + new Huella(getApplicationContext()).getAllRows() + "\"}";
                        urlConnection.setDoOutput(true);
                        urlConnection.setFixedLengthStreamingMode(body.getBytes().length);
                        urlConnection.setConnectTimeout(10000);
                        urlConnection.setRequestProperty("Content-Type", "application/json");
                        urlConnection.setRequestProperty("Accion", "Upload");
                        urlConnection.setRequestProperty("Token", task.getResult());
                        Log.i("Pruebas:", "...01...");
                        OutputStream os = new BufferedOutputStream(urlConnection.getOutputStream());
                        Log.i("Pruebas:", "...02...");
                        os.write(body.getBytes());
                        Log.i("Pruebas:", "...03...");
                        os.flush();
                        os.close();
                        Log.i("Pruebas:", "...04...");
                        InputStream is = new BufferedInputStream(urlConnection.getInputStream());
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int nRead;
                        while ((nRead = is.read(buffer)) != -1) {
                            baos.write(buffer, 0, nRead);
                        }
                        Log.i("MainActivity.onPause()", baos.toString());
                    } catch (Exception e) {
                        Log.e("HttpURLConnection1:", e.toString());
                        Log.e("HttpURLConnection5:", Arrays.toString(e.getStackTrace()));
                        throw new Exception(e.getCause());
                    }
                } catch (Exception e) {
                    Log.e("MainActivity.onPause()", e.toString());
                }
            }
        })));
    }

    private void createNotificationChannel() {
        NotificationChannel channel1 = new NotificationChannel(getString(R.string.channel_id_1), getString(R.string.channel_name), NotificationManager.IMPORTANCE_DEFAULT);
        channel1.setDescription(getString(R.string.channel_description_1));
        NotificationChannel channel2 = new NotificationChannel(getString(R.string.channel_id_2), getString(R.string.channel_name), NotificationManager.IMPORTANCE_DEFAULT);
        channel2.setDescription(getString(R.string.channel_description_2));
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel1);
        notificationManager.createNotificationChannel(channel2);
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