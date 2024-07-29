package ar.edu.utn.frbb.tup.proyectoFinal;

import static java.nio.file.Paths.get;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ar.edu.utn.frbb.tup.proyectoFinal.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.proyectoFinal.model.Cliente;
import ar.edu.utn.frbb.tup.proyectoFinal.model.TipoPersona;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.ClienteAlreadyExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.ClienteDoesntExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.InputErrorException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.NotPosibleException;
import ar.edu.utn.frbb.tup.proyectoFinal.persistencia.ClienteDao;
import ar.edu.utn.frbb.tup.proyectoFinal.service.ClienteService;
import ar.edu.utn.frbb.tup.proyectoFinal.service.CuentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

public class ClienteServiceTest {
    @Mock
    private ClienteDao clienteDao;

    @Mock
    private CuentaService cuentaService;

    @InjectMocks
    private ClienteService clienteService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//POST

    @Test
    public void testDarDeAltaCliente_Success() throws Exception, ClienteDoesntExistException, NotPosibleException {
        //Creo un cliente
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setDni("45889159");
        clienteDto.setNombre("Bruno Martin");
        clienteDto.setApellido("Dahua");
        clienteDto.setFechaNacimiento("2005-01-03");
        clienteDto.setTipoPersona(String.valueOf(TipoPersona.PERSONA_FISICA));
        clienteDto.setBanco("Provincia");

        //Lo busco. No lo encuentro, retorno null.
        when(clienteDao.find("45889159L")).thenReturn(null);

        //Simulo el retorno de la llamada a la base de datos.
        clienteService.darDeAltaCliente(clienteDto);

        //Verifico que se haya llamado una vez.
        verify(clienteDao, times(1)).save(any(Cliente.class));
    }

    @Test
    public void testDarDeAltaCliente_ClienteExists() throws ClienteDoesntExistException {
        // Creo un cliente
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setDni("45889159");
        clienteDto.setNombre("Bruno Martin");
        clienteDto.setApellido("Dahua");
        clienteDto.setFechaNacimiento("2005-01-03");
        clienteDto.setTipoPersona(String.valueOf(TipoPersona.PERSONA_FISICA));
        clienteDto.setBanco("Provincia");

        // Lo busco. Lo encuentro, retorno el cliente.
        when(clienteDao.find("45889159L")).thenReturn(new Cliente());

        assertThrows(NotPosibleException.class, () -> clienteService.darDeAltaCliente(clienteDto));
    }

    @Test
    public void testDarDeAltaCliente_UnderageClient() throws ClienteDoesntExistException {
        //Creo un cliente.
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setDni("45889159");
        clienteDto.setNombre("Bruno Martin");
        clienteDto.setApellido("Dahua");
        clienteDto.setFechaNacimiento("2023-01-03");
        clienteDto.setTipoPersona(String.valueOf(TipoPersona.PERSONA_FISICA));
        clienteDto.setBanco("Provincia");

        //Lo busco. No lo encuentro, retorno null.
        when(clienteDao.find("45889159L")).thenReturn(null);

        assertThrows(NotPosibleException.class, () -> clienteService.darDeAltaCliente(clienteDto));
    }

//GET

    @Test
    void testBuscarClientePorDni_ClienteExiste() throws ClienteDoesntExistException {
        String dni = "45889159";
        Cliente cliente = new Cliente();
        cliente.setDni(dni);
        when(clienteDao.find(dni)).thenReturn(cliente);

        Cliente result = clienteService.buscarClientePorDni(dni);

        assertNotNull(result);
        assertEquals(Long.parseLong(dni), result.getDni());
    }

    @Test
    void testBuscarClientePorDni_ClienteNoExiste() throws ClienteDoesntExistException {
        String dni = "45889159L";
        when(clienteDao.find(dni)).thenReturn(null);

        ClienteDoesntExistException thrown = assertThrows(ClienteDoesntExistException.class, () -> {
            clienteService.buscarClientePorDni(dni);
        });
        assertEquals("No existe el cliente con DNI " + dni + ".", thrown.getMessage());
    }

//DELETE

    @Test
    void testEliminarCliente() throws ClienteDoesntExistException {
        String dni = "45889159";
        Cliente clienteExistente = new Cliente();
        clienteExistente.setDni(dni);

        when(clienteDao.find(dni)).thenReturn(clienteExistente);
        when(clienteDao.delete(Long.parseLong(dni))).thenReturn(true);

        clienteService.eliminarCliente(dni);

        verify(clienteDao).find(dni);
        verify(clienteDao).delete(Long.parseLong(dni));
    }

    @Test
    void testEliminarCliente_NoExiste() throws ClienteDoesntExistException {
        String dni = "45889159";

        when(clienteDao.find(dni)).thenReturn(null);

        ClienteDoesntExistException thrown = assertThrows(ClienteDoesntExistException.class, () -> {
            clienteService.eliminarCliente(dni);
        });

        assertEquals("No existe el cliente con DNI " + dni + ".", thrown.getMessage());
    }
}
