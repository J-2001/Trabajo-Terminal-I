package com.example.prototipo2;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import java.util.Objects;
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
    private int tiempoIgnorar;
    private BroadcastReceiver br;
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
        tiempoIgnorar = 0;
        bateria = new Bateria(getApplicationContext());
        escaneo = new Escaneo(getApplicationContext());
        timer = new Timer();
        br = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter(getString(R.string.broadcast_action_1));
        this.registerReceiver(br, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        currentRowId = bateria.updateValues();
        Log.i("Pruebas(07): ", "currentRowId: " + currentRowId);
        currentRowChargeCounter = bateria.getChargeCounter();
        Log.i("Pruebas(08): ", "currentRowChargeCounter: " + currentRowChargeCounter);
        currentRowTimeStamp = bateria.getTimeStamp();
        Log.i("Pruebas(09): ", "currentRowTimeStamp: " + currentRowTimeStamp);
        averageVoltage = bateria.getVoltage();
        Log.i("Pruebas(10): ", "averageVoltage: " + averageVoltage);
        voltageCounter += 1;
        Log.i("Pruebas(11): ", "voltageCounter: " + voltageCounter);

        escaneo.setStartId(currentRowId);
        escaneo.setStartTimeStamp(currentRowTimeStamp);

        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (bateria.checkValues()) {
                    Log.i("Pruebas(14): ", "bateria.checkValues(): true\nCambio en la carga de la bateria");
                    previousRowId = currentRowId;
                    Log.i("Pruebas(15): ", "previousRowId: " + previousRowId);
                    previousRowChargeCounter = currentRowChargeCounter;
                    Log.i("Pruebas(16): ", "previousRowChargeCounter: " + previousRowChargeCounter);
                    previousRowTimeStamp = currentRowTimeStamp;
                    Log.i("Pruebas(17): ", "previousRowTimeStamp: " + previousRowTimeStamp);
                    currentRowId = bateria.updateValues();
                    Log.i("Pruebas(18): ", "currentRowId: " + currentRowId);
                    currentRowChargeCounter = bateria.getChargeCounter();
                    Log.i("Pruebas(19): ", "currentRowChargeCounter: " + currentRowChargeCounter);
                    currentRowTimeStamp = bateria.getTimeStamp();
                    Log.i("Pruebas(20): ", "currentRowTimeStamp: " + currentRowTimeStamp);
                    averageVoltage += bateria.getVoltage();
                    Log.i("Pruebas(21): ", "averageVoltage: " + averageVoltage);
                    voltageCounter += 1;
                    Log.i("Pruebas(22): ", "voltageCounter: " + voltageCounter);
                    status = bateria.getStatus();
                    Log.i("Pruebas(23): ", "status: " + status);

                    int cc = Math.abs(currentRowChargeCounter - previousRowChargeCounter);
                    Log.i("Pruebas(24): ", "cc: " + cc);
                    if (status == 3) {
                        Log.i("Pruebas(25): ", "status: " + status + "\nEl dispositivo no se esta cargando");
                        escaneo.updateDatosConsumo(cc);
                    }

                    ccpm = (float) (cc * 60 / ((currentRowTimeStamp - previousRowTimeStamp) / 1000.0));
                    Log.i("Pruebas(26): ", "ccpm: " + ccpm);

                    media = updateMedia();
                    Log.i("Pruebas(29): ", "media: " + media);
                    desvEst = updateDesvEst();
                    Log.i("Pruebas(32): ", "desvEst: " + desvEst);

                    float x = 0;

                    if (desvEst != 0) {
                        x = (ccpm - media) / desvEst;
                        Log.i("Pruebas(33): ", "x: " + x);
                    }

                    String z = df.format(x);
                    Log.i("Pruebas(34): ", "z: " + z);

                    DistribucionNormalEstandar dne = new DistribucionNormalEstandar();
                    pz = dne.getP(Float.parseFloat(z));
                    Log.i("Pruebas(35): ", "pz:\nP(Z>x): " + pz);

                    if (pz != -1) {
                        if (tiempoIgnorar > 0) {
                            Log.i("Pruebas(36): ", "tiempoIgnorar: " + tiempoIgnorar);
                        } else {
                            showNotification();
                        }
                    }

                    insertIntoDB();
                } else {
                    Log.i("Pruebas(13): ", "bateria.checkValues(): false\nNo ha cambiado la carga de la bateria");
                }
                if (tiempoIgnorar > 0) {
                    tiempoIgnorar -= 1;
                }
            }
        };
        Log.i("Pruebas(12): ", "Analizador - TimerTask Iniciado!");
        timer.scheduleAtFixedRate(timerTask, 0, 10000);
        foregroundNotification();

        return START_REDELIVER_INTENT;
    }

    public void foregroundNotification() {
        Notification notification = new NotificationCompat.Builder(this, getString(R.string.channel_id_1))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Trabajo Terminal")
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
        this.unregisterReceiver(br);
        Log.w("Analizador", "Analizador destruido");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("Analizador MyBroadcastReceiver", Objects.requireNonNull(intent.getAction()));
            tiempoIgnorar = intent.getIntExtra("MIN", 0) * 6;
        }
    }

    public float updateMedia() {
        ArrayList<Float> ccpms = getAllCCpm();
        Log.i("Pruebas(27): ", "ccpms: " + ccpms);

        if (ccpms.isEmpty()) {
            return 0;
        }

        float sum = 0;

        for (Float ccpm : ccpms) {
            sum += ccpm;
        }
        Log.i("Pruebas(28): ", "sum: " + sum);

        return sum / ccpms.size();
    }

    public float updateDesvEst() {
        ArrayList<Float> ccpms = getAllCCpm();
        Log.i("Pruebas(30): ", "ccpms: " + ccpms);

        if (ccpms.isEmpty()) {
            return 0;
        }

        float sum = 0;

        for (Float ccpm : ccpms) {
            sum += (float) Math.pow(ccpm - media, 2);
        }
        Log.i("Pruebas(31): ", "sum: " + sum);

        return (float) Math.sqrt(sum / ccpms.size());
    }

    public void showNotification() {
        Intent intent = new Intent(this, NotificationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(getString(R.string.extra_0), 1);
        intent.putExtra(getString(R.string.extra_1_1), ccpm);
        intent.putExtra(getString(R.string.extra_1_2), media);
        intent.putExtra(getString(R.string.extra_1_3), desvEst);
        intent.putExtra(getString(R.string.extra_1_4), pz);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        Intent ignorar = new Intent(this, MyBroadcastReceiver.class);
        ignorar.setAction(getString(R.string.broadcast_action_1));
        ignorar.putExtra("MIN", 1);
        PendingIntent ignorarPendingIntent = PendingIntent.getBroadcast(this, 0, ignorar, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.channel_id_2))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("P(Z>x)")
                .setContentText(String.valueOf(pz))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_launcher_foreground, "Omitir (1 min)", ignorarPendingIntent)
                .setAutoCancel(true);
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
            Log.i("Error(02): ", "Analizador.getAllCCpm(): " + e);

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
