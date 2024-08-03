package ar.edu.utn.frbb.tup.proyectoFinal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import ar.edu.utn.frbb.tup.proyectoFinal.controller.dto.RespuestaTransaccionDto;
import ar.edu.utn.frbb.tup.proyectoFinal.controller.dto.RetiroDto;
import ar.edu.utn.frbb.tup.proyectoFinal.model.*;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.ClienteDoesntExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.CuentaDoesntExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.InputErrorException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.NotPosibleException;
import ar.edu.utn.frbb.tup.proyectoFinal.persistencia.CuentaDao;
import ar.edu.utn.frbb.tup.proyectoFinal.service.CuentaService;
import ar.edu.utn.frbb.tup.proyectoFinal.service.RetiroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class RetiroServiceTest {

    @Mock
    private CuentaDao cuentaDao;

    @Mock
    private CuentaService cuentaService;

    @Mock
    private Transaccion transaccion;

    @InjectMocks
    private RetiroService retiroService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRealizarRetiroConCuentaExistenteYMonedaCorrectaYSaldoSuficiente() throws Exception, CuentaDoesntExistException, InputErrorException, ClienteDoesntExistException, NotPosibleException {
        RetiroDto retiroDto = new RetiroDto();
        retiroDto.setCuenta("123456789");
        retiroDto.setMonto("100.0");
        retiroDto.setMoneda(TipoMoneda.PESOS);

        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(123456789L);
        cuenta.setMoneda(TipoMoneda.PESOS);
        cuenta.setBalance(500.0);

        when(cuentaDao.findByNumeroCuenta(123456789L)).thenReturn(cuenta);
        when(transaccion.calcularComision(retiroDto)).thenReturn(2.0);

        RespuestaTransaccionDto respuesta = retiroService.realizarRetiro(retiroDto);

        assertEquals("EXITOSA", respuesta.getEstado());
        verify(cuentaService).actualizarBalance(cuenta, 100.0, 2.0, TipoMovimiento.RETIRO);
        verify(transaccion).save(any(Cuenta.class), any(Retiro.class), eq(TipoMovimiento.RETIRO), anyString());
    }

    @Test
    public void testRealizarRetiroConCuentaNoExistente() {
        RetiroDto retiroDto = new RetiroDto();
        retiroDto.setCuenta("123456789");

        when(cuentaDao.findByNumeroCuenta(123456789L)).thenReturn(null);

        // Ejecutar y verificar que se lanza la excepción correspondiente
        assertThrows(CuentaDoesntExistException.class, () -> {
            retiroService.realizarRetiro(retiroDto);
        });
    }

    @Test
    public void testRealizarRetiroConMonedaIncorrecta() {
        RetiroDto retiroDto = new RetiroDto();
        retiroDto.setCuenta("123456789");
        retiroDto.setMoneda(TipoMoneda.DOLARES);

        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(123456789L);
        cuenta.setMoneda(TipoMoneda.PESOS);

        when(cuentaDao.findByNumeroCuenta(123456789L)).thenReturn(cuenta);

        // Ejecutar y verificar que se lanza la excepción correspondiente
        assertThrows(NotPosibleException.class, () -> {
            retiroService.realizarRetiro(retiroDto);
        });
    }

    @Test
    public void testRealizarRetiroConSaldoInsuficiente() throws Exception, CuentaDoesntExistException, InputErrorException, ClienteDoesntExistException, NotPosibleException {
        RetiroDto retiroDto = new RetiroDto();
        retiroDto.setCuenta("123456789");
        retiroDto.setMonto("600.0");
        retiroDto.setMoneda(TipoMoneda.PESOS);

        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(123456789L);
        cuenta.setMoneda(TipoMoneda.PESOS);
        cuenta.setBalance(500.0);

        when(cuentaDao.findByNumeroCuenta(123456789L)).thenReturn(cuenta);

        RespuestaTransaccionDto respuesta = retiroService.realizarRetiro(retiroDto);

        // Verificar el resultado
        assertEquals("FALLIDA", respuesta.getEstado());
        assertEquals("No se pudo realizar el retiro. Saldo insuficiente.", respuesta.getMensaje());
    }
}
