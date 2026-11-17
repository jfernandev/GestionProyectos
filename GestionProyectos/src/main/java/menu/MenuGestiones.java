package menu;

import modelo.Gestion;
import modelo.Pieza;
import modelo.Proyecto;
import modelo.Proveedor;
import servicios.GestionServicio;
import utilidades.Entrada;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class MenuGestiones {

    private final Scanner scanner;
    private final GestionServicio gestionServicio;

    public MenuGestiones(Scanner scanner) {
        this.scanner = scanner;
        this.gestionServicio = new GestionServicio();
    }

    public void iniciar() {

        int opcion;

        do {
            mostrarMenu();

            opcion = Entrada.leerEntero(scanner, "Selecciona una opción: ");

            switch (opcion) {

                case 1:
                    registrarGestion();
                    break;

                case 2:
                    listarGestiones();
                    break;

                case 3:
                    buscarPorId();
                    break;

                case 4:
                    buscarPorProveedor();
                    break;

                case 5:
                    buscarPorPieza();
                    break;

                case 6:
                    buscarPorProyecto();
                    break;

                case 7:
                    buscarAvanzado();
                    break;

                case 8:
                    analizarCostesPorProyecto();
                    break;

                case 9:
                    actualizarGestion();
                    break;

                case 10:
                    eliminarGestion();
                    break;

                case 11:
                    System.out.println("\nVolviendo al menú principal...");
                    break;

                default:
                    System.out.println("\nError: selecciona una opción entre 1 y 11.");
                    break;
            }

        } while (opcion != 11);
    }

    private void mostrarMenu() {

        System.out.println("\n========================================");
        System.out.println("         GESTIÓN DE SUMINISTROS");
        System.out.println("========================================");
        System.out.println("1. Registrar gestión");
        System.out.println("2. Listar todas las gestiones");
        System.out.println("3. Buscar gestión por identificador");
        System.out.println("4. Buscar gestiones por proveedor");
        System.out.println("5. Buscar gestiones por pieza");
        System.out.println("6. Buscar gestiones por proyecto");
        System.out.println("7. Búsqueda avanzada de gestiones");
        System.out.println("8. Análisis de costes por proyecto");
        System.out.println("9. Modificar gestión");
        System.out.println("10. Eliminar gestión");
        System.out.println("11. Volver al menú principal");
        System.out.println("========================================");
    }

    private void registrarGestion() {

        System.out.println("\n--- REGISTRAR GESTIÓN ---");

        LocalDate fecha = Entrada.leerFecha(scanner, "Fecha (AAAA-MM-DD): ");
        int cantidad = Entrada.leerEntero(scanner, "Cantidad suministrada: ");

        String codigoProveedor = Entrada.leerTexto(scanner, "Código del proveedor: ");
        String codigoPieza = Entrada.leerTexto(scanner, "Código de la pieza: ");
        String codigoProyecto = Entrada.leerTexto(scanner, "Código del proyecto: ");

        Proveedor proveedor = new Proveedor(codigoProveedor, null, null, null);
        Pieza pieza = new Pieza(codigoPieza, null, null, null);
        Proyecto proyecto = new Proyecto(codigoProyecto, null, null);

        Gestion gestion = new Gestion(
                fecha,
                cantidad,
                proveedor,
                pieza,
                proyecto
        );

        try {
            gestionServicio.registrar(gestion);
            System.out.println("\nGestión registrada correctamente.");

        } catch (RuntimeException e) {
            System.out.println("\nNo se ha podido registrar la gestión.");
            System.out.println("Motivo: " + e.getMessage());
        }
    }

    private void listarGestiones() {

        System.out.println("\n--- LISTADO DE GESTIONES ---");

        try {
            List<Gestion> gestiones = gestionServicio.listarTodas();
            mostrarResultados(gestiones);

        } catch (RuntimeException e) {
            System.out.println("Error al consultar las gestiones: " + e.getMessage());
        }
    }

    private void buscarPorId() {

        System.out.println("\n--- BUSCAR GESTIÓN POR IDENTIFICADOR ---");

        long id = Entrada.leerLong(scanner, "Identificador de la gestión: ");

        try {
            Gestion gestion = gestionServicio.buscarPorId(id);

            if (gestion == null) {
                System.out.println("No se ha encontrado ninguna gestión con ese identificador.");
            } else {
                System.out.println(gestion);
            }

        } catch (RuntimeException e) {
            System.out.println("Error durante la búsqueda: " + e.getMessage());
        }
    }

    private void buscarPorProveedor() {

        System.out.println("\n--- BUSCAR GESTIONES POR PROVEEDOR ---");

        String codigo = Entrada.leerTexto(scanner, "Código del proveedor: ");

        try {
            List<Gestion> gestiones = gestionServicio.buscarPorProveedor(codigo);
            mostrarResultados(gestiones);

        } catch (RuntimeException e) {
            System.out.println("Error durante la búsqueda: " + e.getMessage());
        }
    }

    private void buscarPorPieza() {

        System.out.println("\n--- BUSCAR GESTIONES POR PIEZA ---");

        String codigo = Entrada.leerTexto(scanner, "Código de la pieza: ");

        try {
            List<Gestion> gestiones = gestionServicio.buscarPorPieza(codigo);
            mostrarResultados(gestiones);

        } catch (RuntimeException e) {
            System.out.println("Error durante la búsqueda: " + e.getMessage());
        }
    }

    private void buscarPorProyecto() {

        System.out.println("\n--- BUSCAR GESTIONES POR PROYECTO ---");

        String codigo = Entrada.leerTexto(scanner, "Código del proyecto: ");

        try {
            List<Gestion> gestiones = gestionServicio.buscarPorProyecto(codigo);
            mostrarResultados(gestiones);

        } catch (RuntimeException e) {
            System.out.println("Error durante la búsqueda: " + e.getMessage());
        }
    }

    private void buscarAvanzado() {

        System.out.println("\n--- BÚSQUEDA AVANZADA DE GESTIONES ---");
        System.out.println("Deja los campos vacíos si no quieres aplicar ese filtro.\n");

        String codigoProveedor = leerFiltroTexto("Código del proveedor: ");
        String codigoPieza = leerFiltroTexto("Código de la pieza: ");
        String codigoProyecto = leerFiltroTexto("Código del proyecto: ");

        LocalDate fechaDesde = leerFiltroFecha("Fecha desde (AAAA-MM-DD): ");
        LocalDate fechaHasta = leerFiltroFecha("Fecha hasta (AAAA-MM-DD): ");

        Integer cantidadMinima = leerFiltroEntero("Cantidad mínima: ");
        Integer cantidadMaxima = leerFiltroEntero("Cantidad máxima: ");

        try {

            List<Gestion> gestiones = gestionServicio.buscarAvanzado(
                    codigoProveedor,
                    codigoPieza,
                    codigoProyecto,
                    fechaDesde,
                    fechaHasta,
                    cantidadMinima,
                    cantidadMaxima
            );

            System.out.println("\n--- RESULTADOS DE LA BÚSQUEDA AVANZADA ---");
            mostrarResultados(gestiones);

        } catch (RuntimeException e) {
            System.out.println("\nNo se ha podido realizar la búsqueda.");
            System.out.println("Motivo: " + e.getMessage());
        }
    }

    private String leerFiltroTexto(String mensaje) {

        System.out.print(mensaje);
        String texto = scanner.nextLine().trim();

        return texto.isEmpty() ? null : texto;
    }

    private LocalDate leerFiltroFecha(String mensaje) {

        while (true) {

            System.out.print(mensaje);
            String texto = scanner.nextLine().trim();

            if (texto.isEmpty()) {
                return null;
            }

            try {
                return LocalDate.parse(texto);

            } catch (Exception e) {
                System.out.println("Formato incorrecto. Utiliza AAAA-MM-DD.");
            }
        }
    }

    private Integer leerFiltroEntero(String mensaje) {

        while (true) {

            System.out.print(mensaje);
            String texto = scanner.nextLine().trim();

            if (texto.isEmpty()) {
                return null;
            }

            try {
                return Integer.parseInt(texto);

            } catch (NumberFormatException e) {
                System.out.println("Introduce un número entero válido.");
            }
        }
    }

    private void analizarCostesPorProyecto() {

        System.out.println("\n--- ANÁLISIS DE COSTES POR PROYECTO ---");

        try {

            List<Object[]> resultados =
                    gestionServicio.calcularCostesPorProyecto();

            if (resultados.isEmpty()) {
                System.out.println("No hay datos suficientes para calcular los costes.");
                return;
            }

            NumberFormat formatoEuros =
                    NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-ES"));

            System.out.printf("%-15s %-30s %15s%n",
                    "CÓDIGO", "PROYECTO", "COSTE TOTAL");

            System.out.println("---------------------------------------------------------------");

            BigDecimal costeGlobal = BigDecimal.ZERO;

            for (Object[] fila : resultados) {

                String codigo = (String) fila[0];
                String nombre = (String) fila[1];
                BigDecimal coste = new BigDecimal(fila[2].toString());

                System.out.printf("%-15s %-30s %15s%n",
                        codigo,
                        nombre,
                        formatoEuros.format(coste));

                costeGlobal = costeGlobal.add(coste);
            }

            System.out.println("---------------------------------------------------------------");
            System.out.println("Coste global de todos los proyectos: "
                    + formatoEuros.format(costeGlobal));

        } catch (RuntimeException e) {
            System.out.println("Error al calcular los costes: " + e.getMessage());
        }
    }

    private void actualizarGestion() {

        System.out.println("\n--- MODIFICAR GESTIÓN ---");

        long id = Entrada.leerLong(scanner, "Identificador de la gestión: ");

        try {
            Gestion gestionExistente = gestionServicio.buscarPorId(id);

            if (gestionExistente == null) {
                System.out.println("No existe ninguna gestión con ese identificador.");
                return;
            }

            System.out.println("\nGestión encontrada:");
            System.out.println(gestionExistente);

            System.out.println("\nIntroduce los nuevos datos:");

            LocalDate fecha = Entrada.leerFecha(scanner, "Nueva fecha (AAAA-MM-DD): ");
            int cantidad = Entrada.leerEntero(scanner, "Nueva cantidad: ");

            String codigoProveedor = Entrada.leerTexto(scanner, "Nuevo código del proveedor: ");
            String codigoPieza = Entrada.leerTexto(scanner, "Nuevo código de la pieza: ");
            String codigoProyecto = Entrada.leerTexto(scanner, "Nuevo código del proyecto: ");

            Proveedor proveedor = new Proveedor(codigoProveedor, null, null, null);
            Pieza pieza = new Pieza(codigoPieza, null, null, null);
            Proyecto proyecto = new Proyecto(codigoProyecto, null, null);

            Gestion gestionActualizada = new Gestion(
                    fecha,
                    cantidad,
                    proveedor,
                    pieza,
                    proyecto
            );

            gestionActualizada.setId(id);

            gestionServicio.actualizar(gestionActualizada);

            System.out.println("\nGestión actualizada correctamente.");

        } catch (RuntimeException e) {
            System.out.println("\nNo se ha podido actualizar la gestión.");
            System.out.println("Motivo: " + e.getMessage());
        }
    }

    private void eliminarGestion() {

        System.out.println("\n--- ELIMINAR GESTIÓN ---");

        long id = Entrada.leerLong(scanner, "Identificador de la gestión: ");

        try {
            Gestion gestion = gestionServicio.buscarPorId(id);

            if (gestion == null) {
                System.out.println("No existe ninguna gestión con ese identificador.");
                return;
            }

            System.out.println("\nGestión seleccionada:");
            System.out.println(gestion);

            String confirmacion = Entrada.leerTexto(
                    scanner,
                    "¿Estás seguro de que quieres eliminarla? (S/N): "
            );

            if (confirmacion.equalsIgnoreCase("S")) {

                gestionServicio.eliminar(id);

                System.out.println("\nGestión eliminada correctamente.");

            } else {
                System.out.println("\nOperación cancelada.");
            }

        } catch (RuntimeException e) {
            System.out.println("\nNo se ha podido eliminar la gestión.");
            System.out.println("Motivo: " + e.getMessage());
        }
    }

    private void mostrarResultados(List<Gestion> gestiones) {

        if (gestiones.isEmpty()) {
            System.out.println("No se han encontrado gestiones.");
            return;
        }

        for (Gestion gestion : gestiones) {
            System.out.println(gestion);
        }

        System.out.println("\nResultados encontrados: " + gestiones.size());
    }
}
