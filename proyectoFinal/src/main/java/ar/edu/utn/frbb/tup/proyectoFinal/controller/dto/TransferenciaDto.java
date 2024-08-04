package ar.edu.utn.frbb.tup.proyectoFinal.controller.dto;

import ar.edu.utn.frbb.tup.proyectoFinal.model.TipoMoneda;

public class TransferenciaDto {
    private long cuentaOrigen;
    private long cuentaDestino;
    private double monto;
    private String moneda;

    public long getCuentaOrigen() {
        return cuentaOrigen;
    }

    public void setCuentaOrigen(long cuentaOrigen) {
        this.cuentaOrigen = cuentaOrigen;
    }

    public long getCuentaDestino() {
        return cuentaDestino;
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

}
