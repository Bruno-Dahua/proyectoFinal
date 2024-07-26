package ar.edu.utn.frbb.tup.proyectoFinal.controller.validator;
import ar.edu.utn.frbb.tup.proyectoFinal.controller.dto.DepositoDto;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.InputErrorException;
import org.springframework.stereotype.Component;

@Component
public class DepositoValidator {
    public void validate(DepositoDto depositoDto) throws InputErrorException {
        if ((!depositoDto.getCuenta().matches("\\d{8}"))) {
            throw new InputErrorException("El DNI ingresado no es válido.");
        }
        double monto;
        try {
            monto = Double.parseDouble(depositoDto.getMonto());
        } catch (Exception e) {
            throw new InputErrorException("El MONTO no es valido.");
        }

        if (Double.parseDouble(depositoDto.getMonto()) <= 0) {
            throw new InputErrorException("El MONTO no es valido.");
        }
        if (!"PESOS".equals(depositoDto.getMoneda().name()) && !"DOLARES".equals(depositoDto.getMoneda().name())) {
            throw new InputErrorException("El tipo de moneda no es correcto");
        }
    }
}
