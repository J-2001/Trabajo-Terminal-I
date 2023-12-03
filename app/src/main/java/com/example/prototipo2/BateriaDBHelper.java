package com.example.prototipo2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BateriaDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Bateria.db";

    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + BateriaContract.BateriaEntry.TABLE_NAME + " (" +
            BateriaContract.BateriaEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            BateriaContract.BateriaEntry.COLUMN_CHARGE_COUNTER + " INTEGER NOT NULL," +
            BateriaContract.BateriaEntry.COLUMN_CURRENT_NOW + " INTEGER NOT NULL," +
            BateriaContract.BateriaEntry.COLUMN_BATTERY_CAPACITY + " INTEGER NOT NULL," +
            BateriaContract.BateriaEntry.COLUMN_BATTERY_STATUS + " INTEGER NOT NULL," +
            BateriaContract.BateriaEntry.COLUMN_BATTERY_VOLTAGE + " REAL NOT NULL," +
            BateriaContract.BateriaEntry.COLUMN_TIMESTAMP + " INTEGER NOT NULL)";
    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + BateriaContract.BateriaEntry.TABLE_NAME;

    public BateriaDBHelper(Context context) {
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
