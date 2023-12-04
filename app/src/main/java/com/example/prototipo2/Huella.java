package com.example.prototipo2;

public class Huella {

    private final float factorEmision = 0.435F; // gCO2e/Wh
    private int escaneoId;
    private float huellaCarbono;
    private int datosConsumo;

    public Huella() {
        this.escaneoId = 0;
        this.huellaCarbono = 0;
    }

    public String getAllRows() {
        return "";
    }

    public void calcularHuellaCarbono() {
        //
    }

    public float getTotalHuellaCarbono() {
        return 0;
    }

    public void setEscaneoId(int escaneoId) {
        this.escaneoId = escaneoId;
    }

    public void setDatosConsumo(int datosConsumo) {
        this.datosConsumo = datosConsumo;
    }
}
