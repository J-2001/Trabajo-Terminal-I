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

    private int id; // Puede no ser int
    private String osVersion;
    private String deviceName;
    private String deviceModel;

    public Usuario(Context context, Context layoutContext) {
        // Buscamos si la base de datos "Usuario" tiene al menos un valor
        UsuarioDBHelper dbHelper = new UsuarioDBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(UsuarioContract.UsuarioEntry.TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToNext()) {
            // Si lo tiene, el usuario ya esta registrado
            Log.d("Usuario", cursor.getInt(cursor.getColumnIndexOrThrow(UsuarioContract.UsuarioEntry.COLUMN_ID)) + "\n" +
                        cursor.getString(cursor.getColumnIndexOrThrow(UsuarioContract.UsuarioEntry.COLUMN_OS_VERSION)) + "\n" +
                        cursor.getString(cursor.getColumnIndexOrThrow(UsuarioContract.UsuarioEntry.COLUMN_DEVICE_NAME)) + "\n" +
                        cursor.getString(cursor.getColumnIndexOrThrow(UsuarioContract.UsuarioEntry.COLUMN_DEVICE_MODEL)));
        } else {
            // Si no, lo registramos llamando a registerUser()
            registerUser(dbHelper.getWritableDatabase(), layoutContext);
        }

        cursor.close();
    }

    private void registerUser(SQLiteDatabase db, Context context) {
        db.insert(UsuarioContract.UsuarioEntry.TABLE_NAME, null, toContentValues());

        // Petición de registro al servidor

        Snackbar snackbar = Snackbar.make(((Activity) context).findViewById(R.id.mainActivity), "Usuario Registrado", Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    public void sendData() {
        // Metodo no void, envía las bases de datos de la aplicación
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
        id = 1;
        return id;
    }

    public String getOsVersion() {
        osVersion = System.getProperty("os.version") + "-" + Build.VERSION.INCREMENTAL;
        return osVersion;
    }

    public String getDeviceName() {
        deviceName = Build.DEVICE;
        return deviceName;
    }

    public String getDeviceModel() {
        deviceModel = Build.MODEL + "-" + Build.PRODUCT;
        return deviceModel;
    }
}
