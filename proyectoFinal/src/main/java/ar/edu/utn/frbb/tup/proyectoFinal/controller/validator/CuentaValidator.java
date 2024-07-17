package ar.edu.utn.frbb.tup.proyectoFinal.controller.validator;

import ar.edu.utn.frbb.tup.proyectoFinal.controller.CuentaDto;
import ar.edu.utn.frbb.tup.proyectoFinal.model.Cliente;
import ar.edu.utn.frbb.tup.proyectoFinal.model.Cuenta;
import ar.edu.utn.frbb.tup.proyectoFinal.model.TipoCuenta;
import ar.edu.utn.frbb.tup.proyectoFinal.model.TipoMoneda;
import ar.edu.utn.frbb.tup.proyectoFinal.persistencia.ClienteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CuentaValidator {
    @Autowired
    private ClienteDao clienteDao;

    public void validate(CuentaDto cuentaDto, long dni) {
        //valido que los campos de String que se estan ingresando sean los correctos (tipoCuenta y moneda)
        if (!"PESOS".equals(cuentaDto.getMoneda().name()) && !"DOLARES".equals(cuentaDto.getMoneda().name())) {
            throw new IllegalArgumentException("El tipo de moneda no es correcto");
        }
        if (!"CUENTA_CORRIENTE".equals(cuentaDto.getTipoCuenta().name()) && !"CAJA_AHORRO".equals(cuentaDto.getTipoCuenta().name())) {
            throw new IllegalArgumentException("El tipo de cuenta no es correcto");
        }

        if (cuentaDto.getTipoCuenta() == TipoCuenta.CUENTA_CORRIENTE && cuentaDto.getMoneda() == TipoMoneda.DOLARES) {
            throw new IllegalArgumentException("No es posible crear una CUENTA CORRIENTE en DOLARES");
        }
        //valido que el cliente exista
        Cliente cliente = clienteDao.find(dni, true);
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente no existe");
        }

        //valido que el cliente no tenga esa misma cuenta
        for (Cuenta cuenta : cliente.getCuentas()) {
            if (cuenta.getTipoCuenta().name().equals(cuentaDto.getTipoCuenta().name()) &&
                    cuenta.getMoneda().name().equals(cuentaDto.getMoneda().name())) {
                throw new IllegalArgumentException("El cliente ya tiene una cuenta con el mismo tipo y moneda.");
            }
        }


        try {
            LocalDate.parse(cuentaDto.getTitular().getFechaNacimiento());
            if (!(cuentaDto.getTitular().getFechaNacimiento()).equals(cliente.getFechaNacimiento())){
                throw new IllegalArgumentException("La FECHA DE NACIMIENTO es incorrecta.");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("La FECHA DE NACIMIENTO ingresada no es valida.");
        }

        //valido campos String para que no se ingresen caracteres que no sean letras
        if (cuentaDto.getTitular().getNombre() == null || !cuentaDto.getTitular().getNombre().matches("[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\\s]+")) {
            throw new IllegalArgumentException("El NOMBRE ingresado no es valido.");
        }else if (!(cuentaDto.getTitular().getNombre()).equals(cliente.getNombre())){
            throw new IllegalArgumentException("El NOMBRE es incorrecto.");
        }

        if (cuentaDto.getTitular().getApellido() == null || !cuentaDto.getTitular().getApellido().matches("[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\\s]+")){
            throw new IllegalArgumentException("El APELLIDO ingresado no es valido");
        }else if (!(cuentaDto.getTitular().getApellido()).equals(cliente.getApellido())){
            throw new IllegalArgumentException("El APELLIDO es incorrecto.");
        }

        if (cuentaDto.getTitular().getBanco() == null || !cuentaDto.getTitular().getBanco().matches("[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\\s]+")){
            throw new IllegalArgumentException("El BANCO ingresado no es valido.");
        }else if (!(cuentaDto.getTitular().getBanco()).equals(cliente.getBanco())){
            throw new IllegalArgumentException("El BANCO es incorrecto.");
        }

        if (!cuentaDto.getTitular().getDni().matches("\\d{8}")) {
            throw new IllegalArgumentException("El DNI ingresado no es válido.");
        }
    }
}
