
package menu;

import servicios.GestionServicio;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class MenuEstadisticas {

    private final Scanner scanner;
    private final GestionServicio gestionServicio;

    // Constructor: recibe el Scanner y el servicio de gestiones
    public MenuEstadisticas(Scanner scanner, GestionServicio gestionServicio) {
        this.scanner = scanner;
        this.gestionServicio = gestionServicio;
    }

    // Muestra el menú de estadísticas
    public void mostrarMenu() {

        int opcion;

        do {
            System.out.println("\n===== ESTADÍSTICAS =====");
            System.out.println("1. Cantidad total suministrada");
            System.out.println("2. Proveedores más activos");
            System.out.println("3. Piezas más utilizadas");
            System.out.println("4. Costes por proyecto");
            System.out.println("5. Volver");
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
                        System.out.println("Volviendo al menú principal...");
                        break;

                    default:
                        System.out.println("Opción no válida.");
                }

            } catch (NumberFormatException e) {
                opcion = 0;
                System.out.println("Debes introducir un número válido.");
            } catch (RuntimeException e) {
                opcion = 0;
                System.out.println("Error: " + e.getMessage());
            }

        } while (opcion != 5);
    }

    // Muestra la cantidad total de unidades suministradas
    private void mostrarCantidadTotal() {

        Long total = gestionServicio.calcularCantidadTotalSuministrada();

        System.out.println("\n===== CANTIDAD TOTAL SUMINISTRADA =====");
        System.out.println("Unidades suministradas: " + total);
    }

    // Muestra los proveedores ordenados por cantidad suministrada
    private void mostrarProveedoresMasActivos() {

        List<Object[]> resultados =
                gestionServicio.obtenerProveedoresMasActivos();

        System.out.println("\n===== PROVEEDORES MÁS ACTIVOS =====");

        if (resultados.isEmpty()) {
            System.out.println("No hay datos disponibles.");
            return;
        }

        System.out.printf("%-15s %-30s %-20s%n",
                "CÓDIGO", "PROVEEDOR", "UNIDADES");

        System.out.println("----------------------------------------------------------------");

        for (Object[] fila : resultados) {

            System.out.printf("%-15s %-30s %-20s%n",
                    fila[0],
                    fila[1],
                    fila[2]);
        }
    }

    // Muestra las piezas ordenadas por cantidad utilizada
    private void mostrarPiezasMasUtilizadas() {

        List<Object[]> resultados =
                gestionServicio.obtenerPiezasMasUtilizadas();

        System.out.println("\n===== PIEZAS MÁS UTILIZADAS =====");

        if (resultados.isEmpty()) {
            System.out.println("No hay datos disponibles.");
            return;
        }

        System.out.printf("%-15s %-30s %-20s%n",
                "CÓDIGO", "PIEZA", "UNIDADES");

        System.out.println("----------------------------------------------------------------");

        for (Object[] fila : resultados) {

            System.out.printf("%-15s %-30s %-20s%n",
                    fila[0],
                    fila[1],
                    fila[2]);
        }
    }

    // Muestra los costes totales agrupados por proyecto
    private void mostrarCostesPorProyecto() {

        List<Object[]> resultados =
                gestionServicio.calcularCostesPorProyecto();

        System.out.println("\n===== COSTES POR PROYECTO =====");

        if (resultados.isEmpty()) {
            System.out.println("No hay datos disponibles.");
            return;
        }

        NumberFormat formatoMoneda =
                NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-ES"));

        BigDecimal costeGlobal = BigDecimal.ZERO;

        System.out.printf("%-15s %-30s %-20s%n",
                "CÓDIGO", "PROYECTO", "COSTE TOTAL");

        System.out.println("----------------------------------------------------------------");

        for (Object[] fila : resultados) {

            BigDecimal coste = (BigDecimal) fila[2];

            costeGlobal = costeGlobal.add(coste);

            System.out.printf("%-15s %-30s %-20s%n",
                    fila[0],
                    fila[1],
                    formatoMoneda.format(coste));
        }

        System.out.println("----------------------------------------------------------------");
        System.out.println("Coste global: " + formatoMoneda.format(costeGlobal));
    }
}
