package ar.edu.utn.frbb.tup.proyectoFinal.service;

import ar.edu.utn.frbb.tup.proyectoFinal.controller.ClienteDto;
import ar.edu.utn.frbb.tup.proyectoFinal.model.Cliente;
import ar.edu.utn.frbb.tup.proyectoFinal.model.Cuenta;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.ClienteAlreadyExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.TipoCuentaAlreadyExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.persistencia.ClienteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ClienteService {

    @Autowired
    ClienteDao clienteDao;

    public ClienteService(ClienteDao clienteDao) {
        this.clienteDao = clienteDao;
    }

    public Cliente darDeAltaCliente(ClienteDto clienteDto) throws ClienteAlreadyExistException {
        Cliente cliente = new Cliente(clienteDto);

        if (clienteDao.find(cliente.getDni(), false) != null) {
            throw new ClienteAlreadyExistException("Ya existe un cliente con DNI " + cliente.getDni());
        }

        if (cliente.getEdad() < 18) {
            throw new IllegalArgumentException("El cliente debe ser mayor a 18 años");
        }

        clienteDao.save(cliente);
        return cliente;
    }

    public void agregarCuenta(Cuenta cuenta, long dniTitular) throws TipoCuentaAlreadyExistException {
        Cliente titular = buscarClientePorDni(dniTitular);
        cuenta.setTitular(titular);
        if (titular.tieneCuenta(cuenta.getTipoCuenta(), cuenta.getMoneda())) {
            throw new TipoCuentaAlreadyExistException("El cliente ya posee una cuenta de ese tipo y moneda");
        }
        titular.addCuenta(cuenta);
        clienteDao.save(titular);
    }

    public Cliente buscarClientePorDni(long dni) {
        Cliente cliente = clienteDao.find(dni, false);
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no existe");
        }
        return cliente;
    }

    public Cliente actualizarCliente(long dni, Cliente clienteActualizado) {
        Cliente clienteExistente = clienteDao.find(dni, false);
                if (clienteExistente == null){
                    throw new IllegalArgumentException("Cliente no encontrado");
                }


        clienteExistente.setNombre(clienteActualizado.getNombre());
        clienteExistente.setApellido(clienteActualizado.getApellido());
        clienteExistente.setFechaNacimiento(clienteActualizado.getFechaNacimiento());
        clienteExistente.setBanco(clienteActualizado.getBanco());
        clienteExistente.setTipoPersona(clienteActualizado.getTipoPersona());
        clienteExistente.setDni(clienteActualizado.getDni());

        return clienteDao.save(clienteExistente);
    }

    public boolean eliminarCliente(long dni) {
        return clienteDao.delete(dni);
    }

}