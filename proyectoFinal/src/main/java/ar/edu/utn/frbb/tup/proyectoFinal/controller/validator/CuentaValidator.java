package ar.edu.utn.frbb.tup.proyectoFinal.controller.validator;

import ar.edu.utn.frbb.tup.proyectoFinal.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.ClienteDoesntExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.InputErrorException;
import ar.edu.utn.frbb.tup.proyectoFinal.persistencia.ClienteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CuentaValidator {
    @Autowired
    private ClienteDao clienteDao;

    public void validate(CuentaDto cuentaDto) throws ClienteDoesntExistException, InputErrorException {
        //valido que los campos de String que se estan ingresando sean los correctos (tipoCuenta y moneda)
        if (!"PESOS".equals(cuentaDto.getMoneda().name()) && !"DOLARES".equals(cuentaDto.getMoneda().name())) {
            throw new InputErrorException("La MONEDA ingresada no es valida.");
        }
        if (!"CUENTA_CORRIENTE".equals(cuentaDto.getTipoCuenta().name()) && !"CAJA_AHORRO".equals(cuentaDto.getTipoCuenta().name())) {
            throw new InputErrorException("El TIPO DE CUENTA no es valido.");
        }
        if (!cuentaDto.getTitular().matches("\\d{8}")) {
            throw new InputErrorException("El DNI ingresado no es válido.");
        }
    }
}
