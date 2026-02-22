
package compilador;

import java.util.ArrayList;
import java.util.List;


 // Valida sentencias Console.WriteLine según los requerimientos del proyecto.
 
public class ValidadorWriteLine {

    /*
      Valida una línea que contenga Console.WriteLine
     
      @param linea línea de código
      @param numeroLinea número de línea en el archivo
      @return lista de errores léxicos encontrados
     */
    public List<ErrorLexico> validar(String linea, int numeroLinea) {

        List<ErrorLexico> errores = new ArrayList<>();

        String lineaLimpia = linea.trim();

        // Solo analizar líneas que contengan Console.WriteLine
        if (!lineaLimpia.toLowerCase().contains("console.writeline")) {
            return errores;
        }

        int indiceParentesisAbre = lineaLimpia.indexOf("(");
        int indiceParentesisCierra = lineaLimpia.lastIndexOf(")");

        
         // Regla 1 y 2: deben existir ambos paréntesis
        
        if (indiceParentesisAbre == -1 || indiceParentesisCierra == -1) {
            errores.add(new ErrorLexico(
                    600,
                    numeroLinea,
                    "Faltan paréntesis en la sentencia Console.WriteLine"
            ));
            return errores;
        }

       
         // Extraer contenido dentro de los paréntesis
         
        if (indiceParentesisCierra < indiceParentesisAbre) {
            errores.add(new ErrorLexico(
                    601,
                    numeroLinea,
                    "Orden incorrecto de paréntesis en Console.WriteLine"
            ));
            return errores;
        }

        String contenido = lineaLimpia.substring(
                indiceParentesisAbre + 1,
                indiceParentesisCierra
        ).trim();

        
         // Regla 3: contenido no puede estar vacío
         
        if (contenido.isEmpty()) {
            errores.add(new ErrorLexico(
                    602,
                    numeroLinea,
                    "Console.WriteLine no puede tener paréntesis vacíos"
            ));
            return errores;
        }

        
         // Regla 4: si inicia con comillas, debe cerrar con comillas
         
        int contadorComillas = 0;

        for (char c : contenido.toCharArray()) {
            if (c == '"') {
                contadorComillas++;
            }
        }

        if (contadorComillas % 2 != 0) {
            errores.add(new ErrorLexico(
                    603,
                    numeroLinea,
                    "Cadena de texto sin cierre de comillas en Console.WriteLine"
            ));
        }

        return errores;
    }
}
