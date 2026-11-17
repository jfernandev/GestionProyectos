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

    List<Gestion> buscarAvanzado(
            String codigoProveedor,
            String codigoPieza,
            String codigoProyecto,
            LocalDate fechaDesde,
            LocalDate fechaHasta,
            Integer cantidadMinima,
            Integer cantidadMaxima
    );

    List<Object[]> calcularCostesPorProyecto();

    Long calcularCantidadTotalSuministrada();

    List<Object[]> obtenerProveedoresMasActivos();

    List<Object[]> obtenerPiezasMasUtilizadas();

    void actualizar(Gestion gestion);

    void eliminar(Long id);
}
