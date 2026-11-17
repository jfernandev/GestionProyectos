
package menu;

import modelo.Gestion;
import modelo.Proveedor;
import modelo.Pieza;
import modelo.Proyecto;
import servicios.GestionServicio;
import utilidades.Entrada;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class MenuGestiones {

    private final Scanner scanner;
    private final GestionServicio gestionServicio;

    // Constructor: recibe el Scanner y prepara el servicio
    public MenuGestiones(Scanner scanner) {
        this.scanner = scanner;
        this.gestionServicio = new GestionServicio();
    }

    // Inicia el submenú de gestiones
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
                    actualizarGestion();
                    break;

                case 8:
                    eliminarGestion();
                    break;

                case 9:
                    System.out.println("\nVolviendo al menú principal...");
                    break;

                default:
                    System.out.println("\nError: selecciona una opción entre 1 y 9.");
                    break;
            }

        } while (opcion != 9);
    }

    // Muestra las opciones disponibles
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
        System.out.println("7. Modificar gestión");
        System.out.println("8. Eliminar gestión");
        System.out.println("9. Volver al menú principal");
        System.out.println("========================================");
    }

    // Registra una nueva gestión
    private void registrarGestion() {

        System.out.println("\n--- REGISTRAR GESTIÓN ---");

        LocalDate fecha = Entrada.leerFecha(scanner, "Fecha (AAAA-MM-DD): ");
        int cantidad = Entrada.leerEntero(scanner, "Cantidad suministrada: ");

        String codigoProveedor = Entrada.leerTexto(scanner, "Código del proveedor: ");
        String codigoPieza = Entrada.leerTexto(scanner, "Código de la pieza: ");
        String codigoProyecto = Entrada.leerTexto(scanner, "Código del proyecto: ");

        // Creamos objetos con los códigos de las entidades relacionadas.
        // El servicio y el DAO comprobarán que existen en la base de datos.
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

    // Lista todas las gestiones
    private void listarGestiones() {

        System.out.println("\n--- LISTADO DE GESTIONES ---");

        try {
            List<Gestion> gestiones = gestionServicio.listarTodas();
            mostrarResultados(gestiones);

        } catch (RuntimeException e) {
            System.out.println("Error al consultar las gestiones: " + e.getMessage());
        }
    }

    // Busca una gestión por su identificador
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

    // Busca las gestiones de un proveedor
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

    // Busca las gestiones de una pieza
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

    // Busca las gestiones de un proyecto
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

    // Modifica una gestión existente
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

            // Conservamos el identificador de la gestión original
            gestionActualizada.setId(id);

            gestionServicio.actualizar(gestionActualizada);

            System.out.println("\nGestión actualizada correctamente.");

        } catch (RuntimeException e) {
            System.out.println("\nNo se ha podido actualizar la gestión.");
            System.out.println("Motivo: " + e.getMessage());
        }
    }

    // Elimina una gestión por su identificador
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

    // Muestra los resultados de una consulta
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
