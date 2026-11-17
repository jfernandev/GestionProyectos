package utilidades;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ExportadorCSV {

    private static final Path CARPETA_EXPORTACIONES =
            Path.of("exportaciones");

    private ExportadorCSV() {
    }

    public static Path exportar(
            String nombreArchivo,
            String[] cabeceras,
            List<String[]> filas
    ) {

        if (nombreArchivo == null || nombreArchivo.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre del archivo es obligatorio."
            );
        }

        if (cabeceras == null || cabeceras.length == 0) {
            throw new IllegalArgumentException(
                    "El archivo debe tener cabeceras."
            );
        }

        if (filas == null) {
            throw new IllegalArgumentException(
                    "La lista de datos no puede ser nula."
            );
        }

        // Evitamos que el nombre recibido incluya rutas de otras carpetas
        String nombreSeguro = Path.of(nombreArchivo).getFileName().toString();

        if (!nombreSeguro.toLowerCase().endsWith(".csv")) {
            nombreSeguro += ".csv";
        }

        Path rutaArchivo = CARPETA_EXPORTACIONES.resolve(nombreSeguro);

        try {

            Files.createDirectories(CARPETA_EXPORTACIONES);

            try (BufferedWriter escritor = Files.newBufferedWriter(
                    rutaArchivo,
                    StandardCharsets.UTF_8
            )) {

                // BOM para mejorar la compatibilidad con Excel
                escritor.write('\uFEFF');

                escribirFila(escritor, cabeceras);

                for (String[] fila : filas) {

                    if (fila.length != cabeceras.length) {
                        throw new IllegalArgumentException(
                                "Una fila tiene un número incorrecto de columnas."
                        );
                    }

                    escribirFila(escritor, fila);
                }
            }

            return rutaArchivo;

        } catch (IOException e) {

            throw new RuntimeException(
                    "Error al exportar el archivo CSV.", e
            );
        }
    }

    private static void escribirFila(
            BufferedWriter escritor,
            String[] valores
    ) throws IOException {

        for (int i = 0; i < valores.length; i++) {

            if (i > 0) {
                escritor.write(";");
            }

            escritor.write(escaparValor(valores[i]));
        }

        escritor.newLine();
    }

    private static String escaparValor(String valor) {

        if (valor == null) {
            return "";
        }

        if (valor.contains(";") ||
                valor.contains("\"") ||
                valor.contains("\n") ||
                valor.contains("\r")) {

            return "\"" + valor.replace("\"", "\"\"") + "\"";
        }

        return valor;
    }
}
