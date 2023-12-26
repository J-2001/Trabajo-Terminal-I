package com.example.prototipo2;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import com.google.android.material.snackbar.Snackbar;

public class Usuario {

    public Usuario(Context context, Context layoutContext) {
        UsuarioDBHelper dbHelper = new UsuarioDBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(UsuarioContract.UsuarioEntry.TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToNext()) {
            Log.d("Usuario", cursor.getInt(cursor.getColumnIndexOrThrow(UsuarioContract.UsuarioEntry.COLUMN_ID)) + "\n" +
                        cursor.getString(cursor.getColumnIndexOrThrow(UsuarioContract.UsuarioEntry.COLUMN_OS_VERSION)) + "\n" +
                        cursor.getString(cursor.getColumnIndexOrThrow(UsuarioContract.UsuarioEntry.COLUMN_DEVICE_NAME)) + "\n" +
                        cursor.getString(cursor.getColumnIndexOrThrow(UsuarioContract.UsuarioEntry.COLUMN_DEVICE_MODEL)));
        } else {
            registerUser(dbHelper.getWritableDatabase(), layoutContext);
        }

        cursor.close();
    }

    private void registerUser(SQLiteDatabase db, Context context) {
        db.insert(UsuarioContract.UsuarioEntry.TABLE_NAME, null, toContentValues());

        // Send register to server

        Snackbar snackbar = Snackbar.make(((Activity) context).findViewById(R.id.mainActivity), "Usuario Registrado", Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    public void sendData() {
        // Send All Data to Server
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(UsuarioContract.UsuarioEntry.COLUMN_ID, getId());
        values.put(UsuarioContract.UsuarioEntry.COLUMN_OS_VERSION, getOsVersion());
        values.put(UsuarioContract.UsuarioEntry.COLUMN_DEVICE_NAME, getDeviceName());
        values.put(UsuarioContract.UsuarioEntry.COLUMN_DEVICE_MODEL, getDeviceModel());

        return values;
    }

    public int getId() {
        // No int
        return 1;
    }

    public String getOsVersion() {
        return System.getProperty("os.version") + "-" + Build.VERSION.INCREMENTAL;
    }

    public String getDeviceName() {
        return Build.DEVICE;
    }

    public String getDeviceModel() {
        return Build.MODEL + "-" + Build.PRODUCT;
    }
}
