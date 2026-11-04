
package dao;

import modelo.Proyecto;

import java.util.List;

public interface ProyectoDAO {

    // Guarda un proyecto nuevo
    void guardar(Proyecto proyecto);

    // Busca un proyecto por su código
    Proyecto buscarPorCodigo(String codigo);

    // Busca proyectos por nombre
    List<Proyecto> buscarPorNombre(String nombre);

    // Busca proyectos por ciudad
    List<Proyecto> buscarPorCiudad(String ciudad);

    // Devuelve todos los proyectos registrados
    List<Proyecto> listarTodos();

    // Actualiza los datos de un proyecto
    void actualizar(Proyecto proyecto);

    // Elimina un proyecto por su código
    void eliminar(String codigo);
}
