package ar.edu.utn.frbb.tup.proyectoFinal.model;

import java.util.Random;
import java.time.LocalDate;

public class Deposito {
    private long numeroTransaccion;
    private long cuenta;
    private double monto;
    private String moneda;
    private LocalDate fecha;

    public Deposito() {
        this.numeroTransaccion = new Random().nextLong(9999) + 1000;
        this.fecha = LocalDate.now();
    }

    public long getNumeroTransaccion() {
        return numeroTransaccion;
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
}
