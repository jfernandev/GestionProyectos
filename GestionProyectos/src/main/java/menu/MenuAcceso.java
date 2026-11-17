package menu;

import modelo.Usuario;
import servicios.UsuarioServicio;

import java.io.Console;
import java.util.Scanner;

public class MenuAcceso {

    private final Scanner scanner;
    private final UsuarioServicio usuarioServicio;

    public MenuAcceso(
            Scanner scanner,
            UsuarioServicio usuarioServicio
    ) {
        this.scanner = scanner;
        this.usuarioServicio = usuarioServicio;
    }

    public Usuario mostrarMenu() {

        boolean salir = false;

        while (!salir) {

            System.out.println("\n===== ACCESO A GESTIONPROYECTOS =====");
            System.out.println("1. Iniciar sesión");
            System.out.println("2. Crear administrador inicial");
            System.out.println("3. Salir");
            System.out.print("Selecciona una opción: ");

            String opcion = scanner.nextLine();

            switch (opcion) {

                case "1":
                    Usuario usuario = iniciarSesion();

                    if (usuario != null) {
                        return usuario;
                    }
                    break;

                case "2":
                    crearAdministradorInicial();
                    break;

                case "3":
                    salir = true;
                    break;

                default:
                    System.out.println("Opción no válida.");
            }
        }

        return null;
    }

    private Usuario iniciarSesion() {

        System.out.println("\n===== INICIAR SESIÓN =====");

        System.out.print("Nombre de usuario: ");
        String nombreUsuario = scanner.nextLine();

        String contrasena = leerContrasena("Contraseña: ");

        try {

            Usuario usuario = usuarioServicio.iniciarSesion(
                    nombreUsuario,
                    contrasena
            );

            System.out.println(
                    "\nBienvenido, " + usuario.getNombreUsuario() + "."
            );

            return usuario;

        } catch (IllegalArgumentException e) {

            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    private void crearAdministradorInicial() {

        System.out.println("\n===== CREAR ADMINISTRADOR INICIAL =====");

        System.out.print("Nombre de usuario: ");
        String nombreUsuario = scanner.nextLine();

        String contrasena = leerContrasena(
                "Contraseña (mínimo 8 caracteres): "
        );

        String confirmacion = leerContrasena(
                "Repite la contraseña: "
        );

        if (!contrasena.equals(confirmacion)) {
            System.out.println("Las contraseñas no coinciden.");
            return;
        }

        try {

            usuarioServicio.crearAdministradorInicial(
                    nombreUsuario,
                    contrasena
            );

            System.out.println(
                    "Administrador creado correctamente."
            );

        } catch (IllegalArgumentException |
                 IllegalStateException e) {

            System.out.println("Error: " + e.getMessage());
        }
    }

    // Intenta ocultar la contraseña si se ejecuta desde una consola compatible
    private String leerContrasena(String mensaje) {

        Console consola = System.console();

        if (consola != null) {
            char[] caracteres = consola.readPassword(mensaje);
            return caracteres == null ? "" : new String(caracteres);
        }

        // Alternativa para la consola de IntelliJ
        System.out.print(mensaje);
        return scanner.nextLine();
    }
}
