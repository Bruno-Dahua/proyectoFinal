package ar.edu.utn.frbb.tup.proyectoFinal.controller;

import ar.edu.utn.frbb.tup.proyectoFinal.controller.validator.ClienteValidator;
import ar.edu.utn.frbb.tup.proyectoFinal.model.Cliente;
import ar.edu.utn.frbb.tup.proyectoFinal.model.Cuenta;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.ClienteAlreadyExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteValidator clienteValidator;


    @PostMapping
    public Cliente crearCliente(@RequestBody ClienteDto clienteDto, WebRequest request) throws ClienteAlreadyExistException {
        clienteValidator.validate(clienteDto);
        System.out.println("Creando Cliente");
        return clienteService.darDeAltaCliente(clienteDto);
    }

    @GetMapping("/{dni}")
    public Cliente mostrarClientePorDni(@PathVariable long dni, WebRequest request){
        return clienteService.buscarClientePorDni(dni);
    }

    /*@PutMapping("/{dni}")
    public void actualizarClientePorDni(@PathVariable long dni, @RequestBody Cliente cliente, WebRequest request) throws ClienteAlreadyExistException{
        System.out.println("Actualizando cliente con DNI: " + dni);

        cliente.setDni(dni);
        Cliente actualizado = clienteService.actualizarCliente(dni, cliente);

        System.out.println("Cliente actualizado: " + actualizado);
        return actualizado;
    }*/

    @DeleteMapping("/{dni}")
    public ResponseEntity<String> eliminarClientePorDni(@PathVariable long dni) {
        boolean eliminado = clienteService.eliminarCliente(dni);
        if (eliminado) {
            return ResponseEntity.ok("Cliente eliminado exitosamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente no encontrado");
        }
    }


}
