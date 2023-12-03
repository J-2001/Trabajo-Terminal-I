package com.example.prototipo2;

import android.content.Context;
import android.os.BatteryManager;

public class Bateria {

    private int chargeCounter;
    private int currentNow;
    private int capacity;
    private int status;
    private float voltage;
    private long timeStamp;
    private BatteryManager batteryManager;

    public Bateria(Context context) {
        this.chargeCounter = 0;
        this.currentNow = 0;
        this.capacity = 0;
        this.status = 0;
        this.voltage = 0;
        this.timeStamp = 0;
        this.batteryManager = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
    }

    public boolean checkValues() {
        // Metodo que nos indica si un valor de interes (chargeCounter) ha cambiado
        return batteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER) != this.chargeCounter;
    }

    public void updateValues() {
        // Metodo que actualiza los valores variables (o inicializa los que estan en cero)
        this.chargeCounter = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER);
        this.currentNow = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW);
        this.capacity = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        this.status = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_STATUS);
        this.voltage = batteryManager.getIntProperty(BatteryManager.BATTERY_HEALTH_COLD);

        // Luego llama a insertIntoDB()
        // Puede ser tipo int, que devuelva lo que devolvio la funcion anterior
    }

    public void insertIntoDB() {
        // Insertamos los datos en la base de datos "Bateria"
        // Puede ser tipo int, que devuelva el id de la insercion
        // Puede ser private
    }

    public void getAllRows() {
        // Regresa los datos en la base de datos
    }

    public int getChargeCounter() {
        return chargeCounter;
    }

    public int getCurrentNow() {
        return currentNow;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getStatus() {
        return status;
    }

    public float getVoltage() {
        return voltage;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
