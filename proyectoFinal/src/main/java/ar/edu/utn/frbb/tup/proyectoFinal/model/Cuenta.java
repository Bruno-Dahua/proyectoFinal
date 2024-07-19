package ar.edu.utn.frbb.tup.proyectoFinal.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.util.Random;

@JsonIgnoreProperties({"titular"})
public class Cuenta {
    private long numeroCuenta;
    LocalDateTime fechaCreacion;
    int balance;
    TipoCuenta tipoCuenta;
    TipoMoneda moneda;

    @JsonBackReference
    Cliente titular;
    

    public Cuenta() {
        this.numeroCuenta = new Random().nextLong();
        this.fechaCreacion = LocalDateTime.now();
        this.balance = 0;
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

    public int getBalance() {
        return balance;
    }

    public Cuenta setBalance(int balance) {
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
