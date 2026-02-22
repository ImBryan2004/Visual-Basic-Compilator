
package compilador;

/*
 La clase utilitaria que centraliza las palabras reservadas
 del lenguaje Visual Basic requeridas por el proyecto
 */
public class PalabrasReservadas {

    
     // Verifica si una palabra es reservada (case-insensitive)
     
    public static boolean esPalabraReservada(String palabra) {

        if (palabra == null) {
            return false;
        }

        String p = palabra.toLowerCase();

        return p.equals("module")
            || p.equals("sub")
            || p.equals("dim")
            || p.equals("as")
            || p.equals("if")
            || p.equals("then")
            || p.equals("elseif")
            || p.equals("else")
            || p.equals("function")
            || p.equals("return")
            || p.equals("while")
            || p.equals("end");
    }
}
