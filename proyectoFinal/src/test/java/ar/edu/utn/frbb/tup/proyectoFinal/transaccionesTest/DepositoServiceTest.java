package ar.edu.utn.frbb.tup.proyectoFinal.transaccionesTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import ar.edu.utn.frbb.tup.proyectoFinal.controller.dto.DepositoDto;
import ar.edu.utn.frbb.tup.proyectoFinal.controller.dto.RespuestaTransaccionDto;
import ar.edu.utn.frbb.tup.proyectoFinal.model.*;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.ClienteDoesntExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.CuentaDoesntExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.NotPosibleException;
import ar.edu.utn.frbb.tup.proyectoFinal.persistencia.CuentaDao;
import ar.edu.utn.frbb.tup.proyectoFinal.service.CuentaService;
import ar.edu.utn.frbb.tup.proyectoFinal.service.DepositoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class DepositoServiceTest {

    @Mock
    private CuentaDao cuentaDao;

    @Mock
    private CuentaService cuentaService;

    @Mock
    private Transaccion transaccion;

    @InjectMocks
    private DepositoService depositoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRealizarDepositoConCuentaExistenteYMonedaCorrecta() throws CuentaDoesntExistException, ClienteDoesntExistException, NotPosibleException {
        DepositoDto depositoDto = new DepositoDto();
        depositoDto.setCuenta("45889159");
        depositoDto.setMonto("100.0");
        depositoDto.setMoneda(TipoMoneda.PESOS);

        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(45889159L);
        cuenta.setMoneda(TipoMoneda.PESOS);
        cuenta.setBalance(500.0);

        when(cuentaDao.findByNumeroCuenta(45889159L)).thenReturn(cuenta);
        when(transaccion.calcularComision(depositoDto)).thenReturn(2.0);

        RespuestaTransaccionDto respuesta = depositoService.realizarDeposito(depositoDto);

        assertEquals("EXITOSA", respuesta.getEstado());
        verify(cuentaService).actualizarBalance(cuenta, 100.0, 2.0, TipoMovimiento.DEPOSITO);
        verify(transaccion).save(any(Cuenta.class), any(Deposito.class), eq(TipoMovimiento.DEPOSITO), anyString());
    }

    @Test
    public void testRealizarDepositoConCuentaNoExistente() {
        DepositoDto depositoDto = new DepositoDto();
        depositoDto.setCuenta("45889159");

        when(cuentaDao.findByNumeroCuenta(45889159L)).thenReturn(null);

        // Ejecutar y verificar que se lanza la excepción correspondiente
        assertThrows(CuentaDoesntExistException.class, () -> {
            depositoService.realizarDeposito(depositoDto);
        });
    }

    @Test
    public void testRealizarDepositoConMonedaIncorrecta() {
        DepositoDto depositoDto = new DepositoDto();
        depositoDto.setCuenta("45889159");
        depositoDto.setMoneda(TipoMoneda.DOLARES);

        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(45889159L);
        cuenta.setMoneda(TipoMoneda.PESOS);

        when(cuentaDao.findByNumeroCuenta(45889159L)).thenReturn(cuenta);

        // Ejecutar y verificar que se lanza la excepción correspondiente
        assertThrows(NotPosibleException.class, () -> {
            depositoService.realizarDeposito(depositoDto);
        });
    }
}
