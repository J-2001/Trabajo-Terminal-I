package com.example.prototipo2;

import android.provider.BaseColumns;

public class BateriaContract {

    private BateriaContract() {}

    public static class BateriaEntry implements BaseColumns {
        public static final String TABLE_NAME = "BATERIA";
        public static final String COLUMN_CHARGE_COUNTER = "CHARGE_COUNTER";
        public static final String COLUMN_BATTERY_CAPACITY = "BATTERY_CAPACITY";
        public static final String COLUMN_BATTERY_STATUS = "BATTERY_STATUS";
        public static final String COLUMN_BATTERY_VOLTAGE = "BATTERY_VOLTAGE";
        public static final String COLUMN_TIMESTAMP = "TIMESTAMP";
    }

}
