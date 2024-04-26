package com.example.prototipo2;

import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

public class TimeStampAxisValueFormatter extends ValueFormatter {

    private final DateHandler dh;

    public TimeStampAxisValueFormatter() {
        this.dh = new DateHandler();
    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        // return super.getAxisLabel(value, axis);
        return dh.trimTimeStampToFormattedString(value);
    }

}
