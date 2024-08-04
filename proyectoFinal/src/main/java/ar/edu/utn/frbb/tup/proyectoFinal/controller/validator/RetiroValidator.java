package ar.edu.utn.frbb.tup.proyectoFinal.controller.validator;

import ar.edu.utn.frbb.tup.proyectoFinal.controller.dto.DepositoDto;
import ar.edu.utn.frbb.tup.proyectoFinal.controller.dto.RetiroDto;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.InputErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RetiroValidator {

    @Autowired
    private Validaciones validaciones;

    public void validate(RetiroDto retiroDto) throws InputErrorException {

        validaciones.validarNumeroCuenta(retiroDto.getCuenta());
        validaciones.validarMonto(retiroDto.getMonto());
        validaciones.validarMoneda(retiroDto.getMoneda());
    }
}
