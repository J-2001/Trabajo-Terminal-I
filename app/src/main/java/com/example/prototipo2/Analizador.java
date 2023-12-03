package com.example.prototipo2;

public class Analizador {
    // Esta clase seria un Service, por lo cual, el constructor sería el onCreate() y el iniciarAnalizador() sería el onStartCommand()

    public Analizador() {

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
        // iniciarModelo()
        // Nuestra referencia a Batería y Escaneo deberia ser global, para que la funcion anterior la use tambien
    }

    public void iniciarModelo() {
        // bateria.checkValues()
    }

}
