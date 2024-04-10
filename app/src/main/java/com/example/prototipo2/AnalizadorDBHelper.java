package com.example.prototipo2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AnalizadorDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Analizador.db";
    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + AnalizadorContract.AnalizadorEntry.TABLE_NAME + " (" +
            AnalizadorContract.AnalizadorEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            AnalizadorContract.AnalizadorEntry.COLUMN_PREVIOUS_BATERIA_ID + " INTEGER NOT NULL," +
            AnalizadorContract.AnalizadorEntry.COLUMN_CURRENT_BATERIA_ID + " INTEGER NOT NULL," +
            AnalizadorContract.AnalizadorEntry.COLUMN_BATTERY_STATUS + " INTEGER NOT NULL," +
            AnalizadorContract.AnalizadorEntry.COLUMN_CCPM + " REAL NOT NULL," +
            AnalizadorContract.AnalizadorEntry.COLUMN_MEDIA + " REAL NOT NULL," +
            AnalizadorContract.AnalizadorEntry.COLUMN_DESV_EST + " REAL NOT NULL," +
            AnalizadorContract.AnalizadorEntry.COLUMN_PZ + " REAL NOT NULL)";
    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + AnalizadorContract.AnalizadorEntry.TABLE_NAME;

    public AnalizadorDBHelper(Context context) {
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
        Log.d("AnalizadorDBHelper", "onUpgrade() successful!");
    }
}
