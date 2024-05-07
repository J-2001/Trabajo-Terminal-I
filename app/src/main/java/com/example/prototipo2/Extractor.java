package com.example.prototipo2;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Extractor extends Worker {

    public Extractor(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        Log.i("Pruebas(05): ", "Extractor!");
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i("Pruebas(06): ", "doWork()");
        String data = getDeviceInfo();

        String accion = "";

        switch (0) {
            case 0:
                accion = "Info";
                Log.i("Pruebas(07): ", "Accion: " + accion);
                break;
            case 1:
                Bateria bateria = new Bateria(getApplicationContext());
                String bateriaDB = bateria.getAllRows();
                String analizadorDB = getAnalizadorRows();
                Escaneo escaneo = new Escaneo(getApplicationContext());
                String escaneoDB = escaneo.getAllScans();
                Huella huella = new Huella(getApplicationContext());
                String huellaDB = huella.getAllRows();

                data += "." + bateriaDB + "." + analizadorDB + "." + escaneoDB + "." + huellaDB;
                accion = "Extraction";
                break;
        }

        try {
            Log.i("Pruebas(08): ", "Try...!");
            URL url = new URL("https://trabajo-terminal-servidor.uc.r.appspot.com");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            try {
                Log.i("Pruebas(09): ", "openConnection()!");
                String body = "{\"data\": \"" + data + "\"}";
                urlConnection.setDoOutput(true);
                urlConnection.setFixedLengthStreamingMode(body.getBytes().length);
                urlConnection.setConnectTimeout(10000);

                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accion", accion);
                Log.i("Pruebas(10): ", "Before OutputStream!");

                OutputStream os = new BufferedOutputStream(urlConnection.getOutputStream());
                os.write(body.getBytes());
                os.flush();
                os.close();
                Log.i("Pruebas(11): ", "After flush()");

                InputStream is = new BufferedInputStream(urlConnection.getInputStream());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int nRead;
                while ((nRead = is.read(buffer)) != -1) {
                    baos.write(buffer, 0, nRead);
                }
                String response = baos.toString();
                Log.i("Pruebas(12): ", "response: " + response);

                Log.i("Extractor Response: ", response);

                return Result.success();
            } catch (Exception e) {
                throw new Exception(e.getCause());
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            Log.e("Extractor Error: ", e.toString());
            return Result.failure();
        }
    }

    public String getDeviceInfo() {
        return "Fabricante:" + Build.MANUFACTURER + ";Marca:" + Build.BRAND + ";Modelo:" + Build.MODEL + ";Android:" + Build.VERSION.RELEASE;
    }

    public String getAnalizadorRows() {
        AnalizadorDBHelper dbHelper = new AnalizadorDBHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {AnalizadorContract.AnalizadorEntry._ID, AnalizadorContract.AnalizadorEntry.COLUMN_PREVIOUS_BATERIA_ID,
                AnalizadorContract.AnalizadorEntry.COLUMN_CURRENT_BATERIA_ID, AnalizadorContract.AnalizadorEntry.COLUMN_BATTERY_STATUS,
                AnalizadorContract.AnalizadorEntry.COLUMN_CCPM, AnalizadorContract.AnalizadorEntry.COLUMN_MEDIA,
                AnalizadorContract.AnalizadorEntry.COLUMN_DESV_EST, AnalizadorContract.AnalizadorEntry.COLUMN_PZ,
                AnalizadorContract.AnalizadorEntry.COLUMN_EXCESSIVE};
        Cursor cursor = db.query(AnalizadorContract.AnalizadorEntry.TABLE_NAME, columns, null, null, null, null, null);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(columns[0]);
        stringBuilder.append(",");
        stringBuilder.append(columns[1]);
        stringBuilder.append(",");
        stringBuilder.append(columns[2]);
        stringBuilder.append(",");
        stringBuilder.append(columns[3]);
        stringBuilder.append(",");
        stringBuilder.append(columns[4]);
        stringBuilder.append(",");
        stringBuilder.append(columns[5]);
        stringBuilder.append(",");
        stringBuilder.append(columns[6]);
        stringBuilder.append(",");
        stringBuilder.append(columns[7]);
        stringBuilder.append(",");
        stringBuilder.append(columns[8]);
        stringBuilder.append(";");
        while (cursor.moveToNext()) {
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
            stringBuilder.append(cursor.getFloat(cursor.getColumnIndexOrThrow(columns[5])));
            stringBuilder.append(",");
            stringBuilder.append(cursor.getFloat(cursor.getColumnIndexOrThrow(columns[6])));
            stringBuilder.append(",");
            stringBuilder.append(cursor.getFloat(cursor.getColumnIndexOrThrow(columns[7])));
            stringBuilder.append(",");
            stringBuilder.append(cursor.getInt(cursor.getColumnIndexOrThrow(columns[8])));
            stringBuilder.append(";");
        }
        cursor.close();
        return stringBuilder.toString();
    }

}
