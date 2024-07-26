package ar.edu.utn.frbb.tup.proyectoFinal.controller;

import ar.edu.utn.frbb.tup.proyectoFinal.controller.dto.RespuestaTransferenciaDto;
import ar.edu.utn.frbb.tup.proyectoFinal.controller.dto.TransferenciaDto;
import ar.edu.utn.frbb.tup.proyectoFinal.controller.validator.TransferenciaValidator;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.ClienteDoesntExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.CuentaDoesntExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.InputErrorException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.NotPosibleException;
import ar.edu.utn.frbb.tup.proyectoFinal.service.TransferenciaService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public RespuestaTransferenciaDto realizarTransferencia(@RequestBody TransferenciaDto transferenciaDto) throws NotPosibleException, ClienteDoesntExistException, InputErrorException, CuentaDoesntExistException {
        transferenciaValidator.validate(transferenciaDto);
        return transferenciaService.realizarTransferencia(transferenciaDto);
    }
}
