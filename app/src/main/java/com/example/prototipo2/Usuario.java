package com.example.prototipo2;

public class Usuario {

    private int id;
    private String osVersion;
    private String deviceName;
    private String deviceModel;

    public Usuario() {
        // Buscamos si la base de datos "Usuario" tiene al menos un valor
        // Si lo tiene, el usuario ya esta registrado
        // Si no, lo registramos llamando a registerUser()
    }

    public void registerUser() {
        // Petición de registro al servidor
    }

    public void sendData() {
        // Metodo no void, envía las bases de datos de la aplicación
    }

    public int getId() {
        return id;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getDeviceModel() {
        return deviceModel;
    }
}
