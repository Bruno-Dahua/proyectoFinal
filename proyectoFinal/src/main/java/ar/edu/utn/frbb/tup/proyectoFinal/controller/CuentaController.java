package ar.edu.utn.frbb.tup.proyectoFinal.controller;

import ar.edu.utn.frbb.tup.proyectoFinal.controller.validator.CuentaValidator;
import ar.edu.utn.frbb.tup.proyectoFinal.model.Cuenta;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.ClienteDoesntExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.CuentaAlreadyExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.NotPosibleException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.TipoCuentaAlreadyExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.service.ClienteService;
import ar.edu.utn.frbb.tup.proyectoFinal.service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/cuenta")
public class CuentaController {
    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private CuentaValidator cuentaValidator;

    @Autowired
    private ClienteService clienteService;

    //Endpoint para crear una cuenta. Con condiciones, debe existir el cliente, los datos del titular deben ser los mismos
    //y ademas el cliente no puede tener una cuenta del mismo tipo y moneda.
    @PostMapping
    public ResponseEntity<String> crearCuenta(@RequestBody CuentaDto cuentaDto, WebRequest request) throws TipoCuentaAlreadyExistException, ClienteDoesntExistException, NotPosibleException {
        cuentaValidator.validate(cuentaDto);
        if (cuentaService.darDeAltaCuenta(cuentaDto)){
            System.out.println("Cuenta creada con exito.");
            return ResponseEntity.ok("Cuenta creada con exito.");
        } else {
            System.out.println("No fue posible crear la cuenta.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No fue posible crear la cuenta.");
        }
    }

    @GetMapping("/{dni}")
    public ResponseEntity<Set<Cuenta>> mostrarCuentas(@PathVariable long dni, WebRequest request)
            throws ClienteDoesntExistException, NotPosibleException {
        Set<Cuenta> cuentas = clienteService.getCuentasPorDni(dni);
        return ResponseEntity.ok(cuentas);
    }
}
