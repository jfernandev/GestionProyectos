package servicios;

import dao.ProyectoDAO;
import modelo.Proyecto;
import modelo.Usuario;
import utilidades.SesionUsuario;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProyectoServicioTest {

    private ProyectoDAO proyectoDAO;

    private ProyectoServicio proyectoServicio;

    @BeforeEach
    void setUp() {

        proyectoDAO = mock(ProyectoDAO.class);

        Usuario administrador = new Usuario(
                "admin",
                "hash_simulado",
                "ADMIN"
        );

        SesionUsuario.establecerUsuario(administrador);

        proyectoServicio = new ProyectoServicio(proyectoDAO);
    }

    @AfterEach
    void tearDown() {

        SesionUsuario.cerrarSesion();
    }

    private Proyecto crearProyecto() {

        Proyecto proyecto = new Proyecto();

        proyecto.setCodigo("PROY001");
        proyecto.setNombre("Construcción edificio");
        proyecto.setCiudad("Vitoria-Gasteiz");

        return proyecto;
    }

    @Test
    void registrarProyectoNulo() {

        assertThrows(
                IllegalArgumentException.class,
                () -> proyectoServicio.registrar(null)
        );

        verify(proyectoDAO, never()).guardar(any());
    }

    @Test
    void registrarProyectoSinCodigo() {

        Proyecto proyecto = crearProyecto();
        proyecto.setCodigo("");

        assertThrows(
                IllegalArgumentException.class,
                () -> proyectoServicio.registrar(proyecto)
        );

        verify(proyectoDAO, never()).guardar(any());
    }

    @Test
    void registrarProyectoSinNombre() {

        Proyecto proyecto = crearProyecto();
        proyecto.setNombre("");

        assertThrows(
                IllegalArgumentException.class,
                () -> proyectoServicio.registrar(proyecto)
        );

        verify(proyectoDAO, never()).guardar(any());
    }

    @Test
    void registrarProyectoSinCiudad() {

        Proyecto proyecto = crearProyecto();
        proyecto.setCiudad("");

        assertThrows(
                IllegalArgumentException.class,
                () -> proyectoServicio.registrar(proyecto)
        );

        verify(proyectoDAO, never()).guardar(any());
    }

    @Test
    void registrarProyectoDuplicado() {

        Proyecto proyecto = crearProyecto();

        when(proyectoDAO.buscarPorCodigo("PROY001"))
                .thenReturn(proyecto);

        assertThrows(
                IllegalArgumentException.class,
                () -> proyectoServicio.registrar(proyecto)
        );

        verify(proyectoDAO, never()).guardar(any());
    }

    @Test
    void registrarProyectoCorrectamente() {

        Proyecto proyecto = crearProyecto();

        when(proyectoDAO.buscarPorCodigo("PROY001"))
                .thenReturn(null);

        assertDoesNotThrow(
                () -> proyectoServicio.registrar(proyecto)
        );

        verify(proyectoDAO, times(1)).guardar(proyecto);
    }

    @Test
    void buscarProyectoPorCodigo() {

        Proyecto proyecto = crearProyecto();

        when(proyectoDAO.buscarPorCodigo("PROY001"))
                .thenReturn(proyecto);

        Proyecto resultado =
                proyectoServicio.buscarPorCodigo("PROY001");

        assertNotNull(resultado);
        assertEquals("PROY001", resultado.getCodigo());

        verify(proyectoDAO, times(1)).buscarPorCodigo("PROY001");
    }

    @Test
    void buscarProyectoPorNombre() {

        List<Proyecto> proyectos = List.of(crearProyecto());

        when(proyectoDAO.buscarPorNombre("Construcción edificio"))
                .thenReturn(proyectos);

        List<Proyecto> resultado =
                proyectoServicio.buscarPorNombre("Construcción edificio");

        assertEquals(1, resultado.size());

        verify(proyectoDAO, times(1))
                .buscarPorNombre("Construcción edificio");
    }

    @Test
    void buscarProyectoPorCiudad() {

        List<Proyecto> proyectos = List.of(crearProyecto());

        when(proyectoDAO.buscarPorCiudad("Vitoria-Gasteiz"))
                .thenReturn(proyectos);

        List<Proyecto> resultado =
                proyectoServicio.buscarPorCiudad("Vitoria-Gasteiz");

        assertEquals(1, resultado.size());

        verify(proyectoDAO, times(1))
                .buscarPorCiudad("Vitoria-Gasteiz");
    }

    @Test
    void listarTodosLosProyectos() {

        List<Proyecto> proyectos = List.of(crearProyecto());

        when(proyectoDAO.listarTodos())
                .thenReturn(proyectos);

        List<Proyecto> resultado = proyectoServicio.listarTodos();

        assertEquals(1, resultado.size());

        verify(proyectoDAO, times(1)).listarTodos();
    }

    @Test
    void actualizarProyectoInexistente() {

        Proyecto proyecto = crearProyecto();

        when(proyectoDAO.buscarPorCodigo("PROY001"))
                .thenReturn(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> proyectoServicio.actualizar(proyecto)
        );

        verify(proyectoDAO, never()).actualizar(any());
    }

    @Test
    void actualizarProyectoCorrectamente() {

        Proyecto proyecto = crearProyecto();

        when(proyectoDAO.buscarPorCodigo("PROY001"))
                .thenReturn(proyecto);

        assertDoesNotThrow(
                () -> proyectoServicio.actualizar(proyecto)
        );

        verify(proyectoDAO, times(1)).actualizar(proyecto);
    }

    @Test
    void eliminarProyectoInexistente() {

        when(proyectoDAO.buscarPorCodigo("PROY999"))
                .thenReturn(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> proyectoServicio.eliminar("PROY999")
        );

        verify(proyectoDAO, never()).eliminar(anyString());
    }

    @Test
    void eliminarProyectoCorrectamente() {

        Proyecto proyecto = crearProyecto();

        when(proyectoDAO.buscarPorCodigo("PROY001"))
                .thenReturn(proyecto);

        assertDoesNotThrow(
                () -> proyectoServicio.eliminar("PROY001")
        );

        verify(proyectoDAO, times(1)).eliminar("PROY001");
    }
}
