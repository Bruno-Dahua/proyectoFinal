package ar.edu.utn.frbb.tup.proyectoFinal.controller.validator;

import ar.edu.utn.frbb.tup.proyectoFinal.controller.dto.TransferenciaDto;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.InputErrorException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.NotPosibleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

@Component
public class TransferenciaValidator {

   @Autowired
   private Validaciones validaciones;

    public void validate(TransferenciaDto transferenciaDto) throws NotPosibleException, InputErrorException {
        validaciones.validarNumeroCuenta(transferenciaDto.getCuentaOrigen());
        validaciones.validarNumeroCuenta(transferenciaDto.getCuentaDestino());
        validaciones.validarMonto(transferenciaDto.getMonto());
        validaciones.validarMoneda(transferenciaDto.getMoneda());

    }
}
