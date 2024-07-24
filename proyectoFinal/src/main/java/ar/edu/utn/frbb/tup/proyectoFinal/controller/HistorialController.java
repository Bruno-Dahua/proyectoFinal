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
    private CuentaService cuentaService;

    @Autowired
    private CuentaDao cuentaDao;

    @Autowired
    private HistorialService historialService;

    @GetMapping("/{cuentaId}/transacciones")
    public ResponseEntity<RespuestaHistorialDto> obtenerHistorial(@PathVariable long cuentaId) throws NotPosibleException {
        // Busco la cuenta por su numeroCuenta
        Cuenta cuenta = historialService.buscarCuentaPorId(cuentaId);

        if (cuenta == null) {
            return ResponseEntity.notFound().build();
        }

        RespuestaHistorialDto respuesta = historialService.obtenerHistorial(cuentaId);
        return ResponseEntity.ok(respuesta);
    }


}
