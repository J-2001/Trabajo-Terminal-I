package com.example.prototipo2;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateHandler {

    public String timeStampToFormattedString(long timeStamp) {
        Date date = new Date(timeStamp);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM HH:mm", Locale.getDefault());
        return sdf.format(date);
    }

}
