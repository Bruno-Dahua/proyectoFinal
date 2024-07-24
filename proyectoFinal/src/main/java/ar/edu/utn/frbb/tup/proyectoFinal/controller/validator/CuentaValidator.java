package ar.edu.utn.frbb.tup.proyectoFinal.controller.validator;

import ar.edu.utn.frbb.tup.proyectoFinal.controller.CuentaDto;
import ar.edu.utn.frbb.tup.proyectoFinal.model.Cliente;
import ar.edu.utn.frbb.tup.proyectoFinal.model.Cuenta;
import ar.edu.utn.frbb.tup.proyectoFinal.model.TipoCuenta;
import ar.edu.utn.frbb.tup.proyectoFinal.model.TipoMoneda;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.ClienteDoesntExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.persistencia.ClienteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CuentaValidator {
    @Autowired
    private ClienteDao clienteDao;

    public void validate(CuentaDto cuentaDto) throws ClienteDoesntExistException {
        //valido que los campos de String que se estan ingresando sean los correctos (tipoCuenta y moneda)
        if (!"PESOS".equals(cuentaDto.getMoneda().name()) && !"DOLARES".equals(cuentaDto.getMoneda().name())) {
            throw new IllegalArgumentException("El tipo de moneda no es correcto.");
        }
        if (!"CUENTA_CORRIENTE".equals(cuentaDto.getTipoCuenta().name()) && !"CAJA_AHORRO".equals(cuentaDto.getTipoCuenta().name())) {
            throw new IllegalArgumentException("El tipo de cuenta no es correcto.");
        }
        if (!cuentaDto.getTitular().matches("\\d{8}")) {
            throw new IllegalArgumentException("El DNI ingresado no es válido.");
        }
    }
}
