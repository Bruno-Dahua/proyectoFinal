package ar.edu.utn.frbb.tup.proyectoFinal.model;

import ar.edu.utn.frbb.tup.proyectoFinal.controller.ClienteDto;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Cliente extends Persona{
    private TipoPersona tipoPersona;
    private String banco;
    private LocalDate fechaAlta;

    @JsonIgnore  // Evita la serialización recursiva
    private Set<Cuenta> cuentas = new HashSet<>();

    public Cliente() {
        super();
    }
    public Cliente(ClienteDto clienteDto) {
        super(clienteDto.getDni(), clienteDto.getApellido(), clienteDto.getNombre(), clienteDto.getFechaNacimiento());
        this.tipoPersona = TipoPersona.fromString(clienteDto.getTipoPersona());
        fechaAlta = LocalDate.now();
        banco = clienteDto.getBanco();
    }

    public TipoPersona getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(TipoPersona tipoPersona) {
        this.tipoPersona = tipoPersona;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Set<Cuenta> getCuentas() {
        return cuentas;
    }

    public void addCuenta(Cuenta cuenta) {
        this.cuentas.add(cuenta);
        cuenta.setTitular(this);
    }

    public boolean tieneCuenta(TipoCuenta tipoCuenta, TipoMoneda moneda) {
        for (Cuenta cuenta:
                cuentas) {
            if (tipoCuenta.equals(cuenta.getTipoCuenta()) && moneda.equals(cuenta.getMoneda())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "tipoPersona=" + tipoPersona +
                ", banco='" + banco + '\'' +
                ", fechaAlta=" + fechaAlta +
                ", cuentas=" + cuentas +
                '}';
    }
}
