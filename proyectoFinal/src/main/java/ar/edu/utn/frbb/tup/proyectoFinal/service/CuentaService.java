package ar.edu.utn.frbb.tup.proyectoFinal.service;

import ar.edu.utn.frbb.tup.proyectoFinal.controller.CuentaDto;
import ar.edu.utn.frbb.tup.proyectoFinal.model.Cliente;
import ar.edu.utn.frbb.tup.proyectoFinal.model.Cuenta;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.CuentaAlreadyExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.TipoCuentaAlreadyExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.persistencia.ClienteDao;
import ar.edu.utn.frbb.tup.proyectoFinal.persistencia.CuentaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CuentaService {
    CuentaDao cuentaDao = new CuentaDao();
    @Autowired
    private ClienteDao clienteDao;

    @Autowired
    ClienteService clienteService;

    //Generar casos de test para darDeAltaCuenta
    //    1 - cuenta existente
    //    2 - cuenta no soportada
    //    3 - cliente ya tiene cuenta de ese tipo
    //    4 - cuenta creada exitosamente
    public Cuenta darDeAltaCuenta(CuentaDto cuentaDto, long dniTitular) throws CuentaAlreadyExistException, TipoCuentaAlreadyExistException {
        // Crear una nueva cuenta y asignar valores desde cuentaDto
        Cuenta cuenta = new Cuenta();
        cuenta.setTipoCuenta(cuentaDto.getTipoCuenta());
        cuenta.setMoneda(cuentaDto.getMoneda());

        // Obtener el titular desde el clienteDao
        Cliente titular = clienteService.buscarClientePorDni(dniTitular);
        cuenta.setTitular(titular);

        // Verificar si la cuenta ya existe
        if (cuentaDao.find(cuenta.getNumeroCuenta()) != null) {
            throw new CuentaAlreadyExistException("La cuenta " + cuenta.getNumeroCuenta() + " ya existe.");
        }

        // Agregar la cuenta al cliente
        clienteService.agregarCuenta(cuenta, dniTitular);

        // Guardar la cuenta en la base de datos
        cuentaDao.save(cuenta);

        return cuenta;
    }


    public Cuenta find(long id) {
        return cuentaDao.find(id);
    }
}
