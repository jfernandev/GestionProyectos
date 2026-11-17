package dao;

import modelo.Proyecto;

import java.util.List;

public interface ProyectoDAO {

    void guardar(Proyecto proyecto);

    Proyecto buscarPorCodigo(String codigo);

    List<Proyecto> buscarPorNombre(String nombre);

    List<Proyecto> buscarPorCiudad(String ciudad);

    List<Proyecto> listarTodos();

    void actualizar(Proyecto proyecto);

    void eliminar(String codigo);
}
