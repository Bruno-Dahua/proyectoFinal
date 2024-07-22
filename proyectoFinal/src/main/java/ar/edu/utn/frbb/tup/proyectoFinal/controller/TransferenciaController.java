package ar.edu.utn.frbb.tup.proyectoFinal.controller;

import ar.edu.utn.frbb.tup.proyectoFinal.controller.validator.ClienteValidator;
import ar.edu.utn.frbb.tup.proyectoFinal.controller.validator.TransferenciaValidator;
import ar.edu.utn.frbb.tup.proyectoFinal.model.Transferencia;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.ClienteDoesntExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.NotPosibleException;
import ar.edu.utn.frbb.tup.proyectoFinal.service.ClienteService;
import ar.edu.utn.frbb.tup.proyectoFinal.service.TransferenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transferencia")
public class TransferenciaController {

    @Autowired
    private TransferenciaService transferenciaService;

    @Autowired
    private TransferenciaValidator transferenciaValidator;


    //Endpoint para realizar una transferencia, ingresando Json con los campos necesarios (cuentaOrigen, cuentaDestino, monto y tipoMoneda)
    @PostMapping
    public RespuestaTransferenciaDto realizarTransferencia(@RequestBody TransferenciaDto transferenciaDto) throws NotPosibleException, ClienteDoesntExistException {
        transferenciaValidator.validate(transferenciaDto);
        RespuestaTransferenciaDto respuestaTransferenciaDto = transferenciaService.realizarTransferencia(transferenciaDto);
        return respuestaTransferenciaDto;
    }
}
