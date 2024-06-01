package com.example.prototipo2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NotificationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Intent intent = getIntent();

        LinearLayout linearLayout = this.findViewById(R.id.notification_linlay);

        String videoStreaming = intent.getStringExtra("videostreaming");

        switch (videoStreaming) {
            case "Netflix":
                linearLayout.setBackgroundColor(getColor(R.color.netflix));
                break;
            case "Disney+":
                linearLayout.setBackgroundColor(getColor(R.color.disneyplus));
                break;
            case "Star+":
                linearLayout.setBackgroundColor(getColor(R.color.starplus));
                break;
            case "Prime Video":
                linearLayout.setBackgroundColor(getColor(R.color.primevideo));
                break;
            case "Max":
                linearLayout.setBackgroundColor(getColor(R.color.max));
                break;
            case "Crunchyroll":
                linearLayout.setBackgroundColor(getColor(R.color.crunchyroll));
                break;
            case "ViX":
                linearLayout.setBackgroundColor(getColor(R.color.vix));
                break;
        }

        linearLayout.setOnClickListener(v -> Log.i("LinearLayout", "Touched!"));

        TextView tv01 = this.findViewById(R.id.notification_tv_01);
        TextView tv02 = this.findViewById(R.id.notification_tv_02);
        TextView tv03 = this.findViewById(R.id.notification_tv_03);

        DateHandler dh = new DateHandler();

        tv01.setText(getString(R.string.notification_tv_01, dh.timeStampToFormattedHour(intent.getLongExtra("timestamp", 0)), videoStreaming));

        tv02.setOnClickListener(v -> tv03.setVisibility(View.VISIBLE));

        String status;

        if (intent.getIntExtra("status", 3) == 3) {
            status = "Descargando";
        } else {
            status = "Cargando";
        }

        tv03.setText(getString(R.string.notification_tv_03, videoStreaming, dh.timeStampToFormattedHour(intent.getLongExtra("timestamp", 0)), status, intent.getFloatExtra("averagevoltage", 0), intent.getFloatExtra("ccpm", 0), intent.getFloatExtra("media", 0), intent.getFloatExtra("desvest", 0), intent.getFloatExtra("pz", 0)));

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return super.onTouchEvent(event);
    }

}