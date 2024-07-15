package ar.edu.utn.frbb.tup.proyectoFinal.persistencia.entity;

import ar.edu.utn.frbb.tup.proyectoFinal.controller.CuentaDto;
import ar.edu.utn.frbb.tup.proyectoFinal.model.Cuenta;
import ar.edu.utn.frbb.tup.proyectoFinal.model.TipoCuenta;
import ar.edu.utn.frbb.tup.proyectoFinal.model.TipoMoneda;

import java.time.LocalDateTime;

public class CuentaEntity extends BaseEntity {
    String nombre;
    LocalDateTime fechaCreacion;
    int balance;
    String tipoCuenta;
    Long titular;
    long numeroCuenta;
    String moneda;

    public CuentaEntity(Cuenta cuenta) {
        super(cuenta.getNumeroCuenta());
        this.balance = cuenta.getBalance();
        this.tipoCuenta = cuenta.getTipoCuenta().toString();
        this.titular = cuenta.getTitular().getDni();
        this.fechaCreacion = cuenta.getFechaCreacion();
        this.moneda = cuenta.getMoneda().toString();
        this.numeroCuenta = cuenta.getNumeroCuenta();
    }

    public Cuenta toCuenta() {
        Cuenta cuenta = new Cuenta();
        cuenta.setBalance(this.balance);
        cuenta.setNumeroCuenta(this.numeroCuenta);
        cuenta.setTipoCuenta(TipoCuenta.valueOf(this.tipoCuenta));
        cuenta.setFechaCreacion(this.fechaCreacion);
        cuenta.setMoneda(TipoMoneda.valueOf(this.moneda));
        return cuenta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public Long getTitular() {
        return titular;
    }

    public void setTitular(Long titular) {
        this.titular = titular;
    }

    public long getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(long numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }
}
