package com.example.prototipo2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NotificationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Intent intent = getIntent();
        String extras = "Descripcion: " + intent.getStringExtra("desc");
        extras += "\nEstatus: " + intent.getStringExtra("stat");
        extras += "\nCCpm: " + intent.getStringExtra("ccpm");
        extras += "\nMedia: " + intent.getStringExtra("media");
        extras += "\nDesv. Est.: " + intent.getStringExtra("desvest");
        extras += "\nP(Z>x): " + intent.getStringExtra("pz");

        TextView tv = this.findViewById(R.id.notification_tv);
        tv.setText(extras);
    }
}