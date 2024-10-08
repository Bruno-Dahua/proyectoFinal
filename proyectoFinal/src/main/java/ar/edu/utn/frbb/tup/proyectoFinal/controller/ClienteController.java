package ar.edu.utn.frbb.tup.proyectoFinal.controller;

import ar.edu.utn.frbb.tup.proyectoFinal.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.proyectoFinal.controller.validator.ClienteValidator;
import ar.edu.utn.frbb.tup.proyectoFinal.model.Cliente;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.ClienteAlreadyExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.ClienteDoesntExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.InputErrorException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.NotPosibleException;
import ar.edu.utn.frbb.tup.proyectoFinal.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;


@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteValidator clienteValidator;


    //Endpoint para crear un cliente, ingresando Json con los campos necesarios (dni, nombre, apellido, fechaNacimiento, tipoPersona y banco)
    @PostMapping
    public ResponseEntity<String> crearCliente(@RequestBody ClienteDto clienteDto, WebRequest request) throws ClienteAlreadyExistException, ClienteDoesntExistException, NotPosibleException, InputErrorException {
        clienteValidator.validate(clienteDto);
        clienteService.darDeAltaCliente(clienteDto);
        System.out.println("El cliente con DNI " + clienteDto.getDni() + " fue creado con exito.");
        return ResponseEntity.ok("El cliente con DNI " + clienteDto.getDni() + " fue creado con exito.");
    }

    //Endpoint para mostrar un cliente, buscandolo por su dni
    @GetMapping("/{dni}")
    public Cliente mostrarClientePorDni(@PathVariable long dni, WebRequest request) throws ClienteDoesntExistException {
        return clienteService.buscarClientePorDni(dni);
    }

    //Endpoint para actualizar un cliente, buscandolo por su dni. Se valida el ingreso de los datos necesarios
    @PutMapping("/{dni}")
    public ResponseEntity<String> actualizarClientePorDni(@PathVariable long dni, @RequestBody ClienteDto clienteDto, WebRequest request) throws ClienteAlreadyExistException, ClienteDoesntExistException, NotPosibleException, InputErrorException {
        clienteValidator.validate(clienteDto);
        clienteService.actualizarCliente(dni, clienteDto);
        return ResponseEntity.ok("El cliente con DNI " + dni + " fue actualizado con exito.");
    }

    //Endpoint para eliminar un cliente, buscandolo por su DNI
    @DeleteMapping("/{dni}")
    public ResponseEntity<String> eliminarClientePorDni(@PathVariable long dni) throws ClienteDoesntExistException {
        if (clienteService.eliminarCliente(dni)) {
            System.out.println("El cliente con DNI " + dni + " fue eliminado con exito.");
            return ResponseEntity.ok("El cliente con DNI " + dni + " fue eliminado con exito.");
        } else {
            System.out.println("No fue posible eliminar el cliente con DNI " + dni + ".");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No fue posible eliminar el cliente con DNI " + dni + ".");
        }
    }
}
