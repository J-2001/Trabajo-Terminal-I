package com.example.prototipo2;

public class Bateria {

    private long chargeCounter;
    private long currentNow;
    private long batteryCapacity;
    private long batteryStatus;
    private float batteryVoltage;
    private long currentAverage;
    private long energyCounter;
    private long timeStamp;

    public Bateria() {
        this.chargeCounter = 0;
        this.currentNow = 0;
        this.batteryCapacity = 0;
        this.batteryStatus = 0;
        this.batteryVoltage = 0;
        this.currentAverage = 0;
        this.energyCounter = 0;
        this.timeStamp = 0;
    }

    public void checkValues() {
        // Metodo no void (boolean) que nos indica si un valor de interes (chargeCounter) ha cambiado
    }

    public void updateValues() {
        // Metodo que actualiza los valores variables (o inicializa los que estan en cero)
        // Luego llama a insertIntoDB()
        // Puede ser tipo int, que devuelva lo que devolvio la funcion anterior
    }

    public void insertIntoDB() {
        // Insertamos los datos en la base de datos "Bateria"
        // Puede ser tipo int, que devuelva el id de la insercion
        // Puede ser private
    }

    public long getChargeCounter() {
        return chargeCounter;
    }

    public long getCurrentNow() {
        return currentNow;
    }

    public long getBatteryCapacity() {
        return batteryCapacity;
    }

    public long getBatteryStatus() {
        return batteryStatus;
    }

    public float getBatteryVoltage() {
        return batteryVoltage;
    }

    public long getCurrentAverage() {
        return currentAverage;
    }

    public long getEnergyCounter() {
        return energyCounter;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
