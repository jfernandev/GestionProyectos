package menu;

import modelo.Proyecto;
import servicios.ProyectoServicio;
import utilidades.Entrada;

import java.util.List;
import java.util.Scanner;

public class MenuProyectos {

    private final Scanner scanner;
    private final ProyectoServicio proyectoServicio;

    public MenuProyectos(Scanner scanner) {
        this.scanner = scanner;
        this.proyectoServicio = new ProyectoServicio();
    }

    public void iniciar() {
        int opcion;

        do {
            mostrarMenu();
            opcion = Entrada.leerEntero(scanner, "Selecciona una opción: ");

            try {
                switch (opcion) {
                    case 1:
                        registrarProyecto();
                        break;
                    case 2:
                        listarProyectos();
                        break;
                    case 3:
                        buscarPorCodigo();
                        break;
                    case 4:
                        buscarPorNombre();
                        break;
                    case 5:
                        buscarPorCiudad();
                        break;
                    case 6:
                        actualizarProyecto();
                        break;
                    case 7:
                        eliminarProyecto();
                        break;
                    case 8:
                        System.out.println("\nVolviendo al menú principal...");
                        break;
                    default:
                        System.out.println("\nError: selecciona una opción entre 1 y 8.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("\nError: " + e.getMessage());
            }

        } while (opcion != 8);
    }

    private void mostrarMenu() {
        System.out.println("\n========================================");
        System.out.println("          MENÚ DE PROYECTOS");
        System.out.println("========================================");
        System.out.println("1. Registrar proyecto");
        System.out.println("2. Listar todos los proyectos");
        System.out.println("3. Buscar proyecto por código");
        System.out.println("4. Buscar proyectos por nombre");
        System.out.println("5. Buscar proyectos por ciudad");
        System.out.println("6. Modificar proyecto");
        System.out.println("7. Eliminar proyecto");
        System.out.println("8. Volver al menú principal");
        System.out.println("========================================");
    }

    private void registrarProyecto() {
        System.out.println("\n--- REGISTRAR PROYECTO ---");

        String codigo = Entrada.leerTexto(scanner, "Código: ");
        String nombre = Entrada.leerTexto(scanner, "Nombre: ");
        String ciudad = Entrada.leerTexto(scanner, "Ciudad: ");

        Proyecto proyecto = new Proyecto(codigo, nombre, ciudad);

        proyectoServicio.registrar(proyecto);

        System.out.println("\nProyecto registrado correctamente.");
    }

    private void listarProyectos() {
        System.out.println("\n--- LISTADO DE PROYECTOS ---");

        List<Proyecto> proyectos = proyectoServicio.listarTodos();

        if (proyectos.isEmpty()) {
            System.out.println("No hay proyectos registrados.");
            return;
        }

        for (Proyecto proyecto : proyectos) {
            System.out.println(proyecto);
        }
    }

    private void buscarPorCodigo() {
        System.out.println("\n--- BUSCAR PROYECTO POR CÓDIGO ---");

        String codigo = Entrada.leerTexto(scanner, "Introduce el código: ");

        Proyecto proyecto = proyectoServicio.buscarPorCodigo(codigo);

        if (proyecto == null) {
            System.out.println("No se ha encontrado ningún proyecto.");
        } else {
            System.out.println(proyecto);
        }
    }

    private void buscarPorNombre() {
        System.out.println("\n--- BUSCAR PROYECTOS POR NOMBRE ---");

        String nombre = Entrada.leerTexto(scanner, "Introduce el nombre: ");

        List<Proyecto> proyectos = proyectoServicio.buscarPorNombre(nombre);

        mostrarResultados(proyectos);
    }

    private void buscarPorCiudad() {
        System.out.println("\n--- BUSCAR PROYECTOS POR CIUDAD ---");

        String ciudad = Entrada.leerTexto(scanner, "Introduce la ciudad: ");

        List<Proyecto> proyectos = proyectoServicio.buscarPorCiudad(ciudad);

        mostrarResultados(proyectos);
    }

    private void mostrarResultados(List<Proyecto> proyectos) {
        if (proyectos.isEmpty()) {
            System.out.println("No se han encontrado proyectos.");
            return;
        }

        for (Proyecto proyecto : proyectos) {
            System.out.println(proyecto);
        }
    }

    private void actualizarProyecto() {
        System.out.println("\n--- MODIFICAR PROYECTO ---");

        String codigo = Entrada.leerTexto(scanner, "Código del proyecto: ");

        Proyecto proyecto = proyectoServicio.buscarPorCodigo(codigo);

        if (proyecto == null) {
            System.out.println("No existe ningún proyecto con ese código.");
            return;
        }

        System.out.println("Datos actuales: " + proyecto);

        String nombre = Entrada.leerTexto(scanner, "Nuevo nombre: ");
        String ciudad = Entrada.leerTexto(scanner, "Nueva ciudad: ");

        Proyecto proyectoActualizado = new Proyecto(codigo, nombre, ciudad);

        proyectoServicio.actualizar(proyectoActualizado);

        System.out.println("\nProyecto actualizado correctamente.");
    }

    private void eliminarProyecto() {
        System.out.println("\n--- ELIMINAR PROYECTO ---");

        String codigo = Entrada.leerTexto(scanner, "Código del proyecto: ");

        Proyecto proyecto = proyectoServicio.buscarPorCodigo(codigo);

        if (proyecto == null) {
            System.out.println("No existe ningún proyecto con ese código.");
            return;
        }

        System.out.println("Proyecto seleccionado: " + proyecto);

        String confirmacion = Entrada.leerTexto(
                scanner,
                "¿Seguro que quieres eliminarlo? (S/N): "
        );

        if (confirmacion.equalsIgnoreCase("S")) {
            proyectoServicio.eliminar(codigo);
            System.out.println("Proyecto eliminado correctamente.");
        } else {
            System.out.println("Operación cancelada.");
        }
    }
}
