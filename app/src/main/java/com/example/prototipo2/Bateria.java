package com.example.prototipo2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.BatteryManager;
import android.util.Log;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class Bateria {

    private int chargeCounter;
    private int capacity;
    private int status;
    private float voltage;
    private long timeStamp;
    private final Context applicationContext;
    private final BatteryManager batteryManager;

    public Bateria(Context context) {
        this.chargeCounter = 0;
        this.capacity = 0;
        this.status = 0;
        this.voltage = 0;
        this.timeStamp = 0;
        this.applicationContext = context;
        this.batteryManager = (BatteryManager) applicationContext.getSystemService(Context.BATTERY_SERVICE);
    }

    public boolean checkValues() {
        return batteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER) != this.chargeCounter;
    }

    public int updateValues() {
        this.chargeCounter = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER);
        this.capacity = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        Log.i("Pruebas(03): ", "capacity: " + this.capacity);
        this.status = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_STATUS);
        Log.i("Pruebas(04): ", "status: " + this.status);
        Intent intent = applicationContext.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        assert intent != null;
        this.voltage = (float) (intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1) / 1000.0);
        Log.i("Pruebas(05): ", "voltage: " + this.voltage);
        Date date = new Date();
        this.timeStamp = date.getTime();

        return insertIntoDB();
    }

    private int insertIntoDB() {
        try (BateriaDBHelper dbHelper = new BateriaDBHelper(applicationContext)) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            return (int) db.insert(BateriaContract.BateriaEntry.TABLE_NAME, null, toContentValues());
        } catch (Exception e) {
            Log.i("Error(01): ", "Bateria.insertIntoDB(): " + e);
            return -1;
        }
    }

    public long getTimeStamp(int id) {
        BateriaDBHelper dbHelper = new BateriaDBHelper(applicationContext);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {BateriaContract.BateriaEntry.COLUMN_TIMESTAMP};
        String selection = BateriaContract.BateriaEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(BateriaContract.BateriaEntry.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        cursor.moveToNext();
        long ts = cursor.getLong(cursor.getColumnIndexOrThrow(columns[0]));
        cursor.close();
        return ts;
    }

    public Map<Long, Integer> getScanData(int startId, int endId) {
        BateriaDBHelper dbHelper = new BateriaDBHelper(applicationContext);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {BateriaContract.BateriaEntry.COLUMN_CHARGE_COUNTER, BateriaContract.BateriaEntry.COLUMN_TIMESTAMP};
        String selection = BateriaContract.BateriaEntry._ID + " >= ? AND " + BateriaContract.BateriaEntry._ID + " <= ?";
        String[] selectionArgs = {String.valueOf(startId), String.valueOf(endId)};
        Cursor cursor = db.query(BateriaContract.BateriaEntry.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        Map<Long, Integer> data = new LinkedHashMap<>();
        while (cursor.moveToNext()) {
            data.put(cursor.getLong(cursor.getColumnIndexOrThrow(columns[1])), cursor.getInt(cursor.getColumnIndexOrThrow(columns[0])));
        }
        cursor.close();
        return data;
    }

    public Map<Long, Integer> getRowsDataFilteredByStatus(int stat) {
        BateriaDBHelper dbHelper = new BateriaDBHelper(applicationContext);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {BateriaContract.BateriaEntry.COLUMN_CHARGE_COUNTER, BateriaContract.BateriaEntry.COLUMN_TIMESTAMP};
        String selection = BateriaContract.BateriaEntry.COLUMN_BATTERY_STATUS + " = ?";
        String[] selectionArgs = {String.valueOf(stat)};
        Cursor cursor = db.query(BateriaContract.BateriaEntry.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        Map<Long, Integer> filteredData = new LinkedHashMap<>();
        while (cursor.moveToNext()) {
            filteredData.put(cursor.getLong(cursor.getColumnIndexOrThrow(columns[1])), cursor.getInt(cursor.getColumnIndexOrThrow(columns[0])));
        }
        cursor.close();
        return filteredData;
    }

    public Map<Long, Integer> getRowsDataFilteredByConsumption(int excessive) {
        AnalizadorDBHelper analizadorDBHelper = new AnalizadorDBHelper(applicationContext);
        SQLiteDatabase database = analizadorDBHelper.getReadableDatabase();
        String[] columnsA = {AnalizadorContract.AnalizadorEntry.COLUMN_CURRENT_BATERIA_ID};
        String selectionA = AnalizadorContract.AnalizadorEntry.COLUMN_EXCESSIVE + " = ?";
        String[] selectionArgsA = {String.valueOf(excessive)};
        Cursor cursorA = database.query(AnalizadorContract.AnalizadorEntry.TABLE_NAME, columnsA, selectionA, selectionArgsA, null, null, null);
        int[] ids = new int[cursorA.getCount()];
        int i = 0;
        while (cursorA.moveToNext()) {
            ids[i] = cursorA.getInt(cursorA.getColumnIndexOrThrow(columnsA[0]));
            i += 1;
        }
        cursorA.close();
        BateriaDBHelper dbHelper = new BateriaDBHelper(applicationContext);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {BateriaContract.BateriaEntry.COLUMN_CHARGE_COUNTER, BateriaContract.BateriaEntry.COLUMN_TIMESTAMP};
        String selection = BateriaContract.BateriaEntry._ID + " = ?";
        Map<Long, Integer> filteredData = new LinkedHashMap<>();
        for (int id : ids) {
            String[] selectionArgs = {String.valueOf(id)};
            Cursor cursor = db.query(BateriaContract.BateriaEntry.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
            cursor.moveToNext();
            filteredData.put(cursor.getLong(cursor.getColumnIndexOrThrow(columns[1])), cursor.getInt(cursor.getColumnIndexOrThrow(columns[0])));
            cursor.close();
        }
        return filteredData;
    }

    public Map<Long, Integer> getAllRowsData() {
        BateriaDBHelper dbHelper = new BateriaDBHelper(applicationContext);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {BateriaContract.BateriaEntry.COLUMN_CHARGE_COUNTER, BateriaContract.BateriaEntry.COLUMN_TIMESTAMP};
        Cursor cursor = db.query(BateriaContract.BateriaEntry.TABLE_NAME, columns, null, null, null, null, null);
        Map<Long, Integer> data = new LinkedHashMap<>();
        while (cursor.moveToNext()){
            data.put(cursor.getLong(cursor.getColumnIndexOrThrow(columns[1])), cursor.getInt(cursor.getColumnIndexOrThrow(columns[0])));
        }
        cursor.close();
        return data;
    }

    public String getAllRows() {
        BateriaDBHelper dbHelper = new BateriaDBHelper(applicationContext);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {BateriaContract.BateriaEntry._ID, BateriaContract.BateriaEntry.COLUMN_CHARGE_COUNTER,
                BateriaContract.BateriaEntry.COLUMN_BATTERY_CAPACITY, BateriaContract.BateriaEntry.COLUMN_BATTERY_STATUS,
                BateriaContract.BateriaEntry.COLUMN_BATTERY_VOLTAGE, BateriaContract.BateriaEntry.COLUMN_TIMESTAMP};
        Cursor cursor = db.query(BateriaContract.BateriaEntry.TABLE_NAME, columns, null, null, null, null, null);
        StringBuilder stringBuilder = new StringBuilder();
        while (cursor.moveToNext()) {
            stringBuilder.append(";");
            stringBuilder.append(cursor.getInt(cursor.getColumnIndexOrThrow(columns[0])));
            stringBuilder.append(",");
            stringBuilder.append(cursor.getInt(cursor.getColumnIndexOrThrow(columns[1])));
            stringBuilder.append(",");
            stringBuilder.append(cursor.getInt(cursor.getColumnIndexOrThrow(columns[2])));
            stringBuilder.append(",");
            stringBuilder.append(cursor.getInt(cursor.getColumnIndexOrThrow(columns[3])));
            stringBuilder.append(",");
            stringBuilder.append(cursor.getFloat(cursor.getColumnIndexOrThrow(columns[4])));
            stringBuilder.append(",");
            stringBuilder.append(cursor.getLong(cursor.getColumnIndexOrThrow(columns[5])));
        }
        cursor.close();

        String all = stringBuilder.toString();

        if (!all.isEmpty()) {
            return all.substring(1);
        }

        return all;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(BateriaContract.BateriaEntry.COLUMN_CHARGE_COUNTER, getChargeCounter());
        values.put(BateriaContract.BateriaEntry.COLUMN_BATTERY_CAPACITY, getCapacity());
        values.put(BateriaContract.BateriaEntry.COLUMN_BATTERY_STATUS, getStatus());
        values.put(BateriaContract.BateriaEntry.COLUMN_BATTERY_VOLTAGE, getVoltage());
        values.put(BateriaContract.BateriaEntry.COLUMN_TIMESTAMP, getTimeStamp());

        return values;
    }

    public int getChargeCounter() {
        return chargeCounter;
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
