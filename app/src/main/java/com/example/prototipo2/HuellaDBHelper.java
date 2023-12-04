package com.example.prototipo2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HuellaDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Huella.db";
    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + HuellaContract.HuellaEntry.TABLE_NAME + " (" +
            HuellaContract.HuellaEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            HuellaContract.HuellaEntry.COLUMN_ESCANEO_ID + " INTEGER NOT  NULL," +
            HuellaContract.HuellaEntry.COLUMN_HUELLA_CARBONO + " REAL NOT NULL)";
    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + HuellaContract.HuellaEntry.TABLE_NAME;

    public HuellaDBHelper(Context context) {
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
