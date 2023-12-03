package com.example.prototipo2;

public class Escaneo {

    private int startId;
    private long startTimeStamp;
    private int endId;
    private long endTimeStamp;
    private long duration;
    private long datosConsumo;

    public Escaneo() {
        this.startId = 0;
        this.startTimeStamp = 0;
        this.endId = 0;
        this.endTimeStamp = 0;
        this.duration = 0;
        this.datosConsumo = 0;
    }

    public void insertIntoDB() {
        // Insertamos los datos en la base de datos
    }

    public void getLastScan() {
        // Metodo no void (ContentValues o String) que regresa los datos relativos al ultimo escaneo
    }

    public void getAllScans() {
        // Metodo no void (String quiza) que regrese todos los datos de la base de datos
    }

    public void updateDatosConsumo(long datos) {
        this.datosConsumo += datos;
    }

    public int getStartId() {
        return startId;
    }

    public long getStartTimeStamp() {
        return startTimeStamp;
    }

    public int getEndId() {
        return endId;
    }

    public long getEndTimeStamp() {
        return endTimeStamp;
    }

    public long getDuration() {
        return duration;
    }

    public long getDatosConsumo() {
        return datosConsumo;
    }

    public void setStartId(int startId) {
        this.startId = startId;
    }

    public void setStartTimeStamp(long startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    public void setEndId(int endId) {
        this.endId = endId;
    }

    public void setEndTimeStamp(long endTimeStamp) {
        this.endTimeStamp = endTimeStamp;
    }
}
