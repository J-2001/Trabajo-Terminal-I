package com.example.prototipo2;

import android.provider.BaseColumns;

public class HuellaContract {

    private HuellaContract() {}

    public static class HuellaEntry implements BaseColumns {
        public static final String TABLE_NAME = "HUELLA";
        public static final String COLUMN_ESCANEO_ID = "ESCANEO_ID";
        public static final String COLUMN_HUELLA_CARBONO = "HUELLA_CARBONO";
    }

}
