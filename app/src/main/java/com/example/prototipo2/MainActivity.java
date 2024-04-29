package com.example.prototipo2;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;

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

        /*                          Delete databases (Testing)
        AnalizadorDBHelper dbHelper = new AnalizadorDBHelper(getApplicationContext());
        dbHelper.onUpgrade(dbHelper.getWritableDatabase(), 1, 1);                       */

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

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.w("FirebaseMessaging", "Fetching FCM registration token failed", task.getException());
                    return;
                }

                String token = task.getResult();

                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Token: " + token, Snackbar.LENGTH_LONG);
                snackbar.show();
                Log.d("FCM Token", token);
            }
        });

        //Log.i("Pruebas(01): ", "OS Version: " + System.getProperty("os.version") + "(" + Build.VERSION.INCREMENTAL + ")");
        Log.i("Pruebas(02): ", "API Level: " + Build.VERSION.SDK_INT);
        //Log.i("Pruebas(03): ", "Device: " + Build.DEVICE);
        //Log.i("Pruebas(04): ", "Model (and Product): " + Build.MODEL + "(" + Build.PRODUCT + ")");
        Log.i("Pruebas(05): ", "Manufacturer: " + Build.MANUFACTURER);
        //Log.i("Pruebas(06): ", "Tags: " + Build.TAGS);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.i("Error(01):", "No hubo permiso!");
            return;
        }
        Log.i("Pruebas(07): ", "Mobile Data: " + ((TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE)).getNetworkType());
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

    private void createNotificationChannel() {
        NotificationChannel channel1 = new NotificationChannel(getString(R.string.channel_id_1), getString(R.string.channel_name), NotificationManager.IMPORTANCE_DEFAULT);
        channel1.setDescription(getString(R.string.channel_description_1));
        NotificationChannel channel2 = new NotificationChannel(getString(R.string.channel_id_2), getString(R.string.channel_name), NotificationManager.IMPORTANCE_DEFAULT);
        channel2.setDescription(getString(R.string.channel_description_2));
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel1);
        notificationManager.createNotificationChannel(channel2);
    }
}