package com.example.prototipo2;

import android.provider.BaseColumns;

public class EscaneoContract {

    private EscaneoContract() {}

    public static class EscaneoEntry implements BaseColumns {
        public static final String TABLE_NAME = "ESCANEO";
        public static final String COLUMN_START_BATERIA_ID = "START_BATERIA_ID";
        public static final String COLUMN_END_BATERIA_ID = "END_BATERIA_ID";
        public static final String COLUMN_DURACION_ESCANEO = "DURACION_ESCANEO";
        public static final String COLUMN_DATOS_CONSUMO = "DATOS_CONSUMO";
        public static final String COLUMN_AVERAGE_VOLTAGE = "AVERAGE_VOLTAGE";
        public static final String COLUMN_VIDEO_STREAMING = "VIDEO_STREAMING";
    }

}
