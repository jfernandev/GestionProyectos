
package dao;

import modelo.Gestion;

import java.util.List;

public interface GestionDAO {

    // Guarda una nueva gestión
    void guardar(Gestion gestion);

    // Busca una gestión por su identificador
    Gestion buscarPorId(Long id);

    // Devuelve todas las gestiones registradas
    List<Gestion> listarTodas();

    // Busca las gestiones realizadas por un proveedor
    List<Gestion> buscarPorProveedor(String codigoProveedor);

    // Busca las gestiones correspondientes a una pieza
    List<Gestion> buscarPorPieza(String codigoPieza);

    // Busca las gestiones correspondientes a un proyecto
    List<Gestion> buscarPorProyecto(String codigoProyecto);

    // Actualiza una gestión existente
    void actualizar(Gestion gestion);

    // Elimina una gestión por su identificador
    void eliminar(Long id);
}
