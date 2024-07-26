package ar.edu.utn.frbb.tup.proyectoFinal.service;

import ar.edu.utn.frbb.tup.proyectoFinal.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.proyectoFinal.model.Cliente;
import ar.edu.utn.frbb.tup.proyectoFinal.model.Cuenta;
import ar.edu.utn.frbb.tup.proyectoFinal.model.TipoCuenta;
import ar.edu.utn.frbb.tup.proyectoFinal.model.TipoMoneda;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.ClienteDoesntExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.CuentaAlreadyExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.NotPosibleException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.TipoCuentaAlreadyExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.persistencia.ClienteDao;
import ar.edu.utn.frbb.tup.proyectoFinal.persistencia.CuentaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
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
    public boolean darDeAltaCuenta(CuentaDto cuentaDto) throws TipoCuentaAlreadyExistException, ClienteDoesntExistException, NotPosibleException, CuentaAlreadyExistException {
        // Crear una nueva cuenta y asignar valores desde cuentaDto
        Cuenta cuenta = new Cuenta();
        cuenta.setTipoCuenta(cuentaDto.getTipoCuenta());
        cuenta.setMoneda(cuentaDto.getMoneda());

        long dniTitular = Long.parseLong(cuentaDto.getTitular());
        // Obtener el titular desde el clienteDao
        Cliente titular = clienteService.buscarClientePorDni(dniTitular);
        cuenta.setTitular(titular);

        // Verificar si la cuenta ya existe
        if (cuentaDao.find(cuenta.getTitular().getDni()) != null) {
            return false;
        }

        if (titular.tieneCuenta(cuenta.getTipoCuenta(), cuenta.getMoneda())) {
            throw new CuentaAlreadyExistException("El cliente ya posee una cuenta de ese tipo y moneda");
        }

        if (cuentaDto.getTipoCuenta() == TipoCuenta.CUENTA_CORRIENTE && cuentaDto.getMoneda() == TipoMoneda.DOLARES) {
            throw new NotPosibleException("No es posible crear una CUENTA CORRIENTE en DOLARES");
        }

        // Agregar la cuenta al cliente
        clienteService.agregarCuenta(cuenta, dniTitular);

        // Guardar la cuenta en la base de datos
        cuentaDao.save(cuenta);

        return true;
    }

    public void actualizarTitularCuenta(Cliente clienteActualizado, long dniAntiguo){
        Set<Cuenta> cuentasCliente = cuentaDao.getCuentasByCliente(dniAntiguo);

        for (Cuenta c : cuentasCliente){
            c.setTitular(clienteActualizado);
            cuentaDao.update(c);
            clienteActualizado.addCuenta(c);
        }
    }

    public void actualizarBalanceTransferencia(Cuenta cuentaOrigen, Cuenta cuentaDestino, double monto, double comision){
        cuentaOrigen.setBalance(cuentaOrigen.getBalance() - monto - comision);
        cuentaDestino.setBalance(cuentaDestino.getBalance() + monto);
    }

    public void actualizarBalanceCuenta(Cuenta cuenta, double monto, double comision){
        cuenta.setBalance(cuenta.getBalance() + monto - comision);
    }
}
