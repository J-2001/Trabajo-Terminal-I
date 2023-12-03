package com.example.prototipo2;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class Analizador extends Service {
    // El constructor sería el onCreate() y el iniciarAnalizador() sería el onStartCommand()


    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void iniciarAnalizador() {
        // Como este método sera el onStartCommand(), el iniciarModelo() sería la función que se ejecuta en segundo plano, como un TimerTask
        // Bateria bateria = new Bateria();
        // bateria.updateValues();
        // bateria.getTimeStamp();
        // Escaneo escaneo = new Escaneo();
        // escaneo.setStartId();
        // escaneo.setStartTimeStamp();
        // Comenzar modelo
        // modelo()
        // Nuestra referencia a Batería y Escaneo deberia ser global, para que la funcion anterior la use tambien
    }

    public void modelo() {
        // Esta funcion es ciclica
        // bateria.checkValues()
        // Si la funcion anterior regresa true, comenzamos el modelo
        // bateria.updateValues()
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
