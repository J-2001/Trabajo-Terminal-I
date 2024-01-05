package com.example.prototipo2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "AnalizadorConsumoDeEnergia";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*                          Delete databases (Testing)
        AnalizadorDBHelper dbHelper = new AnalizadorDBHelper(getApplicationContext());
        dbHelper.onUpgrade(dbHelper.getWritableDatabase(), 1, 1);                       */

        Usuario usuario = new Usuario(getApplicationContext(), this);

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
            Intent sixthActivity = new Intent(MainActivity.this, SixthActivity.class);
            startActivity(sixthActivity);
        });

        // NotificationChannel
        CharSequence name = "Trabajo Terminal";
        String description = "Consumo de energía en aplicaciones de Video Streaming";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}