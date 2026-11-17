package servicios;

import dao.ProveedorDAO;
import modelo.Proveedor;
import modelo.Usuario;
import utilidades.SesionUsuario;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProveedorServicioTest {

    private ProveedorDAO proveedorDAO;

    private ProveedorServicio proveedorServicio;

    @BeforeEach
    void setUp() {

        proveedorDAO = mock(ProveedorDAO.class);

        Usuario administrador = new Usuario(
                "admin",
                "hash_simulado",
                "ADMIN"
        );

        SesionUsuario.establecerUsuario(administrador);

        proveedorServicio = new ProveedorServicio(proveedorDAO);
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

    @Test
    void registrarProveedorNulo() {

        assertThrows(
                IllegalArgumentException.class,
                () -> proveedorServicio.registrar(null)
        );

        verify(proveedorDAO, never()).guardar(any());
    }

    @Test
    void registrarProveedorSinCodigo() {

        Proveedor proveedor = crearProveedor();
        proveedor.setCodigo("");

        assertThrows(
                IllegalArgumentException.class,
                () -> proveedorServicio.registrar(proveedor)
        );

        verify(proveedorDAO, never()).guardar(any());
    }

    @Test
    void registrarProveedorDuplicado() {

        Proveedor proveedor = crearProveedor();

        when(proveedorDAO.buscarPorCodigo("PROV001"))
                .thenReturn(proveedor);

        assertThrows(
                IllegalArgumentException.class,
                () -> proveedorServicio.registrar(proveedor)
        );

        verify(proveedorDAO, never()).guardar(any());
    }

    @Test
    void registrarProveedorCorrectamente() {

        Proveedor proveedor = crearProveedor();

        when(proveedorDAO.buscarPorCodigo("PROV001"))
                .thenReturn(null);

        assertDoesNotThrow(
                () -> proveedorServicio.registrar(proveedor)
        );

        verify(proveedorDAO, times(1)).guardar(proveedor);
    }

    @Test
    void buscarProveedorConCodigoVacio() {

        assertThrows(
                IllegalArgumentException.class,
                () -> proveedorServicio.buscarPorCodigo("")
        );

        verify(proveedorDAO, never()).buscarPorCodigo(anyString());
    }

    @Test
    void actualizarProveedorInexistente() {

        Proveedor proveedor = crearProveedor();

        when(proveedorDAO.buscarPorCodigo("PROV001"))
                .thenReturn(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> proveedorServicio.actualizar(proveedor)
        );

        verify(proveedorDAO, never()).actualizar(any());
    }

    @Test
    void actualizarProveedorCorrectamente() {

        Proveedor proveedor = crearProveedor();

        when(proveedorDAO.buscarPorCodigo("PROV001"))
                .thenReturn(proveedor);

        assertDoesNotThrow(
                () -> proveedorServicio.actualizar(proveedor)
        );

        verify(proveedorDAO, times(1)).actualizar(proveedor);
    }

    @Test
    void eliminarProveedorInexistente() {

        when(proveedorDAO.buscarPorCodigo("PROV999"))
                .thenReturn(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> proveedorServicio.eliminar("PROV999")
        );

        verify(proveedorDAO, never()).eliminar(anyString());
    }

    @Test
    void eliminarProveedorCorrectamente() {

        Proveedor proveedor = crearProveedor();

        when(proveedorDAO.buscarPorCodigo("PROV001"))
                .thenReturn(proveedor);

        assertDoesNotThrow(
                () -> proveedorServicio.eliminar("PROV001")
        );

        verify(proveedorDAO, times(1)).eliminar("PROV001");
    }

    @Test
    void buscarProveedorPorNombre() {

        List<Proveedor> proveedores = List.of(crearProveedor());

        when(proveedorDAO.buscarPorNombre("Ángel"))
                .thenReturn(proveedores);

        List<Proveedor> resultado =
                proveedorServicio.buscarPorNombre("Ángel");

        assertEquals(1, resultado.size());
        assertEquals("Ángel", resultado.get(0).getNombre());

        verify(proveedorDAO, times(1)).buscarPorNombre("Ángel");
    }

    @Test
    void listarTodosLosProveedores() {

        List<Proveedor> proveedores = List.of(crearProveedor());

        when(proveedorDAO.listarTodos())
                .thenReturn(proveedores);

        List<Proveedor> resultado = proveedorServicio.listarTodos();

        assertEquals(1, resultado.size());

        verify(proveedorDAO, times(1)).listarTodos();
    }
}
