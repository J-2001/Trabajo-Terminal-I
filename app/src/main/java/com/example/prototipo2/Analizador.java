package com.example.prototipo2;

import android.app.Notification;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Analizador extends Service {

    private static final DecimalFormat df = new DecimalFormat("0.00");
    private int previousRowId;
    private int previousRowChargeCounter;
    private long previousRowTimeStamp;
    private int currentRowId;
    private int currentRowChargeCounter;
    private long currentRowTimeStamp;
    private float averageVoltage;
    private int voltageCounter;
    private int status;
    private float ccpm; // x
    private float media;
    private float desvEst;
    private float pz;
    private Bateria bateria;
    private Escaneo escaneo;
    private Timer timer;
    private TimerTask timerTask;

    @Override
    public void onCreate() {
        previousRowId = 0;
        previousRowChargeCounter = 0;
        previousRowTimeStamp = 0;
        currentRowId = 0;
        currentRowChargeCounter = 0;
        currentRowTimeStamp = 0;
        averageVoltage = 0;
        voltageCounter = 0;
        status = 0;
        ccpm = 0;
        media = 0;
        desvEst = 0;
        pz = 0;
        bateria = new Bateria(getApplicationContext());
        escaneo = new Escaneo(getApplicationContext());
        timer = new Timer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        currentRowId = bateria.updateValues();
        currentRowChargeCounter = bateria.getChargeCounter();
        currentRowTimeStamp = bateria.getTimeStamp();
        averageVoltage = bateria.getVoltage();
        voltageCounter += 1;

        escaneo.setStartId(currentRowId);
        escaneo.setStartTimeStamp(currentRowTimeStamp);

        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (bateria.checkValues()) {
                    previousRowId = currentRowId;
                    previousRowChargeCounter = currentRowChargeCounter;
                    previousRowTimeStamp = currentRowTimeStamp;
                    currentRowId = bateria.updateValues();
                    Log.d("Bateria", "Nuevo registro!");
                    currentRowChargeCounter = bateria.getChargeCounter();
                    currentRowTimeStamp = bateria.getTimeStamp();
                    averageVoltage += bateria.getVoltage();
                    voltageCounter += 1;
                    status = bateria.getStatus();

                    int cc = Math.abs(currentRowChargeCounter - previousRowChargeCounter);
                    if (status == 3) {
                        escaneo.updateDatosConsumo(cc);
                    }

                    ccpm = (float) (cc * 60 / ((currentRowTimeStamp - previousRowTimeStamp) / 1000.0));

                    media = updateMedia();
                    Log.d("Media = ", String.valueOf(media));
                    desvEst = updateDesvEst();
                    Log.d("Desv. Est. =", String.valueOf(desvEst));

                    float x = 0;

                    if (desvEst != 0) {
                        x = (ccpm - media) / desvEst;
                    }

                    String z = df.format(x);

                    DistribucionNormalEstandar dne = new DistribucionNormalEstandar();
                    pz = dne.getP(Float.parseFloat(z));
                    Log.d("P(Z>x)=", String.valueOf(pz));

                    if (pz != -1) {
                        showNotification();
                    }

                    insertIntoDB();
                }
                Log.i("Analizador", "Nada...");
            }
        };
        Log.w("Analizador", "Analizador iniciado");
        timer.scheduleAtFixedRate(timerTask, 0, 10000);
        foregroundNotification();

        return START_REDELIVER_INTENT;
    }

    public void foregroundNotification() {
        Notification notification = new NotificationCompat.Builder(this, getString(R.string.channel_id_1))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Analizador")
                .setContentText("Analizador iniciado...")
                .build();
        startForeground(1, notification);
    }

    @Override
    public void onDestroy() {
        timerTask.cancel();
        escaneo.setEndId(currentRowId);
        escaneo.setEndTimeStamp(currentRowTimeStamp);
        averageVoltage = averageVoltage / voltageCounter;
        escaneo.setAverageVoltage(averageVoltage);
        escaneo.insertIntoDB();
        Log.w("Analizador", "Analizador destruido");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public float updateMedia() {
        ArrayList<Float> ccpms = getAllCCpm();

        if (ccpms.size() == 0) {
            return 0;
        }

        float sum = 0;

        for (Float ccpm : ccpms) {
            sum += ccpm;
        }

        return sum / ccpms.size();
    }

    public float updateDesvEst() {
        ArrayList<Float> ccpms = getAllCCpm();

        if (ccpms.size() == 0) {
            return 0;
        }

        float sum = 0;

        for (Float ccpm : ccpms) {
            sum += Math.pow(ccpm - media, 2);
        }

        return (float) Math.sqrt(sum / ccpms.size());
    }

    public void showNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.channel_id_2))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("P(Z>x)")
                .setContentText(String.valueOf(pz))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(2, builder.build());
        /* AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        /* Aquí se podrían buscar distintas fuertes de exceso de batería y mostrar distintos layouts para disminuirlas
        LayoutInflater inflater;

        builder.setTitle("P(Z)>x")
                .setMessage(String.valueOf(pz))
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("Analizador.showNotification()", "Dialog aceptado!");
                    }
                })
                .setNeutralButton("Omitir x s", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("Analizaodr.showNotification()", "Dialog omitido!");
                    }
                })
                .setNegativeButton("Descartar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("Analizador.showNotification()", "Dialog descartado!");
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show(); */
    }

    public void insertIntoDB() {
        try (AnalizadorDBHelper dbHelper = new AnalizadorDBHelper(getApplicationContext())) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.insert(AnalizadorContract.AnalizadorEntry.TABLE_NAME, null, toContentValues());
            Log.d("Analizador", "CCpm:" + getCcpm());
        } catch (Exception e) {
            Log.e("Analizador.insertIntoDB()", e.toString());
        }
    }

    public ArrayList<Float> getAllCCpm() {
        try (AnalizadorDBHelper dbHelper = new AnalizadorDBHelper(getApplicationContext())) {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String[] columns = {AnalizadorContract.AnalizadorEntry.COLUMN_CCPM};
            Cursor cursor = db.query(AnalizadorContract.AnalizadorEntry.TABLE_NAME, columns, null, null, null, null, null);
            ArrayList<Float> ccpms = new ArrayList<>();
            while (cursor.moveToNext()) {
                ccpms.add(cursor.getFloat(cursor.getColumnIndexOrThrow(AnalizadorContract.AnalizadorEntry.COLUMN_CCPM)));
            }
            cursor.close();

            return ccpms;
        } catch (Exception e) {
            Log.e("Analizador.getAllCCpm()", e.toString());

            return null;
        }
    }

    public String getAllData(Context context) {
        AnalizadorDBHelper dbHelper = new AnalizadorDBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(AnalizadorContract.AnalizadorEntry.TABLE_NAME, null, null, null, null, null, null);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(AnalizadorContract.AnalizadorEntry._ID + "," + AnalizadorContract.AnalizadorEntry.COLUMN_PREVIOUS_BATERIA_ID + "," +
                AnalizadorContract.AnalizadorEntry.COLUMN_CURRENT_BATERIA_ID + "," + AnalizadorContract.AnalizadorEntry.COLUMN_BATTERY_STATUS + "," +
                AnalizadorContract.AnalizadorEntry.COLUMN_CCPM + "," + AnalizadorContract.AnalizadorEntry.COLUMN_MEDIA + "," +
                AnalizadorContract.AnalizadorEntry.COLUMN_DESV_EST + "," + AnalizadorContract.AnalizadorEntry.COLUMN_PZ + ";");
        while (cursor.moveToNext()) {
            stringBuilder.append(cursor.getInt(cursor.getColumnIndexOrThrow(AnalizadorContract.AnalizadorEntry._ID)));
            stringBuilder.append(",");
            stringBuilder.append(cursor.getInt(cursor.getColumnIndexOrThrow(AnalizadorContract.AnalizadorEntry.COLUMN_PREVIOUS_BATERIA_ID)));
            stringBuilder.append(",");
            stringBuilder.append(cursor.getInt(cursor.getColumnIndexOrThrow(AnalizadorContract.AnalizadorEntry.COLUMN_CURRENT_BATERIA_ID)));
            stringBuilder.append(",");
            stringBuilder.append(cursor.getInt(cursor.getColumnIndexOrThrow(AnalizadorContract.AnalizadorEntry.COLUMN_BATTERY_STATUS)));
            stringBuilder.append(",");
            stringBuilder.append(cursor.getFloat(cursor.getColumnIndexOrThrow(AnalizadorContract.AnalizadorEntry.COLUMN_CCPM)));
            stringBuilder.append(",");
            stringBuilder.append(cursor.getFloat(cursor.getColumnIndexOrThrow(AnalizadorContract.AnalizadorEntry.COLUMN_MEDIA)));
            stringBuilder.append(",");
            stringBuilder.append(cursor.getFloat(cursor.getColumnIndexOrThrow(AnalizadorContract.AnalizadorEntry.COLUMN_DESV_EST)));
            stringBuilder.append(",");
            stringBuilder.append(cursor.getFloat(cursor.getColumnIndexOrThrow(AnalizadorContract.AnalizadorEntry.COLUMN_PZ)));
            stringBuilder.append(";");
        }
        cursor.close();

        return stringBuilder.toString();
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(AnalizadorContract.AnalizadorEntry.COLUMN_PREVIOUS_BATERIA_ID, getPreviousRowId());
        values.put(AnalizadorContract.AnalizadorEntry.COLUMN_CURRENT_BATERIA_ID, getCurrentRowId());
        values.put(AnalizadorContract.AnalizadorEntry.COLUMN_BATTERY_STATUS, getStatus());
        values.put(AnalizadorContract.AnalizadorEntry.COLUMN_CCPM, getCcpm());
        values.put(AnalizadorContract.AnalizadorEntry.COLUMN_MEDIA, getMedia());
        values.put(AnalizadorContract.AnalizadorEntry.COLUMN_DESV_EST, getDesvEst());
        values.put(AnalizadorContract.AnalizadorEntry.COLUMN_PZ, getPz());

        return values;
    }

    public int getPreviousRowId() {
        return previousRowId;
    }

    public int getCurrentRowId() {
        return currentRowId;
    }

    public int getStatus() {
        return status;
    }

    public float getCcpm() {
        return ccpm;
    }

    public float getMedia() {
        return media;
    }

    public float getDesvEst() {
        return desvEst;
    }

    public float getPz() {
        return pz;
    }
}
