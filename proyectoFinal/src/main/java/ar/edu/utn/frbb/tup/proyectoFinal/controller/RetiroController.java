package ar.edu.utn.frbb.tup.proyectoFinal.controller;

import ar.edu.utn.frbb.tup.proyectoFinal.controller.dto.RespuestaTransaccionDto;
import ar.edu.utn.frbb.tup.proyectoFinal.controller.dto.RetiroDto;
import ar.edu.utn.frbb.tup.proyectoFinal.controller.validator.RetiroValidator;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.ClienteDoesntExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.CuentaDoesntExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.InputErrorException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.NotPosibleException;
import ar.edu.utn.frbb.tup.proyectoFinal.service.RetiroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/retiro")
public class RetiroController {
    @Autowired
    private RetiroService retiroService;

    @Autowired
    private RetiroValidator retiroValidator;

    @PostMapping
    public RespuestaTransaccionDto realizarRetiro(@RequestBody RetiroDto retiroDto) throws NotPosibleException, ClienteDoesntExistException, InputErrorException, CuentaDoesntExistException {
        retiroValidator.validate(retiroDto);
        return retiroService.realizarRetiro(retiroDto);
    }
}
