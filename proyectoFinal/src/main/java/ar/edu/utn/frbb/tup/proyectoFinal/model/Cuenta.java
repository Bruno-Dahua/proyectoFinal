package ar.edu.utn.frbb.tup.proyectoFinal.model;

import com.fasterxml.jackson.annotation.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Cuenta {
    private long numeroCuenta;
    private LocalDateTime fechaCreacion;
    private double balance;
    private TipoCuenta tipoCuenta;
    private TipoMoneda moneda;

    @JsonManagedReference
    private Cliente titular;

    @JsonIgnore
    private Set<Transaccion> historialTransacciones;

    public Cuenta() {
        Random random = new Random();
        this.numeroCuenta = 1 + (Math.abs(random.nextLong()) % 99999999);
        this.fechaCreacion = LocalDateTime.now();
        this.balance = 0;
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

    public long getNumeroCuenta() {
        return numeroCuenta;
    }

}
