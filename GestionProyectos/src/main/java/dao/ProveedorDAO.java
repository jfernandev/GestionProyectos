package dao;

import modelo.Proveedor;

import java.util.List;

public interface ProveedorDAO {

    void guardar(Proveedor proveedor);

    Proveedor buscarPorCodigo(String codigo);

    List<Proveedor> buscarPorNombre(String nombre);

    List<Proveedor> buscarPorDireccion(String direccion);

    List<Proveedor> listarTodos();

    void actualizar(Proveedor proveedor);

    void eliminar(String codigo);
}
