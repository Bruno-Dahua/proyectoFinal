package ar.edu.utn.frbb.tup.proyectoFinal.controller;

import ar.edu.utn.frbb.tup.proyectoFinal.model.Cliente;
import ar.edu.utn.frbb.tup.proyectoFinal.model.Cuenta;
import ar.edu.utn.frbb.tup.proyectoFinal.model.TipoCuenta;
import ar.edu.utn.frbb.tup.proyectoFinal.model.TipoMoneda;

import java.time.LocalDateTime;
import java.util.Random;

public class CuentaDto {
//    private long numeroCuenta;
//    LocalDateTime fechaCreacion;
//    int balance;
    TipoCuenta tipoCuenta;
    Cliente titular;
    TipoMoneda moneda;

//    public long getNumeroCuenta() {
//        return numeroCuenta;
//    }
//
//    public void setNumeroCuenta(long numeroCuenta) {
//        this.numeroCuenta = numeroCuenta;
//    }
//
//    public LocalDateTime getFechaCreacion() {
//        return fechaCreacion;
//    }
//
//    public void setFechaCreacion(LocalDateTime fechaCreacion) {
//        this.fechaCreacion = fechaCreacion;
//    }
//
//    public int getBalance() {
//        return balance;
//    }
//
//    public void setBalance(int balance) {
//        this.balance = balance;
//    }

    public TipoCuenta getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(TipoCuenta tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

//    public Cliente getTitular() {
//        return titular;
//    }
//
//    public void setTitular(Cliente titular) {
//        this.titular = titular;
//    }

    public TipoMoneda getMoneda() {
        return moneda;
    }

    public void setMoneda(TipoMoneda moneda) {
        this.moneda = moneda;
    }
}
