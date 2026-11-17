
package servicios;

import modelo.Gestion;
import utilidades.ExportadorCSV;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ExportacionCSVServicio {

    private final GestionServicio gestionServicio;

    // Recibimos el servicio de gestiones mediante el constructor
    public ExportacionCSVServicio(GestionServicio gestionServicio) {
        this.gestionServicio = gestionServicio;
    }

    // Exporta todos los archivos CSV disponibles
    public List<Path> exportarTodo() {

        List<Path> archivos = new ArrayList<>();

        archivos.add(exportarGestiones());
        archivos.add(exportarCantidadTotal());
        archivos.add(exportarProveedoresMasActivos());
        archivos.add(exportarPiezasMasUtilizadas());
        archivos.add(exportarCostesPorProyecto());

        return archivos;
    }

    // Exporta todas las gestiones registradas
    public Path exportarGestiones() {

        List<String[]> filas = new ArrayList<>();

        for (Gestion gestion : gestionServicio.listarTodas()) {

            filas.add(new String[]{
                    String.valueOf(gestion.getId()),
                    String.valueOf(gestion.getFecha()),
                    String.valueOf(gestion.getCantidad()),
                    gestion.getProveedor().getCodigo(),
                    gestion.getProveedor().getNombre(),
                    gestion.getPieza().getCodigo(),
                    gestion.getPieza().getNombre(),
                    gestion.getProyecto().getCodigo(),
                    gestion.getProyecto().getNombre()
            });
        }

        String[] cabeceras = {
                "ID",
                "Fecha",
                "Cantidad",
                "Código proveedor",
                "Proveedor",
                "Código pieza",
                "Pieza",
                "Código proyecto",
                "Proyecto"
        };

        return ExportadorCSV.exportar(
                "gestiones.csv",
                cabeceras,
                filas
        );
    }

    // Exporta la cantidad total suministrada
    public Path exportarCantidadTotal() {

        Long cantidad = gestionServicio.calcularCantidadTotalSuministrada();

        List<String[]> filas = new ArrayList<>();

        filas.add(new String[]{
                String.valueOf(cantidad)
        });

        return ExportadorCSV.exportar(
                "cantidad_total.csv",
                new String[]{"Cantidad total suministrada"},
                filas
        );
    }

    // Exporta los proveedores ordenados por actividad
    public Path exportarProveedoresMasActivos() {

        List<Object[]> resultados =
                gestionServicio.obtenerProveedoresMasActivos();

        List<String[]> filas = new ArrayList<>();

        for (Object[] resultado : resultados) {

            filas.add(new String[]{
                    String.valueOf(resultado[0]),
                    String.valueOf(resultado[1]),
                    String.valueOf(resultado[2])
            });
        }

        return ExportadorCSV.exportar(
                "proveedores_mas_activos.csv",
                new String[]{
                        "Código",
                        "Proveedor",
                        "Cantidad suministrada"
                },
                filas
        );
    }

    // Exporta las piezas ordenadas por cantidad utilizada
    public Path exportarPiezasMasUtilizadas() {

        List<Object[]> resultados =
                gestionServicio.obtenerPiezasMasUtilizadas();

        List<String[]> filas = new ArrayList<>();

        for (Object[] resultado : resultados) {

            filas.add(new String[]{
                    String.valueOf(resultado[0]),
                    String.valueOf(resultado[1]),
                    String.valueOf(resultado[2])
            });
        }

        return ExportadorCSV.exportar(
                "piezas_mas_utilizadas.csv",
                new String[]{
                        "Código",
                        "Pieza",
                        "Cantidad utilizada"
                },
                filas
        );
    }

    // Exporta los costes totales agrupados por proyecto
    public Path exportarCostesPorProyecto() {

        List<Object[]> resultados =
                gestionServicio.calcularCostesPorProyecto();

        List<String[]> filas = new ArrayList<>();

        for (Object[] resultado : resultados) {

            BigDecimal coste = (BigDecimal) resultado[2];

            filas.add(new String[]{
                    String.valueOf(resultado[0]),
                    String.valueOf(resultado[1]),
                    coste != null ? coste.toPlainString() : "0"
            });
        }

        return ExportadorCSV.exportar(
                "costes_por_proyecto.csv",
                new String[]{
                        "Código proyecto",
                        "Proyecto",
                        "Coste total"
                },
                filas
        );
    }
}
