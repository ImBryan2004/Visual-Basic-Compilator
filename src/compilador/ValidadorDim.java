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
          Extraer partes con REGEX el cual es robusto a espacios
          Formato: Dim nombre As Tipo significa que = valor opcional
         */
        Pattern pattern = Pattern.compile(
                "^Dim\\s+(.+)$",
                Pattern.CASE_INSENSITIVE
        );

        Matcher matcher = pattern.matcher(lineaLimpia);

        if (!matcher.find()) {
            return errores;
        }

        String resto = matcher.group(1).trim();

        
          //Separar identificador y resto
         
        String[] partes = resto.split("\\s+", 2);

        if (partes.length < 2) {
            errores.add(new ErrorLexico(
                    501,
                    numeroLinea,
                    "Falta el identificador en la declaración Dim"
            ));
            return errores;
        }

        String identificador = partes[0];
        String despuesIdentificador = partes[1];

        ValidadorIdentificadores validadorIdentificadores =
                new ValidadorIdentificadores();

        if (!validadorIdentificadores.esIdentificadorValido(identificador)) {
            errores.add(new ErrorLexico(
                    502,
                    numeroLinea,
                    "Identificador inválido en la declaración Dim: " + identificador
            ));
            return errores;
        }

        if (PalabrasReservadas.esPalabraReservada(identificador)) {
            errores.add(new ErrorLexico(
                    503,
                    numeroLinea,
                    "Palabra reservada usada como identificador: " + identificador
            ));
            return errores;
        }

        /*
          PASO 3
          Verificar palabra As y tipo
         */
        Pattern patternAs = Pattern.compile(
                "^As\\s+([A-Za-z]+)(.*)$",
                Pattern.CASE_INSENSITIVE
        );

        Matcher matcherAs = patternAs.matcher(despuesIdentificador.trim());

        if (!matcherAs.find()) {
            errores.add(new ErrorLexico(
                    505,
                    numeroLinea,
                    "Falta la palabra reservada 'As' en la declaración Dim"
            ));
            return errores;
        }

        String tipoDato = matcherAs.group(1);
        String restoAsignacion = matcherAs.group(2).trim();

        /*
          PASO 4
          Validar tipo de dato
         */
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
        if (!restoAsignacion.isEmpty()) {

            if (!restoAsignacion.startsWith("=")) {
                errores.add(new ErrorLexico(
                        507,
                        numeroLinea,
                        "Formato incorrecto en declaración Dim. Se esperaba '='"
                ));
                return errores;
            }

            String valor = restoAsignacion.substring(1).trim();

            if (valor.isEmpty()) {
                errores.add(new ErrorLexico(
                        508,
                        numeroLinea,
                        "Falta valor u operación después del '='"
                ));
                return errores;
            }

            /*
              Validar según tipo de dato
             */

            // ---------------- NUMÉRICOS ----------------
            if (tipoDato.equalsIgnoreCase("integer")
                    || tipoDato.equalsIgnoreCase("byte")) {

                String[] operandos = valor.split("[+\\-*/]");

                for (String operando : operandos) {

                    operando = operando.trim();

                    if (operando.matches("\\d+")) {
                        continue;
                    }

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