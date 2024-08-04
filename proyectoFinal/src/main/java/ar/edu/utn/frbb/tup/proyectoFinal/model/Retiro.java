package ar.edu.utn.frbb.tup.proyectoFinal.model;

import java.time.LocalDate;
import java.util.Random;

public class Retiro {
    private long numeroTransaccion;
    private long cuenta;
    private double monto;
    private String moneda;
    private LocalDate fecha;

    public Retiro() {
        this.numeroTransaccion = new Random().nextLong(9999) + 1000;
        this.fecha = LocalDate.now();
    }

    public long getNumeroTransaccion() {
        return numeroTransaccion;
    }

    public void setNumeroTransaccion(long numeroTransaccion) {
        this.numeroTransaccion = numeroTransaccion;
    }

    public long getCuenta() {
        return cuenta;
    }

    public void setCuenta(long cuenta) {
        this.cuenta = cuenta;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}

