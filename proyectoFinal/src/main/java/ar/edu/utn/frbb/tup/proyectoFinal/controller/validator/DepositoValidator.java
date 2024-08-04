package ar.edu.utn.frbb.tup.proyectoFinal.controller.validator;
import ar.edu.utn.frbb.tup.proyectoFinal.controller.dto.DepositoDto;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.InputErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DepositoValidator {

    @Autowired
    private Validaciones validaciones;

    public void validate(DepositoDto depositoDto) throws InputErrorException {

        validaciones.validarNumeroCuenta(depositoDto.getCuenta());
        validaciones.validarMonto(depositoDto.getMonto());
        validaciones.validarMoneda(depositoDto.getMoneda());
    }
}
