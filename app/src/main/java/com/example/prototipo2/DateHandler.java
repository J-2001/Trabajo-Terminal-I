package com.example.prototipo2;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateHandler {

    public String newFormattedString() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss", Locale.forLanguageTag("es-MX"));
        return sdf.format(date);
    }

    public String timeStampToFormattedString(long timeStamp) {
        Date date = new Date(timeStamp);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM HH:mm", Locale.forLanguageTag("es-MX"));
        return sdf.format(date);
    }

    public String timeStampToFormattedHour(long timeStamp) {
        return timeStampToFormattedString(timeStamp).substring(6);
    }

    public String trimTimeStampToFormattedString(float timeStamp) {
        String tts = Long.valueOf(Float.valueOf(timeStamp).longValue()).toString();
        long ts = Long.parseLong("17" + tts + "000");
        return timeStampToFormattedString(ts);
    }
}
