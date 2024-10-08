package ar.edu.utn.frbb.tup.proyectoFinal.controller;

import ar.edu.utn.frbb.tup.proyectoFinal.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.proyectoFinal.controller.validator.CuentaValidator;
import ar.edu.utn.frbb.tup.proyectoFinal.model.Cuenta;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.*;
import ar.edu.utn.frbb.tup.proyectoFinal.service.ClienteService;
import ar.edu.utn.frbb.tup.proyectoFinal.service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.Set;

@RestController
@RequestMapping("/cuenta")
public class CuentaController {
    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private CuentaValidator cuentaValidator;

    //Endpoint para crear una cuenta. Con condiciones, debe existir el cliente, los datos del titular deben ser los mismos
    //y ademas el cliente no puede tener una cuenta del mismo tipo y moneda.
    @PostMapping
    public ResponseEntity<String> crearCuenta(@RequestBody CuentaDto cuentaDto, WebRequest request)
            throws TipoCuentaAlreadyExistException, ClienteDoesntExistException, NotPosibleException, InputErrorException, CuentaAlreadyExistException {
        cuentaValidator.validate(cuentaDto);
        Cuenta cuenta = cuentaService.darDeAltaCuenta(cuentaDto);
        System.out.println("Cuenta creada con exito. NUMERO DE CUENTA: " + cuenta.getNumeroCuenta());
        return ResponseEntity.ok("Cuenta creada con exito. NUMERO DE CUENTA: " + cuenta.getNumeroCuenta());

    }

    @GetMapping("/{numeroCuenta}")
    public ResponseEntity<Cuenta> mostrarCuenta(@PathVariable long numeroCuenta, WebRequest request)
            throws NotPosibleException {
        Cuenta cuenta = cuentaService.buscarCuentaPorNumeroCuenta(numeroCuenta);
        return ResponseEntity.ok(cuenta);
    }

    @GetMapping("/{dni}/cuentas")
    public ResponseEntity<Set<Cuenta>> mostrarCuentasPorDni(@PathVariable long dni, WebRequest request)
            throws ClienteDoesntExistException {
        Set<Cuenta> cuentas = cuentaService.buscarCuentasPorDni(dni);
        return ResponseEntity.ok(cuentas);    }
}
