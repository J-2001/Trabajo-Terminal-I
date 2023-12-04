package com.example.prototipo2;

import android.provider.BaseColumns;

public class AnalizadorContract {

    private AnalizadorContract() {}

    public static class AnalizadorEntry implements BaseColumns {
        public static final String TABLE_NAME = "Analizador";
        public static final String COLUMN_PREVIOUS_BATERIA_ID = "PREVIOUS_BATERIA_ID";
        public static final String COLUMN_CURRENT_BATERIA_ID = "CURRENT_BATERIA_ID";
        public static final String COLUMN_BATTERY_STATUS = "BATTERY_STATUS";
        public static final String COLUMN_CCPM = "CCPM";
        public static final String COLUMN_MEDIA = "MEDIA";
        public static final String COLUMN_DESV_EST = "DESVIACION_ESTANDAR";
    }

}
