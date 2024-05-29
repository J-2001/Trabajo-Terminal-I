package com.example.prototipo2;

import android.app.Notification;
import android.app.NotificationManager;
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
import androidx.core.content.ContextCompat;

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
    private int excessive;
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
        excessive = 0;
        tiempoIgnorar = 0;
        bateria = new Bateria(getApplicationContext());
        escaneo = new Escaneo(getApplicationContext());
        timer = new Timer();
        br = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter(getString(R.string.broadcast_action_1));
        ContextCompat.registerReceiver(this, br, filter, ContextCompat.RECEIVER_NOT_EXPORTED);
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

        String videoStreaming = intent.getStringExtra("videostreaming");
        escaneo.setVideoStreaming(videoStreaming);
        Log.i("Pruebas(40): ", "Video Streaming: " + videoStreaming);

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
                        // Dado que buscamos evaluar cuando la carga de la bateria se consume excesivamente se deben de evaluar dos posibles casos de uso:
                        if (status == 3) {
                            // Si el dispositivo NO se esta cargando solo nos interesan los CCpm que superan la media, o sea, la bateria esta disminuyendo a grandes intervalos
                            x = (ccpm - media) / desvEst;
                        } else {
                            // Si el dispositivo SI se esta cargando solo nos interesan los CCpm por debajo de la media, o sea, la bateria se esta cargando en intervalos muy bajos
                            x = (media - ccpm) / desvEst;
                        }
                        Log.i("Pruebas(33): ", "x: " + x);
                    }

                    String z = df.format(x);
                    Log.i("Pruebas(34): ", "z: " + z);

                    DistribucionNormalEstandar dne = new DistribucionNormalEstandar();
                    pz = dne.getP(Float.parseFloat(z));
                    Log.i("Pruebas(35): ", "pz:\nP(Z>x): " + pz);

                    if (pz != -1 && pz < 1F) {
                        excessive = 1;
                        if (tiempoIgnorar > 0) {
                            Log.i("Pruebas(36): ", "tiempoIgnorar: " + tiempoIgnorar);
                        } else {
                            Log.i("Pruebas(37): ", "Mostrando Notificacion...");
                            showNotification();
                        }
                    } else {
                        excessive = 0;
                    }

                    insertIntoDB();
                } else {
                    // Des-comentar para Personalizar la Notificación
                    showNotification();
                    Log.i("Pruebas(13): ", "bateria.checkValues(): false\nNo ha cambiado la carga de la bateria");
                }
                if (tiempoIgnorar > 0) {
                    tiempoIgnorar -= 1;
                    Log.i("Pruebas(40): ", "tiempoIgnorar: " + tiempoIgnorar);
                }
            }
        };
        Log.i("Pruebas(12): ", "Analizador - TimerTask Iniciado!");
        timer.schedule(timerTask, 0, 10000);
        foregroundNotification();

        return START_REDELIVER_INTENT;
    }

    public void foregroundNotification() {
        Notification notification = new NotificationCompat.Builder(this, getString(R.string.channel_id_1))
                .setSmallIcon(R.drawable.ic_logoappfondo)
                .setContentTitle("Analizador Iniciado")
                .setContentText("Analizando el comportamiento de la batería...")
                .build();
        startForeground(1, notification);
    }

    @Override
    public void onDestroy() {
        timerTask.cancel();
        escaneo.setEndId(currentRowId);
        if (escaneo.getStartId() != escaneo.getEndId()) {
            escaneo.setEndTimeStamp(currentRowTimeStamp);
            averageVoltage = averageVoltage / voltageCounter;
            escaneo.setAverageVoltage(averageVoltage);
            escaneo.insertIntoDB();
        }
        unregisterReceiver(br);
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
            Log.d("Analizador MyBroadcastReceiver", intent.getAction());
            tiempoIgnorar = intent.getIntExtra("MIN", 1) * 6;
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.cancel(2);
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
        intent.putExtra("timestamp", currentRowTimeStamp);
        intent.putExtra("averagevoltage", averageVoltage / voltageCounter);
        intent.putExtra("status", status);
        intent.putExtra("ccpm", ccpm);
        intent.putExtra("media", media);
        intent.putExtra("desvest", desvEst);
        intent.putExtra("pz", pz);
        intent.putExtra("videostreaming", escaneo.getVideoStreaming());
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Intent ignorar = new Intent();
        ignorar.setAction(getString(R.string.broadcast_action_1));
        ignorar.putExtra("MIN", 5);
        PendingIntent ignorarPendingIntent = PendingIntent.getBroadcast(this, 0, ignorar, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.channel_id_2))
                .setSmallIcon(R.drawable.ic_logoappfondo)
                .setContentTitle("Consumo de Energía Excesivo Detectado")
                .setContentText("Presione para más información....")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_logoappfondo, getString(R.string.broadcast_action_1), ignorarPendingIntent)
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
            Log.i("Pruebas(38): ", "No se tiene permiso para mostrar notificaciones!");
            return;
        }

        notificationManager.notify(2, builder.build());
        Log.i("Pruebas(39): ", "Notificacion mostrada!");
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

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(AnalizadorContract.AnalizadorEntry.COLUMN_PREVIOUS_BATERIA_ID, getPreviousRowId());
        values.put(AnalizadorContract.AnalizadorEntry.COLUMN_CURRENT_BATERIA_ID, getCurrentRowId());
        values.put(AnalizadorContract.AnalizadorEntry.COLUMN_BATTERY_STATUS, getStatus());
        values.put(AnalizadorContract.AnalizadorEntry.COLUMN_CCPM, getCcpm());
        values.put(AnalizadorContract.AnalizadorEntry.COLUMN_MEDIA, getMedia());
        values.put(AnalizadorContract.AnalizadorEntry.COLUMN_DESV_EST, getDesvEst());
        values.put(AnalizadorContract.AnalizadorEntry.COLUMN_PZ, getPz());
        values.put(AnalizadorContract.AnalizadorEntry.COLUMN_EXCESSIVE, getExcessive());

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

    public int getExcessive() {
        return excessive;
    }
}
