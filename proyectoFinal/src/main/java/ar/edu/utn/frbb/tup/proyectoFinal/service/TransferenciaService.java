package ar.edu.utn.frbb.tup.proyectoFinal.service;

import ar.edu.utn.frbb.tup.proyectoFinal.controller.dto.RespuestaTransferenciaDto;
import ar.edu.utn.frbb.tup.proyectoFinal.controller.dto.TransferenciaDto;
import ar.edu.utn.frbb.tup.proyectoFinal.model.*;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.ClienteDoesntExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.CuentaDoesntExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.NotPosibleException;
import ar.edu.utn.frbb.tup.proyectoFinal.persistencia.ClienteDao;
import ar.edu.utn.frbb.tup.proyectoFinal.persistencia.CuentaDao;
import ar.edu.utn.frbb.tup.proyectoFinal.persistencia.TransferenciaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class TransferenciaService {
    @Autowired
    private ClienteDao clienteDao;

    @Autowired
    private TransferenciaDao transferenciaDao;

    @Autowired
    private CuentaDao cuentaDao;

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private BanelcoService banelcoService;

    public RespuestaTransferenciaDto realizarTransferencia(TransferenciaDto transferenciaDto) throws ClienteDoesntExistException, NotPosibleException, CuentaDoesntExistException {
        // Buscar clientes y sus cuentas
        Cliente clienteOrigen = clienteDao.find(Long.parseLong(transferenciaDto.getCuentaOrigen()));
        Cliente clienteDestino = clienteDao.find(Long.parseLong(transferenciaDto.getCuentaDestino()));

        if (clienteOrigen == null) {
            throw new ClienteDoesntExistException("El cliente con DNI " + transferenciaDto.getCuentaOrigen() + " no existe.");
        } else if (clienteDestino == null) {
            throw new ClienteDoesntExistException("El cliente con DNI " + transferenciaDto.getCuentaDestino() + " no existe.");
        }

        Set<Cuenta> cuentasOrigen = clienteOrigen.getCuentas();
        Set<Cuenta> cuentasDestino = clienteDestino.getCuentas();

        Cuenta cuentaOrigen = cuentaDao.obtenerCuentaPrioritaria(cuentasOrigen, Long.parseLong(transferenciaDto.getCuentaOrigen()));
        if (cuentaOrigen == null) {
            throw new CuentaDoesntExistException("La cuenta de origen no existe");
        }

        Cuenta cuentaDestino = cuentaDao.obtenerCuentaPrioritaria(cuentasDestino, Long.parseLong(transferenciaDto.getCuentaDestino()));
        if (cuentaDestino == null) {
            throw new CuentaDoesntExistException("La cuenta de destino no existe");
        }

        if (!cuentaOrigen.getMoneda().equals(cuentaDestino.getMoneda())) {
            throw new NotPosibleException("Las monedas de las cuentas no coinciden");
        }

        RespuestaTransferenciaDto respuestaTransferenciaDto = new RespuestaTransferenciaDto();

        if (clienteOrigen.getBanco().equals(clienteDestino.getBanco())) {
            return generarRespuestaYActualizar(transferenciaDto, cuentaOrigen, cuentaDestino, respuestaTransferenciaDto);
        } else {
            if (banelcoService.servicioDeBanelco(transferenciaDto)) {
                return generarRespuestaYActualizar(transferenciaDto, cuentaOrigen, cuentaDestino, respuestaTransferenciaDto);
            } else {
                respuestaTransferenciaDto.setEstado("FALLIDA");
                respuestaTransferenciaDto.setMensaje("No es posible realizar la transferencia, los bancos son diferentes.");
                return respuestaTransferenciaDto;
            }
        }
    }


    private RespuestaTransferenciaDto generarRespuestaYActualizar(TransferenciaDto transferenciaDto, Cuenta cuentaOrigen, Cuenta cuentaDestino, RespuestaTransferenciaDto respuestaTransferenciaDto) {
        Transferencia transferencia = toTransferencia(transferenciaDto);

        if (cuentaOrigen.getBalance() >= Double.parseDouble(transferenciaDto.getMonto())) {
            double monto = Double.parseDouble(transferenciaDto.getMonto());
            double comision = calcularComision(transferenciaDto);

            cuentaService.actualizarBalance(cuentaOrigen, cuentaDestino, monto, comision);

            // Creo y agrego las transacciones al historial
            transferenciaDao.save(cuentaOrigen, transferencia, TipoMovimiento.TRANSFERENCIA_SALIDA, "Transferencia a cuenta " + cuentaDestino.getNumeroCuenta());
            transferenciaDao.save(cuentaDestino, transferencia, TipoMovimiento.TRANSFERENCIA_ENTRADA, "Transferencia desde cuenta " + cuentaOrigen.getNumeroCuenta());

            // Actualizo las cuentas en la base de datos
            cuentaDao.update(cuentaOrigen);
            cuentaDao.update(cuentaDestino);

            // Preparo una respuestaDto
            respuestaTransferenciaDto.setEstado("EXITOSA");
            respuestaTransferenciaDto.setMensaje("Se realizó la transferencia exitosamente. Número de transferencia: " + transferencia.getNumeroTransaccion() + ". Realizado el " + transferencia.getFecha() + ". Saldo actual: " + (cuentaOrigen.getBalance() > 0 ? (transferenciaDto.getMoneda() == TipoMoneda.PESOS ? "ARG $ " : "USD $ ") + cuentaOrigen.getBalance() : "Usted tiene una deuda con el Banco."));
            return respuestaTransferenciaDto;
        } else {
            respuestaTransferenciaDto.setEstado("FALLIDA");
            respuestaTransferenciaDto.setMensaje("Saldo insuficiente para realizar la transferencia.");
            return respuestaTransferenciaDto;
        }
    }

    private double calcularComision(TransferenciaDto transferenciaDto) {
        if (transferenciaDto.getMoneda() == TipoMoneda.PESOS && Double.parseDouble(transferenciaDto.getMonto()) >= 1000000) {
            return 0.02 * Double.parseDouble(transferenciaDto.getMonto());
        } else if (transferenciaDto.getMoneda() == TipoMoneda.DOLARES && Double.parseDouble(transferenciaDto.getMonto()) >= 5000) {
            return 0.005 * Double.parseDouble(transferenciaDto.getMonto());
        }
        return 0;
    }


    private Transferencia toTransferencia(TransferenciaDto transferenciaDto) {
        Transferencia transferencia = new Transferencia();
        transferencia.setMonto(Double.parseDouble(transferenciaDto.getMonto()));
        transferencia.setCuentaOrigen(transferenciaDto.getCuentaOrigen());
        transferencia.setCuentaDestino(transferenciaDto.getCuentaDestino());
        transferencia.setMoneda(transferenciaDto.getMoneda().toString());
        return transferencia;
    }


}
