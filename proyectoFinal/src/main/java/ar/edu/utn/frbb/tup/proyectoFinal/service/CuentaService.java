package ar.edu.utn.frbb.tup.proyectoFinal.service;

import ar.edu.utn.frbb.tup.proyectoFinal.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.proyectoFinal.model.*;
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
    private ClienteService clienteService;

    @Autowired
    private Transaccion transaccion;

    public Cuenta darDeAltaCuenta(CuentaDto cuentaDto) throws TipoCuentaAlreadyExistException, ClienteDoesntExistException, NotPosibleException, CuentaAlreadyExistException {
        // Crear una nueva cuenta y asignar valores desde cuentaDto
        Cuenta cuenta = new Cuenta();
        cuenta.setTipoCuenta(cuentaDto.getTipoCuenta());
        cuenta.setMoneda(cuentaDto.getMoneda());

        String dniTitular = cuentaDto.getTitular();

        Cliente titular = clienteService.buscarClientePorDni(dniTitular);
        cuenta.setTitular(titular);

        // Verificar si la cuenta ya existe
        if (cuentaDao.find(cuenta.getTitular().getDni()) != null) {
            throw new NotPosibleException("El cliente ya posee una cuenta.");
        }

        if (titular.tieneCuenta(cuenta.getTipoCuenta(), cuenta.getMoneda())) {
            throw new CuentaAlreadyExistException("El cliente ya posee una cuenta de ese tipo y moneda");
        }

        if (cuentaDto.getTipoCuenta() == TipoCuenta.CUENTA_CORRIENTE && cuentaDto.getMoneda() == TipoMoneda.DOLARES) {
            throw new NotPosibleException("No es posible crear una CUENTA CORRIENTE en DOLARES");
        }

        // Agregar la cuenta al cliente
        clienteService.agregarCuenta(cuenta, Long.parseLong(dniTitular));

        // Guardar la cuenta en la base de datos
        cuentaDao.save(cuenta);

        return cuenta;
    }

    public Cuenta buscarCuentaPorNumeroCuenta(long numeroCuenta) throws NotPosibleException {
        if (cuentaDao.findByNumeroCuenta(numeroCuenta) != null){
            return cuentaDao.findByNumeroCuenta(numeroCuenta);
        } else{
            throw new NotPosibleException("No existe una cuenta con ese número");
        }
    }

    public void actualizarTitularCuenta(Cliente clienteActualizado, String dniAntiguo){
        Set<Cuenta> cuentasCliente = cuentaDao.getCuentasByCliente(Long.parseLong(dniAntiguo));

        for (Cuenta c : cuentasCliente){
            c.setTitular(clienteActualizado);
            cuentaDao.update(c);
            clienteActualizado.addCuenta(c);
        }
    }

    public void actualizarBalance(Cuenta cuenta, double monto, double comision, TipoMovimiento movimiento){
        cuenta.setBalance(transaccion.calcularNuevoBalance(cuenta.getBalance(), monto, comision, movimiento));

        cuentaDao.update(cuenta);

        Cliente clienteOrigen = cuenta.getTitular();

        actualizarCuentaEnCliente(clienteOrigen, cuenta);
    }

    private void actualizarCuentaEnCliente(Cliente cliente, Cuenta cuentaActualizada) {
        if (cliente != null) {
            Set<Cuenta> cuentas = cliente.getCuentas();
            Cuenta cuentaExistente = buscarCuentaPorNumeroCuenta(cuentas, cuentaActualizada.getNumeroCuenta());

            if (cuentaExistente != null) {
                cuentas.remove(cuentaExistente);
                cuentas.add(cuentaActualizada);
                clienteDao.update(cliente);
            }
        }
    }

    private Cuenta buscarCuentaPorNumeroCuenta(Set<Cuenta> cuentas, long numeroCuenta) {
        for (Cuenta cuenta : cuentas) {
            if (cuenta.getNumeroCuenta() == numeroCuenta) {
                return cuenta;
            }
        }
        return null;
    }
}
