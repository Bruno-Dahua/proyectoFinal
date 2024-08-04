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
    private Validaciones validaciones;

    public void validate(CuentaDto cuentaDto) throws ClienteDoesntExistException, InputErrorException {
        validaciones.validarTipoCuenta(cuentaDto.getTipoCuenta());
        validaciones.validarMoneda(cuentaDto.getMoneda());
        validaciones.validarDni(cuentaDto.getTitular());
    }
}
