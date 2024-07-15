package ar.edu.utn.frbb.tup.proyectoFinal.controller;

import ar.edu.utn.frbb.tup.proyectoFinal.controller.validator.CuentaValidator;
import ar.edu.utn.frbb.tup.proyectoFinal.model.Cuenta;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.CuentaAlreadyExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.TipoCuentaAlreadyExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cuenta")
public class CuentaController {
    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private CuentaValidator cuentaValidator;

    @PostMapping("/{dni}")
    public ResponseEntity<Cuenta> crearCuenta(@PathVariable long dni, @RequestBody CuentaDto cuentaDto) {
        try {
            cuentaValidator.validate(cuentaDto, dni);
            Cuenta cuentaCreada = cuentaService.darDeAltaCuenta(cuentaDto, dni);
            return new ResponseEntity<>(cuentaCreada, HttpStatus.CREATED);
        } catch (TipoCuentaAlreadyExistException | CuentaAlreadyExistException | IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
