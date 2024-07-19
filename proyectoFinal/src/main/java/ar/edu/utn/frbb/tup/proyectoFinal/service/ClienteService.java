package ar.edu.utn.frbb.tup.proyectoFinal.service;

import ar.edu.utn.frbb.tup.proyectoFinal.controller.ClienteDto;
import ar.edu.utn.frbb.tup.proyectoFinal.model.Cliente;
import ar.edu.utn.frbb.tup.proyectoFinal.model.Cuenta;
import ar.edu.utn.frbb.tup.proyectoFinal.model.TipoPersona;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.ClienteAlreadyExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.TipoCuentaAlreadyExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.persistencia.ClienteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class ClienteService {

    @Autowired
    ClienteDao clienteDao;

    public ClienteService(ClienteDao clienteDao) {
        this.clienteDao = clienteDao;
    }

    //devuelve un booleano para que en el controller pueda dar una buena o mala respuesta
    public boolean darDeAltaCliente(ClienteDto clienteDto) throws ClienteAlreadyExistException {

        Cliente cliente = new Cliente(clienteDto);

        if (clienteDao.find(cliente.getDni(), false) != null) {
            return false;
        }

        else if (cliente.getEdad() < 18) {
            return false;
        }

        else {
            clienteDao.save(cliente);
            return true;
        }
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

    public Cliente mostrarCliente(Cliente clienteMostrar){
        Cliente clienteRespuesta = new Cliente();
        clienteRespuesta.setDni(clienteMostrar.getDniString());
        clienteRespuesta.setNombre(clienteMostrar.getNombre());
        clienteRespuesta.setApellido(clienteMostrar.getApellido());
        clienteRespuesta.setBanco(clienteMostrar.getBanco());
        clienteRespuesta.setTipoPersona(clienteMostrar.getTipoPersona());
        clienteRespuesta.setCuentas(clienteMostrar.getCuentas());
        clienteRespuesta.setFechaNacimiento(clienteMostrar.getFechaNacimiento());
        clienteRespuesta.setFechaAlta(clienteMostrar.getFechaAlta());
        return clienteRespuesta;
    }

    public void actualizarCliente(long dni, ClienteDto clienteActualizado) {

        Cliente clienteExistente = clienteDao.find(dni, false);

        if (clienteExistente == null){
            throw new IllegalArgumentException("Cliente no encontrado");
        }

        clienteExistente.setNombre(clienteActualizado.getNombre());
        clienteExistente.setApellido(clienteActualizado.getApellido());
        clienteExistente.setFechaNacimiento(clienteActualizado.getFechaNacimiento());
        clienteExistente.setBanco(clienteActualizado.getBanco());
        clienteExistente.setTipoPersona(TipoPersona.fromString(clienteActualizado.getTipoPersona()));
        clienteExistente.setDni(clienteActualizado.getDni());

        clienteDao.save(clienteExistente);
    }

    public boolean eliminarCliente(long dni) {
        return clienteDao.delete(dni);
    }

    public Set<Cuenta> getCuentasPorDni(long dni) {
        Cliente cliente = clienteDao.find(dni, true); // Suponiendo que find busca un cliente por DNI
        if (cliente != null) {
            return cliente.getCuentas(); // Suponiendo que getCuentas() devuelve el Set de cuentas del cliente
        } else {
            return Collections.emptySet(); // Si el cliente no existe, devuelve un Set vacío
        }
    }
}