package com.example.prototipo2;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class Analizador extends Service {
    // Cuando se cargue el dispositivo, terminaria manualmente un escaneo para iniciar otro

    private int previousRowId;
    private int previousRowChargeCounter;
    private long previousRowTimeStamp;
    private int currentRowId;
    private int currentRowChargeCounter;
    private long currentRowTimeStamp;
    // Propiedades para la distribucion normal estandar
    // Crear una clase con una propiedad que almacene los valores de la tabla de propiedades de una dne
    private int status;
    private float ccpm; // x
    private float media;
    private float desvest;
    private Bateria bateria;
    private Escaneo escaneo;

    @Override
    public void onCreate() {
        previousRowId = 0;
        previousRowChargeCounter = 0;
        previousRowTimeStamp = 0;
        currentRowId = 0;
        currentRowChargeCounter = 0;
        currentRowTimeStamp = 0;
        status = 0;
        ccpm = 0;
        media = 0;
        desvest = 0;
        bateria = new Bateria(getApplicationContext());
        escaneo = new Escaneo(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // iniciarModelo() sería la función que se ejecuta en segundo plano, como un TimerTask
        currentRowId = bateria.updateValues();
        currentRowChargeCounter = bateria.getChargeCounter();
        currentRowTimeStamp = bateria.getTimeStamp();

        escaneo.setStartId(currentRowId);
        escaneo.setStartTimeStamp(currentRowTimeStamp);

        // Comenzar modelo
        // modelo()

        return START_REDELIVER_INTENT;
    }

    public void modelo() {
        // Esta funcion sería el TimerTask()
        if (bateria.checkValues()) {
            previousRowId = currentRowId;
            previousRowChargeCounter = currentRowChargeCounter;
            previousRowTimeStamp = currentRowTimeStamp;
            currentRowId = bateria.updateValues();
            currentRowChargeCounter = bateria.getChargeCounter();
            currentRowTimeStamp = bateria.getTimeStamp();

            int cc = currentRowChargeCounter - previousRowChargeCounter;
            escaneo.updateDatosConsumo(cc);

            ccpm = (float) (cc * 60 / ((currentRowTimeStamp-previousRowTimeStamp)/1000.0));

            // Calculo de la media y la desviacion estandar
        }
        // escaneo.updateDatosConsumo()
        // Calculo del CCpm comparando este y el ultimo registro de Bateria
        // Almacenamos en la base de datos "Analizador"
        // Modelo matematico
        // Si se detecta un alto consumo llamamos a mostrarNotificacion()
    }

    public void mostrarNotificacion() {
        // Se despliega una notificacion sobre el alto consumo de energía
        // Es aqui donde se podria analizar los componentes e intentar estimar cual genera el alto consumo
        // Para esto tambien se podria considerar ampliar la base de datos "Analizador" con otros datos (red, bufer, etc.)
    }

    public void terminarAnalizador() {
        // Esta funcion seria el onDestroy() de un Service, y deberia terminar el funcionamiento de modelo()
        // escaneo.setEndId()
        // escaneo.setEndTimeStamp()
        // escaneo.insertIntoDB()
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
