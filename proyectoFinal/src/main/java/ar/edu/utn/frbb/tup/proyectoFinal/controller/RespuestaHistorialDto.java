package ar.edu.utn.frbb.tup.proyectoFinal.controller;

import ar.edu.utn.frbb.tup.proyectoFinal.model.Transaccion;

import java.util.HashSet;
import java.util.Set;

public class RespuestaHistorialDto {
    private long numeroCuenta;
    private Set<Transaccion> historialTransacciones;

    // Constructor sin parámetros
    public RespuestaHistorialDto() {
        this.historialTransacciones = new HashSet<>();
    }

    // Constructor con parámetros
    public RespuestaHistorialDto(long numeroCuenta, Set<Transaccion> historialTransacciones) {
        this.numeroCuenta = numeroCuenta;
        this.historialTransacciones = historialTransacciones;
    }

    // Getters y Setters
    public long getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(long numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public Set<Transaccion> getHistorialTransacciones() {
        return historialTransacciones;
    }

    public void setHistorialTransacciones(Set<Transaccion> historialTransacciones) {
        this.historialTransacciones = historialTransacciones;
    }
}
