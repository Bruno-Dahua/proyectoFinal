package ar.edu.utn.frbb.tup.proyectoFinal.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@JsonIgnoreProperties({"titular"})
public class Cuenta {
    private long numeroCuenta;
    private LocalDateTime fechaCreacion;
    private double balance;
    private TipoCuenta tipoCuenta;
    private TipoMoneda moneda;

    @JsonBackReference
    private Cliente titular;
    
    private Set<Transaccion> historialTransacciones;

    public Cuenta() {
        this.numeroCuenta = new Random().nextLong();
        this.fechaCreacion = LocalDateTime.now();
        this.balance = 1000000;
        this.historialTransacciones = new HashSet<>();
    }

    public void addToHistorial(Transaccion transaccion) {
        this.historialTransacciones.add(transaccion);
    }

    public Set<Transaccion> getHistorialTransacciones() {
        return historialTransacciones;
    }

    public void setHistorialTransacciones(Set<Transaccion> historialTransacciones) {
        this.historialTransacciones = historialTransacciones;
    }

    public Cliente getTitular() {
        return titular;
    }

    public void setTitular(Cliente titular) {
        this.titular = titular;
    }


    public TipoCuenta getTipoCuenta() {
        return tipoCuenta;
    }

    public Cuenta setTipoCuenta(TipoCuenta tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
        return this;
    }

    public TipoMoneda getMoneda() {
        return moneda;
    }

    public Cuenta setMoneda(TipoMoneda moneda) {
        this.moneda = moneda;
        return this;
    }


    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public Cuenta setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
        return this;
    }

    public double getBalance() {
        return balance;
    }

    public Cuenta setBalance(double balance) {
        this.balance = balance;
        return this;
    }

    public void setNumeroCuenta(long numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    /*public void forzaDebitoDeCuenta(int i) {
        this.balance = this.balance - i;
    }*/

    public long getNumeroCuenta() {
        return numeroCuenta;
    }

}
