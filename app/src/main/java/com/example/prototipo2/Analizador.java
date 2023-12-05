package com.example.prototipo2;

import android.app.Notification;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.Timer;
import java.util.TimerTask;

public class Analizador extends Service {
    // Cuando se cargue el dispositivo, terminaria manualmente un escaneo para iniciar otro

    private final String CHANNEL_ID = "AnalizadorConsumoDeEnergia";
    private int previousRowId;
    private int previousRowChargeCounter;
    private long previousRowTimeStamp;
    private int currentRowId;
    private int currentRowChargeCounter;
    private long currentRowTimeStamp;
    private float averageVoltage;
    private int status;
    private float ccpm; // x
    private float media;
    private float desvEst;
    // Propiedades para la distribucion normal estandar
    // Crear una clase con una propiedad que almacene los valores de la tabla de propiedades de una dne
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
        status = 0;
        ccpm = 0;
        media = 0;
        desvEst = 0;
        bateria = new Bateria(getApplicationContext());
        escaneo = new Escaneo(getApplicationContext());
        timer = new Timer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // iniciarModelo() sería la función que se ejecuta en segundo plano, como un TimerTask
        currentRowId = bateria.updateValues();
        currentRowChargeCounter = bateria.getChargeCounter();
        currentRowTimeStamp = bateria.getTimeStamp();
        averageVoltage = bateria.getVoltage();

        escaneo.setStartId(currentRowId);
        escaneo.setStartTimeStamp(currentRowTimeStamp);

        // Comenzar modelo
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
                    averageVoltage = (float) ((averageVoltage + bateria.getVoltage()) / 2.0);
                    status = bateria.getStatus();

                    int cc = Math.abs(currentRowChargeCounter - previousRowChargeCounter);
                    // Si el dispositivo esta cargandose, no aumentar los datos de consumo
                    if (status == 3) { // No cargandose
                        escaneo.updateDatosConsumo(cc);
                    }

                    ccpm = (float) (cc * 60 / ((currentRowTimeStamp-previousRowTimeStamp)/1000.0));

                    // Calculo de la media y la desviacion estandar

                    // Calculamos P(Z<=ccpm)

                    // Dependiendo del rango de P, podremos decir que hay un consumo excesivo de energía
                    // Si si lo hay, llamamos a mostrarNotificacion()

                    // Almacenamos en la base de datos "Analizador"
                    insertIntoDB();
                }
                Log.i("Analizador", "Nada...");
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 10000);
        foregroundNotification();
        Log.w("Analizador", "Analizador iniciado");

        return START_REDELIVER_INTENT;
    }

    public void foregroundNotification() {
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID).setContentTitle("Analizador").setContentText("Test").setSmallIcon(R.drawable.ic_launcher_foreground).build();
        startForeground(1, notification);
    }

    @Override
    public void onDestroy() {
        timerTask.cancel();
        escaneo.setEndId(currentRowId);
        escaneo.setEndTimeStamp(currentRowTimeStamp);
        escaneo.setAverageVoltage(averageVoltage);
        escaneo.insertIntoDB();
        Log.w("Analizador", "Analizador destruido");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void mostrarNotificacion() {
        // Se despliega una notificacion sobre el alto consumo de energía
        // Es aqui donde se podria analizar los componentes e intentar estimar cual genera el alto consumo
        // Para esto tambien se podria considerar ampliar la base de datos "Analizador" con otros datos (red, bufer, etc.)
    }

    public void insertIntoDB() {
        AnalizadorDBHelper dbHelper = new AnalizadorDBHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insert(AnalizadorContract.AnalizadorEntry.TABLE_NAME, null, toContentValues());
        Log.d("Analizador", "CCpm:" + getCcpm());
    }

    public String getAllData(Context context) {
        AnalizadorDBHelper dbHelper = new AnalizadorDBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(AnalizadorContract.AnalizadorEntry.TABLE_NAME, null, null, null, null, null, null);
        String analizador = AnalizadorContract.AnalizadorEntry._ID + "," + AnalizadorContract.AnalizadorEntry.COLUMN_PREVIOUS_BATERIA_ID + "," +
                AnalizadorContract.AnalizadorEntry.COLUMN_CURRENT_BATERIA_ID + "," + AnalizadorContract.AnalizadorEntry.COLUMN_BATTERY_STATUS + "," +
                AnalizadorContract.AnalizadorEntry.COLUMN_CCPM + "," + AnalizadorContract.AnalizadorEntry.COLUMN_MEDIA + "," +
                AnalizadorContract.AnalizadorEntry.COLUMN_DESV_EST + ";";
        while (cursor.moveToNext()) {
            analizador += cursor.getInt(cursor.getColumnIndexOrThrow(AnalizadorContract.AnalizadorEntry._ID)) + "," +
            cursor.getInt(cursor.getColumnIndexOrThrow(AnalizadorContract.AnalizadorEntry.COLUMN_PREVIOUS_BATERIA_ID)) + "," +
            cursor.getInt(cursor.getColumnIndexOrThrow(AnalizadorContract.AnalizadorEntry.COLUMN_CURRENT_BATERIA_ID)) + "," +
            cursor.getInt(cursor.getColumnIndexOrThrow(AnalizadorContract.AnalizadorEntry.COLUMN_BATTERY_STATUS)) + "," +
            cursor.getFloat(cursor.getColumnIndexOrThrow(AnalizadorContract.AnalizadorEntry.COLUMN_CCPM)) + "," +
            cursor.getFloat(cursor.getColumnIndexOrThrow(AnalizadorContract.AnalizadorEntry.COLUMN_MEDIA)) + "," +
            cursor.getFloat(cursor.getColumnIndexOrThrow(AnalizadorContract.AnalizadorEntry.COLUMN_DESV_EST)) + ";";
        }
        cursor.close();

        return analizador;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(AnalizadorContract.AnalizadorEntry.COLUMN_PREVIOUS_BATERIA_ID, getPreviousRowId());
        values.put(AnalizadorContract.AnalizadorEntry.COLUMN_CURRENT_BATERIA_ID, getCurrentRowId());
        values.put(AnalizadorContract.AnalizadorEntry.COLUMN_BATTERY_STATUS, getStatus());
        values.put(AnalizadorContract.AnalizadorEntry.COLUMN_CCPM, getCcpm());
        values.put(AnalizadorContract.AnalizadorEntry.COLUMN_MEDIA, getMedia());
        values.put(AnalizadorContract.AnalizadorEntry.COLUMN_DESV_EST, getDesvEst());

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
}
