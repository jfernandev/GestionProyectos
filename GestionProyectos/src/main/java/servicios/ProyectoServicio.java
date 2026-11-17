package servicios;

import dao.ProyectoDAO;
import dao.ProyectoDAOImpl;
import modelo.Proyecto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ProyectoServicio {

    // Logger encargado de registrar las operaciones y los errores
    private static final Logger logger =
            LoggerFactory.getLogger(ProyectoServicio.class);

    // DAO encargado de comunicarse con la base de datos
    private final ProyectoDAO proyectoDAO;

    // Constructor que inicializa el DAO
    public ProyectoServicio() {
        this.proyectoDAO = new ProyectoDAOImpl();
    }

    // Constructor alternativo para facilitar las pruebas
    public ProyectoServicio(ProyectoDAO proyectoDAO) {
        this.proyectoDAO = proyectoDAO;
    }

    // Registra un proyecto después de validar sus datos
    public void registrar(Proyecto proyecto) {

        try {
            validarProyecto(proyecto);

            // Comprobamos que no exista otro proyecto con el mismo código
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

    // Busca un proyecto por su código
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

    // Busca proyectos por su nombre
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

    // Busca proyectos por ciudad
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

    // Devuelve todos los proyectos registrados
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

    // Actualiza un proyecto existente
    public void actualizar(Proyecto proyecto) {

        try {
            validarProyecto(proyecto);

            // Comprobamos que el proyecto exista
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

    // Elimina un proyecto por su código
    public void eliminar(String codigo) {

        try {
            validarTexto(codigo, "El código");

            // Comprobamos que el proyecto exista
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

    // Valida todos los campos obligatorios del proyecto
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

    // Valida un campo obligatorio y su longitud máxima
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

    // Comprueba que un texto de búsqueda no esté vacío
    private void validarTexto(String valor, String campo) {

        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    campo + " no puede estar vacío."
            );
        }
    }
}