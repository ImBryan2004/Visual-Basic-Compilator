
package compilador;

import java.util.ArrayList;
import java.util.List;

public class ValidadorComentarios {
    /*
      En esta clase se valida si una línea es un comentario válido en Visual Basic
      Regla del proyecto:
      - SOLO es válido si la línea inicia con '
      - No se permiten comentarios al final de una línea de código
     */

    public boolean esComentarioValido(String linea) {
        String lineaLimpia = linea.trim();

        return lineaLimpia.startsWith("'");
    }

    /*
      Detecta comentarios inválidos (inline)
      Ejemplo inválido:
      Dim x As Integer = 10 ' comentario
     */

    public List<ErrorLexico> validar(String linea, int numeroLinea) {

        List<ErrorLexico> errores = new ArrayList<>();

        String lineaLimpia = linea.trim();

        // Si es comentario válido al inicio → se ignora totalmente
        if (lineaLimpia.startsWith("'")) {
            return errores;
        }

        // Si contiene ' pero no inicia con él → error (comentario inline)
        if (linea.contains("'")) {
            errores.add(new ErrorLexico(
                    900,
                    numeroLinea,
                    "Comentario inválido: solo se permiten líneas que inicien con apóstrofe (')"
            ));
        }

        return errores;
    }
}
