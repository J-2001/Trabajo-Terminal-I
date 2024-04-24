package com.example.prototipo2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EscaneoDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Escaneo.db";
    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + EscaneoContract.EscaneoEntry.TABLE_NAME + " (" +
            EscaneoContract.EscaneoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            EscaneoContract.EscaneoEntry.COLUMN_START_BATERIA_ID + " INTEGER NOT NULL," +
            EscaneoContract.EscaneoEntry.COLUMN_END_BATERIA_ID + " INTEGER NOT NULL," +
            EscaneoContract.EscaneoEntry.COLUMN_DURACION_ESCANEO + " INTEGER NOT NULL," +
            EscaneoContract.EscaneoEntry.COLUMN_DATOS_CONSUMO + " INTEGER NOT NULL," +
            EscaneoContract.EscaneoEntry.COLUMN_AVERAGE_VOLTAGE + " REAL NOT NULL)";
            //EscaneoContract.EscaneoEntry.COLUMN_VIDEO_STREAMING + " TEXT NOT NULL)";
    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + EscaneoContract.EscaneoEntry.TABLE_NAME;

    public EscaneoDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
