package ar.edu.utn.frbb.tup.proyectoFinal.service;

import ar.edu.utn.frbb.tup.proyectoFinal.controller.dto.DepositoDto;
import ar.edu.utn.frbb.tup.proyectoFinal.controller.dto.RespuestaTransaccionDto;
import ar.edu.utn.frbb.tup.proyectoFinal.model.*;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.ClienteDoesntExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.CuentaDoesntExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.NotPosibleException;
import ar.edu.utn.frbb.tup.proyectoFinal.persistencia.ClienteDao;
import ar.edu.utn.frbb.tup.proyectoFinal.persistencia.CuentaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class DepositoService {
    @Autowired
    private ClienteDao clienteDao;

    @Autowired
    private CuentaDao cuentaDao;

    @Autowired
    private CuentaService cuentaService;

    public RespuestaTransaccionDto realizarDeposito(DepositoDto depositoDto) throws ClienteDoesntExistException, CuentaDoesntExistException, NotPosibleException {
        Cliente cliente = clienteDao.find(Long.parseLong(depositoDto.getCuenta()));

        if (cliente == null) {
            throw new ClienteDoesntExistException("El cliente con DNI: " + depositoDto.getCuenta() + " no existe.");
        }

        Set<Cuenta> cuentas = cliente.getCuentas();

        Cuenta cuenta = cuentaDao.obtenerCuentaPrioritaria(cuentas, Long.parseLong(depositoDto.getCuenta()));

        if (cuenta == null) {
            throw new CuentaDoesntExistException("La cuenta con DNI: " + depositoDto.getCuenta() + " no existe.");
        }
        if (!cuenta.getMoneda().equals(depositoDto.getMoneda())) {
            throw new NotPosibleException("Las monedas de las cuentas no coinciden");
        }

        RespuestaTransaccionDto respuestaTransaccionDto = new RespuestaTransaccionDto();

        cuentaDao.update(cuenta);

        return realizarDepositoYActualizarBalance(cuenta, depositoDto, respuestaTransaccionDto);
    }

    private RespuestaTransaccionDto realizarDepositoYActualizarBalance(Cuenta cuenta, DepositoDto depositoDto, RespuestaTransaccionDto respuestaDepositoDto) throws NotPosibleException {
        Deposito deposito = toDeposito(depositoDto);

        double monto = Double.parseDouble(depositoDto.getMonto());
        double comision = calcularComision(depositoDto);

        cuentaService.actualizarBalanceCuenta(cuenta, monto, comision);

        save(cuenta, deposito, "Deposito a la cuenta " + cuenta.getNumeroCuenta() + ".");

        respuestaDepositoDto.setEstado("EXITOSA");
        respuestaDepositoDto.setMensaje("Se realizó el deposito exitosamente. Número de transaccion: " + deposito.getNumeroTransaccion() + ". Realizado el " + deposito.getFecha() + ". Saldo actual: " + (cuenta.getBalance() > 0 ? (depositoDto.getMoneda() == TipoMoneda.PESOS ? "ARG $ " : "USD $ ") + cuenta.getBalance() : "Usted tiene una deuda con el Banco."));

        return respuestaDepositoDto;
    }

    private double calcularComision(DepositoDto depositoDto) {
        if (depositoDto.getMoneda() == TipoMoneda.PESOS && Double.parseDouble(depositoDto.getMonto()) >= 1000000) {
            return 0.02 * Double.parseDouble(depositoDto.getMonto());
        } else if (depositoDto.getMoneda() == TipoMoneda.DOLARES && Double.parseDouble(depositoDto.getMonto()) >= 5000) {
            return 0.005 * Double.parseDouble(depositoDto.getMonto());
        }
        return 0;
    }

    private void save(Cuenta cuenta, Deposito deposito, String descripcion) {
        Transaccion transaccion = new Transaccion();
        transaccion.setNumeroMovimiento(deposito.getNumeroTransaccion());
        transaccion.setFecha(deposito.getFecha());
        transaccion.setMonto(+deposito.getMonto());
        transaccion.setTipo(TipoMovimiento.DEPOSITO);
        transaccion.setDescripcion(descripcion);
        cuenta.addToHistorial(transaccion);
    }

    private Deposito toDeposito(DepositoDto depositoDto) {
        Deposito deposito = new Deposito();
        deposito.setCuenta(depositoDto.getCuenta());
        deposito.setMonto(Double.parseDouble(depositoDto.getMonto()));
        deposito.setMoneda(depositoDto.getMoneda().toString());
        return deposito;
    }
}
