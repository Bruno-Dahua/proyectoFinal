package ar.edu.utn.frbb.tup.proyectoFinal.model;

import java.time.LocalDate;
import java.util.Random;

public class Transferencia {
    private long numeroTransaccion;
    private long cuentaOrigen;
    private long cuentaDestino;
    private double monto;
    private String moneda;
    private LocalDate fecha;

    public Transferencia() {
        this.numeroTransaccion = new Random().nextLong(9999) + 1000;
        this.fecha = LocalDate.now();
    }

    public long getNumeroTransaccion() {
        return numeroTransaccion;
    }

    public void setCuentaOrigen(long cuentaOrigen) {
        this.cuentaOrigen = cuentaOrigen;
    }

    public void setCuentaDestino(long cuentaDestino) {
        this.cuentaDestino = cuentaDestino;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public LocalDate getFecha() {
        return fecha;
    }
}

