package ar.edu.utn.frbb.tup.proyectoFinal.controller.dto;

import ar.edu.utn.frbb.tup.proyectoFinal.model.TipoMoneda;

public class TransferenciaDto {
    private String cuentaOrigen;
    private String cuentaDestino;
    private String monto;
    private TipoMoneda moneda;

    public String getCuentaOrigen() {
        return cuentaOrigen;
    }

    public void setCuentaOrigen(String cuentaOrigen) {
        this.cuentaOrigen = cuentaOrigen;
    }

    public String getCuentaDestino() {
        return cuentaDestino;
    }

    public void setCuentaDestino(String cuentaDestino) {
        this.cuentaDestino = cuentaDestino;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    public TipoMoneda getMoneda() {
        return moneda;
    }

    public void setMoneda(TipoMoneda moneda) {
        this.moneda = moneda;
    }

}
