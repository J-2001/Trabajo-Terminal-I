package com.example.prototipo2;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
        if (cursor.getCount() == 0) {
            // Si no lo tiene, lo registramos llamando a registerUser()
            registerUser(dbHelper.getWritableDatabase(), layoutContext);
        } else {
            // Si lo tiene, el usuario ya esta registrado
            System.out.println(cursor.getInt(cursor.getColumnIndexOrThrow(UsuarioContract.UsuarioEntry.COLUMN_ID)) + "\n" +
                    cursor.getString(cursor.getColumnIndexOrThrow(UsuarioContract.UsuarioEntry.COLUMN_OS_VERSION)) + "\n" +
                    cursor.getString(cursor.getColumnIndexOrThrow(UsuarioContract.UsuarioEntry.COLUMN_DEVICE_NAME)) + "\n" +
                    cursor.getString(cursor.getColumnIndexOrThrow(UsuarioContract.UsuarioEntry.COLUMN_DEVICE_MODEL)));
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
        return id;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getDeviceModel() {
        return deviceModel;
    }
}
