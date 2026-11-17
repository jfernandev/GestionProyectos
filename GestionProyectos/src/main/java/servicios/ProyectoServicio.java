package servicios;

import dao.ProyectoDAO;
import dao.ProyectoDAOImpl;
import modelo.Proyecto;
import utilidades.SesionUsuario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ProyectoServicio {

    private static final Logger logger =
            LoggerFactory.getLogger(ProyectoServicio.class);

    private final ProyectoDAO proyectoDAO;

    public ProyectoServicio() {
        this.proyectoDAO = new ProyectoDAOImpl();
    }

    // Constructor alternativo para facilitar las pruebas
    public ProyectoServicio(ProyectoDAO proyectoDAO) {
        this.proyectoDAO = proyectoDAO;
    }

    public void registrar(Proyecto proyecto) {

        SesionUsuario.exigirAdministrador();

        try {
            validarProyecto(proyecto);

            if (proyectoDAO.buscarPorCodigo(proyecto.getCodigo()) != null) {
                throw new IllegalArgumentException(
                        "Ya existe un proyecto con ese código."
                );
            }

            proyectoDAO.guardar(proyecto);

            logger.info("Proyecto registrado correctamente. Código: {}",
                    proyecto.getCodigo());

        } catch (RuntimeException e) {
            logger.error("Error al registrar el proyecto.", e);
            throw e;
        }
    }

    public Proyecto buscarPorCodigo(String codigo) {

        try {
            validarTexto(codigo, "El código");

            Proyecto proyecto =
                    proyectoDAO.buscarPorCodigo(codigo.trim());

            logger.info("Búsqueda de proyecto por código: {}", codigo.trim());

            return proyecto;

        } catch (RuntimeException e) {
            logger.error("Error al buscar el proyecto por código: {}", codigo, e);
            throw e;
        }
    }

    public List<Proyecto> buscarPorNombre(String nombre) {

        try {
            validarTexto(nombre, "El nombre");

            List<Proyecto> proyectos =
                    proyectoDAO.buscarPorNombre(nombre.trim());

            logger.info("Búsqueda de proyectos por nombre realizada. Resultados: {}",
                    proyectos.size());

            return proyectos;

        } catch (RuntimeException e) {
            logger.error("Error al buscar proyectos por nombre.", e);
            throw e;
        }
    }

    public List<Proyecto> buscarPorCiudad(String ciudad) {

        try {
            validarTexto(ciudad, "La ciudad");

            List<Proyecto> proyectos =
                    proyectoDAO.buscarPorCiudad(ciudad.trim());

            logger.info("Búsqueda de proyectos por ciudad realizada. Resultados: {}",
                    proyectos.size());

            return proyectos;

        } catch (RuntimeException e) {
            logger.error("Error al buscar proyectos por ciudad.", e);
            throw e;
        }
    }

    public List<Proyecto> listarTodos() {

        try {
            List<Proyecto> proyectos = proyectoDAO.listarTodos();

            logger.info("Listado de proyectos realizado. Total: {}",
                    proyectos.size());

            return proyectos;

        } catch (RuntimeException e) {
            logger.error("Error al listar los proyectos.", e);
            throw e;
        }
    }

    public void actualizar(Proyecto proyecto) {

        SesionUsuario.exigirAdministrador();

        try {
            validarProyecto(proyecto);

            if (proyectoDAO.buscarPorCodigo(proyecto.getCodigo()) == null) {
                throw new IllegalArgumentException(
                        "No existe ningún proyecto con ese código."
                );
            }

            proyectoDAO.actualizar(proyecto);

            logger.info("Proyecto actualizado correctamente. Código: {}",
                    proyecto.getCodigo());

        } catch (RuntimeException e) {
            logger.error("Error al actualizar el proyecto.", e);
            throw e;
        }
    }

    public void eliminar(String codigo) {

        SesionUsuario.exigirAdministrador();

        try {
            validarTexto(codigo, "El código");

            if (proyectoDAO.buscarPorCodigo(codigo.trim()) == null) {
                throw new IllegalArgumentException(
                        "No existe ningún proyecto con ese código."
                );
            }

            proyectoDAO.eliminar(codigo.trim());

            logger.info("Proyecto eliminado correctamente. Código: {}",
                    codigo.trim());

        } catch (RuntimeException e) {
            logger.error("Error al eliminar el proyecto. Código: {}", codigo, e);
            throw e;
        }
    }

    private void validarProyecto(Proyecto proyecto) {

        if (proyecto == null) {
            throw new IllegalArgumentException(
                    "El proyecto no puede ser nulo."
            );
        }

        validarCampo(proyecto.getCodigo(), "El código", 20);
        validarCampo(proyecto.getNombre(), "El nombre", 100);
        validarCampo(proyecto.getCiudad(), "La ciudad", 100);
    }

    private void validarCampo(String valor, String campo, int longitudMaxima) {

        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    campo + " es obligatorio."
            );
        }

        if (valor.trim().length() > longitudMaxima) {
            throw new IllegalArgumentException(
                    campo + " no puede superar los " +
                            longitudMaxima + " caracteres."
            );
        }
    }

    private void validarTexto(String valor, String campo) {

        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    campo + " no puede estar vacío."
            );
        }
    }
}
