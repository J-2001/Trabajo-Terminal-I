package com.example.prototipo2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Huella {

    private final float factorEmision = 0.435F; // gCO2e/Wh
    private int escaneoId;
    private float huellaCarbono;
    private int datosConsumo;
    private float voltage;

    public Huella() {
        this.escaneoId = 0;
        this.huellaCarbono = 0;
    }

    public void calcularHuellaCarbono(Context context) {
        float n = datosConsumo * 1000000 * voltage; // Wh
        huellaCarbono = n * factorEmision; // gCO2e
        insertIntoDB(context);
    }

    public void insertIntoDB(Context context) {
        HuellaDBHelper dbHelper = new HuellaDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insert(HuellaContract.HuellaEntry.TABLE_NAME, null, toContentValues());
        Log.d("Huella", getHuellaCarbono() + " gCO2e");
    }

    public String getAllRows(Context context) {
        HuellaDBHelper dbHelper = new HuellaDBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(HuellaContract.HuellaEntry.TABLE_NAME, null, null, null, null, null, null);
        String huella = HuellaContract.HuellaEntry._ID + "," + HuellaContract.HuellaEntry.COLUMN_ESCANEO_ID + "," +
                HuellaContract.HuellaEntry.COLUMN_HUELLA_CARBONO + ";";
        while (cursor.moveToNext()) {
            huella += cursor.getInt(cursor.getColumnIndexOrThrow(HuellaContract.HuellaEntry._ID)) + "," +
            cursor.getInt(cursor.getColumnIndexOrThrow(HuellaContract.HuellaEntry.COLUMN_ESCANEO_ID)) + "," +
            cursor.getFloat(cursor.getColumnIndexOrThrow(HuellaContract.HuellaEntry.COLUMN_HUELLA_CARBONO)) + ";";
        }
        cursor.close();

        return huella;
    }

    public float getTotalHuellaCarbono(Context context) {
        HuellaDBHelper dbHelper = new HuellaDBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {HuellaContract.HuellaEntry.COLUMN_HUELLA_CARBONO};
        Cursor cursor = db.query(HuellaContract.HuellaEntry.TABLE_NAME, columns, null, null, null, null, null);
        float totalHuellaCarbono = 0;
        while (cursor.moveToNext()) {
            totalHuellaCarbono += cursor.getFloat(cursor.getColumnIndexOrThrow(HuellaContract.HuellaEntry.COLUMN_HUELLA_CARBONO));
        }

        return totalHuellaCarbono;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(HuellaContract.HuellaEntry.COLUMN_ESCANEO_ID, getEscaneoId());
        values.put(HuellaContract.HuellaEntry.COLUMN_HUELLA_CARBONO, getHuellaCarbono());

        return values;
    }

    public int getEscaneoId() {
        return escaneoId;
    }

    public float getHuellaCarbono() {
        return huellaCarbono;
    }

    public float getVoltage() {
        return voltage;
    }

    public void setEscaneoId(int escaneoId) {
        this.escaneoId = escaneoId;
    }

    public void setDatosConsumo(int datosConsumo) {
        this.datosConsumo = datosConsumo;
    }

    public void setVoltage(float voltage) {
        this.voltage = voltage;
    }
}
