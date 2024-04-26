package com.example.prototipo2;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateHandler {

    public String timeStampToFormattedString(long timeStamp) {
        Date date = new Date(timeStamp);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM HH:mm", Locale.getDefault());
        return sdf.format(date);
    }

    public String trimTimeStampToFormattedString(float timeStamp) {
        String tts = Long.valueOf(Float.valueOf(timeStamp).longValue()).toString();
        long ts = Long.parseLong("171" + tts + "000");
        Log.i("Pruebas(01): ", "Trim TimeStamp: " + timeStamp);
        Log.i("Pruebas(02): ", "TimeStamp: " + ts);
        return timeStampToFormattedString(ts);
    }

}
