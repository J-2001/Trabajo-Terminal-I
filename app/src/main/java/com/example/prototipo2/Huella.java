package com.example.prototipo2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Huella {

    private static final float factorEmision = 0.435F; // gCO2e/Wh
    private int escaneoId;
    private float huellaCarbono;
    private int datosConsumo;
    private float voltage;
    private final Context applicationContext;

    public Huella(Context context) {
        this.escaneoId = 0;
        this.huellaCarbono = 0;
        this.applicationContext = context;
    }

    public void calcularHuellaCarbono() {
        float n = (float) (datosConsumo / 1000000.0 * voltage); // Wh
        huellaCarbono = n * factorEmision; // gCO2e
        insertIntoDB();
    }

    public void insertIntoDB() {
        try (HuellaDBHelper dbHelper = new HuellaDBHelper(applicationContext)) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.insert(HuellaContract.HuellaEntry.TABLE_NAME, null, toContentValues());
            Log.d("Huella", getHuellaCarbono() + " gCO2e");
        } catch (Exception e) {
            Log.e("Huella.insertIntoDB()", e.toString());
        }
    }

    public String getAllRows() {
        HuellaDBHelper dbHelper = new HuellaDBHelper(applicationContext);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {HuellaContract.HuellaEntry.COLUMN_ESCANEO_ID, HuellaContract.HuellaEntry.COLUMN_ESCANEO_ID,
                HuellaContract.HuellaEntry.COLUMN_HUELLA_CARBONO};
        Cursor cursor = db.query(HuellaContract.HuellaEntry.TABLE_NAME, columns, null, null, null, null, null);
        StringBuilder stringBuilder = new StringBuilder();
        while (cursor.moveToNext()) {
            stringBuilder.append(";");
            stringBuilder.append(cursor.getInt(cursor.getColumnIndexOrThrow(columns[0])));
            stringBuilder.append(",");
            stringBuilder.append(cursor.getInt(cursor.getColumnIndexOrThrow(columns[1])));
            stringBuilder.append(",");
            stringBuilder.append(cursor.getFloat(cursor.getColumnIndexOrThrow(columns[2])));
        }
        cursor.close();

        String all = stringBuilder.toString();

        if (!all.isEmpty()) {
            return all.substring(1);
        }

        return all;
    }

    public float getTotalHuellaCarbono() {
        try (HuellaDBHelper dbHelper = new HuellaDBHelper(applicationContext)) {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String[] columns = {HuellaContract.HuellaEntry.COLUMN_HUELLA_CARBONO};
            Cursor cursor = db.query(HuellaContract.HuellaEntry.TABLE_NAME, columns, null, null, null, null, null);
            float totalHuellaCarbono = 0;
            while (cursor.moveToNext()) {
                totalHuellaCarbono += cursor.getFloat(cursor.getColumnIndexOrThrow(HuellaContract.HuellaEntry.COLUMN_HUELLA_CARBONO));
            }
            cursor.close();

            return totalHuellaCarbono;
        } catch (Exception e) {
            Log.e("Huella.getTotalHuellaCarbono()", e.toString());
            return 0;
        }
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
