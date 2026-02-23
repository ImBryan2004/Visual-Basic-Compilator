package compilador;

import java.util.ArrayList;
import java.util.List;

/*
  Validador de asignaciones generales
  
  Esta clase valida líneas del tipo:
      variable = expresión
  
  Reglas:
  - La variable debe existir en la tabla de sÃ­mbolos
  - Debe ser un identificador válido
  - No puede ser palabra reservada
  - Si no está declarada error
  
 */

public class ValidadorAsignaciones {

    private TablaSimbolos tablaSimbolos;

    // Constructor
    public ValidadorAsignaciones(TablaSimbolos tablaSimbolos) {
        this.tablaSimbolos = tablaSimbolos;
    }

    // Valida una posible asignación
    public List<ErrorLexico> validar(String linea, int numeroLinea) {

        List<ErrorLexico> errores = new ArrayList<>();

        String lineaLimpia = linea.trim();

        // Ignorar líneas vacías
        if (lineaLimpia.isEmpty()) return errores;

        // Ignorar comentarios
        if (lineaLimpia.startsWith("'")) return errores;

        String lower = lineaLimpia.toLowerCase();

        // Ignorar palabras reservadas que no analizamos en Proyecto 1
        if (lower.startsWith("dim ")
                || lower.startsWith("if ")
                || lower.startsWith("elseif ")
                || lower.startsWith("else")
                || lower.startsWith("end ")
                || lower.startsWith("module ")
                || lower.startsWith("sub ")
                || lower.startsWith("function ")
                || lower.startsWith("return ")
                || lower.startsWith("console.")) {

            return errores;
        }

        // Debe contener '=' para ser asignación
        int indiceIgual = lineaLimpia.indexOf('=');

        if (indiceIgual == -1) {
            return errores;
        }

        // Obtener lado izquierdo
        String izquierda = lineaLimpia.substring(0, indiceIgual).trim();

        if (izquierda.isEmpty()) return errores;

        // Validar identificador
        ValidadorIdentificadores validadorIdentificadores =
                new ValidadorIdentificadores();

        if (!validadorIdentificadores.esIdentificadorValido(izquierda)) {

            // Si no es identificador válido y no es palabra reservada error
            if (!PalabrasReservadas.esPalabraReservada(izquierda)) {

                errores.add(new ErrorLexico(
                        520,
                        numeroLinea,
                        "Identificador inválido en asignación: " + izquierda
                ));
            }

            return errores;
        }

        // Si es palabra reservada no analizar
        if (PalabrasReservadas.esPalabraReservada(izquierda)) {
            return errores;
        }

        // Verificar que la variable exista
        if (!tablaSimbolos.existeVariable(izquierda)) {

            errores.add(new ErrorLexico(
                    521,
                    numeroLinea,
                    "Variable no declarada: " + izquierda
            ));
        }

        return errores;
    }
}