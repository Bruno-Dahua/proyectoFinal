package ar.edu.utn.frbb.tup.proyectoFinal.controller.dto;

import ar.edu.utn.frbb.tup.proyectoFinal.model.TipoCuenta;
import ar.edu.utn.frbb.tup.proyectoFinal.model.TipoMoneda;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CuentaDto {

    private TipoCuenta tipoCuenta;
    private String titular;
    private TipoMoneda moneda;

    public TipoCuenta getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(TipoCuenta tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public TipoMoneda getMoneda() {
        return moneda;
    }

    public void setMoneda(TipoMoneda moneda) {
        this.moneda = moneda;
    }
}
