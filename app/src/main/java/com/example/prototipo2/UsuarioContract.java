package com.example.prototipo2;

import android.provider.BaseColumns;

public class UsuarioContract {

    private UsuarioContract() {}

    public static class UsuarioEntry implements BaseColumns {
        public static final String TABLE_NAME = "USUARIO";
        public static final String COLUMN_ID = "USUARIO_ID";
        public static final String COLUMN_OS_VERSION = "OS_VERSION";
        public static final String COLUMN_DEVICE_NAME = "DEVICE_NAME";
        public static final String COLUMN_DEVICE_MODEL = "DEVICE_MODEL";
    }

}
