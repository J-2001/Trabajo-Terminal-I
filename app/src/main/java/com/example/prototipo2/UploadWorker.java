package com.example.prototipo2;

import android.content.Context;
import android.database.Cursor;
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

public class UploadWorker extends Worker {

    public UploadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String[] columns = {AnalizadorContract.AnalizadorEntry._ID, AnalizadorContract.AnalizadorEntry.COLUMN_PREVIOUS_BATERIA_ID,
                AnalizadorContract.AnalizadorEntry.COLUMN_CURRENT_BATERIA_ID, AnalizadorContract.AnalizadorEntry.COLUMN_BATTERY_STATUS,
                AnalizadorContract.AnalizadorEntry.COLUMN_CCPM, AnalizadorContract.AnalizadorEntry.COLUMN_MEDIA,
                AnalizadorContract.AnalizadorEntry.COLUMN_DESV_EST, AnalizadorContract.AnalizadorEntry.COLUMN_PZ,
                AnalizadorContract.AnalizadorEntry.COLUMN_EXCESSIVE};
        Cursor cursor = new AnalizadorDBHelper(getApplicationContext()).getReadableDatabase().query(AnalizadorContract.AnalizadorEntry.TABLE_NAME, columns, null, null, null, null, null);
        StringBuilder analizadorDB = new StringBuilder();
        while (cursor.moveToNext()) {
            analizadorDB.append(";").append(cursor.getInt(cursor.getColumnIndexOrThrow(columns[0]))).append(",").append(cursor.getInt(cursor.getColumnIndexOrThrow(columns[1])))
                    .append(",").append(cursor.getInt(cursor.getColumnIndexOrThrow(columns[2]))).append(",").append(cursor.getInt(cursor.getColumnIndexOrThrow(columns[3])))
                    .append(",").append(cursor.getFloat(cursor.getColumnIndexOrThrow(columns[4]))).append(",").append(cursor.getFloat(cursor.getColumnIndexOrThrow(columns[5])))
                    .append(",").append(cursor.getFloat(cursor.getColumnIndexOrThrow(columns[6]))).append(",").append(cursor.getFloat(cursor.getColumnIndexOrThrow(columns[7])))
                    .append(",").append(cursor.getInt(cursor.getColumnIndexOrThrow(columns[8])));
        }
        cursor.close();
        if (analizadorDB.length() > 0) {
            analizadorDB.deleteCharAt(0);
        }
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) new URL("https://trabajo-terminal-servidor.uc.r.appspot.com").openConnection();
            byte[] body = ("{\"data\": \"" + new Bateria(getApplicationContext()).getAllRows() + ":" + analizadorDB + ":" +
                    new Escaneo(getApplicationContext()).getAllScans() + ":" + new Huella(getApplicationContext()).getAllRows() + "\"}").getBytes();
            try {
                urlConnection.setDoOutput(true);
                urlConnection.setFixedLengthStreamingMode(body.length);
                urlConnection.setConnectTimeout(10000);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accion", "Upload");
                urlConnection.setRequestProperty("Token", getInputData().getString("TOKEN"));
                OutputStream os = new BufferedOutputStream(urlConnection.getOutputStream());
                os.write(body);
                os.flush();
                os.close();
                InputStream is = new BufferedInputStream(urlConnection.getInputStream());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int nRead;
                while ((nRead = is.read(buffer)) != -1) {
                    baos.write(buffer, 0, nRead);
                }
                Log.i("UploadWorker", baos.toString());
                return Result.success();
            } catch (Exception e) {
                throw new Exception(e);
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            Log.e("HttpURLConnection:", e.toString());
            return Result.failure();
        }
    }

}
