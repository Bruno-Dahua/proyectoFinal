package ar.edu.utn.frbb.tup.proyectoFinal.service;

import ar.edu.utn.frbb.tup.proyectoFinal.controller.TransferenciaDto;
import ar.edu.utn.frbb.tup.proyectoFinal.model.Cliente;
import ar.edu.utn.frbb.tup.proyectoFinal.model.Cuenta;
import ar.edu.utn.frbb.tup.proyectoFinal.model.Transferencia;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.ClienteDoesntExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.NotPosibleException;
import ar.edu.utn.frbb.tup.proyectoFinal.persistencia.ClienteDao;
import ar.edu.utn.frbb.tup.proyectoFinal.persistencia.TransferenciaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class TransferenciaService {
    @Autowired
    private ClienteDao clienteDao;

    @Autowired
    private TransferenciaDao transferenciaDao;


    public void realizarTransferencia(TransferenciaDto transferenciaDto) throws ClienteDoesntExistException, NotPosibleException {
        Cliente clienteOrigen = clienteDao.find(Long.parseLong(transferenciaDto.getCuentaDestino()), false);

        Cliente clienteDestino = clienteDao.find(Long.parseLong(transferenciaDto.getCuentaOrigen()), false);
        Set<Cuenta> cuentasDestino = clienteDestino.getCuentas();


        if (clienteOrigen == null) {
            throw new ClienteDoesntExistException("El cliente con DNI " + transferenciaDto.getCuentaOrigen() + " no existe.");
        } else if (clienteDestino == null) {
            throw new ClienteDoesntExistException("El cliente con DNI " + transferenciaDto.getCuentaDestino() + "no existe.");
        }
        else if (cuentasDestino == null) {
            throw new NotPosibleException("El cliente con DNI " + transferenciaDto.getCuentaDestino() + " no tiene cuentas.");
        }
        Transferencia transferencia = toTransferencia(transferenciaDto);
        if (transferenciaDto.getMoneda().equals("PESOS")){
            for (Cuenta c : cuentasDestino){
                if (c.getTipoCuenta().equals("CUENTA_CORRIENTE")) {
                    transferenciaDao.realizar(transferencia);
                }
            }
        }

        if (clienteOrigen.get() < transferenciaDto.getMonto()) {
            clienteDestino.setSaldo(clienteDestino.getSaldo() + transferenciaDto.getMonto());
            clienteOrigen.setSaldo(clienteOrigen.getSaldo() - transferenciaDto.getMonto());
            clienteDao.save(clienteDestino);
            clienteDao.save(clienteOrigen);
        }
    }

    private Transferencia toTransferencia(TransferenciaDto transferenciaDto) {
        Transferencia transferencia = new Transferencia();
        transferencia.setMonto(transferenciaDto.getMonto());
        transferencia.setCuentaOrigen(transferenciaDto.getCuentaOrigen());
        transferencia.setCuentaDestino(transferenciaDto.getCuentaDestino());
        transferencia.setMoneda(transferenciaDto.getMoneda().toString());
        return transferencia;
    }
}
