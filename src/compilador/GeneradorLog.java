
package compilador;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/*
  Esta clase es la encargada de generar el archivo de errores .log
  - Copia el contenido del archivo .vb respetando formato original
  - Anteponen números de línea con 4 dígitos
  - Permite agregar errores léxicos al final
 */

public class GeneradorLog {

    private String nombreArchivoLog;

    public GeneradorLog(String nombreArchivoVB) {
        this.nombreArchivoLog =
                nombreArchivoVB.substring(0, nombreArchivoVB.lastIndexOf(".")) + "-errores.log";
    }

    
     // Se genera el contenido base numerado
    
    public void generarContenidoBase(List<String> lineas) throws IOException {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivoLog))) {

            int contador = 1;

            for (String linea : lineas) {

                String numeroLinea = String.format("%04d", contador);

                writer.write(numeroLinea + " " + linea);
                writer.newLine();

                contador++;
            }
        }
    }

    
     // Se agregan los errores al final
     
    public void agregarErrores(List<ErrorLexico> errores) throws IOException {

        if (errores.isEmpty()) return;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivoLog, true))) {

            writer.newLine();

            for (ErrorLexico error : errores) {
                writer.write(error.toString());
                writer.newLine();
            }
        }
    }

    public String getNombreArchivoLog() {
        return nombreArchivoLog;
    }
}
