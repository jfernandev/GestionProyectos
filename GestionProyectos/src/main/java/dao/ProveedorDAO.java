
package dao;

import modelo.Proveedor;

import java.util.List;

public interface ProveedorDAO {

    // Guarda un proveedor nuevo en la base de datos
    void guardar(Proveedor proveedor);

    // Busca un proveedor por su código exacto
    Proveedor buscarPorCodigo(String codigo);

    // Busca proveedores cuyo nombre contenga el texto indicado
    List<Proveedor> buscarPorNombre(String nombre);

    // Busca proveedores cuya dirección contenga el texto indicado
    List<Proveedor> buscarPorDireccion(String direccion);

    // Devuelve todos los proveedores registrados
    List<Proveedor> listarTodos();

    // Actualiza los datos de un proveedor existente
    void actualizar(Proveedor proveedor);

    // Elimina un proveedor a partir de su código
    void eliminar(String codigo);
}
