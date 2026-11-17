package menu;

import modelo.Gestion;
import servicios.ExportacionCSVServicio;
import servicios.GestionServicio;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class MenuEstadisticas {

    private final Scanner scanner;
    private final GestionServicio gestionServicio;
    private final ExportacionCSVServicio exportacionServicio;

    public MenuEstadisticas(
            Scanner scanner,
            GestionServicio gestionServicio
    ) {
        this.scanner = scanner;
        this.gestionServicio = gestionServicio;

        this.exportacionServicio =
                new ExportacionCSVServicio(gestionServicio);
    }

    public void mostrarMenu() {

        int opcion;
        boolean salir = false;

        while (!salir) {

            System.out.println("\n===== ESTADÍSTICAS =====");
            System.out.println("1. Cantidad total suministrada");
            System.out.println("2. Proveedores más activos");
            System.out.println("3. Piezas más utilizadas");
            System.out.println("4. Costes por proyecto");
            System.out.println("5. Exportar todos los datos a CSV");
            System.out.println("6. Volver al menú principal");
            System.out.print("Selecciona una opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {

                    case 1:
                        mostrarCantidadTotal();
                        break;

                    case 2:
                        mostrarProveedoresMasActivos();
                        break;

                    case 3:
                        mostrarPiezasMasUtilizadas();
                        break;

                    case 4:
                        mostrarCostesPorProyecto();
                        break;

                    case 5:
                        exportarDatos();
                        break;

                    case 6:
                        salir = true;
                        break;

                    default:
                        System.out.println("Opción no válida.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Debes introducir un número.");
            }
        }
    }

    private void mostrarCantidadTotal() {

        Long cantidad =
                gestionServicio.calcularCantidadTotalSuministrada();

        System.out.println("\nCantidad total suministrada: " + cantidad);
    }

    private void mostrarProveedoresMasActivos() {

        List<Object[]> resultados =
                gestionServicio.obtenerProveedoresMasActivos();

        System.out.println("\n===== PROVEEDORES MÁS ACTIVOS =====");

        if (resultados.isEmpty()) {
            System.out.println("No hay datos disponibles.");
            return;
        }

        for (Object[] resultado : resultados) {

            System.out.println(
                    "Código: " + resultado[0] +
                            " | Proveedor: " + resultado[1] +
                            " | Cantidad suministrada: " + resultado[2]
            );
        }
    }

    private void mostrarPiezasMasUtilizadas() {

        List<Object[]> resultados =
                gestionServicio.obtenerPiezasMasUtilizadas();

        System.out.println("\n===== PIEZAS MÁS UTILIZADAS =====");

        if (resultados.isEmpty()) {
            System.out.println("No hay datos disponibles.");
            return;
        }

        for (Object[] resultado : resultados) {

            System.out.println(
                    "Código: " + resultado[0] +
                            " | Pieza: " + resultado[1] +
                            " | Cantidad utilizada: " + resultado[2]
            );
        }
    }

    private void mostrarCostesPorProyecto() {

        List<Object[]> resultados =
                gestionServicio.calcularCostesPorProyecto();

        System.out.println("\n===== COSTES POR PROYECTO =====");

        if (resultados.isEmpty()) {
            System.out.println("No hay datos disponibles.");
            return;
        }

        NumberFormat formatoMoneda =
                NumberFormat.getCurrencyInstance(
                        new Locale("es", "ES")
                );

        BigDecimal costeTotal = BigDecimal.ZERO;

        for (Object[] resultado : resultados) {

            BigDecimal coste = (BigDecimal) resultado[2];

            if (coste == null) {
                coste = BigDecimal.ZERO;
            }

            costeTotal = costeTotal.add(coste);

            System.out.println(
                    "Código: " + resultado[0] +
                            " | Proyecto: " + resultado[1] +
                            " | Coste: " + formatoMoneda.format(coste)
            );
        }

        System.out.println(
                "\nCoste total de todos los proyectos: " +
                        formatoMoneda.format(costeTotal)
        );
    }

    private void exportarDatos() {

        System.out.println("\n===== EXPORTACIÓN CSV =====");

        try {

            List<Path> archivos =
                    exportacionServicio.exportarTodo();

            System.out.println("Exportación completada correctamente.");

            System.out.println("\nArchivos generados:");

            for (Path archivo : archivos) {
                System.out.println("- " + archivo);
            }

        } catch (RuntimeException e) {

            System.out.println(
                    "No se pudieron exportar los datos: " +
                            e.getMessage()
            );
        }
    }
}
