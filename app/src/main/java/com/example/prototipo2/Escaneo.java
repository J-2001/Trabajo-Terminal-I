package com.example.prototipo2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Escaneo {

    private int startId;
    private long startTimeStamp;
    private int endId;
    private long endTimeStamp;
    private long duracion;
    private int datosConsumo;
    private float averageVoltage;
    private final Context applicationContext;

    public Escaneo(Context context) {
        this.startId = 0;
        this.startTimeStamp = 0;
        this.endId = 0;
        this.endTimeStamp = 0;
        this.duracion = 0;
        this.datosConsumo = 0;
        this.applicationContext = context;
    }

    public void insertIntoDB() {
        try (EscaneoDBHelper dbHelper = new EscaneoDBHelper(applicationContext)) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Huella huella = new Huella();
            huella.setEscaneoId((int) db.insert(EscaneoContract.EscaneoEntry.TABLE_NAME, null, toContentVales()));
            Log.d("Escaneo", "Escaneo Completado!");
            huella.setDatosConsumo(getDatosConsumo());
            huella.setVoltage(getAverageVoltage());
            huella.calcularHuellaCarbono(applicationContext);
        } catch (Exception e) {
            Log.e("Escaneo.insertIntoDB()", e.toString());
        }
    }

    public int[] getLastScanIDs() {
        EscaneoDBHelper dbHelper = new EscaneoDBHelper(applicationContext);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = EscaneoContract.EscaneoEntry._ID + " = (SELECT MAX(" + EscaneoContract.EscaneoEntry._ID + ") FROM " + EscaneoContract.EscaneoEntry.TABLE_NAME + ")";
        Cursor cursor = db.query(EscaneoContract.EscaneoEntry.TABLE_NAME, null, selection, null, null, null, null);
        cursor.moveToNext();
        int[] ids = {cursor.getInt(cursor.getColumnIndexOrThrow(EscaneoContract.EscaneoEntry.COLUMN_START_BATERIA_ID)), cursor.getInt(cursor.getColumnIndexOrThrow(EscaneoContract.EscaneoEntry.COLUMN_END_BATERIA_ID))};
        cursor.close();

        return ids;
    }

    public String getAllScans() {
        EscaneoDBHelper dbHelper = new EscaneoDBHelper(applicationContext);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(EscaneoContract.EscaneoEntry.TABLE_NAME, null, null, null, null, null, null);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(EscaneoContract.EscaneoEntry._ID + "," + EscaneoContract.EscaneoEntry.COLUMN_START_BATERIA_ID + "," +
                EscaneoContract.EscaneoEntry.COLUMN_END_BATERIA_ID + "," + EscaneoContract.EscaneoEntry.COLUMN_DURACION_ESCANEO + "," +
                EscaneoContract.EscaneoEntry.COLUMN_DATOS_CONSUMO + "," + EscaneoContract.EscaneoEntry.COLUMN_AVERAGE_VOLTAGE + ";");
        while (cursor.moveToNext()) {
            stringBuilder.append(cursor.getInt(cursor.getColumnIndexOrThrow(EscaneoContract.EscaneoEntry._ID)));
            stringBuilder.append(",");
            stringBuilder.append(cursor.getInt(cursor.getColumnIndexOrThrow(EscaneoContract.EscaneoEntry.COLUMN_START_BATERIA_ID)));
            stringBuilder.append(",");
            stringBuilder.append(cursor.getInt(cursor.getColumnIndexOrThrow(EscaneoContract.EscaneoEntry.COLUMN_END_BATERIA_ID)));
            stringBuilder.append(",");
            stringBuilder.append(cursor.getInt(cursor.getColumnIndexOrThrow(EscaneoContract.EscaneoEntry.COLUMN_DURACION_ESCANEO)));
            stringBuilder.append(",");
            stringBuilder.append(cursor.getInt(cursor.getColumnIndexOrThrow(EscaneoContract.EscaneoEntry.COLUMN_DATOS_CONSUMO)));
            stringBuilder.append(",");
            stringBuilder.append(cursor.getFloat(cursor.getColumnIndexOrThrow(EscaneoContract.EscaneoEntry.COLUMN_AVERAGE_VOLTAGE)));
            stringBuilder.append(";");
        }
        cursor.close();

        return stringBuilder.toString();
    }

    public void updateDatosConsumo(int datos) {
        this.datosConsumo += datos;
    }

    public ContentValues toContentVales() {
        ContentValues values = new ContentValues();
        values.put(EscaneoContract.EscaneoEntry.COLUMN_START_BATERIA_ID, getStartId());
        values.put(EscaneoContract.EscaneoEntry.COLUMN_END_BATERIA_ID, getEndId());
        values.put(EscaneoContract.EscaneoEntry.COLUMN_DURACION_ESCANEO, getDuracion());
        values.put(EscaneoContract.EscaneoEntry.COLUMN_DATOS_CONSUMO, getDatosConsumo());
        values.put(EscaneoContract.EscaneoEntry.COLUMN_AVERAGE_VOLTAGE, getAverageVoltage());

        return values;
    }

    public int getStartId() {
        return startId;
    }

    public long getStartTimeStamp() {
        return startTimeStamp;
    }

    public int getEndId() {
        return endId;
    }

    public long getEndTimeStamp() {
        return endTimeStamp;
    }

    public long getDuracion() {
        duracion = getEndTimeStamp() - getStartTimeStamp();
        return duracion;
    }

    public int getDatosConsumo() {
        return datosConsumo;
    }

    public float getAverageVoltage() {
        return averageVoltage;
    }

    public void setStartId(int startId) {
        this.startId = startId;
    }

    public void setStartTimeStamp(long startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    public void setEndId(int endId) {
        this.endId = endId;
    }

    public void setEndTimeStamp(long endTimeStamp) {
        this.endTimeStamp = endTimeStamp;
    }

    public void setAverageVoltage(float averageVoltage) {
        this.averageVoltage = averageVoltage;
    }
}
