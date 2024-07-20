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
    @Autowired
    private CuentaService cuentaService;

    public ClienteService(ClienteDao clienteDao) {
        this.clienteDao = clienteDao;
    }

    //devuelve un booleano para que en el controller pueda dar una buena o mala respuesta
    public boolean darDeAltaCliente(ClienteDto clienteDto) {

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

    public void actualizarCliente(long dniAntiguo, ClienteDto clienteDto) {

        Cliente clienteExistente = clienteDao.find(dniAntiguo, false);

        if (clienteExistente == null){
            throw new IllegalArgumentException("Cliente no encontrado");
        }
        else if (clienteExistente.getEdad() < 18) {
            throw new IllegalArgumentException("No fue posible actualizar el cliente");
        }

        Cliente clienteActualizado = toCliente(clienteDto);
        clienteActualizado.setFechaAlta(clienteExistente.getFechaAlta());

        cuentaService.actualizarTitularCuenta(clienteActualizado, dniAntiguo);

        clienteDao.update(clienteActualizado);

        clienteDao.delete(dniAntiguo);
    }

    public boolean eliminarCliente(long dni) {
        Cliente clienteEliminar = clienteDao.find(dni, false);
        if (clienteEliminar == null) {
            throw new IllegalArgumentException("El cliente no existe");
        }else {
            return clienteDao.delete(dni);
        }
    }

    public Set<Cuenta> getCuentasPorDni(long dni) {
        Cliente cliente = clienteDao.find(dni, true);

        if (cliente != null) {
            return cliente.getCuentas();
        } else if (cliente != null && cliente.getCuentas() == null) {
            throw new IllegalArgumentException("El cliente no tiene cuentas");
        }else{
            throw new IllegalArgumentException("El cliente no existe");
        }
    }

    private Cliente toCliente(ClienteDto clienteDto) {
        Cliente cliente = new Cliente();
        cliente.setNombre(clienteDto.getNombre());
        cliente.setApellido(clienteDto.getApellido());
        cliente.setFechaNacimiento(clienteDto.getFechaNacimiento());
        cliente.setBanco(clienteDto.getBanco());
        cliente.setTipoPersona(TipoPersona.fromString(clienteDto.getTipoPersona()));
        cliente.setDni(clienteDto.getDni());
        return cliente;
    }
}