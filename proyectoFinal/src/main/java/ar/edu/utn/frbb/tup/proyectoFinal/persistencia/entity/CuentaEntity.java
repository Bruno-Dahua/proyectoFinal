package ar.edu.utn.frbb.tup.proyectoFinal.persistencia.entity;

import ar.edu.utn.frbb.tup.proyectoFinal.model.*;

import java.time.LocalDateTime;
import java.util.Set;

public class CuentaEntity extends BaseEntity {
    private LocalDateTime fechaCreacion;
    private double balance;
    private String tipoCuenta;
    private Cliente titular;
    private long numeroCuenta;
    private String moneda;
    private Set<Transaccion> historialTransacciones;

    public CuentaEntity(Cuenta cuenta) {
        super(cuenta.getNumeroCuenta());
        this.balance = cuenta.getBalance();
        this.tipoCuenta = cuenta.getTipoCuenta().toString();
        this.titular = cuenta.getTitular();
        this.fechaCreacion = cuenta.getFechaCreacion();
        this.moneda = cuenta.getMoneda().toString();
        this.numeroCuenta = cuenta.getNumeroCuenta();
        this.historialTransacciones = cuenta.getHistorialTransacciones();
    }

    public Cuenta toCuenta() {
        Cuenta cuenta = new Cuenta();
        cuenta.setBalance(this.balance);
        cuenta.setNumeroCuenta(this.numeroCuenta);
        cuenta.setTipoCuenta(TipoCuenta.valueOf(this.tipoCuenta));
        cuenta.setFechaCreacion(this.fechaCreacion);
        cuenta.setMoneda(TipoMoneda.valueOf(this.moneda));
        cuenta.setHistorialTransacciones(this.historialTransacciones);
        return cuenta;
    }

    public Cliente getTitular() {
        return titular;
    }

    public long getNumeroCuenta(){
        return numeroCuenta;
    }
}
