package servicios;

import dao.PiezaDAO;
import modelo.Pieza;
import modelo.Usuario;
import utilidades.SesionUsuario;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PiezaServicioTest {

    private PiezaDAO piezaDAO;

    private PiezaServicio piezaServicio;

    @BeforeEach
    void setUp() {

        piezaDAO = mock(PiezaDAO.class);

        Usuario administrador = new Usuario(
                "admin",
                "hash_simulado",
                "ADMIN"
        );

        SesionUsuario.establecerUsuario(administrador);

        piezaServicio = new PiezaServicio(piezaDAO);
    }

    @AfterEach
    void tearDown() {

        SesionUsuario.cerrarSesion();
    }

    private Pieza crearPieza() {

        Pieza pieza = new Pieza();

        pieza.setCodigo("PIEZA001");
        pieza.setNombre("Tornillo M8");
        pieza.setPrecio(new BigDecimal("0.25"));
        pieza.setDescripcion("Tornillo de acero inoxidable");

        return pieza;
    }

    @Test
    void registrarPiezaNula() {

        assertThrows(
                IllegalArgumentException.class,
                () -> piezaServicio.registrar(null)
        );

        verify(piezaDAO, never()).guardar(any());
    }

    @Test
    void registrarPiezaSinCodigo() {

        Pieza pieza = crearPieza();
        pieza.setCodigo("");

        assertThrows(
                IllegalArgumentException.class,
                () -> piezaServicio.registrar(pieza)
        );

        verify(piezaDAO, never()).guardar(any());
    }

    @Test
    void registrarPiezaSinPrecio() {

        Pieza pieza = crearPieza();
        pieza.setPrecio(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> piezaServicio.registrar(pieza)
        );

        verify(piezaDAO, never()).guardar(any());
    }

    @Test
    void registrarPiezaConPrecioCero() {

        Pieza pieza = crearPieza();
        pieza.setPrecio(BigDecimal.ZERO);

        assertThrows(
                IllegalArgumentException.class,
                () -> piezaServicio.registrar(pieza)
        );

        verify(piezaDAO, never()).guardar(any());
    }

    @Test
    void registrarPiezaConPrecioNegativo() {

        Pieza pieza = crearPieza();
        pieza.setPrecio(new BigDecimal("-5.00"));

        assertThrows(
                IllegalArgumentException.class,
                () -> piezaServicio.registrar(pieza)
        );

        verify(piezaDAO, never()).guardar(any());
    }

    @Test
    void registrarPiezaDuplicada() {

        Pieza pieza = crearPieza();

        when(piezaDAO.buscarPorCodigo("PIEZA001"))
                .thenReturn(pieza);

        assertThrows(
                IllegalArgumentException.class,
                () -> piezaServicio.registrar(pieza)
        );

        verify(piezaDAO, never()).guardar(any());
    }

    @Test
    void registrarPiezaCorrectamente() {

        Pieza pieza = crearPieza();

        when(piezaDAO.buscarPorCodigo("PIEZA001"))
                .thenReturn(null);

        assertDoesNotThrow(
                () -> piezaServicio.registrar(pieza)
        );

        verify(piezaDAO, times(1)).guardar(pieza);
    }

    @Test
    void registrarPiezaSinDescripcion() {

        Pieza pieza = crearPieza();
        pieza.setDescripcion(null);

        when(piezaDAO.buscarPorCodigo("PIEZA001"))
                .thenReturn(null);

        assertDoesNotThrow(
                () -> piezaServicio.registrar(pieza)
        );

        verify(piezaDAO, times(1)).guardar(pieza);
    }

    @Test
    void registrarPiezaConDescripcionDemasiadoLarga() {

        Pieza pieza = crearPieza();
        pieza.setDescripcion("A".repeat(501));

        assertThrows(
                IllegalArgumentException.class,
                () -> piezaServicio.registrar(pieza)
        );

        verify(piezaDAO, never()).guardar(any());
    }

    @Test
    void buscarPiezaPorCodigo() {

        Pieza pieza = crearPieza();

        when(piezaDAO.buscarPorCodigo("PIEZA001"))
                .thenReturn(pieza);

        Pieza resultado = piezaServicio.buscarPorCodigo("PIEZA001");

        assertNotNull(resultado);
        assertEquals("PIEZA001", resultado.getCodigo());

        verify(piezaDAO, times(1)).buscarPorCodigo("PIEZA001");
    }

    @Test
    void listarTodasLasPiezas() {

        List<Pieza> piezas = List.of(crearPieza());

        when(piezaDAO.listarTodas())
                .thenReturn(piezas);

        List<Pieza> resultado = piezaServicio.listarTodas();

        assertEquals(1, resultado.size());

        verify(piezaDAO, times(1)).listarTodas();
    }
}
