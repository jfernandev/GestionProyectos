package servicios;

import dao.GestionDAO;
import dao.ProveedorDAO;
import dao.PiezaDAO;
import dao.ProyectoDAO;

import modelo.Gestion;
import modelo.Proveedor;
import modelo.Pieza;
import modelo.Proyecto;
import modelo.Usuario;

import utilidades.SesionUsuario;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GestionServicioTest {

    private GestionDAO gestionDAO;
    private ProveedorDAO proveedorDAO;
    private PiezaDAO piezaDAO;
    private ProyectoDAO proyectoDAO;

    private GestionServicio gestionServicio;

    @BeforeEach
    void setUp() {

        gestionDAO = mock(GestionDAO.class);
        proveedorDAO = mock(ProveedorDAO.class);
        piezaDAO = mock(PiezaDAO.class);
        proyectoDAO = mock(ProyectoDAO.class);

        Usuario administrador = new Usuario(
                "admin",
                "hash_simulado",
                "ADMIN"
        );

        SesionUsuario.establecerUsuario(administrador);

        gestionServicio = new GestionServicio(
                gestionDAO,
                proveedorDAO,
                piezaDAO,
                proyectoDAO
        );
    }

    @AfterEach
    void tearDown() {

        SesionUsuario.cerrarSesion();
    }

    private Proveedor crearProveedor() {

        Proveedor proveedor = new Proveedor();

        proveedor.setCodigo("PROV001");
        proveedor.setNombre("Ángel");
        proveedor.setApellidos("Cornés de la Vera");
        proveedor.setDireccion("Calle Baiona 24");

        return proveedor;
    }

    private Pieza crearPieza() {

        Pieza pieza = new Pieza();

        pieza.setCodigo("PIEZA001");
        pieza.setNombre("Tornillo M8");

        return pieza;
    }

    private Proyecto crearProyecto() {

        Proyecto proyecto = new Proyecto();

        proyecto.setCodigo("PROY001");
        proyecto.setNombre("Construcción edificio");
        proyecto.setCiudad("Vitoria-Gasteiz");

        return proyecto;
    }

    private Gestion crearGestion() {

        Gestion gestion = new Gestion();

        gestion.setFecha(LocalDate.now());
        gestion.setCantidad(10);
        gestion.setProveedor(crearProveedor());
        gestion.setPieza(crearPieza());
        gestion.setProyecto(crearProyecto());

        return gestion;
    }

    private void configurarRelacionesExistentes() {

        when(proveedorDAO.buscarPorCodigo("PROV001"))
                .thenReturn(crearProveedor());

        when(piezaDAO.buscarPorCodigo("PIEZA001"))
                .thenReturn(crearPieza());

        when(proyectoDAO.buscarPorCodigo("PROY001"))
                .thenReturn(crearProyecto());
    }

    @Test
    void registrarGestionNula() {

        assertThrows(
                IllegalArgumentException.class,
                () -> gestionServicio.registrar(null)
        );

        verify(gestionDAO, never()).guardar(any());
    }

    @Test
    void registrarGestionSinFecha() {

        Gestion gestion = crearGestion();
        gestion.setFecha(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> gestionServicio.registrar(gestion)
        );

        verify(gestionDAO, never()).guardar(any());
    }

    @Test
    void registrarGestionSinCantidad() {

        Gestion gestion = crearGestion();
        gestion.setCantidad(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> gestionServicio.registrar(gestion)
        );

        verify(gestionDAO, never()).guardar(any());
    }

    @Test
    void registrarGestionConCantidadCero() {

        Gestion gestion = crearGestion();
        gestion.setCantidad(0);

        assertThrows(
                IllegalArgumentException.class,
                () -> gestionServicio.registrar(gestion)
        );

        verify(gestionDAO, never()).guardar(any());
    }

    @Test
    void registrarGestionConCantidadNegativa() {

        Gestion gestion = crearGestion();
        gestion.setCantidad(-5);

        assertThrows(
                IllegalArgumentException.class,
                () -> gestionServicio.registrar(gestion)
        );

        verify(gestionDAO, never()).guardar(any());
    }

    @Test
    void registrarGestionSinProveedor() {

        Gestion gestion = crearGestion();
        gestion.setProveedor(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> gestionServicio.registrar(gestion)
        );

        verify(gestionDAO, never()).guardar(any());
    }

    @Test
    void registrarGestionSinPieza() {

        Gestion gestion = crearGestion();
        gestion.setPieza(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> gestionServicio.registrar(gestion)
        );

        verify(gestionDAO, never()).guardar(any());
    }

    @Test
    void registrarGestionSinProyecto() {

        Gestion gestion = crearGestion();
        gestion.setProyecto(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> gestionServicio.registrar(gestion)
        );

        verify(gestionDAO, never()).guardar(any());
    }

    @Test
    void registrarGestionConProveedorInexistente() {

        Gestion gestion = crearGestion();

        when(proveedorDAO.buscarPorCodigo("PROV001"))
                .thenReturn(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> gestionServicio.registrar(gestion)
        );

        verify(gestionDAO, never()).guardar(any());
    }

    @Test
    void registrarGestionCorrectamente() {

        Gestion gestion = crearGestion();

        configurarRelacionesExistentes();

        assertDoesNotThrow(
                () -> gestionServicio.registrar(gestion)
        );

        verify(gestionDAO, times(1)).guardar(gestion);
    }

    @Test
    void listarTodasLasGestiones() {

        List<Gestion> gestiones = List.of(crearGestion());

        when(gestionDAO.listarTodas())
                .thenReturn(gestiones);

        List<Gestion> resultado = gestionServicio.listarTodas();

        assertEquals(1, resultado.size());

        verify(gestionDAO, times(1)).listarTodas();
    }

    @Test
    void buscarGestionPorId() {

        Gestion gestion = crearGestion();

        when(gestionDAO.buscarPorId(1L))
                .thenReturn(gestion);

        Gestion resultado = gestionServicio.buscarPorId(1L);

        assertNotNull(resultado);

        verify(gestionDAO, times(1)).buscarPorId(1L);
    }

    @Test
    void buscarAvanzadoCorrectamente() {

        List<Gestion> gestiones = List.of(crearGestion());

        LocalDate fechaDesde = LocalDate.of(2026, 1, 1);
        LocalDate fechaHasta = LocalDate.of(2026, 12, 31);

        when(gestionDAO.buscarAvanzado(
                "PROV001",
                "PIEZA001",
                "PROY001",
                fechaDesde,
                fechaHasta,
                5,
                100
        )).thenReturn(gestiones);

        List<Gestion> resultado = gestionServicio.buscarAvanzado(
                "PROV001",
                "PIEZA001",
                "PROY001",
                fechaDesde,
                fechaHasta,
                5,
                100
        );

        assertEquals(1, resultado.size());

        verify(gestionDAO, times(1)).buscarAvanzado(
                "PROV001",
                "PIEZA001",
                "PROY001",
                fechaDesde,
                fechaHasta,
                5,
                100
        );
    }

    @Test
    void buscarAvanzadoConFechasIncorrectas() {

        LocalDate fechaDesde = LocalDate.of(2026, 12, 31);
        LocalDate fechaHasta = LocalDate.of(2026, 1, 1);

        assertThrows(
                IllegalArgumentException.class,
                () -> gestionServicio.buscarAvanzado(
                        null,
                        null,
                        null,
                        fechaDesde,
                        fechaHasta,
                        null,
                        null
                )
        );

        verifyNoInteractions(gestionDAO);
    }

    @Test
    void buscarAvanzadoConCantidadesIncorrectas() {

        assertThrows(
                IllegalArgumentException.class,
                () -> gestionServicio.buscarAvanzado(
                        null,
                        null,
                        null,
                        null,
                        null,
                        100,
                        10
                )
        );

        verifyNoInteractions(gestionDAO);
    }

    @Test
    void buscarAvanzadoConCantidadMinimaNoValida() {

        assertThrows(
                IllegalArgumentException.class,
                () -> gestionServicio.buscarAvanzado(
                        null,
                        null,
                        null,
                        null,
                        null,
                        0,
                        null
                )
        );

        verifyNoInteractions(gestionDAO);
    }

    @Test
    void buscarAvanzadoConCantidadMaximaNoValida() {

        assertThrows(
                IllegalArgumentException.class,
                () -> gestionServicio.buscarAvanzado(
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        -5
                )
        );

        verifyNoInteractions(gestionDAO);
    }
}