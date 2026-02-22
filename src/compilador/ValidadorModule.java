/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compilador;

import java.util.ArrayList;
import java.util.List;

/*
  Validador de la estructura Module en Visual Basic
  
  Este componente forma parte de la etapa de análisis léxico / validación de patrones.
  Su función es verificar:
   - Que Imports aparezca antes de Module
   - Que Module tenga un identificador válido
   - Que el formato sea exactamente: Module Identificador
   - Que exista un solo espacio entre Module e identificador
 */

public class ValidadorModule {

    private boolean importsEncontrado = false;
    private boolean moduleEncontrado = false;

    private ValidadorIdentificadores validadorIdentificadores;

    public ValidadorModule() {
        this.validadorIdentificadores = new ValidadorIdentificadores();
    }

    /*
      Método principal de validación de la estructura Module
     */
    public List<ErrorLexico> validar(String linea, int numeroLinea) {

        List<ErrorLexico> errores = new ArrayList<>();

        String lineaLimpia = linea.trim();

        // Imports
        if (lineaLimpia.equalsIgnoreCase("Imports System")) {
            importsEncontrado = true;
            return errores;
        }

        // Module
        if (lineaLimpia.toLowerCase().startsWith("module")) {

            // Verificar duplicado
            if (moduleEncontrado) {
                errores.add(new ErrorLexico(
                        704,
                        numeroLinea,
                        "Solo puede existir una declaración Module"
                ));
                return errores;
            }

            moduleEncontrado = true;

            // Verificar orden
            if (!importsEncontrado) {
                errores.add(new ErrorLexico(
                        700,
                        numeroLinea,
                        "La instrucción Module aparece antes de Imports"
                ));
            }

            // Separar tokens manualmente
            String[] partes = lineaLimpia.split(" ");

            // Debe haber exactamente 2 partes
            if (partes.length != 2) {
                errores.add(new ErrorLexico(
                        701,
                        numeroLinea,
                        "Formato incorrecto de Module. Debe ser: Module Identificador"
                ));
                return errores;
            }

            // Verificar espacio único
            if (lineaLimpia.contains("  ")) {
                errores.add(new ErrorLexico(
                        702,
                        numeroLinea,
                        "Debe existir exactamente un espacio entre 'Module' y el identificador"
                ));
            }

            String identificador = partes[1];

            // Validar identificador
            if (!validadorIdentificadores.esIdentificadorValido(identificador)) {
                errores.add(new ErrorLexico(
                        703,
                        numeroLinea,
                        "Identificador inválido después de Module: " + identificador
                ));
            }
        }

        return errores;
    }

    
     // Para saber si ya se declaró Module

    public boolean seDeclaroModule() {
        return moduleEncontrado;
    }
}
