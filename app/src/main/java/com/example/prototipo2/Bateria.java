package com.example.prototipo2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.BatteryManager;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class Bateria {

    private int chargeCounter;
    private int currentNow;
    private int capacity;
    private int status;
    private float voltage;
    private long timeStamp;
    private final Context applicationContext;
    private final BatteryManager batteryManager;

    public Bateria(Context context) {
        this.chargeCounter = 0;
        this.currentNow = 0;
        this.capacity = 0;
        this.status = 0;
        this.voltage = 0;
        this.timeStamp = 0;
        this.applicationContext = context;
        this.batteryManager = (BatteryManager) applicationContext.getSystemService(Context.BATTERY_SERVICE);
    }

    public boolean checkValues() {
        // Metodo que nos indica si un valor de interes (chargeCounter) ha cambiado
        return batteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER) != this.chargeCounter;
    }

    public int updateValues() {
        // Metodo que actualiza los valores variables (o inicializa los que estan en cero)
        this.chargeCounter = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER);
        this.currentNow = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW);
        this.capacity = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        this.status = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_STATUS);
        Intent intent = applicationContext.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        this.voltage = (float) (intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1) / 1000.0);
        Date date = new Date();
        this.timeStamp = date.getTime();

        return insertIntoDB();
    }

    private int insertIntoDB() {
        // Insertamos los datos en la base de datos "Bateria"
        BateriaDBHelper dbHelper = new BateriaDBHelper(applicationContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return (int) db.insert(BateriaContract.BateriaEntry.TABLE_NAME, null, toContentValues());
    }

    public Map<Long, Integer> getLastScanData() {
        BateriaDBHelper dbHelper = new BateriaDBHelper(applicationContext);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Escaneo escaneo = new Escaneo(applicationContext);
        int[] ids = escaneo.getLastScanIDs();
        String[] columns = {BateriaContract.BateriaEntry.COLUMN_CHARGE_COUNTER, BateriaContract.BateriaEntry.COLUMN_TIMESTAMP};
        String selection = BateriaContract.BateriaEntry._ID + " >= ? AND " + BateriaContract.BateriaEntry._ID + " <= ?";
        String[] selectionArgs = {"" + ids[0], "" + ids[1]};
        Cursor cursor = db.query(BateriaContract.BateriaEntry.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        Map<Long, Integer> data = new TreeMap<Long, Integer>();
        while (cursor.moveToNext()) {
            data.put(cursor.getLong(cursor.getColumnIndexOrThrow(BateriaContract.BateriaEntry.COLUMN_TIMESTAMP)), cursor.getInt(cursor.getColumnIndexOrThrow(BateriaContract.BateriaEntry.COLUMN_CHARGE_COUNTER)));
        }
        cursor.close();

        return data;
    }

    public String getLastScan() {
        BateriaDBHelper dbHelper = new BateriaDBHelper(applicationContext);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Escaneo escaneo = new Escaneo(applicationContext);
        int[] ids = escaneo.getLastScanIDs();
        String selection = BateriaContract.BateriaEntry._ID + " >= ? AND " + BateriaContract.BateriaEntry._ID + " <= ?";
        String[] selectionArgs = {"" + ids[0], "" + ids[1]};
        Cursor cursor = db.query(BateriaContract.BateriaEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null);
        String bateria = BateriaContract.BateriaEntry._ID + "," + BateriaContract.BateriaEntry.COLUMN_CHARGE_COUNTER + "," +
                BateriaContract.BateriaEntry.COLUMN_CURRENT_NOW + "," + BateriaContract.BateriaEntry.COLUMN_BATTERY_CAPACITY + "," +
                BateriaContract.BateriaEntry.COLUMN_BATTERY_STATUS + "," + BateriaContract.BateriaEntry.COLUMN_BATTERY_VOLTAGE + "," +
                BateriaContract.BateriaEntry.COLUMN_TIMESTAMP + ";";
        while (cursor.moveToNext()) {
            bateria += cursor.getInt(cursor.getColumnIndexOrThrow(BateriaContract.BateriaEntry._ID)) + "," +
                    cursor.getInt(cursor.getColumnIndexOrThrow(BateriaContract.BateriaEntry.COLUMN_CHARGE_COUNTER)) + "," +
                    cursor.getInt(cursor.getColumnIndexOrThrow(BateriaContract.BateriaEntry.COLUMN_CURRENT_NOW)) + "," +
                    cursor.getInt(cursor.getColumnIndexOrThrow(BateriaContract.BateriaEntry.COLUMN_BATTERY_CAPACITY)) + "," +
                    cursor.getInt(cursor.getColumnIndexOrThrow(BateriaContract.BateriaEntry.COLUMN_BATTERY_STATUS)) + "," +
                    cursor.getFloat(cursor.getColumnIndexOrThrow(BateriaContract.BateriaEntry.COLUMN_BATTERY_VOLTAGE)) + "," +
                    cursor.getLong(cursor.getColumnIndexOrThrow(BateriaContract.BateriaEntry.COLUMN_TIMESTAMP)) + ";";
        }
        cursor.close();

        return bateria;
    }

    public String getAllRows() {
        // Regresa los datos en la base de datos
        BateriaDBHelper dbHelper = new BateriaDBHelper(applicationContext);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(BateriaContract.BateriaEntry.TABLE_NAME, null, null, null, null, null, null);
        String bateria = BateriaContract.BateriaEntry._ID + "," + BateriaContract.BateriaEntry.COLUMN_CHARGE_COUNTER + "," +
                BateriaContract.BateriaEntry.COLUMN_CURRENT_NOW + "," + BateriaContract.BateriaEntry.COLUMN_BATTERY_CAPACITY + "," +
                BateriaContract.BateriaEntry.COLUMN_BATTERY_STATUS + "," + BateriaContract.BateriaEntry.COLUMN_BATTERY_VOLTAGE + "," +
                BateriaContract.BateriaEntry.COLUMN_TIMESTAMP + ";";
        while (cursor.moveToNext()) {
            bateria += cursor.getInt(cursor.getColumnIndexOrThrow(BateriaContract.BateriaEntry._ID)) + "," +
            cursor.getInt(cursor.getColumnIndexOrThrow(BateriaContract.BateriaEntry.COLUMN_CHARGE_COUNTER)) + "," +
            cursor.getInt(cursor.getColumnIndexOrThrow(BateriaContract.BateriaEntry.COLUMN_CURRENT_NOW)) + "," +
            cursor.getInt(cursor.getColumnIndexOrThrow(BateriaContract.BateriaEntry.COLUMN_BATTERY_CAPACITY)) + "," +
            cursor.getInt(cursor.getColumnIndexOrThrow(BateriaContract.BateriaEntry.COLUMN_BATTERY_STATUS)) + "," +
            cursor.getFloat(cursor.getColumnIndexOrThrow(BateriaContract.BateriaEntry.COLUMN_BATTERY_VOLTAGE)) + "," +
            cursor.getLong(cursor.getColumnIndexOrThrow(BateriaContract.BateriaEntry.COLUMN_TIMESTAMP)) + ";";
        }
        cursor.close();

        return bateria;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(BateriaContract.BateriaEntry.COLUMN_CHARGE_COUNTER, getChargeCounter());
        values.put(BateriaContract.BateriaEntry.COLUMN_CURRENT_NOW, getCurrentNow());
        values.put(BateriaContract.BateriaEntry.COLUMN_BATTERY_CAPACITY, getCapacity());
        values.put(BateriaContract.BateriaEntry.COLUMN_BATTERY_STATUS, getStatus());
        values.put(BateriaContract.BateriaEntry.COLUMN_BATTERY_VOLTAGE, getVoltage());
        values.put(BateriaContract.BateriaEntry.COLUMN_TIMESTAMP, getTimeStamp());

        return values;
    }

    public int getChargeCounter() {
        return chargeCounter;
    }

    public int getCurrentNow() {
        return currentNow;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getStatus() {
        return status;
    }

    public float getVoltage() {
        return voltage;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

}
