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
    public ResponseEntity<String> crearCuenta(@PathVariable long dni, @RequestBody CuentaDto cuentaDto) {
        try {
            cuentaValidator.validate(cuentaDto, dni);
            Cuenta cuentaCreada = cuentaService.darDeAltaCuenta(cuentaDto, dni);
            return ResponseEntity.ok("Cuenta creada con exito.");
        } catch (TipoCuentaAlreadyExistException | CuentaAlreadyExistException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No fue posible crear la cuenta.");
        }
    }


}
