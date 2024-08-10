package ar.edu.utn.frbb.tup.proyectoFinal.transaccionesTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import ar.edu.utn.frbb.tup.proyectoFinal.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.proyectoFinal.controller.dto.RespuestaTransaccionDto;
import ar.edu.utn.frbb.tup.proyectoFinal.controller.dto.TransferenciaDto;
import ar.edu.utn.frbb.tup.proyectoFinal.model.*;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.ClienteDoesntExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.CuentaDoesntExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.NotPosibleException;
import ar.edu.utn.frbb.tup.proyectoFinal.persistencia.CuentaDao;
import ar.edu.utn.frbb.tup.proyectoFinal.service.BanelcoService;
import ar.edu.utn.frbb.tup.proyectoFinal.service.CuentaService;
import ar.edu.utn.frbb.tup.proyectoFinal.service.TransferenciaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TransferenciaServiceTest {

    @Mock
    private CuentaDao cuentaDao;

    @Mock
    private CuentaService cuentaService;

    @Mock
    private Transaccion transaccion;

    @Mock
    private BanelcoService banelcoService;

    @InjectMocks
    private TransferenciaService transferenciaService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRealizarTransferenciaConCuentasExistentesYMismasMonedasYSuficienteSaldo() throws Exception, CuentaDoesntExistException, ClienteDoesntExistException, NotPosibleException {
        // Datos de prueba
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setCuentaOrigen(123456789L);
        transferenciaDto.setCuentaDestino(987654321L);
        transferenciaDto.setMonto(100.0);
        transferenciaDto.setMoneda(String.valueOf(TipoMoneda.PESOS));

        // Creo el ClienteDto y Cliente para cuentaOrigen
        ClienteDto clienteDtoOrigen = new ClienteDto();
        clienteDtoOrigen.setBanco("Banco A");
        clienteDtoOrigen.setFechaNacimiento("2005-03-03");
        Cliente clienteOrigen = new Cliente(clienteDtoOrigen);

        // Creo el ClienteDto y Cliente para cuentaDestino
        ClienteDto clienteDtoDestino = new ClienteDto();
        clienteDtoDestino.setBanco("Banco A");
        clienteDtoDestino.setFechaNacimiento("2005-03-03");
        Cliente clienteDestino = new Cliente(clienteDtoDestino);

        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setNumeroCuenta(123456789L);
        cuentaOrigen.setMoneda(TipoMoneda.PESOS);
        cuentaOrigen.setBalance(500.0);
        cuentaOrigen.setTitular(clienteOrigen);

        Cuenta cuentaDestino = new Cuenta();
        cuentaDestino.setNumeroCuenta(987654321L);
        cuentaDestino.setMoneda(TipoMoneda.PESOS);
        cuentaDestino.setTitular(clienteDestino);

        when(cuentaDao.findByNumeroCuenta(123456789L)).thenReturn(cuentaOrigen);
        when(cuentaDao.findByNumeroCuenta(987654321L)).thenReturn(cuentaDestino);
        when(transaccion.calcularComision(transferenciaDto)).thenReturn(2.0);

        RespuestaTransaccionDto respuesta = transferenciaService.realizarTransferencia(transferenciaDto);

        assertEquals("EXITOSA", respuesta.getEstado());
        verify(cuentaService).actualizarBalance(cuentaOrigen, 100.0, 2.0, TipoMovimiento.TRANSFERENCIA_SALIDA);
        verify(cuentaService).actualizarBalance(cuentaDestino, 100.0, 2.0, TipoMovimiento.TRANSFERENCIA_ENTRADA);
        verify(transaccion, times(2)).save(any(Cuenta.class), any(Transferencia.class), any(TipoMovimiento.class), anyString());
        verify(cuentaDao).update(cuentaOrigen);
        verify(cuentaDao).update(cuentaDestino);
    }

    @Test
    public void testRealizarTransferenciaConCuentaOrigenNoExistente() {
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setCuentaOrigen(123456789L);

        when(cuentaDao.findByNumeroCuenta(123456789L)).thenReturn(null);

        // Ejecutar y verificar que se lanza la excepción correspondiente
        assertThrows(CuentaDoesntExistException.class, () -> {
            transferenciaService.realizarTransferencia(transferenciaDto);
        });
    }

    @Test
    public void testRealizarTransferenciaConCuentaDestinoNoExistente() {
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setCuentaOrigen(123456789L);
        transferenciaDto.setCuentaDestino(987654321L);

        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setNumeroCuenta(123456789L);

        when(cuentaDao.findByNumeroCuenta(123456789L)).thenReturn(cuentaOrigen);
        when(cuentaDao.findByNumeroCuenta(987654321L)).thenReturn(null);

        // Ejecutar y verificar que se lanza la excepción correspondiente
        assertThrows(CuentaDoesntExistException.class, () -> {
            transferenciaService.realizarTransferencia(transferenciaDto);
        });
    }

    @Test
    public void testRealizarTransferenciaConMonedasDistintas() {
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setCuentaOrigen(123456789L);
        transferenciaDto.setCuentaDestino(987654321L);
        transferenciaDto.setMoneda(String.valueOf(TipoMoneda.DOLARES));

        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setNumeroCuenta(123456789L);
        cuentaOrigen.setMoneda(TipoMoneda.PESOS);

        Cuenta cuentaDestino = new Cuenta();
        cuentaDestino.setNumeroCuenta(987654321L);
        cuentaDestino.setMoneda(TipoMoneda.PESOS);

        when(cuentaDao.findByNumeroCuenta(123456789L)).thenReturn(cuentaOrigen);
        when(cuentaDao.findByNumeroCuenta(987654321L)).thenReturn(cuentaDestino);

        // Ejecutar y verificar que se lanza la excepción correspondiente
        assertThrows(NotPosibleException.class, () -> {
            transferenciaService.realizarTransferencia(transferenciaDto);
        });
    }

    @Test
    public void testRealizarTransferenciaConSaldoInsuficiente() throws CuentaDoesntExistException, ClienteDoesntExistException, NotPosibleException {
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setCuentaOrigen(123456789L);
        transferenciaDto.setCuentaDestino(987654321L);
        transferenciaDto.setMonto(600.0);
        transferenciaDto.setMoneda(String.valueOf(TipoMoneda.PESOS));

        // Creo el ClienteDto y Cliente para cuentaOrigen
        ClienteDto clienteDtoOrigen = new ClienteDto();
        clienteDtoOrigen.setBanco("Banco A");
        clienteDtoOrigen.setFechaNacimiento("2005-03-03");
        Cliente clienteOrigen = new Cliente(clienteDtoOrigen);

        // Creo el ClienteDto y Cliente para cuentaDestino
        ClienteDto clienteDtoDestino = new ClienteDto();
        clienteDtoDestino.setBanco("Banco A");
        clienteDtoDestino.setFechaNacimiento("2005-03-03");
        Cliente clienteDestino = new Cliente(clienteDtoDestino);

        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setNumeroCuenta(123456789L);
        cuentaOrigen.setMoneda(TipoMoneda.PESOS);
        cuentaOrigen.setBalance(500.0);
        cuentaOrigen.setTitular(clienteOrigen);

        Cuenta cuentaDestino = new Cuenta();
        cuentaDestino.setNumeroCuenta(987654321L);
        cuentaDestino.setMoneda(TipoMoneda.PESOS);
        cuentaDestino.setTitular(clienteDestino);

        when(cuentaDao.findByNumeroCuenta(123456789L)).thenReturn(cuentaOrigen);
        when(cuentaDao.findByNumeroCuenta(987654321L)).thenReturn(cuentaDestino);

        RespuestaTransaccionDto respuesta = transferenciaService.realizarTransferencia(transferenciaDto);

        assertEquals("FALLIDA", respuesta.getEstado());
        assertEquals("Saldo insuficiente para realizar la transferencia.", respuesta.getMensaje());
    }

    @Test
    public void testRealizarTransferenciaEntreBancosDiferentesConServicioBanelco() throws CuentaDoesntExistException, ClienteDoesntExistException, NotPosibleException {
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setCuentaOrigen(123456789L);
        transferenciaDto.setCuentaDestino(987654322L); // Cuenta destino par (aprueba transferencia)
        transferenciaDto.setMonto(100.0);
        transferenciaDto.setMoneda(String.valueOf(TipoMoneda.PESOS));

        // Creo el ClienteDto y Cliente para cuentaOrigen
        ClienteDto clienteDtoOrigen = new ClienteDto();
        clienteDtoOrigen.setBanco("Banco A");
        clienteDtoOrigen.setFechaNacimiento("2005-03-03");
        Cliente clienteOrigen = new Cliente(clienteDtoOrigen);

        // Creo el ClienteDto y Cliente para cuentaDestino
        ClienteDto clienteDtoDestino = new ClienteDto();
        clienteDtoDestino.setBanco("Banco B"); //Bancos diferentes
        clienteDtoDestino.setFechaNacimiento("2005-03-03");
        Cliente clienteDestino = new Cliente(clienteDtoDestino);

        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setNumeroCuenta(123456789L);
        cuentaOrigen.setMoneda(TipoMoneda.PESOS);
        cuentaOrigen.setBalance(500.0);
        cuentaOrigen.setTitular(clienteOrigen);

        Cuenta cuentaDestino = new Cuenta();
        cuentaDestino.setNumeroCuenta(987654322L);
        cuentaDestino.setMoneda(TipoMoneda.PESOS);
        cuentaDestino.setTitular(clienteDestino);

        when(cuentaDao.findByNumeroCuenta(123456789L)).thenReturn(cuentaOrigen);
        when(cuentaDao.findByNumeroCuenta(987654322L)).thenReturn(cuentaDestino);
        when(banelcoService.servicioDeBanelco(transferenciaDto)).thenReturn(true);
        when(transaccion.calcularComision(transferenciaDto)).thenReturn(0.0);

        RespuestaTransaccionDto respuesta = transferenciaService.realizarTransferencia(transferenciaDto);

        assertEquals("EXITOSA", respuesta.getEstado());
        verify(cuentaService).actualizarBalance(cuentaOrigen, 100.0, 0.0, TipoMovimiento.TRANSFERENCIA_SALIDA);
        verify(cuentaService).actualizarBalance(cuentaDestino, 100.0, 0.0, TipoMovimiento.TRANSFERENCIA_ENTRADA);
        verify(transaccion, times(2)).save(any(Cuenta.class), any(Transferencia.class), any(TipoMovimiento.class), anyString());
        verify(cuentaDao).update(cuentaOrigen);
        verify(cuentaDao).update(cuentaDestino);
    }

    @Test
    public void testRealizarTransferenciaEntreBancosDiferentesSinServicioBanelco() throws CuentaDoesntExistException, ClienteDoesntExistException, NotPosibleException {
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setCuentaOrigen(123456789L);
        transferenciaDto.setCuentaDestino(987654321L); // Cuenta destino impar (desaprueba transferencia)
        transferenciaDto.setMonto(100.0);
        transferenciaDto.setMoneda(String.valueOf(TipoMoneda.PESOS));

        // Creo el ClienteDto y Cliente para cuentaOrigen
        ClienteDto clienteDtoOrigen = new ClienteDto();
        clienteDtoOrigen.setBanco("Banco A");
        clienteDtoOrigen.setFechaNacimiento("2005-03-03");
        Cliente clienteOrigen = new Cliente(clienteDtoOrigen);

        // Creo el ClienteDto y Cliente para cuentaDestino
        ClienteDto clienteDtoDestino = new ClienteDto();
        clienteDtoDestino.setBanco("Banco B"); // Bancos diferentes
        clienteDtoDestino.setFechaNacimiento("2005-03-03");
        Cliente clienteDestino = new Cliente(clienteDtoDestino);

        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setNumeroCuenta(123456789L);
        cuentaOrigen.setMoneda(TipoMoneda.PESOS);
        cuentaOrigen.setBalance(500.0);
        cuentaOrigen.setTitular(clienteOrigen);

        Cuenta cuentaDestino = new Cuenta();
        cuentaDestino.setNumeroCuenta(987654321L);
        cuentaDestino.setMoneda(TipoMoneda.PESOS);
        cuentaDestino.setTitular(clienteDestino);

        when(cuentaDao.findByNumeroCuenta(123456789L)).thenReturn(cuentaOrigen);
        when(cuentaDao.findByNumeroCuenta(987654321L)).thenReturn(cuentaDestino);
        when(banelcoService.servicioDeBanelco(transferenciaDto)).thenReturn(false);

        RespuestaTransaccionDto respuesta = transferenciaService.realizarTransferencia(transferenciaDto);

        assertEquals("FALLIDA", respuesta.getEstado());
        assertEquals("No es posible realizar la transferencia, los bancos son diferentes.", respuesta.getMensaje());
    }

}
