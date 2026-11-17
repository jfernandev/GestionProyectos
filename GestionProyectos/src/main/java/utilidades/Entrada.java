package utilidades;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Entrada {

    public static String leerTexto(Scanner scanner, String mensaje) {

        String texto;

        do {
            System.out.print(mensaje);
            texto = scanner.nextLine().trim();

            if (texto.isEmpty()) {
                System.out.println("Error: este campo no puede estar vacío.");
            }

        } while (texto.isEmpty());

        return texto;
    }

    public static String leerTextoOpcional(Scanner scanner, String mensaje) {

        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    public static int leerEntero(Scanner scanner, String mensaje) {

        while (true) {
            try {
                System.out.print(mensaje);
                return Integer.parseInt(scanner.nextLine().trim());

            } catch (NumberFormatException e) {
                System.out.println("Error: introduce un número entero válido.");
            }
        }
    }

    public static long leerLong(Scanner scanner, String mensaje) {

        while (true) {
            try {
                System.out.print(mensaje);
                return Long.parseLong(scanner.nextLine().trim());

            } catch (NumberFormatException e) {
                System.out.println("Error: introduce un número válido.");
            }
        }
    }

    public static BigDecimal leerDecimal(Scanner scanner, String mensaje) {

        while (true) {
            try {
                System.out.print(mensaje);

                String texto = scanner.nextLine().trim().replace(",", ".");

                return new BigDecimal(texto);

            } catch (NumberFormatException e) {
                System.out.println("Error: introduce un número decimal válido.");
            }
        }
    }

    public static LocalDate leerFecha(Scanner scanner, String mensaje) {

        while (true) {
            try {
                System.out.print(mensaje);

                String texto = scanner.nextLine().trim();

                return LocalDate.parse(texto);

            } catch (DateTimeParseException e) {
                System.out.println("Error: introduce una fecha válida con formato AAAA-MM-DD.");
            }
        }
    }
}
