
package menu;

import modelo.Proveedor;
import servicios.ProveedorServicio;
import utilidades.Entrada;

import java.util.List;
import java.util.Scanner;

public class MenuProveedores {

    private final Scanner scanner;
    private final ProveedorServicio proveedorServicio;

    // Constructor: recibe el Scanner y prepara el servicio
    public MenuProveedores(Scanner scanner) {
        this.scanner = scanner;
        this.proveedorServicio = new ProveedorServicio();
    }

    // Inicia el submenú de proveedores
    public void iniciar() {

        int opcion;

        do {
            mostrarMenu();

            opcion = Entrada.leerEntero(scanner, "Selecciona una opción: ");

            switch (opcion) {

                case 1:
                    registrarProveedor();
                    break;

                case 2:
                    listarProveedores();
                    break;

                case 3:
                    buscarPorCodigo();
                    break;

                case 4:
                    buscarPorNombre();
                    break;

                case 5:
                    buscarPorDireccion();
                    break;

                case 6:
                    actualizarProveedor();
                    break;

                case 7:
                    eliminarProveedor();
                    break;

                case 8:
                    System.out.println("\nVolviendo al menú principal...");
                    break;

                default:
                    System.out.println("\nError: selecciona una opción entre 1 y 8.");
                    break;
            }

        } while (opcion != 8);
    }

    // Muestra las opciones disponibles
    private void mostrarMenu() {

        System.out.println("\n========================================");
        System.out.println("       GESTIÓN DE PROVEEDORES");
        System.out.println("========================================");
        System.out.println("1. Registrar proveedor");
        System.out.println("2. Listar todos los proveedores");
        System.out.println("3. Buscar proveedor por código");
        System.out.println("4. Buscar proveedores por nombre");
        System.out.println("5. Buscar proveedores por dirección");
        System.out.println("6. Modificar proveedor");
        System.out.println("7. Eliminar proveedor");
        System.out.println("8. Volver al menú principal");
        System.out.println("========================================");
    }

    // Registra un proveedor nuevo
    private void registrarProveedor() {

        System.out.println("\n--- REGISTRAR PROVEEDOR ---");

        String codigo = Entrada.leerTexto(scanner, "Código: ");
        String nombre = Entrada.leerTexto(scanner, "Nombre: ");
        String apellidos = Entrada.leerTexto(scanner, "Apellidos: ");
        String direccion = Entrada.leerTexto(scanner, "Dirección: ");

        Proveedor proveedor = new Proveedor(
                codigo,
                nombre,
                apellidos,
                direccion
        );

        try {
            proveedorServicio.registrar(proveedor);
            System.out.println("\nProveedor registrado correctamente.");

        } catch (RuntimeException e) {
            System.out.println("\nNo se ha podido registrar el proveedor.");
            System.out.println("Motivo: " + e.getMessage());
        }
    }

    // Lista todos los proveedores
    private void listarProveedores() {

        System.out.println("\n--- LISTADO DE PROVEEDORES ---");

        try {
            List<Proveedor> proveedores = proveedorServicio.listarTodos();
            mostrarResultados(proveedores);

        } catch (RuntimeException e) {
            System.out.println("Error al consultar los proveedores: " + e.getMessage());
        }
    }

    // Busca un proveedor por su código
    private void buscarPorCodigo() {

        System.out.println("\n--- BUSCAR PROVEEDOR POR CÓDIGO ---");

        String codigo = Entrada.leerTexto(scanner, "Introduce el código: ");

        try {
            Proveedor proveedor = proveedorServicio.buscarPorCodigo(codigo);

            if (proveedor == null) {
                System.out.println("No se ha encontrado ningún proveedor con ese código.");
            } else {
                System.out.println(proveedor);
            }

        } catch (RuntimeException e) {
            System.out.println("Error durante la búsqueda: " + e.getMessage());
        }
    }

    // Busca proveedores por nombre
    private void buscarPorNombre() {

        System.out.println("\n--- BUSCAR PROVEEDORES POR NOMBRE ---");

        String nombre = Entrada.leerTexto(scanner, "Introduce el nombre: ");

        try {
            List<Proveedor> proveedores = proveedorServicio.buscarPorNombre(nombre);
            mostrarResultados(proveedores);

        } catch (RuntimeException e) {
            System.out.println("Error durante la búsqueda: " + e.getMessage());
        }
    }

    // Busca proveedores por dirección
    private void buscarPorDireccion() {

        System.out.println("\n--- BUSCAR PROVEEDORES POR DIRECCIÓN ---");

        String direccion = Entrada.leerTexto(scanner, "Introduce la dirección: ");

        try {
            List<Proveedor> proveedores = proveedorServicio.buscarPorDireccion(direccion);
            mostrarResultados(proveedores);

        } catch (RuntimeException e) {
            System.out.println("Error durante la búsqueda: " + e.getMessage());
        }
    }

    // Modifica los datos de un proveedor existente
    private void actualizarProveedor() {

        System.out.println("\n--- MODIFICAR PROVEEDOR ---");

        String codigo = Entrada.leerTexto(scanner, "Código del proveedor: ");

        try {
            Proveedor proveedorExistente = proveedorServicio.buscarPorCodigo(codigo);

            if (proveedorExistente == null) {
                System.out.println("No existe ningún proveedor con ese código.");
                return;
            }

            System.out.println("\nProveedor encontrado:");
            System.out.println(proveedorExistente);

            System.out.println("\nIntroduce los nuevos datos:");

            String nombre = Entrada.leerTexto(scanner, "Nuevo nombre: ");
            String apellidos = Entrada.leerTexto(scanner, "Nuevos apellidos: ");
            String direccion = Entrada.leerTexto(scanner, "Nueva dirección: ");

            // Conservamos el código original
            Proveedor proveedorActualizado = new Proveedor(
                    codigo,
                    nombre,
                    apellidos,
                    direccion
            );

            proveedorServicio.actualizar(proveedorActualizado);

            System.out.println("\nProveedor actualizado correctamente.");

        } catch (RuntimeException e) {
            System.out.println("\nNo se ha podido actualizar el proveedor.");
            System.out.println("Motivo: " + e.getMessage());
        }
    }

    // Elimina un proveedor por su código
    private void eliminarProveedor() {

        System.out.println("\n--- ELIMINAR PROVEEDOR ---");

        String codigo = Entrada.leerTexto(scanner, "Código del proveedor: ");

        try {
            Proveedor proveedor = proveedorServicio.buscarPorCodigo(codigo);

            if (proveedor == null) {
                System.out.println("No existe ningún proveedor con ese código.");
                return;
            }

            System.out.println("\nProveedor seleccionado:");
            System.out.println(proveedor);

            String confirmacion = Entrada.leerTexto(
                    scanner,
                    "¿Estás seguro de que quieres eliminarlo? (S/N): "
            );

            if (confirmacion.equalsIgnoreCase("S")) {

                proveedorServicio.eliminar(codigo);

                System.out.println("\nProveedor eliminado correctamente.");

            } else {
                System.out.println("\nOperación cancelada.");
            }

        } catch (RuntimeException e) {
            System.out.println("\nNo se ha podido eliminar el proveedor.");
            System.out.println("Motivo: " + e.getMessage());
        }
    }

    // Muestra los resultados de una consulta
    private void mostrarResultados(List<Proveedor> proveedores) {

        if (proveedores.isEmpty()) {
            System.out.println("No se han encontrado proveedores.");
            return;
        }

        for (Proveedor proveedor : proveedores) {
            System.out.println(proveedor);
        }

        System.out.println("\nResultados encontrados: " + proveedores.size());
    }
}
