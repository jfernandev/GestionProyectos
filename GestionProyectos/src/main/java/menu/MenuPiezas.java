package menu;

import modelo.Pieza;
import servicios.PiezaServicio;
import utilidades.Entrada;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class MenuPiezas {

    private final Scanner scanner;
    private final PiezaServicio piezaServicio;

    public MenuPiezas(Scanner scanner) {
        this.scanner = scanner;
        this.piezaServicio = new PiezaServicio();
    }

    public void iniciar() {

        int opcion;

        do {
            mostrarMenu();

            opcion = Entrada.leerEntero(scanner, "Selecciona una opción: ");

            switch (opcion) {

                case 1:
                    registrarPieza();
                    break;

                case 2:
                    listarPiezas();
                    break;

                case 3:
                    buscarPorCodigo();
                    break;

                case 4:
                    buscarPorNombre();
                    break;

                case 5:
                    actualizarPieza();
                    break;

                case 6:
                    eliminarPieza();
                    break;

                case 7:
                    System.out.println("\nVolviendo al menú principal...");
                    break;

                default:
                    System.out.println("\nError: selecciona una opción entre 1 y 7.");
                    break;
            }

        } while (opcion != 7);
    }

    private void mostrarMenu() {

        System.out.println("\n========================================");
        System.out.println("          GESTIÓN DE PIEZAS");
        System.out.println("========================================");
        System.out.println("1. Registrar pieza");
        System.out.println("2. Listar todas las piezas");
        System.out.println("3. Buscar pieza por código");
        System.out.println("4. Buscar piezas por nombre");
        System.out.println("5. Modificar pieza");
        System.out.println("6. Eliminar pieza");
        System.out.println("7. Volver al menú principal");
        System.out.println("========================================");
    }

    private void registrarPieza() {

        System.out.println("\n--- REGISTRAR PIEZA ---");

        String codigo = Entrada.leerTexto(scanner, "Código: ");
        String nombre = Entrada.leerTexto(scanner, "Nombre: ");
        BigDecimal precio = Entrada.leerDecimal(scanner, "Precio: ");
        String descripcion = Entrada.leerTextoOpcional(scanner, "Descripción (opcional): ");

        Pieza pieza = new Pieza(
                codigo,
                nombre,
                precio,
                descripcion
        );

        try {
            piezaServicio.registrar(pieza);
            System.out.println("\nPieza registrada correctamente.");

        } catch (RuntimeException e) {
            System.out.println("\nNo se ha podido registrar la pieza.");
            System.out.println("Motivo: " + e.getMessage());
        }
    }

    private void listarPiezas() {

        System.out.println("\n--- LISTADO DE PIEZAS ---");

        try {
            List<Pieza> piezas = piezaServicio.listarTodas();
            mostrarResultados(piezas);

        } catch (RuntimeException e) {
            System.out.println("Error al consultar las piezas: " + e.getMessage());
        }
    }

    private void buscarPorCodigo() {

        System.out.println("\n--- BUSCAR PIEZA POR CÓDIGO ---");

        String codigo = Entrada.leerTexto(scanner, "Introduce el código: ");

        try {
            Pieza pieza = piezaServicio.buscarPorCodigo(codigo);

            if (pieza == null) {
                System.out.println("No se ha encontrado ninguna pieza con ese código.");
            } else {
                System.out.println(pieza);
            }

        } catch (RuntimeException e) {
            System.out.println("Error durante la búsqueda: " + e.getMessage());
        }
    }

    private void buscarPorNombre() {

        System.out.println("\n--- BUSCAR PIEZAS POR NOMBRE ---");

        String nombre = Entrada.leerTexto(scanner, "Introduce el nombre: ");

        try {
            List<Pieza> piezas = piezaServicio.buscarPorNombre(nombre);
            mostrarResultados(piezas);

        } catch (RuntimeException e) {
            System.out.println("Error durante la búsqueda: " + e.getMessage());
        }
    }

    private void actualizarPieza() {

        System.out.println("\n--- MODIFICAR PIEZA ---");

        String codigo = Entrada.leerTexto(scanner, "Código de la pieza: ");

        try {
            Pieza piezaExistente = piezaServicio.buscarPorCodigo(codigo);

            if (piezaExistente == null) {
                System.out.println("No existe ninguna pieza con ese código.");
                return;
            }

            System.out.println("\nPieza encontrada:");
            System.out.println(piezaExistente);

            System.out.println("\nIntroduce los nuevos datos:");

            String nombre = Entrada.leerTexto(scanner, "Nuevo nombre: ");
            BigDecimal precio = Entrada.leerDecimal(scanner, "Nuevo precio: ");
            String descripcion = Entrada.leerTextoOpcional(
                    scanner,
                    "Nueva descripción (opcional): "
            );

            Pieza piezaActualizada = new Pieza(
                    codigo,
                    nombre,
                    precio,
                    descripcion
            );

            piezaServicio.actualizar(piezaActualizada);

            System.out.println("\nPieza actualizada correctamente.");

        } catch (RuntimeException e) {
            System.out.println("\nNo se ha podido actualizar la pieza.");
            System.out.println("Motivo: " + e.getMessage());
        }
    }

    private void eliminarPieza() {

        System.out.println("\n--- ELIMINAR PIEZA ---");

        String codigo = Entrada.leerTexto(scanner, "Código de la pieza: ");

        try {
            Pieza pieza = piezaServicio.buscarPorCodigo(codigo);

            if (pieza == null) {
                System.out.println("No existe ninguna pieza con ese código.");
                return;
            }

            System.out.println("\nPieza seleccionada:");
            System.out.println(pieza);

            String confirmacion = Entrada.leerTexto(
                    scanner,
                    "¿Estás seguro de que quieres eliminarla? (S/N): "
            );

            if (confirmacion.equalsIgnoreCase("S")) {

                piezaServicio.eliminar(codigo);

                System.out.println("\nPieza eliminada correctamente.");

            } else {
                System.out.println("\nOperación cancelada.");
            }

        } catch (RuntimeException e) {
            System.out.println("\nNo se ha podido eliminar la pieza.");
            System.out.println("Motivo: " + e.getMessage());
        }
    }

    private void mostrarResultados(List<Pieza> piezas) {

        if (piezas.isEmpty()) {
            System.out.println("No se han encontrado piezas.");
            return;
        }

        for (Pieza pieza : piezas) {
            System.out.println(pieza);
        }

        System.out.println("\nResultados encontrados: " + piezas.size());
    }
}
