package ar.edu.utn.frbb.tup.proyectoFinal.controller;

import ar.edu.utn.frbb.tup.proyectoFinal.controller.dto.RespuestaHistorialDto;
import ar.edu.utn.frbb.tup.proyectoFinal.model.Cuenta;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.NotPosibleException;
import ar.edu.utn.frbb.tup.proyectoFinal.persistencia.CuentaDao;
import ar.edu.utn.frbb.tup.proyectoFinal.service.CuentaService;
import ar.edu.utn.frbb.tup.proyectoFinal.service.HistorialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cuenta")
public class HistorialController {

    @Autowired
    private HistorialService historialService;

    @GetMapping("/{numeroCuenta}/transacciones")
    public ResponseEntity<RespuestaHistorialDto> obtenerHistorial(@PathVariable long numeroCuenta) throws NotPosibleException {
        RespuestaHistorialDto respuesta = historialService.obtenerHistorial(numeroCuenta);
        return ResponseEntity.ok(respuesta);

    }
}
