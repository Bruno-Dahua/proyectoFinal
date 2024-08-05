package ar.edu.utn.frbb.tup.proyectoFinal.controller.dto;

import ar.edu.utn.frbb.tup.proyectoFinal.model.TipoCuenta;
import ar.edu.utn.frbb.tup.proyectoFinal.model.TipoMoneda;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CuentaDto {

    private String tipoCuenta;
    private long titular;
    private String moneda;

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public long getTitular() {
        return titular;
    }

    public void setTitular(long titular) {
        this.titular = titular;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }
}
