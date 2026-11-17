
package dao;

import modelo.Gestion;

import java.time.LocalDate;
import java.util.List;

public interface GestionDAO {

    void guardar(Gestion gestion);

    Gestion buscarPorId(Long id);

    List<Gestion> listarTodas();

    List<Gestion> buscarPorProveedor(String codigoProveedor);

    List<Gestion> buscarPorPieza(String codigoPieza);

    List<Gestion> buscarPorProyecto(String codigoProyecto);

    // Búsqueda avanzada con filtros opcionales
    List<Gestion> buscarAvanzado(
            String codigoProveedor,
            String codigoPieza,
            String codigoProyecto,
            LocalDate fechaDesde,
            LocalDate fechaHasta,
            Integer cantidadMinima,
            Integer cantidadMaxima
    );

    // Calcula el coste total agrupado por proyecto
    List<Object[]> calcularCostesPorProyecto();

    // Calcula la cantidad total de unidades suministradas
    Long calcularCantidadTotalSuministrada();

    // Devuelve los proveedores ordenados por cantidad suministrada
    List<Object[]> obtenerProveedoresMasActivos();

    // Devuelve las piezas ordenadas por cantidad utilizada
    List<Object[]> obtenerPiezasMasUtilizadas();

    void actualizar(Gestion gestion);

    void eliminar(Long id);
}
