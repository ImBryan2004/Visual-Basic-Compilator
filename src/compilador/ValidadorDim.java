package compilador;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
  Validador de declaraciones Dim en Visual Basic
  
  Esta clase se encarga de validar:
   - La correcta declaración de variables
   - El orden donde Dim debe ir después de Module
   - El formato y tipo de datos
   - El registro en la tabla de símbolos
  
  Esta sera una implementación incremental.
 */
public class ValidadorDim {

    private TablaSimbolos tablaSimbolos;
    private ValidadorModule validadorModule;

    // Constructor del validador Dim
    public ValidadorDim(TablaSimbolos tablaSimbolos, ValidadorModule validadorModule) {
        this.tablaSimbolos = tablaSimbolos;
        this.validadorModule = validadorModule;
    }

    // Valida una línea que contenga una declaración Dim
    public List<ErrorLexico> validar(String linea, int numeroLinea) {

        List<ErrorLexico> errores = new ArrayList<>();

        String original = linea;
        String lineaLimpia = linea.trim();

        // Solo analizamos líneas que inician con Dim
        if (!lineaLimpia.toLowerCase().startsWith("dim")) {
            return errores;
        }

        /*
          PASO 1
          Dim debe aparecer después de Module
        */
        if (!validadorModule.seDeclaroModule()) {
            errores.add(new ErrorLexico(
                    500,
                    numeroLinea,
                    "La declaración Dim aparece antes de Module"
            ));
            return errores;
        }

        /*
          PASO 2
          Normalizar espacios alrededor del =
        */
        String normalizada = lineaLimpia.replaceAll("\\s*=\\s*", "=");

        /*
          PASO 3
          Separar parte izquierda y derecha
        */
        String izquierda;
        String derecha = null;

        if (normalizada.contains("=")) {
            String[] partes = normalizada.split("=", 2);
            izquierda = partes[0].trim();
            derecha = partes[1].trim();
        } else {
            izquierda = normalizada.trim();
        }

        /*
          PASO 4
          Procesar tokens izquierdos
        */
        String[] tokens = izquierda.split("\\s+");

        if (tokens.length < 4) {
            errores.add(new ErrorLexico(
                    504,
                    numeroLinea,
                    "Formato incorrecto en declaración Dim. Se esperaba: Dim nombre As TipoDato"
            ));
            return errores;
        }

        String identificador = tokens[1];

        ValidadorIdentificadores validadorIdentificadores =
                new ValidadorIdentificadores();

        // Validar identificador
        if (!validadorIdentificadores.esIdentificadorValido(identificador)) {
            errores.add(new ErrorLexico(
                    502,
                    numeroLinea,
                    "Identificador inválido en la declaración Dim: " + identificador
            ));
            return errores;
        }

        // Palabra reservada como identificador
        if (PalabrasReservadas.esPalabraReservada(identificador)) {
            errores.add(new ErrorLexico(
                    503,
                    numeroLinea,
                    "Palabra reservada usada como identificador: " + identificador
            ));
            return errores;
        }

        // Validar palabra AS
        if (!tokens[2].equalsIgnoreCase("as")) {
            errores.add(new ErrorLexico(
                    505,
                    numeroLinea,
                    "Falta la palabra reservada 'As' en la declaración Dim"
            ));
            return errores;
        }

        String tipoDato = tokens[3];

        // Validar tipo de dato
        if (!esTipoDatoValido(tipoDato)) {
            errores.add(new ErrorLexico(
                    506,
                    numeroLinea,
                    "Tipo de dato no válido en declaración Dim: " + tipoDato
            ));
            return errores;
        }

        /*
          PASO 5
          Validar asignación si existe
        */
        if (derecha != null) {

            if (derecha.isEmpty()) {
                errores.add(new ErrorLexico(
                        508,
                        numeroLinea,
                        "Falta valor u operación después del '='"
                ));
                return errores;
            }

            String valor = derecha;

            // ---------------- NUMÉRICOS ----------------
            if (tipoDato.equalsIgnoreCase("integer")
                    || tipoDato.equalsIgnoreCase("byte")) {

                String[] operandos = valor.split("[+\\-*/]");

                for (String operando : operandos) {

                    operando = operando.trim();

                    // Número literal
                    if (operando.matches("\\d+")) {
                        continue;
                    }

                    // Variable existente
                    if (tablaSimbolos.existeVariable(operando)) {

                        if (!tablaSimbolos.esNumerica(operando)) {
                            errores.add(new ErrorLexico(
                                    509,
                                    numeroLinea,
                                    "La variable '" + operando + "' no es numérica"
                            ));
                            return errores;
                        }

                    } else {
                        errores.add(new ErrorLexico(
                                510,
                                numeroLinea,
                                "Operando no válido en expresión: " + operando
                        ));
                        return errores;
                    }
                }
            }

            // STRING
            if (tipoDato.equalsIgnoreCase("string")) {

                if (!valor.startsWith("\"") || !valor.endsWith("\"")) {
                    errores.add(new ErrorLexico(
                            511,
                            numeroLinea,
                            "El valor String debe estar entre comillas dobles"
                    ));
                    return errores;
                }
            }

            // BOOLEAN
            if (tipoDato.equalsIgnoreCase("boolean")) {

                if (!valor.equalsIgnoreCase("true")
                        && !valor.equalsIgnoreCase("false")) {
                    errores.add(new ErrorLexico(
                            512,
                            numeroLinea,
                            "Valor Boolean no válido: " + valor
                    ));
                    return errores;
                }
            }
        }

        // Registrar variable si todo fue válido
        tablaSimbolos.registrarVariable(identificador, tipoDato);

        return errores;
    }

    // Verifica tipos permitidos
    private boolean esTipoDatoValido(String tipoDato) {
        return tipoDato.equalsIgnoreCase("integer")
                || tipoDato.equalsIgnoreCase("string")
                || tipoDato.equalsIgnoreCase("boolean")
                || tipoDato.equalsIgnoreCase("byte");
    }
}