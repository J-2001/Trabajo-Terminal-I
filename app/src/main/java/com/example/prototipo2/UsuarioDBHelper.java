package com.example.prototipo2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UsuarioDBHelper  extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Usuario.db";

    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + UsuarioContract.UsuarioEntry.TABLE_NAME + " (" +
            UsuarioContract.UsuarioEntry.COLUMN_ID + " INTEGER PRIMARY KEY," +
            UsuarioContract.UsuarioEntry.COLUMN_OS_VERSION + " TEXT NOT NULL," +
            UsuarioContract.UsuarioEntry.COLUMN_DEVICE_NAME + " TEXT NOT NULL," +
            UsuarioContract.UsuarioEntry.COLUMN_DEVICE_MODEL + " TEXT NOT NULL)";
    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + UsuarioContract.UsuarioEntry.TABLE_NAME;

    public UsuarioDBHelper(Context context) {
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
