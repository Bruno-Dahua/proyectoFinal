package ar.edu.utn.frbb.tup.proyectoFinal.service;

import ar.edu.utn.frbb.tup.proyectoFinal.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.proyectoFinal.model.Cliente;
import ar.edu.utn.frbb.tup.proyectoFinal.model.Cuenta;
import ar.edu.utn.frbb.tup.proyectoFinal.model.TipoPersona;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.ClienteDoesntExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.NotPosibleException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.TipoCuentaAlreadyExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.persistencia.ClienteDao;
import ar.edu.utn.frbb.tup.proyectoFinal.persistencia.CuentaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;

@Service
public class ClienteService {

    @Autowired
    ClienteDao clienteDao;
    @Autowired
    private CuentaService cuentaService;
    @Autowired
    private CuentaDao cuentaDao;

    public ClienteService(ClienteDao clienteDao) {
        this.clienteDao = clienteDao;
    }

    //devuelve un booleano para que en el controller pueda dar una buena o mala respuesta
    public void darDeAltaCliente(ClienteDto clienteDto) throws ClienteDoesntExistException, NotPosibleException {

        Cliente cliente = new Cliente(clienteDto);

        if (clienteDao.find(cliente.getDni()) != null) {
            throw new NotPosibleException("El cliente con DNI " + cliente.getDni() + " ya existe.");
        }

        else if (cliente.getEdad() < 18) {
            throw new NotPosibleException("El cliente con DNI " + cliente.getDni() + " no puede registrarse, no cumple con la edad mínima.");
        }

        clienteDao.save(cliente);

    }

    public void agregarCuenta(Cuenta cuenta, long dniTitular) throws ClienteDoesntExistException {
        Cliente titular = buscarClientePorDni(dniTitular);
        cuenta.setTitular(titular);

        titular.addCuenta(cuenta);

        clienteDao.save(titular);
    }

    public Cliente buscarClientePorDni(Long dni) throws ClienteDoesntExistException {
        Cliente cliente = clienteDao.find(dni);

        if (cliente == null) {
            throw new ClienteDoesntExistException("No existe el cliente con DNI " + dni + ".");
        }

        return cliente;
    }


    public void actualizarCliente(Long dniAntiguo, ClienteDto clienteDto) throws ClienteDoesntExistException, NotPosibleException {
        Cliente clienteExistente = clienteDao.find(dniAntiguo);

        if (clienteExistente == null) {
            throw new ClienteDoesntExistException("No existe el cliente con DNI " + dniAntiguo + ".");
        }

        if (clienteExistente.getEdad() < 18) {
            throw new NotPosibleException("No fue posible actualizar el cliente con DNI " + clienteDto.getDni() + ".");
        }

        Cliente clienteActualizado = toCliente(clienteDto);
        clienteActualizado.setFechaAlta(clienteExistente.getFechaAlta());

        cuentaService.actualizarTitularCuenta(clienteActualizado, dniAntiguo);


        clienteDao.update(clienteActualizado);
        clienteDao.delete(dniAntiguo);
    }


    public boolean eliminarCliente(Long dni) throws ClienteDoesntExistException {
        Cliente clienteEliminar = clienteDao.find(dni);
        if (clienteEliminar == null) {
            throw new ClienteDoesntExistException("No existe el cliente con DNI " + dni + ".");
        }else {
            return clienteDao.delete(dni);
        }
    }



    private Cliente toCliente(ClienteDto clienteDto) {
        Cliente cliente = new Cliente();
        cliente.setNombre(clienteDto.getNombre());
        cliente.setApellido(clienteDto.getApellido());
        cliente.setFechaNacimiento(LocalDate.parse(clienteDto.getFechaNacimiento()));
        cliente.setBanco(clienteDto.getBanco());
        cliente.setTipoPersona(clienteDto.getTipoPersona());
        cliente.setDni(clienteDto.getDni());
        return cliente;
    }
}