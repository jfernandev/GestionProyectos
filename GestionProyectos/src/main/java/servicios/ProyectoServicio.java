
package servicios;

import dao.ProyectoDAO;
import dao.ProyectoDAOImpl;
import modelo.Proyecto;

import java.util.List;

public class ProyectoServicio {

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

        validarProyecto(proyecto);

        // Comprobamos que no exista otro proyecto con el mismo código
        if (proyectoDAO.buscarPorCodigo(proyecto.getCodigo()) != null) {
            throw new IllegalArgumentException(
                    "Ya existe un proyecto con ese código."
            );
        }

        proyectoDAO.guardar(proyecto);
    }

    // Busca un proyecto por su código
    public Proyecto buscarPorCodigo(String codigo) {

        validarTexto(codigo, "El código");

        return proyectoDAO.buscarPorCodigo(codigo.trim());
    }

    // Busca proyectos por su nombre
    public List<Proyecto> buscarPorNombre(String nombre) {

        validarTexto(nombre, "El nombre");

        return proyectoDAO.buscarPorNombre(nombre.trim());
    }

    // Busca proyectos por ciudad
    public List<Proyecto> buscarPorCiudad(String ciudad) {

        validarTexto(ciudad, "La ciudad");

        return proyectoDAO.buscarPorCiudad(ciudad.trim());
    }

    // Devuelve todos los proyectos registrados
    public List<Proyecto> listarTodos() {
        return proyectoDAO.listarTodos();
    }

    // Actualiza un proyecto existente
    public void actualizar(Proyecto proyecto) {

        validarProyecto(proyecto);

        // Comprobamos que el proyecto exista
        if (proyectoDAO.buscarPorCodigo(proyecto.getCodigo()) == null) {
            throw new IllegalArgumentException(
                    "No existe ningún proyecto con ese código."
            );
        }

        proyectoDAO.actualizar(proyecto);
    }

    // Elimina un proyecto por su código
    public void eliminar(String codigo) {

        validarTexto(codigo, "El código");

        // Comprobamos que el proyecto exista
        if (proyectoDAO.buscarPorCodigo(codigo.trim()) == null) {
            throw new IllegalArgumentException(
                    "No existe ningún proyecto con ese código."
            );
        }

        proyectoDAO.eliminar(codigo.trim());
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
